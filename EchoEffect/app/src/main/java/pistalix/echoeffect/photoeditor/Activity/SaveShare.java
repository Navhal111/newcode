package pistalix.echoeffect.photoeditor.Activity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

import pistalix.echoeffect.photoeditor.R;
import pistalix.echoeffect.photoeditor.View.Glob;


public class SaveShare extends AppCompatActivity implements View.OnClickListener {

    private ImageView ic_back;
    private ImageView finalimg;
    private TextView ic_path;
    private ImageView cration;
    private ImageView iv_whatsapp;
    private ImageView iv_hike;
    private ImageView iv_instagram;
    private ImageView iv_facebook;
    private ImageView iv_Share_More;
    private ArrayList<String> listIcon = new ArrayList<>();
    private ArrayList<String> listName = new ArrayList<>();
    private ArrayList<String> listUrl = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_save_share);
        listIcon.clear();
        listName.clear();
        listUrl.clear();
        bindview();
    }

    private void bindview() {

        finalimg = (ImageView) findViewById(R.id.finalimg);
        finalimg.setImageBitmap(Glob.finalBitmap);

        ic_path = (TextView) findViewById(R.id.ic_path);
        ic_path.setText(Glob.shareuri);

        cration = (ImageView) findViewById(R.id.cration);
        Animation animBlink = AnimationUtils.loadAnimation(SaveShare.this, R.anim.zoominout);
        cration.startAnimation(animBlink);
        cration.setOnClickListener(this);

        iv_whatsapp = (ImageView) findViewById(R.id.iv_whatsapp);
        iv_whatsapp.setOnClickListener(this);
        iv_hike = (ImageView) findViewById(R.id.iv_Hike);
        iv_hike.setOnClickListener(this);
        iv_instagram = (ImageView) findViewById(R.id.iv_instagram);
        iv_instagram.setOnClickListener(this);
        iv_facebook = (ImageView) findViewById(R.id.iv_facebook);
        iv_facebook.setOnClickListener(this);
        iv_Share_More = (ImageView) findViewById(R.id.iv_Share_More);
        iv_Share_More.setOnClickListener(this);


//        app


    }


    @Override
    public void onClick(View view) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("image/*");
        shareIntent.putExtra(Intent.EXTRA_TEXT, Glob.app_name + " Created By : " + Glob.app_link);
        shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(Glob.shareuri)));

        switch (view.getId()) {
            case R.id.cration:
                Intent intent = new Intent(SaveShare.this, MyCreation.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
                break;

            case R.id.iv_whatsapp:
                try {
                    shareIntent.setPackage("com.whatsapp");
                    startActivity(shareIntent);
                } catch (Exception e) {
                    Toast.makeText(this, "WhatsApp doesn't installed", Toast.LENGTH_LONG).show();
                }
                break;

            case R.id.iv_Hike:
                try {
                    shareIntent.setPackage("com.bsb.hike");
                    startActivity(shareIntent);
                } catch (Exception e) {
                    Toast.makeText(this, "Hike doesn't installed", Toast.LENGTH_LONG).show();
                }
                break;

            case R.id.iv_instagram:
                try {
                    shareIntent.setPackage("com.instagram.android");
                    startActivity(shareIntent);
                } catch (Exception e) {
                    Toast.makeText(this, "Instagram doesn't installed", Toast.LENGTH_LONG).show();
                }
                break;


            case R.id.iv_facebook:
                try {
                    shareIntent.setPackage("com.facebook.katana");
                    startActivity(shareIntent);
                } catch (Exception e) {
                    Toast.makeText(this, "Facebook doesn't installed", Toast.LENGTH_LONG).show();
                }
                break;

            case R.id.iv_Share_More:
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("image/*");
                sharingIntent.putExtra(Intent.EXTRA_TEXT, Glob.app_name + " Create By : " + Glob.app_link);
                sharingIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(Glob.shareuri)));
                startActivity(Intent.createChooser(sharingIntent, "Share Image using"));
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
    }

}
