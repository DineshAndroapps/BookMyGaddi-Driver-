package androapps.bookmygaddidriver.activities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.telephony.SmsMessage;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import androapps.bookmygaddidriver.R;
import androapps.bookmygaddidriver.interfaces.ApiResponse;
import androapps.bookmygaddidriver.models.Login;
import androapps.bookmygaddidriver.network.NetworkRequest;
import androapps.bookmygaddidriver.utils.Constants;
import androapps.bookmygaddidriver.utils.Methods;

public class MainActivity extends AppCompatActivity implements ApiResponse {

    EditText edt_email, edt_password;
    TextView txt_forgot, txt_title;
    Button btn_submit;
    RelativeLayout rel_main;
    String email, mobile, password;
    HashMap<String, String> params;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ImageView img_back;

    static EditText etForgotPassword, edtenterpassword, edtenterotp;
    String otp;
    static Button enter;
    String regmobilenumber;
    String userid, verificationcode;
    ProgressDialog pDailog;

    String id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialise();


        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userValidation() && passValidation()) {
                    Log.d("params_1", "onClick: " + params);
                    params = new HashMap<>();
                    params.put("mobile", email);
                    params.put("password", password);
                    checkConnectionAndProceed();
                }
            }
        });
        txt_forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Dialog dialog = new Dialog(view.getContext());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.activity_forgotpassword);
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(dialog.getWindow().getAttributes());
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;


                Button dialogButton = (Button) dialog.findViewById(R.id.button_submit);
                enter = (Button) dialog.findViewById(R.id.enter);

                etForgotPassword = (EditText) dialog.findViewById(R.id.et_forgot_password);
                edtenterotp = (EditText) dialog.findViewById(R.id.edtenterotp);
                final TextView resendotp = (TextView) dialog.findViewById(R.id.resendotp);


                resendotp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });

                dialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        regmobilenumber = etForgotPassword.getText().toString();


                        if (regmobilenumber.equals("")) {
                            Toast.makeText(MainActivity.this, "Please provide Mobile number", Toast.LENGTH_SHORT).show();
                        } else {

                            if (Methods.getNetworkStatus(MainActivity.this)) {
                                forgotpassword();

                                enter.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        otp = edtenterotp.getText().toString();

                                        if (otp.equals("")) {
                                            Toast.makeText(MainActivity.this, "Enter Verification code", Toast.LENGTH_SHORT).show();
                                        } else {

                                            if (otp.equals(verificationcode)) {
                                                dialog.dismiss();
                                                Dialoge(view);
                                            } else {
                                                Toast.makeText(MainActivity.this, "Please Enter correct otp ", Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                    }
                                });
                            } else {
                                Toast.makeText(MainActivity.this, "Please check your internet connection", Toast.LENGTH_SHORT).show();
                            }
                        }

                    }


                });

                resendotp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        regmobilenumber = etForgotPassword.getText().toString();
                        if (regmobilenumber.equals("")) {
                            Toast.makeText(MainActivity.this, "Please provide Mobile number", Toast.LENGTH_SHORT).show();
                        } else {
                            if (Methods.getNetworkStatus(MainActivity.this)) {
                                forgotpassword();


                                enter.setOnClickListener(
                                        new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                otp = edtenterotp.getText().toString();
                                                if (otp.equals(verificationcode)) {
                                                    dialog.dismiss();
                                                    Dialoge(view);
                                                } else {
                                                    Toast.makeText(MainActivity.this, "Please Enter correct otp ", Toast.LENGTH_SHORT).show();
                                                }


                                            }
                                        });
                            } else {
                                Toast.makeText(MainActivity.this, "Please check your internet connection", Toast.LENGTH_SHORT).show();
                            }

                        }
                    }
                });

                dialog.show();
                dialog.getWindow().setAttributes(lp);


            }
        });
    }

    private void initialise() {
        edt_email = (EditText) findViewById(R.id.edt_email);
        edt_password = (EditText) findViewById(R.id.edt_password);
        txt_forgot = (TextView) findViewById(R.id.txt_forgot);
        btn_submit = (Button) findViewById(R.id.btn_submit);
        rel_main = (RelativeLayout) findViewById(R.id.rel_main);
        txt_title = (TextView) findViewById(R.id.txt_title);
        img_back = (ImageView) findViewById(R.id.img_back);

        txt_title.setText("Login To Your Account");
        sharedPreferences = getSharedPreferences("Details", 0);
        editor = sharedPreferences.edit();

        img_back.setVisibility(View.GONE);
//        getActionBar().setHomeButtonEnabled(true);
//        getActionBar().setDisplayHomeAsUpEnabled(true);


    }

    private boolean userValidation() {
        email = edt_email.getText().toString().trim();

        if (email.length() == 0) {
            edt_email.setError("Enter email or mobile");
            edt_email.requestFocus();
            return false;
        } else {
            boolean isDigit = Character.isDigit(email.charAt(0));
            if (isDigit) {
                if (!Methods.mobileValidator(email)) {
                    edt_email.setError("Enter proper mobile");
                    edt_email.requestFocus();
                    return false;
                } else {
                    return true;
                }
            } else {
                if (!Methods.emailValidator(email)) {
                    edt_email.setError("Enter proper Email");
                    edt_email.requestFocus();
                    return false;
                } else {
                    return true;
                }
            }
        }
    }

    private boolean passValidation() {
        password = edt_password.getText().toString().trim();
        if (password.length() == 0) {
            edt_password.setError("Enter password");
            edt_password.requestFocus();
            return false;
        } else {
            return true;
        }
    }

    private void checkConnectionAndProceed() {
        if (Methods.getNetworkStatus(MainActivity.this)) {
            login();
        } else {
            Snackbar bar = Snackbar.make(rel_main, "Please check internet connection", Snackbar.LENGTH_LONG);
            bar.show();


        }
    }

    private void login() {
        Log.d("params_3", "onClick: " + Constants.Urls.LOGIN + " gg=>" + Constants.APICodes.LOGIN);
        Log.d("params_2", "onClick: " + params);

        NetworkRequest.sendStringRequest(MainActivity.this, Constants.Urls.LOGIN, params, this, Constants.APICodes.LOGIN);
    }

    @Override
    public void NetworkRequestCompleted(int apiCode, JSONObject response, ProgressDialog progressDialog) {
        Gson gson = new GsonBuilder().create();
        Log.d("response", "NetworkRequestCompleted: " + response);
        switch (apiCode) {
            case Constants.APICodes.LOGIN: {
                try {
                    if (response.getString(Constants.APIKeys.CODE).equals("1")) {
                        String sessioncode = response.getString("token_session");
                        Login login = gson.fromJson(response.getJSONObject("detail").toString(), Login.class);
                        editor.putString("name", login.getName());
                        editor.putString("email", login.getEmail());
                        editor.putString("id", login.getId());
                        editor.putString("contact", login.getContact_no());
                        editor.putBoolean("isLogin", true);
                        editor.putString("sessioncode", sessioncode);
                        editor.commit();
                        Intent intent = new Intent(MainActivity.this, NewRideActivity.class);
                        intent.putExtra("Details", login);
                        startActivity(intent);
                        finish();
                        Toast.makeText(MainActivity.this, "Successfully Logged In", Toast.LENGTH_SHORT).show();
                    } else if (response.getString(Constants.APIKeys.CODE).equals("10")) {
                        Toast.makeText(MainActivity.this, response.getString(Constants.APIKeys.MESSAGE), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this, response.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        progressDialog.dismiss();
    }

    @Override
    public void NetworkRequestCompleted(int apiCode, JSONObject response) {

    }


    public void Dialoge(View view) {


        final Dialog dialog1 = new Dialog(view.getContext());
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.setContentView(R.layout.activity_reenter_password);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog1.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        Button dialogButton = (Button) dialog1.findViewById(R.id.button_submit1);
        edtenterpassword = (EditText) dialog1.findViewById(R.id.edtenterpassword);
        //dialogButton.setTypeface(type);
        // if button is clicked, close the custom dialog

        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (edtenterpassword.getText().toString().equals("")) {
                    Toast.makeText(MainActivity.this, "Please provide password", Toast.LENGTH_SHORT).show();
                } else {
                    if (Methods.getNetworkStatus(MainActivity.this)) {
                        Reenterpassword();
                        dialog1.dismiss();
                    } else {
                        Toast.makeText(MainActivity.this, "Please check your internet connection", Toast.LENGTH_SHORT).show();
                    }


                }

            }


        });
        dialog1.show();
        dialog1.getWindow().setAttributes(lp);


    }


    void forgotpassword() {
        pDailog = new ProgressDialog(MainActivity.this);
        pDailog.setMessage("Loading");
        pDailog.setCancelable(false);
        pDailog.show();


        String tag_json_obj = "json_obj_req";
        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                Constants.Urls.mobileverifivation,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        pDailog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String code = jsonObject.getString("code");


                            if (code.equals("1")) {

                                userid = jsonObject.getString("user_id");
                                verificationcode = jsonObject.getString("veri_code");


                            } else {
                                Toast.makeText(MainActivity.this, " Wrong Mobile number ", Toast.LENGTH_LONG).show();
                                pDailog.dismiss();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            pDailog.dismiss();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                        pDailog.dismiss();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("mobile", regmobilenumber.trim());
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

        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        int socketTimeout = 15000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjReq.setRetryPolicy(policy);

        requestQueue.add(jsonObjReq);

    }


    public static class Broadcast extends BroadcastReceiver {
        IntentFilter filter = new IntentFilter(" androapps.bookmygaddidriver.activities");


// SmsManager sms = SmsManager.getDefault();

        //@Override
        public void onReceive(Context context, Intent intent) {

            // Retrieves a map of extended data from the intent.
            final Bundle bundle = intent.getExtras();

            try {

                if (bundle != null) {

                    final Object[] pdusObj = (Object[]) bundle.get("pdus");

                    for (int i = 0; i < pdusObj.length; i++) {

                        SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                        String phoneNumber = currentMessage.getDisplayOriginatingAddress();

                        String senderNum = phoneNumber;
                        String message = currentMessage.getDisplayMessageBody();
                        //String testString = "This is a sentence";
                        String[] parts = message.split(" ");
                        String lastWord = parts[parts.length - 1];
                        //  System.out.println(lastWord); //
                        edtenterotp.setText(lastWord);
                        enter.performClick();


//                        final Dialog dialog1 = new Dialog(Login.this);
//                        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                        dialog1.setContentView(R.layout.activity_reenter_password);
//                        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
//                        lp.copyFrom(dialog1.getWindow().getAttributes());
//                        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
//                        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
//
//                        Button dialogButton = (Button) dialog1.findViewById(R.id.button_submit);
//                        //dialogButton.setTypeface(type);
//                        // if button is clicked, close the custom dialog
//
//                        dialogButton.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//
//                                edtenterpassword = (EditText) dialog1.findViewById(R.id.edtenterpassword);
//                                //etForgotPassword.setTypeface(type);
//
////                        if (etForgotPassword.getText().toString().length() > 0
////                                && android.util.Patterns.EMAIL_ADDRESS.matcher(sendEmail).matches()) {
////
////                           // forgotpassword();
////                        } else {
////                            Toast.makeText(Login.this, "Please enter a valid email address", Toast.LENGTH_SHORT).show();
////                        }
//
//                                dialog1.dismiss();
//
//                            }
//
//
//                        });
//                        dialog1.show();
//                        dialog1.getWindow().setAttributes(lp);


//                        if (etForgotPassword.getText().toString().length() > 0
//                                && android.util.Patterns.EMAIL_ADDRESS.matcher(sendEmail).matches()) {
//
//                           // forgotpassword();
//                        } else {
//                            Toast.makeText(Login.this, "Please enter a valid email address", Toast.LENGTH_SHORT).show();
//                        }


                        Log.i("SmsReceiver", "senderNum: " + senderNum + "; message: " + message);


                        // Show Alert
                        //    int duration = Toast.LENGTH_LONG;
                        //         Toast toast = Toast.makeText(context,
                        //            "senderNum: "+ senderNum + ", message: " + message, duration);
                        //     toast.show();

                    } // end for loop
                } // bundle is null

            } catch (Exception e) {
                Log.e("SmsReceiver", "Exception smsReceiver" + e);

            }
        }
    }


    public void Reenterpassword() {
        pDailog = new ProgressDialog(MainActivity.this);
        pDailog.setMessage("Loading");
        pDailog.setCancelable(false);
        pDailog.show();


        String tag_json_obj = "json_obj_req";
        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                Constants.Urls.forgotpassword,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        pDailog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String code = jsonObject.getString("code");
                            //String userid=jsonObject.getString("id");


                            if (code.equals("1")) {
                                Toast.makeText(MainActivity.this, "Password changed", Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                                startActivity(intent);

                            } else {
                                Toast.makeText(MainActivity.this, "Some thing went wrong please try again ", Toast.LENGTH_SHORT).show();
                            }


                        } catch (JSONException e) {
                            pDailog.dismiss();
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                pDailog.dismiss();

                Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("mobile", regmobilenumber.trim());
                params.put("password", edtenterpassword.getText().toString().trim());
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
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        int socketTimeout = 15000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjReq.setRetryPolicy(policy);

        requestQueue.add(jsonObjReq);

    }


}
