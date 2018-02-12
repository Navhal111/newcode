package com.neurondigital.nudge;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.SystemClock;

public class Sprite {
    //image/animation
    Bitmap unrotated_image_sequence[], image_sequence[];
    public int current_image = 0;
    long start_time = SystemClock.uptimeMillis();
    int animation_speed;
    public Paint imagePaint = new Paint();
    float scale = 1f, screenPercentage_scale;

    //text
    public Paint textPaint;
    public String text;

    //background
    public Paint backgroundPaint;
    public int back_width, back_height;
    public int backgroundShape;

    //private variables
    private int pause_image_number = -1;
    private float direction = 0;

    //constants
    public static final int VERTICAL = 0, HORIZONTAL = 1;
    public static final int SQUARE = 0, CIRCLE = 1;

    //Constructors______________________________________________________________________________________________________________________________

    /**
     * Creates a Sprite based on a single image.
     *
     * @param image The bitmap image to attach to Sprite
     * @param scale scale factor of image. Eg: ScreenWidth * 0.15f. put a negative number to leave unscaled
     */
    public Sprite(Bitmap image, float scale) {
        image_sequence = new Bitmap[1];
        screenPercentage_scale = scale;

        unrotated_image_sequence = new Bitmap[1];
        if (scale >= 0)
            image_sequence[0] = unrotated_image_sequence[0] = Bitmap.createScaledBitmap(image, (int) ((float) (scale)), (int) (((float) (scale) / image.getWidth()) * image.getHeight()), true);
        else
            image_sequence[0] = unrotated_image_sequence[0] = image;

    }

    /**
     * Creates a Sprite based on a single image.
     *
     * @param image        The bitmap image to attach to Sprite
     * @param scale        scale factor of image. Eg: ScreenWidth * 0.15f. put a negative number to leave unscaled
     * @param scale_height if true the scale factor is calculated on the image height
     */
    public Sprite(Bitmap image, float scale, boolean scale_height) {
        image_sequence = new Bitmap[1];
        screenPercentage_scale = scale;

        unrotated_image_sequence = new Bitmap[1];
        if (scale >= 0)
            image_sequence[0] = unrotated_image_sequence[0] = Bitmap.createScaledBitmap(image, (int) (((float) (scale) / image.getHeight()) * image.getWidth()), (int) ((float) (scale)), true);
        else
            image_sequence[0] = unrotated_image_sequence[0] = image;

    }

    /**
     * Creates a Sprite animation based on a sprite sheet.
     *
     * @param sprite_sheet    The bitmap image to obtain images from
     * @param scale           scale factor of image. Eg: ScreenWidth * 0.15f. put a negative number to leave unscaled
     * @param itemsX          the number of images in the first row of the sprite sheet.
     * @param length          the number of images in the sprite sheet.
     * @param animation_speed a number from 0 to 500. The higher the animation_speed the faster the images will change.
     */
    public Sprite(Bitmap sprite_sheet, float scale, int itemsX, int length, int animation_speed) {
        this.animation_speed = animation_speed;
        unrotated_image_sequence = convert(sprite_sheet, itemsX, length);
        image_sequence = new Bitmap[length];
        screenPercentage_scale = scale;

        //scale images
        if (scale >= 0) {
            for (int count = 0; count < length; count++)
                image_sequence[count] = unrotated_image_sequence[count] = Bitmap.createScaledBitmap(unrotated_image_sequence[count], (int) ((float) (scale)), (int) (((float) (scale) / unrotated_image_sequence[count].getWidth()) * unrotated_image_sequence[count].getHeight()), true);
        } else {
            for (int count = 0; count < length; count++)
                image_sequence[count] = unrotated_image_sequence[count];
        }
        start_time = (long) (SystemClock.uptimeMillis() + (Math.random() * 400));
    }

    /**
     * Create new text button
     *
     * @param text   text to bisplay on button
     * @param dpSize size of text in dp
     * @param font   Typface of text
     * @param color  Color to use for text
     */
    public Sprite(String text, float dpSize, Typeface font, int color) {
        textPaint = new Paint();
        textPaint.setTextSize(dpSize);
        textPaint.setAntiAlias(true);
        textPaint.setColor(color);
        textPaint.setTypeface(font);
        this.text = text;
    }

