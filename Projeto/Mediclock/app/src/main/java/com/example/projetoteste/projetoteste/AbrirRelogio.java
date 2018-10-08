package com.example.projetoteste.projetoteste;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
    Alarme alarme2;
    Medicamento med;
    PosologiaDAO posologiaDAO;
    long retornoDB;
    ArrayList<Medicamento> arrayMedicamento;

    ImageView clock;
    Button salvar, desativar;

    private TimePicker alarmTimePicker;
    String hora, minuto;

    Context context;

    ArrayList<Alarme> arrayList;
    TextView textView, medInfo;
    Spinner spIntervalo;
    String idTratamento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_abrir_relogio);

        preencherS();

        textView = findViewById(R.id.textView);
        medInfo = findViewById(R.id.medInfo);
        medInfo.setVisibility(View.INVISIBLE);
        textView.setVisibility(View.INVISIBLE);
        alarmTimePicker = findViewById(R.id.relogio);
        alarmTimePicker.setIs24HourView(true);
        salvar = findViewById(R.id.btnAlarme);
        desativar = findViewById(R.id.btnDesativar);
        desativar.setVisibility(View.INVISIBLE);

        clock = findViewById(R.id.clock);
        clock.setVisibility(View.INVISIBLE);

        posologiaDAO = new PosologiaDAO(AbrirRelogio.this);
        arrayList = posologiaDAO.selectAllAlarme();

        Intent i = getIntent();

        alarme2 = (Alarme) i.getSerializableExtra("alarme-enviado");
        Intent i2 = getIntent();
        med = (Medicamento) i2.getSerializableExtra("med-enviado");
        idTratamento = i2.getStringExtra("idTratamento");

        if(alarme2!=null){
            alarmTimePicker.setVisibility(View.INVISIBLE);
            salvar.setVisibility(View.INVISIBLE);
            textView.setVisibility(View.VISIBLE);
            spIntervalo.setVisibility(View.INVISIBLE);
            clock.setVisibility(View.VISIBLE);
            desativar.setVisibility(View.VISIBLE);
            String info = "Horário(s) do "+alarme2.getLegenda();
            medInfo.setVisibility(View.VISIBLE);
            medInfo.setText(info);
            textView.setText(alarme2.getRepeating());

        }

        this.context = this;

        final Calendar calendar = Calendar.getInstance();


        salvar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                final int hour = alarmTimePicker.getCurrentHour();
                final int minute = alarmTimePicker.getCurrentMinute();

                hora = String.valueOf(hour);
                minuto = String.valueOf(minute);

                Random gerador = new Random();

                Calendar now = Calendar.getInstance();
                Calendar alarm = Calendar.getInstance();
                alarm.set(Calendar.HOUR_OF_DAY, alarmTimePicker.getCurrentHour());
                alarm.set(Calendar.MINUTE, alarmTimePicker.getCurrentMinute());

                int segundo = 0;

                if(arrayList.isEmpty()) {
                    alarm.set(Calendar.SECOND, gerador.nextInt(20 + 2));
                    alarm.set(Calendar.MILLISECOND, gerador.nextInt(20 + 2));
                }
                else{
                    segundo = arrayList.get(arrayList.size()-1).getSegundo();
                    alarm.set(Calendar.SECOND, segundo+3);
                    alarm.set(Calendar.MILLISECOND, segundo+3);

                }

                if (alarm.before(now)) {
                    //Add 1 day if time selected before now
                    alarm.add(Calendar.DAY_OF_MONTH, 1);
                }

                calendar.set(Calendar.HOUR_OF_DAY, alarmTimePicker.getCurrentHour());
                calendar.set(Calendar.MINUTE, alarmTimePicker.getCurrentMinute());

                int idPendingAnterior = 1;

                if(!arrayList.isEmpty()){
                     idPendingAnterior = arrayList.get(arrayList.size()-1).getIdAlarme();
                     idPendingAnterior = idPendingAnterior+1;
                }

                Alarme alarme = new Alarme();

                alarme.setHora(alarmTimePicker.getCurrentHour());
                alarme.setMinuto(alarmTimePicker.getCurrentMinute());
                alarme.setSegundo(segundo+3);
                alarme.setLegenda(med.getNomeMedicamento());
                alarme.setIdPending(idPendingAnterior);

                Toast.makeText(AbrirRelogio.this, "segundo "+alarme.getSegundo(), Toast.LENGTH_SHORT).show();

                int intervalo = selecionaIntervalo();

                if(intervalo!=0) {
                    alarme.setRepeating(somar(calendar, intervalo));
                }
                else{
                    alarme.setRepeating("2 em 2 minutos");
                }
                alarme.setIdMedicamento(med.getIdMedicamento());
                alarme.setIdTratameto(idTratamento);

                retornoDB = posologiaDAO.salvarAlarme(alarme);

                if (retornoDB == -1){
                    //deu erro
                    Toast.makeText(AbrirRelogio.this, "Erro na gravação", Toast.LENGTH_SHORT).show();
                }else{

                    //sucesso
                    Toast.makeText(AbrirRelogio.this, "Cadastrado com sucesso", Toast.LENGTH_SHORT).show();

                    Intent myIntent = new Intent(context, AlarmReceiver.class);
                    myIntent.putExtra("legenda", med.getNomeMedicamento());
                    myIntent.putExtra("idPending", idPendingAnterior);
                    if(intervalo==0) {
                        myIntent.putExtra("intervalo", intervalo);
                    }
                    else{
                        myIntent.putExtra("intervalo", 24 / intervalo);
                    }

                    PendingIntent pending_intent = PendingIntent.getBroadcast(AbrirRelogio.this, idPendingAnterior, myIntent, 0);
                    AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, alarm.getTimeInMillis(), pending_intent);


                    finish();

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
                    Toast.makeText(AbrirRelogio.this, "alarme cancelado", Toast.LENGTH_SHORT).show();

            }
        });


    }

    public boolean Verifica(Medicamento med){

        boolean resultado = false;

        for(Medicamento medicamento: arrayMedicamento){

            if(medicamento.getIdMedicamento().equals(med.getIdMedicamento())){
                resultado = true;
                break;
            }
            else{
                resultado = false;
            }
        }

        return  resultado;
    }

    
    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public int selecionaIntervalo(){

        int id = spIntervalo.getSelectedItemPosition();
        int id2 = 0;

        switch (id){
            case 0:
                id2 = 0;
                break;
            case 1:
                id2= 24;
                break;
            case 2:
                id2= 12;
                break;
            case 3:
                id2= 8;
                break;
            case 4:
                id2= 6;
                break;
            case 5:
                id2= 4;
                break;
            case 6:
                id2 = 4;
                break;
            case 7:
                id2= 3;
                break;
            case 8:
                id2 =3;
                break;
            case 9:
                id2= 2;
                break;
            case 10:
                id2= 2;
                break;
            case 11:
                id2 = 2;
                break;
            case 12:
                id2= 2;
                break;
            case 13:
                id2 =1;
                break;
        }

        return id2;
    }

    public String somar(Calendar calendar, int intervalo){

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

    public void preencherS(){

        spIntervalo = findViewById(R.id.spinner);

        list = new ArrayList<String>();
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
