package com.lbconsulting.homework02_lorenbak;

import java.util.Calendar;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;

public class DatePickerFragment extends DialogFragment
		implements DatePickerDialog.OnDateSetListener {

	public interface DatePicker {
		public void onDateSelected(Bundle dateBundle);
	}

	private DatePicker dateSelection;

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// Use the current date as the default date in the picker
		final Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int day = c.get(Calendar.DAY_OF_MONTH);

		dateSelection = (DatePicker) getActivity();

		// Create a new instance of DatePickerDialog and return it
		/*datePicker.setMinDate(System.currentTimeMillis() - 1000);*/
		return new DatePickerDialog(getActivity(), this, year, month, day);
	}

	@Override
	public void onDateSet(android.widget.DatePicker view, int year, int month, int day_of_month) {

		Bundle dateBundle = new Bundle(3);
		dateBundle.putInt("year", year);
		dateBundle.putInt("month", month);
		dateBundle.putInt("day_of_month", day_of_month);

		if (dateSelection != null)
		{
			dateSelection.onDateSelected(dateBundle);
		}
	}
}
