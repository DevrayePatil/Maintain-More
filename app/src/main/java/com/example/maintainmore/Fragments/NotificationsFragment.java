package com.example.maintainmore.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.maintainmore.Modals.NotificationsModal;
import com.example.maintainmore.R;

import java.util.ArrayList;


public class NotificationsFragment extends Fragment {


    RecyclerView recycleView_notifications;
    ImageView emptyNotifications;

    public NotificationsFragment() {
        // Required empty public constructor
    }

    ArrayList<NotificationsModal> notificationsModals = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notifications, container, false);

        recycleView_notifications = view.findViewById(R.id.recycleView_notifications);
        emptyNotifications = view.findViewById(R.id.emptyNotifications);


        if (!notificationsModals.isEmpty()){
            recycleView_notifications.setVisibility(View.VISIBLE);
            emptyNotifications.setVisibility(View.INVISIBLE);
        } else {
            recycleView_notifications.setVisibility(View.INVISIBLE);
            emptyNotifications.setVisibility(View.VISIBLE);
        }

        return view;
    }
}