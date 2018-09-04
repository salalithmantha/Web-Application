package com.example.salalith.placessearch;

import java.util.List;


interface DirectionListener {

    void onDirectionFinderStart();
    void onDirectionFinderSuccess(List<RoutePoly> route);
}
