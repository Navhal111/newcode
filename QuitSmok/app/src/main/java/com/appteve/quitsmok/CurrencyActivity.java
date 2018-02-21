package com.appteve.quitsmok;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.eminayar.panter.DialogType;
import com.eminayar.panter.PanterDialog;
import com.eminayar.panter.interfaces.OnSingleCallbackConfirmListener;

public class CurrencyActivity extends AppCompatActivity implements AppData {

    Button chooseCurrency;
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currency);

        prefs = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);

        chooseCurrency = (Button) findViewById(R.id.currencyBtn);
        chooseCurrency.setText(R.string.currency_btn);

        chooseCurrency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                choose();

            }
        });

    }

    void choose(){

        new PanterDialog(this)
                .setHeaderBackground(R.drawable.pattern_bg_blue)
                .setHeaderLogo(R.drawable.icon_app)
                .setDialogType(DialogType.SINGLECHOICE)
                .isCancelable(false)
                .items(R.array.sample_array, new OnSingleCallbackConfirmListener() {
                    @Override
                    public void onSingleCallbackConfirmed(PanterDialog dialog, int pos, String text) {


                        prefs.edit().putString(USER_CURRENSY,text).apply();
                        prefs.edit().putFloat(USER_POINT,12).apply();
                        prefs.edit().putBoolean(AD_REMOVE, false).apply();
                        Intent startIntent = new Intent(CurrencyActivity.this, MainActivity.class);
                        startActivity(startIntent);
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    }
                })
                .show();


    }
}
