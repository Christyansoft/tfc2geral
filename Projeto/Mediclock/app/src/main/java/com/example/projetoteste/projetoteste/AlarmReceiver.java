package com.example.projetoteste.projetoteste;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.Ringtone;


public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        String legenda = intent.getStringExtra("legenda");
        String quantidade = intent.getStringExtra("quantidade");
        int idPending = intent.getIntExtra("idPending",0);
        int intervalo = intent.getIntExtra("intervalo",0);
        String obs = intent.getStringExtra("obs");

        Intent serviceIntent = new Intent(context, AlarmService.class);

        serviceIntent.putExtra("legenda", legenda);
        serviceIntent.putExtra("idPending", idPending);
        serviceIntent.putExtra("intervalo", intervalo);
        serviceIntent.putExtra("quantidade", quantidade);
        serviceIntent.putExtra("obs", obs);
        context.startService(serviceIntent);

    }


}





