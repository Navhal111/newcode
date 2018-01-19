package pistalix.echoeffect.photoeditor.Activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.NativeExpressAdView;

import java.io.File;
import java.util.ArrayList;

import pistalix.echoeffect.photoeditor.Adpter.Galleryadapter;
import pistalix.echoeffect.photoeditor.R;
import pistalix.echoeffect.photoeditor.View.Glob;


/**
 * Created by PC1 on 11/14/2016.
 */
public class MyCreation extends AppCompatActivity {
    public static int pos;
    private ListView lv;
    private ImageView fback;
    private Galleryadapter galleryAdapter;
    public static ArrayList<String> IMAGEALLARY = new ArrayList<>();
    private Typeface tf;
    private TextView tt_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_creation);
        showadmobnative();
        lv = (ListView) findViewById(R.id.gridview);
        fback = (ImageView) findViewById(R.id.fback);
        galleryAdapter = new Galleryadapter(MyCreation.this, IMAGEALLARY);
        IMAGEALLARY.clear();
        listAllImages(new File(Environment.getExternalStorageDirectory().getPath() + "/" + Glob.Edit_Folder_name + "/"));
        lv.setAdapter(galleryAdapter);

        fback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();


            }
        });
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                galleryAdapter.getItemId(position);
                pos = position;
                Dialog dialog = new Dialog(MyCreation.this, 16973839);
                DisplayMetrics displayMetrics = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                int i = (int) (((double) displayMetrics.heightPixels) * 1.0d);
                int i2 = (int) (((double) displayMetrics.widthPixels) * 1.0d);
                dialog.requestWindowFeature(1);
                dialog.getWindow().setFlags(1024, 1024);
                dialog.setContentView(R.layout.activity_full_screen_view);
                dialog.getWindow().setLayout(i2, i);
                dialog.setCanceledOnTouchOutside(true);
                ((ImageView) dialog.findViewById(R.id.iv_image)).setImageURI(Uri.parse(MyCreation.IMAGEALLARY.get(MyCreation.pos)));
                dialog.show();

            }
        });

    }


    private void listAllImages(File filepath) {
        File[] files = filepath.listFiles();
        if (files != null) {

            for (int j = files.length - 1; j >= 0; j--) {
                String ss = files[j].toString();
                File check = new File(ss);
                Log.d("" + check.length(), "" + check.length());
                if (check.length() > 1024) {
                    if (check.toString().contains(".jpg") || check.toString().contains(".png") || check.toString().contains(".jpeg")) {
                        IMAGEALLARY.add(ss);
                    }
                } else {
                    Log.e("Invalid Image", "Delete Image");
                }
                System.out.println(ss);
            }
        } else
        {
            System.out.println("Empty Folder");
        }
    }


    @Override
    public void onBackPressed() {

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();

    }

    private void showadmobnative() {
        NativeExpressAdView adView = (NativeExpressAdView) findViewById(R.id.adView);
        if (isOnline()) {
            adView.setVisibility(View.VISIBLE);
            AdRequest request = new AdRequest.Builder().build();
            adView.loadAd(request);
        } else {
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



