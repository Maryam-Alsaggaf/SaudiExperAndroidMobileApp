package com.example.saudiexpertsecondversion.Guest;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.saudiexpertsecondversion.Main_Registertion;
import com.example.saudiexpertsecondversion.Object.Tour;
import com.example.saudiexpertsecondversion.R;
import com.example.saudiexpertsecondversion.TourGuide.tourAdapter;
import com.example.saudiexpertsecondversion.databinding.FragmentGHomeBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class g_fragment_home extends Fragment implements tourAdapter.onTourClickListener  {
    FragmentGHomeBinding binding;
    FirebaseDatabase database=FirebaseDatabase.getInstance();
    DatabaseReference referenceAllTour=database.getReference("AllTour");
    ArrayList<Tour> tourArrayList;
    tourAdapter recycleAdapter;


    public g_fragment_home() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding=FragmentGHomeBinding.inflate(inflater,container,false);
        binding.leftImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(), Main_Registertion.class);
                startActivity(intent);
            }
        });
        tourArrayList=new ArrayList<>();
        recycleAdapter=new tourAdapter(tourArrayList,getContext(), (tourAdapter.onTourClickListener) this);
        binding.tourRecycleView.setAdapter(recycleAdapter);
        loadingAllTours("");
        binding.searchBar.clearFocus();
        binding.searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                loadingAllTours(charSequence.toString());


            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        return binding.getRoot();
    }
    public void loadingAllTours(String city){
        binding.progressBar2.setVisibility(View.VISIBLE);
        referenceAllTour.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                tourArrayList.clear();
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    Tour tour=dataSnapshot.getValue(Tour.class);
                    if(city=="") {
                        tourArrayList.add(tour);
                    }else if(tour.getTourCity().toLowerCase().startsWith(city.toLowerCase())){
                        tourArrayList.add(tour);
                    }

                }
                recycleAdapter.notifyDataSetChanged();
                binding.progressBar2.setVisibility(View.INVISIBLE);

                if(!(city.equals(""))&&tourArrayList.isEmpty()){
                    emptyResultsDialog();
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void emptyResultsDialog(){
        Dialog dialog=new Dialog(getContext());
        dialog.setContentView(R.layout.search_pop_window);
        dialog.show();
    }
    @Override
    public void OnItemClick(int position) {
        Tour selectedTour=tourArrayList.get(position);
        Intent intent=new Intent(getContext(), G_ShowTourDetails.class);
        intent.putExtra("selectedTour",selectedTour);
        startActivity(intent);

    }



}