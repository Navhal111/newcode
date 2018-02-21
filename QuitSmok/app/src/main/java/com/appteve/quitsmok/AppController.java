package com.appteve.quitsmok;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.Settings;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Date;

/**
 * Created by appteve on 30/01/2017.
 */

public class AppController extends Application  implements AppData{

    SharedPreferences prefs;


    public void onCreate() {
        super.onCreate();

        FontsOverride.setDefaultFont(this, "DEFAULT", "appteve/roboto-condensed.light.ttf");
        FontsOverride.setDefaultFont(this, "MONOSPACE", "appteve/roboto-condensed.light.ttf");
        FontsOverride.setDefaultFont(this, "SERIF", "appteve/roboto-condensed.light.ttf");
        FontsOverride.setDefaultFont(this, "SANS_SERIF", "appteve/roboto-condensed.light.ttf");
        FontsOverride.setDefaultFont(this, "DEFAULT_BOLD", "appteve/roboto-condensed.light.ttf");


        prefs = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);

        float myDate = prefs.getFloat(USER_CIG_PER_DAY,0);
        Date myDates = new Date(prefs.getLong(USER_DATE, 0));

        if(myDate == 0) {

            Intent startIntent = new Intent(this, SetDateActivity.class);
            startIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(startIntent);


        } else {

            System.out.println("Start app MAIN SCREEN ");
        }

        if (prefs.getBoolean("firstrun", true)) {
                // Do first run stuff here then set 'firstrun' as false
                // using the following line to edit/commit prefs
                prefs.edit().putBoolean("firstrun", false).commit();



            }



    }


}
