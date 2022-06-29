package com.flutter.moum.screenshot_callback

import android.content.ContentResolver
import android.content.Context
import android.database.ContentObserver
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore

class ScreenshotDetector(private val context: Context,
                         private val callback: (name: String) -> Unit) {

    private var contentObserver: ContentObserver? = null

    fun start() {
        if (contentObserver == null) {
            contentObserver = context.contentResolver.registerObserver()
        }
    }

    fun stop() {
        contentObserver?.let { context.contentResolver.unregisterContentObserver(it) }
        contentObserver = null
    }

    private fun reportScreenshotsUpdate(uri: Uri) {
        val screenshots: List<String> = queryScreenshots(uri)
        if (screenshots.isNotEmpty()) {
            callback.invoke(screenshots.last());
        }
    }

    private fun queryScreenshots(uri: Uri): List<String> {
        return try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                queryRelativeDataColumn(uri)
            } else {
                queryDataColumn(uri)
            }
        }  catch (e:Exception){
            listOf()
        }
    }

    private fun queryDataColumn(uri: Uri): List<String> {
        val screenshots = mutableListOf<String>()

        val projection = arrayOf(
                MediaStore.Images.Media.DATA
        )
        context.contentResolver.query(
                uri,
                projection,
                null,
                null,
                null
        )?.use { cursor ->
            val dataColumn = cursor.getColumnIndex(MediaStore.Images.Media.DATA)

            while (cursor.moveToNext()) {
                val path = cursor.getString(dataColumn)
                if (path.contains("screenshot", true)) {
                    screenshots.add(path)
                }
            }
        }

        return screenshots
    }

    private fun queryRelativeDataColumn(uri: Uri): List<String> {
        val screenshots = mutableListOf<String>()

        val projection = arrayOf(
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.RELATIVE_PATH
        )
        context.contentResolver.query(
                uri,
                projection,
                null,
                null,
                null
        )?.use { cursor ->
            val relativePathColumn =
                    cursor.getColumnIndex(MediaStore.Images.Media.RELATIVE_PATH)
            val displayNameColumn =
                    cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME)
            while (cursor.moveToNext()) {
                val name = cursor.getString(displayNameColumn)
                val relativePath = cursor.getString(relativePathColumn)
                if (name.contains("screenshot", true) or
                        relativePath.contains("screenshot", true)
                ) {
                    screenshots.add(name)
                }
            }
        }

        return screenshots
    }

    private fun ContentResolver.registerObserver(): ContentObserver {
        val contentObserver = object : ContentObserver(Handler(Looper.getMainLooper())) {
            override fun onChange(selfChange: Boolean, uri: Uri?) {
                super.onChange(selfChange, uri)
                uri?.let { reportScreenshotsUpdate(it) }
            }
        }
        registerContentObserver(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, true, contentObserver)
        return contentObserver
    }
}