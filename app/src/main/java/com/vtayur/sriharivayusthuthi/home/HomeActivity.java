package com.vtayur.sriharivayusthuthi.home;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.etsy.android.grid.StaggeredGridView;
import com.vtayur.sriharivayusthuthi.R;
import com.vtayur.sriharivayusthuthi.data.DataProvider;
import com.vtayur.sriharivayusthuthi.detail.StaggeredGridAdapter;
import com.vtayur.sriharivayusthuthi.settings.SettingsActivity;

public class HomeActivity extends AppCompatActivity {
    private static String TAG = "HomeActivity";
    private ProgressDialog progressDialog;

    private class DataProviderTask extends AsyncTask<AssetManager, Void, Long> {
        Activity currentActivity;

        public DataProviderTask(Activity activity) {
            this.currentActivity = activity;
        }

        protected void onPostExecute(Long result) {
            final LayoutInflater inflater = this.currentActivity.getLayoutInflater();
            final Activity activity = this.currentActivity;
            HomeActivity.this.runOnUiThread(new Runnable() {
                public void run() {
                    StaggeredGridView listView = (StaggeredGridView) HomeActivity.this.findViewById(R.id.grid_view);
                    View header = inflater.inflate(R.layout.list_item_header_footer, null);
                    ((TextView) header.findViewById(R.id.txt_title)).setText(HomeActivity.this.getResources().getString(R.string.app_name));
                    listView.addHeaderView(header);
                    header.setClickable(false);
                    StaggeredGridAdapter mAdapter = new StaggeredGridAdapter(activity, R.id.txt_line1);
                    for (String data : DataProvider.getMenuNames()) {
                        mAdapter.add(data);
                    }
                    listView.setAdapter((ListAdapter) mAdapter);
                    listView.setOnItemClickListener(DataProviderTask.this.getOnMenuClickListener(activity));
                    SharedPreferences settings = HomeActivity.this.getSharedPreferences(DataProvider.PREFS_NAME, 0);
                    if (settings.getString(DataProvider.SHLOKA_DISP_LANGUAGE, "").isEmpty()) {
                        Editor editor = settings.edit();
                        editor.putString(DataProvider.SHLOKA_DISP_LANGUAGE, Language.san.toString());
                        editor.commit();
                        Log.d(HomeActivity.TAG, "Setting the default launch preference to Sanskrit at startup - " + settings.getString(DataProvider.SHLOKA_DISP_LANGUAGE, ""));
                    }
                    listView.addFooterView(inflater.inflate(R.layout.advt, null));
                    HomeActivity.this.progressDialog.dismiss();
                }
            });
            Log.d(HomeActivity.TAG, "Finished launching main-menu");
        }

        private OnItemClickListener getOnMenuClickListener(final Activity activity) {
            return new OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String item = (String) parent.getAdapter().getItem(position);
                    SriHariVayuSthuthiMenu.getEnum(item).execute(activity, item, position, Language.getLanguageEnum(HomeActivity.this.getSharedPreferences(DataProvider.PREFS_NAME, 0).getString(DataProvider.SHLOKA_DISP_LANGUAGE, "")));
                }
            };
        }

        protected Long doInBackground(AssetManager... assetManagers) {
            DataProvider.init(HomeActivity.this.getAssets());
            Log.d(HomeActivity.TAG, "Finished background task execution.");
            return Long.valueOf(1);
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_sgv);
        this.progressDialog = ProgressDialog.show(this, "", getResources().getString(R.string.loading_please_wait), true);
        new DataProviderTask(this).execute(new AssetManager[]{getAssets()});
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.languageselector:
                Intent intent = new Intent(this, SettingsActivity.class);
                Log.d(TAG, "Launching Settings Activity");
                startActivity(intent);
                break;
            case R.id.help:
                View messageView = getLayoutInflater().inflate(R.layout.fragment_about, null, false);
                TextView textView = (TextView) messageView.findViewById(R.id.about_credits);
                textView.setTextColor(textView.getTextColors().getDefaultColor());
                Builder builder = new Builder(this);
                builder.setIcon(R.drawable.ic_launcher);
                builder.setTitle(R.string.app_name);
                builder.setView(messageView);
                builder.create();
                builder.show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void launchMadhvanama(View v) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.setData(Uri.parse("market://details?id=com.vtayur.madhvanama"));
        startActivity(intent);
    }
}