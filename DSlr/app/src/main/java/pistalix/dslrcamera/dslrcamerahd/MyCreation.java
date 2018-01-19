package pistalix.dslrcamera.dslrcamerahd;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.io.File;
import java.util.ArrayList;


public class MyCreation extends Activity {
    public static int pos;
    private ListView lv;
    private ImageView fback;
    private Galleryadapter galleryAdapter;
    public static ArrayList<String> IMAGEALLARY = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_creation);

        showadmobnative();


        lv = (ListView) findViewById(R.id.gridview);
        fback = (ImageView) findViewById(R.id.fback);
        galleryAdapter = new Galleryadapter(MyCreation.this, IMAGEALLARY);
        IMAGEALLARY.clear();
        listAllImages(new File(Environment.getExternalStorageDirectory().getPath() + "/" + Utility.Edit_Folder_name + "/"));
        lv.setAdapter(galleryAdapter);

        fback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);


            }
        });
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent fullscreen = new Intent(MyCreation.this, ImagefullscreenActivity.class);
                galleryAdapter.getItemId(position);
                pos = position;
                fullscreen.putExtra("position", position);
                startActivity(fullscreen);

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

    }

    private void showadmobnative() {
        AdView adView = (AdView) findViewById(R.id.adView);
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



