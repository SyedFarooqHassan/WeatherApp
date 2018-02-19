package eficode.fi.weatherapp;

import android.app.Application;
import android.graphics.drawable.PictureDrawable;

import com.bumptech.glide.RequestBuilder;

import eficode.fi.weatherapp.interfaces.IApiWeatherService;
import eficode.fi.weatherapp.svg.GlideApp;
import eficode.fi.weatherapp.svg.SvgSoftwareLayerSetter;

import static eficode.fi.weatherapp.common.Extra.BASE_URL;

public class WeatherApplication extends Application {
    private static WeatherApplication instance;

    private IApiWeatherService iApiWeatherService;
    private RequestBuilder<PictureDrawable> requestBuilderPictureDrawable;

    public static WeatherApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;

        iApiWeatherService = RetrofitClient.getClient(BASE_URL).create(IApiWeatherService.class);
        requestBuilderPictureDrawable = GlideApp.with(this)
                .as(PictureDrawable.class)
                .listener(new SvgSoftwareLayerSetter());
    }

    public IApiWeatherService getApiWeatherService() {
        return iApiWeatherService;
    }

    public RequestBuilder<PictureDrawable> getRequestBuilderPictureDrawable() {
        return requestBuilderPictureDrawable;
    }
}
