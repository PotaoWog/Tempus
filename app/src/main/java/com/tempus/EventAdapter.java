/*
 * Copyright (c) 2016. This app was made by Otavio Tarelho and Diego Nunes as requirement to get their major certificate. Any copy of this project will suffer legal penalties under Copyrights Laws.
 */

package com.tempus;

/* Imports section */
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by otaviotarelho on 7/31/16.
 */

public class EventAdapter extends ArrayAdapter<Event> {

    private ArrayList<Event> e;

    public EventAdapter(Context context, ArrayList<Event> events) {
        super(context,0,events);
        e = events;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent){

        convertView = LayoutInflater.from(getContext()).inflate(R.layout.events_rows, parent, false);
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getContext());
        Boolean hour_system = sharedPref.getBoolean("hour_system", true);
        String day = sharedPref.getString("lang_setting", "en-us");

        ErowsElements elements = new ErowsElements();

        elements.name = (TextView) convertView.findViewById(R.id.EventNameList);
        elements.date = (TextView) convertView.findViewById(R.id.EventDateList);
        elements.time = (TextClock) convertView.findViewById(R.id.EventTimeClock);
        elements.btn = (ImageButton) convertView.findViewById(R.id.eventAddButton);

        Event ev;
        ev = e.get(position);

        elements.name.setText(ev.getName());

        if(!day.equals("en-us")) { // change with getPreferences
            elements.date.setText(getStringFromDate("dd/MM/yyyy", ev.getDay_start()));
        }
        else {
            elements.date.setText(getStringFromDate("MM/dd/yyyy", ev.getDay_start()));
        }

        if(hour_system) { // change with getPreferences
            elements.time.setFormat24Hour(getStringFromDate("H:mm", ev.getDay_start()));
        }
        else {
            elements.time.setFormat12Hour(getStringFromDate("h:mm", ev.getDay_start()));
        }


        elements.btn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), NewAlarmActivity.class);
                //send information to new activity about the event
                intent.putExtra("ALARM", MainActivity.EXTRA_MESSAGE_ADD_EVENT);
                //start activity
                getContext().startActivity(intent);
            }

        });

        return convertView;
    }

    // Format Calender Content Provider into readable format -- Current format in milliseconds from epoch
    public String getStringFromDate(String pattern, String longDTSTART){
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        Calendar c = Calendar.getInstance();
        Long mili = Long.valueOf(longDTSTART).longValue();
        c.setTimeInMillis(mili);
        String result = sdf.format(c.getTime());
        return result;
    }

    //elements class to make it more organized and reusable
    public class ErowsElements {
        TextView name, date;
        TextClock time;
        ImageButton btn;
    }


}
