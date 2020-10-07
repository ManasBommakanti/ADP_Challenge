package com.example.appchallenge;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import java.text.DecimalFormat;

import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.Duration;

public class PastEntriesFragment extends Fragment{
    View view;
    TextView displayHours;
    ExpandableListView entryView;
    List<String> dateList;
    HashMap<String, List<Event>> listItem;
    Context mContext;
    ImageView bronzeMedalImage;
    ImageView silverMedalImage;
    ImageView goldMedalImage;
    TextView bronzeMedalTextView;
    TextView silverMedalTextView;
    TextView goldMedalTextView;
    TextView awardTitle;
    FloatingActionButton mailButton;
    String email;
    List<Event> listEvents;

    ImageButton displayMedals;
    double passHours;

    LoadingDialog loadingDialog;
    HomeAdapter homeAdapter;

    DecimalFormat df = new DecimalFormat("0.00");

    public PastEntriesFragment(){

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        mContext = getActivity();
        final LayoutInflater inflate = inflater;
        view = inflate.inflate(R.layout.pasthoursscreen, container, false);

        loadingDialog = new LoadingDialog(getActivity());
        listEvents = new ArrayList<>();

        entryView = view.findViewById(R.id.id_listHrs);
        displayHours = view.findViewById(R.id.id_totalHrs);
        displayMedals = view.findViewById(R.id.id_medalsScreen);
        mailButton = view.findViewById(R.id.floatingActionButton);

        passHours = 0;

        final DatabaseReference specificRef = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        mailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String[] name = new String[1];
                DatabaseReference specificRef2 = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                specificRef2.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String value = snapshot.child("email").getValue(String.class);
                        email = value;
                        name[0] = snapshot.child("name").getValue(String.class);
                        Log.d("TAG_USERNAME", name[0]);

                        Intent Email = new Intent(Intent.ACTION_SEND);
                        Email.setType("text/email");
                        Email.putExtra("","");
                        Email.putExtra(Intent.EXTRA_SUBJECT, "Past Volunteer Hours - " + name[0]); // Email 's Subject

                        String text = "Dear Advisor ______,\n\n" +
                                "Here is my history of volunteering events:\n\n";

                        for(int i = 0; i < listEvents.size(); i++){
                            String[] split = listEvents.get(i).toString().split(";"); //return date + " " + initTime + " " + endTime + " " + event + " " + schoolCord + " " + comments;
                            text += "Event " + (i + 1) + ":" + "\n\n" +
                                    "\tDate: \"" + split[0] + "\"\n" +
                                    "   Start Time: \"" + split[1] + "\"\n" +
                                    "   End Time: \"" + split[2] + "\"\n" +
                                    "   Event: \"" + split[3] + "\"\n" +
                                    "   School Coordinator: \"" + split[4] + "\"\n" +
                                    "   Comments: \"" + split[5] + "\"\n\n\n";
                            if(i == listEvents.size() - 1) {
                                text += "Have a nice day,\n" + name[0];
                            }
                        }
                        if(listEvents.size() == 0)
                            text += "Sincerely,\n" + name[0];

                        Email.putExtra(Intent.EXTRA_TEXT, text + "");  // Email 's Greeting text
                        startActivity(Intent.createChooser(Email, "Send Feedback:"));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
             /*   specificRef.child("name").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String n = snapshot.getValue(String.class);
                        name = n;
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                */
                /*Log.d("TAG_USERNAME2", name[0] + " null");
                Intent Email = new Intent(Intent.ACTION_SEND);
                Email.setType("text/email");
                Email.putExtra("","");
               // Email.putExtra(Intent.EXTRA_EMAIL, email);  // Advisor's email
                Email.putExtra(Intent.EXTRA_SUBJECT, "Past Volunteer Hours - " + email); // Email 's Subject

                String text = "Dear Advisor ______,\n\n" +
                        "Here is my history of volunteering events:\n\n";

                for(int i = 0; i < listEvents.size(); i++){
                    String[] split = listEvents.get(i).toString().split(";"); //return date + " " + initTime + " " + endTime + " " + event + " " + schoolCord + " " + comments;
                    text += "Event " + (i + 1) + ":" + "\n" +
                        "\tDate: \"" + split[0] + "\"\n" +
                            "   Start Time: \"" + split[1] + "\"\n" +
                            "   End Time: \"" + split[2] + "\"\n" +
                            "   Event: \"" + split[3] + "\"\n" +
                            "   School Coordinator: \"" + split[4] + "\"\n" +
                            "   Comments: \"" + split[5] + "\"\n\n";
                    if(i == listEvents.size() - 1) {
                        text += "Have a nice day,\n" + name[0];
                    }
                }
                if(listEvents.size() == 0)
                    text += "Have a nice day,\n" + name[0];

                Email.putExtra(Intent.EXTRA_TEXT, text + "");  // Email 's Greeting text
                startActivity(Intent.createChooser(Email, "Send Feedback:"));*/
            }
        });

        displayMedals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final View popUpView = inflate.inflate(R.layout.viewmedals, null);
                final PopupWindow pw = new PopupWindow(popUpView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                String[] textSplit = displayHours.getText().toString().split(" ");
                final double value = Double.parseDouble(textSplit[2]);

                specificRef.child("hours").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        double val = value;

                        bronzeMedalImage = popUpView.findViewById(R.id.id_bronzeImage);
                        silverMedalImage = popUpView.findViewById(R.id.id_silverImage);
                        goldMedalImage = popUpView.findViewById(R.id.id_goldimage);
                        bronzeMedalTextView = popUpView.findViewById(R.id.id_desBronze);
                        silverMedalTextView = popUpView.findViewById(R.id.id_desSilver);
                        goldMedalTextView= popUpView.findViewById(R.id.id_desGold);
                        awardTitle = popUpView.findViewById(R.id.id_textviewAward);
                        Button back = popUpView.findViewById(R.id.id_backToPast);

                        back.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pw.dismiss();
                            }
                        });

                        if(val >= 50.0)
                        {
                            bronzeMedalTextView.setText("Congrats on 50 hours of service! This is only the beginning to your volunteering career. Keep up the good work and do your part in community service!");
                            bronzeMedalImage.setVisibility(View.VISIBLE);
                            bronzeMedalTextView.setVisibility(View.VISIBLE);

                            if(val >= 100.0){
                                silverMedalImage.setVisibility(View.VISIBLE);
                                silverMedalTextView.setVisibility(View.VISIBLE);

                                if(val >= 200.0){
                                    goldMedalImage.setVisibility(View.VISIBLE);
                                    goldMedalTextView.setVisibility(View.VISIBLE);
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                pw.showAtLocation(view, Gravity.CENTER, 10, 10);
                pw.setAnimationStyle(R.style.Animation);
                pw.update();
            }
        });

        ValueEventListener valueEventListener = new ValueEventListener() {
            @SuppressLint({"DefaultLocale", "SetTextI18n"})
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                loadingDialog.startLoadingDialog();
                //GenericTypeIndicator<ArrayList<Event>> t = new GenericTypeIndicator<ArrayList<Event>>(){};
                dateList = new ArrayList<>();
                List<Event> list;
                listItem = new HashMap<>();
                homeAdapter = new HomeAdapter(getActivity(), dateList, listItem, entryView.getExpandableListAdapter());

                int count = 0;
                double finalHours = 0;

                for(DataSnapshot eventSnap : snapshot.child("Events").getChildren())
                {
                    list = new ArrayList<>();
                    Event event = eventSnap.getValue(Event.class);
                    dateList.add(event.getDate() + ";" + count);
                    list.add(event);
                    listItem.put(dateList.get(count), list);

                    Log.d("TAG", event.getInitTime() + " InitTime");
                    String start_Time = event.getInitTime();
                    Log.d("TAG", event.getEndTime() + " EndTime");
                    String end_Time = event.getEndTime();

                    try {
                        SimpleDateFormat format = new SimpleDateFormat("hh:mm aa");
                        //SimpleDateFormat format2 = new SimpleDateFormat("HH:mm");

                        String[] initArray = start_Time.split(" ");
                        String[] initFinalArray = initArray[0].split(":");
                        double hoursInitial = Double.parseDouble(initFinalArray[0]);
                        double minInitial = Double.parseDouble(initFinalArray[1]);

                        Log.d("TAG_TIME_I", hoursInitial + " hrs; " + minInitial + " min");

                        String[] finArray = end_Time.split(" ");
                        String[] finFinalArray = finArray[0].split(":");
                        double hoursFin = Double.parseDouble(finFinalArray[0]);
                        double minFin = Double.parseDouble(finFinalArray[1]);

                        if(finArray[1].equals("AM") && initArray[1].equals("PM")) {
                            if(hoursFin != 12 && hoursInitial != 12) {
                                hoursFin += 12;
                                double ini = hoursInitial * 60 + minInitial;
                                double fin = hoursFin * 60 + minFin;

                                double diff = Math.abs(fin - ini);
                                double hrs = diff / 60;
                                Log.d("TAG_HOURS_I", hrs + "");
                                finalHours += hrs;
                            }
                            else{
                                double ini = hoursInitial * 60 + minInitial;
                                double fin = hoursFin * 60 + minFin;

                                double diff = Math.abs(fin - ini);
                                double hrs = diff / 60;
                                Log.d("TAG_HOURS_I", hrs + "");
                                finalHours += hrs;
                            }
                        }
                        else if(finArray[1].equals("PM") && initArray[1].equals("AM")) {
                            if(hoursFin != 12 && hoursInitial != 12) {
                                hoursFin += 12;
                                double ini = hoursInitial * 60 + minInitial;
                                double fin = hoursFin * 60 + minFin;

                                double diff = Math.abs(fin - ini);
                                double hrs = diff / 60;
                                Log.d("TAG_HOURS_F", hrs + "");
                                finalHours += hrs;
                            }
                            else{
                                double ini = hoursInitial * 60 + minInitial;
                                double fin = hoursFin * 60 + minFin;

                                double diff = Math.abs(fin - ini);
                                double hrs = diff / 60;
                                Log.d("TAG_HOURS_F", hrs + "");
                                finalHours += hrs;
                            }
                        }
                        else{
                            if(hoursFin > hoursInitial) {
                                double ini = hoursInitial * 60 + minInitial;
                                double fin = hoursFin * 60 + minFin;

                                double diff = Math.abs(fin - ini);
                                double hrs = diff / 60;
                                Log.d("TAG_HOURS_R", hrs + "");
                                finalHours += hrs;
                            }
                            else if(hoursFin < hoursInitial){
                                hoursFin+=24;
                                double ini = hoursInitial * 60 + minInitial;
                                double fin = hoursFin * 60 + minFin;

                                double diff = Math.abs(fin - ini);
                                double hrs = diff / 60;
                                Log.d("TAG_HOURS_R", hrs + "");
                                finalHours += hrs;
                            }
                            else{
                                double ini = hoursInitial * 60 + minInitial;
                                double fin = hoursFin * 60 + minFin;

                                double diff = Math.abs(fin - ini);
                                double hrs = diff / 60;
                                Log.d("TAG_HOURS_R", hrs + "");
                                finalHours += hrs;
                            }
                        }
                        listEvents.add(list.get(0));
                    }catch(Exception e)
                    {
                        Log.d("TAGHourCalculation",e.getMessage());
                    }
                    count++;
                }
                //df.setRoundingMode(RoundingMode.UP);
                Log.d("TAG_FIRE_R", finalHours + "");
                passHours = finalHours;
                displayHours.setText("Total Hours: " + df.format((finalHours)) + " hrs");
                entryView.setAdapter(homeAdapter);

                loadingDialog.dismissDialog();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("TAG", "Failed to read value.", error.toException());
            }
        };
        specificRef.addValueEventListener(valueEventListener);

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }
}

