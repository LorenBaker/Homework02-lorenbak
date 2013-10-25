package com.example.homework02_lorenbak;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class AlarmClockActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_clock);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.alarm_clock, menu);
        return true;
    }
    
}
