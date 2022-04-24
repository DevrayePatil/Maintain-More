package com.example.maintainmore;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static com.example.maintainmore.App.CATEGORY_ID;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.core.splashscreen.SplashScreen;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Notification;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import java.util.Objects;

public class ServiceCompletedActivity extends AppCompatActivity {

    private static final String TAG = "ServiceCompletedActivityInfo";
    private static final int READ_EXTERNAL_STORAGE_CODE = 1;



    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseFirestore db;

    Toolbar toolbar;

    TextView displayTechnicianName, displayTechnicianEmail, displayTechnicianPhoneNumber;
    TextView displayServiceName, displayTotalServices, displayServicePrice, displayTotalAmount;

    String userID, bookingID, assignedTechnician;

    Button buttonCancel, buttonInvoice;

    NotificationManagerCompat notificationManagerCompat;

    String userName, userPhoneNumber, userEmail;
    String technicianName, technicianPhoneNumber, technicianEmail;
    String bookingDate, bookingTime, visitingDate, visitingTime;
    String serviceName;

    int servicePrice, totalServices;


    ActivityResultLauncher<Intent> activityResultLauncher;

    String [] permissions = {READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE};



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SplashScreen.installSplashScreen(this);
        setContentView(R.layout.activity_service_completed);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        db = FirebaseFirestore.getInstance();

        userID = Objects.requireNonNull(firebaseUser).getUid();


        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Service Details");

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        notificationManagerCompat = NotificationManagerCompat.from(this);



        displayTechnicianName = findViewById(R.id.displayTechnicianName);
        displayTechnicianEmail = findViewById(R.id.displayTechnicianEmail);
        displayTechnicianPhoneNumber = findViewById(R.id.displayTechnicianPhoneNumber);

        displayServiceName = findViewById(R.id.displayServiceName);
        displayTotalServices = findViewById(R.id.displayTotalServices);
        displayServicePrice = findViewById(R.id.displayServicePrice);
        displayTotalAmount = findViewById(R.id.displayTotalAmount);

        buttonCancel = findViewById(R.id.buttonCancel);
        buttonInvoice = findViewById(R.id.buttonInvoice);

        getDataFromPreviousActivity();

        getUserInfo();
        getTechnicianInfo();
        getBookingDetails();

        setDataToViews();



