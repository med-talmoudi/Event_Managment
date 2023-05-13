package com.example.mini_projet_med_talmoudi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class InscriptionActivity extends AppCompatActivity {

    // this InscriptionActivity contains a form to student should fill it to subscript to the desired event
    // event Title on the top get it with Intent (Full Name, Phone Number, Class and Email)
    //after submitting TOAST with pop and document will save in the database with the form inputs and field event equals to the event title
    // in



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription);
        String eventTitle = getIntent().getStringExtra("eventTitle");

        TextView eventTitleView = findViewById(R.id.tv_event_title);
        eventTitleView.setText(eventTitle);
        TextView fullNameView = findViewById(R.id.et_full_name);
        TextView PhoneNumberView = findViewById(R.id.et_phone_input);
        TextView classView = findViewById(R.id.et_class);
        TextView emailView = findViewById(R.id.et_email);
        Button submitBtn = findViewById(R.id.btn_submit);

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fullName = fullNameView.getText().toString();
                String PhoneNumber = PhoneNumberView.getText().toString();
                String classText = classView.getText().toString();
                String email = emailView.getText().toString();

                Map<String, Object> inscription = new HashMap<>();
                inscription.put("event", eventTitle); // hidden field
                inscription.put("fullName", fullName);
                inscription.put("PhoneNumber", PhoneNumber);
                inscription.put("class", classText);
                inscription.put("email", email);

                FirebaseFirestore db = FirebaseFirestore.getInstance();

                db.collection("dbInscriptions")
                        .add(inscription)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Log.d("sucTAG", "successful Inscription " + documentReference.getId());
                                Toast.makeText(InscriptionActivity.this, "Inscription has been added successfully!", Toast.LENGTH_SHORT).show();
                                fullNameView.setText("");
                                PhoneNumberView.setText("");
                                classView.setText("");
                                emailView.setText("");

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w("errTAG", "Error adding event", e);
                                Toast.makeText(InscriptionActivity.this, "Inscription has not been added successfully!", Toast.LENGTH_SHORT).show();
                            }
                        });



            }
        });




    }

}