package com.shray.wisemonkeystutor.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.shray.wisemonkeystutor.R;
import com.shray.wisemonkeystutor.model.Offer;

import java.util.List;

/**
 * Created by Shray on 5/24/2017.
 */

public class OfferAdapter extends RecyclerView.Adapter<OfferAdapter.MyViewHolder> {

    public List<Offer> offerList;
    Context mContext;
    private static String TAG = "Offer Adapter";
    FirebaseAuth mAuth;

    public OfferAdapter(Context context,List<Offer> offerList) {
        this.mContext=context;
        this.offerList=offerList;
        System.out.println("adapter list "+offerList);
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.offer_row, parent, false);;

        mAuth = FirebaseAuth.getInstance();

        return new OfferAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Offer offer=offerList.get(position);
        holder.mPlaceName.setText(offer.getPlaceName());
        holder.mOfferDetail.setText(offer.getOfferDetail());

    }

    @Override
    public int getItemCount() {
        return offerList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView mPlaceName,mOfferDetail;

        public MyViewHolder(View itemView) {
            super(itemView);
            mPlaceName= (TextView) itemView.findViewById(R.id.offerRowPlaceNameTextView);
            mOfferDetail= (TextView) itemView.findViewById(R.id.offerRowOfferDetailTextView);

        }
    }
}

