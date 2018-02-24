package eficode.fi.weatherapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;

import java.util.ArrayList;

import eficode.fi.weatherapp.adapter.RecyclerViewAdapter;
import eficode.fi.weatherapp.async.AsyncCheckDbId;
import eficode.fi.weatherapp.async.AsyncDeleteDbData;
import eficode.fi.weatherapp.async.AsyncGetDbAllData;
import eficode.fi.weatherapp.async.AsyncInsertDbData;
import eficode.fi.weatherapp.common.Extra;
import eficode.fi.weatherapp.entity.LocationInfo;
import eficode.fi.weatherapp.interfaces.IOnItemClickListener;
import eficode.fi.weatherapp.interfaces.IResponseHelper;

public class MainActivity extends AppCompatActivity implements PlaceSelectionListener, IOnItemClickListener {
    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeUI();
        initializeData();
    }

    private void initializeData() {
        recyclerViewAdapter = new RecyclerViewAdapter(this, this);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        final LocationInfo currentLocationInfo = new LocationInfo();
        currentLocationInfo.setLocationName(getString(R.string.current_location));

        recyclerViewAdapter.add(currentLocationInfo);

        AsyncGetDbAllData asyncGetDbData = new AsyncGetDbAllData(new IResponseHelper() {
            @Override
            public void getData(Object object) {
                recyclerViewAdapter.addAll((ArrayList<LocationInfo>) object);
            }
        });
        asyncGetDbData.execute();
    }

    @Override
    public void onItemClick(View view, final Object object) {
        switch (view.getId()) {
            case R.id.ib_delete_cities:
                final LocationInfo locationInfo = (LocationInfo) object;
                new AsyncDeleteDbData().execute(locationInfo.getLocationId());
                recyclerViewAdapter.remove(locationInfo);
                break;
            case R.id.rl_city_info:
                Intent intent = new Intent(MainActivity.this, WeatherViewerActivity.class);
                intent.putExtra(Extra.LOCATION_INFO_LIST, (ArrayList) recyclerViewAdapter.getLocationInfoList());
                intent.putExtra(Extra.LOCATION_INFO_SELECTED_INDEX, (int) object);
                startActivity(intent);
                break;
        }
    }

    private void initializeUI() {
        PlaceAutocompleteFragment placeAutocompleteFragment = (PlaceAutocompleteFragment) getFragmentManager().findFragmentById(R.id.fragment_place_autocomplete);
        placeAutocompleteFragment.setOnPlaceSelectedListener(this);
        recyclerView = findViewById(R.id.rv_cities);
    }


    @Override
    public void onPlaceSelected(final Place place) {
        new AsyncCheckDbId(new IResponseHelper() {
            @Override
            public void getData(Object object) {
                LocationInfo locationInfo = (LocationInfo) object;
                if (locationInfo != null) {
                    createDialog();
                } else {
                    locationInfo = new LocationInfo(place.getId(), place.getName().toString(), place.getLatLng().latitude, place.getLatLng().longitude);
                    new AsyncInsertDbData().execute(locationInfo);
                    recyclerViewAdapter.add(locationInfo);
                }

            }
        }).execute(place.getId());
    }


    @Override
    public void onError(Status status) {
        // TODO: Handle the error.
    }

    public void createDialog() {
        new AlertDialog.Builder(this).setTitle(R.string.already_present).setMessage(R.string.clicked_city_is_already_present_in_added_cities).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        }).create().show();
    }
}
