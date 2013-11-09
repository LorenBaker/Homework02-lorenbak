package com.lbconsulting.homework02_lorenbak;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.homework02_lorenbak.R;

public class AlarmClockActivity extends Activity {

	// /////////////////////////////////////////////////////////////////////////////
	// AList Activity variables
	// /////////////////////////////////////////////////////////////////////////////

	// String for logging the class name
	public final String TAG = AlarmClockUtilities.TAG;
	private final boolean L = AlarmClockUtilities.L; // enable Logging
	private boolean verbose = true;
	private TextView txtTime = null;
	final Handler timerHandler = new Handler();
	private String nextTime = "";
	private long nextTimeValue;

	// /////////////////////////////////////////////////////////////////////////////
	// AlarmClockActivity skeleton
	// /////////////////////////////////////////////////////////////////////////////
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (L)
			Log.i(TAG, "AlarmClockActivity onCreate Starting.");

		// To keep this method simple place onCreate code at doCreate
		doCreate(savedInstanceState);

		// If we had state to restore, we note that in the log message
		if (L)
			Log.i(TAG, "AlarmClockActivity onCreate completed."
					+ (null != savedInstanceState ? " Restored state." : ""));

	}

	@Override
	protected void onRestart() {
		super.onRestart();
		// Notification that the activity will be started
		if (L)
			Log.i(TAG, "AlarmClockActivity onRestart");
	}

	@Override
	protected void onStart() {
		super.onStart();
		// Notification that the activity is starting
		if (L)
			Log.i(TAG, "AlarmClockActivity onStart");
	}

	@Override
	protected void onResume() {
		super.onResume();
		// Notification that the activity will interact with the user
		if (L)
			Log.i(TAG, "AlarmClockActivity onResume");
	}

	@Override
	protected void onPause() {
		super.onPause();

		// Notification that the activity will stop interacting with the user
		if (L)
			Log.i(TAG, "AlarmClockActivity onPause" + (isFinishing() ? " Finishing" : ""));

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
			Log.i(TAG, "AlarmClockActivity onStop");
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// Notification the activity will be destroyed
		if (L)
			Log.i(TAG, "AlarmClockActivity onDestroy"
					// Are we finishing?
					+ (isFinishing() ? " Finishing" : ""));
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		// Called when state should be saved

		if (L)
			Log.i(TAG, "AlarmClockActivity onSaveInstanceState");

	}

	@Override
	protected void onRestoreInstanceState(Bundle savedState) {
		super.onRestoreInstanceState(savedState);

		// If we had state to restore, we note that in the log message
		if (L)
			Log.i(TAG, "AlarmClockActivity onRestoreInstanceState."
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
			Log.i(TAG, "AlarmClockActivity onPostCreate"
					+ (null == savedState ? " Restored state" : ""));
		}

	}

	@Override
	protected void onPostResume() {
		super.onPostResume();
		// Notification that resuming the activity is complete
		if (L)
			Log.i(TAG, "AlarmClockActivity onPostResume");
	}

	@Override
	protected void onUserLeaveHint() {
		super.onUserLeaveHint();
		// Notification that user navigated away from this activity
		if (L) {
			Log.i(TAG, "AlarmClockActivity onUserLeaveHint");
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
			Log.i(TAG, "AlarmClockActivity onConfigurationChanged");
	}

	@Override
	public void onLowMemory() {
		// No guarantee this is called before or after other callbacks
		if (L)
			Log.i(TAG, "AlarmClockActivity onLowMemory");
	}

	// /////////////////////////////////////////////////////////////////////////////
	// AlarmClockActivity skeleton
	// /////////////////////////////////////////////////////////////////////////////

	private void doCreate(Bundle savedInstanceState) {
		/*this.requestWindowFeature(Window.FEATURE_NO_TITLE);*/
		setContentView(R.layout.activity_alarm_clock);
		/*PreferenceManager.setDefaultValues(this, R.xml.preferences, false);*/

		this.txtTime = (TextView) findViewById(R.id.txtTime);
		this.txtTime.setTextColor(this.getResources().getColor(R.color.white));

		/*Typeface face = Typeface.createFromAsset(getAssets(), "fonts/iceland.ttf");
		txtTime.setTypeface(face);*/

		Timer myTimer = new Timer();
		nextTimeValue = Calendar.getInstance().getTimeInMillis();
		nextTimeValue += 1000;
		nextTime = getTime(nextTimeValue);

		myTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				UpdateGUI();
			}
		}, 0, 1000);

	} // End doCreate

	private void UpdateGUI() {
		timerHandler.post(myRunnable);
	}

	final Runnable myRunnable = new Runnable() {
		public void run() {
			txtTime.setText(nextTime);
			nextTimeValue += 1000;
			nextTime = getTime(nextTimeValue);
		}

	};

	private String getTime(long timeInMillis) {
		return formatDateTime(timeInMillis);
	}

	//}

	/*		this.txtTime.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View viewIn) {
					ShowTime(txtTime, Calendar.getInstance().getTimeInMillis());
				}
			});*/

	//} // End doCreate

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.alarm_clock, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		String msg = null;

		switch (item.getItemId()) {

		case R.id.setAlarmMenu:
			// TODO code menu editActiveListTitle
			/*msg = "Set Alarm Menu under construction.";
			Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();*/

			Intent setAlarmActivityIntent = new Intent(AlarmClockActivity.this, SetAlarmActivity.class);
			/*
			 * setAlarmActivityIntent.putExtra("key", value); //Optional
			 * parameters
			 */
			AlarmClockActivity.this.startActivity(setAlarmActivityIntent);

			return true;

		case R.id.action_settings:
			// TODO code menu masterListSettings

			msg = "Settings under construction.";
			Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/*	private void ShowTime(TextView txtView, long timeInMillis) {
			txtView.setText(formatDateTime(timeInMillis));
		}*/

	private String formatDateTime(long timeToFormatInMilliseconds) {

		SimpleDateFormat formatter = new SimpleDateFormat("h:mm:ss a", Locale.US);

		formatter.setTimeZone(TimeZone.getDefault());
		String currentTime = formatter.format(timeToFormatInMilliseconds);
		return currentTime;
	}

	/*public static class PrefsFragment extends PreferenceFragment {

		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);

			// Load the preferences from an XML resource
			addPreferencesFromResource(R.xml.preferences);
		}
	}*/

}
