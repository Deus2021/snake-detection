package com.udom.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.view.View;

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

        femaleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GetPlantInformation.this, FemaleInfo.class);
                startActivity(intent);
            }
        });

        maleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GetPlantInformation.this, MaleInfo.class);
                startActivity(intent);
            }
        });

        generalBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GetPlantInformation.this, GeneralPlantInfo.class);
                startActivity(intent);
            }
        });

    }
}