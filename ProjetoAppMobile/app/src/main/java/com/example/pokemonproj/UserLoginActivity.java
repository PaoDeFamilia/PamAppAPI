package com.example.pokemonproj;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

public class UserLoginActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String> {
    public final static String EXTRA_ID_USER = "com.example.intent.idUser";
    public final static String EXTRA_NOME_USER = "com.example.intent.nomeUser";
    public final static String EXTRA_USERNAME = "com.example.intent.username";
    public final static String EXTRA_SENHA = "com.example.intent.senha";
    public final static String EXTRA_EMAIL = "com.example.intent.email";

    EditText mEmail;
    EditText mPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);
        mEmail = (EditText) findViewById(R.id.etEmail);
        mPassword = (EditText) findViewById(R.id.etPassword);

        findViewById(R.id.btnNConta).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserLoginActivity.this, UserCadActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent(UserLoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void buscaUser(View view) {
        String queryString = mEmail.getText().toString();
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
        if (networkInfo != null && networkInfo.isConnected() && !(queryString.trim().isEmpty()) && !(mPassword.getText().toString() == null || mPassword.getText().toString().trim().isEmpty())) {
            Bundle queryBundle = new Bundle();
            queryBundle.putString("queryString", queryString);
            getSupportLoaderManager().restartLoader(0, queryBundle, this);
            Toast.makeText(this, R.string.loading, Toast.LENGTH_SHORT).show();
        } else {
            if (queryString.trim().isEmpty())
                Toast.makeText(this, "Email inválido", Toast.LENGTH_LONG).show();
            else {
                if (mPassword.getText().toString() == null || mPassword.getText().toString().trim().isEmpty())
                    Toast.makeText(this, "Senha inválida", Toast.LENGTH_LONG).show();
                else
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
        return new CarregaUser(this, queryString);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<String> loader, String data) {
        try {
            JSONArray jsonArray = new JSONArray(data);
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            String id = jsonObject.getString("idUsuario");
            String nome = jsonObject.getString("nomeUsuario");
            String username = jsonObject.getString("username");
            String senha = jsonObject.getString("senha");
            String email = jsonObject.getString("email");
            if (email.equals(mEmail.getText().toString()) && senha.equals(mPassword.getText().toString())) {
                Intent intent = new Intent(UserLoginActivity.this, MainActivity.class);
                intent.putExtra(EXTRA_ID_USER, id);
                intent.putExtra(EXTRA_NOME_USER, nome);
                intent.putExtra(EXTRA_USERNAME, username);
                intent.putExtra(EXTRA_SENHA, senha);
                intent.putExtra(EXTRA_EMAIL, email);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(intent);
                        finish();
                    }
                }, 1500);
                getSupportLoaderManager().destroyLoader(0);
            }
            else {
                if (id == null || id.isEmpty() || id.trim().isEmpty()) {
                    Toast.makeText(this, R.string.no_results, Toast.LENGTH_SHORT).show();
                    getSupportLoaderManager().destroyLoader(0);
                }
                else {
                    if (!senha.equals(mPassword.getText().toString())) {
                        Toast.makeText(this, "Senha incorreta", Toast.LENGTH_SHORT).show();
                        getSupportLoaderManager().destroyLoader(0);
                    }
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