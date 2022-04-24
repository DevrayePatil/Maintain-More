package com.example.maintainmore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.splashscreen.SplashScreen;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.ResultReceiver;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class ManageAddressActivity extends AppCompatActivity {

    private static final String TAG = "ManageAddressActivityInfo";

    Toolbar toolbar;

    FirebaseFirestore db;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    DocumentReference documentReference;


    String userID;

    EditText editAddress;
    ExtendedFloatingActionButton buttonLocation;
    Button buttonCancel, buttonSave;

    LocationRequest locationRequest;

    double latitude, longitude;

    ResultReceiver resultReceiver;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SplashScreen.installSplashScreen(this);
        setContentView(R.layout.activity_manage_address);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Address");

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        resultReceiver = new AddressResultReceiver(new Handler());

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();

        userID = Objects.requireNonNull(firebaseUser).getUid();
        
        

        editAddress = findViewById(R.id.editAddress);

        buttonLocation = findViewById(R.id.buttonGetLocation);
        buttonCancel = findViewById(R.id.buttonCancel);
        buttonSave = findViewById(R.id.buttonSave);

        buttonLocation.setOnClickListener(view -> GetLocation());
        buttonCancel.setOnClickListener(view -> finish());
        buttonSave.setOnClickListener(view -> SaveDataToDB());


        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(2000);

        AddressInformation();

    }

    private void SaveDataToDB() {
        
        String address = editAddress.getText().toString();
        
        if (address.equals("")){
            Toast.makeText(this, "Add Address", Toast.LENGTH_SHORT).show();
            return;
        }

        db.collection("Users").document(userID).update(
                "address", address
        ).addOnSuccessListener(unused ->
                Toast.makeText(ManageAddressActivity.this, "Address Saved", Toast.LENGTH_SHORT).show()
        );
    }

    private void AddressInformation() {

        documentReference = db.collection("Users").document(userID);
        documentReference.addSnapshotListener((value, error) -> {
            if (error != null) {
                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
            }
            if (value != null && value.exists()){
                editAddress.setText(value.getString("address"));
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void GetLocation() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (ActivityCompat.checkSelfPermission(getApplicationContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){

                if (isGPSEnabled()) {

                    final SweetAlertDialog progressDialog= new SweetAlertDialog(ManageAddressActivity.this,SweetAlertDialog.PROGRESS_TYPE);
                    progressDialog.setTitleText("Please Wait");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();

                    LocationServices.getFusedLocationProviderClient(getApplicationContext())
                            .requestLocationUpdates(locationRequest, new LocationCallback() {
                                @SuppressLint( "LongLogTag")
                                @Override
                                public void onLocationResult(@NonNull LocationResult locationResult) {
                                    super.onLocationResult(locationResult);

                                    LocationServices.getFusedLocationProviderClient(getApplicationContext())
                                            .removeLocationUpdates(this);

                                    if (locationResult.getLocations().size() >0){

                                        int index = locationResult.getLocations().size() - 1;

                                        latitude = locationResult.getLocations().get(index).getLatitude();
                                        longitude = locationResult.getLocations().get(index).getLongitude();

                                        progressDialog.dismissWithAnimation();

                                        Log.i(TAG, "Location Found");


                                        db.collection("Users").document(userID).update(
                                                "latitude", latitude,
                                                "longitude", longitude
                                        );

                                        Location location = new Location("privateNA");
                                        location.setLatitude(latitude);
                                        location.setLongitude(longitude);

                                        FetchDataFromLatLong(location);
                                    }
                                }
                            }, Looper.getMainLooper());

                } else {
                    turnOnGPS();
                }

            } else {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }


    }

    private void turnOnGPS() {



        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        Task<LocationSettingsResponse> result = LocationServices.getSettingsClient(getApplicationContext())
                .checkLocationSettings(builder.build());

        result.addOnCompleteListener(task -> {

            try {
                task.getResult(ApiException.class);
                Toast.makeText(ManageAddressActivity.this, "GPS is already turned on", Toast.LENGTH_SHORT).show();

            } catch (ApiException e) {

                switch (e.getStatusCode()) {
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:

                        try {
                            ResolvableApiException resolvableApiException = (ResolvableApiException) e;
                            resolvableApiException.startResolutionForResult(ManageAddressActivity.this, 2);
                        } catch (IntentSender.SendIntentException ex) {
                            ex.printStackTrace();
                        }
                        break;

                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        //Device does not have location
                        break;
                }
            }
        });

    }

    private boolean isGPSEnabled() {
        LocationManager locationManager;
        boolean isEnabled;

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        isEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        return isEnabled;

    }

    private void FetchDataFromLatLong(Location location){
        Intent intent = new Intent(this, FetchAddressIntentService.class);
        intent.putExtra(ConstantsVariables.RECEIVER, resultReceiver);
        intent.putExtra(ConstantsVariables.LOCATION_DATA_EXTRA, location);
        startService(intent);
    }

    private class AddressResultReceiver extends ResultReceiver{

        public AddressResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            super.onReceiveResult(resultCode, resultData);

            if (resultCode == ConstantsVariables.SUCCESS_RESULT){

                editAddress.setText(resultData.getString(ConstantsVariables.RESULT_DATA_KEY));
                
            }
            else {
                Toast.makeText(ManageAddressActivity.this, resultData.getString(ConstantsVariables.RESULT_DATA_KEY), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}