package androapps.bookmygaddidriver.utils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AlertDialog;

import com.google.android.gcm.GCMRegistrar;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androapps.bookmygaddidriver.activities.MainActivity;

/**
 * Created by ADMIN on 27-12-2016.
 */
public class Methods {

    static AlertDialog alertDialog = null;

    public static boolean getNetworkStatus(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    public static boolean emailValidator(String email) {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }
    public static boolean mobileValidator(String mobile)
    {
        if(mobile.length()==0||mobile.length()<10||mobile.length()>10)
        {
            return false;
        }else{
            return true;
        }
    }

    public static void showDialog(final Context context, String dialogMessage) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setTitle("Ride Details");
        alertDialogBuilder.setMessage(dialogMessage);
        alertDialogBuilder.setCancelable(false);

        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id1) {
                ((Activity)context).finish();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();


    }

    public static String getAddress(Double lat,Double lan,Context context){
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(context, Locale.getDefault());
        String fullAddress = null;

        try {
            addresses = geocoder.getFromLocation(lat, lan, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            String city = addresses.get(0).getLocality();
            fullAddress=address+", "+city;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return fullAddress;

    }

    public static String Converttodate(String inputDate) {

        DateFormat theDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date date = null;

        try {
            date = theDateFormat.parse(inputDate);
            theDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        } catch (ParseException parseException) {
            // Date is invalid. Do what you want.
        } catch (Exception exception) {
            // Generic catch. Do what you want.
        }

        theDateFormat = new SimpleDateFormat("EE, MMM dd, hh:mm a");

        return theDateFormat.format(date);
    }

    public static void logout(final Context context, String dialogMessage, DrawerLayout drawer) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
//        alertDialogBuilder.setTitle("Logout");
        alertDialogBuilder.setMessage(dialogMessage);
        alertDialogBuilder.setCancelable(false);

        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id1) {
                SharedPreferences sharedPreferences = context.getSharedPreferences("Details", 0);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.commit();
                ((Activity) context).finishAffinity();
                ((Activity) context).startActivity(new Intent(context, MainActivity.class));
            }
        });
        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id1) {
                alertDialog.dismiss();
            }
        });

        drawer.closeDrawers();
        alertDialog = alertDialogBuilder.create();
        alertDialog.show();


        }

    public static void setGcm(Context context) {
        GCMRegistrar.checkDevice(context);
        GCMRegistrar.checkManifest(context);
        GCMRegistrar.register(context, Constants.APIKeys.sender_id);
        boolean isRegister= GCMRegistrar.isRegistered(context);
    }

    public static String formatHoursAndMinutes(int totalMinutes) {
        String minutes = Integer.toString(totalMinutes % 60);
        minutes = minutes.length() == 1 ? "0" + minutes : minutes;
        return (totalMinutes / 60+"hr") + ":" + minutes+"min";
    }


    public static void logout(Context context){
        SharedPreferences address = context
                .getSharedPreferences("Details", 0);
        SharedPreferences.Editor editor = address.edit();
        editor.clear();
        editor.commit();
     //   context.stopService(new Intent(context, LocationService.class));
        Intent i = new Intent(context, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(i);
        ((Activity) context).finishAffinity();

    }


}
