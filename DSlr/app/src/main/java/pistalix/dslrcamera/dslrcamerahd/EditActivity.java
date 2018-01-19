package pistalix.dslrcamera.dslrcamerahd;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;


import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;

import java.util.ArrayList;
import java.util.Iterator;

public class EditActivity extends AppCompatActivity implements View.OnClickListener {

    public static int quality = 1;
    private ImageView iv_OffSet, iv_size;
    private DiscreteSeekBar sbBrush, sbOffset;
    private Bitmap bm1, bm2;
    int progress;
    private ImageView img;
    static Bitmap newbitMap;
    Bitmap Transparent;
    AlertDialog alert;
    Bitmap bitmap;
    Canvas bitmapCanvas;
    int checkeditem = 2;
    ImageView cust;
    Dialog dialog;
    TouchView dv;
    double height;
    ImageView ivFull;
    Paint mPaint;
    Matrix matrix;
    int offset = 10;
    Canvas pcanvas;
    int r = 55;
    Paint strokePaint;
    int x = 0;
    int y = 0;
    private ImageView iv_Undo, iv_Redo, ic_done, fback;
    private FrameLayout imageFrame;
    private com.google.android.gms.ads.InterstitialAd mInterstitialAdMob;
    private LinearLayout adView;


    public class TouchView extends ImageView implements View.OnTouchListener {
        private static final float TOUCH_TOLERANCE = 0.0f;
        private Canvas c2;
        private int color;
        private boolean isTouched = false;
        private Paint mBitmapPaint;
        private Canvas mCanvas;
        private Path mPath;
        private float mX;
        private float mY;
        Paint paint = new Paint();
        private ArrayList<PathPoints> paths = new ArrayList();
        private ArrayList<PathPoints> undonePaths = new ArrayList();
        private int x;
        private int y;

        class PathPoints {
            private int color;
            private boolean isTextToDraw;
            private Paint mPaint;
            private Path path;
            private String textToDraw;
            private int x;
            private int y;

            public PathPoints(Path path, int color, Paint mpaint) {
                this.path = path;
                this.color = color;
                this.mPaint = mpaint;
            }

            public PathPoints(int color, String textToDraw, boolean isTextToDraw, int x, int y) {
                this.color = color;
                this.textToDraw = textToDraw;
                this.isTextToDraw = isTextToDraw;
                this.x = x;
                this.y = y;
            }

            public Path getPath() {
                return this.path;
            }

            public void setPath(Path path) {
                this.path = path;
            }

            public Paint getPaint() {
                return this.mPaint;
            }

            public void setPaint(Paint path) {
                this.mPaint = path;
            }

            public int getColor() {
                return this.color;
            }

            public void setColor(int color) {
                this.color = color;
            }

            public String getTextToDraw() {
                return this.textToDraw;
            }

            public void setTextToDraw(String textToDraw) {
                this.textToDraw = textToDraw;
            }

            public boolean isTextToDraw() {
                return this.isTextToDraw;
            }

            public void setTextToDraw(boolean isTextToDraw) {
                this.isTextToDraw = isTextToDraw;
            }

            public int getX() {
                return this.x;
            }

            public void setX(int x) {
                this.x = x;
            }

            public int getY() {
                return this.y;
            }

            public void setY(int y) {
                this.y = y;
            }
        }

        public TouchView(Context context) {
            super(context);
            setFocusable(true);
            setFocusableInTouchMode(true);
            DisplayMetrics metrics = context.getResources().getDisplayMetrics();
            int width = metrics.widthPixels;
            int height = metrics.heightPixels;
            setOnTouchListener(this);
            Transparent = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            this.mBitmapPaint = new Paint(4);
            mPaint = new Paint();
            mPaint.setAntiAlias(true);
            mPaint.setDither(true);
            mPaint.setColor(0);
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setStrokeJoin(Paint.Join.ROUND);
            mPaint.setStrokeCap(Paint.Cap.ROUND);
            mPaint.setStrokeWidth((float) r);
            mPaint.setMaskFilter(new BlurMaskFilter(10.0f, BlurMaskFilter.Blur.NORMAL));
            mPaint.setAlpha(0);
            mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
            strokePaint = new Paint();
            strokePaint.setDither(true);
            strokePaint.setColor(-256);
            strokePaint.setStyle(Paint.Style.STROKE);
            strokePaint.setAntiAlias(true);
            strokePaint.setStrokeWidth(5.0f);
            this.c2 = new Canvas();
            this.c2.setBitmap(Transparent);
            this.mPath = new Path();
            this.paths.add(new PathPoints(this.mPath, this.color, mPaint));
            this.mCanvas = new Canvas();
        }

