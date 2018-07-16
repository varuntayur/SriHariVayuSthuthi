package com.vtayur.sriharivayusthuthi.detail;

import android.app.Fragment;
import android.app.FragmentManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.SimpleOnPageChangeListener;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import com.vtayur.sriharivayusthuthi.R;
import com.vtayur.sriharivayusthuthi.data.BundleArgs;
import com.vtayur.sriharivayusthuthi.data.DataProvider;
import com.vtayur.sriharivayusthuthi.data.model.Shloka;
import com.vtayur.sriharivayusthuthi.home.Language;
import java.io.Serializable;
import java.util.List;

public class ShlokaSlideActivity extends FragmentActivity {
    private static String TAG = "ShlokaSlideActivity";

    private class ShlokaSlidePagerAdapter extends FragmentStatePagerAdapter {
        private Window curWindow;
        private List<Shloka> localLangShlokas;
        private final String sectionName;
        private List<Shloka> shlokas;
        private final Typeface tf;
        private ViewPager viewPager;

        public ShlokaSlidePagerAdapter(String sectionName, List<Shloka> shlokas, List<Shloka> localizedShlokas, FragmentManager fm, Window window, ViewPager mPager, Typeface tf) {
            super(fm);
            this.tf = tf;
            this.shlokas = shlokas;
            this.sectionName = sectionName;
            this.localLangShlokas = localizedShlokas;
            this.curWindow = window;
            this.viewPager = mPager;
        }

        public Fragment getItem(int position) {
            ShlokaPageFragment stotraPageFragment = new ShlokaPageFragment(this.curWindow);
            Bundle bundleArgs = new Bundle();
            bundleArgs.putString(BundleArgs.SECTION_NAME, this.sectionName);
            bundleArgs.putSerializable(BundleArgs.ENG_SHLOKA_LIST, (Serializable) this.shlokas);
            bundleArgs.putSerializable(BundleArgs.LOCAL_LANG_SHLOKA_LIST, (Serializable) this.localLangShlokas);
            bundleArgs.putInt(BundleArgs.PAGE_NUMBER, position);
            stotraPageFragment.setArguments(bundleArgs);
            stotraPageFragment.setViewPager(this.viewPager);
            return stotraPageFragment;
        }

        public int getCount() {
            return this.shlokas.size();
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shloka_slide);
        Log.d(TAG, "-> Starting ScreenSlideActivity <-");
        Typeface langTypeface = getTypeface();
        Integer menuPosition = Integer.valueOf(getIntent().getIntExtra(BundleArgs.PAGE_NUMBER, 0));
        String mSectionName = getIntent().getStringExtra(BundleArgs.SECTION_NAME);
        List<Shloka> engShlokas = (List) getIntent().getSerializableExtra(BundleArgs.ENG_SHLOKA_LIST);
        List<Shloka> localLangShlokas = (List) getIntent().getSerializableExtra(BundleArgs.LOCAL_LANG_SHLOKA_LIST);
        ViewPager mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setPageTransformer(true, new ZoomOutPageTransformer());
        mPager.setBackgroundResource(DataProvider.getBackgroundColor(menuPosition.intValue() - 1));
        mPager.setAdapter(new ShlokaSlidePagerAdapter(mSectionName, engShlokas, localLangShlokas, getFragmentManager(), getWindow(), mPager, langTypeface));
        mPager.setOnPageChangeListener(new SimpleOnPageChangeListener() {
            public void onPageSelected(int position) {
                ShlokaSlideActivity.this.invalidateOptionsMenu();
            }
        });
        Log.d(TAG, "* ScreenSlideActivity created *");
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    public void onBackPressed() {
        super.onBackPressed();
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
}