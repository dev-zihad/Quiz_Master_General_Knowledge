package com.zihadsoftltd.generalknowledge;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.TextView;

public class WrongActivity extends AppCompatActivity {

    TextView Right_Ans;
    private MediaPlayer audio;
    public static String MY_ANS = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_wrong);
        // কন্টেন্টকে status bar ও navigation bar এর নিচে যেতে দেয় এবং প্যাডিং ঠিক রাখে
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.wrong),(view, insets) -> {
            Insets systemBarsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            view.setPadding(systemBarsInsets.left, systemBarsInsets.top, systemBarsInsets.right, systemBarsInsets.bottom);
            return insets;
        });


        Right_Ans = findViewById(R.id.Right_Ans);
        audio = MediaPlayer.create(this, R.raw.wrong);
        audio.start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(4000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                finish();
            }
        }).start();

        Right_Ans.setText(MY_ANS);
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
