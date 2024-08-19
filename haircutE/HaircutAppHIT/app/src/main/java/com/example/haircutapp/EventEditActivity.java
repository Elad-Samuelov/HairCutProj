package com.example.haircutapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.time.LocalTime;
import java.util.Calendar;
import java.util.Locale;

public class EventEditActivity extends AppCompatActivity
{

    private FirebaseAuth mAuth;
    private EditText eventNameET;

    private EditText EditTimeTV;
    private TextView eventDateTV, eventTimeTV;
    private DatabaseReference productsRef;
    private LocalTime time;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_edit);
        initWidgets();
        time = LocalTime.now();
        eventDateTV.setText("Date: " + CalendarUtils.formattedDate(CalendarUtils.selectedDate));
        eventTimeTV.setText("Time: " + CalendarUtils.formattedTime(time));
        FirebaseDatabase database = FirebaseDatabase.getInstance();
    }
    public void selectTime(View view) {
        // Create a Calendar instance to get the current time
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        // Create a TimePickerDialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                this,
                (timePicker, selectedHour, selectedMinute) -> {
                    // Update the EditTimeTV with the selected time
                    EditText editTimeTV = findViewById(R.id.EditTimeTV);
                    String selectedTime = String.format(Locale.getDefault(), "%02d:%02d", selectedHour, selectedMinute);
                    editTimeTV.setText(selectedTime);
                },
                hour,
                minute,
                true // 24-hour format
        );

        // Show the TimePickerDialog
        timePickerDialog.show();
    }

    private void initWidgets()
    {
        String username = getIntent().getStringExtra("USERNAME");
        eventNameET = findViewById(R.id.eventNameET);
        EditTimeTV = findViewById(R.id.EditTimeTV);
        eventDateTV = findViewById(R.id.eventDateTV);
        eventTimeTV = findViewById(R.id.eventTimeTV);
        eventNameET.setText(username);
    }

    // Inside saveEventAction method of EventEditActivity
// Inside saveEventAction method of EventEditActivity
    public void saveEventAction(View view) {
        String eventName = eventNameET.getText().toString();
        String eventTime = EditTimeTV.getText().toString();

        // Create the event with the entered name, current date, and parsed time
        Event newEvent = new Event(eventName, CalendarUtils.selectedDate, LocalTime.parse(eventTime));
        Event.eventsList.add(newEvent);

        // Get a reference to your Firebase Database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference eventsRef = database.getReference("events");

        // Create a unique key for the event
        String eventId = eventsRef.push().getKey();

        // Set the event data under the unique key
        eventsRef.child(eventId).setValue(newEvent);

        Toast.makeText(this, "Event added successfully", Toast.LENGTH_SHORT).show();
        finish();
    }




}