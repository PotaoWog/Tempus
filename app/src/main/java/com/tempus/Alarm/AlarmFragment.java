/*
 * Copyright (c) 2016. This app was made by Otavio Tarelho and Diego Nunes as requirement to get their major certificate. Any copy of this project will suffer legal penalties under Copyrights Laws.
 */

package com.tempus.Alarm;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.tempus.Events.Event;
import com.tempus.MainActivity;
import com.tempus.R;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class AlarmFragment extends Fragment {

    public static ArrayList<Alarm> alarms = new ArrayList<>();
    private AlarmAdapter adapter;
    private static ListView listView;
    private AlertDialog confirmDialogObj;

    public AlarmFragment() {
        // Required empty public constructor
    }

    public static AlarmFragment newInstance(){ return new AlarmFragment(); }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //If to prevent add same items to the ArrayList
        if(savedInstanceState != null) {

            alarms = (ArrayList<Alarm>) savedInstanceState.get(MainActivity.SAVE_ALARM_LIST);

        }
        else {

            if(alarms.size() == 0) {
                setAlarms();
            }

        }

        View fragmentLayout = inflater.inflate(R.layout.fragment_alarm, container, false);
        listView = (ListView) fragmentLayout.findViewById(R.id.listView);
        adapter = new AlarmAdapter(getActivity(), alarms);
        listView.setAdapter(adapter);
        return fragmentLayout;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        registerForContextMenu(listView);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater menuInflater = getActivity().getMenuInflater();
        menuInflater.inflate(R.menu.long_click_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item){
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)
                item.getMenuInfo();

        int rowPosition = info.position; //RETURN THE ID OF MENU SELECTED

        switch (item.getItemId()){
            case R.id.edit:
                Intent intent = new Intent(getContext(), NewAlarmActivity.class);
                intent.putExtra("DATA", alarms.get(rowPosition));
                intent.putExtra("ALARM", MainActivity.EXTRA_MESSAGE_EDIT);
                getContext().startActivity(intent);
                return true;
            case R.id.delete:
                buildConformDialog(rowPosition);
                confirmDialogObj.show();
                return true;
            case R.id.view:
                //Colocar aqui activity traffic
                return true;
        }

        return super.onContextItemSelected(item);
    }


    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putSerializable(MainActivity.SAVE_ALARM_LIST, alarms);
    }

    public static void setAlarms(){
        //pegar alarms do banco de dados e jogar no arraylist
    }

    public static void deleteAlarm(){
        //colocar deletar alarm aqui
    }

    private void buildConformDialog(final int position){
        AlertDialog.Builder confirmBuilder = new AlertDialog.Builder(getActivity());
        confirmBuilder.setTitle(R.string.delete_alarm_title);
        confirmBuilder.setMessage(R.string.delete_alarm_sum);
        confirmBuilder.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                alarms.remove(position);
                adapter.notifyDataSetChanged();
            }
        });

        confirmBuilder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
            }
        });

        confirmDialogObj = confirmBuilder.create();
    }
}

