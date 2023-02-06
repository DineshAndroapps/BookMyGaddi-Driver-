package androapps.bookmygaddidriver.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

/**
 * Created by ADMIN on 27-01-2017.
 */
public class BootCompleteReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {


        SharedPreferences userdetalis1 = context
                .getSharedPreferences("Service", 0);
        String status="";
        status = userdetalis1.getString("services", "");
         if(status.equals("1")) {
             Toast.makeText(context, "Device Started", Toast.LENGTH_SHORT).show();
             Intent service = new Intent(context, LocationService.class);
             service.setAction("service_reboot");
             context.startService(service);
         }else {

         }

    }

}
