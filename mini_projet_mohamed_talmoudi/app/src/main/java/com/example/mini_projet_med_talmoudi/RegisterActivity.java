package com.example.mini_projet_med_talmoudi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class RegisterActivity extends AppCompatActivity {

        // RegisterActivity is the Starting Activity in this application and choose to log in as Student or as Administrator
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //Declare 2 button one for Student an other for Admin
        Button studentBtn = findViewById(R.id.student_button);
        Button adminBtn = findViewById(R.id.admin_button);

        // Declare Intent for Each path
        Intent studentIntent = new Intent(this,StudentActivity.class);
        Intent adminIntent = new Intent(this,AdministrationActivity.class);

        studentBtn.setOnClickListener(new View.OnClickListener() {
            @Override // If clicked on Access as Student interface change to Student Activity
            public void onClick(View v) {
                startActivity(studentIntent);
            }
        });

        adminBtn.setOnClickListener(new View.OnClickListener() {
            @Override //If clicked on Access as Administrator interface change to Administration Activity
            public void onClick(View v) {
                startActivity(adminIntent);
            }
        });

    }
}