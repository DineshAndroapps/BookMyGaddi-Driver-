package androapps.bookmygaddidriver.activities;

import android.app.ActivityManager;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import com.google.android.material.tabs.TabLayout;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import androapps.bookmygaddidriver.R;
import androapps.bookmygaddidriver.adapters.ViewPagerAdapter;
import androapps.bookmygaddidriver.fragments.CanceledRideFragment;
import androapps.bookmygaddidriver.fragments.CompletedRideFragment;
import androapps.bookmygaddidriver.fragments.NewRideFragment;
import androapps.bookmygaddidriver.models.Login;
import androapps.bookmygaddidriver.utils.Constants;
import androapps.bookmygaddidriver.utils.GPSTracker;
import androapps.bookmygaddidriver.utils.LocationService;
import androapps.bookmygaddidriver.utils.Methods;

public class NewRideActivity extends AppCompatActivity implements View.OnClickListener {

    Intent intent;
    TextView txt_title, txt_service;
    TabLayout tabLayout;
    ViewPager viewPager;
    Context context = this;
    private int[] tabIcons = {
            R.mipmap.tab1,
            R.mipmap.tab3,
            R.mipmap.tab2
    };
    ImageView img_back;
    DrawerLayout drawer;
    Toolbar toolbar;
    boolean doubleBackToExitPressedOnce = false;
    LinearLayout linearNavLogout, lnrMyProfile, lnrTerms, lnrContactUs;
    SharedPreferences sharedPreferences;

    private GPSTracker mGpsTracker;

