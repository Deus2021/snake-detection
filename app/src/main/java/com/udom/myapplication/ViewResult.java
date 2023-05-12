package com.udom.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Outline;
import android.os.Bundle;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ViewResult extends AppCompatActivity {

    ImageView imageView;
    Button maleinfo, femaleinfo;
    TextView result;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_result);

        imageView = findViewById(R.id.imageview);
        result  = findViewById(R.id.viewresult);
        maleinfo = findViewById(R.id.maleBtninfo);
        femaleinfo = findViewById(R.id.femaleBtninfo);

        imageView.setOutlineProvider(new ViewOutlineProvider() {
            @Override
            public void getOutline(View view, Outline outline) {
                int curveRadius = 50;
                outline.setRoundRect(0, 0, view.getWidth(), view.getHeight() + curveRadius, curveRadius);
            }
        });

        if (getIntent().hasExtra("image")) {
            Bitmap bitmap = getIntent().getParcelableExtra("image");
            imageView.setImageBitmap(bitmap);
            //imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        }

        maleinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewResult.this, MaleInfo.class);
                startActivity(intent);
            }
        });

        femaleinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewResult.this, FemaleInfo.class);
                startActivity(intent);
            }
        });
    }
}  