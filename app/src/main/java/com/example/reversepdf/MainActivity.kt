package com.example.reversepdf

import android.graphics.pdf.PdfRenderer
import android.icu.text.AlphabeticIndex.Bucket.LabelType
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.AndroidPath
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.example.reversepdf.Logic.readPdfText
import com.example.reversepdf.ui.theme.ReversePdfTheme
import org.w3c.dom.Text

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ReversePdfTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                   PdfReaderApp()
                }
            }
        }
    }
}

@Composable
fun PdfReaderApp() {
    val context = LocalContext.current
    var filePath by remember { mutableStateOf("") }
    var pdfText by remember { mutableStateOf("") }

    Column {
        FileInput("Enter the PDF file path:", filePath) { input ->
            filePath = input
            pdfText = readPdfText(context,filePath)
        }
        TextDisplay(pdfText)
    }
}
@Composable
fun FileInput(label: String , filePath: String, onFilePathChange: (String) ->(Unit)){

}

@Composable
fun TextDisplay(text: String){
    // implement your file input ui here
    Text(text = text)
}

