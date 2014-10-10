package com.vtayur.sriharivayusthuthi.data;

import android.content.res.AssetManager;
import android.util.Log;

import com.vtayur.sriharivayusthuthi.R;
import com.vtayur.sriharivayusthuthi.data.model.SriHariVayuSthuthi;
import com.vtayur.sriharivayusthuthi.home.Language;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by vtayur on 8/19/2014.
 */
public class DataProvider {

    private static final String TAG = "DataProvider";

    public static final String PREFS_NAME = "SriHariVayuStuthi";
    public static final String SHLOKA_DISP_LANGUAGE = "localLanguage";
    public static final String LEARNING_MODE = "learningMode";

    private static Map<String, SriHariVayuSthuthi> lang2vayuSthuthi = new ConcurrentHashMap<String, SriHariVayuSthuthi>();

    private static List<Integer> mBackgroundColors = new ArrayList<Integer>() {
        {
            add(R.color.orange);
            add(R.color.green);
            add(R.color.blue);
            add(R.color.yellow);
            add(R.color.grey);
            add(R.color.lblue);
            add(R.color.slateblue);
            add(R.color.cyan);
            add(R.color.silver);
        }
    };

    private final static CharSequence[] languages = {"Sanskrit", "Kannada"};

    public static CharSequence[] getLanguages() {
        return languages;
    }

    public static List<Integer> getBackgroundColorList() {
        return Collections.unmodifiableList(mBackgroundColors);
    }

    public static List<String> getMenuNames() {

        String anyResource = lang2vayuSthuthi.keySet().iterator().next();

        return new ArrayList<String>(DataProvider.getVayuSthuthi(Language.getLanguageEnum(anyResource)).getSectionNames());
    }

    public static int getBackgroundColor(int location) {
        return mBackgroundColors.get(location);
    }

    public static void init(AssetManager am) {
        Serializer serializer;
        InputStream inputStream;
        try {
            inputStream = am.open("db/sriharivayustuthi-eng.xml");
            serializer = new Persister();
            SriHariVayuSthuthi vayuSthuthi = serializer.read(SriHariVayuSthuthi.class, inputStream);
            lang2vayuSthuthi.put(vayuSthuthi.getLang(), vayuSthuthi);
            Log.d(TAG, "* Finished de-serializing the file - sriharivayustuthi-eng.xml *");
//            System.out.println(vayuSthuthi);

            inputStream = am.open("db/sriharivayustuthi-kan.xml");
            serializer = new Persister();
            vayuSthuthi = serializer.read(SriHariVayuSthuthi.class, inputStream);
            lang2vayuSthuthi.put(vayuSthuthi.getLang(), vayuSthuthi);
            Log.d(TAG, "* Finished de-serializing the file - sriharivayustuthi-kan.xml *");
//            System.out.println(vayuSthuthi);

            inputStream = am.open("db/sriharivayustuthi-san.xml");
            serializer = new Persister();
            vayuSthuthi = serializer.read(SriHariVayuSthuthi.class, inputStream);
            lang2vayuSthuthi.put(vayuSthuthi.getLang(), vayuSthuthi);
            Log.d(TAG, "* Finished de-serializing the file - sriharivayustuthi-san.xml *");
//            System.out.println(vayuSthuthi);

            System.out.println(lang2vayuSthuthi.keySet());

        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "* IOException de-serializing the file *" + e);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "* Exception de-serializing the file *" + e);
        }
    }

    public static SriHariVayuSthuthi getVayuSthuthi(Language lang) {
        return lang2vayuSthuthi.get(lang.toString());
    }


}