    //clone
    public Sprite Clone() {
        Sprite clone;
        if (image_sequence != null)
            clone = new Sprite(image_sequence[0], screenPercentage_scale);
        else
            clone = new Sprite(text, textPaint.getTextSize(), textPaint.getTypeface(), textPaint.getColor());
        clone.rotate(direction);
        clone.animation_speed = animation_speed;
        clone.image_sequence = image_sequence.clone();
        clone.unrotated_image_sequence = unrotated_image_sequence.clone();
        clone.current_image = current_image;
        clone.scale = scale;
        if (imagePaint != null)
            clone.imagePaint = new Paint(imagePaint);
        clone.back_height = back_height;
        clone.back_width = back_width;
        if (backgroundPaint != null)
            clone.backgroundPaint = new Paint(backgroundPaint);
        clone.pause_image_number = pause_image_number;
        clone.screenPercentage_scale = screenPercentage_scale;
        clone.start_time = start_time;
        if (textPaint != null)
            clone.textPaint = new Paint(textPaint);
        return clone;
    }

    //background_____________________________________________________________________________________________________________
    public void addBackground(int shape, int Color, int width, int height) {
        backgroundPaint = new Paint();
        backgroundPaint.setColor(Color);
        backgroundPaint.setAntiAlias(true);
        this.back_height = height;
        this.back_width = width;
        this.backgroundShape = shape;
    }

    public void removeBackground() {
        backgroundPaint = null;
    }

    //getters______________________________________________________________________________________________________________
    public int getWidth() {
        if (backgroundPaint != null)
            return back_width;
        else if (image_sequence != null)
            return image_sequence[0].getWidth();
        else if (textPaint != null) {
            Rect bounds = new Rect();
            textPaint.getTextBounds(text, 0, text.length(), bounds);
            return bounds.width();
        } else
            return 1;
    }

    public int getHeight() {
        if (backgroundPaint != null)
            return back_height;
        else if (image_sequence != null)
            return image_sequence[0].getHeight();
        else if (textPaint != null) {
            Rect bounds = new Rect();
            textPaint.getTextBounds(text, 0, text.length(), bounds);
            return bounds.height();
        } else
            return 1;
    }

    public float getDirection() {
        return direction;
    }

    public void setScale(float scale) {
        if (scale != this.scale) {
            this.scale = scale / 100;

            if (image_sequence != null) {
                float scale_factor = screenPercentage_scale * this.scale;
                for (int count = 0; count < image_sequence.length; count++)
                    image_sequence[count] = Bitmap.createScaledBitmap(unrotated_image_sequence[count], (int) scale_factor, (int) ((scale_factor / unrotated_image_sequence[count].getWidth()) * unrotated_image_sequence[count].getHeight()), true);
            }

        }
    }

    public void setScale_keeprotation(float scale) {
        if (scale != this.scale) {
            this.scale = scale / 100;

            if (image_sequence != null) {
                float scale_factor = screenPercentage_scale * this.scale;
                for (int count = 0; count < image_sequence.length; count++)
                    image_sequence[count] = Bitmap.createScaledBitmap(image_sequence[count], (int) scale_factor, (int) ((scale_factor / image_sequence[count].getWidth()) * image_sequence[count].getHeight()), true);
            }

        }
    }

    //animation_______________________________________________________________________________________________________________
    public void PauseOn(int image_number) {
        pause_image_number = image_number;
    }

    public void Play() {
        pause_image_number = -1;
    }

    public boolean isPaused() {
        return (pause_image_number != -1);
    }

    //convert from sprite sheet to image array
    private Bitmap[] convert(Bitmap sprite_sheet, int itemsX, int length) {
        int itemsY = (int) Math.ceil(length / (float) itemsX);
        int tile_height = (int) (sprite_sheet.getHeight() / itemsY);
        int tile_width = (int) (sprite_sheet.getWidth() / itemsX);
        Bitmap image_sequence[] = new Bitmap[length];

        for (int y = 0; y < itemsY; y++) {
            for (int x = 0; x < itemsX; x++) {
                if ((x + (itemsX * y)) < length)
                    image_sequence[x + (itemsX * y)] = Bitmap.createBitmap(sprite_sheet, (x * tile_width), (y * tile_height), tile_width, tile_height);
            }
        }
        return image_sequence;
    }

    //draw______________________________________________________________________________________________________________________
    //draw the sprite to screen
    public void draw(Canvas canvas, float x, float y) {
        draw(canvas, x, y, imagePaint);
    }

