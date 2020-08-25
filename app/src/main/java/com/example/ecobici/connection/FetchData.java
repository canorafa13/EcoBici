package com.example.ecobici.connection;

import android.os.AsyncTask;
import com.example.ecobici.R;
import com.example.ecobici.classes.CONST;
import com.example.ecobici.classes.EcoBici;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class FetchData extends AsyncTask<Void, Void, String> {
    private GoogleMap mMap;
    public void setmMap(GoogleMap mMap){
        this.mMap = mMap;
    }
    @Override
    protected String doInBackground(Void... voids) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String forecastJsonStr = null;
        try {
            //Generamos la url
            URL url = new URL(CONST.server);
            //Generamos la conexión
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            //Generamos el JSON
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                return null;
            }
            forecastJsonStr = buffer.toString();
            return forecastJsonStr;
        } catch (IOException e) {
            return null;
        } finally{
            //Desconectamos la conexión
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                }
            }
        }
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        try{
            //Parseamos el JSON a la clase EcoBici con Gson
            EcoBici ecoBici = new Gson().fromJson(s, EcoBici.class);
            //Añadimos los Markers al Mapa
            setStationsOnMap(ecoBici);
        }catch (Exception e){
        }
    }

    public void setStationsOnMap(EcoBici ecoBici){
        int sizeStations = ecoBici.getNetwork().getStations().size();
        for(int i = 0; i < sizeStations; i++){
            //Se verifica que exitan bicicletas disponibles
            int free_bikes = ecoBici.getNetwork().getStations().get(i).getFree_bikes();
            LatLng stationsLL = new LatLng(ecoBici.getNetwork().getStations().get(i).getLatitude(), ecoBici.getNetwork().getStations().get(i).getLongitude());
            boolean visible = free_bikes >= 1 ? true : false;
            int icon = free_bikes >= 1 ? R.drawable.available : R.drawable.notavailable;
            //Añadimos el Marker al Mapa
            mMap.addMarker(
                    new MarkerOptions()
                            .position(stationsLL)
                            .icon(BitmapDescriptorFactory.fromResource(icon))
                            .visible(visible)
            ).setTag(ecoBici.getNetwork().getStations().get(i));
        }

    }
}
