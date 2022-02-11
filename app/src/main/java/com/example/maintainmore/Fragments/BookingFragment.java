package com.example.maintainmore.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.maintainmore.Adapters.ServiceBookingAdapter;
import com.example.maintainmore.Models.ServiceBookingModels;
import com.example.maintainmore.R;
import com.example.maintainmore.UpdateBookingActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;


public class BookingFragment extends Fragment
        implements ServiceBookingAdapter.viewHolder.OnServiceBookingCardClickListener {

    private static final String TAG = "BookingFragmentInfo";



    FirebaseFirestore db;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    String userID;

    public BookingFragment() {
        // Required empty public constructor
    }


    RecyclerView recyclerView_Users;

    ArrayList<ServiceBookingModels> bookingModels = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_booking, container, false);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();

        userID = firebaseUser.getUid();

        recyclerView_Users = view.findViewById(R.id.recycleView_bookings);


        db.collection("Bookings").whereEqualTo("whoBookedService", userID).addSnapshotListener((value, error) -> {
            bookingModels.clear();
            assert value != null;
            for (DocumentSnapshot snapshot: value){
                bookingModels.add(
                        new ServiceBookingModels(
                        snapshot.getId(),snapshot.getString("whoBookedService"),
                        snapshot.getString("serviceName"), snapshot.getString("serviceDescription"),
                        snapshot.getString("serviceType"), snapshot.getString("serviceIcon"),
                        snapshot.getString("visitingDate"), snapshot.getString("visitingTime"),
                        snapshot.getString("requiredTime"), snapshot.getString("bookingDate"),
                        snapshot.getString("bookingTime"), snapshot.getString("servicePrice"),
                        snapshot.getString("servicesForMale"),snapshot.getString("servicesForFemale"),
                        snapshot.getString("totalServices"), snapshot.getString("totalPrice"),
                        snapshot.getString("cancelTillHour"), snapshot.getString("serviceStatus")
                        )
                );
            }
            ServiceBookingAdapter bookingAdapter = new ServiceBookingAdapter(bookingModels, getContext(),this);
            recyclerView_Users.setAdapter(bookingAdapter);

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
            recyclerView_Users.setLayoutManager(linearLayoutManager);

        });




        return view;
    }


    @Override
    public void onBookingCardClick(int position) {


        String bookingID = bookingModels.get(position).getBookingID();
        String whoBookedService = bookingModels.get(position).getUserID();
        String serviceName = bookingModels.get(position).getServiceName();
        String serviceDescription = bookingModels.get(position).getServiceDescription();
        String serviceRequiredTime = bookingModels.get(position).getServiceRequiredTime();
        String servicePrice = bookingModels.get(position).getServicePrice();
        String totalServicesPrice = bookingModels.get(position).getTotalServicesPrice();
        String visitingDate = bookingModels.get(position).getVisitingDate();
        String visitingTime = bookingModels.get(position).getVisitingTime();
        String serviceType = bookingModels.get(position).getServiceType();
        String servicesForMale = bookingModels.get(position).getServicesForMale();
        String servicesForFemale = bookingModels.get(position).getServicesForFemale();
        String totalServices = bookingModels.get(position).getTotalServices();
        String serviceCancellationTime = bookingModels.get(position).getCancellationTill();


        Intent intent = new Intent(getContext(), UpdateBookingActivity.class);
        intent.putExtra("bookingID", bookingID);
        intent.putExtra("serviceName", serviceName);
        intent.putExtra("serviceDescription", serviceDescription);
        intent.putExtra("servicesForMale", servicesForMale);
        intent.putExtra("servicesForFemale", servicesForFemale);
        intent.putExtra("serviceRequiredTime", serviceRequiredTime);
        intent.putExtra("servicePrice", servicePrice);
        intent.putExtra("visitingDate", visitingDate);
        intent.putExtra("visitingTime", visitingTime);
        intent.putExtra("totalServices", totalServices);
        intent.putExtra("totalServicesPrice", totalServicesPrice);
        intent.putExtra("serviceCancellationTime", serviceCancellationTime);


        startActivity(intent);

        Log.i(TAG,"who booked service: " + bookingID);
        Log.i(TAG,"serviceID: " + whoBookedService);
        Log.i(TAG,"Name: " + serviceName);
        Log.i(TAG,"Description: " + serviceDescription);
        Log.i(TAG,"Required time: " + serviceRequiredTime);
        Log.i(TAG,"Service Price: " + servicePrice);
        Log.i(TAG,"Total Price: " + totalServicesPrice);
        Log.i(TAG,"Visiting Date: " + visitingDate);
        Log.i(TAG,"Visiting Time: " + visitingTime);
        Log.i(TAG,"Service Type: " + serviceType);
        Log.i(TAG,"Service for Male: " + servicesForMale);
        Log.i(TAG,"Service for Female: " + servicesForFemale);
        Log.i(TAG,"Total Services: " + totalServices);
        Log.i(TAG,"serviceCancellationTime: " + serviceCancellationTime);
    }
}