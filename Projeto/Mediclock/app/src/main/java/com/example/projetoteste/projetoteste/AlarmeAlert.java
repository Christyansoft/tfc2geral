package com.example.projetoteste.projetoteste;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import model.Alarme;

public class AlarmeAlert extends AppCompatActivity {

    private boolean alarmActive;
    Button desliga;
    TextView med, quant, obs, horaAtual;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarme_alert);

        med = findViewById(R.id.txtMed);
        quant = findViewById(R.id.txtQuant);
        obs = findViewById(R.id.txtObs);
        horaAtual = findViewById(R.id.txtHoraAtual);

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String horario = sdf.format(cal.getTime());

        final Bundle bundle = getIntent().getExtras();
        if (!bundle.isEmpty()){
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


        desliga = findViewById(R.id.btnDesliga);

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

    public void stopAlarme(){
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
