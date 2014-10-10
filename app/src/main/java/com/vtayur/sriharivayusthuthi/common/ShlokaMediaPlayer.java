package com.vtayur.sriharivayusthuthi.common;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.PowerManager;
import android.util.Log;

/**
 * Created by vtayur on 9/30/2014.
 */
public class ShlokaMediaPlayer {

    private static MediaPlayer mediaPlayer;
    private static final String TAG = "ShlokaMediaPlayer";

    public static void pause() {
        if (mediaPlayer != null) {
            try {
                mediaPlayer.pause();
            } catch (IllegalStateException ex) {
                Log.e(TAG, "Exception while trying to pause mediaplayer");
            }
        }
    }

    public static void release() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
    }


    public static String play(Activity activity, int resId) {

        try {
            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                return "Media player is already playing another track. Please try again after that completes.";
            }
        } catch (IllegalStateException ex) {
            Log.e(TAG, "Exception while trying to fetch mediaplayer status.");
        }
        mediaPlayer = MediaPlayer.create(activity, resId);
        mediaPlayer.setWakeMode(activity.getBaseContext(), PowerManager.SCREEN_DIM_WAKE_LOCK);
        mediaPlayer.start();

        return "";
    }

}
