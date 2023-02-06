package androapps.bookmygaddidriver.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import android.text.TextUtils;
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
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

/**
 * Created by androapps on 6/29/17.
 */

public class NewGpsTracker implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private GoogleApiClient mGoogleApiClient;
    Context context;
    public static LatLng currentLatlang;
    int REQUEST_LOCATION_ON = 1;
    String currentPlaceId = "";

    public NewGpsTracker(Context context) {
        this.context = context;
        intiateGPSState();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        createLocationRequest();

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public void intiateGPSState() {

        if ((ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 999);
        } else {
            if (isGooglePlayServicesAvailable()) {
                buildGoogleApiClient();
            }
        }
    }

    public boolean isGooglePlayServicesAvailable() {
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int status = googleApiAvailability.isGooglePlayServicesAvailable(context);
        if (status != ConnectionResult.SUCCESS) {
            if (googleApiAvailability.isUserResolvableError(status)) {
                googleApiAvailability.getErrorDialog((Activity) context, status, 2404).show();
            }
            return false;
        }
        return true;
    }


    public synchronized void buildGoogleApiClient() {
        try {
            mGoogleApiClient = new GoogleApiClient.Builder(context)
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
                            status.startResolutionForResult((Activity) context, REQUEST_LOCATION_ON);
                        else {

                            if (GPSAvailable()) {
                                fetchLocation();
                            } else {
                                //buildAlertMessageNoGps();
                                Toast.makeText(context, "No GPS Available", Toast.LENGTH_SHORT).show();
                            }
                        }
                    } catch (IntentSender.SendIntentException e) {
                        e.printStackTrace();
                    }
                } else {
                    fetchLocation();
                }
            }
        });
    }

    public boolean GPSAvailable() {
        LocationManager loc_manager = (LocationManager)
                context.getSystemService(Context.LOCATION_SERVICE);
        List<String> str = loc_manager.getProviders(true);

        if (str.size() > 0)
            return true;
        else
            return false;
    }

    public LatLng fetchLocation() {
        if (GPSAvailable() && mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return null;
            }

            PendingResult<PlaceLikelihoodBuffer> results = Places.PlaceDetectionApi.getCurrentPlace(mGoogleApiClient, null);

            results.setResultCallback(new ResultCallback<PlaceLikelihoodBuffer>() {
                @Override
                public void onResult(@NonNull PlaceLikelihoodBuffer likelyPlaces) {

                    if (likelyPlaces != null && likelyPlaces.getCount() > 0) {
                        PlaceLikelihood placeLikelihood = likelyPlaces.get(0);

                        if (placeLikelihood != null && placeLikelihood.getPlace() != null && !TextUtils.isEmpty(placeLikelihood.getPlace().getName())) {
                            currentLatlang = placeLikelihood.getPlace().getLatLng();
                            currentPlaceId = placeLikelihood.getPlace().getId();
                            likelyPlaces.release();
                        }

                        // locationListener.receivedLocation();
                    } else {
                        //  locationListener.receivedLocation();
                    }
                }
            });
        } else {

            //   locationListener.receivedLocation();
        }

        return currentLatlang;

    }

    public double getLatitude() {
        Double latitude=0d;
        if(currentLatlang !=null) {
             latitude = currentLatlang.latitude;
        }
        return latitude;
    }
    public double getLongitude() {
        Double longitude=0d;
        if(currentLatlang !=null) {
            longitude = currentLatlang.longitude;
        }

        return longitude;
    }


}
