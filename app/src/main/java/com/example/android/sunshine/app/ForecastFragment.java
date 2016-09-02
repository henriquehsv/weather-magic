package com.example.android.sunshine.app;

import android.os.AsyncTask;
import android.os.Bundle;
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

    private ArrayAdapter<String> forecastAdapter;

    public ForecastFragment() {
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_refresh) {
            fetchWeatherData();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void fetchWeatherData() {FetchWeatherTask fetchWeatherTask = new FetchWeatherTask();
        fetchWeatherTask.execute("Fortaleza,CE,Brazil");
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

        List<String> weekForecast = new ArrayList<>();

        forecastAdapter = new ArrayAdapter<>(getContext(), R.layout.list_item_forecast, R.id.list_item_forecast_textview, weekForecast);

        ListView forecastList = (ListView) rootView.findViewById(R.id.listview_forecast);
        forecastList.setAdapter(forecastAdapter);

        fetchWeatherData();

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

        @Override
        protected void onPostExecute(String[] strings) {
            forecastAdapter.clear();
            forecastAdapter.addAll(strings);
            forecastAdapter.notifyDataSetChanged();
        }
    }
}
