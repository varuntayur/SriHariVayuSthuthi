package com.vtayur.sriharivayusthuthi.home;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.etsy.android.grid.StaggeredGridView;
import com.vtayur.sriharivayusthuthi.data.DataProvider;
import com.vtayur.sriharivayusthuthi.R;
import com.vtayur.sriharivayusthuthi.detail.StaggeredGridAdapter;

import java.util.List;


public class HomeActivity extends ActionBarActivity {
    private static String TAG = "HomeActivity";
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sgv);

        progressDialog = ProgressDialog.show(this, "", getResources().getString(R.string.loading_please_wait), true);

        new DataProviderTask(this).execute(getAssets());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class DataProviderTask extends AsyncTask<AssetManager, Void, Long> {

        Activity currentActivity;

        public DataProviderTask(Activity activity) {
            this.currentActivity = activity;
        }

        protected void onPostExecute(Long result) {
            final LayoutInflater inflater = currentActivity.getLayoutInflater();
            final Activity activity = this.currentActivity;
            runOnUiThread(new Runnable() {
                public void run() {

                    StaggeredGridView listView = (StaggeredGridView) findViewById(R.id.grid_view);

                    View header = inflater.inflate(R.layout.list_item_header_footer, null);
                    TextView txtHeaderTitle = (TextView) header.findViewById(R.id.txt_title);
                    txtHeaderTitle.setText(getResources().getString(R.string.app_name));

                    listView.addHeaderView(header);
                    header.setClickable(false);

                    StaggeredGridAdapter mAdapter = new StaggeredGridAdapter(activity, R.id.txt_line1);

                    final List<String> sectionNames = DataProvider.getMenuNames();

                    for (String data : sectionNames) {
                        mAdapter.add(data);
                    }
                    listView.setAdapter(mAdapter);
                    listView.setOnItemClickListener(getOnMenuClickListener(activity));

                    progressDialog.dismiss();
                }
            });
            Log.d(TAG, "Finished launching main-menu");

        }

        private AdapterView.OnItemClickListener getOnMenuClickListener(final Activity activity) {
            return new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String item = (String) parent.getAdapter().getItem(position);

                    SriHariVayuSthuthiMenu.getEnum(item).execute(activity, item, position);

                }
            };
        }

        @Override
        protected Long doInBackground(AssetManager... assetManagers) {

            DataProvider.init(getAssets());
            Log.d(TAG, "Finished background task execution.");
            return 1l;
        }
    }
}
