package androapps.bookmygaddidriver.network;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import androapps.bookmygaddidriver.interfaces.ApiResponse;
import androapps.bookmygaddidriver.utils.Constants;


/**
 * Created by ADMIN on 22-12-2016.
 */
public class NetworkRequest {

    private static JSONObject createJsonObject(Object model) throws JSONException {
        Gson gsonBuilder = new GsonBuilder().disableHtmlEscaping().serializeNulls().create();
        String abc = gsonBuilder.toJson(model);
        return new JSONObject(abc);
    }


    public static void sendObjectRequest(Context context,String url,Object model,final ApiResponse responseListener,final int apiCode)
    {
        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Please Wait");
        progressDialog.show();
        JsonObjectRequest jsonObjReq=null;

        try {
            jsonObjReq = new JsonObjectRequest(url, createJsonObject(model),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d("Response", response.toString());
                            responseListener.NetworkRequestCompleted(apiCode,response, progressDialog);
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d("Error", "Error: " + error.getMessage());
                    progressDialog.dismiss();
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }


        RequestQueue requestQueue = Volley.newRequestQueue(context);

        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(jsonObjReq);
    }

    public static void sendStringRequest(Context context,String url, final HashMap<String,String> params,
                                         final ApiResponse responseListener,final int apiCode)
    {
        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Please Wait");
        progressDialog.show();


        StringRequest stringRequest = new StringRequest(Request.Method.POST,url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject serverResponse = new JSONObject(response);
                            responseListener.NetworkRequestCompleted(apiCode, serverResponse, progressDialog);
                        }catch (Exception e)
                        {
                            e.printStackTrace();
                            progressDialog.dismiss();

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                return params;
            }


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> header = new HashMap<String, String>();
                header.put("BookMyGaddi", Constants.tokenheader);
                //    return super.getHeaders();
                return header;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }


    public static void sendStringRequest1(Context context,String url, final HashMap<String,String> params,
                                         final ApiResponse responseListener,final int apiCode)
    {

        StringRequest stringRequest = new StringRequest(Request.Method.POST,url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject serverResponse = new JSONObject(response);
                            responseListener.NetworkRequestCompleted(apiCode, serverResponse);
                        }catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> header = new HashMap<String, String>();
                header.put("BookMyGaddi", Constants.tokenheader);
                //    return super.getHeaders();
                return header;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }


}
