package com.example.saudiexpertsecondversion.TourGuide;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.saudiexpertsecondversion.R;
import com.example.saudiexpertsecondversion.databinding.ActivityTgHomeBinding;

public class TG_Home extends AppCompatActivity {
    ActivityTgHomeBinding binding;
    tg_fragament_profile profile=new tg_fragament_profile();
    tg_fragment_home home=new tg_fragment_home();
    tg_fragment_cart cart= new tg_fragment_cart();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityTgHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        home();

        binding.navigtionBar.setOnItemSelectedListener(item -> {

            switch (item.getItemId()){
                case R.id.Home:
                   getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,home).commit();
                    break;
                case R.id.Cart:
                    getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,cart).commit();
                    break;
                case R.id.profile:
                    getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,profile).commit();
                    break;
            }
        return true;
        });
    }
    public void home(){
        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,home).commit();


    }

}