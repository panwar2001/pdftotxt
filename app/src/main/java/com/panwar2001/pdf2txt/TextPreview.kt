package com.panwar2001.pdf2txt

import android.content.ClipData
import android.content.ClipboardManager
import android.os.Bundle
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.widget.FrameLayout
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.AdRequest
import com.google.android.material.snackbar.Snackbar
import com.panwar2001.pdf2txt.databinding.ActivityTextpreviewBinding

class TextPreview : AppCompatActivity() {
    private var binding: ActivityTextpreviewBinding? = null
    private var unchangedText: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTextpreviewBinding.inflate(
            layoutInflater
        )
        setContentView(binding!!.root)
        setSupportActionBar(binding!!.toolbar.toolbar)
        val actionBar:ActionBar?= supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            // assign text to EditText
            val intent = intent
            unchangedText = intent.getStringExtra("text")
            binding!!.editText.setText(unchangedText)
        }
        //ads
        val adRequest = AdRequest.Builder().build()
        binding!!.adView.loadAd(adRequest)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
            R.id.copy -> {
                copyToClipBoard()
                return true
            }
            R.id.undo_changes -> {
                undoChanges()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

   private fun copyToClipBoard() {
        val text = binding!!.editText.getText().toString()
        val clipboard = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("text", text)
        clipboard.setPrimaryClip(clip)
        snackBarTop("Copied To Clipboard!")
    }

     private fun undoChanges() {
        binding!!.editText.setText(unchangedText)
        snackBarTop("Reverted Changes!")
    }

    private fun snackBarTop(text: String?) {
        val snack = Snackbar.make(binding!!.root, text!!, Snackbar.ANIMATION_MODE_FADE)
        val view = snack.view
        snack.setDuration(300)
        val params = view.layoutParams as FrameLayout.LayoutParams
        params.gravity = Gravity.TOP
        view.setLayoutParams(params)
        snack.show()
    }
}
