package com.appteve.quitsmok;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.SystemClock;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Fade;
import android.transition.Slide;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import io.netopen.hotbitmapgg.library.view.RingProgressBar;

public class HealthActivity extends AppCompatActivity implements AppData {

    SharedPreferences prefs;
    Date dateEnd;
    float cigPerDay;
    float cigInPack;
    float pricePack;
    float yearsSmok;
    String currency;
    float dateToProgress;

    float endProgress20Min = 1200;
    float end8Hours = 28800;
    float end24Hours = 86400;
    float end48Hours = 172800;
    float end72Hours =  259200;
    float end12Weeks = 7257599;
    float end9Month = 23328000;
    float end5Years = 157680000;
    float end10Years = 315360000;

    TextView header01;
    TextView fotter01;
    TextView counter01;
    RingProgressBar progress01;

    TextView header8Hours;
    TextView fotter8Hours;
    TextView counter8Hours;
    RingProgressBar progress8Hours;

    TextView header24Hours;
    TextView fotter24Hours;
    TextView counter24Hours;
    RingProgressBar progress24Hours;

    TextView header48Hours;
    TextView fotter48Hours;
    TextView counter48Hours;
    RingProgressBar progress48Hours;

    TextView header72Hours;
    TextView fotter72Hours;
    TextView counter72Hours;
    RingProgressBar progress72Hours;

    TextView header12Weeks;
    TextView fotter12Weeks;
    TextView counter12Weeks;
    RingProgressBar progress12Weeks;

    TextView header9Month;
    TextView fotter9Month;
    TextView counter9Month;
    RingProgressBar progress9Month;

    TextView header5Years;
    TextView fotter5Years;
    TextView counter5Years;
    RingProgressBar progress5Years;

    TextView header10Years;
    TextView fotter10Years;
    TextView counter10Years;
    RingProgressBar progress10Years;

    InterstitialAd mInterstitialAd;

