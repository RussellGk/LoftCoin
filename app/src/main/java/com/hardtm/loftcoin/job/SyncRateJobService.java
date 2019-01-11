package com.hardtm.loftcoin.job;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.hardtm.loftcoin.App;
import com.hardtm.loftcoin.R;
import com.hardtm.loftcoin.data.api.Api;
import com.hardtm.loftcoin.data.db.Database;
import com.hardtm.loftcoin.data.db.model.CoinEntity;
import com.hardtm.loftcoin.data.db.model.CoinEntityMapper;
import com.hardtm.loftcoin.data.db.model.QuoteEntity;
import com.hardtm.loftcoin.data.model.Fiat;
import com.hardtm.loftcoin.data.prefs.Prefs;
import com.hardtm.loftcoin.screens.main.MainActivity;
import com.hardtm.loftcoin.utils.CurrencyFormatter;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class SyncRateJobService extends JobService {

    public static final String EXTRA_SYMBOL = "symbol";
    public static final String NOTIFICATION_CHANNEL_RATE_CHANGED = "RATE_CHANGED";
    public static final int NOTIFICATION_ID_RATE_CHANGED = 10;

    private Api api;
    private Database database;
    private Prefs prefs;
    private CoinEntityMapper mapper;
    private CurrencyFormatter formatter;
    private Disposable disposable;
    private String symbol = "BTC";

    @Override
    public void onCreate() {
        super.onCreate();

        api = ((App)getApplication()).getApi();
        database = ((App)getApplication()).getDatabase();
        prefs = ((App)getApplication()).getPrefs();
        mapper = new CoinEntityMapper();
        formatter = new CurrencyFormatter();
    }

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        doJob(jobParameters);
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        if(!disposable.isDisposed()) {
            disposable.dispose();
        }
        return false;
    }

    private void doJob(JobParameters jobParameters) {
        symbol = jobParameters.getExtras().getString(EXTRA_SYMBOL,"BTC");
        disposable = api.ticker("array", prefs.getFiatCurrency().name())
                .subscribeOn(Schedulers.io())
                .map(response -> mapper.mapCoins(response.data))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        coinEntities -> {
                            handleCoins(coinEntities);
                            jobFinished(jobParameters, false);
                            },
                        error -> {
                            handleError(error);
                            jobFinished(jobParameters,false);
                        }
                );
    }

    private void handleCoins(List<CoinEntity> newCoins) {
        database.open();
        Fiat fiat = prefs.getFiatCurrency();
        CoinEntity oldCoin = database.getCoin(symbol);
        CoinEntity newCoin = findCoin(newCoins,symbol);

        if(oldCoin != null && newCoin != null) {
            QuoteEntity oldQuote = oldCoin.getQuote(fiat);
            QuoteEntity newQuote = newCoin.getQuote(fiat);
            if(newQuote.price != oldQuote.price) {
                double priceDiff = newQuote.price - oldQuote.price;
                String priceDiffText;
                String price = formatter.format(Math.abs(priceDiff), false);
                if(priceDiff > 0) {
                    priceDiffText = "+ " + price + " " + fiat.symbol;
                }else {
                    priceDiffText = "- " + price + " " + fiat.symbol;
                }
                showRateChangedNotification(newCoin, priceDiffText);
            }else {
                Toast.makeText(this ,"Price not changed", Toast.LENGTH_LONG).show();
            }
        }

        database.saveCoins(newCoins);
        database.close();

    }

    private void handleError(Throwable throwable) {
        Toast.makeText(this ,throwable.getMessage(), Toast.LENGTH_LONG).show();
    }

    private CoinEntity findCoin(List<CoinEntity> newCoins, String symbol) {
        for(CoinEntity coin : newCoins) {
            if(coin.symbol.equals(symbol)) {
                return coin;
            }
        }
        return null;
    }

    private void showRateChangedNotification(CoinEntity newCoin, String priceDiff) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_RATE_CHANGED);
        notificationBuilder.setSmallIcon(R.drawable.ic_notification);
        notificationBuilder.setContentTitle(newCoin.name);
        notificationBuilder.setContentText(getString(R.string.notification_rate_changed_body, priceDiff));
        notificationBuilder.setAutoCancel(true);
        notificationBuilder.setContentIntent(pendingIntent);
        Notification notification = notificationBuilder.build();
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL_RATE_CHANGED, getString(R.string.notification_channel_rate_changed), NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }
        notificationManager.notify(newCoin.symbol, NOTIFICATION_ID_RATE_CHANGED, notification);
    }
}

