package com.appteve.quitsmok;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.annotation.IdRes;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.appteve.quitsmok.database.AwardsContract;
import com.appteve.quitsmok.database.AwardsDbHelper;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class AwardsActivity extends AppCompatActivity implements AppData {

    private AwardsDbHelper mHelper;
    ListView awardsListView;
    InterstitialAd mInterstitialAd;
    private Timer timer;
    private TimerTask timerTask;
    Boolean isADS;
    SharedPreferences prefs;
    final ArrayList<AwardsItem> awardsItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_awards);
        setTitle(R.string.user_awards);
        prefs = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        isADS = prefs.getBoolean(AD_REMOVE,false);

        mHelper = new AwardsDbHelper(this);
        awardsListView = (ListView) findViewById(R.id.awardsList);

        BottomBar bottomBar = (BottomBar) findViewById(R.id.bottomBar);
        bottomBar.setDefaultTabPosition(2);
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                if (tabId == R.id.tab_outsmocking) {

                    Intent startIntent = new Intent(AwardsActivity.this, MainActivity.class);
                    startActivity(startIntent);
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                }

                if (tabId == R.id.tab_health) {

                    Intent startIntent = new Intent(AwardsActivity.this, HealthActivity.class);
                    startActivity(startIntent);
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                }

                if (tabId == R.id.tab_settings){

                    Intent startIntent = new Intent(AwardsActivity.this, SettingsActivity.class);
                    startActivity(startIntent);
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);


                }

            }
        });

        fetchData();

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(INTERSTITIAL_ID);

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                requestNewInterstitial();

            }
        });

        requestNewInterstitial();




    }

    public void deleteTask(View view) {
        View parent = (View) view.getParent();
        TextView taskTextView = (TextView) parent.findViewById(R.id.awardsHeaderText);
        String task = String.valueOf(taskTextView.getText());
        SQLiteDatabase db = mHelper.getWritableDatabase();
        db.delete(AwardsContract.AwardsEntry.TABLE,
                AwardsContract.AwardsEntry.COL_TASK_TITLE + " = ?",
                new String[]{task});
        db.close();
    }



    public void fetchData(){

        awardsItems.clear();

        SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor cursor = db.query(AwardsContract.AwardsEntry.TABLE,
                new String[]{AwardsContract.AwardsEntry._ID, AwardsContract.AwardsEntry.COL_TASK_TITLE,AwardsContract.AwardsEntry.COL_TASK_PRICE, AwardsContract.AwardsEntry.COL_TASK_DATE},
                null, null, null, null, null);
        while (cursor.moveToNext()) {
            int idx = cursor.getColumnIndex(AwardsContract.AwardsEntry.COL_TASK_TITLE);

            int idxp = cursor.getColumnIndex(AwardsContract.AwardsEntry.COL_TASK_PRICE);

            int idxpdate = cursor.getColumnIndex(AwardsContract.AwardsEntry.COL_TASK_DATE);


            String title = cursor.getString(idx);
            String price = cursor.getString(idxp);
            String date = cursor.getString(idxpdate);


            awardsItems.add(new AwardsItem(title,price,date));
        }
        updatesUi();

    }

    public void updatesUi(){

        try {

            final ListView awardsLists = (ListView) findViewById(R.id.awardsList);

            AwardsAdapter adapter = new AwardsAdapter(getApplicationContext(), awardsItems);
            awardsLists.setAdapter(adapter);
            adapter.notifyDataSetChanged();

            awardsLists.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                }


            });

            awardsLists.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                               int arg2, long arg3) {

                    String task = awardsItems.get(arg2).titles;
                    SQLiteDatabase db = mHelper.getWritableDatabase();
                    db.delete(AwardsContract.AwardsEntry.TABLE,
                            AwardsContract.AwardsEntry.COL_TASK_TITLE + " = ?",
                            new String[]{task});
                    db.close();
                    fetchData();

                    return true;
                }
            });
        } catch (NullPointerException e){
            Toast.makeText(getApplicationContext(), "No File", Toast.LENGTH_LONG).show();
        }



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_name:

                LinearLayout layout = new LinearLayout(this);
                layout.setOrientation(LinearLayout.VERTICAL);

                final EditText awardTitle = new EditText(this);
                final EditText awardPrice = new EditText(this);
                awardPrice.setInputType(InputType.TYPE_NUMBER_FLAG_SIGNED);
                awardTitle.setHint(R.string.hint_award_title);
                awardPrice.setHint(R.string.hint_award_price);

                layout.addView(awardTitle);
                layout.addView(awardPrice);

                AlertDialog dialog = new AlertDialog.Builder(this)
                        .setTitle(R.string.title_add_awards)
                        .setMessage(R.string.award_add_message)
                        .setView(layout)
                        .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String task = String.valueOf(awardTitle.getText());
                                String price = String.valueOf(awardPrice.getText());
                                SQLiteDatabase db = mHelper.getWritableDatabase();
                                ContentValues values = new ContentValues();
                                values.put(AwardsContract.AwardsEntry.COL_TASK_TITLE, task);
                                values.put(AwardsContract.AwardsEntry.COL_TASK_PRICE, price);
                                values.put(AwardsContract.AwardsEntry.COL_TASK_DATE, getDateTime());
                                db.insertWithOnConflict(AwardsContract.AwardsEntry.TABLE,
                                        null,
                                        values,
                                        SQLiteDatabase.CONFLICT_REPLACE);
                                db.close();
                                fetchData();
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .create();
                dialog.show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
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