        buttonCancel.setOnClickListener(v -> finish());
        buttonInvoice.setOnClickListener(v -> {
            if (checkPermission()){

                generateInvoice();

            }
            else {
                requestStoragePermission();
            }
        });

        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {

            if (result.getResultCode() == Activity.RESULT_OK){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
                    if (Environment.isExternalStorageManager()){
                        Toast.makeText(ServiceCompletedActivity.this, "Permission Granted", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(ServiceCompletedActivity.this, "Permission Not Granted", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


    }

    private void setDataToViews() {



    }


    private void generateInvoice() {

        Bitmap bitmap, scaledBitmap;


        int pageWidth = 1200;
        int pageHeight = 1800;


        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.icon);
        scaledBitmap = Bitmap.createScaledBitmap(bitmap, 200, 200, false);


        PdfDocument pdfDocument = new PdfDocument();
        Paint paint = new Paint();

        Paint titlePaint = new Paint();

        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(pageWidth, pageHeight, 1).create();

        PdfDocument.Page page = pdfDocument.startPage(pageInfo);

        Canvas canvas = page.getCanvas();


        canvas.drawRect(0, 0, pageWidth, 300, paint);

        canvas.drawBitmap(scaledBitmap, 0,0, paint);

        titlePaint.setColor(Color.WHITE);
        titlePaint.setTextAlign(Paint.Align.CENTER);
        titlePaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        titlePaint.setTextSize(70f);
        canvas.drawText("Maintain More", pageWidth/2, 100, titlePaint);
        titlePaint.setTextSize(30f);
        canvas.drawText("Personal Service | Home Service | Repair Appliances", pageWidth/2, 150, titlePaint);

        int textY = 400;

        titlePaint.setColor(Color.BLACK);
        titlePaint.setTextAlign(Paint.Align.CENTER);
        titlePaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        titlePaint.setTextSize(70f);
        canvas.drawText("Invoice",pageWidth/2, textY, titlePaint);

        textY += 100;

        paint.setTextAlign(Paint.Align.LEFT);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        paint.setTextSize(40f);
        canvas.drawText("Customer Details", 20, textY, paint);

        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        paint.setTextSize(35f);
        textY += 50;
        canvas.drawText("Customer Name: "+ userName , 20, textY, paint);
        textY += 50;
        canvas.drawText("Phone Number: "+ userPhoneNumber , 20, textY, paint);
        textY += 50;
        canvas.drawText("E-mail: "+ userEmail , 20, textY, paint);

        canvas.drawText("Booking Date: "+ bookingDate , pageWidth - (pageWidth/3), textY - 100, paint);
        canvas.drawText("Booking Time: "+ bookingTime , pageWidth - (pageWidth/3), textY - 50, paint);


        textY += 100;

        paint.setTextAlign(Paint.Align.LEFT);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        paint.setTextSize(40f);
        canvas.drawText("Technician Details", 20, textY, paint);

        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        paint.setTextSize(35f);
        textY += 50;
        canvas.drawText("Technician Name: "+ technicianName , 20, textY, paint);
        textY += 50;
        canvas.drawText("Phone Number: "+ technicianPhoneNumber , 20, textY, paint);
        textY += 50;
        canvas.drawText("E-mail: "+ technicianEmail , 20, textY, paint);

        canvas.drawText("Visiting Date: "+ visitingDate , pageWidth - (pageWidth/3), textY - 100, paint);
        canvas.drawText("Visiting Time: "+ visitingTime , pageWidth - (pageWidth/3), textY - 50, paint);


//        Table Structure
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(2);
        textY += 50;
//      y = 650
        canvas.drawRect(20, textY, pageWidth - 20, textY + 80, paint);

        paint.setTextAlign(Paint.Align.LEFT);
        paint.setStyle(Paint.Style.FILL);
        textY += 50;
//        y = 700
        canvas.drawText("Sr. No.", 40, textY, paint);
        canvas.drawText("Service Name", 200, textY, paint);
        canvas.drawText("Price", 700, textY, paint);
        canvas.drawText("Qty.", 900, textY, paint);
        canvas.drawText("Total", 1050, textY, paint);

        int startY = 980;
        int stopY = 1010;

        canvas.drawLine(180, startY, 180, stopY, paint);
        canvas.drawLine(680, startY, 680, stopY, paint);
        canvas.drawLine(880, startY, 880, stopY, paint);
        canvas.drawLine(1030, startY, 1030, stopY, paint);

//        Add Items

        float total;

        textY += 100;
//        y = 800
        canvas.drawText("1.", 40, textY, paint);
        canvas.drawText(serviceName, 200, textY, paint);
        canvas.drawText(String.valueOf(servicePrice), 700, textY, paint);
        canvas.drawText(String.valueOf(totalServices), 900, textY, paint);

        total = Float.parseFloat(String.valueOf(totalServices * servicePrice));

        paint.setTextAlign(Paint.Align.RIGHT);
        canvas.drawText(String.valueOf(total), pageWidth - 40, textY, paint);

        paint.setTextAlign(Paint.Align.LEFT);
        paint.setColor(Color.BLACK);
        textY+=100;
//        y =  900
        canvas.drawRect(20, textY, pageWidth - 20, textY + 100, paint);

        paint.setColor(Color.WHITE);
        paint.setTextSize(50f);
        paint.setTextAlign(Paint.Align.LEFT);
        canvas.drawText("Total", 40, textY + 65, paint);

        paint.setTextAlign(Paint.Align.RIGHT);
        canvas.drawText(String.valueOf(total), pageWidth - 40, textY + 65, paint);





        pdfDocument.finishPage(page);

        File fullPath = new File(Environment.getExternalStorageDirectory(), "MaintainMore");

        if (fullPath.exists()){
            Log.i("TAG", "already exist");
        }
        else {
            fullPath.mkdir();
        }

        try {


//            String fileName = "Invoice-" + System.currentTimeMillis() + ".pdf";
            String fileName = "Invoice.pdf";

            File file = new File(fullPath, fileName);

            pdfDocument.writeTo(new FileOutputStream(file));

            pushNotifications("Invoice", fileName + " Download Successful");

        }
        catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "err:" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        pdfDocument.close();

    }

    public void pushNotifications(String title, String massage){

        Notification notification = new NotificationCompat.Builder(this, CATEGORY_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(title)
                .setContentText(massage)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .build();

        notificationManagerCompat.notify(10, notification);
    }

    private boolean checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            return Environment.isExternalStorageManager();
        } else {
            int checkReadStoragePermission = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);
            int checkWriteStoragePermission = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);

            return checkReadStoragePermission == PackageManager.PERMISSION_GRANTED
                    && checkWriteStoragePermission == PackageManager.PERMISSION_GRANTED;
        }
    }

