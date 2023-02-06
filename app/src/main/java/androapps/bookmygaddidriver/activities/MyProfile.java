package androapps.bookmygaddidriver.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import androapps.bookmygaddidriver.R;
import androapps.bookmygaddidriver.interfaces.ApiResponse;
import androapps.bookmygaddidriver.network.NetworkRequest;
import androapps.bookmygaddidriver.utils.Constants;
import androapps.bookmygaddidriver.utils.LocationService;
import androapps.bookmygaddidriver.utils.Methods;


public class MyProfile extends AppCompatActivity implements ApiResponse{
    EditText edtusername,edtmobile,edtuseremail,edtAddress;
    String id;
    TextView txt_title;
    ImageView img_back;
    HashMap<String,String> params;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        txt_title = (TextView) findViewById(R.id.txt_title);
        img_back = (ImageView) findViewById(R.id.img_back);
        edtusername = (EditText) findViewById(R.id.edtusername);
        edtmobile = (EditText) findViewById(R.id.edtmobile);
        edtuseremail = (EditText) findViewById(R.id.edtuseremail);
        edtAddress = (EditText) findViewById(R.id.edtAddress);

        params = new HashMap<String, String>();

        txt_title.setText("My Profile");

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        setdata();
    }
    public void setdata(){
        SharedPreferences sharedPreferences=getSharedPreferences("Details", 0);
        id = sharedPreferences.getString("id","");
        String sessioncode=sharedPreferences.getString("sessioncode","");
        params.put("driver_id",id);
        params.put("token_session", sessioncode.trim());


        NetworkRequest.sendStringRequest(MyProfile.this, Constants.Urls.MY_PROFILE, params, this, Constants.APICodes.MY_PROFILE);
    }

    @Override
    public void NetworkRequestCompleted(int apiCode, JSONObject response, ProgressDialog progressDialog) {

        Gson gson = new GsonBuilder().create();

        switch (apiCode){
            case Constants.APICodes.MY_PROFILE: {
                try{
                    if (response.getString(Constants.APIKeys.CODE).equals("1")){

                        JSONArray jsonArray = response.getJSONArray("driver_details");
                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                        edtusername.setText(jsonObject.getString("name"));
                        edtAddress.setText(jsonObject.getString("address"));
                        edtmobile.setText(jsonObject.getString("contact_no"));
                        edtuseremail.setText(jsonObject.getString("email"));

                    }

                    else if(response.getString(Constants.APIKeys.CODE).equals("9")){
                        stopService(new Intent(getBaseContext(), LocationService.class));
                        Toast.makeText(MyProfile.this, response.getString(Constants.APIKeys.MESSAGE), Toast.LENGTH_SHORT).show();
                        Methods.logout(MyProfile.this);
                    }


                    else if(response.getString(Constants.APIKeys.CODE).equals("10")) {
                        Toast.makeText(MyProfile.this,response.getString(Constants.APIKeys.MESSAGE) , Toast.LENGTH_SHORT).show();
                    }

                }catch (JSONException j){
                    j.printStackTrace();
                }
            }
        }
        progressDialog.dismiss();

    }

    @Override
    public void NetworkRequestCompleted(int apiCode, JSONObject response) {

    }
}
