package com.example.mini_projet_med_talmoudi;

import androidx.annotation.NonNull;
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
import android.widget.TextView;

import com.example.mini_projet_med_talmoudi.InscriptionActivity;
import com.example.mini_projet_med_talmoudi.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class StudentActivity extends AppCompatActivity {


    //Student Activity have a List of events as ListView and when Click on title a Dialog box will pop from Dialog.xml
    // Dialog box that i took from TP exam and change it





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);


    }

    @Override
    protected void onResume() {
        super.onResume();


        // i use onResume() method instead of onCreate() because it doesn't refresh the list

        // Declare the ListView and ArrayList
        ListView eventListView  = findViewById(R.id.event_list);
        ArrayList<String> eventList = new ArrayList<>();


        // Get all Events for DataBase

        // Create an Instance of DB
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        //Query Create "DbEvent" if not exist
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
                            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                                    StudentActivity.this, android.R.layout.simple_list_item_1, eventList);

                            // Set the adapter on the ListView
                            eventListView.setAdapter(adapter);

                            // Dialog Box
                            eventListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> AdapterView, View view, int i, long l) {

                                    //Get the event title from the list item is clicked
                                    String eventTitle = ((TextView) view).getText().toString().split("\n")[1];

                                    Log.d("firstTag", eventTitle);

                                    // Create an Instance of DB
                                    FirebaseFirestore db = FirebaseFirestore.getInstance();

                                    //Query get Document where title field equals to eventTitle variable
                                            db.collection("dbEvent").whereEqualTo("title", eventTitle).get()
                                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                    if (task.isSuccessful()) {
                                                        {
                                                            for (QueryDocumentSnapshot document : task.getResult()) {
                                                                //retrieve location,description,date from the result (assumed that event title is UNIQUE)
                                                                String location = document.getString("location");
                                                                String description = document.getString("description");
                                                                String date = document.getString("date");


                                                                //declare Dialog from dialog.xml
                                                                Dialog dialog = new Dialog(StudentActivity.this);
                                                                dialog.setContentView(R.layout.dialog);
                                                                TextView t = dialog.findViewById(R.id.tit);
                                                                TextView d = dialog.findViewById(R.id.des);
                                                                TextView dt = dialog.findViewById(R.id.dat);

                                                                t.setText("Location: "+location);
                                                                d.setText("description: "+description);
                                                                dt.setText("Date: "+date);

                                                                Button inscriptionBtn = dialog.findViewById(R.id.yes);
                                                                Button dismissBtn = dialog.findViewById(R.id.no);
                                                                Intent toForm = new Intent(StudentActivity.this, InscriptionActivity.class);
                                                                toForm.putExtra("eventTitle", document.getString("title"));
                                                                inscriptionBtn.setOnClickListener(new View.OnClickListener() {
                                                                    // if inscriptionBtn clicked go to another interface ( Activity Inscription ) with Intent have event title
                                                                    @Override
                                                                    public void onClick(View v) {
                                                                        startActivity(toForm);
                                                                    }
                                                                });
                                                                dismissBtn.setOnClickListener(new View.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(View v) {
                                                                        dialog.dismiss();
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