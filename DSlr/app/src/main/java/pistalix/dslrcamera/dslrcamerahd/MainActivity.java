package pistalix.dslrcamera.dslrcamerahd;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
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

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int RE_CAMERA = 2;
    private static final int MY_REQUEST_CODE = 3;
    private static final int RE_GALLERY = 4;
    private static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    private static final int MY_REQUEST_CODE2 = 6;
    public static Uri imageUri;
    private static final String TAG = "MainActivity";
    private ImageView iv_glry, iv_Camera, my_creation;
    RecyclerView applistrecycle;
    private Uri destiuri;
    private AdView adview;
    private static final int REQUEST_CROP = 69;
    private static final int RESULT_ERROR = 96;
    private long diffMills = 0;
    private int totalHours = 0;
    public static ArrayList<String> listIcon = new ArrayList<String>();
    public static ArrayList<String> listName = new ArrayList<String>();
    public static ArrayList<String> listUrl = new ArrayList<String>();
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private LinearLayout nativeAdContainer;

    private LinearLayout adView;
    private com.google.android.gms.ads.InterstitialAd mInterstitialAdMob;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mInterstitialAdMob = showAdmobFullAd();
        loadAdmobAd();

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        try{
            String msg = getIntent().getStringExtra("update");
            if(msg.equals("1")){

                String mag1 = getIntent().getStringExtra("msg");
                MainFunction obj = new MainFunction();
                obj.update_app(mag1,this);
            }
        } catch (NullPointerException e){
            e.printStackTrace();
        }catch (IllegalArgumentException e){
            e.printStackTrace();
        }
        catch (Exception e){
            e.printStackTrace();
        }

        Utility.listIcon.clear();
        Utility.listName.clear();
        Utility.listUrl.clear();


        Bind();
        if(isOnline()){
            SetAppList();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);//Menu Resource, Menu
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.rate_us:
                Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=pistalix.dslrcamera.dslrcamerahd");
                Intent myAppLinkToMarket = new Intent(Intent.ACTION_VIEW, uri);
                try {
                    startActivity(myAppLinkToMarket);
                } catch (ActivityNotFoundException e) {
                    //the device hasn't installed Google Play
                    Toast.makeText(MainActivity.this, "You don't have Google Play installed", Toast.LENGTH_LONG).show();
                }
                return true;
            case R.id.share:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                            == PackageManager.PERMISSION_GRANTED) {
                        Shareapp();
                    } else if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {


                        requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                MY_REQUEST_CODE2);
                    }
                } else {
                    Shareapp();
                }

                return true;
            case R.id.more:
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://play.google.com/store/apps/developer?id=Pistalix+Software+Solutions"));
                startActivity(intent);

                return true;
            case R.id.privicy_police:
                startActivity(new Intent(MainActivity.this, WebviewActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void Shareapp() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("image/*");
        shareIntent.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=pistalix.dslrcamera.dslrcamerahd&hl=en");
        shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(),
                BitmapFactory.decodeResource(getResources(), R.drawable.banner), null, null)));
        startActivity(Intent.createChooser(shareIntent, "Share Image using"));
    }

    private void Bind() {
        iv_glry = (ImageView) findViewById(R.id.iv_glry);
        iv_glry.setOnClickListener(this);
        iv_Camera = (ImageView) findViewById(R.id.iv_Camera);
        iv_Camera.setOnClickListener(this);
        my_creation = (ImageView) findViewById(R.id.btn_my_creation);
        my_creation.setOnClickListener(this);
        applistrecycle= (RecyclerView)findViewById(R.id.recyclerView);
        applistrecycle.setLayoutManager(new GridLayoutManager(this,3));
        adview = (AdView)findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adview.loadAd(adRequest);

    }


    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
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
                        if(!responseArray.getJSONObject(i).get("packeg").equals("pistalix.dslrcamera.dslrcamerahd")){
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_Camera:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkAndRequestPermissions()) {

                        opencamera();
                    }
                } else {
                    opencamera();

                }
                break;
            case R.id.iv_glry:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                            == PackageManager.PERMISSION_GRANTED) {
                        openGallery();

                    } else if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {


                        requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                MY_REQUEST_CODE);
                    }
                } else {
                    openGallery();
                }

                break;
            case R.id.btn_my_creation:
                Intent i = new Intent(this, MyCreation.class);
                startActivity(i);
                break;
        }
    }


    private void openGallery() {
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, RE_GALLERY);

    }

    private void opencamera() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Intent intentCamera = new Intent("android.media.action.IMAGE_CAPTURE");
            File filePhoto = new File(Environment.getExternalStorageDirectory(), "DSLR_" + System.currentTimeMillis() + ".jpg");
            imageUri = FileProvider.getUriForFile(MainActivity.this,
                    BuildConfig.APPLICATION_ID + ".provider",
                    filePhoto);
            intentCamera.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            startActivityForResult(intentCamera, RE_CAMERA);
        }else{
            Intent intentCamera = new Intent("android.media.action.IMAGE_CAPTURE");
            File filePhoto = new File(Environment.getExternalStorageDirectory(), "DSLR_" + System.currentTimeMillis() + ".jpg");
            imageUri = Uri.fromFile(filePhoto);
            intentCamera.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            startActivityForResult(intentCamera, RE_CAMERA);
        }

    }

    private boolean checkAndRequestPermissions() {
        int permissionSendMessage = ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA);
        int locationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (locationPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (permissionSendMessage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
//            File filePhoto = new File(Environment.getExternalStorageDirectory(), "CropPic.jpg");
//            destiuri = Uri.fromFile(filePhoto);

            switch (requestCode) {
                case RE_CAMERA:
//                    UCrop.of(imageUri, destiuri)
//                            .start(MainActivity.this);
//                    imageUri = destiuri;

                    startActivity(new Intent(MainActivity.this, CropActivity.class));
                    break;
                case RE_GALLERY:
                    imageUri = data.getData();
//                    UCrop.of(imageUri, destiuri)
//                            .start(MainActivity.this);
//                    imageUri = destiuri;

                    startActivity(new Intent(MainActivity.this, CropActivity.class));

                    break;
//                case REQUEST_CROP:
//                    imageUri = UCrop.getOutput(data);
//                    startActivity(new Intent(getApplicationContext(), ImageDSLRActivity.class));
//                    break;
//
//                case RESULT_ERROR:
//                    Throwable cropError = UCrop.getError(data);
//                    break;

            }
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        if (requestCode == MY_REQUEST_CODE) {
//            Log.d(TAG, "Permission callback called-------");
        switch (requestCode) {
            case REQUEST_ID_MULTIPLE_PERMISSIONS: {

                Map<String, Integer> perms = new HashMap<>();
                // Initialize the map with both permissions
                perms.put(Manifest.permission.CAMERA, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                // Fill with actual results from user
                if (grantResults.length > 0) {
                    for (int i = 0; i < permissions.length; i++)
                        perms.put(permissions[i], grantResults[i]);
                    // Check for both permissions
                    if (perms.get(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                            && perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        Log.d(TAG, "sms & location services permission granted");
                        opencamera();
                        // process the normal flow
                        //else any one or both the permissions are not granted
                    } else {
                        Log.d(TAG, "Some permissions are not granted ask again ");
                        //permission is denied (this is the first time, when "never ask again" is not checked) so ask again explaining the usage of permission
//                        // shouldShowRequestPermissionRationale will return true
                        //show the dialog or snackbar saying its necessary and try again otherwise proceed with setup.
                        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA) || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                            showDialogOK("SMS and Location Services Permission required for this app",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            switch (which) {
                                                case DialogInterface.BUTTON_POSITIVE:
                                                    checkAndRequestPermissions();
                                                    break;
                                                case DialogInterface.BUTTON_NEGATIVE:
                                                    // proceed with logic by disabling the related features or quit the app.
                                                    break;
                                            }
                                        }
                                    });
                        }
                        //permission is denied (and never ask again is  checked)
                        //shouldShowRequestPermissionRationale will return false
                        else {
                            Toast.makeText(this, "Go to settings and enable permissions", Toast.LENGTH_LONG)
                                    .show();
                            //proceed with logic by disabling the related features or quit the app.
                        }
                    }
                }
                break;
            }

            case MY_REQUEST_CODE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openGallery();
                } else {
                    if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {

                        requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                MY_REQUEST_CODE);
                    }
                }
                break;
            case MY_REQUEST_CODE2:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Shareapp();
                } else {
                    if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {

                        requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                MY_REQUEST_CODE2);
                    }
                }
                break;

        }
    }

    private void showDialogOK(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", okListener)
                .create()
                .show();
    }

    @Override
    public void onBackPressed() {
    
        startActivity(new Intent(this, BackButton.class));

    }


    private com.google.android.gms.ads.InterstitialAd showAdmobFullAd() {
        com.google.android.gms.ads.InterstitialAd interstitialAd = new com.google.android.gms.ads.InterstitialAd(this);
        interstitialAd.setAdUnitId(getString(R.string.interstitial_full_screen));
        interstitialAd.setAdListener(new com.google.android.gms.ads.AdListener() {
            @Override
            public void onAdClosed() {
                loadAdmobAd();
            }

            @Override
            public void onAdLoaded() {


            }

            @Override
            public void onAdOpened() {
                //   super.onAdOpened();
            }
        });
        return interstitialAd;
    }

    private void loadAdmobAd() {
        this.mInterstitialAdMob.loadAd(new AdRequest.Builder().build());
    }






    //FB InterstitialAds En



}
