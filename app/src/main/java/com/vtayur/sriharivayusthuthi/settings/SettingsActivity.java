package com.vtayur.sriharivayusthuthi.settings;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.vtayur.sriharivayusthuthi.R;
import com.vtayur.sriharivayusthuthi.data.DataProvider;
import com.vtayur.sriharivayusthuthi.data.YesNo;
import com.vtayur.sriharivayusthuthi.home.Language;


/**
 * Created by vtayur on 9/30/2014.
 */
public class SettingsActivity extends Activity {
    private static final String TAG = "SettingsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_settings);

        Log.d(TAG, "activity_settings activity being launched");

        final RadioGroup radioGrpLangSelector = (RadioGroup) findViewById(R.id.language_selector);

        radioGrpLangSelector.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                SharedPreferences settings = getSharedPreferences(DataProvider.PREFS_NAME, 0);
                SharedPreferences.Editor editor = settings.edit();
                RadioButton rb = (RadioButton) findViewById(radioGrpLangSelector.getCheckedRadioButtonId());
                editor.putString(DataProvider.SHLOKA_DISP_LANGUAGE, Language.getLanguageEnum(rb.getText().toString()).toString());

                editor.commit();

                Log.d(TAG, "setOnCheckedChangeListener - Language activity_settings saved - " + getSharedPreferences(DataProvider.PREFS_NAME, 0).getString(DataProvider.SHLOKA_DISP_LANGUAGE, ""));
            }
        });

        final RadioGroup radioGrpLearnModeSelector = (RadioGroup) findViewById(R.id.learning_mode_selector);

        radioGrpLearnModeSelector.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                SharedPreferences settings = getSharedPreferences(DataProvider.PREFS_NAME, 0);
                SharedPreferences.Editor editor = settings.edit();
                RadioButton rb = (RadioButton) findViewById(radioGrpLearnModeSelector.getCheckedRadioButtonId());
                editor.putString(DataProvider.LEARNING_MODE, YesNo.getYesNoEnum(rb.getText().toString()).toString());

                editor.commit();

                Log.d(TAG, "setOnCheckedChangeListener - Learning Mode activity_settings saved - " + getSharedPreferences(DataProvider.PREFS_NAME, 0).getString(DataProvider.LEARNING_MODE, ""));
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        SharedPreferences settings = getSharedPreferences(DataProvider.PREFS_NAME, 0);
        String savedLocalLang = settings.getString(DataProvider.SHLOKA_DISP_LANGUAGE, "");
        String learningMode = settings.getString(DataProvider.LEARNING_MODE, "");
        if (savedLocalLang.isEmpty()) {
            Log.d(TAG, "Language activity_settings are not set - will set to sanskrit and continue");
        }
        if (learningMode.isEmpty()) {
            Log.d(TAG, "Learning mode activity_settings are not set - will set to Yes and continue");
        }
        RadioButton rbSanskrit = (RadioButton) findViewById(R.id.language_sanskrit);
        RadioButton rbKannada = (RadioButton) findViewById(R.id.language_kannada);
        if (Language.getLanguageEnum(savedLocalLang).equals(Language.san)) {
            rbSanskrit.setChecked(true);
        } else {
            rbKannada.setChecked(true);
        }

        RadioButton rbLearnModeYes = (RadioButton) findViewById(R.id.learn_mode_yes);
        RadioButton rbLearnModeNo = (RadioButton) findViewById(R.id.learn_mode_no);
        if (YesNo.getYesNoEnum(learningMode).equals(YesNo.yes)) {
            rbLearnModeYes.setChecked(true);
        } else {
            rbLearnModeNo.setChecked(true);
        }

        Log.d(TAG, "Settings are restored for Language - " + getSharedPreferences(DataProvider.PREFS_NAME, 0).getString(DataProvider.SHLOKA_DISP_LANGUAGE, ""));
        Log.d(TAG, "Settings are restored for Learning mode - " + getSharedPreferences(DataProvider.PREFS_NAME, 0).getString(DataProvider.LEARNING_MODE, ""));
    }
}
