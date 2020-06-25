package net.smallacademy.authenticatorapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    Button btnNewBatch,btnProfile;
    Dialog Dialog;
    RecyclerView batchList;
    BatchAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Dialog = new Dialog(this);
        btnProfile = findViewById(R.id.btnProfile);
        btnNewBatch = findViewById(R.id.btnNewBatch);
        btnNewBatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateBatchFrag dialogPopUP = new CreateBatchFrag();
                /*Dialog.setContentView(R.layout.fragment_create_batch);
                Dialog.show();*/
                CreateBatchFrag dialogFragment = new CreateBatchFrag();

                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

                ft.replace(R.id.asd, dialogFragment);
                ft.commit();
            }
        });
        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,ProfileActivity.class);
                startActivity(intent);

            }
        });


        batchList = findViewById(R.id.batchList);
        batchList.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<String> options;
        options = new FirebaseRecyclerOptions.Builder<String>()
                .setQuery(FirebaseDatabase.getInstance().getReference("Batches"),String.class).build();
        adapter = new BatchAdapter(options);
        batchList.setAdapter(adapter);

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
