package com.example.ecobici.interfaces;

import android.content.Intent;

import com.example.ecobici.classes.Stations;

import java.util.ArrayList;

public interface MapsActivity {
    interface View{
        void showMarkers(ArrayList<Stations> stations);
        void showError(Exception e);
        void setIntent(Intent intent);
    }

    interface Controller{
        void getData();
        void showMarkers(ArrayList<Stations> stations);
        void showError(Exception e);
        void openWaze(String url);
        void setIntent(Intent intent);
    }

    interface Model{
        void getData();
        void openWaze(String url);
    }
}
