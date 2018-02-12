package com.neurondigital.nudge;

import android.content.Intent;
import android.widget.Toast;

import com.google.android.gms.games.Games;
import com.google.android.gms.games.GamesActivityResultCodes;
import com.google.example.games.basegameutils.GameHelper;
import com.google.example.games.basegameutils.GameHelper.GameHelperListener;
import com.neurondigital.avoidthespikes.R;

public class LeaderboardManager {

    //Google Play leaderboard
    public GameHelper mHelper;
    public String App_id;
    Screen screen;
    int OPEN_LEADERBOARD = 2154;
    int OPEN_ACHIEVMENTS = 1458;

    String[] leaderboard_ids;
    GameHelperListener listener;

    public LeaderboardManager(Screen Screen, String App_id, String[] Leaderboard_ids) {
        this.screen = Screen;
        this.App_id = App_id;
        this.leaderboard_ids = Leaderboard_ids;

        //initialise and login
        if (App_id.length() > 0) {
            mHelper = new GameHelper(screen, GameHelper.CLIENT_GAMES);
            mHelper.enableDebugLog(true);
            listener = new GameHelper.GameHelperListener() {
                @Override
                public void onSignInSucceeded() {
                    // handle sign-in succeess
                    Toast.makeText(screen, screen.getResources().getString(R.string.google_Play_signed_in_successfully), Toast.LENGTH_LONG).show();

                    //upload locally saved scores
                    SingleScore hiscore = new SingleScore(screen);
                    for (int i = 0; i < leaderboard_ids.length; i++) {
                        Games.Leaderboards.submitScore(mHelper.getApiClient(), leaderboard_ids[i], hiscore.load_localscore_simple(leaderboard_ids[i]));
                    }

                }

                @Override
                public void onSignInFailed() {
                    // handle sign-in failure (e.g. show Sign In button)
                    Toast.makeText(screen, screen.getResources().getString(R.string.google_Play_signed_in_error), Toast.LENGTH_LONG).show();
                }

            };
            mHelper.setup(listener);

           // LogIn();
        }
    }

    public void LogIn() {
        if (App_id.length() > 0) {
            mHelper.beginUserInitiatedSignIn();
        }
    }

    public void OpenLeaderboard(String leaderboard_id) {
        if (App_id.length() > 0) {
            if (mHelper.isSignedIn())
                screen.startActivityForResult(Games.Leaderboards.getLeaderboardIntent(mHelper.getApiClient(), leaderboard_id), OPEN_LEADERBOARD);
            else {
                // if (!mHelper.getApiClient().isConnecting())
                //    mHelper.getApiClient().connect();
                LogIn();
            }
        }
    }


    public void OpenAllLeaderBoards() {
        if (App_id.length() > 0) {
            if (mHelper.isSignedIn())
                screen.startActivityForResult(Games.Leaderboards.getAllLeaderboardsIntent(mHelper.getApiClient()), OPEN_LEADERBOARD);
            else {
                // if (!mHelper.getApiClient().isConnecting())
                //    mHelper.getApiClient().connect();
                LogIn();
            }
        }
    }


    public void OpenAchievements() {
        if (App_id.length() > 0) {
            if (mHelper.isSignedIn())
                screen.startActivityForResult(Games.Achievements.getAchievementsIntent(mHelper.getApiClient()), OPEN_ACHIEVMENTS);
            else {
                // if (!mHelper.getApiClient().isConnecting())
                //     mHelper.getApiClient().connect();
                LogIn();
            }
        }
    }

    /* updates score of a specific leaderboard */
    public void updateScore(int score, boolean largerisbetter, String leaderboard_id) {
        SingleScore scoremanager = new SingleScore(screen);

        //save score if larger than the one saved
        //if (score > scoremanager.load_localscore().score)
        scoremanager.save_localscore_simple(score, leaderboard_id, largerisbetter);

        if (App_id.length() > 0) {

            if (mHelper.isSignedIn()) {
                Games.Leaderboards.submitScore(mHelper.getApiClient(), leaderboard_id, score);
            }
        }
    }


    /* new Achievement unlocked */
    public void unlockAchievement(String Achievement_id) {
        if (App_id.length() > 0) {

            if (mHelper.isSignedIn()) {
                Games.Achievements.unlock(mHelper.getApiClient(), Achievement_id);
            }
        }
    }

    public int getTopScore(String leaderboard_id) {
        SingleScore scoremanager = new SingleScore(screen);

        return scoremanager.load_localscore_simple("" + leaderboard_id);
    }

    public void onStart() {
        if (App_id.length() > 0)
            mHelper.onStart(screen);
    }

    public void onStop() {
        if (App_id.length() > 0)
            mHelper.onStop();
    }

    public void onActivityResult(int request, int response, Intent data) {
        if (App_id.length() > 0) {
            mHelper.onActivityResult(request, response, data);
            // check for "inconsistent state"
            if (response == GamesActivityResultCodes.RESULT_RECONNECT_REQUIRED && (request == OPEN_LEADERBOARD || request == OPEN_ACHIEVMENTS)) {

                // force a disconnect to sync up state, ensuring that mClient reports "not connected"
                mHelper.disconnect();

            }
        }
    }

}
