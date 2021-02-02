package com.ecobici.app.models;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;

import com.ecobici.app.classes.Stations;
import com.ecobici.app.connection.FetchData;
import com.ecobici.app.interfaces.MapsActivity;

import java.util.ArrayList;

public class MapsActivityModel implements MapsActivity.Model {
    private MapsActivity.Controller controller;

    public MapsActivityModel(MapsActivity.Controller controller) {
        this.controller = controller;
    }

    @Override
    public void getData() {
        FetchData d = new FetchData() {
            @Override
            public void showStations(ArrayList<Stations> stations) {
                controller.showMarkers(stations);
            }

            @Override
            public void showError(Exception e) {
                controller.showError(e);
            }
        };
        d.execute();
    }

    @Override
    public void openWaze(String url) {
        Intent intent;
        try{
            intent = new Intent( Intent.ACTION_VIEW, Uri.parse( url ) );
        }catch ( ActivityNotFoundException ex  ){
            intent = new Intent( Intent.ACTION_VIEW, Uri.parse( "market://details?id=com.waze" ) );
        }
        controller.setIntent(intent);
    }


}