    private Timer timer;
    private TimerTask timerTask;
    Boolean isADS;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health);
        prefs = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        setTitle(R.string.user_health);


        progress01 = (RingProgressBar) findViewById(R.id.progress01);
        header01 = (TextView) findViewById(R.id.header1Text);
        fotter01 = (TextView ) findViewById(R.id.fotter01Text);
        counter01 = (TextView) findViewById(R.id.counter01);

        header8Hours = (TextView) findViewById(R.id.header8Hours);
        fotter8Hours = (TextView) findViewById(R.id.fotter8Hours);
        counter8Hours = (TextView) findViewById(R.id.counter8Hours);
        progress8Hours = (RingProgressBar) findViewById(R.id.progress8Hours);

        header24Hours = (TextView) findViewById(R.id.header24Hours);
        fotter24Hours = (TextView) findViewById(R.id.fotter24Hours);
        counter24Hours = (TextView) findViewById(R.id.counter24Hours);
        progress24Hours = (RingProgressBar) findViewById(R.id.progress24Hours);

        header48Hours = (TextView) findViewById(R.id.header48Hours);
        fotter48Hours = (TextView) findViewById(R.id.fotter48Hours);
        counter48Hours = (TextView) findViewById(R.id.counter48Hours);
        progress48Hours = (RingProgressBar) findViewById(R.id.progress48Hours);

        header72Hours = (TextView) findViewById(R.id.header72Hours);
        fotter72Hours = (TextView) findViewById(R.id.fotter72Hours);
        counter72Hours = (TextView) findViewById(R.id.counter72Hours);
        progress72Hours = (RingProgressBar) findViewById(R.id.progress72Hours);

        header12Weeks = (TextView) findViewById(R.id.header12Weeks);
        fotter12Weeks = (TextView) findViewById(R.id.fotter12Weeks);
        counter12Weeks = (TextView) findViewById(R.id.counter12Weeks);
        progress12Weeks = (RingProgressBar) findViewById(R.id.progress12Weeks);

        header9Month = (TextView) findViewById(R.id.header9Month);
        fotter9Month = (TextView) findViewById(R.id.fotter9Month);
        counter9Month = (TextView) findViewById(R.id.counter9Month);
        progress9Month = (RingProgressBar) findViewById(R.id.progress9Month);

        header5Years = (TextView) findViewById(R.id.header5Years);
        fotter5Years = (TextView) findViewById(R.id.fotter5Years);
        counter5Years = (TextView) findViewById(R.id.counter5Years);
        progress5Years = (RingProgressBar) findViewById(R.id.progress5Years);

        header10Years = (TextView) findViewById(R.id.header10Years);
        fotter10Years = (TextView) findViewById(R.id.fotter10Years);
        counter10Years = (TextView) findViewById(R.id.counter10Years);
        progress10Years = (RingProgressBar) findViewById(R.id.progress10Years);



        header01.setText(R.string.header_text_01_health);
        header8Hours.setText(R.string.header_8hours);
        header24Hours.setText(R.string.header_24_hours);
        header48Hours.setText(R.string.header_48_hours);
        header72Hours.setText(R.string.header_72_hours);
        header12Weeks.setText(R.string.header_12_weeaks);
        header9Month.setText(R.string.header_9_Month);
        header5Years.setText(R.string.header_5_years);
        header10Years.setText(R.string.header_10_years);




        BottomBar bottomBar = (BottomBar) findViewById(R.id.bottomBar);
        bottomBar.setDefaultTabPosition(1);
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                if (tabId == R.id.tab_outsmocking) {

                    Intent startIntent = new Intent(HealthActivity.this, MainActivity.class);
                    startActivity(startIntent);
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                }

                if (tabId == R.id.tab_awards) {

                    Intent startIntent = new Intent(HealthActivity.this, AwardsActivity.class);
                    startActivity(startIntent);
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                }

                if (tabId == R.id.tab_settings) {

                    Intent startIntent = new Intent(HealthActivity.this, SettingsActivity.class);
                    startActivity(startIntent);
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

                }

            }
        });

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(INTERSTITIAL_ID);

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                requestNewInterstitial();

            }
        });

        requestNewInterstitial();



        getUserData();
    }

    void getUserData(){

        dateEnd = new Date(prefs.getLong(USER_DATE, 0));
        cigPerDay = prefs.getFloat(USER_CIG_PER_DAY,0);
        cigInPack = prefs.getFloat(USER_CIG_IN_PACK,0);
        pricePack = prefs.getFloat(USER_PRICE_PACK,0);
        yearsSmok = prefs.getFloat(USER_YEARS_SMOK,0);
        currency = prefs.getString(USER_CURRENSY,"");
        isADS = prefs.getBoolean(AD_REMOVE,false);

        getHealth();


    }

    void getHealth(){

        Date d = new Date();
        long diffInMs = d.getTime() - dateEnd.getTime();

        dateToProgress = TimeUnit.MILLISECONDS.toSeconds(diffInMs);

        if (dateToProgress < endProgress20Min){

            float prog20 = dateToProgress / endProgress20Min;

            float presetns = prog20 * 100;
            int prigr =(int) presetns;

//            progress01.setProgress(prigr);
            fotter01.setText(R.string.progress);
            counter01.setText(prigr + "%");

        } else {

//            progress01.setProgress(100);
            fotter01.setText(R.string.done);
            counter01.setText( "100%");


        }

        if (dateToProgress < end8Hours){

            float progressSec = dateToProgress / end8Hours;
            float percent8 = progressSec * 100;
            int prog =(int) percent8;

//            progress8Hours.setProgress(prog);
            fotter8Hours.setText(R.string.progress);
            counter8Hours.setText(prog + "%");

        } else {

//            progress8Hours.setProgress(100);
            fotter8Hours.setText(R.string.done);
            counter8Hours.setText("100%");


        }

        if (dateToProgress < end24Hours){

            float progressSec = dateToProgress / end24Hours;
            float percent = progressSec * 100;
            int prog =(int) percent;

//            progress24Hours.setProgress(prog);
            fotter24Hours.setText(R.string.progress);
            counter24Hours.setText(prog + "%");

        } else {

//            progress24Hours.setProgress(100);
            fotter24Hours.setText(R.string.done);
            counter24Hours.setText("100%");


        }

        if (dateToProgress < end48Hours){

            float progressSec = dateToProgress / end48Hours;
            float percent = progressSec * 100;
            int prog =(int) percent;

//            progress48Hours.setProgress(prog);
            fotter48Hours.setText(R.string.progress);
            counter48Hours.setText(prog + "%");

        } else {

//            progress48Hours.setProgress(100);
            fotter48Hours.setText(R.string.done);
            counter48Hours.setText("100%");


        }

        if (dateToProgress < end72Hours){

            float progressSec = dateToProgress / end72Hours;
            float percent = progressSec * 100;
            int prog =(int) percent;

//            progress72Hours.setProgress(prog);
            fotter72Hours.setText(R.string.progress);
            counter72Hours.setText(prog + "%");

        } else {

//            progress72Hours.setProgress(100);
            fotter72Hours.setText(R.string.done);
            counter72Hours.setText("100%");


        }

        if (dateToProgress < end12Weeks){

            float progressSec = dateToProgress / end12Weeks;
            float percent = progressSec * 100;
            int prog =(int) percent;
//
//            progress12Weeks.setProgress(prog);
            fotter12Weeks.setText(R.string.progress);
            counter12Weeks.setText(prog + "%");

        } else {

//            progress12Weeks.setProgress(100);
            fotter12Weeks.setText(R.string.done);
            counter12Weeks.setText("100%");


        }

        if (dateToProgress < end9Month){

            float progressSec = dateToProgress / end9Month;
            float percent = progressSec * 100;
            int prog =(int) percent;

//            progress9Month.setProgress(prog);
            fotter9Month.setText(R.string.progress);
            counter9Month.setText(prog + "%");

        } else {

//            progress9Month.setProgress(100);
            fotter9Month.setText(R.string.done);
            counter9Month.setText("100%");


        }

        if (dateToProgress < end5Years){

            float progressSec = dateToProgress / end5Years;
            float percent = progressSec * 100;
            int prog =(int) percent;

//            progress5Years.setProgress(prog);
            fotter5Years.setText(R.string.progress);
            counter5Years.setText(prog + "%");

        } else {

//            progress5Years.setProgress(100);
            fotter5Years.setText(R.string.done);
            counter5Years.setText("100%");


        }

        if (dateToProgress < end10Years){

            float progressSec = dateToProgress / end10Years;
            float percent = progressSec * 100;
            int prog =(int) percent;

//            progress10Years.setProgress(prog);
            fotter10Years.setText(R.string.progress);
            counter10Years.setText(prog + "%");

        } else {

//            progress10Years.setProgress(100);
            fotter10Years.setText(R.string.done);
            counter10Years.setText("100%");


        }



    }

    public void onPause(){
        super.onPause();
        timer.cancel();
    }

    public void onResume(){
        super.onResume();
        try {
            timer = new Timer();
            timerTask = new TimerTask() {
                @Override
                public void run() {

                    showMyInterstitial();
                }
            };
            timer.schedule(timerTask, PAUSE_SHOW_INTERSTITIAL, PAUSE_SHOW_INTERSTITIAL);
        } catch (IllegalStateException e){
            android.util.Log.i("Damn", "resume error");
        }
    }



    private void showMyInterstitial() {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                if (mInterstitialAd.isLoaded()) {
                    if(isADS){

                    } else {

                        mInterstitialAd.show();
                    }

                } else {


                }

            }
        });
    }

    private void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("SEE_YOUR_LOGCAT_TO_GET_YOUR_DEVICE_ID")
                .build();

        mInterstitialAd.loadAd(adRequest);
    }









}
