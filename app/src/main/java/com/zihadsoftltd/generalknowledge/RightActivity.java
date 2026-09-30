package com.zihadsoftltd.generalknowledge;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class RightActivity extends AppCompatActivity {

    private MediaPlayer audio;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_right);

        // কন্টেন্টকে status bar ও navigation bar এর নিচে যেতে দেয় এবং প্যাডিং ঠিক রাখে
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.right),(view, insets) -> {
            Insets systemBarsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            view.setPadding(systemBarsInsets.left, systemBarsInsets.top, systemBarsInsets.right, systemBarsInsets.bottom);
            return insets;
        });

        audio = MediaPlayer.create(this, R.raw.right_sound);
        audio.start();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, 2200);
    }

    @Override
    protected void onDestroy() {
        if (audio != null) {
            audio.release();
            audio = null;
        }
        super.onDestroy();
    }
}







