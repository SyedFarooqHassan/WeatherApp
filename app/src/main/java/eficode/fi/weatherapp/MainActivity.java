package eficode.fi.weatherapp;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import eficode.fi.weatherapp.common.Extra;
import eficode.fi.weatherapp.data.GetForecast;
import eficode.fi.weatherapp.interfaces.ILocationHelper;
import eficode.fi.weatherapp.interfaces.IResponseHelper;

public class MainActivity extends AppCompatActivity implements ILocationHelper, View.OnClickListener {
    private GpsChecker gpsChecker;
    private LocationManager locationManager;
    private LocationListener locationListener;

    private ImageView ivWeatherCondition;
    private TextView tvWeatherDescription;
    private ProgressBar pgWeatherLoading;
    private FloatingActionButton fbAddCities;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeUI();
        initializeObj();
        askForLocationPermission();
    }

    private void initializeUI() {
        ivWeatherCondition = findViewById(R.id.iv_weather_condition);
        ivWeatherCondition.setVisibility(View.INVISIBLE);
        tvWeatherDescription = findViewById(R.id.tv_weather_description);
        tvWeatherDescription.setVisibility(View.INVISIBLE);
        pgWeatherLoading = findViewById(R.id.pb_weather_loading);
        fbAddCities = findViewById(R.id.fb_add_cities);
        fbAddCities.setVisibility(View.INVISIBLE);
        fbAddCities.setOnClickListener(this);
    }

    private void initializeObj() {
        gpsChecker = new GpsChecker(MainActivity.this);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fb_add_cities:
                Intent intent = new Intent(MainActivity.this,AddCityActivity.class);
                startActivity(intent);
                break;
        }
    }

    /*asks for permission of location*/
    private void askForLocationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //this code will be executed on devices running on DONUT (NOT ICS) or later
            askForPermission(Manifest.permission.ACCESS_FINE_LOCATION, Extra.LOCATION);
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

    public void askForPermission(@NonNull String permission, @NonNull Integer requestCode) {
        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                //This is called if user has denied the permission before
                //In this case I am just asking the permission again
                ActivityCompat.requestPermissions(this, new String[]{permission}, requestCode);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{permission}, requestCode);
            }
        } else if (requestCode == Extra.LOCATION) {
            getLocation();
        }
    }

    /*what happens if user grants location or clicks never ask again*/
    @Override
    public void onRequestPermissionsResult(@NonNull int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (permissions.length == 0) {
            return;
        }
        boolean allPermissionsGranted = true;
        if (grantResults.length > 0) {
            for (int grantResult : grantResults) {
                if (grantResult != PackageManager.PERMISSION_GRANTED) {
                    allPermissionsGranted = false;
                    break;
                }
            }
        }
        if (!allPermissionsGranted) {
            boolean somePermissionsForeverDenied = false;
            for (String permission : permissions) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                    //denied
                } else {
                    if (ActivityCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
                            && requestCode == Extra.LOCATION) {
                        getLocation();
                    } else {
                        //set to never ask again
                        somePermissionsForeverDenied = true;
                    }
                }
            }
            if (somePermissionsForeverDenied) {
                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setTitle(R.string.permissions_required)
                        .setMessage(R.string.you_have_explicitly_denied_permissions_which_are_required_by_this_app_to_run_for_this_action_open_settings_go_to_permissions_and_allow_them)
                        .setPositiveButton(R.string.settings, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                        Uri.fromParts(Extra.PACKAGE, getPackageName(), null));
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .setCancelable(false)
                        .create()
                        .show();
            }
        } else if (requestCode == Extra.LOCATION) {
            getLocation();
        }
    }
    public void setVisibility(){
        pgWeatherLoading.setVisibility(View.INVISIBLE);
        ivWeatherCondition.setVisibility(View.VISIBLE);
        tvWeatherDescription.setVisibility(View.VISIBLE);
        fbAddCities.setVisibility(View.VISIBLE);
    }
    @Override
    public void onLocationChanged(final Location location) {
        ApiRequest.getForecast(location, new IResponseHelper() {
            @Override
            public void getData(final Object object) {
                final GetForecast getForecast = (GetForecast) object;
                setVisibility();
                tvWeatherDescription.setText(getForecast.getDescription());
                Uri uri = Uri.parse(Extra.IMAGE_URL + getForecast.getIcon() + Extra.IMAGE_FORMAT);
                WeatherApplication.getInstance().getRequestBuilderPictureDrawable()
                        .load(uri).into(ivWeatherCondition);

            }
        });
    }
}
