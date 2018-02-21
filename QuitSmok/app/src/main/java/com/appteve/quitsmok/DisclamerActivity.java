package com.appteve.quitsmok;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class DisclamerActivity extends AppCompatActivity {

    TextView textDmr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disclamer);

        textDmr = (TextView) findViewById(R.id.disclamerText);
        textDmr.setText(R.string.disclamer_text);
    }
}
