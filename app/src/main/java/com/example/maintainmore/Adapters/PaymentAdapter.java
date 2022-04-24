package com.example.maintainmore.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.maintainmore.Modals.PaymentsModal;
import com.example.maintainmore.R;

import java.util.ArrayList;


public class PaymentAdapter extends RecyclerView.Adapter<PaymentAdapter.ViewHolder>{

    ArrayList<PaymentsModal> paymentsModals;
    Context context;

    public PaymentAdapter(ArrayList<PaymentsModal> paymentsModals, Context context) {
        this.paymentsModals = paymentsModals;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.card_payments, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        PaymentsModal modal = paymentsModals.get(position);

        holder.displayPaymentID.setText(modal.getPaymentID());
        holder.displayBookingID.setText(modal.getBookingID());
        holder.displayPaidAmount.setText(modal.getAmountPaid());

        holder.displayPaymentDate.setText(modal.getPaymentDate());
        holder.displayPaymentTime.setText(modal.getPaymentTime());

        if (modal.getPaymentStatus().equals("Dr")){
            holder.displayPaymentStatus.setText(modal.getPaymentStatus());
            holder.displayPaymentStatus.setTextColor(context.getResources().getColor(R.color.color_red));
        }
        else if (modal.getPaymentStatus().equals("Cr")){
            holder.displayPaymentStatus.setText(modal.getPaymentStatus());
            holder.displayPaymentStatus.setTextColor(context.getResources().getColor(R.color.color_green));
        }



    }

    @Override
    public int getItemCount() {
        return paymentsModals.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView displayPaymentID, displayBookingID, displayPaidAmount;
        TextView displayPaymentDate, displayPaymentTime, displayPaymentStatus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            displayPaymentID = itemView.findViewById(R.id.displayPaymentID);
            displayBookingID = itemView.findViewById(R.id.displayBookingID);
            displayPaidAmount = itemView.findViewById(R.id.displayPaidAmount);
            displayPaymentDate = itemView.findViewById(R.id.displayPaymentDate);
            displayPaymentTime = itemView.findViewById(R.id.displayPaymentTime);
            displayPaymentStatus = itemView.findViewById(R.id.displayPaymentStatus);
        }
    }
}
