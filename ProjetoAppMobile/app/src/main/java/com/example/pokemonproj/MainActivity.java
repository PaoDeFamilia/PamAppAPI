package com.example.pokemonproj;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import org.json.JSONArray;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String> {
    public final static String EXTRA_NOME_POKE = "com.example.intent.nomePoke";
    public final static String EXTRA_ID_POKE = "com.example.intent.idPoke";
    public final static String EXTRA_HEIGHT_POKE = "com.example.intent.height";
    public final static String EXTRA_WEIGHT_POKE = "com.example.intent.weight";
    public final static String EXTRA_SPRITE_POKE = "com.example.intent.sprite";
    public final static String EXTRA_TYPE1_POKE = "com.example.intent.type1";
    public final static String EXTRA_TYPE2_POKE = "com.example.intent.type2";

    public final static String EXTRA_ID_USER = "com.example.intent.idUser";
    public final static String EXTRA_NOME_USER = "com.example.intent.nomeUser";
    public final static String EXTRA_USERNAME = "com.example.intent.username";
    public final static String EXTRA_SENHA = "com.example.intent.senha";
    public final static String EXTRA_EMAIL = "com.example.intent.email";

    private EditText pokeBusca;

    public String pokeName;
    public String pokeId;
    public String pokeHeight;
    public String pokeWeight;

    public String userId = null;
    public String userNome;
    public String userUsername;
    public String userSenha;
    public String userEmail;
    public String userFirstName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Bundle extras = getIntent().getExtras();

        try {
            userId = extras.getString(UserLoginActivity.EXTRA_ID_USER);
            userNome = extras.getString(UserLoginActivity.EXTRA_NOME_USER);
            userUsername = extras.getString(UserLoginActivity.EXTRA_USERNAME);
            userSenha = extras.getString(UserLoginActivity.EXTRA_SENHA);
            userEmail = extras.getString(UserLoginActivity.EXTRA_EMAIL);
        }catch (Exception e){
            e.printStackTrace();
        }

        if (!(userNome == null || userNome.isEmpty() || userNome.trim().isEmpty()))
            userFirstName = userNome.substring(0, 1).toUpperCase() + userNome.substring(1);
        try{
            int index = userFirstName.indexOf(" ");
            userFirstName = userFirstName.substring(0, index);
        }
        catch (Exception e){
            e.printStackTrace();
        }

        ImageView imgUser = findViewById(R.id.imgUser);
        imgUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
                if (userId == null || userId.isEmpty() || userId.trim().isEmpty()){
                    View mView = getLayoutInflater().inflate(R.layout.dialog_login, null);
                    Button mLogin = (Button) mView.findViewById(R.id.btnLogin);
                    Button mNConta = (Button) mView.findViewById(R.id.btnNConta);

                    mBuilder.setView(mView);
                    final AlertDialog dialog = mBuilder.create();
                    dialog.show();
                    mLogin.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(MainActivity.this, UserLoginActivity.class);
                            startActivity(intent);
                            dialog.dismiss();
                            finish();
                        }
                    });mNConta.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(MainActivity.this, UserCadActivity.class);
                            startActivity(intent);
                            dialog.dismiss();
                            finish();
                        }
                    });
                } else {
                    View mView = getLayoutInflater().inflate(R.layout.dialog_logout, null);
                    TextView mTxtNome = (TextView) mView.findViewById(R.id.txtNome);
                    TextView mTxtUsername = (TextView) mView.findViewById(R.id.txtUsername);
                    TextView mTxtId = (TextView) mView.findViewById(R.id.txtId);
                    TextView mTxtEmail = (TextView) mView.findViewById(R.id.txtEmailUser);
                    Button mLogout = (Button) mView.findViewById(R.id.btnLogout);

                    mBuilder.setView(mView);
                    final AlertDialog dialog = mBuilder.create();
                    mTxtNome.setText("Olá, "+userFirstName+"!");
                    mTxtUsername.setText("Nome de usuário: "+userUsername);
                    mTxtId.setText("id: #"+userId);
                    mTxtEmail.setText("Email: "+userEmail);
                    dialog.show();
                    mLogout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(MainActivity.this, MainActivity.class);
                            intent.putExtra(EXTRA_ID_USER, "");
                            intent.putExtra(EXTRA_NOME_USER, "");
                            intent.putExtra(EXTRA_USERNAME, "");
                            intent.putExtra(EXTRA_SENHA, "");
                            intent.putExtra(EXTRA_EMAIL, "");
                            overridePendingTransition(0, 0);
                            startActivity(intent);
                            overridePendingTransition(0, 0);
                            dialog.dismiss();
                            finish();
                        }
                    });
                }
            }
        });

        pokeBusca = findViewById(R.id.pokeInput);

        if (getSupportLoaderManager().getLoader(0) != null) {
            getSupportLoaderManager().initLoader(0, null, this);
        }
    }

    public void buscaPoke(View view) {
        String queryString = pokeBusca.getText().toString().toLowerCase();
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
        if (networkInfo != null && networkInfo.isConnected() && !(queryString.trim().isEmpty())) {
            Bundle queryBundle = new Bundle();
            queryBundle.putString("queryString", queryString);
            getSupportLoaderManager().restartLoader(0, queryBundle, this);
            Toast.makeText(this, R.string.loading, Toast.LENGTH_SHORT).show();
        } else {
            if (queryString.trim().isEmpty()) {
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

            if (name != null && id != null) {
                Intent intent = new Intent(MainActivity.this, PokemonActivity.class);
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
                        startActivity(intent);
                    }
                }, 1500);
                getSupportLoaderManager().destroyLoader(0);
            } else {
                Toast.makeText(this, R.string.no_results, Toast.LENGTH_SHORT).show();
                getSupportLoaderManager().destroyLoader(0);
            }
        } catch (Exception e) {
                Toast.makeText(this, R.string.no_results, Toast.LENGTH_SHORT).show();
                getSupportLoaderManager().destroyLoader(0);
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) {

    }

    public void openLocalizacao(View view){
        Intent intent = new Intent(MainActivity.this, LocalizacaoActivity.class);
        startActivity(intent);
    }
}