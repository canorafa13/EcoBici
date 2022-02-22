package com.ecobici.app.views

import android.Manifest
import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.OnRequestPermissionsResultCallback
import androidx.fragment.app.FragmentActivity
import com.ecobici.app.R
import com.ecobici.app.classes.Stations
import com.ecobici.app.custom.DialogInfo
import com.ecobici.app.viewmodels.MapsViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivityView: FragmentActivity(), OnMapReadyCallback, OnRequestPermissionsResultCallback,
    LocationListener, DialogInfo.Action {

    private var mMap: GoogleMap? = null
    private var locManager: LocationManager? = null
    private var current: Marker? = null
    private var mapsViewModel: MapsViewModel? = null
    private var dialogInfo: DialogInfo? = null

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        mapsViewModel = MapsViewModel()

        //Fragmento del Google Maps
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)
        dialogInfo = DialogInfo(this, this.layoutInflater, this)


        //Solicitud de Permisos
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            1
        )
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            locManager = getSystemService(LOCATION_SERVICE) as LocationManager
            locManager!!.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0f, this)
        }
        mapsViewModel!!.stations.observe(
            this
        ) { stations: List<Stations> ->
            for (i in stations.indices) {
                //Se verifica que exitan bicicletas disponibles
                val free_bikes = stations[i].free_bikes
                val stationsLL =
                    LatLng(stations[i].latitude, stations[i].longitude)
                //Añadimos el Marker al Mapa
                if (free_bikes >= 1) {
                    mMap!!.addMarker(
                        MarkerOptions()
                            .position(stationsLL)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.available))
                    ).tag = stations[i]
                }
            }
        }
        mapsViewModel!!.error.observe(
            this
        ) { error: String ->
            Toast.makeText(
                this@MapsActivityView,
                "Error: $error",
                Toast.LENGTH_LONG
            ).show()
        }
        mapsViewModel!!.getStations("ecobici")
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        mMap = googleMap
        //Zoom minimo de 13
        //Zoom minimo de 13
        mMap!!.setMinZoomPreference(13f)
        //Posición inicial en CDMX
        //Posición inicial en CDMX
        val cdmx = LatLng(19.4326077, -99.133208)

        ///Añadir mi posición

        ///Añadir mi posición
        current = mMap!!.addMarker(
            MarkerOptions()
                .position(cdmx)
                .title("Mi posición")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_current))
        )
        //Move la cámara a la posición de la CDMX
        //Move la cámara a la posición de la CDMX
        mMap!!.moveCamera(CameraUpdateFactory.newLatLng(cdmx))

        //Escuchar cuando se presiona un Marker

        //Escuchar cuando se presiona un Marker
        mMap!!.setOnMarkerClickListener { marker: Marker ->
            if (marker.id != "m0") {
                //Parseo el Objeto Station que tiene el Marker
                val station = marker.tag as Stations?
                //Se abre la ventana de información
                dialogInfo!!.openInfoWindow(station!!)
            }
            false
        }
    }

    override fun onLocationChanged(location: Location) {

        //Actualizar mi posición, cada vez que cambia
        val updateLocation = LatLng(location.getLatitude(), location.longitude)
        current!!.position = updateLocation
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}

    override fun onProviderEnabled(provider: String) {}

    override fun onProviderDisabled(provider: String) {}

    override fun openWaze(vararg url: String) {
        try {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url[0])))
        } catch (ex: ActivityNotFoundException) {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url[1])))
        }
    }
}