package com.example.appchallenge;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.FileInputStream;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;

public class LoggerActivity extends AppCompatActivity {
    Spinner eventsDropdown;
    DatePicker datePickerInitial;
    DatePicker datePickerFinal;
    EditText hoursInput;
    EditText schoolCoordinatorName;
    EditText eventComments;
    Button addNewEntryButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loggerscreen);
        datePickerInitial = findViewById(R.id.id_calendarInitial);
        //datePickerFinal = findViewById(R.id.id_calendarFinal);
        //hoursInput = findViewById(R.id.id_hoursInput);
        schoolCoordinatorName = findViewById(R.id.id_schoolCoordInput);
        eventComments = findViewById(R.id.id_commentSectionInput);
        addNewEntryButton = findViewById(R.id.id_addNewEntry);

        getWindow().setStatusBarColor(Color.parseColor("#20111111"));
        getWindow().setNavigationBarColor(Color.parseColor("#20111111"));

        eventsDropdown = findViewById(R.id.id_eventDropdown);
        //Sets up list in alphabetical order with 'other' at the end of the list
        Log.d("TAG", "Setting up list in alphabetical order...");

        String[] eventsInitial = {"Finance Park", "Company Program", "Career Success Workshop",
                "NextGen Tech Series", "Titan Competition", "Personal Finance", "Be Entrepreneurial",
                "Career Speaker Series", "Mentorship Forums", "Women's Future Leadership Forums",
                "Launch Lessons", "Career Exploration", "It's My Job - Soft Skills Workshops",
                "Job Shadow", "NJ Business Hall of Fame Ambassador", "Special Event Speaker",
                "Biztown (elementary or middle school)", "Elementary School Experiences",
                "Middle School Experiences"};

        ArrayList<String> eventsList = new ArrayList<>();
        Collections.addAll(eventsList, eventsInitial);

        Collections.sort(eventsList);
        eventsList.add("Other");
        eventsList.add(0, "Select event from dropdown...");

        Log.d("TAG", "Finished setting up list in alphabetical order...");

        //Dropdown ArrayAdapter
        Log.d("TAG", "Dropdown ArrayAdapter setup...");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, eventsList);
        eventsDropdown.setAdapter(adapter);
        Log.d("TAG", "Finished Dropdown ArrayAdapter setup...");
    }
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        if (hasFocus) {
            hideSystemUI();
        }
    }

    private void hideSystemUI() {
        // Enables regular immersive mode.
        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
        // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        // Set the content to appear under the system bars so that the
                        // content doesn't resize when the system bars hide and show.
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        // Hide the nav bar and status bar
        //| View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        //| View.SYSTEM_UI_FLAG_FULLSCREEN);

    }

    /*public void logService() {
        Log.d("TAG", "Checking if method opens");
        boolean passed = true;
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        final String datePicker1 = datePickerInitial.getValue().format(DateTimeFormatter.ofPattern("MM-dd-yyyy"));
        final String datePicker2 = datePickerFinal.getValue().format(DateTimeFormatter.ofPattern("MM-dd-yyyy"));
        final String hours = hoursInput.getText().toString();
        final String coordinatorName = schoolCoordinatorName.getText().toString();
        final String comments = eventComments.getText().toString();

        boolean passwordC = false;

        if (datePicker1.isEmpty()) {
            firstnameInput.setError("First name is required");
            firstnameInput.requestFocus();
            Log.d("TAG", "first is empty");
            //Toast.makeText(this, "Make sure you entered all fields!", Toast.LENGTH_SHORT).show();
            passed = false;
        }
        if (last.isEmpty()) {
            lastnameInput.setError("Last name is required");
            lastnameInput.requestFocus();
            Log.d("TAG", "last is empty");
            //Toast.makeText(this, "Make sure you entered all fields!", Toast.LENGTH_SHORT).show();
            passed = false;
        }
        if (grade.isEmpty()) {
            gradeInput.setError("Grade is required");
            gradeInput.requestFocus();
            Log.d("TAG", "grade is empty");
            //Toast.makeText(this, "Make sure you entered all fields!", Toast.LENGTH_SHORT).show();
            passed = false;
        }
        if (city.isEmpty()) {
            cityInput.setError("City is required");
            cityInput.requestFocus();
            Log.d("TAG", "city is empty");
            //Toast.makeText(this, "Make sure you entered all fields!", Toast.LENGTH_SHORT).show();
            passed = false;
        }
        if (!passed)
            return;
        else {

        }
    }*/
}