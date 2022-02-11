package com.example.maintainmore;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.splashscreen.SplashScreen;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
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
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;


public class ServiceBookingActivity extends AppCompatActivity {

    private static final String TAG = "ServiceDetailsActivityInfo";
    private final int cancelTillHour = 1;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseFirestore db;
    Calendar calendar;

    String userID;

    Toolbar toolbar;
    Button buttonCancel, buttonBook;
    Button buttonChooseDate, buttonChooseTime;

    TextView displayServiceName, displayServiceDescription, displayTotalItems, displayTotalPrice;
    TextView displayRequiredTime, displayServicePrice;
    TextView displayServiceDate, displayServiceTime;
    ImageView imageBackground;

    int numberOfServicesForMale = 0;
    int numberOfServicesForFemale = 0;
    int servicePrice = 0;
    int totalServices = 0;
    int totalPrice = 0;


    String chosenDate= "";
    String chosenTime = "";
    String cancellationTime = "";
    String bookingDate = "";
    String bookingTime = "";

    String name, description, serviceType, requiredTime, price, iconUrl, backgroundImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SplashScreen.installSplashScreen(this);
        setContentView(R.layout.activity_service_booking);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
        calendar = Calendar.getInstance();

        userID = Objects.requireNonNull(firebaseUser).getUid();


        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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

        Map<String,String> bookService = new HashMap<>();


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

        if (numberOfServicesForMale != 0){
            bookService.put("servicesForMale", String.valueOf(numberOfServicesForMale));
        }
        if (numberOfServicesForFemale != 0){
            bookService.put("servicesForFemale", String.valueOf(numberOfServicesForFemale));
        }


        bookService.put("whoBookedService", userID);
        bookService.put("serviceName",name);
        bookService.put("serviceDescription",description);
        bookService.put("totalPrice", String.valueOf(totalPrice));
        bookService.put("totalServices", String.valueOf(totalServices));
        bookService.put("serviceIcon",iconUrl);
        bookService.put("requiredTime", requiredTime);
        bookService.put("servicePrice", String.valueOf(servicePrice));
        bookService.put("visitingDate", chosenDate);
        bookService.put("bookingDate", bookingDate);
        bookService.put("cancelTillHour", cancellationTime);
        bookService.put("visitingTime",chosenTime);
        bookService.put("bookingTime",bookingTime);

        db.collection("Bookings").document().set(bookService).addOnSuccessListener(unused -> {
            Toast.makeText(this, "Booking Completed", Toast.LENGTH_SHORT).show();
            finish();

        }).addOnFailureListener(e ->
                Toast.makeText(this, "Booking Failed: " + e, Toast.LENGTH_SHORT).show()
        );

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

                totalPrice = servicePrice * totalServices;
                displayTotalPrice.setText(String.valueOf(totalPrice));
            }
        });

        buttonIncrementForFemale.setOnClickListener(view -> {
            if (numberOfServicesForFemale < 5) {
                numberOfServicesForFemale++;
                totalServices++;

                numberPickerForFemale.setText(String.valueOf(numberOfServicesForFemale));
                displayTotalItems.setText(String.valueOf(totalServices));

                totalPrice = servicePrice * totalServices;
                displayTotalPrice.setText(String.valueOf(totalPrice));
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

                totalPrice = servicePrice * totalServices;
                displayTotalPrice.setText(String.valueOf(totalPrice));
            }
        });

        buttonIncrement.setOnClickListener(view -> {
            if (numberOfServicesForMale < 5) {
                numberOfServicesForMale++;
                totalServices++;
                numberPicker.setText(String.valueOf(numberOfServicesForMale));
                displayTotalItems.setText(String.valueOf(totalServices));

                totalPrice = servicePrice * totalServices;
                displayTotalPrice.setText(String.valueOf(totalPrice));
            }
            else {
                Toast.makeText(this, "You Can't Choose More then 5", Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void SetDataToView() {
        //        Toolbar
        Objects.requireNonNull(getSupportActionBar()).setTitle(name);
        Glide.with(this).load(backgroundImage).placeholder(R.drawable.ic_account).into(imageBackground);

        displayServiceName.setText(name);
        displayServiceDescription.setText(description);
        displayRequiredTime.setText(requiredTime);
        displayServicePrice.setText(price);
    }

    @SuppressLint("LongLogTag")
    private void GetDataFromCard(){
        Intent intent = getIntent();

        name = intent.getStringExtra("Name");
        description = intent.getStringExtra("Description");
        serviceType = intent.getStringExtra("ServiceType");
        requiredTime = intent.getStringExtra("RequiredTime");
        price = intent.getStringExtra("Price");

        servicePrice = Integer.parseInt(price);

        iconUrl = intent.getStringExtra("IconUrl");
        backgroundImage = intent.getStringExtra("BackgroundImageUrl");
        name = intent.getStringExtra("Name");


        Log.i(TAG,"Name: " + name);
        Log.i(TAG,"Description: " + description);
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
    }

}