package com.lbconsulting.homework02_lorenbak;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.homework02_lorenbak.R;
import com.lbconsulting.homework02_lorenbak.DatePickerFragment.DatePicker;
import com.lbconsulting.homework02_lorenbak.TimePickerFragment.TimePicker;

public class SetAlarmActivity extends Activity implements DatePicker, TimePicker {

	// /////////////////////////////////////////////////////////////////////////////
	// AList Activity variables
	// /////////////////////////////////////////////////////////////////////////////

	// String for logging the class name
	public final String TAG = AlarmClockUtilities.TAG;
	private final boolean L = AlarmClockUtilities.L; // enable Logging

	private TextView tvAlarmDate;
	private TextView tvAlarmTime;
	private TextView tvAlarmCountdown;

	private Calendar calendarDate;
	private Calendar calendarTime;
	private Calendar calendarDateAndTime;

	// /////////////////////////////////////////////////////////////////////////////
	// SetAlarmActivity skeleton
	// /////////////////////////////////////////////////////////////////////////////
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (L)
			Log.i(TAG, "SetAlarmActivity onCreate Starting.");

		// To keep this method simple place onCreate code at doCreate
		doCreate(savedInstanceState);

		// If we had state to restore, we note that in the log message
		if (L)
			Log.i(TAG, "SetAlarmActivity onCreate completed."
					+ (null != savedInstanceState ? " Restored state." : ""));

	}

	@Override
	protected void onRestart() {
		super.onRestart();
		// Notification that the activity will be started
		if (L)
			Log.i(TAG, "SetAlarmActivity onRestart");
	}

	@Override
	protected void onStart() {
		super.onStart();
		// Notification that the activity is starting
		if (L)
			Log.i(TAG, "SetAlarmActivity onStart");
	}

	@Override
	protected void onResume() {
		super.onResume();
		// Notification that the activity will interact with the user
		if (L)
			Log.i(TAG, "SetAlarmActivity onResume");
	}

	@Override
	protected void onPause() {
		super.onPause();

		// Notification that the activity will stop interacting with the user
		if (L)
			Log.i(TAG, "SetAlarmActivity onPause" + (isFinishing() ? " Finishing" : ""));

		// Store values between instances here
		SharedPreferences preferences = getPreferences(MODE_PRIVATE);
		SharedPreferences.Editor applicationStates = preferences.edit();

		// Commit to storage
		applicationStates.commit();
		/*- See more at: http://eigo.co.uk/labs/managing-state-in-an-android-activity/#sthash.13x5kIOB.dpuf*/
	}

	@Override
	protected void onStop() {
		super.onStop();
		// Notification that the activity is no longer visible
		if (L)
			Log.i(TAG, "SetAlarmActivity onStop");
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// Notification the activity will be destroyed
		if (L)
			Log.i(TAG, "SetAlarmActivity onDestroy"
					// Are we finishing?
					+ (isFinishing() ? " Finishing" : ""));
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		// Called when state should be saved

		if (L)
			Log.i(TAG, "SetAlarmActivity onSaveInstanceState");

	}

	@Override
	protected void onRestoreInstanceState(Bundle savedState) {
		super.onRestoreInstanceState(savedState);

		// If we had state to restore, we note that in the log message
		if (L)
			Log.i(TAG, "SetAlarmActivity onRestoreInstanceState."
					+ (null != savedState ? " Restored state." : ""));
	}

	// /////////////////////////////////////////////////////////////////////////////
	// The minor lifecycle methods - you probably won't need these
	// /////////////////////////////////////////////////////////////////////////////
	@Override
	protected void onPostCreate(Bundle savedState) {
		super.onPostCreate(savedState);
		/* if (null != savedState) restoreState(savedState); */

		// If we had state to restore, we note that in the log message
		if (L) {
			Log.i(TAG, "SetAlarmActivity onPostCreate"
					+ (null == savedState ? " Restored state" : ""));
		}

	}

	@Override
	protected void onPostResume() {
		super.onPostResume();
		// Notification that resuming the activity is complete
		if (L)
			Log.i(TAG, "SetAlarmActivity onPostResume");
	}

	@Override
	protected void onUserLeaveHint() {
		super.onUserLeaveHint();
		// Notification that user navigated away from this activity
		if (L) {
			Log.i(TAG, "SetAlarmActivity onUserLeaveHint");
		}
	}

	// /////////////////////////////////////////////////////////////////////////////
	// Overrides of the implementations ComponentCallbacks methods in Activity
	// /////////////////////////////////////////////////////////////////////////////
	@Override
	public void onConfigurationChanged(Configuration newConfiguration) {
		super.onConfigurationChanged(newConfiguration);

		// This won't happen unless we declare changes we handle in the manifest
		if (L)
			Log.i(TAG, "SetAlarmActivity onConfigurationChanged");
	}

	@Override
	public void onLowMemory() {
		// No guarantee this is called before or after other callbacks
		if (L)
			Log.i(TAG, "SetAlarmActivity onLowMemory");
	}

	// /////////////////////////////////////////////////////////////////////////////
	// SetAlarmActivity skeleton
	// /////////////////////////////////////////////////////////////////////////////

	private void doCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_set_alarm);
		this.tvAlarmDate = (TextView) findViewById(R.id.tvAlarmDate);
		this.tvAlarmTime = (TextView) findViewById(R.id.tvAlarmTime);
		this.tvAlarmCountdown = (TextView) findViewById(R.id.tvAlarmCountdown);

	} // End doCreate

	/*	public void setAlarmDate(View view) {
			// Do something in response to button click
			String msg = "Alarm Date View click.";
			Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
		}

		public void setAlarmTime(View view) {
			// Do something in response to button click
			String msg = "Alarm Time View click.";
			Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
		}*/

	public void showDatePickerDialog(View v) {
		DialogFragment newFragment = new DatePickerFragment();
		newFragment.show(getFragmentManager(), "datePicker");
	}

	public void showTimePickerDialog(View v) {
		DialogFragment newFragment = new TimePickerFragment();
		newFragment.show(getFragmentManager(), "timePicker");
	}

	public void startAlarmTimer(View view) {
		// Do something in response to button click
		String msg = "Start Button click.";
		Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
	}

	public void stopAlarmTimer(View view) {
		// Do something in response to button click
		String msg = "Stop Button click.";
		Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.set_alarm, menu);
		return true;
	}

	@Override
	public void onDateSelected(Calendar cal) {
		this.calendarDate = cal;
		this.setDateAndTime();
		SimpleDateFormat sdf = new SimpleDateFormat("MMMM d, yyyy", Locale.US);
		String formattedDate = sdf.format(cal.getTime());
		this.tvAlarmDate.setText(formattedDate);

	}

	@Override
	public void onTimeSelected(Calendar cal) {
		this.calendarTime = cal;
		this.setDateAndTime();
		SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss a", Locale.US);
		String formattedDate = sdf.format(cal.getTime());
		this.tvAlarmTime.setText(formattedDate);

	}

	//public final void set (int year, int month, int day, int hourOfDay, int minute, int second)
	private void setDateAndTime() {

		if (this.calendarDate == null) {
			this.calendarDate = Calendar.getInstance();
		}
		if (this.calendarTime == null) {
			this.calendarTime = Calendar.getInstance();
			this.calendarTime.set(Calendar.HOUR_OF_DAY, 0);
			this.calendarTime.set(Calendar.MINUTE, 0);
			this.calendarTime.set(Calendar.SECOND, 0);
		}

		this.calendarDateAndTime = Calendar.getInstance();
		this.calendarDateAndTime.set(
				this.calendarDate.get(Calendar.YEAR),
				this.calendarDate.get(Calendar.MONTH),
				this.calendarDate.get(Calendar.DAY_OF_MONTH),
				this.calendarTime.get(Calendar.HOUR_OF_DAY),
				this.calendarTime.get(Calendar.MINUTE),
				this.calendarTime.get(Calendar.SECOND)
				);

		SimpleDateFormat sdf = new SimpleDateFormat("MMMM d, yyyy  hh:mm:ss a", Locale.US);
		String formattedDate = sdf.format(this.calendarDateAndTime.getTime());
		this.tvAlarmCountdown.setText(formattedDate);
	}

}
