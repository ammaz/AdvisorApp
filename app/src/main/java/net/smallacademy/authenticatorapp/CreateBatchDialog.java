package net.smallacademy.authenticatorapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.zip.Inflater;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

public class CreateBatchDialog extends AppCompatDialogFragment {

    private static final String TAG = "Create New Batch";

    EditText etCreateBatch;
    FirebaseDatabase myFirebase;
    DatabaseReference mRef;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userID;
    String BatchName;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_create_batch,null);

        builder.setView(view).setTitle("New batch").setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        }).setPositiveButton("Create", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                BatchName = etCreateBatch.getText().toString().trim();
                if(BatchName.equals("")){
                    Toast.makeText(getActivity(), "Batch name can not be empty.", Toast.LENGTH_LONG).show();
                } else {

                    userID = Objects.requireNonNull(fAuth.getCurrentUser()).getUid();
                    CollectionReference documentReference = fStore.collection("users").document(userID).collection("Batches");
                    Map<String, Object> user = new HashMap<>();
                    user.put("BatchName", BatchName);
                    documentReference.add(user).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Log.d(TAG, "onSuccess: Batch is created for " + userID);
                         //       Toast.makeText(getContext(), "Batch Created", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(TAG, "onFailure: " + e.toString());
                            //    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });

        etCreateBatch = view.findViewById(R.id.etCreateBatch);
        myFirebase = FirebaseDatabase.getInstance();
        mRef = myFirebase.getReference("Batches");
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        return builder.create();

    }

}
