package com.vtayur.sriharivayusthuthi.detail;

import android.app.Activity;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.vtayur.sriharivayusthuthi.R;
import com.vtayur.sriharivayusthuthi.data.BundleArgs;
import com.vtayur.sriharivayusthuthi.data.DataProvider;
import com.vtayur.sriharivayusthuthi.data.model.Shloka;
import com.vtayur.sriharivayusthuthi.home.Language;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class StotraInOnePageActivity extends FragmentActivity {
    private static String TAG = "StotraInOnePageActivity";
    private MediaPlayer mediaPlayer;
    private Iterator<Integer> mediaResIterator;
    private List<Integer> mediaResources;
    private AtomicInteger playCounter = new AtomicInteger();

    private class PauseButtonClickListener implements OnClickListener {
        private final Activity curActivity;

        public PauseButtonClickListener(Activity curActivity) {
            this.curActivity = curActivity;
        }

        public void onClick(View v) {
            Log.d(StotraInOnePageActivity.TAG, "Stopping the stream for media playback");
            Toast.makeText(this.curActivity, "Pausing sound", 0).show();
            try {
                StotraInOnePageActivity.this.mediaPlayer.pause();
                if (StotraInOnePageActivity.this.getWindow() != null) {
                    Log.d(StotraInOnePageActivity.TAG, "Pause/Resume: Allowing screen to be turned Off");
                    StotraInOnePageActivity.this.getWindow().clearFlags(128);
                }
            } catch (IllegalStateException e) {
                Log.e(StotraInOnePageActivity.TAG, "Exception while trying to stop mediaplayer status.");
            }
            ((ImageButton) this.curActivity.findViewById(R.id.imageButtonPlay)).setClickable(true);
        }
    }

    private class PlayButtonClickListener implements OnClickListener {
        private final Activity curActivity;

        public PlayButtonClickListener(Activity curActivity) {
            this.curActivity = curActivity;
        }

        public void onClick(View v) {
            StotraInOnePageActivity.this.mediaResIterator = StotraInOnePageActivity.this.mediaResources.iterator();
            StotraInOnePageActivity.this.playCounter.set(StotraInOnePageActivity.this.getShlokaRepeatCount());
            StotraInOnePageActivity.this.playMediaTrack(this.curActivity);
            v.setClickable(false);
            Toast.makeText(this.curActivity, "Starting media playback", 0).show();
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stotra_one_page);
        Log.d(TAG, "-> Starting StotraInOnePageActivity <-");
        Typeface typeface = getTypeface();
        LinearLayout rootLayout = (LinearLayout) findViewById(R.id.rootLayout);
        Integer menuPosition = Integer.valueOf(getIntent().getIntExtra(BundleArgs.PAGE_NUMBER, 0));
        List<Shloka> engShlokas = (List) getIntent().getSerializableExtra(BundleArgs.ENG_SHLOKA_LIST);
        List<Shloka> localLangShlokas = (List) getIntent().getSerializableExtra(BundleArgs.LOCAL_LANG_SHLOKA_LIST);
        String sectionName = getIntent().getStringExtra(BundleArgs.SECTION_NAME);
        rootLayout.setBackgroundResource(DataProvider.getBackgroundColor(menuPosition.intValue() - 1));
        ((TextView) findViewById(R.id.sectiontitle)).setText(sectionName);
        Log.d(TAG, "StotraInOnePageActivity needs to render " + localLangShlokas.size() + " shlokas");
        Log.d(TAG, "StotraInOnePageActivity needs to render english " + engShlokas.size() + " shlokas");
        List<Pair<Shloka, Shloka>> lstPairShlokas = getListPairedShlokas(engShlokas, localLangShlokas);
        this.mediaResources = getAllMediaResources(sectionName, localLangShlokas.size());
        this.mediaResIterator = this.mediaResources.iterator();
        for (Pair<Shloka, Shloka> shlokaPair : lstPairShlokas) {
            LinearLayout ll = new LinearLayout(this);
            ll.setOrientation(1);
            TextView localLang = new TextView(this);
            localLang.setTypeface(typeface);
            localLang.setText(((Shloka) shlokaPair.second).getText());
            TextView engLang = new TextView(this);
            engLang.setText(((Shloka) shlokaPair.first).getText());
            ll.addView(localLang);
            ll.addView(engLang);
            ll.setPadding(0, 5, 0, 50);
            rootLayout.addView(ll);
        }
        ImageButton pauseButton = (ImageButton) findViewById(R.id.imageButtonPause);
        pauseButton.setOnClickListener(new PauseButtonClickListener(this));
        ImageButton playButton = (ImageButton) findViewById(R.id.imageButtonPlay);
        playButton.setOnClickListener(new PlayButtonClickListener(this));
        if (menuPosition.intValue() == 1) {
            pauseButton.setVisibility(4);
            playButton.setVisibility(4);
        }
        if (menuPosition.intValue() > 2) {
            pauseButton.setVisibility(4);
            playButton.setVisibility(4);
        }
        Log.d(TAG, "* StotraInOnePageActivity created *");
    }

    private void playMediaTrack(final Activity curActivity) {
        if (!this.mediaResIterator.hasNext()) {
            Log.d(TAG, "Done with all streams for media playback");
            ((ImageButton) curActivity.findViewById(R.id.imageButtonPlay)).setClickable(true);
            if (this.mediaPlayer != null) {
                this.mediaPlayer.reset();
                this.mediaPlayer.release();
                this.mediaPlayer = null;
                if (getWindow() != null) {
                    Log.d(TAG, "EndTrack: Allowing screen to be turned Off");
                    getWindow().clearFlags(128);
                }
            }
            Log.d(TAG, "playMediaTrack, how many more to go? " + this.playCounter.get());
            if (this.playCounter.decrementAndGet() >= 0) {
                Log.d(TAG, "playMediaTrack, how many more to go? " + this.playCounter.get());
                this.mediaResIterator = this.mediaResources.iterator();
                playMediaTrack(curActivity);
            }
        } else if (this.mediaPlayer == null || this.mediaPlayer.getCurrentPosition() <= 0) {
            Integer nextResource = (Integer) this.mediaResIterator.next();
            this.mediaPlayer = MediaPlayer.create(curActivity, nextResource.intValue());
            if (getWindow() != null) {
                Log.d(TAG, "start: Allowing screen to be turned On");
                getWindow().addFlags(128);
            }
            this.mediaPlayer.start();
            Log.d(TAG, "Starting new stream for media playback " + nextResource);
            this.mediaPlayer.setOnCompletionListener(new OnCompletionListener() {
                public void onCompletion(MediaPlayer mp) {
                    Log.d(StotraInOnePageActivity.TAG, "Release before next media stream is played");
                    if (StotraInOnePageActivity.this.mediaPlayer != null) {
                        StotraInOnePageActivity.this.mediaPlayer.reset();
                        StotraInOnePageActivity.this.mediaPlayer.release();
                        StotraInOnePageActivity.this.mediaPlayer = null;
                        if (StotraInOnePageActivity.this.getWindow() != null) {
                            Log.d(StotraInOnePageActivity.TAG, "EndTrack: Allowing screen to be turned Off");
                            StotraInOnePageActivity.this.getWindow().clearFlags(128);
                        }
                    }
                    Log.d(StotraInOnePageActivity.TAG, "Requesting for continuing next media stream");
                    StotraInOnePageActivity.this.playMediaTrack(curActivity);
                }
            });
            this.mediaPlayer.setOnErrorListener(new OnErrorListener() {
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    Log.d(StotraInOnePageActivity.TAG, "Error encountered streams media playback");
                    ((ImageButton) curActivity.findViewById(R.id.imageButtonPlay)).setClickable(true);
                    if (StotraInOnePageActivity.this.mediaPlayer != null) {
                        StotraInOnePageActivity.this.mediaPlayer.reset();
                        StotraInOnePageActivity.this.mediaPlayer.release();
                        StotraInOnePageActivity.this.mediaPlayer = null;
                        if (StotraInOnePageActivity.this.getWindow() != null) {
                            Log.d(StotraInOnePageActivity.TAG, "EndTrack: Allowing screen to be turned Off");
                            StotraInOnePageActivity.this.getWindow().clearFlags(128);
                        }
                    }
                    return false;
                }
            });
        } else {
            if (getWindow() != null) {
                Log.e(TAG, "Pause/Resume: Allowing screen to be turned On");
                getWindow().addFlags(128);
            }
            this.mediaPlayer.start();
        }
    }

    private List<Integer> getAllMediaResources(String sectionName, int numOfResources) {
        List<Integer> lstRes = new ArrayList();
        for (int i = 1; i <= numOfResources; i++) {
            String resourceName = sectionName.toLowerCase().concat(String.valueOf(i)).replaceAll(" ", "");
            int resNameId = getResources().getIdentifier(resourceName, "raw", getPackageName());
            if (resNameId > 0) {
                lstRes.add(Integer.valueOf(resNameId));
            }
            Log.d(TAG, "ID fetched for packageName " + getPackageName() + " - " + resourceName + " -> " + resNameId);
        }
        return lstRes;
    }

    private List<Pair<Shloka, Shloka>> getListPairedShlokas(List<Shloka> engShlokas, List<Shloka> localLangShlokas) {
        List<Pair<Shloka, Shloka>> lstPairShlokas = new ArrayList();
        Iterator<Shloka> iterLocalLang = localLangShlokas.iterator();
        for (Shloka shloka : engShlokas) {
            if (iterLocalLang.hasNext()) {
                lstPairShlokas.add(new Pair(shloka, iterLocalLang.next()));
            } else {
                lstPairShlokas.add(new Pair(shloka, new Shloka()));
            }
        }
        if (engShlokas.size() < localLangShlokas.size()) {
            Log.w(TAG, "getListPairedShlokas found a mismatch in eng vs. local lang.. " + engShlokas.size() + " vs. " + localLangShlokas.size() + " shlokas");
            while (iterLocalLang.hasNext()) {
                lstPairShlokas.add(new Pair(new Shloka(), iterLocalLang.next()));
            }
        }
        return lstPairShlokas;
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    protected void onStop() {
        super.onStop();
        handleMediaStop();
    }

    public void onBackPressed() {
        super.onBackPressed();
        handleMediaStop();
    }

    private void handleMediaStop() {
        if (this.mediaPlayer != null) {
            Log.d(TAG, "************ Attempting to stop media that was initiated with this activity *********");
            this.mediaPlayer.release();
            ((ImageButton) findViewById(R.id.imageButtonPlay)).setClickable(true);
            Log.d(TAG, "************ Release media player resource was successful *********");
        }
    }

    private Typeface getTypeface() {
        String langPrefs = getSelectedLanguage();
        Log.d(TAG, "Trying to launch activity in selected language :" + langPrefs);
        Language lang = Language.getLanguageEnum(langPrefs);
        Log.d(TAG, "Will get assets for activity in language :" + lang.toString());
        return lang.getTypeface(getAssets());
    }

    private String getSelectedLanguage() {
        return getSharedPreferences(DataProvider.PREFS_NAME, 0).getString(DataProvider.SHLOKA_DISP_LANGUAGE, Language.san.toString());
    }

    public int getShlokaRepeatCount() {
        return Integer.valueOf(getSharedPreferences(DataProvider.PREFS_NAME, 0).getString(DataProvider.REPEAT_SHLOKA, DataProvider.REPEAT_SHLOKA_DEFAULT)).intValue();
    }
}