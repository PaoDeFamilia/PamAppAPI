package com.example.pokemonproj;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

public class CarregaPokemon extends AsyncTaskLoader<String> {
    private final String mQueryString;
    CarregaPokemon(Context context, String queryString) {
        super(context);
        mQueryString = queryString;
    }
    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }
    @Nullable
    @Override
    public String loadInBackground() {
        return PokeURL.buscaPoke(mQueryString);
    }
}