    private void requestStoragePermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            try {
                Intent intent = new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                intent.addCategory("android.intent.category.DEFAULT");
                intent.setData(Uri.parse(String.format("package:%s", getApplicationContext().getPackageName())));
                activityResultLauncher.launch(intent);

            }
            catch (Exception e){
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                activityResultLauncher.launch(intent);
            }
        }
        else {
            ActivityCompat.requestPermissions(ServiceCompletedActivity.this, permissions, READ_EXTERNAL_STORAGE_CODE);
        }
    }

    @SuppressLint("LongLogTag")
    private void getBookingDetails(){
        db.collection("Bookings Completed").document(bookingID)
                .get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    serviceName = document.getString("serviceName");

                    bookingDate = document.getString("bookingDate");
                    bookingTime = document.getString("bookingTime");
                    visitingDate = document.getString("visitingDate");
                    visitingTime = document.getString("visitingTime");

                    servicePrice = Integer.parseInt(Objects.requireNonNull(document.getString("servicePrice")));
                    totalServices = Integer.parseInt(Objects.requireNonNull(document.getString("totalServices")));


                    displayServiceName.setText(serviceName);
                    displayTotalServices.setText(String.valueOf(totalServices));
                    displayServicePrice.setText(String.valueOf(servicePrice));

                    int totalAmount = servicePrice * totalServices;
                    displayTotalAmount.setText(String.valueOf(totalAmount));
                } else {
                    Log.d(TAG, "No such document");
                }
            } else {
                Log.d(TAG, "get failed with ", task.getException());
            }
        });
    }

    @SuppressLint("LongLogTag")
    private void getTechnicianInfo(){

        db.collection("Technicians").document(assignedTechnician)
                .get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    technicianName = document.getString("name");
                    technicianPhoneNumber = document.getString("phoneNumber");
                    technicianEmail = document.getString("email");

                    displayTechnicianName.setText(technicianName);
                    displayTechnicianEmail.setText(technicianEmail);
                    displayTechnicianPhoneNumber.setText(technicianPhoneNumber);
                } else {
                    Log.d(TAG, "No such document");
                }
            } else {
                Log.d(TAG, "get failed with ", task.getException());
            }
        });

    }

    @SuppressLint("LongLogTag")
    private void getUserInfo() {
        db.collection("Users").document(userID)
                .get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    userName = document.getString("name");
                    userPhoneNumber = document.getString("phoneNumber");
                    userEmail = document.getString("email");
                } else {
                    Log.d(TAG, "No such document");
                }
            } else {
                Log.d(TAG, "get failed with ", task.getException());
            }
        });
    }

    private void getDataFromPreviousActivity() {

        Intent intent = getIntent();

        bookingID = intent.getStringExtra("bookingID");
        assignedTechnician = intent.getStringExtra("assignedTechnician");

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == READ_EXTERNAL_STORAGE_CODE){
            if (grantResults.length > 0 ){

                boolean readStoragePermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                boolean writeStoragePermission = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                if (readStoragePermission && writeStoragePermission) {
                    Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(this, "Permission not Granted", Toast.LENGTH_SHORT).show();
                }
            }
            else {
                Toast.makeText(this, "Permission not Granted", Toast.LENGTH_SHORT).show();
            }
        }
    }
}