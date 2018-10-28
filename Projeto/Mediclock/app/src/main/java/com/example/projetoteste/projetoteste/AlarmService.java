package com.example.projetoteste.projetoteste;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Vibrator;
import android.support.annotation.Nullable;

public class AlarmService extends Service {

    private boolean rodando;
    private Ringtone r;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {

        String legenda = intent.getStringExtra("legenda");
        String quantidade = intent.getStringExtra("quantidade");
        int intervalo = intent.getIntExtra("intervalo", 0);
        int idPending = intent.getIntExtra("idPending",0);
        String obs = intent.getStringExtra("obs");

        Intent alarmIntent = new Intent(this, AlarmReceiver.class);
        alarmIntent.putExtra("legenda", legenda);
        alarmIntent.putExtra("quantidade", quantidade);
        alarmIntent.putExtra("obs", obs);

        long repeticao;

        if(intervalo==25) {
            //  long scTime = 60*1000;//mins
            repeticao = 60*2000;
        }
        else{
            repeticao = 60*1000*60*intervalo;
        }

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, idPending, alarmIntent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + repeticao, pendingIntent);

        if(rodando){
            r.stop();
        }

        Intent sendIntent = new Intent(this,AlarmeAlert.class);

        Bundle ex = new Bundle();

        ex.putString("legenda",legenda);
        ex.putString("quantidade", quantidade);
        ex.putString("obs", obs);
        sendIntent.putExtras(ex);

        sendIntent.addFlags(sendIntent.FLAG_ACTIVITY_NEW_TASK);

        startActivity(sendIntent);


        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        r = RingtoneManager.getRingtone(getBaseContext(), notification);
        r.play();

        Vibrator vibrator;
        vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
        vibrator. vibrate(3000);

        rodando=true;
        //   sendNotification(legenda, idPending);



        return START_NOT_STICKY;


    }


    @Override
    public void onDestroy() {

        r.stop();
        //    super.onDestroy();


    }


}




