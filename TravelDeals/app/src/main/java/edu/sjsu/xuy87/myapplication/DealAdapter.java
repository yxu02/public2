package edu.sjsu.xuy87.myapplication;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.google.firebase.database.*;

import java.util.ArrayList;

public class DealAdapter extends RecyclerView.Adapter<DealAdapter.DealViewHolder>{
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private ChildEventListener mChildEventListener;
    private ArrayList<TravelDeal> deals;


    public DealAdapter() {
//        FirebaseUtil.openFirebaseUtil("travedeals");
        mFirebaseDatabase = FirebaseUtil.mFirebaseDatabase;
        mDatabaseReference = FirebaseUtil.mDatabaseReference;
        deals = FirebaseUtil.mTravelDeals;
        mChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                TravelDeal travelDeal = dataSnapshot.getValue(TravelDeal.class);
                travelDeal.setId(dataSnapshot.getKey());
                deals.add(travelDeal);
                notifyItemInserted(deals.size()-1);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        mDatabaseReference.addChildEventListener(mChildEventListener);
    }

    @Override
    public DealViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View itemView = LayoutInflater.from(context).inflate(R.layout.recycler_item, parent, false);
        return new DealViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(DealViewHolder holder, int position) {
        TravelDeal deal = deals.get(position);
        holder.bind(deal);

    }

    @Override
    public int getItemCount() {
        return deals.size();
    }

    public class DealViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView tv_title;
        TextView tv_desc;
        TextView tv_price;

        public DealViewHolder(View itemView) {
            super(itemView);
            tv_title = itemView.findViewById(R.id.tv_title);
            tv_desc = itemView.findViewById(R.id.tv_desc);
            tv_price = itemView.findViewById(R.id.tv_price);
            itemView.setOnClickListener(this);
        }

        public void bind(TravelDeal deal){
            tv_title.setText(deal.getTitle());
            tv_desc.setText(deal.getDesc());
            tv_price.setText(deal.getPrice());
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            Log.d("clicked at position", String.valueOf(position));
            TravelDeal selectedDeal = deals.get(position);
            Intent intent = new Intent(v.getContext(),DealActivity.class);
            intent.putExtra("Deal", selectedDeal);
            v.getContext().startActivity(intent);
        }
    }
}
