package com.example.haircutapp;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class HourAdapter extends ArrayAdapter<HourEvent> {
    public HourAdapter(@NonNull Context context, ArrayList<HourEvent> hourEvents) {
        super(context, 0, hourEvents);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        HourEvent event = getItem(position);

        if (convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.hour_cell, parent, false);

        // Get the reference to the eventContainer LinearLayout
        LinearLayout eventContainer = convertView.findViewById(R.id.eventContainer);

        // Get the current selected date
        LocalDate selectedDate = CalendarUtils.selectedDate;

        // Check if the current day is Saturday
        if (selectedDate.getDayOfWeek() == DayOfWeek.SATURDAY) {
            // Set the background color of the eventContainer to red
            eventContainer.setBackgroundColor(getContext().getResources().getColor(android.R.color.holo_red_light));
        } else {
            // Reset the background color to default for other days
            eventContainer.setBackgroundColor(Color.TRANSPARENT); // Or set to whatever default color you prefer
        }

        setHour(convertView, event.time);
        setEvents(convertView, event.events);

        return convertView;
    }


    private void setHour(View convertView, LocalTime time) {
        TextView timeTV = convertView.findViewById(R.id.timeTV);
        timeTV.setText(CalendarUtils.formattedShortTime(time));
    }

    private void setEvents(View convertView, ArrayList<Event> events) {
        TextView event1 = convertView.findViewById(R.id.event1);

        if (events.size() == 0) {
            hideEvent(event1);
        } else if (events.size() == 1) {
            setEvent(event1, events.get(0));
        }
        // Add other cases for different event sizes as needed
    }

    private void setEvent(TextView textView, Event event) {
        textView.setText(event.getName());
        textView.setVisibility(View.VISIBLE);
    }

    private void hideEvent(TextView tv) {
        tv.setVisibility(View.INVISIBLE);
    }
}
