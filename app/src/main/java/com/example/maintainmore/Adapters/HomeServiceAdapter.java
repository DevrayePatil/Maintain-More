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
import com.example.maintainmore.Modals.HomeServiceModal;
import com.example.maintainmore.R;

import java.util.ArrayList;

public class HomeServiceAdapter extends RecyclerView.Adapter<HomeServiceAdapter.ViewHolder>{

    ArrayList<HomeServiceModal> homeServiceModals;
    Context context;

    ViewHolder.OnHomeServiceClickListener homeServiceClickListener;

    public HomeServiceAdapter(ArrayList<HomeServiceModal> homeServiceModals, Context context,
                              ViewHolder.OnHomeServiceClickListener serviceClickListener) {
        this.homeServiceModals = homeServiceModals;
        this.context = context;
        this.homeServiceClickListener = serviceClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.card_services,parent, false);
        return new HomeServiceAdapter.ViewHolder(view, homeServiceClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HomeServiceModal modal = homeServiceModals.get(position);

        holder.textView.setText(modal.getName());

        Glide.with(context).load(modal.getIconUrl()).placeholder(R.drawable.ic_account).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return homeServiceModals.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView imageView;
        TextView textView;


        OnHomeServiceClickListener homeServiceClickListener;

        public ViewHolder(@NonNull View itemView, OnHomeServiceClickListener homeServiceClickListener) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageView);
            textView = itemView.findViewById(R.id.textView);

            this.homeServiceClickListener = homeServiceClickListener;

            itemView.setOnClickListener(this);


        }

        @Override
        public void onClick(View v) {
           homeServiceClickListener.onHomeServiceClickListener(getAdapterPosition());
        }

        public interface OnHomeServiceClickListener{
            void onHomeServiceClickListener(int position);
        }
    }
}
