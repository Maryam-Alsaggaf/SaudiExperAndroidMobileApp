package com.example.saudiexpertsecondversion.Tourist;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.saudiexpertsecondversion.R;
import com.example.saudiexpertsecondversion.databinding.ActivityTHomeBinding;

public class T_Home extends AppCompatActivity {
    ActivityTHomeBinding binding;
    t_fragment_home homeT=new t_fragment_home();
    t_cart_fragment cartT=new t_cart_fragment();
    t_fragment_profile profileT=new t_fragment_profile();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityTHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        home();
        binding.navigtionBar.setOnItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.Home:
                   getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,homeT).commit();
                    break;
                case R.id.Cart:
                   getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,cartT).commit();
                    break;
                case R.id.profile:
                   getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,profileT).commit();
                    break;
            }
            return true;
        });

    }
    public void home(){
        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,homeT).commit();

    }

}