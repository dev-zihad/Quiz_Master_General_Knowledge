package com.zihadsoftltd.generalknowledge;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class  MainFlag extends AppCompatActivity {
   Button byEnglish;

    LinearLayout RateUs,privacyPolicy,moreApps,shareApp,exitApp,detailsApp;
    Dialog dialogBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.main_flag);
        // কন্টেন্টকে status bar ও navigation bar এর নিচে যেতে দেয় এবং প্যাডিং ঠিক রাখে
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_flag),(view, insets) -> {
            Insets systemBarsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            view.setPadding(systemBarsInsets.left, systemBarsInsets.top, systemBarsInsets.right, systemBarsInsets.bottom);
            return insets;
        });
        byEnglish=findViewById(R.id.byEnglish);
        RateUs = findViewById(R.id.RateUs);
        privacyPolicy = findViewById(R.id.privacyPolicy);
        moreApps = findViewById(R.id.moreApps);
        shareApp = findViewById(R.id.shareApp);
        exitApp = findViewById(R.id.exitApp);
        detailsApp = findViewById(R.id.detailsApp);
        dialogBox = new Dialog(MainFlag.this);


        byEnglish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Code here
                Intent myIntent = new Intent(MainFlag.this, MainActivity.class);
                startActivity(myIntent);
                finish();
            }
        });

        RateUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RateUs();
            }
        });
        moreApps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MoreApps();
            }
        });
        privacyPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainFlag.this,"অপেক্ষা করুন...",Toast.LENGTH_LONG).show();

                Intent myIntent=new Intent(MainFlag.this,PrivacyPolicy.class);
                startActivity(myIntent);
                finish();
            }
        });

        shareApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareApp(MainFlag.this);
            }
        });
        exitApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });



        detailsApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogBox.setContentView(R.layout.popup_menu);
                dialogBox.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialogBox.show();
            }
        });

        //================Onback Press Method========================

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                final AlertDialog.Builder alert = new AlertDialog.Builder(MainFlag.this);
                View mView = getLayoutInflater().inflate(R.layout.dialog_layout, null);
                alert.setView(mView);

                final AlertDialog alertDialog = alert.create();
                alertDialog.setCancelable(false);

                mView.findViewById(R.id.chancelBTN).setOnClickListener(v -> {
                    alertDialog.dismiss();
                });

                mView.findViewById(R.id.okBTN).setOnClickListener(v -> {
                    Toast.makeText(MainFlag.this, "Closed", Toast.LENGTH_SHORT).show();
                    alertDialog.dismiss();
                    finishAndRemoveTask();
                });

                alertDialog.show();
            }
        });


    }//======= end oncreate method =========


    //======================= details apps ===================

/*
    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed(){
        //super.onBackPressed();
        final AlertDialog.Builder alert = new AlertDialog.Builder(MainFlag.this);
        View mView = getLayoutInflater().inflate(R.layout.dialog_layout, null);
        alert.setView(mView);

        final AlertDialog alertDialog = alert.create();
        alertDialog.setCancelable(false);

        mView.findViewById(R.id.chancelBTN).setOnClickListener(v -> {
            alertDialog.dismiss();
        });

        mView.findViewById(R.id.okBTN).setOnClickListener(v -> {
            Toast.makeText(this, "Closed", Toast.LENGTH_SHORT).show();
            alertDialog.dismiss();
            finishAndRemoveTask();
        });

        alertDialog.show();


    }

    */



    //#############################################################################################
    //#############################################################################################

    private void MoreApps(){
        // code here
        String developerName ="Zihad Hossain";
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://search?q=pub:"+ developerName)));

        }catch (ActivityNotFoundException e){

            startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse("https://play.google.com/store/apps/details?id="+developerName)));
        }


    }



    // =========================== SHARE APPS METHOD =========================



    private  void ShareApp(Context context){
        final String appPackageName= context.getPackageName();
        Intent myIntent= new Intent();
        myIntent.setAction(Intent.ACTION_SEND);
        myIntent.putExtra(Intent.EXTRA_TEXT,"Download Now : https://play.google.com/store/apps/details?id=" + appPackageName);
        myIntent.setType("text/plain");
        Intent shareIntent = Intent.createChooser(myIntent, "Share App via");
        context.startActivity(shareIntent);
    }
    //============================================================================
    //================= Rate us  Method============================================

    private void RateUs(){
        Uri uri= Uri.parse("https://play.google.com/store/apps/details?id="+ getApplicationContext().getPackageName());
        Intent RateIntent= new Intent(Intent.ACTION_VIEW,uri);

        try {
            startActivity(RateIntent);
        }catch ( Exception e){
            Toast.makeText(MainFlag.this,"Unable to Open\n"+e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }


}