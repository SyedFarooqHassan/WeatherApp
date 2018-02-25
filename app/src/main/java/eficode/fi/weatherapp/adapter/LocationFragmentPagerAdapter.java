package eficode.fi.weatherapp.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import eficode.fi.weatherapp.entity.LocationInfo;
import eficode.fi.weatherapp.fragment.LocationFragment;

public class LocationFragmentPagerAdapter extends FragmentPagerAdapter {
    final List<LocationInfo> locationInfoList;

    public LocationFragmentPagerAdapter(final FragmentManager fragmentManager) {
        super(fragmentManager);
        locationInfoList = new ArrayList<>();
    }

    public void addAll(final List<LocationInfo> locationInfoList) {
        this.locationInfoList.addAll(locationInfoList);
        notifyDataSetChanged();
    }

    // Returns total number of pages
    @Override
    public int getCount() {
        return locationInfoList.size();
    }

    // Returns the fragment to display for that page
    @Override
    public Fragment getItem(int position) {
        return LocationFragment.newInstance(locationInfoList.get(position));
    }

    // Returns the page title for the top indicator
    @Override
    public CharSequence getPageTitle(int position) {
        return locationInfoList.get(position).getLocationName();
    }
}
