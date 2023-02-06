package androapps.bookmygaddidriver.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;

import java.util.HashMap;

import androapps.bookmygaddidriver.R;
import androapps.bookmygaddidriver.interfaces.ApiResponse;
import androapps.bookmygaddidriver.network.NetworkRequest;
import androapps.bookmygaddidriver.utils.Constants;
import androapps.bookmygaddidriver.utils.Methods;

public class ForgotPasswordActivity extends AppCompatActivity implements ApiResponse {

    EditText edt_mobile;
    Button btn_submit;
    String mobile;
    HashMap<String,String> params;
    RelativeLayout rel_main;
    TextView txt_title;
    ImageView img_back;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        initialise();

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validation()) {
                    checkConnectionAndProceed();
                }
            }
        });

        img_back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                onBackPressed();
            }
        });
    }

    private void initialise(){
        edt_mobile= (EditText) findViewById(R.id.edt_mobile);
        btn_submit= (Button) findViewById(R.id.btn_submit);
        rel_main= (RelativeLayout) findViewById(R.id.rel_main);
        txt_title= (TextView) findViewById(R.id.txt_title);
        img_back= (ImageView) findViewById(R.id.img_back);
        txt_title.setText("Forgot Password");
        txt_title.setVisibility(View.GONE);
    }

    private boolean validation(){
        mobile=edt_mobile.getText().toString().trim();
        if(mobile.length()==0){
            edt_mobile.setError("Enter Mobile");
            edt_mobile.requestFocus();
            return false;
        }else if(mobile.length()<10){
            edt_mobile.setError("Enter Proper Mobile");
            edt_mobile.requestFocus();
            return false;
        }else{
            params=new HashMap<>();
            params.put("mobile",mobile);
            return true;
        }
    }
    private void checkConnectionAndProceed(){
        if (Methods.getNetworkStatus(ForgotPasswordActivity.this)) {
            sendMobile();
        }
        else {
            Snackbar bar = Snackbar.make(rel_main, "Please check internet connection", Snackbar.LENGTH_LONG);
            bar.show();
        }
    }

    private void sendMobile(){
        NetworkRequest.sendStringRequest(ForgotPasswordActivity.this, Constants.Urls.MOBILE_VERIFICATION, params, this, Constants.APICodes.MOBILE_VERIFICATION);
    }

    @Override
    public void NetworkRequestCompleted(int apiCode, JSONObject response, ProgressDialog progressDialog){
        Gson gson = new GsonBuilder().create();

        switch (apiCode){
            case Constants.APICodes.MOBILE_VERIFICATION: {
                try {
                    if (response.getString(Constants.APIKeys.CODE).equals("1")){

                        Intent intent=new Intent(ForgotPasswordActivity.this,VerificationActivity.class);
                        intent.putExtra("code",response.getInt("veri_code"));
                        intent.putExtra("mobile",mobile);
                        startActivity(intent);
                        finish();
                    }
                    else if(response.getString(Constants.APIKeys.CODE).equals("10")) {
                        response.getString(Constants.APIKeys.MESSAGE);
                    }

                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
        progressDialog.dismiss();
    }

    @Override
    public void NetworkRequestCompleted(int apiCode, JSONObject response) {

    }
}
