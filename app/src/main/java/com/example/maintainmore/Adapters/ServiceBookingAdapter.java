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

public class ServiceBookingAdapter extends RecyclerView.Adapter<ServiceBookingAdapter.viewHolder>{

    ArrayList<ServiceBookingModal> bookingModels;
    Context context;

    viewHolder.OnServiceBookingCardClickListener bookingCardClickListener;

    public ServiceBookingAdapter(ArrayList<ServiceBookingModal> bookingModels,
                                 Context context,
                                 viewHolder.OnServiceBookingCardClickListener bookingCardClickListener) {
        this.bookingModels = bookingModels;
        this.context = context;
        this.bookingCardClickListener = bookingCardClickListener;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.card_ongoing_bookings, parent, false);

        return new viewHolder(view, bookingCardClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {

        ServiceBookingModal models = bookingModels.get(position);

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
        return bookingModels.size();
    }

    public static class viewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView displayServiceName, displayServiceDescription, totalServices, displayTotalPrice;
        TextView displayTechnicianName, displayTechnicianEmail, displayTechnicianPhoneNumber;
        TextView displayStatus, serviceType;
        ImageView serviceIconUrl;

        OnServiceBookingCardClickListener cardClickListener;


        public viewHolder(@NonNull View itemView, OnServiceBookingCardClickListener cardClickListener) {
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

            this.cardClickListener = cardClickListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            cardClickListener.onBookingCardClick(getAdapterPosition());
        }

        public interface OnServiceBookingCardClickListener{
            void onBookingCardClick(int position);
        }
    }
}
