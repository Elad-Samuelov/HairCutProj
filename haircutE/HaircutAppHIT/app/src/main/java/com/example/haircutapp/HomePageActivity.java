package com.example.haircutapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

public class HomePageActivity extends AppCompatActivity {

    private TextView WelcomeMessage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initWidgets();

    }
    private void initWidgets()
    {
        String username = getIntent().getStringExtra("USERNAME");

        WelcomeMessage = findViewById(R.id.Hod);
        WelcomeMessage.setText(username);

    }
    public void ScheduleHairCut(View view) {
        String username = WelcomeMessage.getText().toString();
        Intent intent = new Intent(this, DailyCalendarActivity.class);
        intent.putExtra("USERNAME", username);
        startActivity(intent);
    }

    public void toClients(View view) {
        Intent intent = new Intent(this, DailyCalendarActivity.class);
       // intent.putExtra("USERNAME", username);
        startActivity(intent);
    }
}