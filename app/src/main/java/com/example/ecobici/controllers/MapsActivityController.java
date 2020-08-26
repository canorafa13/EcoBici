package com.example.ecobici.controllers;

import android.content.Intent;

import com.example.ecobici.classes.Stations;
import com.example.ecobici.interfaces.MapsActivity;
import com.example.ecobici.models.MapsActivityModel;

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
