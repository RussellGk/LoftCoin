package com.hardtm.loftcoin.screens.main.rate;

public interface RatePresenter {

    void attechView(RateView view);

    void detachView();

    void getRate();

    void onRefresh();
}
