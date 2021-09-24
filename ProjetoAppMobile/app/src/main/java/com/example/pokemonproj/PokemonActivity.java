package com.example.pokemonproj;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.example.pokemonproj.R.color.black;
import static com.example.pokemonproj.R.color.blue_grey_300;
import static com.example.pokemonproj.R.color.brown_900;
import static com.example.pokemonproj.R.color.bug;
import static com.example.pokemonproj.R.color.dark;
import static com.example.pokemonproj.R.color.deep_orange_A400;
import static com.example.pokemonproj.R.color.dragon;
import static com.example.pokemonproj.R.color.electric;
import static com.example.pokemonproj.R.color.fairy;
import static com.example.pokemonproj.R.color.fighting;
import static com.example.pokemonproj.R.color.fire;
import static com.example.pokemonproj.R.color.flying;
import static com.example.pokemonproj.R.color.ghost;
import static com.example.pokemonproj.R.color.grass;
import static com.example.pokemonproj.R.color.grey_100;
import static com.example.pokemonproj.R.color.grey_400;
import static com.example.pokemonproj.R.color.grey_900;
import static com.example.pokemonproj.R.color.ground;
import static com.example.pokemonproj.R.color.ice;
import static com.example.pokemonproj.R.color.indigo_300;
import static com.example.pokemonproj.R.color.normal;
import static com.example.pokemonproj.R.color.pink_50;
import static com.example.pokemonproj.R.color.poison;
import static com.example.pokemonproj.R.color.psychic;
import static com.example.pokemonproj.R.color.purple_700;
import static com.example.pokemonproj.R.color.purple_A100;
import static com.example.pokemonproj.R.color.red_600;
import static com.example.pokemonproj.R.color.rock;
import static com.example.pokemonproj.R.color.steel;
import static com.example.pokemonproj.R.color.water;
import static com.example.pokemonproj.R.color.yellow_800;

public class PokemonActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String> {
    public final static String EXTRA_NOME_POKE = "com.example.intent.nomePoke";
    public final static String EXTRA_ID_POKE = "com.example.intent.idPoke";
    public final static String EXTRA_HEIGHT_POKE = "com.example.intent.height";
    public final static String EXTRA_WEIGHT_POKE = "com.example.intent.weight";
    public final static String EXTRA_SPRITE_POKE = "com.example.intent.sprite";
    public final static String EXTRA_TYPE1_POKE = "com.example.intent.type1";
    public final static String EXTRA_TYPE2_POKE = "com.example.intent.type2";
    private Handler handler = new Handler();

    public TextView pokeT1;
    public TextView pokeT2;

    public String nomePoke;
    public String idPoke;
    public String spritePoke;
    public String tipo1Poke;
    public String tipo2Poke;

    public String pokeName;
    public String pokeId;
    public String pokeHeight;
    public String pokeWeight;

