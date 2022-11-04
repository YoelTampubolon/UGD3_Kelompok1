package com.ugd3.ugd3_kelompok1

import android.annotation.SuppressLint
import android.hardware.Camera
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity

class CameraActivity: AppCompatActivity() {
    private var mCamera: Camera? = null
    private var mCameraView: CameraView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_cameraprofile)

        try{
            mCamera = Camera.open()
        }catch (e: Exception){
            Log.d("Erorr", "Failed to get camera" + e.message)
        }

        if(mCamera != null){
            mCameraView = CameraView(this, mCamera!!)
            val camera_view = findViewById<View>(R.id.FLCamera) as FrameLayout
            camera_view.addView(mCameraView)
        }

        @SuppressLint("MissingInflatedId", "LocalSupress")
        val imageClose = findViewById<View>(R.id.imgClose) as ImageButton
        imageClose.setOnClickListener{
                view: View? -> System.exit(0)
        }
    }
}