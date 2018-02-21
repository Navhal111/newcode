package com.appteve.quitsmok;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.appteve.quitsmok.database.AwardsContract;
import com.eminayar.panter.DialogType;
import com.eminayar.panter.PanterDialog;
import com.eminayar.panter.enums.Animation;
import com.eminayar.panter.interfaces.OnSingleCallbackConfirmListener;
import com.kunzisoft.switchdatetime.SwitchDateTimeDialogFragment;
import com.onurciner.toastox.ToastOXDialog;
import com.yarolegovich.lovelydialog.LovelyStandardDialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;


import pl.polak.clicknumberpicker.ClickNumberPickerListener;
import pl.polak.clicknumberpicker.ClickNumberPickerView;
import pl.polak.clicknumberpicker.PickerClickType;




public class UserDataActivity extends AppCompatActivity implements  AppData {

    private SwitchDateTimeDialogFragment dateTimeFragment;
    private static final String TAG_DATETIME_FRAGMENT = "TAG_DATETIME_FRAGMENT";
    private static final String TAG = "Sample";

    SharedPreferences prefs;
    TextView userDataEnds;
    Button dateBtn;
    TextView cigPerDayLabel;
    ClickNumberPickerView pickerCigPerDay;
    TextView cigInPackLabel;
    ClickNumberPickerView pickerCigInPack;
    TextView pricePackLabel;
    ClickNumberPickerView pickerPrice;
    TextView laberYearSmok;
    ClickNumberPickerView pickerYears;
    TextView currencyLabel;
    Button changeCuttencyBtn;
    Date dateEnd;
    float cigPerDay;
    float cigInPack;
    float pricePack;
    float yearsSmok;
    String currency;
    float userPoints;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_data);
        prefs = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        makeUIS();
        getUserData();

    }

    public void makeUIS(){

        userDataEnds = (TextView) findViewById(R.id.userEndDataText);
        dateBtn = (Button) findViewById(R.id.dateBtn);
        dateBtn.setText(getResources().getString(R.string.change_date_btn));
        cigPerDayLabel = (TextView) findViewById(R.id.cigPerDayText);
        cigPerDayLabel.setText(R.string.header_cig_per_day_text);
        pickerCigPerDay = (ClickNumberPickerView) findViewById(R.id.clickpikerCigPerDay) ;

        pickerCigPerDay.setClickNumberPickerListener(new ClickNumberPickerListener() {
            @Override
            public void onValueChange(float previousValue, float currentValue, PickerClickType pickerClickType) {

                cigPerDay = currentValue;

            }
        });

        cigInPackLabel = (TextView) findViewById(R.id.cigInPackText);
        cigInPackLabel.setText(R.string.header_cig_in_pack);

        pickerCigInPack = (ClickNumberPickerView) findViewById(R.id.clickpikerCigInPack);

        pickerCigInPack.setClickNumberPickerListener(new ClickNumberPickerListener() {
            @Override
            public void onValueChange(float previousValue, float currentValue, PickerClickType pickerClickType) {

                cigInPack = currentValue;

            }
        });

        pricePackLabel = (TextView) findViewById(R.id.pricePackText);
        pricePackLabel.setText(R.string.price_pack);

        pickerPrice = (ClickNumberPickerView) findViewById(R.id.clickpikerPrice) ;
        pickerPrice.setClickNumberPickerListener(new ClickNumberPickerListener() {
            @Override
            public void onValueChange(float previousValue, float currentValue, PickerClickType pickerClickType) {

                pricePack = currentValue;

            }
        });

        laberYearSmok = (TextView) findViewById(R.id.yearsSmokText);
        laberYearSmok.setText(R.string.years_smok);

        pickerYears = (ClickNumberPickerView) findViewById(R.id.clickpikerYears);
        pickerYears.setClickNumberPickerListener(new ClickNumberPickerListener() {
            @Override
            public void onValueChange(float previousValue, float currentValue, PickerClickType pickerClickType) {

                yearsSmok = currentValue;

            }
        });

        currencyLabel = (TextView) findViewById(R.id.currencyText);

        changeCuttencyBtn = (Button) findViewById(R.id.currencyBtn);
        changeCuttencyBtn.setText(R.string.change_cuttency_btn);

        /// datatime

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

                dateEnd = date;
                String datezs = DateFormat.format("yyyy.MM.dd HH:mm:ss", dateEnd).toString();
                userDataEnds.setText(getResources().getString(R.string.date_end)+ " " + datezs);

            }

            @Override
            public void onNegativeButtonClick(Date date) {

            }
        });


    }

    public void getUserData(){

        dateEnd = new Date(prefs.getLong(USER_DATE, 0));
        cigPerDay = prefs.getFloat(USER_CIG_PER_DAY,0);
        cigInPack = prefs.getFloat(USER_CIG_IN_PACK,0);
        pricePack = prefs.getFloat(USER_PRICE_PACK,0);
        yearsSmok = prefs.getFloat(USER_YEARS_SMOK,0);
        currency = prefs.getString(USER_CURRENSY,"");
        userPoints = prefs.getFloat(USER_POINT,0);
        makeUI();



    }

    public void makeUI(){

        String datez = DateFormat.format("yyyy.MM.dd HH:mm:ss", dateEnd).toString();
        System.out.println(datez);
        userDataEnds.setText(getResources().getString(R.string.date_end)+ " " + datez);
        pickerCigPerDay.setPickerValue(cigPerDay);
        pickerCigInPack.setPickerValue(cigInPack);
        pickerPrice.setPickerValue(pricePack);
        pickerYears.setPickerValue(yearsSmok);
        currencyLabel.setText(currency);
        makeUIS();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.user_data_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_name_save:

                new ToastOXDialog.Build(this)
                        .setTitle(R.string.change_user_datas_allert_title)
                        .setContent(R.string.change_user_data_message)
                        .setPositiveText(R.string.positive_answer)
                        .setPositiveBackgroundColorResource(R.color.positive_color)
                        .setPositiveTextColorResource(R.color.text_all_color)
                        .onPositive(new ToastOXDialog.ButtonCallback() {
                            @Override
                            public void onClick(@NonNull ToastOXDialog toastOXDialog) {


                                if (userPoints >= 9){

                                    userPoints = userPoints - 9;

                                    prefs.edit().putLong(USER_DATE, dateEnd.getTime()).apply();
                                    prefs.edit().putFloat(USER_CIG_PER_DAY,cigPerDay).apply();
                                    prefs.edit().putFloat(USER_CIG_IN_PACK,cigInPack).apply();
                                    prefs.edit().putFloat(USER_YEARS_SMOK,yearsSmok).apply();
                                    prefs.edit().putFloat(USER_PRICE_PACK,pricePack).apply();
                                    prefs.edit().putString(USER_CURRENSY,currency).apply();
                                    prefs.edit().putFloat(USER_POINT,userPoints).apply();
                                    getUserData();

                                    Intent startIntent = new Intent(UserDataActivity.this, MainActivity.class);
                                    startActivity(startIntent);
                                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

                                } else {


                                    new LovelyStandardDialog(UserDataActivity.this)
                                            .setTopColorRes(R.color.colorPrimary)
                                            .setButtonsColorRes(R.color.positive_color)
                                            .setIcon(R.drawable.sad)
                                            .setTitle(R.string.title_warning_point)
                                            .setMessage(R.string.message_warning_points)
                                            .setPositiveButton(android.R.string.ok, new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {

                                                    Intent startIntent = new Intent(UserDataActivity.this, BuyPointsActivity.class);
                                                    startActivity(startIntent);
                                                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);


                                                }
                                            })
                                            .setNegativeButton(android.R.string.no, null)
                                            .show();

                                }


                            }
                        })
                        .setNegativeText(R.string.negative_answer)
                        .setNegativeBackgroundColorResource(R.color.negative_color)
                        .setNegativeTextColorResource(R.color.text_all_color)
                        .onNegative(new ToastOXDialog.ButtonCallback(){
                            @Override
                            public void onClick(@NonNull ToastOXDialog toastOXDialog) {


                            }
                        }).show();


                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void changeDataEnd (View view){

        dateTimeFragment.show(getSupportFragmentManager(), TAG_DATETIME_FRAGMENT);


    }

    public void changeCurrency(View view){

        new PanterDialog(this)
                .setHeaderBackground(R.drawable.pattern_bg_blue)
                .setHeaderLogo(R.drawable.icon_app)
                .setDialogType(DialogType.SINGLECHOICE)
                .isCancelable(false)
                .items(R.array.sample_array, new OnSingleCallbackConfirmListener() {
                    @Override
                    public void onSingleCallbackConfirmed(PanterDialog dialog, int pos, String text) {

                        currency = text;
                        currencyLabel.setText(text);

                    }
                })
                .show();


    }

    @Override
    protected void onResume() {

        super.onResume();
        makeUIS();

    }
}
