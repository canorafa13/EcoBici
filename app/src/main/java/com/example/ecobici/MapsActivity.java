package com.example.ecobici;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import android.Manifest;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.example.ecobici.connection.FetchData;
import com.example.ecobici.classes.CONST;
import com.example.ecobici.classes.Stations;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, ActivityCompat.OnRequestPermissionsResultCallback, LocationListener {
    private GoogleMap mMap;
    private LocationManager locManager;
    private Marker current;
    private FetchData fetchData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

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

        /// Obtener los datos de EcoBICI
        fetchData = new FetchData();
        fetchData.setmMap(mMap);
        fetchData.execute();
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
        final AlertDialog.Builder builder = new AlertDialog.Builder(MapsActivity.this);
        LayoutInflater inflater = MapsActivity.this.getLayoutInflater();

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
                        try{
                            String url = CONST.serve_waze + "?q=" + station.getExtra().getAddress() + "&ll=" + station.getLatitude() + "," + station.getLongitude() +"&navigate=yes";
                            Intent intent = new Intent( Intent.ACTION_VIEW, Uri.parse( url ) );
                            startActivity( intent );
                        }catch ( ActivityNotFoundException ex  ){
                            Intent intent = new Intent( Intent.ACTION_VIEW, Uri.parse( "market://details?id=com.waze" ) );
                            startActivity(intent);
                        }
                    }
                }
        );
    }

}