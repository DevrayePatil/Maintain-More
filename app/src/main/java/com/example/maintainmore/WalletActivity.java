package com.example.maintainmore;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class WalletActivity extends AppCompatActivity {

    private static final String TAG = "ManageAddressActivityInfo";



    FirebaseFirestore db;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    DocumentReference documentReference;


    String userID;

    TextView walletBalanceInINR;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SplashScreen.installSplashScreen(this);
        setContentView(R.layout.activity_wallet);


        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();

        userID = Objects.requireNonNull(firebaseUser).getUid();

        walletBalanceInINR = findViewById(R.id.walletBalanceInINR);

        WalletInformation();
    }

    private void WalletInformation() {

        documentReference = db.collection("Users").document(userID);
        documentReference.addSnapshotListener((value, error) -> {
            if (error != null) {
                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
            }
            if (value != null && value.exists()){
                if (value.getString("walletBalanceInINR") == null){
                    walletBalanceInINR.setText("0");
                }
                else {
                    walletBalanceInINR.setText(value.getString("walletBalanceInINR"));
                }
            }
        });
    }
}