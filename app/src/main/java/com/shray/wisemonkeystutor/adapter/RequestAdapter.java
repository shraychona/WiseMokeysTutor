package com.shray.wisemonkeystutor.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shray.wisemonkeystutor.R;
import com.shray.wisemonkeystutor.model.Request;
import com.shray.wisemonkeystutor.ui.ChatActivity;

import java.io.IOException;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

/**
 * Created by Shray on 5/14/2017.
 */

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.MyViewHolder> {
    public List<Request> requestsList;
    Context mContext;
    private static String TAG="Request Adapter";
    FirebaseAuth mAuth;
    DatabaseReference mRef,mReference;
    String mTutorName,mStudentToken,mStudentName,mStudentUid,mTutorUid;
    Request mRequest;

    public RequestAdapter(Context context, List<Request> teachersList) {
        this.mContext = context;
        this.requestsList = teachersList;
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.request_row, parent, false);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        mAuth=FirebaseAuth.getInstance();
        mReference = FirebaseDatabase.getInstance().getReference().child("users").child(mAuth.getCurrentUser().getUid());
        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mTutorName=dataSnapshot.child("name").getValue().toString();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final RequestAdapter.MyViewHolder holder, int position) {

        final Request request=requestsList.get(position);
        mRef= FirebaseDatabase.getInstance().getReference().child("teacherRequest");
        holder.mStudentNameTextView.setText(request.getStudentName());
        holder.mStatus.setText(request.getStatus());
        holder.mLevel.setText(request.getLanguageLevel());
        String statusCheck;
        statusCheck=request.getStatus();
        System.out.println("test"+request.getStatus());
        mStudentName=request.getStudentName();
        mStudentUid=request.getStudentUid();
        mTutorUid=request.getTutorUid();
        mTutorName=request.getTutorName();
        String chatRef=mStudentUid+"_"+mTutorUid;
        final Intent putIntent=new Intent(mContext, ChatActivity.class);
        putIntent.putExtra("studentName",mStudentName);
        putIntent.putExtra("chatref",chatRef);
        putIntent.putExtra("tutorName",mTutorName);



        if (statusCheck.equals("Accepted")||statusCheck.equals("Paid")){

            holder.mReject.setVisibility(View.GONE);
            holder.mAccept.setText("Proceed To Chat");
        }
        holder.mAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.mAccept.getText().equals("Proceed To Chat")){
                    mContext.startActivity(putIntent);
                }
                else {
                    String status = "Accepted";
                    holder.mStatus.setText(status);
                    mRef.child(request.getKey()).child("status").setValue(status);
                    holder.mReject.setVisibility(View.GONE);
                    holder.mAccept.setText("Proceed To Chat");
                    mStudentToken = request.getStudentToken();
                    sendNotification(status);
                }
            }
        });
        holder.mReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String status="Rejected";
                mRef.child(request.getKey()).removeValue();
                mRef.child(request.getKey()).child("status").setValue(status);
                mStudentToken=request.getStudentToken();
                sendNotification(status);
            }
        });


    }

    @Override
    public int getItemCount() {
        return requestsList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView mStudentNameTextView,mLevel,mStatus;
        public Button mAccept,mReject;

        public MyViewHolder(final View view) {
            super(view);
            mStudentNameTextView = (TextView) view.findViewById(R.id.requestRowNameTextView);
            mLevel = (TextView) view.findViewById(R.id.requestRowLevelTextView);
            mStatus = (TextView) view.findViewById(R.id.requestRowStatusTextView);
            mAccept= (Button) view.findViewById(R.id.requestRowAcceptButton);
            mReject= (Button) view.findViewById(R.id.requestRowRejectButton);
        }
    }


    private void sendNotification(String status) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("UserName", mTutorName)
                .add("Token", mStudentToken)
                .add("Status",status)
                .build();
        okhttp3.Request request = new okhttp3.Request.Builder()
                .url("http://www.indiagrocerystore.com.au/wisemonkeys/push_notification_reply.php")
                .post(body)
                .build();
        try {
            client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
