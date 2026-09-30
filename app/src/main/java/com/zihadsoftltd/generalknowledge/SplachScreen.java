package com.zihadsoftltd.generalknowledge;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SplachScreen extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        //for full screen
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.splach_screen);
        // কন্টেন্টকে status bar ও navigation bar এর নিচে যেতে দেয় এবং প্যাডিং ঠিক রাখে
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.splash),(view, insets) -> {
            Insets systemBarsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            view.setPadding(systemBarsInsets.left, systemBarsInsets.top, systemBarsInsets.right, systemBarsInsets.bottom);
            return insets;
        });


        // For Splash Screen
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!isFinishing()) { // Check if the activity is still running
                    startMainActivity();
                }
            }
        }, 5000);
        //==================================
    }
    private void startMainActivity() {
        Intent intent = new Intent(SplachScreen.this, MainFlag.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish(); // Ensure splash activity finishes
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getWindow().getDecorView().setVisibility(View.GONE); // Ensure the window is hidden
    }
}



