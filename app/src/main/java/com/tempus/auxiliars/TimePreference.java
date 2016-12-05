/*
 * Copyright (c) 2016. This app was made by Otavio Tarelho and Diego Nunes as requirement to get their major certificate. Any copy of this project will suffer legal penalties under Copyrights Laws.
 */

package com.tempus.auxiliars;

import android.content.SharedPreferences;
import android.preference.DialogPreference;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import android.content.Context;
import android.content.res.TypedArray;
import android.preference.PreferenceManager;
import android.text.format.DateFormat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TimePicker;

import com.tempus.Alarm.NewAlarmActivity;
import com.tempus.R;

public class TimePreference extends DialogPreference {
    private Calendar calendar;
    private TimePicker picker = null;

    public TimePreference(Context ctxt) {
        this(ctxt, null);
    }

    public TimePreference(Context ctxt, AttributeSet attrs) {
        this(ctxt, attrs, android.R.attr.dialogPreferenceStyle);
    }

    public TimePreference(Context ctxt, AttributeSet attrs, int defStyle) {
        super(ctxt, attrs, defStyle);

        setPositiveButtonText(R.string.confirm);
        setNegativeButtonText(R.string.cancel);
        calendar = new GregorianCalendar();
    }

    @Override
    protected View onCreateDialogView() {
        picker = new TimePicker(getContext());
        return (picker);
    }

    @Override
    protected void onBindDialogView(View v) {
        super.onBindDialogView(v);

        if(android.text.format.DateFormat.is24HourFormat(getContext())) {
            picker.setIs24HourView(true);
        }
        else {
            picker.setIs24HourView(false);
        }

        picker.setCurrentHour(calendar.get(Calendar.HOUR_OF_DAY));
        picker.setCurrentMinute(calendar.get(Calendar.MINUTE));

    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        super.onDialogClosed(positiveResult);

        if (positiveResult) {
            calendar.set(Calendar.HOUR_OF_DAY, picker.getCurrentHour());
            calendar.set(Calendar.MINUTE, picker.getCurrentMinute());

            setSummary(getSummary());
            if (callChangeListener(calendar.getTimeInMillis())) {
                persistLong(calendar.getTimeInMillis());
                notifyChanged();
            }
        }
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return (a.getString(index));
    }

    @Override
    protected void onSetInitialValue(boolean restoreValue, Object defaultValue) {

        if (restoreValue) {
            if (defaultValue == null) {
                calendar.setTimeInMillis(getPersistedLong(System.currentTimeMillis()));
            } else {
                calendar.setTimeInMillis(Long.parseLong(getPersistedString((String) defaultValue)));
            }
        } else {
            if (defaultValue == null) {
                calendar.setTimeInMillis(System.currentTimeMillis());
            } else {
                calendar.setTimeInMillis(Long.parseLong((String) defaultValue));
            }
        }
        setSummary(getSummary());
    }

    @Override
    public CharSequence getSummary() {
        if (calendar == null) {
            return null;
        }

        Date date = new Date(calendar.getTimeInMillis());

        if(android.text.format.DateFormat.is24HourFormat(getContext())){
            return DateFormat.format("H:mm", date);
        }

        return DateFormat.format("h:mm aa", date);
    }
}