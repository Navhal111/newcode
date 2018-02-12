package com.neurondigital.nudge;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.view.MotionEvent;

@Deprecated
public class Button extends Instance {
	public final int TEXT_BTN = 0, SPRITE_BTN = 1, TEXT_BOX_BTN = 2, TEXT_CIRCLE_BTN = 3;
	public int type;
	public Paint textPaint = new Paint();
	public String text;
	public Paint BackPaint = new Paint();
	public float height, width;

	/**
	 * Create new sprite button
	 * 
	 * @param sprite
	 *            sprite to bisplay on button
	 * @param x
	 *            x-coordinate to draw button
	 * @param y
	 *            y-coordinate to draw button
	 * @param screen
	 *            A reference to the main nudge engine screen instance
	 * @param world
	 *            true if you wish to draw the button relative to the camera or false if you wish to draw it relative to screen
	 */
	public Button(Sprite sprite, float x, float y, Screen screen, boolean world) {
		super(sprite, x, y, screen, world);
		type = SPRITE_BTN;
	}

	/**
	 * Create new text button
	 * 
	 * @param text
	 *            text to bisplay on button
	 * @param dpSize
	 *            size of text in dp
	 * @param font
	 *            Typface of text
	 * @param color
	 *            Color to use for text
	 * @param x
	 *            x-coordinate to draw button
	 * @param y
	 *            y-coordinate to draw button
	 * @param screen
	 *            A reference to the main nudge engine screen instance
	 * @param world
	 *            true if you wish to draw the button relative to the camera or false if you wish to draw it relative to screen
	 */
	public Button(String text, int dpSize, Typeface font, int color, float x, float y, Screen screen, boolean world) {
		super(null, x, y, screen, world);
		type = TEXT_BTN;
		textPaint = new Paint();
		textPaint.setTextSize(screen.dpToPx(dpSize));
		textPaint.setAntiAlias(true);
		textPaint.setColor(color);
		textPaint.setTypeface(font);
		this.text = text;
	}

	/**
	 * Create new rectangle button
	 * 
	 * @param text
	 *            text to bisplay on button
	 * @param dpSize
	 *            size of text in dp
	 * @param font
	 *            Typface of text
	 * @param color
	 *            Color to use for text
	 * @param x
	 *            x-coordinate to draw button
	 * @param y
	 *            y-coordinate to draw button
	 * @param height
	 *            button width
	 * @param width
	 *            button height
	 * @param screen
	 *            A reference to the main nudge engine screen instance
	 * @param world
	 *            true if you wish to draw the button relative to the camera or false if you wish to draw it relative to screen
	 */
	public Button(String text, int dpSize, Typeface font, int color, float x, float y, float height, float width, int BackColor, Screen screen, boolean world) {
		super(null, x, y, screen, world);
		type = TEXT_BOX_BTN;
		textPaint = new Paint();
		textPaint.setTextSize(screen.dpToPx(dpSize));
		textPaint.setAntiAlias(true);
		textPaint.setColor(color);
		textPaint.setTypeface(font);
		BackPaint.setColor(BackColor);
		BackPaint.setAntiAlias(true);
		this.text = text;
		this.height = height;
		this.width = width;
	}

	/**
	 * Create new round button
	 * 
	 * @param text
	 *            text to bisplay on button
	 * @param dpSize
	 *            size of text in dp
	 * @param font
	 *            Typface of text
	 * @param color
	 *            Color to use for text
	 * @param x
	 *            x-coordinate to draw button
	 * @param y
	 *            y-coordinate to draw button
	 * @param height
	 *            button width
	 * @param width
	 *            button height
	 * @param screen
	 *            A reference to the main nudge engine screen instance
	 * @param world
	 *            true if you wish to draw the button relative to the camera or false if you wish to draw it relative to screen
	 */
	public Button(String text, int dpSize, Typeface font, int color, float x, float y, float radius, int BackColor, Screen screen, boolean world) {
		super(null, x, y, screen, world);
		type = TEXT_CIRCLE_BTN;
		textPaint = new Paint();
		textPaint.setTextSize(screen.dpToPx(dpSize));
		textPaint.setAntiAlias(true);
		textPaint.setColor(color);
		textPaint.setTypeface(font);
		BackPaint.setColor(BackColor);
		BackPaint.setAntiAlias(true);
		this.text = text;
		this.height = radius * 2;
		this.width = radius * 2;
	}

	public void Highlight(int color) {
		ColorFilter filter = new LightingColorFilter(1, color);
		if (type == SPRITE_BTN)
			sprite.imagePaint.setColorFilter(filter);
		else
			textPaint.setColorFilter(filter);
	}

	public void LowLight() {
		ColorFilter filter = null;
		if (type == SPRITE_BTN)
			sprite.imagePaint.setColorFilter(filter);
		else
			textPaint.setColorFilter(filter);
	}

	@Override
	public int getWidth() {
		if (type == SPRITE_BTN)
			return super.getWidth();
		else if (height != 0 && width != 0) {
			return (int) width;
		} else {
			Rect bounds = new Rect();
			textPaint.getTextBounds(text, 0, text.length(), bounds);
			return bounds.width();
		}
	}

	@Override
	public int getHeight() {
		if (type == SPRITE_BTN)
			return super.getHeight();
		else if (height != 0 && width != 0) {
			return (int) height;
		} else {
			Rect bounds = new Rect();
			textPaint.getTextBounds(text, 0, text.length(), bounds);
			return bounds.height();
		}
	}

	//draw the sprite to screen
	@Override
	public void draw(Canvas canvas) {
		//draw background color
		if (type == TEXT_BOX_BTN) {

			Rect bounds = new Rect();
			textPaint.getTextBounds(text, 0, text.length(), bounds);

			canvas.drawRect(x, y, x + width, y + height, BackPaint);
			canvas.drawText(text, x + (getWidth() / 2) - (bounds.width() / 2), y + (getHeight() / 2), textPaint);

		} else if (type == TEXT_CIRCLE_BTN) {

			Rect bounds = new Rect();
			textPaint.getTextBounds(text, 0, text.length(), bounds);

			canvas.drawCircle(x + (width / 2), y + (height / 2), (width / 2), BackPaint);
			canvas.drawText(text, x + (getWidth() / 2) - (bounds.width() / 2), y + (getHeight() / 2) + (bounds.height() / 2), textPaint);

		} else if (type == SPRITE_BTN) {
			super.draw(canvas);
		} else {
			canvas.drawText(text, x, y + getHeight(), textPaint);
		}
		if (screen.debug_mode)
			physics.drawDebug(canvas);

	}

	@Override
	public boolean isTouched(MotionEvent event) {
		if (world)
			return physics.intersect(screen.ScreenX((int) x), screen.ScreenY((int) y), getWidth(), (int) getHeight(), (int) event.getX(), (int) event.getY());
		else
			return physics.intersect((int) x, (int) y, getWidth(), (int) getHeight(), (int) event.getX(), (int) event.getY());

	}

}
