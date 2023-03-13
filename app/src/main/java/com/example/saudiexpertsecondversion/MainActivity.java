package com.example.saudiexpertsecondversion;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        Thread splash=new Thread(){
            public void run(){
                try {
                    sleep(3000);
                Intent i=new Intent(getBaseContext(), Main_Registertion.class);
                startActivity(i);

                finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        };
        splash.start();




    }
}