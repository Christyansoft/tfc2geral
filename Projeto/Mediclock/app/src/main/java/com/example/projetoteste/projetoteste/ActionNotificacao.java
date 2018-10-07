package com.example.projetoteste.projetoteste;

import android.content.Intent;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class ActionNotificacao extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext().getApplicationContext());
        notificationManagerCompat.cancel(getIntent().getIntExtra("idPending",0));

        Intent serviceIntent = new Intent(ActionNotificacao.this,AlarmService.class);
        stopService(serviceIntent);

        finish();
    }
}
