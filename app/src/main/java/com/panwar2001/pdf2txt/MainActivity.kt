package com.panwar2001.pdf2txt

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.panwar2001.pdf2txt.databinding.ActivityMainBinding
import com.tom_roush.pdfbox.android.PDFBoxResourceLoader
import com.tom_roush.pdfbox.pdmodel.PDDocument
import com.tom_roush.pdfbox.text.PDFTextStripper
import java.util.function.Function

class MainActivity : AppCompatActivity() {
    private var filePicker: ActivityResultLauncher<String>? = null
    private var binding: ActivityMainBinding? = null
    private var pdfURI: Uri? = null
    private var text = ""
    private var fileName = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        setSupportActionBar(binding!!.toolbar.toolbar)
        //initialize pdf box library's resource loader
        PDFBoxResourceLoader.init(applicationContext)
        //Admob code below
        MobileAds.initialize(this) { }
        val adRequest = AdRequest.Builder().build()
        binding!!.adView.loadAd(adRequest)
        //Admob code above
        filePicker = registerForActivityResult<String, Uri>(
            ActivityResultContracts.GetContent()
        ) { result: Uri? ->
            if (result == null) {
                // when user does not choose any file then the resultant uri is null and null pointer exception will occur. uri is checked if user
                // didn't choose any pdf file then then further options are executed.
                return@registerForActivityResult
            }
            pdfURI = result
            val returnCursor = contentResolver.query(result, null, null, null, null)
            if (returnCursor != null) {
                val nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                returnCursor.moveToFirst()
                fileName = returnCursor.getString(nameIndex)
                binding!!.selected.text = fileName
                fileName = fileName.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0]
                returnCursor.close()
                buttonConfiguration(
                    previewEnabled = true,
                    clearTextEnabled = true,
                    convertEnabled = true,
                    resultEnabled = false,
                    selectPdfEnabled = true
                )
            }
        }
        binding!!.selectPdf.setOnClickListener { _: View? -> filePicker!!.launch("application/pdf") }

        // Preview the pdf
        binding!!.preview.setOnClickListener { _: View? ->
            val intent = Intent(this@MainActivity, PreviewPDF::class.java)
            intent.setData(pdfURI)
            startActivity(intent)
        }

        //set to initial state
        binding!!.clearText.setOnClickListener { _: View? ->
            pdfURI = null
            buttonConfiguration(
                previewEnabled = false,
                clearTextEnabled = false,
                convertEnabled = false,
                resultEnabled = false,
                selectPdfEnabled = true
            )
            binding!!.textFile.setText(R.string.text_file)
            binding!!.selected.setText(R.string.no_file_selected)
        }
        // convert pdf to text with pdfBox
        binding!!.convert.setOnClickListener { _: View? ->
            try {
                val inputStream = contentResolver.openInputStream(pdfURI!!)
                val document = PDDocument.load(inputStream)
                val textStripper = PDFTextStripper()
                text = textStripper.getText(document)
                document.close()
                inputStream?.close()
                buttonConfiguration(
                    previewEnabled = false,
                    clearTextEnabled = true,
                    convertEnabled = false,
                    resultEnabled = true,
                    selectPdfEnabled = false
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
            binding!!.textFile.text = fileName
        }
        binding!!.result.setOnClickListener { _: View? ->
            val intent = Intent(this@MainActivity, TextPreview::class.java)
            intent.putExtra("text", text)
            startActivity(intent)
        }
    }

   private fun buttonConfiguration(
        previewEnabled: Boolean,
        clearTextEnabled: Boolean,
        convertEnabled: Boolean,
        resultEnabled: Boolean,
        selectPdfEnabled: Boolean
    ) {
        val dim = Function { enabled: Boolean -> if (enabled) 1f else 0.5.toFloat() }
        if (binding!!.preview.isEnabled != previewEnabled) {
            binding!!.preview.setEnabled(previewEnabled)
            binding!!.preview.setAlpha(dim.apply(previewEnabled))
        }
        if (binding!!.clearText.isEnabled != clearTextEnabled) {
            binding!!.clearText.setEnabled(clearTextEnabled)
            binding!!.clearText.setAlpha(dim.apply(clearTextEnabled))
        }
        if (binding!!.convert.isEnabled != convertEnabled) {
            binding!!.convert.setEnabled(convertEnabled)
            binding!!.convert.setAlpha(dim.apply(convertEnabled))
        }
        if (binding!!.result.isEnabled != resultEnabled) {
            binding!!.result.setEnabled(resultEnabled)
            binding!!.result.setAlpha(dim.apply(resultEnabled))
        }
        if (binding!!.selectPdf.isEnabled != selectPdfEnabled) {
            binding!!.selectPdf.setEnabled(selectPdfEnabled)
            binding!!.selectPdf.setAlpha(dim.apply(selectPdfEnabled))
        }
    }
}
