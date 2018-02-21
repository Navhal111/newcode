package com.neurondigital.avoidthespikes;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.view.Display;
import android.view.MotionEvent;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.neurondigital.nudge.Instance;
import com.neurondigital.nudge.Instance.AnimationReadyListener;
import com.neurondigital.nudge.LeaderboardManager;
import com.neurondigital.nudge.Rate;
import com.neurondigital.nudge.Screen;
import com.neurondigital.nudge.Share;
import com.neurondigital.nudge.Sprite;

import java.util.ArrayList;
import java.util.Random;

public class MainGame extends Screen {

    //Paints
    Paint Title_Paint = new Paint();
    Paint Instruction_Paint = new Paint();
    Paint subTitle_Paint = new Paint();
    Paint score_paint = new Paint();
    Paint score_text_Paint = new Paint();
    Paint game_over_score_text_Paint = new Paint();
    Paint game_over_score_paint = new Paint();
    Paint menu_background_Paint = new Paint();
    Paint gameover_circle_Paint = new Paint();
    Paint restart_Paint = new Paint();
    Paint color_trans_overlay_Paint = new Paint();

    //states
    final int MENU = 1, GAMEPLAY = 2, GAMEOVER = 3, RESTART = 4;
    int state = MENU;
    boolean notstarted = true;

    //score
    float score = 0;
    int coins_collected = 0;
    int best_score = 0;

    //ad
    int ad_counter = 0;

    //buttons
    Instance btn_start, btn_leaderboard, btn_achievments, btn_sound, btn_exit, btn_yes, btn_no, btn_restart, btn_share;

    //screenshot holder
    Bitmap screenshot;

    //sound
    SoundPool sp;
    MediaPlayer music;
    int sound_button, sound_gameover, sound_coin;
    boolean sound_muted = false, music_muted = false;
    Sprite sound_on_sprite, sound_off_sprite;
    boolean musicpaused = false;

    //leaderboard
    LeaderboardManager leaderboardmanager;

    //game over counter
    int gameover_counter = 0;
    boolean game_over = false;

    //characters
    Character character;
    ArrayList<Instance> obstacles = new ArrayList<Instance>();
    ArrayList<Instance> coins = new ArrayList<Instance>();

    //sprites
    Sprite spike_left, spike_right, coin_sprite, coin_collected_sprite;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setDebugMode(Configuration.DEBUG_MODE);
        //initialiseAccelerometer();

        //initialise interstitial ad
        if (initialiseInterstitialAd()) {
            interstitial.setAdListener(new AdListener() {
                public void onAdLoaded() {
                    //Toast.makeText(activity, "loaded ad", Toast.LENGTH_LONG).show();
                }

                public void onAdClosed() {
                    //Toast.makeText(activity, "ad closed", Toast.LENGTH_LONG).show();
                    interstitial.loadAd(adRequest);
                    ShowingAd = false;
                    state = GAMEOVER;
                    musicpaused = false;
                }

            });
        }

        //initialise banner ad
        this.BANNER_AD_UNIT_ID = getResources().getString(R.string.BannerAd_unit_id);
        showBanner(false);

        //initialise Analytics
        initialiseAnalytics(getResources().getString(R.string.trackingId));

        //add Google Analytics view
        AnalyticsView();

        //initialise leaderboard
        leaderboardmanager = new LeaderboardManager(this, getResources().getString(R.string.app_id), new String[]{getResources().getString(R.string.leaderboard_id)});
       // leaderboardmanager.LogIn(getResources().getString(R.string.google_Play_signed_in_error), getResources().getString(R.string.google_Play_signed_in_successfully), new String[]{getResources().getString(R.string.leaderboard_id)});

