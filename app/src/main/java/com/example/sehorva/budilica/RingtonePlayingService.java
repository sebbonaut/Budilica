package com.example.sehorva.budilica;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;



public class RingtonePlayingService extends Service {

    MediaPlayer media_song;
    int startId;
    boolean isRunning;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        Log.e("LocalService", "Receiver start id "+startId+": "+intent);

        //dohvati extra string vrijednosti
        String state = intent.getExtras().getString("extra");

        Log.e("Ringtone state extra: ", state);



        //pretvara extra stringove iz intenta u start Idijove, 0 ili 1
        assert state != null;
        switch (state) {
            case "alarm on":
                startId = 1;
                break;
            case "alarm off":
                startId = 0;
                Log.e("Start Id je ", state);
                break;
            default:
                startId = 0;
                break;
        }

        //if elseovi

        //ako ništa ne svira i pritisne se "Postavi" --> glazba treba početi svirati
        if(!this.isRunning && startId == 1){
            Log.e("nema glazbe:", "sviraj");

            //stvaranje instance media playera
            media_song = MediaPlayer.create(this, R.raw.dove);
            //pokreni alarm
            media_song.start();

            this.isRunning = true;
            this.startId = 0;

            //notifikacija
            //postavi notification service
            NotificationManager notify_manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

            //postaviti intent koji ide MainActivity
            Intent intent_main_activity = new Intent(this.getApplicationContext(), MainActivity.class);

            //postaviti pending intent (jer ne ide odmah, čeka alarm)
            PendingIntent pending_intent_main_activity = PendingIntent.getActivity(this, 0,
                    intent_main_activity, 0);

            //notification parametri
            Notification notification_popup = new Notification.Builder(this)
                    .setContentTitle("Alarm svira!")
                    .setContentText("Klikni me!")
                    .setContentIntent(pending_intent_main_activity)
                    .setAutoCancel(true) //kad kliknemo na njega, automatski nestane
                    .build();

            //postaviti notification start command
            notify_manager.notify(0,notification_popup);

        }

        //ako se svira i pritisne se "Odbaci" --> glazba treba prestati
        else if(this.isRunning && startId == 0){
            Log.e("glazba svira:","zaustavi");

            //zaustavi alarm
            media_song.stop();
            media_song.reset();

            this.isRunning = false;
            this.startId = 0;
        }

        //ako ništa ne svira i pritisne se "Odbaci" --> ne radi ništa
        else if(!this.isRunning /*&& startId == 0*/) {
            Log.e("nema glazbe:", "zaustavi");

            this.isRunning = false;
            this.startId  = 0;
        }

        //ako glazba svira i pritisne se "Postavi" --> ništa
        else /*if(this.isRunning && startId == 1)*/ {
            Log.e("glazba svira:", "sviraj?");

            this.isRunning = true;
            this.startId = 1;
        }

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy(){
        //obavijesti korisnika da si zaustavljen
        Log.e("pozvan onDestroy", "kraj");

        super.onDestroy();
        this.isRunning = false;
    }
}
