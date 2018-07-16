package com.vtayur.sriharivayusthuthi.settings;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.widget.NumberPicker;
import android.widget.NumberPicker.OnValueChangeListener;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import com.vtayur.sriharivayusthuthi.R;
import com.vtayur.sriharivayusthuthi.data.DataProvider;
import com.vtayur.sriharivayusthuthi.data.YesNo;
import com.vtayur.sriharivayusthuthi.home.Language;

public class SettingsActivity extends Activity {
    private static final String TAG = "SettingsActivity";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Log.d(TAG, "activity_settings activity being launched");
        NumberPicker repeatShlokasCnt = (NumberPicker) findViewById(R.id.repeatShlokasCount);
        repeatShlokasCnt.setMinValue(1);
        repeatShlokasCnt.setMaxValue(10);
        repeatShlokasCnt.setOnValueChangedListener(new OnValueChangeListener() {
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                Editor editor = SettingsActivity.this.getSharedPreferences(DataProvider.PREFS_NAME, 0).edit();
                editor.putString(DataProvider.REPEAT_SHLOKA, Integer.toString(newVal));
                editor.commit();
                Log.d(SettingsActivity.TAG, "setOnValueChangedListener - Repeat shloka settings saved - " + SettingsActivity.this.getSharedPreferences(DataProvider.PREFS_NAME, 0).getString(DataProvider.REPEAT_SHLOKA, DataProvider.REPEAT_SHLOKA_DEFAULT));
            }
        });
        final RadioGroup radioGrpLangSelector = (RadioGroup) findViewById(R.id.language_selector);
        radioGrpLangSelector.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Editor editor = SettingsActivity.this.getSharedPreferences(DataProvider.PREFS_NAME, 0).edit();
                editor.putString(DataProvider.SHLOKA_DISP_LANGUAGE, Language.getLanguageEnum(((RadioButton) SettingsActivity.this.findViewById(radioGrpLangSelector.getCheckedRadioButtonId())).getText().toString()).toString());
                editor.commit();
                Log.d(SettingsActivity.TAG, "setOnCheckedChangeListener - Language activity_settings saved - " + SettingsActivity.this.getSharedPreferences(DataProvider.PREFS_NAME, 0).getString(DataProvider.SHLOKA_DISP_LANGUAGE, ""));
            }
        });
        final RadioGroup radioGrpLearnModeSelector = (RadioGroup) findViewById(R.id.learning_mode_selector);
        radioGrpLearnModeSelector.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Editor editor = SettingsActivity.this.getSharedPreferences(DataProvider.PREFS_NAME, 0).edit();
                editor.putString(DataProvider.LEARNING_MODE, YesNo.getYesNoEnum(((RadioButton) SettingsActivity.this.findViewById(radioGrpLearnModeSelector.getCheckedRadioButtonId())).getText().toString()).toString());
                editor.commit();
                Log.d(SettingsActivity.TAG, "setOnCheckedChangeListener - Learning Mode activity_settings saved - " + SettingsActivity.this.getSharedPreferences(DataProvider.PREFS_NAME, 0).getString(DataProvider.LEARNING_MODE, ""));
            }
        });
    }

    protected void onStart() {
        super.onStart();
        SharedPreferences settings = getSharedPreferences(DataProvider.PREFS_NAME, 0);
        String savedRepeatShlokaCnt = settings.getString(DataProvider.REPEAT_SHLOKA, "");
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
        if (savedRepeatShlokaCnt.isEmpty()) {
            Log.d(TAG, "Repeat Shloka activity_settings are not set - will set to 3");
            ((NumberPicker) findViewById(R.id.repeatShlokasCount)).setValue(Integer.valueOf(DataProvider.REPEAT_SHLOKA_DEFAULT).intValue());
        } else {
            Log.d(TAG, "Repeat Shloka activity_settings are set - will set to " + savedRepeatShlokaCnt);
            ((NumberPicker) findViewById(R.id.repeatShlokasCount)).setValue(Integer.valueOf(savedRepeatShlokaCnt).intValue());
        }
        Log.d(TAG, "Settings are restored for Language - " + getSharedPreferences(DataProvider.PREFS_NAME, 0).getString(DataProvider.SHLOKA_DISP_LANGUAGE, ""));
        Log.d(TAG, "Settings are restored for Learning mode - " + getSharedPreferences(DataProvider.PREFS_NAME, 0).getString(DataProvider.LEARNING_MODE, ""));
        Log.d(TAG, "Settings are restored for Repeat Shloka settings - " + getSharedPreferences(DataProvider.PREFS_NAME, 0).getString(DataProvider.REPEAT_SHLOKA, ""));
    }
}