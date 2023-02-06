package androapps.bookmygaddidriver.utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.LocationManager;
import android.os.Handler;
import android.os.IBinder;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.HashMap;

import androapps.bookmygaddidriver.R;
import androapps.bookmygaddidriver.activities.AcceptedRideDetailsActivity;
import androapps.bookmygaddidriver.interfaces.ApiResponse;
import androapps.bookmygaddidriver.models.RidesDetails;
import androapps.bookmygaddidriver.network.NetworkRequest;

/**
 * Created by ADMIN on 24-01-2017.
 */
public class LocationService extends Service implements ApiResponse {

    Handler handler;
    Runnable runnable;
    RidesDetails ridesDetails;
    SharedPreferences sharedPreferences;
    private double latitude;
    private double longitude;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sharedPreferences = getSharedPreferences("Ride", 0);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {

              //  Toast.makeText(LocationService.this, "Service Started", Toast.LENGTH_LONG).show();
                if (Methods.getNetworkStatus(LocationService.this)) {

                    SharedPreferences.Editor editor = getApplicationContext().getSharedPreferences("Service", 0).edit();
                    editor.putString("services", "1");
                    editor.commit();

                    getLatLong();
                } else {
                    Toast.makeText(LocationService.this, "Please Check Network Connection", Toast.LENGTH_LONG).show();
                }
                handler.postDelayed(this,
                        Constants.Twentyfive_seconds);

            }
        };
        handler.postDelayed(runnable, Constants.Twentyfive_seconds );

        play();

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable);
        stopForeground(true);
        Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
        SharedPreferences.Editor editor = getApplicationContext().getSharedPreferences("Service", 0).edit();
        editor.putString("services", "0");
        editor.commit();
    }

    @Override
    public void NetworkRequestCompleted(int apiCode, JSONObject response, ProgressDialog progressDialog) {
    }

    @Override
    public void NetworkRequestCompleted(int apiCode, JSONObject response) {

        switch (apiCode) {
            case Constants.APICodes.UPDATE_LATLAN: {
                try {
                    if (response.getString(Constants.APIKeys.CODE).equals("1")) {
                        System.out.print("");
                       // Toast.makeText(this, "Success", Toast.LENGTH_LONG).show();
                    }

                    else if(response.getString(Constants.APIKeys.CODE).equals("9")){
                        stopService(new Intent(getBaseContext(), LocationService.class));
                        Toast.makeText(LocationService.this, response.getString(Constants.APIKeys.MESSAGE), Toast.LENGTH_SHORT).show();
                        Methods.logout(LocationService.this);
                    }


                    else if(response.getString(Constants.APIKeys.CODE).equals("10")) {
                        Toast.makeText(LocationService.this,response.getString(Constants.APIKeys.MESSAGE) , Toast.LENGTH_SHORT).show();
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }


    void getLatLong() {

        GPSTracker gpsTracker;
        gpsTracker = new GPSTracker(LocationService.this);
        LocationManager locationManager;
        boolean isGPSEnabled = false;
        boolean isNetworkEnabled = false;
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (gpsTracker != null) {

            if (isGPSEnabled || isNetworkEnabled) {

                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("Details", 0);
                latitude = gpsTracker.getLatitude();
                longitude = gpsTracker.getLongitude();
                String lat= String.valueOf(latitude);
                String log= String.valueOf(longitude);
                if(lat.equals("0.0")||log.equals("0.0")){
                    Toast.makeText(LocationService.this, "Your device has GPS issue/contanct with admin", Toast.LENGTH_SHORT).show();
                }
                else {
                    HashMap<String, String> params = new HashMap<>();
                    params.put("driver_id", sharedPreferences.getString("id", ""));
                    params.put("token_session", sharedPreferences.getString("sessioncode", ""));
                    params.put("latitude", latitude + "");
                    params.put("longitude", longitude + "");
                    params.put("abc", "");

                    NetworkRequest.sendStringRequest1(LocationService.this, Constants.Urls.UPDATE_LATLAN, params, LocationService.this, Constants.APICodes.UPDATE_LATLAN);
                }
            } else {

                gpsTracker.showSettingsAlert();

            }
        }

    }


    private void play() {

//        Intent notificationIntent = new Intent(this, AcceptedRideDetailsActivity.class);
//        notificationIntent.setAction("Service");
//        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
//                | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
//                notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);
//        Notification notification = new NotificationCompat.Builder(this)
//                .setContentTitle("Your are Available to users")
//                .setSmallIcon(R.drawable.logo)
//                .setContentText(" ")
//                .setContentIntent(pendingIntent).build();
//
//        startForeground(100, notification);

        startMyOwnForeground();

    }
    private void startMyOwnForeground(){
        String NOTIFICATION_CHANNEL_ID = "androapps.bookmygaddidriver";
        String channelName = "bookmygaddi";
        NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
        chan.setLightColor(Color.BLUE);
        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        manager.createNotificationChannel(chan);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        Notification notification = notificationBuilder.setOngoing(true)
                .setSmallIcon(R.drawable.km)
                .setContentTitle("BookMyGaddi")
                .setPriority(NotificationManager.IMPORTANCE_MIN)
                .setCategory(Notification.CATEGORY_SERVICE)
                .build();
        startForeground(2, notification);
    }

}
