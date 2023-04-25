package com.udom.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.view.View;
import android.widget.Button;

public class GetPlantInformation extends AppCompatActivity {
    Button femaleBtn, maleBtn, generalBtn;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_plant_information);

        femaleBtn = findViewById(R.id.femaleBtn);
        maleBtn = findViewById(R.id.maleBtn);
        generalBtn = findViewById(R.id.generalBtn);

    }
}