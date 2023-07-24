package com.udom.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class firstaidjava extends AppCompatActivity {

    LinearLayout layoutvenom, layoutnonvenom, layouttdesc,layoutpreventantion;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_aid_based_on_type);

        layoutvenom = findViewById(R.id.layoutvenom);
        layoutnonvenom =  findViewById(R.id.layoutnonvenom);
        layouttdesc = findViewById(R.id.layouttdesc);
        layoutpreventantion = findViewById(R.id.layoutpreventantion);

        layoutvenom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setContentView(R.layout.venomousfirsaid);
            }
        });
        layoutnonvenom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setContentView(R.layout.nonvenomousfirstaid);
            }
        });
        layouttdesc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setContentView(R.layout.descriptiondontdo);
            }
        });
        layoutpreventantion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setContentView(R.layout.preventationofsnakebite);
            }
        });



    }
}