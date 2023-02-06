package androapps.bookmygaddidriver.interfaces;

import android.app.ProgressDialog;

import org.json.JSONObject;

/**
 * Created by ADMIN on 01-10-2016.
 */
public interface ApiResponse {
    void NetworkRequestCompleted(int apiCode, JSONObject response, ProgressDialog progressDialog);
    void NetworkRequestCompleted(int apiCode, JSONObject response);
}
