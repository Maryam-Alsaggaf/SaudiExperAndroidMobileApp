package com.example.saudiexpertsecondversion.TourGuide;

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

import com.example.saudiexpertsecondversion.Object.Tour;
import com.example.saudiexpertsecondversion.R;
import com.example.saudiexpertsecondversion.databinding.FragmentTgFragemntHomeBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class tg_fragment_home extends Fragment implements tourAdapter.onTourClickListener {
    FragmentTgFragemntHomeBinding binding;
    ArrayList<Tour> tourArrayList;

    FirebaseDatabase database=FirebaseDatabase.getInstance();
    DatabaseReference referenceAllTour=database.getReference("AllTour");
    FirebaseAuth auth=FirebaseAuth.getInstance();
    FirebaseUser user=auth.getCurrentUser();
    tourAdapter recycleAdapter;


    public tg_fragment_home() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding=FragmentTgFragemntHomeBinding.inflate(inflater,container,false);
        tourArrayList=new ArrayList<>();
        loadingAllTour("");
        recycleAdapter=new tourAdapter(tourArrayList,getContext(), this);
        binding.tourRecycleView.setAdapter(recycleAdapter);
        binding.searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                loadingAllTour(charSequence.toString());

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });



        binding.addTourButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addTourActivity=new Intent(getActivity(),TG_AddTour.class);
                startActivity(addTourActivity);
            }
        });


        return binding.getRoot();
    }
    public void loadingAllTour(String city){
        binding.progressBar2.setVisibility(View.VISIBLE);
        referenceAllTour.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                tourArrayList.clear();
                for (DataSnapshot datasnapshot : snapshot.getChildren()){
                    Tour tour=datasnapshot.getValue(Tour.class);
                    if(tour.getTourGuideKey().equals(user.getUid())) {
                        if(city=="") {
                            tourArrayList.add(tour);
                        }else if(tour.getTourCity().toLowerCase().startsWith(city.toLowerCase())){
                            tourArrayList.add(tour);
                        }
                    }

                }
                recycleAdapter.notifyDataSetChanged();
                binding.progressBar2.setVisibility(View.INVISIBLE);
                if(!(city=="") && tourArrayList.isEmpty()){
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
        Intent intent=new Intent(getContext(),TG_ShowTourDetails.class);
        intent.putExtra("selectedTour",selectedTour);
        startActivity(intent);

    }
}