package com.panwar2001.pdf2txt

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.AdRequest
import com.panwar2001.pdf2txt.databinding.ActivityOnBoardBinding

class OnBoard : AppCompatActivity() {
    private var binding: ActivityOnBoardBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnBoardBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        binding!!.onBoardButton.setOnClickListener { _: View? ->
            val intent = Intent(this@OnBoard, MainActivity::class.java)
            startActivity(intent)
        }
        // admob code
        val adRequest = AdRequest.Builder().build()
        binding!!.adView.loadAd(adRequest)
    }
}