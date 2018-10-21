package com.hardtm.loftcoin.screens.currencies;

import com.hardtm.loftcoin.data.db.model.CoinEntity;

public interface CurrenciesAdapterListener {
    void onCurrencyClick(CoinEntity coin);
}
