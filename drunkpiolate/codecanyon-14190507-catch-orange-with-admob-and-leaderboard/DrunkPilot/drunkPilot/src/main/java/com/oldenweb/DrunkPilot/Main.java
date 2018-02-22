package com.oldenweb.DrunkPilot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.AssetFileDescriptor;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.games.Games;
import com.google.example.games.basegameutils.BaseGameActivity;

public class Main extends BaseGameActivity {
	Handler h = new Handler();
	SharedPreferences sp;
	Editor ed;
	AnimationDrawable anim_explode;
	MediaPlayer mp;
	MediaPlayer mp_fly;
	SoundPool sndpool;
	List<ImageView> balloons = new ArrayList<ImageView>();
	List<Float> balloon_speed = new ArrayList<Float>();
	int score;
	int screen_width;
	int screen_height;
	int current_section = R.id.main;
	float x_pos;
	boolean show_leaderboard;
	boolean game_paused;
	ImageView ground0;
	ImageView ground1;
	ImageView ground2;
	ImageView hero;
	int snd_result;
	int snd_explode;
	final int speed = 4; // hero speed

	// AdMob
	AdView adMob_smart;
	InterstitialAd adMob_interstitial;
	final boolean show_admob_smart = true; // show AdMob Smart banner
	final boolean show_admob_interstitial = true; // show AdMob Interstitial

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		// fullscreen
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

		// preferences
		sp = PreferenceManager.getDefaultSharedPreferences(this);
		ed = sp.edit();

		// AdMob smart
		add_admob_smart();

		// elements
		hero = (ImageView) findViewById(R.id.hero);
		ground0 = (ImageView) findViewById(R.id.ground0);
		ground1 = (ImageView) findViewById(R.id.ground1);
		ground2 = (ImageView) findViewById(R.id.ground2);

		// bg sound
		mp = new MediaPlayer();
		try {
			AssetFileDescriptor descriptor = getAssets().openFd("snd_bg.mp3");
			mp.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
			descriptor.close();
			mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mp.setLooping(true);
			mp.setVolume(0, 0);
			mp.prepare();
			mp.start();
		} catch (Exception e) {
		}

		// fly sound
		mp_fly = new MediaPlayer();
		try {
			AssetFileDescriptor descriptor = getAssets().openFd("snd_fly.mp3");
			mp_fly.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
			descriptor.close();
			mp_fly.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mp_fly.setLooping(true);
			mp_fly.setVolume(0, 0);
			mp_fly.prepare();
			mp_fly.start();
		} catch (Exception e) {
		}

		// if mute
		if (sp.getBoolean("mute", false))
			((Switch) findViewById(R.id.config_mute)).setChecked(true);
		else
			mp.setVolume(0.5f, 0.5f);

		// volume switch listener
		((Switch) findViewById(R.id.config_mute)).setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				ed.putBoolean("mute", isChecked);
				ed.commit();

