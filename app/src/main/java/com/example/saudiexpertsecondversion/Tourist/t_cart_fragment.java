package com.example.saudiexpertsecondversion.Tourist;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.saudiexpertsecondversion.Object.Appointment;
import com.example.saudiexpertsecondversion.databinding.FragmentTCartFragmentBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class t_cart_fragment extends Fragment  {
    FragmentTCartFragmentBinding binding;
    ArrayList<Appointment> appointmentArrayList;
    touristCartAdapter recycleAdapter;
    FirebaseDatabase database=FirebaseDatabase.getInstance();
    FirebaseAuth auth=FirebaseAuth.getInstance();
    FirebaseUser user =auth.getCurrentUser();
    DatabaseReference referenceAllAppointments=database.getReference("AllAppointments");




    public t_cart_fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding=FragmentTCartFragmentBinding.inflate(inflater,container,false);

        appointmentArrayList=new ArrayList<>();
        recycleAdapter=new touristCartAdapter(appointmentArrayList,getContext());
        binding.tourRecycleView.setAdapter(recycleAdapter);

        loadingUserAppointment();

        return binding.getRoot();
    }
    public void loadingUserAppointment(){
        binding.progressBar.setVisibility(View.VISIBLE);

        referenceAllAppointments.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                appointmentArrayList.clear();
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    Appointment appointments=dataSnapshot.getValue(Appointment.class);
                    if (user.getUid().equals(appointments.getTouristKey())) {
                        appointmentArrayList.add(appointments);
                    }
                }
                recycleAdapter.notifyDataSetChanged();
                binding.progressBar.setVisibility(View.INVISIBLE);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {


            }
        });

    }
}