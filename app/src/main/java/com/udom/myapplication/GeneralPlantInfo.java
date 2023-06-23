package com.udom.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class GeneralPlantInfo extends AppCompatActivity {

    LinearLayout layoutone, layouttwo, layoutthree;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general_plant_info);

        layoutone = findViewById(R.id.layoutone);
        layouttwo =  findViewById(R.id.layouttwo);
        layoutthree = findViewById(R.id.layoutthree);

        layoutone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setContentView(R.layout.generalunderstand);
            }
        });
        layouttwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setContentView(R.layout.stagesofpapaygrowth);
            }
        });
        layoutthree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setContentView(R.layout.generalunderstand);
            }
        });



    }
}