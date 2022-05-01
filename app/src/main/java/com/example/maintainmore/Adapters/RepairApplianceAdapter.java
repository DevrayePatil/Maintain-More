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
import com.example.maintainmore.Modals.RepairApplianceModal;
import com.example.maintainmore.R;

import java.util.ArrayList;

public class RepairApplianceAdapter extends RecyclerView.Adapter<RepairApplianceAdapter.ViewHolder>{

    ArrayList<RepairApplianceModal> repairApplianceModals;

    Context context;

    ViewHolder.OnRepairAppliancesClickListener repairAppliancesClickListener;

    public RepairApplianceAdapter(ArrayList<RepairApplianceModal> repairApplianceModals, Context context,
                                  ViewHolder.OnRepairAppliancesClickListener repairAppliancesClickListener) {
        this.repairApplianceModals = repairApplianceModals;
        this.context = context;
        this.repairAppliancesClickListener = repairAppliancesClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.card_services,parent, false);

        return new RepairApplianceAdapter.ViewHolder(view, repairAppliancesClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        RepairApplianceModal modal = repairApplianceModals.get(position);

        holder.textView.setText(modal.getName());

        Glide.with(context).load(modal.getIconUrl()).placeholder(R.drawable.ic_account).into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        return repairApplianceModals.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView imageView;
        TextView textView;

        OnRepairAppliancesClickListener repairAppliancesClickListener;

        public ViewHolder(@NonNull View itemView, OnRepairAppliancesClickListener repairAppliancesClickListener) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageView);
            textView = itemView.findViewById(R.id.textView);

            this.repairAppliancesClickListener = repairAppliancesClickListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            repairAppliancesClickListener.onRepairApplianceClickListener(getAdapterPosition());
        }

        public interface OnRepairAppliancesClickListener{
            void onRepairApplianceClickListener(int position);
        }
    }
}
