package com.example.saudiexpertsecondversion.TourGuide;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.saudiexpertsecondversion.Main_Registertion;
import com.example.saudiexpertsecondversion.R;
import com.example.saudiexpertsecondversion.databinding.FragmentTgFragamentProfileBinding;
import com.google.firebase.auth.FirebaseAuth;

public class tg_fragament_profile extends Fragment {
    FragmentTgFragamentProfileBinding binding;
    FirebaseAuth auth=FirebaseAuth.getInstance();



    public tg_fragament_profile() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding=FragmentTgFragamentProfileBinding.inflate(inflater,container,false);
        // Inflate the layout for this fragment
        Intent intent=new Intent(getActivity(),TG_EditProfile.class);
        binding.editPersonalInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intent);
            }
        });
        binding.logoutBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logoutDialog();
            }
        });


        return binding.getRoot();


    }
    public void logoutDialog(){
        Dialog dialog=new Dialog(getContext());//or this
        dialog.setContentView(R.layout.logout_window);
        dialog.show();
        TextView yesButton=dialog.findViewById(R.id.yes);
        TextView noButton=dialog.findViewById(R.id.no);
        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.signOut();
                Intent intentRegistration=new Intent(getActivity(), Main_Registertion.class);
                startActivity(intentRegistration);

            }
        });
        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }
}