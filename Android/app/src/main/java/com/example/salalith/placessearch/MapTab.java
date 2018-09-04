package com.example.salalith.placessearch;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.PlacePhotoMetadata;
import com.google.android.gms.location.places.PlacePhotoMetadataBuffer;
import com.google.android.gms.location.places.PlacePhotoMetadataResponse;
import com.google.android.gms.location.places.PlacePhotoResponse;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.List;

public class MapTab extends Fragment {

    public static GeoDataClient mGeoDataClient;
    private ImageView img1;
    public static ArrayList<PlacePhotoMetadata> photosDataList=new ArrayList<>();

    public static List<ImageData> movieList = new ArrayList<>();
    private RecyclerView recyclerView;
    public static ImageAdapter mAdapter;
    private TextView tv1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.maptab, container, false);
        return rootView;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //mGeoDataClient = Places.getGeoDataClient(getActivity(), null);
        //getPhotos();



        recyclerView = (RecyclerView) getActivity().findViewById(R.id.recycler_view1);
        tv1=(TextView)getActivity().findViewById(R.id.tv1);
        tv1.setVisibility(TextView.INVISIBLE);

        mAdapter = new ImageAdapter(movieList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

        if(photosDataList.isEmpty()){
            tv1.setVisibility(TextView.VISIBLE);
            tv1.setText("No Photos");
            Toast.makeText(getActivity(),"No Photos Found",Toast.LENGTH_LONG).show();



        }







    }

    public static void getPhotos( String placeId) {
        final Task<PlacePhotoMetadataResponse> photoMetadataResponse = mGeoDataClient.getPlacePhotos(placeId);
        photoMetadataResponse.addOnCompleteListener(new OnCompleteListener<PlacePhotoMetadataResponse>() {
            @Override
            public void onComplete(@NonNull Task<PlacePhotoMetadataResponse> task) {
                // Get the list of photos.
                PlacePhotoMetadataResponse photos = task.getResult();
                // Get the PlacePhotoMetadataBuffer (metadata for all of the photos).
                PlacePhotoMetadataBuffer photoMetadataBuffer = photos.getPhotoMetadata();
                // Get the first photo in the list.


                for(PlacePhotoMetadata photoMetadata : photoMetadataBuffer){
                    photosDataList.add(photoMetadata);
                }

                for(PlacePhotoMetadata m:photosDataList)
                    getPhoto(m);
//                mAdapter.notifyDataSetChanged();




            }
        });
    }


    public static void getPhoto(PlacePhotoMetadata photoMetadata){
        Task<PlacePhotoResponse> photoResponse = mGeoDataClient.getPhoto(photoMetadata);
        photoResponse.addOnCompleteListener(new OnCompleteListener<PlacePhotoResponse>() {
            @Override
            public void onComplete(@NonNull Task<PlacePhotoResponse> task) {
                PlacePhotoResponse photo = task.getResult();
                Bitmap photoBitmap = photo.getBitmap();
                ImageData imgr=new ImageData(photoBitmap);
                movieList.add(imgr);
            }
        });
    }





}