package com.example.salalith.placessearch;

import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import static com.example.salalith.placessearch.ReviewsTab.prepareMovieData;

class SpinnerClickReviews implements android.widget.AdapterView.OnItemSelectedListener {


    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

        ReviewsTab.prepareMovieData(parent.getItemAtPosition(pos).toString().replace(" ",""));

    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }
}
