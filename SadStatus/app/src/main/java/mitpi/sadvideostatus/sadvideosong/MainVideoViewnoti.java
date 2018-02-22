package mitpi.sadvideostatus.sadvideosong;


import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.SparseArray;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.numberprogressbar.NumberProgressBar;
import com.github.johnpersano.supertoasts.library.Style;
import com.github.johnpersano.supertoasts.library.SuperActivityToast;
import com.github.johnpersano.supertoasts.library.utils.PaletteUtils;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import at.huber.youtubeExtractor.YtFile;

public class MainVideoViewnoti extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {
    String Title, VideoId, videoUrl,playlistId;
    TextView Test,VideoTitle;
    ImageView share_image,download_image,whatsapp,instagram,hike,fb,fbmsg,main_share,whatsapp_share_mp3;
    private AdView mAdView;
    NumberProgressBar DownloadBAr;
    int j = 0, set = 0,flagDownload=0,flagDownloadmp3=0;
    private static final int ITAG_FOR_AUDIO = 140;
    Boolean Downloding = true;
    String filename;
    private YouTubePlayerView youTubeView;
    private List<YtFragmentedVideo> formatsToShowList, MainSetVideos;
    private static final int RECOVERY_DIALOG_REQUEST = 1;

    String DEVELOPER_KEY = "AIzaSyDFaZ9yHK_TqYvAmNG9VGUZUinAwNlCyKs";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_video_view);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        share_image = findViewById(R.id.share_video);
        download_image =  findViewById(R.id.whatsapp_share);
        whatsapp_share_mp3 = findViewById(R.id.whatsapp_share_mp3);
        whatsapp =   findViewById(R.id.whatsapp);
        instagram =   findViewById(R.id.insta);
        hike =   findViewById(R.id.hike);
        fb =  findViewById(R.id.fb);
        fbmsg =   findViewById(R.id.fbmsg);
        main_share = findViewById(R.id.main_share);
        DownloadBAr = findViewById(R.id.DownloadBAr);
        youTubeView = (YouTubePlayerView) findViewById(R.id.youtubevideo);
        VideoTitle =  (TextView) findViewById(R.id.videotitle);
        try{
            String Title_create = getIntent().getStringExtra("Title");
            Title = getStringOfLettersOnly(Title_create);
            VideoId = getIntent().getStringExtra("videoid");

            mAdView = (AdView) findViewById(R.id.adView);
            AdRequest adRequest = new AdRequest.Builder()
                    .build();
            mAdView.loadAd(adRequest);
            videoUrl = "https://www.youtube.com/watch?v=" + VideoId;
            SingelFunction downlodvideocheck = new SingelFunction();
            JSONArray videoIds = downlodvideocheck.DounloadVideos();
            ArrayList<File> files= downlodvideocheck.DounloadVideosName();

            int j=0;
            while(j<videoIds.length()){
                JSONObject videoidcheck = (JSONObject) videoIds.get(j);
                if(videoidcheck.get("Id").equals(VideoId)){
                    flagDownload =1;
                    filename = files.get(j).getAbsolutePath();
                    download_image.setImageResource(R.drawable.downloaded);
                }
                j++;
            }
            if(!isNetworkAvailable()){
                ToastMsgFail("Check your Network Connection");
            }
            VideoTitle.setText(Title_create);
            youTubeView.initialize(DEVELOPER_KEY, this);

        }catch (NullPointerException e){
            ToastMsgFail("Sorry Problem On Server, Try Again After One Minute");
        }catch (IllegalArgumentException e){
            ToastMsgFail("Something Went Wrong");
        } catch (JSONException e) {
            ToastMsgFail("Something Went Wrong");
        }catch (Exception e){
            ToastMsgFail("Something Went Wrong");
        }
        download_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(flagDownload == 1){
                    ToastMsgSuc("Allredy Download");
                }else{
                    try{
                        ToastMsgSuc("Start Downloading ..");
                        DownloadBAr.setVisibility(View.VISIBLE);
                        Thread thread = new Thread(){
                            @Override
                            public void run() {
                                try {
                                    try {
                                        Thread.sleep(1000);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    JSONArray MainJson=getJson("https://www.zaasmart.com/ytd/getvideo.php?videoid="+VideoId+"&type=Download");
                                    JSONObject MainJsonObj = MainJson.getJSONObject(0);
                                    String filnename = Title+" $ "+VideoId+".mp4";
                                    downloadFromUrl(MainJsonObj.getString("url"),"Sad Status",filnename);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        };
                        thread.start();
                    }
                    catch (NullPointerException e){
                        ToastMsgFail("Sorry Problem On Server, Try Again After One Minute");
                    }catch (IllegalArgumentException e){

                        ToastMsgFail("Sorry Problem On Server, Try Again After One Minute");
                    }catch (Exception e){
                        ToastMsgFail("Sorry Problem On Server, Try Again After One Minute");
                    }
                }
            }
        });
        whatsapp_share_mp3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(flagDownloadmp3 == 1){
                    ToastMsgSuc("Allredy Download");
                }else{
                    try{
                        ToastMsgSuc("Start Downloading ..");
                        DownloadBAr.setVisibility(View.VISIBLE);
                        Thread thread = new Thread(){
                            @Override
                            public void run() {
                                try {
                                    try {
                                        Thread.sleep(3000);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    JSONArray MainJson=getJson("https://www.zaasmart.com/ytd/getvideo.php?videoid="+VideoId+"&type=Download");
                                    JSONObject MainJsonObj = MainJson.getJSONObject(1);
                                    String filnename = Title+" $ "+VideoId+".mp4";
                                    downloadFromUrl(MainJsonObj.getString("url"),"Sad Status",filnename);
                                    flagDownloadmp3=1;
                                } catch (Exception e) {
                                    ToastMsgFail("No Audio Found In this video");
                                    e.printStackTrace();
                                }
                            }
                        };
                        thread.start();
                    }
                    catch (NullPointerException e){
                        ToastMsgFail("Sorry Problem On Server, Try Again After One Minute");
                    }catch (IllegalArgumentException e){

                        ToastMsgFail("Sorry Problem On Server, Try Again After One Minute");
                    }catch (Exception e){
                        ToastMsgFail("Sorry Problem On Server, Try Again After One Minute");
                    }

                }
            }
        });
        share_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(flagDownload == 1){
                    //                        String string_path = ;
                    Intent sharingIntent =  new Intent(android.content.Intent.ACTION_SEND);
                    sharingIntent.setType("video/*");
                    sharingIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(filename));
                    try {
                        startActivity(Intent.createChooser(sharingIntent, "Share via"));
                    } catch (android.content.ActivityNotFoundException e) {
                        ToastMsgSuc("First Install App...");
                    }
                }else{
                    ToastMsgSuc("Start Downloading ..");
                    DownloadBAr.setVisibility(View.VISIBLE);
                    try{
                        Thread thread = new Thread(){
                            @Override
                            public void run() {
                                try {
                                    try {
                                        Thread.sleep(3000);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    JSONArray MainJson=getJson("https://www.zaasmart.com/ytd/getvideo.php?videoid="+VideoId+"&type=Download");
                                    JSONObject MainJsonObj = MainJson.getJSONObject(0);
                                    String filnename = Title+" $ "+VideoId+".mp4";
                                    boolean set1 = downloadFromUrl(MainJsonObj.getString("url"),"Sad Status",filnename);

                                    if(set1){
                                        Intent sharingIntent =  new Intent(android.content.Intent.ACTION_SEND);
                                        sharingIntent.setType("video/*");
                                        sharingIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(filename));
                                        try {
                                            startActivity(Intent.createChooser(sharingIntent, "Share via"));
                                        } catch (android.content.ActivityNotFoundException e) {
                                            ToastMsgSuc("First Install App...");
                                        }
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        };
                        thread.start();


                    }
                    catch (NullPointerException e){
                        ToastMsgFail("Sorry Problem On Server, Try Again After One Minute");
                    }catch (IllegalArgumentException e){
                        ToastMsgFail("Sorry Problem On Server, Try Again After One Minute");
                    }catch (Exception e){
                        ToastMsgFail("Sorry Problem On Server, Try Again After One Minute");
                    }

                }
            }
        });

        whatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean isAppInstalled = appInstalledOrNot("com.whatsapp");
                if(!isAppInstalled){
                    ToastInstallAppBottom();
                }else{
                    if(flagDownload == 1){
                        share_via_app("com.whatsapp");
                    }else{
                        try{
                            ToastMsgSuc("Start Downloading ..");
                            DownloadBAr.setVisibility(View.VISIBLE);
                            Thread thread = new Thread(){
                                @Override
                                public void run() {
                                    try {
                                        try {
                                            Thread.sleep(3000);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                        DownloadBAr.setVisibility(View.VISIBLE);
                                        JSONArray MainJson=getJson("https://www.zaasmart.com/ytd/getvideo.php?videoid="+VideoId+"&type=Download");
                                        JSONObject MainJsonObj = MainJson.getJSONObject(0);
                                        String filnename = Title+" $ "+VideoId+".mp4";
                                        boolean set1 = downloadFromUrl(MainJsonObj.getString("url"),"Sad Status",filnename);

                                        if(set1){
                                            share_via_app("com.whatsapp");
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            };
                            thread.start();
                        }
                        catch (NullPointerException e){
                            ToastMsgFail("Sorry Problem On Server, Try Again After One Minute");
                        }catch (IllegalArgumentException e){

                            ToastMsgFail("Sorry Problem On Server, Try Again After One Minute");
                        }catch (Exception e){
                            ToastMsgFail("Sorry Problem On Server, Try Again After One Minute");
                        }
                    }
                }

            }
        });
        instagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean isAppInstalled = appInstalledOrNot("com.instagram.android");
                if (!isAppInstalled) {
                    ToastInstallAppBottom();
                } else {
                    if (flagDownload == 1) {
                        share_via_app("com.instagram.android");
                    } else {

                        try{
                            ToastMsgSuc("Start Downloading ..");
                            DownloadBAr.setVisibility(View.VISIBLE);
                            Thread thread = new Thread(){
                                @Override
                                public void run() {
                                    try {
                                        try {
                                            Thread.sleep(3000);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                        DownloadBAr.setVisibility(View.VISIBLE);
                                        JSONArray MainJson=getJson("https://www.zaasmart.com/ytd/getvideo.php?videoid="+VideoId+"&type=Download");
                                        JSONObject MainJsonObj = MainJson.getJSONObject(0);
                                        String filnename = Title+" $ "+VideoId+".mp4";
                                        boolean set1 = downloadFromUrl(MainJsonObj.getString("url"),"Sad Status",filnename);

                                        if(set1){
                                            share_via_app("com.instagram.android");
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            };
                            thread.start();
                        }
                        catch (NullPointerException e){
                            ToastMsgFail("Sorry Problem On Server, Try Again After One Minute");
                        }catch (IllegalArgumentException e){

                            ToastMsgFail("Sorry Problem On Server, Try Again After One Minute");
                        }catch (Exception e){
                            ToastMsgFail("Sorry Problem On Server, Try Again After One Minute");
                        }
                    }
                }
            }

        });
        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean isAppInstalled = appInstalledOrNot("com.facebook.katana");
                if (!isAppInstalled) {
                    ToastInstallAppBottom();
                } else {
                    if (flagDownload == 1) {
                        share_via_app("com.facebook.katana");
                    } else {
                        try{
                            ToastMsgSuc("Start Downloading ..");
                            DownloadBAr.setVisibility(View.VISIBLE);
                            Thread thread = new Thread(){
                                @Override
                                public void run() {
                                    try {
                                        try {
                                            Thread.sleep(3000);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                        DownloadBAr.setVisibility(View.VISIBLE);
                                        JSONArray MainJson=getJson("https://www.zaasmart.com/ytd/getvideo.php?videoid="+VideoId+"&type=Download");
                                        JSONObject MainJsonObj = MainJson.getJSONObject(0);
                                        String filnename = Title+" $ "+VideoId+".mp4";
                                        boolean set1 = downloadFromUrl(MainJsonObj.getString("url"),"Sad Status",filnename);

                                        if(set1){
                                            share_via_app("com.facebook.katana");
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            };
                            thread.start();

                        }
                        catch (NullPointerException e){
                            ToastMsgFail("Sorry Problem On Server, Try Again After One Minute");
                        }catch (IllegalArgumentException e){

                            ToastMsgFail("Sorry Problem On Server, Try Again After One Minute");
                        }catch (Exception e){
                            ToastMsgFail("Sorry Problem On Server, Try Again After One Minute");
                        }
                    }
                }
            }

        });

        fbmsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean isAppInstalled = appInstalledOrNot("com.facebook.orca");
                if (!isAppInstalled) {
                    ToastInstallAppBottom();
                } else {
                    if (flagDownload == 1) {
                        share_via_app("com.facebook.orca");
                    } else {
                        try{
                            ToastMsgSuc("Start Downloading ..");
                            DownloadBAr.setVisibility(View.VISIBLE);
                            Thread thread = new Thread(){
                                @Override
                                public void run() {
                                    try {
                                        try {
                                            Thread.sleep(3000);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                        DownloadBAr.setVisibility(View.VISIBLE);
                                        JSONArray MainJson=getJson("https://www.zaasmart.com/ytd/getvideo.php?videoid="+VideoId+"&type=Download");
                                        JSONObject MainJsonObj = MainJson.getJSONObject(0);
                                        String filnename = Title+" $ "+VideoId+".mp4";
                                        boolean set1 = downloadFromUrl(MainJsonObj.getString("url"),"Sad Status",filnename);

                                        if(set1){
                                            share_via_app("com.facebook.katana");
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            };
                            thread.start();
                        }
                        catch (NullPointerException e){
                            ToastMsgFail("Sorry Problem On Server, Try Again After One Minute");
                        }catch (IllegalArgumentException e){

                            ToastMsgFail("Sorry Problem On Server, Try Again After One Minute");
                        }catch (Exception e){
                            ToastMsgFail("Sorry Problem On Server, Try Again After One Minute");
                        }
                    }
                }
            }

        });

        hike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean isAppInstalled = appInstalledOrNot("com.bsb.hike");
                if (!isAppInstalled) {
                    ToastInstallAppBottom();
                } else {
                    if (flagDownload == 1) {
                        share_via_app("com.bsb.hike");
                    } else {
                        try {
                            ToastMsgSuc("Start Downloading ..");
                            DownloadBAr.setVisibility(View.VISIBLE);
                            Thread thread = new Thread(){
                                @Override
                                public void run() {
                                    try {
                                        try {
                                            Thread.sleep(3000);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                        DownloadBAr.setVisibility(View.VISIBLE);
                                        JSONArray MainJson=getJson("https://www.zaasmart.com/ytd/getvideo.php?videoid="+VideoId+"&type=Download");
                                        JSONObject MainJsonObj = MainJson.getJSONObject(0);
                                        String filnename = Title+" $ "+VideoId+".mp4";
                                        boolean set1 = downloadFromUrl(MainJsonObj.getString("url"),"Sad Status",filnename);

                                        if(set1){
                                            share_via_app("com.bsb.hike");
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            };
                            thread.start();

                        } catch (NullPointerException e) {
                            ToastMsgFail("Sorry Problem On Server, Try Again After One Minute");
                        } catch (IllegalArgumentException e) {
                            ToastMsgFail("Sorry Problem On Server, Try Again After One Minute");
                        } catch (Exception e) {
                            ToastMsgFail("Sorry Problem On Server, Try Again After One Minute");
                        }

                    }
                }
            }

        });

        main_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(flagDownload == 1){
                    //                        String string_path = ;
                    Intent sharingIntent =  new Intent(android.content.Intent.ACTION_SEND);
                    sharingIntent.setType("video/*");
                    sharingIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(filename));
                    try {
                        startActivity(Intent.createChooser(sharingIntent, "Share via"));
                    } catch (android.content.ActivityNotFoundException e) {
                        ToastMsgSuc("First Install App...");
                    }
                }else{

                    try{

                        ToastMsgSuc("Start Downloading ..");
                        DownloadBAr.setVisibility(View.VISIBLE);
                        Thread thread = new Thread(){
                            @Override
                            public void run() {
                                try {
                                    try {
                                        Thread.sleep(3000);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    DownloadBAr.setVisibility(View.VISIBLE);
                                    JSONArray MainJson=getJson("https://www.zaasmart.com/ytd/getvideo.php?videoid="+VideoId+"&type=Download");
                                    JSONObject MainJsonObj = MainJson.getJSONObject(0);
                                    String filnename = Title+" $ "+VideoId+".mp4";
                                    boolean set1 = downloadFromUrl(MainJsonObj.getString("url"),"Sad Status",filnename);

                                    if(set1){
                                        Intent sharingIntent =  new Intent(android.content.Intent.ACTION_SEND);
                                        sharingIntent.setType("video/*");
                                        sharingIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(filename));
                                        try {
                                            startActivity(sharingIntent);
                                        } catch (android.content.ActivityNotFoundException e) {
                                            ToastMsgSuc("First Install App...");
                                        }
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        };
                        thread.start();
                    }
                    catch (NullPointerException e){
                        ToastMsgFail("Sorry Problem On Server, Try Again After One Minute");
                    }catch (IllegalArgumentException e){

                        ToastMsgFail("Sorry Problem On Server, Try Again After One Minute");
                    }catch (Exception e){
                        ToastMsgFail("Sorry Problem On Server, Try Again After One Minute");
                    }

                }
            }
        });
//            try{
//                videoUrl = "https://www.youtube.com/watch?v=" + VideoId;
//                getYoutubeDownloadUrl(videoUrl);
//            }
//            catch (NullPointerException e){
//                ToastMsgFail("Sorry Problem On Server, Try Again After One Minute");
//            }catch (IllegalArgumentException e){
//                ToastMsgFail("Sorry Problem On Server, Try Again After One Minute");
//            }
    }

    //        private void getYoutubeDownloadUrl(String youtubeLink) {
//
//            YouTubeUriExtractor ytEx = new YouTubeUriExtractor(getApplicationContext()) {
//                @Override
//                public void onUrisAvailable(String videoId, String videoTitle, SparseArray<YtFile> ytFiles) {
//
//                    if (ytFiles == null) {
//                        ToastMsgFail("Sorry there is no any url to be download");
//                        try{
//                        }catch (NullPointerException e) {
//                            ToastMsgFail("Sorry Problem On Server, Try Again After One Minute");
//                        } catch (IllegalArgumentException ex) {
//                            ToastMsgFail("Sorry Problem On Server, Try Again After One Minute");
//                        } catch (Exception e) {
//                            ToastMsgFail("Sorry Problem On Server, Try Again After One Minute");
//                        }
//
//
//                    }
//                    formatsToShowList = new ArrayList<>();
//                    try {
//                        for (int i = 0, itag; i < ytFiles.size(); i++) {
//                            itag = ytFiles.keyAt(i);
//                            YtFile ytFile = ytFiles.get(itag);
//
//                            if (ytFile.getMeta().getHeight() == -1 || ytFile.getMeta().getHeight() >= 360) {
//                                addFormatToList(ytFile, ytFiles);
//                            }
//                        }
//                        Collections.sort(formatsToShowList, new Comparator<YtFragmentedVideo>() {
//                            @Override
//                            public int compare(YtFragmentedVideo lhs, YtFragmentedVideo rhs) {
//                                return lhs.height - rhs.height;
//                            }
//                        });
//                        MainSetVideos = new ArrayList<>();
//                        for (j = 0; j < formatsToShowList.size(); j++) {
//                            if (formatsToShowList.get(j).height <= 0){
//                                MainDownloadAudio = formatsToShowList.get(j);
//                            }
//                            if (formatsToShowList.get(j).height >= 240) {
//                                if(formatsToShowList.get(j).height==360){
//
//                                        MainDownloadFile = formatsToShowList.get(j);
//                                        set=1;
//                                }
//                                MainSetVideos.add(formatsToShowList.get(j));
//                            }
//                        }
//                        if(set ==0){
//                            for (j = 0; j < formatsToShowList.size(); j++) {
//                                if (formatsToShowList.get(j).height >= 240) {
//                                    if(formatsToShowList.get(j).height==240){
//
//                                        MainDownloadFile = formatsToShowList.get(j);
//                                        set=1;
//                                    }
//                                    MainSetVideos.add(formatsToShowList.get(j));
//                                }
//                            }
//                        }
//                    } catch (NullPointerException e) {
//                        ToastMsgFail("Sorry Problem On Server, Try Again After One Minute");
//                    } catch (IllegalArgumentException ex) {
//                        ToastMsgFail("Sorry Problem On Server, Try Again After One Minute");
//                    } catch (Exception e) {
//                        ToastMsgFail("Sorry Problem On Server, Try Again After One Minute");
//                    }
//
//                }
//            };
//
//            try {
//                ytEx.setIncludeWebM(false);
//                ytEx.setParseDashManifest(true);
//                ytEx.execute(youtubeLink);
//            }catch (NullPointerException e) {
//                ToastMsgFail("Sorry Problem On Server, Try Again After One Minute");
//            } catch (IllegalArgumentException ex) {
//                ToastMsgFail("Sorry Problem On Server, Try Again After One Minute");
//            } catch (Exception e) {
//                ToastMsgFail("Sorry Problem On Server, Try Again After One Minute");
//            }
//
//        }
    private void addFormatToList(YtFile ytFile, SparseArray<YtFile> ytFiles) {
        int height = ytFile.getMeta().getHeight();
        if (height != -1) {
            for (YtFragmentedVideo frVideo : formatsToShowList) {
                if (frVideo.height == height && (frVideo.videoFile == null ||
                        frVideo.videoFile.getMeta().getFps() == ytFile.getMeta().getFps())) {
                    return;
                }
            }
        }
        YtFragmentedVideo frVideo = new YtFragmentedVideo();
        frVideo.height = height;
        if (ytFile.getMeta().isDashContainer()) {
            if (height > 0) {
                frVideo.videoFile = ytFile;
                frVideo.audioFile = ytFiles.get(ITAG_FOR_AUDIO);
            } else {
                frVideo.audioFile = ytFile;
            }
        } else {
            frVideo.videoFile = ytFile;
        }
        formatsToShowList.add(frVideo);
    }

    void ToastMsgFail(String str) {
        SuperActivityToast.create(this).setText(str).setDuration(Style.DURATION_MEDIUM).setFrame(Style.FRAME_KITKAT).setColor(PaletteUtils.getSolidColor(PaletteUtils.MATERIAL_RED)).setAnimations(Style.ANIMATIONS_POP).show();
    }
    void ToastMsgSuc(String str) {
        SuperActivityToast.create(this).setText(str).setDuration(Style.DURATION_MEDIUM).setFrame(Style.FRAME_KITKAT).setColor(PaletteUtils.getSolidColor(PaletteUtils.MATERIAL_GREEN)).setAnimations(Style.ANIMATIONS_POP).show();
    }
    void ToastInstallAppBottom(){

        SuperActivityToast.create(this).setText("First Install App...").setDuration(Style.DURATION_MEDIUM).setFrame(Style.FRAME_LOLLIPOP).setColor(PaletteUtils.getSolidColor(PaletteUtils.MATERIAL_RED)).setAnimations(Style.ANIMATIONS_POP).show();
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
        if (!b) {

            // loadVideo() will auto play video
            youTubePlayer.setShowFullscreenButton(false);
            // Use cueVideo() method, if you don't want to play it automatically
            youTubePlayer.loadVideo(VideoId);
            // Hiding player controls
            youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.MINIMAL);
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
        if (youTubeInitializationResult.isUserRecoverableError()) {
            youTubeInitializationResult.getErrorDialog(this, RECOVERY_DIALOG_REQUEST).show();
        } else {
            ToastMsgFail("Error On video");
        }
    }

    private boolean downloadFromUrl(String youtubeDlUrl, String downloadTitle, String fileName) {
        Uri uri = Uri.parse(youtubeDlUrl);
        Long m_id;
        int persantage;
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setTitle(downloadTitle);

        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        File downloadfolder = new File(Environment.getExternalStorageDirectory(), "SadStatusNew");
        //        File downloadfolder = new File(Environment.getExternalStorageDirectory() +
        //                File.separator + "SadStatus");
        if (!downloadfolder.exists()){
            downloadfolder.mkdirs();
        }
        request.setDestinationInExternalPublicDir("SadStatusNew", fileName);

        DownloadManager manager = (DownloadManager)getApplication(). getSystemService(Context.DOWNLOAD_SERVICE);

        m_id= manager.enqueue(request);

//            if(query==null){
//            return false;
//            }
        try{
            while (Downloding){
                DownloadManager.Query query = new DownloadManager.Query();
                query.setFilterByStatus(DownloadManager.STATUS_FAILED | DownloadManager.STATUS_PAUSED |  DownloadManager.STATUS_SUCCESSFUL | DownloadManager.STATUS_RUNNING | DownloadManager.STATUS_PENDING);
                Cursor c;
                c=manager.query(query);
                if(c.moveToFirst()) {
                    int bytes_downloaded = c.getInt(c
                            .getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
                    int bytes_total = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
                    persantage = (bytes_downloaded*100)/bytes_total;

                    int status =c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS));
                    if (status==DownloadManager.STATUS_SUCCESSFUL) {
                        this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                download_image.setImageResource(R.drawable.downloaded);
                                DownloadBAr.setProgress(100);

                            }
                        });

                        filename =c.getString(c.getColumnIndex("local_uri"));
                        flagDownload =1;
                        Downloding = false;
                    }
                    if (status==DownloadManager.STATUS_FAILED) {
                        this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastMsgSuc("Fail");
                            }
                        });

                        Downloding = false;
                    }
                    if(status == DownloadManager.STATUS_RUNNING){
                        final int finalPersantage = persantage;
                        this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                DownloadBAr.setProgress(finalPersantage);
                            }
                        });

                    }
                }
                c.close();
            }

        }catch (IllegalArgumentException e){
            ToastMsgFail("Problem On download, Try Again After One Minute");
        }catch (NullPointerException e){
            ToastMsgFail("Problem On download, Try Again After One Minute");
        }catch (Exception e) {
            ToastMsgFail("Problem On download, Try Again After One Minute");
        }
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(getApplication());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong("Download_ID", m_id);
        editor.apply();
        return true;

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager)getApplication(). getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void share_via_app(String packaeg){
        try {
            Uri string_path = Uri.parse(filename);
            Intent sharingIntent =  new Intent(Intent.ACTION_SEND);
            sharingIntent.setType("video/*");
            sharingIntent.setPackage(packaeg);
            sharingIntent.putExtra(Intent.EXTRA_STREAM, string_path);
            startActivity(sharingIntent);
        } catch (android.content.ActivityNotFoundException e) {
            ToastInstallAppBottom();
        } catch (NullPointerException e){
            ToastMsgFail("Problem to read Your directory null ");
        }catch (Exception e){
            try{
                Uri string_path = Uri.fromFile(new File(filename));
                Intent sharingIntent =  new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("video/*");
                sharingIntent.setPackage(packaeg);
                sharingIntent.putExtra(Intent.EXTRA_STREAM, string_path);
                startActivity(sharingIntent);
            }catch (Exception e1){
                ToastMsgFail("Problem to read Your directory");
            }
//
        }
    }

    private boolean appInstalledOrNot(String uri) {
        PackageManager pm = getPackageManager();
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
        }

        return false;
    }

    public String getStringOfLettersOnly(String s) {
        //using a StringBuilder instead of concatenate Strings
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < s.length(); i++) {
            if (Character.isLetter(s.charAt(i)) || s.charAt(i) == ' ' || s.charAt(i)=='$') {
                //adding data into the StringBuilder
                sb.append(s.charAt(i));
            }
        }
        //return the String contained in the StringBuilder
        return sb.toString();
    }

    public static JSONArray getJson(String url){

        InputStream is = null;
        String result = "";
        JSONArray jsonObject = null;

        // HTTP
        try {
            HttpClient httpclient = new DefaultHttpClient(); // for port 80 requests!
            HttpPost httppost = new HttpPost(url);
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();
        } catch(Exception e) {
            return null;
        }

        // Read response to string
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is,"utf-8"),8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
            result = sb.toString();
        } catch(Exception e) {
            return null;
        }

        // Convert string to object
        try {
            jsonObject = new JSONArray(result);
        } catch(JSONException e) {
            return null;
        }

        return jsonObject;

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this,MainActivity.class));
    }
}
