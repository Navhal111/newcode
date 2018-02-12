package com.neurondigital.nudge;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.FragmentActivity;
import android.view.Display;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Logger;
import com.google.android.gms.analytics.Tracker;
import com.neurondigital.avoidthespikes.R;

import java.util.HashMap;

public class Screen extends FragmentActivity implements Runnable, SensorEventListener {
    private SurfaceHolder holder;
    private boolean locker = true, initialised = false;
    private Thread thread;
    //public WakeLock WL;
    private int width = 0, height = 0;
    public float cameraX = 0, cameraY = 0;

    public FragmentActivity activity = this;
    public boolean debug_mode = false;
    private long now = SystemClock.elapsedRealtime(), lastRefresh, lastfps;
    public SurfaceView surface;
    private int fps = 0, frames = 0, runtime = 0, drawtime = 0;

    //sensor
    SensorManager sm;
    Sensor s;
    float sensorx, calibratex = 0;
    float sensory, calibratey = 0;
    private boolean default_lanscape = false;
    private int default_lanscape_rotation = 0;

    //world origin
    public final int TOP_LEFT = 0, BOTTOM_LEFT = 1;
    public int origin = TOP_LEFT;

    //layout
    public RelativeLayout layout;
    public LinearLayout linear_layout;

    //gesture
    private GestureDetector gestureDetector;

    //canvas
    Canvas canvas;
    Bitmap screenshot;

    //ad
    private AdView adView;
    public String BANNER_AD_UNIT_ID;
    public InterstitialAd interstitial;
    public AdRequest adRequest;
    public boolean wait_dont_show_adbanner_now = false;

    public boolean ShowingAd = false;

    //recalculate screen dimensions
    int recalculateScreenCounter = 0;

    //TODO: Switches-must be modified from here to be enabled in App.
    private boolean Switch_GestureDetector = false;

    //Google analytics
    public enum TrackerName {
        APP_TRACKER, // Tracker used only in this app.
        GLOBAL_TRACKER, // Tracker used by all the apps from a company. eg: roll-up tracking.
        ECOMMERCE_TRACKER, // Tracker used by all ecommerce transactions from a company.
    }

    public Tracker tracker;
    protected HashMap<TrackerName, Tracker> mTrackers = new HashMap<TrackerName, Tracker>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activity = this;

        //full screen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        //create surface
        layout = new RelativeLayout(this);
        surface = new SurfaceView(this);
        layout.addView(surface);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        params.weight = 1;
        layout.setLayoutParams(params);

        //add relative layout to linear layout for banner ad mobility
        linear_layout = new LinearLayout(this);
        linear_layout.setOrientation(LinearLayout.VERTICAL);
        linear_layout.addView(layout);

        setContentView(linear_layout);
        holder = surface.getHolder();

        //listeners
        //surface.setOnTouchListener(this);
        if (Switch_GestureDetector)
            gestureDetector = new GestureDetector(this, new GestureListener());

        // start game loop
        thread = new Thread(this);
        thread.start();

