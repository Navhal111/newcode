package pistalix.dslrcamera.dslrcamerahd;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import java.io.File;

public class SaveShare extends AppCompatActivity implements View.OnClickListener {
    Uri uri;
    private TextView tvTitle;
    private ImageView ivBack;
    private String _url;
    private Bitmap bitmap;
    private ImageView iv_home;
    private boolean isAlreadySave = false;
    private ImageView ivPopUp;
    private ImageView iv_Final_Image;
    private ImageView ivFacebook, ivWhatsapp, ivInstagram, ivShareMore, ivHike, ivTwitter;

    private ImageView ivBanner;
    private int index = 0;
    private Uri uriBanner;
    private String banner = null;
    private TextView ttlink;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_share);

        bindView();

    }

    private void bindView() {
        ivBanner = (ImageView) findViewById(R.id.iv_Show_Image);
        ivBack = (ImageView) findViewById(R.id.iv_Back_Save);
        ivBack.setOnClickListener(this);

        iv_home = (ImageView) findViewById(R.id.iv_Home);
        iv_home.setOnClickListener(this);

        ivInstagram = (ImageView) findViewById(R.id.iv_instagram);
        ivInstagram.setOnClickListener(this);
        ivWhatsapp = (ImageView) findViewById(R.id.iv_whatsapp);
        ivWhatsapp.setOnClickListener(this);
        ivFacebook = (ImageView) findViewById(R.id.iv_facebook);
        ivFacebook.setOnClickListener(this);
        ivShareMore = (ImageView) findViewById(R.id.iv_Share_More);
        ivShareMore.setOnClickListener(this);
        ivHike = (ImageView) findViewById(R.id.iv_Hike);
        ivHike.setOnClickListener(this);
        ttlink = (TextView) findViewById(R.id.ttlink);
        ttlink.setText(ImageEditActivity._url);
//        ivMain = (ImageView) findViewById(R.id.iv_holder);
        SetImageView();
    }

    void SetImageView() {

        iv_Final_Image = (ImageView) findViewById(R.id.iv_Show_Image);
        bitmap = ImageEditActivity.finalEditedBitmapImage;
        iv_Final_Image.setImageBitmap(bitmap);
        iv_Final_Image.setScaleType(ImageView.ScaleType.FIT_XY);
    }

    @Override
    public void onClick(View v) {

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("image/*");
        shareIntent.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=pistalix.dslrcamera.dslrcamerahd&hl=en");
        shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(ImageEditActivity._url)));

        switch (v.getId())

        {
            case R.id.iv_Back_Save:

                finish();
                break;

            case R.id.iv_Home:
                Intent intent = new Intent(getApplicationContext(), MyCreation.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.putExtra("ToHome", true);
                startActivity(intent);
                finish();

                break;

            case R.id.iv_instagram:
                try {
                    shareIntent.setPackage("com.instagram.android");
                    startActivity(shareIntent);
                } catch (Exception e) {
                    Toast.makeText(this, "Instagram doesn't installed", Toast.LENGTH_LONG).show();
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
            case R.id.iv_whatsapp:
                try {
                    shareIntent.setPackage("com.whatsapp");
                    startActivity(shareIntent);
                } catch (Exception e) {
                    Toast.makeText(this, "WhatsApp doesn't installed", Toast.LENGTH_LONG).show();
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
//
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("image/*");
                sharingIntent.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=pistalix.dslrcamera.dslrcamerahd&hl=en");
                sharingIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(ImageEditActivity._url)));
                startActivity(Intent.createChooser(sharingIntent, "Share Image using"));
                break;
        }
    }


    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }


    @Override
    public void onBackPressed() {

        finish();


    }


}