    public void draw(Canvas canvas, float x, float y, Paint paint) {

        float centerx = x + (getWidth() / 2);
        float centery = y + (getHeight() / 2);

        //render background
        if (backgroundPaint != null) {
            if (backgroundShape == SQUARE)
                canvas.drawRect(centerx - (back_width * scale * 0.5f), centery - (back_height * scale * 0.5f), centerx + (back_width * scale * 0.5f), centery + (back_height * scale * 0.5f), backgroundPaint);
            else if (backgroundShape == CIRCLE)
                canvas.drawCircle(centerx, centery, (back_width * scale * 0.5f), backgroundPaint);

        }

        //render text
        if (textPaint != null) {
            Rect bounds = new Rect();
            textPaint.getTextBounds(text, 0, text.length(), bounds);
            canvas.drawText(text, centerx - (bounds.width() / 2), centery + (bounds.height() / 2), textPaint);
        }

        //render image/animation
        if (image_sequence != null) {
            if (pause_image_number >= 0) {
                //pause
                canvas.drawBitmap(image_sequence[pause_image_number], centerx - (image_sequence[current_image].getWidth() / 2), centery - (image_sequence[current_image].getHeight() / 2), paint);
            } else {
                //draw image
                canvas.drawBitmap(image_sequence[current_image], centerx - (image_sequence[current_image].getWidth() / 2), centery - (image_sequence[current_image].getHeight() / 2), paint);
                //update to next image
                if (image_sequence.length > 1) {
                    long now = SystemClock.uptimeMillis();
                    if (now > start_time + (500 - animation_speed)) {
                        start_time = SystemClock.uptimeMillis();
                        current_image++;
                        if (current_image + 1 > image_sequence.length)
                            current_image = 0;
                    }
                }
            }
        }
    }

    //transformations_____________________________________________________________________________________________________________

    public void setAlpha(int alpha) {
        imagePaint.setAlpha(alpha);
    }

    public void Highlight(int color) {
        ColorFilter filter = new LightingColorFilter(1, color);
        if (imagePaint != null)
            imagePaint.setColorFilter(filter);
        if (textPaint != null)
            textPaint.setColorFilter(filter);
    }

    public void unHighlight() {
        ColorFilter filter = null;
        if (imagePaint != null)
            imagePaint.setColorFilter(filter);
        if (textPaint != null)
            textPaint.setColorFilter(filter);
    }

    /**
     * Rotates the sprite by a specified angle. 0 means Top.
     *
     * @param direction The new direction of the sprite in degrees
     */
    public void rotate(float direction) {
        float angle = direction;
        this.direction = direction;

        Paint paint = new Paint();
        paint.setAntiAlias(true);
        Bitmap m = Bitmap.createBitmap((int) 5, 5, Bitmap.Config.ARGB_8888);

        //int height = (int) (unrotated_image_sequence[0].getWidth() * Math.abs(Math.sin(direction * (Math.PI / 180)))) + unrotated_image_sequence[0].getHeight();
        //int width = (int) (unrotated_image_sequence[0].getWidth() * Math.abs(Math.cos(direction * (Math.PI / 180)))) + unrotated_image_sequence[0].getHeight();
        RectF a = new RectF(0, 0, unrotated_image_sequence[0].getWidth(), unrotated_image_sequence[0].getHeight());
        Matrix mat = new Matrix();
        mat.setRotate(direction, (unrotated_image_sequence[0].getWidth() / 2), (unrotated_image_sequence[0].getHeight() / 2));
        mat.mapRect(a);

        for (int i = 0; i < image_sequence.length; i++) {
            image_sequence[i] = Bitmap.createScaledBitmap(m, (int) a.width(), (int) a.height(), true);
            Canvas canvas = new Canvas(image_sequence[i]);
            //canvas.drawColor(Color.BLUE);
            canvas.rotate(angle, image_sequence[i].getWidth() / 2, image_sequence[i].getHeight() / 2);
            canvas.drawBitmap(unrotated_image_sequence[i], (image_sequence[i].getWidth() / 2) - (unrotated_image_sequence[i].getWidth() / 2), (image_sequence[i].getHeight() / 2) - (unrotated_image_sequence[i].getHeight() / 2), paint);
            canvas.rotate(-angle, image_sequence[i].getWidth() / 2, image_sequence[i].getHeight() / 2);
        }

    }

    public void flip(int direction) {

        Matrix m = new Matrix();
        if (direction == VERTICAL)
            m.preScale(1, -1);
        else
            m.preScale(-1, 1);

        for (int i = 0; i < image_sequence.length; i++) {
            image_sequence[i] = Bitmap.createBitmap(image_sequence[i], 0, 0, image_sequence[i].getWidth(), image_sequence[i].getHeight(), m, false);
        }

    }

    public static Bitmap Scale(Bitmap unscaled, float scale) {
        return Bitmap.createScaledBitmap(unscaled, (int) ((float) (scale)), (int) (((float) (scale) / unscaled.getWidth()) * unscaled.getHeight()), true);
    }
}