        //TODO: change audio names from here---------------------------------------
        //initialise music and sound fx
        activity.setVolumeControlStream(AudioManager.STREAM_MUSIC);
        sp = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);

        //music
        music = MediaPlayer.create(activity, R.raw.music);

        //sound fx
        sound_button = sp.load(activity, R.raw.button, 1);
        sound_gameover = sp.load(activity, R.raw.gameover, 1);
        sound_coin = sp.load(activity, R.raw.coin, 1);

        //---------------------------------------------------------------------------

        //play music
        //PlayMusic();
    }

    /*This is performed once when the game starts*/
    @Override
    public void Start() {
        super.Start();
        //fonts
        Typeface mainFont = Typeface.createFromAsset(getAssets(), "PrintClearly.otf");
        Typeface secondaryFont = Typeface.createFromAsset(getAssets(), "PrintBold.otf");

        //TODO: change any fonts from here. Font sizes can be modified from the values/dimens.xml files--------
        //set paints
        //title
        Title_Paint.setTextSize((int) this.getResources().getDimension(R.dimen.title));
        Title_Paint.setTextScaleX(1.2f);
        Title_Paint.setAntiAlias(true);
        Title_Paint.setColor(getResources().getColor(R.color.color_title));
        Title_Paint.setTypeface(mainFont);

        //subTitle
        subTitle_Paint.setTextSize((int) this.getResources().getDimension(R.dimen.subtitle));
        subTitle_Paint.setAntiAlias(true);
        subTitle_Paint.setColor(getResources().getColor(R.color.color_title));
        subTitle_Paint.setTypeface(mainFont);

        //Instruction paint
        Instruction_Paint.setTextSize((int) this.getResources().getDimension(R.dimen.instruction));
        Instruction_Paint.setAntiAlias(true);
        Instruction_Paint.setColor(getResources().getColor(R.color.color_instructions));
        Instruction_Paint.setTypeface(secondaryFont);

        //score paints
        score_paint.setColor(this.getResources().getColor(R.color.color_score));
        score_paint.setAntiAlias(true);
        score_paint.setTextSize((int) this.getResources().getDimension(R.dimen.score));
        score_paint.setTypeface(secondaryFont);

        //top score paint
        score_text_Paint.setTextSize((int) this.getResources().getDimension(R.dimen.score) / 3);
        score_text_Paint.setAntiAlias(true);
        score_text_Paint.setColor(getResources().getColor(R.color.color_top_score));
        score_text_Paint.setTypeface(mainFont);

        //game over score paints
        game_over_score_paint.setColor(this.getResources().getColor(R.color.color_game_over_score));
        game_over_score_paint.setAntiAlias(true);
        game_over_score_paint.setTextSize((int) this.getResources().getDimension(R.dimen.score));
        game_over_score_paint.setTypeface(mainFont);

        //top score paint
        game_over_score_text_Paint.setTextSize((int) this.getResources().getDimension(R.dimen.score) / 3);
        game_over_score_text_Paint.setAntiAlias(true);
        game_over_score_text_Paint.setColor(getResources().getColor(R.color.color_game_over_score));
        game_over_score_text_Paint.setTypeface(mainFont);

        //restart paint
        restart_Paint.setTextSize((int) this.getResources().getDimension(R.dimen.yes_no));
        restart_Paint.setAntiAlias(true);
        restart_Paint.setColor(getResources().getColor(R.color.color_restart_text));
        restart_Paint.setTypeface(secondaryFont);

        //transparent white overlay
        menu_background_Paint.setColor(this.getResources().getColor(R.color.color_trans_white_background));
        menu_background_Paint.setAntiAlias(true);

        //transparent blue overlay
        color_trans_overlay_Paint.setColor(this.getResources().getColor(R.color.color_trans_overlay_blue));
        color_trans_overlay_Paint.setAntiAlias(true);

        //blue paint
        gameover_circle_Paint.setColor(this.getResources().getColor(R.color.color_game_over_circle));
        gameover_circle_Paint.setAntiAlias(true);


        //-------------------------------------------------------------------------------------------------------

        //set world origin
        setOrigin(BOTTOM_LEFT);

        //initialise buttons

        //start button
        Sprite btn_start_sprite = new Sprite(this.getResources().getString(R.string.Start), (int) this.getResources().getDimension(R.dimen.start), secondaryFont, this.getResources().getColor(R.color.color_start));
        btn_start_sprite.addBackground(Sprite.CIRCLE, this.getResources().getColor(R.color.color_buttons), ScreenWidth() / 2, ScreenWidth() / 2);
        btn_start = new Instance(btn_start_sprite, 0, 0, this, false);
        btn_start.y = ScreenHeight() / 2 - btn_start.getHeight() / 2;
        btn_start.x = ScreenWidth() / 2 - btn_start.getWidth() / 2;

        //leaderboard button
        Sprite btn_leaderboard_sprite = new Sprite(BitmapFactory.decodeResource(getResources(), R.drawable.highscore), ScreenWidth() * 0.07f);
        btn_leaderboard_sprite.addBackground(Sprite.CIRCLE, this.getResources().getColor(R.color.color_buttons), ScreenWidth() / 8, ScreenWidth() / 4);
        btn_leaderboard = new Instance(btn_leaderboard_sprite, 0, 0, this, false);
        btn_leaderboard.y = ScreenHeight() - btn_leaderboard.getHeight() * 1f;
        btn_leaderboard.x = ScreenWidth() / 2 - btn_leaderboard.getWidth() * 1.1f;

        //achievments button
        Sprite btn_achievments_sprite = new Sprite(BitmapFactory.decodeResource(getResources(), R.drawable.achievments), ScreenWidth() * 0.05f);
        btn_achievments_sprite.addBackground(Sprite.CIRCLE, this.getResources().getColor(R.color.color_buttons), ScreenWidth() / 8, ScreenWidth() / 4);
        btn_achievments = new Instance(btn_achievments_sprite, 0, 0, this, false);
        btn_achievments.y = ScreenHeight() - btn_achievments.getHeight() * 1f;
        btn_achievments.x = ScreenWidth() / 2 + btn_achievments.getWidth() * 0.1f;

        //sound button
        sound_off_sprite = new Sprite(BitmapFactory.decodeResource(getResources(), R.drawable.sound_off), ScreenWidth() * 0.1f);
        sound_off_sprite.addBackground(Sprite.CIRCLE, this.getResources().getColor(R.color.color_buttons), ScreenWidth() / 5, ScreenWidth() / 5);

        sound_on_sprite = new Sprite(BitmapFactory.decodeResource(getResources(), R.drawable.sound_on), ScreenWidth() * 0.1f);
        sound_on_sprite.addBackground(Sprite.CIRCLE, this.getResources().getColor(R.color.color_buttons), ScreenWidth() / 5, ScreenWidth() / 5);

        btn_sound = new Instance(sound_on_sprite, 0, 0, this, false);
        btn_sound.y = ScreenHeight() - btn_sound.getHeight() * 1.2f;
        btn_sound.x = ScreenWidth() / 2 - btn_leaderboard.getWidth() / 2 - btn_sound.getWidth() * 1.5f;

        //exit button
        Sprite btn_exit_sprite = new Sprite(BitmapFactory.decodeResource(getResources(), R.drawable.exit), ScreenWidth() * 0.1f);
        btn_exit_sprite.addBackground(Sprite.CIRCLE, this.getResources().getColor(R.color.color_buttons), ScreenWidth() / 5, ScreenWidth() / 5);
        btn_exit = new Instance(btn_exit_sprite, 0, 0, this, false);
        btn_exit.y = ScreenHeight() - btn_exit.getHeight() * 1.2f;
        btn_exit.x = ScreenWidth() / 2 + btn_leaderboard.getWidth() / 2 + btn_sound.getWidth() * 0.5f;

        //menu button
        Sprite btn_menu_sprite = new Sprite(BitmapFactory.decodeResource(getResources(), R.drawable.replay), ScreenWidth() * 0.1f);
        btn_menu_sprite.addBackground(Sprite.CIRCLE, this.getResources().getColor(R.color.color_buttons), ScreenWidth() / 5, ScreenWidth() / 5);
        btn_restart = new Instance(btn_menu_sprite, 0, 0, this, false);
        btn_restart.y = ScreenHeight() - btn_exit.getHeight() * 1.2f;
        btn_restart.x = ScreenWidth() / 2 + btn_leaderboard.getWidth() / 2 + btn_sound.getWidth() * 0.5f;

        //restart yes button
        btn_yes = new Instance(new Sprite(this.getResources().getString(R.string.Yes), (int) this.getResources().getDimension(R.dimen.yes_no), secondaryFont, this.getResources().getColor(R.color.color_buttons)), 0, 0, this, false);
        btn_yes.y = ScreenHeight() * 0.55f;
        btn_yes.x = ScreenWidth() / 2 - btn_yes.getWidth() * 2;

        //restart button no
        btn_no = new Instance(new Sprite(this.getResources().getString(R.string.No), (int) this.getResources().getDimension(R.dimen.yes_no), secondaryFont, this.getResources().getColor(R.color.color_buttons)), 0, 0, this, false);
        btn_no.y = ScreenHeight() * 0.55f;
        btn_no.x = ScreenWidth() / 2 + btn_no.getWidth();

        //share button
        btn_share = new Instance(new Sprite(this.getResources().getString(R.string.Share), (int) this.getResources().getDimension(R.dimen.share), secondaryFont, this.getResources().getColor(R.color.color_share_btn)), 0, 0, this, false);
        btn_share.y = ScreenHeight() / 2 + ScreenWidth() / 4 + btn_share.getHeight() / 2;
        btn_share.x = ScreenWidth() / 2 - btn_share.getWidth() / 2;

        //initialise character
        character = new Character(this);

        //initialise spike
        spike_left = new Sprite(BitmapFactory.decodeResource(getResources(), R.drawable.spike_left), ScreenWidth() * 0.05f);
        spike_right = new Sprite(BitmapFactory.decodeResource(getResources(), R.drawable.spike_right), ScreenWidth() * 0.05f);

        coin_sprite = new Sprite(BitmapFactory.decodeResource(getResources(), R.drawable.newcoin), ScreenWidth() * 0.05f);
        coin_collected_sprite = new Sprite(BitmapFactory.decodeResource(getResources(), R.drawable.coin), ScreenWidth() * 0.08f);


    }

    /*This is performed before every screen refresh, just before drawing*/
    @Override
    synchronized public void Step() {
        super.Step();

        if (state == MENU) {

            btn_leaderboard.Update();
            btn_achievments.Update();
            btn_start.Update();
            btn_sound.Update();
            btn_exit.Update();
            btn_share.Update();

        } else if (state == GAMEPLAY) {
            //things to pause before game starts
            if (!notstarted) {

                //check if game over delay has ended, thus call game over.
                if (game_over)
                    gameover_counter++;
                else
                    gameover_counter = 0;
                if (gameover_counter > Configuration.GAME_OVER_DELAY)
                    GameOver();

                //update character
                character.Update();

                //move camera and add score according to height
                if (ScreenY(character.y) < ScreenHeight() / 2) {
                    cameraY += (ScreenHeight() / 2) - ScreenY(character.y);
                    score += 0.2f;

                    if(score%100 == 0){
                        Toast.makeText(activity, "Cross 100", Toast.LENGTH_SHORT).show();
                    }
                }

                //if character hits spikes game over
                if (character.CollidedWith(ScreenWidth() - spike_right.getWidth(), 0, spike_right.getWidth(), ScreenHeight(), false)) {
                    if (!game_over)
                        StartGameOverCountDown();
                    character.speedx = -Math.abs(character.speedx);
                    character.fall();
                }
                if (character.CollidedWith(0, 0, spike_left.getWidth(), ScreenHeight(), false)) {
                    if (!game_over)
                        StartGameOverCountDown();

                    character.speedx = Math.abs(character.speedx);
                    character.fall();
                }

                //add obstacle only if last added obstacle has moved OBSTACLE_SEPARATION down
                if (obstacles.size() > 0) {
                    int separation = Configuration.OBSTACLE_SEPARATION;
                    if (cameraY > 50)
                        separation /= 1.2f;
                    if (cameraY > 200)
                        separation /= 1.2f;
                    if (cameraY > 500)
                        separation /= 1.2f;

                    if (ScreenY(obstacles.get(obstacles.size() - 1).y) > ((ScreenHeight() * separation * 0.002f) - (ScreenHeight() * 0.5f))) {
                        addObstacle();
                    }
                } else {
                    addObstacle();
                }


                //loop through all obstacles and handle them
                for (int i = obstacles.size() - 1; i >= 0; i--) {
                    Obstacle current_obstacle = (Obstacle) obstacles.get(i);

                    //update obstacles
                    current_obstacle.Update();

                    //handle collision of obstacles with character
                    if (character.CollidedWith(current_obstacle)) {
                        if (!game_over) {
                            character.fall();
                            character.speedx = -character.speedx;
                            StartGameOverCountDown();
                        }
                    }

                    //remove obstacles that go out of screen
                    if (ScreenY(current_obstacle.y) > ScreenHeight()) {
                        obstacles.remove(i);
                    }

                    //remove coin if it is over obstacle
                    for (int coin_i = coins.size() - 1; coin_i >= 0; coin_i--) {
                        Instance current_coin = coins.get(coin_i);
                        if (current_obstacle.CollidedWith(current_coin)) {
                            coins.remove(coin_i);
                        }
                    }

                }

                //add coin randomly
                Random random = new Random();
                int chance = random.nextInt(100 - Configuration.COIN_PROBABILITY);
                if (chance == 0) {

                    random = new Random();
                    float x = (random.nextFloat() * 0.8f) + 0.1f;

                    Instance coin = new Instance(coin_sprite, (ScreenWidth() * x), (ScreenHeight() * 1.5f) + cameraY, this, true);
                    coins.add(coin);
                }

                //loop through all coins
                for (int coin_i = coins.size() - 1; coin_i >= 0; coin_i--) {
                    Instance current_coin = coins.get(coin_i);
                    current_coin.Update();
                    if (character.CollidedWith(current_coin)) {
                        coins.remove(coin_i);
                        score += 10;
                        coins_collected++;

                        if (sound_coin != 0 && !sound_muted)
                            sp.play(sound_coin, 1, 1, 0, 0, 1);
                    }

                    //remove obstacles that go out of screen
                    if (ScreenY(current_coin.y) > ScreenHeight()) {
                        coins.remove(coin_i);
                    }
                }
            }

        } else if (state == GAMEOVER) {
            //update buttons
            btn_leaderboard.Update();
            btn_achievments.Update();
            btn_restart.Update();
            btn_sound.Update();
            btn_exit.Update();

        } else if (state == RESTART) {
            //update buttons
            btn_yes.Update();
            btn_no.Update();
        }

    }

    @Override
    public synchronized void onAccelerometer(PointF point) {

    }

    @Override
    public synchronized void BackPressed() {
        //Android back button pressed
        if (state == MENU) {
            Exit();
        } else if (state == GAMEPLAY) {
            state = RESTART;
            StopMusic();
        } else if (state == RESTART) {
            state = GAMEPLAY;
            if (!music_muted)
                PlayMusic();
        } else if (state == GAMEOVER) {
            state = MENU;
            wait_dont_show_adbanner_now = false;
        }
    }

    @Override
    public synchronized void onTouch(float TouchX, float TouchY, MotionEvent event) {

        if (state == MENU) {
            //screen touched in menu
            if (event.getAction() == MotionEvent.ACTION_DOWN) {

                if (btn_start.isTouched(event)) {
                    btn_start.animateScale(120, 5, 1);
                }

                if (btn_sound.isTouched(event)) {
                    btn_sound.animateScale(120, 8, 1);
                }
                if (getResources().getString(R.string.app_id).length() > 0) {
                    if (btn_leaderboard.isTouched(event)) {
                        btn_leaderboard.animateScale(120, 8, 1);
                    }

                    if (btn_achievments.isTouched(event)) {
                        btn_achievments.animateScale(120, 8, 1);
                    }
                }

                if (btn_exit.isTouched(event)) {
                    btn_exit.animateScale(120, 8, 1);
                }
            }

            //screen released in menu
            if (event.getAction() == MotionEvent.ACTION_UP) {
                btn_leaderboard.animateScale(100, 10, 2);
                btn_achievments.animateScale(100, 10, 2);
                if (getResources().getString(R.string.app_id).length() > 0) {
                    if (btn_leaderboard.isTouched(event)) {
                        if (sound_button != 0 && !sound_muted)
                            sp.play(sound_button, 1, 1, 0, 0, 1);
                        // open leaderboard
                        leaderboardmanager.OpenLeaderboard(getResources().getString(R.string.leaderboard_id));
                    }

                    if (btn_achievments.isTouched(event)) {
                        if (sound_button != 0 && !sound_muted)
                            sp.play(sound_button, 1, 1, 0, 0, 1);
                        // open achievements
                        leaderboardmanager.OpenAchievements();
                    }
                }

                btn_start.animateScale(100, 10, 2);
                btn_start.setAnimationReadyListener(new AnimationReadyListener() {
                    @Override
                    public void onAnimationReady(int id) {
                        if (id == 2) {
                            if (!Rate.rateWithCounter(activity))
                                StartGame();
                            if (sound_button != 0 && !sound_muted)
                                sp.play(sound_button, 1, 1, 0, 0, 1);
                        }
                    }
                });

                //sound button released
                btn_sound.animateScale(100, 8, 2);
                btn_sound.setAnimationReadyListener(new AnimationReadyListener() {
                    @Override
                    public void onAnimationReady(int id) {
                        if (id == 2) {
                            toggleSound();
                        }
                    }
                });

                btn_exit.animateScale(100, 8, 2);
                if (btn_exit.isTouched(event)) {
                    Exit();
                }

            }

        } else if (state == RESTART) {
            //screen touched in reset screen
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                if (btn_no.isTouched(event)) {
                    btn_no.Highlight(getResources().getColor(R.color.color_restart_text_highlight));
                }
                if (btn_yes.isTouched(event)) {
                    btn_yes.Highlight(getResources().getColor(R.color.color_restart_text_highlight));
                }
            }
            //screen released in reset screen
            if (event.getAction() == MotionEvent.ACTION_UP) {
                btn_no.unHighlight();
                btn_yes.unHighlight();

                if (btn_no.isTouched(event)) {
                    //no pressed
                    state = GAMEPLAY;
                    if (sound_button != 0 && !sound_muted)
                        sp.play(sound_button, 1, 1, 0, 0, 1);
                    if (!music_muted)
                        PlayMusic();
                }
                if (btn_yes.isTouched(event)) {
                    //yes pressed
                    state = MENU;
                    //reload screen if ad shown during gameplay
                    wait_dont_show_adbanner_now = false;

                    if (sound_button != 0 && !sound_muted)
                        sp.play(sound_button, 1, 1, 0, 0, 1);
                }
            }

        } else if (state == GAMEOVER) {
            //screen touched in game over screen
            if (event.getAction() == MotionEvent.ACTION_DOWN) {

                if (btn_sound.isTouched(event)) {
                    btn_sound.animateScale(120, 8, 1);
                }

                if (getResources().getString(R.string.app_id).length() > 0) {
                    if (btn_leaderboard.isTouched(event)) {
                        btn_leaderboard.animateScale(120, 8, 1);
                    }

                    if (btn_achievments.isTouched(event)) {
                        btn_achievments.animateScale(120, 8, 1);
                    }
                }

                if (btn_restart.isTouched(event)) {
                    btn_restart.animateScale(120, 8, 1);
                }
                if (btn_share.isTouched(event)) {
                    btn_share.Highlight(getResources().getColor(R.color.color_share_btn_highlight));
                }
            }

            //screen released in game over screen
            if (event.getAction() == MotionEvent.ACTION_UP) {
                //leaderboard button released
                btn_leaderboard.animateScale(100, 10, 2);
                btn_achievments.animateScale(100, 10, 2);
                if (getResources().getString(R.string.app_id).length() > 0) {
                    if (btn_leaderboard.isTouched(event)) {
                        if (sound_button != 0 && !sound_muted)
                            sp.play(sound_button, 1, 1, 0, 0, 1);
                        // open leaderboard
                        leaderboardmanager.OpenLeaderboard(getResources().getString(R.string.leaderboard_id));
                    }

                    if (btn_achievments.isTouched(event)) {
                        if (sound_button != 0 && !sound_muted)
                            sp.play(sound_button, 1, 1, 0, 0, 1);
                        // open achievements
                        leaderboardmanager.OpenAchievements();
                    }
                }

                //sound button released
                btn_sound.animateScale(100, 8, 2);
                btn_sound.setAnimationReadyListener(new AnimationReadyListener() {
                    @Override
                    public void onAnimationReady(int id) {
                        if (id == 2) {
                            toggleSound();
                        }
                    }
                });

                //restart game
                btn_restart.animateScale(100, 8, 2);
                if (btn_restart.isTouched(event)) {
                    StartGame();
                    if (sound_button != 0 && !sound_muted)
                        sp.play(sound_button, 1, 1, 0, 0, 1);
                }

                //share screenshot
                btn_share.unHighlight();
                if (btn_share.isTouched(event)) {
                    share();
                    if (sound_button != 0 && !sound_muted)
                        sp.play(sound_button, 1, 1, 0, 0, 1);
                }

            }
        } else if (state == GAMEPLAY) {
            if (!game_over) {
                //screen tapped during game play
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    //start game
                    if (notstarted)
                        notstarted = false;

                    //change character direction
                    character.toggleDirection();

                }

            }
        }

    }

    //..................................................Game Functions..................................................................................................................................

    public void StartGame() {

        //refresh character
        character.reset();

        //refresh obstacles
        obstacles.clear();
        Obstacle obstacle = new Obstacle((ScreenWidth() * 0.5f), (ScreenHeight() * 0.5f), this);
        obstacles.add(obstacle);

        //refresh score
        score = 0;
        coins_collected = 0;

        //refresh camera
        cameraY = 0;
        cameraX = 0;

        //get top score from leaderboard
        best_score = leaderboardmanager.getTopScore(getResources().getString(R.string.leaderboard_id));

        //not started
        notstarted = true;
        game_over = false;
        state = GAMEPLAY;

        if (!music_muted)
            PlayMusic();

        //dont show banner ad during game play if not loaded
        wait_dont_show_adbanner_now = true;
    }

    //handle game over
    public synchronized void GameOver() {

        //stop music
        StopMusic();

        //save score
        leaderboardmanager.updateScore(getScore(), true, getResources().getString(R.string.leaderboard_id));

        //unlock achievements
        if (coins_collected >= 20)
            leaderboardmanager.unlockAchievement(getResources().getString(R.string.Achievement_id_20_coins));
        if (coins_collected >= 30)
            leaderboardmanager.unlockAchievement(getResources().getString(R.string.Achievement_id_30_coins));
        if (coins_collected >= 40)
            leaderboardmanager.unlockAchievement(getResources().getString(R.string.Achievement_id_40_coins));
        if (coins_collected >= 50)
            leaderboardmanager.unlockAchievement(getResources().getString(R.string.Achievement_id_50_coins));
        if (coins_collected >= 100)
            leaderboardmanager.unlockAchievement(getResources().getString(R.string.Achievement_id_100_coins));

        //load best score
        best_score = leaderboardmanager.getTopScore(getResources().getString(R.string.leaderboard_id));

        //switch state to game over
        state = GAMEOVER;

        // show banner ad after game play if not loaded
        wait_dont_show_adbanner_now = false;
    }

    /*Start game over countdown. The delay can be changed by changing GAMEOVER_DELAY*/
    public void StartGameOverCountDown() {
        //play gameover sound
        if (sound_gameover != 0 && !sound_muted)
            sp.play(sound_gameover, 1, 1, 0, 0, 1);

        //show interstitial ad if its time has come
        ad_counter++;
        if (ad_counter >= getResources().getInteger(R.integer.ad_shows_every_X_gameovers)) {
            openInterstitialAd();
            ad_counter = 0;
        }

        game_over = true;
    }

    /*Add Obstacle to screen*/
    public void addObstacle() {

        Random random = new Random();
        float x = (random.nextFloat() * 0.8f) + 0.1f;

        random = new Random();
        int double_obstacle = random.nextInt(3);


        Obstacle obstacle = new Obstacle((ScreenWidth() * x), (ScreenHeight() * 1.5f) + cameraY, this);
        obstacles.add(obstacle);

        if (x < 0.7f & (double_obstacle == 1 || double_obstacle == 2)) {
            Obstacle obstacle2 = new Obstacle((ScreenWidth() * x) + obstacle.getWidth(), (ScreenHeight() * 1.5f) + cameraY, this);
            obstacles.add(obstacle2);
        }

        if (x < 0.6f & double_obstacle == 2) {
            Obstacle obstacle3 = new Obstacle((ScreenWidth() * x) + (obstacle.getWidth() * 2), (ScreenHeight() * 1.5f) + cameraY, this);
            obstacles.add(obstacle3);
        }
    }

    public int getScore() {
        return (int) Math.floor(score);
    }

    //share screenshot
    public void share() {
        //share
        Share sharer = new Share();
        screenshot = Bitmap.createBitmap(ScreenWidth(), ScreenHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas();
        canvas.setBitmap(screenshot);
        int temp_state = state;
        state = GAMEPLAY;
        Draw(canvas);
        state = temp_state;
        sharer.share_screenshot(this, screenshot);
    }

    //sound
    public void PlayMusic() {
        if (!music_muted) {
            music = MediaPlayer.create(activity, R.raw.music);
            music.start();
            music.setVolume(0.4f, 0.4f);
            music.setLooping(true);
        }
    }

    public void StopMusic() {
        if (music != null)
            music.stop();
    }

    public void toggleMusic() {
        if (music_muted) {

            music_muted = false;
            PlayMusic();
        } else {
            music_muted = true;
            StopMusic();
        }
    }

    public void toggleSoundFx() {
        if (sound_muted) {
            sound_muted = false;
            btn_sound.sprite = sound_on_sprite;
        } else {
            sound_muted = true;
            btn_sound.sprite = sound_off_sprite;
        }
    }

    public void toggleSound() {
        if (sound_muted) {
            sound_muted = false;
            music_muted = false;
            btn_sound.sprite = sound_on_sprite;
            //PlayMusic();
        } else {
            sound_muted = true;
            music_muted = true;
            btn_sound.sprite = sound_off_sprite;
            StopMusic();
        }
    }

    //...................................................Rendering of screen............................................................................................................................
    @Override
    public void Draw(Canvas canvas) {

        //draw background
        renderBackground(canvas);

        //MENU_____________________________________________________________________________________________
        if (state == MENU) {

            //draw buttons
            btn_start.draw(canvas);
            if (getResources().getString(R.string.app_id).length() > 0)
                btn_leaderboard.draw(canvas);
            if (getResources().getString(R.string.app_id).length() > 0)
                btn_achievments.draw(canvas);
            btn_sound.draw(canvas);
            btn_exit.draw(canvas);

            //draw title
            canvas.drawText(getResources().getString(R.string.Title), (ScreenWidth() / 2) - (Title_Paint.measureText(getResources().getString(R.string.Title)) / 2), (float) (ScreenHeight() * 0.10f), Title_Paint);

            //draw subtitle
            Rect bounds = new Rect();
            Title_Paint.getTextBounds(getResources().getString(R.string.Title), 0, getResources().getString(R.string.Title).length(), bounds);
            canvas.drawText(getResources().getString(R.string.Subtitle), (ScreenWidth() / 2) - (subTitle_Paint.measureText(getResources().getString(R.string.Subtitle)) / 2), (float) (ScreenHeight() * 0.10f) + (bounds.height() * 1.5f), subTitle_Paint);

        }

        //GAMEPLAY/RESTART/GAMEOVER_____________________________________________________________________________________________
        else if (state == GAMEPLAY || state == RESTART || state == GAMEOVER) {
            //draw spikes
            for (int y = 0; y < (ScreenHeight() / spike_left.getHeight() + 1); y++) {
                float spike_y = cameraY - ((float) (Math.floor(cameraY / ScreenHeight()) * ScreenHeight())) + (y * spike_left.getHeight());
                if (spike_y > ScreenHeight())
                    spike_y -= ScreenHeight() + spike_left.getHeight();
                spike_left.draw(canvas, 0, spike_y);
                spike_right.draw(canvas, ScreenWidth() - spike_right.getWidth(), spike_y);
            }

            //draw all coins
            for (int coin_i = coins.size() - 1; coin_i >= 0; coin_i--) {
                coins.get(coin_i).draw(canvas);
            }

            //draw obstacles
            for (int i = 0; i < obstacles.size(); i++) {
                obstacles.get(i).draw(canvas);
            }

            //draw character
            character.draw(canvas);

            //draw coins collected
            Rect coin_bounds = new Rect();
            score_text_Paint.getTextBounds(this.getResources().getString(R.string.Top) + " " + best_score, 0, (this.getResources().getString(R.string.Top) + " " + best_score).length(), coin_bounds);
            coin_collected_sprite.draw(canvas, coin_collected_sprite.getWidth() / 2, coin_collected_sprite.getHeight() / 2);
            canvas.drawText("" + coins_collected, coin_collected_sprite.getWidth() * 1.8f, coin_collected_sprite.getHeight() + coin_bounds.height() * 0.5f, score_text_Paint);

            //draw the following when state == GAMEPLAY/RESTART
            if (state != GAMEOVER) {
                //score
                Rect bounds = new Rect();
                score_paint.getTextBounds("" + getScore(), 0, ("" + getScore()).length(), bounds);
                Rect bounds_2 = new Rect();
                score_text_Paint.getTextBounds(this.getResources().getString(R.string.Top) + " " + best_score, 0, (this.getResources().getString(R.string.Top) + " " + best_score).length(), bounds_2);
                canvas.drawText("" + getScore(), (ScreenWidth() / 2) - (bounds.width() / 2), ScreenHeight() / 7, score_paint);

                //top score
                canvas.drawText(this.getResources().getString(R.string.Top) + " " + best_score, (ScreenWidth() / 2) - (bounds_2.width() / 2), 20 + (bounds.height() / 2) + bounds_2.height() * 0.5f, score_text_Paint);

            }

            //before game starts
            if (notstarted) {
                //draw instructions
                StaticLayout instructionlayout = new StaticLayout(getResources().getString(R.string.Tap_to_start), new TextPaint(Instruction_Paint), (int) ((ScreenWidth() / 1.60)), Layout.Alignment.ALIGN_CENTER, 1.2f, 1f, false);
                canvas.translate((ScreenWidth() / 2) - (ScreenWidth() / 3.3f), (ScreenHeight() / 2) - instructionlayout.getHeight() / 2); //position the text
                instructionlayout.draw(canvas);
                canvas.translate(-((ScreenWidth() / 2) - (ScreenWidth() / 3.3f)), -((ScreenHeight() / 2) - instructionlayout.getHeight() / 2)); //position the text
            }

            //restart menu
            if (state == RESTART) {
                //draw white overlay
                canvas.drawRect(0, 0, ScreenWidth(), ScreenHeight(), menu_background_Paint);

                //draw restart
                canvas.drawText(getResources().getString(R.string.exit_question), (ScreenWidth() / 2) - (restart_Paint.measureText(getResources().getString(R.string.exit_question)) / 2), (float) (ScreenHeight() * 0.45f), restart_Paint);

                //draw restart buttons
                btn_no.draw(canvas);
                btn_yes.draw(canvas);
            }
        }

        //GAMEOVER ONLY. Will be drawn on top of GamePlay_____________________________________________________________________________________________
        if (state == GAMEOVER) {
            //draw white overlay
            canvas.drawRect(0, 0, ScreenWidth(), ScreenHeight(), color_trans_overlay_Paint);

            //draw gameover
            Rect bounds = new Rect();
            Title_Paint.getTextBounds(getResources().getString(R.string.game_over), 0, getResources().getString(R.string.game_over).length(), bounds);
            canvas.drawText(getResources().getString(R.string.game_over), (ScreenWidth() / 2) - (bounds.width() / 2), ScreenHeight() / 6 + (bounds.height() / 2), Title_Paint);

            //score circle
            canvas.drawCircle(ScreenWidth() / 2, ScreenHeight() / 2, ScreenWidth() / 4, gameover_circle_Paint);

            //score
            game_over_score_paint.getTextBounds("" + getScore(), 0, ("" + getScore()).length(), bounds);
            canvas.drawText("" + getScore(), (ScreenWidth() / 2) - (bounds.width() / 2), (ScreenHeight() * 0.5f) + bounds.height() * 0.5f, game_over_score_paint);

            //top score
            Rect bounds_2 = new Rect();
            game_over_score_text_Paint.getTextBounds(this.getResources().getString(R.string.Top) + " " + best_score, 0, (this.getResources().getString(R.string.Top) + " " + best_score).length(), bounds_2);
            canvas.drawText(this.getResources().getString(R.string.Top) + " " + best_score, (ScreenWidth() / 2) - (bounds_2.width() / 2), (ScreenHeight() * 0.5f) + (bounds.height() / 2) + (bounds_2.height() * 2f), game_over_score_text_Paint);

            //draw buttons
            btn_restart.draw(canvas);
            if (getResources().getString(R.string.app_id).length() > 0)
                btn_leaderboard.draw(canvas);
            if (getResources().getString(R.string.app_id).length() > 0)
                btn_achievments.draw(canvas);
            btn_sound.draw(canvas);
            btn_share.draw(canvas);
        }

        //physics.drawDebug(canvas);
        super.Draw(canvas);

    }

    //Rendering of background
    public void renderBackground(Canvas canvas) {
        //TODO: you may wish to change background colors from here
        canvas.drawColor(getResources().getColor(R.color.color_background));
//        Display display = getWindowManager().getDefaultDisplay();
//        int dwidth = display.getWidth();
//        int dheight = display.getHeight();
//        Bitmap background1 = BitmapFactory.decodeResource(getResources(),R.drawable.bg);
//        Bitmap BSunny = Bitmap.createScaledBitmap(background1,dwidth,dheight,true);
//        canvas.drawBitmap(BSunny, 0, 0, null);
//        canvas.draw

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        leaderboardmanager.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    protected void onResume() {
        //when game continues after being closed
        super.onResume();
        if (musicpaused) {
            //music_muted = false;
            //btn_music_mute.sprite = music_on;
            PlayMusic();
            musicpaused = false;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onPause() {
        if (!music_muted && state == GAMEPLAY) {
            // music_muted = true;
            //btn_music_mute.sprite = music_off;
            StopMusic();
            musicpaused = true;
        }
        super.onPause();
    }

    @Override
    public void onDestroy() {
        StopMusic();
        super.onDestroy();
    }

    @Override
    protected void onStart() {
        super.onStart();
        leaderboardmanager.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        leaderboardmanager.onStop();
    }

}
