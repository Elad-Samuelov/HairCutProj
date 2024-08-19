package com.example.haircutapp;

import static com.example.haircutapp.CalendarUtils.selectedDate;

import android.app.AutomaticZenRule;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.DayOfWeek;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class DailyCalendarActivity extends AppCompatActivity
{

    private TextView monthDayText;
    private TextView usernameTV;
    private TextView dayOfWeekTV;
    private ListView hourListView;
    private DatabaseReference eventsRef;
    private Button newEventButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_calendar);
        initWidgets();
        fetchEventsFromFirebase();

        String loggedInUsername = getIntent().getStringExtra("USERNAME");

        hourListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get the clicked HourEvent
                HourEvent hourEvent = (HourEvent) parent.getItemAtPosition(position);

                // Check if the hour event has events
                if (hourEvent.getEvents().size() > 0) {
                    // Get the first event in the hour event
                    Event event = hourEvent.getEvents().get(0);

                    // Check if the user is an admin or the event belongs to the logged-in user
                    if (loggedInUsername.equals("Admin") || event.getName().equals(loggedInUsername)) {
                        // Proceed with the action
                        Intent intent = new Intent(DailyCalendarActivity.this, EventDetailsActivity.class);
                        intent.putExtra("eventName", event.getName());
                        intent.putExtra("eventTime", event.getTime().toString());
                        intent.putExtra("appointmentKey", event.getKey()); // Pass the appointment key
                        startActivity(intent);
                    } else {
                        // Display a message indicating that the user doesn't have permission
                        Toast.makeText(DailyCalendarActivity.this, "You don't have permission to access this appointment", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // If no events, show a toast message
                    Toast.makeText(DailyCalendarActivity.this, "No events for this hour", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initWidgets() {
        String username = getIntent().getStringExtra("USERNAME");
        monthDayText = findViewById(R.id.monthDayText);
        dayOfWeekTV = findViewById(R.id.dayOfWeekTV);
        hourListView = findViewById(R.id.hourListView);
        usernameTV = findViewById(R.id.usernameTV);
        newEventButton = findViewById(R.id.btnNewEvent);
        usernameTV.setText("Welcome, " + username + "!");
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        setDayView();
    }
    private void fetchEventsFromFirebase() {
        DatabaseReference eventsRef = FirebaseDatabase.getInstance().getReference("events");
        eventsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Event.eventsList.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // Get the unique key of the appointment
                    String appointmentKey = snapshot.getKey();

                    String name = snapshot.child("name").getValue(String.class);
                    int dayOfMonth = snapshot.child("date").child("dayOfMonth").getValue(Integer.class);
                    String month = snapshot.child("date").child("month").getValue(String.class);
                    int year = snapshot.child("date").child("year").getValue(Integer.class);
                    int hour = snapshot.child("time").child("hour").getValue(Integer.class);
                    String formattedHour = String.format("%02d:00", hour);
                    LocalDate date = LocalDate.of(year, Month.valueOf(month.toUpperCase()), dayOfMonth);
                    LocalTime time = LocalTime.parse(formattedHour);

                    // Create the event and add it to the list
                    Event event = new Event(name, date, time);
                    Event.eventsList.add(event);

                    // Store the appointment key in the Event object
                    event.setKey(appointmentKey);
                }

                setDayView();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle any errors
                Log.e("FirebaseError", "Error retrieving data from Firebase: " + databaseError.getMessage());
            }
        });
    }

    private HourEvent convertToHourEvent(Event event) {
        // Implement the conversion logic here
        // For example:
        LocalTime time = event.getTime();
        ArrayList<Event> events = new ArrayList<>();
        events.add(event);
        return new HourEvent(time, events);
    }

    public void setDayView() {
        monthDayText.setText(CalendarUtils.monthDayFromDate(selectedDate));
        String dayOfWeek = selectedDate.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.getDefault());
        dayOfWeekTV.setText(dayOfWeek);
        if (selectedDate.getDayOfWeek() == DayOfWeek.SATURDAY) {
            newEventButton.setEnabled(false);
            newEventButton.setTextColor(Color.RED);
            Toast.makeText(this, "No appointments can be scheduled on Saturdays", Toast.LENGTH_SHORT).show();
        } else {
            newEventButton.setEnabled(true);
            newEventButton.setTextColor(Color.BLUE);
        }

        setHourAdapter();
    }


    private void setHourAdapter()
    {
        HourAdapter hourAdapter = new HourAdapter(getApplicationContext(), hourEventList());
        hourListView.setAdapter(hourAdapter);
    }
    private ArrayList<HourEvent> hourEventList()
    {
        ArrayList<HourEvent> list = new ArrayList<>();

        for(int hour = 8; hour <= 17; hour++)
        {
            LocalTime time = LocalTime.of(hour, 0);
            ArrayList<Event> events = Event.eventsForDateAndTime(selectedDate, time);
            HourEvent hourEvent = new HourEvent(time, events);
            list.add(hourEvent);
        }

        return list;
    }
    public void previousDayAction(View view)
    {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.minusDays(1);
        setDayView();
    }
    public void nextDayAction(View view)
    {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.plusDays(1);
        setDayView();
    }
    public void newEventAction(View view) {
        String username = getIntent().getStringExtra("USERNAME");
        Intent intent = new Intent(this, EventEditActivity.class);
        intent.putExtra("USERNAME", username);
        startActivity(intent);
    }

}