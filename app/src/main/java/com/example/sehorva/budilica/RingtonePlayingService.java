package com.example.sehorva.budilica;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import java.util.Random;


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

        //dohvati extra string vrijednosti iz alarm on/alarm off vrijednosti
        String state = intent.getExtras().getString("extra");

        //dohvati alarm izbor int vrijednosti
        Integer alarm_sound_choice = intent.getExtras().getInt("alarm_choice");


        Log.e("Ringtone state extra: ", state);
        Log.e("Alarm izbor je: ", alarm_sound_choice.toString());



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


        //notifikacija
        //postavi notification service
        NotificationManager notify_manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        //postaviti intent koji ide MainActivity
        Intent intent_main_activity = new Intent(this.getApplicationContext(), MainActivity.class);

        //postaviti pending intent (jer ne ide odmah, čeka alarm)
        PendingIntent pending_intent_main_activity = PendingIntent.getActivity(this, 0,
                intent_main_activity, 0);

        String channelId = "some_channel_id";

        CharSequence channelName = "Some Channel";
        int importance = NotificationManager.IMPORTANCE_LOW;
        NotificationChannel notificationChannel = new NotificationChannel(channelId, channelName, importance);
        notificationChannel.enableLights(true);
        notificationChannel.setLightColor(Color.RED);
        notificationChannel.enableVibration(true);
        notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
        notify_manager.createNotificationChannel(notificationChannel);

        int notifyId = 1;



        //notification parametri
        Notification notification_popup = new Notification.Builder(this, channelId)
                .setSmallIcon(R.drawable.clock)
                .setContentTitle("Alarm svira!")
                .setContentText("Klikni me!")
                .setContentIntent(pending_intent_main_activity)
                .setAutoCancel(true) //kad kliknemo na njega, automatski nestane
                .build();


        //if elseovi

        //ako ništa ne svira i pritisne se "Postavi" --> glazba treba početi svirati
        if(!this.isRunning && startId == 1){
            Log.e("nema glazbe:", "sviraj");


            this.isRunning = true;
            this.startId = 0;


            //postaviti notification start command
            notify_manager.notify(0,notification_popup);

            if(alarm_sound_choice == 0)
            {
                //sviraj random izabranu stvar
                int number_of_songs = 5;

                Random random_number = new Random();
                int alarm_number = random_number.nextInt(number_of_songs)+1;
                Log.e("Slučajan broj je ", String.valueOf(alarm_number));
                alarm_sound_choice = alarm_number;
            }

            //stvaranje instance media playera
            switch (alarm_sound_choice)
            {
                case 1:
                    media_song = MediaPlayer.create(this, R.raw.bip);
                    break;
                case 2:
                    media_song = MediaPlayer.create(this, R.raw.dove);
                    break;
                case 3:
                    media_song = MediaPlayer.create(this, R.raw.laganini);
                    break;
                case 4:
                    media_song = MediaPlayer.create(this, R.raw.moderni);
                    break;
                case 5:
                    media_song = MediaPlayer.create(this, R.raw.reklama);
                    break;
                default:
                    media_song = MediaPlayer.create(this, R.raw.bip);
                    break;
            }

            //pokreni alarm
            media_song.start();
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

        //ako glazba svira i pritisne se "Postavi" --> ponovo pokreni
        else /*if(this.isRunning && startId == 1)*/ {
            Log.e("glazba svira:", "sviraj?");

            this.isRunning = true;
            this.startId = 1;

            //zaustavi alarm
            media_song.stop();
            media_song.reset();

            //stvaranje instance media playera
            switch (alarm_sound_choice)
            {
                case 1:
                    media_song = MediaPlayer.create(this, R.raw.bip);
                    break;
                case 2:
                    media_song = MediaPlayer.create(this, R.raw.dove);
                    break;
                case 3:
                    media_song = MediaPlayer.create(this, R.raw.laganini);
                    break;
                case 4:
                    media_song = MediaPlayer.create(this, R.raw.moderni);
                    break;
                case 5:
                    media_song = MediaPlayer.create(this, R.raw.reklama);
                    break;
                default:
                    media_song = MediaPlayer.create(this, R.raw.bip);
                    break;
            }

            //pokreni alarm
            media_song.start();
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
