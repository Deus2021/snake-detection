package com.udom.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class Help extends AppCompatActivity {

    private Button contactSupportButton;
    private Button faqsButton;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        contactSupportButton = findViewById(R.id.contactSupportButton);
        faqsButton = findViewById(R.id.faqsButton);

        contactSupportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open a support contact form or chat
                openSupportContactForm();
            }
        });

        faqsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open FAQs activity or website
                openFAQs();
            }
        });
    }

    private void openSupportContactForm() {
        // Replace "support_contact_form_url" with the actual URL of your support contact form
        String supportContactFormUrl = "https://example.com/support/contact";
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(supportContactFormUrl));
        startActivity(intent);
    }

    private void openFAQs() {
        // Replace "faqs_url" with the actual URL of your FAQs page
        String faqsUrl = "https://example.com/faqs";
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(faqsUrl));
        startActivity(intent);
    }
}