    String city, add;
    double lat, log;
    String id;
    ProgressDialog pDailog;
    String checkridestatus = "0";
    private TextView txtVersion;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_ride);
        initialise();

        tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#FFFFFF"));


        linearNavLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (checkridestatus.equals("1")) {
                    Toast.makeText(context, "Your Ride is going on", Toast.LENGTH_SHORT).show();
                } else {
                    driverunavailability();
                    stopService(new Intent(getBaseContext(), LocationService.class));
                    Methods.logout(NewRideActivity.this, "Are you sure you want to logout?", drawer);
                }

            }
        });

    }


    private void initialise() {

        intent = getIntent();
        Login login = (Login) intent.getSerializableExtra("Details");
        txt_title = (TextView) findViewById(R.id.txt_title);
        txt_service = (TextView) findViewById(R.id.txt_service);
        txtVersion = (TextView) findViewById(R.id.txtVersion);


        img_back = (ImageView) findViewById(R.id.img_back);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        linearNavLogout = (LinearLayout) findViewById(R.id.linearNavLogout);
        lnrMyProfile = (LinearLayout) findViewById(R.id.lnrMyProfile);
        lnrTerms = (LinearLayout) findViewById(R.id.lnrTerms);
        lnrContactUs = (LinearLayout) findViewById(R.id.lnrContactUs);
        txt_title.setText("New Ride");
        toolbar = (Toolbar) findViewById(R.id.toolbar1);
        drawer = (DrawerLayout) findViewById(R.id.drawer);
        img_back.setVisibility(View.GONE);
        setupViewPager(viewPager);
        viewPager.setOffscreenPageLimit(2);
        tabLayout.setupWithViewPager(viewPager);


        try {

            PackageInfo pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
            String version = pInfo.versionName;

            txtVersion.setText("v " + version);

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        lnrMyProfile.setOnClickListener(this);
        lnrContactUs.setOnClickListener(this);
        lnrTerms.setOnClickListener(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.openDrawer, R.string.closeDrawer) {

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        drawer.setDrawerListener(toggle);
        toggle.syncState();

//
//        String status_reboot=intent.getAction();
//
//        if(status_reboot.equals("service_reboot")){
//
//            txt_service.setText("Stop");
//
//        }

        // setupTabIcons();


        //  txt_service.setText("Available");

        txt_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (txt_service.getText().toString().equals("UnAvailable")) {
                    Intent intent = new Intent(getBaseContext(), LocationService.class);
                    startService(intent);

                    // txt_service.setText("UnAvailable");


                    driveravailability();

                } else if (txt_service.getText().toString().equals("Available")) {

                    if (checkridestatus.equals("1")) {
                        Toast.makeText(context, "Your Ride is going on", Toast.LENGTH_SHORT).show();

                    } else {
                        driverunavailability();
                        stopService(new Intent(getBaseContext(), LocationService.class));
                    }
//                    txt_service.setText("Available");

                }
            }
        });


        sharedPreferences = getSharedPreferences("Details", 0);
        if (sharedPreferences.getString("id", "").length() > 0) {
            // setGcm();
        }
    }

    private void setGcm() {

//        SharedPreferences sharedPreferences;
//        sharedPreferences = getSharedPreferences("GCM", 0);
//        if (sharedPreferences.contains("gcm_no")) {
//            String gcmno = sharedPreferences.getString("gcm_no", "");
//            if (gcmno.length() > 0) {
////                return;
//                Methods.setGcm(NewRideActivity.this);
//            } else {
//                Methods.setGcm(NewRideActivity.this);
//            }
//        } else {
        Methods.setGcm(NewRideActivity.this);
//        }

    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new NewRideFragment(), "Current/Advance Booking");
        adapter.addFragment(new CompletedRideFragment(), "Ride Details");
        adapter.addFragment(new CanceledRideFragment(), "Canceled Booking");
        viewPager.setAdapter(adapter);
    }

    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
    }

    void backPressed() {

        if (doubleBackToExitPressedOnce) {
            finishAffinity();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawers();
        } else {
            backPressed();
        }
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.lnrMyProfile:
                drawer.closeDrawers();
                if (Methods.getNetworkStatus(NewRideActivity.this)) {
                    intent = new Intent(NewRideActivity.this, MyProfile.class);
                    startActivity(intent);
                } else
                    Toast.makeText(this, "Please check internet connection", Toast.LENGTH_SHORT).show();
                break;
            case R.id.lnrTerms:
                drawer.closeDrawers();
                if (Methods.getNetworkStatus(NewRideActivity.this)) {
                    intent = new Intent(NewRideActivity.this, Terms.class);
                    startActivity(intent);
                } else
                    Toast.makeText(this, "Please check internet connection", Toast.LENGTH_SHORT).show();
                break;
            case R.id.lnrContactUs:
                drawer.closeDrawers();
                if (Methods.getNetworkStatus(NewRideActivity.this)) {
                    intent = new Intent(NewRideActivity.this, ContactUs.class);
                    startActivity(intent);
                } else
                    Toast.makeText(this, "Please check internet connection", Toast.LENGTH_SHORT).show();
                break;
        }
    }


    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences userdetalis1 = getApplicationContext()
                .getSharedPreferences("Ridedetalis", 0);
        checkridestatus = userdetalis1.getString("ridestatus", "");
        Geocoder geocoder;
        List<Address> addresses;
        Address address = null;
        mGpsTracker = new GPSTracker(this);
        geocoder = new Geocoder(NewRideActivity.this, Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(mGpsTracker.getLatitude(), mGpsTracker.getLongitude(), 1);
            address = addresses.get(0);
//                txtRegion.setText(address.getLocality() + "," + address.getCountryName());
            lat = mGpsTracker.getLatitude();
            log = mGpsTracker.getLongitude();
        } catch (IndexOutOfBoundsException e) {
//                txtRegion.setText("Select Your Location");
        } catch (IOException e) {
            e.printStackTrace();
        }

        //  setupTabIcons();

        if (isMyServiceRunning(context)) {

            txt_service.setText("Available");
            txt_service.setBackgroundColor(ContextCompat.getColor(NewRideActivity.this, R.color.green));


        } else {
            txt_service.setText("UnAvailable");
            txt_service.setBackgroundColor(ContextCompat.getColor(NewRideActivity.this, R.color.red));
        }


    }

    private boolean isMyServiceRunning(Context mContext) {
        ActivityManager manager = (ActivityManager) mContext
                .getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager
                .getRunningServices(Integer.MAX_VALUE)) {
            if (LocationService.class.getName().equals(
                    service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }


    void driveravailability() {

        pDailog = new ProgressDialog(NewRideActivity.this);
        pDailog.setMessage("Loading");
        pDailog.setCancelable(true);
        pDailog.show();
        SharedPreferences sharedPreferences = getSharedPreferences("Details", 0);
        id = sharedPreferences.getString("id", "");
        String tag_json_obj = "json_obj_req";
        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                Constants.Urls.Driver_availability,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        pDailog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String code = jsonObject.getString("code");
                            String msg = jsonObject.getString("message");
                            if (code.equals("1")) {
                                Toast.makeText(NewRideActivity.this, "Success", Toast.LENGTH_LONG).show();
                                txt_service.setText("Available");
                                txt_service.setBackgroundColor(ContextCompat.getColor(NewRideActivity.this, R.color.green));
                            } else if (code.equals("9")) {
                                stopService(new Intent(getBaseContext(), LocationService.class));
                                Toast.makeText(NewRideActivity.this, msg, Toast.LENGTH_SHORT).show();
                                Methods.logout(NewRideActivity.this);
                            } else if (code.equals("10")) {
                                Toast.makeText(NewRideActivity.this, msg, Toast.LENGTH_SHORT).show();
                            } else {

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
                        Toast.makeText(NewRideActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                        pDailog.dismiss();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                SharedPreferences sharedPreferences = getSharedPreferences("Details", 0);
                id = sharedPreferences.getString("id", "");
                String sessioncode = sharedPreferences.getString("sessioncode", "");

                params.put("driver_id", id);
                params.put("token_session", sessioncode.trim());
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

        RequestQueue requestQueue = Volley.newRequestQueue(NewRideActivity.this);
        int socketTimeout = 15000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjReq.setRetryPolicy(policy);

        requestQueue.add(jsonObjReq);

    }

    void driverunavailability() {

        pDailog = new ProgressDialog(NewRideActivity.this);
        pDailog.setMessage("Loading");
        pDailog.setCancelable(true);
        pDailog.show();
        SharedPreferences sharedPreferences = getSharedPreferences("Details", 0);
        id = sharedPreferences.getString("id", "");

        String tag_json_obj = "json_obj_req";
        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                Constants.Urls.Driver_unAvailability,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        pDailog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String code = jsonObject.getString("code");
                            String msg = jsonObject.getString("message");
                            if (code.equals("1")) {
                                Toast.makeText(NewRideActivity.this, "Success", Toast.LENGTH_LONG).show();
                                txt_service.setText("UnAvailable");
                                txt_service.setBackgroundColor(ContextCompat.getColor(NewRideActivity.this, R.color.red));
                            } else if (code.equals("9")) {
                                stopService(new Intent(getBaseContext(), LocationService.class));
                                Toast.makeText(NewRideActivity.this, msg, Toast.LENGTH_SHORT).show();
                                Methods.logout(NewRideActivity.this);
                            } else if (code.equals("10")) {
                                Toast.makeText(NewRideActivity.this, msg, Toast.LENGTH_SHORT).show();
                            } else {

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
                        Toast.makeText(NewRideActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                        pDailog.dismiss();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                SharedPreferences sharedPreferences = getSharedPreferences("Details", 0);
                id = sharedPreferences.getString("id", "");
                String sessioncode = sharedPreferences.getString("sessioncode", "");

                params.put("driver_id", id);
                params.put("token_session", sessioncode.trim());
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

        RequestQueue requestQueue = Volley.newRequestQueue(NewRideActivity.this);
        int socketTimeout = 15000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjReq.setRetryPolicy(policy);

        requestQueue.add(jsonObjReq);

    }

}




