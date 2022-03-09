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
import com.example.maintainmore.Models.ServiceCanceledModal;
import com.example.maintainmore.R;

import java.util.ArrayList;

public class ServiceCanceledAdapter extends RecyclerView.Adapter<ServiceCanceledAdapter.ViewHolder>{

    ArrayList<ServiceCanceledModal> serviceCanceledModals;
    Context context;

    public ServiceCanceledAdapter(ArrayList<ServiceCanceledModal> serviceCanceledModals, Context context) {
        this.serviceCanceledModals = serviceCanceledModals;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.card_cancelled_bookings, parent, false);

        return new ServiceCanceledAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        ServiceCanceledModal models = serviceCanceledModals.get(position);

        holder.displayServiceName.setText(models.getServiceName());
        holder.displayServiceDescription.setText(models.getServiceDescription());
        holder.totalServices.setText(models.getTotalServices());
        holder.displayTotalPrice.setText(models.getTotalServicesPrice());
        holder.displayStatus.setText(models.getServiceStatus());
        holder.serviceType.setText(models.getServiceType());

        Glide.with(context).load(models.getServiceIconUrl()).placeholder(R.drawable.ic_person).into(holder.serviceIconUrl);

    }

    @Override
    public int getItemCount() {
        return serviceCanceledModals.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView displayServiceName, displayServiceDescription, totalServices, displayTotalPrice;
        TextView displayTechnicianName, displayTechnicianEmail, displayTechnicianPhoneNumber;
        TextView displayStatus, serviceType;
        ImageView serviceIconUrl;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            displayServiceName = itemView.findViewById(R.id.displayServiceName);
            displayServiceDescription = itemView.findViewById(R.id.displayServiceDescription);
            serviceIconUrl = itemView.findViewById(R.id.imageViewIcon);
            totalServices = itemView.findViewById(R.id.totalServices);
            displayTotalPrice = itemView.findViewById(R.id.displayTotalPrice);
            displayStatus = itemView.findViewById(R.id.displayStatus);
            serviceType = itemView.findViewById(R.id.serviceType);

            displayTechnicianName = itemView.findViewById(R.id.displayTechnicianName);
            displayTechnicianEmail = itemView.findViewById(R.id.displayTechnicianEmail);
            displayTechnicianPhoneNumber = itemView.findViewById(R.id.displayTechnicianPhoneNumber);
        }
    }
}
