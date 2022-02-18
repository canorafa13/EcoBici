package com.ecobici.app.views;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;
import com.ecobici.app.R;
import com.ecobici.app.classes.Stations;
import com.ecobici.app.custom.DialogInfo;
import com.ecobici.app.viewmodels.MapsViewModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivityView extends FragmentActivity implements OnMapReadyCallback, ActivityCompat.OnRequestPermissionsResultCallback, LocationListener, DialogInfo.Action{
    private GoogleMap mMap;
    private LocationManager locManager;
    private Marker current;
    private MapsViewModel mapsViewModel;
    private DialogInfo dialogInfo;

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        mapsViewModel = new MapsViewModel();

        //Fragmento del Google Maps
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        dialogInfo = new DialogInfo(this, this.getLayoutInflater(), this);




        //Solicitud de Permisos
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        if (!(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)){
            locManager = (LocationManager) getSystemService(getApplicationContext().LOCATION_SERVICE);
            locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,0, this);
        }

        mapsViewModel.getStations().observe(this, stations -> {
            for(int i = 0; i < stations.size(); i++){
                //Se verifica que exitan bicicletas disponibles
                int free_bikes = stations.get(i).getFree_bikes();
                LatLng stationsLL = new LatLng(stations.get(i).getLatitude(), stations.get(i).getLongitude());
                //Añadimos el Marker al Mapa
                if(free_bikes >= 1){
                    mMap.addMarker(
                            new MarkerOptions()
                                    .position(stationsLL)
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.available))
                    ).setTag(stations.get(i));
                }
            }
        });

        mapsViewModel.getError().observe(this, error -> Toast.makeText(MapsActivityView.this, "Error: " + error, Toast.LENGTH_LONG).show());

        mapsViewModel.getStations("ecobici");
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
        mMap.setOnMarkerClickListener(marker -> {
            if(!marker.getId().equals("m0")){
                //Parseo el Objeto Station que tiene el Marker

                Stations station = (Stations) marker.getTag();
                //Se abre la ventana de información
                dialogInfo.openInfoWindow(station);
            }
            return false;
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

    @Override
    public void openWaze(@NonNull String... url) {
        try{
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url[0])));
        }catch ( ActivityNotFoundException ex  ){
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url[1])));
        }
    }
}