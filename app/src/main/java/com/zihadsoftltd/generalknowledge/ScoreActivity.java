package com.zihadsoftltd.generalknowledge;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class ScoreActivity extends AppCompatActivity {
    TextView txtscore,firstMenu,mainMenu;

    TextView txtStatus, tvSubjectName;
    MediaPlayer audio;
    ImageView imgBack;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_show_score);
        // কন্টেন্টকে status bar ও navigation bar এর নিচে যেতে দেয় এবং প্যাডিং ঠিক রাখে
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.score),(view, insets) -> {
            Insets systemBarsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            view.setPadding(systemBarsInsets.left, systemBarsInsets.top, systemBarsInsets.right, systemBarsInsets.bottom);
            return insets;
        });

        imgBack = findViewById(R.id.imgBack);
        tvSubjectName = findViewById(R.id.tvSubjectName);
        txtscore = findViewById(R.id.txtscore);
        firstMenu = findViewById(R.id.firstMenu);
        mainMenu = findViewById(R.id.mainMenu);
        txtStatus = findViewById(R.id.txtStatus);
        sharedPreferences = getSharedPreferences(getString(R.string.app_name), MODE_PRIVATE);

        Intent intent = getIntent();
        String scores = String.valueOf(intent.getIntExtra("score", 0));
        //txtscore.setText(scores);
        // 🔹 ইংরেজি সংখ্যা → বাংলা সংখ্যায় রূপান্তর করে দেখানো
        txtscore.setText(convertToBanglaNumber(scores));
        txtStatus.setText(setStatus(scores));
        // 🔹 অডিও চালানো (slight delay দিলে smooth হয়)
        new Handler().postDelayed(() -> audio.start(), 200);


        tvSubjectName.setText(QuestionCollection.SUBJECT_NAME);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent home = new Intent(ScoreActivity.this, MainActivity.class);
                home.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(home);
                finish();
            }
        });


        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("savedScore", scores + "  (" + QuestionCollection.SUBJECT_NAME+")");
        editor.apply();
        /* + " ("+ QuestionCollection.SUBJECT_NAME+")"*/






        firstMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(ScoreActivity.this, MainActivity.class);
                startActivity(myIntent);
                finish();

            }
        });


        mainMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(ScoreActivity.this, MainFlag.class);
                startActivity(myIntent);
                finish();

            }
        });


        //========================ONBackPresss  =====================

        // ✅ Modern onBackPressed handling
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Intent home = new Intent(ScoreActivity.this, MainActivity.class);
                home.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(home);
                finish();
            }
        });


    }

    private String setStatus(String scores){
        int score = Integer.parseInt(scores);

        if(score >= 150){
            audio = MediaPlayer.create(this, R.raw.high_score);
            return "অভিনন্দন। আপনি অসাধারন খেলেছেন!";
        }

        if (score >= 100){
            audio = MediaPlayer.create(this,  R.raw.medium_score);
            return "দারুণ! খুব ভালো হয়েছে !";
        }

        audio = MediaPlayer.create(this,  R.raw.low_score);
        return "আরো ভালো করার চেষ্টা করুন।";

    }


    //=======================================================

    // ইংরেজি সংখ্যা → বাংলা সংখ্যায় রূপান্তর ফাংশন
    private String convertToBanglaNumber(String englishNumber) {
        return englishNumber
                .replace("0", "০")
                .replace("1", "১")
                .replace("2", "২")
                .replace("3", "৩")
                .replace("4", "৪")
                .replace("5", "৫")
                .replace("6", "৬")
                .replace("7", "৭")
                .replace("8", "৮")
                .replace("9", "৯");
    }


}