package com.example.projetoteste.projetoteste;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AlarmeAlert extends AppCompatActivity {

    private boolean alarmActive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarme_alert);

        TextView med = findViewById(R.id.txtMed);
        TextView quant = findViewById(R.id.txtQuant);
        TextView obs = findViewById(R.id.txtObs);
        TextView horaAtual = findViewById(R.id.txtHoraAtual);

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String horario = sdf.format(cal.getTime());

        final Bundle bundle = getIntent().getExtras();
        if (bundle.containsKey("legenda")){
            med.setText((String) bundle.get("legenda"));
            quant.setText((String) bundle.get("quantidade"));
            obs.setText((String) bundle.get("obs"));
            horaAtual.setText(horario);
        }

 //       final Window window = getWindow();
 //       window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
//                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
//        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
//                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
//
//        setContentView(R.layout.activity_alarme_alert);


        Button desliga = findViewById(R.id.btnDesliga);

        desliga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                stopAlarme();
                Vibrator vibrator;
                vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
                vibrator.cancel();

            }
        });

    }

    private void stopAlarme(){
        Intent serviceIntent = new Intent(AlarmeAlert.this,AlarmService.class);
        stopService(serviceIntent);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        alarmActive = true;
    }

    @Override
    public void onBackPressed() {
        if (!alarmActive) {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        stopAlarme();
        super.onDestroy();
    }
}
