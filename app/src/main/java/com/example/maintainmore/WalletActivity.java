package com.example.maintainmore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.splashscreen.SplashScreen;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.maintainmore.Adapters.PaymentAdapter;
import com.example.maintainmore.Modals.PaymentsModal;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Objects;

public class WalletActivity extends AppCompatActivity {

    private static final String TAG = "ManageAddressActivityInfo";


    FirebaseFirestore db;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    DocumentReference documentReference;

    String userID;

    Toolbar toolbar;
    TextView walletBalanceInINR;

    RecyclerView recycleView_transactions;

    ArrayList<PaymentsModal> paymentsModals = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SplashScreen.installSplashScreen(this);
        setContentView(R.layout.activity_wallet);


        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();

        userID = Objects.requireNonNull(firebaseUser).getUid();

        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Wallet");

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        walletBalanceInINR = findViewById(R.id.walletBalanceInINR);

        recycleView_transactions = findViewById(R.id.recycleView_transactions);

        WalletInformation();
        Transactions();
    }

    private void Transactions() {
        db.collection("Payments").whereEqualTo("whoPaidAmount", userID).addSnapshotListener((value, error) -> {
            paymentsModals.clear();

            assert value != null;
            for (DocumentSnapshot snapshot: value){


                    paymentsModals.add(new PaymentsModal(snapshot.getId(),
                            snapshot.getString("bookingID"),
                            snapshot.getString("amountPaid"),
                            snapshot.getString("paymentDate"),
                            snapshot.getString("paymentTime"),
                            snapshot.getString("paymentStatus")
                    ));

            }
            PaymentAdapter paymentAdapter = new PaymentAdapter(paymentsModals, this);
            recycleView_transactions.setAdapter(paymentAdapter);

            Toast.makeText(this, "Size :" + paymentsModals.size(), Toast.LENGTH_SHORT).show();

        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recycleView_transactions.setLayoutManager(linearLayoutManager);
    }

    private void WalletInformation() {

        documentReference = db.collection("Users").document(userID);
        documentReference.addSnapshotListener((value, error) -> {
            if (error != null) {
                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
            }
            if (value != null && value.exists()){
                if (value.getString("walletBalanceInINR") == null ||
                        Objects.requireNonNull(value.getString("walletBalanceInINR")).equals("")){
                    walletBalanceInINR.setText("0");
                }
                else {
                    walletBalanceInINR.setText(value.getString("walletBalanceInINR"));
                }
            }
        });
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