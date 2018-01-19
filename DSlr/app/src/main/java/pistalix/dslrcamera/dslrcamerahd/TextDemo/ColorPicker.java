package pistalix.dslrcamera.dslrcamerahd.TextDemo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ComposeShader;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Join;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.PorterDuff.Mode;
import android.graphics.RadialGradient;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Shader.TileMode;
import android.graphics.SweepGradient;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.google.android.gms.cast.TextTrackStyle;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;


public class ColorPicker extends View {
    private Path arrowPointerPath;
    private int arrowPointerSize;
    private float[] colorHSV = new float[]{0.0f, 0.0f, TextTrackStyle.DEFAULT_FONT_SCALE};
    private RectF colorPointerCoords;
    private Paint colorPointerPaint;
    private Paint colorViewPaint;
    private Path colorViewPath;
    private Bitmap colorWheelBitmap;
    private Paint colorWheelPaint;
    private int colorWheelRadius;
    private Matrix gradientRotationMatrix;
    private int innerPadding;
    private int innerWheelRadius;
    private RectF innerWheelRect;
    private int outerPadding;
    private int outerWheelRadius;
    private RectF outerWheelRect;
    private final int paramArrowPointerSize = 4;
    private final int paramInnerPadding = 5;
    private final int paramOuterPadding = 2;
    private final int paramValueSliderWidth = 10;
    private Paint valuePointerArrowPaint;
    private Paint valuePointerPaint;
    private Paint valueSliderPaint;
    private Path valueSliderPath;
    private int valueSliderWidth;

