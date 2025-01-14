package com.eaapps.weather_glovy.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Paint
import java.io.File

fun Bitmap.drawTextOnBitmap(text: String, targetHeight: Int = 800, targetWidth: Int = 800): Bitmap {
    val resize = Bitmap.createScaledBitmap(this, targetWidth, targetHeight, false)
    val tempBitmap = resize.copy(Bitmap.Config.ARGB_8888, true)
    val canvas = android.graphics.Canvas(tempBitmap)
    val paint = Paint().apply {
        color = android.graphics.Color.WHITE
        strokeWidth = 2f
        isAntiAlias = true
        textSize = 36f
        textAlign = Paint.Align.CENTER
    }

    val lines = text.split("\n")
    val lineHeight = paint.textSize + 10f
    val totalHeight = lineHeight + lines.size
    var y = (targetHeight - totalHeight) / 2f + lineHeight
    for (lin in lines) {
        canvas.drawText(lin, (targetWidth / 2).toFloat(), y, paint)
        y += lineHeight
    }
    return tempBitmap
}

fun Bitmap.saveBitmapToFile(context: Context, fileName: String): String {
    val dir = File(context.getExternalFilesDir(null), "weatherPhoto")
    if (dir.exists().not()) dir.mkdirs()
    val file = File(dir, "${fileName}_weather.png")
    file.outputStream().use {
        compress(Bitmap.CompressFormat.PNG, 100, it)
    }
    return file.absolutePath
}
