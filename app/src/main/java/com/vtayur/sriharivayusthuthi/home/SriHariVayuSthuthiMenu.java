package com.vtayur.sriharivayusthuthi.home;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import com.vtayur.sriharivayusthuthi.data.DataProvider;
import com.vtayur.sriharivayusthuthi.data.model.Section;
import com.vtayur.sriharivayusthuthi.data.model.SriHariVayuSthuthi;
import com.vtayur.sriharivayusthuthi.detail.ShlokaSlideActivity;

import java.io.Serializable;

/**
 * Created by varuntayur on 6/21/2014.
 */
public enum SriHariVayuSthuthiMenu {

    DEFAULT("Default") {
        @Override
        public void execute(Activity activity, String item, int position, Language language) {
            Intent intent = new Intent(activity, ShlokaSlideActivity.class);
            intent.putExtra("sectionName", item);
            intent.putExtra("menuPosition", position);

            Section section = DataProvider.getVayuSthuthi(Language.eng).getSection(item);

            if (section == null) return;

            intent.putExtra("shlokaList", (Serializable) section.getShlokaList());

            SriHariVayuSthuthi sanVayuStuthi = DataProvider.getVayuSthuthi(language);
            section = sanVayuStuthi.getSection(item);
            Log.d(TAG, "SriHariVayuSthuthiMenu item section ->" + item + " " + section);
            intent.putExtra("shlokaListLocalLang", (Serializable) section.getShlokaList());

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
