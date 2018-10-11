package com.hardtm.loftcoin.screens.welcome;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.hardtm.loftcoin.App;
import com.hardtm.loftcoin.R;
import com.hardtm.loftcoin.data.prefs.Prefs;
import com.hardtm.loftcoin.screens.start.StartActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WelcomeActivity extends AppCompatActivity {

    @BindView(R.id.viewPager)
    ViewPager pager;
    @BindView(R.id.start_btn)
    Button startBtn;

    public static void startInNewTask(Context context){
        Intent starter = new Intent(context, WelcomeActivity.class);
        starter.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        ButterKnife.bind(this);

        final Prefs prefs = ((App)getApplication()).getPrefs();
        pager.setAdapter(new WelcomePageAdapter(getSupportFragmentManager()));
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prefs.setFirstLaunch(false);
                StartActivity.startInNewTask(WelcomeActivity.this);
            }
        });
    }
}
