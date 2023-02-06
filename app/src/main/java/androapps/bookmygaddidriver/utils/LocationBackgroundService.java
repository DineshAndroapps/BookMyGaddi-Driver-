package androapps.bookmygaddidriver.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Handler;
import android.os.IBinder;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import android.util.Log;
import android.widget.Button;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;

import androapps.bookmygaddidriver.R;
import im.delight.android.location.SimpleLocation;

/**
 * Created by androapps on 8/22/17.
 */

public class LocationBackgroundService extends Service {

    public static final String BROADCAST_ACTION = "" +
            "";
    private static final int PERMISSION_REQUEST_CODE_LOCATION = 1;
    int notifyId_Step = 100;
    Button connectButton;
    GoogleApiClient googleApiClient;
    Location lastLocation;
    LocationRequest locationRequest;
    int REQUEST_CHECK_SETTINGS = 1000;
    Double startLatitude = 0d;
    Double startLongitude = 0d;
    Double totalDistance = 0d;
    NewGpsTracker newGpsTracker;
    Intent intent;
    boolean isFirst = true;
    private NotificationCompat.Builder mBuilder;
    private NotificationManager mNotificationManager;
    private String CURRENT_DIST = "0";
    private GoogleApiClient client;
    private SimpleLocation simpleLocation;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("click1","onCreate");
        SharedPreferences sharedPreferences = getSharedPreferences("location", 0);
        boolean isService = sharedPreferences.getBoolean("isservice", false);
        if (isService) {
            intent = new Intent(BROADCAST_ACTION);

            initNotification();
            simpleLocation = new SimpleLocation(this);
            newGpsTracker = new NewGpsTracker(this);
            if (NewGpsTracker.currentLatlang != null) {
                startLatitude = NewGpsTracker.currentLatlang.latitude;
                startLongitude = NewGpsTracker.currentLatlang.longitude;
            }
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("click1","onStartCommand");

        SharedPreferences sharedPreferences = getSharedPreferences("location", 0);
        boolean isService = sharedPreferences.getBoolean("isservice", false);


        if (isService) {

            final Handler ha = new Handler();
            ha.postDelayed(new Runnable() {

                @Override
                public void run() {

                    //call function

                    SharedPreferences sharedPreferences = getSharedPreferences("location", 0);
                    boolean isService = sharedPreferences.getBoolean("isservice", false);
                    if (isService) {
                    setloca();
                    newGpsTracker.fetchLocation();




                        ha.postDelayed(this, 20000);
                    }
                }
            }, 20000);

        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        //stopForground();
        Log.d("Click1", "ondestroy");
        super.onDestroy();
    }




    private void initNotification() {
        mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setContentTitle(getResources().getString(R.string.app_name))
                .setContentText("Distance : " + CURRENT_DIST)
                .setContentIntent(getDefalutIntent(Notification.FLAG_ONGOING_EVENT))
                .setWhen(System.currentTimeMillis())
                .setPriority(Notification.PRIORITY_DEFAULT)
                .setAutoCancel(false)
                .setOngoing(true)
                .setSmallIcon(R.mipmap.ic_launcher);
        Notification notification = mBuilder.build();
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        startForeground(notifyId_Step, notification);

        //Log.d(TAG, "initNotification()");
    }

    public void setloca() {

        if (NewGpsTracker.currentLatlang != null) {


            double ditance = simpleLocation.calculateDistance(startLatitude, startLongitude, NewGpsTracker.currentLatlang.latitude, NewGpsTracker.currentLatlang.longitude);

            totalDistance = totalDistance + ditance;
            Log.d("click1", "diance " + ditance);
            startLatitude = NewGpsTracker.currentLatlang.latitude;
            startLongitude = NewGpsTracker.currentLatlang.longitude;
            Log.d("click1", "startLatitude" + NewGpsTracker.currentLatlang.latitude);
            Log.d("click1", "startLatitude" + NewGpsTracker.currentLatlang.longitude);
            //Double totalDistanceKM = totalDistance / 1000;
            CURRENT_DIST = String.format("%.2f", totalDistance / 1000) + " KM";
            updateNotification();
            intent.putExtra("distance", CURRENT_DIST);
            sendBroadcast(intent);
            SharedPreferences sharedPreferences = getSharedPreferences("location", 0);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("dist", String.format("%.2f", totalDistance / 1000));
            //editor.putFloat("dist",totalDistance);
            editor.commit();
        }
        //   ((TextView) (findViewById(R.id.distance))).setText(String.format("%.2f", totalDistance)+" Meter");

    }



    private void updateNotification() {

       // Intent hangIntent = new Intent(this, AcceptedRideDetailsActivity.class);
        Intent hangIntent = new Intent();
        PendingIntent hangPendingIntent = PendingIntent.getActivity(this, 0, hangIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        Notification notification = mBuilder.setContentTitle(getResources().getString(R.string.app_name))
                .setContentText("Distance : " + CURRENT_DIST)
                .setWhen(System.currentTimeMillis())
                .setContentIntent(hangPendingIntent)
                .build();
        mNotificationManager.notify(notifyId_Step, notification);

    }


    public PendingIntent getDefalutIntent(int flags) {
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1, new Intent(), flags);
        return pendingIntent;
    }

}
