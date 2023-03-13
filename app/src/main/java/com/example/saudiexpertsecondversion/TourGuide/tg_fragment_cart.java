package com.example.saudiexpertsecondversion.TourGuide;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.saudiexpertsecondversion.Object.Appointment;
import com.example.saudiexpertsecondversion.databinding.FragmentTgCartBindingImpl;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class tg_fragment_cart extends Fragment {
    FragmentTgCartBindingImpl binding;
    ArrayList<Appointment> appointmentArrayList;
     tourGuideCartAdapter recycleAdapter;
    FirebaseDatabase database=FirebaseDatabase.getInstance();
    FirebaseAuth auth=FirebaseAuth.getInstance();
    FirebaseUser user =auth.getCurrentUser();
    DatabaseReference referenceAllAppointments=database.getReference("AllAppointments");




    public tg_fragment_cart() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding= (FragmentTgCartBindingImpl) FragmentTgCartBindingImpl.inflate(inflater,container,false);
        appointmentArrayList=new ArrayList<>();
        recycleAdapter=new tourGuideCartAdapter(appointmentArrayList,getContext());
        binding.requestsRecycleView.setAdapter(recycleAdapter);

        loadAllAppointment();

        return binding.getRoot();
    }
    public void loadAllAppointment(){
        binding.progressBar2.setVisibility(View.VISIBLE);
        referenceAllAppointments.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                appointmentArrayList.clear();
                for(DataSnapshot dataSnapshot:snapshot.getChildren()) {
                    Appointment appointment = dataSnapshot.getValue(Appointment.class);
                    if (user.getUid().equals(appointment.getTourGuideKey())) {
                        appointmentArrayList.add(appointment);

                    }
                }
                recycleAdapter.notifyDataSetChanged();
                binding.progressBar2.setVisibility(View.INVISIBLE);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}