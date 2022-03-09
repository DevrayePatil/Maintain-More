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

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class BookingPaymentActivity extends AppCompatActivity {

    private static final String TAG = "BookingPaymentActivityInfo";
    
    String userID, technicianID;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseFirestore db ;
    DocumentReference documentReference;


    TextView displayTechnicianName, displayTechnicianEmail,displayTechnicianPhoneNumber;
    TextView displayVisitingDate, displayVisitingTime,displayRequiredTime;
    TextView displayNumberOfBookings, displayAmountPerBooking, displayTotalAmount;
    
    Button buttonCancel, buttonBook;


    int numberOfServicesForMale = 0;
    int numberOfServicesForFemale = 0;
    int servicePrice = 0;
    int totalServices = 0;
    int totalServicesPrice = 0;

    String serviceName, serviceDescription, serviceType, requiredTime;
    String bookingDate, bookingTime, visitingDate, visitingTime, cancellationTimeTill, bookingStatus;
    String iconUrl;


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


        displayTechnicianName = findViewById(R.id.displayTechnicianName);
        displayTechnicianEmail = findViewById(R.id.displayTechnicianEmail);
        displayTechnicianPhoneNumber = findViewById(R.id.displayTechnicianPhoneNumber);

        displayVisitingDate = findViewById(R.id.displayVisitingDate);
        displayVisitingTime = findViewById(R.id.displayVisitingTime);
        displayRequiredTime = findViewById(R.id.displayRequiredTime);

        displayNumberOfBookings = findViewById(R.id.displayNumberOfBookings);
        displayAmountPerBooking = findViewById(R.id.displayAmountPerBooking);
        displayTotalAmount = findViewById(R.id.displayTotalAmount);



        buttonCancel = findViewById(R.id.buttonCancel);
        buttonBook = findViewById(R.id.buttonConform);

        
        GetDataFromPreviousActivity();
        GetDataFromDatabase();

        Log.d(TAG, "technicianID: " + technicianID);

        
        buttonCancel.setOnClickListener(view -> finish());
        buttonBook.setOnClickListener(view -> BookService());

    }

    private void BookService() {
        Map<String,String> bookService = new HashMap<>();


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

        db.collection("Bookings").document()
                .set(bookService).addOnSuccessListener(unused ->
                        db.collection("Technicians").document(technicianID)
                        .update("availabilityStatus", "Engaged").addOnSuccessListener(unused1 -> {

                            Toast.makeText(this, "Booking Completed", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(this, MainActivity.class));

                }).addOnFailureListener(e -> Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show()))
                .addOnFailureListener(e ->
                Toast.makeText(this, "Booking Failed: " + e, Toast.LENGTH_SHORT).show()
        );
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

        String strTotalServices = intent.getStringExtra("totalServices");
        String strServicePrice = intent.getStringExtra("servicePrice");
        String strTotalServicePrice= intent.getStringExtra("totalServicesPrice");

        Log.i(TAG,"ServiceType: " + serviceType);
        Log.i(TAG,"Total Services: " + strTotalServices);

        bookingDate = intent.getStringExtra("bookingDate");
        bookingTime = intent.getStringExtra("bookingTime");
        visitingDate = intent.getStringExtra("visitingDate");
        visitingTime = intent.getStringExtra("visitingTime");
        cancellationTimeTill = intent.getStringExtra("cancelTillHour");
        bookingStatus = intent.getStringExtra("bookingStatus");

        totalServices = Integer.parseInt(strTotalServices);
        servicePrice = Integer.parseInt(strServicePrice);
        totalServicesPrice = Integer.parseInt(strTotalServicePrice);

    }

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
                displayAmountPerBooking.setText(String.valueOf(servicePrice));
                displayTotalAmount.setText(String.valueOf(totalServicesPrice));
            }
        });
    }
}