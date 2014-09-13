package io.github.jiayochen.sunshine.app;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    static final String QUERY_PARAM = "q";
    static final String GEO_BASE_URL = "geo:0,0?";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(LOG_TAG, "onCreate called");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new ForecastFragment())
                    .commit();
        }
    }

    @Override
    protected void onStart() {
        Log.d(LOG_TAG, "onStart called");
        super.onStart();
    }

    @Override
    protected void onResume() {
        Log.d(LOG_TAG, "onResume called");
        super.onResume();
    }

    @Override
    protected void onPause() {
        Log.d(LOG_TAG, "onPause called");
        super.onPause();
    }

    @Override
    protected void onStop() {
        Log.d(LOG_TAG, "onStop called");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.d(LOG_TAG, "onDestroy called");
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }
        else if (id == R.id.action_location) {
            String location = PreferenceManager.getDefaultSharedPreferences(this)
                    .getString(getString(R.string.pref_location_key), getString(R.string.pref_location_default));
            Uri builtUri = Uri.parse(GEO_BASE_URL).buildUpon()
                    .appendQueryParameter(QUERY_PARAM, location).build();
            showMap(builtUri);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void showMap(Uri geoLocation) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(geoLocation);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
        else {
            Toast.makeText(this, getString(R.string.msg_no_map_app), Toast.LENGTH_SHORT).show();
        }
    }

}
