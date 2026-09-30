package com.zihadsoftltd.generalknowledge;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;

public class PrivacyPolicy extends AppCompatActivity {

   public WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.privacy_policy);
        // কন্টেন্টকে status bar ও navigation bar এর নিচে যেতে দেয় এবং প্যাডিং ঠিক রাখে
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.privacy),(view, insets) -> {
            Insets systemBarsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            view.setPadding(systemBarsInsets.left, systemBarsInsets.top, systemBarsInsets.right, systemBarsInsets.bottom);
            return insets;
        });

        webView=findViewById(R.id.webView);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("https://sites.google.com/view/quizmaster-general-knowledge/home");





        // Back press dispatcher setup
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // যদি WebView-তে back history থাকে, তাহলে পিছনে যাবে
                if (webView.canGoBack()) {
                    webView.goBack();
                } else {
                    // না থাকলে MainFlag এ যাবে
                    Intent intent = new Intent(PrivacyPolicy.this, MainFlag.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

    }

}