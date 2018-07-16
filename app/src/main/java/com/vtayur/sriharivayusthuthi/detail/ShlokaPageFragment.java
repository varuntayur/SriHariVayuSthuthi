package com.vtayur.sriharivayusthuthi.detail;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.vtayur.sriharivayusthuthi.R;
import com.vtayur.sriharivayusthuthi.common.ShlokaMediaPlayer;
import com.vtayur.sriharivayusthuthi.data.BundleArgs;
import com.vtayur.sriharivayusthuthi.data.DataProvider;
import com.vtayur.sriharivayusthuthi.data.model.Shloka;
import com.vtayur.sriharivayusthuthi.home.Language;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class ShlokaPageFragment extends Fragment {
    private static String TAG = "ShlokaPageFragment";
    private Window curWindow;
    private int resNameId;
    private ViewPager viewPager;

    private class GotoShlokaClickListener implements OnClickListener {
        private final Activity curActivity;

        public GotoShlokaClickListener(Activity curActivity) {
            this.curActivity = curActivity;
        }

        public void onClick(View v) {
            ArrayList<String> list = new ArrayList();
            for (Shloka shloka : ShlokaPageFragment.this.getLocalLangShlokas()) {
                if (!shloka.getText().isEmpty()) {
                    list.add(shloka.getText().substring(0, 16) + " ...");
                }
            }
            final Dialog dialog = new Dialog(this.curActivity);
            dialog.setContentView(R.layout.shloka_select_dialog);
            dialog.setTitle("Title...");
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            ((Button) dialog.findViewById(R.id.buttonClose)).setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            ListView listShlokas = (ListView) dialog.findViewById(R.id.listShlokas);
            listShlokas.setAdapter(new StableArrayAdapter(this.curActivity.getApplicationContext(), 17367043, list));
            listShlokas.setOnItemClickListener(new OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Toast.makeText(GotoShlokaClickListener.this.curActivity.getApplicationContext(), ((String) parent.getAdapter().getItem(position)) + " selected", 1).show();
                    ShlokaPageFragment.this.viewPager.setCurrentItem(position);
                    dialog.dismiss();
                }
            });
            dialog.show();
        }
    }

    private class StableArrayAdapter extends ArrayAdapter<String> {
        HashMap<String, Integer> mIdMap = new HashMap();

        public StableArrayAdapter(Context context, int textViewResourceId, List<String> objects) {
            super(context, textViewResourceId, objects);
            for (int i = 0; i < objects.size(); i++) {
                this.mIdMap.put(objects.get(i), Integer.valueOf(i));
            }
        }

        public long getItemId(int position) {
            return (long) ((Integer) this.mIdMap.get((String) getItem(position))).intValue();
        }

        public boolean hasStableIds() {
            return true;
        }
    }

    public ShlokaPageFragment(Window window) {
        this.curWindow = window;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return intializeView(inflater, container);
    }

    public void onStop() {
        super.onStop();
        Log.d(TAG, "************ Attempting to stop media that was initiated with this fragment *********");
        ShlokaMediaPlayer.release();
        Log.d(TAG, "************ Pause media was successful *********");
    }

    public String getSectionName() {
        Bundle bundle = getArguments();
        if (bundle == null || !bundle.containsKey(BundleArgs.SECTION_NAME)) {
            return "";
        }
        return bundle.getString(BundleArgs.SECTION_NAME);
    }

    public List<Shloka> getEngShlokas() {
        Bundle bundle = getArguments();
        if (bundle == null || !bundle.containsKey(BundleArgs.ENG_SHLOKA_LIST)) {
            return Collections.emptyList();
        }
        return (List) bundle.getSerializable(BundleArgs.ENG_SHLOKA_LIST);
    }

    public List<Shloka> getLocalLangShlokas() {
        Bundle bundle = getArguments();
        if (bundle == null || !bundle.containsKey(BundleArgs.LOCAL_LANG_SHLOKA_LIST)) {
            return Collections.emptyList();
        }
        return (List) bundle.getSerializable(BundleArgs.LOCAL_LANG_SHLOKA_LIST);
    }

    public int getPageNumber() {
        Bundle bundle = getArguments();
        if (bundle == null || !bundle.containsKey(BundleArgs.PAGE_NUMBER)) {
            return 1;
        }
        return bundle.getInt(BundleArgs.PAGE_NUMBER);
    }

    private Typeface getTypeface() {
        String langPrefs = getSelectedLanguage();
        Log.d(TAG, "Trying to launch activity in selected language :" + langPrefs);
        Language lang = Language.getLanguageEnum(langPrefs);
        Log.d(TAG, "Will get assets for activity in language :" + lang.toString());
        return lang.getTypeface(getActivity().getAssets());
    }

    private String getSelectedLanguage() {
        return getActivity().getSharedPreferences(DataProvider.PREFS_NAME, 0).getString(DataProvider.SHLOKA_DISP_LANGUAGE, Language.san.toString());
    }

    private ViewGroup intializeView(LayoutInflater inflater, ViewGroup container) {
        final Activity curActivity = getActivity();
        Typeface customTypeface = getTypeface();
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_shloka_slide_page, container, false);
        ShlokaMediaPlayer.setCurrentWindow(getCurWindow());
        ((TextView) rootView.findViewById(R.id.sectiontitle)).setText(getSectionName() + " ( " + String.valueOf(getPageNumber() + 1) + " / " + getEngShlokas().size() + " )");
        Shloka shloka = (Shloka) getEngShlokas().get(getPageNumber());
        Shloka localLangShloka = (Shloka) getLocalLangShlokas().get(getPageNumber());
        rootView.findViewById(R.id.buttonGoto).setOnClickListener(new GotoShlokaClickListener(curActivity));
        TextView shlokaText = (TextView) rootView.findViewById(R.id.shlokalocallangtext);
        shlokaText.setTypeface(customTypeface);
        shlokaText.setText(localLangShloka.getText());
        ((TextView) rootView.findViewById(R.id.shlokaentext)).setText(shloka.getText());
        WebView shlokaExplanation = (WebView) rootView.findViewById(R.id.shlokaexplanation);
        shlokaExplanation.setBackgroundColor(0);
        shlokaExplanation.loadData(shloka.getFormattedExplanation(), "text/html", null);
        this.resNameId = getResourceName(curActivity);
        ImageButton pauseButton = (ImageButton) rootView.findViewById(R.id.imageButtonPause);
        setVisibility(this.resNameId, pauseButton);
        pauseButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(curActivity, "Stopping media playback", 0).show();
                ShlokaMediaPlayer.pause();
            }
        });
        ImageButton playButton = (ImageButton) rootView.findViewById(R.id.imageButtonPlay);
        setVisibility(this.resNameId, playButton);
        playButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (ShlokaPageFragment.this.resNameId != 0) {
                    ShlokaMediaPlayer.setLoopCounter(ShlokaPageFragment.this.getShlokaRepeatCount());
                    String playStatus = ShlokaMediaPlayer.play(ShlokaPageFragment.this.getActivity(), ShlokaPageFragment.this.resNameId);
                    if (playStatus.isEmpty()) {
                        Toast.makeText(curActivity, "Playing media", 0).show();
                    } else {
                        Toast.makeText(curActivity, playStatus, 0).show();
                    }
                }
            }
        });
        return rootView;
    }

    private Window getCurWindow() {
        return this.curWindow;
    }

    private int getResourceName(Activity curActivity) {
        String resourceName = getSectionName().toLowerCase().concat(String.valueOf(getPageNumber() + 1)).replaceAll(" ", "");
        int resNameId = curActivity.getResources().getIdentifier(resourceName, "raw", curActivity.getPackageName());
        Log.d(TAG, "ID fetched for packageName " + curActivity.getPackageName() + " - " + resourceName + " -> " + resNameId);
        return resNameId;
    }

    private void setVisibility(int resNameId, ImageButton pauseButton) {
        if (resNameId == 0) {
            pauseButton.setVisibility(4);
        } else {
            pauseButton.setVisibility(0);
        }
    }

    public int getShlokaRepeatCount() {
        return Integer.valueOf(getActivity().getSharedPreferences(DataProvider.PREFS_NAME, 0).getString(DataProvider.REPEAT_SHLOKA, DataProvider.REPEAT_SHLOKA_DEFAULT)).intValue();
    }

    public void setViewPager(ViewPager viewPager) {
        this.viewPager = viewPager;
    }
}