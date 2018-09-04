package com.example.salalith.placessearch;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Spinner;

import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;


import static com.example.salalith.placessearch.DetailsActivity.ptemp;

public class PhotosTab extends Fragment implements OnMapReadyCallback, DirectionListener {


    private GoogleMap mMap;

    private List<Marker> originMarkers = new ArrayList<>();
    private List<Marker> destinationMarkers = new ArrayList<>();
    private List<Polyline> polylinePaths = new ArrayList<>();
    public static String dest = "";


    public static final String TAG = "AutoCompleteActivity";
    private static final int AUTO_COMP_REQ_CODE = 2;

    protected GeoDataClient geoDataClient;
    private AutoCompleteTextView searchPlace;
    private Spinner mode;
    private varaible var;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.photostab, container, false);
        var = new varaible(this, "");


        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        return rootView;


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        searchPlace = (AutoCompleteTextView) getActivity().findViewById(R.id.search_place1);

        mode = (Spinner) getActivity().findViewById(R.id.spinner3);
        ArrayAdapter<CharSequence> adapterspinner1 = ArrayAdapter.createFromResource(getActivity(),
                R.array.mode_array, android.R.layout.simple_spinner_item);
        adapterspinner1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mode.setAdapter(adapterspinner1);
        mode.setOnItemSelectedListener(spinnermode);


        CustomAutoCompleteAdapter adapter = new CustomAutoCompleteAdapter(getActivity());
        searchPlace.setAdapter(adapter);
        searchPlace.setOnItemClickListener(onItemClickListener);


    }



    private AdapterView.OnItemSelectedListener spinnermode=new AdapterView.OnItemSelectedListener() {

        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {


            Log.d("spin",dest);
            switch (parent.getItemAtPosition(pos).toString()) {
                case "Driving":
                    if(!dest.equals("")){
                        var.setA(dest,"driving");
                    }
                    break;
                case "Bicycling":
                    if(!dest.equals("")) {

                        var.setA(dest, "bicycling");
                    }
                    break;
                case "Transit":
                    if(!dest.equals("")){
                        var.setA(dest,"transit");
                    }
                    break;
                case "Walking":
                    if(!dest.equals("")){
                        var.setA(dest,"walking");
                    }
                    break;

            }


        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };


    private AdapterView.OnItemClickListener onItemClickListener =
            new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


                    searchPlace.setText(((com.example.salalith.placessearch.PlaceAPI) adapterView.getItemAtPosition(i)).getPlaceText());
                    dest = ((com.example.salalith.placessearch.PlaceAPI) adapterView.getItemAtPosition(i)).getPlaceText();
                    var.setA(((com.example.salalith.placessearch.PlaceAPI) adapterView.getItemAtPosition(i)).getPlaceText(),"walking");


                }
            };


    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        LatLng loc = ptemp.getLatLng();
        mMap.addMarker(new MarkerOptions().position(loc)
                .title(""));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(loc));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 14));


    }


    public static PhotosTab newInstance() {
        PhotosTab fragment = new PhotosTab();
        return fragment;
    }


    //add


    public void sendRequest(String dest,String mode) {
        String origin = "place_id:" + ptemp.getId();
        String destination = dest;


        try {
            new DirectionLine(this, origin, destination,mode).execute();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onDirectionFinderStart() {


        if (originMarkers != null) {
            for (Marker marker : originMarkers) {
                marker.remove();
            }
        }

        if (destinationMarkers != null) {
            for (Marker marker : destinationMarkers) {
                marker.remove();
            }
        }

        if (polylinePaths != null) {
            for (Polyline polyline : polylinePaths) {
                polyline.remove();
            }
        }
    }


    @Override
    public void onDirectionFinderSuccess(List<RoutePoly> routes) {
        polylinePaths = new ArrayList<>();
        originMarkers = new ArrayList<>();
        destinationMarkers = new ArrayList<>();

        for (RoutePoly route : routes) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(route.startLocation, 13));

            destinationMarkers.add(mMap.addMarker(new MarkerOptions().position(route.endLocation)
                    .title("")));

                    PolylineOptions polylineOptions = new PolylineOptions().
                    geodesic(true).
                    color(Color.BLUE).
                    width(10);

            for (int i = 0; i < route.points.size(); i++)
                polylineOptions.add(route.points.get(i));

            polylinePaths.add(mMap.addPolyline(polylineOptions));
        }
    }


}