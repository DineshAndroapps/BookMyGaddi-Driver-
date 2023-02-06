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
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;

import java.util.HashMap;

import androapps.bookmygaddidriver.R;
import androapps.bookmygaddidriver.interfaces.ApiResponse;
import androapps.bookmygaddidriver.network.NetworkRequest;
import androapps.bookmygaddidriver.utils.Constants;
import androapps.bookmygaddidriver.utils.Methods;

public class ResetPasswordActivity extends AppCompatActivity implements ApiResponse {

    EditText edt_password,edt_cpass;
    Button btn_reset;
    RelativeLayout rel_main;
    HashMap<String,String> params;
    String password,cpassword,mobile;
    Intent intent;
    TextView txt_title;
    ImageView img_back;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        initialise();

        btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validation())
                    checkConnectionAndProceed();
            }
        });
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               onBackPressed();
            }
        });
    }

    private void initialise(){
        edt_password= (EditText) findViewById(R.id.edt_password);
        edt_cpass= (EditText) findViewById(R.id.edt_cpass);
        btn_reset= (Button) findViewById(R.id.btn_reset);
        intent=getIntent();
        mobile=intent.getStringExtra("mobile");
        txt_title= (TextView) findViewById(R.id.txt_title);
        txt_title.setText("Reset Password");
        img_back = (ImageView) findViewById(R.id.img_back);
        txt_title.setVisibility(View.GONE);
    }

    boolean validation()
    {
        password=edt_password.getText().toString().trim();
        cpassword=edt_cpass.getText().toString().trim();
        if(password.length()==0){
            edt_password.setText("Enter Password");
            edt_password.requestFocus();
            return false;
        }else if(cpassword.length()==0){
            edt_cpass.setText("Enter Password");
            edt_cpass.requestFocus();
            return false;
        }else {
            params=new HashMap<>();
            params.put("mobile",mobile);
            params.put("password",cpassword);
            return true;
        }
    }

    private void checkConnectionAndProceed(){
        if (Methods.getNetworkStatus(ResetPasswordActivity.this)){
            resetPassword();
        }
        else {
            Snackbar bar = Snackbar.make(rel_main, "Please check internet connection", Snackbar.LENGTH_LONG);
            bar.show();
        }
    }

    private void resetPassword(){

        NetworkRequest.sendStringRequest(ResetPasswordActivity.this, Constants.Urls.FORGOT_PASSWORD, params, this,
                Constants.APICodes.FORGOT_PASSWORD);

    }


    @Override
    public void NetworkRequestCompleted(int apiCode, JSONObject response, ProgressDialog progressDialog) {

        Gson gson = new GsonBuilder().create();

        switch (apiCode) {
            case Constants.APICodes.FORGOT_PASSWORD: {
                try {
                    if (response.getString(Constants.APIKeys.CODE).equals("1")) {
                        Toast.makeText(ResetPasswordActivity.this, "Password Reset successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    else if(response.getString(Constants.APIKeys.CODE).equals("10")) {
                        Toast.makeText(ResetPasswordActivity.this,response.getString(Constants.APIKeys.MESSAGE) , Toast.LENGTH_SHORT).show();
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
}
