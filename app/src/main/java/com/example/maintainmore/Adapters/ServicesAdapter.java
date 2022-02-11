package com.example.maintainmore.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.maintainmore.Models.CardModels;
import com.example.maintainmore.R;

import java.util.ArrayList;

public class ServicesAdapter extends RecyclerView.Adapter<ServicesAdapter.viewHolder> {

    ArrayList<CardModels> cardModels;
    Context context;

    viewHolder.OnServiceClickListener onServiceClickListener;

    public ServicesAdapter(ArrayList<CardModels> cardModels, Context context, viewHolder.OnServiceClickListener onServiceClickListener) {
        this.cardModels = cardModels;
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

        CardModels models = cardModels.get(position);

        holder.imageView.setImageResource(models.getPicture());
        holder.textView.setText(models.getName());

    }

    @Override
    public int getItemCount() {
        return cardModels.size();
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
