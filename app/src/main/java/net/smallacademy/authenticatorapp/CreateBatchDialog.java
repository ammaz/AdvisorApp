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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

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

                    addToFireStore();
                }
            }
        });

        etCreateBatch = view.findViewById(R.id.etCreateBatch);
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();



        return builder.create();

    }

    private void addToFireStore() {
        userID = Objects.requireNonNull(fAuth.getCurrentUser()).getUid();
        CollectionReference documentReference = fStore.collection("Batches");
        final Map<String, Object> batch = new HashMap<>();
        batch.put("BatchName", BatchName);
        documentReference.add(batch).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(final DocumentReference documentReference) {
                Log.d(TAG, "onSuccess: Batch is created for " + userID);

                AddStudentModel adviser = new AddStudentModel();
                fStore.collection("users").document(userID).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                        UserModel model = documentSnapshot.toObject(UserModel.class);
                        AddStudentModel adviser = new AddStudentModel();
                        adviser.setName(model.getName());
                        adviser.setEmail(model.getEmail());
                        adviser.setStatus("Advisor");
                        adviser.setUserID(userID);
                        documentReference.collection("Students").document(userID).set(adviser);

                        fStore.collection("users").document(userID).collection("Batches").document(documentReference.getId()).set(batch);
                    }
                });
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
