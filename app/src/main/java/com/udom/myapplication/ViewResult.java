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

    ImageView imageView, camera;
    Button maleinfo, femaleinfo;

    LinearLayout layoutfemale, layoutmale, resultlayout, layoutnotpapaya;
    TextView resultview;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_result);

        imageView = findViewById(R.id.imageview);
        resultview  = findViewById(R.id.viewresult);
        maleinfo = findViewById(R.id.maleBtninfo);
        femaleinfo = findViewById(R.id.femaleBtninfo);
        resultlayout = findViewById(R.id.layout1);
        camera = findViewById(R.id.cameranotpapaya);

        layoutfemale = findViewById(R.id.layoutfemale);
        layoutmale  = findViewById(R.id.layoutmale);
        layoutnotpapaya = findViewById(R.id.layoutnotpapaya);

        maleinfo.setEnabled(false);
        femaleinfo.setEnabled(false);


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

        if (result.equals("female")) {
            resultview.setTextColor(getResources().getColor(R.color.light_green));
            layoutfemale.setVisibility(View.VISIBLE);
        } else if (result.equals("male")) {
            resultview.setTextColor(getResources().getColor(R.color.light_green));
            layoutmale.setVisibility(View.VISIBLE);
        } else if (result.equals("not-papaya")) {
            resultview.setTextColor(getResources().getColor(R.color.red));
            layoutnotpapaya.setVisibility(View.VISIBLE);
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

        camera.setOnClickListener(new View.OnClickListener() {
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