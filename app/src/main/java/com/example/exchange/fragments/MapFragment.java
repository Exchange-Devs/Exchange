package com.example.exchange.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.exchange.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapFragment extends Fragment {

    public MapFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        // Initialize the map fragment
        SupportMapFragment supportMapFragment = (SupportMapFragment)
                getChildFragmentManager().findFragmentById(R.id.google_map);

        // Async map
        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng latLng) {
                        // by clicking on the map
                        // initiate the marker position
                        MarkerOptions markerOptions = new MarkerOptions();
                        //set the position
                        markerOptions.position(latLng);
                        //set the title
                        markerOptions.title(latLng.latitude + " : " + latLng.longitude);
                        // clear the markers
                        googleMap.clear();
                        // animating to zoom to the marker
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));

                        // add marker on map
                        googleMap.addMarker(markerOptions);
                    }
                });


            }
        });

        return view;
    }
}