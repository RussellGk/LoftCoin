package com.hardtm.loftcoin.data.prefs;

import com.hardtm.loftcoin.data.model.Fiat;

public interface Prefs {

    boolean isFirstLaunch();

    void setFirstLaunch(boolean firstLaunch);

    Fiat getFiatCurrency();

    void setFiatCurrency(Fiat currency);
}
