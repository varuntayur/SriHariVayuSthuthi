package com.vtayur.sriharivayusthuthi.home;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.vtayur.sriharivayusthuthi.data.BundleArgs;
import com.vtayur.sriharivayusthuthi.data.DataProvider;
import com.vtayur.sriharivayusthuthi.data.YesNo;
import com.vtayur.sriharivayusthuthi.data.model.Section;
import com.vtayur.sriharivayusthuthi.detail.ShlokaSlideActivity;
import com.vtayur.sriharivayusthuthi.detail.StotraInOnePageActivity;

import java.io.Serializable;

/**
 * Created by varuntayur on 6/21/2014.
 */
public enum SriHariVayuSthuthiMenu {

    DEFAULT("Default") {
        @Override
        public void execute(Activity activity, String item, int position, Language language) {

            Intent intent = null;

            SharedPreferences settings = activity.getSharedPreferences(DataProvider.PREFS_NAME, 0);
            String learningMode = settings.getString(DataProvider.LEARNING_MODE, "");

            if (YesNo.yes.toString().equalsIgnoreCase(learningMode))
                intent = new Intent(activity, ShlokaSlideActivity.class);
            else
                intent = new Intent(activity, StotraInOnePageActivity.class);

            Section secEnglish = DataProvider.getVayuSthuthi(Language.eng).getSection(item);

            if (secEnglish == null) return;

            Section sanVayuStuthi = DataProvider.getVayuSthuthi(language).getSection(item);

            intent.putExtra(BundleArgs.SECTION_NAME, item);
            intent.putExtra(BundleArgs.PAGE_NUMBER, position);
            intent.putExtra(BundleArgs.ENG_SHLOKA_LIST, (Serializable) secEnglish.getShlokaList());
            intent.putExtra(BundleArgs.LOCAL_LANG_SHLOKA_LIST, (Serializable) sanVayuStuthi.getShlokaList());

            Log.d(TAG, "SriHariVayuSthuthiMenu item secEnglish ->" + item + " " + secEnglish);

            activity.startActivity(intent);
        }
    };

    private static final String TAG = "SriHariVayuSthuthiMenu";
    private String menuDisplayName;

    SriHariVayuSthuthiMenu(String menu) {
        this.menuDisplayName = menu;
    }

    public static SriHariVayuSthuthiMenu getEnum(String item) {
        for (SriHariVayuSthuthiMenu v : values())
            if (v.toString().equalsIgnoreCase(item)) return v;
        return SriHariVayuSthuthiMenu.DEFAULT;
    }

    @Override
    public String toString() {
        return menuDisplayName;
    }

    public abstract void execute(Activity activity, String item, int position, Language language);
}
