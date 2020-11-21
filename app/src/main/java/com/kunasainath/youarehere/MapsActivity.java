package com.kunasainath.youarehere;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private final int REQUEST_CODE = 7;

    private FusedLocationProviderClient locationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        askLocationPermission();

        getTheUserLocation();
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        getTheUserLocation();
    }

    public void askLocationPermission(){
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == REQUEST_CODE){
            if(grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                getTheUserLocation();
            }else{
                Toast.makeText(this, "The app has no permission to access your location.", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void getTheUserLocation(){

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            askLocationPermission();
        }

        locationProviderClient = LocationServices.getFusedLocationProviderClient(this);


        if(locationProviderClient != null) {


            locationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    Location location = task.getResult();
                    mMap.setMyLocationEnabled(true);
                    if(location != null) {

                        LatLng latitudeAndLongitude = new LatLng(location.getLatitude(), location.getLongitude());

                        //mMap.addMarker(new MarkerOptions().position(latitudeAndLongitude).title(getString(R.string.app_name)));

                        CameraUpdate camera = CameraUpdateFactory.newLatLngZoom(latitudeAndLongitude, 50.0f);
                        mMap.moveCamera(camera);
                    }else{
                        Toast.makeText(MapsActivity.this, "Location not detected.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }else{
            Toast.makeText(this, "Something went wrong.", Toast.LENGTH_SHORT).show();
        }

    }

}