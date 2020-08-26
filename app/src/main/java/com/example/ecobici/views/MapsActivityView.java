package com.example.ecobici.views;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.example.ecobici.R;
import com.example.ecobici.classes.CONST;
import com.example.ecobici.classes.Stations;
import com.example.ecobici.controllers.MapsActivityController;
import com.example.ecobici.interfaces.MapsActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MapsActivityView extends FragmentActivity implements OnMapReadyCallback, ActivityCompat.OnRequestPermissionsResultCallback, LocationListener, MapsActivity.View {
    private GoogleMap mMap;
    private LocationManager locManager;
    private Marker current;
    private MapsActivity.Controller controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        //Controlador de la actividad
        controller = new MapsActivityController(this);
        controller.getData();

        //Fragmento del Google Maps
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);




        //Solicitud de Permisos
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        if (!(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)){
            locManager = (LocationManager) getSystemService(getApplicationContext().LOCATION_SERVICE);
            locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,0, this);
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        //Zoom minimo de 13
        mMap.setMinZoomPreference(13);
        //Posición inicial en CDMX
        LatLng cdmx = new LatLng(19.4326077, -99.133208);

        ///Añadir mi posición
        current = mMap.addMarker(
                new MarkerOptions()
                        .position(cdmx)
                        .title("Mi posición")
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_current))
            );
        //Move la cámara a la posición de la CDMX
        mMap.moveCamera(CameraUpdateFactory.newLatLng(cdmx));

        //Escuchar cuando se presiona un Marker
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if(!marker.getId().equals("m0")){
                    //Parseo el Objeto Station que tiene el Marker
                    Stations station = (Stations) marker.getTag();
                    //Se abre la ventana de información
                    openInfoWindow(station);
                }
                return false;
            }
        });
    }



    @Override
    public void onLocationChanged(@NonNull Location location) {
        //Actualizar mi posición, cada vez que cambia
        LatLng updateLocation = new LatLng(location.getLatitude(), location.getLongitude());
        current.setPosition(updateLocation);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {

    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {

    }


    public void openInfoWindow(final Stations station){
        /// Se muestra la pantalla de información
        final AlertDialog.Builder builder = new AlertDialog.Builder(MapsActivityView.this);
        LayoutInflater inflater = MapsActivityView.this.getLayoutInflater();

        View v = inflater.inflate(R.layout.dialog_fragment, null);

        builder.setView(v);

        final AlertDialog dialog = builder.create();
        dialog.show();
        Button openWaze = (Button) v.findViewById(R.id.openWaze);
        TextView  name = (TextView) v.findViewById(R.id.name);
        TextView free_bikes = (TextView) v.findViewById(R.id.free_bikes);
        TextView address = (TextView) v.findViewById(R.id.address);
        TextView empty_slots = (TextView) v.findViewById(R.id.empty_slots);
        TextView timestamp = (TextView) v.findViewById(R.id.timestamp);
        name.setText(station.getName());
        free_bikes.setText(station.getFree_bikes() + "");
        empty_slots.setText(station.getEmpty_slots() + "");
        timestamp.setText(station.getTimestamp().substring(0, 19));
        address.setText(station.getExtra().getAddress() + ", C. P. " + station.getExtra().getZip());
        openWaze.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Se intenta abrir una navegación con Waze
                        String url = CONST.serve_waze + "?q=" + station.getExtra().getAddress() + "&ll=" + station.getLatitude() + "," + station.getLongitude() +"&navigate=yes";
                        controller.openWaze(url);
                    }
                }
        );
    }

    @Override
    public void showMarkers(ArrayList<Stations> stations) {
        for(int i = 0; i < stations.size(); i++){
            //Se verifica que exitan bicicletas disponibles
            int free_bikes = stations.get(i).getFree_bikes();
            LatLng stationsLL = new LatLng(stations.get(i).getLatitude(), stations.get(i).getLongitude());
            boolean visible = free_bikes >= 1 ? true : false;
            int icon = free_bikes >= 1 ? R.drawable.available : R.drawable.notavailable;
            //Añadimos el Marker al Mapa
            mMap.addMarker(
                    new MarkerOptions()
                            .position(stationsLL)
                            .icon(BitmapDescriptorFactory.fromResource(icon))
                            .visible(visible)
            ).setTag(stations.get(i));
        }
    }

    @Override
    public void showError(Exception e) {
        Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void setIntent(Intent intent){
        startActivity(intent);
    }
}