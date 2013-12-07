package com.lbconsulting.homework02_lorenbak;

import java.util.Calendar;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;

public class TimePickerFragment extends DialogFragment
		implements TimePickerDialog.OnTimeSetListener {

	private final String TAG = AlarmClockUtilities.TAG;
	private final boolean L = AlarmClockUtilities.L; // enable Logging

	private boolean onTimeSetFired = false;

	public interface TimePicker {
		public void onTimeSelected(Bundle timeBundle);
	}

	TimePicker timeSelection;

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {

		// Use the current time as the default values for the picker
		final Calendar c = Calendar.getInstance();
		int hour = c.get(Calendar.HOUR_OF_DAY);
		int minute = c.get(Calendar.MINUTE) + 1;

		this.timeSelection = (TimePicker) getActivity();

		// Create a new instance of TimePickerDialog and return it
		return new TimePickerDialog(getActivity(), this, hour, minute,
				DateFormat.is24HourFormat(getActivity()));
	}

	public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
		// Do something with the time chosen by the user
	}

	@Override
	public void onTimeSet(android.widget.TimePicker view, int hourOfDay, int minute) {
		if (!this.onTimeSetFired) {
			if (L)
				Log.i(TAG, "TimePickerFragment onTimeSet.");
			Bundle timeBundle = new Bundle(2);
			timeBundle.putInt("hourOfDay", hourOfDay);
			timeBundle.putInt("minute", minute);

			if (this.timeSelection != null) {
				this.timeSelection.onTimeSelected(timeBundle);
			}
			this.onTimeSetFired = true;
		} else {
			if (L)
				Log.i(TAG, "TimePickerFragment onTimeSet previously fired!");
		}
	}
}
