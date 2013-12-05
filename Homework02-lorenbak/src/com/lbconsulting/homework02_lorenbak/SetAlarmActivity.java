package com.lbconsulting.homework02_lorenbak;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.DialogFragment;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import com.lbconsulting.homework02_lorenbak.DatePickerFragment.DatePicker;
import com.lbconsulting.homework02_lorenbak.TimePickerFragment.TimePicker;

/*import com.example.homework02_lorenbak.R;*/

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

	private Calendar alarmDateAndTime;
	private long millsAlarmDateAndTime;
	private long millsNow;
	// private long millsCountdownTime;

	private int year;
	private int month;
	private int day_of_month;
	private int hourOfDay;
	private int minute;

	private int minYear;
	private int minMonth;
	private int minDay_of_month;
	private int minHourOfDay;
	private int minMinute;

	private boolean alarmRunning = false;
	/*private SimpleDateFormat sdfTime;*/
	/*private SimpleDateFormat sdfDate;*/

	private Timer myTimer;

	final Handler timerHandler = new Handler();
	private String nextAlarmCountdownText = "";
	private long nextAlarmCountdownValueMills = -1;

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
		// Get the between instance stored values
		SharedPreferences storedStates = getSharedPreferences("AlarmClock", MODE_PRIVATE);

		// Set application states
		this.alarmRunning = storedStates.getBoolean("alarmRunning", false);
		this.millsAlarmDateAndTime = storedStates.getLong("millsAlarmDateAndTime", -1);
		this.alarmDateAndTime = Calendar.getInstance();

		if (this.millsAlarmDateAndTime < 0) {
			// first time run and millsAlarmDateAndTime set to it's default of -1		
			this.millsAlarmDateAndTime = this.alarmDateAndTime.getTimeInMillis();
		} else {
			this.alarmDateAndTime.setTimeInMillis(this.millsAlarmDateAndTime);
		}

		this.year = alarmDateAndTime.get(Calendar.YEAR);
		this.month = alarmDateAndTime.get(Calendar.MONTH);
		this.day_of_month = alarmDateAndTime.get(Calendar.DAY_OF_MONTH);
		this.hourOfDay = alarmDateAndTime.get(Calendar.HOUR_OF_DAY);
		this.minute = alarmDateAndTime.get(Calendar.MINUTE);

		this.setMinimumDatesAndTimes();

		this.showDate();
		this.showTime();

		this.nextAlarmCountdownValueMills = this.millsAlarmDateAndTime - Calendar.getInstance().getTimeInMillis();
		this.nextAlarmCountdownText = getAlarmCountdownText(this.nextAlarmCountdownValueMills);

		if (this.alarmRunning) {
			this.startTimer();
		} else {
			this.showCountdown(nextAlarmCountdownText);
		}
	}

	@Override
	protected void onPause() {
		super.onPause();

		// Notification that the activity will stop interacting with the user
		if (L)
			Log.i(TAG, "SetAlarmActivity onPause" + (isFinishing() ? " Finishing" : ""));

		// Store values between instances here
		SharedPreferences preferences = getSharedPreferences("AlarmClock", MODE_PRIVATE);
		SharedPreferences.Editor applicationStates = preferences.edit();

		applicationStates.putBoolean("alarmRunning", alarmRunning);
		applicationStates.putLong("millsAlarmDateAndTime", millsAlarmDateAndTime);

		// Commit to storage
		applicationStates.commit();

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

	public void SetAlarm()
	{
		//final Button button = buttons[2]; // replace with a button from your own UI
		/*BroadcastReceiver receiver = new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
				// TODO Auto-generated method stub
				context.unregisterReceiver(this); // this == BroadcastReceiver, not Activity
			}
		};

		this.registerReceiver(receiver, new IntentFilter("com.lbconsulting.homework02_lorenbak.AlarmClock.Alarm"));
		*/
		PendingIntent pintent = PendingIntent.getBroadcast(this, 0, new Intent(
				"com.lbconsulting.homework02_lorenbak.AlarmClock.Alarm"), 0);
		AlarmManager manager = (AlarmManager) (this.getSystemService(Context.ALARM_SERVICE));

		// set alarm to fire 5 sec (1000*5) from now (SystemClock.elapsedRealtime())
		/*manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + 1000 * 5, pintent);*/
		manager.set(AlarmManager.RTC_WAKEUP, this.millsAlarmDateAndTime, pintent);
	}

	private void UpdateGUI() {
		timerHandler.post(myRunnable);
	}

	final Runnable myRunnable = new Runnable() {
		public void run() {
			showCountdown(nextAlarmCountdownText);
			nextAlarmCountdownValueMills -= 1000;
			if (nextAlarmCountdownValueMills < 0) {
				stopTimer();
			} else {
				nextAlarmCountdownText = getAlarmCountdownText(nextAlarmCountdownValueMills);
			}
		}
	};

	public void showDatePickerDialog(View v) {
		DialogFragment newFragment = new DatePickerFragment();
		newFragment.show(getFragmentManager(), "datePicker");
	}

	public void showTimePickerDialog(View v) {
		DialogFragment newFragment = new TimePickerFragment();
		newFragment.show(getFragmentManager(), "timePicker");
	}

	public void startAlarmTimer(View view) {
		this.startTimer();
	}

	public void stopAlarmTimer(View view) {
		this.stopTimer();
	}

	private void startTimer() {
		this.SetAlarm();
		this.alarmRunning = true;
		this.nextAlarmCountdownValueMills = this.millsAlarmDateAndTime - Calendar.getInstance().getTimeInMillis();
		nextAlarmCountdownText = this.getAlarmCountdownText(nextAlarmCountdownValueMills);

		myTimer = new Timer();
		myTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				UpdateGUI();
			}
		}, 0, 1000);
	}

	private void stopTimer() {
		this.myTimer.cancel();
		this.alarmRunning = false;
		this.showCountdown(this.getAlarmCountdownText(this.nextAlarmCountdownValueMills));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.set_alarm, menu);
		return true;
	}

	@Override
	public void onDateSelected(Bundle dateBundle) {
		this.year = dateBundle.getInt("year");
		this.month = dateBundle.getInt("month");
		this.day_of_month = dateBundle.getInt("day_of_month");
		this.setMinimumDatesAndTimes();

		if (this.year < minYear) {
			this.year = this.minYear;
			this.month = this.minMonth;
			this.day_of_month = this.minDay_of_month;
			this.signalError();

		} else if (month < minMonth && year == minYear) {
			this.month = this.minMonth;
			this.day_of_month = this.minDay_of_month;
			this.signalError();

		} else if (day_of_month < minDay_of_month && month == minMonth && year == minYear) {
			this.day_of_month = this.minDay_of_month;
			this.signalError();

		} else {
			this.setAlarmDateAndTime();
			this.showDate();
		}
	}

	@Override
	public void onTimeSelected(Bundle timeBundle) {
		this.hourOfDay = timeBundle.getInt("hourOfDay");
		this.minute = timeBundle.getInt("minute");
		this.setMinimumDatesAndTimes();

		if (hourOfDay < minHourOfDay && day_of_month == minDay_of_month && month == minMonth && year == minYear) {
			this.hourOfDay = this.minHourOfDay;
			this.minute = this.minMinute;
			this.signalError();

		} else if (minute < minMinute && hourOfDay == minHourOfDay && day_of_month == minDay_of_month
				&& month == minMonth && year == minYear) {
			this.minute = this.minMinute;
			this.signalError();

		} else {
			this.setAlarmDateAndTime();
			this.showTime();
		}
	}

	private String getAlarmCountdownText(long millsCountdownTime) {
		String countDownString = "Alarm Not Running";
		if (this.alarmRunning) {
			millsCountdownTime = (millsCountdownTime / 1000);

			long x = millsCountdownTime;
			long seconds = x % 60;
			x /= 60;
			long minutes = x % 60;
			x /= 60;
			long hours = x % 24;
			x /= 24;
			long days = x;

			if (millsCountdownTime > 0) {

				if (days > 0) {
					countDownString =
							String.valueOf(days) + "d: "
									+ String.valueOf(hours) + "h: "
									+ String.valueOf(minutes) + "m: "
									+ String.valueOf(seconds) + "s";
				} else if (hours > 0) {
					countDownString =
							String.valueOf(hours) + "h: "
									+ String.valueOf(minutes) + "m: "
									+ String.valueOf(seconds) + "s";

				} else if (minutes > 0) {
					countDownString =
							String.valueOf(minutes) + "m: "
									+ String.valueOf(seconds) + "s";
				} else {
					countDownString =
							String.valueOf(seconds) + "s";
				}
			} else {
				// millsCountdownTime not > 0
				countDownString = "0s";
			}
		}
		return countDownString;
	}

	private void showCountdown(String countDownText) {
		this.tvAlarmCountdown.setText(countDownText);
	}

	private void showDate() {
		String formattedDate = AlarmClockUtilities.formatDate(this.alarmDateAndTime.getTimeInMillis());
		this.tvAlarmDate.setText(formattedDate);
	}

	private void showTime() {
		String formattedDate = AlarmClockUtilities.formatTimeNoSeconds(this.alarmDateAndTime.getTimeInMillis());
		this.tvAlarmTime.setText(formattedDate);
	}

	private void setAlarmDateAndTime() {
		this.alarmDateAndTime.set(
				this.year,
				this.month,
				this.day_of_month,
				this.hourOfDay,
				this.minute,
				0
				);
		this.millsAlarmDateAndTime = this.alarmDateAndTime.getTimeInMillis();
	}

	private void setMinimumDatesAndTimes() {
		Calendar calendarNow = Calendar.getInstance();
		this.millsNow = calendarNow.getTimeInMillis();
		this.minYear = calendarNow.get(Calendar.YEAR);
		this.minMonth = calendarNow.get(Calendar.MONTH);
		this.minDay_of_month = calendarNow.get(Calendar.DAY_OF_MONTH);
		this.minHourOfDay = calendarNow.get(Calendar.HOUR_OF_DAY);
		this.minMinute = calendarNow.get(Calendar.MINUTE);
	}

	private void signalError() {
		Vibrator v = (Vibrator) getSystemService(SetAlarmActivity.VIBRATOR_SERVICE);
		// Vibrate for 500 milliseconds
		v.vibrate(500);
	}
}
