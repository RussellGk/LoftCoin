package com.hardtm.loftcoin.screens.start;

import android.support.annotation.Nullable;

import com.hardtm.loftcoin.data.api.Api;
import com.hardtm.loftcoin.data.api.model.Coin;
import com.hardtm.loftcoin.data.db.Database;
import com.hardtm.loftcoin.data.db.model.CoinEntity;
import com.hardtm.loftcoin.data.db.model.CoinEntityMapper;
import com.hardtm.loftcoin.data.prefs.Prefs;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class StartPresenterImpl implements StartPresenter {

    private Api api;
    private Prefs prefs;
    private Database database;
    private CoinEntityMapper mapper;
    private CompositeDisposable disposables = new CompositeDisposable();

    @Nullable
    private StartView view;

    public StartPresenterImpl(Api api, Prefs prefs, Database database, CoinEntityMapper mapper) {
        this.api = api;
        this.prefs = prefs;
        this.database = database;
        this.mapper = mapper;
    }

    @Override
    public void attechView(StartView view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        disposables.dispose();
        this.view = null;
    }

    @Override
    public void loadRate() {

        Disposable disposable = api.ticker("array", prefs.getFiatCurrency().name())
                .subscribeOn(Schedulers.io())
                .map(rateResponse -> {
                    List<Coin> coins = rateResponse.data;
                    List<CoinEntity> coinEntities = mapper.mapCoins(coins);
                    database.saveCoins(coinEntities);
                    return coinEntities;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        coinEntities -> {
                            if(view != null) {
                                view.navigateToMainScreen();
                            }
                        },
                        throwable -> {

                        }
                );

        disposables.add(disposable);
    }
}
