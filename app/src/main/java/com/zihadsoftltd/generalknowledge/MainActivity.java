package com.zihadsoftltd.generalknowledge;


import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    AdView mAdView;
    GridViewbyZihad mainGrid;
    SharedPreferences sharedPreferences;
    TextView tvScore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        // কন্টেন্টকে status bar ও navigation bar এর নিচে যেতে দেয় এবং প্যাডিং ঠিক রাখে
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main),(view, insets) -> {
                    Insets systemBarsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                    view.setPadding(systemBarsInsets.left, systemBarsInsets.top, systemBarsInsets.right, systemBarsInsets.bottom);
                    return insets;
                });

        mAdView = findViewById(R.id.adView);
        mainGrid = findViewById(R.id.mainGrid);
        tvScore = findViewById(R.id.tvScore);

        mAdView.setVisibility(View.GONE);
        sharedPreferences = getSharedPreferences(getString(R.string.app_name), MODE_PRIVATE);

        //calling a method to create our question bank with ans
        QuestionCollection.createQuestionBank();

        adSettings();


        /*

        if (getString(R.string.show_admob_ad).contains("ON")){
            initAdmobAd();
            loadBannerAd();
            loadFullscreenAd();
        }
        */


        MyAdapter adapter = new MyAdapter();
        mainGrid.setExpanded(true);
        mainGrid.setAdapter(adapter);
        adapter.notifyDataSetChanged();


        String lastScore = sharedPreferences.getString("savedScore", "0");
        tvScore.setText(lastScore);


        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // আগের মতো কাজ করান এখানে
                Intent intent = new Intent(MainActivity.this, MainFlag.class);
                startActivity(intent);
                finish();
            }
        });




    }
    //============ onCreate Ends here
    //====================================================================



    //========== FIREBASE ADS CONTROL====================

    private void adSettings(){
        //code here =====

        FirebaseDatabase database=FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("ads");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String settings = snapshot.child("settings").getValue(String.class);
                    if (settings.contains("ON")) {
                        initAdmobAd();
                        loadBannerAd();
                        loadFullscreenAd();
                        mAdView.setVisibility(View.VISIBLE);
                    } else if (settings.contains("OFF")) {
                        mAdView.setVisibility(View.GONE);
                    }
                }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {
               Log.e("Firebase", "Ad settings load failed: " + error.getMessage());
            }
        });



                /*

                if (settings.contains("ON")){
                    initAdmobAd();
                    loadBannerAd();
                    loadFullscreenAd();
                    mAdView.setVisibility(View.VISIBLE);

                }else{
                    mAdView.setVisibility(View.VISIBLE);

                    if (settings.contains("OFF")){
                        mAdView.setVisibility(View.GONE);
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });*/

    }




    @Override
    protected void onResume() {
        String lastScore = sharedPreferences.getString("savedScore", "0");
        tvScore.setText(lastScore);
        super.onResume();
    }


    private class MyAdapter extends BaseAdapter {
        private LayoutInflater inflater;

        public  MyAdapter(){
            this.inflater = (LayoutInflater) MainActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return QuestionCollection.questionBank.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            convertView = inflater.inflate(R.layout.grid_item, parent, false); 


            ImageView imgIcon = convertView.findViewById(R.id.imgIcon);
            TextView tvTitle = convertView.findViewById(R.id.tvTitle);
            LinearLayout layItem = convertView.findViewById(R.id.layItem);

            HashMap<String, String> mHashMap =  QuestionCollection.subjectList.get(position);
            String subjectName = mHashMap.get("subjectName");
            String icon = mHashMap.get("icon");


            if (tvTitle!=null) tvTitle.setText(subjectName);
            if (imgIcon!=null && icon!=null) {
                int drawable = Integer.parseInt(icon);
                imgIcon.setImageResource( drawable );
            }

            Animation animation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.anim_grid);
            animation.setStartOffset(position*400);
            convertView.startAnimation(animation);


            layItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    QuestionCollection.SUBJECT_NAME = subjectName;
                    QuestionCollection.question_list = QuestionCollection.questionBank.get(position);

                    Intent intent = new Intent(MainActivity.this, QuestionCollection.class);
                    startActivity(intent);

                    showInterstitial();

                }
            });



            return convertView;
        }
    }




    //=============================================
    //=============================================
    //=============================================












    private void initAdmobAd(){
        if (getString(R.string.device_id).length()>12){
            //Adding your device id -- to avoid invalid activity from your device
            List<String> testDeviceIds = Arrays.asList(getString(R.string.device_id));
            RequestConfiguration configuration =
                    new RequestConfiguration.Builder().setTestDeviceIds(testDeviceIds).build();
            MobileAds.setRequestConfiguration(configuration);
        }
        //Init Admob Ads

        //===================================================

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });



    }


    //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>





    int BANNER_AD_CLICK_COUNT =0;
    //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    private void loadBannerAd(){
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                if (BANNER_AD_CLICK_COUNT >=2){
                    if(mAdView!=null) mAdView.setVisibility(View.GONE);
                }else{
                    if(mAdView!=null) mAdView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onAdFailedToLoad(LoadAdError adError) {
                // Code to be executed when an ad request fails.
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }

            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
                BANNER_AD_CLICK_COUNT++;

                if (BANNER_AD_CLICK_COUNT >=2){
                    if(mAdView!=null) mAdView.setVisibility(View.GONE);
                }

            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }
        });

    }
    //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>






    //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

    // loadFullscreenAd method starts here.....
    InterstitialAd mInterstitialAd;
    int FULLSCREEN_AD_LOAD_COUNT=0;
    private void loadFullscreenAd(){

        //Requesting for a fullscreen Ad
        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(this,getString(R.string.admob_INTERSTITIAL_UNIT_ID), adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                // The mInterstitialAd reference will be null until
                // an ad is loaded.
                mInterstitialAd = interstitialAd;

                //Fullscreen callback || Requesting again when an ad is shown already
                mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback(){
                    @Override
                    public void onAdDismissedFullScreenContent() {
                        // Called when fullscreen content is dismissed.
                        //User dismissed the previous ad. So we are requesting a new ad here
                        FULLSCREEN_AD_LOAD_COUNT++;

                        if(FULLSCREEN_AD_LOAD_COUNT<3)
                            loadFullscreenAd();
                        Log.d("FULLSCREEN_AD", ""+FULLSCREEN_AD_LOAD_COUNT);

                    }

                }); // FullScreen Callback Ends here


            }
            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                // Handle the error
                mInterstitialAd = null;
            }

        });

    }
    // loadFullscreenAd method ENDS  here..... >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

    //==============================================
    private void showInterstitial() {
        // Show the ad if it's ready.
        if (mInterstitialAd != null ) {
            mInterstitialAd.show(this);
        }
    }
    //============================================



    /*
    @SuppressLint("MissingSuperCall")
    public void onBackPressed(){
        Intent intent = new Intent(MainActivity.this, MainFlag.class);
        startActivity(intent);
        finish();
    }

     */

    //#############################################################################################






//---------------------------------------
}