package com.example.android.sunshine;
//http://api.openweathermap.org/data/2.5/forecast?
//q=Nanjing,CN&mode=json&units=metric&lang=zh_cn&cnt=50&appid=98bd9ea6b58ec679fd7281e900797081
//db5f1b7aae8249c8854867670ebfc312
//https://api.heweather.com/x3/weather?cityid=CN101190107&key=db5f1b7aae8249c8854867670ebfc312

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class ForecastFragment extends Fragment {

    private ArrayAdapter<String> mForecastAdapter;
    private ListView listView;
    private FetchWeatherTask weatherTask = new FetchWeatherTask();

    public ForecastFragment() {

    }

    @Override
    public void onStart() {
        super.onStart();
        updateWeather();
    }

    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.forecastfragment,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_refresh) {
            //Toast.makeText(getActivity(), "refresh", Toast.LENGTH_SHORT).show();
            updateWeather();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main,container,false);
        mForecastAdapter = new ArrayAdapter<>(
                getActivity(),
                R.layout.list_item_forecast,
                R.id.list_item_forecast_textView,
                new ArrayList<String>()
        );
        listView = (ListView)rootView.findViewById(R.id.list_forecast);
        listView.setAdapter(mForecastAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(getActivity(), "123456789", Toast.LENGTH_SHORT).show();
                String forecast = mForecastAdapter.getItem(position);
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                intent.putExtra(Intent.EXTRA_TEXT, forecast);
                startActivity(intent);
            }
        });
        return rootView;
    }

    private void updateWeather(){
        String position;
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        position = prefs.getString(getString(R.string.pref_location_key),
                getString(R.string.pref_location_default));
        /*String units1 = prefs.getString(getString(R.string.pref_units_key),
                getString(R.string.pref_units_metric));
        String units2 = prefs.getString(getString(R.string.pref_units_key),
                getString(R.string.pref_units_imperial));
        Toast.makeText(getActivity(),units1+"   "+units2,Toast.LENGTH_LONG).show();*/
        weatherTask.execute(position);
    }

    public class FetchWeatherTask extends AsyncTask<String, Void, String[]> {

        private final String LOG_TAG = FetchWeatherTask.class.getSimpleName();


        @Override
        protected void onPostExecute(String[] strings) {
            if(strings != null){
                mForecastAdapter.clear();
                mForecastAdapter.addAll(strings);
            }
        }

        private String[] getWeatherFromJson(String forecastString) throws JSONException{
            /* from api.openweathermap.org weatherDate process
            final String OWM_LIST = "list";
            final String OWM_WEATHER = "weather";
            final String OWM_DESCRIPTION = "description";
            final String OWM_DT = "dt_txt";
            final String OWM_MAX = "temp_max";
            final String OWM_MIN = "temp_min";
            final String OWM_MAIN = "main";
            JSONObject forecastJson = new JSONObject(forecastString);
            JSONArray listArray = forecastJson.getJSONArray(OWM_LIST);
            //JSONArray mainTempArray = forecastJson.getJSONArray(OWN_MAIN);
            //JSONArray weatherArray = forecastJson.getJSONArray(OWM_WEATHER);
            //JSONArray dataArray = forecastJson.getJSONArray(OWM_DT);
            Log.e("----------"," "+listArray.length());
            String []results = new String [listArray.length()];
            for (int i = 0; i <listArray.length() ; i++) {
                String day = listArray.getJSONObject(i).getString(OWM_DT);
                String description;
                String highAndLow;
                JSONObject weatherDes = listArray.getJSONObject(i);
                results[i] = day;
                JSONObject mainObj = weatherDes.getJSONObject(OWM_MAIN);
                highAndLow = formatHighLow(mainObj.getDouble(OWM_MIN),mainObj.getDouble(OWM_MAX));
                results[i] += " "+highAndLow;
                JSONArray weatherArray = weatherDes.getJSONArray(OWM_WEATHER);
                description = weatherArray.getJSONObject(0).getString(OWM_DESCRIPTION);
                results[i] += " "+description;
                Log.e("i=",i+"   "+results[i]);
            }*/
            JSONObject weatherJson = new JSONObject(forecastString);
            final String LIST = "HeWeather data service 3.0";
            final String DAY = "daily_forecast";
            final String DATE = "date";
            final String TEMP = "tmp";
            final String COND = "cond";
            JSONArray weatherArr = weatherJson.getJSONArray(LIST);
            JSONArray dayArray = weatherArr.getJSONObject(0).getJSONArray(DAY);
            String [] results = new String[dayArray.length()];
            for (int i = 0; i < dayArray.length();i++){
                String date;
                String temp;
                String weatherDes;
                date = dayArray.getJSONObject(i).getString(DATE).substring(5);
                temp = dayArray.getJSONObject(i).getJSONObject(TEMP).getString("min");
                temp += "/"+dayArray.getJSONObject(i).getJSONObject(TEMP).getString("max");
                weatherDes ="D:"+dayArray.getJSONObject(i).getJSONObject(COND).getString("txt_d");
                weatherDes +=" N:"+dayArray.getJSONObject(i).getJSONObject(COND).getString("txt_n");
                results[i]= date+"    "+temp+"    "+weatherDes;
                //Log.e("i=",i+"   "+results[i]);
            }
            return results;
        }

        @Override
        protected String[] doInBackground(String... params) {
            if(params.length == 0){
                return null;
            }
            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            // Will contain the raw JSON response as a string.
            String forecastJsonStr = null;
            /*String format = "json";
            String units = "metric";
            String id = "98bd9ea6b58ec679fd7281e900797081";
            int numDays = 50;*/
            try {
                // Construct the URL for the OpenWeatherMap query
                // Possible parameters are available at OWM's forecast API page, at
                // http://openweathermap.org/API#forecast
                //http://api.openweathermap.org/data/2.5/forecast?
                //q=Nanjing,CN&mode=json&appid=98bd9ea6b58ec679fd7281e900797081
                //URL url = new URL();
                /*final String FORECAST_BASE_URL = "http://api.openweathermap.org/data/2.5/forecast?lang=zh_cn";
                final String QUERY_PARAM = "q";
                final String FORMAT_PARAM = "mode";
                final String UNITS_PARAM = "units";
                final String DAYS_PARAM = "cnt";
                final String APPID_PARAM = "appid";
                Uri builtUri = Uri.parse(FORECAST_BASE_URL).buildUpon()
                        .appendQueryParameter(QUERY_PARAM, params[0])
                        .appendQueryParameter(FORMAT_PARAM, format)
                        .appendQueryParameter(UNITS_PARAM, units)
                        .appendQueryP,arameter(DAYS_PARAM Integer.toString(numDays))
                        .appendQueryParameter(APPID_PARAM,id).build();
                URL url = new URL(builtUri.toString());*/
                String urlString ="https://api.heweather.com/x3/weather?city=" +
                        params[0]+"&key=db5f1b7aae8249c8854867670ebfc312";
                URL url = new URL(urlString);
                //Log.e(LOG_TAG, "Built URI " + builtUri.toString());
                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    forecastJsonStr = null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    forecastJsonStr = null;
                }
                forecastJsonStr = buffer.toString();
                //Log.e("weather------------",forecastJsonStr);
                try {
                    return getWeatherFromJson(forecastJsonStr);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attempting
                // to parse it.
                forecastJsonStr = null;
            } finally{
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }
            return null;
        }
    }

}