    public int iPoke;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pokemon);
        Bundle extras = getIntent().getExtras();

        iPoke = 0;

        TextView pokeN = findViewById(R.id.txtPoke1);
        TextView pokeI = findViewById(R.id.txtPoke2);
        TextView pokeH = findViewById(R.id.txtHeight);
        TextView pokeW = findViewById(R.id.txtWeight);
        pokeT1 = findViewById(R.id.txtType1);
        pokeT2 = findViewById(R.id.txtType2);

        nomePoke = extras.getString(MainActivity.EXTRA_NOME_POKE);
        idPoke = extras.getString(MainActivity.EXTRA_ID_POKE);
        String alturaPoke = extras.getString(MainActivity.EXTRA_HEIGHT_POKE);
        String pesoPoke = extras.getString(MainActivity.EXTRA_WEIGHT_POKE);
        tipo1Poke = extras.getString(MainActivity.EXTRA_TYPE1_POKE);
        tipo2Poke = extras.getString(MainActivity.EXTRA_TYPE2_POKE);
        spritePoke = extras.getString(MainActivity.EXTRA_SPRITE_POKE);

        switch (tipo1Poke){
            case "normal": pokeT1.setBackgroundResource(normal); break;
            case "fighting": pokeT1.setBackgroundResource(fighting); break;
            case "flying": pokeT1.setBackgroundResource(flying); break;
            case "poison": pokeT1.setBackgroundResource(poison); break;
            case "ground": pokeT1.setBackgroundResource(ground); break;
            case "rock": pokeT1.setBackgroundResource(rock); break;
            case "bug": pokeT1.setBackgroundResource(bug); break;
            case "ghost": pokeT1.setBackgroundResource(ghost); break;
            case "steel": pokeT1.setBackgroundResource(steel); break;
            case "fire": pokeT1.setBackgroundResource(fire); break;
            case "water": pokeT1.setBackgroundResource(water); break;
            case "grass": pokeT1.setBackgroundResource(grass); break;
            case "electric": pokeT1.setBackgroundResource(electric); break;
            case "psychic": pokeT1.setBackgroundResource(psychic); break;
            case "ice": pokeT1.setBackgroundResource(ice); break;
            case "dragon": pokeT1.setBackgroundResource(dragon); break;
            case "dark": pokeT1.setBackgroundResource(dark); break;
            case "fairy": pokeT1.setBackgroundResource(fairy); break;
            case "unknown": pokeT1.setBackgroundResource(grey_100);pokeT1.setTextColor(black); break;
            case "shadow": pokeT1.setBackgroundResource(grey_900); break;
            default: finish(); break;
        }

        try{
            switch (tipo2Poke){
                case "normal": pokeT2.setBackgroundResource(normal); break;
                case "fighting": pokeT2.setBackgroundResource(fighting); break;
                case "flying": pokeT2.setBackgroundResource(flying); break;
                case "poison": pokeT2.setBackgroundResource(poison); break;
                case "ground": pokeT2.setBackgroundResource(ground); break;
                case "rock": pokeT2.setBackgroundResource(rock); break;
                case "bug": pokeT2.setBackgroundResource(bug); break;
                case "ghost": pokeT2.setBackgroundResource(ghost); break;
                case "steel": pokeT2.setBackgroundResource(steel); break;
                case "fire": pokeT2.setBackgroundResource(fire); break;
                case "water": pokeT2.setBackgroundResource(water); break;
                case "grass": pokeT2.setBackgroundResource(grass); break;
                case "electric": pokeT2.setBackgroundResource(electric); break;
                case "psychic": pokeT2.setBackgroundResource(psychic); break;
                case "ice": pokeT2.setBackgroundResource(ice); break;
                case "dragon": pokeT2.setBackgroundResource(dragon); break;
                case "dark": pokeT2.setBackgroundResource(dark); break;
                case "fairy": pokeT2.setBackgroundResource(fairy); break;
                case "unknown": pokeT2.setBackgroundResource(grey_100);pokeT2.setTextColor(black); break;
                case "shadow": pokeT2.setBackgroundResource(grey_900); break;
                default: finish(); break;
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        if (tipo2Poke == null)
            pokeT2.setVisibility(View.GONE);
        else{
            pokeT2.setVisibility(View.VISIBLE);
        }


        pokeN.setText(nomePoke);
        pokeI.setText("#"+idPoke);
        pokeH.setText("Altura: " + alturaPoke + "m");
        pokeW.setText("Peso: " + pesoPoke + "kg");
        pokeT1.setText(tipo1Poke);
        pokeT2.setText(tipo2Poke);
        load();
    }

    private void load() {

        new Thread() {
            public void run() {
                Bitmap img = null;

                try {
                    URL url = new URL(spritePoke);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    InputStream input = connection.getInputStream();
                    img = BitmapFactory.decodeStream(input);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                final Bitmap imgAux = img;
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        ImageView pokeS = findViewById(R.id.imgPokeSprite);
                        pokeS.setImageBitmap(imgAux);
                    }
                });
            }
        }.start();
    }

    public void backActivity(View view){
        finish();
    }

    public void moreInfo(View view){
        String url = "https://pokemondb.net/pokedex/" + nomePoke.toLowerCase();
        if (Integer.parseInt(idPoke) == 29)
            url = url + "-f";
        if (Integer.parseInt(idPoke) == 32)
            url = url + "-m";
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }

    public void pokeAnterior(View view){
        int pokeBusca = Integer.parseInt(idPoke)-1;
        String queryString = Integer.toString(pokeBusca);
        InputMethodManager inputManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputManager != null) {
            inputManager.hideSoftInputFromWindow(view.getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }

        ConnectivityManager connectivityManager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;
        if (connectivityManager != null) {
            networkInfo = connectivityManager.getActiveNetworkInfo();
        }
        if (networkInfo != null && networkInfo.isConnected() && queryString.length() != 0) {
            Bundle queryBundle = new Bundle();
            queryBundle.putString("queryString", queryString);
            getSupportLoaderManager().restartLoader(0, queryBundle, this);
            Toast.makeText(this, R.string.loading, Toast.LENGTH_SHORT).show();
        } else {
            if (queryString.length() == 0) {
                Toast.makeText(this, R.string.no_search_term, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, R.string.no_network, Toast.LENGTH_LONG).show();
            }
        }
    }

    public void pokeProximo(View view){
        int pokeBusca = Integer.parseInt(idPoke)+1;
        String queryString = Integer.toString(pokeBusca);
        InputMethodManager inputManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputManager != null) {
            inputManager.hideSoftInputFromWindow(view.getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }

        ConnectivityManager connectivityManager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;
        if (connectivityManager != null) {
            networkInfo = connectivityManager.getActiveNetworkInfo();
        }
        if (networkInfo != null && networkInfo.isConnected() && queryString.length() != 0) {
            Bundle queryBundle = new Bundle();
            queryBundle.putString("queryString", queryString);
            getSupportLoaderManager().restartLoader(0, queryBundle, this);
            Toast.makeText(this, R.string.loading, Toast.LENGTH_SHORT).show();
        } else {
            if (queryString.length() == 0) {
                Toast.makeText(this, R.string.no_search_term, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, R.string.no_network, Toast.LENGTH_LONG).show();
            }
        }
    }

    @NonNull
    @Override
    public Loader<String> onCreateLoader(int id, @Nullable Bundle args) {
        String queryString = "";
        if (args != null) {
            queryString = args.getString("queryString");
        }
        return new CarregaPokemon(this, queryString);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<String> loader, String data) {
        try {
            JSONArray jsonArray = new JSONArray(data);
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            String id = jsonObject.getString("idPoke");
            String name = jsonObject.getString("nomePoke");
            String front_default = jsonObject.getString("sprite");
            String tipo1 = jsonObject.getString("tipo1");
            String tipo2 = jsonObject.getString("tipo2");
            String height = jsonObject.getString("altura");
            String weight = jsonObject.getString("peso");

            if(tipo2.isEmpty() || tipo2.trim().isEmpty())
                tipo2 = null;

            pokeName = name.substring(0, 1).toUpperCase() + name.substring(1);
            try{
                int index = pokeName.indexOf("-");
                pokeName = pokeName.substring(0, index);
            }
            catch (Exception e){
                e.printStackTrace();
            }
            pokeId = id;
            double douH = Double.parseDouble(height) / 10;
            pokeHeight = Double.toString(douH);
            double douW = Double.parseDouble(weight) / 10;
            pokeWeight = Double.toString(douW);

            if (name != null && id != null && iPoke == 0) {
                iPoke = 1;
                Intent intent = new Intent(PokemonActivity.this, PokemonActivity.class);
                intent.putExtra(EXTRA_NOME_POKE, pokeName);
                intent.putExtra(EXTRA_ID_POKE, pokeId);
                intent.putExtra(EXTRA_TYPE1_POKE, tipo1);
                intent.putExtra(EXTRA_TYPE2_POKE, tipo2);
                intent.putExtra(EXTRA_HEIGHT_POKE, pokeHeight);
                intent.putExtra(EXTRA_WEIGHT_POKE, pokeWeight);
                intent.putExtra(EXTRA_SPRITE_POKE, front_default);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                        overridePendingTransition(0, 0);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                    }
                }, 1500);
            } else {
                if (iPoke == 0) {
                    Toast.makeText(this, R.string.no_results, Toast.LENGTH_SHORT).show();
                    getSupportLoaderManager().destroyLoader(0);
                }
            }
        } catch (Exception e) {
            Toast.makeText(this, R.string.no_results, Toast.LENGTH_SHORT).show();
            getSupportLoaderManager().destroyLoader(0);
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) {

    }
}