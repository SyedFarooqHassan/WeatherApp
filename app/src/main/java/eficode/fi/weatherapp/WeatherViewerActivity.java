package eficode.fi.weatherapp;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import eficode.fi.weatherapp.adapter.LocationFragmentPagerAdapter;
import eficode.fi.weatherapp.common.Extra;
import eficode.fi.weatherapp.entity.LocationInfo;

public class WeatherViewerActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_viewer);

        initializeUI();
        initializeData();
    }

    private void initializeUI() {
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        tabLayout = findViewById(R.id.tabs);
        viewPager = findViewById(R.id.view_pager);
    }

    private void initializeData() {
        final List<LocationInfo> locationInfoList = (ArrayList<LocationInfo>) getIntent().getSerializableExtra(Extra.LOCATION_INFO_LIST);
        final LocationFragmentPagerAdapter locationFragmentPagerAdapter = new LocationFragmentPagerAdapter(getSupportFragmentManager());
        locationFragmentPagerAdapter.addAll(locationInfoList);

        viewPager.setAdapter(locationFragmentPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        final int selectedIndex = getIntent().getIntExtra(Extra.LOCATION_INFO_SELECTED_INDEX, -1);
        if (selectedIndex != -1) {
            viewPager.setCurrentItem(selectedIndex, true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


    /*public void askForPermission(@NonNull String permission, @NonNull Integer requestCode) {
        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                //This is called if user has denied the permission before
                //In this case I am just asking the permission again
                ActivityCompat.requestPermissions(this, new String[]{permission}, requestCode);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{permission}, requestCode);
            }
        } else if (requestCode == Extra.LOCATION) {
            //getLocation();
        }
    }*/


    /*what happens if user grants location or clicks never ask again*/
    //@Override
    /*public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
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
                        //getLocation();
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
            //getLocation();
        }
    }*/


}
