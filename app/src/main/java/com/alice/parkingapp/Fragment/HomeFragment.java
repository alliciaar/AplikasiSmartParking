package com.alice.parkingapp.Fragment;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alice.parkingapp.Adapter.AdapterHome;
import com.alice.parkingapp.Class.Session;
import com.alice.parkingapp.Model.Slot;
import com.alice.parkingapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    RecyclerView recyclerView;
    AdapterHome adapter;
    DatabaseReference reference;

    List<Slot> mListItem;
    String lattitude, longitude;

    Session session;
    HashMap<String, String> map = new HashMap<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        lattitude = getArguments().getString("lattitude");
        longitude = getArguments().getString("longitude");
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        session = new Session(getContext().getApplicationContext());
        map = session.getDetailLogin();

        mListItem  = new ArrayList<>();

        recyclerView = (RecyclerView)view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        readSlot();

        return view;
    }

    private void readSlot(){
        reference = FirebaseDatabase.getInstance().getReference("Slot");
        reference.keepSynced(true);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mListItem.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Slot slot = snapshot.getValue(Slot.class);
                    mListItem.add(slot);
                }

                adapter = new AdapterHome(mListItem,getContext(), lattitude, longitude);
                recyclerView.setAdapter(adapter);

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
