package com.example.maintainmore.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.maintainmore.Adapters.ServiceCanceledAdapter;
import com.example.maintainmore.Models.ServiceCanceledModal;
import com.example.maintainmore.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Objects;


public class BookingsCanceledFragment extends Fragment {


    FirebaseFirestore db;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    String userID;

    public BookingsCanceledFragment() {
        // Required empty public constructor
    }

    RecyclerView recyclerView_canceled_bookings;
    ArrayList<ServiceCanceledModal> canceledModals = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cancelled_bookings, container, false);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();

        userID = firebaseUser.getUid();

        recyclerView_canceled_bookings = view.findViewById(R.id.recycleView_canceled_bookings);


        db.collection("Bookings Cancelled").whereEqualTo("whoBookedService", userID).addSnapshotListener((value, error) -> {
            canceledModals.clear();

            assert value != null;
            for (DocumentSnapshot snapshot: value){

                if (Objects.equals(snapshot.getString("bookingStatus"), "Booked")) {

                    canceledModals.add(new ServiceCanceledModal(snapshot.getId(),
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
            ServiceCanceledAdapter canceledAdapter = new ServiceCanceledAdapter(canceledModals,getContext());
            recyclerView_canceled_bookings.setAdapter(canceledAdapter);
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView_canceled_bookings.setLayoutManager(linearLayoutManager);


        return view;
    }


}