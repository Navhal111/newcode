package com.neurondigital.nudge;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SingleScore {

	//highscore related
	Screen screen;

	public static class Highscore {
		public int score;
		public String[] Details;
	}

	public SingleScore(Screen screen) {
		this.screen = screen;
	}

	//Local_________________________________________________________________________________________________________________________________
	public Highscore load_localscore() {
		// load preferences
		SharedPreferences hiscores = PreferenceManager.getDefaultSharedPreferences(screen.getApplicationContext());
		Highscore highscore = new Highscore();

		highscore.score = hiscores.getInt("score", 0);
		int length = hiscores.getInt("details_length", 0);
		if (length > 0) {
			highscore.Details = new String[length];
			for (int i = 0; i < highscore.Details.length; i++) {
				highscore.Details[i] = hiscores.getString("details" + i, "---");
			}
		}
		return highscore;
	}

	public void save_localscores(Highscore highscore) {
		//load preferences
		SharedPreferences hiscores = PreferenceManager.getDefaultSharedPreferences(screen.getApplicationContext());
		SharedPreferences.Editor hiscores_editor = hiscores.edit();

		hiscores_editor.putInt("score", highscore.score);
		hiscores_editor.putInt("details_length", highscore.Details.length);
		for (int i = 0; i < highscore.Details.length; i++) {
			hiscores_editor.putString("details" + i, highscore.Details[i]);
		}
		hiscores_editor.commit();
	}

	//Local score. Simple with no details______________________________________________________________________________________________________
	public int load_localscore_simple(String identifier) {
		// load preferences
		SharedPreferences hiscores = PreferenceManager.getDefaultSharedPreferences(screen.getApplicationContext());
		int score = hiscores.getInt("score" + identifier, 0);
		return score;
	}

	public void save_localscore_simple(int score, String identifier) {
		//load preferences
		SharedPreferences hiscores = PreferenceManager.getDefaultSharedPreferences(screen.getApplicationContext());
		SharedPreferences.Editor hiscores_editor = hiscores.edit();

		hiscores_editor.putInt("score" + identifier, score);

		hiscores_editor.commit();
	}

	public void save_localscore_simple(int score, String identifier, boolean largerisbetter) {

		if (largerisbetter) {
			//save score if larger than the one saved
			if (score > load_localscore_simple(identifier))
				save_localscore_simple(score, identifier);
		} else {
			//save score if smaller than the one saved
			if (score < load_localscore_simple(identifier) || load_localscore_simple(identifier) == 0)
				save_localscore_simple(score, identifier);
		}
	}

}
