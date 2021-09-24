package com.example.pokemonproj;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

public class CarregaUser extends AsyncTaskLoader<String> {
    private final String mQueryString;
    CarregaUser(Context context, String queryString) {
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
        return UserGetURL.buscaUser(mQueryString);
    }
}
