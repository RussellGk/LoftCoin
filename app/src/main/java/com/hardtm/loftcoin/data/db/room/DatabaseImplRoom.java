package com.hardtm.loftcoin.data.db.room;

import com.hardtm.loftcoin.data.db.Database;
import com.hardtm.loftcoin.data.db.model.CoinEntity;

import java.util.List;

public class DatabaseImplRoom implements Database {

    private AppDatabase database;

    public DatabaseImplRoom(AppDatabase database) {
        this.database = database;
    }

    @Override
    public void saveCoins(List<CoinEntity> coins) {
        database.coinDao().saveCoins(coins);
    }

    @Override
    public List<CoinEntity> getCoins() {
        return database.coinDao().getCoins();
    }
}