        protected void onDraw(Canvas canvas) {
            this.c2.drawBitmap(bm2, 0.0f, 0.0f, this.mBitmapPaint);
            canvas.drawBitmap(Transparent, 0.0f, 0.0f, null);
            Iterator it = this.paths.iterator();
            while (it.hasNext()) {
                PathPoints p = (PathPoints) it.next();
                if (r == 80) {
                    canvas.drawCircle(this.mX, this.mY, (float) (r - 38), strokePaint);
                } else if (r == 55) {
                    canvas.drawCircle(this.mX, this.mY, (float) (r - 27), strokePaint);
                } else {
                    canvas.drawCircle(this.mX, this.mY, (float) (r - 18), strokePaint);
                }
                this.c2.drawPath(p.getPath(), p.getPaint());
            }
        }

        private void touch_start(float x, float y) {
            this.mPath.reset();
            this.mPath.moveTo(x, y);
            this.mX = x;
            this.mY = y;
        }

        private void touch_move(float x, float y) {
            float dx = Math.abs(x - this.mX);
            float dy = Math.abs(y - this.mY);
            if (dx >= 0.0f || dy >= 0.0f) {
                this.mPath.quadTo(this.mX, this.mY, (this.mX + x) / 2.0f, (this.mY + y) / 2.0f);
                this.mX = x;
                this.mY = y;
            }
        }

        private void touch_up() {
            this.mPath.lineTo(this.mX, this.mY);
            this.mPath = new Path();
            mPaint = new Paint();
            mPaint.setAntiAlias(true);
            mPaint.setDither(true);
            mPaint.setColor(0);
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setStrokeJoin(Paint.Join.ROUND);
            mPaint.setStrokeCap(Paint.Cap.ROUND);
            mPaint.setStrokeWidth((float) r);
            mPaint.setAlpha(0);
            mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
            mPaint.setMaskFilter(new BlurMaskFilter(10.0f, BlurMaskFilter.Blur.NORMAL));
            this.paths.add(new PathPoints(this.mPath, this.color, mPaint));
        }

        public boolean onTouch(View arg0, MotionEvent event) {
            this.isTouched = true;
            float x = event.getX();
            float y = event.getY() - ((float) offset);
            switch (event.getAction()) {
                case 0:
                    touch_start(x, y);
                    invalidate();
                    break;
                case 1:
                    touch_up();
                    invalidate();
                    break;
                case 2:
                    touch_move(x, y);
                    invalidate();
                    break;
            }
            return true;
        }

        public void onClickUndo() {
            if (this.paths.size() > 1) {
                this.undonePaths.add((PathPoints) this.paths.remove(this.paths.size() - 2));
                invalidate();
            }
        }

        public void onClickRedo() {
            if (this.undonePaths.size() > 0) {
                this.paths.add((PathPoints) this.undonePaths.remove(this.undonePaths.size() - 1));
                invalidate();
            }
        }
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

