package com.example.salalith.placessearch;

import android.view.View;
import android.widget.AdapterView;

import static com.example.salalith.placessearch.ReviewsTab.Highest_rating;
import static com.example.salalith.placessearch.ReviewsTab.Least_rating;
import static com.example.salalith.placessearch.ReviewsTab.Most_recent;
import static com.example.salalith.placessearch.ReviewsTab.least_recent;
import static com.example.salalith.placessearch.ReviewsTab.prepareMovieData;
import static com.example.salalith.placessearch.ReviewsTab.s12;

class SpinnerSortReviews implements android.widget.AdapterView.OnItemSelectedListener {


    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {


        switch (parent.getItemAtPosition(pos).toString()) {
            case "Default order":
                ReviewsTab.prepareMovieData(s12);

                break;
            case "Highest Rating":
                Highest_rating();
                break;
            case "Lowest Rating":
                Least_rating();
                break;
            case "Most Recent":
                Most_recent();
                break;
            case "Least Recent":
                least_recent();
                break;


        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }
}
