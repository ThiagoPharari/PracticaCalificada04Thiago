package com.pharari.thiago.lab13phararithiago

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale
class MainActivity : AppCompatActivity() {
    private lateinit var previewView: PreviewView

    private lateinit var imgCaptureBtn: Button

    private lateinit var switchBtn: Button
    private lateinit var galleryBtn: Button
    private var imageCapture: ImageCapture? = null

    private var isUsingFrontCamera = false
    private var cameraProvider: ProcessCameraProvider? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        previewView = findViewById(R.id.preview)
        imgCaptureBtn = findViewById(R.id.img_capture_btn)
        switchBtn = findViewById(R.id.switch_btn)
        galleryBtn = findViewById(R.id.gallery_btn)

        startCamera()

        imgCaptureBtn.setOnClickListener {
            takePhoto()
        }
        switchBtn.setOnClickListener {
            switchCamera()
        }
        galleryBtn.setOnClickListener {
            openGallery()
        }
    }
    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({
            cameraProvider = cameraProviderFuture.get()
            bindCameraUseCases()
        }, ContextCompat.getMainExecutor(this))
    }
    private fun bindCameraUseCases() {
        val cameraProvider = cameraProvider ?: return
        val preview = androidx.camera.core.Preview.Builder().build().also {
            it.setSurfaceProvider(previewView.surfaceProvider)
        }

        imageCapture = ImageCapture.Builder().build()

        val cameraSelector = if (isUsingFrontCamera) {
            CameraSelector.DEFAULT_FRONT_CAMERA
        } else {
            CameraSelector.DEFAULT_BACK_CAMERA
        }
        try {
            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(
                this,
                cameraSelector,
                preview,
                imageCapture
            )
        } catch (e: Exception) {
            Toast.makeText(this, "Error starting camera: ${e.message}",
                Toast.LENGTH_SHORT).show()
        }
    }
    private fun takePhoto() {
        val imageCapture = imageCapture ?: return
        val outputDir = File(getExternalFilesDir(null), "images")
        if (!outputDir.exists()) {
            outputDir.mkdirs()
        }
        val fileName = SimpleDateFormat("yyyyMMdd_HHmmss",
            Locale.US).format(System.currentTimeMillis()) + ".jpg"
        val outputFile = File(outputDir, fileName)
        val outputOptions =
            ImageCapture.OutputFileOptions.Builder(outputFile).build()
        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults:
                                          ImageCapture.OutputFileResults) {
                    Toast.makeText(this@MainActivity, "Photo saved:${outputFile.absolutePath}", Toast.LENGTH_SHORT).show()
                }
                override fun onError(exception: ImageCaptureException) {

                    Toast.makeText(this@MainActivity, "Error capturing photo: ${exception.message}", Toast.LENGTH_SHORT).show()
                }
            }
        )
    }
    private fun switchCamera() {
        isUsingFrontCamera = !isUsingFrontCamera
        bindCameraUseCases()
    }
    private fun openGallery() {
        val intent = Intent(this, GalleryActivity::class.java)
        startActivity(intent)
    }
}
