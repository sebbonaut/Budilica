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
        //kaže je li pritisnut gumb "Postavi" ili "Odbaci"
        String get_your_string = intent.getExtras().getString("extra");

        Log.e("Key jednak: ", get_your_string);

        //dohvati extra long iz intenta
        //kaže koju pjesmu (koji id) je izabrao korisnik iz spinnera
        int get_your_alarm_choice = intent.getExtras().getInt("alarm_choice");

        Log.e("Izabrani alarm je ", String.valueOf(get_your_alarm_choice));

        //stvaramo intent za ringtone service
        Intent service_intent = new Intent(context, RingtonePlayingService.class);

        //proslijedi extra string iz MainActivity RingtonePlayingService-u
        service_intent.putExtra("extra", get_your_string);

        //proslijedi extra long iz Receivera u RingtonePlayingService
        service_intent.putExtra("alarm_choice", get_your_alarm_choice);

        //pokrenemo ringtone service
        context.startService(service_intent);
    }
}
