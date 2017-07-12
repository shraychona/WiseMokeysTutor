package com.shray.wisemonkeystutor.ui;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.shray.wisemonkeystutor.BaseActivity;
import com.shray.wisemonkeystutor.R;

import java.util.HashMap;

import io.paperdb.Paper;

public class TutorSignUpActivity extends BaseActivity {

    private static final String TAG ="TUTOR_SIGN_UP" ;
    private static final int GALLERY_INTENT = 2;

    private EditText mEmailEditText,mQualificationEditText,mAgeEditText,mContactEditText,
            mNameEditText,mPasswordEditText,mNationalityEditText;
    private Button mSignInBtn;
    private Spinner mLanguageSpinner;

    private DatabaseReference mDatabase;;
    private FirebaseAuth mAuth;
    private StorageReference mStorage;


//    private Uri uri;


    private String[] languages={"English","Spanish"};
    String mLanguage;

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if(requestCode==GALLERY_INTENT && resultCode==RESULT_OK) {
//            uri=data.getData();
//
//        }
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_sign_up);

        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setTitle("Wise Monkeys");

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mStorage= FirebaseStorage.getInstance().getReference();

        mLanguageSpinner = (Spinner) findViewById(R.id.tutorLanguageSpinner);
        mEmailEditText= (EditText) findViewById(R.id.tutorEmailEditText);
        mPasswordEditText= (EditText) findViewById(R.id.tutorPasswordEditText);
        mNameEditText= (EditText) findViewById(R.id.tutorNameEditText);
        mContactEditText= (EditText) findViewById(R.id.tutorContactEditText);
        mAgeEditText= (EditText) findViewById(R.id.tutorAgeEditText);
        mQualificationEditText= (EditText) findViewById(R.id.tutorQulificationEditText);
        mSignInBtn= (Button) findViewById(R.id.tutorSignUpBtn);
        mNationalityEditText= (EditText) findViewById(R.id.tutorNationalityEditText);

        mLanguageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                mLanguage=languages[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                mLanguage=languages[0];
            }
        });
        ArrayAdapter languageAdapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,languages);
        languageAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        mLanguageSpinner.setAdapter(languageAdapter);


        mSignInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String mEmail=mEmailEditText.getText().toString().trim();
                String mPassword=mPasswordEditText.getText().toString().trim();


                mAuth.createUserWithEmailAndPassword(mEmail, mPassword)
                        .addOnCompleteListener(TutorSignUpActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "createUserWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    updateUI(user);
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                    Toast.makeText(TutorSignUpActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }

                                // ...
                            }
                        });



            }
        });
    }

    private void updateUI(FirebaseUser user) {
        String mName=mNameEditText.getText().toString().trim();
        String mContact=mContactEditText.getText().toString().trim();
        String mNationality=mNationalityEditText.getText().toString().trim();
        String mAge=mAgeEditText.getText().toString().trim();
        String mQualification= mQualificationEditText.getText().toString().trim();
        String uid =user.getUid();
        String mEmail = user.getEmail();
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        double longitude = location.getLongitude();
        double latitude = location.getLatitude();
        String mLatitude=String.valueOf(latitude);
        String mLongitude=String.valueOf(longitude);

        HashMap<String, String> dataMap = new HashMap<String, String>();
        dataMap.put("email",mEmail);
        dataMap.put("name",mName);
        dataMap.put("contact",mContact);
        dataMap.put("nationality",mNationality);
        dataMap.put("age",mAge);
        dataMap.put("qualification",mQualification);
        dataMap.put("language",mLanguage);
        dataMap.put("uid",uid);
        dataMap.put("latitude",mLatitude);
        dataMap.put("longitude",mLongitude);
        dataMap.put("balance","0");

        dataMap.put("profileCode","1");
        Paper.init(this);
        String token = Paper.book().read("token","invalid");
        dataMap.put("token",token);
        mDatabase.child("users").child(uid).setValue(dataMap);
        mDatabase.child("languages").child(mLanguage).child(mAuth.getCurrentUser().getUid()).setValue(dataMap);
//        StorageReference filePath=mStorage.child("userDp").child(uri.getLastPathSegment());
//        filePath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//            @SuppressWarnings("VisibleForTests")
//            @Override
//            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                String link=taskSnapshot.getDownloadUrl().toString();
//            }
//        });
        Intent intent =new Intent(TutorSignUpActivity.this, TutorInterfaceActivity.class);
        startActivity(intent);
    }


}
