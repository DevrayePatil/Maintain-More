package com.example.maintainmore;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.splashscreen.SplashScreen;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;


public class BookServiceActivity extends AppCompatActivity {

    private static final String TAG = "BookServiceActivityInfo";
    private final int cancelTillHour = 1;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseFirestore db;
    Calendar calendar;

    String userID, technicianID = "";

    Toolbar toolbar;
    Button buttonCancel, buttonBook;
    Button buttonChooseDate, buttonChooseTime;

    TextView displayServiceName, displayServiceDescription, displayTotalItems, displayTotalPrice;
    TextView displayWalletBalance;
    TextView displayRequiredTime, displayServicePrice;
    TextView displayServiceDate, displayServiceTime;
    ImageView imageBackground;

    int numberOfServicesForMale = 0;
    int numberOfServicesForFemale = 0;
    int totalServices = 0;
    int servicePrice = 0;
    int totalServicesPrice = 0;
    int totalWalletBalance = 0;

    boolean availableTechnician = false;

    String chosenDate= "";
    String chosenTime = "";
    String cancellationTime = "";
    String bookingDate = "";
    String bookingTime = "";

    String technicianName, technicianEmail, technicianPhoneNumber;

    String serviceName, serviceDescription, serviceType, requiredTime, price, iconUrl, backgroundImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SplashScreen.installSplashScreen(this);
        setContentView(R.layout.activity_book_service);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
        calendar = Calendar.getInstance();

        userID = Objects.requireNonNull(firebaseUser).getUid();


        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        GetDataFromDataBase();

        LinkedViewIDes();
        GetDataFromCard();
        SetDataToView();
        ServicePickerForMale();
        ServicePickerForFemale();

        buttonChooseDate.setOnClickListener(view -> DatePickerForService());
        buttonChooseTime.setOnClickListener(view -> TimePickerForService());

        buttonCancel.setOnClickListener(view -> finish());
        buttonBook.setOnClickListener(view -> BookService());

