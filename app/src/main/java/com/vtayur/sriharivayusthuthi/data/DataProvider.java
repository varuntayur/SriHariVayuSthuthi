package com.vtayur.sriharivayusthuthi.data;

import android.content.res.AssetManager;
import android.util.Log;

import com.vtayur.sriharivayusthuthi.R;
import com.vtayur.sriharivayusthuthi.data.model.SriHariVayuSthuthi;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by vtayur on 8/19/2014.
 */
public class DataProvider {

    private static final String TAG = "DataProvider";

    private static SriHariVayuSthuthi vayuSthuthi;

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

    public static List<Integer> getBackgroundColorList() {
        return Collections.unmodifiableList(mBackgroundColors);
    }

    public static List<String> getMenuNames() {

        final List<String> sectionNames = new ArrayList<String>(DataProvider.getVayuSthuthi().getSectionNames());

        return sectionNames;
    }

    public static int getBackgroundColor(int location) {
        return mBackgroundColors.get(location);
    }

    public static void init(AssetManager am) {
        Serializer serializer = new Persister();
        InputStream inputStream = null;
        try {
            inputStream = am.open("db/sriharivayustuthi.xml");
            serializer = new Persister();
            vayuSthuthi = serializer.read(SriHariVayuSthuthi.class, inputStream);
            Log.d(TAG, "* Finished de-serializing the file - sriharivayustuthi.xml *");
//            System.out.println(vayuSthuthi);

            inputStream = am.open("db/sriharivayustuthi-ka.xml");
            serializer = new Persister();
            SriHariVayuSthuthi vayuSthuthiKa = serializer.read(SriHariVayuSthuthi.class, inputStream);
            Log.d(TAG, "* Finished de-serializing the file - sriharivayustuthi-ka.xml *");
            System.out.println(vayuSthuthiKa);

            inputStream = am.open("db/sriharivayustuthi-sa.xml");
            serializer = new Persister();
            SriHariVayuSthuthi vayuSthuthiSa = serializer.read(SriHariVayuSthuthi.class, inputStream);
            Log.d(TAG, "* Finished de-serializing the file - sriharivayustuthi-sa.xml *");
            System.out.println(vayuSthuthiSa);

        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "* IOException de-serializing the file *" + e);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "* Exception de-serializing the file *" + e);
        }
    }

    public static SriHariVayuSthuthi getVayuSthuthi() {
        return vayuSthuthi;
    }
}
