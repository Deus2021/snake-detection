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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.udom.myapplication.ml.ModelUnquant;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.image.ImageProcessor;
import org.tensorflow.lite.support.image.ops.ResizeOp;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class SetImage extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_IMAGE_SELECT = 2;
    private static final int PERMISSION_REQUEST_CODE_STORAGE = 3;
    private static final int PERMISSION_REQUEST_CODE_CAMERA = 4;
    private static final int MAX_IMAGE_SIZE = 1024; // Maximum allowed image size in kilobytes

    //private  static  final  int imageSize = 224;
    public static final String EXTRA_IMAGE_DATA = "image_data";

    private Button selectBtn, captureBtn, predictBtn;
    private TextView result;
    private ImageView imageView;

    private Toolbar toolbar;
    private Bitmap bitmap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_image);

        toolbar = findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.menu_toolbar);
        setSupportActionBar(toolbar);

        selectBtn = findViewById(R.id.selectBtn);
        captureBtn = findViewById(R.id.captureBtn);
        predictBtn = findViewById(R.id.detectBtn);
        imageView = findViewById(R.id.imageview);
        result = findViewById(R.id.resulttext);

        ImageProcessor imageProcessor = new  ImageProcessor.Builder()
                .add(new ResizeOp(224, 244, ResizeOp.ResizeMethod.BILINEAR)).build();

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
                    bitmap = Bitmap.createScaledBitmap(bitmap,224,224,false);
                    try {
                        ModelUnquant model = ModelUnquant.newInstance(getApplicationContext());


                        TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 224, 224, 3}, DataType.FLOAT32);
                        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4 * 224 * 224 * 3);
                        byteBuffer.order(ByteOrder.nativeOrder());

                        int [] intValues = new int[224 * 224];
                        bitmap.getPixels(intValues, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());
                        int pixel = 0;
                        for (int i = 0; i < 224; i++) {
                            for (int j = 0; j < 224; j++) {
                                int val = intValues[pixel++];
                                byteBuffer.putFloat(((val >> 16) & 0xFF) * (1.f / 255.f));
                                byteBuffer.putFloat(((val >> 8) & 0xFF) * (1.f / 255.f));
                                byteBuffer.putFloat((val & 0xFF) * (1.f / 255.f));
                            }
                        }

                        inputFeature0.loadBuffer(byteBuffer);

                        // Runs model inference and gets result.
                        ModelUnquant.Outputs outputs = model.process(inputFeature0);
                        TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();

                        float[] confidences = outputFeature0.getFloatArray();
                        int maxPos = 0;
                        float maxConfidence = 0;
                        for (int i = 0; i < confidences.length; i++) {
                            if (confidences[i] > maxConfidence) {
                                maxConfidence = confidences[i];
                                maxPos = i;
                            }
                        }
                        String[] classes = {"male", "female", "not-papaya"};

                        result.setText(classes[maxPos]);
                        result.setVisibility(View.GONE);


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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.help:
                Intent intent = new Intent(SetImage.this, Help.class);
                startActivity(intent);
                return true;

            case R.id.manual:
                Intent intent2 = new Intent(SetImage.this, UserManual.class);
                startActivity(intent2);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
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
           // bitmap = resizeBitmap(bitmap, MAX_IMAGE_SIZE);
            imageView.setImageBitmap(bitmap);

        } else if (requestCode == REQUEST_IMAGE_SELECT && resultCode == RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
               // bitmap = resizeBitmap(bitmap, MAX_IMAGE_SIZE);
                imageView.setImageBitmap(bitmap);
                getImageSize(selectedImage);
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
                    Toast.makeText(this, "Image size exceeds the limit of 1024 KB, select another image", Toast.LENGTH_LONG).show();
                }
                return size;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }
}