    public ColorPicker(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public ColorPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ColorPicker(Context context) {
        super(context);
        init();
    }

    private void init() {
        this.colorPointerPaint = new Paint();
        this.colorPointerPaint.setStyle(Style.STROKE);
        this.colorPointerPaint.setStrokeWidth(2.0f);
        this.colorPointerPaint.setARGB(128, 0, 0, 0);
        this.valuePointerPaint = new Paint();
        this.valuePointerPaint.setStyle(Style.STROKE);
        this.valuePointerPaint.setStrokeWidth(2.0f);
        this.valuePointerArrowPaint = new Paint();
        this.colorWheelPaint = new Paint();
        this.colorWheelPaint.setAntiAlias(true);
        this.colorWheelPaint.setDither(true);
        this.valueSliderPaint = new Paint();
        this.valueSliderPaint.setAntiAlias(true);
        this.valueSliderPaint.setDither(true);
        this.colorViewPaint = new Paint();
        this.colorViewPaint.setAntiAlias(true);
        this.colorViewPath = new Path();
        this.valueSliderPath = new Path();
        this.arrowPointerPath = new Path();
        this.outerWheelRect = new RectF();
        this.innerWheelRect = new RectF();
        this.colorPointerCoords = new RectF();
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int size = Math.min(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.getSize(heightMeasureSpec));
        setMeasuredDimension(size, size);
    }

    @SuppressLint({"DrawAllocation"})
    protected void onDraw(Canvas canvas) {
        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;
        canvas.drawBitmap(this.colorWheelBitmap, (float) (centerX - this.colorWheelRadius), (float) (centerY - this.colorWheelRadius), null);
        this.colorViewPaint.setColor(Color.HSVToColor(this.colorHSV));
        canvas.drawPath(this.colorViewPath, this.colorViewPaint);
        float[] hsv = new float[]{this.colorHSV[0], this.colorHSV[1], TextTrackStyle.DEFAULT_FONT_SCALE};
        Shader sweepGradient = new SweepGradient((float) centerX, (float) centerY, new int[]{ViewCompat.MEASURED_STATE_MASK, Color.HSVToColor(hsv), -1}, null);
        sweepGradient.setLocalMatrix(this.gradientRotationMatrix);
        this.valueSliderPaint.setShader(sweepGradient);
        canvas.drawPath(this.valueSliderPath, this.valueSliderPaint);
        float hueAngle = (float) Math.toRadians((double) this.colorHSV[0]);
        float pointerRadius = 0.075f * ((float) this.colorWheelRadius);
        int pointerX = (int) (((float) (((int) (((-Math.cos((double) hueAngle)) * ((double) this.colorHSV[1])) * ((double) this.colorWheelRadius))) + centerX)) - (pointerRadius / 2.0f));
        int pointerY = (int) (((float) (((int) (((-Math.sin((double) hueAngle)) * ((double) this.colorHSV[1])) * ((double) this.colorWheelRadius))) + centerY)) - (pointerRadius / 2.0f));
        this.colorPointerCoords.set((float) pointerX, (float) pointerY, ((float) pointerX) + pointerRadius, ((float) pointerY) + pointerRadius);
        canvas.drawOval(this.colorPointerCoords, this.colorPointerPaint);
        this.valuePointerPaint.setColor(Color.HSVToColor(new float[]{0.0f, 0.0f, TextTrackStyle.DEFAULT_FONT_SCALE - this.colorHSV[2]}));
        double valueAngle = ((double) (this.colorHSV[2] - 0.5f)) * 3.141592653589793d;
        float valueAngleX = (float) Math.cos(valueAngle);
        float valueAngleY = (float) Math.sin(valueAngle);
        Canvas canvas2 = canvas;
        canvas2.drawLine(((float) centerX) + (((float) this.innerWheelRadius) * valueAngleX), ((float) centerY) + (((float) this.innerWheelRadius) * valueAngleY), ((float) centerX) + (((float) this.outerWheelRadius) * valueAngleX), ((float) centerY) + (((float) this.outerWheelRadius) * valueAngleY), this.valuePointerPaint);
        if (this.arrowPointerSize > 0) {
            drawPointerArrow(canvas);
        }
    }

    private void drawPointerArrow(Canvas canvas) {
        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;
        double tipAngle = ((double) (this.colorHSV[2] - 0.5f)) * 3.141592653589793d;
        double leftAngle = tipAngle + 0.032724923474893676d;
        double rightAngle = tipAngle - 0.032724923474893676d;
        double tipAngleX = Math.cos(tipAngle) * ((double) this.outerWheelRadius);
        double tipAngleY = Math.sin(tipAngle) * ((double) this.outerWheelRadius);
        double leftAngleX = Math.cos(leftAngle) * ((double) (this.outerWheelRadius + this.arrowPointerSize));
        double leftAngleY = Math.sin(leftAngle) * ((double) (this.outerWheelRadius + this.arrowPointerSize));
        double rightAngleX = Math.cos(rightAngle) * ((double) (this.outerWheelRadius + this.arrowPointerSize));
        double rightAngleY = Math.sin(rightAngle) * ((double) (this.outerWheelRadius + this.arrowPointerSize));
        this.arrowPointerPath.reset();
        this.arrowPointerPath.moveTo(((float) tipAngleX) + ((float) centerX), ((float) tipAngleY) + ((float) centerY));
        this.arrowPointerPath.lineTo(((float) leftAngleX) + ((float) centerX), ((float) leftAngleY) + ((float) centerY));
        this.arrowPointerPath.lineTo(((float) rightAngleX) + ((float) centerX), ((float) rightAngleY) + ((float) centerY));
        this.arrowPointerPath.lineTo(((float) tipAngleX) + ((float) centerX), ((float) tipAngleY) + ((float) centerY));
        this.valuePointerArrowPaint.setColor(Color.HSVToColor(this.colorHSV));
        this.valuePointerArrowPaint.setStyle(Style.FILL);
        canvas.drawPath(this.arrowPointerPath, this.valuePointerArrowPaint);
        this.valuePointerArrowPaint.setStyle(Style.STROKE);
        this.valuePointerArrowPaint.setStrokeJoin(Join.ROUND);
        this.valuePointerArrowPaint.setColor(ViewCompat.MEASURED_STATE_MASK);
        canvas.drawPath(this.arrowPointerPath, this.valuePointerArrowPaint);
    }

    protected void onSizeChanged(int width, int height, int oldw, int oldh) {
        int centerX = width / 2;
        int centerY = height / 2;
        this.innerPadding = (width * 5) / 100;
        this.outerPadding = (width * 2) / 100;
        this.arrowPointerSize = (width * 4) / 100;
        this.valueSliderWidth = (width * 10) / 100;
        this.outerWheelRadius = ((width / 2) - this.outerPadding) - this.arrowPointerSize;
        this.innerWheelRadius = this.outerWheelRadius - this.valueSliderWidth;
        this.colorWheelRadius = this.innerWheelRadius - this.innerPadding;
        this.outerWheelRect.set((float) (centerX - this.outerWheelRadius), (float) (centerY - this.outerWheelRadius), (float) (this.outerWheelRadius + centerX), (float) (this.outerWheelRadius + centerY));
        this.innerWheelRect.set((float) (centerX - this.innerWheelRadius), (float) (centerY - this.innerWheelRadius), (float) (this.innerWheelRadius + centerX), (float) (this.innerWheelRadius + centerY));
        this.colorWheelBitmap = createColorWheelBitmap(this.colorWheelRadius * 2, this.colorWheelRadius * 2);
        this.gradientRotationMatrix = new Matrix();
        this.gradientRotationMatrix.preRotate(BitmapDescriptorFactory.HUE_VIOLET, (float) (width / 2), (float) (height / 2));
        this.colorViewPath.arcTo(this.outerWheelRect, BitmapDescriptorFactory.HUE_VIOLET, -180.0f);
        this.colorViewPath.arcTo(this.innerWheelRect, 90.0f, BitmapDescriptorFactory.HUE_CYAN);
        this.valueSliderPath.arcTo(this.outerWheelRect, BitmapDescriptorFactory.HUE_VIOLET, BitmapDescriptorFactory.HUE_CYAN);
        this.valueSliderPath.arcTo(this.innerWheelRect, 90.0f, -180.0f);
    }

    private Bitmap createColorWheelBitmap(int width, int height) {
        Bitmap bitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
        int[] colors = new int[13];
        float[] hsv = new float[]{0.0f, TextTrackStyle.DEFAULT_FONT_SCALE, TextTrackStyle.DEFAULT_FONT_SCALE};
        for (int i = 0; i < colors.length; i++) {
            hsv[0] = (float) (((i * 30) + 180) % 360);
            colors[i] = Color.HSVToColor(hsv);
        }
        colors[12] = colors[0];
        this.colorWheelPaint.setShader(new ComposeShader(new SweepGradient((float) (width / 2), (float) (height / 2), colors, null), new RadialGradient((float) (width / 2), (float) (height / 2), (float) this.colorWheelRadius, -1, ViewCompat.MEASURED_SIZE_MASK, TileMode.CLAMP), Mode.SRC_OVER));
        new Canvas(bitmap).drawCircle((float) (width / 2), (float) (height / 2), (float) this.colorWheelRadius, this.colorWheelPaint);
        return bitmap;
    }

    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case 0:
            case 2:
                int x = (int) event.getX();
                int cx = x - (getWidth() / 2);
                int cy = ((int) event.getY()) - (getHeight() / 2);
                double d = Math.sqrt((double) ((cx * cx) + (cy * cy)));
                if (d <= ((double) this.colorWheelRadius)) {
                    this.colorHSV[0] = (float) (Math.toDegrees(Math.atan2((double) cy, (double) cx)) + 180.0d);
                    this.colorHSV[1] = Math.max(0.0f, Math.min(TextTrackStyle.DEFAULT_FONT_SCALE, (float) (d / ((double) this.colorWheelRadius))));
                    invalidate();
                } else if (x >= getWidth() / 2 && d >= ((double) this.innerWheelRadius)) {
                    this.colorHSV[2] = (float) Math.max(0.0d, Math.min(1.0d, (Math.atan2((double) cy, (double) cx) / 3.141592653589793d) + 0.5d));
                    invalidate();
                }
                return true;
            default:
                return super.onTouchEvent(event);
        }
    }

    public void setColor(int color) {
        Color.colorToHSV(color, this.colorHSV);
    }

    public int getColor() {
        return Color.HSVToColor(this.colorHSV);
    }

    protected Parcelable onSaveInstanceState() {
        Bundle state = new Bundle();
        state.putFloatArray("color", this.colorHSV);
        state.putParcelable("super", super.onSaveInstanceState());
        return state;
    }

    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            this.colorHSV = bundle.getFloatArray("color");
            super.onRestoreInstanceState(bundle.getParcelable("super"));
            return;
        }
        super.onRestoreInstanceState(state);
    }
}