				if (isChecked)
					mp.setVolume(0, 0);
				else
					mp.setVolume(0.5f, 0.5f);
			}
		});

		// SoundPool
		sndpool = new SoundPool(3, AudioManager.STREAM_MUSIC, 0);
		try {
			snd_explode = sndpool.load(getAssets().openFd("snd_explode.mp3"), 1);
			snd_result = sndpool.load(getAssets().openFd("snd_result.mp3"), 1);
		} catch (IOException e) {
		}

		// explode animation
		anim_explode = (AnimationDrawable) getResources().getDrawable(R.drawable.explode);
		((ImageView) findViewById(R.id.explode)).setImageDrawable(anim_explode);
		anim_explode.start();

		// hero animation
		((AnimationDrawable) ((ImageView) findViewById(R.id.hero)).getDrawable()).start();

		// add balloons
		for (int i = 0; i < 8; i++) {
			ImageView balloon = new ImageView(this);
			balloon.setImageResource(R.drawable.balloon);
			((ViewGroup) findViewById(R.id.game)).addView(balloon);
			balloon.getLayoutParams().width = (int) DpToPx(32);
			balloon.getLayoutParams().height = (int) DpToPx(62);
			balloons.add(balloon);
			balloon_speed.add(0f);
		}

		// index
		findViewById(R.id.ground0).bringToFront();
		findViewById(R.id.ground1).bringToFront();
		findViewById(R.id.ground2).bringToFront();
		findViewById(R.id.hero).bringToFront();
		findViewById(R.id.explode).bringToFront();
		findViewById(R.id.txt_score).bringToFront();
		findViewById(R.id.btn_play).bringToFront();

		// custom font
		Typeface font = Typeface.createFromAsset(getAssets(), "CooperBlack.otf");
		((TextView) findViewById(R.id.txt_result)).setTypeface(font);
		((TextView) findViewById(R.id.txt_high_result)).setTypeface(font);
		((TextView) findViewById(R.id.txt_score)).setTypeface(font);
	}

	// START
	void START() {
		show_section(R.id.game);
		score = 0;
		game_paused = false;
		((TextView) findViewById(R.id.txt_score)).setText(getString(R.string.score) + " " + score);
		findViewById(R.id.explode).setX(-1000);
		findViewById(R.id.explode).setY(-1000);
		findViewById(R.id.btn_play).setVisibility(View.VISIBLE);
		((ToggleButton) findViewById(R.id.btn_play)).setChecked(true);

		// fly sound
		if (!sp.getBoolean("mute", false))
			mp_fly.setVolume(0.07f, 0.07f);

		// hero position
		findViewById(R.id.hero).setX(findViewById(R.id.hero).getWidth() / 2);
		findViewById(R.id.hero).setY(0);
		findViewById(R.id.hero).setRotation(0);
		findViewById(R.id.hero).setVisibility(View.VISIBLE);

		// ground position
		ground0.setX(0);
		ground1.setX(ground0.getX() - ground1.getWidth());
		ground2.setX(ground1.getX() - ground2.getWidth());

		// screen size
		screen_width = Math.max(findViewById(R.id.all).getWidth(), findViewById(R.id.all).getHeight());
		screen_height = Math.min(findViewById(R.id.all).getWidth(), findViewById(R.id.all).getHeight());

		// cloud
		findViewById(R.id.cloud).setX((float) (Math.random() * screen_width));
		findViewById(R.id.cloud).setY(
				(float) (Math.random() * (screen_height - findViewById(R.id.cloud).getHeight() - DpToPx(50))));

		// restart balloons
		x_pos = screen_width;
		for (int i = 0; i < balloons.size(); i++) {
			random_balloon(i);
		}

		h.post(MOVE);
	}

	// random_balloon
	void random_balloon(int i) {
		balloons.get(i).setX(x_pos);
		balloons.get(i).setY((float) (screen_height * 0.5 + Math.random() * screen_height * 0.75));
		x_pos += (findViewById(R.id.hero).getWidth() + balloons.get(i).getWidth() * 2);
		balloon_speed.set(i, DpToPx((float) (0.5 + Math.random() * 0.5)));
	}

	// MOVE
	Runnable MOVE = new Runnable() {
		@Override
		public void run() {
			if (!game_paused) {
				// move cloud
				findViewById(R.id.cloud).setX(findViewById(R.id.cloud).getX() - DpToPx((float) (speed * 0.5)));

				// cloud off screen
				if (findViewById(R.id.cloud).getX() < -findViewById(R.id.cloud).getWidth()) {
					((ImageView) findViewById(R.id.cloud)).setImageResource(getResources().getIdentifier(
							"cloud" + Math.round(Math.random() * 2), "drawable", getPackageName()));
					findViewById(R.id.cloud).setX(screen_width);
					findViewById(R.id.cloud).setY(
							(float) (Math.random() * (screen_height - findViewById(R.id.cloud).getHeight() - DpToPx(50))));
				}

				// ground
				ground0.setX(ground0.getX() - DpToPx((float) (speed * 0.75)));
				ground1.setX(ground1.getX() - DpToPx((float) (speed * 0.75)));
				ground2.setX(ground2.getX() - DpToPx((float) (speed * 0.75)));
				if (ground0.getX() < -ground0.getWidth())
					ground0.setX(ground2.getX() + ground2.getWidth());
				if (ground1.getX() < -ground0.getWidth())
					ground1.setX(ground0.getX() + ground0.getWidth());
				if (ground2.getX() < -ground0.getWidth())
					ground2.setX(ground1.getX() + ground1.getWidth());

				// balloons
				for (int i = 0; i < balloons.size(); i++) {
					// move
					balloons.get(i).setX((float) (balloons.get(i).getX() - DpToPx(speed)));
					balloons.get(i).setY(balloons.get(i).getY() - balloon_speed.get(i));

					// balloon offScreen
					if (balloons.get(i).getX() < -balloons.get(i).getWidth()) {
						// score
						score += 5;
						((TextView) findViewById(R.id.txt_score)).setText(getString(R.string.score) + " " + score);

						random_balloon(i);
					}
				}

				x_pos -= DpToPx(speed);

				if (findViewById(R.id.hero).getVisibility() == View.VISIBLE) {
					// move hero
					if (findViewById(R.id.game).isPressed()) {
						findViewById(R.id.hero).setRotation((float) Math.max(-10, findViewById(R.id.hero).getRotation() - 1));
						findViewById(R.id.hero).setY(Math.max(0, findViewById(R.id.hero).getY() - DpToPx(2)));
					} else {
						findViewById(R.id.hero).setRotation((float) Math.min(10, findViewById(R.id.hero).getRotation() + 1));
						findViewById(R.id.hero).setY(findViewById(R.id.hero).getY() + DpToPx(2));
					}

					// hero hit points
					float[] src = { DpToPx(8), DpToPx(18), DpToPx(27), DpToPx(19), DpToPx(44), DpToPx(2), DpToPx(58), DpToPx(18),
							DpToPx(74), DpToPx(28), DpToPx(58), DpToPx(38), DpToPx(26), DpToPx(38), DpToPx(8), DpToPx(34) };
					float[] dst = new float[16];
					findViewById(R.id.hero).getMatrix().mapPoints(dst, src);

					// hit hero with balloons
					for (int i = 0; i < balloons.size(); i++) {
						final RectF balloon_rect = new RectF(balloons.get(i).getX(), balloons.get(i).getY(), balloons.get(i)
								.getX() + balloons.get(i).getWidth(), balloons.get(i).getY() + DpToPx(40));

						for (int j = 0; j < dst.length; j += 2) {
							if (new RectF(dst[j], dst[j + 1], dst[j], dst[j + 1]).intersect(balloon_rect)) {
								game_over();
								h.postDelayed(MOVE, 10);
								return;
							}
						}
					}

					// hit hero with ground
					if (dst[11] > screen_height - DpToPx(30))
						game_over();
				}
			}

			h.postDelayed(MOVE, 10);
		}
	};

	// game_over
	void game_over() {
		// hide
		findViewById(R.id.hero).setVisibility(View.GONE);
		findViewById(R.id.btn_play).setVisibility(View.GONE);

		mp_fly.setVolume(0, 0);

		// message
		Toast toast = Toast.makeText(Main.this, getString(R.string.game_over), Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER, 0, 0);
		((TextView) ((LinearLayout) toast.getView()).getChildAt(0)).setTextSize(30);
		toast.show();

		// explode
		findViewById(R.id.explode).setX(
				findViewById(R.id.hero).getX() + findViewById(R.id.hero).getWidth() / 2 - findViewById(R.id.explode).getWidth()
						/ 2);
		findViewById(R.id.explode).setY(
				findViewById(R.id.hero).getY() + findViewById(R.id.hero).getHeight() / 2 - findViewById(R.id.explode).getHeight()
						/ 2);
		anim_explode.stop();
		anim_explode.start();

		// sound
		if (!sp.getBoolean("mute", false))
			sndpool.play(snd_explode, 0.4f, 0.4f, 0, 0, 1);

		h.postDelayed(STOP, 3000);
	}

	// STOP
	Runnable STOP = new Runnable() {
		@Override
		public void run() {
			// show result
			show_section(R.id.result);
			h.removeCallbacks(MOVE);

			// save score
			if (score > sp.getInt("score", 0)) {
				ed.putInt("score", score);
				ed.commit();
			}

			// show score
			((TextView) findViewById(R.id.txt_result)).setText(getString(R.string.score) + " " + score);
			((TextView) findViewById(R.id.txt_high_result)).setText(getString(R.string.high_score) + " " + sp.getInt("score", 0));

			// save score to leaderboard
			if (getApiClient().isConnected())
				Games.Leaderboards.submitScore(getApiClient(), getString(R.string.leaderboard_id), sp.getInt("score", 0));

			// sound
			if (!sp.getBoolean("mute", false))
				sndpool.play(snd_result, 0.6f, 0.6f, 0, 0, 1);

			// AdMob Interstitial
			add_admob_interstitial();
		}
	};

	// onClick
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_start:
		case R.id.btn_start2:
			START();
			break;
		case R.id.btn_config:
			show_section(R.id.config);
			break;
		case R.id.btn_home:
		case R.id.btn_home2:
			show_section(R.id.main);
			break;
		case R.id.btn_exit:
			finish();
			break;
		case R.id.btn_play:
			if (((ToggleButton) v).isChecked()) {
				game_paused = false;
				if (!sp.getBoolean("mute", false))
					mp_fly.setVolume(0.07f, 0.07f);
			} else {
				game_paused = true;
				mp_fly.setVolume(0, 0);
			}
			break;
		case R.id.btn_leaderboard:
			// show leaderboard
			show_leaderboard = true;
			if (getApiClient().isConnected())
				onSignInSucceeded();
			else
				beginUserInitiatedSignIn();
			break;
		case R.id.btn_sign:
			// Google sign in/out
			if (getApiClient().isConnected()) {
				signOut();
				onSignInFailed();
			} else
				beginUserInitiatedSignIn();
			break;
		}
	}

	@Override
	public void onBackPressed() {
		switch (current_section) {
		case R.id.main:
			super.onBackPressed();
			break;
		case R.id.config:
		case R.id.result:
			show_section(R.id.main);
			break;
		case R.id.game:
			show_section(R.id.main);
			h.removeCallbacks(MOVE);
			h.removeCallbacks(STOP);
			mp_fly.setVolume(0, 0);
			break;
		}
	}

	// show_section
	void show_section(int section) {
		current_section = section;
		findViewById(R.id.main).setVisibility(View.GONE);
		findViewById(R.id.game).setVisibility(View.GONE);
		findViewById(R.id.config).setVisibility(View.GONE);
		findViewById(R.id.result).setVisibility(View.GONE);
		findViewById(current_section).setVisibility(View.VISIBLE);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		h.removeCallbacks(MOVE);
		h.removeCallbacks(STOP);
		mp.release();
		mp_fly.release();
		sndpool.release();

		// destroy AdMob
		if (adMob_smart != null)
			adMob_smart.destroy();
	}

	// DpToPx
	float DpToPx(float dp) {
		return (dp * (getResources().getDisplayMetrics().densityDpi / 160f));
	}

	// PxToDp
	float PxToDp(float px) {
		return px / (getResources().getDisplayMetrics().densityDpi / 160f);
	}

	@TargetApi(Build.VERSION_CODES.KITKAT)
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		if (hasFocus) {
			// fullscreen mode
			if (android.os.Build.VERSION.SDK_INT >= 19) {
				getWindow().getDecorView().setSystemUiVisibility(
						View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
								| View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
								| View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
			}
		}
	}

	@Override
	public void onSignInSucceeded() {
		((Button) findViewById(R.id.btn_sign)).setText(getString(R.string.btn_sign_out));

		if (show_leaderboard) {
			// save score to leaderboard
			Games.Leaderboards.submitScore(getApiClient(), getString(R.string.leaderboard_id), sp.getInt("score", 0));

			// show leaderboard
			startActivityForResult(Games.Leaderboards.getLeaderboardIntent(getApiClient(), getString(R.string.leaderboard_id)),
					9999);
		}
		show_leaderboard = false;
	}

	@Override
	public void onSignInFailed() {
		((Button) findViewById(R.id.btn_sign)).setText(getString(R.string.btn_sign_in));
		show_leaderboard = false;
	}

	// add_admob_smart
	void add_admob_smart() {
		if (show_admob_smart
				&& ((ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo() != null) {
			adMob_smart = new AdView(this);
			adMob_smart.setAdUnitId(getString(R.string.adMob_smart));
			adMob_smart.setAdSize(AdSize.SMART_BANNER);
			((ViewGroup) findViewById(R.id.admob)).addView(adMob_smart);
			com.google.android.gms.ads.AdRequest.Builder builder = new AdRequest.Builder();
			// builder.addTestDevice(AdRequest.DEVICE_ID_EMULATOR).addTestDevice("4d0555dfcad9b000");
			adMob_smart.loadAd(builder.build());
		}
	}

	// add_admob_interstitial
	void add_admob_interstitial() {
		if (show_admob_interstitial
				&& ((ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo() != null) {
			adMob_interstitial = new InterstitialAd(this);
			adMob_interstitial.setAdUnitId(getString(R.string.adMob_interstitial));
			com.google.android.gms.ads.AdRequest.Builder builder = new AdRequest.Builder();
			// builder.addTestDevice(AdRequest.DEVICE_ID_EMULATOR).addTestDevice("4d0555dfcad9b000");
			adMob_interstitial.setAdListener(new AdListener() {
				@Override
				public void onAdLoaded() {
					super.onAdLoaded();
					adMob_interstitial.show();
				}
			});
			adMob_interstitial.loadAd(builder.build());
		}
	}
}