from threading import Lock

import firebase_admin
from firebase_admin import messaging
from firebase_admin import credentials
from firebase_admin import firestore
from google.api_core.datetime_helpers import DatetimeWithNanoseconds
import datetime

config = {
    'apiKey': "AIzaSyBd_NC2WenOrpS-J9xWXoQf9-hBcP843Ko",
    'authDomain': "parkitnow-a8dc8.firebaseapp.com",
    'databaseURL': "https://parkitnow-a8dc8.firebaseio.com",
    'projectId': "parkitnow-a8dc8",
    'storageBucket': "parkitnow-a8dc8.appspot.com",
    'messagingSenderId': "296134061159",
    'appId': "1:296134061159:web:b3873f1989c5e4633038c2"
}

cred = credentials.Certificate("key.json")
default_app = firebase_admin.initialize_app(cred)

db = firestore.client()
first_time = True
cars = {}
lock = Lock()


def send_notifications(title, body, tokens):
    message = messaging.MulticastMessage(
        notification=messaging.Notification(
            title=title,
            body=body
        ),
        tokens=tokens
    )
    response = messaging.send_multicast(message)
    print('{0} messages were sent successfully'.format(response.success_count))


def get_user_docs(car_ids):
    docs = []
    for car_id in car_ids:
        doc = db.collection(u"users").where(u'selectedCar', u'==', car_id).get()
        if len(doc) > 0:
            docs.append(doc[0])
    return docs


def get_tokens(docs):
    tokens = []
    for doc in docs:
        tokens.append(doc._data['token'])

    return tokens


def send_notification(title, body, token):
    message = messaging.Message(
        notification=messaging.Notification(
            title=title,
            body=body,
        ),
        token=token
    )
    response = messaging.send(message)
    print("Successfully sent message:", response)


def get_blocked_cars(car_id_changed):
    blocked_cars = []

    current_car_ids = [car_id_changed]
    while current_car_ids:
        current_blocking = []
        for current_car_id in current_car_ids:
            blocking = cars[current_car_id]['blocking']
            for blocker_car_id in blocking:
                if blocker_car_id not in current_blocking:
                    current_blocking.append(blocker_car_id)
        current_car_ids = current_blocking
        blocked_cars.extend(current_blocking)

    return list(set(blocked_cars))


def unobstructed(current_car_id):
    global cars
    for car, data in cars.items():
        if data['blocking'] == current_car_id:
            return True
    return False


def last_element_is_root(path):
    return not cars[path[len(path) - 1]]['blocking']


def last_element_is_user_car(path, car_id):
    return path[len(path) - 1] == car_id


def found_all_paths(paths, car_id):
    for path in paths:
        if not last_element_is_user_car(path, car_id) and not last_element_is_root(path):
            return False
    return True


def grow_path(path, car_id):
    new_paths = []

    if last_element_is_root(path) or last_element_is_user_car(path, car_id):
        new_paths.append(path)
        return new_paths

    blocked_cars = cars[path[len(path) - 1]]['blocking']

    for j in range(len(blocked_cars)):
        new_path = []
        new_path.extend(path)
        new_path.append(blocked_cars[j])
        new_paths.append(new_path)

    return new_paths


def get_blockers_of(car_id_changed):
    global cars
    paths = []

    blocked_cars = []
    for _, data in cars.items():
        blocked_cars.append(data['blocking'])
    flat_blocked_cars = [item for sublist in blocked_cars for item in sublist]
    blocked_cars = list(set(flat_blocked_cars))

    unobstructed_cars = []
    for car_id in cars:
        if car_id not in blocked_cars:
            unobstructed_cars.append(car_id)

    for car_id in unobstructed_cars:
        paths.append([])
        paths[len(paths) - 1].append(car_id)

    while not found_all_paths(paths, car_id_changed):
        new_paths = []
        for i in range(len(paths)):
            new_paths.extend(grow_path(paths[i], car_id_changed))
        paths = new_paths

    new_paths = []
    for path in paths:
        if car_id_changed in path:
            new_paths.append(path)
    paths = new_paths

    blocked_by_user = get_blocked_cars(car_id_changed)

    blockers = []
    for car in list(set([item for sublist in paths for item in sublist])):
        if car not in blocked_by_user and not car == car_id_changed:
            blockers.append(car)

    return blockers


def notify_blockers(blockers, car_id_changed):
    tokens = []
    for blocker in blockers:
        print("BLOCKER:", blocker)
        doc = db.collection(u"users").where(u'selectedCar', u'==', blocker).get()
        if len(doc) > 0:
            # send_notification("ParkItNow",
            #                   car_id_changed + " is leaving NOW!",
            #                   doc[0]._data['token'])
            tokens.append(doc[0]._data['token'])

    send_notifications("ParkItNow",
                       car_id_changed + " is leaving NOW!",
                       tokens)


