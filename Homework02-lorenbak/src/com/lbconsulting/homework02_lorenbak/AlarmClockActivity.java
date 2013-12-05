package com.lbconsulting.homework02_lorenbak;

// /////////////////////////////////////////////////////////////////////////////
// Requirements of this homework assignment include:
//  -	A black background without a title bar; and
//  -	To set an alarm via the option menu
// Since not all android devices have a physical option menu button but instead use 
// the application’s title bar to open the option menu I’ve design this application 
// to open the SetAlarmActivity through both an option menu and by touching 
// anywhere on the screen.
// /////////////////////////////////////////////////////////////////////////////

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.TouchDelegate;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class AlarmClockActivity extends Activity {

	// /////////////////////////////////////////////////////////////////////////////
	// AlarmClockActivity variables
	// /////////////////////////////////////////////////////////////////////////////

	// String for logging the class name
	public final String TAG = AlarmClockUtilities.TAG;
	private final boolean L = AlarmClockUtilities.L; // enable Logging

	private TextView txtTime = null;
	private RelativeLayout parentView = null;
	private TextView txtAlarmSet = null;

	final Handler timerHandler = new Handler();
	private String nextTime = "";
	private long nextTimeValue;

	private boolean alarmRunning = false;
	private long millsAlarmDateAndTime;
	private Calendar alarmDateAndTime;

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

		// Get the between instance stored values
		SharedPreferences storedStates = getSharedPreferences("AlarmClock", MODE_PRIVATE);
		// Set application states
		alarmRunning = storedStates.getBoolean("alarmRunning", false);
		millsAlarmDateAndTime = storedStates.getLong("millsAlarmDateAndTime", -1);
		alarmDateAndTime = Calendar.getInstance();
		if (millsAlarmDateAndTime < 0) {
			// millsAlarmDateAndTime set to it's default of -1		
			millsAlarmDateAndTime = alarmDateAndTime.getTimeInMillis();
		} else {
			alarmDateAndTime.setTimeInMillis(millsAlarmDateAndTime);
		}

		txtAlarmSet = (TextView) findViewById(R.id.txtAlarmSet);
		if (alarmRunning) {
			Calendar currentDateAndTime = Calendar.getInstance();
			long currentDate = currentDateAndTime.getTimeInMillis();
			String formattedCurrentDate = AlarmClockUtilities.formatDate(currentDate);

			String formattedAlarmDate = AlarmClockUtilities.formatDate(millsAlarmDateAndTime);
			String formattedAlarmDateAndTime = "";

			if (formattedCurrentDate.equals(formattedAlarmDate)) {
				formattedAlarmDateAndTime = getString(R.string.today);
				formattedAlarmDateAndTime = formattedAlarmDateAndTime
						+ AlarmClockUtilities.formatTimeNoSeconds(millsAlarmDateAndTime);
			} else {
				formattedAlarmDateAndTime = getString(R.string.alarm_set);
				formattedAlarmDateAndTime = formattedAlarmDateAndTime
						+ AlarmClockUtilities.formatDateAndTime(millsAlarmDateAndTime);
			}

			txtAlarmSet.setText(formattedAlarmDateAndTime);
			txtAlarmSet.setVisibility(View.VISIBLE);
		} else {
			txtAlarmSet.setVisibility(View.GONE);
		}
	}

	@Override
	protected void onPause() {
		super.onPause();

		// Notification that the activity will stop interacting with the user
		if (L)
			Log.i(TAG, "AlarmClockActivity onPause" + (isFinishing() ? " Finishing" : ""));

		// Nothing to store!
		// Store values between instances here

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
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_alarm_clock);

		this.parentView = (RelativeLayout) findViewById(R.id.layoutAlarmClockActivity);

		BroadcastReceiver receiver = new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
				// TODO Auto-generated method stub
				context.unregisterReceiver(this); // this == BroadcastReceiver, not Activity
			}
		};

		this.registerReceiver(receiver, new IntentFilter("com.lbconsulting.homework02_lorenbak.AlarmClock.Alarm"));

		parentView.post(new Runnable() {
			// Since some devices do not have a physical options button
			// Expand the txtTime's TextView hit rectangle to cover the entire screen
			// Post in the parent's message queue to make sure the parent
			// lays out its children before you call getHitRect() for the child view

			@Override
			public void run() {

				if (L)
					Log.i(TAG, "AlarmClockActivity parentView.post Run");

				// The bounds for the delegate view (an TextView
				// in this example)
				Rect delegateArea = new Rect();
				Rect parentArea = new Rect();
				txtTime = (TextView) findViewById(R.id.txtTime);
				txtTime.setTextColor(AlarmClockActivity.this.getResources().getColor(R.color.white));
				Typeface face = Typeface.createFromAsset(getAssets(), "fonts/Iceland-Regular.ttf");
				txtTime.setTypeface(face);

				txtTime.setEnabled(true);
				txtTime.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						Intent setAlarmActivityIntent = new Intent(AlarmClockActivity.this, SetAlarmActivity.class);
						AlarmClockActivity.this.startActivity(setAlarmActivityIntent);
					}
				});

				// The hit rectangle for the parent RelativeLayout
				parentView.getHitRect(parentArea);
				// The hit rectangle for the TextView
				txtTime.getHitRect(delegateArea);
				// make the TextView's hit rectangle the same as 
				// the parent's RelativeLayout hit rectangle
				delegateArea = parentArea;

				// Instantiate a TouchDelegate.
				// "delegateArea" is the bounds in local coordinates of 
				// the containing view to be mapped to the delegate view.
				// "txtTime" is the child view that should receive motion events.
				TouchDelegate touchDelegate = new TouchDelegate(delegateArea, txtTime);

				// Sets the TouchDelegate on the parent view, such that touches 
				// within the touch delegate bounds are routed to the child.
				if (View.class.isInstance(txtTime.getParent())) {
					((View) txtTime.getParent()).setTouchDelegate(touchDelegate);
				}

				Timer clockTimer = new Timer();
				nextTimeValue = Calendar.getInstance().getTimeInMillis();
				nextTimeValue += 1000;
				nextTime = getTime(nextTimeValue);

				clockTimer.schedule(new TimerTask() {
					@Override
					public void run() {
						UpdateGUI();
					}
				}, 0, 1000);

			}
		});

	} // End doCreate

	private void UpdateGUI() {
		timerHandler.post(displayTimeRunnable);
	}

	final Runnable displayTimeRunnable = new Runnable() {
		public void run() {
			// display the formatted time string
			txtTime.setText(nextTime);
			// get ready to show the next time
			nextTimeValue += 1000;
			nextTime = getTime(nextTimeValue);
		}

	};

	private String getTime(long timeInMillis) {
		return AlarmClockUtilities.formatTime(timeInMillis);
	}

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
			Intent setAlarmActivityIntent = new Intent(AlarmClockActivity.this, SetAlarmActivity.class);
			/*
			 * setAlarmActivityIntent.putExtra("key", value); //Optional
			 * parameters
			 */
			AlarmClockActivity.this.startActivity(setAlarmActivityIntent);

			return true;

		case R.id.action_settings:
			// TODO code menu masterListSettings
			msg = "There are no Settings for this application.";
			Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}

}
