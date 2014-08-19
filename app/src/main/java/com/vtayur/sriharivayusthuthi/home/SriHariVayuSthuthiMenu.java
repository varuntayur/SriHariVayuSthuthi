package com.vtayur.sriharivayusthuthi.home;

import android.app.Activity;
import android.content.Intent;

import com.vtayur.sriharivayusthuthi.data.DataProvider;
import com.vtayur.sriharivayusthuthi.detail.ShlokaSlideActivity;
import com.vtayur.sriharivayusthuthi.data.model.Section;

import java.io.Serializable;

/**
 * Created by varuntayur on 6/21/2014.
 */
public enum SriHariVayuSthuthiMenu {

    DEFAULT("Default", "") {
        @Override
        public void execute(Activity activity, String item, int position) {
            Intent intent = new Intent(activity, ShlokaSlideActivity.class);
            intent.putExtra("sectionName", item);
            intent.putExtra("menuPosition", position);

            Section section = DataProvider.getVayuSthuthi().getSection(item);

            if (section == null) return;

            intent.putExtra("shlokaList", (Serializable) section.getShlokaList());
            activity.startActivity(intent);
        }
    };

    private String menuDisplayName;
    private String menuDisplayKey;

    SriHariVayuSthuthiMenu(String menu, String key) {
        this.menuDisplayName = menu;
        this.menuDisplayKey = key;
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

    public String getMenuDisplayKey() {
        return menuDisplayKey;
    }

    public abstract void execute(Activity activity, String item, int position);
}
