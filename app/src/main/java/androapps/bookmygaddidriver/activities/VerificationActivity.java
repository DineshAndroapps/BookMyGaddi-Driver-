package androapps.bookmygaddidriver.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
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

public class VerificationActivity extends AppCompatActivity implements ApiResponse {

    EditText edt_email;
    TextView txt_resend,txt_title;
    Button btn_verify;
    String code;
    HashMap<String, String> params;
    Intent intent;
    int veriCode;
    RelativeLayout rel_main;
    String mobile;
    ImageView img_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);
        initialise();
        btn_verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validation()) {
                    Intent intent = new Intent(VerificationActivity.this, ResetPasswordActivity.class);
                    intent.putExtra("mobile", mobile);
                    startActivity(intent);
                    finish();
                }
            }
        });

        img_back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                onBackPressed();
            }
        });

        txt_resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                params=new HashMap<String, String>();
                params.put("mobile",mobile);
                checkConnectionAndProceed();
            }
        });
    }

    private void initialise() {
        edt_email = (EditText) findViewById(R.id.edt_email);
        txt_resend = (TextView) findViewById(R.id.txt_resend);
        btn_verify = (Button) findViewById(R.id.btn_verify);
        img_back = (ImageView) findViewById(R.id.img_back);
        intent = getIntent();

        veriCode = intent.getIntExtra("code", 0);
        mobile = intent.getStringExtra("mobile");

        txt_title= (TextView) findViewById(R.id.txt_title);
        txt_title.setText("Verification");
    }

    private boolean validation() {
        code = edt_email.getText().toString().trim();
        if (code.length() == 0) {
            edt_email.setError("Enter Code");
            edt_email.requestFocus();
            return false;
        } else if (veriCode != Integer.parseInt(code)) {
            edt_email.setError("Enter Valid Code");
            edt_email.requestFocus();
            return false;
        } else {
            return true;
        }
    }

    private void checkConnectionAndProceed() {
        if (Methods.getNetworkStatus(VerificationActivity.this)) {
            sendMobile();
        } else {
            Snackbar bar = Snackbar.make(rel_main, "Please check internet connection", Snackbar.LENGTH_LONG);
            bar.show();
        }
    }

    private void sendMobile() {
        NetworkRequest.sendStringRequest(VerificationActivity.this, Constants.Urls.MOBILE_VERIFICATION, params, this, Constants.APICodes.MOBILE_VERIFICATION);
    }


    @Override
    public void NetworkRequestCompleted(int apiCode, JSONObject response, ProgressDialog progressDialog) {
        Gson gson = new GsonBuilder().create();

        switch (apiCode) {
            case Constants.APICodes.MOBILE_VERIFICATION: {
                try {
                    if (response.getString(Constants.APIKeys.CODE).equals("1")) {
                        veriCode = response.getInt("veri_code");
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
