package androapps.bookmygaddidriver.activities;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.PlaceLikelihoodBuffer;
import com.google.android.gms.location.places.Places;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import javax.xml.transform.Source;

import androapps.bookmygaddidriver.R;
import androapps.bookmygaddidriver.interfaces.ApiResponse;
import androapps.bookmygaddidriver.models.RidesDetails;
import androapps.bookmygaddidriver.models.StopRideDetails;
import androapps.bookmygaddidriver.network.NetworkRequest;
import androapps.bookmygaddidriver.utils.Constants;
import androapps.bookmygaddidriver.utils.GPSTracker;
import androapps.bookmygaddidriver.utils.LocationBackgroundService;
import androapps.bookmygaddidriver.utils.LocationService;
import androapps.bookmygaddidriver.utils.Methods;
import androapps.bookmygaddidriver.utils.NewGpsTracker;

public class AcceptedRideDetailsActivity extends AppCompatActivity implements ApiResponse , GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    Context context = this;
    String km;


    TextView txt_name, txt_source, txt_destination, txt_date;
    RidesDetails ridesDetails;

    Intent intent;
    ImageView img_back;
    TextView txt_title, txt_start, txt_stop;
    LinearLayout lnr_call;
    RelativeLayout lnr_start;
    double longitude;
    double latitude;
    HashMap<String, String> params;
    private boolean isStarted = false;
    SharedPreferences sharedPreferences;
    String started = "0";
    private GoogleApiClient mGoogleApiClient;
    int REQUEST_LOCATION_ON = 1;

    String currentPlaceId = "";
    RelativeLayout rlt_trackRide, rlt_trackCust;
    Bitmap bitmap;


    private GPSTracker mGpsTracker;
    double lat, log;
    private ProgressDialog dialog;
    LinearLayout lnr_trackRide, lnr_trackCust;
    TextView txt_bookingId, txt_rideType;
    String bookid, type, status;
    String id;
    String source_km;
    String image;


    public static final int CAMERA_REQUEST = 1888;
    private static final int CAMERA_PHOTO = 111;
    private Uri imageToUploadUri;
    ImageView img_Capture;
    private String mCurrentPhotoPath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accepted_ride_details_track);
        dialog = new ProgressDialog(context);
        initialise();
        intiateGPSState();
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                if (txt_stop.getVisibility()==View.VISIBLE){
//
//                    Toast.makeText(AcceptedRideDetailsActivity.this, "Please stop ride to go back", Toast.LENGTH_SHORT).show();
//                }else{
                onBackPressed();


                //  }


            }


        });


        lnr_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!isStarted) {


                    started = "1";
                    SharedPreferences.Editor editor = AcceptedRideDetailsActivity.this.getSharedPreferences("Ridedetalis", 0).edit();
                    editor.putString("ridestatus", started);
                    editor.commit();
                    sharedPreferences = getSharedPreferences("Ride", 0);


                    SharedPreferences sharedPreferences = getSharedPreferences("Details", 0);
                    id = sharedPreferences.getString("id", "");
//                    source_km=sharedPreferences.getString("source_km","");
//                    image=sharedPreferences.getString("image","");

                    String sessioncode = sharedPreferences.getString("sessioncode", "");

                    getLatLong();
                    params = new HashMap<>();
                    params.put("booking_id", ridesDetails.getBooking_id());
                    params.put("source_latitude", latitude + "");
                    params.put("source_longitude", longitude + "");
                    params.put("driver_id", id);
                    params.put("token_session", sessioncode.trim());
//                    params.put("image",getBase64String(bitmap));
//                   params.put("source_km",km);
                    startDialogbox();
                    //getStartRides();
                } else {
                    started = "0";
                    SharedPreferences.Editor editor = AcceptedRideDetailsActivity.this.getSharedPreferences("Ridedetalis", 0).edit();
                    editor.putString("ridestatus", started);
                    editor.commit();
                    SharedPreferences sharedPreferences1 = getSharedPreferences("location", 0);
                    SharedPreferences.Editor editor1 = sharedPreferences1.edit();
                    editor1.putBoolean("isservice", false);
                    editor1.commit();
                    stopService(new Intent(AcceptedRideDetailsActivity.this, LocationBackgroundService.class));

                    SharedPreferences sharedPreferences = getSharedPreferences("Details", 0);
                    id = sharedPreferences.getString("id", "");
                    String sessioncode = sharedPreferences.getString("sessioncode", "");
                    SharedPreferences location = getSharedPreferences("location", 0);
                    String dist = location.getString("dist", "0");
                    Log.d("click1", "dist" + dist);
                    getLatLong();
                    params = new HashMap<>();
                    params.put("booking_id", ridesDetails.getBooking_id());
                    params.put("destinaion_latitude", latitude + "");
                    params.put("destinaion_longitude", longitude + "");
                    params.put("driver_id", id);
                    params.put("dist", dist);
                    params.put("token_session", sessioncode.trim());
                    stoptDialogbox();
                    //  stopRide();
                }
            }

            private String getBase64String(Bitmap bitmap) {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();

                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

                byte[] imageBytes = baos.toByteArray();

                String base64String = Base64.encodeToString(imageBytes, Base64.NO_WRAP);

                return base64String;
            }
        });


        lnr_trackRide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent2 = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse("http://maps.google.com/maps?saddr=" + ridesDetails.getSource_latitude() + "," + ridesDetails.getSource_longitude()
                                + "&daddr=" + ridesDetails.getDestinaion_latitude() + "," + ridesDetails.getDestinaion_longitude()));
                startActivity(intent2);

            }
        });


        lnr_trackCust.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent2 = new Intent(android.content.Intent.ACTION_VIEW,
//                        Uri.parse("http://maps.google.com/maps?saddr=" + lat+ "," + log
//                                + "&daddr=" + ridesDetails.getSource_latitude() + "," + ridesDetails.getSource_longitude()));


                        Uri.parse("http://maps.google.com/maps?saddr=" + ridesDetails.getSource() +
                                "&daddr=" + ridesDetails.getDestination()));


                startActivity(intent2);

            }
        });


    }

    private void getStartRides() {
        NetworkRequest.sendStringRequest(AcceptedRideDetailsActivity.this, Constants.Urls.START_RIDE, params, this, Constants.APICodes.START_RIDE);
    }

    private void stopRide() {
        NetworkRequest.sendStringRequest(AcceptedRideDetailsActivity.this, Constants.Urls.STOP_RIDE, params, this, Constants.APICodes.STOP_RIDE);
    }

    void getLatLong() {

        GPSTracker gpsTracker;
        gpsTracker = new GPSTracker(AcceptedRideDetailsActivity.this);
        LocationManager locationManager;
        boolean isGPSEnabled = false;
        boolean isNetworkEnabled = false;
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (gpsTracker != null) {
            if (isGPSEnabled || isNetworkEnabled) {
                latitude = gpsTracker.getLatitude();
                longitude = gpsTracker.getLongitude();
            } else {
                gpsTracker.showSettingsAlert();
            }

        }


    }


    private void initialise() {


        intent = getIntent();
        String action = intent.getAction();
        if (action.equals("Service")) {
            Gson gson = new Gson();
            sharedPreferences = getSharedPreferences("Ride", 0);
            String json = sharedPreferences.getString("Ride", "");
            ridesDetails = gson.fromJson(json, RidesDetails.class);
        } else {
            ridesDetails = (RidesDetails) intent.getSerializableExtra("Ride");
        }


        txt_title = (TextView) findViewById(R.id.txt_title);
        txt_start = (TextView) findViewById(R.id.txt_start);
        txt_stop = (TextView) findViewById(R.id.txt_stop);
        txt_name = (TextView) findViewById(R.id.txt_name);
        txt_source = (TextView) findViewById(R.id.txt_source);
        txt_destination = (TextView) findViewById(R.id.txt_destination);
        txt_date = (TextView) findViewById(R.id.txt_date);

        txt_bookingId = (TextView) findViewById(R.id.txt_bookingId);
        txt_rideType = (TextView) findViewById(R.id.txt_bookingType);


        img_back = (ImageView) findViewById(R.id.img_back);
        lnr_call = (LinearLayout) findViewById(R.id.lnr_call);
        lnr_start = (RelativeLayout) findViewById(R.id.lnr_start);

        lnr_trackRide = (LinearLayout) findViewById(R.id.lnr_trackRide);
        lnr_trackCust = (LinearLayout) findViewById(R.id.lnr_trackCust);
//        rlt_trackRide.setVisibility(View.GONE);

        txt_title.setText("Accepted Ride Details");

        txt_bookingId.setText("Booking Id : " + ridesDetails.getBooking_id());
        txt_rideType.setText("Ride Type: " + ridesDetails.getVehicle_category());

        txt_name.setText(ridesDetails.getName());
        txt_source.setText(ridesDetails.getSource());
        txt_destination.setText(ridesDetails.getDestination());


//        status = getIntent().getExtras().getString("status");
        String status_action = intent.getAction();

        if (status_action.equals("status_started")) {


//                Gson gson = new Gson();
//                sharedPreferences=getSharedPreferences("Ride",0);
//                String json = sharedPreferences.getString("Ride", "");
//                ridesDetails = gson.fromJson(json, RidesDetails.class);

            isStarted =
                    true;
            txt_start.setVisibility(View.GONE);
            txt_stop.setVisibility(View.VISIBLE);


        }


//        if((ridesDetails.getVehicle_category()!="1") || (ridesDetails.getVehicle_category()!="2") ||(ridesDetails.getVehicle_category()!="3")){
//            txt_rideType.setText("Ride Type : "+"Short Pickup/Drop");
//        }
//        else if((ridesDetails.getVehicle_category()!="2") || (ridesDetails.getVehicle_category()!="3") ||(ridesDetails.getVehicle_category()!="4")){
//
//            txt_rideType.setText("Ride Type : "+"Outstaion");
//
//        }else if((ridesDetails.getVehicle_category()!="4") || (ridesDetails.getVehicle_category()!="1") ||(ridesDetails.getVehicle_category()!="3")){
//
//            txt_rideType.setText("Ride Type : "+"Local");
//
//        }else if((ridesDetails.getVehicle_category()!="1") || (ridesDetails.getVehicle_category()!="2") ||(ridesDetails.getVehicle_category()!="4")){
//
//            txt_rideType.setText("Ride Type : "+"Airport");
//
//        }


        if (ridesDetails.getVehicle_category() != null && ridesDetails.getVehicle_category().equals("1")) {

            txt_rideType.setText("Ride Type : " + "Outstation");

        } else if (ridesDetails.getVehicle_category() != null && ridesDetails.getVehicle_category().equals("2")) {

            txt_rideType.setText("Ride Type : " + "Local");

        } else if (ridesDetails.getVehicle_category() != null && ridesDetails.getVehicle_category().equals("3")) {

            txt_rideType.setText("Ride Type : " + "Airport");

        } else if (ridesDetails.getVehicle_category() != null && ridesDetails.getVehicle_category().equals("4")) {

            txt_rideType.setText("Ride Type : " + "Short Pickup/Drop");

        } else {

        }


        txt_date.setText(Methods.Converttodate(ridesDetails.getAdded_date()));

//        txt_start.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startDialogbox();
//            }
//        });


        if (action.equals("Service")) {
            Intent intent2 = new Intent(android.content.Intent.ACTION_VIEW,
                    Uri.parse("http://maps.google.com/maps?saddr=" + ridesDetails.getSource_latitude() + "," + ridesDetails.getSource_longitude()
                            + "&daddr=" + ridesDetails.getDestinaion_latitude() + "," + ridesDetails.getDestinaion_longitude()));
            startActivity(intent2);
            isStarted = true;
            txt_start.setVisibility(View.GONE);

//            rlt_trackRide.setVisibility(View.VISIBLE);
            txt_stop.setVisibility(View.VISIBLE);
        } else {
        }

    }

    @Override
    public void NetworkRequestCompleted(int apiCode, JSONObject response, ProgressDialog progressDialog) {
        Gson gson = new GsonBuilder().create();

        switch (apiCode) {

            case Constants.APICodes.START_RIDE: {

                try {

                    if (response.getString(Constants.APIKeys.CODE).equals("1")) {
                        SharedPreferences sharedPreferences1 = getSharedPreferences("location", 0);
                        SharedPreferences.Editor editor1 = sharedPreferences1.edit();
                        editor1.putString("dist", "0");

                        editor1.putBoolean("isservice", true);
                        editor1.commit();
//                        Intent intent = new Intent(AcceptedRideDetailsActivity.this, LocationBackgroundService.class);
//                        startService(intent);

                        Gson gson1 = new Gson();
                        String json = gson1.toJson(ridesDetails);

                        SharedPreferences sharedPreferences = getSharedPreferences("Ride", 0);
                        SharedPreferences.Editor editor = sharedPreferences.edit();

                        editor.putString("Ride", json);
                        editor.putString("Status", "1");
                        editor.commit();

//                        Intent intent=new Intent(getBaseContext(), LocationService.class);
//                        startService(intent);

                        Toast.makeText(AcceptedRideDetailsActivity.this, "Ride Started", Toast.LENGTH_SHORT).show();
//                        Intent intent2 = new Intent(android.content.Intent.ACTION_VIEW,
//                                Uri.parse("http://maps.google.com/maps?saddr=" + ridesDetails.getSource_latitude() + "," + ridesDetails.getSource_longitude()
//                                        + "&daddr=" + ridesDetails.getDestinaion_latitude() + "," + ridesDetails.getDestinaion_longitude()));
//                        startActivity(intent2);

                        isStarted = true;
                        txt_start.setVisibility(View.GONE);
                        txt_stop.setVisibility(View.VISIBLE);
//                        rlt_trackRide.setVisibility(View.VISIBLE);

                    } else if (response.getString(Constants.APIKeys.CODE).equals("9")) {
                        stopService(new Intent(getBaseContext(), LocationService.class));
                        Toast.makeText(AcceptedRideDetailsActivity.this, response.getString(Constants.APIKeys.MESSAGE), Toast.LENGTH_SHORT).show();
                        Methods.logout(AcceptedRideDetailsActivity.this);
                    } else if (response.getString(Constants.APIKeys.CODE).equals("10")) {

                        Toast.makeText(AcceptedRideDetailsActivity.this, response.getString(Constants.APIKeys.MESSAGE), Toast.LENGTH_SHORT).show();
                    } else if (response.getString(Constants.APIKeys.CODE).equals("2")) {
                        Toast.makeText(AcceptedRideDetailsActivity.this, response.getString(Constants.APIKeys.MESSAGE), Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }
            case Constants.APICodes.STOP_RIDE: {
                try {

                    ArrayList<StopRideDetails> stopRideDetailses;
                    if (response.getString(Constants.APIKeys.CODE).equals("1")) {

                        stopRideDetailses = gson.fromJson(response.getJSONArray("list").toString(), new TypeToken<List<StopRideDetails>>() {
                        }.getType());

                     /*   SharedPreferences sharedPreferences= getSharedPreferences("location",0);
                     String dist=   sharedPreferences.getString("dist","");*/


                        stopService(new Intent(AcceptedRideDetailsActivity.this, LocationService.class));
                        stoptDialogbox();

                        // stopService(new Intent(getBaseContext(), LocationService.class));

                        txt_start.setVisibility(View.VISIBLE);
                        txt_stop.setVisibility(View.GONE);
                        isStarted = false;
//                        Methods.showDialog(AcceptedRideDetailsActivity.this
//                                , "Distance : " + stopRideDetailses.get(0).getDistance() + " K.M.\nFare : " + stopRideDetailses.get(0).getFare() + " Rs.");

                        //showDialog(stopRideDetailses.get(0));
                    } else if (response.getString(Constants.APIKeys.CODE).equals("9")) {
                        stopService(new Intent(getBaseContext(), LocationService.class));
                        Toast.makeText(AcceptedRideDetailsActivity.this, response.getString(Constants.APIKeys.MESSAGE), Toast.LENGTH_SHORT).show();
                        Methods.logout(AcceptedRideDetailsActivity.this);
                    } else if (response.getString(Constants.APIKeys.CODE).equals("10")) {
                        Toast.makeText(AcceptedRideDetailsActivity.this, response.getString(Constants.APIKeys.MESSAGE), Toast.LENGTH_SHORT).show();
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }
        }
        progressDialog.dismiss();
    }

//

    @Override
    public void NetworkRequestCompleted(int apiCode, JSONObject response) {
    }

    void showDialog(StopRideDetails stopRideDetails) {
        final Dialog dialog = new Dialog(AcceptedRideDetailsActivity.this);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View dislogView = inflater.inflate(R.layout.dialog_ride_details, null);

        TextView txt_distance, txt_fare;
        Button btn_ok;

        txt_distance = (TextView) dislogView.findViewById(R.id.txt_distance);
        txt_fare = (TextView) dislogView.findViewById(R.id.txt_fare);
        btn_ok = (Button) dislogView.findViewById(R.id.btn_ok);

        txt_distance.setText(stopRideDetails.getDistance() + " km");
        // txt_distance.setText(new DecimalFormat("##.##").format(stopRideDetails.getDistance()+" km"));
        txt_fare.setText(stopRideDetails.getFare() + " Rs.");

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(AcceptedRideDetailsActivity.this, NewRideActivity.class);
                startActivity(i);
                finish();
            }
        });

        dialog.setContentView(dislogView);
        dialog.setCancelable(false);
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.show();
        stoptDialogbox();
    }


    @Override
    protected void onResume() {
        super.onResume();


        Geocoder geocoder;
        List<Address> addresses;
        Address address = null;
        mGpsTracker = new GPSTracker(this);
        geocoder = new Geocoder(AcceptedRideDetailsActivity.this, Locale.getDefault());
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


//        status = getIntent().getExtras().getString("status");
//
//        if(status.equals("2")){
//
//            txt_start.setVisibility(View.GONE);
//            txt_stop.setVisibility(View.VISIBLE);
//
//            txt_stop.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//
//                    stopRide();
//
//                }
//            });

        lnr_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:" + ridesDetails.getMobile()));
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {

//        if (txt_stop.getVisibility()==View.VISIBLE){
//
//            Toast.makeText(AcceptedRideDetailsActivity.this, "Please stop ride to go back", Toast.LENGTH_SHORT).show();
//        }else{

        finish();

        // }
    }

    public void intiateGPSState() {

        if ((ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 999);
        } else {
            if (isGooglePlayServicesAvailable()) {
                buildGoogleApiClient();
            }
        }
    }

    public boolean isGooglePlayServicesAvailable() {
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int status = googleApiAvailability.isGooglePlayServicesAvailable(this);
        if (status != ConnectionResult.SUCCESS) {
            if (googleApiAvailability.isUserResolvableError(status)) {
                googleApiAvailability.getErrorDialog(this, status, 2404).show();
            }
            return false;
        }
        return true;
    }


    public synchronized void buildGoogleApiClient() {
        try {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .addApi(Places.PLACE_DETECTION_API)
                    .addApi(Places.GEO_DATA_API)
                    .build();

            mGoogleApiClient.connect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void onConnected(@Nullable Bundle bundle) {
/*
        if (isCurrentLocationRequired) {
            createLocationRequest();
        } else {
            fetchLocation();
        }*/
        createLocationRequest();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }


    public void createLocationRequest() {
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(mLocationRequest);
        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());
        builder.setAlwaysShow(true);

        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                if (status.getStatusCode() == LocationSettingsStatusCodes.RESOLUTION_REQUIRED) {
                    try {

                        if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.KITKAT)
                            status.startResolutionForResult(AcceptedRideDetailsActivity.this, REQUEST_LOCATION_ON);
                        else {

                            if (GPSAvailable()) {
//                                fetchLocation();
                            } else {
                                //buildAlertMessageNoGps();
                                Toast.makeText(AcceptedRideDetailsActivity.this, "No GPS Available", Toast.LENGTH_SHORT).show();
                            }
                        }
                    } catch (IntentSender.SendIntentException e) {
                        e.printStackTrace();
                    }
                } else {
//                    fetchLocation();
                }
            }
        });
    }

    public boolean GPSAvailable() {
        LocationManager loc_manager = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);
        List<String> str = loc_manager.getProviders(true);

        if (str.size() > 0)
            return true;
        else
            return false;
    }

    private void fetchLocation() {
        dialog.show();
        if (GPSAvailable() && mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }

            PendingResult<PlaceLikelihoodBuffer> results = Places.PlaceDetectionApi.getCurrentPlace(mGoogleApiClient, null);

            results.setResultCallback(new ResultCallback<PlaceLikelihoodBuffer>() {
                @Override
                public void onResult(PlaceLikelihoodBuffer likelyPlaces) {
                    dialog.dismiss();

                    if (likelyPlaces != null && likelyPlaces.getCount() > 0) {
                        PlaceLikelihood placeLikelihood = likelyPlaces.get(0);

                        if (placeLikelihood != null && placeLikelihood.getPlace() != null && !TextUtils.isEmpty(placeLikelihood.getPlace().getName())) {
                            NewGpsTracker.currentLatlang = placeLikelihood.getPlace().getLatLng();

                            currentPlaceId = placeLikelihood.getPlace().getId();
                            likelyPlaces.release();
                            //startSplashing();
                        }

                        // locationListener.receivedLocation();
                    } else {
                        Toast.makeText(context, "Can not get your Location", Toast.LENGTH_SHORT).show();

                    }
                }
            });
        } else {

            //   locationListener.receivedLocation();
        }

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    private void startDialogbox() {
        Button btn_submit, btn_Camera;
        EditText edit_startKm;
        final Dialog dialog = new Dialog(AcceptedRideDetailsActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setTitle("");

        dialog.setContentView(R.layout.startdialogbox);
        dialog.show();

        btn_submit = dialog.findViewById(R.id.btn_submit);

        btn_Camera = dialog.findViewById(R.id.btn_Camera);
        edit_startKm = dialog.findViewById(R.id.edit_startKm);

        img_Capture = dialog.findViewById(R.id.img_Capture);

        //String km = edit_startKm.getText().toString();


        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getStartRides();
            }
        });

        btn_Camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }


        });
    }

    @Override

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            bitmap = (Bitmap) data.getExtras().get("data");

            img_Capture.setImageBitmap(bitmap);
        }
    }


    private void stoptDialogbox() {
        Button btnstop, btn_Camera;
        EditText edit_stopKm;
        final Dialog dialog = new Dialog(AcceptedRideDetailsActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setTitle("");

        dialog.setContentView(R.layout.stopdialogbox);
        dialog.show();

        btnstop = dialog.findViewById(R.id.btn_submit);

        btn_Camera = dialog.findViewById(R.id.btn_Camera);
        edit_stopKm = dialog.findViewById(R.id.edit_stopKm);

        img_Capture = dialog.findViewById(R.id.img_Capture);

        String km = edit_stopKm.getText().toString();


        btnstop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopRide();

            }

        });
        btn_Camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }


        });
    }






}













