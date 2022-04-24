package com.example.maintainmore.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.maintainmore.Modals.ServiceBookingModal;
import com.example.maintainmore.R;

import java.util.ArrayList;

public class ServiceCompletedAdapter extends RecyclerView.Adapter<ServiceCompletedAdapter.ViewHolder>{

    ArrayList<ServiceBookingModal> serviceBookingModals;
    Context context;

    ViewHolder.OnServiceCompletedCardClickListener clickListener;

    public ServiceCompletedAdapter(ArrayList<ServiceBookingModal> serviceBookingModals, Context context,
                                   ViewHolder.OnServiceCompletedCardClickListener clickListener) {
        this.serviceBookingModals = serviceBookingModals;
        this.context = context;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.card_ongoing_bookings, parent, false);

        return new ServiceCompletedAdapter.ViewHolder(view, clickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        ServiceBookingModal models = serviceBookingModals.get(position);

        holder.displayServiceName.setText(models.getServiceName());
        holder.displayServiceDescription.setText(models.getServiceDescription());
        holder.totalServices.setText(models.getTotalServices());
        holder.displayTotalPrice.setText(models.getTotalServicesPrice());
        holder.displayStatus.setText(models.getServiceStatus());
        holder.serviceType.setText(models.getServiceType());

        holder.displayBookingDate.setText(models.getBookingDate());
        holder.displayBookingTime.setText(models.getBookingTime());
        holder.displayVisitingDate.setText(models.getVisitingDate());
        holder.displayVisitingTime.setText(models.getVisitingTime());

        Glide.with(context).load(models.getServiceIconUrl()).placeholder(R.drawable.ic_person).into(holder.serviceIconUrl);


    }

    @Override
    public int getItemCount() {
        return serviceBookingModals.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView displayServiceName, displayServiceDescription, totalServices, displayTotalPrice;
        TextView displayTechnicianName, displayTechnicianEmail, displayTechnicianPhoneNumber;
        TextView displayBookingDate, displayBookingTime, displayVisitingDate, displayVisitingTime;
        TextView displayStatus, serviceType;
        ImageView serviceIconUrl;

        OnServiceCompletedCardClickListener cardClickListener;

        public ViewHolder(@NonNull View itemView, OnServiceCompletedCardClickListener cardClickListener) {
            super(itemView);

            displayServiceName = itemView.findViewById(R.id.displayServiceName);
            displayServiceDescription = itemView.findViewById(R.id.displayServiceDescription);
            serviceIconUrl = itemView.findViewById(R.id.imageViewIcon);
            totalServices = itemView.findViewById(R.id.totalServices);
            displayTotalPrice = itemView.findViewById(R.id.displayTotalPrice);
            displayStatus = itemView.findViewById(R.id.displayStatus);
            serviceType = itemView.findViewById(R.id.serviceType);

            displayBookingDate = itemView.findViewById(R.id.displayBookingDate);
            displayBookingTime = itemView.findViewById(R.id.displayBookingTime);
            displayVisitingDate = itemView.findViewById(R.id.displayVisitingDate);
            displayVisitingTime = itemView.findViewById(R.id.displayVisitingTime);

            displayTechnicianName = itemView.findViewById(R.id.displayTechnicianName);
            displayTechnicianEmail = itemView.findViewById(R.id.displayTechnicianEmail);
            displayTechnicianPhoneNumber = itemView.findViewById(R.id.displayTechnicianPhoneNumber);

            this.cardClickListener = cardClickListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            cardClickListener.onServiceCompletedListener(getAdapterPosition());

        }


        public interface OnServiceCompletedCardClickListener{
            void onServiceCompletedListener(int position);
        }
    }


}
