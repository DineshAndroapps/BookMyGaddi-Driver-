package androapps.bookmygaddidriver.GCM;

import android.content.Context;

import com.google.android.gcm.GCMBroadcastReceiver;

/**
 * Created by sunnygupta on 14/09/15.
 */
public class GCMReceiver extends GCMBroadcastReceiver {
    @Override
    protected String getGCMIntentServiceClassName(Context context) {
        return "androapps.bookmygaddidriver.GCM.GCMIntentServiceYoBooky";
    }
}
