package com.panwar2001.pdf2txt;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.material.snackbar.Snackbar;
import com.panwar2001.pdf2txt.databinding.ActivityTextpreviewBinding;

public class TextPreview extends AppCompatActivity {
    ActivityTextpreviewBinding binding;
    String unchangedText;
    @Override
    protected void onCreate(@Nullable  Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding =ActivityTextpreviewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar.toolbar);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            // assign text to EditText
            Intent intent = getIntent();
            unchangedText = intent.getStringExtra("text");
            binding.editText.setText(unchangedText);
        }
        //ads
        AdRequest adRequest = new AdRequest.Builder().build();
        binding.adView.loadAd(adRequest);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater= getMenuInflater();
        inflater.inflate(R.menu.menu,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id=item.getItemId();
        if(id==android.R.id.home){
            finish();
            return true;
        }else if(id==R.id.copy) {
            copyToClipBoard();
            return true;
        }else if(id==R.id.undo_changes){
            undoChanges();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public void copyToClipBoard(){
        String text= String.valueOf(binding.editText.getText());
        ClipboardManager clipboard=(ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip= ClipData.newPlainText("text",text);
        clipboard.setPrimaryClip(clip);
        SnackBarTop("Copied To Clipboard!");
    }
    public void undoChanges(){
        binding.editText.setText(unchangedText);
        SnackBarTop("Reverted Changes!");
    }
    public void  SnackBarTop(String text){
        Snackbar snack=Snackbar.make(binding.getRoot(),text,Snackbar.ANIMATION_MODE_FADE);
        View view=snack.getView();
        snack.setDuration(300);
        FrameLayout.LayoutParams params=(FrameLayout.LayoutParams) view.getLayoutParams();
        params.gravity= Gravity.TOP;
        view.setLayoutParams(params);
        snack.show();
    }
}
