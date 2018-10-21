package com.hardtm.loftcoin.screens.main.rate;

import com.hardtm.loftcoin.data.model.Fiat;

public interface RatePresenter {

    void attechView(RateView view);

    void detachView();

    void getRate();

    void onRefresh();

    void onMenuItemCurrencyClick();

    void onFiatCurrencySelected(Fiat currency);
}
