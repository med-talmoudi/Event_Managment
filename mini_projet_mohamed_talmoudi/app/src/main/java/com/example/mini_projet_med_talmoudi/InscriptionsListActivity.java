package com.example.mini_projet_med_talmoudi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class InscriptionsListActivity extends AppCompatActivity {

// this InscriptionsListActivity to display ListView of Student subscript to an event





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_inscriptions_list);
        // Initialize the ListView and ArrayList

        ListView studentsListView = findViewById(R.id.listViewStudents);
        ArrayList<String> studentsList = new ArrayList<>();


        String eventTitle = getIntent().getStringExtra("eventTitle");
        TextView eventTileView = findViewById(R.id.eventTitle);
        eventTileView.setText(eventTitle);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("dbInscriptions").whereEqualTo("event", eventTitle)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            // Loop through the query results and add each event to the list
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String studentFullName = document.getString("fullName");
                                String studentClass = document.getString("class");
                                String studentEmail = document.getString("email");
                                String studentPhoneNumber = document.getString("PhoneNumber");

                                studentsList.add("\n Name: " + studentFullName + "\n Class: " + studentClass + "\n Phone Number: "
                                        + studentPhoneNumber + "\n Email: " + studentEmail);
                                ArrayAdapter<String> adapter = new ArrayAdapter<>(
                                        InscriptionsListActivity.this,
                                        android.R.layout.simple_list_item_1, studentsList);

                                studentsListView.setAdapter(adapter);

                            }

                        } else {
                            Log.d("TAG", "Error getting events: ", task.getException());
                        }
                    }
                });

    }
}