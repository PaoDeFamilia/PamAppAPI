package com.example.pokemonproj;

import android.net.Uri;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class PokeURL {
    private static final String LOG_TAG = PokeURL.class.getSimpleName();
    private static final String POKE_URL = "http://192.168.1.12:60110/api/Pokemon/";
    static String buscaPoke(String queryString) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String pokeJSONString = null;
        try {
            Uri builtURI = Uri.parse(POKE_URL).buildUpon()
                    .appendPath(queryString)
                    .build();
            URL requestURL = new URL(builtURI.toString());
            urlConnection = (HttpURLConnection) requestURL.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            InputStream inputStream = urlConnection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder builder = new StringBuilder();
            String linha;
            while ((linha = reader.readLine()) != null) {
                builder.append(linha);
            }
            if (builder.length() == 0) {
                return null;
            }
            pokeJSONString = builder.toString();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        try {
            Log.d(LOG_TAG, pokeJSONString);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return pokeJSONString;
    }
}
