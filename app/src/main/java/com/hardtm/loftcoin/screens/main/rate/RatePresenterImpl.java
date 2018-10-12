package com.hardtm.loftcoin.screens.main.rate;

import android.support.annotation.Nullable;

import com.hardtm.loftcoin.data.api.Api;
import com.hardtm.loftcoin.data.prefs.Prefs;

public class RatePresenterImpl implements RatePresenter {

    private Api api;
    private Prefs prefs;
    @Nullable
    private RateView view;

    public RatePresenterImpl(Api api, Prefs prefs) {
        this.api = api;
        this.prefs = prefs;
    }

    @Override
    public void attechView(RateView view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        this.view = null;
    }

    @Override
    public void getRate() {

    }

    @Override
    public void onRefresh() {

    }


}
