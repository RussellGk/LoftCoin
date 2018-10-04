package com.hardtm.loftcoin.screens.launch;

import android.app.Activity;
import android.os.Bundle;

import com.hardtm.loftcoin.App;
import com.hardtm.loftcoin.data.prefs.Prefs;
import com.hardtm.loftcoin.screens.welcome.WelcomeActivity;

public class LaunchActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Prefs prefs = ((App)getApplication()).getPrefs();

        if(prefs.isFirstLaunch()){
            WelcomeActivity.start(this);
        }else {

        }
    }
}
