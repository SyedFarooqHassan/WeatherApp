package eficode.fi.weatherapp.interfaces;


import eficode.fi.weatherapp.data.GetForecast;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface IEficodeWeatherService {
    @GET("/api/forecast")
    Call<GetForecast> getForecast(@Query("lat") double latitude,
                                  @Query("lon") double longitude);
}
