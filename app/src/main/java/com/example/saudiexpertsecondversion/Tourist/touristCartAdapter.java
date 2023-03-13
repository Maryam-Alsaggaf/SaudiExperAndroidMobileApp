package com.example.saudiexpertsecondversion.Tourist;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.saudiexpertsecondversion.AppointmentDetails;
import com.example.saudiexpertsecondversion.Object.Appointment;
import com.example.saudiexpertsecondversion.Object.Tour;
import com.example.saudiexpertsecondversion.Object.TourGuide;
import com.example.saudiexpertsecondversion.R;
import com.example.saudiexpertsecondversion.SelectedTourInfo;
import com.example.saudiexpertsecondversion.databinding.TouristCartItemBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class touristCartAdapter extends RecyclerView.Adapter<touristCartAdapter.ViewHolder> {
    Context context;
    ArrayList<Appointment> appointmentArrayList;
    FirebaseDatabase database=FirebaseDatabase.getInstance();
    DatabaseReference referenceAllTourGuide=database.getReference("AllTourGuide");
    DatabaseReference referenceAllTour=database.getReference("AllTour");
    DatabaseReference referenceAllAppointment=database.getReference("AllAppointments");

    public touristCartAdapter(ArrayList<Appointment> appointmentArrayList,Context context) {
        this.appointmentArrayList = appointmentArrayList;
        this.context=context;
    }

    @NonNull
    @Override
    public touristCartAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(context);

        return new ViewHolder(TouristCartItemBinding.inflate(inflater));
    }

    @Override
    public void onBindViewHolder(@NonNull touristCartAdapter.ViewHolder holder, int position) {
        Appointment appointment=appointmentArrayList.get(position);
        holder.binding.appointmentDate.setText(appointment.getDate());
        holder.binding.Status.setText(appointment.getStatus());
        holder.binding.tourTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, SelectedTourInfo.class);
                intent.putExtra("tourInfo",appointment.getTourID());
                context.startActivity(intent);
            }
        });
        holder.binding.appointmentInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent appointmentIntent=new Intent(context, AppointmentDetails.class);
                appointmentIntent.putExtra("appointmentInfo",appointment.getAppointmentID());
                context.startActivity(appointmentIntent);

            }
        });
        holder.binding.tourGuideName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent tourGuideIntent=new Intent(context,T_ShowTourGuideInfo.class);
                tourGuideIntent.putExtra("TourGuideId",appointment.getTourGuideKey());
                context.startActivity(tourGuideIntent);
            }
        });

        if(appointment.getStatus().equals("Approved")){
            holder.binding.Status.setBackgroundTintList(context.getResources().getColorStateList(R.color.darkGreen));
        }
        if (appointment.getStatus().equals("Rejected")){
            holder.binding.Status.setBackgroundTintList(context.getResources().getColorStateList(R.color.red));
        }
        if(appointment.getStatus().equals("Pending")){
            holder.binding.Status.setBackgroundTintList(context.getResources().getColorStateList(R.color.deepGray));
        }
        retrieveTourGuide(appointment,holder);
        retrieveTour(appointment,holder);
        holder.binding.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(appointment.getStatus().equals("Pending")){
                    optionsDialog("Are you sure you want to cancel this appointment ?",appointment);
                }
                else {
                    showDialog("You can't cancel approved appointment !");
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return appointmentArrayList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        TouristCartItemBinding binding;
        public ViewHolder(TouristCartItemBinding binding){
            super(binding.getRoot());
            this.binding=binding;
        }
    }
    public void retrieveTourGuide(Appointment appointment , ViewHolder holder){
        referenceAllTourGuide.child(appointment.getTourGuideKey()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                TourGuide tourGuide=snapshot.getValue(TourGuide.class);
                String firstName=String.valueOf(tourGuide.getFirstName());
                String lastName=String.valueOf(tourGuide.getLastName());
                holder.binding.tourGuideName.setText(firstName+" "+lastName);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }
    public void retrieveTour(Appointment appointment, ViewHolder holder){
        referenceAllTour.child(appointment.getTourID()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Tour tour=snapshot.getValue(Tour.class);
                holder.binding.tourTitle.setText(tour.getTourTitle());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void cancelAppointment(Appointment appointment){
        referenceAllAppointment.child(appointment.getAppointmentID()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                referenceAllAppointment.child(appointment.getAppointmentID()).removeValue();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    public void optionsDialog(String message ,Appointment appointment){
        Dialog dialog=new Dialog(context);
        dialog.setContentView(R.layout.logout_window);
        TextView title=dialog.findViewById(R.id.title);
        TextView yesButton=dialog.findViewById(R.id.yes);
        TextView noButton=dialog.findViewById(R.id.no);
        title.setText(message);
        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelAppointment(appointment);
                dialog.dismiss();
                Toast.makeText(context,"Your Appointment has been canceld successfully",Toast.LENGTH_SHORT).show();

            }
        });
        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
    public void showDialog(String message){
        Dialog dialog=new Dialog(context);
        dialog.setContentView(R.layout.search_pop_window);
        TextView dialogMessage=dialog.findViewById(R.id.title);
        Button dismissButton=dialog.findViewById(R.id.done);
        dialogMessage.setText(message);
        dismissButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }



}
