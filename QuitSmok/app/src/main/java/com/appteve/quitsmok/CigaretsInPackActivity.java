package com.appteve.quitsmok;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import pl.polak.clicknumberpicker.ClickNumberPickerListener;
import pl.polak.clicknumberpicker.ClickNumberPickerView;
import pl.polak.clicknumberpicker.PickerClickType;


public class CigaretsInPackActivity extends AppCompatActivity implements AppData {


    TextView headerText;
    Button nextBtn ;
    SharedPreferences prefs;
    float cigcount;
    ClickNumberPickerView picker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cigarets_in_pack);
        cigcount = 0;

        prefs = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);


        headerText = (TextView) findViewById(R.id.headerText);

        headerText.setText(R.string.header_cig_in_pack);
        nextBtn = (Button) findViewById(R.id.nextBtn);
        nextBtn.setText(R.string.next);

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                prefs.edit().putFloat(USER_CIG_IN_PACK,cigcount).apply();
                Intent startIntent = new Intent(CigaretsInPackActivity.this, YearsSmokActivity.class);
                startActivity(startIntent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);


            }
        });

        picker = (ClickNumberPickerView) findViewById(R.id.clickpiker) ;

        picker.setClickNumberPickerListener(new ClickNumberPickerListener() {
            @Override
            public void onValueChange(float previousValue, float currentValue, PickerClickType pickerClickType) {

                cigcount = currentValue;

            }
        });

    }
}

