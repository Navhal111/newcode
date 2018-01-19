package pistalix.dslrcamera.dslrcamerahd.TextDemo;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.design.widget.Snackbar;
import android.support.v4.internal.view.SupportMenu;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;

import java.util.ArrayList;
import java.util.Locale;

import pistalix.dslrcamera.dslrcamerahd.R;


//import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;


//import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;


public class TextDemoActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView iv_Enter_text, iv_done, iv_size, iv_color, iv_DoneSize, iv_style, final_done, fback, iv_Mic;
    private CardView CV_TEXT;
    private EditText ET_text;
    public static String etData;
    static TextView TV_Text;
    private final int REQ_CODE_SPEECH_INPUT = 100;
    private LinearLayout llSize, llEnter_text, llcolor, llSizeSeek, llstyle, llMic;
    private DiscreteSeekBar sizeseekBar;
    int textSize = 25;
    ArrayList<Typeface> fontList;
    private FrameLayout mainFrame;
    private int currentBackgroundColor = 0xffffffff;
    private GridView grid_font;
    private FrameLayout FLText;
    public static Bitmap finalBitmapText;//for store image after editing
    private int colorCode = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_demo);
        Bind();
        etData = TV_Text.getText().toString();
        setFontListForGrid();
        FontList_Adapter adapterFont = new FontList_Adapter(this, fontList);
        grid_font.setAdapter(adapterFont);
        grid_font.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                if (i == 0) {
                    TV_Text.setTypeface(FontFace.f1(getApplicationContext()));
                } else if (i == 1) {
                    TV_Text.setTypeface(FontFace.f2(getApplicationContext()));
                } else if (i == 2) {
                    TV_Text.setTypeface(FontFace.f3(getApplicationContext()));
                } else if (i == 3) {
                    TV_Text.setTypeface(FontFace.f4(getApplicationContext()));
                } else if (i == 4) {
                    TV_Text.setTypeface(FontFace.f5(getApplicationContext()));
                } else if (i == 5) {
                    TV_Text.setTypeface(FontFace.f6(getApplicationContext()));
                } else if (i == 6) {
                    TV_Text.setTypeface(FontFace.f7(getApplicationContext()));
                } else if (i == 7) {
                    TV_Text.setTypeface(FontFace.f8(getApplicationContext()));
                } else if (i == 8) {
                    TV_Text.setTypeface(FontFace.f9(getApplicationContext()));
                } else if (i == 9) {
                    TV_Text.setTypeface(FontFace.f10(getApplicationContext()));
                } else if (i == 10) {
                    TV_Text.setTypeface(FontFace.f11(getApplicationContext()));
                } else if (i == 11) {
                    TV_Text.setTypeface(FontFace.f12(getApplicationContext()));
                } else if (i == 12) {
                    TV_Text.setTypeface(FontFace.f13(getApplicationContext()));
                } else if (i == 13) {
                    TV_Text.setTypeface(FontFace.f14(getApplicationContext()));
                } else if (i == 14) {
                    TV_Text.setTypeface(FontFace.f15(getApplicationContext()));
                } else if (i == 15) {
                    TV_Text.setTypeface(FontFace.f16(getApplicationContext()));
                } else if (i == 16) {
                    TV_Text.setTypeface(FontFace.f17(getApplicationContext()));
                } else if (i == 17) {
                    TV_Text.setTypeface(FontFace.f18(getApplicationContext()));
                } else if (i == 18) {
                    TV_Text.setTypeface(FontFace.f19(getApplicationContext()));
                } else if (i == 19) {
                    TV_Text.setTypeface(FontFace.f20(getApplicationContext()));
                } else if (i == 20) {
                    TV_Text.setTypeface(FontFace.f21(getApplicationContext()));
                } else if (i == 21) {
                    TV_Text.setTypeface(FontFace.f22(getApplicationContext()));
                } else if (i == 22) {
                    TV_Text.setTypeface(FontFace.f23(getApplicationContext()));
                } else if (i == 23) {
                    TV_Text.setTypeface(FontFace.f24(getApplicationContext()));
                } else if (i == 24) {
                    TV_Text.setTypeface(FontFace.f25(getApplicationContext()));
                } else if (i == 25) {
                    TV_Text.setTypeface(FontFace.f26(getApplicationContext()));
                } else if (i == 26) {
                    TV_Text.setTypeface(FontFace.f27(getApplicationContext()));
                } else if (i == 27) {
                    TV_Text.setTypeface(FontFace.f28(getApplicationContext()));
                } else if (i == 28) {
                    TV_Text.setTypeface(FontFace.f29(getApplicationContext()));
                } else if (i == 29) {
                    TV_Text.setTypeface(FontFace.f30(getApplicationContext()));
                } else if (i == 30) {
                    TV_Text.setTypeface(FontFace.f31(getApplicationContext()));
                } else if (i == 31) {
                    TV_Text.setTypeface(FontFace.f32(getApplicationContext()));
                } else if (i == 32) {
                    TV_Text.setTypeface(FontFace.f33(getApplicationContext()));
                } else if (i == 33) {
                    TV_Text.setTypeface(FontFace.f34(getApplicationContext()));
                } else if (i == 34) {
                    TV_Text.setTypeface(FontFace.f35(getApplicationContext()));
                } else if (i == 35) {
                    TV_Text.setTypeface(FontFace.f36(getApplicationContext()));
                } else if (i == 36) {
                    TV_Text.setTypeface(FontFace.f37(getApplicationContext()));
                } else if (i == 37) {
                    TV_Text.setTypeface(FontFace.f38(getApplicationContext()));
                } else if (i == 38) {
                    TV_Text.setTypeface(FontFace.f39(getApplicationContext()));
                } else if (i == 39) {
                    TV_Text.setTypeface(FontFace.f40(getApplicationContext()));
                } else if (i == 40) {
                    TV_Text.setTypeface(FontFace.f41(getApplicationContext()));
                }
                Animation s2 = AnimationUtils.loadAnimation(getApplicationContext(),
                        R.anim.slide_down);
                grid_font.startAnimation(s2);
                grid_font.setVisibility(View.GONE);
            }
        });
        sizeseekBar.setOnProgressChangeListener(new DiscreteSeekBar.OnProgressChangeListener() {
            @Override
            public void onProgressChanged(DiscreteSeekBar seekBar, int value, boolean fromUser) {
                textSize = value;
                TV_Text.setTextSize(textSize);
            }

            @Override
            public void onStartTrackingTouch(DiscreteSeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(DiscreteSeekBar seekBar) {

            }
        });
        sizeseekBar.setProgress(textSize);
    }

    public static String getText() {
        String data = (String) TV_Text.getText();
        return data;
    }

    private void setFontListForGrid() {
        fontList = new ArrayList<>();
        fontList.add(FontFace.f1(getApplicationContext()));
        fontList.add(FontFace.f2(getApplicationContext()));
        fontList.add(FontFace.f3(getApplicationContext()));
        fontList.add(FontFace.f4(getApplicationContext()));
        fontList.add(FontFace.f5(getApplicationContext()));
        fontList.add(FontFace.f6(getApplicationContext()));
        fontList.add(FontFace.f7(getApplicationContext()));
        fontList.add(FontFace.f8(getApplicationContext()));
        fontList.add(FontFace.f9(getApplicationContext()));
        fontList.add(FontFace.f10(getApplicationContext()));
        fontList.add(FontFace.f11(getApplicationContext()));
        fontList.add(FontFace.f12(getApplicationContext()));
        fontList.add(FontFace.f13(getApplicationContext()));
        fontList.add(FontFace.f14(getApplicationContext()));
        fontList.add(FontFace.f15(getApplicationContext()));
        fontList.add(FontFace.f16(getApplicationContext()));
        fontList.add(FontFace.f17(getApplicationContext()));
        fontList.add(FontFace.f18(getApplicationContext()));
        fontList.add(FontFace.f19(getApplicationContext()));
        fontList.add(FontFace.f20(getApplicationContext()));
        fontList.add(FontFace.f21(getApplicationContext()));
        fontList.add(FontFace.f22(getApplicationContext()));
        fontList.add(FontFace.f23(getApplicationContext()));
        fontList.add(FontFace.f24(getApplicationContext()));
        fontList.add(FontFace.f25(getApplicationContext()));
        fontList.add(FontFace.f26(getApplicationContext()));
        fontList.add(FontFace.f27(getApplicationContext()));
        fontList.add(FontFace.f28(getApplicationContext()));
        fontList.add(FontFace.f29(getApplicationContext()));
        fontList.add(FontFace.f30(getApplicationContext()));
        fontList.add(FontFace.f31(getApplicationContext()));
        fontList.add(FontFace.f32(getApplicationContext()));
        fontList.add(FontFace.f33(getApplicationContext()));
        fontList.add(FontFace.f34(getApplicationContext()));
        fontList.add(FontFace.f35(getApplicationContext()));
        fontList.add(FontFace.f36(getApplicationContext()));
        fontList.add(FontFace.f37(getApplicationContext()));
        fontList.add(FontFace.f38(getApplicationContext()));
        fontList.add(FontFace.f39(getApplicationContext()));
        fontList.add(FontFace.f40(getApplicationContext()));
        fontList.add(FontFace.f41(getApplicationContext()));
    }

    private void Bind() {
        ET_text = (EditText) findViewById(R.id.ET_text);
        iv_done = (ImageView) findViewById(R.id.iv_done);
        iv_done.setOnClickListener(this);
        llEnter_text = (LinearLayout) findViewById(R.id.llEnter_text);
        iv_Enter_text = (ImageView) findViewById(R.id.iv_Enter_text);
        iv_Enter_text.setOnClickListener(this);
        llSize = (LinearLayout) findViewById(R.id.llSize);
        iv_size = (ImageView) findViewById(R.id.iv_size);
        iv_size.setOnClickListener(this);
        TV_Text = (TextView) findViewById(R.id.TV_Text);
        CV_TEXT = (CardView) findViewById(R.id.CV_TEXT);
        mainFrame = (FrameLayout) findViewById(R.id.mainFrame);
        llcolor = (LinearLayout) findViewById(R.id.llcolor);
        iv_color = (ImageView) findViewById(R.id.iv_color);
        iv_color.setOnClickListener(this);
        iv_DoneSize = (ImageView) findViewById(R.id.iv_DoneSize);
        iv_DoneSize.setOnClickListener(this);
        llSizeSeek = (LinearLayout) findViewById(R.id.llSizeSeek);
        sizeseekBar = (DiscreteSeekBar) findViewById(R.id.sizeseekBar);
        grid_font = (GridView) findViewById(R.id.grid_font);
        iv_style = (ImageView) findViewById(R.id.iv_style);
        iv_style.setOnClickListener(this);
        llstyle = (LinearLayout) findViewById(R.id.llstyle);
        fback = (ImageView) findViewById(R.id.fback);
        fback.setOnClickListener(this);
        final_done = (ImageView) findViewById(R.id.final_done);
        final_done.setOnClickListener(this);
        iv_Mic = (ImageView) findViewById(R.id.iv_Mic);
        iv_Mic.setOnClickListener(this);
        llMic = (LinearLayout) findViewById(R.id.llMic);
        FLText = (FrameLayout) findViewById(R.id.FLText);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fback:
                llSizeSeek.setVisibility(View.GONE);
                CV_TEXT.setVisibility(View.GONE);
                grid_font.setVisibility(View.GONE);
                finish();
                break;
            case R.id.final_done:
                if (TV_Text.getText().toString().isEmpty()) {
                    Snackbar snackbar = Snackbar
                            .make(mainFrame, "Text Is Not Found, Please Insert Text First.", Snackbar.LENGTH_SHORT);
                    View sbView = snackbar.getView();
                    TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                    textView.setTextColor(getResources().getColor(R.color.colorAccent));
                    textView.setTextSize(16);
                    snackbar.show();
                } else {
                    llSizeSeek.setVisibility(View.GONE);
                    CV_TEXT.setVisibility(View.GONE);
                    grid_font.setVisibility(View.GONE);
                    finalBitmapText = getMainFrameBitmap();
                    setResult(RESULT_OK);
                    finish();
                }
                break;
            case R.id.iv_Enter_text:
                Animation slide_up = AnimationUtils.loadAnimation(getApplicationContext(),
                        R.anim.slide_up);
                CV_TEXT.startAnimation(slide_up);
                CV_TEXT.setVisibility(View.VISIBLE);
                llSizeSeek.setVisibility(View.GONE);
                grid_font.setVisibility(View.GONE);
                break;

            case R.id.iv_done:
                if (ET_text.getText().toString().isEmpty()) {
                    ET_text.setError("Please Enter Text");
                } else {
                    // hide virtual keyboard
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(iv_done.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);

                    Animation slide_Down = AnimationUtils.loadAnimation(getApplicationContext(),
                            R.anim.slide_down);
                    CV_TEXT.startAnimation(slide_Down);
                    getDataText();
                    CV_TEXT.setVisibility(View.GONE);
                }
                break;
            case R.id.iv_size:
                if (TV_Text.getText().toString().isEmpty()) {
                    Snackbar snackbar = Snackbar
                            .make(mainFrame, "Text Is Not Found, Please Insert Text First.", Snackbar.LENGTH_SHORT);
                    View sbView = snackbar.getView();
                    TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                    textView.setTextColor(getResources().getColor(R.color.colorAccent));
                    textView.setTextSize(16);
                    snackbar.show();
                } else {
                    Animation s1 = AnimationUtils.loadAnimation(getApplicationContext(),
                            R.anim.slide_up);
                    llSizeSeek.startAnimation(s1);
                    llSizeSeek.setVisibility(View.VISIBLE);
                    CV_TEXT.setVisibility(View.GONE);
                    grid_font.setVisibility(View.GONE);
                }
                break;
            case R.id.iv_color:
                if (TV_Text.getText().toString().isEmpty()) {
                    Snackbar snackbar = Snackbar
                            .make(mainFrame, "Text Is Not Found, Please Insert Text First.", Snackbar.LENGTH_SHORT);
                    View sbView = snackbar.getView();
                    TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                    textView.setTextColor(getResources().getColor(R.color.colorAccent));
                    textView.setTextSize(16);
                    snackbar.show();
                } else {
                    showColorPickerDialogDemo();
                    llSizeSeek.setVisibility(View.GONE);
                    CV_TEXT.setVisibility(View.GONE);
                    grid_font.setVisibility(View.GONE);
                }
                break;
            case R.id.iv_DoneSize:
                Animation s2 = AnimationUtils.loadAnimation(getApplicationContext(),
                        R.anim.slide_down);
                llSizeSeek.startAnimation(s2);
                llSizeSeek.setVisibility(View.GONE);
                break;
            case R.id.iv_style:
                if (TV_Text.getText().toString().isEmpty()) {
                    Snackbar snackbar = Snackbar
                            .make(mainFrame, "Text Is Not Found, Please Insert Text First.", Snackbar.LENGTH_SHORT);
                    View sbView = snackbar.getView();
                    TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                    textView.setTextColor(getResources().getColor(R.color.colorAccent));
                    textView.setTextSize(16);
                    snackbar.show();
                } else {
                    Animation s1 = AnimationUtils.loadAnimation(getApplicationContext(),
                            R.anim.slide_up);
                    grid_font.startAnimation(s1);
                    grid_font.setVisibility(View.VISIBLE);
                    llSizeSeek.setVisibility(View.GONE);
                    CV_TEXT.setVisibility(View.GONE);
                }
                break;
            case R.id.iv_Mic:

                llSizeSeek.setVisibility(View.GONE);
                CV_TEXT.setVisibility(View.GONE);
                grid_font.setVisibility(View.GONE);
                promptSpeechInput();
        }
    }

    private void showColorPickerDialogDemo() {
        new TextDemoActivity.ColorPickerDialog(this, SupportMenu.CATEGORY_MASK, new TextDemoActivity.ColorPickerDialog.OnColorSelectedListener() {
            public void onColorSelected(int color) {
                TextDemoActivity.this.colorCode = color;
                TV_Text.setTextColor(colorCode);
            }
        }).show();

    }

    private static class ColorPickerDialog extends AlertDialog {
        private ColorPicker colorPickerView;

        @SuppressLint("ResourceType")
        public ColorPickerDialog(Context context, int categoryMask, TextDemoActivity.ColorPickerDialog.OnColorSelectedListener onColorSelectedListener) {
            super(context);
            this.onColorSelectedListener = onColorSelectedListener;
            RelativeLayout relativeLayout = new RelativeLayout(context);
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(-1, -1);
            layoutParams.addRule(13);
            this.colorPickerView = new ColorPicker(context);
            this.colorPickerView.setColor(categoryMask);
            relativeLayout.addView(this.colorPickerView, layoutParams);
            setButton(-1, context.getString(17039370), this.onClickListener);
            setButton(-2, context.getString(17039360), this.onClickListener);
            setView(relativeLayout);

        }

        private final TextDemoActivity.ColorPickerDialog.OnColorSelectedListener onColorSelectedListener;

        public interface OnColorSelectedListener {
            void onColorSelected(int i);

        }

        private DialogInterface.OnClickListener onClickListener = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case -2:
                        dialog.dismiss();
                        return;
                    case -1:
                        TextDemoActivity.ColorPickerDialog.this.onColorSelectedListener.onColorSelected(TextDemoActivity.ColorPickerDialog.this.colorPickerView.getColor());
                        return;
                    default:
                        return;
                }
            }
        };

    }

    /**
     * Showing google speech input dialog
     */
    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.speech_prompt));
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    getString(R.string.speech_not_supported),
                    Toast.LENGTH_SHORT).show();
        }
    }

