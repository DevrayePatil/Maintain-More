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

import com.example.maintainmore.Adapters.HomeServiceAdapter;
import com.example.maintainmore.Adapters.ImageSlideAdapter;
import com.example.maintainmore.Adapters.PersonalServicesAdapter;
import com.example.maintainmore.Adapters.RepairApplianceAdapter;
import com.example.maintainmore.Adapters.ServicesAdapter;
import com.example.maintainmore.BookServiceActivity;
import com.example.maintainmore.Modals.HomeServiceModal;
import com.example.maintainmore.Modals.RepairApplianceModal;
import com.example.maintainmore.Modals.ServiceCardModal;
import com.example.maintainmore.Modals.PersonalServicesModal;
import com.example.maintainmore.R;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;


public class HomeFragment extends Fragment implements ServicesAdapter.viewHolder.OnServiceClickListener,
        PersonalServicesAdapter.viewHolder.OnPersonalServiceClickListener,
        HomeServiceAdapter.ViewHolder.OnHomeServiceClickListener,
        RepairApplianceAdapter.ViewHolder.OnRepairAppliancesClickListener
        {

    private static final String TAG = "HomeFragmentInfo";

    public HomeFragment() {
        // Required empty public constructor
    }
    RecyclerView recyclerView_PersonalServices, recyclerView_HomeServices, recyclerView_RepairAppliances;
    SliderView imageSliderCarousel;

    FirebaseFirestore db;

    ArrayList<PersonalServicesModal> personalServicesModals = new ArrayList<>();
    ArrayList<HomeServiceModal> homeServiceModals = new ArrayList<>();
    ArrayList<RepairApplianceModal> repairApplianceModals = new ArrayList<>();

    ArrayList<ServiceCardModal> homeServiceServiceCardModels = new ArrayList<>();




            @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        imageSliderCarousel = view.findViewById(R.id.imageSliderCarousel);
        recyclerView_PersonalServices = view.findViewById(R.id.recycleView_PersonalServices);
        recyclerView_HomeServices = view.findViewById(R.id.recycleView_HomeServices);
        recyclerView_RepairAppliances = view.findViewById(R.id.recycleView_RepairAppliances);

        Log.i(TAG,"you are in HomeFragment");


        ArrayList<ServiceCardModal> imageView = new ArrayList<>();

        imageView.add(new ServiceCardModal(R.drawable.slider_image,""));
        imageView.add(new ServiceCardModal(R.drawable.slider_image_1,""));
        imageView.add(new ServiceCardModal(R.drawable.slider_image_2,""));
        imageView.add(new ServiceCardModal(R.drawable.slider_image_3,""));
        imageView.add(new ServiceCardModal(R.drawable.slider_image_4,""));

        ImageSlideAdapter slideAdapter = new ImageSlideAdapter(imageView, getContext());
        imageSliderCarousel.setSliderAdapter(slideAdapter);
        imageSliderCarousel.setIndicatorAnimation(IndicatorAnimationType.SLIDE);
        imageSliderCarousel.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        imageSliderCarousel.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_RIGHT);

        imageSliderCarousel.setAutoCycle(true);
        imageSliderCarousel.startAutoCycle();


        db = FirebaseFirestore.getInstance();

        db.collection("Personal Services").addSnapshotListener((value, error) -> {
            personalServicesModals.clear();
            assert value != null;
            for (DocumentSnapshot snapshot: value){
                personalServicesModals.add(new PersonalServicesModal(
                        snapshot.getString("serviceType"), snapshot.getString("serviceName"),
                        snapshot.getString("serviceDescription"),snapshot.getString("requiredTime"),
                        snapshot.getString("servicePrice"), snapshot.getString("iconUrl"),
                        snapshot.getString("backgroundImageUrl")
                        )
                );
            }
            PersonalServicesAdapter servicesAdapter = new PersonalServicesAdapter(personalServicesModals, getContext(),this);
            recyclerView_PersonalServices.setAdapter(servicesAdapter);

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false );
            recyclerView_PersonalServices.setLayoutManager(linearLayoutManager);
        });


        db.collection("Home Services").addSnapshotListener((value, error) -> {
            homeServiceModals.clear();
            assert value != null;
            for (DocumentSnapshot snapshot: value){
                homeServiceModals.add(new HomeServiceModal(
                                snapshot.getString("serviceType"), snapshot.getString("serviceName"),
                                snapshot.getString("serviceDescription"),snapshot.getString("requiredTime"),
                                snapshot.getString("servicePrice"), snapshot.getString("iconUrl"),
                                snapshot.getString("backgroundImageUrl")
                        )
                );
            }
            HomeServiceAdapter homeServiceAdapter = new HomeServiceAdapter(homeServiceModals, getContext(),this);
            recyclerView_HomeServices.setAdapter(homeServiceAdapter);


        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false );
        recyclerView_HomeServices.setLayoutManager(linearLayoutManager);


        db.collection("Repair Appliance Services").addSnapshotListener((value, error) -> {
            repairApplianceModals.clear();
            assert value != null;
            for (DocumentSnapshot snapshot: value){
                repairApplianceModals.add(new RepairApplianceModal(
                                snapshot.getString("serviceType"), snapshot.getString("serviceName"),
                                snapshot.getString("serviceDescription"),snapshot.getString("requiredTime"),
                                snapshot.getString("servicePrice"), snapshot.getString("iconUrl"),
                                snapshot.getString("backgroundImageUrl")
                        )
                );
            }
            RepairApplianceAdapter repairApplianceAdapter = new RepairApplianceAdapter(repairApplianceModals, getContext(),this);
            recyclerView_RepairAppliances.setAdapter(repairApplianceAdapter);

        });

        LinearLayoutManager HomeAppliancesLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false );
        recyclerView_RepairAppliances.setLayoutManager(HomeAppliancesLayoutManager);



        return view;
    }


    @Override
    public void onServiceClick(int position) {
        String name = homeServiceServiceCardModels.get(position).getName();
        Log.i(TAG,"Name: " + name);

        Toast.makeText(getContext(),"Item Clicked  " + position + " " + name , Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPersonalServiceClick(int position) {
         String name = personalServicesModals.get(position).getName();
         String description = personalServicesModals.get(position).getDescription();
         String serviceType = personalServicesModals.get(position).getServiceType();
         String requiredTime = personalServicesModals.get(position).getTimeRequired();
         String price = personalServicesModals.get(position).getPrice();

         String iconUrl = personalServicesModals.get(position).getIconUrl();
         String backgroundImageUrl = personalServicesModals.get(position).getBackgroundImageUrl();


        Log.i(TAG,"Name: " + name);
        Log.i(TAG,"Description: " + description);
        Log.i(TAG,"ServiceType: " + serviceType);
        Log.i(TAG,"Required time: " + requiredTime);
        Log.i(TAG,"Price: " + price);

        Log.i(TAG,"iconUrl: " + iconUrl);
        Log.i(TAG,"backgroundImageUrl: " + backgroundImageUrl);

        Intent intent = new Intent(getActivity(), BookServiceActivity.class);

        intent.putExtra("Name", name);
        intent.putExtra("Description", description);
        intent.putExtra("ServiceType", serviceType);
        intent.putExtra("RequiredTime", requiredTime);
        intent.putExtra("Price", price);

        intent.putExtra("IconUrl",iconUrl);
        intent.putExtra("BackgroundImageUrl",backgroundImageUrl);


        startActivity(intent);
    }

    @Override
    public void onHomeServiceClickListener(int position) {
        String name = homeServiceModals.get(position).getName();
        String description = homeServiceModals.get(position).getDescription();
        String serviceType = homeServiceModals.get(position).getServiceType();
        String requiredTime = homeServiceModals.get(position).getTimeRequired();
        String price = homeServiceModals.get(position).getPrice();

        String iconUrl = homeServiceModals.get(position).getIconUrl();
        String backgroundImageUrl = homeServiceModals.get(position).getBackgroundImageUrl();

        Intent intent = new Intent(getActivity(), BookServiceActivity.class);

        intent.putExtra("Name", name);
        intent.putExtra("Description", description);
        intent.putExtra("ServiceType", serviceType);
        intent.putExtra("RequiredTime", requiredTime);
        intent.putExtra("Price", price);

        intent.putExtra("IconUrl",iconUrl);
        intent.putExtra("BackgroundImageUrl",backgroundImageUrl);


        startActivity(intent);
    }

    @Override
    public void onRepairApplianceClickListener(int position) {

        String name = repairApplianceModals.get(position).getName();
        String description = repairApplianceModals.get(position).getDescription();
        String serviceType = repairApplianceModals.get(position).getServiceType();
        String requiredTime = repairApplianceModals.get(position).getTimeRequired();
        String price = repairApplianceModals.get(position).getPrice();

        String iconUrl = repairApplianceModals.get(position).getIconUrl();
        String backgroundImageUrl = repairApplianceModals.get(position).getBackgroundImageUrl();

        Intent intent = new Intent(getActivity(), BookServiceActivity.class);

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