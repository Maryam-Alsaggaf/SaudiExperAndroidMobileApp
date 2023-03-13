package com.example.saudiexpertsecondversion.Guest;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.saudiexpertsecondversion.Main_Registertion;
import com.example.saudiexpertsecondversion.databinding.FragmentGProfileBinding;


public class g_fragment_profile extends Fragment {
    FragmentGProfileBinding binding;

    public g_fragment_profile() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding=FragmentGProfileBinding.inflate(getLayoutInflater());
        binding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(), Main_Registertion.class);
                startActivity(intent);
            }
        });
        return binding.getRoot();
    }
}