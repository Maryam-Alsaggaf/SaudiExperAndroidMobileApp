package com.example.saudiexpertsecondversion.Tourist;

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
import com.example.saudiexpertsecondversion.databinding.FragmentTProfileBinding;
import com.google.firebase.auth.FirebaseAuth;


public class t_fragment_profile extends Fragment {
    FragmentTProfileBinding binding;

    FirebaseAuth auth=FirebaseAuth.getInstance();


    public t_fragment_profile() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
     binding=FragmentTProfileBinding.inflate(inflater,container,false);


        binding.logoutBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logOutDialog();
            }
        });
        //coding place
        binding.editPersonalInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(),T_EditProfile.class);
                startActivity(intent);
            }
        });
        return binding.getRoot();
    }
    public void logOutDialog(){
        Dialog dialog=new Dialog(getContext());
        dialog.setContentView(R.layout.logout_window);
        dialog.show();
        TextView yesButton=dialog.findViewById(R.id.yes);
        TextView noButton=dialog.findViewById(R.id.no);
        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.signOut();
                Intent intentRegistration=new Intent(getActivity(),Main_Registertion.class);
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