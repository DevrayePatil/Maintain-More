package com.example.maintainmore.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.maintainmore.Models.ServiceCardModels;
import com.example.maintainmore.R;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.ArrayList;


public class ImageSlideAdapter extends SliderViewAdapter<ImageSlideAdapter.viewHolder>{

    ArrayList<ServiceCardModels> serviceCardModels;
    Context context;

    public ImageSlideAdapter(ArrayList<ServiceCardModels> serviceCardModels, Context context) {
        this.serviceCardModels = serviceCardModels;
        this.context = context;
    }

    @Override
    public viewHolder onCreateViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.image_slider, parent, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(viewHolder viewHolder, int position) {
        ServiceCardModels models = serviceCardModels.get(position);
        viewHolder.imageView.setImageResource(models.getPicture());
    }

    @Override
    public int getCount() {
        return serviceCardModels.size();
    }

    public static class viewHolder extends SliderViewAdapter.ViewHolder{

        ImageView imageView;

        public viewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }
}
