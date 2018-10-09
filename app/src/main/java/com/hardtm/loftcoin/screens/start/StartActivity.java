package com.hardtm.loftcoin.screens.start;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.hardtm.loftcoin.App;
import com.hardtm.loftcoin.R;
import com.hardtm.loftcoin.data.api.Api;
import com.hardtm.loftcoin.data.prefs.Prefs;
import com.hardtm.loftcoin.screens.main.MainActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StartActivity extends AppCompatActivity implements StartView {

    private static final String TAG = "StartActivity";

    public static void startInNewTask(Context context){
        Intent starter = new Intent(context, StartActivity.class);
        starter.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(starter);
    }

    @BindView(R.id.start_top_corner)
    ImageView topCorner;

    @BindView(R.id.start_bottom_corner)
    ImageView bottomCorner;

    private StartPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        ButterKnife.bind(this);
        Api api = ((App)getApplication()).getApi();
        Prefs prefs = ((App)getApplication()).getPrefs();
        presenter = new StartPresenterImpl(api, prefs);
        presenter.attechView(this);
        presenter.loadRate();
    }

    @Override
    protected void onDestroy() {
        presenter.detachView();
        super.onDestroy();
    }

    @Override
    public void navigateToMainScreen() {
        MainActivity.startInNewTask(this);
    }
}
