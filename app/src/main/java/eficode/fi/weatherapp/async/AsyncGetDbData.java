package eficode.fi.weatherapp.async;


import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.List;

import eficode.fi.weatherapp.WeatherApplication;
import eficode.fi.weatherapp.entity.LocationInfo;
import eficode.fi.weatherapp.interfaces.IResponseHelper;

public class AsyncGetDbData extends AsyncTask<LocationInfo, Void, List<LocationInfo>> {
    private IResponseHelper iResponseHelper;

    public AsyncGetDbData(IResponseHelper iResponseHelper) {
        this.iResponseHelper = iResponseHelper;
    }

    @Override
    protected List<LocationInfo> doInBackground(LocationInfo... locationInfo) {
        return WeatherApplication.getInstance().getAppDatabase().iLocationDao().getAll();
    }

    @Override
    protected void onPostExecute(List result) {
        iResponseHelper.getData(result);
    }

    @Override
    protected void onPreExecute() {
    }


}
