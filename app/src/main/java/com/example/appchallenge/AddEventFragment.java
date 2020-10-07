package com.example.appchallenge;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.sql.Time;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Objects;
import java.util.zip.Inflater;

public class AddEventFragment extends Fragment {
    Spinner eventsDropdown;
    BottomNavigationView bottomNavigationView;
    DatePicker calendar;
    TimePicker startTime;
    TimePicker endTime;
    EditText schoolCoord;
    EditText commentInput;
    Button addEntry;
    int previousNum;

    LayoutInflater inflater;
    ViewGroup container;
    Bundle bundle;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("TAG", "Starting AddEventFragment...");
        View view = inflater.inflate(R.layout.loggerscreen, container, false);

        this.inflater = inflater;
        this.container = container;
        this.bundle = bundle;

        eventsDropdown = view.findViewById(R.id.id_eventDropdown);
        calendar = view.findViewById(R.id.id_calendarInitial);
        startTime = view.findViewById(R.id.id_timeStart);
        endTime = view.findViewById(R.id.id_timeEnd);
        schoolCoord = view.findViewById(R.id.id_schoolCoordInput);
        commentInput = view.findViewById(R.id.id_commentSectionInput);
        addEntry = view.findViewById(R.id.id_addNewEntry);

        final String[] event = new String[1];

        eventsDropdown.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d("TAG", position + " " + (eventsDropdown.getAdapter().getCount() - 1));
                event[0] = eventsDropdown.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        addEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logService(event);
            }
        });
        return view;
    }

    public void logService(final String[] event) {
        Log.d("TAG", "Checking if method opens");
        boolean passed = true;
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        SimpleDateFormat formatter2 = new SimpleDateFormat("MM/dd/yyyy");
        Calendar formatCalendar = Calendar.getInstance();
        formatCalendar.set( calendar.getYear(), calendar.getMonth(), calendar.getDayOfMonth() , startTime.getHour(), startTime.getMinute());
        String date = formatter2.format( new Date(formatCalendar.getTimeInMillis()) );

        SimpleDateFormat formatter = new SimpleDateFormat("h:mm a");

        String startTimeString = formatter.format(new Date(formatCalendar.getTimeInMillis()));

        formatCalendar.set(calendar.getYear(), calendar.getMonth(), calendar.getDayOfMonth() , endTime.getHour(), endTime.getMinute());

        String endTimeString = formatter.format(new Date(formatCalendar.getTimeInMillis()));
        String coordinatorName = schoolCoord.getText().toString();
        String comments = commentInput.getText().toString();

        boolean complete = true;

        if(date.isEmpty())
        {
            Toast.makeText(getContext(), "Start Date is empty", Toast.LENGTH_LONG).show();
            complete = false;
        }
        if(startTimeString.isEmpty())
        {
            Toast.makeText(getContext(), "Start time is empty", Toast.LENGTH_LONG).show();
            complete = false;
        }
        if(endTimeString.isEmpty())
        {
            Toast.makeText(getContext(), "End time is empty", Toast.LENGTH_LONG).show();
            complete = false;
        }
        if(coordinatorName.isEmpty())
        {
            Toast.makeText(getContext(), "Coordinator Name is empty", Toast.LENGTH_LONG).show();
            complete = false;
        }
        if(comments.isEmpty())
        {
            Toast.makeText(getContext(), "Comments is empty", Toast.LENGTH_LONG).show();
            complete = false;
        }
        if(event[0].equals("-") )
        {
            Toast.makeText(getContext(), "Please select an event", Toast.LENGTH_LONG).show();
            complete = false;
        }
        Log.d("TAG", event[0]);

        if(complete) {
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    previousNum = (int) snapshot.child("Events").getChildrenCount();
                    Log.d("PreviousNum", previousNum + "");
                    schoolCoord.setText("");
                    commentInput.setText("");
                    eventsDropdown.setSelection(0);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            String key = ref.child("Events").push().getKey();
            Log.d("TAG_KEY", key);
            Event newEvent = new Event(date, startTimeString, endTimeString, event[0], coordinatorName, comments, key);
            ref.child("Events").child(key).setValue(newEvent);
            Toast.makeText(getActivity(), "Event Added Successfully", Toast.LENGTH_SHORT).show();
            date = "";
            startTimeString = "";
            endTimeString = "";
            comments = "";
            event[0] = "";
            coordinatorName = "";
        }
        else
        {
            return;
        }
    }
}
