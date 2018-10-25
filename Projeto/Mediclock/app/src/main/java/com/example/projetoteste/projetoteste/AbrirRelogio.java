package com.example.projetoteste.projetoteste;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.graphics.Color;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import dao.PosologiaDAO;
import model.Alarme;
import model.Medicamento;

public class AbrirRelogio extends AppCompatActivity {

    List<String> list;
    private Alarme alarme2;
    private Medicamento med;
    private PosologiaDAO posologiaDAO;
    private long retornoDB;

    private TimePicker relogio;
    private Context context;

    private ArrayList<Alarme> arrayList;
    private EditText quant, horario, obs;
    private Spinner spIntervalo;
    private String idTratamento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_abrir_relogio);

        preencherS();
        obs = findViewById(R.id.edtObs);
        Button abrirR = findViewById(R.id.btnAddH);
        horario = findViewById(R.id.edtHorario);
        horario.setEnabled(false);
        horario.setTextColor(Color.BLACK);
        quant = findViewById(R.id.edtQtd);
        TextView textView = findViewById(R.id.textView);
        TextView medInfo = findViewById(R.id.medInfo);
        medInfo.setVisibility(View.INVISIBLE);
        textView.setVisibility(View.INVISIBLE);
        Button salvar = findViewById(R.id.btnAlarme);
        Button desativar = findViewById(R.id.btnDesativar);
        desativar.setVisibility(View.INVISIBLE);

        ImageView clock = findViewById(R.id.clock);
        clock.setVisibility(View.INVISIBLE);

        posologiaDAO = new PosologiaDAO(AbrirRelogio.this);
        arrayList = posologiaDAO.selectAllAlarme();

        Intent i = getIntent();

        alarme2 = (Alarme) i.getSerializableExtra("alarme-enviado");
        Intent i2 = getIntent();
        med = (Medicamento) i2.getSerializableExtra("med-enviado");
        idTratamento = i2.getStringExtra("idTratamento");

        if(alarme2!=null){

            obs.setVisibility(View.INVISIBLE);
            abrirR.setVisibility(View.INVISIBLE);
            horario.setVisibility(View.INVISIBLE);
            quant.setVisibility(View.INVISIBLE);
            salvar.setVisibility(View.INVISIBLE);
            textView.setVisibility(View.VISIBLE);
            spIntervalo.setVisibility(View.INVISIBLE);
            clock.setVisibility(View.VISIBLE);
            desativar.setVisibility(View.VISIBLE);
            String info = "Horário(s) do "+alarme2.getLegenda();
            medInfo.setVisibility(View.VISIBLE);
            medInfo.setText(info);
            medInfo.setTextColor(Color.BLACK);
            textView.setText(alarme2.getRepeating());
            textView.setTextColor(Color.BLACK);
        }

        this.context = this;

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Posologia");
        if(alarme2==null) {
            toolbar.setSubtitle(med.getNomeMedicamento());
        }
        setSupportActionBar(toolbar);

        final Calendar calendar = Calendar.getInstance();

        abrirR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(AbrirRelogio.this);
                View view1 = getLayoutInflater().inflate(R.layout.diagrelogio, null);
                relogio = view1.findViewById(R.id.relogio);
                Button ok = view1.findViewById(R.id.ok);
                relogio.setIs24HourView(true);
                builder.setView(view1);
                final AlertDialog dialog = builder.create();
                dialog.show();

                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String hora = String.valueOf(relogio.getHour());
                        String minuto = String.valueOf(relogio.getMinute());

                        if(relogio.getHour()==0){
                            hora +=0;
                        }else if(relogio.getHour()<10){
                            hora="0"+relogio.getHour();
                        }

                        if(relogio.getMinute()==0){
                            minuto+=0;
                        }else if(relogio.getMinute()<10){
                            minuto="0"+relogio.getMinute();
                        }

                        String horarios = hora+":"+minuto;

                        horario.setText(horarios);
                        dialog.dismiss();

                    }
                });

            }
        });

        salvar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (quant.getText().toString().equals("")) {
                    quant.setError("Informe a quantidade da medicação");
                } else if (spIntervalo.getSelectedItemPosition() == 0) {

                    TextView errorText = (TextView) spIntervalo.getSelectedView();
                    errorText.setError("Informe o intervalo");

                } else if(horario.getText().toString().equals("")) {

                    horario.setError("Informe o horario da medicação");
                }
                else{

                    Random gerador = new Random();

                  //  Calendar now = Calendar.getInstance();
                    Calendar alarm = Calendar.getInstance();
                    alarm.set(Calendar.HOUR_OF_DAY, relogio.getHour());
                    alarm.set(Calendar.MINUTE, relogio.getMinute());
                    alarm.set(Calendar.SECOND, 0);
                    alarm.set(Calendar.MILLISECOND, 0);

                int segundo;

                if(arrayList.isEmpty()) {
                    alarm.set(Calendar.SECOND, gerador.nextInt(20 + 2));
                    alarm.set(Calendar.MILLISECOND, gerador.nextInt(20 + 2));
                }
                else{
                    segundo = arrayList.get(arrayList.size()-1).getSegundo();
                    alarm.set(Calendar.SECOND, segundo+3);
                    alarm.set(Calendar.MILLISECOND, segundo+3);

                }

//                    if (alarm.before(now)) {
//                        //Add 1 day if time selected before now
//                        alarm.add(Calendar.DAY_OF_MONTH, 1);
//                    }

                    if(!alarm.after(Calendar.getInstance()))
                        alarm.roll(Calendar.DATE, true);

                    calendar.set(Calendar.HOUR_OF_DAY, relogio.getHour());
                    calendar.set(Calendar.MINUTE, relogio.getMinute());

                    int idPendingAnterior = 1;

                    if (!arrayList.isEmpty()) {
                        idPendingAnterior = arrayList.get(arrayList.size() - 1).getIdAlarme();
                        idPendingAnterior = idPendingAnterior + 1;
                    }

                    Alarme alarme = new Alarme();

                    alarme.setHora(relogio.getHour());
                    alarme.setMinuto(relogio.getMinute());
                    alarme.setSegundo(0);
                    alarme.setLegenda(med.getNomeMedicamento());
                    alarme.setIdPending(idPendingAnterior);

                    int intervalo = selecionaIntervalo();

                    if (intervalo != 25) {
                        alarme.setRepeating(somar(calendar, intervalo));
                    } else {
                        alarme.setRepeating("2 em 2 minutos");
                    }
                    alarme.setIdMedicamento(med.getIdMedicamento());
                    alarme.setIdTratameto(idTratamento);

                    retornoDB = posologiaDAO.salvarAlarme(alarme);

                    if (retornoDB == -1) {
                        //deu erro
                        Toast.makeText(AbrirRelogio.this, "Erro na gravação", Toast.LENGTH_SHORT).show();
                    } else {

                        //sucesso
                        Toast.makeText(AbrirRelogio.this, "Cadastrado com sucesso", Toast.LENGTH_SHORT).show();

                        Intent myIntent = new Intent(context, AlarmReceiver.class);
                        myIntent.putExtra("legenda", med.getNomeMedicamento());
                        myIntent.putExtra("idPending", idPendingAnterior);
                        myIntent.putExtra("quantidade", quant.getText().toString());
                        myIntent.putExtra("obs", obs.getText().toString());

                        if (intervalo == 25) {
                            myIntent.putExtra("intervalo", intervalo);
                        } else {
                            myIntent.putExtra("intervalo", 24 / intervalo);
                        }

                        PendingIntent pending_intent = PendingIntent.getBroadcast(AbrirRelogio.this, idPendingAnterior, myIntent, 0);
                        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

                        alarmManager.setExact(AlarmManager.RTC_WAKEUP, alarm.getTimeInMillis(), pending_intent);

                        finish();

                    }

                }
            }

        });


        desativar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int id = alarme2.getIdPending();

                    Intent alarmIntent = new Intent(context, AlarmReceiver.class);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(context, id, alarmIntent, 0);
                    AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                    alarmManager.cancel(pendingIntent);
                    retornoDB = posologiaDAO.deletarAlarme(alarme2);
                    finish();
                    Toast.makeText(AbrirRelogio.this, "alarme desativado", Toast.LENGTH_SHORT).show();

            }
        });


    }

    
    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private int selecionaIntervalo(){

        int id = spIntervalo.getSelectedItemPosition();
        int id2 = 0;

        switch (id){

            case 1:
                id2= 25;
                break;
            case 2:
                id2= 24;
                break;
            case 3:
                id2= 12;
                break;
            case 4:
                id2= 8;
                break;
            case 5:
                id2= 6;
                break;
            case 6:
                id2= 4;
                break;
            case 7:
                id2 = 4;
                break;
            case 8:
                id2= 3;
                break;
            case 9:
                id2 =3;
                break;
            case 10:
                id2= 2;
                break;
            case 11:
                id2= 2;
                break;
            case 12:
                id2 = 2;
                break;
            case 13:
                id2= 2;
                break;
            case 14:
                id2 =1;
                break;
        }

        return id2;
    }

    private String somar(Calendar calendar, int intervalo){

        int divisao = 24/intervalo;
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String repeticao = "";

        for(int i = 0; i< intervalo; i++){
            calendar.add(Calendar.HOUR, divisao);
            repeticao = repeticao+ sdf.format(calendar.getTime())+", \t";
        }
      //  showDialogo(repeticao);

        return repeticao;
    }

    public void showDialogo(String info){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //define o titulo
        builder.setTitle(info);
        //define a mensagem
        builder.setMessage("");
        //define um botão como positivo

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {

            }
        });

        //cria o AlertDialog
        AlertDialog alerta = builder.create();
        //Exibe
        alerta.show();
    }

    private void preencherS(){

        spIntervalo = findViewById(R.id.spinner);

        list = new ArrayList<>();
        list.add("Selecione o intervalo");
        list.add("2 em 2 minutos");
        list.add("1 em 1 hora");
        list.add("2 em 2 horas");
        list.add("3 em 3 horas");
        list.add("4 em 4 horas");
        list.add("5 em 5 horas");
        list.add("6 em 6 horas");
        list.add("7 em 7 horas");
        list.add("8 em 8 horas");
        list.add("9 em 9 horas");
        list.add("10 em 10 horas");
        list.add("11 em 11 horas");
        list.add("12 em 12 horas");
        list.add("1x ao dia");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spIntervalo.setAdapter(dataAdapter);
    }

}
