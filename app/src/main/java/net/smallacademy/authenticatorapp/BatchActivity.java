package net.smallacademy.authenticatorapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
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

    }
}
