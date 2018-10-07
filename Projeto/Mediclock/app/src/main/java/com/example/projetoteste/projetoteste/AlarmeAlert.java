package com.example.projetoteste.projetoteste;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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

import model.Alarme;

public class AlarmeAlert extends AppCompatActivity {

    private boolean alarmActive;
    Button desliga;
    TextView text;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarme_alert);

        text = findViewById(R.id.txtInfo);

        String legenda = "";
        final Bundle bundle = getIntent().getExtras();
        if (bundle.containsKey("legenda")){
            legenda = (String) bundle.get("legenda");
            Toast.makeText(this, "legenda: "+legenda, Toast.LENGTH_SHORT).show();
            text.setText(legenda);

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