//

    private void getDataText() {
        TV_Text.setText(ET_text.getText().toString());
        ET_text.getText().clear();

    }

    private Bitmap getMainFrameBitmap() {

        FLText.setDrawingCacheEnabled(true);

        Bitmap bitmap = Bitmap.createBitmap(FLText.getDrawingCache());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            bitmap.setConfig(Bitmap.Config.ARGB_8888);
        }
        FLText.setDrawingCacheEnabled(false);
        Bitmap bmp = bitmap;

        int imgHeight = bmp.getHeight();
        int imgWidth = bmp.getWidth();
        int smallX = 0, largeX = imgWidth, smallY = 0, largeY = imgHeight;
        int left = imgWidth, right = imgWidth, top = imgHeight, bottom = imgHeight;
        for (int i = 0; i < imgWidth; i++) {
            for (int j = 0; j < imgHeight; j++) {
                if (bmp.getPixel(i, j) != Color.TRANSPARENT) {
                    if ((i - smallX) < left) {
                        left = (i - smallX);
                    }
                    if ((largeX - i) < right) {
                        right = (largeX - i);
                    }
                    if ((j - smallY) < top) {
                        top = (j - smallY);
                    }
                    if ((largeY - j) < bottom) {
                        bottom = (largeY - j);
                    }
                }
            }
        }
        Log.d("Trimed bitmap", "left:" + left + " right:" + right + " top:" + top + " bottom:" + bottom);
        bmp = Bitmap.createBitmap(bmp, left, top, imgWidth - left - right, imgHeight - top - bottom);
        return bmp;


    }

    /**
     * Receiving speech input
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    TV_Text.setText(result.get(0));
                }
                break;
            }

        }
    }
}
