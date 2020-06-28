package net.smallacademy.authenticatorapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;


import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.common.api.Batch;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class MainActivity extends AppCompatActivity {

    ImageButton btnProfile;
    FloatingActionButton btnNewBatch;
    Dialog Dialog;
    RecyclerView batchList;
    BatchNameAdapter adapter;
    TextView tvEmptyRecycler;


    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userID;
    int i = 0;
    CollectionReference collectionReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Dialog = new Dialog(this);
        btnProfile = findViewById(R.id.btnProfile);
        tvEmptyRecycler = findViewById(R.id.tvEmptyRecycler);
        btnNewBatch = findViewById(R.id.btnNewBatch);



        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();



        btnNewBatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateBatchDialog dialog = new CreateBatchDialog();
                dialog.show(getSupportFragmentManager(),"CreateBatchDialog");
            }
        });
        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,ProfileActivity.class);
                startActivity(intent);

            }
        });
        userID = fAuth.getCurrentUser().getUid();


        setUpRecyclerView();

    }

    private void setUpRecyclerView() {
        collectionReference = fStore.collection("users").document(userID).collection("Batches");
        Query query = collectionReference; //.orderBy("priority",Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<BatchNames> options = new FirestoreRecyclerOptions.Builder<BatchNames>().setQuery(query,BatchNames.class).build();
        adapter = new BatchNameAdapter(options);
        RecyclerView recyclerView = findViewById(R.id.batchList);
        //recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                adapter.deleteItem(viewHolder.getAdapterPosition());
            }
        }).attachToRecyclerView(recyclerView);
        adapter.setOnItemClickListener(new BatchNameAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int positoin) {
              //  BatchNames batchNames = documentSnapshot.toObject(BatchNames.class);
                Intent intent = new Intent(MainActivity.this, BatchActivity.class);
                intent.putExtra("batchID",documentSnapshot.getId());
                startActivity(intent);
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
