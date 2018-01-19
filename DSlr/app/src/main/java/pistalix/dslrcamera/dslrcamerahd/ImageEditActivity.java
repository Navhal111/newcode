package pistalix.dslrcamera.dslrcamerahd;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorSelectedListener;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.cast.TextTrackStyle;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import it.sephiroth.android.library.imagezoom.ImageViewTouchBase;
import pistalix.dslrcamera.dslrcamerahd.MyTouch.MultiTouchListener;
import pistalix.dslrcamera.dslrcamerahd.TextDemo.DemoStickerView;
import pistalix.dslrcamera.dslrcamerahd.TextDemo.StickerTextiimageView;
import pistalix.dslrcamera.dslrcamerahd.TextDemo.TextDemoActivity;
import pistalix.dslrcamera.dslrcamerahd.Vignette.ImageViewVignette;
import pistalix.dslrcamera.dslrcamerahd.stickerview.StickerImageView;
import pistalix.dslrcamera.dslrcamerahd.stickerview.StickerView;

public class ImageEditActivity extends AppCompatActivity implements View.OnClickListener, DiscreteSeekBar.OnProgressChangeListener {
    public static int mcolor;

    public static int REQ_TEXT = 100;
    Bitmap bitmap;
    ArrayList<Integer> stickerviewId = new ArrayList<>();
    ArrayList<Modal> Overlylist;
    ArrayList<Integer> stikerlist1;
    ArrayList<Integer> stikerlist2;
    ArrayList<Integer> stikerlist3;
    ArrayList<Integer> stikerlist4;
    ArrayList<Integer> stikerlist5;
    ArrayList<Integer> stikerlist6;
    ArrayList<Integer> stikerlist7;
    ArrayList<Integer> stikerlist8;
    private ImageView ivShow, iv_briteness, iv_Contrass, iv_satchuation;
    private ImageView iv_ColorEffact;
    private LinearLayout llbritness;
    private DiscreteSeekBar sbBriteness, sbContrass, sbsatwation;
    private Bitmap newbmpBrightContrast;
    DiscreteSeekBar.OnProgressChangeListener BrightBarChangeListener = new DiscreteSeekBar.OnProgressChangeListener() {
        @Override
        public void onProgressChanged(DiscreteSeekBar seekBar, int value, boolean fromUser) {
            changeBitmapContrastBrightness();
        }

        @Override
        public void onStartTrackingTouch(DiscreteSeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(DiscreteSeekBar seekBar) {

        }
    };
    public static Bitmap finalEditedBitmapImage;
    private ImageView iv_Text, iv_OverLy;
    private int colorCode = 0;
    private FrameLayout imageFrame;
    private HorizontalListView Grid_OverLay;
    private Overlyadapter overlayAdaptor;
    private ImageView overlayImage, Iv_Feather, Iv_Intensity, iv_Vignette;
    private int overlayid;
    private ImageView ef_original, ef1, ef2, ef3, ef4, ef5, ef6, ef7, ef8, ef9, ef10, ef11, ef12, ef13, ef14, ef15, ef16, ef17, ef18, ef19, ef20, ef21, ef22;
    private ImageView iv_sticker, sticker_type_01, sticker_type_02, sticker_type_03, sticker_type_04, sticker_type_05, sticker_type_06, sticker_type_07, sticker_type_08;
    private HorizontalScrollView HSSticker;
    private LinearLayout llMainSticker, llVignette, llseekbar, llseekbar2;
    private LinearLayout llHLSticker;
    private ImageView HlBack;
    private StickerAdapter stikerAdaptor;
    private HorizontalListView HLSticker;
    private StickerImageView sticker;
    private Integer stickerId;
    private int view_id;
    private HorizontalScrollView HL_Effact;
    private ImageView iv_effact;
    private DiscreteSeekBar mSeekBar1, mSeekBar2;
    private ImageViewVignette mImageView;
    private int intensity;
    private String TAG = "PhotoEditorActivity";
    private int currentBackgroundColor = 0xffffffff;
    private DemoStickerView.OnTouchSticker onTouchSticker1 = new DemoStickerView.OnTouchSticker() {
        @Override
        public void onTouchedSticker() {
            removeBorder();
        }
    };
    private StickerView.OnTouchSticker onTouchSticker = new StickerView.OnTouchSticker() {
        @Override
        public void onTouchedSticker() {
            removeBorder();
        }

    };
    private ImageView iv_color;
    private ImageView iv_save;
    private static final int MY_REQUEST_CODE = 1;
    public static String _url;
    private ProgressBar mProgress;
    private ImageView back;

    private AdView mAdView;
    private LinearLayout adView;
    private com.google.android.gms.ads.InterstitialAd mInterstitialAdMob;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_edit);


        mInterstitialAdMob = showAdmobFullAd();
        loadAdmobAd();
        Bind();

        bindEffectIcon();
        mImageView.setDisplayType(ImageViewTouchBase.DisplayType.FIT_IF_BIGGER);
        intensity = (mImageView.getVignetteIntensity() + 100) / 2;
        Log.d(TAG, "intensity: " + intensity);
        mSeekBar1.setProgress(intensity);

