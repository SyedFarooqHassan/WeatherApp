package eficode.fi.weatherapp.async;


import android.os.AsyncTask;

import eficode.fi.weatherapp.WeatherApplication;
import eficode.fi.weatherapp.entity.LocationInfo;

public class AsyncInsertDbData extends AsyncTask<LocationInfo, Void, String> {
    @Override
    protected String doInBackground(LocationInfo... locationInfo) {
        WeatherApplication.getInstance().getAppDatabase().iLocationDao().insertAll(locationInfo);
        return "Data Saved";
    }

    @Override
    protected void onPostExecute(String result) {

    }

    @Override
    protected void onPreExecute() {
    }

    @Override
    protected void onProgressUpdate(Void... values) {
    }
}
