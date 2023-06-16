package com.udom.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashScreen extends Activity {


    private static final int SPLASH_SCREEN_TIMEOUT = 3000; // 3 seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the content view to your splash screen layout
        setContentView(R.layout.activity_splash_screen);

        // Create a Handler to delay the start of the next Activity
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Start the next Activity
                Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                startActivity(intent);

                // Finish the splash screen Activity
                finish();
            }
        }, SPLASH_SCREEN_TIMEOUT);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
