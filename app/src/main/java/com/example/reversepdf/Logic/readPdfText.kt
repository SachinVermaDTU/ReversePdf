package com.example.reversepdf.Logic

import android.graphics.pdf.PdfRenderer
import java.io.File
import java.io.IOException
import java.lang.StringBuilder
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import 	android.os.ParcelFileDescriptor
import androidx.core.content.ContextCompat
import com.google.android.gms.tasks.Task
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions



fun readPdfText(context: Context,filepath: String): String{
    try {
        val file = File(filepath)
        val parcelFileDescriptor: ParcelFileDescriptor = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY)
        val pdfRenderer = PdfRenderer(parcelFileDescriptor)
        val pageCount = pdfRenderer.pageCount
        val sb = StringBuilder()

        for (pageIndex in 0 until pageCount){
            val page = pdfRenderer.openPage(pageIndex)

            // Render the page content onto a bitmap
            val bitmap = Bitmap.createBitmap(page.width, page.height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            canvas.drawColor(Color.WHITE)
            page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)

            // Perform OCR on the bitmap to extract the text
            val text = performOcr(context ,bitmap)

            // Split text into lines
            val lines = text.split("\n")
            //reverse the function
            for(i in lines.indices){
               if(i%2 ==1){ // reverse the alternate the function
                sb.append(lines[i].reversed())
            }
            else
            {
                sb.append(lines[i])
            }
            if(i<lines.size-1){
                sb.append('\n')
            }
        }
      page.close()
    }
       pdfRenderer.close()
        parcelFileDescriptor.close()
       return sb.toString()
   } catch (e: IOException){
       e.printStackTrace()
        return "Error reading PDF"

    }   }
private fun performOcr(context: Context, bitmap: Bitmap): String {
    val image = InputImage.fromBitmap(bitmap, 0)
    val options = TextRecognizerOptions.Builder()
        .build()
    val recognizer = TextRecognition.getClient(options)

    val result: Task<Text> = recognizer.process(image)
    val text = StringBuilder()

    result.addOnSuccessListener { visionText ->
        for (block in visionText.textBlocks) {
            for (line in block.lines) {
                text.append(line.text)
                text.append("\n")
            }
        }
    }.addOnFailureListener { exception ->
        exception.printStackTrace()
    }

    return text.toString()
}