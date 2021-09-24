package com.example.pokemonproj;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class UserCadActivity extends AppCompatActivity {

    EditText mNome, mUser, mSenha, mConfSenha, mEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_cad);

        mNome = (EditText) findViewById(R.id.eTxtNomeUser);
        mUser = (EditText) findViewById(R.id.eTxtUserUser);
        mSenha = (EditText) findViewById(R.id.eTxtSenhaUser);
        mConfSenha = (EditText) findViewById(R.id.eTxtConfSenha);
        mEmail = (EditText) findViewById(R.id.eTxtEmailUser);
    }

    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent(UserCadActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void fazerLogin(View view){
        Intent intent = new Intent(UserCadActivity.this, UserLoginActivity.class);
        startActivity(intent);
        finish();
    }

    public void cadastraUser(View view){
        if (mNome.getText().toString().trim().isEmpty() || mUser.getText().toString().trim().isEmpty() || mSenha.getText().toString().trim().isEmpty() || mConfSenha.getText().toString().trim().isEmpty() || mEmail.getText().toString().trim().isEmpty()){
            Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_LONG).show();
        }
        else {
            if (!(mSenha.getText().toString().equals(mConfSenha.getText().toString()))){
                Toast.makeText(this, "Senhas diferentes!", Toast.LENGTH_LONG).show();
            }
            else {
                String url = "http://192.168.1.12:60110/api/Usuario";
                String[] dados = {mNome.getText().toString(), mUser.getText().toString(), mSenha.getText().toString(), mEmail.getText().toString(), url};
                new HTTPReqTask().execute(dados);
            }
        }
    }

    private class HTTPReqTask extends AsyncTask<String, Void, Void> {

        @Override
        protected void onPreExecute() {
            Toast.makeText(UserCadActivity.this, R.string.loading, Toast.LENGTH_LONG).show();
        }

        @Override
        protected Void doInBackground(String... params) {
            HttpURLConnection urlConnection = null;
            String nomeUsuario, username, senha, email, qurl;
            nomeUsuario = params[0];
            username = params[1];
            senha = params[2];
            email = params[3];
            qurl = params[4];

            try {
                JSONObject postBody = new JSONObject();
                postBody.put("idUsuario",0);
                postBody.put("nomeUsuario", nomeUsuario);
                postBody.put("username", username);
                postBody.put("senha", senha);
                postBody.put("email", email);

                URL url = new URL(qurl);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setRequestMethod("POST");
                urlConnection.setDoOutput(true);
                urlConnection.setDoInput(true);
                urlConnection.setChunkedStreamingMode(0);

                OutputStreamWriter wr= new OutputStreamWriter(urlConnection.getOutputStream());
                wr.write(postBody.toString());
                wr.flush();

                int code = urlConnection.getResponseCode();
                if (code !=  201) {
                    throw new IOException("Resposta inválida: " + code);
                }

                BufferedReader rd = new BufferedReader(new InputStreamReader(
                        urlConnection.getInputStream()));
                String line;
                while ((line = rd.readLine()) != null) {
                    Log.i("data", line);
                }
            } catch (Exception e) {
                e.printStackTrace();
                try {
                    Toast.makeText(UserCadActivity.this, "Algum erro aconteceu! " + e, Toast.LENGTH_LONG).show();
                }catch (Exception exception){
                    e.printStackTrace();
                }
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            Toast.makeText(UserCadActivity.this, "Cadastro concluído!", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(UserCadActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
}