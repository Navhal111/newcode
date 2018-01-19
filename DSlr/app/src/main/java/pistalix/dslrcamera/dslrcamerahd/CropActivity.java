package pistalix.dslrcamera.dslrcamerahd;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.theartofdev.edmodo.cropper.CropImageView;

public class CropActivity extends AppCompatActivity {
    private CropImageView cropImageView;
    ImageView ivSave, Iv_back_creation;
    public static Bitmap cropped;
    private int rorate = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop);

        showadmobnative();

        cropImageView = (CropImageView) findViewById(R.id.cropImageView);
        ivSave = (ImageView) findViewById(R.id.ivSave);
        Iv_back_creation = (ImageView) findViewById(R.id.Iv_back_creation);

        Iv_back_creation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ivSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cropped = cropImageView.getCroppedImage();
                startActivity(new Intent(CropActivity.this, ImageDSLRActivity.class));
            }
        });
        cropImageView.setImageUriAsync(MainActivity.imageUri);
        cropImageView.setFixedAspectRatio(true);


    }

    private void showadmobnative() {
        AdView adView = (AdView) findViewById(R.id.adView);
        if (isOnline()) {
            adView.setVisibility(View.VISIBLE);
            AdRequest request = new AdRequest.Builder().build();
            adView.loadAd(request);
        }
        else {
            adView.setVisibility(View.GONE);
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
}
