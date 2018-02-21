package com.neurondigital.nudge;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.view.View;
import android.widget.Toast;

public class Share {

	public void share_screenshot(Screen screen, Bitmap bitmap) {

		Uri path = saveBitmap(bitmap, "screenshot.jpg", screen);
		System.out.println("Sharing");
		if (path != null) {
			Intent share = new Intent(Intent.ACTION_SEND);
			share.setType("image/jpeg");
			share.putExtra(Intent.EXTRA_STREAM, path);
			screen.startActivity(Intent.createChooser(share, "share"));
			System.out.println("shared");
		}

	}

	private static Bitmap takeScreenShot(Screen screen) {
		View view = screen.layout;
		Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(returnedBitmap);
		Drawable bgDrawable = view.getBackground();
		if (bgDrawable != null)
			bgDrawable.draw(canvas);
		else
			canvas.drawColor(Color.WHITE);
		view.draw(canvas);
		return returnedBitmap;

	}

	private static Uri saveBitmap(Bitmap bitmap, String BitmapName, Activity activity) {
		FileOutputStream fos = null;
		try {
			File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), BitmapName);
			fos = new FileOutputStream(file);

			if (null != fos) {
				bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
				fos.flush();
				fos.close();
				System.out.println("saved");

			}

			return Uri.fromFile(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			Toast.makeText(activity, "You need to have an SDCard to share screenshot", Toast.LENGTH_LONG).show();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;

	}
}
