package pistalix.dslrcamera.dslrcamerahd;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;


import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;

import java.lang.reflect.Array;

public class ImageDSLRActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView iv_show_image;
    private Bitmap bitmap;
    private DiscreteSeekBar blurseekBar;
    private Bitmap bitmap1 = null;
    private ImageView iv_blur;
    private Bitmap bm2 = null;
    private int progress = 2;
    private int angle = 0;
    private ImageView iv_Gallary;
    private static final int RE_GALLERY = 1;
    public static Uri imageUri;
    private ImageView ic_done;
    int itemselect;
    private ImageView back;

    private ImageView rotate;
    private int rorate = 1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_dslr);


        iv_show_image = (ImageView) findViewById(R.id.iv_show_image);
        blurseekBar = (DiscreteSeekBar) findViewById(R.id.blurseekBar);
        back = (ImageView) findViewById(R.id.fback);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();


            }
        });
        iv_blur = (ImageView) findViewById(R.id.iv_blur);
        iv_blur.setOnClickListener(this);
        rotate = (ImageView) findViewById(R.id.rotate);
        rotate.setOnClickListener(this);
        iv_Gallary = (ImageView) findViewById(R.id.iv_Gallary);
        iv_Gallary.setOnClickListener(this);
        ic_done = (ImageView) findViewById(R.id.ic_done);
        ic_done.setOnClickListener(this);
        EditActivity.quality = 0;
        bitmap = CropActivity.cropped;
        bitmap1 = bitmap;
        iv_show_image.setImageBitmap(bitmap1);
        blurseekBar.setOnProgressChangeListener(new DiscreteSeekBar.OnProgressChangeListener() {
            @Override
            public void onProgressChanged(DiscreteSeekBar seekBar, int value, boolean fromUser) {
//                float radius = (float) ImageDSLRActivity.this.blurseekBar.getProgress();
//                iv_show_image.setImageBitmap();
                progress = value + 1;
            }

            @Override
            public void onStartTrackingTouch(DiscreteSeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(DiscreteSeekBar seekBar) {
                EditActivity.quality = 0;
                if (bitmap1 != null) {
//                    bm2=  createBlurBitmap(bitmap1, progress);
                    bm2 = fastblur(bitmap1, progress);
                } else {
                    Toast.makeText(ImageDSLRActivity.this, "Something went wrong, Please try again", 0).show();
                }
                if (bm2 != null) {
                    iv_show_image.setImageBitmap(bm2);
                } else {
                    Toast.makeText(ImageDSLRActivity.this, "Something went wrong, Please try again", 0).show();
                }

            }
        });


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.iv_HFlip:
//                if (this.bitmap1 == null || this.bm2 == null) {
//                    Toast.makeText(getApplicationContext(), "First Image Blur", 1).show();
//                } else {
//
//                    blurseekBar.setVisibility(View.GONE);
//
//                    this.angle = 90;
////                Matrix matrix = new Matrix();
////                matrix.postRotate((float) this.angle);
////                this.bitmap1 = Bitmap.createBitmap(this.bitmap1, 0, 0, this.bitmap1.getWidth(), this.bitmap.getHeight(), matrix, true);
//                    bitmap1 = rotateImage(bitmap1, angle);
//                    this.bm2 = fastblur(this.bitmap1, this.progress);
//                    if (this.bm2 != null) {
//                        this.iv_show_image.setImageBitmap(this.bm2);
//                    } else {
//                        Toast.makeText(this, "Something went wrong, Please try again", 0).show();
//                    }
//                }
//                EditActivity.quality = 0;
//
//                break;
            case R.id.iv_blur:
                blurseekBar.setVisibility(View.VISIBLE);
                break;
            case R.id.iv_Gallary:
                this.angle = 0;
                openGallery();

                break;
            case R.id.ic_done:
                if (this.bitmap1 == null || this.bm2 == null) {
                    Toast.makeText(getApplicationContext(), "First Image Blur!", Toast.LENGTH_LONG).show();
                } else {
                    if (EditActivity.quality == 0) {
                        Display display = getWindowManager().getDefaultDisplay();
                        Point size = new Point();
                        display.getSize(size);
                        int screenw = size.x;
                        int screenh = size.y;
                        if (this.bitmap1.getHeight() > this.bitmap1.getWidth()) {
                            int wt1 = (int) ((((float) this.bitmap1.getWidth()) / ((float) this.bitmap1.getHeight())) * ((float) screenh));
                            System.out.println("common");
                            if (wt1 > screenw) {
                                screenh = (int) ((((float) screenw) / ((float) wt1)) * ((float) screenh));
                                wt1 = screenw;
                                System.out.println("no keeda");
                                Common.prog = 0;
                            } else {
                                Common.prog = 1;
                            }
                            this.bitmap1 = scaleBitmap(this.bitmap1, wt1, screenh);
                        } else {
                            Common.prog = 0;
                            this.bitmap1 = scaleBitmap(this.bitmap1, screenw, (int) ((((float) this.bitmap1.getHeight()) / ((float) this.bitmap1.getWidth())) * ((float) screenw)));
                        }
                        this.bm2 = scaleBitmap(this.bm2, this.bitmap1.getWidth(), this.bitmap1.getHeight());
                        if (((double) this.bitmap1.getHeight()) / ((double) this.bitmap1.getWidth()) > 1.25d) {
                            this.bitmap1 = Bitmap.createBitmap(this.bitmap1, 0, 0, this.bitmap1.getWidth(), (int) (((double) ((float) this.bitmap1.getWidth())) * 1.25d));
                        }
                        this.bm2 = Bitmap.createBitmap(this.bm2, 0, 0, this.bitmap1.getWidth(), this.bitmap1.getHeight());
                    }
                    CharSequence[] items = new CharSequence[]{"Unfocus Background", "Focus Image"};
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Select an Option.");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            if (ImageDSLRActivity.this.itemselect == 1) {
                                Common.bmA = ImageDSLRActivity.this.bitmap1;
                                Common.bmB = ImageDSLRActivity.this.bm2;
                            }
                            if (ImageDSLRActivity.this.itemselect == 0) {
                                Common.bmA = ImageDSLRActivity.this.bm2;
                                Common.bmB = ImageDSLRActivity.this.bitmap1;
                            }
                            ImageDSLRActivity.this.startActivity(new Intent(ImageDSLRActivity.this, EditActivity.class));
                        }
                    });


                    final CharSequence[] charSequenceArr = items;
                    builder.setSingleChoiceItems(items, this.itemselect, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            if ("Focus Image".equals(charSequenceArr[which])) {
                                Common.bmA = ImageDSLRActivity.this.bitmap1;
                                Common.bmB = ImageDSLRActivity.this.bm2;
                                ImageDSLRActivity.this.itemselect = 1;
                            } else if ("Unfocus Background".equals(charSequenceArr[which])) {
                                Common.bmA = ImageDSLRActivity.this.bm2;
                                Common.bmB = ImageDSLRActivity.this.bitmap1;
                                ImageDSLRActivity.this.itemselect = 0;
                            }
                        }
                    });
                    builder.show();

                }
                break;
            case R.id.rotate:
                rotate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (rorate == 1) {
                            iv_show_image.setRotation((float) 90.0);
                            rorate = 2;
                        } else if (rorate == 2) {
                            iv_show_image.setRotation((float) 180.0);
                            rorate = 3;
                        } else if (rorate == 3) {
                            iv_show_image.setRotation((float) 270.0);
                            rorate = 4;
                        } else if (rorate == 4) {
                            iv_show_image.setRotation((float) 360.0);
                            rorate = 1;
                        }

                    }
                });
        }
    }

    public static Bitmap scaleBitmap(Bitmap bitmap, int newWidth, int newHeight) {
        Bitmap scaledBitmap = Bitmap.createBitmap(newWidth, newHeight, Bitmap.Config.ARGB_8888);
        float scaleX = ((float) newWidth) / ((float) bitmap.getWidth());
        float scaleY = ((float) newHeight) / ((float) bitmap.getHeight());
        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(scaleX, scaleY, 0.0f, 0.0f);
        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bitmap, 0.0f, 0.0f, new Paint(2));
        return scaledBitmap;
    }


    private void openGallery() {
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, RE_GALLERY);
    }

    public Bitmap rotateImage(Bitmap src, float degree) {
        // create new matrix object
        Matrix matrix = new Matrix();
        // setup rotation degree
        matrix.postRotate(degree);
        // return new bitmap rotated using matrix
        return Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix, true);
    }

    @SuppressLint({"NewApi"})
    public Bitmap fastblur(Bitmap sentBitmap, int radius) {
        Bitmap bitmap = this.bitmap1.copy(this.bitmap1.getConfig(), true);
        try {
            if (Build.VERSION.SDK_INT > 16) {
                bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);
                RenderScript rs = RenderScript.create(this);
                Allocation input = Allocation.createFromBitmap(rs, sentBitmap, Allocation.MipmapControl.MIPMAP_NONE, 1);
                Allocation output = Allocation.createTyped(rs, input.getType());
                ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
                script.setRadius((float) radius);
                script.setInput(input);
                script.forEach(output);
                output.copyTo(bitmap);
                System.out.println("BLUR");
                return bitmap;
            }
            int i;
            int y;
            int bsum;
            int gsum;
            int rsum;
            int boutsum;
            int goutsum;
            int routsum;
            int binsum;
            int ginsum;
            int rinsum;
            int p;
            int[] sir;
            int rbs;
            int stackpointer;
            int x;
            int w = bitmap.getWidth();
            int h = bitmap.getHeight();
            int[] pix = new int[(w * h)];
            Log.e("pix", new StringBuilder(String.valueOf(w)).append(" ").append(h).append(" ").append(pix.length).toString());
            bitmap.getPixels(pix, 0, w, 0, 0, w, h);
            int wm = w - 1;
            int hm = h - 1;
            int wh = w * h;
            int div = (radius + radius) + 1;
            int[] r = new int[wh];
            int[] g = new int[wh];
            int[] b = new int[wh];
            int[] vmin = new int[Math.max(w, h)];
            int divsum = (div + 1) >> 1;
            divsum *= divsum;
            int[] dv = new int[(divsum * 256)];
            for (i = 0; i < divsum * 256; i++) {
                dv[i] = i / divsum;
            }
            int yi = 0;
            int yw = 0;
            int[][] stack = (int[][]) Array.newInstance(Integer.TYPE, new int[]{div, 3});
            int r1 = radius + 1;
            for (y = 0; y < h; y++) {
                bsum = 0;
                gsum = 0;
                rsum = 0;
                boutsum = 0;
                goutsum = 0;
                routsum = 0;
                binsum = 0;
                ginsum = 0;
                rinsum = 0;
                for (i = -radius; i <= radius; i++) {
                    p = pix[Math.min(wm, Math.max(i, 0)) + yi];
                    sir = stack[i + radius];
                    sir[0] = (16711680 & p) >> 16;
                    sir[1] = (MotionEventCompat.ACTION_POINTER_INDEX_MASK & p) >> 8;
                    sir[2] = p & MotionEventCompat.ACTION_MASK;
                    rbs = r1 - Math.abs(i);
                    rsum += sir[0] * rbs;
                    gsum += sir[1] * rbs;
                    bsum += sir[2] * rbs;
                    if (i > 0) {
                        rinsum += sir[0];
                        ginsum += sir[1];
                        binsum += sir[2];
                    } else {
                        routsum += sir[0];
                        goutsum += sir[1];
                        boutsum += sir[2];
                    }
                }
                stackpointer = radius;
                for (x = 0; x < w; x++) {
                    r[yi] = dv[rsum];
                    g[yi] = dv[gsum];
                    b[yi] = dv[bsum];
                    rsum -= routsum;
                    gsum -= goutsum;
                    bsum -= boutsum;
                    sir = stack[((stackpointer - radius) + div) % div];
                    routsum -= sir[0];
                    goutsum -= sir[1];
                    boutsum -= sir[2];
                    if (y == 0) {
                        vmin[x] = Math.min((x + radius) + 1, wm);
                    }
                    p = pix[vmin[x] + yw];
                    sir[0] = (16711680 & p) >> 16;
                    sir[1] = (MotionEventCompat.ACTION_POINTER_INDEX_MASK & p) >> 8;
                    sir[2] = p & MotionEventCompat.ACTION_MASK;
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                    rsum += rinsum;
                    gsum += ginsum;
                    bsum += binsum;
                    stackpointer = (stackpointer + 1) % div;
                    sir = stack[stackpointer % div];
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                    rinsum -= sir[0];
                    ginsum -= sir[1];
                    binsum -= sir[2];
                    yi++;
                }
                yw += w;
            }
            for (x = 0; x < w; x++) {
                bsum = 0;
                gsum = 0;
                rsum = 0;
                boutsum = 0;
                goutsum = 0;
                routsum = 0;
                binsum = 0;
                ginsum = 0;
                rinsum = 0;
                int yp = (-radius) * w;
                for (i = -radius; i <= radius; i++) {
                    yi = Math.max(0, yp) + x;
                    sir = stack[i + radius];
                    sir[0] = r[yi];
                    sir[1] = g[yi];
                    sir[2] = b[yi];
                    rbs = r1 - Math.abs(i);
                    rsum += r[yi] * rbs;
                    gsum += g[yi] * rbs;
                    bsum += b[yi] * rbs;
                    if (i > 0) {
                        rinsum += sir[0];
                        ginsum += sir[1];
                        binsum += sir[2];
                    } else {
                        routsum += sir[0];
                        goutsum += sir[1];
                        boutsum += sir[2];
                    }
                    if (i < hm) {
                        yp += w;
                    }
                }
                yi = x;
                stackpointer = radius;
                for (y = 0; y < h; y++) {
                    pix[yi] = (((ViewCompat.MEASURED_STATE_MASK & pix[yi]) | (dv[rsum] << 16)) | (dv[gsum] << 8)) | dv[bsum];
                    rsum -= routsum;
                    gsum -= goutsum;
                    bsum -= boutsum;
                    sir = stack[((stackpointer - radius) + div) % div];
                    routsum -= sir[0];
                    goutsum -= sir[1];
                    boutsum -= sir[2];
                    if (x == 0) {
                        vmin[y] = Math.min(y + r1, hm) * w;
                    }
                    p = x + vmin[y];
                    sir[0] = r[p];
                    sir[1] = g[p];
                    sir[2] = b[p];
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                    rsum += rinsum;
                    gsum += ginsum;
                    bsum += binsum;
                    stackpointer = (stackpointer + 1) % div;
                    sir = stack[stackpointer];
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                    rinsum -= sir[0];
                    ginsum -= sir[1];
                    binsum -= sir[2];
                    yi += w;
                }
            }
            Log.e("pix", new StringBuilder(String.valueOf(w)).append(" ").append(h).append(" ").append(pix.length).toString());
            bitmap.setPixels(pix, 0, w, 0, 0, w, h);
            return bitmap;
        } catch (Exception e) {
            return null;
        } catch (OutOfMemoryError e2) {
            if (this.bitmap1 != null) {
                this.bitmap1.recycle();
            }
            if (this.bm2 != null) {
                this.bm2.recycle();
            }
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 6;
//            this.bitmap1 = BitmapFactory.decodeFile(this.picturePath, options);
            this.bm2 = fastblur(this.bitmap1, this.progress);
            if (this.bm2 != null) {
                this.iv_show_image.setImageBitmap(this.bm2);
            } else {
                Toast.makeText(this, "Something went wrong, Please try again", Toast.LENGTH_SHORT).show();
            }
        }
        return bitmap;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
//                case RE_GALLERY:
//                    EditActivity.quality = 0;
//
//                    imageUri = data.getData();
//                    try {
//                        bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                    bitmap1 = bitmap;
//                    iv_show_image.setImageBitmap(bitmap1);
//                    break;
                case RE_GALLERY:
                    MainActivity.imageUri = data.getData();
//                    UCrop.of(myURI, destiuri).start(Image_ShowActivity.this);
                    startActivity(new Intent(ImageDSLRActivity.this, CropActivity.class));
//                    myURI = destiuri;

                    break;
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(ImageDSLRActivity.this, MainActivity.class));
    }


    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }



    //FB InterstitialAds En

    @Override
    protected void onResume() {
        super.onResume();
    }

}
