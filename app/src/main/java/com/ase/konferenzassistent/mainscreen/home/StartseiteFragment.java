package com.ase.konferenzassistent.mainscreen.home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.ase.konferenzassistent.R;
import com.ase.konferenzassistent.databinding.FragmentMiStartseiteBinding;
import com.ase.konferenzassistent.shared.InformationBottomSheetAdapter;
import com.ase.konferenzassistent.shared.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

public class StartseiteFragment extends Fragment {

    private static final String RKI_API_URL = "https://api.corona-zahlen.org/germany";
    private static final String RKI_NEU_INFEKTIONEN = "RKI_NEU_INFEKTIONEN";
    private static final String RKI_HOSPITALISIERUNG = "RKI_HOSPITALISIERUNG";
    private static final String RKI_LAST_UPDATE = "RKI_LAST_UPDATE";

    private static final double DAY_TO_MILLISEC = 86400000;

    FragmentMiStartseiteBinding bi;

    public StartseiteFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        bi = DataBindingUtil.bind(view);

        // Open information for RKI
        assert bi != null;
        bi.infoButton.setOnClickListener(viewListener -> {
            // creates a Bottom sheet to display Information
            InformationBottomSheetAdapter informationBottomSheetAdapter = new InformationBottomSheetAdapter();
            // Set the layout
            informationBottomSheetAdapter.setmLayout(R.layout.start_information);
            informationBottomSheetAdapter.show(getParentFragmentManager(), informationBottomSheetAdapter.getTag());
        });
        // load RKI data
        loadRKIData();
        super.onViewCreated(view, savedInstanceState);
    }

    // Loads the RKI data from the APi
    @SuppressLint("SetTextI18n")
    private void loadRKIData() {
        Log.d("fragment not added debug", "loadRKIData: called");
        // check if user is connected to a Network
        if (isNetworkConnected()) {
            // check if data already got fetched today
            Date date = new Date();

            // Check if last update was more then a day ago
            if (date.getTime() - DAY_TO_MILLISEC > lastUpdated()) {
                // if update is a day ago update
                Log.d("fragment not added debug", "loadRKIData: Update RKI Data");
                fetchData();
            } else {
                // if not load from shared pref
                Log.d("fragment not added debug", "loadRKIData: Load RKI Data from SharedPref");
                bi.hospitalisiertungText.setText(cutDecimals(Float.toString(hospital())));
                bi.neuinfektionenText.setText(cutDecimals(Float.toString(newInfect())));
            }
        } else {
            // No connection
            netWorkFailed();
        }

    }

    // Fetches the data from the RKI api
    private void fetchData() {
        @SuppressLint("SetTextI18n") JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, RKI_API_URL, null, response -> {
                    try {
                        JSONObject object = (JSONObject) response.get("hospitalization");

                        float newInfect = Float.parseFloat(response.get("weekIncidence").toString());
                        float hospital = Float.parseFloat(object.get("incidence7Days").toString());


                        // Set the text
                        bi.neuinfektionenText.setText(cutDecimals(Float.toString(newInfect)));
                        bi.hospitalisiertungText.setText(cutDecimals(Float.toString(hospital)));

                        // Save the values in shared pref
                        saveRKIData(newInfect, hospital, new Date());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {
                    // Error revert to pref data
                    netWorkFailed();
                });

        VolleySingleton.getInstance(this.getContext()).addToRequestQueue(jsonObjectRequest);
    }

    private String cutDecimals(String floatNumber) {
        return floatNumber.substring(0, floatNumber.indexOf(".") + 2);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_mi_startseite, container, false);
    }

    // Saves the RKI data into shared pref
    private void saveRKIData(float newInfect, float hospital, Date date) {
        if (!isAdded()) {
            Log.d("fragment not added debug", "saveRKIData: fragment not added");
            return;
        }
        SharedPreferences sharedPreferences = this.requireActivity().
                getSharedPreferences("RKI", Context.MODE_PRIVATE);

        Log.d("fragment not added debug", "saveRKIData: Save RKI Data");

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat(RKI_NEU_INFEKTIONEN, newInfect);
        editor.putFloat(RKI_HOSPITALISIERUNG, hospital);
        editor.putLong(RKI_LAST_UPDATE, date.getTime());
        editor.apply();
    }

    // If the Network fails to load new data
    @SuppressLint("SetTextI18n")
    private void netWorkFailed() {
        SharedPreferences sharedPreferences = this.requireActivity().
                getSharedPreferences("RKI", Context.MODE_PRIVATE);
        // if no internet check if there is a shared pref
        if (sharedPreferences.contains(RKI_LAST_UPDATE)) {
            // if shared pref exists load it
            bi.hospitalisiertungText.setText(cutDecimals(Float.toString(hospital())));
            bi.neuinfektionenText.setText(cutDecimals(Float.toString(newInfect())));
        } else {
            // if no pref shared pref show N/A
            bi.hospitalisiertungText.setText("N/A");
            bi.neuinfektionenText.setText("N/A");
        }
    }

    // Load Infected from Shared Pref
    private float newInfect() {
        SharedPreferences sharedPreferences = this.requireActivity().
                getSharedPreferences("RKI", Context.MODE_PRIVATE);
        return sharedPreferences.getFloat(RKI_NEU_INFEKTIONEN, 0);
    }

    // Load hospitalisation from Shared Pref
    private float hospital() {
        SharedPreferences sharedPreferences = this.requireActivity().
                getSharedPreferences("RKI", Context.MODE_PRIVATE);
        return sharedPreferences.getFloat(RKI_HOSPITALISIERUNG, 0);
    }

    // Load Infected from Shared Pref
    private long lastUpdated() {
        SharedPreferences sharedPreferences = this.requireActivity().
                getSharedPreferences("RKI", Context.MODE_PRIVATE);
        return sharedPreferences.getLong(RKI_LAST_UPDATE, 0);
    }

    //https://stackoverflow.com/a/51692369
    // check for network
    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) requireContext().
                getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

}