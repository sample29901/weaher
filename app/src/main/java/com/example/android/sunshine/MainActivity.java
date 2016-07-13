package com.example.android.sunshine;
//API key  98bd9ea6b58ec679fd7281e900797081
//http://api.openweathermap.org/data/2.5/forecast/
//daily?q=London&mode=json&units=metric&cnt=7&lang=zh_cn&appid=98bd9ea6b58ec679fd7281e900797081
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;


public class MainActivity extends AppCompatActivity {

    private final String LOG_TAG = MainActivity.class.getSimpleName();
    private static final String DETAILFRAGMENT_TAG = "DFTAG";
    private String mLocation;
    private boolean mTwoPane;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        mLocation = Utility.getPreferredLocation(this);
        setContentView(R.layout.activity_main);
        Log.e("sample---","进入1111");
        if (findViewById(R.id.weather_detail_container) != null){
            mTwoPane = true;
            Log.e("sample---","进入2222");
            if (savedInstanceState == null){
                Log.e("sample---","meiyou3333");
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.weather_detail_container,new DetailFragment(),DETAILFRAGMENT_TAG)
                        .commit();
            }

        }else {
            mTwoPane = false;
        }
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //  Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.forecastfragment, menu);
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

        if (id == R.id.action_map) {
            openPreferredLocationInMap();
            return true;
        }
        //noinspection SimplifiableIfStatement
        /*if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    private void openPreferredLocationInMap() {
        /*SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        String location = sharedPref.getString(getString(R.string.pref_location_key),
                getString(R.string.pref_location_default));*/
        /*Uri geoLocation = Uri.parse("geo:0,0?????/").buildUpon().
                appendQueryParameter("q",location).build();*/
        String location = Utility.getPreferredLocation(this);
        Uri geoLocation = Uri.parse("geo:0,0?q=" + location);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(geoLocation);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
            Log.e(this.toString(), geoLocation.toString() + " " + location);
        } else {
            Log.e(this.toString(), "could not find " + location + " in the map.");

        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        String location = Utility.getPreferredLocation(this);
        if (location != null && location.equals(mLocation)) {

            ForecastFragment ff = (ForecastFragment)getSupportFragmentManager()
                    .findFragmentById(R.id.fragment_forecast);
            if (ff != null ) {
                ff.onLocationChanged();
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.example.android.sunshine/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.example.android.sunshine/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }


    //long 转 普通时间函数测试
    /*public static String getStringFromLong(long millis)
    {
        SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        java.util.Date dt = new Date(millis);
        return sdf.format(dt);
    }

    public static String getDate(long millis)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(millis);
        return String.format("%1$d年%2$d月%3$d日", cal.get(Calendar.YEAR), cal.get(Calendar.MONTH)+ 1,cal.get(Calendar.DAY_OF_MONTH));
    }*/


    //获取天气按钮点击事件
    /*public void catchWeather(View view){
        ForecastFragment.FetchWeatherTask weather = forecastFragment.new FetchWeatherTask();
        String position;
        EditText editext = (EditText)findViewById(R.id.city);
        position = editext.getText().toString();
        weather.execute(position);
    }*/

}