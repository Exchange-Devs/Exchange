package com.example.exchange;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback
{
    private GoogleMap mapView;
    private static final int REQUEST_LOCATION = 1;
    LocationManager locationManager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapView);
        mapFragment.getMapAsync(this);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mapView = googleMap;

        final int function = getIntent().getExtras().getInt("function");

        // showing current user location
        if(function == 1){
            showCurrentUserInMap(mapView);
        }
        // showing closest user location
        else if(function == 2){
            showClosestUser(mapView);
        }
        // showing closest store on map
        else if(function == 4){
            showClosestStore(mapView);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode){
            case REQUEST_LOCATION:
                saveCurrentUserLocation();
                break;
        }
    }
    private void saveCurrentUserLocation() {
        // requesting permission to get user's location
        if (ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MapsActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
            } else {
                // getting last know user's location
                Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                // checking if the location is null
                if (location != null) {
                    // if it isn't, save it to Back4App Dashboard
                    ParseGeoPoint currentUserLocation = new ParseGeoPoint(location.getLatitude(), location.getLongitude());

                    ParseUser currentUser = ParseUser.getCurrentUser();

                    if (currentUser != null) {
                        currentUser.put("Location", currentUserLocation);
                        currentUser.saveInBackground();
                    } else {
                        // do something like coming back to the login activity
                    }
                } else {
                    // if it is null, do something like displaying error and coming back to the menu activity
                }
            }
        } else {
            // getting last know user's location
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            // checking if the location is null
            if (location != null) {
                // if it isn't, save it to Back4App Dashboard
                ParseGeoPoint currentUserLocation = new ParseGeoPoint(location.getLatitude(), location.getLongitude());

                ParseUser currentUser = ParseUser.getCurrentUser();

                if (currentUser != null) {
                    currentUser.put("Location", currentUserLocation);
                    currentUser.saveInBackground();
                } else {
                    // do something like coming back to the login activity
                }
            } else {
                // if it is null, do something like displaying error and coming back to the menu activity
            }
        }


    /* saving the current user location, using the saveCurrentUserLocation method of Step 3, to avoid
    null pointer exception and also to return the user's most current location */
        saveCurrentUserLocation();
    }

    private void showCurrentUserInMap(final GoogleMap googleMap) {

        // calling retrieve user's location method of Step 4
        ParseGeoPoint currentUserLocation = getCurrentUserLocation();

        // creating a marker in the map showing the current user location
        LatLng currentUser = new LatLng(currentUserLocation.getLatitude(), currentUserLocation.getLongitude());
        googleMap.addMarker(new MarkerOptions().position(currentUser).title(ParseUser.getCurrentUser().getUsername()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));

        // zoom the map to the currentUserLocation
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentUser, 5));
    }

    private ParseGeoPoint getCurrentUserLocation() {
        // finding currentUser
        ParseUser currentUser = ParseUser.getCurrentUser();

        if (currentUser == null) {
            // if it's not possible to find the user, do something like returning to login activity
        }
        // otherwise, return the current user location
        return currentUser.getParseGeoPoint("Location");
    }

    private void showClosestUser(final GoogleMap googleMap) {
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereNear("Location", getCurrentUserLocation());
        // setting the limit of near users to find to 2, you'll have in the nearUsers list only two users: the current user and the closest user from the current
        query.setLimit(2);
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> nearUsers, ParseException e) {
                if (e == null) {
                    // avoiding null pointer
                    ParseUser closestUser = ParseUser.getCurrentUser();
                    // set the closestUser to the one that isn't the current user
                    for (int i = 0; i < nearUsers.size(); i++) {
                        if (!nearUsers.get(i).getObjectId().equals(ParseUser.getCurrentUser().getObjectId())) {
                            closestUser = nearUsers.get(i);
                        }
                    }
                    // finding and displaying the distance between the current user and the closest user to him, using method implemented in Step 4
                    double distance = getCurrentUserLocation().distanceInKilometersTo(closestUser.getParseGeoPoint("Location"));
                    alertDisplayer("We found the closest user from you!", "It's " + closestUser.getUsername() + ". \n You are " + Math.round(distance * 100.0) / 100.0 + " km from this user.");
                    // showing current user in map, using the method implemented in Step 5
                    showCurrentUserInMap(googleMap);
                    // creating a marker in the map showing the closest user to the current user location
                    LatLng closestUserLocation = new LatLng(closestUser.getParseGeoPoint("Location").getLatitude(), closestUser.getParseGeoPoint("Location").getLongitude());
                    googleMap.addMarker(new MarkerOptions().position(closestUserLocation).title(closestUser.getUsername()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                    // zoom the map to the currentUserLocation
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(closestUserLocation, 3));
                } else {
                    Log.d("store", "Error: " + e.getMessage());
                }
            }
        });
        ParseQuery.clearAllCachedResults();
    }

    private void alertDisplayer(String title, String message) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(MapsActivity.this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        android.app.AlertDialog ok = builder.create();
        ok.show();
    }

    private void showStoresInMap(final GoogleMap googleMap) {

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Stores");
        query.whereExists("Location");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> stores, ParseException e) {
                if (e == null) {
                    for (int i = 0; i < stores.size(); i++) {
                        LatLng storeLocation = new LatLng(stores.get(i).getParseGeoPoint("Location").getLatitude(), stores.get(i).getParseGeoPoint("Location").getLongitude());
                        googleMap.addMarker(new MarkerOptions().position(storeLocation).title(stores.get(i).getString("Name")).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
                    }
                } else {
                    // handle the error
                    Log.d("store", "Error: " + e.getMessage());
                }
            }
        });
        ParseQuery.clearAllCachedResults();
    }

    private void showClosestStore(final GoogleMap googleMap) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Stores");
        query.whereNear("Location", getCurrentUserLocation());
        // setting the limit of near stores to 1, you'll have in the nearStores list only one object: the closest store from the current user
        query.setLimit(1);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> nearStores, ParseException e) {
                if (e == null) {
                    ParseObject closestStore = nearStores.get(0);
                    // showing current user location, using the method implemented in Step 5
                    showCurrentUserInMap(googleMap);
                    // finding and displaying the distance between the current user and the closest store to him, using method implemented in Step 4
                    double distance = getCurrentUserLocation().distanceInKilometersTo(closestStore.getParseGeoPoint("Location"));
                    alertDisplayer("We found the closest store from you!", "It's " + closestStore.getString("Name") + ". \nYou are " + Math.round(distance * 100.0) / 100.0 + " km from this store.");
                    // creating a marker in the map showing the closest store to the current user
                    LatLng closestStoreLocation = new LatLng(closestStore.getParseGeoPoint("Location").getLatitude(), closestStore.getParseGeoPoint("Location").getLongitude());
                    googleMap.addMarker(new MarkerOptions().position(closestStoreLocation).title(closestStore.getString("Name")).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                    // zoom the map to the closestStoreLocation
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(closestStoreLocation, 3));
                } else {
                    Log.d("store", "Error: " + e.getMessage());
                }
            }
        });

        ParseQuery.clearAllCachedResults();

    }



}


