package com.example.haircutapp;

// EventDetailsActivity.java

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EventDetailsActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);

        // Retrieve event information from the intent
        String eventName = getIntent().getStringExtra("eventName");
        String eventTime = getIntent().getStringExtra("eventTime");
        String appointmentKey = getIntent().getStringExtra("appointmentKey");
        // Display event details in TextViews
        TextView eventNameTextView = findViewById(R.id.eventNameTextView);
        TextView eventTimeTextView = findViewById(R.id.eventTimeTextView);

        eventNameTextView.setText("Event Name: " + eventName);
        eventTimeTextView.setText("Event Time: " + eventTime);

        // Back button click listener

        Toast.makeText(this, "Appointment Key: " + appointmentKey, Toast.LENGTH_SHORT).show();

        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Close the current activity
            }
        });

        // Cancel button click listener
        Button cancelButton = findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call method to delete the appointment from Firebase
                deleteAppointmentFromFirebase(appointmentKey);
            }
        });
    }

    private void deleteAppointmentFromFirebase(String appointmentKey) {
        DatabaseReference appointmentsRef = FirebaseDatabase.getInstance().getReference("events").child(appointmentKey);
        appointmentsRef.removeValue(new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError error, DatabaseReference ref) {
                if (error == null) {
                    // Appointment deleted successfully
                    Toast.makeText(EventDetailsActivity.this, "Appointment cancelled", Toast.LENGTH_SHORT).show();
                    finish(); // Close the activity after cancelling the appointment
                } else {
                    // Error occurred while deleting the appointment
                    Toast.makeText(EventDetailsActivity.this, "Failed to cancel appointment", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
