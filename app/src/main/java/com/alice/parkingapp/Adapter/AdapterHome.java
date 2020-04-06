package com.alice.parkingapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.alice.parkingapp.Location.MapsActivity;
import com.alice.parkingapp.Model.Slot;
import com.alice.parkingapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

public class AdapterHome extends RecyclerView.Adapter<AdapterHome.HolderItem>{

    List<Slot> mListItem;
    Context context;
    String lattitude, longitude;
    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

    public AdapterHome(List<Slot> mListItem, Context context, String lattitude, String longitude) {
        this.mListItem = mListItem;
        this.context = context;
        this.lattitude = lattitude;
        this.longitude = longitude;
    }

    @Override
    public AdapterHome.HolderItem onCreateViewHolder(final ViewGroup parent, int viewType) {
        final View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_slot,parent,false);
        final HolderItem holderItem = new HolderItem(layout);
        return holderItem;
    }

    @Override
    public void onBindViewHolder(final HolderItem holder, int position) {
        final Slot mList = mListItem.get(position);

        holder.textView.setText(String.valueOf(position+1));

        if (mList.getStatus().equals("kosong")){
            holder.cardView.setCardBackgroundColor(ContextCompat.getColor(context, R.color.bayar));
        }else {
            holder.cardView.setCardBackgroundColor(ContextCompat.getColor(context, R.color.boomTiga));
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mList.getStatus().equals("kosong")){
                    Intent intent = new Intent(context, MapsActivity.class);
                    intent.putExtra("lattitude", lattitude);
                    intent.putExtra("longitude", longitude);
                    intent.putExtra("id", mList.getId());
                    context.startActivity(intent);

//                    AlertDialog alertDialog = new AlertDialog.Builder(context).create();
//                    alertDialog.setMessage("Anda memesan tempat parkir ini?");
//                    alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "TIDAK", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialogInterface, int i) {
//                            dialogInterface.dismiss();
//                        }
//                    });
//                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
//                            new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int which) {
//                                    Calendar calendar = Calendar.getInstance();
//                                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE, MMM d HH:mm");
//                                    String Date = simpleDateFormat.format(calendar.getTime());
//
//                                    String languageToLoad  = "in";
//                                    Locale locale = new Locale(languageToLoad);
//                                    Locale.setDefault(locale);
//                                    Configuration config = new Configuration();
//                                    config.locale = locale;
//
//                                    FirebaseDatabase.getInstance().getReference("User").child(firebaseUser.getUid())
//                                            .addListenerForSingleValueEvent(new ValueEventListener() {
//                                                @Override
//                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                                    User user = dataSnapshot.getValue(User.class);
//                                                    if (user.getPlat().equals("kosong")){
//                                                        Toast.makeText(context, "Isi data user", Toast.LENGTH_LONG).show();
//                                                        ((Home)context).DialogForm();
//                                                    }else {
//                                                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Pesanan");
//                                                        String id = reference.push().getKey();
//                                                        HashMap<String, Object> map = new HashMap<>();
//                                                        map.put("id", id);
//                                                        map.put("slot", mList.getId());
//                                                        map.put("plat", user.getPlat());
//                                                        map.put("pemesan", user.getId());
//                                                        map.put("status", "terisi");
//                                                        map.put("start", Date);
//                                                        map.put("finish", "finish");
//                                                        reference.child(id).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
//                                                            @Override
//                                                            public void onComplete(@NonNull Task<Void> task) {
//
//                                                            }
//                                                        });
//                                                        HashMap<String, Object> hashMap = new HashMap<>();
//                                                        map.put("status", "terisi");
//                                                        map.put("waktu", Date);
//                                                        FirebaseDatabase.getInstance().getReference("Slot")
//                                                                .child(mList.getId()).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
//                                                            @Override
//                                                            public void onComplete(@NonNull Task<Void> task) {
//                                                                Toast.makeText(context, "Pesanan berhasil", Toast.LENGTH_LONG).show();
//                                                            }
//                                                        });
//                                                    }
//                                                }
//
//                                                @Override
//                                                public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                                                }
//                                            });
//                                    dialog.dismiss();
//                                }
//                            });
//                    alertDialog.show();

                } else {
                    Toast.makeText(context, "Slot parkir sudah terisi", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return mListItem.size();
    }


    public class HolderItem extends RecyclerView.ViewHolder {

        CardView cardView;
        TextView textView;

        public HolderItem(View v)
        {
            super(v);
            cardView = (CardView)v.findViewById(R.id.cardView);
            textView = (TextView)v.findViewById(R.id.textView);
        }
    }
}