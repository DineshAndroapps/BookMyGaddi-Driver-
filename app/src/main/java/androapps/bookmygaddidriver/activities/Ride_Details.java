package androapps.bookmygaddidriver.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Build;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import androapps.bookmygaddidriver.R;
import androapps.bookmygaddidriver.models.ItemListModel;
import androapps.bookmygaddidriver.utils.Constants;
import androapps.bookmygaddidriver.utils.LocationService;
import androapps.bookmygaddidriver.utils.Methods;

public class Ride_Details extends AppCompatActivity {

    String bookingid;

    TextView txtbill,txtcartype,txtkm,txttimes,txtsourceadd,txtdestinationadd,txtsetdate,txtamount;
    TextView txtfarerate,txtdiscount,txttotal;
    ProgressDialog pDailog;
    RelativeLayout rlt_amount;
    LinearLayout line4,lnr_driver;
    ImageView img_back;
    ImageView imgtrack;
    TextView txtridetype,txt_title;
    String driverlat,driverlog;
    String sourcelat,sourcelog,destinationlat,destinationlog;
    TextView discount;
    RelativeLayout rlt_tax;
    TextView txttax1,txttax2,txttax3,txttaxamount1,txttaxamount2,txttaxamount3,txtamountroundoff;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride__details);

          Bindid();

        Intent mintent=getIntent();
        bookingid=mintent.getStringExtra("bookingid");
        txt_title.setText("Ride Details");

        Typeface type = Typeface.createFromAsset(Ride_Details.this.getAssets(),
                "HelveticaNeueLTStd-Md.otf");

        txtcartype.setTypeface(type);
        txtsourceadd.setTypeface(type);
        txtdestinationadd.setTypeface(type);
        txtridetype.setTypeface(type);

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        Getdetalis();

    }

    public void Bindid(){
        txtbill=(TextView)findViewById(R.id.txtbill);
        txtcartype=(TextView)findViewById(R.id.txtcartype);
        txtkm=(TextView)findViewById(R.id.txtkm);
        txttimes=(TextView)findViewById(R.id.txttimes);
        txtsourceadd=(TextView)findViewById(R.id.txtsourceadd);
        txtdestinationadd=(TextView)findViewById(R.id.txtdestinationadd);
        txtsetdate=(TextView)findViewById(R.id.txtsetdate);
        txtamount=(TextView)findViewById(R.id.txtamount);
        txtridetype=(TextView)findViewById(R.id.txtridetype);
        discount=(TextView)findViewById(R.id.discount);

        txttax1=(TextView)findViewById(R.id.txttax1);
        txttax2=(TextView)findViewById(R.id.txttax2);
        txttax3=(TextView)findViewById(R.id.txttax3);
        txttaxamount1=(TextView)findViewById(R.id.txttaxamount1);
        txttaxamount2=(TextView)findViewById(R.id.txttaxamount2);
        txttaxamount3=(TextView)findViewById(R.id.txttaxamount3);
        txtamountroundoff=(TextView)findViewById(R.id.txtamountroundoff);

        txtfarerate=(TextView)findViewById(R.id.txtfarerate);
        txtdiscount=(TextView)findViewById(R.id.txtdiscount);
        txttotal=(TextView)findViewById(R.id.txttotal);
        rlt_tax=(RelativeLayout) findViewById(R.id.rlt_tax);

        rlt_amount=(RelativeLayout) findViewById(R.id.rlt_amount);
        line4=(LinearLayout) findViewById(R.id.line4);
        img_back=(ImageView) findViewById(R.id.img_back);
        txt_title=(TextView) findViewById(R.id.txt_title);

    }


    public void Getdetalis(){
        pDailog = new ProgressDialog(Ride_Details.this);
        pDailog.setMessage("Loading");
        pDailog.setCancelable(false);
        pDailog.show();


        String tag_json_obj = "json_obj_req";
        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                Constants.Urls.Booking_details,
                new Response.Listener<String>() {

                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onResponse(String response) {
                        pDailog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String code = jsonObject.getString("code");
                            String msg = jsonObject.getString("message");



                            if (code.equals("1")) {


                                JSONArray jsonArray = jsonObject.getJSONArray("list");
                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject jbjct = jsonArray.getJSONObject(i);
                                    ItemListModel showDeviceMode = new ItemListModel();
                                    showDeviceMode.sourcelat = jbjct.getString("source_latitude");
                                    showDeviceMode.sourcelog = jbjct.getString("source_longitude");
                                    showDeviceMode.destinationlat = jbjct.getString("destinaion_latitude");
                                    showDeviceMode.destinationlog = jbjct.getString("destinaion_longitude");
                                    showDeviceMode.booking_date = jbjct.getString("booking_date");
                                    showDeviceMode.drivername = jbjct.getString("name");
                                    showDeviceMode.vehicle_type = jbjct.getString("vehicle_type");
                                    showDeviceMode.sourcepoint = jbjct.getString("source");
                                    showDeviceMode.destinationpoint = jbjct.getString("destination");
                                    showDeviceMode.distance = jbjct.getString("distance");
                                    showDeviceMode.traveltime = jbjct.getString("total_time");
                                    showDeviceMode.fare = jbjct.getString("fare");
                                    showDeviceMode.ridetype = jbjct.getString("vehicle_category");
                                    showDeviceMode.ridebill = jbjct.getString("bill_number");
                                    showDeviceMode.discountamount = jbjct.getString("discount");
                                    showDeviceMode.total_fare = jbjct.getString("total_fare");
                                    showDeviceMode.offer_code = jbjct.getString("offer_code");
                                    showDeviceMode.taxname1 = jbjct.getString("tax1");
                                    showDeviceMode.taxname2 = jbjct.getString("tax2");
                                    showDeviceMode.taxname3 = jbjct.getString("tax3");
                                    showDeviceMode.taxamount1 = jbjct.getString("tax1_amount");
                                    showDeviceMode.taxamount2 = jbjct.getString("tax2_amount");
                                    showDeviceMode.taxamount3 = jbjct.getString("tax3_amount");
                                    showDeviceMode.roundof = jbjct.getString("round_off");
                                    sourcelat=showDeviceMode.sourcelat;
                                    sourcelog=showDeviceMode.sourcelog;
                                    destinationlat=showDeviceMode.destinationlat;
                                    destinationlog=showDeviceMode.destinationlog;

                                    txtsetdate.setText(Methods.Converttodate(showDeviceMode.booking_date));
                                    txtcartype.setText(showDeviceMode.vehicle_type);
                                    txtsourceadd.setText(showDeviceMode.sourcepoint);
                                    txtdestinationadd.setText(showDeviceMode.destinationpoint);

                                    txttimes.setText(Methods.formatHoursAndMinutes(Integer.parseInt(showDeviceMode.traveltime)));
                                    txtkm.setText(showDeviceMode.distance+" Km");

                                    txtfarerate.setText("₹ " +showDeviceMode.fare);
                                    txttotal.setText("₹ " +showDeviceMode.total_fare);

                                    txtdiscount.setText("₹ " +showDeviceMode.discountamount);
                                    txtbill.setText(showDeviceMode.ridebill);

                                    txtamount.setText("₹ " +showDeviceMode.total_fare);
                                    txtamountroundoff.setText("₹ " +showDeviceMode.roundof);

                                    if(!jbjct.getString("tax1").equals("")){
                                        rlt_tax.setVisibility(View.VISIBLE);
                                        txttax1.setText(jbjct.getString("tax1"));
                                        txttax2.setText(jbjct.getString("tax2"));
                                        txttax3.setText(jbjct.getString("tax3"));
                                        txttaxamount1.setText(("₹ "+showDeviceMode.taxamount1));
                                        txttaxamount2.setText(("₹ "+ showDeviceMode.taxamount2));
                                        txttaxamount3.setText(("₹ "+ showDeviceMode.taxamount3));

                                    }else {
                                        rlt_tax.setVisibility(View.GONE);
                                    }


                                    if(showDeviceMode.offer_code.equals("null")){
                                        discount.setText("Discount"+"("+"No offer"+")");
                                    }else {
                                        discount.setText("Discount"+"("+showDeviceMode.offer_code+")");
                                    }

                                    if(showDeviceMode.discountamount.equals("null")){
                                        txtdiscount.setText("₹ " +"0");
                                    }
                                    else {

                                        txtdiscount.setText("₹ " + showDeviceMode.discountamount);
                                    }

                                    if(showDeviceMode.ridetype.equals("1")){
                                        txtridetype.setText("Outstation");
                                    }
                                    else if(showDeviceMode.ridetype.equals("2")){
                                        txtridetype.setText("Local");
                                    }
                                    else if(showDeviceMode.ridetype.equals("3")){
                                        txtridetype.setText("Airport");
                                    }
                                    else if(showDeviceMode.ridetype.equals("4")){
                                        txtridetype.setText("Short Pickup/Drop");
                                    }

                                }

                            }


                            else if(code.equals("9")){
                                stopService(new Intent(getBaseContext(), LocationService.class));
                                Toast.makeText(Ride_Details.this, msg, Toast.LENGTH_SHORT).show();
                                Methods.logout(Ride_Details.this);
                            }

                            else if(code.equals("10")){
                                Toast.makeText(Ride_Details.this, msg, Toast.LENGTH_SHORT).show();
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

                Toast.makeText(Ride_Details.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                SharedPreferences sharedPreferences=getSharedPreferences("Details", 0);
               String id = sharedPreferences.getString("id","");
                String sessioncode=sharedPreferences.getString("sessioncode","");


                params.put("booking_id",bookingid.trim());
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
        RequestQueue requestQueue = Volley.newRequestQueue(Ride_Details.this);
        int socketTimeout = 15000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjReq.setRetryPolicy(policy);

        requestQueue.add(jsonObjReq);

    }

}
