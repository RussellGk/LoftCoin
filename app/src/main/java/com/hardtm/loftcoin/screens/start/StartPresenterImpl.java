package com.hardtm.loftcoin.screens.start;

import android.support.annotation.Nullable;

import com.hardtm.loftcoin.data.api.Api;
import com.hardtm.loftcoin.data.api.model.RateResponse;
import com.hardtm.loftcoin.data.prefs.Prefs;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StartPresenterImpl implements StartPresenter {

    private Api api;
    private Prefs prefs;

    @Nullable
    private StartView view;

    public StartPresenterImpl(Api api, Prefs prefs) {
        this.api = api;
        this.prefs = prefs;
    }

    @Override
    public void attechView(StartView view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        this.view = null;
    }

    @Override
    public void loadRate() {
        api.ticker("array", prefs.getFiatCurrency().name()).enqueue(new Callback<RateResponse>() {
            @Override
            public void onResponse(Call<RateResponse> call, Response<RateResponse> response) {
                if(view != null) {
                    view.navigateToMainScreen();
                }
            }

            @Override
            public void onFailure(Call<RateResponse> call, Throwable t) {

            }
        });
    }
}
