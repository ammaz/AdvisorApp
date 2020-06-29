package net.smallacademy.authenticatorapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroupOverlay;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class BatchActivity extends AppCompatActivity {

    Button btnAddStudents,btnFAQs,btnChat ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_batch);
        btnAddStudents = findViewById(R.id.btnAddStudents);
        btnChat = findViewById(R.id.btnChat);
        btnFAQs = findViewById(R.id.btnFAQs);
        btnFAQs.setVisibility(View.GONE);

       // final String batchID = getIntent().getStringExtra("batchID");
//
//        DocumentReference ref = FirebaseFirestore.getInstance().collection("Batches").document(batchID).collection("Students").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
//        ref.addSnapshotListener(new EventListener<DocumentSnapshot>() {
//            @Override
//            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
//                AddStudentModel model = documentSnapshot.toObject(AddStudentModel.class);
//                if(model.getStatus() == "Student"){
//                    Intent intent = new Intent(BatchActivity.this,Activity.class);
//                    startActivity(intent);
//                }
//            }
//        });

        btnAddStudents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BatchActivity.this,AddStudentActivity.class);
                intent.putExtra("batchID", getIntent().getStringExtra("batchID"));
                startActivity(intent);
            }
        });

        btnChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BatchActivity.this,Activity.class);
                startActivity(intent);
            }
        });
    }
}
