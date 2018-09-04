package com.example.salalith.placessearch;

import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import static com.example.salalith.placessearch.PhotosTab.dest;

class SpinnerMode implements android.widget.AdapterView.OnItemSelectedListener {


    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {


        switch (parent.getItemAtPosition(pos).toString()) {
            case "Driving":
                if(!dest.equals("")){

                }
                break;
            case "Bicycling":
                break;
            case "Transit":
                break;
            case "Walking":
                break;

        }


    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }
}

