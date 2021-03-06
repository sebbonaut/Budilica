package com.example.sehorva.budilica;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.sql.Time;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    public static final String MYPREFS = "mojePreference";

    AlarmManager alarm_manager;
    TimePicker alarm_timepicker;
    TextView update_text;
    Context context;
    CheckBox repeat_alarm;
    Spinner spinner;

    //string u textviewu (obavijest o postavljanju alarma)
    String obavijest = " ";

    //ponavljam alarm ili ne
    boolean repeating;

    //boja pozadine
     int boja_pozadine = Color.WHITE;
     int boja_teksta = Color.BLACK;

    //za stvaranje alarm managera
    PendingIntent pending_intent;
    int choose_alarm_sound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.context = this;

        //orijentacija
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        //nema ponavljanja po defaultu
        repeating = false;

        //inicijalizacija timepickera
        alarm_timepicker = (TimePicker) findViewById(R.id.timePicker);

        //inicijalizacija time managera
        alarm_manager = (AlarmManager) getSystemService(ALARM_SERVICE);

        //incijalizacija text boxa
        update_text = (TextView) findViewById(R.id.update_text);

        //incijalizacija check boxa
        repeat_alarm = (CheckBox) findViewById(R.id.checkBox);

        //stvaranje kalendara
        final Calendar calendar = Calendar.getInstance();

        //stvaranje intenta za Alarm Receiver klasu
        final Intent my_intent = new Intent(this.context, Alarm_Receiver.class);

        //incijalizacija start gumba
        Button alarm_on = (Button) findViewById(R.id.alarm_on);

        //stvaranje onClick listenera za pokretanje alarma
        alarm_on.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                //postavljanje kalendara sa odabranim satom i minutama iz time pickera
                calendar.set(Calendar.HOUR_OF_DAY, alarm_timepicker.getHour());
                calendar.set(Calendar.MINUTE, alarm_timepicker.getMinute());

                //dohvati vrijednosti za sate i minute
                int hour = alarm_timepicker.getHour();
                int minute = alarm_timepicker.getMinute();

                //pretvorim gornje intove u stringove
                String hour_string = String.valueOf(hour);
                String minute_string = String.valueOf(minute);

                //12:5 --> 12:05
                if(minute < 10)
                {
                    minute_string = "0" + String.valueOf(minute);
                }

                //metoda koja mijenja tekst u textboxu
                obavijest = "Alarm postavljen u " + hour_string + " sati i " + minute_string + " minuta.";
                set_alarm_text(obavijest);

                //stavi extra string u my_intent
                //kaže clock-u da je pritisnut "Postavi" gumb
                my_intent.putExtra("extra", "alarm on");

                //stavi extra long u my_intent
                //kaže clock-u koju smo pjesmu (tj. id) izabrali iz spinnera
                my_intent.putExtra("alarm_choice", choose_alarm_sound);


                //stvori pending intent koji odgađa intent do zadanog vremena kalendara
                pending_intent = PendingIntent.getBroadcast(MainActivity.this, 0, my_intent, PendingIntent.FLAG_UPDATE_CURRENT);


                if(repeating == false) {
                    //postavimo alarm manager
                    alarm_manager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pending_intent);
                }
                else //if(repeating == true)
                {
                    //za ponavljanje
                    long repeating_interval = 5 * 1000; //millis
                    alarm_manager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), repeating_interval, pending_intent);
                }
            }
        });

        //stvaranje spinnera
        spinner = (Spinner) findViewById(R.id.alarm_spinner);
        //stvaranje ArrayAdaptera koristeći string array i defaultni spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.budilica_niz, R.layout.support_simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        //postavljanje onClick listenera za onItemSelected metodu
        spinner.setOnItemSelectedListener(this);


        //inicijalizacija stop gumba
        Button alarm_off = (Button) findViewById(R.id.alarm_off);

        //stvaranje onClick listenera za zaustavljanje alarma
        // ili poništavanje postavljanja alarma
        alarm_off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //metoda koja mijenja tekst u textboxu
                obavijest = "Alarm isključen!";
                set_alarm_text(obavijest);

                //otkaži alarm
                try {
                    PendingIntent sender = PendingIntent.getBroadcast(context, 0, my_intent, 0);
                    alarm_manager.cancel(sender);
                } catch (Exception e){}

                //stavi extra string u my_intent
                //kaže clock-u da je pritisnut "Odbaci" gumb
                my_intent.putExtra("extra", "alarm off");

                //također stavim extra long (da se program ne skrši (Null Pointer Exception))
                my_intent.putExtra("alarm_choice", choose_alarm_sound);
                Log.e("Alarm id je ", String.valueOf(choose_alarm_sound));

                //zaustavljanje zvuka alarma
                sendBroadcast(my_intent);
            }
        });

        //postavljanje boja na početku
       // setVariousColors();
    }


    private void set_alarm_text(String s) {
        update_text.setTextColor(boja_teksta);
        update_text.setText(s);
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
        if (id == R.id.background_color) {
            if(boja_pozadine == Color.WHITE)
            {
                //pozadina u crnu, tekst bijeli
                boja_pozadine = Color.BLACK;
                boja_teksta = Color.WHITE;
            }
            else if(boja_pozadine == Color.BLACK) {
                //pozadina u bijelu, tekst crn
                boja_pozadine = Color.WHITE;
                boja_teksta = Color.BLACK;
            }

            setVariousColors();
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    //pomoćna funkcija za postavljanje nekih boja
    public void setVariousColors()
    {
        //postavljanje boje pozadine
        this.findViewById(R.id.pozadina).setBackgroundColor(boja_pozadine);
        ((CheckBox)this.findViewById(R.id.checkBox)).setTextColor(boja_teksta);

        ((Spinner)this.findViewById(R.id.alarm_spinner)).setBackgroundColor(boja_pozadine);

        try {
            ((TextView) ((Spinner) this.findViewById(R.id.alarm_spinner)).getChildAt(0)).setTextColor(boja_teksta);
        } catch(Exception e) {}

        set_alarm_text(obavijest);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {

        //koji id je izabran iz spinnera
        //Toast.makeText(this, "spinner item je "+id, Toast.LENGTH_SHORT).show();
        choose_alarm_sound = (int) id;

        ((TextView) ((Spinner) this.findViewById(R.id.alarm_spinner)).getChildAt(0)).setTextColor(boja_teksta);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public void itemClicked(View view)
    {
        //pogledam je li označen checkbox za ponavljanje alarma
        CheckBox checkBox = (CheckBox) view;
        if(checkBox.isChecked()){
            repeating = true;
        }
        else{
            repeating = false;
        }
        Log.e("Checkbox kliknut", "postavljeno = " + repeating);
    }

    @Override
    public void onPause() {
        super.onPause();
        savePreferences();
    }

    @Override
    public void onResume(){
        super.onResume();
        loadPreferences();
        setVariousColors();
        ((CheckBox) this.findViewById(R.id.checkBox)).setChecked(repeating);
        if(obavijest.compareTo("Alarm isključen!") == 0)
        {
            obavijest = "Nema zakazanih alarma!";
        }
        ((TextView) this.findViewById(R.id.update_text)).setText(obavijest);
    }



    protected void savePreferences(){
        //stvorimo shared preference
        int mode=MODE_PRIVATE;
        SharedPreferences mySharedPreferences=getSharedPreferences(MYPREFS,mode);

        //editor za modificiranje shared preference
        SharedPreferences.Editor editor=mySharedPreferences.edit();

        //spremamo vrijednosti u shared preference
        editor.putInt("bojaPozadine", boja_pozadine);
        editor.putInt("bojaTeksta", boja_teksta);
        editor.putBoolean("ponavljanje", repeating);
        editor.putString("obavijest", obavijest);

        //commit promjene
        editor.commit();
    }

    public void loadPreferences(){
        // dohvatimo preference
        int mode=MODE_PRIVATE;
        SharedPreferences mySharedPreferences=getSharedPreferences(MYPREFS,mode);

        //dohvatimo vrijednosti
        boja_pozadine = mySharedPreferences.getInt("bojaPozadine", boja_pozadine);
        boja_teksta = mySharedPreferences.getInt("bojaTeksta", boja_teksta);
        repeating = mySharedPreferences.getBoolean("ponavljanje", repeating);
        obavijest = mySharedPreferences.getString("obavijest", " ");
    }

}
