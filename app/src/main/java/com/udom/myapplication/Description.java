package com.udom.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class Description extends AppCompatActivity {

    LinearLayout layoutResearch, layoutgroup, layouthistory;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.descriptiondontdo);

        layoutResearch = findViewById(R.id.layoutResearch);
        layoutgroup =  findViewById(R.id.layoutgroup);
        layouthistory = findViewById(R.id.layouthistory);

        layoutResearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setContentView(R.layout.justsimple);
            }
        });
        layoutgroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setContentView(R.layout.group);
            }
        });
        layouthistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setContentView(R.layout.backgrounddescption);
            }
        });
    }
}