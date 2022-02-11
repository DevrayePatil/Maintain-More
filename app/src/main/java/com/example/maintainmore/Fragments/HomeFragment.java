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
import android.widget.Toast;

import com.example.maintainmore.Adapters.ImageSlideAdapter;
import com.example.maintainmore.Adapters.PersonalServicesAdapter;
import com.example.maintainmore.Adapters.ServicesAdapter;
import com.example.maintainmore.ServiceBookingActivity;
import com.example.maintainmore.Models.CardModels;
import com.example.maintainmore.Models.PersonalServicesModel;
import com.example.maintainmore.R;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;


public class HomeFragment extends Fragment implements ServicesAdapter.viewHolder.OnServiceClickListener,
        PersonalServicesAdapter.viewHolder.OnPersonalServiceClickListener
        {

    private static final String TAG = "HomeFragmentInfo";

    public HomeFragment() {
        // Required empty public constructor
    }
    RecyclerView recyclerView_PersonalServices, recyclerView_HomeServices, recyclerView_HomeAppliances;
    SliderView imageSliderCarousel;

    FirebaseFirestore db;

    ArrayList<PersonalServicesModel> personalServicesModels = new ArrayList<>();
    ArrayList<CardModels> HomeServiceCardModels = new ArrayList<>();




            @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        imageSliderCarousel = view.findViewById(R.id.imageSliderCarousel);
        recyclerView_PersonalServices = view.findViewById(R.id.recycleView_PersonalServices);
        recyclerView_HomeServices = view.findViewById(R.id.recycleView_HomeServices);
        recyclerView_HomeAppliances = view.findViewById(R.id.recycleView_HomeAppliances);

        Log.i(TAG,"you are in HomeFragment");


        ArrayList<CardModels> imageView = new ArrayList<>();

        imageView.add(new CardModels(R.drawable.image0,""));
        imageView.add(new CardModels(R.drawable.image1,""));
        imageView.add(new CardModels(R.drawable.image2,""));
        imageView.add(new CardModels(R.drawable.image3,""));
        imageView.add(new CardModels(R.drawable.image4,""));

        ImageSlideAdapter slideAdapter = new ImageSlideAdapter(imageView, getContext());
        imageSliderCarousel.setSliderAdapter(slideAdapter);
        imageSliderCarousel.setIndicatorAnimation(IndicatorAnimationType.SLIDE);
        imageSliderCarousel.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        imageSliderCarousel.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_RIGHT);

        imageSliderCarousel.setAutoCycle(true);
        imageSliderCarousel.startAutoCycle();


        db = FirebaseFirestore.getInstance();

        db.collection("Personal Services").addSnapshotListener((value, error) -> {
            personalServicesModels.clear();
            assert value != null;
            for (DocumentSnapshot snapshot: value){
                personalServicesModels.add(new PersonalServicesModel(
                        snapshot.getString("serviceType"), snapshot.getString("serviceName"),
                        snapshot.getString("serviceDescription"),snapshot.getString("requiredTime"),
                        snapshot.getString("servicePrice"), snapshot.getString("iconUrl"),
                        snapshot.getString("backgroundImageUrl")
                        )
                );
            }
            PersonalServicesAdapter servicesAdapter = new PersonalServicesAdapter(personalServicesModels, getContext(),this);
            recyclerView_PersonalServices.setAdapter(servicesAdapter);

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false );
            recyclerView_PersonalServices.setLayoutManager(linearLayoutManager);
        });




        HomeServiceCardModels.add(new CardModels(R.drawable.grapefruit, "Google"));
        HomeServiceCardModels.add(new CardModels(R.drawable.ic_google, "Google"));
        HomeServiceCardModels.add(new CardModels(R.drawable.ic_google, "Google is a service"));

        ServicesAdapter homeServicesAdapter = new ServicesAdapter(HomeServiceCardModels, getContext(),this);
        recyclerView_HomeServices.setAdapter(homeServicesAdapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false );
        recyclerView_HomeServices.setLayoutManager(layoutManager);


        ArrayList<CardModels> HomeAppliancesCardModels = new ArrayList<>();

        HomeAppliancesCardModels.add(new CardModels(R.drawable.grapefruit, "Google"));
        HomeAppliancesCardModels.add(new CardModels(R.drawable.ic_google, "Google"));
        HomeAppliancesCardModels.add(new CardModels(R.drawable.ic_google, "Google is a service"));

        ServicesAdapter homeAppliancesAdapter= new ServicesAdapter(HomeAppliancesCardModels, getContext(),this);
        recyclerView_HomeAppliances.setAdapter(homeAppliancesAdapter);

        LinearLayoutManager HomeAppliancesLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false );
        recyclerView_HomeAppliances.setLayoutManager(HomeAppliancesLayoutManager);



        return view;
    }


    @Override
    public void onServiceClick(int position) {
        String name = HomeServiceCardModels.get(position).getName();
        Log.i(TAG,"Name: " + name);

        Toast.makeText(getContext(),"Item Clicked  " + position + " " + name , Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPersonalServiceClick(int position) {
         String name = personalServicesModels.get(position).getName();
         String description = personalServicesModels.get(position).getDescription();
         String serviceType = personalServicesModels.get(position).getServiceType();
         String requiredTime = personalServicesModels.get(position).getTimeRequired();
         String price = personalServicesModels.get(position).getPrice();

         String iconUrl = personalServicesModels.get(position).getIconUrl();
         String backgroundImageUrl = personalServicesModels.get(position).getBackgroundImageUrl();


        Log.i(TAG,"Name: " + name);
        Log.i(TAG,"Description: " + description);
        Log.i(TAG,"ServiceType: " + serviceType);
        Log.i(TAG,"Required time: " + requiredTime);
        Log.i(TAG,"Price: " + price);

        Log.i(TAG,"iconUrl: " + iconUrl);
        Log.i(TAG,"backgroundImageUrl: " + backgroundImageUrl);

        Intent intent = new Intent(getActivity(), ServiceBookingActivity.class);

        intent.putExtra("Name", name);
        intent.putExtra("Description", description);
        intent.putExtra("ServiceType", serviceType);
        intent.putExtra("RequiredTime", requiredTime);
        intent.putExtra("Price", price);

        intent.putExtra("IconUrl",iconUrl);
        intent.putExtra("BackgroundImageUrl",backgroundImageUrl);


        startActivity(intent);
    }

}