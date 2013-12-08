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
import android.app.AlarmManager;
import android.app.DialogFragment;
import android.app.PendingIntent;
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
import android.os.Vibrator;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.TouchDelegate;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lbconsulting.homework02_lorenbak.TimePickerFragment.TimePicker;

public class AlarmClockActivity extends Activity implements TimePicker {

	// /////////////////////////////////////////////////////////////////////////////
	// AlarmClockActivity variables 
	// /////////////////////////////////////////////////////////////////////////////

	// String for logging the class name
	public final String TAG = AlarmClockUtilities.TAG;
	private final boolean L = AlarmClockUtilities.L; // enable Logging

	private static TextView txtTime = null;
	private RelativeLayout parentView = null;
	private TextView txtAlarmSet = null;
	private Button btnCancel = null;

	final Handler timerHandler = new Handler();
	private String nextTime = "";
	private long nextTimeValue;

	private boolean receiverRegistered = false;
	private boolean alarmSet = false;
	private boolean alarmRunning = false;
	private long millsAlarmDateAndTime;
	private Calendar alarmDateAndTime;
	private Vibrator v;

	private static int colorWhite;
	private static int colorRed;

	private int hourOfDay;
	private int minute;
	private int minYear;
	private int minMonth;
	private int minDay_of_month;
	private int minHourOfDay;
	private int minMinute;