def on_snapshot(doc_snapshot, changes, read_time):
    global first_time, cars

    # operations over cars, so better make it atomic
    lock.acquire()
    if first_time:
        first_time = False
        for doc in doc_snapshot:
            cars.update({doc.id: doc._data})
    else:
        for change in changes:
            print(f'Changed document snapshot: {change.document.id}')

            if change.type.name == 'ADDED':
                # there's either a new car, or a user changed his car (both unparked)
                cars.update({change.document.id: change.document._data})

            elif change.type.name == 'REMOVED':
                cars.pop(change.document.id, None)

            elif change.type.name == 'MODIFIED':
                # either somebody parked, or somebody changed the time when he leaves, ~or somebody left~(cut this one
                # out cause we just leave the old data in there)
                #
                # other potential cases: leaveable/blocked_user_left_announcer was switched.
                #
                # I don't know if the following is relevant anymore:
                #  I'll need some flags (synchronous) in order to tackle the case when time is changed and it's gonna be
                #  treated in the while loop too
                #
                if change.document.id not in cars:
                    # somebody just added a new car
                    cars.update({change.document.id, change.document._data})
                else:
                    # TODO: sb entered a new queue:
                    leave_time: DatetimeWithNanoseconds = change.document._data['departureTime']
                    old_leave_time: DatetimeWithNanoseconds = cars[change.document.id]['departureTime']
                    if (cars[change.document.id]['isParked'] == False and
                            change.document._data['isParked'] == True):
                        cars.update({change.document.id: change.document._data})
                    # sb changed the time when he leaves:
                    elif abs(old_leave_time - leave_time) > datetime.timedelta(seconds=1):
                        now = DatetimeWithNanoseconds.now(tz=datetime.timezone.utc)
                        # 1) sb leaves now
                        if abs(leave_time - now) < datetime.timedelta(minutes=2):
                            print("got here")
                            blockers = get_blockers_of(change.document.id)
                            notify_blockers(blockers, change.document.id)

                            user_doc = db.collection(u"users").where(u'selectedCar', u'==', change.document.id).get()[0]
                            docs = get_user_docs((blockers))
                            # update to leaving and to leave_announcer
                            user_doc.reference.update({u'leaver': True})
                            for doc in docs:
                                doc.reference.update({u'leaveAnnouncer': True})
                        # 2) sb leaves later
                        elif old_leave_time < leave_time:
                            cars.update({change.document.id: change.document._data})
                        # 3) sb leaves sooner
                        elif old_leave_time > leave_time:
                            cars.update({change.document.id: change.document._data})
                        # 4) shouldn't get here
                        else:
                            pass
    lock.release()


if __name__ == '__main__':
    cars_ref = db.collection(u'cars')

    cars_ref.on_snapshot(on_snapshot)

    notified_flag = {}

    while True:
        # continuously check how much time is left until car leaves
        now = DatetimeWithNanoseconds.now(tz=datetime.timezone.utc)

        # in case cars is changed
        lock.acquire()
        cars_copy = cars.copy()
        lock.release()

        for car_id, data in cars_copy.items():
            # TODO This is not perfect. They way I should do this is without the 9 50 part. But then I'd have to create a
            #   new variable or something into firebase. So that when the user hits "leave now" he wouldn't also get this
            #   notification
            if (datetime.timedelta(minutes=9, seconds=50) < data['departureTime'] - now < datetime.timedelta(minutes=10) and
                    not notified_flag.get((car_id, "10"), False)):
                print("10 minutes until " + car_id + " should leave")
                notified_flag.update({(car_id, "10"): True})

                user_doc = db.collection(u"users").where(u'selectedCar', u'==', car_id).get()[0]

                send_notification("ParkItNow Reminder",
                                  "You are leaving in 10 minutes. You now have 5 minutes to change the time.",
                                  user_doc._data['token'])

            if (datetime.timedelta(minutes=8, seconds=50) < data['departureTime'] - now < datetime.timedelta(minutes=9) and
                    not notified_flag.get((car_id, "5"), False) and
                    notified_flag.get((car_id, "10"), False)):  # last condition is unnecessary
                print("5 minutes until " + car_id + " should leave")
                notified_flag.update({(car_id, "5"): True})

                blockers = get_blockers_of(car_id)
                user_doc = db.collection(u"users").where(u'selectedCar', u'==', car_id).get()[0]
                user_docs = get_user_docs(blockers)
                # update to leaving and to leave_announcer
                user_doc.reference.update({u'leaver': True})
                for doc in user_docs:
                    doc.reference.update({u'leaveAnnouncer': True})

                tokens = get_tokens(user_docs)
                send_notifications("ParkItNow Alert",
                                   car_id + " is leaving in 5 minutes!",
                                   tokens=tokens)