        bookingDate = DateFormat.getDateInstance().format(calendar.getTime());
        bookingTime = new SimpleDateFormat("hh:mm a", Locale.getDefault()).format(new Date());

    }




    public void BookService() {


        if (totalServices <= 0){
            Toast.makeText(getApplicationContext(), "Please Select at least 1 Service", Toast.LENGTH_SHORT).show();
            return;
        }
        if (chosenDate.equals("")){
            Toast.makeText(getApplicationContext(), "Please Choose service Date", Toast.LENGTH_SHORT).show();
            return;
        }
        if (chosenTime.equals("")){
            Toast.makeText(getApplicationContext(), "Please Choose service Time", Toast.LENGTH_SHORT).show();
            return;
        }

        db.collection("Technicians").addSnapshotListener((value, error) -> {

            assert value != null;
            for (DocumentSnapshot snapshot: value){

                String approvalStatus = snapshot.getString("approvalStatus");
                String availabilityStatus = snapshot.getString("availabilityStatus");

                if (approvalStatus != null && approvalStatus.equals("Approved")) {
                    if (availabilityStatus != null && availabilityStatus.equals("Free")) {

                        availableTechnician = true;

                        technicianID = snapshot.getId();
                        technicianName = snapshot.getString("serviceName");
                        technicianEmail = snapshot.getString("email");
                        technicianPhoneNumber = snapshot.getString("phoneNumber");
                    }
                }
            }
        });

        new Handler().postDelayed(() -> {

            if(!availableTechnician) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Booking");
                builder.setMessage("Our any technician not available at this time \nplease select another date/time");
                builder.setPositiveButton("Ok", (dialogInterface, i) -> dialogInterface.dismiss());
                builder.show();

                return;
            }
            if (!technicianID.equals("")) {


                Intent intent = new Intent(this, BookingPaymentActivity.class);

                intent.putExtra("technicianID", technicianID);
                intent.putExtra("requiredTime", requiredTime);
                intent.putExtra("visitingDate", chosenDate);
                intent.putExtra("visitingTime", chosenTime);

                intent.putExtra("serviceType",serviceType);
                intent.putExtra("serviceIcon",iconUrl);
                intent.putExtra("serviceName",serviceName);
                intent.putExtra("serviceDescription", serviceDescription);
                intent.putExtra("totalServices", String.valueOf(totalServices));
                intent.putExtra("servicePrice", String.valueOf(servicePrice));
                intent.putExtra("totalServicesPrice", String.valueOf(totalServicesPrice));
                intent.putExtra("bookingDate", bookingDate);
                intent.putExtra("bookingTime",bookingTime);
                intent.putExtra("visitingDate", chosenDate);
                intent.putExtra("visitingTime",chosenTime);
                intent.putExtra("cancelTillHour", cancellationTime);
                intent.putExtra("totalWalletBalance", String.valueOf(totalWalletBalance));

                intent.putExtra("bookingStatus","booked");

                Log.d(TAG,"Services" + totalWalletBalance);



                    intent.putExtra("servicesForMale", String.valueOf(numberOfServicesForMale));

                    intent.putExtra("servicesForFemale", String.valueOf(numberOfServicesForFemale));



                Log.i(TAG, "servicesForMale" + numberOfServicesForMale);

                startActivity(intent);


            }

        },100);

    }


    @SuppressLint("SimpleDateFormat")
    private void TimePickerForService() {

        MaterialTimePicker.Builder builder = new MaterialTimePicker.Builder();
        builder.setTitleText("Choose Time");
        builder.setHour(12);
        builder.setMinute(0);

        MaterialTimePicker timePicker = builder.build();

        timePicker.show(getSupportFragmentManager(), "TIME_PICKER");
        timePicker.addOnPositiveButtonClickListener(view ->{

            String timePicked = timePicker.getHour()+ ":" + timePicker.getMinute();

            int cancelTime = timePicker.getHour() - cancelTillHour;

            String cancelTimePicked = cancelTime + ":" + timePicker.getMinute();

             SimpleDateFormat f24Hour = new SimpleDateFormat("HH:mm");
            try {
                Date date = f24Hour.parse(timePicked);
                Date cancelDate = f24Hour.parse(cancelTimePicked);
                assert date != null;
                assert cancelDate != null;

                SimpleDateFormat f12Hour = new SimpleDateFormat("hh:mm aa");

                chosenTime = f12Hour.format(date);
                cancellationTime = f12Hour.format(cancelDate);

                Toast.makeText(this, cancellationTime, Toast.LENGTH_SHORT).show();
                displayServiceTime.setText(chosenTime);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        });
    }

    private void DatePickerForService() {

        long today = MaterialDatePicker.todayInUtcMilliseconds();

        CalendarConstraints.Builder constraintBuilder = new CalendarConstraints.Builder();
        constraintBuilder.setValidator(DateValidatorPointForward.now());

        MaterialDatePicker.Builder<Long> builder = MaterialDatePicker.Builder.datePicker();
        builder.setTitleText("Choose Date");
        builder.setSelection(today);
        builder.setCalendarConstraints(constraintBuilder.build());

        MaterialDatePicker<Long> materialDatePicker = builder.build();
        materialDatePicker.show(getSupportFragmentManager(),"DATE_PICKET");
        materialDatePicker.addOnPositiveButtonClickListener(selection -> {
            chosenDate = String.valueOf(materialDatePicker.getHeaderText());
            displayServiceDate.setText(chosenDate);
        });


    }

    private void ServicePickerForFemale(){
        TextView numberPickerForFemale;
        ImageView buttonDecrementForFemale, buttonIncrementForFemale;

        numberPickerForFemale = findViewById(R.id.numberPickerForFemale);

        buttonDecrementForFemale = findViewById(R.id.buttonDecrementForFemale);
        buttonIncrementForFemale = findViewById(R.id.buttonIncrementForFemale);

        buttonDecrementForFemale.setOnClickListener(view -> {
            if (numberOfServicesForFemale > 0) {
                numberOfServicesForFemale--;
                totalServices--;
                numberPickerForFemale.setText(String.valueOf(numberOfServicesForFemale));
                displayTotalItems.setText(String.valueOf(totalServices));

                totalServicesPrice = servicePrice * totalServices;
                displayTotalPrice.setText(String.valueOf(totalServicesPrice));
            }
        });

        buttonIncrementForFemale.setOnClickListener(view -> {
            if (numberOfServicesForFemale < 5) {
                numberOfServicesForFemale++;
                totalServices++;

                numberPickerForFemale.setText(String.valueOf(numberOfServicesForFemale));
                displayTotalItems.setText(String.valueOf(totalServices));

                totalServicesPrice = servicePrice * totalServices;
                displayTotalPrice.setText(String.valueOf(totalServicesPrice));
            }
            else {
                Toast.makeText(this, "You Can't Choose More then 5", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void ServicePickerForMale(){
        TextView numberPicker;
        ImageView buttonDecrement, buttonIncrement;

        numberPicker = findViewById(R.id.numberPicker);

        buttonDecrement = findViewById(R.id.buttonDecrement);
        buttonIncrement = findViewById(R.id.buttonIncrement);

        buttonDecrement.setOnClickListener(view -> {
            if (numberOfServicesForMale > 0) {
                numberOfServicesForMale--;
                totalServices--;
                numberPicker.setText(String.valueOf(numberOfServicesForMale));
                displayTotalItems.setText(String.valueOf(totalServices));

                totalServicesPrice = servicePrice * totalServices;
                displayTotalPrice.setText(String.valueOf(totalServicesPrice));
            }
        });

        buttonIncrement.setOnClickListener(view -> {
            if (numberOfServicesForMale < 5) {
                numberOfServicesForMale++;
                totalServices++;
                numberPicker.setText(String.valueOf(numberOfServicesForMale));
                displayTotalItems.setText(String.valueOf(totalServices));

                totalServicesPrice = servicePrice * totalServices;
                displayTotalPrice.setText(String.valueOf(totalServicesPrice));
            }
            else {
                Toast.makeText(this, "You Can't Choose More then 5", Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void SetDataToView() {
        //        Toolbar
        Objects.requireNonNull(getSupportActionBar()).setTitle(serviceName);
        Glide.with(this).load(backgroundImage).placeholder(R.drawable.ic_account).into(imageBackground);

        displayServiceName.setText(serviceName);
        displayServiceDescription.setText(serviceDescription);
        displayRequiredTime.setText(requiredTime);
        displayServicePrice.setText(String.format("₹ %s", price));


    }

    private void GetDataFromDataBase() {
        db.collection("Users").document(userID)
                .get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {

                    totalWalletBalance = Integer.parseInt(Objects.requireNonNull(document.getString("walletBalanceInINR")));

                    displayWalletBalance.setText(MessageFormat.format("₹ {0}", totalWalletBalance));

                } else {
                    Log.d(TAG, "No such document");
                }
            } else {
                Log.d(TAG, "get failed with ", task.getException());
            }
        });
    }

    @SuppressLint("LongLogTag")
    private void GetDataFromCard(){
        Intent intent = getIntent();

        serviceName = intent.getStringExtra("Name");
        serviceDescription = intent.getStringExtra("Description");
        serviceType = intent.getStringExtra("ServiceType");
        requiredTime = intent.getStringExtra("RequiredTime");
        price = intent.getStringExtra("Price");

        servicePrice = Integer.parseInt(price);

        iconUrl = intent.getStringExtra("IconUrl");
        backgroundImage = intent.getStringExtra("BackgroundImageUrl");
        serviceName = intent.getStringExtra("Name");


        Log.i(TAG,"Name: " + serviceName);
        Log.i(TAG,"Description: " + serviceDescription);
        Log.i(TAG,"ServiceType: " + serviceType);
        Log.i(TAG,"Required time: " + requiredTime);
        Log.i(TAG,"Price: " + price);

        Log.i(TAG,"iconUrl: " + iconUrl);
        Log.i(TAG,"backgroundImageUrl: " + backgroundImage);

    }

    private void LinkedViewIDes() {
        imageBackground = findViewById(R.id.imageBackground);

        buttonCancel = findViewById(R.id.buttonCancel);
        buttonBook = findViewById(R.id.buttonBook);
        buttonChooseDate = findViewById(R.id.buttonChooseDate);
        buttonChooseTime = findViewById(R.id.buttonChooseTime);

        displayServiceName = findViewById(R.id.displayServiceName);
        displayServiceDescription = findViewById(R.id.displayServiceDescription);
        displayRequiredTime = findViewById(R.id.displayRequiredTime);
        displayServicePrice = findViewById(R.id. displayServicePrice);

        displayServiceDate = findViewById(R.id.displayServiceDate);
        displayServiceTime = findViewById(R.id.displayServiceTime);

        displayTotalItems = findViewById(R.id.displayTotalItems);
        displayTotalPrice = findViewById(R.id.displayTotalPrice);
        displayWalletBalance = findViewById(R.id.displayWalletBalance);
    }

}