        onCreate();

    }

    public synchronized Tracker initialiseAnalytics(String PROPERTY_ID) {
        if (!mTrackers.containsKey(TrackerName.APP_TRACKER)) {

            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            if (debug_mode)
                analytics.getLogger().setLogLevel(Logger.LogLevel.VERBOSE);
            analytics.setDryRun(false);
            tracker = analytics.newTracker(PROPERTY_ID);
            mTrackers.put(TrackerName.APP_TRACKER, tracker);

        }

        return mTrackers.get(TrackerName.APP_TRACKER);
    }

    public void AnalyticsView() {
        tracker.enableAutoActivityTracking(true);
        tracker.send(new HitBuilders.AppViewBuilder().build());
        GoogleAnalytics.getInstance(getActivity().getBaseContext()).dispatchLocalHits();
    }

    public void showBanner(final boolean inLayout) {
        //banner ad
        if (BANNER_AD_UNIT_ID.length() > 0) {
            // Create an ad.
            adView = new AdView(this);
            adView.setAdSize(AdSize.SMART_BANNER);
            adView.setAdUnitId(BANNER_AD_UNIT_ID);

            if (inLayout) {
                //make ad visible on bottom of screen over surface
                RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
                params1.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                params1.addRule(RelativeLayout.CENTER_HORIZONTAL);
                adView.setLayoutParams(params1);
            } else {
                //make ad visible on bottom of screen under surface
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
                params.gravity = Gravity.BOTTOM;
                params.weight = 0;
                adView.setLayoutParams(params);

            }

            // Create an ad request. Check logcat output for the hashed device ID to
            // get test ads on a physical device.
            final AdRequest adRequest = new AdRequest.Builder()
                    .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                    .addTestDevice("275D94C2B5B93B3C4014933E75F92565")///nexus7//////testing
                    .addTestDevice("91608B19766D984A3F929C31EC6AB947") /////////////////testing//////////////////remove///////////
                    .addTestDevice("6316D285813B01C56412DAF4D3D80B40") ///test htc sensesion xl
                    .addTestDevice("8C416F4CAF490509A1DA82E62168AE08")//asus transformer
                    .addTestDevice("EA8AA9C3AA2BD16A954F592C6F935628")//motorola moto G
                    .addTestDevice("7B4C6D080C02BA40EF746C4900BABAD7")//Galaxy S4
                    .build();

            // Start loading the ad in the background.
            adView.loadAd(adRequest);
            adView.setAdListener(new AdListener() {
                public void onAdLoaded() {
                    View parent = (View) adView.getParent();
                    if (parent != null) {
                        if (!(parent.equals(layout) || parent.equals(linear_layout))) {
                            if (inLayout)
                                layout.addView(adView);
                            else
                                linear_layout.addView(adView);
                            recalculateScreen();
                        }
                    } else {
                        //add new banner ad to screen
                        if (inLayout)
                            layout.addView(adView);
                        else
                            linear_layout.addView(adView);
                        recalculateScreen();
                    }
                }
            });
        }
    }

    public void openInterstitialAd() {
        if (getResources().getString(R.string.InterstitialAd_unit_id).length() > 0) {
            ShowingAd = true;
            runOnUiThread(new Runnable() {
                public void run() {

                    if (interstitial.isLoaded()) {
                        interstitial.show();

                    } else {
                        //proceed
                        interstitial.loadAd(adRequest);
                        ShowingAd = false;
                        //Toast.makeText(activity, "ad lost", Toast.LENGTH_LONG).show();
                    }

                }
            });
        }
    }

    public boolean initialiseInterstitialAd() {
        if (getResources().getString(R.string.InterstitialAd_unit_id).length() > 0) {
            // Create the interstitial
            interstitial = new InterstitialAd(this);
            interstitial.setAdUnitId(getResources().getString(R.string.InterstitialAd_unit_id));

            // Create ad request.
            adRequest = new AdRequest.Builder()
                    .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                    .addTestDevice("275D94C2B5B93B3C4014933E75F92565")///nexus7//////testing
                    .addTestDevice("91608B19766D984A3F929C31EC6AB947") /////////////////testing//////////////////remove///////////
                    .addTestDevice("6316D285813B01C56412DAF4D3D80B40") ///test htc sensesion xl
                    .addTestDevice("8C416F4CAF490509A1DA82E62168AE08")//asus transformer
                    .addTestDevice("7B4C6D080C02BA40EF746C4900BABAD7")//Galaxy S4
                    .build();
            if (!interstitial.isLoaded()) {
                interstitial.loadAd(adRequest);
            }
            interstitial.setAdListener(new AdListener() {
                public void onAdLoaded() {
                    //Toast.makeText(activity, "loaded ad", Toast.LENGTH_LONG).show();
                }

                public void onAdClosed() {
                    //Toast.makeText(activity, "ad closed", Toast.LENGTH_LONG).show();
                    interstitial.loadAd(adRequest);
                    ShowingAd = false;

                }

            });
            return true;
        }
        return false;
    }

    /* Main game loop.......................................................... */
    @Override
    public void run() {
        //int rand = (int) (Math.random() * 100);
        synchronized (ACCESSIBILITY_SERVICE) {

            while (locker) {
                //System.out.println("start-");

                now = SystemClock.elapsedRealtime();
                if (now - lastRefresh > 37) {//limit 35fps - 28
                    lastRefresh = SystemClock.elapsedRealtime();
                    if (!holder.getSurface().isValid()) {
                        continue;
                    }

                    //fps
                    if (now - lastfps > 1000) {
                        fps = frames;
                        frames = 0;
                        lastfps = SystemClock.elapsedRealtime();
                    } else {
                        frames++;
                    }

                    //step
                    if (initialised)
                        Step();
                    //take run time
                    runtime = (int) (SystemClock.elapsedRealtime() - lastRefresh);

                    //draw screen
                    canvas = holder.lockCanvas();
                    if (initialised)
                        Draw(canvas);
                    else {
                        //initialise game
                        width = canvas.getWidth();
                        height = canvas.getHeight();
                        Start();
                        initialised = true;
                    }
                    holder.unlockCanvasAndPost(canvas);
                    //take render time
                    drawtime = (int) (SystemClock.elapsedRealtime() - lastRefresh) - runtime;
                }
                //System.out.println("finish-----");
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /* Detect and override back press */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        super.onKeyDown(keyCode, event);
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            BackPressed();
            return false;
        }

        return false;
    }

    /* Events.................................................................. */
    public void onCreate() {

    }

    public void Start() {

    }

    synchronized public void Step() {
        //used to refresh screen after a delay to allow for changes to occur before grabbing the new height/width
        if (recalculateScreenCounter != -1) {
            if (recalculateScreenCounter > 0)
                recalculateScreenCounter--;
            else {
                if (!wait_dont_show_adbanner_now) {
                    initialised = false;
                    recalculateScreenCounter = -1;
                }
            }
        }

    }

    public void Draw(Canvas canvas) {
        if (debug_mode) {
            Paint paint = new Paint();
            paint.setColor(Color.BLACK);
            paint.setTextSize(dpToPx(20));
            canvas.drawText("Width: " + width + ", Height: " + height, 5, dpToPx(20), paint);
            canvas.drawText("default landscape: " + default_lanscape + " Rotation: " + default_lanscape_rotation, 5, 5 + dpToPx(20) * 2, paint);
            canvas.drawText("FPS: " + fps + "run_time: " + runtime + "draw_time: " + drawtime, 5, 5 + dpToPx(20) * 3, paint);
        }

    }

    public void Finish() {

    }

    public void Pause() {
        locker = false;

        while (true) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            break;
        }
        thread = null;
    }

    public void Resume() {
        locker = true;
        thread = new Thread(this);
        thread.start();
    }

    public synchronized void BackPressed() {

    }

    public synchronized void onTouch(float TouchX, float TouchY, MotionEvent event) {

    }

    public synchronized void onAccelerometer(PointF point) {
    }

    public void onSwipeLeft() {
    }

    public void onSwipeRight() {
    }

    public void onSwipeUp() {
    }

    public void onSwipeDown() {
    }

    /* Functions............................................................... */
    public void Exit() {
        locker = false;

        while (true) {
            try {
                if (thread != null)
                    thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            break;
        }
        thread = null;

        System.exit(0);
        activity.finish();
    }

    public Activity getActivity() {
        return activity;
    }

    public void setDebugMode(boolean debugModeOn) {
        debug_mode = debugModeOn;
    }

    //screen related
    public int ScreenWidth() {
        return width;
    }

    public int ScreenHeight() {
        return height;
    }

    /**
     * World X to Screen X
     *
     * @param worldX The x-coordinate relative to the world
     */
    public int ScreenX(float worldX) {
        return (int) (worldX - cameraX);
    }

    /**
     * World Y to Screen Y
     *
     * @param worldY The Y-coordinate relative to the world
     */
    public int ScreenY(float worldY) {
        if (origin == TOP_LEFT)
            return (int) (worldY - cameraY);
        else
            return ScreenHeight() - (int) (worldY - cameraY);
    }

    /**
     * World origin (0,0)
     *
     * @param origin TOP_LEFT or BOTTOM_LEFT
     */
    public void setOrigin(int origin) {
        this.origin = origin;
    }

    public boolean inScreen(float x, float y) {
        return ((ScreenY(y) > 0 && ScreenY(y) < ScreenHeight()) && (ScreenX(x) > 0 && ScreenX(x) < ScreenWidth()));
    }

    public int dpToPx(int dp) {
        float density = getApplicationContext().getResources().getDisplayMetrics().density;
        return Math.round((float) dp * density);
    }

    public void recalculateScreen() {
        recalculateScreenCounter = 10;
    }

    //sensor related
    public void initialiseAccelerometer() {
        //device has its default landscape or portrait
        Display display = ((WindowManager) activity.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        int rotation = display.getRotation();
        if (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            //portrait
            if (rotation == Surface.ROTATION_0)
                default_lanscape = false;
            if (rotation == Surface.ROTATION_180)
                default_lanscape = false;
            if (rotation == Surface.ROTATION_90)
                default_lanscape = true;
            if (rotation == Surface.ROTATION_270)
                default_lanscape = true;
        } else {
            //landscape
            if (rotation == Surface.ROTATION_0)
                default_lanscape = true;
            if (rotation == Surface.ROTATION_180)
                default_lanscape = true;
            if (rotation == Surface.ROTATION_90)
                default_lanscape = false;
            if (rotation == Surface.ROTATION_270)
                default_lanscape = false;
        }
        default_lanscape_rotation = rotation;

        sm = (SensorManager) activity.getSystemService(Context.SENSOR_SERVICE);
        if (sm.getSensorList(Sensor.TYPE_ACCELEROMETER).size() != 0) {
            s = sm.getSensorList(Sensor.TYPE_ACCELEROMETER).get(0);
            sm.registerListener(this, s, SensorManager.SENSOR_DELAY_NORMAL);
        }

    }

    public void CalibrateAccelerometer() {
        calibratex = sensorx * Math.abs(sensorx);
        calibratey = sensory * Math.abs(sensory);
    }

    public PointF getAccelerometer() {
        return new PointF((sensorx * Math.abs(sensorx) - calibratex), (sensory * Math.abs(sensory) - calibratey));
    }

    /* Touch events.......................................................... */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (initialised) {
            int pointerIndex = event.getActionIndex();

            onTouch(event.getX(pointerIndex), event.getY(pointerIndex), event);

        }
        if (Switch_GestureDetector)
            return gestureDetector.onTouchEvent(event);
        else
            return true;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (initialised) {
            //read values
            if (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
                if (default_lanscape) {
                    sensorx = -event.values[1];
                    sensory = -event.values[0];
                } else {
                    sensory = event.values[1];
                    sensorx = -event.values[0];
                }
            } else {
                if (default_lanscape) {
                    sensory = event.values[1];
                    sensorx = -event.values[0];
                } else {
                    sensorx = event.values[1];
                    sensory = event.values[0];
                }
            }

            //call accelerometer event
            onAccelerometer(new PointF((sensorx - calibratex), (sensory - calibratey)));

        }
        //sleep for a while
        try {
            Thread.sleep(16);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }

	/* Gesture detection....................................................... */

    private final class GestureListener extends SimpleOnGestureListener {

        private final int SWIPE_DISTANCE_THRESHOLD = dpToPx(50);
        private final int SWIPE_VELOCITY_THRESHOLD = dpToPx(50);

        int dpToPx(int dp) {
            float density = getApplicationContext().getResources().getDisplayMetrics().density;
            return Math.round((float) dp * density);
        }

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            float distanceX = e2.getX() - e1.getX();
            float distanceY = e2.getY() - e1.getY();
            if (Math.abs(distanceX) > Math.abs(distanceY) && Math.abs(distanceX) > SWIPE_DISTANCE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                if (distanceX > 0)
                    onSwipeRight();
                else
                    onSwipeLeft();
                return true;
            }
            if (Math.abs(distanceY) > Math.abs(distanceX) && Math.abs(distanceY) > SWIPE_DISTANCE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                if (distanceY > 0)
                    onSwipeDown();
                else
                    onSwipeUp();
                return true;
            }
            return false;
        }

    }

    /* pause, destroy, resume................................................ */
    @Override
    protected void onStart() {
        super.onStart();//Get an Analytics tracker to report app starts &amp; uncaught exceptions etc.
        GoogleAnalytics.getInstance(this).reportActivityStart(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        //Stop the analytics tracking
        GoogleAnalytics.getInstance(this).reportActivityStop(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Resume();
        if (adView != null) {
            adView.resume();
        }
    }

    @Override
    protected void onPause() {
        if (adView != null) {
            adView.pause();
        }
        Pause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        Finish();
        // Destroy the AdView.
        if (adView != null) {
            adView.destroy();
        }
        super.onDestroy();
    }

}