    private void showAdmobInterstitial() {
        if (this.mInterstitialAdMob != null && this.mInterstitialAdMob.isLoaded()) {
            this.mInterstitialAdMob.show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        mInterstitialAdMob = showAdmobFullAd();
        loadAdmobAd();
        quality = 1;
        Bind();


        bm1 = Common.bmA;
        bm2 = Common.bmB;
        progress = Common.prog;
        if (bm1 == null || bm2 == null) {
            Toast.makeText(getApplicationContext(), "Error Occured.", 0).show();
            finish();
        }
        img.setImageBitmap(bm1);
        dv = new TouchView(this);
        imageFrame.setForegroundGravity(17);

        imageFrame.addView(this.dv, new FrameLayout.LayoutParams(-1, -1));

    }

    private void Bind() {
        iv_OffSet = (ImageView) findViewById(R.id.iv_OffSet);
        iv_OffSet.setOnClickListener(this);
        img = (ImageView) findViewById(R.id.initial);
        iv_size = (ImageView) findViewById(R.id.iv_size);
        iv_size.setOnClickListener(this);
        iv_Undo = (ImageView) findViewById(R.id.iv_Undo);
        iv_Undo.setOnClickListener(this);
        iv_Redo = (ImageView) findViewById(R.id.iv_Redo);
        iv_Redo.setOnClickListener(this);
        sbOffset = (DiscreteSeekBar) findViewById(R.id.sbOffset);
        sbBrush = (DiscreteSeekBar) findViewById(R.id.sbBrush);
        imageFrame = (FrameLayout) findViewById(R.id.imageFrame);
        fback = (ImageView) findViewById(R.id.fback);
        fback.setOnClickListener(this);
        ic_done = (ImageView) findViewById(R.id.ic_done);
        ic_done.setOnClickListener(this);
        sbBrush.setOnProgressChangeListener(new DiscreteSeekBar.OnProgressChangeListener() {
            @Override
            public void onProgressChanged(DiscreteSeekBar seekBar, int value, boolean fromUser) {
                r = 50;
                r = value;

            }

            @Override
            public void onStartTrackingTouch(DiscreteSeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(DiscreteSeekBar seekBar) {

            }
        });
        sbOffset.setProgress(offset);
        sbOffset.setOnProgressChangeListener(new DiscreteSeekBar.OnProgressChangeListener() {
            @Override
            public void onProgressChanged(DiscreteSeekBar seekBar, int value, boolean fromUser) {
                offset = value;

            }

            @Override
            public void onStartTrackingTouch(DiscreteSeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(DiscreteSeekBar seekBar) {

            }
        });
    }

    public void saveImageAs() {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case -2:
                        Toast.makeText(EditActivity.this, "Cancel", 0).show();
                        return;
                    case -1:
                        int w = EditActivity.this.bm1.getWidth();
                        int h = EditActivity.this.bm1.getHeight();
                        System.out.println(new StringBuilder(String.valueOf(w)).append(" , ").append(h).toString());
                        System.out.println(new StringBuilder(String.valueOf(EditActivity.this.Transparent.getWidth())).append(" , ").append(EditActivity.this.Transparent.getHeight()).toString());
                        Bitmap.Config config = EditActivity.this.bm1.getConfig();
                        if (config == null) {
                            config = Bitmap.Config.ARGB_8888;
                        }
                        EditActivity.newbitMap = Bitmap.createBitmap(w, h, config);
                        Canvas newCanvas = new Canvas(EditActivity.newbitMap);
                        newCanvas.drawBitmap(EditActivity.this.bm1, 0.0f, 0.0f, null);
                        newCanvas.drawBitmap(EditActivity.this.Transparent, 0.0f, 0.0f, null);
                        EditActivity.this.startActivity(new Intent(EditActivity.this, ImageEditActivity.class));
                        showAdmobInterstitial();
                        return;
                    default:
                        return;
                }
            }
        };
        new AlertDialog.Builder(this).setMessage("Are you sure you want to continue?").setPositiveButton("Yes", dialogClickListener).setNegativeButton("No", dialogClickListener).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_OffSet:
                sbOffset.setVisibility(View.VISIBLE);
                sbBrush.setVisibility(View.GONE);
                break;
            case R.id.iv_size:
                sbOffset.setVisibility(View.GONE);
                sbBrush.setVisibility(View.VISIBLE);
                break;
            case R.id.iv_Redo:

                dv.onClickRedo();
                break;
            case R.id.iv_Undo:
                dv.onClickUndo();
                break;
            case R.id.fback:
                finish();
                break;
            case R.id.ic_done:
                saveImageAs();
                break;
        }

    }








}
