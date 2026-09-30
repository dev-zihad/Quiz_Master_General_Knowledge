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
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class QuestionCollection extends AppCompatActivity {
    RadioGroup radioGroup;
    TextView lblQuestion;
    RadioButton optionA;
    RadioButton optionB;
    RadioButton optionC;
    RadioButton optionD;
    Button confirm;
    String rightAnswer;
    String Answer;
    public static List<QuestionModule> question_list;
    public  int score;
    public static String SUBJECT_NAME = "";
    public static ArrayList <ArrayList<QuestionModule>> questionBank = new ArrayList<>();
    public static ArrayList <HashMap<String, String>> subjectList = new ArrayList<>();
    LinearLayout rootLay;

    //=====================
    String my_right_ans;
    //=====================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_question);
        // কন্টেন্টকে status bar ও navigation bar এর নিচে যেতে দেয় এবং প্যাডিং ঠিক রাখে
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.rootLay),(view, insets) -> {
            Insets systemBarsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            view.setPadding(systemBarsInsets.left, systemBarsInsets.top, systemBarsInsets.right, systemBarsInsets.bottom);
            return insets;
        });

        rootLay = findViewById(R.id.rootLay);
        confirm = findViewById(R.id.confirm);
        lblQuestion = findViewById(R.id.lblPergunta);
        optionA = findViewById(R.id.opcaoA);
        optionB = findViewById(R.id.opcaoB);
        optionC = findViewById(R.id.opcaoC);
        optionD = findViewById(R.id.opcaoD);
        score = 0;
        radioGroup = findViewById(R.id.radioGroup);

        if (question_list == null) {
            question_list = new ArrayList<>();
        }
        // Shuffle the questions before starting
        if (question_list != null) {
            Collections.shuffle(question_list);
        }
        loadQuestion();


        //===============================Backpress===============
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {

                SharedPreferences.Editor editor =getSharedPreferences(getString(R.string.app_name),MODE_PRIVATE).edit();
                editor.putString("savedScore",score+ " ("+ QuestionCollection.SUBJECT_NAME+")");
                editor.apply();

                //=============================
                Intent myIntent=new Intent(QuestionCollection.this,ScoreActivity.class);
                myIntent.putExtra("score",score);
                startActivity(myIntent);
                finish();
            }
        });

    }




    @Override
    protected void onRestart(){
        super.onRestart();
        loadQuestion();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(rootLay!=null) rootLay.startAnimation(AnimationUtils.loadAnimation(QuestionCollection.this, R.anim.middle_to_top));
    }



    @Override
    protected void onPause() {
        //super.onBackPressed();
        SharedPreferences.Editor editor =getSharedPreferences(getString(R.string.app_name),MODE_PRIVATE).edit();
        editor.putString("savedScore",score+ " ("+ QuestionCollection.SUBJECT_NAME+")");
        editor.apply();

        super.onPause();

    }




    private void loadQuestion(){
        //Toast.makeText(getApplicationContext(), "Total Questions: "+question_list.size(), Toast.LENGTH_SHORT).show();
        if(question_list != null && !question_list.isEmpty()) {
            QuestionModule q = question_list.remove(0);
            lblQuestion.setText(q.getQuestion());
            List<String> answers = q.getAnswers();

            optionA.setText(answers.get(0));
            optionB.setText(answers.get(1));
            optionC.setText(answers.get(2));
            optionD.setText(answers.get(3));
            rightAnswer = q.getRightAnswer();

            //========== show right ans ============
            if (rightAnswer.contains("A")){
                my_right_ans = optionA.getText().toString();
            } else if (rightAnswer.contains("B")){
                my_right_ans = optionB.getText().toString();
            } else if (rightAnswer.contains("C")){
                my_right_ans=optionC.getText().toString();
            } else if (rightAnswer.contains("D")){
                my_right_ans=optionD.getText().toString();
            }else {
                my_right_ans="No Option";
            }


            //========== show right ans ============
        } else {
            Toast.makeText(this, "No questions available", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, ScoreActivity.class);
            intent.putExtra("score", score);
            startActivity(intent);
            finish();
        }
    }

    public void loadAnswer(View view) {
        int op = radioGroup.getCheckedRadioButtonId();

        switch (op){
            case R.id.opcaoA:
                Answer="A";
                break;

            case R.id.opcaoB:
                Answer="B";
                break;

            case R.id.opcaoC:
                Answer="C";
                break;

            case R.id.opcaoD:
                Answer="D";
                break;

            default:
                return;

        }

        radioGroup.clearCheck();

        this.startActivity(isRightOrWrong(Answer));

    }

    private Intent isRightOrWrong(String Answer){
        Intent screen;
        if(Answer.equals(rightAnswer)) {
            this.score += 10;
            screen = new Intent(this, RightActivity.class);

        }else {
            screen = new Intent(this, WrongActivity.class);
            WrongActivity.MY_ANS = my_right_ans;
        }

        return screen;
    }



    //===============================================================================================







    //====================================================================
    //====================================================================
    public static  ArrayList <QuestionModule> questions;
    public static void createQuestionBank(){
        QuestionCollection.subjectList = new ArrayList<>();
        QuestionCollection.questionBank = new ArrayList<>();



        //------------- Subject 1-----------------
        //-------------  Computer -----------------
        questions = new ArrayList(){
            {

                //=====================================Basic Computer======================================

                add(new QuestionModule("আধুনিক কম্পিউটারের জনক কে?", "B", "বিল গেটস", "অ্যালান টুরিং", "টিম বার্নার্স-লি", "মার্ক জাকারবার্গ"));
                add(new QuestionModule("ইন্টারনেটের আবিষ্কারক কে?", "C", "অ্যালান টুরিং", "চার্লস ব্যাবেজ", "টিম বার্নার্স-লি", "স্টিভ জবস"));

                add(new QuestionModule("প্রথম প্রোগ্রামেবল কম্পিউটারের জনক কে?", "D", "জন ভন নিউম্যান", "গর্ডন মুর", "ডেনিস রিচি", "কনরাড জুস"));
                add(new QuestionModule("বিশ্বব্যাপী ওয়েবের জনক কে?", "B", "বিল গেটস", "টিম বার্নার্স-লি", "ল্যারি পেজ", "মার্ক জাকারবার্গ"));
                add(new QuestionModule("ইউনিক্স অপারেটিং সিস্টেমের জনক কে?", "D", "ডেনিস রিচি", "কেন থম্পসন", "ব্রায়ান কের্নিহান", "ডগলাস ম্যাকিলরয়"));
                add(new QuestionModule("অ্যাপল কোম্পানির সহ-প্রতিষ্ঠাতা কে?", "A", "স্টিভ জবস", "বিল গেটস", "মার্ক জাকারবার্গ", "ল্যারি পেজ"));
                add(new QuestionModule("কম্পিউটার গ্রাফিক্সের জনক কে?", "B", "অ্যালান টুরিং", "ইভান সাদারল্যান্ড", "জন ভন নিউম্যান", "ডগলাস এঙ্গেলবার্ট"));
                add(new QuestionModule("কম্পিউটার নেটওয়ার্কের জনক কে?", "A", "বব মেটকাফ", "ভিন্টন সের্ফ", "ল্যারি রবার্টস", "ডগলাস এঙ্গেলবার্ট"));
                add(new QuestionModule("ইউনিভ্যাক কম্পিউটারের জনক কে?", "A", "জন প্রেসপার একার্ট", "জে. প্রেসপার একার্ট", "মাউরিস উইলকস", "রবার্ট নোয়িস"));

                add(new QuestionModule("কম্পিউটার সায়েন্সের জনক কে?", "B", "অ্যালান টুরিং", "চার্লস ব্যাবেজ", "জন ভন নিউম্যান", "ডেনিস রিচি"));
                add(new QuestionModule("আধুনিক কম্পিউটারের আর্কিটেকচারের জনক কে?", "C", "জন ভন নিউম্যান", "চার্লস ব্যাবেজ", "গর্ডন মুর", "বিল গেটস"));
                add(new QuestionModule("কম্পিউটার ভাইরাসের জনক কে?", "B", "বব থমাস", "ফ্রেড কোহেন", "জন ভন নিউম্যান", "গ্রেস হপার"));
                add(new QuestionModule("মোজাইক ওয়েব ব্রাউজারের জনক কে?", "C", "টিম বার্নার্স-লি", "ল্যারি পেজ", "মার্ক আন্দ্রেসেন", "স্টিভ জবস"));

               add(new QuestionModule("ওপেন সোর্স সফটওয়্যার কোনটি?", "D", "মাইক্রোসফট ওয়ার্ড", "অ্যাডোবি ফটোশপ", "অটোক্যাড", "লিনাক্স"));

                add(new QuestionModule("কম্পিউটারের CPU এর পূর্ণরূপ কী?", "C", "Central Process Unit", "Central Print Unit", "Central Processing Unit", "Central Power Unit"));
                add(new QuestionModule("একটি বাইটে কতগুলো বিট থাকে?", "A", "৮", "১০", "১২", "১৬"));
                add(new QuestionModule("ওয়েব ব্রাউজারের উদাহরণ কোনটি?", "A", "গুগল ক্রোম", "মাইক্রোসফট এক্সেল", "অটোক্যাড", "অ্যাডোবি ফটোশপ"));
                add(new QuestionModule("ফটোশপ একটি কি ধরনের সফটওয়্যার?", "B", "অ্যান্টিভাইরাস", "গ্রাফিক্স এডিটিং", "অফিস স্যুট", "অপারেটিং সিস্টেম"));
                add(new QuestionModule("কম্পিউটারের প্রধান অংশগুলির একটি কোনটি?", "C", "মাউস", "কীবোর্ড", "মাদারবোর্ড", "মডেম"));
                add(new QuestionModule("ইন্টারনেটের আবিষ্কর্তা কে?", "A", "টিম বার্নার্স-লি", "বিল গেটস", "স্টিভ জবস", "মার্ক জাকারবার্গ"));
                add(new QuestionModule("RAM এর পূর্ণরূপ কী?", "B", "Random Access Module", "Random Access Memory", "Read Access Memory", "Random Available Memory"));
                add(new QuestionModule("মোবাইল অপারেটিং সিস্টেমের উদাহরণ কোনটি?", "C", "উইন্ডোজ ১০", "উবুন্টু", "অ্যান্ড্রয়েড", "ম্যাক ওএস"));
                add(new QuestionModule("গুগল প্রতিষ্ঠিত হয় কোন সালে?", "B", "১৯৯৬", "১৯৯৮", "২০০০", "২০০২"));
                add(new QuestionModule("কোনটি ইনপুট ডিভাইস?", "A", "মাউস", "প্রিন্টার", "মনিটর", "হার্ড ডিস্ক"));
                add(new QuestionModule("কম্পিউটারের সবচেয়ে ছোট ডাটা স্টোরেজ ইউনিট কী?", "A", "বিট", "বাইট", "কিলোবাইট", "মেগাবাইট"));
                add(new QuestionModule("HTML এর পূর্ণরূপ কী?", "A", "HyperText Markup Language", "HyperText Management Language", "HyperTool Markup Language", "HyperTime Markup Language"));
                add(new QuestionModule("ওয়াই-ফাই এর পূর্ণরূপ কী?", "B", "Wireless Figure", "Wireless Fidelity", "Wireless Finder", "Wireless Fusion"));
                add(new QuestionModule("কম্পিউটার নেটওয়ার্কে একটি নেটওয়ার্ক কার্ডের কাজ কী?", "B", "তথ্য প্রক্রিয়া করা", "তথ্য প্রেরণ ও গ্রহণ করা", "তথ্য সংরক্ষণ করা", "তথ্য প্রদর্শন করা"));
                add(new QuestionModule("কম্পিউটারের কোন অংশে ইনপুট ডিভাইসগুলি সংযুক্ত করা হয়?", "A", "USB Port", "CPU", "Monitor", "Ram"));

                // Additional questions

                add(new QuestionModule("সর্বপ্রথম প্রোগ্রামযোগ্য কম্পিউটার কোনটি?", "D", "মার্ক I", "এনিয়াক", "ইউনিভ্যাক I", "জেড 3"));
                add(new QuestionModule("সর্বপ্রথম ডিজিটাল কম্পিউটার কোনটি?", "C", "মার্ক I", "ইডসাক", "এনিয়াক", "প্যারাম"));
                add(new QuestionModule("কম্পিউটারের 'ফাদার অফ কম্পিউটিং' হিসেবে পরিচিত কে?", "A", "চার্লস ব্যাবেজ", "আলান টিউরিং", "জন ভন নিউম্যান", "স্টিভ জবস"));
                add(new QuestionModule("মাইক্রোপ্রসেসর  চিপটি সর্বপ্রথম কোন কোম্পানি তৈরি করেছিল?", "B", "মাইক্রোসফট", "ইন্টেল", "আইবিএম", "এপল"));
                add(new QuestionModule("সর্বপ্রথম বাণিজ্যিক কম্পিউটার কোনটি?", "C", "এনিয়াক", "মার্ক I", "ইউনিভ্যাক I", "এপল I"));
                add(new QuestionModule("প্রথম অপারেটিং সিস্টেমটি কোন দশকে বিকাশিত হয়?", "C", "১৯৪০ দশক",  "১৯৬০ দশক", "১৯৫০ দশক","১৯৭০ দশক"));
                add(new QuestionModule("কম্পিউটারের কোন প্রজন্মে ট্রানজিস্টর ব্যবহার শুরু হয়?", "B", "প্রথম প্রজন্ম", "দ্বিতীয় প্রজন্ম", "তৃতীয় প্রজন্ম", "চতুর্থ প্রজন্ম"));
                add(new QuestionModule("বিশ্বের প্রথম ওয়েব ব্রাউজারটির নাম কী?", "D", "ইন্টারনেট এক্সপ্লোরার", "নেটস্কেপ", "মোজিলা", "ওয়ার্ল্ডওয়াইডওয়েব"));
                add(new QuestionModule("প্রথম ইলেকট্রনিক মেমোরি ডিভাইস কোনটি?", "C", "হোলারিথ কার্ড", "চুম্বকীয় টেপ", "উইলিয়াম্স টিউব", "হার্ড ডিস্ক"));
                add(new QuestionModule("কম্পিউটারের কোন প্রজন্মে ইন্টিগ্রেটেড সার্কিট (IC) ব্যবহার শুরু হয়?", "C", "প্রথম প্রজন্ম", "দ্বিতীয় প্রজন্ম", "তৃতীয় প্রজন্ম", "চতুর্থ প্রজন্ম"));
                add(new QuestionModule(" কম্পিউটার মাউস সর্বপ্রথম কে তৈরি করেছিলেন?", "A", "ডগলাস এঙ্গেলবার্ট", "স্টিভ জবস", "বিল গেটস", "অ্যালান কায়"));
                add(new QuestionModule("ENIAC কম্পিউটারের আর্কিটেকচার ভিত্তিক ছিল কোন প্রোগ্রামিং মডেলে?", "B", "হার্ভার্ড আর্কিটেকচার", "ভন নিউম্যান আর্কিটেকচার", "নিউম্যান-পেপিস আর্কিটেকচার", "ক্রে আর্কিটেকচার"));
                add(new QuestionModule("প্রথম হার্ড ডিস্ক ড্রাইভের ধারণক্ষমতা কত ছিল?", "A", "৫ মেগাবাইট", "১০ মেগাবাইট", "১ গিগাবাইট", "৫০০ কিলোবাইট"));
                add(new QuestionModule("কম্পিউটারের কোন প্রজন্মে মাইক্রোপ্রসেসর ব্যবহার শুরু হয়?", "D", "প্রথম প্রজন্ম", "দ্বিতীয় প্রজন্ম", "তৃতীয় প্রজন্ম", "চতুর্থ প্রজন্ম"));
                add(new QuestionModule("ইউনিভ্যাক I কম্পিউটারটি কোন দশকে তৈরি হয়েছিল?", "B", "১৯৪০ দশক", "১৯৫০ দশক", "১৯৬০ দশক", "১৯৭০ দশক"));
                add(new QuestionModule("UNIX অপারেটিং সিস্টেমটি কে তৈরি করেছিলেন?", "B", "বিল গেটস", "কেন থম্পসন ও ডেনিস রিচি", "জেমস গোসলিং", "গুইডো ভ্যান রোসাম"));
                add(new QuestionModule("ইনটেল কোন সালে প্রথম মাইক্রোপ্রসেসর চালু করেছিল?", "A", "১৯৭১", "১৯৮০", "১৯৬৫", "১৯৭৫"));
                add(new QuestionModule("প্রথম কম্পিউটার ভাইরাসের নাম কী?", "B", "মোরিস", "ক্রীপার", "মেলিসা", "আইলাভইউ"));
                add(new QuestionModule("প্রথম প্রিন্টারটি কোন বছর তৈরি হয়?", "B", "১৯৩৮", "১৯৫৩", "১৯৬৪", "১৯৭২"));
                add(new QuestionModule("কোন কম্পিউটারটি সর্বপ্রথম প্রোগ্রামিং ভাষা ব্যবহার করে?", "A", "এডসাক", "ইউনিভ্যাক", "ইউনিভ্যাক I", "মার্ক I"));
                add(new QuestionModule("কম্পিউটারের ইতিহাসে 'প্রথম বাগ' হিসেবে চিহ্নিত কী ছিল?", "C", "মেমোরি ত্রুটি", "কোডিং ত্রুটি", "একটি মথ পোকা", "ম্যাগনেটিক টেপ ত্রুটি"));
                add(new QuestionModule("কোনটি প্রথম বাণিজ্যিকভাবে সফল মাইক্রোপ্রসেসর?", "A", "ইন্টেল ৪০০৪", "জেড ৮০", "মোটোরোলা ৬৮০০", "এমআইপিএস R2000"));
                add(new QuestionModule("প্রথম ব্যক্তিগত কম্পিউটার হিসেবে পরিচিত কম্পিউটার কোনটি?", "B", "আইবিএম পিসি", "অ্যালটেয়ার ৮৮০০", "অ্যাপল I", "জিরক্স অল্টো"));


                add(new QuestionModule("কোনটি প্রথম সফলভাবে বিক্রি হওয়া অ্যাপল কম্পিউটার?", "D", "অ্যাপল I", "অ্যাপল II", "অ্যাপল III", "ম্যাকিনটোশ"));
                add(new QuestionModule("ওপেন সোর্স অপারেটিং সিস্টেম লিনাক্সের প্রাথমিক নির্মাতা কে?", "B", "রিচার্ড স্টলম্যান", "লিনাস টরভাল্ডস", "ডেনিস রিচি", "কেন থম্পসন"));
                add(new QuestionModule("প্রথম ইলেকট্রনিক স্প্রেডশিট সফটওয়্যার কোনটি?", "D", "মাল্টিপ্ল্যান", "লোটাস 1-2-3", "মাইক্রোসফট এক্সেল", "ভিসিক্যাল্ক"));
                add(new QuestionModule("কোন প্রতিষ্ঠান সর্বপ্রথম কম্পিউটার তৈরি করেছিল?", "B", "আইবিএম", "এট্যানস্যান্ট", "মাইক্রোসফট", "এপল"));
                add(new QuestionModule("সর্বপ্রথম প্রোগ্রামিং ভাষা কোনটি?", "A", "মেশিন ল্যাঙ্গুয়েজ", "ফোরট্রান", "কোবোল", "অ্যাসেম্বলি"));
                add(new QuestionModule("কোন প্রতিষ্ঠান প্রথম বাণিজ্যিকভাবে  কম্পিউটার বিক্রি শুরু করেছিল?", "D", "আইবিএম", "এপল", "হিউলেট-প্যাকার্ড", "রেমিংটন র্যান্ড"));

                add(new QuestionModule("সর্বপ্রথম পার্সোনাল কম্পিউটার (PC) কোনটি?", "B", "অ্যাপল I", "অ্যালটেয়ার ৮৮০০", "জিরক্স অল্টো", "আইবিএম পিসি"));
                add(new QuestionModule("কোন প্রোগ্রামিং ভাষা প্রথম আধুনিক ভাষা হিসেবে বিবেচিত হয়?", "A", "ফোরট্রান", "কোবোল", "লিস্প", "এপিএল"));
                add(new QuestionModule("সর্বপ্রথম সফল অফিস সফটওয়্যার প্যাকেজ কোনটি?", "B", "মাইক্রোসফট অফিস", "লোটাস স্মার্টস্যুট", "ওপেনঅফিস", "অ্যাপলওয়ার্কস"));
                add(new QuestionModule("প্রথম ওয়ার্ড প্রসেসিং সফটওয়্যার কোনটি?", "A", "ওয়ার্ডস্টার", "মাইক্রোসফট ওয়ার্ড", "ওয়ার্ডপারফেক্ট", "পেজমেকার"));
                add(new QuestionModule("প্রথম ডেক্সটপ পাবলিশিং সফটওয়্যার কোনটি?", "D", "কোরেলড্র", "এডোবি ফটোশপ", "ইলাস্ট্রেটর", "অ্যালডাস পেজমেকার"));
                add(new QuestionModule("সর্বপ্রথম ইমেইল প্রোটোকল কোনটি?", "C", "HTTP", "FTP", "SMTP", "POP3"));
                add(new QuestionModule("সর্বপ্রথম সফল ভিডিও গেম কোনটি?", "B", "মারিও", "পং", "প্যাক-ম্যান", "টেট্রিস"));
                add(new QuestionModule("সর্বপ্রথম সফল কম্পিউটার নেটওয়ার্ক কোনটি?", "A", "ARPANET", "ইন্টারনেট", "ইথারনেট", "ইনট্রানেট"));
                add(new QuestionModule("সর্বপ্রথম প্রোগ্রাম কম্পাইলার তৈরি করেছিলেন কে?", "C", "অ্যালান টিউরিং", "জেমস গোসলিং", "গ্রেস হপার", "অ্যাডা লাভলেস"));
                add(new QuestionModule("সর্বপ্রথম সফল ল্যাপটপ কম্পিউটার কোনটি?", "A", "ওসম্যান ১", "ম্যাকিনটোশ", "টিআরএস-৮০", "কমপ্যাক পোর্টেবল"));
                add(new QuestionModule("ওয়ার্ল্ড ওয়াইড ওয়েব (WWW) কে এবং কোন সালে উদ্ভাবন করেন?", "D", "ল্যারি পেজ, ১৯৯১", "বিল গেটস, ১৯৮৫", "স্টিভ জবস, ১৯৮৯", "টিম বার্নার্স-লি, ১৯৮৯"));
                add(new QuestionModule("কোনটি সর্বপ্রথম সফল অপারেটিং সিস্টেম?", "B", "ডস", "উইন্ডোজ", "উনিক্স", "ম্যাকিনটোশ"));

                add(new QuestionModule("সর্বপ্রথম মেমোরি ডিভাইস হিসেবে কোনটি ব্যবহার করা হয়?", "B", "চুম্বকীয় টেপ", "পাঞ্চ কার্ড", "র‍্যাম", "হার্ড ডিস্ক"));
                add(new QuestionModule("সর্বপ্রথম সফল ইলেকট্রনিক মেইনফ্রেম কম্পিউটার কোনটি?", "A", "ইউনিভ্যাক I", "মার্ক I", "ইউনিভ্যাক II", "জেড 3"));
                add(new QuestionModule("প্রথম কম্পিউটার ভাইরাস কবে উদ্ভাবিত হয়?", "D", "১৯৮৬", "১৯৯০", "১৯৮০", "১৯৭১"));

                add(new QuestionModule("প্রথম কম্পিউটার প্রোগ্রামার কে?", "A", "এডা লাভলেস", "গ্রেস হপার", "ডেনিস রিচি", "জন কারম্যাক"));
                add(new QuestionModule("কৃত্রিম বুদ্ধিমত্তার জনক কে?", "C", "জন ম্যাকার্থি", "অ্যালান টুরিং", "রোজার শ্যাঙ্ক", "হেনরি পিকিংগার"));
                add(new QuestionModule("ইউনিক্স অপারেটিং সিস্টেমের জনক কে?", "D", "ডেনিস রিচি", "কেন থম্পসন", "ব্রায়ান কের্নিহান", "ডগলাস ম্যাকিলরয়"));
                add(new QuestionModule("কম্পিউটার প্রোগ্রামিং ভাষার জনক কে?", "C", "গ্রেস হপার", "অ্যালান টুরিং", "জন বাকাস", "কেন থম্পসন"));
                add(new QuestionModule("কম্পাইলারের জনক কে?", "B", "এডা লাভলেস", "গ্রেস হপার", "ডেনিস রিচি", "কেন থম্পসন"));
                add(new QuestionModule("কোবোল প্রোগ্রামিং ভাষার জনক কে?", "D", "গ্রেস হপার", "ডেনিস রিচি", "কেন থম্পসন", "হাওয়ার্ড ব্রোমবার্গ"));
                add(new QuestionModule("ফরট্রান প্রোগ্রামিং ভাষার জনক কে?", "C", "ডেনিস রিচি", "কেন থম্পসন", "জন বাকাস", "গ্রেস হপার"));
                add(new QuestionModule("পিএইচপি প্রোগ্রামিং ভাষার জনক কে?", "A", "রাসমুস লার্ডর্ফ", "গুইডো ভ্যান রসাম", "ডেনিস রিচি", "ল্যারি ওয়াল"));
                add(new QuestionModule("পাইথন প্রোগ্রামিং ভাষার জনক কে?", "B", "রাসমুস লার্ডর্ফ", "গুইডো ভ্যান রসাম", "ডেনিস রিচি", "জেমস গোসলিং"));
                add(new QuestionModule("জাভা প্রোগ্রামিং ভাষার জনক কে?", "D", "ডেনিস রিচি", "বজারন স্ট্রাউস্ট্রপ", "গুইডো ভ্যান রসাম", "জেমস গোসলিং"));
                add(new QuestionModule("সি প্রোগ্রামিং ভাষার জনক কে?", "C", "কেন থম্পসন", "জন বাকাস", "ডেনিস রিচি", "গ্রেস হপার"));
                add(new QuestionModule("সি++ প্রোগ্রামিং ভাষার জনক কে?", "A", "বজারন স্ট্রাউস্ট্রপ", "ডেনিস রিচি", "কেন থম্পসন", "জেমস গোসলিং"));
                add(new QuestionModule("পাস্কাল প্রোগ্রামিং ভাষার জনক কে?", "B", "নিক্লাস ওয়ার্থ", "গুইডো ভ্যান রসাম", "ডেনিস রিচি", "জন বাকাস"));
                add(new QuestionModule("রুবি প্রোগ্রামিং ভাষার জনক কে?", "C", "গুইডো ভ্যান রসাম", "ডেনিস রিচি", "ইউকিহিরো মাতসুমোতো", "জেমস গোসলিং"));
                add(new QuestionModule("পার্ল প্রোগ্রামিং ভাষার জনক কে?", "D", "রাসমুস লার্ডর্ফ", "গুইডো ভ্যান রসাম", "ডেনিস রিচি", "ল্যারি ওয়াল"));
                add(new QuestionModule("ডাটাবেস ম্যানেজমেন্ট সিস্টেমের জনক কে?", "D", "চার্লস ব্যাবেজ", "অ্যালান টুরিং", "জন ভন নিউম্যান", "এডগার এফ. কড"));
                add(new QuestionModule("ফ্লোচার্টের জনক কে?", "A", "ফ্রাঙ্ক গিলব্রেথ", "অ্যালান টুরিং", "জন ভন নিউম্যান", "চার্লস ব্যাবেজ"));


                add(new QuestionModule("SSD এর পূর্ণরূপ কী?", "A", "Solid State Drive", "Secure State Drive", "Solid Storage Disk", "Secure Storage Disk"));
                add(new QuestionModule("কোনটি কম্পিউটারের অপারেটিং সিস্টেম?", "C", "পাইথন", "জাভা", "উইন্ডোজ", "মাইক্রোসফট অফিস"));
                add(new QuestionModule("BIOS এর পূর্ণরূপ কী?", "D", "Binary Input Output System", "Basic Input Output Service", "Basic Input Output Software", "Basic Input Output System"));
                add(new QuestionModule("কম্পিউটারের প্রধান চিপটি কী নামে পরিচিত?", "A", "মাইক্রোপ্রসেসর", "মাদারবোর্ড", "হার্ড ডিস্ক", "র‍্যাম"));
                add(new QuestionModule("একটি LAN এর পূর্ণরূপ কী?", "A", "Local Area Network", "Large Area Network", "Live Area Network", "Link Area Network"));
                add(new QuestionModule("কোনটি ক্লাউড স্টোরেজ সেবা নয়?", "D", "গুগল ড্রাইভ", "ড্রপবক্স", "ওয়ানড্রাইভ", "অ্যাডোবি রিডার"));
                add(new QuestionModule("কোনটি কম্পিউটারের নন-ভলাটাইল মেমরি?", "B", "র‍্যাম", "রোম", "ক্যাশ মেমরি", "রেজিস্টার"));
                add(new QuestionModule("কোনটি একটি ইনপুট ডিভাইস নয়?", "D", "কীবোর্ড", "মাউস", "স্ক্যানার", "মনিটর"));
                add(new QuestionModule("একটি গিগাবাইটে কতগুলো মেগাবাইট থাকে?", "A", "১০২৪", "৫১২", "১০০০", "৮০০"));
                add(new QuestionModule("কোনটি মাদারবোর্ডের অংশ?", "C", "মাউস", "প্রিন্টার", "সিপিইউ", "মনিটর"));
                add(new QuestionModule("কোনটি ইমেইল প্রোটোকল?", "B", "HTTP", "SMTP", "FTP", "TCP"));
                add(new QuestionModule("কোনটি একটি সার্চ ইঞ্জিন?", "D", "উইন্ডোজ", "উইকি", "ওয়ার্ডপ্রেস", "গুগল"));
                add(new QuestionModule("কোনটি হার্ডওয়্যার?", "C", "উইন্ডোজ", "উবুন্টু", "মাদারবোর্ড", "মাইক্রোসফট ওয়ার্ড"));
                add(new QuestionModule("কোনটি কম্পিউটারের একক ইউনিট নয়?", "D", "বিট", "বাইট", "কিলোবাইট", "পিক্সেল"));
                add(new QuestionModule("কম্পিউটারের নেটওয়ার্কের জন্য কোনটি প্রয়োজন?", "B", "র‍্যাম", "নেটওয়ার্ক কার্ড", "হার্ড ডিস্ক", "সিডি"));
                add(new QuestionModule("কোনটি একটি ওয়েব ব্রাউজার?", "A", "ফায়ারফক্স", "উইন্ডোজ", "অফিস", "মাইক্রোসফট ওয়ার্ড"));
                add(new QuestionModule("একটি পিক্সেলের পূর্ণরূপ কী?", "A", "Picture Element", "Picture Expansion", "Pixel Element", "Pixel Expansion"));

            }

        };
        QuestionModule.createQuestionsForSubject("কম্পিউটার", R.drawable.computer_1, questions);


        //------------- Subject 2-----------------
        //-------------  Cyber World -----------------
        questions = new ArrayList(){
            {


                add(new QuestionModule("কেভিন মিটনিক কোন দেশের অধিবাসী ?", "A", "মার্কিন যুক্তরাষ্ট্র", "কানাডা", "চীন", "ব্রিটেন"));
                add(new QuestionModule("কেভিন মিটনিক কত বছর কারাদণ্ড ভোগ করেছিলেন?", "D", "৩ বছর", "২ বছর", "৭ বছর", "৫ বছর"));
                add(new QuestionModule("বিশ্বের সবচেয়ে শক্তিশালী হ্যাকার টিম কোনটি?", "A", "Anonymous", "LulzSec", "Equation Group", "APT28"));
                add(new QuestionModule("বিশ্বের সকল হ্যাকার এর জনক কে ?", "D",  "অ্যাড্রিয়ান লামো","মাইকেল ক্যালসে", "গ্যারি ম্যাককিনন", "কেভিন মিটনিক"));
                add(new QuestionModule("অ্যাড্রিয়ান লামো মূলত কোন ধরনের হ্যাকিং কার্যক্রমে জড়িত ছিলেন?", "B",  "ফিশিং","নেটওয়ার্ক হ্যাকিং", "ম্যালওয়্যার", "পাসওয়ার্ড ক্র্যাকিং"));
                add(new QuestionModule("মাফিয়াবয় (মাইকেল ক্যালসে) কোন দেশের হ্যাকার?", "C", "মার্কিন যুক্তরাষ্ট্র", "ভারত","কানাডা", "যুক্তরাজ্য"));
                add(new QuestionModule("‘অ্যানোনিমাস’ হ্যাকার গ্রুপের প্রধান কর্মকাণ্ড কোথায় ঘটে?", "A", "বিশ্বব্যাপী", "ইউরোপে", "মধ্যপ্রাচ্যে", "এশিয়াতে"));
                add(new QuestionModule("গ্যারি ম্যাককিনন কোন দেশের সিস্টেম হ্যাক করেছিলেন?", "A", "মার্কিন যুক্তরাষ্ট্র", "রাশিয়া", "চীন", "জাপান"));
                add(new QuestionModule("লাদার লেভিসন নামক হ্যাকার কোন দেশের নাগরিক?", "B",  "ইংল্যান্ড","মার্কিন যুক্তরাষ্ট্র", "ফ্রান্স", "জার্মানি"));
                add(new QuestionModule("কোন হ্যাকার ‘হ্যামবার্গার’ টুল ব্যবহার করেন?", "C",  "ট্রয় হান্ট", "মাইকেল ক্যালসে","ক্রিস রবার্টস", "এলিয়ট হিগিন্স"));
                add(new QuestionModule("‘লুলজসেক’নামক হ্যাকার গ্রুপ কোন দেশের?", "D", "মার্কিন যুক্তরাষ্ট্র", "জাপান", "চীন", "যুক্তরাজ্য"));
                add(new QuestionModule("অ্যালবার্ট গঞ্জালেজের হ্যাকিং কাজের প্রধান লক্ষ্য ছিল কোন তথ্য?", "A", "ক্রেডিট কার্ড তথ্য", "ব্যাংক অ্যাকাউন্ট তথ্য", "ব্যক্তিগত ইমেইল", "ফেসবুক তথ্য"));
                add(new QuestionModule("Nicolas Charbonnier কোন দেশের হ্যাকার?", "B",  "জার্মানি","ফ্রান্স","বেলজিয়াম", "অস্ট্রেলিয়া"));
                add(new QuestionModule("‘হ্যাকারওয়ান’ কোন দেশের একটি নিরাপত্তা প্ল্যাটফর্ম?", "C",  "কানাডা", "চীন","মার্কিন যুক্তরাষ্ট্র", "জাপান"));
                add(new QuestionModule("জুলিয়ান অ্যাসাঞ্জ নামক হ্যাকার কোন দেশের নাগরিক?", "A", "অস্ট্রেলিয়া", "নিউজিল্যান্ড", "কানাডা", "যুক্তরাজ্য"));
                add(new QuestionModule("‘এলিয়ট হিগিন্স’ কোন সংস্থা প্রতিষ্ঠা করেছেন?", "A", "বেলিংক্যাট", "হ্যাকারওয়ান", "লাভাবিট", "ট্রয় হান্ট"));
                add(new QuestionModule("‘ট্রোজান হর্স’ হ্যাকিং কি ধরনের আক্রমণ?", "B",  "ওয়েবসাইট হ্যাকিং","সফটওয়্যার আক্রমণ","ফাইল শেয়ারিং", "সোশ্যাল ইঞ্জিনিয়ারিং"));
                add(new QuestionModule("‘ডিডোস’ আক্রমণের প্রধান লক্ষ্য কি?", "C", "ফাইল অপসারণ", "ব্যক্তিগত তথ্য চুরি", "সার্ভারকে অতিরিক্ত লোড করা", "ডেটা এনক্রিপ্ট করা"));
                add(new QuestionModule("‘নেটওয়ার্ক স্নিফার’ কী?", "A", "নেটওয়ার্ক ট্রাফিক বিশ্লেষক", "একটি সাইবার আক্রমণ", "নেটওয়ার্ক সফটওয়্যার টুল", "একটি নেটওয়ার্ক ডিভাইস"));
                add(new QuestionModule("‘স্পাইওয়্যার’ কিসের জন্য বিখ্যাত?", "B",  "ফাইল এনক্রিপ্ট করার জন্য","ব্যক্তিগত তথ্য চুরির জন্য", "নেটওয়ার্ক সুরক্ষার জন্য", "সফটওয়্যার উন্নয়নের জন্য"));
                add(new QuestionModule("‘ফিশিং’ আক্রমণ প্রধানত কিভাবে ঘটে?", "A", "মেইল বা নকল ওয়েবসাইটের মাধ্যমে", "ফাইল ডাউনলোড করে", "পাসওয়ার্ড অনুমান করে", "ম্যালওয়্যার ব্যবহার করে"));

                add(new QuestionModule("‘ডাটা এনক্রিপশন’ কাকে বলা হয়?", "C",  "সিস্টেমের অ্যাক্সেস নিয়ন্ত্রণ", "ফাইল ডিলিটিং", "ডেটা নিরাপদভাবে সংরক্ষণ করার পদ্ধতি","নেটওয়ার্ক মনিটরিং"));
                add(new QuestionModule("‘ব্ল্যাক হ্যাট’ হ্যাকারদের কাজ কোনটি?", "D",  "ফাইল শেয়ারিং", "ব্যক্তিগত ডেটা সংগ্রহ", "হার্ডওয়্যার আপডেট","অবৈধভাবে সিস্টেমে প্রবেশ করা"));
                add(new QuestionModule("হ্যাকার কাকে বলা হয়?", "B",  "যারা কম্পিউটার তৈরি করে","যারা সিস্টেম বা নেটওয়ার্কে অনধিকার প্রবেশ করে", "যারা সফটওয়্যার বিকাশ করে", "যারা হার্ডওয়্যার মেরামত করে"));
                add(new QuestionModule("‘হোয়াইট হ্যাট’ হ্যাকার কারা?", "C",  "অভ্যন্তরীণ নিরাপত্তা মূল্যায়ন করে", "ম্যালওয়্যার তৈরি করে", "যারা সিস্টেমের নিরাপত্তা প্রদান করে ","হার্ডওয়্যার বিক্রি করে"));
                add(new QuestionModule("‘গ্রে হ্যাট’ হ্যাকাররা কিভাবে কাজ করে?", "A", "কখনো আইন ভঙ্গ করে এবং কখনো আইন অনুসরণ করে", "শুধু নিরাপত্তা পর্যালোচনা করে", "ম্যালওয়্যার তৈরি করে", "নতুন প্রযুক্তি বিকাশ করে"));
                add(new QuestionModule("বিশ্বখ্যাত হ্যাকার 'কেভিন মিটনিক' কেমন হ্যাকার ছিলেন?", "C",  "ব্ল্যাক হ্যাট", "হোয়াইট হ্যাট","গ্রে হ্যাট", "অনলাইন নিরাপত্তা বিশেষজ্ঞ"));
                add(new QuestionModule("‘প্লাগন’ নামে পরিচিত হ্যাকার কোন দেশের ছিলেন?", "A", "মার্কিন যুক্তরাষ্ট্র", "চীন", "রাশিয়া", "ভারত"));
                add(new QuestionModule("‘কুকিজ’ কি?", "A", "ওয়েবসাইটের দ্বারা ব্রাউজারে সংরক্ষিত তথ্য", "একটি প্রকার ভাইরাস", "হার্ডওয়্যার ডিভাইস", "নেটওয়ার্ক ডিভাইস"));
                add(new QuestionModule("‘অথেন্টিকেশন’ কি?", "B",  "নেটওয়ার্ক স্পিড বাড়ানো","ব্যবহারকারীর পরিচয় যাচাই করার প্রক্রিয়া", "ডাটা ম্যানিপুলেট করা", "পাসওয়ার্ড পরিবর্তন করা"));
                add(new QuestionModule("‘ডিডোস’ আক্রমণ কি?", "A", "সার্ভারকে অতিরিক্ত ট্রাফিকের মাধ্যমে বিপর্যস্ত করা", "ব্যক্তিগত তথ্য চুরি করা", "ফাইল এনক্রিপ্ট করা", "হার্ডওয়্যার অপসারণ করা"));
                add(new QuestionModule("‘নেটওয়ার্ক ইঞ্জিনিয়ার’ কারা?", "C",  "নেটওয়ার্ক শেয়ারিং করে", "নেটওয়ার্ক উন্নয়ন করে", "নেটওয়ার্ক ডিজাইন ও রক্ষণাবেক্ষণ করে","হার্ডওয়্যার মেরামত করে"));
                add(new QuestionModule("‘প্যাসওয়ার্ড ম্যানেজার’ কি?", "B",  "পাসওয়ার্ড ব্যাকআপ টুল", "পাসওয়ার্ড সুরক্ষিতভাবে সংরক্ষণের টুল", "পাসওয়ার্ড উন্নয়ন করে", "পাসওয়ার্ড আপডেট টুল"));
                add(new QuestionModule("‘ম্যালওয়্যার’ কী?", "A", "ক্ষতিকর সফটওয়্যার", "নেটওয়ার্ক সুরক্ষা টুল", "কম্পিউটার হার্ডওয়্যার", "অভ্যন্তরীণ ডিভাইস"));
                add(new QuestionModule("‘স্পাইওয়্যার’ কি?", "B",  "নেটওয়ার্ক রাউটিং ডিভাইস", "ব্যবহারকারীর তথ্য গোপনে সংগ্রহকারী সফটওয়্যার", "নেটওয়ার্ক সুরক্ষা টুল", "সফটওয়্যার আপডেট টুল"));
                add(new QuestionModule("‘র‍্যানসমওয়্যার’ কী?", "C",  "একটি নেটওয়ার্ক নিরাপত্তা প্রোটোকল", "একটি হার্ডওয়্যার ডিভাইস","ফাইল বা সিস্টেম লক করে মুক্তিপণ দাবি করে", "অভ্যন্তরীণ সফটওয়্যার"));
                add(new QuestionModule("‘ট্রোজান হর্স’ কি?", "B",  "একটি হ্যাকিং কৌশল","ক্ষতিকর সফটওয়্যার যা নিজের উপস্থিতি লুকিয়ে রাখে", "হার্ডওয়্যার অংশ", "ডাটা এনক্রিপ্ট করার সফটওয়্যার"));
                add(new QuestionModule("‘পাসওয়ার্ড’ কি?", "A", "গোপন কোড", "সফটওয়্যারের অংশ", "হার্ডওয়্যার সিস্টেম", "নেটওয়ার্ক ট্রাফিক"));
                add(new QuestionModule("‘ফায়ারওয়াল’ কি?", "B",  "ডাটা ব্যাকআপ টুল", "নেটওয়ার্ক সুরক্ষা সিস্টেম", "প্রিন্টার ড্রাইভার", "ওয়েব ব্রাউজার অ্যাড-অন"));
                add(new QuestionModule("‘লিগ্যাল হ্যাকার’ কি?", "A", "যারা আইনীভাবে নিরাপত্তা পরীক্ষা করে", "নেটওয়ার্ক ডিভাইস", "ম্যালওয়্যার প্রোগ্রাম", "হার্ডওয়্যার অংশ"));
                add(new QuestionModule("‘স্প্যাম’ কি?", "C",  "ফাইল এনক্রিপ্ট করা", "হার্ডওয়্যার স্ক্যান করা","অপ্রয়োজনীয় বা অচেনা ইমেইল", "নেটওয়ার্ক স্পিড পরীক্ষা করা"));
                add(new QuestionModule("‘ডাটা চুরি’ কিভাবে ঘটে?", "A", "অনধিকার প্রবেশ বা ম্যালওয়্যার ব্যবহার করে", "পাসওয়ার্ড পরিবর্তন করে", "ফাইল ডাউনলোড করে", "সফটওয়্যার আপডেট করে"));
                add(new QuestionModule("‘ব্রুট ফোর্স অ্যাটাক’ কি?", "B", "সিস্টেম বন্ধ করা", "সব সম্ভাব্য পাসওয়ার্ড চেষ্টা করা",  "ফাইল ডিলিট করা", "নেটওয়ার্ক ব্যান্ডউইথ ব্যবহার করা"));
                add(new QuestionModule("‘জিরো-ডে আক্রমণ’ কি?", "C", "নিরাপত্তা ত্রুটি সনাক্ত হওয়ার আগেই আক্রমণ", "ডেটা ব্যাকআপের অভাব", "হার্ডওয়্যার সমস্যা", "সফটওয়্যার আপডেটের অভাব"));
                add(new QuestionModule("কম্পিউটার ভাইরাসের জনক কে?", "A", "বব থমাস", "জন ম্যাকাফি", "জন ভন নিউম্যান", "গ্রেস হপার"));
                add(new QuestionModule("কোন হ্যাকার 'গডফাদার অফ হ্যাকিং' নামে পরিচিত?", "A", "কেভিন মিটনিক", "গ্যারি ম্যাককিনন", "অ্যাড্রিয়ান লামো", "ডেভিড লিওন"));
                add(new QuestionModule("কম্পিউটার ভাইরাস থেকে কীভাবে রক্ষা পাওয়া যায়?", "A", "অ্যান্টিভাইরাস ইনস্টল করে", "ফাইল ডাউনলোড করে", "ই-মেইল খুলে", "ইন্টারনেট ব্যবহার করে"));
                add(new QuestionModule("কম্পিউটার ভাইরাস মুছে ফেলার সফটওয়্যার কী?", "B", "ফটোশপ", "অ্যান্টিভাইরাস", "ওয়ার্ডপ্রেস", "অটোক্যাড"));

                add(new QuestionModule("কী লগার কী?", "C", "একটি হার্ডওয়্যার", "একটি নেটওয়ার্ক", "একটি ম্যালওয়্যার যা কীস্ট্রোক রেকর্ড করে", "একটি সফটওয়্যার আপডেট"));
                add(new QuestionModule("কোনটি একটি কম্পিউটার ভাইরাস নয়?", "D", "ওয়ার্ম", "ট্রোজান হর্স", "অ্যাডওয়্যার", "উইন্ডোজ"));
                add(new QuestionModule("কম্পিউটার ভাইরাসের লক্ষণ কোনটি?", "C", "তথ্য সংরক্ষণ করা", "ফাস্ট পারফরম্যান্স", "সিস্টেম ক্র্যাশ", "তথ্য বাড়ানো"));
                add(new QuestionModule("কোনটি ভাইরাস সুরক্ষা পদ্ধতি ?", "D", "অ্যান্টিভাইরাস ব্যবহার", "নিয়মিত সফটওয়্যার আপডেট", "অজানা ই-মেইল এড়ানো", "সবগুলো"));
                add(new QuestionModule("বুট সেক্টর ভাইরাস কী?", "A", "যা বুট সেক্টরে আক্রমণ করে", "একটি নিরাপত্তা প্রোগ্রাম", "একটি হার্ডওয়্যার", "একটি নেটওয়ার্ক ডিভাইস"));

                add(new QuestionModule("অ্যাডওয়্যার কী?", "A", "একটি ম্যালওয়্যার যা বিজ্ঞাপন প্রদর্শন করে", "একটি নিরাপত্তা প্রোগ্রাম", "একটি হার্ডওয়্যার", "একটি নেটওয়ার্ক ডিভাইস"));
                add(new QuestionModule("ওয়ার্ম ভাইরাস কী?", "B", "একটি নিরাপত্তা প্রোগ্রাম", "একটি ম্যালওয়্যার যা নিজেকে কপি করে", "একটি হার্ডওয়্যার", "একটি নেটওয়ার্ক ডিভাইস"));
                add(new QuestionModule("ট্রোজান হর্স মানে কি ?", "D", "র‍্যানসমওয়্যার", "স্পাইওয়্যার", "একটি ক্ষতিকর সফটওয়্যার", "ট্রয় নগরীর ঘোড়া"));




    }

        };
        QuestionModule.createQuestionsForSubject("সাইবার জগৎ", R.drawable.hacker, questions);



        //------------- Subject 3 -------------------------
        //=================== Capital ==============================

      questions = new ArrayList(){
            {


                add(new QuestionModule("আফগানিস্তানের রাজধানী কোনটি?", "D", "ইসলামাবাদ", "বাগদাদ", "জাকার্তা", "কাবুল"));
                add(new QuestionModule("আজারবাইজানের রাজধানী কোনটি?", "D", "বার্লি", "মাস্কাট", "তেহরান", "বাকু"));


                add(new QuestionModule("বাহরাইনের রাজধানী কোনটি?", "A", "মানামা", "ত্রিপোলি", "মালে", "ম্যানিলা"));
                add(new QuestionModule("বাংলাদেশের রাজধানী কোনটি?", "A", "ঢাকা", "নতুন দিল্লি", "ইসলামাবাদ", "কাবুল"));
                add(new QuestionModule("ভুটানের রাজধানী কোনটি?", "C", "ইসলামাবাদ", "কাঠমান্ডু", "থিম্পু", "লাসা"));
                add(new QuestionModule("ব্রুনাইয়ের রাজধানী কোনটি?", "B", "হ্যানয়", "বান্দর সেরি বেগাওয়ান", "আবুধাবি", "এন'ড জামেনা"));
                add(new QuestionModule("কম্বোডিয়ার রাজধানী কোনটি?", "D", "বোগোটা", "বুদাপেস্ট", "ওয়ারশ", "ফনম পেন"));
                add(new QuestionModule("চীনের রাজধানী কোনটি?", "B", "টোকিও", "বেইজিং", "উলানবাটার", "সিউল"));
                add(new QuestionModule("সাইপ্রাসের রাজধানী কোনটি?", "A", "নিকোসিয়া", "তিবিলিসি", "পিয়ংইয়াং", "ক্যানবেরা"));
                add(new QuestionModule("ভারতের রাজধানী কোনটি?", "C", "ইসলামাবাদ", "বাগদাদ", "নতুন দিল্লি", "ঢাকা"));
                add(new QuestionModule("ইন্দোনেশিয়ার রাজধানী কোনটি?", "C", "ইসলামাবাদ", "বাগদাদ", "জাকার্তা", "কুয়ালালামপুর"));
                add(new QuestionModule("ইরানের রাজধানী কোনটি?", "C", "রিয়াদ", "বাগদাদ", "তেহরান", "কাবুল"));
                add(new QuestionModule("ইরাকের রাজধানীর নাম কোনটি?", "B", "নুর-সুলতান", "বাগদাদ", "নতুন দিল্লি", "বৈরুত"));
                add(new QuestionModule("জাপানের রাজধানীর নাম কোনটি?", "D", "লাসা", "বেইজিং", "সিউল", "টোকিও"));
                add(new QuestionModule("জর্ডানের রাজধানী কোনটি?", "A", "আম্মান", "ত্রিপোলি", "অসলো", "দোহা"));
                add(new QuestionModule("কাজাখস্তানের রাজধানীর নাম কোনটি?", "C", "ইসলামাবাদ", "বাগদাদ", "নুর-সুলতান", "আশগাবাত"));
                add(new QuestionModule("কিরগিজস্তানের রাজধানীর নাম কোনটি?", "B", "উলানবাটার", "বিশকেক", "নুর-সুলতান", "আশগাবাত"));
                add(new QuestionModule("কুয়েতের রাজধানীর নাম কোনটি?", "B", "ত্রিপোলি", "কুয়েত সিটি", "দোহা", "বৈরুত"));
                add(new QuestionModule("লাওসের রাজধানীর নাম কোনটি?", "B", "ম্যানিলা", "ভিয়েনতিয়েন", "মালে", "দামেস্ক"));
                add(new QuestionModule("লেবাননের রাজধানীর নাম কি?", "C", "মাস্কাট", "তেহরান", "বৈরুত", "লাসা"));
                add(new QuestionModule("মালয়েশিয়ার রাজধানীর নাম কি?", "D", "ইসলামাবাদ", "আবুধাবি", "জাকার্তা", "কুয়ালালামপুর"));
                add(new QuestionModule("মালদ্বীপের রাজধানীর নাম কি?", "D", "ম্যানিলা", "দোহা", "নতুন দিল্লি", "মালে"));
                add(new QuestionModule("মঙ্গোলিয়ার রাজধানীর নাম কি?", "C", "নেপিডো", "বাগদাদ", "উলানবাটার", "মস্কো"));
                add(new QuestionModule("মিয়ানমারের রাজধানীর নাম কি?", "A", "নেপিডো", "থিম্পু", "উলানবাটার", "রাবাত"));
                add(new QuestionModule("নেপালের রাজধানীর নাম কি?", "C", "ইসলামাবাদ", "নতুন দিল্লি", "কাঠমান্ডু", "কিংসাসা"));
                add(new QuestionModule("উত্তর কোরিয়ার রাজধানীর নাম কি?", "B", "জাকার্তা", "পিয়ংইয়াং", "সিউল", "তিরানা"));
                add(new QuestionModule("ওমানের রাজধানীর নাম কি?", "B", "রাবাত", "মাস্কাট", "দোহা", "হ্যানয়"));
                add(new QuestionModule("পাকিস্তানের রাজধানীর নাম কি?", "A", "ইসলামাবাদ", "বাগদাদ", "নতুন দিল্লি", "ঢাকা"));
                add(new QuestionModule("ফিলিস্তিনের রাজধানীর নাম কি?", "A", "ইস্ট জেরুজালেম", "বাগদাদ", "ত্রিপোলি", "দামেস্ক"));
                add(new QuestionModule("ফিলিপাইনের রাজধানীর নাম কি?", "C", "হ্যানয়", "কিয়েভ", "ম্যানিলা", "সিউল"));
                add(new QuestionModule("কাতারের রাজধানীর নাম কি?", "C", "কাতার সিটি", "বৈরুত", "দোহা", "ঢাকা"));
                add(new QuestionModule("সৌদি আরবের রাজধানীর নাম কি?", "C", "ইসলামাবাদ", "মক্কা", "রিয়াদ", "তেহরান"));
                add(new QuestionModule("সিঙ্গাপুরের রাজধানীর নাম কি?", "B", "ব্যাংকক", "সিঙ্গাপুর", "তাইপেই", "আবুধাবি"));
                add(new QuestionModule("দক্ষিণ কোরিয়ার রাজধানীর নাম কি?", "B", "বেইজিং", "সিউল", "টোকিও", "পিয়ংইয়াং"));
                add(new QuestionModule("শ্রীলঙ্কার রাজধানীর নাম কি?", "A", "শ্রী জয়ওয়ার্দেনেপুরা কোট্টে", "পিয়ংইয়াং", "কলম্বো", "নেপিডো"));
                add(new QuestionModule("সিরিয়ার রাজধানীর নাম কি?", "C", "ইসলামাবাদ", "বাগদাদ", "দামেস্ক", "জেরুজালেম"));
                add(new QuestionModule("তাইওয়ানের রাজধানীর নাম কি?", "D", "ত্রিপোলি", "তাইওয়ান সিটি", "তিরানা", "তাইপেই"));
                add(new QuestionModule("তিব্বতের রাজধানীর নাম কি?", "A", "লাসা", "কাঠমান্ডু", "থিম্পু", "তাইপেই"));
                add(new QuestionModule("তাজিকিস্তানের রাজধানীর নাম কি?", "D", "ইসলামাবাদ", "বাগদাদ", "নুর-সুলতান", "দুশানবে"));
                add(new QuestionModule("থাইল্যান্ডের রাজধানীর নাম কি?", "A", "ব্যাংকক", "দারুস সালাম", "কুয়ালালামপুর", "জাকার্তা"));
                add(new QuestionModule("তিমোর-লেস্তের রাজধানীর নাম কি?", "A", "দিলি", "টোকিও", "নতুন দিল্লি", "আঙ্কারা"));
                add(new QuestionModule("তুর্কমেনিস্তানের রাজধানীর নাম কি?", "C", "নুর-সুলতান", "আবুজা", "আশগাবাত", "কাবুল"));
                add(new QuestionModule("সংযুক্ত আরব আমিরাতের রাজধানীর নাম কি?", "B", "দুবাই", "আবুধাবি", "নতুন দিল্লি", "মাস্কাট"));
                add(new QuestionModule("উজবেকিস্তানের রাজধানীর নাম কি?", "C", "ইসলামাবাদ", "মস্কো", "তাশকেন্ত", "দুশানবে"));
                add(new QuestionModule("ভিয়েতনামের রাজধানীর নাম কি?", "C", "ম্যানিলা", "তিরানা", "হ্যানয়", "সিউল"));
                add(new QuestionModule("ইয়েমেনের রাজধানীর নাম কি?", "A", "সানা", "বাগদাদ", "দামেস্ক", "মাস্কাট"));


                add(new QuestionModule("আলবেনিয়ার রাজধানীর নাম কি?", "C", "ভিয়েনা", "মিনস্ক","তিরানা", "নিকোসিয়া"));


                add(new QuestionModule("অস্ট্রিয়ার রাজধানীর নাম কি?", "A", "ভিয়েনা", "অ্যান্ডোরা লা ভেল্লা","মিনস্ক", "ব্রাসেলস"));
                add(new QuestionModule("অ্যান্ডোরার রাজধানীর নাম কি?", "B", "সোফিয়া", "অ্যান্ডোরা লা ভেল্লা","তিরানা", "জাগ্রেব"));
                add(new QuestionModule("আর্মেনিয়ার রাজধানীর নাম কি?", "C", "বার্লিন", "ডাবলিন","ইয়েরেভান", "প্রিস্টিনা"));
                add(new QuestionModule("আজারবাইজানের রাজধানীর নাম কি?", "A", "বাকু", "সারাজেভো","আংকারা", "নিকোসিয়া"));
                add(new QuestionModule("বেলজিয়ামের রাজধানীর নাম কি?", "D", "ডাবলিন", "প্যারিস","জেনেভা", "ব্রাসেলস"));
                add(new QuestionModule("বেলারুশের রাজধানীর নাম কি?", "B", "কিয়েভ", "মিনস্ক","মস্কো", "বুদাপেস্ট"));
                add(new QuestionModule("বসনিয়া ও হার্জেগোভিনার রাজধানী কোনটি ?", "D", "তিরানা", "প্যারিস","আবুজা", "সারাজেভো"));
                add(new QuestionModule("বুলগেরিয়ার রাজধানী কোনটি?", "A", "সোফিয়া", "অসলো","নতুন দিল্লি", "হ্যানয়"));
                add(new QuestionModule("ক্রোয়েশিয়ার রাজধানী কোনটি?", "A", "জাগ্রেব", "ত্রিপোলি","ম্যানিলা", "ব্রাসেলস"));
                add(new QuestionModule("সাইপ্রাসের রাজধানী কোনটি?", "D", "ঢাকা", "প্যারিস","মিনস্ক", "নিকোসিয়া"));
                add(new QuestionModule("চেক প্রজাতন্ত্রের রাজধানী কোনটি?", "C", "হেলসিঙ্কি", "মস্কো","প্রাগ", "ইসলামাবাদ"));
                add(new QuestionModule("এস্তোনিয়ার রাজধানী কোনটি?", "B", "বার্লিন", "টালিন","এথেন্স", "কাবুল"));
                add(new QuestionModule("ডেনমার্কের রাজধানী কোনটি?", "D", "বার্লিন", "প্যারিস","লন্ডন", "কোপেনহেগেন"));
                add(new QuestionModule("ফিনল্যান্ডের রাজধানী কোনটি?", "D", "আংকারা", "বুদাপেস্ট","রিকজাভিক", "হেলসিঙ্কি"));
                add(new QuestionModule("ফ্রান্সের রাজধানী কোনটি?", "C", "রোম", "লন্ডন","প্যারিস", "বার্লিন"));
                add(new QuestionModule("জর্জিয়ার রাজধানী কোনটি?", "A", "তিবিলিসি", "রিয়াদ","ত্রিপোলি", "ক্যানবেরা"));
                add(new QuestionModule("জার্মানির রাজধানী কোনটি?", "A", "বার্লিন", "প্যারিস","লিসবন", "নুর সুলতান"));
                add(new QuestionModule("রোমানিয়ার রাজধানী কোনটি?", "B", "কিয়েভ", "বুখারেস্ট","মস্কো", "দোহা"));
                add(new QuestionModule("গ্রীসের রাজধানী কোনটি?", "C", "কায়রো", "বাগদাদ","এথেন্স", "রোম"));
                add(new QuestionModule("হাঙ্গেরির রাজধানী কোনটি?", "A", "বুদাপেস্ট", "ম্যানিলা","মোনাকো", "বুখারেস্ট"));
                add(new QuestionModule("আইসল্যান্ডের রাজধানী কোনটি?", "D", "ডাবলিন", "নতুন দিল্লি","সান মারিনো", "রিকজাভিক"));
                add(new QuestionModule("আয়ারল্যান্ডের রাজধানী কোনটি?", "C", "ত্রিপোলি", "আমস্টারডাম","ডাবলিন", "লিসবন"));
                add(new QuestionModule("ইতালির রাজধানী কোনটি?", "A", "রোম", "প্যারিস","ভ্যাটিকান সিটি", "ভেনিস"));
                add(new QuestionModule("কাজাখস্তানের রাজধানী কোনটি?", "C", "কাবুল", "তেহরান","নুর-সুলতান", "ইস্তানবুল"));
                add(new QuestionModule("কসোভোর রাজধানী কোনটি?", "B", "কাঠমান্ডু", "প্রিস্টিনা","ম্যানিলা", "তিরানা"));
                add(new QuestionModule("লাতভিয়ার রাজধানী কোনটি?", "A", "রিগা", "রিজা","ভিয়েনা", "হ্যানয়"));
                add(new QuestionModule("লিচেনস্টাইনের রাজধানী কোনটি?", "C", "মালে", "ভ্যালেটা","ভাদুজ", "মোনাকো"));
                add(new QuestionModule("লিথুয়ানিয়ার রাজধানী কোনটি?", "A", "ভিলনিয়াস", "আমস্টারডাম","আলজিয়ার্স", "লুয়ান্ডা"));
                add(new QuestionModule("লুক্সেমবার্গের রাজধানী কোনটি?", "C", "ব্রাসেলস", "ওয়াগাডুগু","লুক্সেমবার্গ সিটি", "বার্ন"));
                add(new QuestionModule("মাল্টার রাজধানী কোনটি?", "C", "ওয়ারশ", "পডগরিকা","ভ্যালেটা", "মাদ্রিদ"));
                add(new QuestionModule("মলদোভার রাজধানী কোনটি?", "B", "ত্রিপোলি", "চিসিনাউ","কিনশাসা", "মালাবো"));
                add(new QuestionModule("মোনাকোর রাজধানী কোনটি?", "B", "কায়রো", "মোনাকো","ওয়ারশ", "অসলো"));
                add(new QuestionModule("মন্টিনিগ্রোর রাজধানী কোনটি?", "D", "বার্ন", "আমস্টারডাম","ঢাকা", "পডগরিকা"));
                add(new QuestionModule("নেদারল্যান্ডসের রাজধানী কোনটি?", "B", "থিম্পু", "আমস্টারডাম","স্টকহোম", "পোর্ট লুইস"));
                add(new QuestionModule("উত্তর মেসিডোনিয়ার রাজধানী কোনটি?", "C", "রাবাত", "কিয়েভ","স্কোপিয়ে", "আংকারা"));
                add(new QuestionModule("নরওয়ের রাজধানী কোনটি?", "A", "অসলো", "কায়রো","নতুন দিল্লি", "মস্কো"));
                add(new QuestionModule("পোল্যান্ডের রাজধানী কোনটি?", "A", "ওয়ারশ", "কিয়েভ","মোনাকো", "কাজান"));
                add(new QuestionModule("পর্তুগালের রাজধানী কোনটি?", "D", "রাবাত", "আমস্টারডাম","অটোয়া", "লিসবন"));
                add(new QuestionModule("রাশিয়ার রাজধানীর নাম কোনটি?", "C", "বেইজিং", "আংকারা","মস্কো", "মানামা"));
                add(new QuestionModule("সান মারিনোর রাজধানীর নাম কি?", "B", "মোনাকো", "সান মারিনো","জাকার্তা", "ম্যানিলা"));
                add(new QuestionModule("সার্বিয়ার রাজধানীর নাম কি?", "C", "বুদাপেস্ট", "পানামা সিটি","বেলগ্রেড", "বৈরুত"));
                add(new QuestionModule("স্লোভাকিয়ার রাজধানীর নাম কি?", "A", "ব্রাটিস্লাভা", "মোনাকো","বেলগ্রেড", "সান সালভাদর"));
                add(new QuestionModule("স্লোভেনিয়ার রাজধানীর নাম কি?", "C", "থিম্পু", "বৈরুত","লুব্লিয়ানা", "উলানবাটার"));
                add(new QuestionModule("স্পেনের রাজধানীর নাম কি?", "C", "ত্রিপোলি", "আমস্টারডাম","মাদ্রিদ", "লিসবন"));
                add(new QuestionModule("সুইডেনের রাজধানীর নাম কি?", "A", "স্টকহোম", "ব্রাসেলস","ম্যানিলা", "সেন্ট জর্জেস"));
                add(new QuestionModule("সুইজারল্যান্ডের রাজধানীর নাম কি?", "A", "ব্রাসেলস", "ইসলামাবাদ","ডাবলিন", "লিসবন"));
                add(new QuestionModule("তুরস্কের রাজধানীর নাম কি?", "D", "কাবুল", "ইস্তানবুল","কারাকাস", "আংকারা"));
                add(new QuestionModule("ইউক্রেনের রাজধানীর নাম কি?", "C", "ত্রিপোলি", "ওয়ারশ","কিয়েভ", "কাজান"));
                add(new QuestionModule("যুক্তরাজ্যের রাজধানীর নাম কি?", "C", "প্যারিস", "ওয়াশিংটন ডিসি","লন্ডন", "কিংস্টন"));
                add(new QuestionModule("ভ্যাটিকান সিটির রাজধানীর নাম কি?", "C", "ঢাকা", "রোম","ভ্যাটিকান সিটি", "রাবাত"));
                add(new QuestionModule("আয়ারল্যান্ডের রাজধানীর নাম কি?", "C", "ত্রিপোলি", "আমস্টারডাম","ডাবলিন", "লিসবন"));



                add(new QuestionModule("এন্টিগুয়া এবং বারবুডার রাজধানীর নাম কোনটি?", "B", "বার্লিন", "সেন্ট জনস", "ওয়ারশ", "কারাকাস"));
                add(new QuestionModule("বাহামাসের রাজধানীর নাম কোনটি?", "A", "নাসাউ", "ব্রিজটাউন", "টোকিও", "কারাকাস"));
                add(new QuestionModule("বার্বাডোসের রাজধানীর নাম কোনটি?", "B", "হনিয়ারা", "ব্রিজটাউন", "সুক্রে", "ল্যুব্লিয়ানা"));
                add(new QuestionModule("বেলিজের রাজধানীর নাম কোনটি?", "C", "কাম্পালা", "মিন্সক", "বেলমোপান", "হ্যানয়"));
                add(new QuestionModule("কানাডার রাজধানীর নাম কোনটি?", "D", "মস্কো", "প্যারিস", "বেইজিং", "অটোয়া"));
                add(new QuestionModule("কোস্টা রিকার রাজধানীর নাম কোনটি?", "C", "কেপ টাউন", "ডাবলিন", "সান হোসে", "বেলগ্রেড"));
                add(new QuestionModule("কিউবার রাজধানীর নাম কোনটি?", "A", "হাভানা", "স্টকহোম", "সিওল", "পডগরিকা"));
                add(new QuestionModule("ডোমিনিকার রাজধানীর নাম কোনটি?", "A", "রোজো", "লিসবন", "চিসিনাউ", "বুখারেস্ট"));
                add(new QuestionModule("ডোমিনিকান প্রজাতন্ত্রের রাজধানীর নাম কোনটি?", "D", "অসলো", "আংকারা", "জাকার্তা", "সান্টো ডোমিংগো"));
                add(new QuestionModule("এল সালভাদরের রাজধানীর নাম কোনটি?", "B", "সেন্ট জনস", "সান সালভাদর", "আমস্টারডাম", "ভ্যালেটা"));
                add(new QuestionModule("গ্রেনাডার রাজধানী কোনটি?", "B", "সেন্ট জনস", "সেন্ট জর্জস", "হেলসিঙ্কি", "এথেন্স"));
                add(new QuestionModule("গুয়াতেমালার রাজধানী কোনটি?", "D", "রিকজাভিক", "সান সালভাদর", "আমস্টারডাম", "গুয়াতেমালা সিটি"));
                add(new QuestionModule("হাইতির রাজধানী কোনটি?", "B", "হ্যানয়", "পোর্ট-অ-প্রিন্স", "আম্মান", "প্রিস্টিনা"));
                add(new QuestionModule("হন্ডুরাসের রাজধানী কোনটি?", "C", "ম্যানিলা", "পোর্ট-অ-প্রিন্স", "টেগুসিগালপা", "হাভানা"));
                add(new QuestionModule("জামাইকার রাজধানী কোনটি?", "A", "কিংস্টন", "বুয়েনোস আইরেস", "বোগোটা", "তিরানা"));
                add(new QuestionModule("মেক্সিকোর রাজধানী কোনটি?", "B", "অটোয়া", "মেক্সিকো সিটি", "মস্কো", "বার্লিন"));
                add(new QuestionModule("নিকারাগুয়ার রাজধানীর নাম কি?", "D", "নূর-সুলতান", "সান হোসে", "প্যারিস", "মানাগুয়া"));
                add(new QuestionModule("পানামার রাজধানীর নাম কি?", "B", "অটোয়া", "পানামা সিটি", "রাবাত", "ব্যাংকক"));
                add(new QuestionModule("সেন্ট কিটস এবং নেভিসের রাজধানীর নাম কি?", "A", "বাসেটের", "মাজুরো", "এন'জামেনা", "ক্যানবেরা"));
                add(new QuestionModule("সেন্ট লুসিয়ার রাজধানীর নাম কি?", "D", "নিউ দিল্লি", "লাসা", "কাঠমান্ডু", "ক্যাসট্রিস"));
                add(new QuestionModule("সেন্ট ভিনসেন্ট এবং গ্রেনাডাইনের রাজধানীর নাম কি?", "D", "সান মারিনো", "সান সালভাদর", "সেন্ট জনস", "কিংস্টাউন"));
                add(new QuestionModule("ত্রিনিদাদ এবং টোবাগোর রাজধানীর নাম কি?", "C", "মাদ্রিদ", "পডগরিকা", "পোর্ট অফ স্পেন", "ব্রাটিস্লাভা"));
                add(new QuestionModule("মার্কিন যুক্তরাষ্ট্রের রাজধানীর নাম কি?", "A", "ওয়াশিংটন ডিসি", "লন্ডন", "নিউ ইয়র্ক", "স্টকহোম"));
                add(new QuestionModule("আলজেরিয়ার রাজধানী কোনটি?", "B", "মরোনি", "আলজিয়ার্স", "কাম্পালা", "মিনস্ক"));
                add(new QuestionModule("অ্যাঙ্গোলার রাজধানী কোনটি?", "B", "সোরোটি", "লুয়ান্ডা", "মালাবো", "ওয়ারশ"));
                add(new QuestionModule("বেনিনের রাজধানী কোনটি?", "C", "বিসাউ", "আলজিয়ার্স", "পোর্টো-নোভো", "আংকারা"));
                add(new QuestionModule("বতসোয়ানার রাজধানী কোনটি?", "C", "ভ্যাটিকান সিটি", "বুয়েনোস আইরেস", "গাবরোন", "মোনাকো"));
                add(new QuestionModule("বুরকিনা ফাসোর রাজধানী কোনটি?", "D", "ক্যানবেরা", "বুয়েনোস আইরেস", "গাবরোন", "ওয়াগাডুগু"));
                add(new QuestionModule("বুরুন্ডির রাজধানী কোনটি?", "A", "গিতেগা", "তিরানা", "কাম্পালা", "পোর্টো-নোভো"));
                add(new QuestionModule("কাবো ভের্দের রাজধানী কোনটি?", "D", "প্যারিস", "মাস্কাট", "এন'জামেনা", "প্রাইয়া"));
                add(new QuestionModule("ক্যামেরুনের রাজধানী কোনটি?", "B", "কিয়েভ", "ইয়াউন্ডে", "মালাবো", "বুদাপেস্ট"));
                add(new QuestionModule("মধ্য আফ্রিকান প্রজাতন্ত্রের রাজধানী কোনটি?", "B", "নিউ দিল্লি", "বানগুই", "মরোনি", "ত্রিপোলি"));
                add(new QuestionModule("মিশরের রাজধানী কোনটি?", "B", "বাগদাদ", "কায়রো", "আদ্দিস আবাবা", "দামেস্ক"));
                add(new QuestionModule("চাদের রাজধানী কোনটি?", "A", "এন'জামেনা", "কারাকাস", "আদ্দিস আবাবা", "আবুজা"));
                add(new QuestionModule("কমোরোসের রাজধানী কোনটি?", "A", "মরোনি", "মোনাকো", "রাবাত", "কারাকাস"));
                add(new QuestionModule("কঙ্গোর গণতান্ত্রিক প্রজাতন্ত্রের রাজধানী কোনটি?", "A", "কিনশাসা", "কায়রো", "ব্রাজাভিল", "বামাকো"));
                add(new QuestionModule("কঙ্গোর প্রজাতন্ত্রের রাজধানীর নাম কি?", "C", "কিনশাসা", "কায়রো", "ব্রাজাভিল", "বামাকো"));
                add(new QuestionModule("মিশরের রাজধানীর নাম কি?", "B", "বাগদাদ", "কায়রো", "আদ্দিস আবাবা", "দামেস্ক"));
                add(new QuestionModule("জিবুতির রাজধানীর নাম কি?", "D", "বাগদাদ", "সিডনি", "আদ্দিস আবাবা", "জিবুতি"));
                add(new QuestionModule("ইকুয়েটোরিয়াল গিনির রাজধানীর নাম কি?", "A", "মালাবো", "আস্মারা", "আস্তানা", "কাঠমান্ডু"));
                add(new QuestionModule("ইরিত্রিয়ার রাজধানীর নাম কি?", "B", "আবুজা", "আস্মারা", "আস্তানা", "কাঠমান্ডু"));
                add(new QuestionModule("ইসওয়াতিনির (সোয়াজিল্যান্ড) রাজধানীর নাম কি?", "C", "এমবাবানে", "নিউ দিল্লি", "আদ্দিস আবাবা", "কিয়েভ"));
                add(new QuestionModule("ইথিওপিয়ার রাজধানীর নাম কি?", "C", "ডাবলিন", "ইসলামাবাদ", "আদ্দিস আবাবা", "ভিয়েনা"));
                add(new QuestionModule("গ্যাবনের রাজধানী কি?", "D", "মোনাকো", "সিউল", "আস্তানা", "লিব্রেভিল"));
                add(new QuestionModule("গাম্বিয়ার রাজধানী কি?", "D", "ম্যানিলা", "বোগোটা", "আস্তানা", "বানজুল"));
                add(new QuestionModule("ঘানার রাজধানীর নাম কি?", "D", "তিরানা", "লাসা", "কায়রো", "আক্রা"));
                add(new QuestionModule("গিনির রাজধানীর নাম কি?", "A", "কোনাক্রি", "মালে", "ওয়ারশ", "কায়রো"));
                add(new QuestionModule("গিনি-বিসাউয়ের রাজধানীর নাম কি?", "A", "বিসাউ", "কাম্পালা", "নাইরোবি", "রাবাত"));
                add(new QuestionModule("কেনিয়ার রাজধানীর নাম কি?", "C", "বিসাউ", "কাম্পালা", "নাইরোবি", "মাস্কাট"));
                add(new QuestionModule("আইভরি কোস্টের রাজধানীর নাম কি?", "B", "বিসাউ", "ইয়ামোসুক্রো", "সোরোটি", "রাবাত"));
                add(new QuestionModule("লেসোথোর রাজধানীর নাম কি?", "D", "মস্কো", "নিউ দিল্লি", "আমস্টারডাম", "মাসেরু"));
                add(new QuestionModule("লাইবেরিয়ার রাজধানীর নাম কি?", "C", "তিরানা", "হ্যানয়", "মনরোভিয়া", "ওয়েলিংটন"));
                add(new QuestionModule("লিবিয়ার রাজধানীর নাম কি?", "B", "তেহরান", "ত্রিপোলি", "প্যারিস", "মাস্কাট"));
                add(new QuestionModule("মাদাগাস্কারের রাজধানীর নাম কি?", "A", "অ্যান্টানানারিভো", "পোর্ট লুই", "বামাকো", "উলানবাটার"));
                add(new QuestionModule("মালাউইর রাজধানীর নাম কি?", "A", "লিলংগওয়ে", "ত্রিপোলি", "আদ্দিস আবাবা", "দামেস্ক"));
                add(new QuestionModule("মালির রাজধানীর নাম কি?", "B", "মালে", "বামাকো", "আস্তানা", "নাইরোবি"));
                add(new QuestionModule("মরিতানিয়ার রাজধানীর নাম কি?", "C", "ভেনিস", "পোর্ট লুই", "নুওয়াকশট", "দামেস্ক"));
                add(new QuestionModule("মরিশাসের রাজধানীর নাম কি?", "B", "কলম্বো", "পোর্ট লুই", "নুওয়াকশট", "বুদাপেস্ট"));
                add(new QuestionModule("মরক্কোর রাজধানীর নাম কি?", "A", "রাবাত", "মাস্কাট", "আদ্দিস আবাবা", "বার্লিন"));
                add(new QuestionModule("মোজাম্বিকের রাজধানীর নাম কি?", "D", "কাম্পালা", "এন'জামেনা", "আবুজা", "মাপুটো"));
                add(new QuestionModule("নামিবিয়ার রাজধানীর নাম কি?", "D", "প্যারামারিবো", "ডাকার", "সান্তিয়াগো", "উইন্ডহোক"));
                add(new QuestionModule("নাইজারের রাজধানীর নাম কি?", "C", "প্যারামারিবো", "সান সালভাদর", "নিয়ামে", "উইন্ডহোক"));
                add(new QuestionModule("নাইজেরিয়ার রাজধানীর নাম কি?", "B", "অসলো", "আবুজা", "নূর-সুলতান", "রিগা"));
                add(new QuestionModule("রুয়ান্ডার রাজধানীর নাম কি?", "A", "কিগালি", "লিসবন", "বার্ন", "ওয়ারশ"));
                add(new QuestionModule("সাও টোমে ও প্রিন্সিপের রাজধানীর নাম কি?", "D", "বোগোটা", "আংকারা", "ভাদুজ", "সাও টোমে"));
                add(new QuestionModule("সেনেগালের রাজধানীর নাম কি?", "B", "মোনাকো", "ডাকার", "অসলো", "দোদোমা"));
                add(new QuestionModule("সেশেলসের রাজধানীর নাম কি?", "C", "সান মারিনো", "নিউ দিল্লি", "ভিক্টোরিয়া", "মানামা"));
                add(new QuestionModule("সিয়েরা লিওনের রাজধানীর নাম কি?", "C", "সিয়েরা লিওন", "সান্তিয়াগো", "ফ্রিটাউন", "কেপটাউন"));
                add(new QuestionModule("সোমালিয়ার রাজধানীর নাম কি?", "A", "মোগাদিশু", "ইসলামাবাদ", "নিকোসিয়া", "পিয়ংইয়ং"));
                add(new QuestionModule("দক্ষিণ আফ্রিকার রাজধানীর নাম কি?", "A", "প্রিটোরিয়া", "কেপটাউন", "বুদাপেস্ট", "ক্যানবেরা"));
                add(new QuestionModule("দক্ষিণ সুদানের রাজধানীর নাম কি?", "D", "ইসলামাবাদ", "ইয়েরেভান", "আদ্দিস আবাবা", "জুবা"));
                add(new QuestionModule("সুদানের রাজধানীর নাম কি?", "A", "খার্তুম", "ইয়েরেভান", "জাগরেব", "জুবা"));
                add(new QuestionModule("তানজানিয়ার রাজধানীর নাম কি?", "B", "মিনস্ক", "দোদোমা", "ফনম পেন", "ভিয়েনতিয়েন"));
                add(new QuestionModule("টোগোর রাজধানীর নাম কি?", "C", "দুশানবে", "আশখাবাদ", "লোমে", "সারায়েভো"));
                add(new QuestionModule("তিউনিসিয়ার রাজধানীর নাম কি?", "C", "দুশানবে", "আশখাবাদ", "তিউনিস", "সারায়েভো"));
                add(new QuestionModule("উগান্ডার রাজধানীর নাম কি?", "A", "কাম্পালা", "মরোনি", "সোরোটি", "প্রিস্টিনা"));
                add(new QuestionModule("জাম্বিয়ার রাজধানীর নাম কি?", "A", "লুসাকা", "নিকোসিয়া", "বুখারেস্ট", "ব্রিজেটাউন"));
                add(new QuestionModule("জিম্বাবুয়ের রাজধানীর নাম কি?", "C", "হাভানা", "কিংস্টন", "হারারে", "ক্যাসট্রিস"));
                add(new QuestionModule("অস্ট্রেলিয়ার রাজধানী কোনটি?", "A", "ক্যানবেরা", "সিডনি", "ম্যানিলা", "ওয়েলিংটন"));
                add(new QuestionModule("ফিজির রাজধানী কোনটি?", "D", "কায়রো", "মালে", "আপিয়া", "সুভা"));
                add(new QuestionModule("কিরিবাতির রাজধানী কোনটি?", "B", "হ্যানয়", "তারাওয়া", "ঢাকা", "ত্রিপোলি"));
                add(new QuestionModule("মার্শাল দ্বীপপুঞ্জের রাজধানী কোনটি?", "A", "মাজুরো", "ম্যানিলা", "নতুন দিল্লি", "কায়রো"));
                add(new QuestionModule("মাইক্রোনেশিয়ার রাজধানী কোনটি?", "A", "পালিকির", "আপিয়া", "ইয়ারেন", "মাইক্রোনেশিয়া"));
                add(new QuestionModule("নাউরুর রাজধানী কোনটি?", "B", "তেহরান", "ইয়ারেন", "ফুনাফুতি", "পোর্ট ভিলা"));
                add(new QuestionModule("নিউজিল্যান্ডের রাজধানী কোনটি?", "C", "আমস্টারডাম", "ক্যানবেরা", "ওয়েলিংটন", "তিরানা"));
                add(new QuestionModule("পালাউয়ের রাজধানী কোনটি?", "B", "মাজুরো", "নগেরুলমুদ", "ওয়ারশ", "সিডনি"));
                add(new QuestionModule("পাপুয়া নিউ গিনির রাজধানীর নাম কী?", "A", "পোর্ট মোর্সবি", "অটাওয়া", "ওয়েলিংটন", "হনিয়ারা"));
                add(new QuestionModule("সামোয়ার রাজধানীর নাম কী?", "D", "বোগোটা", "বেইজিং", "ডাবলিন", "আপিয়া"));
                add(new QuestionModule("সলোমন দ্বীপপুঞ্জের রাজধানীর নাম কী?", "D", "বোগোটা", "আসুনসিওন", "ব্রাসিলিয়া", "হনিয়ারা"));
                add(new QuestionModule("টোঙ্গার রাজধানীর নাম কোনটি?", "D", "মন্টেভিডিও", "ইসলামাবাদ", "কিংস্টন", "নুকুʻআলোফা"));
                add(new QuestionModule("তুভালুর রাজধানীর নাম কী?", "C", "ঢাকা", "মাস্কাট", "ফুনাফুতি", "মস্কো"));
                add(new QuestionModule("ভানুয়াতুর রাজধানীর নাম কী?", "C", "বুদাপেস্ট", "ব্রিজটাউন", "পোর্ট ভিলা", "বেলমোপান"));


                add(new QuestionModule("আর্জেন্টিনার রাজধানীর নাম কোনটি?", "D", "ব্রাসিলিয়া", "বার্লিন", "লা পাজ", "বুয়েনস আইরেস"));

                add(new QuestionModule("ব্রাজিলের রাজধানীর নাম কোনটি?", "A", "ব্রাসিলিয়া", "কিংস্টাউন", "লা পাজ", "বুয়েনস আইরেস"));
                add(new QuestionModule("বলিভিয়ার রাজধানীর নাম কোনটি?", "C", "বোগোটা", "মানাগুয়া", "লা পাজ", "মানামা"));
                add(new QuestionModule("চিলির রাজধানীর নাম কোনটি?", "B", "পোর্ট মোরসবাই", "সান্তিয়াগো", "এথেন্স", "সেন্ট জর্জেস"));
                add(new QuestionModule("কলম্বিয়ার রাজধানীর নাম কোনটি?", "D", "আসুনসিওন", "নিউ ওয়ার্ক", "হনিয়ারা", "বোগোটা"));
                add(new QuestionModule("ইকুয়েডরের রাজধানীর নাম কোনটি?", "D", "বেইজিং", "এনগেরুলমুদ", "মস্কো", "কুইটো"));
                add(new QuestionModule("গায়ানার রাজধানীর নাম কী?", "A", "জর্জটাউন", "অপিয়া", "হাভানা", "পালিকির"));
                add(new QuestionModule("প্যারাগুয়ের রাজধানীর নাম কী?", "B", "মন্টেভিডিও", "আসুনসিওন", "কেপ টাউন", "বুয়েনস আইরেস"));
                add(new QuestionModule("পেরুর রাজধানীর নাম কী?", "C", "ম্যানিলা", "ডাকা", "লিমা", "রাবাত"));
                add(new QuestionModule("সুরিনামের রাজধানীর নাম কী?", "A", "পারামারিবো", "তেগুসিগালপা", "এনগেরুলমুদ", "সুভা"));
                add(new QuestionModule("উরুগুয়ের রাজধানীর নাম কী?", "A", "মন্টেভিডিও", "বাসেটার", "পানামা সিটি", "পোর্ট ভিলা"));
                add(new QuestionModule("ভেনেজুয়েলার রাজধানীর নাম কী?", "C", "ব্রাসেলস", "মাদ্রিদ", "কারাকাস", "সান্টো ডোমিংগো"));

            }
        };
        QuestionModule.createQuestionsForSubject("রাজধানী", R.drawable.capital, questions);

        //------------- Subject 4-----------------------
        //=================== Currency Name =======================
        questions = new ArrayList(){
            {
                add(new QuestionModule("আফগানিস্তান-এর মুদ্রার নাম কী?", "A", "আফগানি (AFN)", "রুপি (INR)", "ডলার (USD)", "ইউরো (EUR)"));
                add(new QuestionModule("আলবেনিয়া-এর মুদ্রার নাম কী?", "A", "লেক (ALL)", "ইউরো (EUR)", "আফগানি (AFN)", "পাউন্ড (GBP)"));
                add(new QuestionModule("আলজেরিয়া-এর মুদ্রার নাম কী?", "A", "দিনার (DZD)", "আফগানি (AFN)", "ডলার (USD)", "পাউন্ড (GBP)"));
                add(new QuestionModule("আমেরিকা এর মুদ্রার নাম কী?", "A", "মার্কিন ডলার (USD)", "ইউরো (EUR)", "পাউন্ড (GBP)", "ফ্রাঙ্ক (CHF)"));
                add(new QuestionModule("অ্যান্ডোরা-এর মুদ্রার নাম কী?", "A", "ইউরো (EUR)", "ডলার (USD)", "পেসো (ARS)", "ইয়েন (JPY)"));
                add(new QuestionModule("অ্যাঙ্গোলা-এর মুদ্রার নাম কী?", "B", "ইউরো (EUR)", "কওয়াঞ্জা (AOA)", "ডলার (USD)", "পাউন্ড (GBP)"));
                add(new QuestionModule("অ্যান্টার্কটিকা-এর মুদ্রার নাম কী?", "C", "কোনও অফিসিয়াল মুদ্রা নেই", "ইউরো (EUR)", "ডলার (USD)", "পাউন্ড (GBP)"));
                add(new QuestionModule("অ্যান্টিগুয়া এবং বারবুডা-এর মুদ্রার নাম কী?", "A", "ইস্টার্ন ক্যারিবিয়ান ডলার (XCD)", "ইউরো (EUR)", "ডলার (USD)", "পাউন্ড (GBP)"));
                add(new QuestionModule("আর্জেন্টিনা-এর মুদ্রার নাম কী?", "B", "কওয়াঞ্জা (AOA)", "পেসো (ARS)", "ডলার (USD)", "পাউন্ড (GBP)"));
                add(new QuestionModule("আর্মেনিয়া-এর মুদ্রার নাম কী?", "C", "লেক (ALL)", "ইউরো (EUR)", " ড্রাম (AMD)", "পাউন্ড (GBP)"));
                add(new QuestionModule("অরুবা-এর মুদ্রার নাম কী?", "D", "লেক (ALL)", "ইউরো (EUR)", "ডলার (USD)", " ফ্লোরিন (AWG)"));
                add(new QuestionModule("অস্ট্রেলিয়া-এর মুদ্রার নাম কী?", "A", "অস্ট্রেলিয়ান ডলার (AUD)", "ইউরো (EUR)", "মার্কিন ডলার (USD)", "পাউন্ড (GBP)"));
                add(new QuestionModule("অস্ট্রিয়া-এর মুদ্রার নাম কী?", "B", " ফ্লোরিন (AWG)", "ইউরো (EUR)", "পেসো (ARS)", "ইয়েন (JPY)"));
                add(new QuestionModule("আজারবাইজান-এর মুদ্রার নাম কী?", "C", "আফগানি (AFN)", "ইউরো (EUR)", "মানাত (AZN)", "পাউন্ড (GBP)"));
                add(new QuestionModule("বাহামা-এর মুদ্রার নাম কী?", "D", " ডলার (USD)", "ইউরো (EUR)", "বাহামাস ডলার (BSD)", " ফ্লোরিন (AWG)"));
                add(new QuestionModule("বাহরাইন-এর মুদ্রার নাম কী?", "A", "দিনার (BHD)", "ইউরো (EUR)", "ডলার (USD)", "পাউন্ড (GBP)"));
                add(new QuestionModule("বাংলাদেশ-এর মুদ্রার নাম কী?", "A", "টাকা (BDT)", "আফগানি (AFN)", "ডলার (USD)", "রুপি (INR)"));
                add(new QuestionModule("বার্বাডোস-এর মুদ্রার নাম কী?", "C", "ফ্লোরিন (AWG)", "ইউরো (EUR)", "বার্বাডোস ডলার (BBD)", "পাউন্ড (GBP)"));
                add(new QuestionModule("বেলারুশ-এর মুদ্রার নাম কী?", "D", "লেক (ALL)", "ইউরো (EUR)", "ডলার (USD)", "রুবেল (BYN)"));
                add(new QuestionModule("বেলজিয়াম-এর মুদ্রার নাম কী?", "C", "রুবেল (BYN)", "ডলার (USD)", "ইউরো (EUR)", "ইয়েন (JPY)"));
                add(new QuestionModule("বেলিজ-এর মুদ্রার নাম কী?", "A", "বেলিজ ডলার (BZD)", "ইউরো (EUR)", "ডলার (USD)", "পাউন্ড (GBP)"));
                add(new QuestionModule("বেনিন-এর মুদ্রার নাম কী?", "A", "সিএফএ ফ্রাঙ্ক (XOF)", "ইউরো (EUR)", "ডলার (USD)", "পাউন্ড (GBP)"));
                add(new QuestionModule("ভারত-এর মুদ্রার নাম কী?", "D", "টাকা (BDT)", "ইউরো (EUR)", "ডলার (USD)", "রুপি (INR)"));
                add(new QuestionModule("ভুটান-এর মুদ্রার নাম কী?", "D", "ফ্লোরিন (AWG)", "লেক (ALL)", "ডলার (USD)", "নুল্ট্রাম (BTN)"));
                add(new QuestionModule("বলিভিয়া-এর মুদ্রার নাম কী?", "B", "ইউরো (EUR)", "বলিভিয়ানো (BOB)", "ডলার (USD)", "পাউন্ড (GBP)"));
                add(new QuestionModule("বসনিয়া এবং হার্জেগোভিনা-এর মুদ্রার নাম কী?", "C", "লেক (ALL)", "ইউরো (EUR)", "মার্ক (BAM)", "পাউন্ড (GBP)"));

                add(new QuestionModule("বতসোয়ানা-এর মুদ্রার নাম কী?", "D", "বলিভিয়ানো (BOB)", "ইউরো (EUR)", "ডলার (USD)", "পুলা (BWP)"));
                add(new QuestionModule("ব্রাজিল-এর মুদ্রার নাম কী?", "D", "মার্ক (BAM)", "ইউরো (EUR)", "ব্রাজিলিয়ান ডলার (USD)", "ব্রাজিলিয়ান রিয়েল (BRL)"));
                add(new QuestionModule("ব্রুনাই-এর মুদ্রার নাম কী?", "A", "ব্রুনাই ডলার (BND)", "ইউরো (EUR)", "ডলার (USD)", "ব্রুনাই ইয়েন (JPY)"));
                add(new QuestionModule("বুলগেরিয়া-এর মুদ্রার নাম কী?", "C", "রিয়েল (BRL)", "ইউরো (EUR)", "লেভ (BGN)", "পাউন্ড (GBP)"));
                add(new QuestionModule("বুর্কিনা ফাসো-এর মুদ্রার নাম কী?", "D", "সিএফএ পাউন্ড (GBP) ", "ইউরো (EUR)", "সিএফএ ডলার (USD)", "সিএফএ ফ্রাঙ্ক (XOF)"));
                add(new QuestionModule("বুরুন্ডি-এর মুদ্রার নাম কী?", "B", "মার্ক (BAM)", "ফ্রাঙ্ক (BIF)", "ডলার (USD)", "পাউন্ড (GBP)"));
                add(new QuestionModule("ক্যাম্বোডিয়া-এর মুদ্রার নাম কী?", "A", "রিয়েল (KHR)", "ইউরো (EUR)", "ডলার (USD)", "সিএফএ পাউন্ড (GBP)"));
                add(new QuestionModule("ক্যামেরুন-এর মুদ্রার নাম কী?", "A", " ফ্রাঙ্ক (XAF)", "ইউরো (EUR)", "ডলার (USD)", "পাউন্ড (GBP)"));
                add(new QuestionModule("কানাডা-এর মুদ্রার নাম কী?", "B", "কানাডিয়ান রিয়েল (BRL)", " কানাডিয়ান ডলার (CAD)", "ডলার (USD)", "কানাডিয়ান  পাউন্ড (GBP)"));
                add(new QuestionModule("মধ্য আফ্রিকান প্রজাতন্ত্র-এর মুদ্রার নাম কী?", "A", "সিএফএ ফ্রাঙ্ক (XAF)", "ইউরো (EUR)", " সিএফএ ডলার (USD)", "সিএফএ পাউন্ড (GBP)"));
                add(new QuestionModule("চাদ-এর মুদ্রার নাম কী?", "D", "বলিভিয়ানো (BOB)", "ইউরো (EUR)", "সিএফএ ডলার (USD)", "এফএ ফ্রাঙ্ক (XAF)"));
                add(new QuestionModule("চিলি-এর মুদ্রার নাম কী?", "B", "ইউরো (EUR)","পেসো (CLP)", "ডলার (USD)", "সিএফএ পাউন্ড (GBP)"));
                add(new QuestionModule("চীন-এর মুদ্রার নাম কী?", "B", "ইউরো (EUR)","ইউয়ান (CNY)", "ডলার (USD)", "সিএফএ পাউন্ড (GBP)"));
                add(new QuestionModule("কলম্বিয়া-এর মুদ্রার নাম কী?", "B",  "ইউরো (EUR)","পেসো (COP)", "ডলার (USD)", "পাউন্ড (GBP)"));
                add(new QuestionModule("কঙ্গো গণতান্ত্রিক প্রজাতন্ত্র-এর মুদ্রার নাম কী?", "A", "সিএফএ ফ্রাঙ্ক (CDF)", "ইউরো (EUR)", "সিএফএ ডলার (USD)", "সিএফএ পাউন্ড (GBP)"));
                add(new QuestionModule("কোস্টা রিকা-এর মুদ্রার নাম কী?", "C", "ইউরো (EUR)", "ইউয়ান (CNY)"," কোলন (CRC)", "পাউন্ড (GBP)"));
                add(new QuestionModule("ক্রোয়েশিয়া-এর মুদ্রার নাম কী?", "C",  "ইউরো (EUR)", "ডলার (USD)"," কুনা (HRK)", "কোলন (CRC)"));

                add(new QuestionModule("কিউবা-এর মুদ্রার নাম কী?", "C", "কোলন (CRC)", "ডলার (USD)","পেসো (CUP)", "পাউন্ড (GBP)"));
                add(new QuestionModule("কুরাকাও-এর মুদ্রার নাম কী?", "B", "ইউয়ান (CNY)"," গিল্ডার (ANG)","ডলার (USD)", "পাউন্ড (GBP)"));
                add(new QuestionModule("সাইপ্রাস-এর মুদ্রার নাম কী?", "C", "পেসো (ARS)", "ডলার (USD)", "ইউরো (EUR)", "ইয়েন (JPY)"));
                add(new QuestionModule("চেক প্রজাতন্ত্র-এর মুদ্রার নাম কী?", "A ", "কোরুনা (CZK)", "ইউরো (EUR)", "গিল্ডার (ANG)", "পাউন্ড (GBP)"));
                add(new QuestionModule("ডেনমার্ক-এর মুদ্রার নাম কী?", "A", "ক্রোন (DKK)", "ইউরো (EUR)", "ডলার (USD)", "পাউন্ড (GBP)"));


                add(new QuestionModule("জিবুতি-এর মুদ্রার নাম কী?", "B", "গিল্ডার (ANG)","ফ্রাঙ্ক (DJF)",  "কোলন (CRC)", "পাউন্ড (GBP)"));
                add(new QuestionModule("ডোমিনিকা-এর মুদ্রার নাম কী?", "B", "ইউরো (EUR)","ক্যারিবিয়ান ডলার (XCD)",  "ডলার (USD)", "পাউন্ড (GBP)"));
                add(new QuestionModule("ডোমিনিকান প্রজাতন্ত্র-এর মুদ্রার নাম কী?", "B", "কোরুনা (CZK)"," ডোমিনিকান  পেসো (DOP)", "গিল্ডার (ANG)",  "ইয়েন (JPY)"));
                add(new QuestionModule("ইকুয়েডর-এর মুদ্রার নাম কী?", "C",  "ইউরো (EUR)", "পেসো (ARS)","মার্কিন ডলার (USD)", "ইয়েন (JPY)"));
                add(new QuestionModule("মিশর-এর মুদ্রার নাম কী?", "B", "ইয়েন (JPY)","মিশরীয় পাউন্ড (EGP)", "মিশরীয় ডলার (USD)", "মার্ক (BAM)"));
                add(new QuestionModule("এল সালভাদর-এর মুদ্রার নাম কী?", "B", "ইউরো (EUR)","মার্কিন ডলার (USD)", "কোলন (CRC)", "গিল্ডার (ANG)"));
                add(new QuestionModule("ইরিত্রিয়া-এর মুদ্রার নাম কী?", "B", "ইয়েন (JPY)","নাকফা (ERN)",  "ক্রোন (DKK)", "ফ্রাঙ্ক (DJF)"));
                add(new QuestionModule("এস্তোনিয়া-এর মুদ্রার নাম কী?", "B", "ইউরো (EUR)", "লিলাঙ্গেনি (SZL)", "পেসো (ARS)", "ইয়েন (JPY)"));
                add(new QuestionModule("এসওয়াতিনি-এর মুদ্রার নাম কী?", "C",  "ইউরো (EUR)", "ডলার (USD)"," লিলাঙ্গেনি (SZL)", "ইয়েন (JPY)"));
                add(new QuestionModule("ইথিওপিয়া-এর মুদ্রার নাম কী?", "C", "ইউরো (EUR)", "ডলার (USD)", "বির (ETB)", "দিনার (DZD)"));
                add(new QuestionModule("ফিজি-এর মুদ্রার নাম কী?", "A", "ফিজিয়ান ডলার (FJD)", "ইউরো (EUR)", "গিল্ডার (ANG)", "পাউন্ড (GBP)"));
                add(new QuestionModule("ফিনল্যান্ড-এর মুদ্রার নাম কী?", "B",  "ডলার (USD)","ইউরো (EUR)", "পেসো (ARS)", "ইয়েন (JPY)"));
                add(new QuestionModule("ফ্রান্স-এর মুদ্রার নাম কী?", "B", "ডলার (USD)","ইউরো (EUR)",  "পেসো (ARS)", "ইয়েন (JPY)"));
                add(new QuestionModule("গ্যাবন-এর মুদ্রার নাম কী?", "D", " লিলাঙ্গেনি (SZL)", "দিনার (DZD)", "ইয়েন (JPY)", "ফ্রাঙ্ক (XAF)"));
                add(new QuestionModule("গাম্বিয়া-এর মুদ্রার নাম কী?", "D", " ফ্লোরিন (AWG)", "ইউরো (EUR)", "ডলার (USD)", "দালাসি (GMD)"));
                add(new QuestionModule("জর্জিয়া-এর মুদ্রার নাম কী?", "C", "পেসো (ARS)", "কোলন (CRC)","লারি (GEL)","গিল্ডার (ANG)"));
                add(new QuestionModule("জার্মানি-এর মুদ্রার নাম কী?", "B", "ডলার (USD)","ইউরো (EUR)", "পেসো (ARS)","পাউন্ড (GBP)"));
                add(new QuestionModule("ঘানা-এর মুদ্রার নাম কী?", "D", "ফ্লোরিন (AWG)", "ইউরো (EUR)", "গিল্ডার (ANG)", "সেডি (GHS)"));
                add(new QuestionModule("গ্রিস-এর মুদ্রার নাম কী?", "B", "ডলার (USD)","ইউরো (EUR)", "পেসো (ARS)", "ইয়েন (JPY)"));
                add(new QuestionModule("গুয়াতেমালা-এর মুদ্রার নাম কী?", "D", "লারি (GEL)","ইউরো (EUR)", "ডলার (USD)","কুয়েটজাল (GTQ)"));
                add(new QuestionModule("গিনি-এর মুদ্রার নাম কী?", "D", "গিনি ডলার (USD)", "ইউরো (EUR)", "গিনি ইয়েন (JPY)", "গিনি ফ্রাঙ্ক (GNF)"));
                add(new QuestionModule("গিনি-বিসাউ-এর মুদ্রার নাম কী?", "D", "কুয়েটজাল (GTQ)", "ইউরো (EUR)", "ডলার (USD)", "সিএফএ ফ্রাঙ্ক (XOF)"));
                add(new QuestionModule("গায়ানা-এর মুদ্রার নাম কী?", "B", "গায়ানিজ ইয়েন (JPY)","গায়ানিজ ডলার (GYD)","ডলার (USD)", "গায়ানিজ পাউন্ড (GBP)"));


                add(new QuestionModule("হাইতি-এর মুদ্রার নাম কী?", "C",  "কোলন (CRC)", "ডলার (USD)","গুরডে (HTG)", "পাউন্ড (GBP)"));
                add(new QuestionModule("হন্ডুরাস-এর মুদ্রার নাম কী?", "B",  "ইউরো (EUR)","লেম্পিরা (HNL)", "গিল্ডার (ANG)", "পাউন্ড (GBP)"));
                add(new QuestionModule("হংকং-এর মুদ্রার নাম কী?", "C", "কুয়েটজাল (GTQ)", "ইউরো (EUR)","হংকং ডলার (HKD)","পাউন্ড (GBP)"));
                add(new QuestionModule("হাঙ্গেরি-এর মুদ্রার নাম কী?", "C",  "ইয়েন (JPY)", "ডলার (USD)","ফোরিন্ট (HUF)", "পাউন্ড (GBP)"));
                add(new QuestionModule("আইসল্যান্ড-এর মুদ্রার নাম কী?", "C", "ইউরো (EUR)", "ডলার (USD)","ক্রোনা (ISK)",  "কোলন (CRC)"));
                add(new QuestionModule("ভারত-এর মুদ্রার নাম কী?", "D", "দিনার (DZD) ", "ইউরো (EUR)", "টাকা (BDT)", "রুপি (INR)"));
                add(new QuestionModule("ইন্দোনেশিয়া-এর মুদ্রার নাম কী?", "C", "ইয়েন (JPY)", "কোলন (CRC)","রুপিয়াহ (IDR)", "টাকা (BDT)"));
                add(new QuestionModule("ইরান-এর মুদ্রার নাম কী?", "B", "শেকেল (ILS)","রিয়াল (IRR)", "দিনার (DZD)", "পাউন্ড (GBP)"));
                add(new QuestionModule("ইরাক-এর মুদ্রার নাম কী?", "C", "ফোরিন্ট (HUF)","কোলন (CRC)","দিনার (IQD)","রিয়াল (IRR)"));
                add(new QuestionModule("আয়ারল্যান্ড-এর মুদ্রার নাম কী?", "D",  "ডলার (USD)", "পেসো (ARS)", "ইয়েন (JPY)","ইউরো (EUR)"));
                add(new QuestionModule("ইসরায়েল-এর মুদ্রার নাম কী?", "D", "পেসো (ARS)", "ফোরিন্ট (HUF)", "দিনার (DZD)", "শেকেল (ILS)"));
                add(new QuestionModule("ইতালি-এর মুদ্রার নাম কী?", "B", "ডলার (USD)","ইউরো (EUR)", "পেসো (ARS)", "ইয়েন (JPY)"));
                add(new QuestionModule("জামাইকা-এর মুদ্রার নাম কী?", "A", "জামাইকান ডলার (JMD)", "ইউরো (EUR)", "ডলার (USD)", "পাউন্ড (GBP)"));

                add(new QuestionModule("জাপান-এর মুদ্রার নাম কী?", "A", "ইয়েন (JPY)", "ইউরো (EUR)", "ডলার (USD)", "পাউন্ড (GBP)"));
                add(new QuestionModule("জর্ডান-এর মুদ্রার নাম কী?", "A", "দিনার (JOD)", "কোলন (CRC)", "ডলার (USD)", "পাউন্ড (GBP)"));
                add(new QuestionModule("কাজাখস্তান-এর মুদ্রার নাম কী?", "B", "ইউরো (EUR)","টেঙ্গে (KZT)",  "ডলার (USD)", "কোলন (CRC)"));
                add(new QuestionModule("কেনিয়া-এর মুদ্রার নাম কী?", "C", "টেঙ্গে (KZT)", "ডলার (USD)", "শিলিং (KES)", "পাউন্ড (GBP)"));
                add(new QuestionModule("কিরিবাতি-এর মুদ্রার নাম কী?", "A", "অস্ট্রেলিয়ান ডলার (AUD)", "ইউরো (EUR)", "ডলার (USD)", "পাউন্ড (GBP)"));
                add(new QuestionModule("কোসোভো-এর মুদ্রার নাম কী?", "A", "ইউরো (EUR)", "ডলার (USD)", "পেসো (ARS)", "ইয়েন (JPY)"));
                add(new QuestionModule("কুয়েত-এর মুদ্রার নাম কী?", "A", "কুয়েতি দিনার (KWD)", "রুপিয়াহ (IDR)", "ডলার (USD)", "পাউন্ড (GBP)"));
                add(new QuestionModule("কিরগিজস্তান-এর মুদ্রার নাম কী?", "C", "দিনার (DZD)","শিলিং (KES)","সোম (KGS)",  "পাউন্ড (GBP)"));
                add(new QuestionModule("লাওস-এর মুদ্রার নাম কী?", "D", "টেঙ্গে (KZT)", "ইউরো (EUR)", "শিলিং (KES)", "লাও কিপ (LAK)"));
                add(new QuestionModule("লাতভিয়া-এর মুদ্রার নাম কী?", "A", "ইউরো (EUR)", "ডলার (USD)", "পেসো (ARS)", "ইয়েন (JPY)"));

                add(new QuestionModule("লেবানন-এর মুদ্রার নাম কী?", "B", "ইউরো (EUR)","লেবানিজ পাউন্ড (LBP)",  "লেবানিজ ডলার (USD)", "ইয়েন (JPY)"));
                add(new QuestionModule("লিবিয়া-এর মুদ্রার নাম কী?", "A", "লিবিয়ান দিনার (LYD)", "ইউরো (EUR)", "লিবিয়ান ডলার (USD)", "ইয়েন (JPY)"));
                add(new QuestionModule("লিচেনস্টেইন-এর মুদ্রার নাম কী?", "B",  "ইউরো (EUR)","সুইস ফ্রাঙ্ক (CHF)", "ডলার (USD)", "সুইস পাউন্ড (GBP)"));
                add(new QuestionModule("লিথুয়ানিয়া-এর মুদ্রার নাম কী?", "A", "ইউরো (EUR)", "ডলার (USD)", "পেসো (ARS)", "ইয়েন (JPY)"));
                add(new QuestionModule("লুক্সেমবার্গ-এর মুদ্রার নাম কী?", "A", "ইউরো (EUR)", "ডলার (USD)", "পেসো (ARS)", "ইয়েন (JPY)"));
                add(new QuestionModule("ম্যাকাও-এর মুদ্রার নাম কী?", "D", "ডেনার (MKD)", "ইউরো (EUR)", "ডলার (USD)", "পাটাকা (MOP)"));
                add(new QuestionModule("মেসিডোনিয়া-এর মুদ্রার নাম কী?", "B", "ডেনার (MKD)", "ইউরো (EUR)", "ডলার (USD)", "পাউন্ড (GBP)"));
                add(new QuestionModule("মাদাগাস্কার-এর মুদ্রার নাম কী?", "C", "গিল্ডার (ANG)", "ডলার (USD)","আরিয়ারি (MGA)",  "পাউন্ড (GBP)"));
                add(new QuestionModule("মালাউই-এর মুদ্রার নাম কী?", "B", "ইউরো (EUR)","কওয়াচা (MWK)",  "ডলার (USD)", "ইয়েন (JPY)"));
                add(new QuestionModule("মালয়েশিয়া-এর মুদ্রার নাম কী?", "A", " রিঙ্গিত (MYR)", "টেঙ্গে (KZT)", "ডলার (USD)", "কওয়াচা (MWK)"));
                add(new QuestionModule("মালদ্বীপ-এর মুদ্রার নাম কী?", "A", " রুফিয়া (MVR)", "আরিয়ারি (MGA)", "লাও কিপ (LAK)", "কওয়াচা (MWK)"));


                add(new QuestionModule("মালি-এর মুদ্রার নাম কী?", "C",  "কওয়াচা (MWK)", "ডলার (USD)","সিএফএ ফ্রাঙ্ক (XOF)", "লাও কিপ (LAK)"));
                add(new QuestionModule("মাল্টা-এর মুদ্রার নাম কী?", "A", "ইউরো (EUR)", "ডলার (USD)", "পেসো (ARS)", "ইয়েন (JPY)"));
                add(new QuestionModule("মার্শাল দ্বীপপুঞ্জ-এর মুদ্রার নাম কী?", "A", "মার্কিন ডলার (USD)", "ইউরো (EUR)", "পেসো (ARS)", "ইয়েন (JPY)"));
                add(new QuestionModule("মরিশাস-এর মুদ্রার নাম কী?", "C",  "ইউরো (EUR)", "ডলার (USD)","মরিশিয়ান রুপি (MUR)", "পাউন্ড (GBP)"));
                add(new QuestionModule("মেক্সিকো-এর মুদ্রার নাম কী?", "A", "পেসো (MXN)", "ইউরো (EUR)", "ডলার (USD)", "পাউন্ড (GBP)"));
                add(new QuestionModule("মাইক্রোনেশিয়া-এর মুদ্রার নাম কী?", "A", "মার্কিন ডলার (USD)", "ইউরো (EUR)", "পেসো (ARS)", "ইয়েন (JPY)"));
                add(new QuestionModule("মোলদোভা-এর মুদ্রার নাম কী?", "B",  "ইউরো (EUR)","লেয়ু (MDL)", "ডলার (USD)", "পাউন্ড (GBP)"));
                add(new QuestionModule("মোনাকো-এর মুদ্রার নাম কী?", "C", "ডলার (USD)", "পেসো (ARS)","ইউরো (EUR)",  "ইয়েন (JPY)"));
                add(new QuestionModule("মঙ্গোলিয়া-এর মুদ্রার নাম কী?", "A", "টোগ্রোগ (MNT)", "ইউরো (EUR)", "ডলার (USD)", "পাউন্ড (GBP)"));
                add(new QuestionModule("মন্টেনেগ্রো-এর মুদ্রার নাম কী?", "A", "ইউরো (EUR)", "ডলার (USD)", "পেসো (ARS)", "ইয়েন (JPY)"));
                add(new QuestionModule("মরক্কো-এর মুদ্রার নাম কী?", "B", "ইউরো (EUR)", "মরোক্কান দিরহাম (MAD)",  "ডলার (USD)", "মরোক্কান ইয়েন (JPY)"));
                add(new QuestionModule("মোজাম্বিক-এর মুদ্রার নাম কী?", "C", "ইউরো (EUR)", "ডলার (USD)", "মেটিক্যাল (MZN)",  "পাউন্ড (GBP)"));
                add(new QuestionModule("মিয়ানমার-এর মুদ্রার নাম কী?", "B", "ডলার (USD)","কিয়াত (MMK)", "ইউরো (EUR)",  "পাউন্ড (GBP)"));
                add(new QuestionModule("নামিবিয়া-এর মুদ্রার নাম কী?", "A", "নামিবিয়ান ডলার (NAD)", "ইউরো (EUR)", "ডলার (USD)", "নামিবিয়ান পাউন্ড (GBP)"));
                add(new QuestionModule("নাউরু-এর মুদ্রার নাম কী?", "B", "ইউরো (EUR)","অস্ট্রেলিয়ান ডলার (AUD)",  "ডলার (USD)", "পাউন্ড (GBP)"));
                add(new QuestionModule("নেপাল-এর মুদ্রার নাম কী?", "A", "নেপালি রুপি (NPR)", "নেপালি দিরহাম (MAD)", "ডলার (USD)", "রুপি (NPR)"));
                add(new QuestionModule("নেদারল্যান্ডস-এর মুদ্রার নাম কী?", "A", "ইউরো (EUR)", "ডলার (USD)", "পেসো (ARS)", "ইয়েন (JPY)"));
                add(new QuestionModule("নিউজিল্যান্ড-এর মুদ্রার নাম কী?", "A", "নিউজিল্যান্ড ডলার (NZD)", "ইউরো (EUR)", "ডলার (USD)", "পাউন্ড (GBP)"));
                add(new QuestionModule("নিকারাগুয়া-এর মুদ্রার নাম কী?", "B", "মেটিক্যাল (MZN)","কর্ডোবা (NIO)", "ইয়েন (JPY)", "পাউন্ড (GBP)"));
                add(new QuestionModule("নাইজার-এর মুদ্রার নাম কী?", "C",  "ডলার (USD)", "মেটিক্যাল (MZN)","সিএফএ ফ্রাঙ্ক (XOF)", "পাউন্ড (GBP)"));
                add(new QuestionModule("নাইজেরিয়া-এর মুদ্রার নাম কী?", "A", "নায়রা (NGN)", "ইউরো (EUR)", "ডলার (USD)", "দিরহাম (MAD)"));
                add(new QuestionModule("নর্থ কোরিয়া-এর মুদ্রার নাম কী?", "A", "ওয়ান (KPW)", "ইউরো (EUR)", "ডলার (USD)", "পাউন্ড (GBP)"));
                add(new QuestionModule("উত্তর মেসিডোনিয়া-এর মুদ্রার নাম কী?", "B", "ডলার (USD)"," ডেনার (MKD)", "ইউরো (EUR)",  "পাউন্ড (GBP)"));
                add(new QuestionModule("নরওয়ে-এর মুদ্রার নাম কী?", "C", "ইউরো (EUR)", "ডলার (USD)","ক্রোন (NOK)", "পাউন্ড (GBP)"));
                add(new QuestionModule("ওমান-এর মুদ্রার নাম কী?", "A", "ওমানি রিয়াল (OMR)", "ইউরো (EUR)", "ওমানি ডলার (USD)", "পাউন্ড (GBP)"));
                add(new QuestionModule("পাকিস্তান-এর মুদ্রার নাম কী?", "A", " রুপি (PKR)", "ইউরো (EUR)", "ডলার (USD)", "পাউন্ড (GBP)"));
                add(new QuestionModule("পালাউ-এর মুদ্রার নাম কী?", "B",  "ইউরো (EUR)","মার্কিন ডলার (USD)", "পেসো (ARS)", "ইয়েন (JPY)"));

                add(new QuestionModule("পাপুয়া নিউ গিনি-এর মুদ্রার নাম কী?", "A", "কিনা (PGK)", "ইউরো (EUR)", "ডলার (USD)", "পাউন্ড (GBP)"));
                add(new QuestionModule("প্যারাগুয়ে-এর মুদ্রার নাম কী?", "A", " গুয়ারানি (PYG)", "ইউরো (EUR)", "ডলার (USD)", "পাউন্ড (GBP)"));
                add(new QuestionModule("পেরু-এর মুদ্রার নাম কী?", "B", "ইউরো (EUR)","সল (PEN)",  "ডলার (USD)", "পাউন্ড (GBP)"));
                add(new QuestionModule("ফিলিপাইন-এর মুদ্রার নাম কী?", "C", "কোলন (CRC)", "ডলার (USD)","পেসো (PHP)",  "পাউন্ড (GBP)"));
                add(new QuestionModule("পোল্যান্ড-এর মুদ্রার নাম কী?", "A", "পোলিশ জ্লটি (PLN)", "ইউরো (EUR)", "ডলার (USD)", "পাউন্ড (GBP)"));
                add(new QuestionModule("পর্তুগাল-এর মুদ্রার নাম কী?", "A", "ইউরো (EUR)", "ডলার (USD)", "পেসো (ARS)", "ইয়েন (JPY)"));
                add(new QuestionModule("কাতার-এর মুদ্রার নাম কী?", "B", " ডেনার (MKD)","কাতারি রিয়াল (QAR)",  "কাতারি ডলার (USD)", "পাউন্ড (GBP)"));
                add(new QuestionModule("রোমানিয়া-এর মুদ্রার নাম কী?", "C", "ইউরো (EUR)", "ডলার (USD)", "লেউ (RON)",  "পাউন্ড (GBP)"));
                add(new QuestionModule("রাশিয়া-এর মুদ্রার নাম কী?", "A", " রুবল (RUB)", "ইউরো (EUR)", "ডলার (USD)", "পাউন্ড (GBP)"));
                add(new QuestionModule("রুয়ান্ডা-এর মুদ্রার নাম কী?", "A", "রুয়ান্ডান ফ্রাঙ্ক (RWF)", "ইউরো (EUR)", "কোলন (CRC)", "পাউন্ড (GBP)"));
                add(new QuestionModule("সেন্ট ভিনসেন্ট এবং গ্রেনাডাইনস-এর মুদ্রার নাম কী?", "A", "ইস্টার্ন ক্যারিবিয়ান ডলার (XCD)", "ইউরো (EUR)", "ডলার (USD)", "পাউন্ড (GBP)"));
                add(new QuestionModule("সামোয়া-এর মুদ্রার নাম কী?", "A", "তালা (WST)", "ইউরো (EUR)", "ডলার (USD)", "পাউন্ড (GBP)"));
                add(new QuestionModule("সান মারিনো-এর মুদ্রার নাম কী?", "B", "পেসো (ARS)", "ইউরো (EUR)", "ডলার (USD)",  "ইয়েন (JPY)"));
                add(new QuestionModule("সেনেগাল-এর মুদ্রার নাম কী?", "A", "সিএফএ ফ্রাঙ্ক (XOF)", "ইউরো (EUR)", "ডলার (USD)", "পাউন্ড (GBP)"));
                add(new QuestionModule("সার্বিয়া-এর মুদ্রার নাম কী?", "A", "সার্বিয়ান দিনার (RSD)", "ইউরো (EUR)", "ডলার (USD)", "পাউন্ড (GBP)"));
                add(new QuestionModule("সেশেলস-এর মুদ্রার নাম কী?", "A", "সেশেলস রুপি (SCR)", "ইউরো (EUR)", "ডলার (USD)", "পাউন্ড (GBP)"));
                add(new QuestionModule("সিয়েরা লিওন-এর মুদ্রার নাম কী?", "C", "ইউরো (EUR)", "ডলার (USD)","লিওন (SLL)",  "পাউন্ড (GBP)"));
                add(new QuestionModule("সিঙ্গাপুর-এর মুদ্রার নাম কী?", "A", "সিঙ্গাপুর ডলার (SGD)", "ইউরো (EUR)", "ডলার (USD)", "ইয়েন (JPY)"));
                add(new QuestionModule("স্লোভাকিয়া-এর মুদ্রার নাম কী?", "A", "ইউরো (EUR)", "ডলার (USD)", "পেসো (ARS)", "ইয়েন (JPY)"));
                add(new QuestionModule("স্লোভেনিয়া-এর মুদ্রার নাম কী?", "B", "ডলার (USD)","ইউরো (EUR)",  "পেসো (ARS)", "ইয়েন (JPY)"));
                add(new QuestionModule("সলোমন দ্বীপপুঞ্জ-এর মুদ্রার নাম কী?", "A", "সলোমন আইল্যান্ড ডলার (SBD)", "ইউরো (EUR)", "ডলার (USD)", "পাউন্ড (GBP)"));
                add(new QuestionModule("সোমালিয়া-এর মুদ্রার নাম কী?", "A", "শিলিং (SOS)", "ইউরো (EUR)", "ডলার (USD)", "পাউন্ড (GBP)"));
                add(new QuestionModule("দক্ষিণ আফ্রিকা-এর মুদ্রার নাম কী?", "A", "আফ্রিকান র্যান্ড (ZAR)", "ইউরো (EUR)", "আফ্রিকান ডলার (USD)", "পাউন্ড (GBP)"));
                add(new QuestionModule("দক্ষিণ কোরিয়া-এর মুদ্রার নাম কী?", "B", "ইউরো (EUR)"," ওয়ান (KRW)",  "ডলার (USD)", "পাউন্ড (GBP)"));
                add(new QuestionModule("দক্ষিণ সুদান-এর মুদ্রার নাম কী?", "C", "ইউরো (EUR)", "সুদানিজ ডলার (USD)","সুদানিজ পাউন্ড (SSP)",  "পাউন্ড (GBP)"));
                add(new QuestionModule("স্পেন-এর মুদ্রার নাম কী?", "A", "ইউরো (EUR)", "ডলার (USD)", "পেসো (ARS)", "ইয়েন (JPY)"));
                add(new QuestionModule("শ্রীলঙ্কা-এর মুদ্রার নাম কী?", "A", "শ্রীলঙ্কান রুপি (LKR)", "ইউরো (EUR)", "ডলার (USD)", "পাউন্ড (GBP)"));
                add(new QuestionModule("সুদান-এর মুদ্রার নাম কী?", "B", "ইউরো (EUR)","সুদানি পাউন্ড (SDG)",  "ডলার (USD)", "পাউন্ড (GBP)"));
                add(new QuestionModule("সুরিনাম-এর মুদ্রার নাম কী?", "C", "ইউরো (EUR)", "ডলার (USD)","সুরিনামিজ ডলার (SRD)",  "পাউন্ড (GBP)"));
                add(new QuestionModule("সুইডেন-এর মুদ্রার নাম কী?", "A", "ক্রোনা (SEK)", "ইউরো (EUR)", "ডলার (USD)", "পাউন্ড (GBP)"));
                add(new QuestionModule("সুইজারল্যান্ড-এর মুদ্রার নাম কী?", "A", "সুইস ফ্রাঙ্ক (CHF)", "ইউরো (EUR)", "সুইস ডলার (USD)", "সুইস পাউন্ড (GBP)"));
                add(new QuestionModule("সিরিয়া-এর মুদ্রার নাম কী?", "B", "ইউরো (EUR)","সিরিয়ান পাউন্ড (SYP)",  "ডলার (USD)", "পাউন্ড (GBP)"));
                add(new QuestionModule("তাজিকিস্তান-এর মুদ্রার নাম কী?", "C", "ইউরো (EUR)", "ডলার (USD)","সোমনি (TJS)",  "পাউন্ড (GBP)"));
                add(new QuestionModule("তানজানিয়া-এর মুদ্রার নাম কী?", "A", " শিলিং (TZS)", "ইউরো (EUR)", "ডলার (USD)", "পাউন্ড (GBP)"));
                add(new QuestionModule("থাইল্যান্ড-এর মুদ্রার নাম কী?", "A", " বাহত (THB)", "ইউরো (EUR)", "ডলার (USD)", "পাউন্ড (GBP)"));
                add(new QuestionModule("তিমুর-লেস্তে-এর মুদ্রার নাম কী?", "A", "মার্কিন ডলার (USD)", "ইউরো (EUR)", "পেসো (ARS)", "ইয়েন (JPY)"));
                add(new QuestionModule("টোঙ্গা-এর মুদ্রার নাম কী?", "C",  "ইউরো (EUR)", "ডলার (USD)","পাঙ্গা (TOP)", "পাউন্ড (GBP)"));
                add(new QuestionModule("ট্রিনিডাড এবং টোবাগো-এর মুদ্রার নাম কী?", "A", "ট্রিনিডাড এবং টোবাগো ডলার (TTD)", "ইউরো (EUR)", "ডলার (USD)", "ট্রিনিডাড এবং টোবাগো পাউন্ড (GBP)"));
                add(new QuestionModule("টিউনিশিয়া-এর মুদ্রার নাম কী?", "A", "তিউনিসিয়ান দিনার (TND)", "ইউরো (EUR)", "তিউনিসিয়ান ডলার (USD)", "তিউনিসিয়ান পাউন্ড (GBP)"));
                add(new QuestionModule("তুর্কমেনিস্তান-এর মুদ্রার নাম কী?", "B", "ইউরো (EUR)","মানাত (TMT)",  "ডলার (USD)", "পাউন্ড (GBP)"));
                add(new QuestionModule("তুরস্ক-এর মুদ্রার নাম কী?", "C",  "ইউরো (EUR)", "ডলার (USD)","লিরা (TRY)", "পাউন্ড (GBP)"));
                add(new QuestionModule("তুভালু-এর মুদ্রার নাম কী?", "A", "অস্ট্রেলিয়ান ডলার (AUD)", "সোমনি (TJS)", "ডলার (USD)", "পাউন্ড (GBP)"));
                add(new QuestionModule("উগান্ডা-এর মুদ্রার নাম কী?", "A", "শিলিং (UGX)", "ইউরো (EUR)", "ডলার (USD)", "পাউন্ড (GBP)"));
                add(new QuestionModule("ইউক্রেন-এর মুদ্রার নাম কী?", "B",  "ইউরো (EUR)"," হৃভনিয়া (UAH)", "ডলার (USD)", "পাউন্ড (GBP)"));
                add(new QuestionModule("সংযুক্ত আরব আমিরাত-এর মুদ্রার নাম কী?", "C", "ইউরো (EUR)", "ডলার (USD)"," দিরহাম (AED)",  "পাউন্ড (GBP)"));
                add(new QuestionModule("যুক্তরাজ্য-এর মুদ্রার নাম কী?", "A", "পাউন্ড স্টার্লিং (GBP)", "ইউরো (EUR)", "ডলার (USD)", "ইয়েন (JPY)"));
                add(new QuestionModule("যুক্তরাষ্ট্র-এর মুদ্রার নাম কী?", "A", "মার্কিন ডলার (USD)", "ইউরো (EUR)", "পেসো (ARS)", "ইয়েন (JPY)"));
                add(new QuestionModule("উরুগুয়ে-এর মুদ্রার নাম কী?", "B", "ইউরো (EUR)"," পেসো (UYU)",  "ডলার (USD)", "পাউন্ড (GBP)"));
                add(new QuestionModule("উজবেকিস্তান-এর মুদ্রার নাম কী?", "C", "ইউরো (EUR)", "ডলার (USD)"," সুম (UZS)",  "পাউন্ড (GBP)"));
                add(new QuestionModule("ভ্যাটিকান সিটি-এর মুদ্রার নাম কী?", "A", "ইউরো (EUR)", "ডলার (USD)", "পেসো (ARS)", "ইয়েন (JPY)"));
                add(new QuestionModule("ভেনেজুয়েলা-এর মুদ্রার নাম কী?", "A", "বলিভার (VES)", "ইউরো (EUR)", "ডলার (USD)", "পাউন্ড (GBP)"));
                add(new QuestionModule("ভিয়েতনাম-এর মুদ্রার নাম কী?", "A", "ডং (VND)", "কোলন (CRC)", "ডলার (USD)", "পাউন্ড (GBP)"));
                add(new QuestionModule("ইয়েমেন-এর মুদ্রার নাম কী?", "A", " রিয়াল (YER)", "ইউরো (EUR)", "ডলার (USD)", "পাউন্ড (GBP)"));
                add(new QuestionModule("জাম্বিয়া-এর মুদ্রার নাম কী?", "A", "কওয়াচা (ZMW)", "ইউরো (EUR)", "ডলার (USD)", "পাউন্ড (GBP)"));
                add(new QuestionModule("জিম্বাবুয়ে-এর মুদ্রার নাম কী?", "A", "জিম্বাবুয়ে ডলার (ZWL)", "ইউরো (EUR)", "ডলার (USD)", "পাউন্ড (GBP)"));

            }
        };
        QuestionModule.createQuestionsForSubject("মুদ্রার নাম", R.drawable.currency, questions);



        //------------- Subject 5 ------------------------------------------

        //================================== Ocean and River====================
        questions = new ArrayList(){
            {
                add(new QuestionModule("বিশ্বের দীর্ঘতম নদী কোনটি?", "A", "নীল নদ", "আমাজন", "ইয়াংজি", "মিসিসিপি"));
                add(new QuestionModule("বিশ্বের বৃহত্তম মহাসাগর কোনটি?", "B",  "আটলান্টিক মহাসাগর","প্রশান্ত মহাসাগর","ভারত মহাসাগর", "বঙ্গোপসাগর"));
                add(new QuestionModule("বাংলাদেশের প্রধান নদী কোনটি?", "D", "মাতামুহুরি", "মেঘনা", "যমুনা", "পদ্মা"));
                add(new QuestionModule("বিশ্বের সবচেয়ে গভীরতম মহাসাগর কোনটি?", "A", "প্যাসিফিক মহাসাগর", "আটলান্টিক মহাসাগর", "ভারত মহাসাগর", "আর্কটিক মহাসাগর"));
                add(new QuestionModule("গঙ্গা নদী কোন দেশ দিয়ে প্রবাহিত হয়?", "C",  "চীন", "নেপাল","ভারত", "বাংলাদেশ"));
                add(new QuestionModule("আমাজন নদীর মুখ কোথায় অবস্থিত?", "B", "প্যাসিফিক মহাসাগর", "আটলান্টিক মহাসাগর", "ভারত মহাসাগর", "বঙ্গোপসাগর"));
                add(new QuestionModule("ইউরোপের সবচেয়ে দীর্ঘতম নদী কোনটি?", "C",  "ড্যানিউব নদী", "রাইন নদী","ভলগা নদী", "এলবে নদী"));
                add(new QuestionModule("মিসিসিপি নদী কোন মহাদেশে অবস্থিত?", "A", "উত্তর আমেরিকা", "দক্ষিণ আমেরিকা", "ইউরোপ", "আফ্রিকা"));
                add(new QuestionModule("নীল নদ কোন দুটি দেশ দিয়ে প্রবাহিত হয়?", "A", "ইথিওপিয়া ও মিশর", "সুদান ও মিশর", "তাঞ্জানিয়া ও মিশর", "রুয়ান্ডা ও বুরুন্ডি"));
                add(new QuestionModule("বিশ্বের সবচেয়ে দীর্ঘতম সাগর কোনটি?", "B", "বাল্টিক সাগর", "দক্ষিণ চীন সাগর", "ভূমধ্য সাগর", "আদ্রিয়াটিক সাগর"));
                add(new QuestionModule("তিব্বতের মালভূমি থেকে কোন নদী উৎপন্ন হয়?", "C", "ইয়াংজি", "পদ্মা", "ব্রহ্মপুত্র", "মেকং"));
                add(new QuestionModule("ম্যাকেঞ্জি নদী কোন দেশে অবস্থিত?", "D",  "যুক্তরাষ্ট্র", "রাশিয়া", "অস্ট্রেলিয়া","কানাডা"));
                add(new QuestionModule("সিন্ধু কোন দেশের প্রধান নদী ?",  "D", "কানাডা", "যুক্তরাষ্ট্র", "রাশিয়া", "পাকিস্থান"));
                add(new QuestionModule("ইরাবতী কোন দেশের প্রধান নদী ?",  "B", "কানাডা", "মিয়ানমার", "রাশিয়া", "পাকিস্থান"));
                add(new QuestionModule("প্রশান্ত মহাসাগর ও আটলান্টিক মহাসাগরের সংযোগকারী প্রণালী কোনটি?", "A", "পানামা খাল", "সুয়েজ খাল", "বসফরাস প্রণালী", "মালাক্কা প্রণালী"));
                add(new QuestionModule("তিস্তা নদী কোন দেশের সাথে সম্পর্কিত?", "B", "ভারত ও নেপাল", "ভারত ও বাংলাদেশ", "নেপাল ও বাংলাদেশ ", "ভুটান ও বাংলাদেশ"));
                add(new QuestionModule("নীল নদের উৎস কোথায়?", "C", "লেক টানা", "লেক ভিক্টোরিয়া", " তানা হ্রদ", "রুবিক্স নদী"));


                add(new QuestionModule("ভারত মহাসাগরের উত্তর সীমান্তে কোন মহাদেশ রয়েছে ?", "A", "এশিয়া", "ইউরোপ", "আফ্রিকা", "অস্ট্রেলিয়া"));
                add(new QuestionModule("অ্যান্টার্কটিক মহাসাগর কোথায় অবস্থিত?", "A", "দক্ষিণ মেরু", "উত্তর মেরু", "পূর্ব মেরু", "পশ্চিম মেরু"));
                add(new QuestionModule("ভলগা নদী কোন দেশে অবস্থিত?", "C",  "জার্মানি", "পোল্যান্ড","রাশিয়া", "ফ্রান্স"));
                add(new QuestionModule("প্রশান্ত মহাসাগর কতটি মহাদেশের সাথে সংযোগ স্থাপন করে?", "B", "পাঁচটি", "চারটি", "তিনটি", "দুটি"));
                add(new QuestionModule("ড্যানিউব নদী কতটি দেশ দিয়ে প্রবাহিত হয়?", "A", "দশটি", "নয়টি", "আটটি", "সাতটি"));
                add(new QuestionModule("উত্তর সাগর কোন মহাসাগরের অংশ?", "B",  "প্যাসিফিক মহাসাগর", "আটলান্টিক মহাসাগর","ভারত মহাসাগর", "আর্কটিক মহাসাগর"));
                add(new QuestionModule("তানজানিয়ার প্রধান নদী কোনটি?", "C",  "মালাগাসি", "কেলো","রুফিজি", "রোয়াহা"));
                add(new QuestionModule("অ্যামাজন নদী কোন মহাদেশে অবস্থিত?", "A", "দক্ষিণ আমেরিকা", "উত্তর আমেরিকা", "আফ্রিকা", "এশিয়া"));
                add(new QuestionModule("ইয়াংজি নদী কোন দেশে অবস্থিত?", "A", "চীন", "ভারত", "জাপান", "দক্ষিণ কোরিয়া"));
                add(new QuestionModule("বাল্টিক সাগরের প্রধান দেশ কোনটি?", "B", "রাশিয়া", "সুইডেন", "পোল্যান্ড", "জার্মানি"));



                add(new QuestionModule("নিগার নদী কোন মহাদেশে অবস্থিত?", "A", "আফ্রিকা", "এশিয়া", "ইউরোপ", "উত্তর আমেরিকা"));
                add(new QuestionModule("ক্যালিফোর্নিয়া উপসাগর কোথায় অবস্থিত?", "A", "মেক্সিকো", "যুক্তরাষ্ট্র", "কানাডা", "চিলি"));
                add(new QuestionModule("গঙ্গা নদী কোথায় মিশেছে?", "B", "বঙ্গোপসাগর", "আরব সাগর", "ভারত মহাসাগর", "আটলান্টিক মহাসাগর"));
                add(new QuestionModule("ককেশাস পর্বত কোন দুটি সাগরের মধ্যে অবস্থিত?", "C",  "আদ্রিয়াটিক সাগর ও আয়োনিয়ান সাগর", "বাল্টিক সাগর ও উত্তর সাগর", "কৃষ্ণ সাগর ও ক্যাস্পিয়ান সাগর","আরব সাগর ও লোহিত সাগর"));
                add(new QuestionModule("মেঘনা নদী কোন দেশে অবস্থিত?", "D",  "ভারত", "নেপাল", "ভুটান","বাংলাদেশ"));
                add(new QuestionModule("আয়োনিয়ান সাগর কোথায় অবস্থিত?", "A", "গ্রিস ও ইতালির মধ্যে", "স্পেন ও পর্তুগালের মধ্যে", "ফ্রান্স ও যুক্তরাজ্যের মধ্যে", "তুরস্ক ও সাইপ্রাসের মধ্যে"));

                add(new QuestionModule("ভেনিস কোন সাগরের তীরে অবস্থিত?", "C",  "ভূমধ্য সাগর", "বাল্টিক সাগর","আদ্রিয়াটিক সাগর", "কৃষ্ণ সাগর"));
                add(new QuestionModule("ইংলিশ চ্যানেল কোন দুটি দেশের মধ্যে অবস্থিত?", "A", "যুক্তরাজ্য ও ফ্রান্স", "জার্মানি ও ডেনমার্ক", "নেদারল্যান্ডস ও বেলজিয়াম", "স্পেন ও পর্তুগাল"));
                add(new QuestionModule("তাসমান সাগর কোন দুটি দেশের মধ্যে অবস্থিত?", "B",  "ইন্দোনেশিয়া ও মালয়েশিয়া", "অস্ট্রেলিয়া ও নিউজিল্যান্ড", "ভারত ও শ্রীলঙ্কা", "জাপান ও দক্ষিণ কোরিয়া"));
                add(new QuestionModule("কারিবীয় সাগরের প্রধান দ্বীপপুঞ্জ কোনটি?", "D", "বাহামা", "কিউবা", "জ্যামাইকা", "সবগুলো"));
                add(new QuestionModule("কৃষ্ণ সাগর কোথায় অবস্থিত?", "D",  "দক্ষিণ এশিয়া", "উত্তর আমেরিকা", "দক্ষিণ আমেরিকা" ,"পূর্ব ইউরোপ"));
                add(new QuestionModule("রোয়াল সাগর কোন দেশের সাথে সম্পর্কিত?", "A", "নিউজিল্যান্ড", "অস্ট্রেলিয়া", "ফিজি", "টুভালু"));
                add(new QuestionModule("হাম্বার নদী কোন দেশে অবস্থিত?", "C",  "জার্মানি", "নেদারল্যান্ডস","যুক্তরাজ্য", "ফ্রান্স"));
                add(new QuestionModule("কাস্পিয়ান সাগর কতটি দেশের সাথে সংযোগ স্থাপন করে?", "A", "পাঁচটি", "চারটি", "তিনটি", "দুটি"));
                add(new QuestionModule("টেমস নদী কোন শহরের মধ্য দিয়ে প্রবাহিত?", "B",  "প্যারিস","লন্ডন", "বার্লিন", "রোম"));
                add(new QuestionModule("পানামা খাল কোন দুটি মহাসাগরের সাথে সংযোগ স্থাপন করে?", "C",  "আটলান্টিক ও ভারত মহাসাগর", "প্যাসিফিক ও আর্কটিক","আটলান্টিক ও প্যাসিফিক", "ভারত ও আর্কটিক"));
                add(new QuestionModule("ইউকন নদী কোথায় অবস্থিত?", "A", "আলাস্কা ও কানাডা", "যুক্তরাষ্ট্র ও মেক্সিকো", "কানাডা ও গ্রিনল্যান্ড", "রাশিয়া ও মঙ্গোলিয়া"));
                add(new QuestionModule("হং নদী কোন দেশে অবস্থিত?", "B",  "চীন","ভিয়েতনাম", "থাইল্যান্ড", "মালয়েশিয়া"));
                add(new QuestionModule("আতাকামা মরুভূমি কোন সাগরের কাছে অবস্থিত?", "C",  "আটলান্টিক সাগর", "ভারত সাগর", "প্রশান্ত মহাসাগর","আর্কটিক সাগর"));

                add(new QuestionModule("কঙ্গো নদী কতটি দেশের মধ্য দিয়ে প্রবাহিত?", "C", "দুটি", "তিনটি", "চারটি", "পাঁচটি"));
                add(new QuestionModule("হাডসন নদী কোন দেশের মধ্য দিয়ে প্রবাহিত?", "D", "যুক্তরাজ্য", "জার্মানি", "নেদারল্যান্ডস", "যুক্তরাষ্ট্র"));
                add(new QuestionModule("সেন্ট লরেন্স নদী কোথায় অবস্থিত?", "B", "কানাডা", "যুক্তরাষ্ট্র", "মেক্সিকো", "ফ্রান্স"));

                add(new QuestionModule("রেড সি কোন মহাসাগরের অংশ?", "D", "ভারত মহাসাগর", "আটলান্টিক মহাসাগর", "প্যাসিফিক মহাসাগর", "ভূমধ্যসাগর"));
                add(new QuestionModule("হোয়াইট নীল নদী কোথায় অবস্থিত?", "A", "সুদান", "কেনিয়া", "ইথিওপিয়া", "তাঞ্জানিয়া"));

               //=========================================



                add(new QuestionModule("পদ্মা নদীর দৈর্ঘ্য কত?", "A", "১,৫০০ কিমি", "১,২০০ কিমি", "১,৮০০ কিমি", "২,০০০ কিমি"));
                add(new QuestionModule("তিস্তা নদীর সবচেয়ে গভীরতম স্থান কোথায়?", "C", "দুর্গাপুর", "শেরপুর", "কুড়িগ্রাম","নীলফামারী"));

                add(new QuestionModule("যমুনার সবচেয়ে গভীরতম স্থান কোথায়?", "B",  "বগুড়া","সিরাজগঞ্জ", "টাঙ্গাইল", "কিশোরগঞ্জ"));
                add(new QuestionModule("ব্রহ্মপুত্র নদীর দৈর্ঘ্য কত?", "C", "৩,২০০ কিমি", "২,৬০০ কিমি","২,৯০০ কিমি", "২,৮০০ কিমি"));
                add(new QuestionModule("বাংলাদেশে মহানন্দা নদীর দৈর্ঘ্য কত?", "D", "১০০ কিমি", "১২০ কিমি", "৮০ কিমি", "১৬০ কিমি"));


                add(new QuestionModule("বঙ্গোপসাগরে কোন দেশের সাথে সীমান্ত ভাগাভাগি হয়?", "A", "ভারত", "পাকিস্তান", "নেপাল", "চীন"));
                add(new QuestionModule("বঙ্গোপসাগরের সবচেয়ে গভীরতম স্থান কোনটি", "B", "অ্যানডামান দ্বীপপুঞ্জের কাছে", " সোয়াচ অব নো গ্রাউন্ড", "কলকাতা উপকূলে", "মাদ্রাজ উপকূলে"));
                add(new QuestionModule("বঙ্গোপসাগরের প্রধান নদীগুলি কোনগুলি?", "C",  "মিসিসিপি, আমাজন, মিসৌরি", "নাইল, সিন্ধু, টেমস","গঙ্গা, ব্রহ্মপুত্র, মেঘনা", "রাইন, এলবে, ড্যানিউব"));
                add(new QuestionModule("বঙ্গোপসাগরের কোন দেশটি সবচেয়ে বেশি উপকূলীয় অঞ্চল নিয়ে গঠিত?", "A", "বাংলাদেশ", "ভারত", "শ্রীলঙ্কা", "মায়ানমার"));
                add(new QuestionModule("বঙ্গোপসাগরের তীরবর্তী দেশের নাম কোনটি?", "A", "বাংলাদেশ", "চীন", "জাপান", "সিঙ্গাপুর"));
                add(new QuestionModule("বঙ্গোপসাগরের পশ্চিম উপকূলে কোন দেশ অবস্থিত?", "B",  "মিয়ানমার", "ভারত","থাইল্যান্ড", "ব্রুনেই"));

                add(new QuestionModule("বঙ্গোপসাগরের একটি উল্লেখযোগ্য প্রবাহী নদী কোনটি?", "A", "গঙ্গা", "মিসিসিপি", "ডানিউব", "এমাজন"));
                add(new QuestionModule("বঙ্গোপসাগরে কোন ধরনের জলবায়ু সবচেয়ে বেশি পাওয়া যায়?", "B",  "সুবর্ণ","মৌসুমি", "আর্কটিক", "মেরিন"));
                add(new QuestionModule("বঙ্গোপসাগরের গুরুত্বপূর্ণ প্রাকৃতিক সম্পদ কোনটি?", "C", "মাছ", "তেল", "গ্যাস", "সোডা"));


                add(new QuestionModule("বঙ্গোপসাগরের দ্বীপগুলির মধ্যে কোনটি বৃহত্তম?", "A", "সেন্ট মার্টিন", "অ্যানডামান", "নিকোবর", "মালদ্বীপ"));
                add(new QuestionModule("বঙ্গোপসাগরের উপকূলীয় শহরগুলির মধ্যে কোনটি পর্যটন কেন্দ্র হিসেবে পরিচিত?", "C",  "কলকাতা", "মাদ্রাজ","কক্সবাজার", "মুম্বাই"));

                add(new QuestionModule("বঙ্গোপসাগরের বিপদজনক ঝড়ের নাম কি?", "A", "সাইক্লোন", "টর্নেডো", "ভূমিধস", "ঝড়ো বাতাস"));

                add(new QuestionModule("অ্যামাজন নদীর প্রধান উৎস কোথায়?", "A", "পেরু", "ব্রাজিল", "কলম্বিয়া", "ভেনেজুয়েলা"));
                add(new QuestionModule("অ্যামাজন নদীর দৈর্ঘ্য কত?", "C", "৬,৫০০ কিমি", "৫,৫০০ কিমি", "৭,০০০ কিমি", "৪,০০০ কিমি"));
                add(new QuestionModule("অ্যামাজন নদী কোথায় মিলিত হয়েছে?", "C", "অ্যাটলান্টিক মহাসাগরে", "মেক্সিকো উপসাগরে", "গালাপাগোস সাগরে", "বঙ্গোপসাগরে"));
                add(new QuestionModule("অ্যামাজন নদীর প্রবাহ কোন দেশের মাধ্যমে হয়?", "A", "ব্রাজিল", "কেনিয়া", "নাইজেরিয়া", "মিশর"));
                add(new QuestionModule("অ্যামাজন নদীর বৈশিষ্ট্য কী?", "A", "বিশ্বের সবচেয়ে বৃহৎ নদী ", "সবচেয়ে গভীর নদী", "দ্বিতীয় সবচেয়ে দীর্ঘ নদী", "সবচেয়ে জলহীন নদী"));
                add(new QuestionModule("অ্যামাজন নদীর উপনদীগুলির মধ্যে কোনটি সবচেয়ে দীর্ঘ?", "A", "রিও নেগ্রো", "রিও জাপোরা", "রিও তাপাজোস", "রিও মারানহাও"));

                add(new QuestionModule("অ্যামাজন নদীর সবচেয়ে বড় দ্বীপ কোনটি?", "A", "মারাজোনাস", "নেটিভ ব্রাজিল", "রিওস", "ব্রাজিলের কেন্দ্রীয় অঞ্চল"));
                add(new QuestionModule("অ্যামাজন নদীর কত শতাংশ পানির উৎস ব্রাজিলের মধ্যে?", "B",  "৪০%","৬০%", "৭০%", "৫০%"));
                add(new QuestionModule("অ্যামাজনের সবচেয়ে বড় প্রাণী কোনটি?", "C", "জেলিফিশ", "ডলফিন", "অ্যানাকোন্ডা", "গোবি মাছ"));
                add(new QuestionModule("নাফ নদীর উৎপত্তি স্থল কোথায় ?", "A", "মিয়ানমার", "বাংলাদেশ", "ভারত", "চীন"));
                add(new QuestionModule("নাফ নদী বাংলাদেশ ও কোন দেশের সাথে সীমান্ত ভাগাভাগি করে?", "C",  "ভারত", "নেপাল","মিয়ানমার", "ভুটান"));
                add(new QuestionModule("নাফ নদীর প্রধান শহর কোনটি?", "D", "কক্সবাজার", "ঢাকা", "চট্টগ্রাম", "টেকনাফ"));

                add(new QuestionModule("নাফ নদীর কোথায় প্রধান বন্দর অবস্থিত?", "B", "টেকনাফ", "চট্টগ্রাম", "বগুড়া", "কক্সবাজার"));

                add(new QuestionModule("পদ্মা নদীর  উৎপত্তি স্থল কোথায় ?", "A", "হিমালয়", "ব্রহ্মপুত্র নদী", "গঙ্গা নদী", "মেঘনা নদী"));
                add(new QuestionModule("মাতামুহুুরি নদীর   উৎপত্তি স্থল কোথায় ?", "C", "হিমালয়", "ব্রহ্মপুত্র নদী", "বান্দরবান", "মেঘনা নদী"));

                add(new QuestionModule("সাঙ্গু নদীর উৎপত্তি স্থল কোথায় ?", "A", "মিয়ানমার", "ভারত", "চীন", "ভুটান"));

            }
        };
        QuestionModule.createQuestionsForSubject("নদী এবং সমুদ্র", R.drawable.river_sea, questions);






        //------------- Subject 6---------------------------------------
        //===================== Chemistry ==============================
        questions = new ArrayList(){
            {

                add(new QuestionModule("কোনটি পৃথিবীর সবচেয়ে মূল্যবান ধাতু ?", "A", "প্ল্যাটিনাম", "সোনা", "রূপা", "প্যালেডিয়াম"));
                add(new QuestionModule("পৃথিবীর সবচেয়ে মূল্যবান অধাতু কোনটি ?", "B", "কার্বন", "ডায়মন্ড", "সিলিকন", "ফসফরাস"));
                add(new QuestionModule("পৃথিবীর সবচেয়ে মূল্যবান গ্যাস কোনটি ?", "C", "হাইড্রোজেন", "অক্সিজেন", "হিলিয়াম", "নাইট্রোজেন"));
                add(new QuestionModule("কোন ধাতুটি সোনা থেকেও মূল্যবান ?", "C", "রোডিয়াম", "রুপা", "প্ল্যাটিনাম", "আলুমিনিয়াম"));

                add(new QuestionModule("সবচেয়ে কঠিন অধাতু কোনটি ?", "B", "গ্রাফাইট", "ডায়মন্ড", "ক্লোরিন", "সালফার"));
                add(new QuestionModule("রুবি পাথর কোন খনিজ পদার্থের একটি জাত ?", "B", "গ্রাফাইট", "কোরান্ডাম", "ম্যাগনেসাইট", "অ্যালুমিনেট"));
                add(new QuestionModule("কোন গ্যাসটি মহাকাশ গবেষণায়  ব্যবহৃত হয়?", "A", "হিলিয়াম", "আর্গন", "নাইট্রোজেন", "অক্সিজেন"));
                add(new QuestionModule("কোন ধাতুটি রত্নধাতু হিসাবে বিবেচিত হয়?", "C", "কপার", "আয়রন", "প্ল্যাটিনাম", "টিন"));

                add(new QuestionModule("কোন ধাতুটি শিল্পে মূল্যবান ধাতু হিসেবে ব্যবহৃত হয়?", "A", "প্যালাডিয়াম", "ম্যাগনেসিয়াম", "অ্যালুমিনিয়াম", "সিলিকন"));
                add(new QuestionModule("কোন অধাতুটি মূল্যবান পাথর হিসাবে ব্যবহৃত হয়?", "B", "গ্রাফাইট", "রুবি", "সোডিয়াম", "পটাশিয়াম"));
                add(new QuestionModule("কোন গ্যাসটি বেলুনে ব্যবহৃত হয় তার হালকা ওজনের কারণে?", "A", "হিলিয়াম", "নাইট্রোজেন", "অক্সিজেন", "কার্বন ডাই অক্সাইড"));
                add(new QuestionModule("সবচেয়ে বেশি জ্বলজ্বলে কোন ধাতু ?", "D", "প্ল্যাটিনাম", "রূপা", "সোনা", "রোডিয়াম"));
                add(new QuestionModule("আর্ক ওয়েল্ডিংয়ে ব্যবহৃত হয় কোন গ্যাসটি ?", "B", "অক্সিজেন", "আর্গন", "হিলিয়াম", "নাইট্রোজেন"));
                add(new QuestionModule("কোন ধাতুটি সবচেয়ে বেশি তাপ পরিবাহী?", "D", "অ্যালুমিনিয়াম", "কপার", "লোহা", "রূপা"));
                add(new QuestionModule(" কোন অধাতুটি বিদ্যুৎ পরিবাহী ?", "C", "সালফার", "ফসফরাস", "গ্রাফাইট", "কার্বন"));
                add(new QuestionModule("কোন গ্যাসটি লাল লিটমাসকে নীল করে?", "A", "অ্যামোনিয়া", "অক্সিজেন", "নাইট্রোজেন", "কার্বন ডাই অক্সাইড"));

                add(new QuestionModule("রাসায়নিকভাবে সবচেয়ে সক্রিয় কোন অধাতুটি ?", "A", "ফ্লোরিন", "নাইট্রোজেন", "অক্সিজেন", "হাইড্রোজেন"));
                add(new QuestionModule("সবচেয়ে বেশি তাপ সংরক্ষণ করে কোন গ্যাসটি?", "C", "মিথেন", "নাইট্রোজেন", "কার্বন ডাই অক্সাইড", "আর্গন"));
                add(new QuestionModule("কোন ধাতুটি সবচেয়ে বেশি বিদ্যুৎ পরিবাহী?", "D", "অ্যালুমিনিয়াম", "কপার", "লোহা", "রূপা"));
                add(new QuestionModule("কোন ধাতুটি সবচেয়ে বেশি আকর্ষণীয় এবং বিরল?", "A", "রোডিয়াম", "প্লাটিনাম", "গোল্ড", "রূপা"));
                add(new QuestionModule("সবচেয়ে বেশি বৈদ্যুতিক প্রতিরোধ ক্ষমতা রয়েছে কার?", "C", "কার্বন", "ফসফরাস", "সালফার", "হাইড্রোজেন"));
                add(new QuestionModule("কোন গ্যাসটি সবচেয়ে বেশি হালকা ?", "D", "অক্সিজেন", "নাইট্রোজেন", "কার্বন ডাই অক্সাইড", "হিলিয়াম"));


                //=================================================

                add(new QuestionModule("পৃথিবীর সবচেয়ে বিষাক্ত গ্যাস কোনটি?", "C", "কার্বন মনোক্সাইড", "অ্যামোনিয়া", " সায়ানাইড", "মিথেন"));
                add(new QuestionModule("পৃথিবীর সবচেয়ে বিষাক্ত ধাতু কোনটি?", "B", "লিথিয়াম", "প্লুটোনিয়াম", "পারদ", "পটাশিয়াম"));
                add(new QuestionModule("কোনটি পৃথিবীর সবচেয়ে বিষাক্ত অধাতু?", "C", "কার্বন", "অক্সিজেন", "আর্সেনিক", "ফ্লোরিন"));
                add(new QuestionModule("কোন গ্যাসটি তীব্র বিষাক্ত এবং বর্ণহীন?", "A", "কার্বন মনোক্সাইড", "হাইড্রোজেন", "অক্সিজেন", "নাইট্রোজেন"));

                add(new QuestionModule("কোন গ্যাসটি অগ্নিদাহ্য এবং বিষাক্ত?", "A", "হাইড্রোজেন সালফাইড", "মিথেন", "ইথান", "প্রোপেন"));
                add(new QuestionModule("বিষাক্ত এবং প্রতিরোধযোগ্য নয় এমন ধাতু কোনটি?", "C", "আয়রন", "কপার", "ক্যাডমিয়াম", "ম্যাঙ্গানিজ"));
                add(new QuestionModule("কোনটি খুব বিষাক্ত এবং গ্রিনহাউজ গ্যাস হিসাবে কাজ করে?", "B", "কার্বন", "ফ্লোরিন", "সালফার", "ফসফরাস"));
                add(new QuestionModule("কোন গ্যাসটি বিষাক্ত এবং গন্ধহীন?", "B", "মিথেন", "কার্বন মনোক্সাইড", "ইথেন", "হাইড্রোজেন"));
                add(new QuestionModule("কোন ধাতুটি নির্দিষ্ট মাত্রার উপরে প্রাণঘাতী?", "C", "সোডিয়াম", "পটাশিয়াম", "আর্সেনিক", "ম্যাগনেসিয়াম"));
                add(new QuestionModule("কোন গ্যাসটি বিষাক্ত এবং প্রাণঘাতী?", "A", "কার্বন মনোক্সাইড", "অ্যামোনিয়া", "কার্বন ডাই অক্সাইড", "মিথেন"));
                add(new QuestionModule("কোন ধাতুটি ব্যাটারি এবং ইলেকট্রনিক্সে ব্যবহৃত হয় কিন্তু বিষাক্ত?", "C", "আলুমিনিয়াম", "তামা", "লিথিয়াম", "সোডিয়াম"));
                add(new QuestionModule("কোন গ্যাসটি বিষাক্ত এবং তীব্র গন্ধযুক্ত?", "D", "অক্সিজেন", "কার্বন ডাই অক্সাইড", "মিথেন", "হাইড্রোজেন সালফাইড"));
                add(new QuestionModule("কোন ধাতুটি বিষাক্ত এবং ত্বকের জন্য ক্ষতিকর?", "B", "কপার", "নিকেল", "লিথিয়াম", "পটাশিয়াম"));
                add(new QuestionModule("অ্যাসিড বৃষ্টি সৃষ্টির জন্য দায়ী কোনটি?", "C", "অক্সিজেন", "হাইড্রোজেন", "সালফার", "ফ্লোরিন"));


                //===========================================================

                add(new QuestionModule("আধুনিক রসায়নের জনক কে?", "A", "অ্যান্টনি ল্যাভয়সিয়ে", "রবার্ট বয়েল", "ডিমিট্রি মেন্ডেলিভ", "জোসেফ প্রিস্টলি"));
                add(new QuestionModule("পারমাণবিক তত্ত্বের জনক কে?", "B", "নিউটন", "জন ডাল্টন", "আলবার্ট আইনস্টাইন", "রাদারফোর্ড"));
                add(new QuestionModule("পর্যায় সারণির জনক কে?", "C", "রবার্ট হুক", "আভোগাদ্রো", "ডিমিট্রি মেন্ডেলিভ", "হেনরি মোজলি"));
                add(new QuestionModule("জৈব রসায়নের জনক কে ?", "D", "হান্স ক্রেবস", "ফ্রিডরিখ কেকুলে", "রবার্ট কচ", "উলার্ড গিবস"));


                add(new QuestionModule("পৃথিবীতে সবচেয়ে বেশি প্রাপ্ত মৌলিক পদার্থ কোনটি?", "A", "অক্সিজেন", "কার্বন", "হাইড্রোজেন", "নাইট্রোজেন"));
                add(new QuestionModule("পানি কোন ধরনের যৌগ?", "C", "ধাতব যৌগ", "জৈব যৌগ", "অজৈব যৌগ", "অজৈব মৌল"));
                add(new QuestionModule("পিরিয়ডিক টেবিলের প্রথম মৌল কোনটি?", "B", "হিলিয়াম", "হাইড্রোজেন", "লিথিয়াম", "কার্বন"));
                add(new QuestionModule("কোনটি বেশি ক্ষারীয় ধাতু?", "C", "ক্যালসিয়াম", "ম্যাগনেসিয়াম", "সোডিয়াম", "আয়রন"));
                add(new QuestionModule("পানির রাসায়নিক সংকেত কী?", "B", "O2", "H2O", "CO2", "H2O2"));
                add(new QuestionModule("কোনটি ধাতু নয়?", "D", "সোনা", "রূপা", "তামা", "ব্রোমিন"));


                add(new QuestionModule("কার্বনের কতটি ইলেকট্রন প্রয়োজন পূর্ণ আউটার শেল পেতে?", "C", "২", "৫", "৪", "৮"));
                add(new QuestionModule("পানির কঠিন অবস্থাকে কী বলা হয়?", "A", "বরফ", "বাষ্প", "তুষার", "ঝরনা"));
                add(new QuestionModule("অ্যামোনিয়ার রাসায়নিক সংকেত কী?", "B", "NH4", "NH3", "N2H4", "NH2"));
                add(new QuestionModule("কোনটি ভৌত পরিবর্তন নয়?", "B", "বাষ্পীভবন", "জ্বালানো", "ঘনীভবন", "গলন"));
                add(new QuestionModule("কোনটি জৈব যৌগ?", "A", "মিথেন", "জল", "কার্বন ডাই অক্সাইড", "অ্যামোনিয়া"));
                add(new QuestionModule("কোনটি সবচেয়ে শক্তিশালী অ্যাসিড?", "C", "HCl", "H2SO4", "HF", "H3PO4"));

                add(new QuestionModule("কোনটির সবচেয়ে বেশি পারমাণবিক সংখ্যা?", "B", "উরেনিয়াম", "ক্যালিফোর্নিয়াম", "রেডন", "প্লুটোনিয়াম"));
                add(new QuestionModule("কোনটি ফটোকেমিক্যাল স্মগের প্রধান উপাদান?", "A", "ওজোন", "কার্বন মনোক্সাইড", "কার্বন ডাই অক্সাইড", "সালফার ডাই অক্সাইড"));
                add(new QuestionModule("কোনটি সবচেয়ে ক্ষারীয়?", "D", "পানি", "লবণ জল", "অ্যাসিড", "লাইম ওয়াটার"));


                //=====================================================================


                add(new QuestionModule("কোনটি হাইড্রোজেনের  আইসোটোপ নয়?", "D", "ডিউটেরিয়াম", "প্রোটিয়াম", "ট্রিটিয়াম", "ক্যালসিয়াম"));
                add(new QuestionModule(" চিকিৎসা ক্ষেত্রে ক্যান্সার নির্ণয় করতে ব্যবহৃত হয় কোন আইসোটোপটি?", "A", "টেকনেশিয়াম-৯৯ম", "কার্বন-১৪", "ক্যালসিয়াম-৪০", "পটাশিয়াম-৪০"));
                add(new QuestionModule("কোন আইসোটোপটি পারমাণবিক চুল্লিতে ব্যবহৃত হয়?", "A", "ইউরেনিয়াম-২৩৫", "কার্বন-১২", "অক্সিজেন-১৮", "নাইট্রোজেন-১৫"));
                add(new QuestionModule("কোন আইসোটোপটি পরমাণু বোমার জন্য ব্যবহৃত হয়?", "A", "প্লুটোনিয়াম-২৩৯", "কার্বন-১৪", "ইউরেনিয়াম-২৩৮", "নাইট্রোজেন-১৫"));

                add(new QuestionModule("অ্যাভোগাড্রো ধ্রুবক কত?", "C", "6.02 x 10^22", "6.02 x 10^21", "6.02 x 10^23", "6.02 x 10^24"));
                add(new QuestionModule("গ্লুকোজের রাসায়নিক সংকেত কী?", "B", "C6H12O6", "C6H12O6", "C12H22O11", "C2H5OH"));
                add(new QuestionModule("কোনটি সবুজ গ্যাস?", "A", "ক্লোরিন", "নাইট্রোজেন", "অক্সিজেন", "কার্বন ডাই অক্সাইড"));
                add(new QuestionModule("সবচেয়ে শক্তিশালী হাইড্রোজেন বন্ধন কোনটি?", "D", "HF", "H2O", "NH3", "HF"));
                add(new QuestionModule("কোনটি নোবেল গ্যাস নয়?", "D", "নিওন", "আর্গন", "ক্সেনন", "হাইড্রোজেন"));
                add(new QuestionModule("কোনটি নিরপেক্ষ pH?", "C", "pH-1", "pH-0", "pH-7", "pH-14"));

                add(new QuestionModule("পিরিয়ডিক টেবিলের কোন গ্রুপে নোবেল গ্যাস আছে?", "B", "গ্রুপ ১", "গ্রুপ ১৮", "গ্রুপ ৭", "গ্রুপ ১৭"));
                add(new QuestionModule("কোনটি প্রোটনের সংখ্যা নির্দেশ করে?", "C", "নিউট্টন সংখ্যা", "ভর সংখ্যা", "পরমাণু সংখ্যা", "মোল সংখ্যা"));
                add(new QuestionModule("কোনটি শক্তি উৎপাদন করে না?", "D", "কাঠ", "কয়লা", "প্রাকৃতিক গ্যাস", "পানি"));
                add(new QuestionModule("কোনটি ধাতু নয়?", "C", "সোনা", "রূপা", "সিলিকন", "তামা"));

                add(new QuestionModule("কোন মৌলটির রাসায়নিক সংকেত 'H'?", "A", "হাইড্রোজেন", "হিলিয়াম", "হলোজেন", "হেলিয়াম"));
                add(new QuestionModule("কোন মৌলটির রাসায়নিক সংকেত 'O'?", "B",  "অর্গানিক","অক্সিজেন", "অ্যালকোহল", "অ্যাক্রিলিক"));
                add(new QuestionModule("কোন মৌলটির রাসায়নিক সংকেত 'N'?", "C",  "নিকেল", "নিয়ন","নাইট্রোজেন", "নাইট্রেট"));

                add(new QuestionModule("কোন মৌলটির রাসায়নিক সংকেত 'Cl'?", "A", "ক্লোরিন", "ক্যালসিয়াম", "ক্রোমিয়াম", "কপার"));
                add(new QuestionModule("কোন মৌলটির রাসায়নিক সংকেত 'Ca'?", "B",  "ক্রোমিয়াম","ক্যালসিয়াম", "কপার", "ক্লোরিন"));
                add(new QuestionModule("রাসায়নিক সংকেত 'Fe' কোন মৌলটির ?", "C",  "ফ্লোরিন", "ফসফরাস","আয়রন", "ফেল্ডস্পার"));
                add(new QuestionModule("কোন মৌলটির রাসায়নিক সংকেত 'Cu'?", "D",  "ক্রোমিয়াম", "সিলভার", "সোডিয়াম","তামা"));
                add(new QuestionModule("কোন মৌলটির রাসায়নিক সংকেত 'K'?", "A", "পটাসিয়াম", "ক্যালসিয়াম", "ক্রোমিয়াম", "নিখার"));
                add(new QuestionModule("রাসায়নিক সংকেত 'Ag' কোন মৌলটির ?", "B",  "সোডিয়াম", "সিলভার","সোডিয়াম ক্লোরাইড", "সোডিয়াম বাইকার্বোনেট"));

                add(new QuestionModule("রাসায়নিক সংকেত 'Au' কোন মৌলটির ?", "A", "সোনা", "আয়রন", "সিলভার", "অ্যালুমিনিয়াম"));
                add(new QuestionModule("অ্যাসিটিক অ্যাসিডের রাসায়নিক সংকেত কী?", "B", "HCl", "CH3COOH", "H2SO4", "NaOH"));
                add(new QuestionModule("পিরিয়ডিক টেবিলের কোন গ্রুপে হ্যালোজেন পাওয়া যায়?", "C", "গ্রুপ ১", "গ্রুপ ২", "গ্রুপ ১৭", "গ্রুপ ১৮"));
                add(new QuestionModule("হাইড্রোজেনের আইসোটোপ কোনটি ?", "D", "প্রোটিয়াম", "ডিউটেরিয়াম", "ট্রিটিয়াম", "সবগুলো"));
                add(new QuestionModule("প্রোটিনের মূল উপাদান কোনটি ?", "B", "কার্বোহাইড্রেট", "অ্যামিনো অ্যাসিড", "লিপিড", "ভিটামিন"));

                //=================================
                add(new QuestionModule("কার্বন এবং হাইড্রোজেন দ্বারা গঠিত যৌগের নাম কী?", "A", "হাইড্রোকার্বন", "অ্যালকোহল", "কেটোন", "ইথার"));
                add(new QuestionModule("মিথেনের রাসায়নিক সংকেত কী?", "B", "CH4", "C2H6", "C3H8", "C4H10"));
                add(new QuestionModule("কোমল পানীয়ের প্রধান উপাদান কোনটি?", "A", "কার্বনিক অ্যাসিড", "অ্যাসিটিক অ্যাসিড", "সালফিউরিক অ্যাসিড", "হাইড্রোক্লোরিক অ্যাসিড"));
                add(new QuestionModule("প্রোপেনের রাসায়নিক সংকেত কী?", "D", "C3H8", "C2H6", "CH4", "C4H10"));

                add(new QuestionModule("কোনটি একটি অ্যারোম্যাটিক যৌগ?", "D", "বেনজিন", "প্রোপেন", "ইথান", "বিউটেন"));
                add(new QuestionModule("কোন যৌগটি একটি হ্যালোজেনযুক্ত কার্বন যৌগ?", "A", "ক্লোরোফর্ম", "ইথানল", "অ্যাসিটিক অ্যাসিড", "অ্যাসিটোন"));
                add(new QuestionModule("কাকে আধুনিক রসায়নের জনক বলা হয়?", "A", "রবার্ট বয়েল", "আলবার্টাস ম্যাগনাস", "পার্সেলসাস", "জাবির ইবনে হাইয়ান"));
                add(new QuestionModule("জাবির ইবনে হাইয়ানের প্রধান অবদান কী?", "B", "আলকেমি", "ফার্মাকোলজি", "জ্যোতির্বিদ্যা", "ভূতত্ত্ব"));
                add(new QuestionModule("অ্যালকেমির উৎপত্তি কোথায়?", "A", "প্রাচীন মিশর", "প্রাচীন গ্রীস", "প্রাচীন চীন", "প্রাচীন ভারত"));
                add(new QuestionModule("আলকেমির প্রধান লক্ষ্য কী ছিল?", "B", "সোনা তৈরি করা", "জীবনদায়ী ঔষধ তৈরি করা", "নতুন মৌল আবিষ্কার করা", "পাথরের তত্ত্ব তৈরি করা"));
                add(new QuestionModule("জাবির ইবনে হাইয়ানের জন্ম কোথায়?", "C", "ইরাক", "ইরান", "সিরিয়া", "মিশর"));
                add(new QuestionModule("কাকে মধ্যযুগীয় আলকেমির জনক বলা হয়?", "D", "আলবার্টাস ম্যাগনাস", "পার্সেলসাস", "রবার্ট বয়েল", "জাবির ইবনে হাইয়ান"));

            }
        };
        QuestionModule.createQuestionsForSubject("রসায়ন বিজ্ঞান", R.drawable.chemistry, questions);


        //------------- Subject 7-------------------------------

        questions = new ArrayList(){
            {

                add(new QuestionModule("পদার্থের মৌলিক অবস্থা কয়টি?", "B", "দুটি", "তিনটি", "চারটি", "পাঁচটি"));
                add(new QuestionModule("ভর পরিমাপের একক কী?", "C", "নিউটন", "জুল", "কিলোগ্রাম", "মিটার"));

                add(new QuestionModule("নিউটনের প্রথম সূত্রটি কোনটি?", "B", "ভরবেগের সূত্র", "জড়তার সূত্র", "ক্রিয়া-প্রতিক্রিয়ার সূত্র", "গতি সংক্রান্ত সূত্র"));
                add(new QuestionModule("তাপমাত্রা পরিমাপের একক কোনটি?", "C", "ফারেনহাইট", "রোমার", "সেলসিয়াস", "ওয়াট"));
                add(new QuestionModule("একটি পদার্থের গলনাংক কোনটির মাধ্যমে নির্ধারণ করা হয়?", "D", "চাপ", "তাপমাত্রা", "ভর", "তাপ পরিবাহিতা"));
                add(new QuestionModule("ধ্বনি কোন ধরনের তরঙ্গ?", "A", "অনুদৈর্ঘ্য তরঙ্গ", "প্রস্থ তরঙ্গ", "স্থিতিস্থাপক তরঙ্গ", "মহাকর্ষীয় তরঙ্গ"));
                add(new QuestionModule("শক্তির একক কী?", "C", "নিউটন", "ওয়াট", "জুল", "কুলম্ব"));
                add(new QuestionModule("নিউটনের দ্বিতীয় সূত্রটি কোনটি?", "B", "F = m × g", "F = m × a", "F = m ÷ a", "F = m + a"));
                add(new QuestionModule("পদার্থের ঘনত্ব নির্ধারণের সূত্র কোনটি?", "A", "ঘনত্ব = ভর ÷ আয়তন", "ঘনত্ব = আয়তন ÷ ভর", "ঘনত্ব = ভর × আয়তন", "ঘনত্ব = আয়তন × ভর"));

                add(new QuestionModule("পদার্থের তিনটি অবস্থা কী কী?", "B", "তরল, কঠিন, প্লাজমা", "বায়বীয়, তরল, কঠিন", "কঠিন, প্লাজমা, বায়বীয়", "তরল, গ্যাস, প্লাজমা"));
                add(new QuestionModule("ধাতুর প্রধান বৈশিষ্ট্য কোনটি?", "A", "তাপ ও বিদ্যুৎ পরিবাহিতা", "অপরিবাহী", "অধাতু", "অ্যালয়"));
                add(new QuestionModule("আলোর প্রতিফলন কোন মাধ্যমে ঘটে?", "D", "গ্যাস", "তরল", "শক্ত পদার্থ", "ধাতু"));
                add(new QuestionModule("ভরবেগের সংরক্ষণ সূত্র কী নির্দেশ করে?", "C", "শক্তি", "বেগ", "মোট ভরবেগ অপরিবর্তিত থাকে", "জড়তা"));
                add(new QuestionModule("তরল পদার্থের অন্যতম বৈশিষ্ট্য কী?", "B", "নির্দিষ্ট আকার", "নির্দিষ্ট আয়তন", "নির্দিষ্ট আকার ও আয়তন", "অপরিবাহী"));
                add(new QuestionModule("বস্তুর ভর কোনটির সাথে সম্পর্কিত?", "C", "ওজন", "গতি", "উপাদান সংখ্যা", "ঘনত্ব"));
                add(new QuestionModule("বস্তুর গতি পরিবর্তনের জন্য কোনটি প্রয়োজন?", "A", "বল", "তাপমাত্রা", "ভর", "চাপ"));
                add(new QuestionModule("কোনটি একটি মৌলিক কণার উদাহরণ?", "B", "আণবিক", "প্রোটন", "অণু", "কেমিক্যাল"));
                add(new QuestionModule("একটি গ্যাসের চাপ কোনটির উপর নির্ভর করে?", "C", "তাপমাত্রা ও ভর", "ঘনত্ব ও আয়তন", "তাপমাত্রা ও ঘনত্ব", "আয়তন ও ভর"));
                add(new QuestionModule("ধ্বনির গতি কোন মাধ্যমে সর্বাধিক?", "A", "কঠিন", "তরল", "গ্যাস", "বায়ু"));

                add(new QuestionModule("বিশ্বের সবচেয়ে হালকা পদার্থ কোনটি?", "D", "অক্সিজেন", "কার্বন", "নাইট্রোজেন", "হাইড্রোজেন"));
                add(new QuestionModule("নিউটনের তৃতীয় সূত্রটি কী নির্দেশ করে?", "C", "ভরবেগ", "উচ্চতা", "ক্রিয়া-প্রতিক্রিয়া", "জড়তা"));

                add(new QuestionModule("পদার্থের তাপ পরিবাহিতা নির্ভর করে কোনটির উপর?", "A", "বস্তুর গঠন", "আয়তন", "তাপমাত্রা", "চাপ"));

                add(new QuestionModule("বস্তুর গতি কোনটির মাধ্যমে প্রকাশিত হয়?", "D", "বল", "ওজন", "তাপমাত্রা", "বেগ"));
                add(new QuestionModule("একটি ইলেকট্রন কোন চার্জ বহন করে?", "A", "ঋণাত্মক", "ধনাত্মক", "নিরপেক্ষ", "কোনোটিই নয়"));
                add(new QuestionModule("প্রকৃতিতে সবচেয়ে শক্তিশালী পদার্থ কোনটি?", "A", "ডায়মন্ড", "ইস্পাত", "কার্বন", "সোনা"));
                add(new QuestionModule("তড়িৎ প্রবাহের একক কী?", "C", "ভোল্ট", "ওয়াট", "অ্যাম্পিয়ার", "ওহম"));

                add(new QuestionModule("আলোক তরঙ্গের গতি নির্ধারণ করা হয় কোনটির মাধ্যমে?", "A", "মাধ্যম", "ঘনত্ব", "চাপ", "তাপমাত্রা"));
                add(new QuestionModule("তড়িৎচুম্বকীয় তরঙ্গের একটি উদাহরণ?", "C", "ধ্বনি তরঙ্গ", "জল তরঙ্গ", "রেডিও তরঙ্গ", "কম্পন তরঙ্গ"));
                add(new QuestionModule("বস্তুর ঘনত্ব নির্ধারণ করতে কোনটি ব্যবহার করা হয়?", "B", "বায়রোমিটার", "পিকনোমিটার", "থার্মোমিটার", "ভের্নিয়ার ক্যালিপার্স"));
                add(new QuestionModule("ভর ও বেগের ফলাফল কী?", "C", "তাপ", "শক্তি", "ভরবেগ", "গতি"));
                add(new QuestionModule("ধ্বনি তরঙ্গের প্রতিফলন কোনটির মাধ্যমে ঘটে?", "D", "তরল", "গ্যাস", "বায়ু", "কঠিন পদার্থ"));
                add(new QuestionModule("তরল পদার্থের স্থির গতি কোন ধরণের?", "A", "বিকল্প গতি", "স্থিতিশীল গতি", "আবর্তক গতি", "স্থির গতি"));
                add(new QuestionModule("পদার্থের তাপীয় স্থিতিস্থাপকতা কোনটির উপর নির্ভর করে?", "C", "তাপমাত্রা", "ঘনত্ব", "ভর", "চাপ"));
                add(new QuestionModule("কোনটি আলো দ্বারা সৃষ্ট?", "B", "তড়িৎ প্রবাহ", "ছায়া", "আলোর প্রতিফলন", "বিকিরণ"));

                add(new QuestionModule("ধাতব পদার্থের গলনাঙ্ক নির্ধারণ করা হয় কোনটির মাধ্যমে?", "A", "তাপমাত্রা", "তাপ", "ভর", "ঘনত্ব"));
                add(new QuestionModule("আধুনিক পদার্থবিজ্ঞানের জনক কে?", "C", "গ্যালিলিও", "নিউটন", "আইনস্টাইন", "বোর"));
                add(new QuestionModule("অধাতু পদার্থের প্রধান বৈশিষ্ট্য কোনটি?", "B", "তাপ পরিবাহিতা", "অপরিবাহী", "জীবাশ্ম", "ধাতবীয়"));
                add(new QuestionModule("তড়িৎ প্রবাহ নির্ধারণ করা হয় কোনটির মাধ্যমে?", "D", "তাপমাত্রা", "চাপ", "আয়তন", "ভোল্টেজ"));
                add(new QuestionModule("একটি ধাতু এবং অধাতু মিশ্রণে কোনটি সৃষ্টি হয়?", "B", "ধাতু", "মিশ্র ধাতু (অ্যালয়)", "অধাতু", "যৌগিক পদার্থ"));
                add(new QuestionModule("শক্তির সংরক্ষণ সূত্র কোনটি নির্দেশ করে?", "A", "শক্তি সৃষ্টি বা ধ্বংস হয় না", "শক্তি স্থানান্তরিত হয় না", "শক্তি বৃদ্ধি পায়", "শক্তি হ্রাস পায়"));
                add(new QuestionModule("নিউটনের চতুর্থ সূত্র কোনটি?", "D", "ভরবেগ", "গতি", "ক্রিয়া-প্রতিক্রিয়া", "এমন কোনো সূত্র নেই"));
                add(new QuestionModule("কোনটি একধরণের শক্তি?", "C", "বেগ", "তরঙ্গ", "তাপ", "চাপ"));
                add(new QuestionModule("কোনটি ভরের অভাবের কারণে থাকে?", "B", "তাপ", "শক্তি", "গতি", "বেগ"));
                add(new QuestionModule("তাপমাত্রা পরিমাপের যন্ত্র কোনটি?", "A", "থার্মোমিটার", "বারোমিটার", "ভোল্টমিটার", "অ্যামিটার"));

                add(new QuestionModule("পানি কি ধরনের মলিকিউল?", "A", "মেটালিক", "আইনিক", "কোভালেন্ট", "ভ্যালেন্স"));

                add(new QuestionModule("প্রোটনের চার্জ কোনটি?", "A", "ধনাত্মক", "ঋণাত্মক", "নিরপেক্ষ", "কোনোটিই নয়"));
                add(new QuestionModule("নিউট্রনের চার্জ কী?", "C", "ধনাত্মক", "ঋণাত্মক", "নিরপেক্ষ", "কোনোটিই নয়"));
                add(new QuestionModule("নিউট্রনের অবস্থান কোথায়?", "C", "ইলেকট্রন মেঘ", "প্রোটন বলয়", "নিউক্লিয়াসে", "কোনোটিই নয়"));
                add(new QuestionModule("ইলেকট্রনের ভর কত?", "D", "প্রোটনের দ্বিগুণ", "প্রোটনের সমান", "প্রোটনের অর্ধেক", "প্রোটনের তুলনায় প্রায় ১৮৩৬ গুণ হালকা"));

                add(new QuestionModule("প্রোটন কোথায় অবস্থিত?", "C", "ইলেকট্রন মেঘ", "আবরণ", "নিউক্লিয়াসে", "কোনোটিই নয়"));
                add(new QuestionModule("ইলেকট্রন কী ধরনের কণা?", "B", "ম্যাক্রোস্কোপিক", "সাব-অ্যাটমিক", "ম্যাসলেস", "কোনোটিই নয়"));
                add(new QuestionModule("নিউট্রনের আবিষ্কারক কে?", "A", "জেমস চ্যাডউইক", "এরনেস্ট রাদারফোর্ড", "নিলস বোর", "আইনস্টাইন"));
                add(new QuestionModule("একটি হাইড্রোজেন পরমাণুতে প্রোটন, নিউট্রন ও ইলেকট্রনের সংখ্যা যথাক্রমে কত?", "B", "1, 1, 1", "1, 0, 1", "1, 1, 0", "0, 1, 1"));

                add(new QuestionModule("আয়ন কী?", "B", "আণবিক", "চার্জযুক্ত কণা", "কেমিক্যাল", "অণু"));
                add(new QuestionModule("প্লাজমা কী?", "A", "চতুর্থ পদার্থের অবস্থা", "তৃতীয় পদার্থের অবস্থা", "দ্বিতীয় পদার্থের অবস্থা", "প্রথম পদার্থের অবস্থা"));
                add(new QuestionModule("প্রকৃতিতে সবচেয়ে ভারী ধাতু কোনটি?", "D", "লোহা", "সোনা", "সীসা", "ইরিডিয়াম"));

                add(new QuestionModule("আলোর বেগ কত?", "C", "3.0 x 10^8 m/s", "2.5 x 10^8 m/s", "3.0 x 10^7 m/s", "3.5 x 10^8 m/s"));


            }
        };
        QuestionModule.createQuestionsForSubject("পদার্থবিজ্ঞান", R.drawable.physics, questions);



        //------------- Subject 8
        questions = new ArrayList(){
            {



                add(new QuestionModule("ডিএনএ কিসের তৈরি?", "B", "প্রোটিন", "নিউক্লিক অ্যাসিড", "কার্বোহাইড্রেট", "লিপিড"));
                add(new QuestionModule("যকৃত কোন অংশে অবস্থিত?", "C", "মস্তিষ্কে", "হৃদয়ে", "পাঁজরের নিচে", "কিডনিতে"));

                add(new QuestionModule("এককোষী প্রাণী কোনটি?", "D", "মানুষ", "ফাঙ্গাস", "পোকা", "অ্যামিবা"));
                add(new QuestionModule("অক্সিজেন কী জন্য প্রয়োজন?", "A", "খাদ্য পরিশোধন", "শ্বাস গ্রহণ", "প্রতিরোধ ব্যবস্থা", "অসীম শক্তি"));
                add(new QuestionModule("প্ল্যান্টস' লিভারেজ কিসের দ্বারা সম্পন্ন হয়?", "B", "পটাসিয়াম", "জল", "সোডিয়াম", "ফসফরাস"));
                add(new QuestionModule("মিউটেশন কাকে বোঝায়?", "C",  "শারীরিক আঘাত", "সংক্রামক রোগ","জেনেটিক পরিবর্তন", "পুষ্টির অভাব"));
                add(new QuestionModule("হৃৎপিণ্ডের প্রধান কাজ কি?", "D", "রক্ত পাম্প করা", "বিষণ্নতা হ্রাস", "হরমোন উৎপাদন", "অগ্ন্যাশয় কন্ট্রোল"));
                add(new QuestionModule("মাইটোকন্ড্রিয়া কী কাজ করে?", "A", "শক্তি উৎপাদন", "উপাদান গঠন", "ডিএনএ সৃষ্টি", "প্রোটিন সংশ্লেষণ"));
                add(new QuestionModule("রক্তে শ্বেতকণিকার ভূমিকা কি?", "B",  "অক্সিজেন পরিবহন","রোগ প্রতিরোধ", "পুষ্টি সরবরাহ", "দ্রব্য বিপাক"));
                add(new QuestionModule("কোনো জীবের কোষের আকার কি নির্ধারণ করে?", "C",  "জলবায়ু", "জেনেটিক কোড","কোষের কার্যাবলী", "পুষ্টির অভাব"));

                add(new QuestionModule("কোন প্রকারের রাসায়নিক পরিবর্তনকে এনজাইম বলা হয় ?", "A", "বায়োকেমিক্যাল", "ফিজিক্যাল", "সাবস্ট্যান্স", "লিপিড"));
                add(new QuestionModule("ডিএনএ-এর পুরো নাম কি?", "B", "ডক্সিরাইবোনিউক্লেইক অ্যাসিড", "ডিঅক্সিরাইবোনিউক্লিক অ্যাসিড", "ডাইঅক্সিরাইবোনিউক্লেইক অ্যাসিড", "ডিফেন্ডার নিউক্লেইক অ্যাসিড"));
                add(new QuestionModule("একটি কোষের নিউক্লিয়াসের প্রধান ভূমিকা কি?", "C",  "পুষ্টি গ্রহণ", "শক্তি উৎপাদন","জিনগত উপাদান ধারণ", "প্রোটিন প্রক্রিয়া"));
                add(new QuestionModule("প্রাণী কোষের বাইরে কোন স্তর থাকে?", "D", "মাইটোকন্ড্রিয়া", "সাইটোপ্লাজম", "নিউক্লিয়াস", "কোষ ঝিল্লি"));


                add(new QuestionModule("বীজের স্তরগুলো কী কী?", "B",  "কোর, টিস্যু", "অ্যামব্রিও, এন্ডোসপার্ম", "মেমব্রেন, সাইটোপ্লাজম", "নিউক্লিয়াস, কোষপত্র"));
                add(new QuestionModule("কোষ বিভাজনের  প্রধান প্রকার কোন দুটি?", "C",  "ফোটোসিন্থেসিস ও ক্যালভিন চক্র", "অক্সিডেশন ও রিডাকশন","মাইটোসিস ও মিওসিস","রিপ্রোডাকশন ও রিজেনারেশন"));
                add(new QuestionModule("ডিএনএ এর কাজ কি?", "A", "জেনেটিক ইনফরমেশন সংরক্ষণ", "শক্তি উৎপাদন", "মেটাবলিজম নিয়ন্ত্রণ", "হরমোন নিয়ন্ত্রণ"));
                add(new QuestionModule("বৃক্ষের ফটোসিন্থেসিসের জন্য কোন উপাদান প্রয়োজন?", "A", "সূর্যালোক", "পানি", "বায়ু", "জমি"));
                add(new QuestionModule("কোন কোষ শক্তি উৎপাদন করে?", "B",  "রাইবোসোম", "মাইটোকন্ড্রিয়া","রিডোক্স", "এন্ডোপ্লাজমিক রেটিকুলাম"));
                add(new QuestionModule("লবণ কি ধরনের রাসায়নিক যৌগ?", "C",  "কোভালেন্ট", "মেটালিক", "আয়নিক","ওয়াটার সলিউবল"));
                add(new QuestionModule("শ্বেতকণিকার প্রধান কাজ কি?", "A", "রোগ প্রতিরোধ", "অক্সিজেন পরিবহন", "শক্তি উৎপাদন", "নিউক্লিক অ্যাসিড উৎপাদন"));
                add(new QuestionModule("মেটাবলিজমের  ভূমিকা কী?", "B",  "বিকিরণ","শক্তি উৎপাদন","উপাদান গঠন", "অক্সিডেশন"));
                add(new QuestionModule("উদ্ভিদের কোন অংশ শ্বাস-প্রশ্বাসে সাহায্য করে?", "D",  "মূল", "ফুল", "ফল","পাতা"));
                add(new QuestionModule("কোন প্রকারের রক্তের কণিকা কোষ পুনর্গঠন করে?", "A", "লসিকা কণিকা", "শ্বেত কণিকা", "প্লেটলেট", "প্লাজমা"));
                add(new QuestionModule("কোন এনজাইম আমিষ হজমে সহায়ক?", "B",  "অ্যামিলেজ","পেপসিন", "লিপেজ", "ট্রিপসিন"));
                add(new QuestionModule("একটি সেল মেমব্রেনের প্রধান উপাদান কি?", "C",  "প্রোটিন", "ডিএনএ", "লিপিড","কার্বোহাইড্রেট"));
                add(new QuestionModule("বৃক্ষের পাতার কাজ কি?", "B",  "শক্তি উৎপাদন","ফটোসিন্থেসিস", "মেটাবলিজম", "রোগ প্রতিরোধ"));
                add(new QuestionModule("ইনফেকশন প্রতিরোধে কোন কোষ গুরুত্বপূর্ণ?", "C",  "লাল কণিকা", "প্লেটলেট","শ্বেতকণিকা", "মাস্কুলার টিস্যু"));
                add(new QuestionModule("কোন কোষের অভ্যন্তরীণ অংশে কোষবস্ত্র থাকে?", "A", "বৃক্ষের কোষ", "প্রাণীর কোষ", "ফাঙ্গাস কোষ", "ব্যাকটেরিয়া কোষ"));
                add(new QuestionModule("উদ্ভিদের কোন অংশ প্রধানত জল শোষণ করে?", "B",  "পাতা","মূল", "ফুল", "ফল"));
                add(new QuestionModule("ফোটোসিন্থেসিসের জন্য কোন প্রক্রিয়া প্রয়োজন?", "C",  "রক্ত", "অক্সিজেন","সূর্যালোক", "অ্যামিনো অ্যাসিড"));
                add(new QuestionModule("মানুষের শরীরে কোন কোষ দেহের অভ্যন্তরীণ পরিবেশ নিয়ন্ত্রণ করে?", "D", "শ্বেতকণিকা", "লাল কণিকা", "প্লেটলেট", "নিউরন"));
                add(new QuestionModule("যকৃত প্রধানত কি কাজ করে?", "B",  "পানির শোষণ","দেহের বর্জ্য অপসারণ", "শ্বাস নিয়ন্ত্রণ", "প্রোটিন উৎপাদন"));

                add(new QuestionModule("শ্বেতকণিকা কিসের অংশ?", "B",  "মাংসপেশি", "রক্ত","হাড়", "চামড়া"));
                add(new QuestionModule("ফটোসিন্থেসিসে প্রধান উপাদান কি?", "C",  "অক্সিজেন", "পানি","সূর্যালোক", "ডিএনএ"));
                add(new QuestionModule("ডিএনএ মলিকিউল কোন ধরনের?", "A", "ডাবল হেলিক্স", "সিঙ্গল হেলিক্স", "ক্লাস্টার", "স্কোয়ার"));
                add(new QuestionModule("ফ্যাট কী দ্বারা গঠিত?", "A", "গ্লিসারল ও ফ্যাটি অ্যাসিড", "এমিনো অ্যাসিড", "নিউক্লিওটিড", "শর্করা"));

                add(new QuestionModule("ফাঙ্গাসে কোন ধরনের কোষ থাকে?", "D",  "প্রোক্যারিওটিক", "অর্থোপড", "ফাইটোপ্লাঙ্কটন","ইউক্যারিওটিক"));
                add(new QuestionModule("একটি কোষে 'রাইবোসোম' কি করে?", "A", "প্রোটিন সংশ্লেষণ", "শক্তি উৎপাদন", "ডিএনএ সঞ্চয়", "মেটাবলিজম নিয়ন্ত্রণ"));
                add(new QuestionModule("উদ্ভিদের পাতা কিসে পূর্ণ থাকে?", "B",  "লিপিড","ক্লোরোফিল", "ক্লোরোপ্লাস্ট", "ফাইটোক্রোম"));


                add(new QuestionModule("মানবদেহের সবচেয়ে বড় অঙ্গ কোনটি?", "D", "হার্ট", "যকৃত", "পেট", "ত্বক"));
                add(new QuestionModule("মানবদেহের কোন অংশ অক্সিজেন পরিবহন করে?", "C", "হৃদপিণ্ড", "মস্তিষ্ক", "রক্ত", "কিডনি"));
                add(new QuestionModule("মানবদেহে সবচেয়ে বড় হাড় কোনটি?", "A", "ফেমার", "হিউমেরাস", "টিবিয়া", "প্যাটেলা"));


                add(new QuestionModule("পেটের প্রধান কাজ কি?", "A", "খাদ্য হজম", "অক্সিজেন পরিবহন", "রক্ত প্রবাহ", "পেশী সঙ্কোচন"));

                add(new QuestionModule("কোন অঙ্গ পিত্ত উৎপাদন করে?", "C", "হার্ট", "কিডনি", "যকৃত", "ফুসফুস"));
                add(new QuestionModule("মানবদেহের কোন অংশ মূত্র নিঃসরণ করে?", "B", "লিভার", "কিডনি", "হার্ট", "ফুসফুস"));
                add(new QuestionModule("মানবদেহের কোন অংশে হৃদস্পন্দন নিয়ন্ত্রণ করা হয়?", "A", "মস্তিষ্ক", "পেট", "কিডনি", "যকৃত"));

                add(new QuestionModule("বিশ্বের সবচেয়ে বড় প্রানী কোনটি?", "A", "ব্লু হোয়েল", "আফ্রিকান হাতি", "জিরাফ", "গণ্ডার"));

                add(new QuestionModule("পৃথিবীর সবচেয়ে বড় পাখি কোনটি?", "C", "পেঙ্গুইন", "গোল্ডেন ঈগল", "অস্ট্রিচ", "মোরগ"));


            }

        };
        QuestionModule.createQuestionsForSubject("জীববিজ্ঞান", R.drawable.biology, questions);



        //------------- Subject 7
        questions = new ArrayList(){

            {
                //===================
                
                add(new QuestionModule("বাংলাদেশের সর্বোচ্চ পর্বত শৃঙ্গ কোনটি?", "A", "তাজিংডং", "কেওক্রাডং", "সাকা হাফং", "লামা"));
                add(new QuestionModule("কেওক্রাডং পাহাড় কোন জেলায় অবস্থিত?", "B",  "খাগড়াছড়ি", "বান্দরবান","রাঙ্গামাটি", "চট্টগ্রাম"));
                add(new QuestionModule("সাকা হাফং পর্বতের উচ্চতা কত?", "C", "৩২০০ ফুট", "৩৪০০ ফুট", "৩৪৬৫ ফুট", "৩১০০ ফুট"));
                add(new QuestionModule("তাজিংডং পর্বতকে অন্য  কী নামে ডাকা হয়?", "D",  "সিতাকুন্ড", "মাহাপুরুষ", "মাহামুনি","বিজয়"));
                add(new QuestionModule("বান্দরবানের কোন উপজেলায় তাজিংডং পর্বত অবস্থিত?", "A",  "রুমা","থানচি", "রোয়াংছড়ি", "লামা"));
                add(new QuestionModule("কেওক্রাডং পাহাড়ের উচ্চতা কত?", "B", "৩১৭২ ফুট", "৩২৩৫ ফুট", "৩২৮০ ফুট", "৩৪০০ ফুট"));
                add(new QuestionModule("সাকা হাফং পর্বত কোন উপজেলায় অবস্থিত?", "C",  "রুমা", "রাঙ্গামাটি","থানচি", "লামা"));
                add(new QuestionModule("বান্দরবানের পাহাড়গুলোর মধ্যে কোনটি সবচেয়ে উঁচু?", "D", "কেওক্রাডং", "রিমাক্রি", "নীলগিরি", "তাজিংডং"));
                add(new QuestionModule("বাংলাদেশের কোন জেলায় সাকা হাফং পর্বত অবস্থিত?", "A", "বান্দরবান", "রাঙ্গামাটি", "খাগড়াছড়ি", "চট্টগ্রাম"));
                add(new QuestionModule("বাংলাদেশের দ্বিতীয় সর্বোচ্চ পর্বত কোনটি?", "C",  "কেওক্রাডং", "তাজিংডং","সাকা হাফং", "রোয়াংছড়ি"));
                add(new QuestionModule("বাংলাদেশের দার্জিলিং বলা হয় কাকে ?", "C",  "কেওক্রাডং", "তাজিংডং","নীলগিরি", "রোয়াংছড়ি"));
                add(new QuestionModule("নীলগিরি পাহাড় কোন জেলায় অবস্থিত?", "D", "খাগড়াছড়ি", "রাঙ্গামাটি", "চট্টগ্রাম", "বান্দরবান"));
                add(new QuestionModule("কেওক্রাডং পাহাড়ের কাছাকাছি কোন নদী প্রবাহিত?", "A", "সাঙ্গু নদী", "কর্ণফুলী নদী", "গঙ্গা নদী", "তিস্তা নদী"));
                add(new QuestionModule("বাংলাদেশের কোন উপজেলায় রোয়াংছড়ি পর্বত অবস্থিত?", "C", "লামা", "থানচি", "রোয়াংছড়ি", "রুমা"));
                add(new QuestionModule("বাংলাদেশের সবচেয়ে বড় কোন পার্বত্য জেলা?", "D",  "বান্দরবান", "খাগড়াছড়ি", "চট্টগ্রাম","রাঙ্গামাটি"));
                add(new QuestionModule("বান্দরবানের কোন পাহাড় পর্যটকদের জন্য আকর্ষণীয়?", "B", "কেওক্রাডং", "নীলগিরি", "তাজিংডং", "সাকা হাফং"));
                add(new QuestionModule("কোন পাহাড়কে নীলপাহাড় বলা হয়?", "D", "বিজয় শিখর", "তাজিংডং", "কেওক্রাডং", "নীলগিরি"));
                add(new QuestionModule("নীলগিরি পাহাড়ের উচ্চতা কত?", "A", "২২০০ ফুট", "২৪০০ ফুট", "২৬০০ ফুট", "২৮০০ ফুট"));
                add(new QuestionModule("বান্দরবানের কোন পর্বতকে 'বাংলাদেশের ছাদ' বলা হয়?", "B", "তাজিংডং", "কেওক্রাডং", "সাকা হাফং", "নীলগিরি"));
                add(new QuestionModule("বাংলাদেশের কোন জেলায় নীলাচল পাহাড় অবস্থিত?", "C", "রাঙ্গামাটি", "খাগড়াছড়ি", "বান্দরবান", "চট্টগ্রাম"));
                add(new QuestionModule("কোন পাহাড়ের চূড়ায় বৌদ্ধ মন্দির অবস্থিত?", "D", "কেওক্রাডং", "তাজিংডং", "রাঙ্গামাটি", "নীলগিরি"));
                add(new QuestionModule("কোন পাহাড়ে 'দার্জিলিং অফ বাংলাদেশ' বলা হয়?", "A", "নীলগিরি", "কেওক্রাডং", "তাজিংডং", "সাকা হাফং"));
                add(new QuestionModule("রিমাক্রি ঝর্নার কাছাকাছি কোন পর্বত অবস্থিত?", "B", "কেওক্রাডং", "তাজিংডং", "সাকা হাফং", "নীলগিরি"));
                add(new QuestionModule("সীতাকুন্ড পাহাড় কোন জেলায় অবস্থিত?", "B",  "কক্সবাজার","চট্টগ্রাম", "বান্দরবান", "রাঙ্গামাটি"));
                add(new QuestionModule("গারো পাহাড় বাংলাদেশের কোন জেলায় অবস্থিত?", "B",  "কক্সবাজার","ময়মনসিংহ ", "বান্দরবান", "রাঙ্গামাটি"));
                add(new QuestionModule("বাংলাদেশে কোন পর্বত  চা-বাগানের জন্য বিখ্যাত?", "C",  "চিকডাউড়া", "হিমছড়ি", "লাউয়াছড়া","খাসিয়া"));
                add(new QuestionModule("চন্দ্রনাথ পাহাড় বাংলাদেশের কোন জেলায় অবস্থিত?", "B", "বান্দরবান", "চট্টগ্রাম", "রাঙ্গামাটি",  "খাগড়াছড়ি"));
                add(new QuestionModule("চন্দ্রনাথ পাহাড়ের চূড়ায় কোন উপসনালয় আছে?", "C", "বৌদ্ধ মন্দির", "মসজিদ", " চন্দ্রনাথ মন্দির", "গির্জা"));

                add(new QuestionModule("বিশ্বের সর্বোচ্চ পর্বতশৃঙ্গ কোনটি?", "A", "এভারেস্ট", "কাঞ্চনজঙ্ঘা", "কে২", "মাকালু"));
                add(new QuestionModule("কাঞ্চনজঙ্ঘা পর্বতটি কোন দেশের মধ্যে অবস্থিত?", "B", "চীন ও নেপাল", "নেপাল ও ভারত", "ভারত ও চীন ", "ভারত ও ভুটান"));
                add(new QuestionModule("আফ্রিকার সর্বোচ্চ পর্বতশৃঙ্গ কোনটি?", "C", "আকনকাগুয়া", "মাকালু", "কিলিমাঞ্জারো", "মাউন্ট এলব্রাস"));
                add(new QuestionModule("রকিস পর্বতমালা কোন মহাদেশে অবস্থিত?", "A", "উত্তর আমেরিকা", "দক্ষিণ আমেরিকা", "ইউরোপ", "এশিয়া"));
                add(new QuestionModule("অ্যামাজন রেইনফরেস্টের নিকটবর্তী পর্বত কোনটি?", "C",  "হিমালয়", "রোরাাইমা","আন্দিজ", "আটলাস"));
                add(new QuestionModule("ইউরোপের সর্বোচ্চ পর্বত কোনটি?", "B", "মন্ট ব্ল্যাঙ্ক", "মাউন্ট এলব্রাস", "পাইরেনিজ", "ডোলোমাইট"));
                add(new QuestionModule("মাউন্ট এলব্রাস কোন দেশে অবস্থিত?", "D", "ফ্রান্স", "স্পেন", "ইতালি", "রাশিয়া"));
                add(new QuestionModule("আন্দিজ পর্বতমালা কোন মহাদেশে অবস্থিত?", "C", "এশিয়া", "উত্তর আমেরিকা", "দক্ষিণ আমেরিকা", "ইউরোপ"));
                add(new QuestionModule("ম্যাকিনলে পর্বতটি বর্তমানে কোন নামে পরিচিত?", "C", "এভারেস্ট", "মন্ট ব্ল্যাঙ্ক", "ডেনালি", "আকনকাগুয়া"));
                add(new QuestionModule("পামির পর্বতশ্রেণী কোন দেশে অবস্থিত?", "B", "চীন", "তাজিকিস্তান", "রাশিয়া", "ভারত"));
                add(new QuestionModule("রকি পর্বতমালা কোন দেশের মধ্য দিয়ে বিস্তৃত?", "A", "যুক্তরাষ্ট্র", "চীন", "অস্ট্রেলিয়া", "দক্ষিণ আফ্রিকা"));
               add(new QuestionModule("এভারেস্ট পর্বতের উচ্চতা কত?", "C", "৭৮৬১ মিটার", "৭৯৪৫ মিটার", "৮৮৪৮ মিটার", "৯১২৩ মিটার"));
               add(new QuestionModule("কিলিমাঞ্জারো পর্বতের অবস্থান কোথায়?", "C", "কেনিয়া", "উগান্ডা", "তানজানিয়া", "ইথিওপিয়া"));
                add(new QuestionModule("কাঞ্চনজঙ্ঘা কোন পর্বতমালার অংশ?", "D", "আন্দিজ", "আল্পস", "কারাকোরাম", "হিমালয়"));
                add(new QuestionModule("মাউন্ট ফুজি কোন দেশে অবস্থিত?", "B", "চীন", "জাপান", "দক্ষিণ কোরিয়া", "মঙ্গোলিয়া"));
                add(new QuestionModule("পাকিস্তানের কোন পর্বত 'K2' নামে পরিচিত?", "D", "গিলগিট", "মুর্গা", "ভূবনেশ্বর", "চোগরি"));
                add(new QuestionModule("পাকিস্তানের সর্বোচ্চ পর্বত কোনটি?", "C", "চোগরি", "নাঙ্গা পার্বত", "K2", "ব্রড পিক"));
                add(new QuestionModule("'K2' পর্বত  কোন দেশে অবস্থিত?", "B", "চীন", "পাকিস্তান", "দক্ষিণ কোরিয়া", "মঙ্গোলিয়া"));
                add(new QuestionModule("মাউন্ট উইলহেম কোন দেশে অবস্থিত?", "C", "ফিজি", "মালয়েশিয়া", "পাপুয়া নিউ গিনি", "নিউজিল্যান্ড"));
                add(new QuestionModule("কিলিমাঞ্জারো পর্বতের মোট কতটি শৃঙ্গ আছে?", "D", "৩টি", "৪টি", "২টি", "৫টি"));
                add(new QuestionModule("সর্বাধিক উচ্চতা সম্পন্ন আগ্নেয় পর্বত কোনটি?", "A", "ওজোস ডেল সালাদো", "এটনা", "কিলিমাঞ্জারো", "ফুজি"));
                add(new QuestionModule("কুনলুন পর্বতের মোট দৈর্ঘ্য কত?", "B", "২৫০০ কিলোমিটার", "৩০০০ কিলোমিটার", "৩৫০০ কিলোমিটার", "৪০০০ কিলোমিটার"));
                add(new QuestionModule("আফ্রিকার কোন দেশে রোরাাইমা পর্বত অবস্থিত?", "C", "মালাউই", "জিম্বাবুয়ে", "উগান্ডা", "নামিবিয়া"));
                add(new QuestionModule("ক্যারাকোরাম রেঞ্জের সবচেয়ে উঁচু পর্বত কোনটি?", "D", "গ্যাশারব্রাম", "লটসে", "নাঙ্গা পারবত", "কে২"));
                add(new QuestionModule("মাচু পিচু পর্বতশ্রেণী কোন দেশের অন্তর্ভুক্ত?", "A", "পেরু", "চিলি", "আর্জেন্টিনা", "ভেনেজুয়েলা"));
                add(new QuestionModule("আলপাইন পর্বতের সর্বোচ্চ শৃঙ্গ কোনটি?", "B", "মন্ট ব্ল্যাঙ্ক", "ম্যাটারহর্ন", "ইউংফ্রাউ", "ডোলোমাইট"));
                add(new QuestionModule("ওজোস ডেল সালাদো কোন পর্বতশ্রেণীতে অবস্থিত?", "C", "হিমালয়", "আল্পস", "আন্ডেস", "রকি"));
                add(new QuestionModule("অ্যান্টার্কটিকার সবচেয়ে উঁচু পর্বত কোনটি?", "D", "মাউন্ট কসিয়াস্কো", "মাউন্ট এলব্রাস", "মাউন্ট ফুজি", "ভিনসন ম্যাসিফ"));

                add(new QuestionModule("'সপ্তভাই ঝর্ণা' নামে পরিচিত ঝর্ণাটি বাংলাদেশের কোন জেলায় অবস্থিত ?", "B",  "কক্সবাজার", "বান্দরবান","সাজেক", "খাগড়াছড়ি"));
                  // 1 to 50
                add(new QuestionModule("বিশ্বের সবচেয়ে দীর্ঘ পর্বতশ্রেণীর নাম কী?", "B", "হিমালয়", "অ্যান্ডিজ", "রকি", "অ্যালপস"));
                add(new QuestionModule("পৃথিবীর সবচেয়ে উচ্চতম সক্রিয় আগ্নেয়গিরির নাম কী?", "A", "ওহোস দেল সালাদো", "মাউন্ট ভসুভিয়াস", "মাউন্ট ফুজি", "মাউন্ট পারিনাচা"));
                add(new QuestionModule("পৃথিবীর সবচেয়ে শীতল পাহাড়ের নাম কী?", "A", "মাউন্ট ভিনসন", "মাউন্ট এভারেস্ট", "মাউন্ট ম্যাকিনলি", "মাউন্ট ফুজি"));
                add(new QuestionModule("পৃথিবীর সবচেয়ে দুর্গম পর্বতশৃঙ্গ কোনটি?", "A", "মাউন্ট এভারেস্ট", "মাউন্ট ম্যাকিনলি", "মাউন্ট ফুজি", "মাউন্ট রেইনার"));
                add(new QuestionModule("বিশ্বের সবচেয়ে সক্রিয় আগ্নেয়গিরি কোথায় অবস্থিত?", "A", "হাওয়াই (যুক্তরাষ্ট্র)", "ইটালি", "জাপান", "পেরু"));

            }
        };
        QuestionModule.createQuestionsForSubject("পাহাড় ও পর্বত", R.drawable.mountain, questions);




        //------------- Subject 7
        questions = new ArrayList(){
            {

                add(new QuestionModule("গ্রেট ওয়াল অব চায়না কোথায় অবস্থিত?", "A", "চীন", "জাপান", "ভারত", "মেক্সিকো"));
                add(new QuestionModule("মাচু পিচু কোন দেশে অবস্থিত?", "B",  "বোলিভিয়া","পেরু", "কলম্বিয়া", "ইকুয়েডর"));
                add(new QuestionModule("কলোসিয়াম কোথায় অবস্থিত?", "D",  "গ্রিস", "মিশর", "স্পেন","ইতালি"));
                add(new QuestionModule("পেট্রা কোন দেশে অবস্থিত?", "B",  "সিরিয়া","জর্ডান","ইরাক", "ইরান"));
                add(new QuestionModule("চিচেন ইটজা কোন দেশে অবস্থিত?", "C",  "জাপান", "পেরু", "মেক্সিকো","ভারত"));
                add(new QuestionModule("মোহেঞ্জো-দারো কোথায় অবস্থিত?", "D", "পাকিস্তান", "ভারত", "বাংলাদেশ", "নেপাল"));
                add(new QuestionModule("পম্পেই নগরী কোথায় অবস্থিত?", "D", "ইতালি", "গ্রিস", "মিশর", "স্পেন"));
                add(new QuestionModule("হাগিয়া সোফিয়া কোথায় অবস্থিত?", "A", "তুরস্ক", "গ্রিস", "মিশর", "ইতালি"));

                add(new QuestionModule("ইস্টার আইল্যান্ড স্ট্যাচু কোথায় অবস্থিত?", "A", "চিলি", "বোলিভিয়া", "পেরু", "কলম্বিয়া"));
                add(new QuestionModule("মিশরের সবচেয়ে বড় পিরামিড কোনটি?", "A", "গ্রেট পিরামিড অফ গিজা", "পিরামিড অফ সাখারা", "পিরামিড অফ দ্য সান", "পিরামিড অফ মুন"));
                add(new QuestionModule("ইস্টার আইল্যান্ড স্ট্যাচুর  নাম কী?", "C",  "মহিপ", "স্টোনহেঞ্জ","মোআই", "মাচু পিচু"));
                add(new QuestionModule("বিশ্বের সবচেয়ে পুরানো শহরটির নাম কী?", "C", "জেরুসালেম", "বাবিল", "এথেন্স", "রোম"));
                add(new QuestionModule("রোমান প্রাচীন ধ্বংসাবশেষের নাম কী?", "A", "কোলসিয়াম", "পম্পেই", "বাথ", "ভ্যালি অফ কিংস"));
                add(new QuestionModule("পেরুতে মাচু পিচু কোথায় অবস্থিত?", "B",  "মেসোপটেমিয়া","অ্যান্ডেস পর্বতমালা", "মিসর", "আফ্রিকা"));
                add(new QuestionModule("মোহেঞ্জো-দারো কোথায় অবস্থিত?", "B",  "ভারত","পাকিস্তান", "বাংলাদেশ", "নেপাল"));

                //Bangladesh Historical Place

                add(new QuestionModule("তাজমহল কোন শহরে অবস্থিত?", "A", "আগ্রা", "দিল্লি", "জয়পুর", "কলকাতা"));
                add(new QuestionModule("কুতুব মিনার কোন শহরে অবস্থিত?", "B",  "মুম্বাই", "দিল্লি","চেন্নাই", "কলকাতা"));
                add(new QuestionModule("মুজিব নগর কোথায় অবস্থিত?", "B",  "ধানমন্ডি", "মেহেরপুর","ঢাকা", "কলকাতা"));
                add(new QuestionModule("মহামুনি বিহার কোথায় অবস্থিত?", "B",  " নোয়াখালীর বেগমগঞ্জে", " চট্টগ্রামের রাউজানে","ঢাকার সদরঘাট", "কুমিল্লার ময়নামতিতে"));
                add(new QuestionModule("পাহাড়পুরের বৌদ্ধ বিহারটি কি নামে পরিচিত?", "B",  "পাহাড়পুর বিহার", "সোমপুর বিহার","সীতাকোট বিহার", "শালবন বিহার"));
                add(new QuestionModule("বাংলাদেশের সবচেয়ে প্রাচীন বৌদ্ধ বিহার কোনটি?", "C",  "পাহাড়পুর বিহার", "সোমপুর বিহার","সীতাকোট বিহার", "শালবন বিহার"));
                add(new QuestionModule("শালবন বিহার কে তৈরী করেন?", "B",  " রাজা আনন্দ দেব", "রাজাধিরাজ ভবদেব"," শ্রী ধর্মপাল দেব", " শ্রী গোপাল দেব"));
                add(new QuestionModule("সোমপুর বিহার কে তৈরী করেন?", "C",  " রাজা আনন্দ দেব", "রাজাধিরাজ ভবদেব"," শ্রী ধর্মপাল দেব", " শ্রী গোপাল দেব"));
                add(new QuestionModule("শালবন বিহার কে তৈরী করেন?", "B",  " রাজা আনন্দ দেব", "রাজাধিরাজ ভবদেব"," শ্রী ধর্মপাল দেব", " শ্রী গোপাল দেব"));
                add(new QuestionModule("লালমাই পাহাড় কোন জেলায় অবস্থিত?", "B", "সিলেট", "কুমিল্লা", "বান্দরবান", "রাঙ্গামাটি"));
                add(new QuestionModule("উয়ারী বটেশ্বর কোথায় অবস্থিত?", "A", "নরসিংদী","ঢাকা", "গাজীপুর",  "টাঙ্গাইল"));
                add(new QuestionModule("বীরশ্রেষ্ঠ মুন্সী আবদুর রউফ জাদুঘর কোথায় অবস্থিত?", "B", "মাদারিপুর", "ফরিদপুর", "ঢাকা", "চট্টগ্রাম"));

                add(new QuestionModule("বাংলাদেশের সবচেয়ে প্রাচীনতম মসজিদের নাম কী?", "A", "জামা মসজিদ","শাহি মসজিদ", "লালবাগ কেল্লা", "বাহাদুর শাহ মসজিদ"));
                add(new QuestionModule("বাংলাদেশের সবচেয়ে বড় প্রাচীনতম মন্দিরের নাম কী?", "A", "ঢাকেশ্বরী মন্দির","ধর্মতলা মন্দির", "জোড়াসাঁকো মন্দির", "বল্লাল সেন মন্দির"));
                add(new QuestionModule("বাংলাদেশের সবচেয়ে  প্রাচীনতম মন্দিরের নাম কী?", "C", "ঢাকেশ্বরী মন্দির","ধর্মতলা মন্দির", "কোটা মন্দির", "বল্লাল সেন মন্দির"));
                add(new QuestionModule("ঢাকার লালবাগ কেল্লা কার দ্বারা নির্মিত?", "B", "নবাব শায়েস্তা খাঁ", "মুহাম্মদ আজম শাহ", "সুবেদার ইসলাম খাঁ", "সম্রাট আকবর"));
                add(new QuestionModule("পুরান ঢাকার ঐতিহাসিক স্থান কোনটি?", "C", "বাবরি মসজিদ", "বাঁশের কেল্লা", "আহসান মঞ্জিল", "ডাকবাঙ্গলা"));
                add(new QuestionModule("বাংলাদেশের সবচেয়ে বড় প্রাচীন মন্দির কোথায় অবস্থিত?", "D", "পঞ্চগড়", "বগুড়া", "রাজশাহী","ঢাকা"));
                add(new QuestionModule("বাংলাদেশের ঐতিহাসিক স্থান সোনারগাঁও কোন জেলার অন্তর্গত?", "A", "নারায়ণগঞ্জ", "ঢাকা", "চট্টগ্রাম", "খুলনা"));

                add(new QuestionModule("'লালবাগ কেল্লা' কোন মুঘল সম্রাটের শাসনকালে নির্মিত?", "B", "আকবর", "আউরঙ্গজেব","শাহজাহান", "জাহাঙ্গীর"));
                add(new QuestionModule("'শাহজালাল মাজার' এর প্রতিষ্ঠাতা কে?", "A", "শাহজালাল", "শাহ পরান", "শাহ মখদুম", "শাহজাহান"));
                add(new QuestionModule("'আহসান মঞ্জিল' কোন নওয়াবের নামে পরিচিত?", "C", "নওয়াব স্যার সলিমুল্লাহ", "নওয়াব খাজা হাবিবুল্লাহ", "নওয়াব খাজা আহসানউল্লাহ", "নওয়াব খাজা শামসুল্লাহ"));
                add(new QuestionModule("'বিবি চিনি মসজিদ' কোন সম্রাজ্ঞীর নামে নির্মিত?", "A", "বিবি চিনি", "বিবি রূপমতি", "বিবি আয়েশা", "বিবি খদিজা"));
                add(new QuestionModule("'ছোট কাটরা' কে নির্মাণ করেন?", "A", "শায়েস্তা খান", "মীর কাসিম", "মহম্মদ আলী", "মীর জাফর"));
                add(new QuestionModule("'বড় কাটরা' কোন মুঘল সম্রাটের সময় নির্মিত?", "C", "জাহাঙ্গীর", "আকবর", "শাহজাহান", "আউরঙ্গজেব"));
                add(new QuestionModule("'সোনারগাঁও' এর প্রতিষ্ঠাতা কে?", "B",  "বখতিয়ার খলজি","ঈসা খান", "শায়েস্তা খান", "নওয়াব সিরাজউদ্দৌলা"));
                add(new QuestionModule("'মহাস্থানগড়' কোন সময়কালে প্রতিষ্ঠিত?", "A", "মৌর্য যুগ", "গুপ্ত যুগ", "পাল যুগ", "সেন যুগ"));

                add(new QuestionModule("'কান্তজির মন্দির' কোন সময়কালে নির্মিত?", "D", "সেন যুগ", "মৌর্য যুগ", "পাল যুগ", "মুগল যুগ"));
                add(new QuestionModule("'সাতগম্বুজ মসজিদ' কোন মুঘল সম্রাটের সময় নির্মিত?", "C", "আকবর", "জাহাঙ্গীর", "শায়েস্তা খান", "আউরঙ্গজেব"));
                add(new QuestionModule("ষাট গম্ভুজ মসজিদ নির্মাণ করেন কে?", "C", "আকবর", "জাহাঙ্গীর", "খান জাহান আলী", "আউরঙ্গজেব"));
                add(new QuestionModule("'হাজারিবাগ কেল্লা' কে নির্মাণ করেন?", "C", "জাহাঙ্গীর", "মীর কাসিম", "শায়েস্তা খান", "মীর জাফর"));
                add(new QuestionModule("লালবাগ কেল্লা কে নির্মাণ শেষ করেন?", "C", "জাহাঙ্গীর", "মীর কাসিম", "শায়েস্তা খান", "মীর জাফর"));
                add(new QuestionModule("লালবাগ কেল্লা কে নির্মাণ শুরু করেন?", "B", "জাহাঙ্গীর", "মোহাম্মদ আযম", "শায়েস্তা খান", "মীর জাফর"));
                add(new QuestionModule("তিতুমীরের বাঁশের কেল্লা কোথায় অবস্থিত ছিল?", "B", "ঢাকা", "নারিকেলবাড়িয়া", "চট্টগ্রাম", "কুষ্টিয়া"));
                add(new QuestionModule("তিতুমীরের প্রকৃত নাম কী ছিল?", "A", "সায়েদ মীর নিসার আলী", "মীর আব্দুল্লাহ খান", "মীর কাশেম", "মীর নিসার উদ্দিন"));
                add(new QuestionModule("তিতুমীরের বাহিনী ব্রিটিশদের বিরুদ্ধে কোন সালে লড়াই করেছিল?", "C", "1829", "1830", "1831", "1832"));
                add(new QuestionModule("তিতুমীরের বাঁশের কেল্লা ব্রিটিশ বাহিনীর বিরুদ্ধে প্রতিরোধ করার কত দিন পর্যন্ত টিকে ছিল?", "B", "১০ দিন", "৭ দিন", "১৫ দিন", "৫ দিন"));
                add(new QuestionModule("শৈলকূপা জমিদার বাড়ি কোন জেলায় অবস্থিত?", "C", "ঢাকা", "খুলনা", "ঝিনাইদহ", "রাজশাহী"));
                add(new QuestionModule("শৈলকূপা জমিদার বাড়ি কোন শতাব্দীতে প্রতিষ্ঠিত হয়?", "B", "১৭শ শতাব্দী", "১৮শ শতাব্দী", "১৯শ শতাব্দী", "২০শ শতাব্দী"));
                add(new QuestionModule("শৈলকূপা জমিদার বাড়ির প্রতিষ্ঠাতা কে ছিলেন?", "A", "রাজা প্রাণকৃষ্ণ রায়", "রাজা হরিশ্চন্দ্র", "নবাব আলীবর্দী খান", "রাজা গোপাল সিংহ"));
                add(new QuestionModule("শৈলকূপা জমিদার বাড়ি কিসের জন্য বিখ্যাত?", "D", "ইতিহাসিক যুদ্ধ", "প্রাচীন মন্দির", "বাণিজ্য কেন্দ্র", "স্থাপত্যশৈলী"));
                add(new QuestionModule("শৈলকূপা জমিদার বাড়ির মূল ভবন কোন ধরনের নির্মাণশৈলীতে তৈরি?", "B", "ইউরোপীয়", "মুঘল", "ব্রিটিশ", "ইন্দো-সারাসেনিক"));
                add(new QuestionModule("হার্ডিঞ্জ ব্রিজ কোন নদীর ওপর নির্মিত?", "C", "যমুনা নদী", "মেঘনা নদী", "পদ্মা নদী", "করতোয়া নদী"));
                add(new QuestionModule("'বড় কুঠি' কোন শহরে অবস্থিত?", "C", "ঢাকা", "সিলেট", "রাজশাহী", "খুলনা"));
                add(new QuestionModule("'পানাম নগর' কোথায় অবস্থিত?", "D", "রাজশাহী", "কুমিল্লা", "সিলেট", "নারায়ণগঞ্জ"));
                add(new QuestionModule("'মধুপুর বন' কোন জেলায় অবস্থিত?", "B", "টাঙ্গাইল", "গাজীপুর", "ময়মনসিংহ", "রংপুর"));
                add(new QuestionModule("'খান জাহান আলীর মাজার' কোথায় অবস্থিত?", "B", "সাতক্ষীরা", "বাগেরহাট", "কুষ্টিয়া", "ঝিনাইদহ"));
                add(new QuestionModule("'বগুড়া' কোন প্রাচীন সভ্যতার নিদর্শন বহন করে?,", "A", "মহাস্থানগড়", "কান্তজীর মন্দির", "মহেঞ্জোদারো", "হরপ্পা"));
                add(new QuestionModule(" কোন নদীর তীরে 'সুন্দরবন' অবস্থিত?", "A", "পশুর", "মধুমতি", "কর্ণফুলী", "সুরমা"));
                add(new QuestionModule("'চন্দ্রদ্বীপ' কোথায় অবস্থিত?", "C", "বরিশাল", "কুমিল্লা", "পটুয়াখালী", "চাঁদপুর"));
                add(new QuestionModule("'শালবন বিহার' কোন জেলার একটি প্রাচীন স্থাপনা?", "C", "ঢাকা", "সিলেট", "কুমিল্লা", "চট্টগ্রাম"));
                add(new QuestionModule("'জাতীয় স্মৃতিসৌধ' কোথায় অবস্থিত?", "B", "ঢাকা", "সাভার", "নারায়ণগঞ্জ", "গাজীপুর"));
                add(new QuestionModule("খালিশপুর নীলকুঠি কোন সময়কালে প্রতিষ্ঠিত হয়?", "C", "১৬শ শতাব্দী", "১৭শ শতাব্দী", "১৮শ শতাব্দী", "১৯শ শতাব্দী"));
                add(new QuestionModule("খালিশপুর নীলকুঠি কোন ঐতিহাসিক ঘটনার সাথে সম্পর্কিত?", "A", "নীল বিদ্রোহ", "বক্সার যুদ্ধ", "পলাশীর যুদ্ধ", "চৌরিচৌরা বিদ্রোহ"));
                add(new QuestionModule("খালিশপুর নীলকুঠি কীসের জন্য বিখ্যাত?", "D", "ইতিহাসিক যুদ্ধ", "ব্যবসা কেন্দ্র", "মন্দির স্থাপনা", "নীলচাষের কেন্দ্রবিন্দু"));
                add(new QuestionModule("নীলচাষের সময় খালিশপুর নীলকুঠি কোন দেশের বণিকরা পরিচালনা করত?", "B", "ফরাসি", "ব্রিটিশ", "পর্তুগিজ", "ডাচ"));
                add(new QuestionModule("সুন্দরবন কোন ধরনের বন?", "A", "ম্যানগ্রেোভ বন", "টেম্পারেট বন", "বোরিয়াল বন", "ঘন অরণ্য"));
                add(new QuestionModule("সুন্দরবনে কত ধরনের মাংগ্রোভ গাছ পাওয়া যায়?", "A", "২০ ধরনের", "১৫ ধরনের", "৩০ ধরনের", "১০ ধরনের"));
                add(new QuestionModule("সুন্দরবনকে 'পৃথিবীর রক্ষাকবচ' কেন বলা হয়?", "A", "জলবায়ু নিয়ন্ত্রণ", "মৌসুমী বৃষ্টির জন্য", "দূষণ কমানোর জন্য", "পর্যটনের আকর্ষণ বাড়ানোর জন্য"));
                add(new QuestionModule("বাংলাদেশের প্রথম ঝুলন্ত সেতু কোথায় অবস্থিত?", "A", "রাঙামাটি", "বান্দরবান", "কক্সবাজার", "সিলেট"));
                add(new QuestionModule("রাঙামাটির ঝুলন্ত সেতুটি কোন কৃত্রিম হ্রদের উপর অবস্থিত?", "B", "তেংরাকুরি হ্রদ", "কাপ্তাই হ্রদ", "ফয়স লেক", "বগাকাইন হ্রদ"));
                add(new QuestionModule("রাঙামাটি ঝুলন্ত সেতুটি কবে নির্মাণ করা হয়?", "B", "১৯৭৫", "১৯৮৪", "১৯৯২", "১৯৯৯"));





            }

        };
        QuestionModule.createQuestionsForSubject("ঐতিহাসিক স্থান", R.drawable.historical_place, questions);



        //------------- Subject 7
        questions = new ArrayList(){
            {
                add(new QuestionModule("তেলের শীর্ষ উৎপাদনকারী দেশ কোনটি?", "A", "যুক্তরাষ্ট্র", "রাশিয়া", "সৌদি আরব", "ইরান"));
                add(new QuestionModule("প্রাকৃতিক গ্যাসের শীর্ষ উৎপাদনকারী দেশ কোনটি?", "B", "কাতার", "রাশিয়া", "যুক্তরাষ্ট্র", "ইরাক"));
                add(new QuestionModule("বিশ্বে সোনার শীর্ষ উৎপাদনকারী দেশ কোনটি?", "C", "দক্ষিণ আফ্রিকা", "অস্ট্রেলিয়া", "চীন", "যুক্তরাষ্ট্র"));
                add(new QuestionModule("চালের শীর্ষ উৎপাদনকারী দেশ কোনটি?", "A", "চীন", "ভারত", "ইন্দোনেশিয়া", "বাংলাদেশ"));
                add(new QuestionModule("গমের শীর্ষ উৎপাদনকারী দেশ কোনটি?", "B", "যুক্তরাষ্ট্র", "চীন", "ভারত", "রাশিয়া"));
                add(new QuestionModule("আলুর শীর্ষ উৎপাদনকারী দেশ কোনটি?", "C", "পাকিস্তান", "ভারত", "চীন", "নেদারল্যান্ডস"));
                add(new QuestionModule("বিশ্বে কফির শীর্ষ উৎপাদনকারী দেশ কোনটি?", "A", "ব্রাজিল", "ভিয়েতনাম", "কলম্বিয়া", "ইথিওপিয়া"));
                add(new QuestionModule("কোকোয়ার শীর্ষ উৎপাদনকারী দেশ কোনটি?", "D", "কোস্টা রিকা", "ইকুয়েডর", "পেরু", "আইভরি কোস্ট"));
                add(new QuestionModule("গবাদি পশুর মাংসের শীর্ষ উৎপাদনকারী দেশ কোনটি?", "B", "যুক্তরাষ্ট্র", "ব্রাজিল", "আর্জেন্টিনা", "চীন"));
                add(new QuestionModule("তুলার শীর্ষ উৎপাদনকারী দেশ কোনটি?", "A", "চীন", "ভারত", "যুক্তরাষ্ট্র", "পাকিস্তান"));

                add(new QuestionModule("কমলার শীর্ষ উৎপাদনকারী দেশ কোনটি?", "B", "যুক্তরাষ্ট্র", "ব্রাজিল", "চীন", "ভারত"));
                add(new QuestionModule("সয়াবিনের শীর্ষ উৎপাদনকারী দেশ কোনটি?", "A", "যুক্তরাষ্ট্র", "ব্রাজিল", "আর্জেন্টিনা", "ভারত"));
                add(new QuestionModule("কাগজের শীর্ষ উৎপাদনকারী দেশ কোনটি?", "C", "জার্মানি", "জাপান", "চীন", "কানাডা"));
                add(new QuestionModule("লোহা আকরিকের শীর্ষ উৎপাদনকারী দেশ কোনটি?", "B", "ভারত", "অস্ট্রেলিয়া", "রাশিয়া", "চীন"));
                add(new QuestionModule("দুধের শীর্ষ উৎপাদনকারী দেশ কোনটি?", "D", "নেদারল্যান্ডস", "ফ্রান্স", "জার্মানি", "ভারত"));
                add(new QuestionModule("মাছের শীর্ষ উৎপাদনকারী দেশ কোনটি?", "A", "চীন", "ইন্দোনেশিয়া", "ভারত", "রাশিয়া"));
                add(new QuestionModule("সিমেন্টের শীর্ষ উৎপাদনকারী দেশ কোনটি?", "C", "ভিয়েতনাম", "যুক্তরাষ্ট্র", "চীন", "ভারত"));
                add(new QuestionModule("চিনির শীর্ষ উৎপাদনকারী দেশ কোনটি?", "B", "চীন", "ব্রাজিল", "ভারত", "থাইল্যান্ড"));
                add(new QuestionModule("কাঁচের শীর্ষ উৎপাদনকারী দেশ কোনটি?", "D", "জাপান", "জার্মানি", "ফ্রান্স", "চীন"));
                add(new QuestionModule("চায়ের শীর্ষ উৎপাদনকারী দেশ কোনটি?", "A", "চীন", "ভারত", "শ্রীলঙ্কা", "বাংলাদেশ"));

                add(new QuestionModule("ক্যাসাভার (ট্যাপিওকা) শীর্ষ উৎপাদনকারী দেশ কোনটি?", "A", "নাইজেরিয়া", "থাইল্যান্ড", "ইন্দোনেশিয়া", "ব্রাজিল"));
                add(new QuestionModule("ভুট্টার শীর্ষ উৎপাদনকারী দেশ কোনটি?", "B", "চীন", "যুক্তরাষ্ট্র", "ব্রাজিল", "ভারত"));
                add(new QuestionModule("কাঁঠালের শীর্ষ উৎপাদনকারী দেশ কোনটি?", "C",  "ইন্দোনেশিয়া", "বাংলাদেশ","ভারত","থাইল্যান্ড"));
                add(new QuestionModule("আঙ্গুরের শীর্ষ উৎপাদনকারী দেশ কোনটি?", "A", "চীন", "স্পেন", "ইতালি", "যুক্তরাষ্ট্র"));
                add(new QuestionModule("টমেটোর শীর্ষ উৎপাদনকারী দেশ কোনটি?", "A", "চীন", "ভারত", "যুক্তরাষ্ট্র", "তুরস্ক"));
                add(new QuestionModule("কলার শীর্ষ উৎপাদনকারী দেশ কোনটি?", "B", "থাইল্যান্ড", "ভারত", "ইকুয়েডর", "ফিলিপাইন"));
                add(new QuestionModule("মুরগির মাংসের শীর্ষ উৎপাদনকারী দেশ কোনটি?", "C", "চীন", "রাশিয়া", "যুক্তরাষ্ট্র", "ব্রাজিল"));
                add(new QuestionModule("তরমুজের শীর্ষ উৎপাদনকারী দেশ কোনটি?", "A", "চীন", "ইরান", "তুরস্ক", "মিশর"));

                add(new QuestionModule("বাদামের শীর্ষ উৎপাদনকারী দেশ কোনটি?", "C", "যুক্তরাষ্ট্র", "সুদান", "চীন", "আর্জেন্টিনা"));
                add(new QuestionModule("আনারসের শীর্ষ উৎপাদনকারী দেশ কোনটি?", "B", "থাইল্যান্ড", "কোস্টা রিকা", "ফিলিপাইন", "ভারত"));
                add(new QuestionModule("অ্যালুমিনিয়ামের শীর্ষ উৎপাদনকারী দেশ কোনটি?", "B", "রাশিয়া", "চীন", "কানাডা", "যুক্তরাষ্ট্র"));
                add(new QuestionModule("বিশ্বে পাট উৎপাদনে শীর্ষ দেশ কোনটি?", "C",  "মিয়ানমার", "ভারত","বাংলাদেশ", "নেপাল"));
                add(new QuestionModule("নারিকেলের শীর্ষ উৎপাদনকারী দেশ কোনটি?", "D", "ভারত", "ফিলিপাইন", "ইন্দোনেশিয়া", "শ্রীলঙ্কা"));
                add(new QuestionModule("পাম তেলের শীর্ষ উৎপাদনকারী দেশ কোনটি?", "B",  "মালয়েশিয়া", "ইন্দোনেশিয়া","নাইজেরিয়া", "থাইল্যান্ড"));

                add(new QuestionModule("জিঙ্কের শীর্ষ উৎপাদনকারী দেশ কোনটি?", "A", "চীন", "অস্ট্রেলিয়া", "যুক্তরাষ্ট্র", "কানাডা"));
                add(new QuestionModule("গ্রীষ্মমন্ডলীয় ফলের শীর্ষ উৎপাদনকারী দেশ কোনটি?", "B", "ব্রাজিল", "ভারত", "চীন", "মেক্সিকো"));
                add(new QuestionModule("লেবুর শীর্ষ উৎপাদনকারী দেশ কোনটি?", "C", "মেক্সিকো", "চীন", "ভারত", "ইন্দোনেশিয়া"));
                add(new QuestionModule("বিশ্বে বেতের শীর্ষ উৎপাদনকারী দেশ কোনটি?", "A", "ইন্দোনেশিয়া", "মালয়েশিয়া", "ভিয়েতনাম", "থাইল্যান্ড"));
                add(new QuestionModule("বিশ্বে ডিম উৎপাদনে শীর্ষ দেশ কোনটি?", "B",  "যুক্তরাষ্ট্র","চীন", "ভারত", "ইন্দোনেশিয়া"));
                add(new QuestionModule("তুলা উৎপাদনে শীর্ষ দেশ কোনটি?", "B", "চীন", "ভারত", "যুক্তরাষ্ট্র", "পাকিস্তান"));


                add(new QuestionModule("চায়ের শীর্ষ রপ্তানিকারক দেশ কোনটি?", "A",  "ভারত","চীন", "শ্রীলঙ্কা", "বাংলাদেশ"));
                add(new QuestionModule("কফির শীর্ষ রপ্তানিকারক দেশ কোনটি?", "B", "ব্রাজিল", "ভিয়েতনাম", "কলম্বিয়া", "ইথিওপিয়া"));
                add(new QuestionModule("অটোমোবাইলের শীর্ষ উৎপাদনকারী দেশ কোনটি?", "C", "জার্মানি", "জাপান", "চীন", "যুক্তরাষ্ট্র"));
                add(new QuestionModule("প্রাকৃতিক রাবারের শীর্ষ উৎপাদনকারী দেশ কোনটি?", "A", "থাইল্যান্ড", "ইন্দোনেশিয়া", "ভিয়েতনাম", "মালয়েশিয়া"));
                add(new QuestionModule("বিশ্বে মোবাইল ফোনের শীর্ষ উৎপাদনকারী দেশ কোনটি?", "C", "চীন", "দক্ষিণ কোরিয়া", "যুক্তরাষ্ট্র", "জাপান"));
                add(new QuestionModule("ইলেকট্রনিক্স পণ্যের শীর্ষ উৎপাদনকারী দেশ কোনটি?", "A", "চীন", "যুক্তরাষ্ট্র", "জাপান", "দক্ষিণ কোরিয়া"));
                add(new QuestionModule("বিশ্বে সার উৎপাদনের শীর্ষ দেশ কোনটি?", "A", "চীন", "ভারত", "রাশিয়া", "যুক্তরাষ্ট্র"));

                add(new QuestionModule("মোটরসাইকেল উৎপাদনে শীর্ষ দেশ কোনটি?", "C", "চীন", "ভারত", "ইন্দোনেশিয়া", "জাপান"));
                add(new QuestionModule("ইলেকট্রিক গাড়ির শীর্ষ উৎপাদনকারী দেশ কোনটি?", "C", "চীন", "জার্মানি", "যুক্তরাষ্ট্র", "দক্ষিণ কোরিয়া"));
                add(new QuestionModule("ইলেকট্রনিক চিপের শীর্ষ উৎপাদনকারী দেশ কোনটি?", "A", "তাইওয়ান", "দক্ষিণ কোরিয়া", "চীন", "জাপান"));


                add(new QuestionModule("ওয়াইন উৎপাদনে শীর্ষ দেশ কোনটি?", "B", "ইতালি", "ফ্রান্স", "স্পেন", "যুক্তরাষ্ট্র"));
                add(new QuestionModule("চামড়ার শীর্ষ রপ্তানিকারক দেশ কোনটি?", "C", "চীন", "ভারত", "বাংলাদেশ", "ইতালি"));
                add(new QuestionModule("ওষুধের শীর্ষ রপ্তানিকারক দেশ কোনটি?", "B", "জার্মানি", "যুক্তরাষ্ট্র", "চীন", "ভারত"));







            }

        };
        QuestionModule.createQuestionsForSubject("শীর্ষ দেশ ", R.drawable.top_country, questions);



        //------------- Subject 7
        questions = new ArrayList(){
            {

                add(new QuestionModule("পলাশীর যুদ্ধ কত সালে সংঘটিত হয়েছিল?", "A", "১৭৫৭", "১৮৫৭", "১৯৪৭", "১৯৫৭"));

                add(new QuestionModule("সূর্যের সবচেয়ে কাছের গ্রহ কোনটি?", "D", "মঙ্গল", "পৃথিবী", "শুক্র", "বুধ"));

                add(new QuestionModule("পৃথিবীর সবথেকে কঠিন প্রাকৃতিক পদার্থ কী?", "A", "হীরা", "সোনা", "লোহা", "প্লাটিনাম"));

                add(new QuestionModule("বিশ্বের দীর্ঘতম নদী কোনটি?", "C", "আমাজন", "গঙ্গা", "নীল নদ", "ইয়াংসি নদ"));

                add(new QuestionModule("বাংলাদেশের বৃহত্তম দ্বীপ কোনটি?", "D", "সন্দ্বীপ", "মহেশখালী", "সেন্টমার্টিন", "হাতিয়া"));

                add(new QuestionModule("কোন দেশকে সূর্যোদয়ের দেশ বলা হয়?", "B", "চীন", "জাপান", "কোরিয়া", "ভিয়েতনাম"));

                add(new QuestionModule("বাংলাদেশের কোন জেলা সবচেয়ে বড়?", "B", "ঢাকা", "রংপুর", "চট্টগ্রাম", "রাজশাহী"));

                add(new QuestionModule("বিশ্বের সবচেয়ে বড় মরুভূমি কোনটি?", "A", "সাহারা", "গোবি", "কালাহারি", "আরব মরুভূমি"));

                add(new QuestionModule("সৌরজগতের সবচেয়ে বড় গ্রহ কোনটি?", "C", "শনি", "পৃথিবী", "বৃহস্পতি", "মঙ্গল"));

                add(new QuestionModule("কোন দেশকে 'ল্যান্ড অব দ্য রাইজিং সান' বলা হয়?", "B", "চীন", "জাপান", "থাইল্যান্ড", "দক্ষিণ কোরিয়া"));

                add(new QuestionModule("বাংলাদেশের রাজধানী ঢাকা কত সালে প্রতিষ্ঠিত হয়?", "C", "১২০৬", "১৩৫০", "১৬০৮", "১৭৫৭"));

                add(new QuestionModule("কোনটি পৃথিবীর দ্বিতীয় দীর্ঘতম নদী?", "A", "আমাজন", "নীল নদ", "গঙ্গা", "মিসিসিপি"));

                add(new QuestionModule("গ্লোবাল ওয়ার্মিংয়ের মূল কারণ কোনটি?", "B", "ওজোন স্তরের ক্ষয়", "কার্বন ডাই অক্সাইড নির্গমন", "জলবায়ু পরিবর্তন", "নদী দূষণ"));

                add(new QuestionModule("পৃথিবীর বৃহত্তম দ্বীপ কোনটি?", "A", "গ্রীনল্যান্ড", "নিউজিল্যান্ড", "মাদাগাস্কার", "ইন্দোনেশিয়া"));

                add(new QuestionModule("মানবদেহের সবচেয়ে শক্তিশালী পেশী কোনটি?", "C", "হাত", "পা", "জিহ্বা", "কোমর"));

                add(new QuestionModule("বিশ্বের সবচেয়ে জনবহুল দেশ কোনটি?", "A", "চীন", "ভারত", "যুক্তরাষ্ট্র", "ইন্দোনেশিয়া"));

                add(new QuestionModule("বিশ্বের সবচেয়ে বড় জঙ্গল কোনটি?", "C", "কঙ্গো", "তাইগা", "আমাজন", "সুন্দরবন"));

                add(new QuestionModule("‘নিশীত সূর্যের দেশ’ বলা হয় কোন দেশকে?", "A", "নরওয়ে", "ফিনল্যান্ড", "সুইডেন", "আলাস্কা"));

                add(new QuestionModule("আলবার্ট আইনস্টাইন কোন থিওরি প্রবর্তন করেন?", "B", "থার্মোডাইনামিক্স", "আপেক্ষিকতা তত্ত্ব", "কোয়ান্টাম মেকানিক্স", "বিগ ব্যাং থিওরি"));

                add(new QuestionModule("পৃথিবীর সবচেয়ে বড় কৃত্রিম লেক কোনটি?", "A", "ভল্টা লেক", "বৈকাল লেক", "ভিক্টোরিয়া লেক", "মিশিগান লেক"));

                add(new QuestionModule("মহাশূন্য থেকে দেখা পৃথিবীর সবচেয়ে বড় কাঠামো কোনটি?", "D", "তাজমহল", "মাচু পিচু", "পিরামিড", "চীনের মহাপ্রাচীর"));

                add(new QuestionModule("শূন্যের আবিষ্কারক কে?", "A", "আর্যভট্ট", "ইউক্লিড", "পিথাগোরাস", "গ্যালিলিও"));

                add(new QuestionModule("ইন্টারনেটের জনক হিসেবে পরিচিত কে?", "C", "বিল গেটস", "টিম বার্নার্স-লি", "ভিন্ট সার্ফ", "অ্যালান টুরিং"));

                add(new QuestionModule("বাকিংহাম প্যালেস কোথায় অবস্থিত?", "C", "প্যারিস", "বার্লিন", "লন্ডন", "রোম"));

                add(new QuestionModule("কোন গ্রহটিতে দিন ও রাতের দৈর্ঘ্য প্রায় সমান?", "B", "মঙ্গল", "শুক্র", "বৃহস্পতি", "শনি"));

                add(new QuestionModule("বাংলাদেশের কোন নদীটি সবচেয়ে দীর্ঘ?", "A", "পদ্মা", "যমুনা", "মেঘনা", "কর্ণফুলি"));


                add(new QuestionModule("বৃহত্তম মিষ্টি পানির লেক কোনটি?", "B", "বৈকাল লেক", "সুপিরিয়র লেক", "টাঙ্গানাইকা লেক", "হুরন লেক"));


                add(new QuestionModule("পাকিস্তানের শাসনামলে বাংলাদেশ কি নামে পরিচিত ছিল?", "B", "বঙ্গভূমি", "পূর্ব পাকিস্তান", "বঙ্গদেশ", "পূর্ব বাংলা"));

                add(new QuestionModule("মুঘল শাসনামলে ঢাকার নাম কী ছিল?", "A", "জাহাঙ্গীরনগর", "সোনারগাঁ", "ঢাকা", "মুর্শিদাবাদ"));

                add(new QuestionModule("মুঘল সম্রাট আওরঙ্গজেবের শাসনামলে বাংলার রাজধানী কোথায় ছিল?", "A", "ঢাকা", "মুর্শিদাবাদ", "পাটনা", "কাঠমুন্ডু"));

                add(new QuestionModule("বাংলায় মুসলিম শাসন কোন সাল থেকে শুরু হয়?", "C", "১১০০ খ্রিস্টাব্দ", "১৩০০ খ্রিস্টাব্দ", "১২০৪ খ্রিস্টাব্দ", "১৪০০ খ্রিস্টাব্দ"));

                add(new QuestionModule("মীর কাসিমের শাসন আমলে বাংলার রাজধানী কোথায় স্থানান্তর করা হয়?", "B", "ঢাকা", "মুর্শিদাবাদ", "কলকাতা", "পাটনা"));

                add(new QuestionModule("মুঘল শাসনকাল কখন সমাপ্ত হয়?", "C", "১৬৫০", "১৭৫০", "১৭৫৭", "১৮৫৭"));

                add(new QuestionModule("বাংলাদেশের মুক্তিযুদ্ধের সময় পাকিস্তানের শাসক কে ছিলেন?", "A", "ইয়াহিয়া খান", "জুলফিকার আলী ভুট্টো", "আয়ুব খান", "জিয়াউর রহমান"));

                add(new QuestionModule("ব্রিটিশ শাসনামলে বাংলার প্রথম গভর্নর কে ছিলেন?", "A", "লর্ড ক্লাইভ", "লর্ড ক্যানিং", "ওয়ারেন হেস্টিংস", "লর্ড কার্জন"));

                add(new QuestionModule("ভারতবর্ষে মুঘল শাসনের প্রতিষ্ঠাতা কে ছিলেন?", "A", "বাবর", "আকবর", "শাহজাহান", "হুমায়ুন"));

                add(new QuestionModule("ভারতে কোন শাসক দিল্লি সালতানাতের প্রতিষ্ঠা করেন?", "B", "আলাউদ্দিন খিলজি", "কুতুবউদ্দিন আইবক", "মহম্মদ ঘোরি", "বাবর"));

                add(new QuestionModule("ভারতবর্ষে  মুঘল শাসন কোন সাল থেকে শুরু হয়?", "A", "১৫২৬", "১৪৯৮", "১৫০০", "১৬০৫"));


                add(new QuestionModule("ভারতবর্ষের শেষ মুঘল সম্রাট কে ছিলেন?", "D", "আকবর", "শাহজাহান", "আলমগীর", "বাহাদুর শাহ জাফর"));


                add(new QuestionModule("বাংলাদেশের সবচেয়ে বড় হাওর কোনটি?", "A", "হাকালুকি হাওর", "টাঙ্গুয়ার হাওর", "বাউলাই হাওর", "হাইল হাওর"));

                add(new QuestionModule("টাঙ্গুয়ার হাওর কোন জেলায় অবস্থিত?", "B", "কিশোরগঞ্জ", "সুনামগঞ্জ", "সিলেট", "নেত্রকোণা"));

                add(new QuestionModule("হাকালুকি হাওর কোন জেলায় অবস্থিত?", "C", "ময়মনসিংহ", "নেত্রকোণা", "মৌলভীবাজার ও সিলেট", "শেরপুর"));

                add(new QuestionModule("কোন হাওরটি বাংলাদেশে পাখির অভয়ারণ্য হিসেবে পরিচিত?", "A", "টাঙ্গুয়ার হাওর", "হাকালুকি হাওর", "বাউলাই হাওর", "হাইল হাওর"));

                add(new QuestionModule("বিল কি?", "C", "বন্য প্রাণীর অভয়ারণ্য", "একটি নদী", "স্বাভাবিক জলাভূমি", "পর্বতশ্রেণী"));

                add(new QuestionModule("বাংলাদেশের কোন জেলায় 'চিনির বিল' অবস্থিত?", "A", "নাটোর", "পাবনা", "বগুড়া", "রাজশাহী"));

                add(new QuestionModule("বাংলাদেশের কোন অঞ্চলে হাওর ও বিলের সংখ্যা বেশি?", "A", "সিলেট", "রাজশাহী", "বরিশাল", "চট্টগ্রাম"));

                add(new QuestionModule("বাংলাদেশে অস্ত্র কারখানা কোথায় অবস্থিত?", "B", "রংপুর", "গাজীপুর", "খুলনা", "চট্টগ্রাম"));

                add(new QuestionModule("বাংলাদেশের বৃহত্তম বনাঞ্চল কোনটি?", "A", "সুন্দরবন", "লাউয়াছড়া বন", "রেমা-কালেঙ্গা বন", "টেকনাফ বন"));

                add(new QuestionModule("সুন্দরবন কোন দুটি দেশের মধ্যে অবস্থিত?", "B", "বাংলাদেশ-নেপাল", "বাংলাদেশ-ভারত", "বাংলাদেশ-ভুটান", "বাংলাদেশ-চীন"));

                add(new QuestionModule("লাউয়াছড়া জাতীয় উদ্যান কোন জেলায় অবস্থিত?", "C", "সিলেট", "নেত্রকোণা", "মৌলভীবাজার", "সুনামগঞ্জ"));

                add(new QuestionModule("বাংলাদেশের একমাত্র সোয়াম্প ফরেস্ট কোনটি?", "B", "লাউয়াছড়া বন", "রাতারগুল", "টেকনাফ বন", "চুনারুঘাট বন"));


                add(new QuestionModule("জীবন আনন্দ দাসের জন্মস্থান কোথায়?", "A", "ঝালকাঠি", "ঢাকা", "চট্টগ্রাম", "কুমিল্লা"));

                add(new QuestionModule("জীবন আনন্দ দাসের পেশা কী?", "A", "কবি ও গীতিকার", "শিক্ষক", "কর্মকর্তা", "ব্যবসায়ী"));

                add(new QuestionModule("টাকা মুদ্রণ প্রক্রিয়ায় কোন অ্যাসিডটি ব্যবহৃত হয়?", "B", "হাইড্রোক্লোরিক অ্যাসিড", "ফসফরিক অ্যাসিড", "নাইট্রিক অ্যাসিড", "অ্যাসিটিক অ্যাসিড"));



                add(new QuestionModule("পাল রাজবংশের প্রতিষ্ঠাতা কে ছিলেন?", "A", "গোপাল", "ধর্মপাল", "বিমানপাল", "মহীপাল"));

                add(new QuestionModule("পাল রাজবংশ কোন সময়কালে শাসন করেছিল?", "B", "৮ম থেকে ১২শ শতাব্দী", "৬ষ্ঠ থেকে ৮ম শতাব্দী", "১০ম থেকে ১৪শ শতাব্দী", "১২শ থেকে ১৫শ শতাব্দী"));

                add(new QuestionModule("পাল রাজবংশের রাজধানী কোথায় ছিল?", "C", "পাহাড়পুর", "গৌড়", "পুন্ড্রনগর", "মহাস্থানগড়"));

                add(new QuestionModule("ধর্মপাল কোন পাল রাজা ছিলেন?", "B", "দ্বিতীয়", "প্রথম", "তৃতীয়", "চতুর্থ"));

                add(new QuestionModule("কোন পাল রাজা নালন্দা ও বিক্রমশীলা বিশ্ববিদ্যালয় প্রতিষ্ঠা করেছিলেন?", "D", "বিমানপাল", "গোপাল", "মহীপাল", "ধর্মপাল"));

                add(new QuestionModule("পাল রাজাদের প্রধান ধর্মীয় বিশ্বাস কোনটি ছিল?", "A", "বৌদ্ধ ধর্ম", "হিন্দু ধর্ম", "ইসলাম", "জৈন ধর্ম"));

                add(new QuestionModule("পাল সাম্রাজ্যের পতনের প্রধান কারণ কী ছিল?", "C", "সেন রাজবংশের উত্থান", "মুঘল আক্রমণ", "রাজনৈতিক দুর্বলতা", "বৈদেশিক আক্রমণ"));

                add(new QuestionModule("পাল রাজাদের শাসনামলে কোন স্থাপত্যকীর্তি বিখ্যাত ছিল?", "B", "কান্তজির মন্দির", "পাহাড়পুর বৌদ্ধবিহার", "ছোটা সোনা মসজিদ", "লালবাগ কেল্লা"));

                add(new QuestionModule("পাল রাজাদের শাসন কালের উল্লেখযোগ্য বৈশিষ্ট্য কোনটি?", "D", "বৌদ্ধ ধর্মের প্রচার", "হিন্দু মন্দির নির্মাণ", "বাণিজ্যের প্রসার", "উচ্চ শিক্ষার উন্নয়ন"));

                add(new QuestionModule("পাল রাজবংশের কোন রাজার শাসনামলে সাম্রাজ্য সবচেয়ে বড় ছিল?", "B", "গোপাল", "ধর্মপাল", "বিমানপাল", "মহীপাল"));





            }

        };
        QuestionModule.createQuestionsForSubject("সাধারন জ্ঞান", R.drawable.knowledge, questions);


        //------------- Subject 7
        questions = new ArrayList(){

            {


                add(new QuestionModule("জাতিসংঘের সদর দপ্তর কোথায় অবস্থিত?", "A", "নিউইয়র্ক", "জেনেভা", "প্যারিস", "লন্ডন"));
                add(new QuestionModule("বিশ্ব স্বাস্থ্য সংস্থার সদর দপ্তর কোথায়?", "B",  "নিউইয়র্ক","জেনেভা", "ব্রাসেলস", "সিডনি"));
                add(new QuestionModule("আন্তর্জাতিক মুদ্রা তহবিলের সদর দপ্তর কোথায়?", "A", "ওয়াশিংটন ডিসি", "নিউইয়র্ক", "টোকিও", "প্যারিস"));
                add(new QuestionModule("ন্যাটো সদর দপ্তর কোথায় অবস্থিত?", "B",  "লন্ডন","ব্রাসেলস", "প্যারিস", "আমস্টারডাম"));
                add(new QuestionModule("ইউনেস্কোর সদর দপ্তর কোথায়?", "A", "প্যারিস", "রোম", "মাদ্রিদ", "লন্ডন"));
                add(new QuestionModule("বিশ্ব ব্যাংকের সদর দপ্তর কোথায় অবস্থিত?", "A", "ওয়াশিংটন ডিসি", "টোকিও", "সিডনি", "নিউইয়র্ক"));
                add(new QuestionModule("আন্তর্জাতিক অপরাধ আদালতের সদর দপ্তর কোথায়?", "B",  "ব্রাসেলস","হেগ", "লন্ডন", "প্যারিস"));
                add(new QuestionModule("আন্তর্জাতিক শ্রম সংস্থার সদর দপ্তর কোথায়?", "A", "জেনেভা", "সিডনি", "নিউইয়র্ক", "মাদ্রিদ"));
                add(new QuestionModule("আন্তর্জাতিক সমুদ্র সংস্থার সদর দপ্তর কোথায়?", "A", "লন্ডন", "নিউইয়র্ক", "ব্রাসেলস", "সিডনি"));
                add(new QuestionModule("আন্তর্জাতিক ক্রিকেট কাউন্সিলের সদর দপ্তর কোথায়?", "A", "দুবাই", "নিউইয়র্ক", "লন্ডন", "সিডনি"));
                add(new QuestionModule("এশিয়ান উন্নয়ন ব্যাংকের সদর দপ্তর কোথায়?", "A", "ম্যানিলা", "টোকিও", "নিউইয়র্ক", "সিডনি"));
                add(new QuestionModule("আন্তর্জাতিক সিভিল এভিয়েশন সংস্থার সদর দপ্তর কোথায়?", "A", "মন্ট্রিয়াল", "টোকিও", "নিউইয়র্ক", "প্যারিস"));
                add(new QuestionModule("মানবাধিকার সংস্থার সদর দপ্তর কোথায়?", "A", "লন্ডন", "নিউইয়র্ক", "প্যারিস", "জেনেভা"));
                add(new QuestionModule("বিশ্ব বাণিজ্য সংস্থার সদর দপ্তর কোথায়?", "B",  "লন্ডন","জেনেভা", "নিউইয়র্ক", "প্যারিস"));
                add(new QuestionModule("ন্যাটোর সদর দপ্তর কোথায়?", "A", "ব্রাসেলস", "লন্ডন", "প্যারিস", "জেনেভা"));
                add(new QuestionModule("দক্ষিণ এশীয় আঞ্চলিক সহযোগিতা সংস্থার সদর দপ্তর কোথায়?", "C",  "নিউ দিল্লী", "ঢাকা","কাঠমান্ডু", "কাবুল"));
                add(new QuestionModule("অর্গানাইজেশন অব ইসলামিক কোঅপারেশন এর সদর দপ্তর কোথায়?", "C",  "কুয়ালালামপুর", "কায়রো","জেদ্দা", "বাগদাদ"));
                add(new QuestionModule("লিগ অফ নেশনসের সদর দপ্তর কোথায় ছিল?", "C",  "প্যারিস", "লন্ডন","জেনেভা", "নিউইয়র্ক"));

                add(new QuestionModule("আফ্রিকান ইউনিয়নের সদর দপ্তর কোথায়?", "C",  "কায়রো", "কিগাল","আদ্দিস আবাবা", "আঙ্কারা"));
                add(new QuestionModule("ইন্টারন্যাশনাল ফান্ড ফর অ্যাগ্রিকালচারাল ডেভেলপমেন্টের সদর দপ্তর কোথায়?", "C",  "নিউইয়র্ক", "প্যারিস","রোম", "মাদ্রিদ"));
                add(new QuestionModule("উন্নয়নশীল দেশগুলোর জন্য আন্তর্জাতিক সংগঠনের সদর দপ্তর কোথায়?", "A", "জেনেভা", "লন্ডন", "নিউইয়র্ক", "প্যারিস"));
                add(new QuestionModule("ইউরোপীয় ইউনিয়নের সদর দপ্তর কোথায়?", "A", "ব্রাসেলস", "প্যারিস", "লন্ডন", "মাদ্রিদ"));

                add(new QuestionModule("বিশ্ব স্বাস্থ্য সংস্থা (WHO) কবে প্রতিষ্ঠিত হয়েছিল?", "C",  "1950", "1965","1948", "1970"));
                add(new QuestionModule("জাতিসংঘ কতটি সদস্য দেশ নিয়ে গঠিত?", "D",  "195", "190", "200","193"));
                add(new QuestionModule("নিরাপত্তা পরিষদের স্থায়ী সদস্য সংখ্যা কত?", "C",  "10", "15","5", "20"));
                add(new QuestionModule("আফ্রিকান ইউনিয়ন (AU) কতটি দেশ নিয়ে গঠিত?", "B",  "50", "55","45", "60"));

                add(new QuestionModule("বিশ্ব স্বাস্থ্য সংস্থার সদস্য দেশ সংখ্যা কত?", "B", "194", "180", "200", "160"));
                add(new QuestionModule("বিশ্ব বাণিজ্য সংস্থার (WTO) সদস্য দেশ সংখ্যা কত?", "D", "150", "180", "200","164"));
                add(new QuestionModule("এশীয় উন্নয়ন ব্যাংকের (ADB) সদস্য দেশ সংখ্যা কত?", "A", "68", "50", "75", "60"));
                add(new QuestionModule("নাটোর (NATO) সদস্য দেশ সংখ্যা কত?", "B",  "28","31", "35", "40"));


            }

        };
        QuestionModule.createQuestionsForSubject("আন্তর্জাতিক সংস্থা", R.drawable.united_nation, questions);



        //------------- Subject 7
        questions = new ArrayList(){
            {
                add(new QuestionModule("কোন সালে ভারত ও পাকিস্তান বিভক্ত হয়ে স্বাধীন হয়?", "A", "১৯৪৭", "১৯৪৮", "১৯৫২", "১৯৭১"));
                add(new QuestionModule("বাংলা ভাষা আন্দোলন শুরু হয় কোন সালে?", "B", "১৯৪৮", "১৯৫২", "১৯৫৬", "১৯৬৯"));
                add(new QuestionModule("মহান ভাষা আন্দোলনের শহীদ দিবস পালন করা হয় কোন তারিখে?", "C", "২৬শে মার্চ", "১৬ই ডিসেম্বর", "২১শে ফেব্রুয়ারি", "৭ই মার্চ"));
                add(new QuestionModule("ষাটের দশকে কোন আন্দোলন পূর্ব পাকিস্তানে ব্যাপক প্রভাব ফেলেছিল?", "A", "ছয় দফা আন্দোলন", "বাষট্টির শিক্ষা আন্দোলন", "চূড়ান্ত আন্দোলন", "স্বাধীনতা আন্দোলন"));
                add(new QuestionModule("বঙ্গবন্ধু শেখ মুজিবুর রহমানের ঐতিহাসিক ৭ই মার্চের ভাষণ কোন সালে দেওয়া হয়েছিল?", "B", "১৯৫২", "১৯৭১", "১৯৬৯", "১৯৭৫"));
                add(new QuestionModule("বঙ্গবন্ধু শেখ মুজিবুর রহমান কবে স্বাধীনতার ঘোষণা দেন?", "C", "২৫শে মার্চ ১৯৭১", "৭ই মার্চ ১৯৭১", "২৬শে মার্চ ১৯৭১", "১৬ই ডিসেম্বর ১৯৭১"));
                add(new QuestionModule("মুক্তিযুদ্ধে বাংলাদেশের সহায়ক প্রধান রাষ্ট্র ছিল কোনটি?", "A", "ভারত", "রাশিয়া", "চীন", "যুক্তরাষ্ট্র"));
                add(new QuestionModule("২৫ শে মার্চ রাতের গনহত্যাকে কি বলা হয়?", "A", "অপারেশন সার্চলাইট", "অপারেশন ব্লুস্টার", "অপারেশন সি-হর্স", "অপারেশন গ্রীনস্টার"));
                add(new QuestionModule("বাংলাদেশের মুক্তিযুদ্ধ শেষ হয় কোন তারিখে?", "D", "২৫শে মার্চ ১৯৭১", "২৬শে মার্চ ১৯৭১", "৭ই মার্চ ১৯৭১","১৬ই ডিসেম্বর ১৯৭১"));
                add(new QuestionModule("বাংলাদেশের প্রথম সংবিধান কবে গৃহীত হয়?", "A", "১৯৭২", "১৯৭৩", "১৯৭৫", "১৯৭৮"));
                add(new QuestionModule("বাংলাদেশের প্রথম রাষ্ট্রপতি কে ছিলেন?", "B", "জিয়াউর রহমান", "শেখ মুজিবুর রহমান", "তাজউদ্দীন আহমদ", "হুসেইন মুহাম্মদ এরশাদ"));
                add(new QuestionModule("জাতীয় চার নেতাকে কারাগারে হত্যা করা হয় কবে?", "A", "৩রা নভেম্বর ১৯৭৫", "১৬ই ডিসেম্বর ১৯৭১", "১০ই জানুয়ারি ১৯৭২", "১৫ই আগস্ট ১৯৭৫"));
                add(new QuestionModule("কোন সালে বঙ্গবন্ধু শেখ মুজিবুর রহমানকে সপরিবারে হত্যা করা হয়?", "B", "১৯৭৫", "১৯৭৭", "১৯৭৯", "১৯৮১"));
                add(new QuestionModule("২০০৭ সালে তত্ত্বাবধায়ক সরকারের প্রধান কে ছিলেন?", "A", "ফখরুদ্দীন আহমদ", "ইফতেখার চৌধুরী", "মো. আবদুল হামিদ", "মুহাম্মদ ইয়াহিয়া খান"));
                add(new QuestionModule("২০০৮ সালের নির্বাচনে আওয়ামী লীগ কতটি আসনে জয়লাভ করে?", "B",  "২৩৪","২৩০", "২২০", "২২৫"));
                add(new QuestionModule("রোহিঙ্গা সংকট কোন সালে সবচেয়ে জোরালোভাবে দেখা দেয়?", "B", "২০১৭", "২০১৮", "২০১৯", "২০২০"));
                add(new QuestionModule("বাংলাদেশ জাতিসংঘের সদস্যপদ লাভ করে কোন সালে?", "A", "১৯৭৪", "১৯৭৫", "১৯৭৩", "১৯৭৬"));

                add(new QuestionModule("পাকিস্তান সরকারের বিরুদ্ধে ছয় দফা দাবি পেশ করা হয় কত সালে?", "B",  "১৯৬৭", "১৯৬৬","১৯৬৯", "১৯৭০"));
                add(new QuestionModule("আগরতলা ষড়যন্ত্র মামলা কত সালে দায়ের করা হয়?", "A", "১৯৬৮", "১৯৬৬", "১৯৭০", "১৯৬৯"));
                add(new QuestionModule("১৯৭০ সালের নির্বাচনে পূর্ব পাকিস্তানের কোন রাজনৈতিক দল সবচেয়ে বেশি আসন লাভ করে?", "D", "বিএনপি", "জাতীয় পার্টি", "ছাত্রলীগ", "আওয়ামী লীগ"));

                add(new QuestionModule("স্বাধীন বাংলা বেতার কেন্দ্র থেকে প্রথম স্বাধীনতার ঘোষণা পাঠ করেন কে?", "B", "শেখ মুজিবুর রহমান", "মেজর জিয়াউর রহমান", "তাজউদ্দীন আহমদ", "সৈয়দ নজরুল ইসলাম"));

                add(new QuestionModule("মুক্তিযুদ্ধের সময় বাংলাদেশের অস্থায়ী সরকারের প্রথম প্রধানমন্ত্রী কে ছিলেন?", "D", "শেখ মুজিবুর রহমান", "খন্দকার মোশতাক আহমেদ", "জিয়াউর রহমান", "তাজউদ্দীন আহমদ"));
                add(new QuestionModule("মুজিবনগর সরকার কবে শপথ গ্রহণ করে?", "A", "১৭ই এপ্রিল ১৯৭১", "২৬শে মার্চ ১৯৭১", "১৬ই ডিসেম্বর ১৯৭১", "১৪ই আগস্ট ১৯৭১"));
                add(new QuestionModule("বাংলাদেশের মুক্তিযুদ্ধের সময় কোন সেক্টর কমান্ডার ছিলেন জিয়াউর রহমান?", "C", "সেক্টর ৭", "সেক্টর ৮", "সেক্টর ১", "সেক্টর ৫"));
                add(new QuestionModule("কোন স্থানে পাকিস্তানি সেনাবাহিনী প্রথম আত্মসমর্পণ করে?", "D", "মেহেরপুর", "চট্টগ্রাম", "খুলনা","ঢাকা"));
                add(new QuestionModule("বাংলাদেশের বিজয় দিবস পালন করা হয় কোন তারিখে?", "A", "১৬ই ডিসেম্বর", "২৬শে মার্চ", "২১শে ফেব্রুয়ারি", "৭ই মার্চ"));
                add(new QuestionModule("বাংলাদেশের প্রথম মুদ্রা কবে চালু হয়?", "B", "১৯৭১", "১৯৭২", "১৯৭৩", "১৯৭৪"));
                add(new QuestionModule("বঙ্গবন্ধু শেখ মুজিবুর রহমান কবে কারাগার থেকে মুক্তি পান?", "C", "২৬শে মার্চ ১৯৭১", "৭ই মার্চ ১৯৭২", "১০ই জানুয়ারি ১৯৭২", "১৬ই ডিসেম্বর ১৯৭১"));

                add(new QuestionModule("কোন দেশে বঙ্গবন্ধু হত্যাকাণ্ডের অন্যতম দায়ী ব্যক্তি কর্নেল রশিদ আশ্রয় নেন?", "B", "লিবিয়া", "পাকিস্তান", "ভারত", "মালয়েশিয়া"));
                add(new QuestionModule("জিয়াউর রহমান কবে বিএনপি প্রতিষ্ঠা করেন?", "C",  "১৯৭৮", "১৯৭৯","১৯৭৭", "১৯৮০"));
                add(new QuestionModule("১৯৯১ সালে বাংলাদেশের কোন রাজনৈতিক দল ক্ষমতায় আসে?", "A", "বিএনপি", "আওয়ামী লীগ", "জাতীয় পার্টি", "ছাত্রলীগ"));
                add(new QuestionModule("বাংলাদেশের প্রথম তত্ত্বাবধায়ক সরকার কবে গঠিত হয়?", "C",  "১৯৯১", "১৯৯৬", "১৯৯০","২০০১"));
                add(new QuestionModule("বাংলাদেশের স্থায়ী আন্তর্জাতিক অপরাধ ট্রাইব্যুনাল কবে প্রতিষ্ঠিত হয়?", "D", "২০২৪", "২০১১", "২০১২", "২০১০"));
                add(new QuestionModule("বাংলাদেশে সবচেয়ে বড় অবকাঠামো প্রকল্প কোনটি?", "B", "মেট্রোরেল", "পদ্মা সেতু", "রূপপুর পারমাণবিক বিদ্যুৎকেন্দ্র", "পায়রা বন্দর"));

                add(new QuestionModule("২০২৪ সালের জুলাই মাসে শিক্ষার্থীদের প্রতিবাদ কোন ঘটনার বিরুদ্ধে পরিচালিত হয়?", "A", "কোটা সংস্কার আন্দোলন", "বিদ্যুৎ সংকট", "পদ্মা সেতু প্রকল্প", "শিক্ষা ব্যবস্থার সংস্কার"));
                add(new QuestionModule("শিক্ষার্থীদের প্রতিবাদের সময় কতজন শিক্ষার্থী গ্রেফতার হয়েছিল?", "D", "১০০", "২০০", "৩০০", "৪০০"));
                add(new QuestionModule("শিক্ষার্থীদের প্রতিবাদের সময় কোন ধরনের দমনমূলক পদক্ষেপ নেওয়া হয়?", "A", "পুলিশের টিয়ার গ্যাস ও লাঠিচার্জ", "শিক্ষার্থী হত্যা", "কারফিউ ও ইন্টারনেট বন্ধ ", "সবগুলো"));
                add(new QuestionModule("২০২৪ সালের শিক্ষার্থীদের প্রতিবাদের প্রধান নেতৃস্থানীয় সংগঠন কোনটি ছিল?", "B", "ছাত্রলীগ", "বৈষম্য বিরোধী ছাত্র আন্দোলন", "শিবির", "ছাত্রদল"));



                add(new QuestionModule("বাংলাদেশের স্বাধীনতা দিবস কবে?", "C", "২৫ মার্চ", "১৬ ডিসেম্বর", "২৬ মার্চ", "২১ ফেব্রুয়ারি"));
                add(new QuestionModule("বঙ্গবন্ধু শেখ মুজিবুর রহমান কত সালে বাংলাদেশের স্বাধীনতা ঘোষণা করেন?", "B", "১৯৭০", "১৯৭১", "১৯৬৯", "১৯৭২"));
                add(new QuestionModule("১৯৫২ সালের ভাষা আন্দোলনে শহীদ হওয়া প্রথম ব্যক্তি কে?", "B", "আব্দুল জব্বার", "রফিক উদ্দিন", "সালাম", "বরকত"));
                add(new QuestionModule("১৯৭১ সালে মুক্তিযুদ্ধের প্রধান সেনাপতি কে ছিলেন?", "A", "জেনারেল এম এ জি ওসমানী", "জেনারেল জিয়াউর রহমান", "কর্নেল তাহের", "কর্নেল শফিউল্লাহ"));
                add(new QuestionModule("বাংলাদেশের সংবিধান কবে গৃহীত হয়?", "D", "১৯৭১", "১৯৭৫", "১৯৭৩", "১৯৭২"));
                add(new QuestionModule("১৯৭৫ সালে ১৫ আগস্ট বঙ্গবন্ধু শেখ মুজিবুর রহমানকে কারা হত্যা করে?", "C", "ভারতীয় সেনা", "পাকিস্তানি সেনা", "কিছু বিদ্রোহী সেনা কর্মকর্তা", "মুক্তিযোদ্ধারা"));
                add(new QuestionModule("বাংলাদেশের মুক্তিযুদ্ধের সময় পাকিস্তানি সেনাবাহিনীর দখলদারিত্ব থেকে কোন শহরটি প্রথম মুক্ত হয়?", "A", "জয়দেবপুর", "ঢাকা", "চট্টগ্রাম", "কুমিল্লা"));
                add(new QuestionModule("বাংলাদেশের স্বাধীনতা যুদ্ধ কত দিন স্থায়ী ছিল?", "C", "১০ মাস", "১ বছর", "৯ মাস", "৬ মাস"));
                add(new QuestionModule("মুক্তিযুদ্ধে কতজন বীরশ্রেষ্ঠ খেতাব পান?", "B", "১১ জন", "৭ জন", "৮ জন", "৬ জন"));
                add(new QuestionModule("শেখ মুজিবুর রহমানকে কবে গ্রেপ্তার করা হয়েছিল?", "A", "১৯৭১ সালের ২৫ মার্চ", "১৯৭১ সালের ২৬ মার্চ", "১৯৭১ সালের ১৬ ডিসেম্বর", "১৯৭২ সালের ১০ জানুয়ারি"));
                add(new QuestionModule("বাংলাদেশের মুক্তিযুদ্ধের সময় মিত্রবাহিনী কোন দেশে অবস্থিত ছিল?", "C", "ভুটান", "রাশিয়া", "ভারত", "চীন"));
                add(new QuestionModule("১৯৭১ সালের ১৬ ডিসেম্বর পাকিস্তানি বাহিনীর আত্মসমর্পণের স্থান কোনটি?", "A", "রেসকোর্স ময়দান", "বিমানবন্দর", "সোহরাওয়ার্দী উদ্যান", "ঢাকা বিশ্ববিদ্যালয়"));
                add(new QuestionModule("বাংলাদেশের জাতীয় স্মৃতিসৌধ কোথায় অবস্থিত?", "B", "মেহেরপুর", "সাভার", "গোপালগঞ্জ", "টাঙ্গাইল"));
                add(new QuestionModule("বাংলাদেশের মুজিবনগর সরকার প্রতিষ্ঠিত হয় কবে?", "C", "১৯৭১ সালের ১০ এপ্রিল", "১৯৭১ সালের ১৪ এপ্রিল", "১৯৭১ সালের ১৭ এপ্রিল", "১৯৭১ সালের ২৬ মার্চ"));
                add(new QuestionModule("বাংলাদেশের প্রথম প্রধানমন্ত্রী কে?", "A", "তাজউদ্দীন আহমদ", "শেখ মুজিবুর রহমান", "মওলানা ভাসানী", "জিয়াউর রহমান"));
                add(new QuestionModule("১৯৭১ সালের ৭ মার্চে বঙ্গবন্ধু শেখ মুজিবুর রহমান কোন ভাষণ দেন?", "B", "৬ দফা দাবি", "৭ মার্চের ঐতিহাসিক ভাষণ", "স্বাধীনতার ঘোষণা", "অর্থনৈতিক মুক্তির আহ্বান"));
                add(new QuestionModule("বাংলাদেশের সংবিধান কবে কার্যকর হয়?", "C", "১৯৭১", "১৯৭৫", "১৯৭২", "১৯৭৩"));
                add(new QuestionModule("মুক্তিযুদ্ধের সময় বাংলাদেশকে প্রথম কোন দেশ স্বীকৃতি দেয়?", "A", "ভারত", "রাশিয়া", "চীন", "যুক্তরাষ্ট্র"));
                add(new QuestionModule("মুক্তিযুদ্ধের সময় কবে মুজিবনগর সরকার শপথ গ্রহণ করে?", "B", "২৫ মার্চ ১৯৭১", "১৭ এপ্রিল ১৯৭১", "২৭ মার্চ ১৯৭১", "১ এপ্রিল ১৯৭১"));
                add(new QuestionModule("জিয়াউর রহমান কত সালে বাংলাদেশের প্রেসিডেন্ট হন?", "C", "১৯৭৪", "১৯৭৫", "১৯৭৭", "১৯৭৮"));


                add(new QuestionModule("বাংলাদেশের জাতীয় পতাকা প্রথম উত্তোলন করা হয় কবে?", "A", "১৯৭১ সালের ২ মার্চ", "১৯৭১ সালের ৭ মার্চ", "১৯৭১ সালের ২৬ মার্চ", "১৯৭১ সালের ১৬ ডিসেম্বর"));
                add(new QuestionModule("১৯৭১ সালে মুক্তিযুদ্ধে কোন শহরটিকে 'স্বাধীনতার শহর' বলা হয়?", "B", "ঢাকা", "মেহেরপুর", "চট্টগ্রাম", "সিলেট"));
                add(new QuestionModule("মুক্তিযুদ্ধে মিত্রবাহিনীর কমান্ডার কে ছিলেন?", "C", "জেনারেল জিয়াউর রহমান", "জেনারেল ওসমানী", "জেনারেল জগজিৎ সিং অরোরা", "জেনারেল শফিউল্লাহ"));
                add(new QuestionModule("১৯৭১ সালের ২৫ মার্চ রাতে পাকিস্তানি সেনাবাহিনীর অপারেশনের নাম কী ছিল?", "B", "অপারেশন থান্ডার", "অপারেশন সার্চলাইট", "অপারেশন ব্লু", "অপারেশন ফায়ারস্টর্ম"));
                add(new QuestionModule("১৯৭১ সালের কোন মাসে বাংলাদেশ-ভারত মিত্রবাহিনী গঠিত হয়?", "D", "অক্টোবর", "নভেম্বর", "ডিসেম্বর", "নভেম্বর"));
                add(new QuestionModule("বাংলাদেশের স্বাধীনতার ঘোষণা কীভাবে প্রচারিত হয়?", "C", "বেতার বাংলাদেশ",  "কলকাতা বেতার","স্বাধীন বাংলা বেতার কেন্দ্র", "রেডিও পাকিস্তান"));
                add(new QuestionModule("১৯৭২ সালে বাংলাদেশের সংবিধান কতটি ধারা নিয়ে গঠিত হয়?", "A", "১৫৩টি", "১৫০টি", "১৪৫টি", "১৬০টি"));
                add(new QuestionModule("স্বাধীনতা যুদ্ধের সময় 'মুক্তিফৌজ' কোন বাহিনীর অংশ ছিল?", "A", "মুক্তিবাহিনী", "পুলিশ বাহিনী", "পাকিস্তানি সেনাবাহিনী", "রাজারবাগ পুলিশ বাহিনী"));
                add(new QuestionModule("মুজিবনগর সরকারের প্রধানমন্ত্রী কে ছিলেন?", "A", "তাজউদ্দীন আহমদ", "সৈয়দ নজরুল ইসলাম", "ক্যাপ্টেন মনসুর আলী", "এ কে ফজলুল হক"));
                add(new QuestionModule("১৯৭১ সালের কোন তারিখে বীরশ্রেষ্ঠ মতিউর রহমান শহীদ হন?", "C", "২০ আগস্ট", "১৮ জুলাই", "২০ আগস্ট", "২৫ সেপ্টেম্বর"));
                add(new QuestionModule("মুক্তিযুদ্ধের সময় বাংলাদেশ সরকারের সদর দপ্তর কোথায় ছিল?", "B", "কলকাতা", "মুজিবনগর", "আগ্রা", "ত্রিপুরা"));
                add(new QuestionModule("বাংলাদেশের স্বাধীনতা আন্দোলনের সময় জয় বাংলা স্লোগানটি কে প্রথম প্রচলন করেন?", "A", "শেখ মুজিবুর রহমান", "তাজউদ্দীন আহমদ", "জিয়াউর রহমান", "মওলানা ভাসানী"));




            }

        };
        QuestionModule.createQuestionsForSubject("বাংলাদেশের ইতিহাস ", R.drawable.history_bd, questions);



        questions = new ArrayList(){
            {
                add(new QuestionModule("পৃথিবীর একমাত্র প্রাকৃতিক উপগ্রহের নাম কী?", "A", "চাঁদ", "মঙ্গল", "বুধ", "শুক্র"));
                add(new QuestionModule("কোন গ্রহকে 'লাল গ্রহ' বলা হয়?", "B", "বুধ", "মঙ্গল", "শনি", "শুক্র"));
                add(new QuestionModule("সূর্যের চারপাশে আবর্তনরত সব গ্রহের মধ্যে কোনটি সবচেয়ে বড়?", "C", "পৃথিবী", "মঙ্গল", "বৃহস্পতি", "শনি"));
                add(new QuestionModule("সূর্যের সবচেয়ে কাছের গ্রহ কোনটি?", "A", "বুধ", "শুক্র", "পৃথিবী", "মঙ্গল"));
                add(new QuestionModule("কোন গ্রহকে 'ভোরের তারা' বলা হয়?", "B", "বুধ", "শুক্র", "মঙ্গল", "শনি"));
                add(new QuestionModule("মহাবিশ্বের বয়স আনুমানিক কত বছর?", "C", "৮ বিলিয়ন", "১০ বিলিয়ন", "১৩.৮ বিলিয়ন", "১৫ বিলিয়ন"));
                add(new QuestionModule("প্রথম মহাকাশচারী কে ছিলেন?", "A", "ইউরি গ্যাগারিন", "নিল আর্মস্ট্রং", "বাস অলড্রিন", "মাইকেল কলিন্স"));
                add(new QuestionModule("কোন গ্রহে সবচেয়ে বেশি বলয় রয়েছে?", "D", "মঙ্গল", "বৃহস্পতি", "শুক্র", "শনি"));
                add(new QuestionModule("কোন নক্ষত্রটি পৃথিবীর সবচেয়ে কাছের?", "A", "সূর্য", "আলফা সেন্টরি", "সিরিয়াস", "বেটেলজুস"));
                add(new QuestionModule("মহাকাশচারীদের পরিধান করা পোশাক কী বলা হয়?", "B",  "অ্যাস্ট্রোনট স্যুট","স্পেস স্যুট", "গ্র্যাভিটি স্যুট", "মহাকাশ স্যুট"));
                add(new QuestionModule("পৃথিবীর সবচেয়ে নিকটবর্তী গ্যালাক্সির নাম কী?", "C", "মিল্কিওয়ে", "মেসিয়ার ৮১", "অ্যান্ড্রোমিডা", "সোমব্রেরো গ্যালাক্সি"));
                add(new QuestionModule("সৌরজগতের মধ্যে এক বছরের চেয়ে বেশি সময় ধরে কোন গ্রহে?", "D", "শনি", "বুধ", "শুক্র", "নেপচুন"));
                add(new QuestionModule("নাসা কত সালে প্রতিষ্ঠিত হয়?", "B", "১৯৫৫", "১৯৫৮", "১৯৬১", "১৯৬৫"));
                add(new QuestionModule("পৃথিবীর বায়ুমণ্ডলে কোন গ্যাস সবচেয়ে বেশি?", "C", "অক্সিজেন", "কার্বন ডাই অক্সাইড", "নাইট্রোজেন", "হাইড্রোজেন"));
                add(new QuestionModule("মানুষ প্রথম কোন বছর চাঁদে পা রাখে?", "A", "১৯৬৯", "১৯৭২", "১৯৬৫", "১৯৬৮"));
                add(new QuestionModule("কোন গ্রহে এক দিনের চেয়ে বেশি সময় ধরে?", "B", "শনি", "শুক্র", "বৃহস্পতি", "নেপচুন"));
                add(new QuestionModule("মহাবিশ্বের সবচেয়ে উজ্জ্বল বস্তু কী?", "D", "তারা", "প্ল্যানেট", "আলফা সেন্টরি", " কুইজার"));
                add(new QuestionModule("পৃথিবীর চেয়ে কোন গ্রহে কম মাধ্যাকর্ষণ রয়েছে?", "A", "মঙ্গল", "বৃহস্পতি", "শনি", "ইউরেনাস"));
                add(new QuestionModule("প্রথম মহাকাশচারী  প্রাণীর নাম কী?", "B", "মাংকি", "লাইকা", "ডগ", "বেলকা"));
                add(new QuestionModule("কোন উপগ্রহ পৃথিবীর মাধ্যাকর্ষণ শক্তির জন্য মহাকাশে রয়ে যায়?", "C", "ইন্টারন্যাশনাল স্পেস স্টেশন", "বয়েজার", "চাঁদ", "মঙ্গল রোভার"));
                add(new QuestionModule("মহাবিশ্বের প্রথম কৃত্রিম উপগ্রহের নাম কী?", "A", "স্পুটনিক ১", "বয়েজার ১", "হাবল টেলিস্কোপ", "লুনা ২"));
                add(new QuestionModule("বৃহস্পতি গ্রহের সবচেয়ে বড় উপগ্রহের নাম কী?", "B", "ইউরোপা", "গ্যানিমিড", "ক্যালিস্টো", "আইও"));
                add(new QuestionModule("কোন মহাকাশচারী প্রথম চাঁদে পা রাখেন?", "A", "নিল আর্মস্ট্রং", "বাস অলড্রিন", "ইউরি গ্যাগারিন", "মাইকেল কলিন্স"));
                add(new QuestionModule("মহাকাশে উড়ন্ত কাগজের মতো জ্যোতিষ্ককে কী বলে?", "D", "মহাজাগতিক কাগজ", "মহাকাশ বস্তু", "উল্কা", "ধূমকেতু"));

                add(new QuestionModule("মহাকাশ গবেষণায় প্রথম কোন দেশের সাফল্য ছিল?", "A", "সোভিয়েত ইউনিয়ন", "যুক্তরাষ্ট্র", "চীন", "জাপান"));
                add(new QuestionModule("চাঁদে পা রাখা দ্বিতীয় ব্যক্তি কোন মহাকাশচারী ?", "B",  "নিল আর্মস্ট্রং", "বাস অলড্রিন","ইউরি গ্যাগারিন", "মাইকেল কলিন্স"));
                add(new QuestionModule("কোন গ্রহকে পৃথিবীর যমজ বলে মনে করা হয়?", "C", "বুধ", "মঙ্গল", "শুক্র", "শনি"));
                add(new QuestionModule("মহাকাশের কেন্দ্রকে কী বলা হয়?", "D", "সেন্টার", "কোয়াসার", "মহাজাগতিক কেন্দ্র", "ব্ল্যাক হোল"));
                add(new QuestionModule("ব্ল্যাক হোল কী?", "A", "একটি জ্যোতিষ্ক,যা থেকে আলোও পালাতে পারে না", "একটি গ্রহ", "একটি নক্ষত্র", "একটি গ্যালাক্সি"));

                add(new QuestionModule("পৃথিবীর সবচেয়ে নিকটবর্তী গ্রহ কোনটি?", "B", "মঙ্গল", "শুক্র", "বুধ", "শনি"));
                add(new QuestionModule("কোন গ্রহকে 'বৃহত্তম গ্যাস জায়ান্ট' বলা হয়?", "C", "শনি", "ইউরেনাস", "বৃহস্পতি", "নেপচুন"));
                add(new QuestionModule("পৃথিবীর বায়ুমণ্ডলে কত শতাংশ নাইট্রোজেন রয়েছে?", "A", "৭৮%", "২১%", "১%", "১০%"));
                add(new QuestionModule("কোন গ্রহে এক বছর সবচেয়ে কম সময় ধরে?", "B", "বুধ", "শনি", "মঙ্গল", "নেপচুন"));
                add(new QuestionModule("মহাশূন্যে মানুষ কতক্ষণ সময় কাটাতে পারে?", "C", "১ দিন", "৫ ঘণ্টা", "১৫ সেকেন্ড", "১০ মিনিট"));
                add(new QuestionModule("মঙ্গল গ্রহে সবচেয়ে বড় পর্বতের নাম কী?", "D", "মাউন্ট এভারেস্ট", "মাউন্ট কিলিমানজারো", "মাউন্ট ডেনালি", "অলিম্পাস মন্স"));
                add(new QuestionModule("কোন গ্রহে সবচেয়ে বেশি চাঁদ রয়েছে?", "A", "শনি","বৃহস্পতি",  "নেপচুন", "মঙ্গল"));

                add(new QuestionModule("মহাবিশ্বের প্রসারণ সম্পর্কে কোন তত্ত্বটি সঠিক?", "C", "স্টেডি স্টেট থিওরি", "অসিলেটিং থিওরি", "বিগ ব্যাং থিওরি", "ব্ল্যাক হোল থিওরি"));
                add(new QuestionModule("মহাকাশে প্রথম প্রাণীটি কী ছিল?", "A", "কুকুর", "বিড়াল", "বানর", "ইঁদুর"));
                add(new QuestionModule("মহাকাশে চাঁদ কত দিন সময় নেয় পৃথিবীকে একবার আবর্তন করতে?", "B", "২৭ দিন", "২৯.৫ দিন", "৩০ দিন", "৩১ দিন"));
                add(new QuestionModule("কোন গ্রহে সবচেয়ে বেশি সক্রিয় আগ্নেয়গিরি রয়েছে?", "C", "বুধ", "শনি", "জুপিটার", "নেপচুন"));
                add(new QuestionModule("মহাকাশে কোন গ্রহের দিনটি সবচেয়ে দীর্ঘ?", "D", "শুক্র", "বুধ", "মঙ্গল", "শুক্র"));
                add(new QuestionModule("ব্ল্যাক হোলের প্রভাব কী?", "C", "আলোকে ধরে রাখে", "সময়কে বিলম্বিত করে", "আলো এবং পদার্থকে টেনে নেয়", "শক্তিকে বিস্ফোরণ ঘটায়"));
                add(new QuestionModule("পৃথিবী থেকে চাঁদের গড় দূরত্ব কত?", "A", "৩৮৪,৪০০ কিলোমিটার", "২০০,০০০ কিলোমিটার", "৫০০,০০০ কিলোমিটার", "১০০,০০০ কিলোমিটার"));
                add(new QuestionModule("কোন গ্রহের চুম্বকীয় ক্ষেত্র সবচেয়ে শক্তিশালী?", "B", "শনি", "বৃহস্পতি", "নেপচুন", "শুক্র"));
                add(new QuestionModule("মহাকাশে কোনটি সবচেয়ে দ্রুত গতি সম্পন্ন?", "C", "শব্দের গতি", "মহাশূন্য রকেট", "আলোর গতি", "মহাজাগতিক কণা"));
                add(new QuestionModule("কোন গ্যালাক্সিতে পৃথিবী অবস্থিত?", "A", "মিল্কিওয়ে", "অ্যান্ড্রোমিডা", "মেসিয়ার ৮১", "সোমব্রেরো গ্যালাক্সি"));
                add(new QuestionModule("মহাকাশে কোন গ্রহের আকাশ সবসময় লাল দেখায়?", "B", "বুধ", "মঙ্গল", "শুক্র", "নেপচুন"));
                add(new QuestionModule("কোন গ্রহে পৃষ্ঠের তাপমাত্রা সবচেয়ে বেশি?", "B", "বুধ", "শুক্র", "মঙ্গল", "শনি"));
                add(new QuestionModule("কোন গ্রহের চৌম্বক ক্ষেত্র নেই?", "A", "শুক্র", "বৃহস্পতি", "শনি", "নেপচুন"));
                add(new QuestionModule("নাসার মঙ্গল মিশনের নাম কী?", "D", "অ্যাপোলো", "মার্স ওডিসি", "স্পুটনিক", "মার্স রোভার"));
                add(new QuestionModule("কোন গ্রহকে 'জ্বলন্ত বল' হিসেবে চিহ্নিত করা হয়?", "C", "বুধ", "শনি", "মঙ্গল", "বৃহস্পতি"));
                add(new QuestionModule("কোন গ্রহটি গ্যাসে আবৃত?", "B", "বুধ", "বৃহস্পতি", "শুক্র", "পৃথিবী"));

                add(new QuestionModule("কোন গ্রহে বাতাসের ঘনত্ব সবচেয়ে বেশি?", "C", "মঙ্গল", "বুধ", "শুক্র", "নেপচুন"));
                add(new QuestionModule("মহাবিশ্বের সবচেয়ে ঠান্ডা গ্রহ কোনটি?", "D", "শনি", "ইউরেনাস", "শুক্র", "নেপচুন"));
                add(new QuestionModule("মহাকাশে রেডিয়েশনের সর্বাধিক উৎস কী?", "A", "কোয়াসার", "ব্ল্যাক হোল", "নিউট্রন তারকা", "আলফা সেন্টরি"));
                add(new QuestionModule("কোন গ্রহটিকে সূর্যের সবচেয়ে দূরের গ্রহ হিসেবে গণ্য করা হয়?", "C", "শুক্র", "শনি", "নেপচুন", "ইউরেনাস"));
                add(new QuestionModule("প্রথম আন্তর্জাতিক স্পেস স্টেশন (ISS) কত সালে প্রতিষ্ঠিত হয়?", "D", "১৯৯৫", "১৯৯৮", "১৯৯৯", "১৯৯৮"));

                add(new QuestionModule("মহাবিশ্বে সবচেয়ে বেশি এলিয়েন জীবনের সম্ভাবনা কোথায়?", "B", "মঙ্গল", "শুক্র", "বুধ", "নেপচুন"));
                add(new QuestionModule("কোন গ্রহে বায়ুমণ্ডল মূলত কার্বন ডাইঅক্সাইড দ্বারা গঠিত?", "A", "শুক্র", "বৃহস্পতি", "মঙ্গল", "নেপচুন"));
                add(new QuestionModule("শুক্র গ্রহের একটি বিশেষত্ব কি?", "B", "পৃথিবীর থেকে ছোট", "বায়ুমণ্ডল নেই", "শুক্রের দিন বছরের থেকে দীর্ঘ", "মঙ্গলের উপগ্রহ রয়েছে"));
                add(new QuestionModule("কোন গ্যাস মহাবিশ্বের ৭৫% জায়গা দখল করে?", "D", "হাইড্রোজেন", "হিলিয়াম", "অক্সিজেন", "নাইট্রোজেন"));
                add(new QuestionModule("পৃথিবীর বায়ুমণ্ডলে কোন গ্যাস সবচেয়ে বেশি পরিমাণে আছে?", "A", "নাইট্রোজেন", "অক্সিজেন", "হাইড্রোজেন", "কার্বন ডাইঅক্সাইড"));

                add(new QuestionModule("মহাকাশের 'ডার্ক ম্যাটার' কি?", "C", "দৃশ্যমান পদার্থ", "অদৃশ্য পদার্থ", "ব্ল্যাক হোল", "গ্যাস"));
                add(new QuestionModule("কোন গ্রহে জীবন খুঁজতে সবচেয়ে বেশি অভিযান হয়েছে?", "C", "শুক্র", "বৃহস্পতি", "মঙ্গল", "নেপচুন"));
                add(new QuestionModule("কোন গ্রহের পৃষ্ঠে জল পাওয়া গেছে?", "B", "বৃহস্পতি", "মঙ্গল", "শুক্র", "নেপচুন"));

            }

        };
        QuestionModule.createQuestionsForSubject("মহাবিশ্ব", R.drawable.universe, questions);


        questions = new ArrayList(){

            {

                //===================ফুটবল=====================

                add(new QuestionModule("ফিফা বিশ্বকাপের প্রথম আসর কোন সালে অনুষ্ঠিত হয়?", "A", "১৯৩০", "১৯৩৪", "১৯৫০", "১৯৫৪"));
                add(new QuestionModule("ফিফা বিশ্বকাপের প্রথম চ্যাম্পিয়ন কোন দেশ?", "B", "আর্জেন্টিনা", "উরুগুয়ে", "ব্রাজিল", "ইতালি"));
                add(new QuestionModule("ফুটবলে 'গোল্ডেন বুট' পুরস্কার কিসের জন্য দেওয়া হয়?", "C", "সেরা ডিফেন্ডার", "সেরা গোলরক্ষক", "সর্বাধিক গোলদাতা", "সেরা অধিনায়ক"));
                add(new QuestionModule("ফুটবলে 'গোল্ডেন বল' পুরস্কার কিসের জন্য দেওয়া হয়?", "B", "সেরা অধিনায়ক", "সেরা খেলোয়াড়", "সর্বোচ্চ গোলরক্ষক", "সেরা ডিফেন্ডার"));
                add(new QuestionModule("ফুটবল বিশ্বকাপ ইতিহাসে সবচেয়ে বেশি চ্যাম্পিয়ন হয়েছে কোন দেশ?", "C", "জার্মানি", "ইতালি", "ব্রাজিল", "আর্জেন্টিনা"));
                add(new QuestionModule("ফিফা বিশ্বকাপ কত বছর অন্তর অনুষ্ঠিত হয়?", "C", "২ বছর", "৩ বছর", "৪ বছর", "৫ বছর"));
                add(new QuestionModule("বিশ্বকাপ ফুটবলের সর্বাধিক গোলদাতা কে?", "D", "পেলে", "মারাদোনা", "মেসি", "মিরোস্লাভ ক্লোসে"));
                add(new QuestionModule("বিশ্বকাপ ফুটবলের ইতিহাসে সবচেয়ে কম বয়সে গোল করা খেলোয়াড় কে?", "A", "পেলে", "মেসি", "ম্যারাডোনা", "নেইমার"));
                add(new QuestionModule("ফুটবল বিশ্বকাপ ২০১৮ কোথায় অনুষ্ঠিত হয়?", "B", "ব্রাজিল", "রাশিয়া", "জার্মানি", "ফ্রান্স"));
                add(new QuestionModule("ফুটবল বিশ্বকাপের ট্রফির নাম কী?", "C", "হেনরি ডেলুনে ট্রফি", "ডেভিস কাপ", "জুলে রিমে ট্রফি", "উইম্বলডন কাপ"));
                add(new QuestionModule("কোন দেশ সবচেয়ে বেশি ফুটবল বিশ্বকাপের ফাইনাল খেলেছে?", "D", "আর্জেন্টিনা", "ফ্রান্স", "ইতালি", "জার্মানি"));
                add(new QuestionModule("ফুটবল মাঠে কোন রঙের কার্ড দেখানো হয়?", "B", "সবুজ এবং নীল", "লাল এবং হলুদ", "সাদা এবং কালো", "নীল এবং সাদা"));
                add(new QuestionModule("ফুটবল বিশ্বকাপের প্রথম গোলদাতা কে ছিলেন?", "C", "মেসি", "ম্যারাডোনা", "লুসিয়ান লরেন্ট", "পেলে"));
                add(new QuestionModule("বিশ্বকাপ ফুটবলে 'গোল্ডেন গ্লোভ' পুরস্কার কিসের জন্য দেওয়া হয়?", "D", "সেরা খেলোয়াড়", "সেরা অধিনায়ক", "সর্বাধিক গোলদাতা", "সেরা গোলরক্ষক"));
                add(new QuestionModule("ফুটবল খেলার গোলপোস্টের উচ্চতা কত ফুট?", "B", "৭ ফুট", "৮ ফুট", "৯ ফুট", "৬ ফুট"));
                add(new QuestionModule("বিশ্বকাপ ফুটবলের ইতিহাসে সবচেয়ে বড় ব্যবধানে জয় কোন দলের?", "A", "হাঙ্গেরি", "ব্রাজিল", "আর্জেন্টিনা", "ইতালি"));

                add(new QuestionModule("ফুটবল ম্যাচে এক টিমের কতজন খেলোয়াড় বদলি  হতে পারে?", "D", "২", "৩", "৪", "৫"));

                add(new QuestionModule("ফুটবলে সবচেয়ে বেশি ব্যালন ডি'অর জয়ী খেলোয়াড় কে?", "A", "লিওনেল মেসি", "ক্রিশ্চিয়ানো রোনালদো", "পেলে", "জিনেদিন জিদান"));
                add(new QuestionModule("ফুটবলে সর্বাধিক গোলদাতা কে?", "B", "মেসি", "ক্রিশ্চিয়ানো রোনালদো", "পেলে", "নেইমার"));
                add(new QuestionModule("চ্যাম্পিয়ন্স লিগে সবচেয়ে বেশি শিরোপা জয়ী দল কোনটি?", "D", "বার্সেলোনা", "বায়ার্ন মিউনিখ", "চেলসি", "রিয়াল মাদ্রিদ"));


                add(new QuestionModule("প্রথম আন্তর্জাতিক টেস্ট ক্রিকেট ম্যাচ কোন দেশে অনুষ্ঠিত হয়?", "A", "অস্ট্রেলিয়া", "ইংল্যান্ড", "ভারত", "দক্ষিণ আফ্রিকা"));
                add(new QuestionModule("কোন দেশ প্রথম ক্রিকেট বিশ্বকাপ জিতেছিল?", "B", "ভারত", "ওয়েস্ট ইন্ডিজ", "অস্ট্রেলিয়া", "ইংল্যান্ড"));
                add(new QuestionModule("কোন ব্যাটসম্যান সর্বাধিক টেস্ট শতক করেছেন?", "C",  "পন্টিং", "কোহলি", "শচীন টেন্ডুলকার","ব্রায়ান লারা"));
                add(new QuestionModule("টি-টোয়েন্টি ক্রিকেটের প্রথম বিশ্বকাপ কোন সালে অনুষ্ঠিত হয়?", "D",  "২০০৮", "২০০৬", "২০০৫","২০০৭"));


                add(new QuestionModule("ক্রিকেটের ইতিহাসে সর্বাধিক সেঞ্চুরি কোন খেলোয়াড়ের?", "B", "জ্যাক ক্যালিস", "শচীন টেন্ডুলকার", "রিকি পন্টিং", "ব্রায়ান লারা"));
                add(new QuestionModule("আইসিসি ক্রিকেট বিশ্বকাপের প্রথম বিজয়ী দল কোনটি?", "A", "ওয়েস্ট ইন্ডিজ", "ভারত", "অস্ট্রেলিয়া", "ইংল্যান্ড"));
                add(new QuestionModule("টি-টোয়েন্টি ফরম্যাটে সর্বাধিক রান সংগ্রাহক কে?", "C", "ক্রিস গেইল", "মার্টিন গাপটিল", "বিরাট কোহলি", "রোহিত শর্মা"));
                add(new QuestionModule("টেস্ট ক্রিকেটে ডাবল সেঞ্চুরি করার প্রথম ব্যাটসম্যান কে?", "A", "ডন ব্র্যাডম্যান", "শচীন  টেন্ডুলকার", "ব্রায়ান লারা", "কুমার সাঙ্গাকারা"));
                add(new QuestionModule("ক্রিকেটে 'গুগলি' কী?", "D", "একটি ব্যাটিং শট", "ফিল্ডিং টেকনিক", "স্পিনারদের জন্য পিচিং স্টাইল", "একটি বিশেষ ধরনের লেগ স্পিন বল"));
                add(new QuestionModule("টি-টোয়েন্টি বিশ্বকাপের প্রথম বিজয়ী দল কোনটি?", "B", "অস্ট্রেলিয়া", "ভারত", "ওয়েস্ট ইন্ডিজ", "ইংল্যান্ড"));
                add(new QuestionModule("ক্রিকেটে সর্বাধিক উইকেট শিকারি বোলার কে?", "A", " মুত্তিয়া মুরালিধরন", "শেন ওয়ার্ন", "জেমস অ্যান্ডারসন", "অনিল কুম্বলে"));

                add(new QuestionModule("ওয়ানডে ক্রিকেটে দ্রুততম শতরান কোন খেলোয়াড় করেছেন?", "B", "শহীদ আফ্রিদি", "এবি ডি ভিলিয়ার্স", "ক্রিস গেইল", "জস বাটলার"));

                add(new QuestionModule("ওয়ানডে ক্রিকেটে সর্বাধিক হাফ সেঞ্চুরি করেছেন কে?", "A", "শচীন টেন্ডুলকার", "রাহুল দ্রাবিড়", "কুমার সাঙ্গাকারা", "ব্রায়ান লারা"));
                add(new QuestionModule("টেস্ট ক্রিকেটে এক ইনিংসে সবচেয়ে বেশি রান করেছেন কে?", "C", "শচীন টেন্ডুলকার", "মাইকেল ক্লার্ক", "ব্রায়ান লারা", "রিকি পন্টিং"));

                add(new QuestionModule("আইসিসি টেস্ট চ্যাম্পিয়নশিপের প্রথম বিজয়ী দল কোনটি?", "D", "ভারত", "অস্ট্রেলিয়া", "ইংল্যান্ড", "নিউজিল্যান্ড"));



                add(new QuestionModule("কোন দেশ প্রথম টি-টোয়েন্টি বিশ্বকাপ আয়োজন করেছিল?", "B", "অস্ট্রেলিয়া", "দক্ষিণ আফ্রিকা", "ইংল্যান্ড", "ভারত"));
                add(new QuestionModule("ক্রিকেটের কোন দেশে 'অ্যাশেজ' সিরিজ খেলা হয়?", "A", "ইংল্যান্ড এবং অস্ট্রেলিয়া", "দক্ষিণ আফ্রিকা এবং নিউজিল্যান্ড", "ভারত এবং পাকিস্তান", "শ্রীলঙ্কা এবং ওয়েস্ট ইন্ডিজ"));
                   add(new QuestionModule("ক্রিকেটে স্টাম্পের উচ্চতা কত?", "D",  "২৯ ইঞ্চি", "৩০ ইঞ্চি", "৩১ ইঞ্চি","২৮ ইঞ্চি"));


                add(new QuestionModule("বাংলাদেশের প্রথম টেস্ট ক্রিকেট ম্যাচ কোন দলের বিপক্ষে ছিল?", "B",  "ইংল্যান্ড","ভারত", "শ্রীলঙ্কা", "জিম্বাবুয়ে"));
                add(new QuestionModule("বাংলাদেশের প্রথম টেস্ট অধিনায়ক কে ছিলেন?", "A", "নাঈমুর রহমান দুর্জয়", "আকরাম খান", "মোহাম্মদ আশরাফুল", "খালেদ মাহমুদ সুজন"));
                add(new QuestionModule("ওয়ানডে ক্রিকেটে বাংলাদেশের প্রথম জয় কোন দলের বিপক্ষে ছিল?", "D", "অস্ট্রেলিয়া", "দক্ষিণ আফ্রিকা", "নিউজিল্যান্ড", "কেনিয়া"));


                add(new QuestionModule("কোন খেলোয়াড় বাংলাদেশ দলের হয়ে প্রথম হ্যাটট্রিক করেছেন?", "C", "তাপস বাইশ্য", "আল আমিন হোসেন","রফিকুল ইসলাম", "আবদুর রাজ্জাক"));

                add(new QuestionModule("বাংলাদেশ ক্রিকেট দলের প্রথম টেস্ট জয় কোন দলের বিপক্ষে?", "B", "ভারত", "জিম্বাবুয়ে", "ওয়েস্ট ইন্ডিজ", "শ্রীলঙ্কা"));
                add(new QuestionModule("কোন খেলোয়াড় বাংলাদেশের হয়ে প্রথম টেস্ট ডাবল সেঞ্চুরি করেন?", "A", "মুশফিকুর রহিম", "তামিম ইকবাল", "মাহমুদুল্লাহ রিয়াদ", "শাকিব আল হাসান"));
                add(new QuestionModule("তামিম ইকবালের প্রথম ওয়ানডে সেঞ্চুরি কোন দলের বিপক্ষে?", "C", "ভারত", "পাকিস্তান", "আয়ারল্যান্ড", "শ্রীলঙ্কা"));

                add(new QuestionModule("বাংলাদেশের কোন ক্রিকেটার প্রথম আইসিসি বর্ষসেরা অলরাউন্ডার হন?", "B", "তামিম ইকবাল", "সাকিব আল হাসান", "মুশফিকুর রহিম", "মাশরাফি বিন মুর্তজা"));


                add(new QuestionModule("কোন ক্রিকেট স্টেডিয়ামকে বাংলাদেশের 'হোম অফ ক্রিকেট' বলা হয়?", "A", "মিরপুর শের-ই-বাংলা জাতীয় স্টেডিয়াম", "চট্টগ্রাম জহুর আহমেদ চৌধুরী স্টেডিয়াম", "সিলেট আন্তর্জাতিক ক্রিকেট স্টেডিয়াম", "ফতুল্লা খান সাহেব ওসমান আলী স্টেডিয়াম"));
                add(new QuestionModule("বাংলাদেশ ক্রিকেট দলের সবচেয়ে বেশি সময় ধরে অধিনায়কত্ব করেছেন কে?", "D", "হাবিবুল বাশার", "মুশফিকুর রহিম", "আকরাম খান", "মাশরাফি বিন মুর্তজা"));
                add(new QuestionModule("টি-টোয়েন্টি ক্রিকেটে বাংলাদেশের প্রথম সেঞ্চুরিয়ান কে?", "B",  "লিটন দাস","তামিম ইকবাল", "মুশফিকুর রহিম", "শাকিব আল হাসান"));

                add(new QuestionModule("ক্রিকেটে 'ডাক' কি?", "C",  "এক ওভার", "পঞ্চম ইনিংস", "শূন্য রানে আউট হওয়া","চার রানের নীচে"));
                
                add(new QuestionModule("ক্রিকেটের 'ট্রিপল সেঞ্চুরি' মানে কী?", "D", "৩০০ রান", "২০০ রান", "১০০ রান", "৫০ রান"));
                add(new QuestionModule("কোন ক্রিকেটার সর্বাধিক আন্তর্জাতিক ম্যাচ খেলার রেকর্ড রাখেন?", "B", "শচীন টেন্ডুলকার", "রাহুল দ্রাবিড়", "ক্রিশ্চিয়ানো রোনালদো", "ব্রায়ান লারা"));
                add(new QuestionModule("ক্রিকেটের 'গোল্ডেন গ্লাভ' পুরস্কার কিসের জন্য দেওয়া হয়?", "C", "সেরা ব্যাটসম্যান", "সেরা বোলার", "সেরা উইকেটরক্ষক", "সেরা অধিনায়ক"));
                add(new QuestionModule("ক্রিকেটের ইতিহাসে সবচেয়ে দ্রুততম সেঞ্চুরি কোন ব্যাটসম্যানের?", "A", "এবিডি ভিলিয়ার্স", "ব্রায়ান লারা", "শচীন টেন্ডুলকার", "বিরাট কোহলি"));
                add(new QuestionModule("কোন আন্তর্জাতিক ক্রিকেট বোর্ড খেলার নিয়মাবলী নির্ধারণ করে?", "B", "বোর্ড অফ কন্ট্রোল ফর ক্রিকেট ইন ইন্ডিয়া", "ইন্টারন্যাশনাল ক্রিকেট কাউন্সিল", "অস্ট্রেলিয়ান ক্রিকেট বোর্ড", "ওয়েস্ট ইন্ডিজ ক্রিকেট বোর্ড"));

                add(new QuestionModule("টেস্ট ক্রিকেটের ইতিহাসে সবচেয়ে বেশি ইনিংসে উইকেট নেওয়ার রেকর্ড রাখে কে?", "C", "শেন ওয়ার্ন", "ব্রায়ান লারা", "আনিল কুম্বলে", "মুথাইয়া মুরলিধরন"));
                add(new QuestionModule("কোন ক্রিকেট দল সবচেয়ে বেশি টি-টোয়েন্টি বিশ্বকাপ জিতেছে?", "D", "ভারত", "অস্ট্রেলিয়া", "নিউজিল্যান্ড", "ওয়েস্ট ইন্ডিজ"));

                add(new QuestionModule("কাবাডি খেলার  উৎপত্তিস্থল কোথায়?", "A", "ভারত", "বাংলাদেশ", "পাকিস্তান", "নেপাল"));
                add(new QuestionModule("বাংলাদেশের জাতীয় খেলা কোনটি?", "A", "কাবাডি", "ক্রিকেট", "ফুটবল", "ব্যাডমিন্টন"));
                add(new QuestionModule("কাবাডির একটি ম্যাচে কতজন খেলোয়াড় থাকে?", "B", "৭", "৯", "৫", "১১"));
                add(new QuestionModule("হকি খেলার উৎপত্তিস্থল কোথায়?", "A", "যুক্তরাজ্য", "অস্ট্রেলিয়া", "কানাডা", "ভারত"));

                add(new QuestionModule("ভলিবলের উৎপত্তিস্থল কোথায়?", "A", "মাসাচুসেটস, যুক্তরাষ্ট্র", "ক্যালিফোর্নিয়া, যুক্তরাষ্ট্র", "জাপান", "ব্রাজিল"));

                add(new QuestionModule("ভলিবল খেলার মাঠের আকার কত?", "C", "১৮ x ৯ মিটার", "২০ x ১০ মিটার", "১৫ x ৮ মিটার", "২২ x ১২ মিটার"));
                add(new QuestionModule("ভলিবল খেলায় কতটি সেট খেলা হয়?", "A", "৫", "৩", "৭", "৪"));
                add(new QuestionModule("ভলিবল খেলায় 'প্যাস' কি?", "D", "বল গ্রহন করার কৌশল", "গোল করার কৌশল", "ফাউল করা", "ফল দেওয়া"));
                add(new QuestionModule("ভলিবল খেলায় 'সার্ভ' কাকে বলে?", "C", "খেলা শুরু করার প্রক্রিয়া", "গোল করার কৌশল", "পাস করা", "ফাউল করা"));

                add(new QuestionModule("ভলিবল খেলার প্রথম বিশ্বকাপ কবে অনুষ্ঠিত হয়েছিল?", "D", "১৯৭০", "১৯৮০", "১৯৯০", "১৯৬৫"));

                add(new QuestionModule("ভলিবল খেলায় 'ফাউল' কি?", "A", "নিয়ম ভঙ্গ করা", "গোল করা", "বল ধরা", "স্পর্শ করা"));
                add(new QuestionModule("ভলিবল খেলার আন্তর্জাতিক সংস্থা কোনটি?", "C",  "NCAA", "NBA","FIVB", "ICC"));

                add(new QuestionModule("ব্যাডমিন্টনের উৎপত্তিস্থল কোথায়?", "A", "ইংল্যান্ড", "ফ্রান্স", "ভারত", "চীন"));

                add(new QuestionModule("ব্যাডমিন্টন খেলায় 'শাটলকক' কি?", "C",  "নেট", "ব্যাট", "বল","মাঠ"));
                add(new QuestionModule("ব্যাডমিন্টনে 'ডাবল' কাকে বলে?", "A", "দলগত খেলা", "একক খেলা", "এলোমেলো খেলা", "ফাউল খেলা"));
                add(new QuestionModule("ব্যাডমিন্টন খেলার সময় ব্যবহার করা ব্যাটের নাম কি?", "B",  "স্টিক", "র‌্যাকেট","ড্রাম", "প্যাডল"));

                add(new QuestionModule("ব্যাডমিন্টন খেলার নেটের উচ্চতা কত?", "D", "১.৫১ মিটার", "১.২৫ মিটার", "১.৪ মিটার", "১.৫৫ মিটার"));
                add(new QuestionModule("ব্যাডমিন্টন খেলায় 'সার্ভ' কাকে বলে?", "A", "বল মারার প্রক্রিয়া", "গোল করার কৌশল", "ফাউল করা", "পাস করা"));


            }

        };
        QuestionModule.createQuestionsForSubject("খেলাধুলা", R.drawable.sports, questions);



        questions = new ArrayList(){

            {
            //======================================================================================

                add(new QuestionModule("ইলেকট্রনিক ডিভাইসে ব্যবহৃত প্রধান উপাদান কোনটি?", "A", "ট্রানজিস্টর", "ক্যাপাসিটার", "রেজিস্টর", "ডায়োড"));
                add(new QuestionModule("সেমিকন্ডাক্টর উপাদানের মধ্যে কোনটি ব্যবহৃত হয়?", "B", "অ্যালুমিনিয়াম", "সিলিকন", "কপার", "জিঙ্ক"));
                add(new QuestionModule("ট্রানজিস্টরের কাজ কি?", "C", "বিদ্যুৎ সঞ্চালন করা", "বিদ্যুৎ উৎপাদন করা", "সংকেত বাড়ানো", "বিদ্যুৎ প্রবাহ রোধ করা"));
                add(new QuestionModule("রেজিস্টরের কাজ কি?", "D", "ভোল্টেজ নিয়ন্ত্রণ করা", "সিগন্যাল বৃদ্ধি করা", "বিদ্যুৎ উৎপাদন করা", "বিদ্যুৎ প্রবাহের প্রতিবন্ধকতা প্রদান করা"));
                add(new QuestionModule("ক্যাপাসিটরের কাজ কি?", "A", "শক্তি সংরক্ষণ করা", "শক্তি উৎপাদন করা", "ভোল্টেজ মাপা", "প্রবাহ মাপা"));
                add(new QuestionModule("ডায়োড কিভাবে কাজ করে?", "B", "বিদ্যুৎ প্রবাহ বৃদ্ধি করে", "একটি দিক থেকে বিদ্যুৎ প্রবাহিত করতে দেয়", "শক্তি সংরক্ষণ করে", "শক্তি রূপান্তর করে"));
                add(new QuestionModule("ক্যাপাসিটরের একক কি?", "D", "ওয়াট", "ওহম", "অ্যাম্পিয়ার", "ফ্যারাড"));
                add(new QuestionModule("একটি LED এর পূর্ণরূপ কি?", "A", "লাইট এমিটিং ডায়োড", "লাইট এক্সপ্যান্ডিং ডিভাইস", "লং এক্সপোজার ডায়োড", "লাইট এনার্জি ডিভাইস"));
                add(new QuestionModule("মাইক্রোফারাড কি পরিমাপ করে?", "A", "ক্যাপাসিট্যান্স", "রেজিস্ট্যান্স", "ইনডাকট্যান্স", "ইনপুট ভোল্টেজ"));
                add(new QuestionModule("সেমিকন্ডাক্টরের প্রকারভেদ কত?", "B", "১", "২", "৩", "৪"));
                add(new QuestionModule("বেসিক ইলেকট্রনিক্সের প্রধান উপাদান কি?", "A", "ট্রানজিস্টর", "ডায়োড", "ক্যাপাসিটার", "রেজিস্টর"));
                add(new QuestionModule("বেসিক ইলেকট্রনিক্সে বিদ্যুৎ প্রবাহ কোন ধরণের?", "B", "অ্যাসিডিক", "ডিসি", "এসি", "ম্যাগনেটিক"));
                add(new QuestionModule("ট্রান্সফরমারের কাজ কি?", "C", "বিদ্যুৎ উৎপাদন", "শক্তি সংরক্ষণ", "ভোল্টেজ বৃদ্ধি বা হ্রাস করা", "কারেন্ট নিয়ন্ত্রণ"));
                add(new QuestionModule("কারেন্টের একক কি?", "A", "অ্যাম্পিয়ার", "ভোল্ট", "ওয়াট", "ওহম"));
                add(new QuestionModule("ভোল্টেজের একক কি?", "B", "ওয়াট", "ভোল্ট", "অ্যাম্পিয়ার", "ওহম"));
                add(new QuestionModule("ইনডাকটরের একক কি?", "C", "ফ্যারাড", "ওহম", "হেনরি", "ওয়াট"));
                add(new QuestionModule("রেজিস্ট্যান্সের একক কি?", "D", "ওয়াট", "ভোল্ট", "অ্যাম্পিয়ার", "ওহম"));
                add(new QuestionModule("DC বিদ্যুৎ প্রবাহের বৈশিষ্ট্য কি?", "B", "বিকল্পভাবে প্রবাহিত হয়", "একটি নির্দিষ্ট দিকে প্রবাহিত হয়", "দুইদিকে প্রবাহিত হয়", "চক্রাকারে প্রবাহিত হয়"));
                add(new QuestionModule("সার্কিট ব্রেকার কিসের জন্য ব্যবহৃত হয়?", "C", "কারেন্ট বৃদ্ধি করা", "ভোল্টেজ হ্রাস করা", "অতিরিক্ত কারেন্ট প্রতিরোধ করা", "শক্তি সঞ্চয় করা"));
                add(new QuestionModule("ফিউজের কাজ কি?", "D", "শক্তি রূপান্তর করা", "ভোল্টেজ বৃদ্ধি করা", "রেজিস্ট্যান্স হ্রাস করা", "অতিরিক্ত কারেন্টের কারণে সুরক্ষা প্রদান করা"));
                add(new QuestionModule("ডায়োডের প্রয়োগ কোনটিতে?", "A", "রেকটিফায়ার", "অসিলেটর", "ফিল্টার", "অ্যাম্প্লিফায়ার"));
                add(new QuestionModule("ক্যাপাসিটরের প্রয়োগ কোথায় দেখা যায়?", "B", "ভোল্টেজ নিয়ন্ত্রণে", "শক্তি সংরক্ষণে", "কারেন্ট মাপতে", "রেজিস্ট্যান্স বৃদ্ধি করতে"));
                add(new QuestionModule("রেজিস্টরের মান নির্ধারণের জন্য কি ব্যবহার করা হয়?", "D", "ভোল্টেজ মিটার", "কারেন্ট মিটার", "ওহম মিটার", "কালার কোড"));
                add(new QuestionModule("অ্যাম্প্লিফায়ারের কাজ কি?", "B", "শক্তি সংরক্ষণ করা", "সিগন্যাল বৃদ্ধি করা", "ভোল্টেজ নিয়ন্ত্রণ করা", "কারেন্ট কমানো"));
                add(new QuestionModule("ক্যাপাসিটর চার্জ ধারণ করে কিভাবে?", "C", "শক্তি রূপান্তর করে", "ভোল্টেজ বৃদ্ধি করে", "ইলেকট্রন সঞ্চয় করে", "প্রবাহ নিয়ন্ত্রণ করে"));
                add(new QuestionModule("ইনডাকটরের কাজ কি?", "D", "শক্তি সঞ্চয় করা", "বিদ্যুৎ উৎপাদন করা", "ভোল্টেজ হ্রাস করা", "চুম্বকত্ব সৃষ্টি করা"));

                add(new QuestionModule("ফিল্টারের কাজ কি?", "B", "কারেন্ট নিয়ন্ত্রণ করা", "প্রবাহ থেকে অবাঞ্ছিত সংকেত সরিয়ে দেয়", "শক্তি সঞ্চয় করা", "ভোল্টেজ বৃদ্ধি করা"));
                add(new QuestionModule("রেকটিফায়ারের কাজ কি?", "C", "প্রবাহ বৃদ্ধি করা", "শক্তি সঞ্চয় করা", "AC কে DC তে রূপান্তর করা", "বিদ্যুৎ উৎপাদন করা"));


                // ইলেকট্রিক্যাল মৌলিক বিষয় নিয়ে ২০০ প্রশ্ন
                add(new QuestionModule("ইলেকট্রিসিটি কি?", "A", "এটি একটি শক্তির রূপ", "এটি একটি পদার্থ", "এটি একটি অবস্থা", "এটি একটি প্রক্রিয়া"));
                add(new QuestionModule("বিদ্যুতের মৌলিক একক কি?", "B", "ভোল্ট", "অ্যাম্পিয়ার", "ওয়াট", "জুল"));
                add(new QuestionModule("একটি সার্কিটের রোধ কি?", "A", "বিদ্যুৎ প্রবাহের প্রতিরোধ", "বিদ্যুৎ উৎপাদনের মাধ্যম", "বিদ্যুৎ সঞ্চালনের গতি", "বিদ্যুৎ শক্তির ইউনিট"));
                add(new QuestionModule("বিদ্যুৎ পরিবাহীর উপাদান কোনটি?", "C",  "জিঙ্ক", "অ্যালুমিনিয়াম","কপার", "লৌহ"));
                add(new QuestionModule("শক্তির ইউনিট কি?", "A", "ওয়াট", "ভোল্ট", "অ্যাম্পিয়ার", "ওহম"));
                add(new QuestionModule("বিদ্যুৎ উৎপাদনের জন্য প্রাথমিক উৎস কি?", "C", "সূর্য", "বায়ু", "জল", "কৃষি"));
                add(new QuestionModule("কনডেনসার একটি  কি?", "D", "বিদ্যুৎ সঞ্চয় করার যন্ত্র", "বিদ্যুৎ উৎপাদনের যন্ত্র", "বিদ্যুৎ পরিচালনার যন্ত্র", "বিদ্যুৎ রোধক"));
               add(new QuestionModule("একটি সার্কিটের মোট শক্তি কিভাবে নির্ণয় করা হয়?", "D", "P = IV", "P = IR", "P = V²/R", "P = Q/t"));
                add(new QuestionModule("বিদ্যুৎ সঞ্চালন লাইনের সাধারণত কোন উপাদান ব্যবহার হয়?", "B", "অ্যালুমিনিয়াম", "কপার", "লৌহ", "জিঙ্ক"));
                add(new QuestionModule("শক বা টান হলে কি ঘটে?", "A", "শরীরে বিদ্যুৎ প্রবাহ  হয়", "শরীর ঠাণ্ডা হয়ে যায়", "শরীর তাপিত হয়", "শরীর শক্তি উৎপাদন করে"));
                add(new QuestionModule("ফ্রিকোয়েন্সির একক কি?", "C",  "ওয়াট", "ভোল্ট","হার্জ", "অ্যাম্পিয়ার"));
                add(new QuestionModule("কোনটি একটি নিরাপদ ভোল্টেজ?", "B",  "১০০ ভোল্ট", "৫ ভোল্ট","৪৫০ ভোল্ট", "২২০ ভোল্ট"));
                add(new QuestionModule("ডায়োডের কাজ কি?", "B",  "বিদ্যুৎ উৎপাদন করা","বিদ্যুৎ প্রবাহ নিয়ন্ত্রণ করা", "বিদ্যুৎ সঞ্চালন করা", "বিদ্যুৎ রোধ করা"));

                add(new QuestionModule("একটি সেমিকন্ডাকটরের মৌলিক উপাদান কি?", "B",  "কপার", "সিলিকন","অ্যালুমিনিয়াম", "লৌহ"));
                add(new QuestionModule("হ্যালোজেন বাল্বের প্রধান বৈশিষ্ট্য কি?", "C", "উচ্চ তাপ উৎপাদন", "নিম্ন তাপ উৎপাদন", "উচ্চ আলোকসজ্জা", "নিম্ন আলোকসজ্জা"));

                add(new QuestionModule("কোনটি বিদ্যুৎ পরিবাহী পদার্থ?", "A", "তামা", "সালফার", "কার্বন", "প্লাস্টিক"));
                add(new QuestionModule("অ্যালুমিনিয়াম কিসের জন্য পরিচিত?", "B", "অ-পরিবাহী", "বিদ্যুৎ পরিবাহী", "তাপ নিরোধক", "অ-বৈদ্যুতিক"));

                add(new QuestionModule("কোন ধাতুটি সবচেয়ে ভালো বিদ্যুৎ পরিবাহী?", "D", "অ্যালুমিনিয়াম", "লৌহ", "তামা", "রুপা"));
                add(new QuestionModule("কোনটি বিদ্যুৎ পরিবাহীর বৈশিষ্ট্য নয়?", "D", "ইলেকট্রন চলাচল সহজ", "তাপ পরিবাহী", "ধাতু", "ইলেকট্রন বন্ধন শক্ত"));

                add(new QuestionModule("কোনটি ইনসুলেটরের প্রধান বৈশিষ্ট্য?", "B", "বিদ্যুৎ সঞ্চালন করে", "বিদ্যুৎ প্রবাহ বাধাগ্রস্ত করে", "তাপ সঞ্চালন করে", "ইলেকট্রন দ্রুত চলে"));
                add(new QuestionModule("কোন পরিস্থিতিতে ইনসুলেটর পদার্থ বিদ্যুৎ সঞ্চালন করতে পারে?", "C", "সাধারণ তাপমাত্রায়", "শূন্য তাপমাত্রায়", "খুব উচ্চ তাপমাত্রায়", "শক্তিশালী আলো উপস্থিতিতে"));




            }

        };
        QuestionModule.createQuestionsForSubject("মৌলিক ইলেকট্রনিক্স", R.drawable.circuit, questions);



        questions = new ArrayList(){

            {
                add(new QuestionModule("বাংলাদেশের জাতীয় ফুল কোনটি?", "A", "শাপলা", "গোলাপ", "পদ্ম", "টিউলিপ"));
                add(new QuestionModule("ভারতের জাতীয় ফুল কোনটি?", "C", "লিলি", "শাপলা", "পদ্ম", "গোলাপ"));
                add(new QuestionModule("শ্রীলঙ্কার জাতীয় ফুল কোনটি?", "B", "টিউলিপ", "নীল মহানেল", "গোলাপ", "পদ্ম"));
                add(new QuestionModule("জাপানের জাতীয় ফুল কোনটি?", "B", "শাপলা", "চন্দ্রমল্লিকা", "গোলাপ", "টিউলিপ"));
                add(new QuestionModule("পাকিস্তানের জাতীয় ফুল কোনটি?", "A", "জেসমিন", "গোলাপ", "টিউলিপ", "লিলি"));
                add(new QuestionModule("নেপালের জাতীয় ফুল কোনটি?", "C", "শাপলা", "গোলাপ", " লাল পদ্ম", "লিলি"));
                add(new QuestionModule("চীনের জাতীয় ফুল কোনটি?", "D", "শাপলা", "পদ্ম", "চন্দ্রমল্লিকা", "বরই ফুল"));
                add(new QuestionModule("যুক্তরাষ্ট্রের জাতীয় ফুল কোনটি?", "A", "গোলাপ", "পদ্ম", "চন্দ্রমল্লিকা", "টিউলিপ"));
                add(new QuestionModule("নেদারল্যান্ডসের জাতীয় ফুল কোনটি?", "A", "টিউলিপ", "গোলাপ", "শাপলা", "লিলি"));
                add(new QuestionModule("মালয়েশিয়ার জাতীয় ফুল কোনটি?", "B", "গোলাপ", "জবা", "বরই", "টিউলিপ"));

                add(new QuestionModule("থাইল্যান্ডের জাতীয় ফুল কোনটি?", "A", "অর্কিড", "পদ্ম", "লিলি", "গোলাপ"));
                add(new QuestionModule("অস্ট্রেলিয়ার জাতীয় ফুল কোনটি?", "B", "গোলাপ", "গোল্ডেন ওয়াটল", "লিলি", "শাপলা"));
                add(new QuestionModule("মেক্সিকোর জাতীয় ফুল কোনটি?", "C", "শাপলা", "গোলাপ", "ডাহলিয়া", "টিউলিপ"));
                add(new QuestionModule("ভিয়েতনামের জাতীয় ফুল কোনটি?", "D", "শাপলা", "লিলি", "গোলাপ", "পদ্ম"));
                add(new QuestionModule("ইন্দোনেশিয়ার জাতীয় ফুল কোনটি?", "A", "জেসমিন", "গোলাপ", "শিউলি", "লিলি"));
                add(new QuestionModule("দক্ষিণ কোরিয়ার জাতীয় ফুল কোনটি?", "B", "জেসমিন", "জবা", "পদ্ম", "গোলাপ"));
                add(new QuestionModule("মালদ্বীপের জাতীয় ফুল কোনটি?", "C", "টিউলিপ","ডাহলিয়া","গোলাপ", "লিলি"));
                add(new QuestionModule("সিঙ্গাপুরের জাতীয় ফুল কোনটি?", "B", "লিলি", "অর্কিড", "শাপলা", "টিউলিপ"));
                add(new QuestionModule("ব্রাজিলের জাতীয় ফুল কোনটি?", "D", "গোলাপ", "লিলি", "শাপলা", "ক্যাটলেয়া অর্কিড"));
                add(new QuestionModule("আর্জেন্টিনার জাতীয় ফুল কোনটি?", "A", "সিইবো", "পদ্ম", "লিলি", "গোলাপ"));
                add(new QuestionModule("ইতালির জাতীয় ফুল কোনটি?", "C", "কাঠগোলাপ",  "গোলাপ","লিলি", "চামেলী"));
                add(new QuestionModule("ফ্রান্সের জাতীয় ফুল কোনটি?", "B", "লিলি", "আইরিস", "শাপলা", "গোলাপ"));
                add(new QuestionModule("ইংল্যান্ডের জাতীয় ফুল কোনটি?", "D", "লিলি", "পদ্ম", "শাপলা", "গোলাপ"));
                add(new QuestionModule("রাশিয়ার জাতীয় ফুল কোনটি?", "A", "ক্যামোমিল", "গোলাপ", "শাপলা", "লিলি"));
                add(new QuestionModule("তুরস্কের জাতীয় ফুল কোনটি?", "C", "পদ্ম", "শাপলা", "টিউলিপ", "গোলাপ"));
                add(new QuestionModule("ফিলিপাইনের জাতীয় ফুল কোনটি?", "A", "জেসমিন", "গোলাপ", "লটাস", "লিলি"));
                add(new QuestionModule("মিশরের জাতীয় ফুল কোনটি?", "B", "ক্যামোমিল", "নীল পদ্ম ", "পদ্ম", "গোলাপ"));
                add(new QuestionModule("স্পেনের জাতীয় ফুল কোনটি?", "B", "লিলি", "কার্নেশন", "গোলাপ", "টিউলিপ"));
                add(new QuestionModule("দক্ষিণ আফ্রিকার জাতীয় ফুল কোনটি?", "C", "গুলবাহার", "গোলাপ", "প্রোটিয়া", "লিলি"));
                add(new QuestionModule("নিউজিল্যান্ডের জাতীয় ফুল কোনটি?", "B", "গোলাপ", "কাওহাই", "গাঁদা", "টিউলিপ"));
                add(new QuestionModule("সুইডেনের জাতীয় ফুল কোনটি?", "D", "লিলি", "কাওহাই", "গোলাপ", "লিনিয়া বোরেলিস"));


                add(new QuestionModule("বাংলাদেশের জাতীয় পাখি কোনটি?", "A", "দোয়েল", "কাক", "পায়রা", "কোকিল"));
                add(new QuestionModule("ভারতের জাতীয় পাখি কোনটি?", "B", "গোলাপি হাঁস", "ময়ূর", "কাক", "কবুতর"));
                add(new QuestionModule("শ্রীলঙ্কার জাতীয় পাখি কোনটি?", "C", "কাক", "হাঁস", "শ্রীলঙ্কান জঙ্গলফাউল", "চিল"));
                add(new QuestionModule("পাকিস্তানের জাতীয় পাখি কোনটি?", "D", "দোয়েল", "পায়রা", "বাজপাখি", "চকোর"));
                add(new QuestionModule("নেপালের জাতীয় পাখি কোনটি?", "A", "ডানফে", "কোকিল", "পায়রা", "টিয়া"));
                add(new QuestionModule("যুক্তরাষ্ট্রের জাতীয় পাখি কোনটি?", "B", "কাক", "ঈগল", "গোলাপি হাঁস", "ময়ূর"));
                add(new QuestionModule("যুক্তরাজ্যের জাতীয় পাখি কোনটি?", "C", "হাঁস", "কাক", "রবিন", "চিল"));
                add(new QuestionModule("অস্ট্রেলিয়ার জাতীয় পাখি কোনটি?", "D", "দোয়েল", "মাছরাঙা", "কোকিল", "ইমু"));
                add(new QuestionModule("জাপানের জাতীয় পাখি কোনটি?", "A", "সবুজ ফিজান্ট", "কাক", "পায়রা", "টিয়া"));
                add(new QuestionModule("চীনের জাতীয় পাখি কোনটি?", "B", "কাক", "সারস", "ময়ূর", "বাজপাখি"));
                add(new QuestionModule("রাশিয়ার জাতীয় পাখি কোনটি?", "C", "ময়ূর", "কোকিল", "ঈগল", "হাঁস"));
                add(new QuestionModule("ফ্রান্সের জাতীয় পাখি কোনটি?", "D", "কাক", "টিয়া", "ময়ূর", "মোরগ"));
                add(new QuestionModule("দক্ষিণ আফ্রিকার জাতীয় পাখি কোনটি?", "A", "সারস", "কোকিল", "পায়রা", "কাক"));
                add(new QuestionModule("মালয়েশিয়ার জাতীয় পাখি কোনটি?", "B", "মাছরাঙা", "রাইনো হর্নবিল", "ময়ূর", "কোকিল"));
                add(new QuestionModule("মেক্সিকোর জাতীয় পাখি কোনটি?", "C", "কাক", "পায়রা", "ঈগল", "টিয়া"));


                add(new QuestionModule("বাংলাদেশের জাতীয় প্রাণী কোনটি?", "A", "বাঘ","রাজহাঁস",  "হরিণ", "হাতি"));
                add(new QuestionModule("ভারতের জাতীয় প্রাণী কোনটি?", "B", "গরু", "বাঘ", "সিংহ", "হাতি"));
                add(new QuestionModule("শ্রীলঙ্কার জাতীয় প্রাণী কোনটি?", "C", "গণ্ডার", "বাঘ", "হাতি", "কুমির"));
                add(new QuestionModule("পাকিস্তানের জাতীয় প্রাণী কোনটি?", "D", "হরিণ", "কুমির", "হাতি", "মারখোর"));
                add(new QuestionModule("নেপালের জাতীয় প্রাণী কোনটি?", "A", "গরু", "বাঘ", "সিংহ", "হরিণ"));
                add(new QuestionModule("ভুটানের জাতীয় প্রাণী কোনটি?", "B", "হরিণ", "টাকিন", "বাঘ", "হাতি"));
                add(new QuestionModule("চীনের জাতীয় প্রাণী কোনটি?", "C", "হাতি", "কুকুর", "পান্ডা", "সিংহ"));
                add(new QuestionModule("জাপানের জাতীয় প্রাণী কোনটি?", "D", "বাঘ", "সারস", "সিংহ", "কুমির"));
                add(new QuestionModule("মালয়েশিয়ার জাতীয় প্রাণী কোনটি?", "A", "মালয়ান বাঘ ", "হাতি", "বাঘ", "সিংহ"));
                add(new QuestionModule("ইন্দোনেশিয়ার জাতীয় প্রাণী কোনটি?", "B",  "হাতি", "কোমোডো ড্রাগন","বাঘ", "সিংহ"));
                add(new QuestionModule("থাইল্যান্ডের জাতীয় প্রাণী কোনটি?", "C",  "জিরাফ", "বাঘ", "থাই হাতি","সিংহ"));

                add(new QuestionModule("ভিয়েতনামের জাতীয় প্রাণী কোনটি?", "A", "ওয়াটার বাফেলো", "বাঘ", "সিংহ", "হাতি"));

                add(new QuestionModule("মিয়ানমারের জাতীয় প্রাণী কোনটি?", "A", "বাঘ", "হাতি", "গণ্ডার", "কুমির"));
                add(new QuestionModule("মালদ্বীপের জাতীয় প্রাণী কোনটি?", "B",  "হাতি"," টুনা", "বাঘ", "সিংহ"));
                add(new QuestionModule("মঙ্গোলিয়ার জাতীয় প্রাণী কোনটি?", "C", "তাহর", "কুমির", "ঘোড়া", "বাঘ"));
                add(new QuestionModule("তাজিকিস্তানের জাতীয় প্রাণী কোনটি?", "A", "মারখোর", "হাতি", "বাঘ", "কুমির"));
                  add(new QuestionModule("আফগানিস্তানের জাতীয় প্রাণী কোনটি?", "A", "স্নো লেপার্ড", "বাঘ", "হাতি", "কুমির"));
                add(new QuestionModule("ইরানের জাতীয় প্রাণী কোনটি?", "B", "পার্সিয়ান চিতা", "হাতি", "বাঘ", "সিংহ"));

                add(new QuestionModule("সৌদি আরবের জাতীয় প্রাণী কোনটি?", "D", "ডোরাকাটা হায়েনা", "হাতি", "সিংহ", "উট"));
                add(new QuestionModule("লেবাননের জাতীয় প্রাণী কোনটি?", "B",  "হাতি","হায়েনা", "সিংহ", "বাঘ"));



            }

        };
        QuestionModule.createQuestionsForSubject("জাতীয়", R.drawable.national, questions);











    }




//====================================================================
// ====================================================================
//====================================================================
// ====================================================================



}