package com.example.saudiexpertsecondversion.TourGuide;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.saudiexpertsecondversion.Object.Tour;
import com.example.saudiexpertsecondversion.R;
import com.example.saudiexpertsecondversion.databinding.TourItemBinding;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;

public class tourAdapter extends RecyclerView.Adapter<tourAdapter.ViewHolder>{

    ArrayList<Tour> tourArrayList;
    Context context;
    private onTourClickListener clickListener;

    public interface  onTourClickListener{
        void OnItemClick(int position);
    }


    public tourAdapter(ArrayList<Tour> tourArrayList,Context context,onTourClickListener clickListener) {
        this.tourArrayList = tourArrayList;
        this.context=context;
        this.clickListener=clickListener;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //row design
        LayoutInflater inflater=LayoutInflater.from(context);
        return new ViewHolder(TourItemBinding.inflate(inflater)) ;
    }
/////
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //what i do when each item arrives
        Tour tour=tourArrayList.get(position);
        Picasso.get().load(tour.getTourImageUrl()).fit().into(holder.binding.itemImage);
        holder.binding.tourItemTitle.setText(tour.getTourTitle());
        holder.binding.tourDescription.setText(tour.getTourDescription());
        holder.binding.tourCity.setText("City: "+tour.getTourCity());

    }

    @Override
    //what is the pont of the adapter?--- its for loop so you read a list
    //
    public int getItemCount() {
        //how many times should i do loop
        return tourArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TourItemBinding binding;
        public ViewHolder(TourItemBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
            binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(clickListener!=null && getAdapterPosition()!=RecyclerView.NO_POSITION){
                        clickListener.OnItemClick(getAdapterPosition());
                    }
                }
            });




        }
    }
}
