package com.appteve.quitsmok;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Handler;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Fade;
import android.transition.Slide;
import android.transition.TransitionInflater;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;


public class MainActivity extends AppCompatActivity implements  AppData{

    SharedPreferences prefs;
    Date dateEnd;
    float cigPerDay;
    float cigInPack;
    float pricePack;
    float yearsSmok;
    String currency;
    Boolean isAds;

    long startTime = 0;

    TextView headerText;
    TextView counterText;
    TextView cigNotSmokedHeader;
    TextView moneySavedHeader;
    TextView cigNotSmockCounter;
    TextView moneySavedCount;
    TextView header2;
    TextView getLabel1Week;
    TextView getLabel1Month;
    TextView getLabel1Year;
    TextView getLabel5Years;
    TextView getLabel10Years;
    TextView getLabel20Years;
    TextView labelLifeLost;
    TextView labelCigSmokedHeader;
    TextView lifeLostCounter;
    TextView cigSmokedCounter;
    TextView moneyWastedHeaderLabel;
    TextView moneyWastedCounter;
    LinearLayout ad;

    Handler timerHandler = new Handler();
    Runnable timerRunnable = new Runnable() {

        @Override
        public void run() {
            long millis = System.currentTimeMillis() - startTime;
            int seconds = (int) (millis / 1000);
            int minutes = seconds / 60;
            seconds = seconds % 60;

          dataCount();

            timerHandler.postDelayed(this, 1000);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        prefs = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        MobileAds.initialize(getApplicationContext(), BANNER_ID);
        getUserData();
        isAds = prefs.getBoolean(AD_REMOVE,false);

        ad = (LinearLayout) findViewById(R.id.ads);

        headerText = (TextView) findViewById(R.id.headerText);
        counterText = (TextView) findViewById(R.id.countText);
        cigNotSmokedHeader = (TextView) findViewById(R.id.cigNotSmokedText);
        moneySavedHeader =(TextView) findViewById(R.id.moneySawedText);
        cigNotSmockCounter = (TextView) findViewById(R.id.notSmokCounter);
        moneySavedCount = (TextView) findViewById(R.id.moneySavedCount);
        header2 = (TextView) findViewById(R.id.header2);
        getLabel1Week = (TextView) findViewById(R.id.textGet1Week);
        getLabel1Month = (TextView) findViewById(R.id.textGet1Month);
        getLabel1Year = (TextView) findViewById(R.id.textGet1Year) ;
        getLabel5Years = (TextView) findViewById(R.id.textGet5Year) ;
        getLabel10Years = (TextView) findViewById(R.id.textGet10Year);
        getLabel20Years = (TextView) findViewById(R.id.textGet20Year);
        labelLifeLost = (TextView) findViewById(R.id.lifeLostTextHeader);
        labelCigSmokedHeader = (TextView) findViewById(R.id.smockedTextHeader);
        lifeLostCounter = (TextView) findViewById(R.id.lifeLostCounter);
        cigSmokedCounter = (TextView) findViewById(R.id.smokedCounterz) ;
        moneyWastedHeaderLabel = (TextView) findViewById(R.id.moneyWastedHeader);
        moneyWastedCounter = (TextView) findViewById(R.id.moneyWastedCounter);

        headerText.setText(R.string.header_main_text);
        cigNotSmokedHeader.setText(R.string.cig_not_smoked_header);
        moneySavedHeader.setText(R.string.money_saved_header);
        header2.setText(R.string.header_2);
        labelLifeLost.setText(R.string.life_lost_header);
        labelCigSmokedHeader.setText(R.string.cig_smoked);
        moneyWastedHeaderLabel.setText(R.string.money_wasted_header);



        BottomBar bottomBar = (BottomBar) findViewById(R.id.bottomBar);
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                if (tabId == R.id.tab_health) {

                    Intent startIntent = new Intent(MainActivity.this, HealthActivity.class);
                    startActivity(startIntent);
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                }

                if (tabId == R.id.tab_awards) {

                    Intent startIntent = new Intent(MainActivity.this, AwardsActivity.class);
                    startActivity(startIntent);
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                }

                if (tabId == R.id.tab_settings) {

                    Intent startIntent = new Intent(MainActivity.this, SettingsActivity.class);
                    startActivity(startIntent);
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

                }



            }
        });


        if (isAds) {

            ad.setVisibility(LinearLayout.GONE);

        } else {

            AdView mAdView = (AdView) findViewById(R.id.adView);
            AdRequest adRequest = new AdRequest.Builder().build();
            mAdView.loadAd(adRequest);
        }

    }

    void getUserData(){

        dateEnd = new Date(prefs.getLong(USER_DATE, 0));
        cigPerDay = prefs.getFloat(USER_CIG_PER_DAY,0);
        cigInPack = prefs.getFloat(USER_CIG_IN_PACK,0);
        pricePack = prefs.getFloat(USER_PRICE_PACK,0);
        yearsSmok = prefs.getFloat(USER_YEARS_SMOK,0);
        currency = prefs.getString(USER_CURRENSY,"");

        startTime = System.currentTimeMillis();
        timerHandler.postDelayed(timerRunnable, 0);




    }

    protected void onPause() {
        super.onPause();
        timerHandler.removeCallbacks(timerRunnable);
    }

    void dataCount(){

        Date d = new Date();
        long diffInMs = d.getTime() - dateEnd.getTime();

        long diffInSec = TimeUnit.MILLISECONDS.toSeconds(diffInMs);


        int day = (int) TimeUnit.SECONDS.toDays(diffInSec);
        long hours = TimeUnit.SECONDS.toHours(diffInSec) -
                TimeUnit.DAYS.toHours(day);
        long minute = TimeUnit.SECONDS.toMinutes(diffInSec) -
                TimeUnit.HOURS.toMinutes(TimeUnit.SECONDS.toHours(diffInSec));
        long second = TimeUnit.SECONDS.toSeconds(diffInSec) -
                TimeUnit.MINUTES.toSeconds(TimeUnit.SECONDS.toMinutes(diffInSec));


        String formater = day + " "+ getResources().getString(R.string.day)+ " " +  hours + " " + getResources().getString(R.string.hours)+ " " + minute + " " + getResources().getString(R.string.min)+ " " + second + " "+ getResources().getString(R.string.sec);
        counterText.setText(formater);

        int cig  = 86400 / (int)cigPerDay;
        int countCig = (int)diffInSec / cig;


        String cigCountz =  "" +countCig;
        cigNotSmockCounter.setText(cigCountz);

        float priced = 86400 / pricePack;
        float econom = (float)diffInSec  / priced;

        String s = String.format("%.2f", econom);

        String economy = s + " " + currency;

        moneySavedCount.setText(economy);

        float  saveMoneyWeek = 604800 / priced;
        float  saveDayWeek = ((float)604800 / (float)6.5) / 86400;

        String saveMpneysFloat = String.format("%.2f", saveMoneyWeek);
        String saveDaysWeekFloat =  String.format("%.2f", saveDayWeek);

        getLabel1Week.setText(getResources().getString(R.string.one_week) + " " + saveMpneysFloat + " " + currency + " " + saveDaysWeekFloat + " " + getResources().getString(R.string.day_save));

        float  saveMoneyMonth = 2592000 / priced;
        float  saveDayMonth = (((float)2592000.00) / (float)6.5) / 86400;

        String saveMoneyFloatMonth = String.format("%.2f", saveMoneyMonth);
        String saveDaysFloatMonth =  String.format("%.2f", saveDayMonth);

        getLabel1Month.setText(getResources().getString(R.string.one_month) + " " + saveMoneyFloatMonth + " " + currency + " " + saveDaysFloatMonth + " " + getResources().getString(R.string.day_save));

        float  saveMoneyYear = 31536000 / priced;
        float  saveDayYear = ((float)(31536000.00042889) / (float)6.5) / 86400;


        String saveMoneyFloat1Year = String.format("%.2f", saveMoneyYear);
        String saveDaysFloat1Year =  String.format("%.2f", saveDayYear);

        getLabel1Year.setText(getResources().getString(R.string.one_year) + " " + saveMoneyFloat1Year + " " + currency + " " + saveDaysFloat1Year + " " + getResources().getString(R.string.day_save));

        float  saveMoney5Year = 157680000/ priced;
        float  saveDay5Year = ((float)(157680000.00214446) / (float)6.5) / 86400;

        String saveMoneyFloat5Year = String.format("%.2f", saveMoney5Year);
        String saveDaysFloat5Year =  String.format("%.2f", saveDay5Year);

        getLabel5Years.setText(getResources().getString(R.string.five_years) + " " + saveMoneyFloat5Year + " " + currency + " " + saveDaysFloat5Year + " " + getResources().getString(R.string.day_save));

        float  saveMoney10Year = 315360000 / priced;
        float  saveDay10Year = ((float)(315360000) / (float)6.5) / 86400;

        String saveMoneyFloat10Year = String.format("%.2f", saveMoney10Year);
        String saveDaysFloat10Year =  String.format("%.2f", saveDay10Year);

        getLabel10Years.setText(getResources().getString(R.string.ten_years) + " " + saveMoneyFloat10Year + " " + currency + " " + saveDaysFloat10Year + " " + getResources().getString(R.string.day_save));

        float  saveMoney20Year = 630720000 / priced;
        float  saveDay20Year = ((float)(630720000) / (float)6.5) / 86400;

        String saveMoneyFloat20Year = String.format("%.2f", saveMoney20Year);
        String saveDaysFloat20Year =  String.format("%.2f", saveDay20Year);

        getLabel20Years.setText(getResources().getString(R.string.twenty_years) + " " + saveMoneyFloat20Year + " " + currency + " " + saveDaysFloat20Year + " " + getResources().getString(R.string.day_save));

        int lostTime = (int)(yearsSmok * 31536000) / 6;

        int dayLost = lostTime / 86400;
        int hoursLost = (lostTime % 86400) / 3600;

        lifeLostCounter.setText(dayLost + " " +getResources().getString(R.string.days)+ " " + hoursLost + " " + getResources().getString(R.string.hours_a));

        int cigc  = 86400 / (int)cigPerDay;
        int countCigCount = (int)(yearsSmok * 31536000) / cigc;

        String counts = ""  + countCigCount;

        cigSmokedCounter.setText(counts);

        float priceds = 86400 / pricePack;
        float economs = yearsSmok * 31536000 / priceds;
        String wasted =  String.format("%.2f", economs);

        moneyWastedCounter.setText(wasted + " " + currency);



    }




}
