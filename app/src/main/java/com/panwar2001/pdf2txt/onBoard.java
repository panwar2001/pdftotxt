package com.panwar2001.pdf2txt;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.ads.AdRequest;
import com.panwar2001.pdf2txt.databinding.ActivityOnBoardBinding;

public class onBoard extends AppCompatActivity {
    ActivityOnBoardBinding binding;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOnBoardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.onBoardButton.setOnClickListener(view->{
            Intent intent=new Intent(onBoard.this,MainActivity.class);
            startActivity(intent);
        });
        // admob code
        AdRequest adRequest = new AdRequest.Builder().build();
        binding.adView.loadAd(adRequest);
    }
}