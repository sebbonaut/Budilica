package com.example.sehorva.budilica;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by sehorva on 2/15/18.
 */

public class RingtonePlayingService extends Service {

    MediaPlayer media_song;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        //stvaranje instance media playera
        media_song = MediaPlayer.create(this, R.raw.dove);
        media_song.start();

        Log.e("LocalService", "Receiver start id "+startId+": "+intent);
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy(){
        //obavijesti korisnika da si zaustavljen
        Toast.makeText(this, "onDestroy pozvan", Toast.LENGTH_SHORT).show();
    }
}
