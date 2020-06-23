package net.smallacademy.authenticatorapp;

import android.app.Activity;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


/**
 * A simple {@link Fragment} subclass.
 */
public class CreateBatchFrag extends Fragment {

    private static final String TAG = "Create New Batch";

    EditText etCreateBatch;
    Button btnCreateBatch;
    FirebaseDatabase myFirebase;
    DatabaseReference mRef;

    public CreateBatchFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_batch, container, false);

        etCreateBatch = view.findViewById(R.id.etCreateBatch);
        btnCreateBatch = view.findViewById(R.id.btnCreateBatch);
        myFirebase = FirebaseDatabase.getInstance();
        mRef = myFirebase.getReference("Batches");

        btnCreateBatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG,"onClick: capturing input");
                String batchName = etCreateBatch.getText().toString().trim();
                String id = mRef.push().getKey();
                if(!batchName.equals("")){

                    mRef.child(id).setValue(batchName);
                    Toast.makeText(getActivity(), "Batch created successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "Enter Batch Name", Toast.LENGTH_SHORT).show();
                }


            }
        });
        return view;
    }
}
