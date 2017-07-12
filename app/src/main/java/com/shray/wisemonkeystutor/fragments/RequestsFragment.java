package com.shray.wisemonkeystutor.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.shray.wisemonkeystutor.R;
import com.shray.wisemonkeystutor.adapter.RequestAdapter;
import com.shray.wisemonkeystutor.model.Request;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class RequestsFragment extends Fragment {

    private RecyclerView mRecyclerView;
    List<Request> requestList = new ArrayList<>();
    Query mReference;
    RequestAdapter mRequestAdapter;
    FirebaseAuth mAuth;

    public RequestsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mView = inflater.inflate(R.layout.fragment_requests, container, false);
        mRecyclerView = (RecyclerView) mView.findViewById(R.id.fragmentRecyclerView);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager;
        linearLayoutManager=new LinearLayoutManager(getActivity());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mAuth = FirebaseAuth.getInstance();
        mReference = FirebaseDatabase.getInstance().getReference().child("teacherRequest").orderByChild("tutoruid").equalTo(mAuth.getCurrentUser().getUid());

        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                requestList.clear();
                for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {

                    String key = childDataSnapshot.getKey();
                    try {
                        System.out.println(childDataSnapshot.getValue().toString());
                        requestList.add(new Request(childDataSnapshot.child("username").getValue().toString(),
                                childDataSnapshot.child("useruid").getValue().toString(),
                                childDataSnapshot.child("tutoruid").getValue().toString(),
                                key,
                                childDataSnapshot.child("status").getValue().toString(),
                                childDataSnapshot.child("studentToken").getValue().toString(),
                                childDataSnapshot.child("tutorName").getValue().toString(),
                                childDataSnapshot.child("languageLevel").getValue().toString()));
                    } catch (Exception e) {
                        //  teachersList.clear();
                        e.printStackTrace();
                    }

                }
                mRequestAdapter = new RequestAdapter(getActivity(), requestList);
                System.out.println(requestList);
                mRecyclerView.setAdapter(mRequestAdapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        return mView;
    }


}
