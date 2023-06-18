package com.udom.myapplication;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;

public class FemaleInfo extends AppCompatActivity {

    private TextView contentTextView;
    private ProgressBar progressBar;

    private static final String URL = "https://www.instagram.com/samihan_sawant/?h1=en";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_female_info);

        contentTextView = findViewById(R.id.contentTextView);
        progressBar = findViewById(R.id.progressBar);

        new FetchContentTask().execute();
    }

    private class FetchContentTask extends AsyncTask<Void, Void, String> {

        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(FemaleInfo.this, "Loading", "Please wait while we are downloading...");
            progressBar.setVisibility(ProgressBar.VISIBLE);
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                Document doc = Jsoup.connect(URL).get();
                Element contentElement = doc.getElementById("content");
                if (contentElement != null) {
                    return contentElement.text();
                } else {
                    Log.e("FemaleInfo", "Content element not found in HTML");
                    return null;
                }
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progressDialog.dismiss();
            progressBar.setVisibility(ProgressBar.GONE);

            if (result != null) {
                contentTextView.setText(result);
            } else {
                Toast.makeText(FemaleInfo.this, "Failed to fetch content", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
