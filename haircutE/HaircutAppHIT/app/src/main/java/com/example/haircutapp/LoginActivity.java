package com.example.haircutapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.time.LocalDate;

public class LoginActivity extends AppCompatActivity {


    EditText username, password;
    Button btnLogin;
    DBHelper DB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = (EditText) findViewById(R.id.inputUserName);
        password = (EditText) findViewById(R.id.inputPassword);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        DB = new DBHelper(this);
        CalendarUtils.selectedDate = LocalDate.now();
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = username.getText().toString();
                String pass = password.getText().toString();

                if(user.isEmpty() || pass.isEmpty())
                    Toast.makeText(LoginActivity.this, "Please enter all the fields", Toast.LENGTH_LONG).show();
                else{
                    Boolean checkUserPass = DB.checkUsernamePassword(user,pass);
                    if(checkUserPass){
                       // Toast.makeText(LoginActivity.this, "Login successfully",Toast.LENGTH_LONG).show();
                        Intent intent;
                        if(user.equals("Admin")) {
                            intent = new Intent(getApplicationContext(), HomePageActivity.class);
                            intent.putExtra("USERNAME", user);
                        }
                        else{
                            intent = new Intent(getApplicationContext(), HomePageActivity.class);
                            intent.putExtra("USERNAME", user);
                        }
                        startActivity(intent);
                    }
                    else{
                        Toast.makeText(LoginActivity.this, "Username or Password incorrect", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        TextView btn = findViewById(R.id.textViewSignUp);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
    }
}