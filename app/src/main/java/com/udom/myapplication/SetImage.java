package com.udom.myapplication;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.udom.myapplication.ml.ModelUnquant;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class SetImage extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_IMAGE_SELECT = 2;
    private static final int PERMISSION_REQUEST_CODE_STORAGE = 3;
    private static final int PERMISSION_REQUEST_CODE_CAMERA = 4;
    private static final int MAX_IMAGE_SIZE = 1024; // Maximum allowed image size in kilobytes

    public static final String EXTRA_IMAGE_DATA = "image_data";

    private Button selectBtn, captureBtn, predictBtn;
    private TextView result;
    private ImageView imageView;
    private Bitmap bitmap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_image);

        String[] labels=new String[4];
        int count=0;
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(getAssets().open("labels.txt")));
            String line=bufferedReader.readLine();
            while (line!=null){
                labels[count]=line;
                count++;
                line=bufferedReader.readLine();
            }
        }catch (IOException e){
            e.printStackTrace();
        }

        selectBtn = findViewById(R.id.selectBtn);
        captureBtn = findViewById(R.id.captureBtn);
        predictBtn = findViewById(R.id.detectBtn);
        imageView = findViewById(R.id.imageview);
        result = findViewById(R.id.resulttext);

        selectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkPermission(Manifest.permission.MANAGE_EXTERNAL_STORAGE)) {
                    openImagePicker();
                } else {
                    requestPermission(Manifest.permission.MANAGE_EXTERNAL_STORAGE, PERMISSION_REQUEST_CODE_STORAGE);
                }
            }
        });

        captureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestCameraPermission();
            }
        });

        predictBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (bitmap != null) {
                    try {
                        ModelUnquant model = ModelUnquant.newInstance(SetImage.this);

                        // Creates inputs for reference.
                        TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 224, 224, 3}, DataType.FLOAT32);

                        // bitmap = Bitmap.createScaledBitmap(bitmap, 224, 224,true);
                        Bitmap input=Bitmap.createScaledBitmap(bitmap,224,224,true);
                        TensorImage image=new TensorImage(DataType.FLOAT32);
                        image.load(input);
                        ByteBuffer byteBuffer=image.getBuffer();
                        inputFeature0.loadBuffer(byteBuffer);
                        //inputFeature0.loadBuffer(TensorImage.fromBitmap(bitmap).getBuffer());

                        // Runs model inference and gets result.
                        ModelUnquant.Outputs outputs = model.process(inputFeature0);
                        TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();

                        result.setText(labels[getMax(outputFeature0.getFloatArray())]+" ");

                        result.setVisibility(View.GONE);

                        int maxIndex = getMax(outputFeature0.getFloatArray());
                        // String resultText = String.valueOf(maxIndex);
                        String resultText = result.getText().toString();

                        Intent intent = new Intent(SetImage.this, ViewResult.class);
                        byte[] imageData = getByteArrayFromBitmap(bitmap);
                        intent.putExtra(EXTRA_IMAGE_DATA, imageData);
                        intent.putExtra("result", resultText);
                        startActivity(intent);

                        // Releases model resources if no longer used.
                        model.close();
                    } catch (IOException e) {
                        // TODO Handle the exception
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Please capture or select an image first", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    int getMax(float[] arr){
        int max = 0;
        for (int i = 0; i<arr.length; i++){
            if(arr[i] > arr[max]) max=i;
        }
        return max;
    }

    private void requestCameraPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            } else {
                ActivityCompat.requestPermissions(SetImage.this, new String[]{Manifest.permission.CAMERA}, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    private boolean checkPermission(String permission) {
        int result = ContextCompat.checkSelfPermission(this, permission);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            return Environment.isExternalStorageManager();
        } else {
            return result == PackageManager.PERMISSION_GRANTED;
        }
    }

    private void requestPermission(String permission, int requestCode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            try {
                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                intent.setData(Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, requestCode);
            } catch (Exception e) {
                // Handle exception
            }
        } else {
            ActivityCompat.requestPermissions(this, new String[]{permission}, requestCode);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE_STORAGE) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                if (Environment.isExternalStorageManager()) {
                    openImagePicker();
                } else {
                    Toast.makeText(this, "Storage permission denied. Unable to proceed.", Toast.LENGTH_SHORT).show();
                }
            } else {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openImagePicker();
                } else {
                    Toast.makeText(this, "Storage permission denied. Unable to proceed.", Toast.LENGTH_SHORT).show();
                }
            }
        } else if (requestCode == PERMISSION_REQUEST_CODE_CAMERA) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            } else {
                Toast.makeText(this, "Camera permission denied. Unable to proceed.", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
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
            bitmap = resizeBitmap(bitmap, MAX_IMAGE_SIZE);
            imageView.setImageBitmap(bitmap);

        } else if (requestCode == REQUEST_IMAGE_SELECT && resultCode == RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                bitmap = resizeBitmap(bitmap, MAX_IMAGE_SIZE);
                imageView.setImageBitmap(bitmap);
                getImageSize(selectedImage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private Bitmap resizeBitmap(Bitmap bitmap, int maxSize) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int maxDimension = Math.max(width, height);

        if (maxDimension > maxSize) {
            float scale = (float) maxSize / maxDimension;
            int scaledWidth = Math.round(scale * width);
            int scaledHeight = Math.round(scale * height);
            return Bitmap.createScaledBitmap(bitmap, scaledWidth, scaledHeight, true);
        }

        return bitmap;
    }

    private byte[] getByteArrayFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

    private int getImageSize(Uri imageUri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(imageUri);
            if (inputStream != null) {
                int size = inputStream.available() / 1024; // convert to kilobytes
                inputStream.close();
                if (bitmap != null && size <= MAX_IMAGE_SIZE) {
                    predictBtn.setEnabled(true);
                } else {
                    predictBtn.setEnabled(false);
                    Toast.makeText(this, "Image size exceeds the limit of 1024 KB, select another image", Toast.LENGTH_SHORT).show();
                }
                return size;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }
}







