package com.udom.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class SetImage extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_IMAGE_SELECT = 2;
    private static final int PERMISSION_REQUEST_CODE = 3;

    public static final String EXTRA_IMAGE_DATA = "image_data";

    private Button selectBtn, captureBtn, predictBtn;
    private ImageView imageView;
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_image);

        selectBtn = findViewById(R.id.selectBtn);
        captureBtn = findViewById(R.id.captureBtn);
        predictBtn = findViewById(R.id.detectBtn);
        imageView = findViewById(R.id.imageview);

        selectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    openImagePicker();
                } else {
                    requestPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
                }
            }
        });

        captureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkPermission(Manifest.permission.CAMERA)) {
                    openCamera();
                } else {
                    requestPermission(Manifest.permission.CAMERA);
                }
            }
        });

        predictBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bitmap != null) {
                    Intent intent = new Intent(SetImage.this, ViewResult.class);
                    byte[] imageData = getByteArrayFromBitmap(bitmap);
                    intent.putExtra(EXTRA_IMAGE_DATA, imageData);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "Please capture/upload an image first", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean checkPermission(String permission) {
        int result = ContextCompat.checkSelfPermission(this, permission);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission(String permission) {
        ActivityCompat.requestPermissions(this, new String[]{permission}, PERMISSION_REQUEST_CODE);
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_IMAGE_SELECT);
    }

    private void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK && data != null) {
            Bundle extras = data.getExtras();
            bitmap = (Bitmap) extras.get("data");
            imageView.setImageBitmap(bitmap);
        } else if (requestCode == REQUEST_IMAGE_SELECT && resultCode == RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
//                imageView.setImageBitmap(bitmap);
                imageView.setImageURI(selectedImage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private byte[] getByteArrayFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, perform the corresponding action again
                int permissionIndex = getPermissionIndex(permissions[0]);
                switch (permissionIndex) {
                    case 0:
                        openCamera();
                        break;
                    case 1:
                        openImagePicker();
                        break;
                }
            } else {
                Toast.makeText(this, "Permission denied. Unable to proceed.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private int getPermissionIndex(String permission) {
        if (permission.equals(Manifest.permission.CAMERA)) {
            return 0;
        } else if (permission.equals(Manifest.permission.READ_EXTERNAL_STORAGE)) {
            return 1;
        }
        return -1;
    }
}




