package com.example.saudiexpertsecondversion.Guest;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.saudiexpertsecondversion.Main_Registertion;
import com.example.saudiexpertsecondversion.databinding.FragmentGCartBinding;


public class g_fragment_cart extends Fragment {
    FragmentGCartBinding binding;


    public g_fragment_cart() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding=FragmentGCartBinding.inflate(getLayoutInflater());
        binding.button.setOnClickListener(view -> {
            Intent intent=new Intent(getContext(), Main_Registertion.class);
            startActivity(intent);
        });

        return binding.getRoot();
    }
}