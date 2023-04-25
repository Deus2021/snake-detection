package com.udom.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button buttonStart = findViewById(R.id.button1);
        Button buttonGetInfo = findViewById(R.id.button2);

        buttonStart.setBackground(ContextCompat.getDrawable(this, R.drawable.button_selector));
        buttonGetInfo.setBackground(ContextCompat.getDrawable(this, R.drawable.button_selector));

        buttonStart.setOnClickListener(new View.OnClickListener(){
         @Override
         public void  onClick(View view){

             Intent intent = new Intent(MainActivity.this, SetImage.class);
             startActivity(intent);
         }
        });

        buttonGetInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, GetPlantInformation.class);
                startActivity(intent);
            }
        });


    }
}