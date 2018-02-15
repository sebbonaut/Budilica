package com.example.sehorva.budilica;

import android.app.AlarmManager;
import android.content.Context;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

public class MainActivity extends AppCompatActivity {

    AlarmManager alarm_manager;
    TimePicker alarm_timepicker;
    TextView update_text;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.context = this;

        //inicijalizacija timepickera
        alarm_timepicker = (TimePicker) findViewById(R.id.timePicker);

        //inicijalizacija time managera
        alarm_manager = (AlarmManager) getSystemService(ALARM_SERVICE);

        //incijalizacija text boxa
        update_text = (TextView) findViewById(R.id.update_text);

        //stvaranje kalendara
        Calendar calendar = Calendar.getInstance();

        //incijalizacija start gumba
        Button alarm_on = (Button) findViewById(R.id.alarm_on);

        //stvaranje onClick listenera za pokretanje alarma
        //...

        //inicijalizacija stop gumba
        Button alarm_off = (Button) findViewById(R.id.alarm_off);

        //stvaranje onClick listenera za zaustavljanje alarma
        // ili poništavanje postavljanja alarma
        //...
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
