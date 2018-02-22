package eficode.fi.weatherapp.async;

import android.os.AsyncTask;

import java.util.List;

import eficode.fi.weatherapp.WeatherApplication;
import eficode.fi.weatherapp.entity.LocationInfo;
import eficode.fi.weatherapp.interfaces.IResponseHelper;


public class AsyncCheckDbId extends AsyncTask<String, Void, List<LocationInfo>> {
    IResponseHelper iResponseHelper;

    public AsyncCheckDbId(IResponseHelper iResponseHelper) {
        this.iResponseHelper = iResponseHelper;
    }

    @Override
    protected List<LocationInfo> doInBackground(String... params) {
        String id = params[0];
        return WeatherApplication.getInstance().getAppDatabase().iLocationDao().checkIdAlreadyExists(id);
    }

    @Override
    protected void onPostExecute(List<LocationInfo> result) {
        iResponseHelper.getData(result);

    }

    @Override
    protected void onPreExecute() {
    }

}
