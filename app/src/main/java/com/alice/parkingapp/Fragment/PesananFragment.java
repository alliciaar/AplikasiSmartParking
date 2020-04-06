package com.alice.parkingapp.Fragment;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alice.parkingapp.Adapter.AdapterPesanan;
import com.alice.parkingapp.Class.Session;
import com.alice.parkingapp.Model.Pesanan;
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
public class PesananFragment extends Fragment {
    RecyclerView recyclerView;
    AdapterPesanan adapter;
    DatabaseReference reference;

    List<Pesanan> mListItem, mListItem2;

    Session session;
    HashMap<String, String> map = new HashMap<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pesanan, container, false);

        session = new Session(getContext().getApplicationContext());
        map = session.getDetailLogin();

        mListItem  = new ArrayList<>();
        mListItem2  = new ArrayList<>();

        recyclerView = (RecyclerView)view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        readPesanan();

        return view;
    }

    private void readPesanan(){
        reference = FirebaseDatabase.getInstance().getReference("Pesanan");
        reference.keepSynced(true);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mListItem.clear();
                mListItem2.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Pesanan slot = snapshot.getValue(Pesanan.class);
                    mListItem2.add(slot);
                }

                urutkan(mListItem2, mListItem);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void urutkan(List<Pesanan> pesanans2, List<Pesanan> pesanans){
        for (int i = 0; i<pesanans2.size(); i++){
            pesanans.add(pesanans2.get(pesanans2.size()-1-i));
        }

        adapter = new AdapterPesanan(mListItem,getContext());
        recyclerView.setAdapter(adapter);

        adapter.notifyDataSetChanged();
    }

}
