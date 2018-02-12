package com.neurondigital.nudge;

import com.neurondigital.nudge.Alert.NegativeButtonListener;
import com.neurondigital.nudge.Alert.PositiveButtonListener;
import com.neurondigital.avoidthespikes.R;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

public class Rate {
	public static int load_localpref(String identifier, Activity activity) {
		// load preferences
		SharedPreferences hiscores = PreferenceManager.getDefaultSharedPreferences(activity.getApplicationContext());
		int score = hiscores.getInt("pref" + identifier, 0);
		return score;
	}

	public static void save_localpref(int pref, String identifier, Activity activity) {
		//load preferences
		SharedPreferences hiscores = PreferenceManager.getDefaultSharedPreferences(activity.getApplicationContext());
		SharedPreferences.Editor hiscores_editor = hiscores.edit();

		hiscores_editor.putInt("pref" + identifier, pref);

		hiscores_editor.commit();
	}

	public static boolean rateWithCounter(final FragmentActivity activity) {
		if (load_localpref("rate", activity) < 100) {
			//ask user to rate
			save_localpref(load_localpref("rate", activity) + 1, "rate", activity);
			if (load_localpref("rate", activity) == activity.getResources().getInteger(R.integer.rate_shows_after_X_starts) || load_localpref("rate", activity) == (activity.getResources().getInteger(R.integer.rate_shows_after_X_starts) * 4)) {

				Alert alert = new Alert();
				alert.DisplayText(activity.getString(R.string.rate_title), activity.getString(R.string.rate_text), activity);
				alert.show(activity.getSupportFragmentManager(), activity.getString(R.string.rate_title));
				alert.setPositiveButtonListener(new PositiveButtonListener() {
					@Override
					public void onPositiveButton(String input) {
						rate(activity);
						//disable rate
						save_localpref(100, "rate", activity);

					}
				});
				alert.setNegativeButtonListener(new NegativeButtonListener() {
					@Override
					public void onNegativeButton(String input) {

					}
				});
				return true;
			}

		}
		return false;
	}

	public static void rate(Activity activity) {
		Uri uri = Uri.parse("market://details?id=" + activity.getPackageName());
		Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
		try {
			activity.startActivity(goToMarket);
		} catch (ActivityNotFoundException e) {
			Toast.makeText(activity, activity.getResources().getString(R.string.unable_to_reach_market), Toast.LENGTH_LONG).show();
		}
	}
}
