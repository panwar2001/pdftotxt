
package com.panwar2001.pdf2txt;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.activity.result.ActivityResultLauncher;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.net.Uri;

import com.google.android.gms.ads.AdRequest;
import com.panwar2001.pdf2txt.databinding.ActivityMainBinding;
import com.tom_roush.pdfbox.android.PDFBoxResourceLoader;
import com.tom_roush.pdfbox.pdmodel.PDDocument;
import com.tom_roush.pdfbox.text.PDFTextStripper;

import java.io.InputStream;
import java.util.function.Function;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

public class MainActivity extends AppCompatActivity {
    ActivityResultLauncher<String> filePicker;
    ActivityMainBinding binding;
    Uri pdfURI=null;
    String text="",FileName="";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar.toolbar);
        //initialize pdf box library's resource loader
        PDFBoxResourceLoader.init(getApplicationContext());
        //Admob code below
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        AdRequest adRequest = new AdRequest.Builder().build();
        binding.adView.loadAd(adRequest);
        //Admob code above

        filePicker=registerForActivityResult(new ActivityResultContracts.GetContent(),result->{
                        if(result==null){
                            // when user does not choose any file then the resultant uri is null and null pointer exception will occur. uri is checked if user
                            // didn't choose any pdf file then then further options are executed.
                            return;
                        }
                        pdfURI=result;
                        Cursor returnCursor=getContentResolver().query(result,null,null,null,null);
                        if(returnCursor!=null) {
                            int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                            returnCursor.moveToFirst();
                            FileName = returnCursor.getString(nameIndex);
                            binding.selected.setText(FileName);
                            FileName = FileName.split("\\.")[0];
                            returnCursor.close();
                            buttonConfiguration(true,true,true,false,true);
                        }
                }
        );

        binding.selectPdf.setOnClickListener(view -> filePicker.launch("application/pdf"));

        // Preview the pdf
        binding.preview.setOnClickListener(view->{
                  Intent intent=new Intent(MainActivity.this,PreviewPDF.class);
                  intent.setData(pdfURI);
                  startActivity(intent);
        });

       //set to initial state
        binding.clearText.setOnClickListener(view -> {
            pdfURI=null;
            buttonConfiguration(false,false,false,false,true);
            binding.textFile.setText(R.string.text_file);
            binding.selected.setText(R.string.no_file_selected);
        });
        // convert pdf to text with pdfBox
        binding.convert.setOnClickListener(view -> {
            try {
                    InputStream inputStream = getContentResolver().openInputStream(pdfURI);
                    PDDocument document=PDDocument.load(inputStream);
                    PDFTextStripper textStripper=new PDFTextStripper();
                    text=textStripper.getText(document);
                    document.close();
                    if(inputStream!=null) {
                        inputStream.close();
                    }
                    buttonConfiguration(false,true,false,true,false);
            }
                catch(Exception e){
                    e.printStackTrace();
                }
            binding.textFile.setText(FileName);
        });
        binding.result.setOnClickListener(view -> {
            Intent intent=new Intent(MainActivity.this,TextPreview.class);
            intent.putExtra("text",text);
            startActivity(intent);
        });
    }

    public void buttonConfiguration(Boolean previewEnabled, Boolean clearTextEnabled, Boolean convertEnabled, Boolean resultEnabled, Boolean selectPdfEnabled){
        Function<Boolean,Float> dim=enabled->enabled?(float)1:(float)0.5;
        if(binding.preview.isEnabled()!=previewEnabled) {
            binding.preview.setEnabled(previewEnabled);
            binding.preview.setAlpha(dim.apply(previewEnabled));
        }
        if(binding.clearText.isEnabled()!=clearTextEnabled){
            binding.clearText.setEnabled(clearTextEnabled);
            binding.clearText.setAlpha(dim.apply(clearTextEnabled));
        }
        if(binding.convert.isEnabled()!=convertEnabled) {
            binding.convert.setEnabled(convertEnabled);
            binding.convert.setAlpha(dim.apply(convertEnabled));
        }
        if(binding.result.isEnabled()!=resultEnabled) {
            binding.result.setEnabled(resultEnabled);
            binding.result.setAlpha(dim.apply(resultEnabled));
        }
        if(binding.selectPdf.isEnabled()!=selectPdfEnabled) {
            binding.selectPdf.setEnabled(selectPdfEnabled);
            binding.selectPdf.setAlpha(dim.apply(selectPdfEnabled));
        }
    }
}
