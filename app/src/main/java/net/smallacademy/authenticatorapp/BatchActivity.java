package net.smallacademy.authenticatorapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class BatchActivity extends AppCompatActivity {

    Button btnAddStudents,btnFAQs,btnChat ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_batch);
        btnAddStudents = findViewById(R.id.btnAddStudents);
        btnChat = findViewById(R.id.btnChat);
        btnFAQs = findViewById(R.id.btnFAQs);

        btnAddStudents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(BatchActivity.this,AddStudentActivity.class));
            }
        });
    }
}
