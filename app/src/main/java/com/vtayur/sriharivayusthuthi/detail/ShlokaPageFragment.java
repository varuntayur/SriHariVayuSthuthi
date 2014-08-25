/*
 * Copyright 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.vtayur.sriharivayusthuthi.detail;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.vtayur.sriharivayusthuthi.R;
import com.vtayur.sriharivayusthuthi.data.model.PhalaShruthi;
import com.vtayur.sriharivayusthuthi.data.model.Shloka;

import java.util.List;

public class ShlokaPageFragment extends Fragment {
    private static String TAG = "ShlokaPageFragment";

    private Typeface customTypeface;

    private String sectionName;

    private List<Shloka> shlokas;
    private List<Shloka> localLangShlokas;

    private int mPageNumber;

    private MediaPlayer mediaPlayer;

    public ShlokaPageFragment() {
        // required - other changing orientation will cause issues
    }

    public ShlokaPageFragment(String sectionName, List<Shloka> engShlokas, List<Shloka> localLangShlokas, int position, Typeface tf) {
        this.shlokas = engShlokas;
        this.localLangShlokas = localLangShlokas;
        this.customTypeface = tf;
        this.mPageNumber = position;
        this.sectionName = sectionName;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Retain this fragment across configuration changes.
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout containing a title and body text.
        ViewGroup rootView = (ViewGroup) inflater
                .inflate(R.layout.fragment_shloka_slide_page, container, false);

        final Activity curActivity = this.getActivity();

        String displayPageNumber = String.valueOf(mPageNumber + 1);

        TextView secTitleViewById = (TextView) rootView.findViewById(R.id.sectiontitle);
        secTitleViewById.setText(sectionName + " ( " + displayPageNumber + " / " + shlokas.size() + " )");

        final Shloka shloka = shlokas.get(mPageNumber);
        final Shloka localLangShloka = localLangShlokas.get(mPageNumber);

        TextView shlokaText = (TextView) rootView.findViewById(R.id.shlokalocallangtext);
        shlokaText.setTypeface(customTypeface);
        shlokaText.setText(localLangShloka.getText());
        shlokaText.setTypeface(shlokaText.getTypeface(), Typeface.BOLD);

        TextView shlokaenText = (TextView) rootView.findViewById(R.id.shlokaentext);
        shlokaenText.setText(shloka.getText());
        shlokaenText.setTypeface(shlokaText.getTypeface(), Typeface.BOLD);

        WebView shlokaPhala = (WebView) rootView.findViewById(R.id.shlokaphala);

        PhalaShruthi phala = shloka.getPhala();
        if (phala != null)
            shlokaPhala.loadData(phala.toString(), "text/html", null);

        shlokaPhala.setBackgroundColor(Color.TRANSPARENT);

        WebView shlokaExplanation = (WebView) rootView.findViewById(R.id.shlokaexplanation);
        shlokaExplanation.setBackgroundColor(Color.TRANSPARENT);
        shlokaExplanation.loadData(shloka.getFormattedExplanation(), "text/html", null);

        final String resourceName = sectionName.toLowerCase().concat(displayPageNumber).replaceAll(" ", "");

        final int resNameId = curActivity.getResources().getIdentifier(resourceName, "raw", curActivity.getPackageName());

        Log.d(TAG, "ID fetched for packageName " + curActivity.getPackageName() + " - " + resourceName + " -> " + resNameId);

        ImageButton pauseButton = (ImageButton) rootView.findViewById(R.id.imageButtonPause);
        setVisibility(resNameId, pauseButton);

        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(curActivity, "Pausing sound",
                        Toast.LENGTH_SHORT).show();

                if (mediaPlayer == null) return;

                mediaPlayer.pause();
            }
        });

        ImageButton playButton = (ImageButton) rootView.findViewById(R.id.imageButtonPlay);
        setVisibility(resNameId, playButton);

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (resNameId == 0) return;

                if (mediaPlayer != null && mediaPlayer.isPlaying()) return;

                Toast.makeText(curActivity, "Playing sound",
                        Toast.LENGTH_SHORT).show();


                mediaPlayer = MediaPlayer.create(getActivity(), resNameId);
                mediaPlayer.start();

            }
        });


        return rootView;
    }

    private void setVisibility(int resNameId, ImageButton pauseButton) {
        if (resNameId == 0)
            pauseButton.setVisibility(View.INVISIBLE);
        else
            pauseButton.setVisibility(View.VISIBLE);
    }

    @Override
    public void onStop() {

        if (mediaPlayer == null){
            super.onStop();
            return;
        }

        if (mediaPlayer.isPlaying()) {

            Log.d(TAG, "************ Attempting to stop media if it is playing *********");
            mediaPlayer.pause();
            Log.d(TAG, "************ Pause media was successful *********");
        }

        super.onStop();
    }

    /**
     * Returns the page number represented by this fragment object.
     */
    public int getPageNumber() {
        return shlokas.size();
    }
}
