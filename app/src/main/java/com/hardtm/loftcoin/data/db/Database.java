package com.hardtm.loftcoin.data.db;

import com.hardtm.loftcoin.data.db.model.CoinEntity;
import com.hardtm.loftcoin.data.db.model.Transaction;
import com.hardtm.loftcoin.data.db.model.TransactionModel;
import com.hardtm.loftcoin.data.db.model.Wallet;
import com.hardtm.loftcoin.data.db.model.WalletModel;

import java.util.List;

import io.reactivex.Flowable;

public interface Database {

    void saveCoins(List<CoinEntity> coins);

    void saveWallet(Wallet wallet);

    void saveTransaction(List<Transaction> transactions);

    Flowable<List<TransactionModel>> getTransactions(String walletId);

    Flowable<List<CoinEntity>> getCoins();

    Flowable<List<WalletModel>> getWallets();

    CoinEntity getCoin(String symbol);
}
