package com.example.projetoteste.projetoteste;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import java.io.Serializable;

import model.Alarme;

import static android.content.Context.ACTIVITY_SERVICE;


public class AlarmReceiver extends BroadcastReceiver {

    MediaPlayer mediaPlayer;
    private Ringtone ringtone;
    Context context;

    @Override
    public void onReceive(Context context, Intent intent) {

        String legenda = intent.getStringExtra("legenda");
        int idPending = intent.getIntExtra("idPending",0);
        int intervalo = intent.getIntExtra("intervalo",0);

        Intent serviceIntent = new Intent(context, AlarmService.class);

            serviceIntent.putExtra("legenda", legenda);
            serviceIntent.putExtra("idPending", idPending);
            serviceIntent.putExtra("intervalo", intervalo);
          //  serviceIntent.putExtra("idPending", idPending);
            context.startService(serviceIntent);

    }


}





