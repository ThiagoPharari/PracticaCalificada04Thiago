package com.pharari.thiago.lab13phararithiago

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import java.io.File

class GalleryActivity : AppCompatActivity() {
    private lateinit var viewPager: ViewPager2
    private lateinit var galleryAdapter: GalleryAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery)

        viewPager = findViewById(R.id.view_pager)

        val imageFiles = getImagesFromDirectory()

        galleryAdapter = GalleryAdapter(this, imageFiles)
        viewPager.adapter = galleryAdapter
    }

    private fun getImagesFromDirectory(): List<File> {
        val imagesDir = File(getExternalFilesDir(null), "images")
                if (!imagesDir.exists()) {
                    imagesDir.mkdirs()
                }
        return imagesDir.listFiles()?.filter { it.isFile && it.extension in
                listOf("jpg", "png") } ?: emptyList()
    }
}
