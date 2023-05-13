package com.example.mini_projet_med_talmoudi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AddEventActivity extends AppCompatActivity {
// this AddEventActivity have a form :
// Title , Date from of (MM/DD/YYYY), Location and Description
// after filling the form and click on ADD EVENT a TOAST with pop and the fields will reset

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);
        Button addEventBtn = findViewById(R.id.add_event_button);
        EditText titleEditText = findViewById(R.id.event_title_edittext);
        EditText dateEditText = findViewById(R.id.event_date_edittext);
        EditText locationEditText = findViewById(R.id.event_location_edittext);
        EditText descriptionEditText = findViewById(R.id.event_description_edittext);




        addEventBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            String title = titleEditText.getText().toString();
            String date = dateEditText.getText().toString();
            String location = locationEditText.getText().toString();
            String description = descriptionEditText.getText().toString();

            FirebaseFirestore db = FirebaseFirestore.getInstance();

            Map<String, Object> event = new HashMap<>();
            event.put("title", title);
            event.put("date", date);
            event.put("location", location);
            event.put("description", description);

                db.collection("dbEvent")
                        .add(event)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Log.d("sucTAG", "Event added with ID: " + documentReference.getId());
                                Toast.makeText(AddEventActivity.this, "event has been added successfully!", Toast.LENGTH_SHORT).show();
                                titleEditText.setText("");
                                dateEditText.setText("");
                                locationEditText.setText("");
                                descriptionEditText.setText("");

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w("errTAG", "Error adding event", e);
                                Toast.makeText(AddEventActivity.this, "event has not been added successfully!", Toast.LENGTH_SHORT).show();
                            }
                        });


            }
        });
    }
}