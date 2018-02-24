package eficode.fi.weatherapp.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import eficode.fi.weatherapp.R;
import eficode.fi.weatherapp.common.Extra;
import eficode.fi.weatherapp.entity.LocationInfo;

public class LocationFragment extends Fragment {
    private LocationInfo locationInfo;

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
    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.pager_item, container, false);

        TextView locationName = view.findViewById(R.id.tv_location_name);
        locationName.setText(locationInfo.getLocationName());

        return view;
    }

}
