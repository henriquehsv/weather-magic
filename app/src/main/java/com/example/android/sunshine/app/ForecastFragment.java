package com.example.android.sunshine.app;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.android.sunshine.app.networking.OpenWeatherMapAPI;
import com.example.android.sunshine.app.parsing.WeatherDataParser;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class ForecastFragment extends Fragment {

    public ForecastFragment() {
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_refresh) {
            FetchWeatherTask fetchWeatherTask = new FetchWeatherTask();
            fetchWeatherTask.execute("Fortaleza,CE,Brazil");

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.forecastfragment, menu);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        String[] data = {"Mon 6/23 - Sunny - 31/17", "Tue 6/24 - Foggy - 21/8", "Wed 6/25 - Cloudy - 22/17", "Thurs 6/26 - Rainy - 18/11",
                "Fri 6/27 - Foggy - 21/10", "Sat 6/28 - TRAPPED IN WEATHERSTATION - 23/18", "Sun 6/29 - Sunny - 20/7"};

        List<String> weekForecast = new ArrayList<>(Arrays.asList(data));

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.list_item_forecast, R.id.list_item_forecast_textview, weekForecast);

        ListView forecastList = (ListView) rootView.findViewById(R.id.listview_forecast);
        forecastList.setAdapter(adapter);


        return rootView;
    }

    private class FetchWeatherTask extends AsyncTask<String, Void, String[]> {

        @Override
        protected String[] doInBackground(String... strings) {
            String weatherData = OpenWeatherMapAPI.getInstance().getWeatherData(strings[0]);
            String[] weatherInfo = null;
            try {
                weatherInfo = new WeatherDataParser().getWeatherDataFromJson(weatherData, 7);
            } catch (JSONException e) {
                Log.e(ForecastFragment.class.getName(), "Error ", e);
            }

            return weatherInfo;
        }
    }
}
