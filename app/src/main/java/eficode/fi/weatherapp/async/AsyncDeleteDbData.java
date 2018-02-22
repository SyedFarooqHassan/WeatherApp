package eficode.fi.weatherapp.async;


import android.os.AsyncTask;

import java.util.List;

import eficode.fi.weatherapp.WeatherApplication;
import eficode.fi.weatherapp.entity.LocationInfo;

public class AsyncDeleteDbData extends AsyncTask<String, Void, String> {
    @Override
    protected String doInBackground(String... params) {
        String id = params[0];
        WeatherApplication.getInstance().getAppDatabase().iLocationDao().deleteId(id);
        return "data deleted";
    }

    @Override
    protected void onPostExecute(String result) {

    }

    @Override
    protected void onPreExecute() {
    }
}
