package eficode.fi.weatherapp.async;

import android.os.AsyncTask;

import java.util.List;

import eficode.fi.weatherapp.WeatherApplication;
import eficode.fi.weatherapp.entity.LocationInfo;
import eficode.fi.weatherapp.interfaces.IResponseHelper;


public class AsyncCheckDbId extends AsyncTask<String, Void, LocationInfo> {
    IResponseHelper iResponseHelper;

    public AsyncCheckDbId(IResponseHelper iResponseHelper) {
        this.iResponseHelper = iResponseHelper;
    }

    @Override
    protected LocationInfo doInBackground(String... params) {
        String id = params[0];
        return WeatherApplication.getInstance().getAppDatabase().iLocationDao().getDataWithId(id);
    }

    @Override
    protected void onPostExecute(LocationInfo result) {
        iResponseHelper.getData(result);

    }

    @Override
    protected void onPreExecute() {
    }

}
