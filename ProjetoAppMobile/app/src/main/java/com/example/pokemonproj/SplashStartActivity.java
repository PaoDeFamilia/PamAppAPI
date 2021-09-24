package com.example.pokemonproj;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashStartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_start);

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run(){
                Intent splash = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(splash);
                finish();
            }
        },1500);
    }
}