package androapps.bookmygaddidriver.activities;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
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

public class Terms extends AppCompatActivity {
    WebView webTerms;
    ImageView img_back;
    TextView txt_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms);

        webTerms = (WebView) findViewById(R.id.webTerms);
        img_back = (ImageView) findViewById(R.id.img_back);
        txt_title = (TextView) findViewById(R.id.txt_title);

        txt_title.setText("Terms & Condition");
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        getData();
    }
    public void getData(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.Urls.TERMS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString(Constants.APIKeys.CODE).equals("1")){
                        webTerms.loadData(jsonObject.getString("content"),"text/html","utf-8");
                    }
                }catch (JSONException j){
                    j.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }

        }) {
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
