package com.example.appchallenge;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.UiThread;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class HomeAdapter extends BaseExpandableListAdapter {
    Context parentContext;
    List<String> listGroup;
    TextView totalHoursDisplay;
    ExpandableListView listView;
    ExpandableListAdapter listAdapter;
    HashMap<String, List<Event>> listItem;

    public HomeAdapter(Context context, List<String> objects, HashMap<String, List<Event>> list, ExpandableListAdapter listAdapter) {
        Log.d("TAG", "Made HomeAdapter...");
        parentContext = context;
        listGroup = objects;
        listItem = list;
        this.listView = listView;
    }

    @Override
    public int getGroupCount() {
        return listGroup.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        if(listItem != null)
            return this.listItem.get(this.listGroup.get(groupPosition)).size();
        return 0;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.listGroup.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        if(listItem != null)
            return this.listItem.get(this.listGroup.get(groupPosition)).get(childPosition);
        return null;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int position, boolean isExpanded, View adapterView, ViewGroup parent) {
        Log.d("TAG", "getGroupView started...");
        String group = (String) getGroup(position);
        if (adapterView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) parentContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            adapterView = layoutInflater.inflate(R.layout.home_adapter, null);
        }
        final TextView eventDate = adapterView.findViewById(R.id.id_eventDateLabel);
        final Button deleteEvent = adapterView.findViewById(R.id.id_deleteEventButton);
        String[] date = group.split(";");
        eventDate.setText(date[0]);

        return adapterView;
    }

    @Override
    public View getChildView(final int position, final int childPosition, boolean isLastChild, View adapterView, ViewGroup parent) {
        Log.d("TAG", "getChildView started...");
        final Event child = (Event) getChild(position, childPosition);

        if (adapterView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) parentContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            adapterView = layoutInflater.inflate(R.layout.pasthourcontainer, null);
        }
        final TextView eventType = adapterView.findViewById(R.id.id_eventTypeLabel);
        final TextView startTime = adapterView.findViewById(R.id.id_startTimeTypeLabel);
        final TextView endTime = adapterView.findViewById(R.id.id_endTimeTypeLabel);
        final TextView coord = adapterView.findViewById(R.id.id_coordTypeLabel);
        final TextView comments = adapterView.findViewById(R.id.id_eventCommentsLabel);
        final Button deleteEvent = adapterView.findViewById(R.id.id_deleteEventButton);

        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Events").child(child.getEventID());

        deleteEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listGroup.remove(childPosition);
                ref.removeValue();
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        notifyDataSetChanged();
                    }
                };
                runnable.run();
            }
        });

        Log.d("TAG", child.getEvent() + " Event");
        eventType.setText(child.getEvent());

        Log.d("TAG", child.getInitTime() + " InitTime");
        String start_Time = child.getInitTime();
        Log.d("TAG", child.getEndTime() + " EndTime");
        String end_Time = child.getEndTime();

        startTime.setText(start_Time); //"hh:mm a" => 4:23 AM
        endTime.setText(end_Time); //"hh:mm a" => 5:28 PM

        Log.d("TAG", child.getSchoolCord() + " SchoolCord");
        coord.setText(child.getSchoolCord());
        Log.d("TAG", child.getComments() + " Comments");
        comments.setText(child.getComments());

        return adapterView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    /*@NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater layoutInflater = (LayoutInflater) parentContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View adapterView = layoutInflater.inflate(xmlResource, null);

        final TextView eventType = adapterView.findViewById(R.id.id_eventTypeLabel);
        final TextView eventDate = adapterView.findViewById(R.id.id_eventDateLabel);
        final TextView startTime = adapterView.findViewById(R.id.id_startTimeTypeLabel);
        final TextView endTime = adapterView.findViewById(R.id.id_endTimeTypeLabel);
        final TextView coord = adapterView.findViewById(R.id.id_coordTypeLabel);
        final TextView comments = adapterView.findViewById(R.id.id_eventCommentsLabel);

        totalHoursDisplay = adapterView.findViewById(R.id.id_totalHrs);

        eventType.setText(eventList.get(position).getEvent());
        eventDate.setText(eventList.get(position).getDate());

        String start_Time = eventList.get(position).getInitTime();
        String end_Time = eventList.get(position).getEndTime();
        startTime.setText(start_Time); //"hh:mm a" => 4:23 AM
        endTime.setText(end_Time); //"hh:mm a" => 5:28 PM

        try {
            SimpleDateFormat format = new SimpleDateFormat("hh:mm aa");
            Date date1 = format.parse(end_Time);
            Date date2 = format.parse(start_Time);
            long mills = date1.getTime() - date2.getTime();
            Log.d("Data1", ""+date1.getTime());
            Log.d("Data2", ""+date2.getTime());
            int hours = (int) (mills/(1000 * 60 * 60));
            int mins = (int) (mills/(1000*60)) % 60;

            String diff = hours + ":" + mins; // updated value every1 second
            totalHoursDisplay.setText(diff);

        }catch(Exception e)
        {
            Log.d("TAG Display Hours",e.getMessage());
        }

        coord.setText(eventList.get(position).getSchoolCord());
        comments.setText(eventList.get(position).getComments());
        mLinearLayout = adapterView.findViewById(R.id.id_expandable);
        //set visibility to GONE
        mLinearLayout.setVisibility(View.GONE);
        mLinearLayoutHeader = adapterView.findViewById(R.id.id_header);

        mLinearLayoutHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mLinearLayout.getVisibility()==View.GONE){
                    expand();
                }else{
                    collapse();
                }
            }
        });
        mLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                collapse();
            }
        });
        return adapterView;
    }*/
    /*private void expand() {
        //set Visible
        mLinearLayout.setVisibility(View.VISIBLE);

        final int widthSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        final int heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        mLinearLayout.measure(widthSpec, heightSpec);

        ValueAnimator mAnimator = slideAnimator(0, mLinearLayout.getMeasuredHeight());
        mAnimator.start();
    }

    private void collapse() {
        int finalHeight = mLinearLayout.getHeight();

        ValueAnimator mAnimator = slideAnimator(finalHeight, 0);

        mAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                //Height=0, but it set visibility to GONE
                mLinearLayout.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        mAnimator.start();
    }
    private ValueAnimator slideAnimator(int start, int end) {

        ValueAnimator animator = ValueAnimator.ofInt(start, end);

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                //Update Height
                int value = (Integer) valueAnimator.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = mLinearLayout.getLayoutParams();
                layoutParams.height = value;
                mLinearLayout.setLayoutParams(layoutParams);
            }
        });
        return animator;
    }*/
}
