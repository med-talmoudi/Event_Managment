package com.example.mini_projet_med_talmoudi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AdministrationActivity extends AppCompatActivity {
    //Administration Activity have a List of events as ListView and when Click on title a Dialog box will pop from Dialog.xml
    // Dialog box that i took from TP exam and change it and a
    // Dialog box contain date,location and description and 2 buttons
    // button Delete: click on it to delete the clicked event normally students that subscript to that event should be deleted too but i didn't see it in the giving worksheet
    // button List of inscriptions : click to go another interface from "Inscription List Activity" that contain ListView of Students that subscript to that event (FullName, Class, Phone Number, Email)

    // Declare the ListView and ArrayList as fields




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_administration);


    }


    @Override
    protected void onResume() {
        super.onResume();

            // Initialize the ListView and ArrayList
            ListView eventListView = findViewById(R.id.event_list);
            ArrayList<String> eventList = new ArrayList<>();


            // ADD event button click to go interface ( ActivityAddEvent)
            Button addEvenBtn = findViewById(R.id.add_event_button);
            Intent addEventIntent = new Intent(this, AddEventActivity.class);
            addEvenBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(addEventIntent);
                }
            });


        // Query the database for all events
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("dbEvent")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            // Loop through the query results and add each event to the list
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String eventTitle = document.getString("title");
                                String eventDate = document.getString("date");
                                eventList.add("\n" + eventTitle + "\n" + eventDate + "\n");

                            }

                            // Create an ArrayAdapter to display the event list in the ListView
                            ArrayAdapter<String> adapter = new ArrayAdapter<>(AdministrationActivity.this, android.R.layout.simple_list_item_1, eventList);

                            // Set the adapter on the ListView
                            eventListView.setAdapter(adapter);


                            eventListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> AdapterView, View view, int i, long l) {

                                    String eventTitle = ((TextView) view).getText().toString().split("\n")[1];
                                    Log.d("firstTag", eventTitle);


                                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                                    db.collection("dbEvent").whereEqualTo("title", eventTitle).get()
                                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                    if (task.isSuccessful()) {
                                                        {
                                                            for (QueryDocumentSnapshot document : task.getResult()) {
                                                                String location = document.getString("location");
                                                                String description = document.getString("description");
                                                                String date = document.getString("date");
                                                                Dialog dialog = new Dialog(AdministrationActivity.this);
                                                                dialog.setContentView(R.layout.dialog);
                                                                TextView t = dialog.findViewById(R.id.tit);
                                                                TextView d = dialog.findViewById(R.id.des);
                                                                TextView dt = dialog.findViewById(R.id.dat);

                                                                t.setText("Location: " + location);
                                                                d.setText("description: " + description);
                                                                dt.setText("Date: " + date);

                                                                //declare 2 buttons delete and listOfInscriptions
                                                                Button deleteBtn = dialog.findViewById(R.id.yes);
                                                                deleteBtn.setText("Delete");

                                                                Button listOfInscriptionsBtn = dialog.findViewById(R.id.no);
                                                                listOfInscriptionsBtn.setText("List of inscriptions");

                                                                // intent when click on listOfInscriptionsBtn take me to another interface from InscriptionListActivity with the title of the clicked Item List
                                                                Intent toList = new Intent(AdministrationActivity.this, InscriptionsListActivity.class);
                                                                toList.putExtra("eventTitle", document.getString("title"));

                                                                deleteBtn.setOnClickListener(new View.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(View v) {


                                                                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                                                                        db.collection("dbEvent").whereEqualTo("title", eventTitle).get()
                                                                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                                                    @Override
                                                                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                                        if (task.isSuccessful()) {
                                                                                            for (QueryDocumentSnapshot document : task.getResult()) {
                                                                                                document.getReference().delete();
                                                                                                Toast.makeText(AdministrationActivity.this, "Event Deleted!", Toast.LENGTH_SHORT).show();
                                                                                                dialog.dismiss(); // close the dialog box
                                                                                                onResume(); // refresh the page
                                                                                            }
                                                                                        } else {
                                                                                            Log.d("TAG12", "Error getting documents: ", task.getException());
                                                                                            Toast.makeText(AdministrationActivity.this, "Event Not Deleted!", Toast.LENGTH_SHORT).show();
                                                                                        }

                                                                                    }


                                                                                });


                                                                    }
                                                                });

                                                                listOfInscriptionsBtn.setOnClickListener(new View.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(View v) {
                                                                        startActivity(toList);
                                                                    }
                                                                });
                                                                dialog.show();
                                                            }

                                                        }
                                                    } else {
                                                        Log.d("TAG", "Error getting events: ", task.getException());
                                                    }

                                                }

                                            });
                                }
                            });


                        }
                    }
                });
    }
}