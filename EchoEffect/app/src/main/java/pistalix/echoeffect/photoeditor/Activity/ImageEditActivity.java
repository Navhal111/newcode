package pistalix.echoeffect.photoeditor.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import pistalix.echoeffect.photoeditor.MyTouch.MultiTouchListener;
import pistalix.echoeffect.photoeditor.R;
import pistalix.echoeffect.photoeditor.View.Glob;

public class ImageEditActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView imagef1_1, imagef1_2, imagef1_3, imagef1_4, imagef1_5, imagef1_6;
    public static Bitmap Edit_bit;
    private int width;
    private int hight;
    private int d_hight;
    private int d_width;
    private Bitmap bit;
    private ImageView close_zoom_pan;
    private Bitmap bit1;
    private ImageView background;
    private RelativeLayout ZoomPan_Layout, rootRelative;
    private LinearLayout backgrounds;
    private LinearLayout backgrond_button;
    private LinearLayout flip, eraser;
    private LinearLayout Frames_Scroll;
    private LinearLayout mirror, save;
    private RelativeLayout First_Frame, Second_Frame, Third_Frame, Fourth_Frame, Five_Frame, Sixth_Frame, Seven_Frame, Eight_Frame, Nine_Frame, Ten_Frame, Leven_Frame, Twelve_Frame;
    private RelativeLayout bottomitem;
    private ImageView iv_bg, iv_mirror, iv_flip, iv_erase, iv_save;
    private TextView ttbg, tvmirror, ttflip, tterase, ttsave;
    private InputStream inputStream;
    private ImageView imagef2_6, imagef2_5, imagef2_4, imagef2_3, imagef2_2, imagef2_1;
    private ImageView imagef3_5, imagef3_4, imagef3_2, imagef3_3, imagef3_1;
    private ImageView imagef4_5, imagef4_4, imagef4_3, imagef4_2, imagef4_1;
    private ImageView imagef5_6, imagef5_5, imagef5_4, imagef5_3, imagef5_2, imagef5_1, imagef5_9, imagef5_8, imagef5_7;
    private ImageView imagef6_3, imagef6_2, imagef6_1;
    private ImageView imagef7_1, imagef7_2, imagef7_3;
    private ImageView imagef8_6, imagef8_5, imagef8_4, imagef8_3, imagef8_2, imagef8_1, imagef8_9, imagef8_8, imagef8_7;
    private ImageView imagef9_1, imagef9_6, imagef9_5, imagef9_4, imagef9_3, imagef9_2;
    private ImageView imagef10_1, imagef10_6, imagef10_5, imagef10_4, imagef10_3, imagef10_2;
    private ImageView imagef11_1, imagef11_6, imagef11_5, imagef11_4, imagef11_3, imagef11_2, imagef11_9, imagef11_8, imagef11_7;
    private ImageView imagef12_6, imagef12_7, imagef12_5, imagef12_4, imagef12_3, imagef12_2, imagef12_1, imagef12_9, imagef12_8;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_edit);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Edit_bit = Glob.bitmap;
        Bind();
        width = Edit_bit.getWidth();
        hight = Edit_bit.getHeight();
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        d_width = displayMetrics.widthPixels;
        d_hight = displayMetrics.heightPixels;
        bit = bitmap();
        bit1 = prescale(bit);
        setImage();

    }

    private Bitmap prescale(Bitmap bitmap) {
        Matrix matrix = new Matrix();
        matrix.preScale(-1.0f, 1.0f);
        Bitmap createBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, false);
        createBitmap.setDensity(160);
        return createBitmap;

    }

    private void setImage() {
        imagef1_1.setImageBitmap(bit);
        imagef1_2.setImageBitmap(bit);
        imagef1_3.setImageBitmap(bit);
        imagef1_4.setImageBitmap(bit);
        imagef1_5.setImageBitmap(bit);
        imagef1_6.setImageBitmap(bit);
        imagef2_1.setImageBitmap(bit);
        imagef2_2.setImageBitmap(bit);
        imagef2_3.setImageBitmap(bit);
        imagef2_4.setImageBitmap(bit);
        imagef2_5.setImageBitmap(bit);
        imagef2_6.setImageBitmap(bit);
        imagef3_1.setImageBitmap(bit);
        imagef3_2.setImageBitmap(bit);
        imagef3_3.setImageBitmap(bit);
        imagef3_4.setImageBitmap(bit);
        imagef3_5.setImageBitmap(bit);
        imagef4_1.setImageBitmap(bit);
        imagef4_2.setImageBitmap(bit);
        imagef4_3.setImageBitmap(bit);
        imagef4_4.setImageBitmap(bit);
        imagef4_5.setImageBitmap(bit);
        imagef5_1.setImageBitmap(bit);
        imagef5_2.setImageBitmap(bit);
        imagef5_3.setImageBitmap(bit);
        imagef5_4.setImageBitmap(bit);
        imagef5_5.setImageBitmap(bit);
        imagef5_6.setImageBitmap(bit);
        imagef5_7.setImageBitmap(bit);
        imagef5_8.setImageBitmap(bit);
        imagef5_9.setImageBitmap(bit);
        imagef6_1.setImageBitmap(bit);
        imagef6_2.setImageBitmap(bit);
        imagef6_3.setImageBitmap(bit);
        imagef7_1.setImageBitmap(bit);
        imagef7_2.setImageBitmap(bit);
        imagef7_3.setImageBitmap(bit);
        imagef8_1.setImageBitmap(bit);
        imagef8_2.setImageBitmap(bit);
        imagef8_3.setImageBitmap(bit);
        imagef8_4.setImageBitmap(bit);
        imagef8_5.setImageBitmap(bit);
        imagef8_6.setImageBitmap(bit);
        imagef8_7.setImageBitmap(bit);
        imagef8_8.setImageBitmap(bit);
        imagef8_9.setImageBitmap(bit);
        imagef9_1.setImageBitmap(bit);
        imagef9_2.setImageBitmap(bit);
        imagef9_3.setImageBitmap(bit);
        imagef9_4.setImageBitmap(bit1);
        imagef9_5.setImageBitmap(bit1);
        imagef9_6.setImageBitmap(bit1);
        imagef10_1.setImageBitmap(bit);
        imagef10_2.setImageBitmap(bit);
        imagef10_3.setImageBitmap(bit);
        imagef10_4.setImageBitmap(bit1);
        imagef10_5.setImageBitmap(bit1);
        imagef10_6.setImageBitmap(bit1);
        imagef11_1.setImageBitmap(bit);
        imagef11_2.setImageBitmap(bit);
        imagef11_3.setImageBitmap(bit);
        imagef11_4.setImageBitmap(bit);
        imagef11_5.setImageBitmap(bit);
        imagef11_6.setImageBitmap(bit);
        imagef11_7.setImageBitmap(bit);
        imagef11_8.setImageBitmap(bit);
        imagef11_9.setImageBitmap(bit);
        imagef12_1.setImageBitmap(bit);
        imagef12_2.setImageBitmap(bit);
        imagef12_3.setImageBitmap(bit);
        imagef12_4.setImageBitmap(bit);
        imagef12_5.setImageBitmap(bit);
        imagef12_6.setImageBitmap(bit);
        imagef12_7.setImageBitmap(bit);
        imagef12_8.setImageBitmap(bit);
        imagef12_9.setImageBitmap(bit);

        imagef1_2.setAlpha(240);
        imagef1_3.setAlpha(210);
        imagef1_4.setAlpha(167);
        imagef1_5.setAlpha(127);
        imagef1_6.setAlpha(127);
        imagef2_2.setAlpha(240);
        imagef2_3.setAlpha(210);
        imagef2_4.setAlpha(167);
        imagef2_5.setAlpha(127);
        imagef2_6.setAlpha(120);
        imagef3_2.setAlpha(240);
        imagef3_3.setAlpha(210);
        imagef3_4.setAlpha(167);
        imagef3_5.setAlpha(127);
        imagef4_2.setAlpha(240);
        imagef4_3.setAlpha(210);
        imagef4_4.setAlpha(127);
        imagef4_5.setAlpha(100);
        imagef5_1.setAlpha(127);
        imagef5_2.setAlpha(180);
        imagef5_3.setAlpha(180);
        imagef5_4.setAlpha(210);
        imagef5_5.setAlpha(210);
        imagef5_6.setAlpha(240);
        imagef8_4.setAlpha(220);
        imagef8_5.setAlpha(220);
        imagef8_6.setAlpha(180);
        imagef8_7.setAlpha(180);
        imagef8_8.setAlpha(127);
        imagef8_9.setAlpha(127);


    }

    private Bitmap bitmap() {
        System.out.println();
        Bitmap createBitmap = Bitmap.createBitmap((int) (((double) width) * 0.8d), (int) (((double) hight) * 0.7d), Bitmap.Config.ARGB_8888);
        new Canvas(createBitmap).drawBitmap(Edit_bit, null, new Rect(0, 0, (int) (((double) width) * 0.9d), (int) (((double) hight) * 0.9d)), new Paint());
        return createBitmap;
    }

    private void Bind() {
        close_zoom_pan = (ImageView) findViewById(R.id.close_zoom_pan);
        close_zoom_pan.setOnClickListener(this);
        background = (ImageView) findViewById(R.id.background);
        background.setImageResource(R.drawable.background_1);
        rootRelative = (RelativeLayout) findViewById(R.id.rootRelative);
        ZoomPan_Layout = (RelativeLayout) findViewById(R.id.ZoomPan_Layout);
        ZoomPan_Layout.setOnTouchListener(new MultiTouchListener());
        backgrounds = (LinearLayout) findViewById(R.id.backgrounds);
        backgrounds.setVisibility(4);
        Frames_Scroll = (LinearLayout) findViewById(R.id.Frames_Scroll);
        Frames_Scroll.setVisibility(4);
        eraser = (LinearLayout) findViewById(R.id.eraser);
        eraser.setOnClickListener(this);
        flip = (LinearLayout) findViewById(R.id.flip);
        flip.setOnClickListener(this);
        bottomitem = (RelativeLayout) findViewById(R.id.bottomitem);
        backgrond_button = (LinearLayout) findViewById(R.id.backgrond_button);
        backgrond_button.setOnClickListener(this);
        mirror = (LinearLayout) findViewById(R.id.mirror);
        mirror.setOnClickListener(this);
        save = (LinearLayout) findViewById(R.id.save);
        save.setOnClickListener(this);
        callmenu();
        First_Frame = (RelativeLayout) findViewById(R.id.First_Frame);
        Second_Frame = (RelativeLayout) findViewById(R.id.Second_Frame);
        Third_Frame = (RelativeLayout) findViewById(R.id.Third_Frame);
        Fourth_Frame = (RelativeLayout) findViewById(R.id.Fourth_Frame);
        Five_Frame = (RelativeLayout) findViewById(R.id.Five_Frame);
        Sixth_Frame = (RelativeLayout) findViewById(R.id.Sixth_Frame);
        Seven_Frame = (RelativeLayout) findViewById(R.id.Seven_Frame);
        Eight_Frame = (RelativeLayout) findViewById(R.id.Eight_Frame);
        Nine_Frame = (RelativeLayout) findViewById(R.id.Nine_Frame);
        Ten_Frame = (RelativeLayout) findViewById(R.id.Ten_Frame);
        Leven_Frame = (RelativeLayout) findViewById(R.id.Leven_Frame);
        Twelve_Frame = (RelativeLayout) findViewById(R.id.Twelve_Frame);

        First_Frame.setVisibility(0);
        Second_Frame.setVisibility(4);
        Third_Frame.setVisibility(4);
        Fourth_Frame.setVisibility(4);
        Five_Frame.setVisibility(4);
        Sixth_Frame.setVisibility(4);
        Seven_Frame.setVisibility(4);
        Eight_Frame.setVisibility(4);
        Nine_Frame.setVisibility(4);
        Ten_Frame.setVisibility(4);
        Leven_Frame.setVisibility(4);
        Twelve_Frame.setVisibility(4);

        imagef1_1 = (ImageView) findViewById(R.id.imagef1_1);
        imagef1_2 = (ImageView) findViewById(R.id.imagef1_2);
        imagef1_3 = (ImageView) findViewById(R.id.imagef1_3);
        imagef1_4 = (ImageView) findViewById(R.id.imagef1_4);
        imagef1_5 = (ImageView) findViewById(R.id.imagef1_5);
        imagef1_6 = (ImageView) findViewById(R.id.imagef1_6);

        imagef2_1 = (ImageView) findViewById(R.id.imagef2_1);
        imagef2_2 = (ImageView) findViewById(R.id.imagef2_2);
        imagef2_3 = (ImageView) findViewById(R.id.imagef2_3);
        imagef2_4 = (ImageView) findViewById(R.id.imagef2_4);
        imagef2_5 = (ImageView) findViewById(R.id.imagef2_5);
        imagef2_6 = (ImageView) findViewById(R.id.imagef2_6);

        imagef3_1 = (ImageView) findViewById(R.id.imagef3_1);
        imagef3_2 = (ImageView) findViewById(R.id.imagef3_2);
        imagef3_3 = (ImageView) findViewById(R.id.imagef3_3);
        imagef3_4 = (ImageView) findViewById(R.id.imagef3_4);
        imagef3_5 = (ImageView) findViewById(R.id.imagef3_5);

        imagef4_1 = (ImageView) findViewById(R.id.imagef4_1);
        imagef4_2 = (ImageView) findViewById(R.id.imagef4_2);
        imagef4_3 = (ImageView) findViewById(R.id.imagef4_3);
        imagef4_4 = (ImageView) findViewById(R.id.imagef4_4);
        imagef4_5 = (ImageView) findViewById(R.id.imagef4_5);

        imagef5_1 = (ImageView) findViewById(R.id.imagef5_1);
        imagef5_2 = (ImageView) findViewById(R.id.imagef5_2);
        imagef5_3 = (ImageView) findViewById(R.id.imagef5_3);
        imagef5_4 = (ImageView) findViewById(R.id.imagef5_4);
        imagef5_5 = (ImageView) findViewById(R.id.imagef5_5);
        imagef5_6 = (ImageView) findViewById(R.id.imagef5_6);
        imagef5_7 = (ImageView) findViewById(R.id.imagef5_7);
        imagef5_8 = (ImageView) findViewById(R.id.imagef5_8);
        imagef5_9 = (ImageView) findViewById(R.id.imagef5_9);

        imagef6_1 = (ImageView) findViewById(R.id.imagef6_1);
        imagef6_2 = (ImageView) findViewById(R.id.imagef6_2);
        imagef6_3 = (ImageView) findViewById(R.id.imagef6_3);

        imagef7_1 = (ImageView) findViewById(R.id.imagef7_1);
        imagef7_2 = (ImageView) findViewById(R.id.imagef7_2);
        imagef7_3 = (ImageView) findViewById(R.id.imagef7_3);

        imagef8_1 = (ImageView) findViewById(R.id.imagef8_1);
        imagef8_2 = (ImageView) findViewById(R.id.imagef8_2);
        imagef8_3 = (ImageView) findViewById(R.id.imagef8_3);
        imagef8_4 = (ImageView) findViewById(R.id.imagef8_4);
        imagef8_5 = (ImageView) findViewById(R.id.imagef8_5);
        imagef8_6 = (ImageView) findViewById(R.id.imagef8_6);
        imagef8_7 = (ImageView) findViewById(R.id.imagef8_7);
        imagef8_8 = (ImageView) findViewById(R.id.imagef8_8);
        imagef8_9 = (ImageView) findViewById(R.id.imagef8_9);

        imagef9_1 = (ImageView) findViewById(R.id.imagef9_1);
        imagef9_2 = (ImageView) findViewById(R.id.imagef9_2);
        imagef9_3 = (ImageView) findViewById(R.id.imagef9_3);
        imagef9_4 = (ImageView) findViewById(R.id.imagef9_4);
        imagef9_5 = (ImageView) findViewById(R.id.imagef9_5);
        imagef9_6 = (ImageView) findViewById(R.id.imagef9_6);

        imagef10_1 = (ImageView) findViewById(R.id.imagef10_1);
        imagef10_2 = (ImageView) findViewById(R.id.imagef10_2);
        imagef10_3 = (ImageView) findViewById(R.id.imagef10_3);
        imagef10_4 = (ImageView) findViewById(R.id.imagef10_4);
        imagef10_5 = (ImageView) findViewById(R.id.imagef10_5);
        imagef10_6 = (ImageView) findViewById(R.id.imagef10_6);

        imagef11_1 = (ImageView) findViewById(R.id.imagef11_1);
        imagef11_2 = (ImageView) findViewById(R.id.imagef11_2);
        imagef11_3 = (ImageView) findViewById(R.id.imagef11_3);
        imagef11_4 = (ImageView) findViewById(R.id.imagef11_4);
        imagef11_5 = (ImageView) findViewById(R.id.imagef11_5);
        imagef11_6 = (ImageView) findViewById(R.id.imagef11_6);
        imagef11_7 = (ImageView) findViewById(R.id.imagef11_7);
        imagef11_8 = (ImageView) findViewById(R.id.imagef11_8);
        imagef11_9 = (ImageView) findViewById(R.id.imagef11_9);

        imagef12_1 = (ImageView) findViewById(R.id.imagef12_1);
        imagef12_2 = (ImageView) findViewById(R.id.imagef12_2);
        imagef12_3 = (ImageView) findViewById(R.id.imagef12_3);
        imagef12_4 = (ImageView) findViewById(R.id.imagef12_4);
        imagef12_5 = (ImageView) findViewById(R.id.imagef12_5);
        imagef12_6 = (ImageView) findViewById(R.id.imagef12_6);
        imagef12_7 = (ImageView) findViewById(R.id.imagef12_7);
        imagef12_8 = (ImageView) findViewById(R.id.imagef12_8);
        imagef12_9 = (ImageView) findViewById(R.id.imagef12_9);

    }

    private void callmenu() {
        iv_bg = (ImageView) findViewById(R.id.iv_bg);
        ttbg = (TextView) findViewById(R.id.ttbg);
        iv_mirror = (ImageView) findViewById(R.id.iv_mirror);
        tvmirror = (TextView) findViewById(R.id.tvmirror);
        iv_flip = (ImageView) findViewById(R.id.iv_flip);
        ttflip = (TextView) findViewById(R.id.ttflip);
        iv_erase = (ImageView) findViewById(R.id.iv_erase);
        tterase = (TextView) findViewById(R.id.tterase);
        iv_save = (ImageView) findViewById(R.id.iv_save);
        ttsave = (TextView) findViewById(R.id.ttsave);
    }

    private void callcolor() {
        iv_bg.setColorFilter(getResources().getColor(R.color.white));
        ttbg.setTextColor(getResources().getColor(R.color.white));
        iv_mirror.setColorFilter(getResources().getColor(R.color.white));
        tvmirror.setTextColor(getResources().getColor(R.color.white));
        iv_flip.setColorFilter(getResources().getColor(R.color.white));
        ttflip.setTextColor(getResources().getColor(R.color.white));
        iv_erase.setColorFilter(getResources().getColor(R.color.white));
        tterase.setTextColor(getResources().getColor(R.color.white));
        iv_save.setColorFilter(getResources().getColor(R.color.white));
        ttsave.setTextColor(getResources().getColor(R.color.white));
    }

    @Override
    public void onClick(View v) {
        Animation translateAnimation = new TranslateAnimation(0.0f, 0.0f, 200.0f, 0.0f);
        switch (v.getId()) {
            case R.id.close_zoom_pan:
                close_zoom_pan.setVisibility(4);
                return;
            case R.id.save:
                callcolor();
                iv_save.setColorFilter(getResources().getColor(R.color.custom_main));
                ttsave.setTextColor(getResources().getColor(R.color.custom_main));
                callvisibility();
                create_Save_Image();
                return;
            case R.id.backgrond_button:
                callcolor();
                iv_bg.setColorFilter(getResources().getColor(R.color.custom_main));
                ttbg.setTextColor(getResources().getColor(R.color.custom_main));
                callvisibility();
                if (backgrounds.getVisibility() == 4) {
                    backgrounds.setVisibility(0);
                    translateAnimation.setDuration(500);
                    translateAnimation.setFillAfter(true);
                    bottomitem.startAnimation(translateAnimation);
                } else {
                    backgrounds.setVisibility(4);
                }

                return;
            case R.id.mirror:
                callcolor();
                iv_mirror.setColorFilter(getResources().getColor(R.color.custom_main));
                tvmirror.setTextColor(getResources().getColor(R.color.custom_main));
                callvisibility();
                if (Frames_Scroll.getVisibility() == 4) {
                    Frames_Scroll.setVisibility(0);
                    translateAnimation.setDuration(500);
                    translateAnimation.setFillAfter(true);
                    bottomitem.startAnimation(translateAnimation);
                } else {
                    Frames_Scroll.setVisibility(4);
                }
                return;
            case R.id.flip:
                callcolor();
                iv_flip.setColorFilter(getResources().getColor(R.color.custom_main));
                ttflip.setTextColor(getResources().getColor(R.color.custom_main));
                callvisibility();
                bit = prescale(bit);
                bit1 = prescale(bit1);
                setImage();
                return;
            case R.id.eraser:
                callcolor();
                iv_erase.setColorFilter(getResources().getColor(R.color.custom_main));
                tterase.setTextColor(getResources().getColor(R.color.custom_main));
                callvisibility();
                startActivityForResult(new Intent(this, EraseActivity.class), 1);
                return;
        }
    }

    private void create_Save_Image() {
        Glob.finalBitmap = getbitmap(rootRelative);
        saveImage(Glob.finalBitmap);
        startActivity(new Intent(this, SaveShare.class));
    }

    private void saveImage(Bitmap bitmap2) {
        Log.v("TAG", "saveImageInCache is called");
        Bitmap bitmap;
        OutputStream output;

        // Retrieve the image from the res folder
        bitmap = bitmap2;

        File filepath = Environment.getExternalStorageDirectory();
        // Create a new folder in SD Card
        File dir = new File(filepath.getAbsolutePath() + "/" + Glob.Edit_Folder_name);
        dir.mkdirs();

        // Create a name for the saved image
        String ts = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String FileName = ts + ".jpeg";
        File file = new File(dir, FileName);
        file.renameTo(file);
        String _uri = "file://" + filepath.getAbsolutePath() + "/" + Glob.Edit_Folder_name + "/" + FileName;
        //for share image
        String _uri2 = filepath.getAbsolutePath() + "/" + Glob.Edit_Folder_name + "/" + FileName;
        Glob.shareuri = _uri2;//used in share image
        Log.d("cache uri=", _uri);
        try {

            output = new FileOutputStream(file);

            // Compress into png format image from 0% - 100%
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, output);
            output.flush();
            output.close();
            //finish();
            new SingleMediaScanner(this,file);
            sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(new File(_uri))));

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private Bitmap getbitmap(View view) {
        Bitmap createBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        view.draw(new Canvas(createBitmap));
        return createBitmap;

    }

    private void callvisibility() {
        Frames_Scroll.setVisibility(4);
        backgrounds.setVisibility(4);
    }

    public void openGallery(View view) {
        Intent intent = new Intent("android.intent.action.PICK");
        intent.setDataAndType(Uri.parse(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getPath()), "image/*");
        startActivityForResult(intent, 2);
    }

    public void OnClickFrames(View view) {
        switch (view.getId()) {
            case R.id.frame1:
                FrameVisibility();
                First_Frame.setVisibility(0);
                return;
            case R.id.frame2:
                FrameVisibility();
                Second_Frame.setVisibility(0);
                return;
            case R.id.frame3:
                FrameVisibility();
                Third_Frame.setVisibility(0);
                return;
            case R.id.frame4:
                FrameVisibility();
                Fourth_Frame.setVisibility(0);
                return;
            case R.id.frame5:
                FrameVisibility();
                Five_Frame.setVisibility(0);
                return;
            case R.id.frame6:
                FrameVisibility();
                Sixth_Frame.setVisibility(0);
                return;
            case R.id.frame7:
                FrameVisibility();
                Seven_Frame.setVisibility(0);
                return;
            case R.id.frame8:
                FrameVisibility();
                Eight_Frame.setVisibility(0);
                return;
            case R.id.frame9:
                FrameVisibility();
                Nine_Frame.setVisibility(0);
                return;
            case R.id.frame10:
                FrameVisibility();
                Ten_Frame.setVisibility(0);
                return;
            case R.id.frame11:
                FrameVisibility();
                Leven_Frame.setVisibility(0);
                return;
            case R.id.frame12:
                FrameVisibility();
                Twelve_Frame.setVisibility(0);
                return;
        }
    }

    private void FrameVisibility() {
        First_Frame.setVisibility(4);
        Second_Frame.setVisibility(4);
        Third_Frame.setVisibility(4);
        Fourth_Frame.setVisibility(4);
        Five_Frame.setVisibility(4);
        Sixth_Frame.setVisibility(4);
        Seven_Frame.setVisibility(4);
        Eight_Frame.setVisibility(4);
        Nine_Frame.setVisibility(4);
        Ten_Frame.setVisibility(4);
        Leven_Frame.setVisibility(4);
        Twelve_Frame.setVisibility(4);
    }


    public void SetBackground(View view) {
        switch (view.getId()) {
            case R.id.backgrond1:
                background.setImageResource(R.drawable.background_1);
                return;
            case R.id.backgrond2:
                background.setImageResource(R.drawable.background_2);
                return;
            case R.id.backgrond3:
                background.setImageResource(R.drawable.background_3);
                return;
            case R.id.backgrond4:
                background.setImageResource(R.drawable.background_4);
                return;
            case R.id.backgrond5:
                background.setImageResource(R.drawable.background_5);
                return;
            case R.id.backgrond6:
                background.setImageResource(R.drawable.background_6);
                return;
            case R.id.backgrond7:
                background.setImageResource(R.drawable.background_7);
                return;
            case R.id.backgrond8:
                background.setImageResource(R.drawable.background_8);
                return;
            case R.id.backgrond9:
                background.setImageResource(R.drawable.background_9);
                return;
            case R.id.backgrond10:
                background.setImageResource(R.drawable.background_10);
                return;
            case R.id.backgrond11:
                background.setImageResource(R.drawable.background_11);
                return;
            case R.id.backgrond12:
                background.setImageResource(R.drawable.background_12);
                return;
            case R.id.backgrond13:
                background.setImageResource(R.drawable.background_13);
                return;
            case R.id.backgrond14:
                background.setImageResource(R.drawable.background_14);
                return;
            case R.id.backgrond15:
                background.setImageResource(R.drawable.background_15);
                return;
            default:
                return;
        }
    }

    protected void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        switch (i) {
            case 1:
                bit = bitmap();
                bit1 = prescale(bit);
                setImage();
                return;
            case 2:
                try {
                    inputStream = getContentResolver().openInputStream(intent.getData());
                } catch (FileNotFoundException e2) {
                    e2.printStackTrace();
                }
                Bitmap decodeStream = BitmapFactory.decodeStream(inputStream);
                int width = decodeStream.getWidth();
                int height = decodeStream.getHeight();
                if (width > height) {
                    while (true) {
                        if (width > this.d_width || height > this.d_hight) {
                            width = (int) (((double) width) * 0.9d);
                            height = (int) (((double) height) * 0.9d);
                        } else {
                            background.setImageBitmap(Bitmap.createScaledBitmap(decodeStream, (width / width) * this.d_width, (height / height) * this.hight, true));
                            return;
                        }
                    }
                }
                Toast.makeText(this, "Incompatable Image !!! Width should be greater that height.", 0).show();

                return;
        }
    }



}
