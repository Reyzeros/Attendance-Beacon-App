package org.altbeacon.beaconapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;

public class SplashActivity extends Activity {
    private static int SPLASH_TIME_OUT=3000;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent splashIntent=new Intent(SplashActivity.this, MainActivity.class);
                startActivity(splashIntent);
                overridePendingTransition(R.anim.slide_in_top,R.anim.slide_out_top);
                finish();
            }
        },SPLASH_TIME_OUT);
    }
}
