package com.vtayur.sriharivayusthuthi.common;

import android.app.Activity;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.util.Log;
import android.view.Window;
import java.util.concurrent.atomic.AtomicInteger;

public class ShlokaMediaPlayer {
    private static final String TAG = "ShlokaMediaPlayer";
    private static Window curWindow;
    private static MediaPlayer mediaPlayer;
    private static AtomicInteger playCounter = new AtomicInteger();

    private static class PlaybackCompleteListener implements OnCompletionListener {
        private final Activity activity;
        private final int resId;

        public PlaybackCompleteListener(Activity activity, int resId) {
            this.activity = activity;
            this.resId = resId;
        }

        public void onCompletion(MediaPlayer mp) {
            Log.d(ShlokaMediaPlayer.TAG, "onComplete track, release mediaPlayer");
            if (ShlokaMediaPlayer.mediaPlayer != null) {
                ShlokaMediaPlayer.mediaPlayer.reset();
                ShlokaMediaPlayer.mediaPlayer.release();
                ShlokaMediaPlayer.mediaPlayer = null;
                if (ShlokaMediaPlayer.curWindow != null) {
                    Log.d(ShlokaMediaPlayer.TAG, "OnComplete: Allowing screen to be turned off");
                    ShlokaMediaPlayer.curWindow.clearFlags(128);
                }
            }
            Log.d(ShlokaMediaPlayer.TAG, "onComplete track, many more to go ? " + ShlokaMediaPlayer.playCounter.get());
            if (ShlokaMediaPlayer.playCounter.decrementAndGet() > 0) {
                ShlokaMediaPlayer.mediaPlayer = MediaPlayer.create(this.activity, this.resId);
                if (ShlokaMediaPlayer.curWindow != null) {
                    Log.e(ShlokaMediaPlayer.TAG, "Loop play: Allowing screen to be turned On");
                    ShlokaMediaPlayer.curWindow.addFlags(128);
                }
                ShlokaMediaPlayer.mediaPlayer.start();
                ShlokaMediaPlayer.mediaPlayer.setOnCompletionListener(new PlaybackCompleteListener(this.activity, this.resId));
                Log.d(ShlokaMediaPlayer.TAG, "onComplete restarting playback, how many more to go ? " + ShlokaMediaPlayer.playCounter.get());
            }
        }
    }

    public static void setLoopCounter(int value) {
        playCounter.set(value);
    }

    public static void pause() {
        if (mediaPlayer != null) {
            try {
                mediaPlayer.pause();
                if (curWindow != null) {
                    Log.d(TAG, "Allowing screen to be turned off");
                    curWindow.clearFlags(128);
                }
            } catch (IllegalStateException e) {
                Log.e(TAG, "Exception while trying to pause mediaplayer");
            }
        }
    }

    public static void release() {
        if (mediaPlayer != null) {
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    public static String play(Activity activity, int resId) {
        try {
            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                return "Media player is already playing another track. Please try again after that completes.";
            }
        } catch (IllegalStateException e) {
            Log.e(TAG, "Exception while trying to fetch mediaplayer status.");
        }
        if (mediaPlayer == null || mediaPlayer.getCurrentPosition() <= 0) {
            mediaPlayer = MediaPlayer.create(activity, resId);
            if (curWindow != null) {
                Log.d(TAG, "Play: Allowing screen to be turned On");
                curWindow.addFlags(128);
            }
            mediaPlayer.start();
            mediaPlayer.setOnCompletionListener(new PlaybackCompleteListener(activity, resId));
            return "";
        }
        mediaPlayer.start();
        if (curWindow != null) {
            Log.d(TAG, "Pause/Resume: Allowing screen to be turned On");
            curWindow.addFlags(128);
        }
        return "";
    }

    public static void setCurrentWindow(Window currentWindow) {
        curWindow = currentWindow;
    }
}