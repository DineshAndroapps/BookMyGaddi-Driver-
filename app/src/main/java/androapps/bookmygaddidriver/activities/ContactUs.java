package androapps.bookmygaddidriver.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import androapps.bookmygaddidriver.R;
import androapps.bookmygaddidriver.utils.Constants;

public class ContactUs extends AppCompatActivity {
    TextView txtNumber,txtEmail,txt_title;
    ImageView img_back;
    String number,email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);

        txtNumber = (TextView) findViewById(R.id.txtNumber);
        txtEmail = (TextView) findViewById(R.id.txtEmail);
        txt_title = (TextView) findViewById(R.id.txt_title);
        img_back = (ImageView) findViewById(R.id.img_back);

        txt_title.setText("Contact Us");
        txtNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + "0" + number));
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        });
        txtEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent emailid = new Intent(Intent.ACTION_SEND);
                emailid.putExtra(Intent.EXTRA_EMAIL, new String[]{email});

                emailid.setType("message/rfc822");

                startActivity(Intent.createChooser(emailid, "Choose an Email client :"));
            }
        });
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        getdata();
    }
    public void getdata(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.Urls.CONTACT_US, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    final JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString(Constants.APIKeys.CODE).equals("1")){

                        txtNumber.setText(jsonObject.getString("call_on"));
                        txtEmail.setText(jsonObject.getString("email_on"));

                        number = jsonObject.getString("call_on");
                        email = jsonObject.getString("email_on");


                    }
                }catch (JSONException j){
                    j.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> header = new HashMap<String, String>();
                header.put("BookMyGaddi", Constants.tokenheader);
                //    return super.getHeaders();
                return header;

            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