        float feather = mImageView.getVignetteFeather();
        mSeekBar2.setProgress((int) (feather * 100));
        bitmap = EditActivity.newbitMap;
        mImageView.setImageBitmap(bitmap);
    }




    private void Bind() {
        mImageView = (ImageViewVignette) findViewById(R.id.images);
        mImageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                removeBorder();
                return false;
            }
        });
        imageFrame = (FrameLayout) findViewById(R.id.imageFrame);
        imageFrame.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                removeBorder();
                return false;
            }
        });
        iv_ColorEffact = (ImageView) findViewById(R.id.iv_ColorEffact);
        iv_ColorEffact.setOnClickListener(this);
        llbritness = (LinearLayout) findViewById(R.id.llbritness);
        iv_Text = (ImageView) findViewById(R.id.iv_Text);
        iv_Text.setOnClickListener(this);
        iv_satchuation = (ImageView) findViewById(R.id.iv_satchuation);
        iv_satchuation.setOnClickListener(this);
        iv_Contrass = (ImageView) findViewById(R.id.iv_Contrass);
        iv_Contrass.setOnClickListener(this);
        iv_briteness = (ImageView) findViewById(R.id.iv_briteness);
        iv_briteness.setOnClickListener(this);
        Grid_OverLay = (HorizontalListView) findViewById(R.id.Grid_OverLay);
        iv_OverLy = (ImageView) findViewById(R.id.iv_OverLy);
        iv_OverLy.setOnClickListener(this);
        iv_sticker = (ImageView) findViewById(R.id.iv_sticker);
        iv_sticker.setOnClickListener(this);
        Common.brightno = 65;
        Common.contrastno = 0;
        Common.saturationno = 237;
        overlayImage = (ImageView) findViewById(R.id.overlayImage);
        overlayImage.setOnTouchListener(new MultiTouchListener());
        sbsatwation = (DiscreteSeekBar) findViewById(R.id.sbsatwation);
        sbContrass = (DiscreteSeekBar) findViewById(R.id.sbContrass);
        sbBriteness = (DiscreteSeekBar) findViewById(R.id.sbBriteness);
        sbBriteness.setOnProgressChangeListener(BrightBarChangeListener);
        sbContrass.setOnProgressChangeListener(BrightBarChangeListener);
        sbsatwation.setOnProgressChangeListener(BrightBarChangeListener);
        HSSticker = (HorizontalScrollView) findViewById(R.id.HSSticker);
        HLSticker = (HorizontalListView) findViewById(R.id.HLSticker);
        llMainSticker = (LinearLayout) findViewById(R.id.llMainSticker);
        llHLSticker = (LinearLayout) findViewById(R.id.llHLSticker);
        HlBack = (ImageView) findViewById(R.id.HlBack);
        HlBack.setOnClickListener(this);
        sticker_type_01 = (ImageView) findViewById(R.id.sticker_type_01);
        sticker_type_01.setOnClickListener(this);
        sticker_type_02 = (ImageView) findViewById(R.id.sticker_type_02);
        sticker_type_02.setOnClickListener(this);
        sticker_type_03 = (ImageView) findViewById(R.id.sticker_type_03);
        sticker_type_03.setOnClickListener(this);
        sticker_type_04 = (ImageView) findViewById(R.id.sticker_type_04);
        sticker_type_04.setOnClickListener(this);
        sticker_type_05 = (ImageView) findViewById(R.id.sticker_type_05);
        sticker_type_05.setOnClickListener(this);
        sticker_type_06 = (ImageView) findViewById(R.id.sticker_type_06);
        sticker_type_06.setOnClickListener(this);
        sticker_type_07 = (ImageView) findViewById(R.id.sticker_type_07);
        sticker_type_07.setOnClickListener(this);
        sticker_type_08 = (ImageView) findViewById(R.id.sticker_type_08);
        sticker_type_08.setOnClickListener(this);

        HL_Effact = (HorizontalScrollView) findViewById(R.id.HL_Effact);
        iv_effact = (ImageView) findViewById(R.id.iv_effact);
        iv_effact.setOnClickListener(this);
        iv_color = (ImageView) findViewById(R.id.iv_color);
        iv_color.setOnClickListener(this);
        Iv_Feather = (ImageView) findViewById(R.id.Iv_Feather);
        Iv_Feather.setOnClickListener(this);
        Iv_Intensity = (ImageView) findViewById(R.id.Iv_Intensity);
        Iv_Intensity.setOnClickListener(this);
        llseekbar2 = (LinearLayout) findViewById(R.id.llseekbar2);
        llseekbar = (LinearLayout) findViewById(R.id.llseekbar);
        llVignette = (LinearLayout) findViewById(R.id.llVignette);
        mSeekBar1 = (DiscreteSeekBar) findViewById(R.id.seekBar);
        mSeekBar2 = (DiscreteSeekBar) findViewById(R.id.seekBar2);
        mSeekBar1.setOnProgressChangeListener(this);
        mSeekBar2.setOnProgressChangeListener(this);
        iv_Vignette = (ImageView) findViewById(R.id.iv_Vignette);
        iv_Vignette.setOnClickListener(this);
        iv_save = (ImageView) findViewById(R.id.iv_save);
        iv_save.setOnClickListener(this);
        mProgress = (ProgressBar) findViewById(R.id.progressbar1);
        back = (ImageView) findViewById(R.id.fback);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();


            }
        });
    }

    private void bindEffectIcon() {
        ef_original = (ImageView) findViewById(R.id.ef_original);
        ef_original.setOnClickListener(this);
        ef1 = (ImageView) findViewById(R.id.ef1);
        ef1.setOnClickListener(this);
        ef2 = (ImageView) findViewById(R.id.ef2);
        ef2.setOnClickListener(this);
        ef3 = (ImageView) findViewById(R.id.ef3);
        ef3.setOnClickListener(this);
        ef4 = (ImageView) findViewById(R.id.ef4);
        ef4.setOnClickListener(this);
        ef5 = (ImageView) findViewById(R.id.ef5);
        ef5.setOnClickListener(this);
        ef6 = (ImageView) findViewById(R.id.ef6);
        ef6.setOnClickListener(this);
        ef7 = (ImageView) findViewById(R.id.ef7);
        ef7.setOnClickListener(this);
        ef8 = (ImageView) findViewById(R.id.ef8);
        ef8.setOnClickListener(this);
        ef9 = (ImageView) findViewById(R.id.ef9);
        ef9.setOnClickListener(this);
        ef10 = (ImageView) findViewById(R.id.ef10);
        ef10.setOnClickListener(this);
        ef11 = (ImageView) findViewById(R.id.ef11);
        ef11.setOnClickListener(this);
        ef12 = (ImageView) findViewById(R.id.ef12);
        ef12.setOnClickListener(this);
        ef13 = (ImageView) findViewById(R.id.ef13);
        ef13.setOnClickListener(this);
        ef14 = (ImageView) findViewById(R.id.ef14);
        ef14.setOnClickListener(this);
        ef15 = (ImageView) findViewById(R.id.ef15);
        ef15.setOnClickListener(this);
        ef16 = (ImageView) findViewById(R.id.ef16);
        ef16.setOnClickListener(this);
        ef17 = (ImageView) findViewById(R.id.ef17);
        ef17.setOnClickListener(this);
        ef18 = (ImageView) findViewById(R.id.ef18);
        ef18.setOnClickListener(this);
        ef19 = (ImageView) findViewById(R.id.ef19);
        ef19.setOnClickListener(this);
        ef20 = (ImageView) findViewById(R.id.ef20);
        ef20.setOnClickListener(this);
        ef21 = (ImageView) findViewById(R.id.ef21);
        ef21.setOnClickListener(this);
        ef22 = (ImageView) findViewById(R.id.ef22);
        ef22.setOnClickListener(this);
        Effects.applyEffectNone(ef_original);
        Effects.applyEffect1(ef1);
        Effects.applyEffect2(ef2);
        Effects.applyEffect3(ef3);
        Effects.applyEffect4(ef4);
        Effects.applyEffect5(ef5);
        Effects.applyEffect6(ef6);
        Effects.applyEffect7(ef7);
        Effects.applyEffect8(ef8);
        Effects.applyEffect9(ef9);
        Effects.applyEffect10(ef10);
        Effects.applyEffect11(ef11);
        Effects.applyEffect12(ef12);
        Effects.applyEffect13(ef13);
        Effects.applyEffect14(ef14);
        Effects.applyEffect15(ef15);
        Effects.applyEffect16(ef16);
        Effects.applyEffect17(ef17);
        Effects.applyEffect18(ef18);
        Effects.applyEffect19(ef19);
        Effects.applyEffect20(ef20);
        Effects.applyEffect21(ef21);
        Effects.applyEffect22(ef22);
        HL_Effact.setVisibility(View.GONE);
    }

    public void changeBitmapContrastBrightness() {
        float brightness = ((float) this.sbBriteness.getProgress()) - 65.0f;
        float contrast = (((float) this.sbContrass.getProgress()) / 100.0f) + TextTrackStyle.DEFAULT_FONT_SCALE;
        float sat = ((float) (this.sbsatwation.getProgress() + 18)) / 256.0f;
        Common.brightno = this.sbBriteness.getProgress();
        Common.contrastno = this.sbContrass.getProgress();
        Common.saturationno = this.sbsatwation.getProgress();
        ColorMatrix cm = new ColorMatrix(new float[]{contrast, 0.0f, 0.0f, 0.0f, brightness, 0.0f, contrast, 0.0f, 0.0f, brightness, 0.0f, 0.0f, contrast, 0.0f, brightness, 0.0f, 0.0f, 0.0f, TextTrackStyle.DEFAULT_FONT_SCALE, 0.0f});
        Bitmap bmpBrightContrast = Bitmap.createBitmap(bitmap.getWidth(), this.bitmap.getHeight(), this.bitmap.getConfig());
        Canvas canvas = new Canvas(bmpBrightContrast);
        Paint paint = new Paint();
        paint.setColorFilter(new ColorMatrixColorFilter(cm));
        canvas.drawBitmap(this.bitmap, 0.0f, 0.0f, paint);
        newbmpBrightContrast = Bitmap.createBitmap(this.bitmap.getWidth(), this.bitmap.getHeight(), this.bitmap.getConfig());
        canvas = new Canvas(this.newbmpBrightContrast);
        ColorMatrix cm1 = new ColorMatrix();
        cm1.setSaturation(sat);
        paint = new Paint();
        paint.setColorFilter(new ColorMatrixColorFilter(cm1));
        canvas.drawBitmap(bmpBrightContrast, 0.0f, 0.0f, paint);
        mImageView.setImageBitmap(this.newbmpBrightContrast);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_ColorEffact:
                removeBorder();
                llbritness.setVisibility(View.VISIBLE);
                Grid_OverLay.setVisibility(View.GONE);
                llMainSticker.setVisibility(View.GONE);
                HL_Effact.setVisibility(View.GONE);
                llVignette.setVisibility(View.GONE);
                break;
            case R.id.iv_effact:
                removeBorder();
                HL_Effact.setVisibility(View.VISIBLE);
                llbritness.setVisibility(View.GONE);
                Grid_OverLay.setVisibility(View.GONE);
                llMainSticker.setVisibility(View.GONE);
                llVignette.setVisibility(View.GONE);
                break;
            case R.id.iv_briteness:
                iv_briteness.setImageResource(R.drawable.briw);
                iv_Contrass.setImageResource(R.drawable.cont);
                iv_satchuation.setImageResource(R.drawable.sat);
                sbBriteness.setVisibility(View.VISIBLE);
                sbContrass.setVisibility(View.GONE);
                sbsatwation.setVisibility(View.GONE);

                break;
            case R.id.iv_Contrass:
                iv_Contrass.setImageResource(R.drawable.contw);
                iv_briteness.setImageResource(R.drawable.bri);
                iv_satchuation.setImageResource(R.drawable.sat);
                sbBriteness.setVisibility(View.GONE);
                sbContrass.setVisibility(View.VISIBLE);
                sbsatwation.setVisibility(View.GONE);
                break;
            case R.id.iv_satchuation:
                iv_satchuation.setImageResource(R.drawable.satw);
                iv_briteness.setImageResource(R.drawable.bri);
                iv_Contrass.setImageResource(R.drawable.cont);
                sbBriteness.setVisibility(View.GONE);
                sbContrass.setVisibility(View.GONE);
                sbsatwation.setVisibility(View.VISIBLE);

                break;
            case R.id.iv_Text:
                removeBorder();
                startActivityForResult(new Intent(ImageEditActivity.this, TextDemoActivity.class), REQ_TEXT);
                llbritness.setVisibility(View.GONE);
                Grid_OverLay.setVisibility(View.GONE);
                llMainSticker.setVisibility(View.GONE);
                HL_Effact.setVisibility(View.GONE);
                llVignette.setVisibility(View.GONE);
                break;
            case R.id.iv_OverLy:
                removeBorder();
                Grid_OverLay.setVisibility(View.VISIBLE);
                llbritness.setVisibility(View.GONE);
                llMainSticker.setVisibility(View.GONE);
                HL_Effact.setVisibility(View.GONE);
                llVignette.setVisibility(View.GONE);
                setOverLayList();
                break;
            case R.id.iv_sticker:
                removeBorder();

                llMainSticker.setVisibility(View.VISIBLE);
                llbritness.setVisibility(View.GONE);
                Grid_OverLay.setVisibility(View.GONE);
                llVignette.setVisibility(View.GONE);
                HL_Effact.setVisibility(View.GONE);
                break;
            case R.id.HlBack:
                llHLSticker.setVisibility(View.GONE);
                break;

            case R.id.sticker_type_01:
                llHLSticker.setVisibility(View.VISIBLE);
                setStickerList1();
                break;
            case R.id.sticker_type_02:
                llHLSticker.setVisibility(View.VISIBLE);
                setStickerList2();
                break;
            case R.id.sticker_type_03:
                llHLSticker.setVisibility(View.VISIBLE);
                setStickerList3();
                break;
            case R.id.sticker_type_04:
                llHLSticker.setVisibility(View.VISIBLE);
                setStickerList4();
                break;
            case R.id.sticker_type_05:
                llHLSticker.setVisibility(View.VISIBLE);
                setStickerList5();
                break;
            case R.id.sticker_type_06:
                llHLSticker.setVisibility(View.VISIBLE);
                setStickerList6();
                break;
            case R.id.sticker_type_07:
                llHLSticker.setVisibility(View.VISIBLE);
                setStickerList7();
                break;
            case R.id.sticker_type_08:
                llHLSticker.setVisibility(View.VISIBLE);
                setStickerList8();
                break;
            case R.id.ef_original:
                Effects.applyEffectNone(mImageView);
                break;
            case R.id.ef1:
                Effects.applyEffect1(mImageView);
                break;
            case R.id.ef2:
                Effects.applyEffect2(mImageView);
                break;
            case R.id.ef3:
                Effects.applyEffect3(mImageView);
                break;
            case R.id.ef4:
                Effects.applyEffect4(mImageView);
                break;
            case R.id.ef5:
                Effects.applyEffect5(mImageView);
                break;
            case R.id.ef6:
                Effects.applyEffect6(mImageView);
                break;
            case R.id.ef7:
                Effects.applyEffect7(mImageView);
                break;
            case R.id.ef8:
                Effects.applyEffect8(mImageView);
                break;
            case R.id.ef9:
                Effects.applyEffect9(mImageView);
                break;
            case R.id.ef10:
                Effects.applyEffect10(mImageView);
                break;
            case R.id.ef11:
                Effects.applyEffect11(mImageView);
                break;
            case R.id.ef12:
                Effects.applyEffect12(mImageView);
                break;
            case R.id.ef13:
                Effects.applyEffect13(mImageView);
                break;
            case R.id.ef14:
                Effects.applyEffect14(mImageView);
                break;
            case R.id.ef15:
                Effects.applyEffect15(mImageView);
                break;
            case R.id.ef16:
                Effects.applyEffect16(mImageView);
                break;
            case R.id.ef17:
                Effects.applyEffect17(mImageView);
                break;
            case R.id.ef18:
                Effects.applyEffect18(mImageView);
                break;
            case R.id.ef19:
                Effects.applyEffect19(mImageView);
                break;
            case R.id.ef20:
                Effects.applyEffect20(mImageView);
                break;
            case R.id.ef21:
                Effects.applyEffect21(mImageView);
                break;
            case R.id.ef22:
                Effects.applyEffect22(mImageView);
                break;
            case R.id.Iv_Intensity:
                llseekbar.setVisibility(View.VISIBLE);
                llseekbar2.setVisibility(View.GONE);
                break;
            case R.id.Iv_Feather:
                llseekbar.setVisibility(View.GONE);
                llseekbar2.setVisibility(View.VISIBLE);
                break;
            case R.id.iv_Vignette:
                removeBorder();
                llVignette.setVisibility(View.VISIBLE);
                llbritness.setVisibility(View.GONE);
                Grid_OverLay.setVisibility(View.GONE);
                llMainSticker.setVisibility(View.GONE);
                HL_Effact.setVisibility(View.GONE);
                break;
            case R.id.iv_color:
                colordailog();
                break;
            case R.id.iv_save:
                llVignette.setVisibility(View.GONE);
                llbritness.setVisibility(View.GONE);
                Grid_OverLay.setVisibility(View.GONE);
                llMainSticker.setVisibility(View.GONE);
                HL_Effact.setVisibility(View.GONE);
                iv_save.setVisibility(View.GONE);
                mProgress.setVisibility(View.VISIBLE);
                removeBorder();
                if (Build.VERSION.SDK_INT >= 23) {
                    if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            == PackageManager.PERMISSION_GRANTED) {
                        create_Save_Image();
                    } else if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                MY_REQUEST_CODE);
                    }
                } else {
                    create_Save_Image();
                }

                break;

        }
    }


    private void create_Save_Image() {

        Processbar();
        Log.v("TAG", "saveImageInCache is called");
        finalEditedBitmapImage = getMainFrameBitmap();
        saveImage(finalEditedBitmapImage);
        Intent i2 = new Intent(ImageEditActivity.this, SaveShare.class);
        startActivity(i2);
//        showFBInterstitial();
        showAdmobInterstitial();

    }

    private void Processbar() {
        mProgress.setProgress(25);   // Main Progress
        mProgress.setSecondaryProgress(50); // Secondary Progress
        mProgress.setMax(100); // Maximum Progress
    }

    private void saveImage(Bitmap bitmap2) {
        Log.v("TAG", "saveImageInCache is called");
        Bitmap bitmap;
        OutputStream output;

        // Retrieve the image from the res folder
        bitmap = bitmap2;

        File filepath = Environment.getExternalStorageDirectory();
        // Create a new folder in SD Card
        File dir = new File(filepath.getAbsolutePath() + "/" + Utility.Edit_Folder_name);
        dir.mkdirs();

        // Create a name for the saved image
        String ts = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String FileName = ts + ".jpeg";
        File file = new File(dir, FileName);
        file.renameTo(file);
        String _uri = "file://" + filepath.getAbsolutePath() + "/" + Utility.Edit_Folder_name + "/" + FileName;
        //for share image
        String _uri2 = filepath.getAbsolutePath() + "/" + Utility.Edit_Folder_name + "/" + FileName;
        _url = _uri2;//used in share image
        Log.d("cache uri=", _uri);
        try {

            output = new FileOutputStream(file);

            // Compress into png format image from 0% - 100%
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, output);
            output.flush();
            output.close();
            new SingleMediaScanner(this,file);
            //finish();
            sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(new File(_uri))));
            Toast.makeText(getApplicationContext(), "Image Saved", Toast.LENGTH_LONG).show();

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private Bitmap getMainFrameBitmap() {
        imageFrame.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(imageFrame.getDrawingCache());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            bitmap.setConfig(Bitmap.Config.ARGB_8888);
        }
        imageFrame.setDrawingCacheEnabled(false);
        bitmap = CropBitmapTransparency(bitmap);
        return bitmap;
    }

    Bitmap CropBitmapTransparency(Bitmap sourceBitmap) {
        int minX = sourceBitmap.getWidth();
        int minY = sourceBitmap.getHeight();
        int maxX = -1;
        int maxY = -1;
        for (int y = 0; y < sourceBitmap.getHeight(); y++) {
            for (int x = 0; x < sourceBitmap.getWidth(); x++) {
                int alpha = (sourceBitmap.getPixel(x, y) >> 24) & 255;
                if (alpha > 0)   // pixel is not 100% transparent
                {
                    if (x < minX)
                        minX = x;
                    if (x > maxX)
                        maxX = x;
                    if (y < minY)
                        minY = y;
                    if (y > maxY)
                        maxY = y;
                }
            }
        }
        if ((maxX < minX) || (maxY < minY))
            return null; // Bitmap is entirely transparent

        // crop bitmap to non-transparent area and return:
        return Bitmap.createBitmap(sourceBitmap, minX, minY, (maxX - minX) + 1, (maxY - minY) + 1);
    }

    private void colordailog() {
        ColorPickerDialogBuilder
                .with(this)
                .initialColor(currentBackgroundColor)
                .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                .density(12)
                .setOnColorSelectedListener(new OnColorSelectedListener() {
                    @Override
                    public void onColorSelected(int selectedColor) {
                        // toast("onColorSelected: 0x" + Integer.toHexString(selectedColor));
                    }
                })
                .setPositiveButton("ok", new ColorPickerClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
                        mcolor = selectedColor;
                        mImageView.setPaintColor(mcolor);
                        if (allColors != null) {
                            StringBuilder sb = null;

                            for (Integer color : allColors) {
                                if (color == null)
                                    continue;
                                if (sb == null)
                                    sb = new StringBuilder("Color List:");
                                sb.append("\r\n#" + Integer.toHexString(color).toUpperCase());
                            }

                            if (sb != null) {
                            }
                            // Toast.makeText(getApplicationContext(), sb.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .showColorEdit(true)
                .setColorEditTextColor(getResources().getColor(R.color.colorPrimary))
                .build()
                .show();
    }

    private void setStickerList2() {
        setArraylistForSticker2();
        stikerAdaptor = new StickerAdapter(ImageEditActivity.this, stikerlist2);
        HLSticker.setAdapter(stikerAdaptor);
        HLSticker.setOnItemClickListener(new AdapterView.OnItemClickListener() {


            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                sticker = new StickerImageView(ImageEditActivity.this, onTouchSticker);
                stickerId = stikerlist2.get(position);
                sticker.setImageResource(stickerId);
                Random r = new Random();
                view_id = r.nextInt();
                if (view_id < 0) {
                    view_id = view_id - (view_id * 2);
                }
                sticker.setId(view_id);
                stickerviewId.add(view_id);

                sticker.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        sticker.setControlItemsHidden(false);
                    }
                });
                imageFrame.addView(sticker);
            }
        });
    }

    private void setStickerList1() {
        setArraylistForSticker1();
        stikerAdaptor = new StickerAdapter(ImageEditActivity.this, stikerlist1);
        HLSticker.setAdapter(stikerAdaptor);
        HLSticker.setOnItemClickListener(new AdapterView.OnItemClickListener() {


            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                sticker = new StickerImageView(ImageEditActivity.this, onTouchSticker);
                stickerId = stikerlist1.get(position);
                sticker.setImageResource(stickerId);
                Random r = new Random();
                view_id = r.nextInt();
                if (view_id < 0) {
                    view_id = view_id - (view_id * 2);
                }
                sticker.setId(view_id);
                stickerviewId.add(view_id);

                sticker.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        sticker.setControlItemsHidden(false);
                    }
                });
                imageFrame.addView(sticker);
            }
        });
    }

    private void setStickerList3() {
        setArraylistForSticker3();
        stikerAdaptor = new StickerAdapter(ImageEditActivity.this, stikerlist3);
        HLSticker.setAdapter(stikerAdaptor);
        HLSticker.setOnItemClickListener(new AdapterView.OnItemClickListener() {


            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                sticker = new StickerImageView(ImageEditActivity.this, onTouchSticker);
                stickerId = stikerlist3.get(position);
                sticker.setImageResource(stickerId);
                Random r = new Random();
                view_id = r.nextInt();
                if (view_id < 0) {
                    view_id = view_id - (view_id * 2);
                }
                sticker.setId(view_id);
                stickerviewId.add(view_id);

                sticker.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        sticker.setControlItemsHidden(false);
                    }
                });
                imageFrame.addView(sticker);
            }
        });
    }

    private void setStickerList4() {
        setArraylistForSticker4();
        stikerAdaptor = new StickerAdapter(ImageEditActivity.this, stikerlist4);
        HLSticker.setAdapter(stikerAdaptor);
        HLSticker.setOnItemClickListener(new AdapterView.OnItemClickListener() {


            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                sticker = new StickerImageView(ImageEditActivity.this, onTouchSticker);
                stickerId = stikerlist4.get(position);
                sticker.setImageResource(stickerId);
                Random r = new Random();
                view_id = r.nextInt();
                if (view_id < 0) {
                    view_id = view_id - (view_id * 2);
                }
                sticker.setId(view_id);
                stickerviewId.add(view_id);

                sticker.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        sticker.setControlItemsHidden(false);
                    }
                });
                imageFrame.addView(sticker);
            }
        });
    }

    private void setStickerList5() {
        setArraylistForSticker5();
        stikerAdaptor = new StickerAdapter(ImageEditActivity.this, stikerlist5);
        HLSticker.setAdapter(stikerAdaptor);
        HLSticker.setOnItemClickListener(new AdapterView.OnItemClickListener() {


            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                sticker = new StickerImageView(ImageEditActivity.this, onTouchSticker);
                stickerId = stikerlist5.get(position);
                sticker.setImageResource(stickerId);
                Random r = new Random();
                view_id = r.nextInt();
                if (view_id < 0) {
                    view_id = view_id - (view_id * 2);
                }
                sticker.setId(view_id);
                stickerviewId.add(view_id);

                sticker.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        sticker.setControlItemsHidden(false);
                    }
                });
                imageFrame.addView(sticker);
            }
        });
    }

    private void setStickerList6() {
        setArraylistForSticker6();
        stikerAdaptor = new StickerAdapter(ImageEditActivity.this, stikerlist6);
        HLSticker.setAdapter(stikerAdaptor);
        HLSticker.setOnItemClickListener(new AdapterView.OnItemClickListener() {


            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                sticker = new StickerImageView(ImageEditActivity.this, onTouchSticker);
                stickerId = stikerlist6.get(position);
                sticker.setImageResource(stickerId);
                Random r = new Random();
                view_id = r.nextInt();
                if (view_id < 0) {
                    view_id = view_id - (view_id * 2);
                }
                sticker.setId(view_id);
                stickerviewId.add(view_id);

                sticker.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        sticker.setControlItemsHidden(false);
                    }
                });
                imageFrame.addView(sticker);
            }
        });
    }

    private void setStickerList7() {
        setArraylistForSticker7();
        stikerAdaptor = new StickerAdapter(ImageEditActivity.this, stikerlist7);
        HLSticker.setAdapter(stikerAdaptor);
        HLSticker.setOnItemClickListener(new AdapterView.OnItemClickListener() {


            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                sticker = new StickerImageView(ImageEditActivity.this, onTouchSticker);
                stickerId = stikerlist7.get(position);
                sticker.setImageResource(stickerId);
                Random r = new Random();
                view_id = r.nextInt();
                if (view_id < 0) {
                    view_id = view_id - (view_id * 2);
                }
                sticker.setId(view_id);
                stickerviewId.add(view_id);

                sticker.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        sticker.setControlItemsHidden(false);
                    }
                });
                imageFrame.addView(sticker);
            }
        });
    }

    private void setStickerList8() {
        setArraylistForSticker8();
        stikerAdaptor = new StickerAdapter(ImageEditActivity.this, stikerlist8);
        HLSticker.setAdapter(stikerAdaptor);
        HLSticker.setOnItemClickListener(new AdapterView.OnItemClickListener() {


            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                sticker = new StickerImageView(ImageEditActivity.this, onTouchSticker);
                stickerId = stikerlist8.get(position);
                sticker.setImageResource(stickerId);
                Random r = new Random();
                view_id = r.nextInt();
                if (view_id < 0) {
                    view_id = view_id - (view_id * 2);
                }
                sticker.setId(view_id);
                stickerviewId.add(view_id);

                sticker.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        sticker.setControlItemsHidden(false);
                    }
                });
                imageFrame.addView(sticker);
            }
        });
    }

    private void AddMObBanner() {
        mAdView = (AdView) findViewById(R.id.adView);

        if (isOnline()) {
            mAdView.setVisibility(View.VISIBLE);
            AdRequest adRequest = new AdRequest.Builder().build();
            mAdView.loadAd(adRequest);
        } else {
            mAdView.setVisibility(View.GONE);
        }
    }

    private void setArraylistForSticker3() {
        stikerlist3 = new ArrayList<>();

        stikerlist3.add(R.drawable.sticker_type0301);
        stikerlist3.add(R.drawable.sticker_type0302);
        stikerlist3.add(R.drawable.sticker_type0303);
        stikerlist3.add(R.drawable.sticker_type0304);
        stikerlist3.add(R.drawable.sticker_type0305);
        stikerlist3.add(R.drawable.sticker_type0306);
        stikerlist3.add(R.drawable.sticker_type0307);
        stikerlist3.add(R.drawable.sticker_type0308);
        stikerlist3.add(R.drawable.sticker_type0309);
        stikerlist3.add(R.drawable.sticker_type0310);
        stikerlist3.add(R.drawable.sticker_type0311);
        stikerlist3.add(R.drawable.sticker_type0313);
        stikerlist3.add(R.drawable.sticker_type0314);
        stikerlist3.add(R.drawable.sticker_type0315);
        stikerlist3.add(R.drawable.sticker_type0316);
        stikerlist3.add(R.drawable.sticker_type0317);
        stikerlist3.add(R.drawable.sticker_type0318);
        stikerlist3.add(R.drawable.sticker_type0319);
        stikerlist3.add(R.drawable.sticker_type0320);
        stikerlist3.add(R.drawable.sticker_type0321);
        stikerlist3.add(R.drawable.sticker_type0322);

    }

    private void setArraylistForSticker1() {
        stikerlist1 = new ArrayList<>();

        stikerlist1.add(R.drawable.sticker_type01);
        stikerlist1.add(R.drawable.sticker_type02);
        stikerlist1.add(R.drawable.sticker_type03);
        stikerlist1.add(R.drawable.sticker_type04);
        stikerlist1.add(R.drawable.sticker_type05);
        stikerlist1.add(R.drawable.sticker_type06);
        stikerlist1.add(R.drawable.sticker_type07);
        stikerlist1.add(R.drawable.sticker_type08);
        stikerlist1.add(R.drawable.sticker_type09);
        stikerlist1.add(R.drawable.sticker_type10);
        stikerlist1.add(R.drawable.sticker_type11);
        stikerlist1.add(R.drawable.sticker_type12);
        stikerlist1.add(R.drawable.sticker_type13);
        stikerlist1.add(R.drawable.sticker_type14);
        stikerlist1.add(R.drawable.sticker_type15);
        stikerlist1.add(R.drawable.sticker_type16);
        stikerlist1.add(R.drawable.sticker_type17);
        stikerlist1.add(R.drawable.sticker_type18);
        stikerlist1.add(R.drawable.sticker_type19);
        stikerlist1.add(R.drawable.sticker_type20);
        stikerlist1.add(R.drawable.sticker_type21);
        stikerlist1.add(R.drawable.sticker_type22);
        stikerlist1.add(R.drawable.sticker_type23);
        stikerlist1.add(R.drawable.sticker_type24);
        stikerlist1.add(R.drawable.sticker_type25);
        stikerlist1.add(R.drawable.sticker_type26);
        stikerlist1.add(R.drawable.sticker_type27);
        stikerlist1.add(R.drawable.sticker_type28);
        stikerlist1.add(R.drawable.sticker_type29);
        stikerlist1.add(R.drawable.sticker_type30);
        stikerlist1.add(R.drawable.sticker_type31);
        stikerlist1.add(R.drawable.sticker_type32);
        stikerlist1.add(R.drawable.sticker_type33);
        stikerlist1.add(R.drawable.sticker_type34);
        stikerlist1.add(R.drawable.sticker_type35);
        stikerlist1.add(R.drawable.sticker_type36);
        stikerlist1.add(R.drawable.sticker_type37);
    }

    private void setArraylistForSticker2() {
        stikerlist2 = new ArrayList<>();

        stikerlist2.add(R.drawable.sticker_type0201);
        stikerlist2.add(R.drawable.sticker_type0202);
        stikerlist2.add(R.drawable.sticker_type0203);
        stikerlist2.add(R.drawable.sticker_type0204);
        stikerlist2.add(R.drawable.sticker_type0205);
        stikerlist2.add(R.drawable.sticker_type0206);
        stikerlist2.add(R.drawable.sticker_type0207);
        stikerlist2.add(R.drawable.sticker_type0208);
        stikerlist2.add(R.drawable.sticker_type0209);
        stikerlist2.add(R.drawable.sticker_type0210);
        stikerlist2.add(R.drawable.sticker_type0211);
        stikerlist2.add(R.drawable.sticker_type0212);
        stikerlist2.add(R.drawable.sticker_type0213);
        stikerlist2.add(R.drawable.sticker_type0214);
        stikerlist2.add(R.drawable.sticker_type0215);
        stikerlist2.add(R.drawable.sticker_type0216);
        stikerlist2.add(R.drawable.sticker_type0217);
        stikerlist2.add(R.drawable.sticker_type0218);
        stikerlist2.add(R.drawable.sticker_type0219);
        stikerlist2.add(R.drawable.sticker_type0220);
    }

    private void setArraylistForSticker4() {
        stikerlist4 = new ArrayList<>();

        stikerlist4.add(R.drawable.sticker_type0401);
        stikerlist4.add(R.drawable.sticker_type0402);
        stikerlist4.add(R.drawable.sticker_type0403);
        stikerlist4.add(R.drawable.sticker_type0404);
        stikerlist4.add(R.drawable.sticker_type0405);
        stikerlist4.add(R.drawable.sticker_type0406);
        stikerlist4.add(R.drawable.sticker_type0408);
        stikerlist4.add(R.drawable.sticker_type0409);
        stikerlist4.add(R.drawable.sticker_type0410);
        stikerlist4.add(R.drawable.sticker_type0411);
        stikerlist4.add(R.drawable.sticker_type0412);
        stikerlist4.add(R.drawable.sticker_type0413);
        stikerlist4.add(R.drawable.sticker_type0414);
        stikerlist4.add(R.drawable.sticker_type0415);
        stikerlist4.add(R.drawable.sticker_type0416);
        stikerlist4.add(R.drawable.sticker_type0417);
        stikerlist4.add(R.drawable.sticker_type0418);
        stikerlist4.add(R.drawable.sticker_type0419);
        stikerlist4.add(R.drawable.sticker_type0420);
        stikerlist4.add(R.drawable.sticker_type0421);
        stikerlist4.add(R.drawable.sticker_type0422);
        stikerlist4.add(R.drawable.sticker_type0423);
        stikerlist4.add(R.drawable.sticker_type0424);
        stikerlist4.add(R.drawable.sticker_type0425);

    }

    private void setArraylistForSticker5() {
        stikerlist5 = new ArrayList<>();

        stikerlist5.add(R.drawable.love_1);
        stikerlist5.add(R.drawable.love_2);
        stikerlist5.add(R.drawable.love_3);
        stikerlist5.add(R.drawable.love_4);
        stikerlist5.add(R.drawable.love_5);
        stikerlist5.add(R.drawable.love_6);
        stikerlist5.add(R.drawable.love_7);
        stikerlist5.add(R.drawable.love_8);
        stikerlist5.add(R.drawable.love_9);
        stikerlist5.add(R.drawable.love_10);
        stikerlist5.add(R.drawable.love_11);
        stikerlist5.add(R.drawable.love_12);
        stikerlist5.add(R.drawable.love_13);
        stikerlist5.add(R.drawable.love_14);
        stikerlist5.add(R.drawable.love_15);
        stikerlist5.add(R.drawable.love_16);
        stikerlist5.add(R.drawable.love_17);
        stikerlist5.add(R.drawable.love_18);
        stikerlist5.add(R.drawable.love_19);
        stikerlist5.add(R.drawable.love_20);
        stikerlist5.add(R.drawable.love_21);
        stikerlist5.add(R.drawable.love_22);
        stikerlist5.add(R.drawable.love_23);
        stikerlist5.add(R.drawable.love_24);
        stikerlist5.add(R.drawable.love_25);
        stikerlist5.add(R.drawable.love_26);
        stikerlist5.add(R.drawable.love_27);
        stikerlist5.add(R.drawable.love_28);
        stikerlist5.add(R.drawable.love_29);
        stikerlist5.add(R.drawable.love_30);
        stikerlist5.add(R.drawable.love_31);
        stikerlist5.add(R.drawable.love_32);
        stikerlist5.add(R.drawable.love_33);
        stikerlist5.add(R.drawable.love_34);
        stikerlist5.add(R.drawable.love_35);
        stikerlist5.add(R.drawable.love_36);
    }

    private void setArraylistForSticker6() {
        stikerlist6 = new ArrayList<>();

        stikerlist6.add(R.drawable.monster_01);
        stikerlist6.add(R.drawable.monster_02);
        stikerlist6.add(R.drawable.monster_03);
        stikerlist6.add(R.drawable.monster_04);
        stikerlist6.add(R.drawable.monster_05);
        stikerlist6.add(R.drawable.monster_06);
        stikerlist6.add(R.drawable.monster_07);
        stikerlist6.add(R.drawable.monster_08);
        stikerlist6.add(R.drawable.monster_09);
        stikerlist6.add(R.drawable.monster_10);
        stikerlist6.add(R.drawable.monster_11);
        stikerlist6.add(R.drawable.monster_12);
        stikerlist6.add(R.drawable.monster_13);
        stikerlist6.add(R.drawable.monster_14);
        stikerlist6.add(R.drawable.monster_15);
        stikerlist6.add(R.drawable.monster_16);
        stikerlist6.add(R.drawable.monster_17);
        stikerlist6.add(R.drawable.monster_18);
        stikerlist6.add(R.drawable.monster_19);
        stikerlist6.add(R.drawable.monster_20);
        stikerlist6.add(R.drawable.monster_21);
        stikerlist6.add(R.drawable.monster_22);
        stikerlist6.add(R.drawable.monster_23);
        stikerlist6.add(R.drawable.monster_24);
        stikerlist6.add(R.drawable.monster_25);
        stikerlist6.add(R.drawable.monster_26);
        stikerlist6.add(R.drawable.monster_27);
        stikerlist6.add(R.drawable.monster_28);
    }

    private void setArraylistForSticker7() {
        stikerlist7 = new ArrayList<>();

        stikerlist7.add(R.drawable.candy_01);
        stikerlist7.add(R.drawable.candy_02);
        stikerlist7.add(R.drawable.candy_03);
        stikerlist7.add(R.drawable.candy_04);
        stikerlist7.add(R.drawable.candy_05);
        stikerlist7.add(R.drawable.candy_06);
        stikerlist7.add(R.drawable.candy_07);
        stikerlist7.add(R.drawable.candy_08);
        stikerlist7.add(R.drawable.candy_09);
        stikerlist7.add(R.drawable.candy_10);
        stikerlist7.add(R.drawable.candy_11);
        stikerlist7.add(R.drawable.candy_12);
        stikerlist7.add(R.drawable.candy_13);
        stikerlist7.add(R.drawable.candy_14);
        stikerlist7.add(R.drawable.candy_15);
        stikerlist7.add(R.drawable.candy_16);
        stikerlist7.add(R.drawable.candy_17);
        stikerlist7.add(R.drawable.candy_18);
        stikerlist7.add(R.drawable.candy_19);
        stikerlist7.add(R.drawable.candy_20);
        stikerlist7.add(R.drawable.candy_21);

    }

    private void setArraylistForSticker8() {
        stikerlist8 = new ArrayList<>();

        stikerlist8.add(R.drawable.emoji_001);
        stikerlist8.add(R.drawable.emoji_002);
        stikerlist8.add(R.drawable.emoji_003);
        stikerlist8.add(R.drawable.emoji_004);
        stikerlist8.add(R.drawable.emoji_005);
        stikerlist8.add(R.drawable.emoji_006);
        stikerlist8.add(R.drawable.emoji_007);
        stikerlist8.add(R.drawable.emoji_008);
        stikerlist8.add(R.drawable.emoji_009);
        stikerlist8.add(R.drawable.emoji_010);
        stikerlist8.add(R.drawable.emoji_011);
        stikerlist8.add(R.drawable.emoji_012);
        stikerlist8.add(R.drawable.emoji_013);
        stikerlist8.add(R.drawable.emoji_014);
        stikerlist8.add(R.drawable.emoji_015);
        stikerlist8.add(R.drawable.emoji_016);
        stikerlist8.add(R.drawable.emoji_017);
        stikerlist8.add(R.drawable.emoji_018);
        stikerlist8.add(R.drawable.emoji_019);
        stikerlist8.add(R.drawable.emoji_020);
        stikerlist8.add(R.drawable.emoji_021);
        stikerlist8.add(R.drawable.emoji_022);
        stikerlist8.add(R.drawable.emoji_023);
        stikerlist8.add(R.drawable.emoji_024);
        stikerlist8.add(R.drawable.emoji_025);
        stikerlist8.add(R.drawable.emoji_026);
        stikerlist8.add(R.drawable.emoji_027);
        stikerlist8.add(R.drawable.emoji_028);
        stikerlist8.add(R.drawable.emoji_029);
        stikerlist8.add(R.drawable.emoji_030);
        stikerlist8.add(R.drawable.emoji_031);
        stikerlist8.add(R.drawable.emoji_032);
        stikerlist8.add(R.drawable.emoji_033);
        stikerlist8.add(R.drawable.emoji_034);
        stikerlist8.add(R.drawable.emoji_035);
        stikerlist8.add(R.drawable.emoji_036);
        stikerlist8.add(R.drawable.emoji_037);
        stikerlist8.add(R.drawable.emoji_038);
        stikerlist8.add(R.drawable.emoji_039);
        stikerlist8.add(R.drawable.emoji_040);
        stikerlist8.add(R.drawable.emoji_041);
        stikerlist8.add(R.drawable.emoji_042);
        stikerlist8.add(R.drawable.emoji_043);
        stikerlist8.add(R.drawable.emoji_044);
        stikerlist8.add(R.drawable.emoji_045);
        stikerlist8.add(R.drawable.emoji_046);
        stikerlist8.add(R.drawable.emoji_047);
        stikerlist8.add(R.drawable.emoji_048);
        stikerlist8.add(R.drawable.emoji_049);
    }

    private void setOverLayList() {
        setArraylistForOverLy();
        overlayAdaptor = new Overlyadapter(ImageEditActivity.this, Overlylist);
        Grid_OverLay.setAdapter(overlayAdaptor);
        Grid_OverLay.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                overlayid = Overlylist.get(position).getFrmId();
                overlayImage.setImageResource(overlayid);
                Grid_OverLay.setVisibility(View.GONE);
            }
        });
    }

    private void setArraylistForOverLy() {
        Overlylist = new ArrayList<>();
        Overlylist.add(new Modal(R.drawable.e0, R.drawable.trans));
        Overlylist.add(new Modal(R.drawable.shape01_thum, R.drawable.shape_01));
        Overlylist.add(new Modal(R.drawable.shape02_thum, R.drawable.shape_02));
        Overlylist.add(new Modal(R.drawable.shape03_thum, R.drawable.shape_03));
        Overlylist.add(new Modal(R.drawable.shape04_thum, R.drawable.shape_04));
        Overlylist.add(new Modal(R.drawable.shape05_thum, R.drawable.shape_05));
        Overlylist.add(new Modal(R.drawable.shape06_thum, R.drawable.shape_06));
        Overlylist.add(new Modal(R.drawable.shape07_thum, R.drawable.shape_07));
        Overlylist.add(new Modal(R.drawable.shape08_thum, R.drawable.shape_08));
        Overlylist.add(new Modal(R.drawable.shape09_thum, R.drawable.shape_09));
        Overlylist.add(new Modal(R.drawable.shape10_thum, R.drawable.shape_10));
        Overlylist.add(new Modal(R.drawable.shape12_thum, R.drawable.shape_12));
        Overlylist.add(new Modal(R.drawable.shape15_thum, R.drawable.shape_15));
        Overlylist.add(new Modal(R.drawable.shape16_thum, R.drawable.shape_16));
        Overlylist.add(new Modal(R.drawable.shape17_thum, R.drawable.shape_17));
        Overlylist.add(new Modal(R.drawable.shape19_thum, R.drawable.shape_19));
        Overlylist.add(new Modal(R.drawable.shape21_thum, R.drawable.shape_21));
        Overlylist.add(new Modal(R.drawable.shape22_thum, R.drawable.shape_22));
        Overlylist.add(new Modal(R.drawable.shape23_thum, R.drawable.shape_23));
        Overlylist.add(new Modal(R.drawable.shape28_thum, R.drawable.shape_28));
        Overlylist.add(new Modal(R.drawable.shape29_thum, R.drawable.shape_29));
        Overlylist.add(new Modal(R.drawable.shape30_thum, R.drawable.shape_30));
        Overlylist.add(new Modal(R.drawable.shape31_thum, R.drawable.shape_31));
        Overlylist.add(new Modal(R.drawable.shape32_thum, R.drawable.shape_32));
        Overlylist.add(new Modal(R.drawable.shape34_thum, R.drawable.shape_34));
        Overlylist.add(new Modal(R.drawable.shape35_thum, R.drawable.shape_35));
        Overlylist.add(new Modal(R.drawable.shape36_thum, R.drawable.shape_36));
        Overlylist.add(new Modal(R.drawable.shape37_thum, R.drawable.shape_37));
        Overlylist.add(new Modal(R.drawable.shape39_thum, R.drawable.shape_39));
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQ_TEXT) {
                addtext();
            }
        }
    }

    private void addtext() {
        final StickerTextiimageView textSticker = new StickerTextiimageView(ImageEditActivity.this, onTouchSticker1);
        Bitmap textstickerId = TextDemoActivity.finalBitmapText;
        textSticker.setImageBitmap(textstickerId);
        Random r = new Random();
        int view_id = r.nextInt();
        if (view_id < 0) {
            view_id = view_id - (view_id * 2);
        }
        textSticker.setId(view_id);
        stickerviewId.add(view_id);
        textSticker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textSticker.setControlItemsHidden(false);
            }
        });
        imageFrame.addView(textSticker);
    }

    private void removeBorder() {
        for (int i = 0; i < stickerviewId.size(); i++) {
            View view = imageFrame.findViewById(stickerviewId.get(i));
            if (view instanceof StickerImageView) {
                StickerImageView stickerView = (StickerImageView) view;
                stickerView.setControlItemsHidden(true);
            }
            if (view instanceof StickerTextiimageView) {
                StickerTextiimageView stickerView = (StickerTextiimageView) view;
                stickerView.setControlItemsHidden(true);
            }
        }
    }

    @Override
    public void onProgressChanged(DiscreteSeekBar seekBar, int progress, boolean fromUser) {
        if (!fromUser) return;

        if (seekBar.getId() == mSeekBar1.getId()) {
            int value = progress * 2 - 100;
            mImageView.setVignetteIntensity(value);
        } else {
            mImageView.setVignetteFeather((float) progress / 100);
        }
    }

    @Override
    public void onStartTrackingTouch(DiscreteSeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(DiscreteSeekBar seekBar) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        mProgress.setVisibility(View.GONE);
        iv_save.setVisibility(View.VISIBLE);
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


    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }



}


