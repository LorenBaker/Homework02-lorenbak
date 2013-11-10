package com.lbconsulting.homework02_lorenbak;

import java.util.Calendar;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;

public class TimePickerFragment extends DialogFragment
		implements TimePickerDialog.OnTimeSetListener {

	public interface TimePicker {
		public void onTimeSelected(Bundle timeBundle);
	}

	TimePicker timeSelection;

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// Use the current time as the default values for the picker
		final Calendar c = Calendar.getInstance();
		int hour = c.get(Calendar.HOUR_OF_DAY);
		int minute = c.get(Calendar.MINUTE);

		timeSelection = (TimePicker) getActivity();

		// Create a new instance of TimePickerDialog and return it
		return new TimePickerDialog(getActivity(), this, hour, minute,
				DateFormat.is24HourFormat(getActivity()));
	}

	public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
		// Do something with the time chosen by the user
	}

	@Override
	public void onTimeSet(android.widget.TimePicker view, int hourOfDay, int minute) {
		Bundle timeBundle = new Bundle(2);
		timeBundle.putInt("hourOfDay", hourOfDay);
		timeBundle.putInt("minute", minute);

		if (timeSelection != null)
		{
			timeSelection.onTimeSelected(timeBundle);
		}
	}
}
