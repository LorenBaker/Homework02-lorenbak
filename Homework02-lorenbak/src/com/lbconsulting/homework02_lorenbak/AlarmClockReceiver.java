package com.lbconsulting.homework02_lorenbak;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class AlarmClockReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {

		Toast.makeText(context, "Alarm Intent Detected.", Toast.LENGTH_LONG).show();
	}

}
