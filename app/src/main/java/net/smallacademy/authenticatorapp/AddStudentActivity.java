package net.smallacademy.authenticatorapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class AddStudentActivity extends AppCompatActivity {

    RecyclerView recyclerAddStudents;
    EditText etSearch;
    ImageView imgSearch;

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    CollectionReference collectionReference;
    AddStudentAdapter adapter;

    String batchID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);
        
        recyclerAddStudents = findViewById(R.id.recyclerAddStudents);
        etSearch = findViewById(R.id.etSearch);
        imgSearch = findViewById(R.id.imgSearch);
       // fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        batchID = getIntent().getStringExtra("batchID");
        
        imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(AddStudentActivity.this, "Yrrrrr", Toast.LENGTH_SHORT).show();
                //setUpRecyclerView();
            }
        });
        setUpRecyclerView();
        
        
    }

    private void setUpRecyclerView() {
        collectionReference = fStore.collection("users");
        Query query = collectionReference;
        Toast.makeText(AddStudentActivity.this, "fgmldfjl", Toast.LENGTH_SHORT).show();

        FirestoreRecyclerOptions<UserModel> options = new FirestoreRecyclerOptions.Builder<UserModel>().setQuery(query,UserModel.class).build();
        adapter = new AddStudentAdapter(options,batchID,this);
        recyclerAddStudents.setLayoutManager(new LinearLayoutManager(this));
        recyclerAddStudents.setAdapter(adapter);
        adapter.setOnItemClickListener(new AddStudentAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();

    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

}
