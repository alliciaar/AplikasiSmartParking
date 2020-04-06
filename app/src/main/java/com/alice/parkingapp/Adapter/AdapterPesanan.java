package com.alice.parkingapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.alice.parkingapp.Model.Pesanan;
import com.alice.parkingapp.Notifications.Detail;
import com.alice.parkingapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class AdapterPesanan extends RecyclerView.Adapter<AdapterPesanan.HolderItem>{

    List<Pesanan> mListItem;
    Context context;
    String lokasi;
    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

    public AdapterPesanan(List<Pesanan> mListItem, Context context) {
        this.mListItem = mListItem;
        this.context = context;
        this.lokasi = lokasi;
    }

    @Override
    public HolderItem onCreateViewHolder(final ViewGroup parent, int viewType) {
        final View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pesanan,parent,false);
        final HolderItem holderItem = new HolderItem(layout);
        return holderItem;
    }

    @Override
    public void onBindViewHolder(final HolderItem holder, int position) {
        final Pesanan mList = mListItem.get(position);

        holder.tanggal.setText(mList.getStart());
        holder.slot.setText(mList.getSlot());
        holder.id.setText(mList.getId());
        holder.status.setText(mList.getStatus());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, Detail.class);
                intent.putExtra("id",mList.getId());
                intent.putExtra("slot",mList.getSlot());
                intent.putExtra("start",mList.getStart());
                intent.putExtra("finish",mList.getFinish());
                intent.putExtra("status",mList.getStatus());
                intent.putExtra("pemesan",mList.getPemesan());
                intent.putExtra("plat",mList.getPlat());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mListItem.size();
    }


    public class HolderItem extends RecyclerView.ViewHolder {

        TextView tanggal, slot, id, status;

        public HolderItem(View v)
        {
            super(v);

            tanggal = (TextView)v.findViewById(R.id.tanggal);
            slot = (TextView)v.findViewById(R.id.slot);
            id = (TextView)v.findViewById(R.id.id);
            status = (TextView)v.findViewById(R.id.status);
        }
    }
}