package com.example.sehorva.budilica;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by sehorva on 2/15/18.
 */

public class Alarm_Receiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("Unutar receivera.","OK");
    }
}
