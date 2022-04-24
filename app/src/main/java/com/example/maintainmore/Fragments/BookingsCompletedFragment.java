package com.example.maintainmore.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.maintainmore.Adapters.ServiceCompletedAdapter;
import com.example.maintainmore.Modals.ServiceBookingModal;
import com.example.maintainmore.R;
import com.example.maintainmore.ServiceCompletedActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Objects;


public class BookingsCompletedFragment extends Fragment
        implements ServiceCompletedAdapter.ViewHolder.OnServiceCompletedCardClickListener {

//    private static final String TAG = "BookingsCompletedFragmentInfo";

    FirebaseFirestore db;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    String userID;

    public BookingsCompletedFragment() {
        // Required empty public constructor
    }

    RecyclerView recyclerView_completed_bookings;
    ArrayList<ServiceBookingModal> bookingModels = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_completed_bookings, container, false);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();

        userID = firebaseUser.getUid();

        recyclerView_completed_bookings = view.findViewById(R.id.recycleView_completed_bookings);


        db.collection("Bookings Completed").whereEqualTo("whoBookedService", userID).addSnapshotListener((value, error) -> {
            bookingModels.clear();

            assert value != null;
            for (DocumentSnapshot snapshot: value){

                if (Objects.equals(snapshot.getString("bookingStatus"), "Completed")) {

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
            ServiceCompletedAdapter serviceCompletedAdapter = new ServiceCompletedAdapter(bookingModels,getContext(), this);
            recyclerView_completed_bookings.setAdapter(serviceCompletedAdapter);
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView_completed_bookings.setLayoutManager(linearLayoutManager);


        return view;
    }


    @Override
    public void onServiceCompletedListener(int position) {

        String bookingID = bookingModels.get(position).getBookingID();
        String assignedTechnician = bookingModels.get(position).getAssignedTechnician();

        Intent intent = new Intent(requireActivity(), ServiceCompletedActivity.class);

        intent.putExtra("bookingID", bookingID);
        intent.putExtra("assignedTechnician", assignedTechnician);

        startActivity(intent);

    }
}