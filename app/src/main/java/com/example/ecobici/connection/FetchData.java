package com.example.ecobici.connection;

import android.os.AsyncTask;
import com.example.ecobici.classes.CONST;
import com.example.ecobici.classes.EcoBici;
import com.example.ecobici.classes.Stations;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public abstract class FetchData extends AsyncTask<Void, Void, String> {
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
            this.showStations(ecoBici.getNetwork().getStations());
        }catch (Exception e){
            this.showError(e);
        }
    }

    public abstract void showStations(ArrayList<Stations> stations);

    public abstract void showError(Exception e);
}
