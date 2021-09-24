package com.example.pokemonproj;

import android.net.Uri;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class UserGetURL {
    private static final String LOG_TAG = UserGetURL.class.getSimpleName();
    private static final String USER_URL = "http://192.168.1.12:60110/api/Usuario/";
    static String buscaUser(String queryString) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String userJSONString = null;
        try {
            Uri builtURI = Uri.parse(USER_URL).buildUpon()
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
            userJSONString = builder.toString();
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
            Log.d(LOG_TAG, userJSONString);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return userJSONString;
    }
}
