package com.example.malki.bktelematics;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.test.mock.MockPackageManager;
import android.util.Log;

import com.example.malki.telematics.R;

public class SplashActivity extends Activity {

    private final int SPLASH_LENGTH = 1000;
    private SharedPreferences pref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */

                pref = getSharedPreferences("myfile", 0);
                boolean value = pref.getBoolean(getString(R.string.verified_number), false);
                if (!value)
                {
                    Intent connect = new Intent(SplashActivity.this, BKSigninActivity.class);
                    SplashActivity.this.startActivity(connect);

                    SplashActivity.this.finish();
                }

                else
                {
                    Intent connect = new Intent(SplashActivity.this, Landing.class);
                    SplashActivity.this.startActivity(connect);

                    SplashActivity.this.finish();
                }

            }
        }, SPLASH_LENGTH);
    }
}
