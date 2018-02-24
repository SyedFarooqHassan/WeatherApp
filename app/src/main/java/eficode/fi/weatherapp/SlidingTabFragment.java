package eficode.fi.weatherapp;


import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import eficode.fi.weatherapp.async.AsyncGetDbAllData;
import eficode.fi.weatherapp.common.Extra;
import eficode.fi.weatherapp.common.view.SlidingTabLayout;
import eficode.fi.weatherapp.data.GetForecast;
import eficode.fi.weatherapp.entity.LocationInfo;
import eficode.fi.weatherapp.interfaces.ILocationHelper;
import eficode.fi.weatherapp.interfaces.IResponseHelper;

import static android.content.Context.LOCATION_SERVICE;

public class SlidingTabFragment extends Fragment implements ILocationHelper {
    private SlidingTabLayout mSlidingTabLayout;
    private ViewPager mViewPager;
    private ArrayList<GetForecast> getForecastArrayList;
    private ViewPagerAdapter viewPagerAdapter;
    private GpsChecker gpsChecker;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private Location location;
    ArrayList<LocationInfo> locationInfoArrayList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sliding_tab, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // BEGIN_INCLUDE (setup_viewpager)
        // Get the ViewPager and set it's PagerAdapter so that it can display items
        locationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener(this);
        getForecastArrayList = new ArrayList<>();
        mViewPager = view.findViewById(R.id.viewpager);
        viewPagerAdapter = new ViewPagerAdapter();
        mViewPager.setAdapter(viewPagerAdapter);
        // END_INCLUDE (setup_viewpager)

        // BEGIN_INCLUDE (setup_slidingtablayout)
        // Give the SlidingTabLayout the ViewPager, this must be done AFTER the ViewPager has had
        // it's PagerAdapter set.
        mSlidingTabLayout = view.findViewById(R.id.sliding_tabs);
        mSlidingTabLayout.setViewPager(mViewPager);
        gpsChecker = new GpsChecker(getActivity());


        AsyncGetDbAllData asyncGetDbData = new AsyncGetDbAllData(new IResponseHelper() {
            @Override
            public void getData(Object object) {
                locationInfoArrayList = (ArrayList<LocationInfo>) object;
                for (int i = 0; i < locationInfoArrayList.size(); i++) {
                    location = new Location(Extra.LOCATION_PROVIDER);
                    location.setLatitude(locationInfoArrayList.get(i).getLatitude());
                    location.setLongitude(locationInfoArrayList.get(i).getLongitude());
                    onLocationChanged(location);
                }
            }
        });
        asyncGetDbData.execute();
        askForPermission(Manifest.permission.ACCESS_FINE_LOCATION, Extra.LOCATION);
        // END_INCLUDE (setup_slidingtablayout)
    }

    @Override
    public void onLocationChanged(final Location location) {
        EficodeApiRequest.getForecast(location, new IResponseHelper() {
            @Override
            public void getData(final Object object) {
                final GetForecast getForecast = (GetForecast) object;
                getForecastArrayList.add(getForecast);
                viewPagerAdapter.notifyDataSetChanged();
            }
        });
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


    class ViewPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return getForecastArrayList.size();
        }


        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
            return o == view;
        }


        @Override
        public CharSequence getPageTitle(int position) {
            return "Item " + (position + 1);
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            // Inflate a new layout from our resources
            View view = getActivity().getLayoutInflater().inflate(R.layout.pager_item,
                    container, false);
            // Add the newly created View to the ViewPager
            container.addView(view);
            TextView tvCityName = view.findViewById(R.id.tv_city_name);
            tvCityName.setText(locationInfoArrayList.get(position).getLocationName());
            TextView tvWeatherDescription = view.findViewById(R.id.tv_weather_description);
            tvWeatherDescription.setText(getForecastArrayList.get(position).getDescription());
            ImageView ivWeatherCondition = view.findViewById(R.id.iv_weather_condition);
            ProgressBar pgWeatherLoading = view.findViewById(R.id.pb_weather_loading);
            pgWeatherLoading.setVisibility(View.INVISIBLE);
            Uri uri = Uri.parse(Extra.IMAGE_URL + getForecastArrayList.get(position).getIcon() + Extra.IMAGE_FORMAT);
            WeatherApplication.getInstance().getRequestBuilderPictureDrawable()
                    .load(uri).into(ivWeatherCondition);
            return view;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
            //Log.i(LOG_TAG, "destroyItem() [position: " + position + "]");
        }

    }
}
