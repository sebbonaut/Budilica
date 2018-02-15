package com.example.sehorva.budilica;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;



public class Alarm_Receiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("Unutar receivera.","OK");

        //dohvati extra string iz intenta
        String get_your_string = intent.getExtras().getString("extra");

        Log.e("Key jednak: ", get_your_string);

        //stvaramo intent za ringtone service
        Intent service_intent = new Intent(context, RingtonePlayingService.class);

        //proslijedi extra string iz MainActivity RingtonePlayingService-u
        service_intent.putExtra("extra", get_your_string);

        //pokrenemo ringtone service
        context.startService(service_intent);
    }
}
