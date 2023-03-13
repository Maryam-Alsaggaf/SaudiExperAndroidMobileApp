package com.example.saudiexpertsecondversion.TourGuide;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.saudiexpertsecondversion.AppointmentDetails;
import com.example.saudiexpertsecondversion.Object.Appointment;
import com.example.saudiexpertsecondversion.Object.Tour;
import com.example.saudiexpertsecondversion.Object.Tourist;
import com.example.saudiexpertsecondversion.R;
import com.example.saudiexpertsecondversion.SelectedTourInfo;
import com.example.saudiexpertsecondversion.Tourist.touristCartAdapter;
import com.example.saudiexpertsecondversion.databinding.TourGuideCartItemBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class tourGuideCartAdapter extends RecyclerView.Adapter<tourGuideCartAdapter.tourGuideViewHolder>{
    Context context;
    ArrayList<Appointment> appointmentArrayList;
    FirebaseDatabase database=FirebaseDatabase.getInstance();
    DatabaseReference referenceAllTour=database.getReference("AllTour");
    DatabaseReference referenceAllTourist=database.getReference("ALLTourist");
    DatabaseReference referenceAllAppointment=database.getReference("AllAppointments");
    String status;

    public tourGuideCartAdapter(ArrayList<Appointment> appointmentArrayList,Context context) {
        this.appointmentArrayList = appointmentArrayList;
        this.context=context;
    }

    @NonNull
    @Override
    public tourGuideViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(context);
        return new tourGuideViewHolder(TourGuideCartItemBinding.inflate(inflater));
    }

    @Override
    public void onBindViewHolder(@NonNull tourGuideViewHolder holder, int position) {
        Appointment appointment=appointmentArrayList.get(position);
        retrieveTour(appointment,holder);
        retrieveTourist(appointment,holder);
        holder.binding.appointmentDate.setText(appointment.getDate());
        holder.binding.time.setText(appointment.getStartTime());
        holder.binding.tourTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent tourIntent=new Intent(context, SelectedTourInfo.class);
                tourIntent.putExtra("tourInfo",appointment.getTourID());
                context.startActivity(tourIntent);

            }
        });
        holder.binding.touristName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent touristIntent=new Intent(context, TG_ShowTouristCartInfo.class);
                touristIntent.putExtra("touristInfo",appointment.getTouristKey());
                context.startActivity(touristIntent);
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

        holder.binding.appointmentStauts.setText(appointment.getStatus());
        if(appointment.getStatus().equals("Pending")){
            holder.binding.appointmentStauts.setBackgroundTintList(context.getResources().getColorStateList(R.color.deepGray));
        }
        else if(appointment.getStatus().equals("Approved")){
            holder.binding.approveCheck.setEnabled(false);
            holder.binding.rejectCheck.setEnabled(false);
            holder.binding.appointmentStauts.setBackgroundTintList(context.getResources().getColorStateList(R.color.darkGreen));

        }
        else if(appointment.getStatus().equals("Rejected")){
            holder.binding.approveCheck.setEnabled(false);
            holder.binding.rejectCheck.setEnabled(false);
            holder.binding.appointmentStauts.setBackgroundTintList(context.getResources().getColorStateList(R.color.red));

        }if(!appointment.getStatus().equals("Approved")|| !appointment.getStatus().equals("Rejected")) {
            holder.binding.approveCheck.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    status = "Approved";
                    optionsDialog("Are you sure you want to approve this appointment?", appointment, status, holder);
                }
            });
            holder.binding.rejectCheck.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    status = "Rejected";
                    optionsDialog("Are you sure you want to reject this appointment !", appointment, status, holder);

                }
            });
        }


    }

    @Override
    public int getItemCount() {
        return appointmentArrayList.size();
    }

    public class tourGuideViewHolder extends RecyclerView.ViewHolder{
        TourGuideCartItemBinding binding;

        public tourGuideViewHolder(TourGuideCartItemBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
        }
    }
    public void retrieveTour(Appointment appointment, tourGuideViewHolder holder){
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


    public void retrieveTourist(Appointment appointment,tourGuideViewHolder holder){
        referenceAllTourist.child(appointment.getTouristKey()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange( DataSnapshot snapshot) {
                Tourist tourist=snapshot.getValue(Tourist.class);
                holder.binding.touristName.setText(tourist.getFirstName());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void retrieveBookedTourInfo(Appointment appointment){
        referenceAllTour.child(appointment.getTourID()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Tour tour=snapshot.getValue(Tour.class);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void approveAppointment(Appointment appointment,String status){
        referenceAllAppointment.child(appointment.getAppointmentID()).child("status").setValue(status);

    }
    public void rejectAppointment(Appointment appointment,String status){
        referenceAllAppointment.child(appointment.getAppointmentID()).child("status").setValue(status);
    }
    public void optionsDialog(String message ,Appointment appointment,String status ,tourGuideViewHolder holder){
        Dialog dialog=new Dialog(context);
        dialog.setContentView(R.layout.logout_window);
        TextView title=dialog.findViewById(R.id.title);
        TextView yesButton=dialog.findViewById(R.id.yes);
        TextView noButton=dialog.findViewById(R.id.no);
        title.setText(message);
        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(status.equals("Approved")) {
                    approveAppointment(appointment, status);
                }else if(status.equals("Rejected")){
                    rejectAppointment(appointment,status);
                }
                dialog.dismiss();
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
}
