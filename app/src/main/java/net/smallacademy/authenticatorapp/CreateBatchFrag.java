package net.smallacademy.authenticatorapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 */
public class CreateBatchFrag extends Fragment {

    private static final String TAG = "Create New Batch";

    EditText etCreateBatch;
    Button btnCreatebatch,Button2;
    FirebaseDatabase myFirebase;
    DatabaseReference mRef;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userID;

    public CreateBatchFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_batch, container, false);

        etCreateBatch = view.findViewById(R.id.etCreateBatch);
        btnCreatebatch = view.findViewById(R.id.btnCreateBatch);
        myFirebase = FirebaseDatabase.getInstance();
        mRef = myFirebase.getReference("Batches");
        Button2=view.findViewById(R.id.button2);

        Button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "Batch Created", Toast.LENGTH_SHORT).show();
            }
        });


        btnCreatebatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String BatchName = etCreateBatch.getText().toString().trim();

                Toast.makeText(getActivity(), "Batch Created", Toast.LENGTH_SHORT).show();
                userID = Objects.requireNonNull(fAuth.getCurrentUser()).getProviderId();
                CollectionReference documentReference = fStore.collection("users").document(userID).collection("Batches");
                Map<String, Object> user = new HashMap<>();
                user.put("BatchName", BatchName);
                documentReference.add(user).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "onSuccess: Batch is created for " + userID);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFailure: " + e.toString());
                    }
                });
            }
        });
        return view;
    }
}



