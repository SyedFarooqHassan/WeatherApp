package eficode.fi.weatherapp;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;


import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;

import java.util.ArrayList;
import java.util.Collections;

import eficode.fi.weatherapp.async.AsyncCheckDbId;
import eficode.fi.weatherapp.async.AsyncGetDbData;
import eficode.fi.weatherapp.async.AsyncInsertDbData;
import eficode.fi.weatherapp.entity.LocationInfo;
import eficode.fi.weatherapp.interfaces.IOnItemClickListener;
import eficode.fi.weatherapp.interfaces.IResponseHelper;
import eficode.fi.weatherapp.recyclerview.RecyclerViewAdapter;
import eficode.fi.weatherapp.recyclerview.RecyclerViewClickListener;

public class AddCityActivity extends AppCompatActivity implements PlaceSelectionListener, IOnItemClickListener {

    private LocationInfo locationInfo;
    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;
    private ArrayList<LocationInfo> locationInfoArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_city);
        initializeUI();
        initializeObj();
    }

    private void initializeObj() {
        locationInfoArrayList = new ArrayList<>();
        recyclerViewAdapter = new RecyclerViewAdapter(this, locationInfoArrayList);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        AsyncGetDbData asyncGetDbData = new AsyncGetDbData(new IResponseHelper() {
            @Override
            public void getData(Object object) {
                ArrayList<LocationInfo> arrayList = (ArrayList<LocationInfo>) object;
                for (int i = 0; i < arrayList.size(); i++) {
                    Collections.addAll(locationInfoArrayList, arrayList.get(i));
                }
                recyclerViewAdapter.notifyDataSetChanged();

            }
        });
        asyncGetDbData.execute();

    }

    private void initializeUI() {
        PlaceAutocompleteFragment placeAutocompleteFragment = (PlaceAutocompleteFragment) getFragmentManager().findFragmentById(R.id.fragment_place_autocomplete);
        placeAutocompleteFragment.setOnPlaceSelectedListener(this);
        recyclerView = findViewById(R.id.rv_cities);
        recyclerView.addOnItemTouchListener(new RecyclerViewClickListener(this,recyclerView,this));
    }

    @Override
    public void onItemClick(View view, int position) {

    }

    @Override
    public void onLongItemClick(View view, int position) {

    }


    @Override
    public void onPlaceSelected(final Place place) {
        new AsyncCheckDbId(new IResponseHelper() {
            @Override
            public void getData(Object object) {
                ArrayList<LocationInfo> arrayList = (ArrayList<LocationInfo>) object;
                if (arrayList.size() > 0) {
                    createDialog();
                } else {
                    locationInfo = new LocationInfo();
                    locationInfo.setLocationId(place.getId());
                    locationInfo.setLatitude(place.getLatLng().latitude);
                    locationInfo.setLongitude(place.getLatLng().longitude);
                    locationInfo.setLocationName(place.getName().toString());
                    new AsyncInsertDbData().execute(locationInfo);
                    locationInfoArrayList.add(locationInfo);
                    recyclerViewAdapter.notifyDataSetChanged();
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
