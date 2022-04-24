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
import android.widget.ImageView;

import com.example.maintainmore.Adapters.ServiceBookingAdapter;
import com.example.maintainmore.Modals.ServiceBookingModal;
import com.example.maintainmore.R;
import com.example.maintainmore.UpdateBookingActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Objects;


public class BookingsOngoingFragment extends Fragment
        implements ServiceBookingAdapter.viewHolder.OnServiceBookingCardClickListener {

    private static final String TAG = "BookingsOngoingFragment";


    FirebaseFirestore db;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    String userID;

    public BookingsOngoingFragment() {
        // Required empty public constructor
    }

    RecyclerView recyclerView_ongoing_bookings;
    ImageView emptyBookings;

    ArrayList<ServiceBookingModal> bookingModels = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ongoing_booking, container, false);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();

        userID = firebaseUser.getUid();

        recyclerView_ongoing_bookings = view.findViewById(R.id.recycleView_ongoing_bookings);
        emptyBookings = view.findViewById(R.id.emptyBookings);


        db.collection("Bookings").whereEqualTo("whoBookedService", userID).addSnapshotListener((value, error) -> {
            bookingModels.clear();

            assert value != null;
            for (DocumentSnapshot snapshot: value){

                if (Objects.equals(snapshot.getString("bookingStatus"), "Booked")) {

                    bookingModels.add(new ServiceBookingModal(snapshot.getId(),
                        snapshot.getString("whoBookedService"),snapshot.getString("assignedTechnician"),
                        snapshot.getString("serviceName"), snapshot.getString("serviceDescription"),
                        snapshot.getString("serviceType"), snapshot.getString("serviceIcon"),
                        snapshot.getString("visitingDate"), snapshot.getString("visitingTime"),
                        snapshot.getString("requiredTime"), snapshot.getString("bookingDate"),
                        snapshot.getString("bookingTime"), snapshot.getString("servicePrice"),
                        snapshot.getString("servicesForMale"), snapshot.getString("servicesForFemale"),
                        snapshot.getString("totalServices"),snapshot.getString("totalServicesPrice"),
                        snapshot.getString("cancelTillHour"), snapshot.getString("bookingStatus")
                    ));
                }
            }
            ServiceBookingAdapter bookingAdapter = new ServiceBookingAdapter(bookingModels,getContext(), this);
            recyclerView_ongoing_bookings.setAdapter(bookingAdapter);

            if(!bookingModels.isEmpty()){
                recyclerView_ongoing_bookings.setVisibility(View.VISIBLE);
                emptyBookings.setVisibility(View.INVISIBLE);
            }
            else {
                recyclerView_ongoing_bookings.setVisibility(View.INVISIBLE);
                emptyBookings.setVisibility(View.VISIBLE);
            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView_ongoing_bookings.setLayoutManager(linearLayoutManager);


        return view;
    }

    @Override
    public void onBookingCardClick(int position) {

        String bookingID = bookingModels.get(position).getBookingID();
        String whoBookedService = bookingModels.get(position).getUserID();
        String assignedTechnician = bookingModels.get(position).getAssignedTechnician();
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
        String serviceStatus = bookingModels.get(position).getServiceStatus();


        Intent intent = new Intent(getContext(), UpdateBookingActivity.class);
        intent.putExtra("bookingID", bookingID);
        intent.putExtra("assignedTechnician", assignedTechnician);
        intent.putExtra("serviceName", serviceName);
        intent.putExtra("serviceDescription", assignedTechnician);
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
        Log.i(TAG,"serviceStatus: " + serviceStatus);
    }
}