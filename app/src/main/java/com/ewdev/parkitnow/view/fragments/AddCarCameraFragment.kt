package com.ewdev.parkitnow.view.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker.PERMISSION_GRANTED
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.ewdev.parkitnow.R
import com.ewdev.parkitnow.viewModel.AddCarCameraViewModel
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import kotlinx.android.synthetic.main.fragment_add_car_camera.*
import java.io.File
import java.util.*

class AddCarCameraFragment : Fragment() {

    lateinit var viewModel: AddCarCameraViewModel

    var camera: Camera? = null
    var preview: Preview? = null
    var imageCapture: ImageCapture? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(AddCarCameraViewModel::class.java)

    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private fun startCamera() {

        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
        cameraProviderFuture.addListener(Runnable {
            val cameraProvider = cameraProviderFuture.get()

            preview = Preview.Builder().build()
            preview?.setSurfaceProvider(camera_view.createSurfaceProvider())

            imageCapture = ImageCapture.Builder()
                .setTargetRotation(requireView().display.rotation)
                .build()
            val cameraSelector = CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_BACK).build()
            cameraProvider.unbindAll()
            camera = cameraProvider.bindToLifecycle(viewLifecycleOwner, cameraSelector, preview, imageCapture)
        }, ContextCompat.getMainExecutor(requireContext()))
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PERMISSION_GRANTED) {
            startCamera()
        } else {
            Toast.makeText(requireContext(), "Accept permision", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_car_camera, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PERMISSION_GRANTED) {
            startCamera()
        } else {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.CAMERA), 0)
        }

        viewModel.validCar.observe(viewLifecycleOwner, { valid ->
            if (valid) {
                viewModel.car.observe(viewLifecycleOwner, { car ->
                    findNavController().previousBackStackEntry?.savedStateHandle?.set("car", car)
                    findNavController().popBackStack()
                })
            } else {
                Toast.makeText(
                    requireActivity(),
                    "Invalid car",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })

        fab_take_picture.setOnClickListener {
            takePhoto()
        }

    }

    private fun takePhoto() {
        imageCapture?.takePicture(
            ContextCompat.getMainExecutor(requireContext()),
            object: ImageCapture.OnImageCapturedCallback() {
                @SuppressLint("UnsafeExperimentalUsageError")
                override fun onCaptureSuccess(imageProxy: ImageProxy) {
                    val mediaImage = imageProxy.image
                    if (mediaImage != null) {
                        val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)

                        val recognizer = TextRecognition.getClient()
                        val result = recognizer.process(image)
                            .addOnSuccessListener { visionText ->

                                val resultText = visionText.text.replace(" ", "").replace("RO", "").replace("\n", "")
                                Log.i("nice", "work")

                                viewModel.requestCarValidity(resultText)
                            }
                            .addOnFailureListener {

                            }
                    }
                }

                override fun onError(exception: ImageCaptureException) {
                    super.onError(exception)
                }
            })
    }

}