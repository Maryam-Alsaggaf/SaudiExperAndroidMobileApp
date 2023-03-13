package com.example.saudiexpertsecondversion.Tourist;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.saudiexpertsecondversion.Object.Tour;
import com.example.saudiexpertsecondversion.R;
import com.example.saudiexpertsecondversion.TourGuide.tourAdapter;
import com.example.saudiexpertsecondversion.databinding.FragmentTFragemntHomeBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class t_fragment_home extends Fragment implements tourAdapter.onTourClickListener {
    FragmentTFragemntHomeBinding binding;
    FirebaseDatabase database=FirebaseDatabase.getInstance();
    DatabaseReference referenceAllTour=database.getReference("AllTour");
    ArrayList<Tour> tourArrayList;
    tourAdapter recycleAdapter;



    public t_fragment_home() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding=FragmentTFragemntHomeBinding.inflate(inflater,container,false);
        tourArrayList=new ArrayList<>();
        recycleAdapter=new tourAdapter(tourArrayList,getContext(),this);
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
        Button doneButton=dialog.findViewById(R.id.done);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(),T_Home.class);
                startActivity(intent);
            }
        });
        dialog.show();
    }

    @Override
    public void OnItemClick(int position) {
        Tour selectedTour=tourArrayList.get(position);
        Intent intent=new Intent(getContext(),T_ShowTourDetails.class);
        intent.putExtra("selectedTour",selectedTour);
        startActivity(intent);
    }
}