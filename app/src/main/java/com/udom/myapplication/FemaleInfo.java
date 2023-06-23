package com.udom.myapplication;

import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;


public class FemaleInfo extends AppCompatActivity {

    private TextView contentTextView;
    private ProgressBar progressBar;

    private static final String URL = "https://www.instagram.com/samihan_sawant/?h1=en";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_female_info);
    }

}
