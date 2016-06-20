
package com.codepath.simpletodo.fragments;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;

import com.codepath.simpletodo.utils.DateUtils;

import java.sql.Date;
import java.util.Calendar;

public class DatePickerFragment extends DialogFragment {
    public static final String ARG_SELECTED_DATE = "EditItemActivity.SELECTED";

    private DatePickerDialog.OnDateSetListener mOnDateSetListener;
    private Long mSelectedDate;

    public static DatePickerFragment createInstance(final Long dateInMillis) {
        final Bundle args = new Bundle();
        args.putLong(ARG_SELECTED_DATE, dateInMillis);

        final DatePickerFragment fragment = new DatePickerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final long selectedDateMillis = getArguments().getLong(ARG_SELECTED_DATE, -1);
        if (selectedDateMillis == -1) {
            mSelectedDate = DateUtils.getToday().getTime();
        } else {
            mSelectedDate = selectedDateMillis;
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (! (activity instanceof DatePickerDialog.OnDateSetListener)) {
            throw new ClassCastException("Activity must implement " + DatePickerDialog.OnDateSetListener.class.getName());
        }
        mOnDateSetListener = (DatePickerDialog.OnDateSetListener) activity;
    }

    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        // use today as minimum
        final Date today = DateUtils.getToday();

        final Calendar selectedCalendar = Calendar.getInstance();
        selectedCalendar.setTimeInMillis(mSelectedDate);
        final int selectedYear = selectedCalendar.get(Calendar.YEAR);
        final int selectedMonth = selectedCalendar.get(Calendar.MONTH);
        final int selectedDay = selectedCalendar.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        final DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), mOnDateSetListener, selectedYear, selectedMonth, selectedDay);
        datePickerDialog.getDatePicker().setMinDate(today.getTime());
        datePickerDialog.getDatePicker().updateDate(selectedYear, selectedMonth, selectedDay);
        datePickerDialog.setTitle(null);

        return datePickerDialog;
    }
}