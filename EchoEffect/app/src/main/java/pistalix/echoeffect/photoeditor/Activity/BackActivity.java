package pistalix.echoeffect.photoeditor.Activity;

import android.annotation.TargetApi;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import pistalix.echoeffect.photoeditor.R;
import pistalix.echoeffect.photoeditor.View.Glob;



public class BackActivity extends AppCompatActivity {
    GridView app_list;

    TextView yes, no;
    private int totalHours;
    private boolean isAlreadyCall = false;
    private TextView btnRate;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_back);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        if (getIntent().hasExtra("fromNotification")) {
            findViewById(R.id.llConfirmExit).setVisibility(View.GONE);
        }
        yes = (TextView) findViewById(R.id.btnYes);
        no = (TextView) findViewById(R.id.btnNo);
        btnRate = (TextView) findViewById(R.id.btnRate);
        btnRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse(Glob.app_link);
                Intent myAppLinkToMarket = new Intent(Intent.ACTION_VIEW, uri);
                myAppLinkToMarket.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                try {
                    startActivity(myAppLinkToMarket);
                } catch (ActivityNotFoundException e) {
                    //the device hasn't installed Google Play
                    Toast.makeText(BackActivity.this, "You don't have Google Play installed", Toast.LENGTH_LONG).show();
                }
            }
        });
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(BackActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });
        app_list = (GridView) findViewById(R.id.grid_More_Apps);

    }




}
