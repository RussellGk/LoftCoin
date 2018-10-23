package com.hardtm.loftcoin.screens.main.wallets;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.hardtm.loftcoin.data.db.model.CoinEntity;
import com.hardtm.loftcoin.data.db.model.TransactionModel;
import com.hardtm.loftcoin.data.db.model.WalletModel;

import java.util.List;

public abstract class WalletsViewModel extends AndroidViewModel {

    public WalletsViewModel(Application application) {
        super(application);
    }

    public abstract void onNewWalletClick();

    public abstract void getWallets();

    public abstract void onCurrencySelected(CoinEntity coin);

    public abstract void onWalletChanged(int position);

    public abstract LiveData<Boolean> walletsVisible();

    public abstract LiveData<Boolean> newWalletVisible();

    public abstract LiveData<List<WalletModel>> wallets();

    public abstract LiveData<List<TransactionModel>> transactions();

    public abstract LiveData<Object> selectCurrency();
}
