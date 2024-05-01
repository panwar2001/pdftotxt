package com.panwar2001.pdf2txt

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.AdRequest
import com.panwar2001.pdf2txt.databinding.ActivityPreviewPdfBinding

class PreviewPDF : AppCompatActivity() {
    private var binding: ActivityPreviewPdfBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPreviewPdfBinding.inflate(
            layoutInflater
        )
        setContentView(binding!!.getRoot())
        setSupportActionBar(binding!!.toolbar.toolbar)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            val intent = intent
            binding!!.pdfView.fromUri(intent.data).spacing(5).load()
        }
        //adview
        val adRequest = AdRequest.Builder().build()
        binding!!.adView.loadAd(adRequest)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}