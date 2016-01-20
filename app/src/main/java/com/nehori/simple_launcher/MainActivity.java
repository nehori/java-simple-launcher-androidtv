package com.nehori.simple_launcher;


import java.util.List;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class MainActivity extends Activity {

    private List<ResolveInfo> mApps;
    private GridView mGrid;
    private OnItemClickListener listener = new OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            ResolveInfo info = mApps.get(position);
            String pkg = info.activityInfo.packageName;
            String cls = info.activityInfo.name;
            ComponentName component = new ComponentName(pkg, cls);

                    Intent i = new Intent();
            i.setComponent(component);
            startActivity(i);
        }

    };

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loadApps();
        setContentView(R.layout.activity_main);
        mGrid = (GridView) findViewById(R.id.apps_list);
        mGrid.setAdapter(new AppsAdapter());

        mGrid.setOnItemClickListener(listener);
    }

    private void loadApps() {
        Intent leanbackLauncherIntent = new Intent(Intent.ACTION_MAIN, null);
        leanbackLauncherIntent.addCategory(Intent.CATEGORY_LEANBACK_LAUNCHER);

        mApps = getPackageManager().queryIntentActivities(leanbackLauncherIntent,
				getPackageManager().GET_META_DATA | getPackageManager().GET_ACTIVITIES);
    }

    public class AppsAdapter extends BaseAdapter {
        public AppsAdapter() {
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView i;

            if (convertView == null) {
                i = new ImageView(MainActivity.this);
                i.setScaleType(ImageView.ScaleType.FIT_CENTER);
                i.setLayoutParams(new GridView.LayoutParams(200, 200));
                i.setPadding(12, 12, 12, 12);
            } else {
                i = (ImageView) convertView;
            }

            ResolveInfo info = mApps.get(position);
            Log.d("MainActivity", "app = " + info.loadLabel(getPackageManager()).toString());

            Drawable banner = info.activityInfo.loadBanner(getPackageManager());
            if (banner == null) {
                Log.d("MainActivity", "loadBanner is null.");
                banner = info.activityInfo.loadLogo(getPackageManager());
            }
            if (banner == null) {
                Log.d("MainActivity", "loadLogo is null.");
                banner = info.activityInfo.loadIcon(getPackageManager());
            }
            i.setImageDrawable(banner);

            return i;
        }

        public final int getCount() {
            return mApps.size();
        }

        public final Object getItem(int position) {
            return mApps.get(position);
        }

        public final long getItemId(int position) {
            return position;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }
}
