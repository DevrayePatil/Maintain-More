package com.example.maintainmore.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.maintainmore.Models.ServiceCardModels;
import com.example.maintainmore.R;

import java.util.ArrayList;

public class ServicesAdapter extends RecyclerView.Adapter<ServicesAdapter.viewHolder> {

    ArrayList<ServiceCardModels> serviceCardModels;
    Context context;

    viewHolder.OnServiceClickListener onServiceClickListener;

    public ServicesAdapter(ArrayList<ServiceCardModels> serviceCardModels, Context context, viewHolder.OnServiceClickListener onServiceClickListener) {
        this.serviceCardModels = serviceCardModels;
        this.context = context;

        this.onServiceClickListener = onServiceClickListener;
    }



    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.card_services,parent, false);
        return new viewHolder(view, onServiceClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {

        ServiceCardModels models = serviceCardModels.get(position);

        holder.imageView.setImageResource(models.getPicture());
        holder.textView.setText(models.getName());

    }

    @Override
    public int getItemCount() {
        return serviceCardModels.size();
    }



    public static class viewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView imageView;
        TextView textView;

        OnServiceClickListener onServiceClickListener;

        public viewHolder(@NonNull View itemView, OnServiceClickListener onServiceClickListener) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageView);
            textView = itemView.findViewById(R.id.textView);

            this.onServiceClickListener = onServiceClickListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onServiceClickListener.onServiceClick(getAdapterPosition());
        }

        public interface OnServiceClickListener{
            void onServiceClick(int position);
        }
    }
}
