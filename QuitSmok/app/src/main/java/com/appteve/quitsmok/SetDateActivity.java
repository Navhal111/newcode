package com.appteve.quitsmok;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.kunzisoft.switchdatetime.SwitchDateTimeDialogFragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;


public class SetDateActivity extends AppCompatActivity implements AppData {

    private static final String TAG_DATETIME_FRAGMENT = "TAG_DATETIME_FRAGMENT";
    private static final String STATE_TEXTVIEW = "STATE_TEXTVIEW";
    private static final String TAG = "Sample";
    private SwitchDateTimeDialogFragment dateTimeFragment;
    TextView header;

    private SharedPreferences prefs;
    Button chooseDateBtn;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_date);

        header = (TextView) findViewById(R.id.header_set);
        header.setText(R.string.first_screen_text);
        prefs = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        chooseDateBtn = (Button) findViewById(R.id.button) ;
        chooseDateBtn.setText(R.string.choose_btn);

        dateTimeFragment = (SwitchDateTimeDialogFragment) getSupportFragmentManager().findFragmentByTag(TAG_DATETIME_FRAGMENT);
        if(dateTimeFragment == null) {
            dateTimeFragment = SwitchDateTimeDialogFragment.newInstance(
                    getString(R.string.label_datetime_dialog),
                    getString(R.string.positive_button_datetime_picker),
                    getString(R.string.negative_button_datetime_picker)
            );
        }

        Calendar c = Calendar.getInstance();
        int seconds = c.get(Calendar.SECOND);
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        int hours = c.get(Calendar.HOUR);

        int min = c.get(Calendar.MINUTE);

        // Assign values we want
        final SimpleDateFormat myDateFormat = new SimpleDateFormat("d MMM yyyy HH:mm", java.util.Locale.getDefault());
        dateTimeFragment.startAtCalendarView();
        dateTimeFragment.set24HoursMode(true);
        dateTimeFragment.setMinimumDateTime(new GregorianCalendar(2015, Calendar.JANUARY, 1).getTime());
        dateTimeFragment.setMaximumDateTime(new GregorianCalendar(2025, Calendar.DECEMBER, 31).getTime());
        dateTimeFragment.setDefaultDateTime(new GregorianCalendar(year, month, day, hours, min).getTime());

        try {
            dateTimeFragment.setSimpleDateMonthAndDayFormat(new SimpleDateFormat("MMMM dd", Locale.getDefault()));
        } catch (SwitchDateTimeDialogFragment.SimpleDateMonthAndDayFormatException e) {
            Log.e(TAG, e.getMessage());
        }

        // Set listener for date
        dateTimeFragment.setOnButtonClickListener(new SwitchDateTimeDialogFragment.OnButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Date date) {

                prefs.edit().putLong(USER_DATE, date.getTime()).apply();

                Intent startIntent = new Intent(SetDateActivity.this, CigaretPerDayActivity.class);
                startActivity(startIntent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

            }

            @Override
            public void onNegativeButtonClick(Date date) {

            }
        });

        Button buttonView = (Button) findViewById(R.id.button);
        buttonView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dateTimeFragment.show(getSupportFragmentManager(), TAG_DATETIME_FRAGMENT);
            }
        });

    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save the current textView

        super.onSaveInstanceState(savedInstanceState);
    }


}
