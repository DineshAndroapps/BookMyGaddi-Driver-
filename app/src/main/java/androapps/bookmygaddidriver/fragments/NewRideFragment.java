package androapps.bookmygaddidriver.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androapps.bookmygaddidriver.R;
import androapps.bookmygaddidriver.adapters.RidesAdapter;
import androapps.bookmygaddidriver.interfaces.ApiResponse;
import androapps.bookmygaddidriver.models.RidesDetails;
import androapps.bookmygaddidriver.network.NetworkRequest;
import androapps.bookmygaddidriver.utils.Constants;
import androapps.bookmygaddidriver.utils.GPSTracker;
import androapps.bookmygaddidriver.utils.LocationService;
import androapps.bookmygaddidriver.utils.Methods;

/**
 * Created by ADMIN on 17-01-2017.
 */
public class NewRideFragment extends Fragment implements ApiResponse {


    RecyclerView rec_ride;
    RecyclerView.LayoutManager rec_layout_manager;
    private RelativeLayout rel_ride;
    TextView nodata;
    HashMap<String,String> params;
    SharedPreferences sharedPreferences;
    String id;
    private GPSTracker mGpsTracker;
    double longitude;
    double latitude;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_new_ride, container, false);
        initialise(rootView);
        return rootView;
    }
    void initialise(View rootView)
    {
        rec_ride= (RecyclerView) rootView.findViewById(R.id.rec_ride);
        rec_layout_manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rec_ride.setLayoutManager(rec_layout_manager);
        nodata= (TextView) rootView.findViewById(R.id.nodata);
        rel_ride= (RelativeLayout) rootView.findViewById(R.id.rel_ride);
        sharedPreferences=getContext().getSharedPreferences("Details", 0);
        id=sharedPreferences.getString("id","");
        String sessioncode=sharedPreferences.getString("sessioncode","");
        params=new HashMap<>();
        params.put("driver_id", id);
        params.put("token_session", sessioncode.trim());



    }


//
//    @Override
//    public void onStart() {
//        super.onStart();
//        checkConnectionAndProceed();
//    }

    @Override
    public void onResume() {
        super.onResume();

        checkConnectionAndProceed();

    }


//    @Override
//    public void onStop() {
//        super.onStop();
////        checkConnectionAndProceed();
//        nodata.setVisibility(View.VISIBLE);
//    }

    private void checkConnectionAndProceed() {
        if (Methods.getNetworkStatus(getContext())) {
            getRides();
            nodata.setVisibility(View.GONE);
        } else {


//            Snackbar bar = Snackbar.make(rel_ride, "Please check internet connection", Snackbar.LENGTH_INDEFINITE);
//            bar.setAction("Retry", new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                    Intent i=new Intent(getActivity(), NewRideActivity.class);
//
//                    checkConnectionAndProceed();
//
//                }
//            });
//            bar.setActionTextColor(Color.parseColor("#4CAF50"));
//            bar.show();
            nodata.setVisibility(View.VISIBLE);
            nodata.setText("Please check internet connection");
            Toast.makeText(getActivity(), "Please check internet connection", Toast.LENGTH_SHORT).show();
        }
    }

    private void getRides() {
        NetworkRequest.sendStringRequest(getContext(), Constants.Urls.GET_RIDES, params, this, Constants.APICodes.GET_RIDES);
    }

    @Override
    public void NetworkRequestCompleted(int apiCode, JSONObject response, ProgressDialog progressDialog) {
        Gson gson = new GsonBuilder().create();

        switch (apiCode) {
            case Constants.APICodes.GET_RIDES: {
                ArrayList<RidesDetails> ridesDetailses=new ArrayList<>();
                try {
                    if (response.getString("code").equals("1")) {
                        ridesDetailses = gson.fromJson(response.getJSONArray("list").toString(), new TypeToken<List<RidesDetails>>() {
                        }.getType());


                        nodata.setVisibility(View.GONE);

                    }

                    else if(response.getString(Constants.APIKeys.CODE).equals("9")){
                        getContext().stopService(new Intent(getContext(), LocationService.class));
                        Toast.makeText(getContext(), response.getString(Constants.APIKeys.MESSAGE), Toast.LENGTH_SHORT).show();
                        Methods.logout(getContext());
                    }


                    else if(response.getString(Constants.APIKeys.CODE).equals("10")) {
                        Toast.makeText(getContext(),response.getString(Constants.APIKeys.MESSAGE) , Toast.LENGTH_SHORT).show();
                    }


                    else {
                        nodata.setVisibility(View.VISIBLE);

                    }
                    RidesAdapter ridesAdapter = new RidesAdapter(getActivity(), ridesDetailses,"Newride");
                    rec_ride.setAdapter(ridesAdapter);
                } catch (JSONException e) {
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
