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
import com.example.maintainmore.Models.PersonalServicesModel;
import com.example.maintainmore.R;

import java.util.ArrayList;

public class PersonalServicesAdapter extends RecyclerView.Adapter<PersonalServicesAdapter.viewHolder>{

    ArrayList<PersonalServicesModel> servicesModel;
    Context context;

    viewHolder.OnPersonalServiceClickListener personalServiceClickListener;

    public PersonalServicesAdapter(ArrayList<PersonalServicesModel> servicesModel, Context context,
                                   viewHolder.OnPersonalServiceClickListener personalServiceClickListener) {
        this.servicesModel = servicesModel;
        this.context = context;
        this.personalServiceClickListener = personalServiceClickListener;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.card_services,parent, false);
        return new viewHolder(view, personalServiceClickListener);

    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        PersonalServicesModel model = servicesModel.get(position);
        holder.textView.setText(model.getName());

        Glide.with(context).load(model.getIconUrl()).placeholder(R.drawable.ic_account).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return servicesModel.size();
    }

    public static class viewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imageView;
        TextView textView;

        OnPersonalServiceClickListener personalServiceClickListener;

        public viewHolder(@NonNull View itemView, OnPersonalServiceClickListener personalServiceClickListener) {
            super(itemView);


            imageView = itemView.findViewById(R.id.imageView);
            textView = itemView.findViewById(R.id.textView);

            this.personalServiceClickListener = personalServiceClickListener;

            itemView.setOnClickListener(this);


        }

        @Override
        public void onClick(View view) {
            personalServiceClickListener.onPersonalServiceClick(getAdapterPosition());
        }

        public interface OnPersonalServiceClickListener{
            void onPersonalServiceClick(int position);
        }
    }
}
