package eficode.fi.weatherapp.fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import eficode.fi.weatherapp.EficodeApiRequest;
import eficode.fi.weatherapp.GpsChecker;
import eficode.fi.weatherapp.LocationListener;
import eficode.fi.weatherapp.R;
import eficode.fi.weatherapp.WeatherApplication;
import eficode.fi.weatherapp.common.Extra;
import eficode.fi.weatherapp.data.GetForecast;
import eficode.fi.weatherapp.entity.LocationInfo;
import eficode.fi.weatherapp.interfaces.ILocationHelper;
import eficode.fi.weatherapp.interfaces.IResponseHelper;

import static android.content.Context.LOCATION_SERVICE;

public class LocationFragment extends Fragment implements ILocationHelper {
    private LocationInfo locationInfo;

    private TextView tvLocationName;
    private ImageView ivWeatherCondition;
    private TextView tvdescription;

    private ProgressBar pbWeatherLoading;

    private GpsChecker gpsChecker;
    private LocationManager locationManager;
    private LocationListener locationListener;

    public static Fragment newInstance(final LocationInfo locationInfo) {
        LocationFragment locationFragment = new LocationFragment();

        Bundle args = new Bundle();
        args.putSerializable(Extra.LOCATION_INFO, locationInfo);
        locationFragment.setArguments(args);

        return locationFragment;
    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        locationInfo = (LocationInfo) getArguments().getSerializable(Extra.LOCATION_INFO);

        //it is current location
        if (locationInfo.getLocationId() == null) {
            locationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);
            locationListener = new LocationListener(this);

            gpsChecker = new GpsChecker(getActivity());

            askForPermission(Manifest.permission.ACCESS_FINE_LOCATION, Extra.LOCATION);
        }
    }

    public void askForPermission(@NonNull String permission, @NonNull Integer requestCode) {
        if (ActivityCompat.checkSelfPermission(getContext(), permission) != PackageManager.PERMISSION_GRANTED) {

            //This is called if user has denied the permission before
            //In this case I am just asking the permission again
            requestPermissions( //Method of Fragment
                    new String[]{permission}, requestCode

            );

        } else if (requestCode == Extra.LOCATION) {
            getLocation();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == Extra.LOCATION) {
            if (permissions[0].equals(Manifest.permission.ACCESS_FINE_LOCATION)
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLocation();
            }
        }
    }

    public void getLocation() {
        try {
            boolean isGpsOn = gpsChecker.CheckStatus();
            if (isGpsOn) {
                locationManager.requestLocationUpdates(LocationManager
                        .GPS_PROVIDER, Extra.MIN_TIME, Extra.MIN_DISTANCE, locationListener); //set after how much distance and time you will get location
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item, container, false);

        tvLocationName = view.findViewById(R.id.tv_location_name);
        tvLocationName.setText(locationInfo.getLocationName());

        ivWeatherCondition = view.findViewById(R.id.iv_weather_condition);
        tvdescription = view.findViewById(R.id.tv_weather_description);

        pbWeatherLoading = view.findViewById(R.id.pb_weather_loading);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (locationInfo.getLocationId() != null) {
            onLocationChanged(locationInfo.getLatitude(), locationInfo.getLongitude());
        }
    }

    @Override
    public void onLocationChanged(final double latitude, final double longitude) {
        EficodeApiRequest.getForecast(latitude, longitude, new IResponseHelper() {
            @Override
            public void getData(final Object object) {
                final GetForecast getForecast = (GetForecast) object;

                Uri uri = Uri.parse(Extra.IMAGE_URL + getForecast.getIcon() + Extra.IMAGE_FORMAT);
                WeatherApplication.getInstance().getRequestBuilderPictureDrawable()
                        .load(uri).into(ivWeatherCondition);

                tvdescription.setText(getForecast.getDescription());
                pbWeatherLoading.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onDestroy() {
        if (locationManager != null && locationListener != null) {
            locationManager.removeUpdates(locationListener);
        }
        super.onDestroy();
    }
}
