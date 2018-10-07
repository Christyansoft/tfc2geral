package com.example.projetoteste.projetoteste;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import java.io.Serializable;

import dao.PosologiaDAO;
import model.Alarme;

public class AlarmService extends Service {

    public boolean rodando;
    private Ringtone r;
    PosologiaDAO posologiaDAO;

    public void setRodando(boolean rodando){
        this.rodando=rodando;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {

        posologiaDAO = new PosologiaDAO(AlarmService.this);

        String legenda = intent.getStringExtra("legenda");
        int intervalo = intent.getIntExtra("intervalo", 0);
        int idPending = intent.getIntExtra("idPending",0);

        Intent alarmIntent = new Intent(this, AlarmReceiver.class);
        alarmIntent.putExtra("legenda", legenda);

        long repeticao;

        if(intervalo==0) {
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

        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        r = RingtoneManager.getRingtone(getBaseContext(), notification);
        r.play();

        Vibrator vibrator;
        vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
        vibrator. vibrate(3000);

        rodando=true;
        Toast.makeText(this, "legenda:"+legenda, Toast.LENGTH_SHORT).show();
        sendNotification(legenda, idPending);

//        Intent sendIntent = new Intent(this,AlarmeAlert.class);
//
//        Bundle ex = new Bundle();
//
//        ex.putString("legenda", legenda);
//        sendIntent.putExtras(ex);
//
//        sendIntent.addFlags(sendIntent.FLAG_ACTIVITY_NEW_TASK);
//
//        startActivity(sendIntent);


        return START_NOT_STICKY;

    }


    private void sendNotification(String msg, int idPending) {
        Log.d("AlarmService", "Preparing to send notification...: " + msg);
        NotificationManager alarmNotificationManager = (NotificationManager) this
                .getSystemService(Context.NOTIFICATION_SERVICE);

        Intent intent = new Intent(AlarmService.this, ActionNotificacao.class);
        intent.putExtra("idPending", idPending);


        PendingIntent contentIntent = PendingIntent.getActivity(this, idPending,
                intent, 0);

        NotificationCompat.Builder alamNotificationBuilder = new NotificationCompat.Builder(
                this).setContentTitle("Hora de tomar seu medicamento")
                .setVisibility(Notification.VISIBILITY_PUBLIC)
                .setSmallIcon(R.drawable.ic_medication)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
                .setContentText(msg)
                .setAutoCancel(true)
                .setOngoing(true)
                .addAction(R.drawable.ic_close_black_24dp, "Dispensar", contentIntent )
                .setContentIntent(contentIntent);



        alamNotificationBuilder.setContentIntent(contentIntent);
        alarmNotificationManager.notify(idPending, alamNotificationBuilder.build());

    }

    @Override
    public void onDestroy() {

        r.stop();
    //    super.onDestroy();


    }


}




