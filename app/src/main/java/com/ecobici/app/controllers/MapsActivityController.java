package com.ecobici.app.controllers;

import android.content.Intent;

import com.ecobici.app.classes.Stations;
import com.ecobici.app.interfaces.MapsActivity;
import com.ecobici.app.models.MapsActivityModel;

import java.util.ArrayList;

public class MapsActivityController implements MapsActivity.Controller {

    private MapsActivity.View view;
    private MapsActivity.Model model;

    public MapsActivityController(MapsActivity.View view) {
        this.view = view;
        this.model = new MapsActivityModel(this);
    }

    @Override
    public void getData() {
        if(view != null){
            model.getData();
        }
    }

    @Override
    public void showMarkers(ArrayList<Stations> stations) {
        if(view != null){
            view.showMarkers(stations);
        }
    }

    @Override
    public void showError(Exception e) {
        if(view != null){
            view.showError(e);
        }
    }

    @Override
    public void openWaze(String url) {
        if(view != null){
            model.openWaze(url);
        }
    }

    @Override
    public void setIntent(Intent intent) {
        if(view != null){
            view.setIntent(intent);
        }
    }
}
