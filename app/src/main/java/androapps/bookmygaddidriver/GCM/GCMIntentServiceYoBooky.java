package androapps.bookmygaddidriver.GCM;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.provider.Settings;
import androidx.core.app.NotificationCompat;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gcm.GCMBaseIntentService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;

import java.util.HashMap;

import androapps.bookmygaddidriver.R;
import androapps.bookmygaddidriver.interfaces.ApiResponse;
import androapps.bookmygaddidriver.network.NetworkRequest;
import androapps.bookmygaddidriver.utils.Constants;


@SuppressLint("NewApi")
public class GCMIntentServiceYoBooky extends GCMBaseIntentService implements ApiResponse {

    public static SharedPreferences sharedPreferences;
    public static SharedPreferences sharedPreferences1;
    static String type = "", message;
    static Context mContext;
    private static int notificationIdOne = 111;
    SharedPreferences.Editor editor;
    String notice_type;
    String GCM_NO;
    PendingIntent pIntent;
    TextView editText;
    String user_id = "";

    public GCMIntentServiceYoBooky() {
        super(Constants.APIKeys.sender_id);
    }

    @Override
    protected void onRegistered(Context context, String registrationId) {

        Log.i(TAG, "Device registered: regId = " + registrationId);

        GCM_NO=registrationId;

        sharedPreferences=context.getSharedPreferences("Details",0);
        sharedPreferences1=context.getSharedPreferences("GCM",0);
        editor=sharedPreferences1.edit();
        user_id=sharedPreferences.getString("id","").trim();
        String sessioncode=sharedPreferences.getString("sessioncode","");


        if (user_id.length() > 0) {
            HashMap<String,String> params=new HashMap<>();
            params.put("driver_id",user_id);
            params.put("gcm_number",registrationId);
            params.put("token_session", sessioncode.trim());
            storeGcmNoToServer(context,params);
        }
    }

    @Override
    protected void onMessage(Context context, Intent intent) {
        this.mContext = context;
        type = intent.getExtras().getString("type");
        message = intent.getExtras().getString("message");/*
        sharedPreferences=context.getSharedPreferences("Details",0);
        user_id=sharedPreferences.getString("id","").trim();
        if (user_id.length() > 0){
            generateNotification(mContext);
        }*/
        generateNotification(mContext);
    }

    @Override
    protected void onDeletedMessages(Context context, int total) {
    }

    @Override
    public void onError(Context context, String errorId) {
        Log.i(TAG, "Received error: " + errorId);
    }

    @Override
    protected boolean onRecoverableError(Context context, String errorId) {
        Log.i(TAG, "onRecoverableError : " + errorId);
        return super.onRecoverableError(context, errorId);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void generateNotification(Context context) {
        PendingIntent pIntent = null;
        NotificationManager notificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);

        Intent notificationIntent = new Intent(context, androapps.bookmygaddidriver.activities.NewRideActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        pIntent = PendingIntent.getActivity(context, 0,
                notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            Notification myNotication;
            Notification.Builder builder = new Notification.Builder(context);
            builder.setAutoCancel(true);
            builder.setContentTitle(getString(R.string.app_name));
            builder.setContentText(message);
            builder.setSmallIcon(R.mipmap.ic_launcher);
            builder.setContentIntent(pIntent);
            builder.setLights(Color.RED, 3000, 3000);
            builder.setSound(Settings.System.DEFAULT_NOTIFICATION_URI);
            builder.build();
            builder.getNotification().flags |= Notification.FLAG_AUTO_CANCEL;
            myNotication = builder.getNotification();
            notificationManager.notify(11, myNotication);
        } else {
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                    context).setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(getString(R.string.app_name)).setContentText(message);
            mBuilder.setAutoCancel(true);
            mBuilder.setVibrate(new long[]{1000, 1000, 1000, 1000, 1000});
            mBuilder.setLights(Color.RED, 3000, 3000);
            mBuilder.setSound(Settings.System.DEFAULT_NOTIFICATION_URI);
            mBuilder.setContentIntent(pIntent);
            Log.d("TAG", "Notification sent successfully.");
            notificationManager.notify(notificationIdOne, mBuilder.build());
        }
    }

    @Override
    protected void onUnregistered(Context arg0, String arg1) {
    }


    void storeGcmNoToServer(Context context,HashMap<String,String> params) {
        NetworkRequest.sendStringRequest1(context,Constants.Urls.SAVE_GCM,params,this,Constants.APICodes.SAVE_GCM);
    }



    @Override
    public void NetworkRequestCompleted(int apiCode, JSONObject response) {
        Gson gson = new GsonBuilder().create();

        switch (apiCode) {
            case Constants.APICodes.SAVE_GCM: {
                try {
                    if (response.getString("status").equals("1")) {
                        sharedPreferences = getSharedPreferences("GCM", 0);
                        editor = sharedPreferences.edit();
                        editor.putString("gcm_no", GCM_NO);
                        editor.commit();
                    }else {
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
    @Override
    public void NetworkRequestCompleted(int apiCode, JSONObject response, ProgressDialog progressDialog) {

    }
}
