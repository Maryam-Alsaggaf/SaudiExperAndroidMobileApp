package com.example.saudiexpertsecondversion.Guest;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.saudiexpertsecondversion.R;
import com.example.saudiexpertsecondversion.databinding.ActivityGHomeBinding;

public class G_Home extends AppCompatActivity {
    ActivityGHomeBinding binding;
    g_fragment_cart cartG=new g_fragment_cart();
    g_fragment_profile profileG=new g_fragment_profile();
    g_fragment_home homeG=new g_fragment_home();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityGHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        homeG();
        binding.navigtion.setOnItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.Home:
                    getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,homeG).commit();
                    break;
                case R.id.Cart:
                    getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,cartG).commit();
                    break;
                case R.id.profile:
                    getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,profileG).commit();
                    break;
            }

            return true ;
        });

    }
    public void homeG(){
        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,homeG).commit();
    }
}
