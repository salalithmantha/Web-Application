package com.example.salalith.placessearch;

import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

class SpinnerCustomOnItemSelectedListenerSearchTab implements android.widget.AdapterView.OnItemSelectedListener {


    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

        SearchTab.index=parent.getItemAtPosition(pos).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }
}

