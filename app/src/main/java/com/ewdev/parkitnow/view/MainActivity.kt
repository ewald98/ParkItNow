package com.ewdev.parkitnow.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.ewdev.parkitnow.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
//        actionBar?.hide()

        setContentView(R.layout.activity_base)

    }

}