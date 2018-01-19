package pistalix.echoeffect.photoeditor.Activity;

import android.annotation.TargetApi;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import pistalix.echoeffect.photoeditor.Adpter.AppRecycleList;
import pistalix.echoeffect.photoeditor.BackButton;
import pistalix.echoeffect.photoeditor.R;
import pistalix.echoeffect.photoeditor.View.Glob;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int MY_REQUEST_CODE = 1;
    private static final int MY_REQUEST_CODE1 = 3;
    private static final int MY_REQUEST_CODE2 = 4;
    private static final int RE_GALLERY = 2;
    private LinearLayout llstart, llalbum;
    private AdView adview;
    RecyclerView applistrecycle;
    public static ArrayList<String> mResults = new ArrayList<>();

    private boolean isAlreadyCall = false;
    private boolean dataAvailable = false;
    private LinearLayout seemoreapp;
    private Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mResults.clear();
        seemoreapp = (LinearLayout) findViewById(R.id.seemoreapp);
        seemoreapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isOnline()) {
                    moreapp();
                } else {
                    Toast.makeText(MainActivity.this, "No Internet Connection..", Toast.LENGTH_SHORT).show();
                }
            }
        });
        adview = (AdView)findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adview.loadAd(adRequest);
        applistrecycle= (RecyclerView)findViewById(R.id.recyclerView);
        applistrecycle.setLayoutManager(new GridLayoutManager(this,3));
        llstart = (LinearLayout) findViewById(R.id.llstart);
        llstart.setOnClickListener(this);
        llalbum = (LinearLayout) findViewById(R.id.llalbum);
        llalbum.setOnClickListener(this);
        SetAppList();
    }

    private  void SetAppList(){
        String url = getApplication().getString(R.string.applist);
        RequestQueue request = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest= new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray responseArray = response.getJSONArray("applist");
                    JSONArray Setter = new JSONArray();
                    int i =0;
                    while(i<responseArray.length()){
                        if(!responseArray.getJSONObject(i).get("packeg").equals("pistalix.echoeffect.photoeditor")){
                            Setter.put(responseArray.get(i));
                        }
                        i++;
                    }
                    AppRecycleList ListAdapter = new AppRecycleList(Setter);
                    applistrecycle.setAdapter(ListAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        request.add(jsonObjectRequest);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.rate:
                gotoStore();
                break;
            case R.id.share:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                            == PackageManager.PERMISSION_GRANTED) {

                        share();
                    } else if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {


                        requestPermissions(new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                                MY_REQUEST_CODE2);
                    }
                } else {
                    share();
                }
                break;
            case R.id.more:
                if (isOnline()) {
                    moreapp();
                } else {
                    Toast.makeText(MainActivity.this, "No Internet Connection..", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.privacy_policy:
                if (isOnline()) {
                    startActivity(new Intent(getApplicationContext(), WebviewActivity.class));
                } else {
                    Toast.makeText(MainActivity.this, "No Internet Connection..", Toast.LENGTH_SHORT).show();
                }

                break;
        }
        return true;
    }

    public void gotoStore() {
        Uri uri = Uri.parse(Glob.app_link);
        Intent myAppLinkToMarket = new Intent(Intent.ACTION_VIEW, uri);
        myAppLinkToMarket.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        try {
            startActivity(myAppLinkToMarket);
        } catch (ActivityNotFoundException e) {
            //the device hasn't installed Google Play
            Toast.makeText(MainActivity.this, "You don't have Google Play installed", Toast.LENGTH_LONG).show();
        }
    }

    private void share() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("image/*");
        shareIntent.putExtra(Intent.EXTRA_TEXT, Glob.app_name + " Created By :" + Glob.app_link);
        shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(),
                BitmapFactory.decodeResource(getResources(), R.drawable.banner), null, null)));
        startActivity(Intent.createChooser(shareIntent, "Share Image using"));
    }

    private void moreapp() {
        Uri uri = Uri.parse(Glob.acc_link);
        Intent myAppLinkToMarket = new Intent(Intent.ACTION_VIEW, uri);

        try {
            startActivity(myAppLinkToMarket);

        } catch (ActivityNotFoundException e) {

            //the device hasn't installed Google Play
            Toast.makeText(MainActivity.this, "You don't have Google Play installed", Toast.LENGTH_LONG).show();
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.llstart:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                            == PackageManager.PERMISSION_GRANTED) {

                        openGallery();
                    } else if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {


                        requestPermissions(new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                                MY_REQUEST_CODE);
                    }
                } else {
                    openGallery();
                }
                break;
            case R.id.llalbum:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                            == PackageManager.PERMISSION_GRANTED) {

                        cration();
                    } else if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {


                        requestPermissions(new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                                MY_REQUEST_CODE1);
                    }
                } else {
                    cration();
                }
                break;
        }
    }

    private void cration() {
        startActivity(new Intent(MainActivity.this, MyCreation.class));
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_REQUEST_CODE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openGallery();
                } else {
                    if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {

                        requestPermissions(new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                                MY_REQUEST_CODE);
                    }
                }
                break;
            case MY_REQUEST_CODE1:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    cration();
                } else {
                    if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {

                        requestPermissions(new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                                MY_REQUEST_CODE1);
                    }
                }
                break;
            case MY_REQUEST_CODE2:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    share();
                } else {
                    if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {

                        requestPermissions(new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                                MY_REQUEST_CODE2);
                    }
                }
                break;
        }
    }

    private void openGallery() {
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, RE_GALLERY);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == RE_GALLERY) {
                uri = data.getData();
                Intent in = new Intent(this, CropActivity.class);
                in.putExtra("image_Uri", this.uri.toString());
                startActivity(in);

            }
        }
    }

    Bitmap resizeBitmap(Bitmap bit) {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        float wr = (float) dm.widthPixels;
        float hr = (float) dm.heightPixels;
        float wd = (float) bit.getWidth();
        float he = (float) bit.getHeight();
        float rat1 = wd / he;
        float rat2 = he / wd;
        if (wd > wr) {
            wd = wr;
            he = wd * rat2;
        } else if (he > hr) {
            he = hr;
            wd = he * rat1;
        } else if (rat1 > 0.75f) {
            wd = wr;
            he = wd * rat2;
        } else if (rat2 > 1.5f) {
            he = hr;
            wd = he * rat1;
        } else {
            wd = wr;
            he = wd * rat2;
        }
        return Bitmap.createScaledBitmap(bit, (int) wd, (int) he, false);
    }

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, BackButton.class));
    }



}