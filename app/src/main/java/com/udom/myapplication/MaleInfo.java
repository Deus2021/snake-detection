package com.udom.myapplication;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;

public class MaleInfo extends AppCompatActivity {

    private TextView contentTextView;
    private ProgressBar progressBar;

    private static final String URL = "https://beeaware.org.au/pollination/pollinator-reliant-crops/papaya/";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_male_info);

        contentTextView = findViewById(R.id.contentTextView);
        progressBar = findViewById(R.id.progressBar);

        new FetchContentTask().execute();
    }

    private class FetchContentTask extends AsyncTask<Void, Void, String> {

        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(MaleInfo.this, "Loading", "Please wait while we are downloading...");
            progressBar.setVisibility(ProgressBar.VISIBLE);
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                Document doc = Jsoup.connect(URL).get();
                Element contentElement = doc.getElementById("content");
                return contentElement.text();
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
                Toast.makeText(MaleInfo.this, "Failed to fetch content", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
