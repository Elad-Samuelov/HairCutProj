package com.example.haircutapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    EditText username, password, phoneNum;
    Button btnLogin, btnRegister;
    DBHelper DB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        username = (EditText) findViewById(R.id.inputUserName);
        password = (EditText) findViewById(R.id.inputPassword);
        phoneNum = (EditText) findViewById(R.id.inputPhoneNum);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        DB = new DBHelper(this);

        btnRegister.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = username.getText().toString();
                String pass = password.getText().toString();
                String phone = phoneNum.getText().toString();

                if(user.isEmpty() || pass.isEmpty() || phone.isEmpty())
                    Toast.makeText(RegisterActivity.this, "Please enter all the fields", Toast.LENGTH_LONG).show();
                else{
                    Boolean checkUser = DB.checkUsername(user);
                    if(!checkUser){
                        Boolean insert = DB.insertData(user, pass);
                        if(insert){
                            Toast.makeText(RegisterActivity.this, "Registered successfully",Toast.LENGTH_LONG).show();
                            Intent intent;
                            if(user.equals("Admin")) {
                                intent = new Intent(getApplicationContext(), HomePageAdminActivity.class);
                            }
                            else{
                                intent = new Intent(getApplicationContext(), HomePageActivity.class);
                            }
                            startActivity(intent);
                        }
                        else {
                            Toast.makeText(RegisterActivity.this, "Registration failed", Toast.LENGTH_LONG).show();
                        }
                    }
                    else{
                        Toast.makeText(RegisterActivity.this, "User already exists", Toast.LENGTH_LONG).show();

                    }
                }
            }
        }));

        TextView btn = findViewById(R.id.alredyHaveAccount);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });
    }
}