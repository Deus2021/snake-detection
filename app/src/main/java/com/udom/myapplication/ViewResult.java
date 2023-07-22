package com.udom.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Outline;
import android.os.Bundle;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ViewResult extends AppCompatActivity {

    ImageView imageView;
    Button moredetails, back;

    LinearLayout  resultlayout, layoutpositive;
    TextView resultview, obtained;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_result);

        imageView = findViewById(R.id.imageview);
        resultview  = findViewById(R.id.viewresult);
        resultlayout = findViewById(R.id.layout1);
        layoutpositive = findViewById(R.id.positive);
        moredetails = findViewById(R.id.moredetails);
        back = findViewById(R.id.back);

        imageView.setOutlineProvider(new ViewOutlineProvider() {
            @Override
            public void getOutline(View view, Outline outline) {
                int curveRadius = 50;
                outline.setRoundRect(0, 0, view.getWidth(), view.getHeight() + curveRadius, curveRadius);
            }
        });

        Intent intent = getIntent();
        byte[] imageData = intent.getByteArrayExtra(SetImage.EXTRA_IMAGE_DATA);
        if (imageData != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
            imageView.setImageBitmap(bitmap);
        }
        String result = intent.getStringExtra("result");
        Toast.makeText(this, result, Toast.LENGTH_SHORT).show();

        resultview.setText(result);

        resultlayout = findViewById(R.id.layout1);

        if (result.equals("Non Venomous")) {
            resultview.setTextColor(getResources().getColor(R.color.green));
            layoutpositive.setVisibility(View.VISIBLE);
        } else if (result.equals("Venomous")) {
            resultview.setTextColor(getResources().getColor(R.color.green));
            layoutpositive.setVisibility(View.VISIBLE);
        } else if (result.equals("Not Snake Bite")) {
            resultview.setTextColor(getResources().getColor(R.color.red));
            layoutpositive.setVisibility(View.VISIBLE);
            moredetails.setVisibility(View.INVISIBLE);
        }

        moredetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewResult.this, GeneralPlantInfo.class);
                startActivity(intent);
            }
        });


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewResult.this, SetImage.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}  