package com.example.sehorva.budilica;

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

        //stvaranje instance media playera
        media_song = MediaPlayer.create(this, R.raw.dove);
        media_song.start();

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy(){
        //obavijesti korisnika da si zaustavljen
        Toast.makeText(this, "onDestroy pozvan", Toast.LENGTH_SHORT).show();
    }
}
