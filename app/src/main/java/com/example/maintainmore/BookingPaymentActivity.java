package com.example.maintainmore;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class BookingPaymentActivity extends AppCompatActivity {

    private static final String TAG = "BookingPaymentActivityInfo";
    
    String userID, technicianID;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseFirestore db ;
    DocumentReference documentReference;

    Calendar calendar;


    TextView displayTechnicianName, displayTechnicianEmail,displayTechnicianPhoneNumber;
    TextView displayVisitingDate, displayVisitingTime,displayRequiredTime;
    TextView displayNumberOfBookings, displayAmountPerBooking, displayTotalAmount;
    TextView displayWalletBalance;
    
    Button buttonCancel, buttonBook;


    int numberOfServicesForMale = 0;
    int numberOfServicesForFemale = 0;
    int servicePrice = 0;
    int totalServices = 0;
    int totalServicesPrice = 0;
    int totalWalletBalance = 0;

    String serviceName, serviceDescription, serviceType, requiredTime;
    String bookingDate, bookingTime, visitingDate, visitingTime, cancellationTimeTill, bookingStatus;
    String iconUrl;


    String paymentDate, paymentTime;


    @SuppressLint("LongLogTag")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SplashScreen.installSplashScreen(this);
        setContentView(R.layout.activity_booking_payment);

        db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        userID = Objects.requireNonNull(firebaseUser).getUid();

        calendar = Calendar.getInstance();


        displayTechnicianName = findViewById(R.id.displayTechnicianName);
        displayTechnicianEmail = findViewById(R.id.displayTechnicianEmail);
        displayTechnicianPhoneNumber = findViewById(R.id.displayTechnicianPhoneNumber);

        displayVisitingDate = findViewById(R.id.displayVisitingDate);
        displayVisitingTime = findViewById(R.id.displayVisitingTime);
        displayRequiredTime = findViewById(R.id.displayRequiredTime);

        displayNumberOfBookings = findViewById(R.id.displayNumberOfBookings);
        displayAmountPerBooking = findViewById(R.id.displayAmountPerBooking);
        displayTotalAmount = findViewById(R.id.displayTotalAmount);

        displayWalletBalance = findViewById(R.id.displayWalletBalance);

        buttonCancel = findViewById(R.id.buttonCancel);
        buttonBook = findViewById(R.id.buttonConform);

        
        GetDataFromPreviousActivity();
        GetDataFromDatabase();

        Log.d(TAG, "technicianID: " + technicianID);

        
        buttonCancel.setOnClickListener(view -> finish());
        buttonBook.setOnClickListener(view -> BookService());

    }

    @SuppressLint("LongLogTag")
    private void BookService() {
        Map<String,String> bookService = new HashMap<>();

        DocumentReference documentReference =  db.collection("Bookings").document();

        String bookingID = documentReference.getId();




        if (numberOfServicesForMale != 0){
            bookService.put("servicesForMale", String.valueOf(numberOfServicesForMale));
        }
        if (numberOfServicesForFemale != 0){
            bookService.put("servicesForFemale", String.valueOf(numberOfServicesForFemale));
        }

        bookService.put("whoBookedService", userID);
        bookService.put("assignedTechnician", technicianID);
        bookService.put("serviceName",serviceName);
        bookService.put("serviceDescription",serviceDescription);
        bookService.put("totalServicesPrice", String.valueOf(totalServicesPrice));
        bookService.put("totalServices", String.valueOf(totalServices));
        bookService.put("serviceIcon",iconUrl);
        bookService.put("requiredTime", requiredTime);
        bookService.put("servicePrice", String.valueOf(servicePrice));
        bookService.put("bookingDate", bookingDate);
        bookService.put("bookingTime",bookingTime);
        bookService.put("cancelTillHour", cancellationTimeTill);
        bookService.put("visitingDate", visitingDate);
        bookService.put("visitingTime",visitingTime);
        bookService.put("serviceType",serviceType);
        bookService.put("bookingStatus","Booked");


        if (totalWalletBalance >= totalServicesPrice){


                documentReference.set(bookService).addOnSuccessListener(unused ->


                        db.collection("Technicians").document(technicianID)
                        .update("availabilityStatus", "Engaged").addOnSuccessListener(unused1 -> {

                            Toast.makeText(this, "Booking Completed", Toast.LENGTH_SHORT).show();

                            Log.i(TAG, "Booking Completed");



                            startActivity(new Intent(this, MainActivity.class));

                }).addOnFailureListener(e -> {
                            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            Log.i(TAG, e.getMessage());
                        }))
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Booking Failed: " + e, Toast.LENGTH_SHORT).show();
                    Log.i(TAG, "Booking Failed");

                }
            );

            totalWalletBalance -= totalServicesPrice;

            Map<String, String> paymentTransaction = new HashMap<>();

            paymentDate = DateFormat.getDateInstance().format(calendar.getTime());
            paymentTime = new SimpleDateFormat("hh:mm a", Locale.getDefault()).format(new Date());

            paymentTransaction.put("bookingID",bookingID);
            paymentTransaction.put("whoPaidAmount", userID);
            paymentTransaction.put("paymentDate",paymentDate);
            paymentTransaction.put("paymentTime",paymentTime);
            paymentTransaction.put("amountPaid",String.valueOf(totalServicesPrice));
            paymentTransaction.put("paymentStatus","Dr");



            db.collection("Payments").document().set(paymentTransaction).addOnSuccessListener(unused ->
                Log.i(TAG, "Payment Completed")
            );

            db.collection("Users").document(userID).update("walletBalanceInINR", String.valueOf(totalWalletBalance))
                    .addOnSuccessListener(unused -> Toast.makeText(this, "Payment Completed", Toast.LENGTH_SHORT).show())
                    .addOnFailureListener(e -> Log.e(TAG,"Failed to update balance" + e));

            Log.i(TAG, "Updated Balance: " + totalWalletBalance);
        }
        else {
            Toast.makeText(this, "Insufficient Wallet Balance", Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("LongLogTag")
    private void GetDataFromPreviousActivity() {
        Intent intent = getIntent();

        technicianID = intent.getStringExtra("technicianID");
        requiredTime = intent.getStringExtra("requiredTime");
        visitingDate = intent.getStringExtra("visitingDate");
        visitingTime = intent.getStringExtra("visitingTime");

        serviceType = intent.getStringExtra("serviceType");
        iconUrl = intent.getStringExtra("serviceIcon");
        serviceName = intent.getStringExtra("serviceName");
        serviceDescription = intent.getStringExtra("serviceDescription");


        String maleServices = intent.getStringExtra("servicesForMale");
        String femaleServices = intent.getStringExtra("servicesForFemale");

        String strTotalServices = intent.getStringExtra("totalServices");
        String strServicePrice = intent.getStringExtra("servicePrice");
        String strTotalServicePrice= intent.getStringExtra("totalServicesPrice");
        String strTotalWalletBalance= intent.getStringExtra("totalWalletBalance");



        bookingDate = intent.getStringExtra("bookingDate");
        bookingTime = intent.getStringExtra("bookingTime");
        visitingDate = intent.getStringExtra("visitingDate");
        visitingTime = intent.getStringExtra("visitingTime");
        cancellationTimeTill = intent.getStringExtra("cancelTillHour");
        bookingStatus = intent.getStringExtra("bookingStatus");

        totalServices = Integer.parseInt(strTotalServices);
        servicePrice = Integer.parseInt(strServicePrice);
        totalServicesPrice = Integer.parseInt(strTotalServicePrice);
        totalWalletBalance = Integer.parseInt(strTotalWalletBalance);

        numberOfServicesForMale = Integer.parseInt(maleServices);
        numberOfServicesForFemale = Integer.parseInt(femaleServices);



        Log.i(TAG, "Technician ID:" + technicianID);
        Log.i(TAG,"ServiceType: " + serviceType);
        Log.i(TAG,"Total Services: " + totalServicesPrice);
        Log.i(TAG,"Services for Male: " + numberOfServicesForMale);
        Log.i(TAG,"Services for Female: " + numberOfServicesForFemale);
    }

    @SuppressLint("SetTextI18n")
    private void GetDataFromDatabase(){

        documentReference = db.collection("Technicians").document(technicianID);

        documentReference.addSnapshotListener((value, error) -> {
            if (error != null) {
                Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
            }
            if (value != null && value.exists()){

                displayTechnicianName.setText(value.getString("name"));
                displayTechnicianEmail.setText(value.getString("email"));
                displayTechnicianPhoneNumber.setText(value.getString("phoneNumber"));

                displayVisitingDate.setText(visitingDate);
                displayVisitingTime.setText(visitingTime);

                displayRequiredTime.setText(requiredTime);

                displayNumberOfBookings.setText(String.valueOf(totalServices));

                displayAmountPerBooking.setText("₹ " + servicePrice);
                displayTotalAmount.setText("₹ " + totalServicesPrice);

                displayWalletBalance.setText("₹ " + totalWalletBalance);
            }
        });
    }
}