package androapps.bookmygaddidriver.activities;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import com.splunk.mint.Mint;

import androapps.bookmygaddidriver.R;
import androapps.bookmygaddidriver.utils.RuntimePermissionsActivity;

public class SplashActivity extends RuntimePermissionsActivity {

    private static int SPLASH_TIME_OUT_IN_MILI_SECONDS = 3000;
    SharedPreferences sharedPreferences;
    private int REQUEST_PERMISSIONS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Mint.initAndStartSession(this.getApplication(), "f0009ddb");

        sharedPreferences = getSharedPreferences("Details", 0);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            SplashActivity.super.requestAppPermissions(new
                    String[]{
                    Manifest.permission.RECEIVE_SMS,
                    Manifest.permission.READ_SMS,
                    Manifest.permission.CALL_PHONE,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            }, R.string.runtime_permissions_txt
                    , REQUEST_PERMISSIONS);
        }else{
            startSplashing();
        }

    }

    @Override
    public void onPermissionsGranted(int requestCode) {
        startSplashing();
    }

    void startSplashing() {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (sharedPreferences.getBoolean("isLogin", false)) {
                    Intent i = new Intent(SplashActivity.this, NewRideActivity.class);
                    startActivity(i);
                    finish();
                } else {
                    Intent i = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        }, SPLASH_TIME_OUT_IN_MILI_SECONDS);

    }

}