	private AlarmManager alManagerStart;
	private PendingIntent pintent;
	private BroadcastReceiver receiver;
	private Handler stopAlarmHandler;
	public static String ALARM_BROADCAST_INTENT = "com.lbconsulting.homework02_lorenbak.AlarmClock.Alarm";

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
		this.alarmSet = storedStates.getBoolean("alarmSet", false);
		this.millsAlarmDateAndTime = storedStates.getLong("millsAlarmDateAndTime", -1);
		if (this.alarmSet && millsAlarmDateAndTime > 0) {
			this.SetAlarm();
		}
	}

	@Override
	protected void onPause() {
		super.onPause();

		// Notification that the activity will stop interacting with the user
		if (L)
			Log.i(TAG, "AlarmClockActivity onPause" + (isFinishing() ? " Finishing" : ""));

		SharedPreferences preferences = getSharedPreferences("AlarmClock", MODE_PRIVATE);
		SharedPreferences.Editor applicationStates = preferences.edit();

		applicationStates.putBoolean("alarmSet", this.alarmSet);
		applicationStates.putLong("millsAlarmDateAndTime", this.millsAlarmDateAndTime);

		// Commit to storage
		applicationStates.commit();

		if (receiver != null && receiverRegistered) {
			this.unregisterReceiver(receiver);
			if (L)
				Log.i(TAG, "AlarmClockActivity: receiver has been unregistered");
			receiver = null;
		}
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

		AlarmClockActivity.colorWhite = this.getResources().getColor(R.color.white);
		AlarmClockActivity.colorRed = this.getResources().getColor(R.color.red);

		AlarmClockActivity.txtTime = (TextView) findViewById(R.id.txtTime);
		txtTime.setTextColor(AlarmClockActivity.colorWhite);
		Typeface face = Typeface.createFromAsset(getAssets(), "fonts/Iceland-Regular.ttf");
		txtTime.setTypeface(face);

		this.btnCancel = (Button) findViewById(R.id.btnCancel);
		this.btnCancel.setTypeface(face);
		btnCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				// cancel alarm
				if (L)
					Log.i(TAG, "AlarmClockActivity btnCancel pressed.");
				if (alManagerStart != null) {
					alManagerStart.cancel(pintent);
				}

				if (receiver != null && receiverRegistered) {
					getBaseContext().unregisterReceiver(receiver);
					receiver = null;
				}
				StopAlarm();
			}

		});

		this.parentView = (RelativeLayout) findViewById(R.id.layoutAlarmClockActivity);
		this.txtAlarmSet = (TextView) findViewById(R.id.txtAlarmSet);
		this.txtAlarmSet.setTypeface(face);
		this.txtAlarmSet.setText(R.string.touch_screen_to_set_alarm_);
		v = (Vibrator) getSystemService(AlarmClockActivity.VIBRATOR_SERVICE);

		this.pintent = PendingIntent.getBroadcast(this, 0, new Intent(ALARM_BROADCAST_INTENT), 0);
		this.alarmDateAndTime = Calendar.getInstance();

		this.parentView.post(new Runnable() {
			// Since some devices do not have a physical options button
			// Expand the txtTime's TextView hit rectangle to cover the entire screen
			// Post in the parent's message queue to make sure the parent
			// lays out its children before you call getHitRect() for the child view

			@Override
			public void run() {

				if (L)
					Log.i(TAG, "AlarmClockActivity parentView.post Run");

				// The bounds for the delegate view (an TextView in this example)
				Rect delegateArea = new Rect();
				Rect parentArea = new Rect();

				// set the alarm via clicking the screen
				txtTime.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						DialogFragment newFragment = new TimePickerFragment();
						newFragment.show(getFragmentManager(), "timePicker");
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
				/*nextTimeValue = Calendar.getInstance().getTimeInMillis();*/
				nextTimeValue = System.currentTimeMillis();
				nextTimeValue = nextTimeValue / 1000;
				nextTimeValue = nextTimeValue * 1000;

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
			if (alarmRunning) {
				v.vibrate(400);
			}
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
			DialogFragment newFragment = new TimePickerFragment();
			newFragment.show(getFragmentManager(), "timePicker");
			return true;

		case R.id.action_settings:
			// TODO code menu masterListSettings
			msg = getString(R.string.no_settings);
			Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void SetAlarm() {
		if (L)
			Log.i(TAG, "AlarmClockActivity SetAlarm");
		// cancel any previous set alarms
		if (alManagerStart != null) {
			if (L)
				Log.i(TAG, "AlarmClockActivity previously set alarms cancled.");
			alManagerStart.cancel(pintent);
		}

		// create a new BroadcastReceiver to catch the alarm
		receiver = new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
				TurnOnAlarm();
				context.unregisterReceiver(receiver);
				receiverRegistered = false;
			}
		};
		// register the receiver ... set receiverRegistered flag
		this.registerReceiver(receiver, new IntentFilter(ALARM_BROADCAST_INTENT));
		this.receiverRegistered = true;

		// set the AlarmManager
		alManagerStart = (AlarmManager) (this.getSystemService(Context.ALARM_SERVICE));
		alManagerStart.set(AlarmManager.RTC, millsAlarmDateAndTime, pintent);
		this.alarmSet = true;

		// display the alarm time
		txtAlarmSet.setText("Alarm set for: " + AlarmClockUtilities.formatTimeNoSeconds(millsAlarmDateAndTime));
		txtAlarmSet.setTextColor(AlarmClockActivity.colorRed);
		btnCancel.setVisibility(View.VISIBLE);
	}

	public void TurnOnAlarm() {
		if (L)
			Log.i(TAG, "AlarmClockActivity TurnOnAlarm");
		txtTime.setTextColor(colorRed);
		alarmRunning = true;

		// stop the running alarm in 5 seconds
		stopAlarmHandler = new Handler();
		stopAlarmHandler.postDelayed(TurnOffAlarm, 5000);
	}

	private Runnable TurnOffAlarm = new Runnable() {
		@Override
		public void run() {
			if (L)
				Log.i(TAG, "AlarmClockActivity TurnOffAlarm");
			StopAlarm();
		}
	};

	private void StopAlarm() {
		txtTime.setTextColor(colorWhite);
		btnCancel.setVisibility(View.GONE);
		alarmRunning = false;
		alarmSet = false;
		txtAlarmSet.setText(R.string.touch_screen_to_set_alarm_);
		txtAlarmSet.setTextColor(AlarmClockActivity.colorWhite);
		stopAlarmHandler = null;
		alManagerStart = null;
	}

	@Override
	public void onTimeSelected(Bundle timeBundle) {
		this.hourOfDay = timeBundle.getInt("hourOfDay");
		this.minute = timeBundle.getInt("minute");
		this.setMinimumDatesAndTimes();

		if (hourOfDay < minHourOfDay) {
			this.hourOfDay = this.minHourOfDay;
			this.minute = this.minMinute;
			this.signalError();

		} else if (minute < minMinute && hourOfDay == minHourOfDay) {
			this.minute = this.minMinute;
			this.signalError();

		} else {

			if (L)
				Log.i(TAG, "AlarmClockActivity onTimeSelected");
			this.setAlarmDateAndTime();
			SetAlarm();
		}
	}

	private void setAlarmDateAndTime() {
		alarmDateAndTime.set(
				this.minYear,
				this.minMonth,
				this.minDay_of_month,
				this.hourOfDay,
				this.minute,
				0
				);
		this.millsAlarmDateAndTime = this.alarmDateAndTime.getTimeInMillis();
		// round to a whole second
		this.millsAlarmDateAndTime = this.millsAlarmDateAndTime / 1000;
		this.millsAlarmDateAndTime = this.millsAlarmDateAndTime * 1000;
	}

	private void signalError() {
		/*Vibrator v = (Vibrator) getSystemService(AlarmClockActivity.VIBRATOR_SERVICE);*/
		// Vibrate for 500 milliseconds
		v.vibrate(500);

	}

	private void setMinimumDatesAndTimes() {
		Calendar calendarNow = Calendar.getInstance();
		this.minYear = calendarNow.get(Calendar.YEAR);
		this.minMonth = calendarNow.get(Calendar.MONTH);
		this.minDay_of_month = calendarNow.get(Calendar.DAY_OF_MONTH);
		this.minHourOfDay = calendarNow.get(Calendar.HOUR_OF_DAY);
		this.minMinute = calendarNow.get(Calendar.MINUTE);
	}
}
