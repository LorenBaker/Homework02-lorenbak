package com.lbconsulting.homework02_lorenbak;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

import com.example.homework02_lorenbak.R;

public class SetAlarmActivity extends Activity {

	// /////////////////////////////////////////////////////////////////////////////
	// AList Activity variables
	// /////////////////////////////////////////////////////////////////////////////

	// String for logging the class name
	public final String TAG = AlarmClockUtilities.TAG;
	private final boolean L = AlarmClockUtilities.L; // enable Logging

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
		// TODO Auto-generated method stub

	} // End doCreate

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.set_alarm, menu);
		return true;
	}

}
