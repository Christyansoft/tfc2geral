package com.example.projetoteste.projetoteste;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;


import java.util.ArrayList;

import dao.PosologiaDAO;
import model.Alarme;

public class ClassePrincipal extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private PosologiaDAO posologiaDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classe_principal);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);

        Button sair = findViewById(R.id.logoff);
        Button btnMedicamento = findViewById(R.id.btnMedicamento);
        Button btnLaboratorio = findViewById(R.id.btnLaboratorio);
        Button btnPrincipio = findViewById(R.id.princiA);
        Button btnClasse = findViewById(R.id.classeT);
        Button btnCodigo = findViewById(R.id.btnCodBarras);



        firebaseAuth=FirebaseAuth.getInstance();

        sair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                posologiaDAO = new PosologiaDAO(ClassePrincipal.this);
                ArrayList<Alarme> arrayAlarme;
                arrayAlarme = posologiaDAO.selectAllAlarme();
                posologiaDAO.close();

                if(!arrayAlarme.isEmpty()){
                    showDialogo();
                }
                else{
                    firebaseAuth = FirebaseAuth.getInstance();
                    firebaseAuth.signOut();
                    Toast.makeText(ClassePrincipal.this, "Logout realizado com sucesso", Toast.LENGTH_SHORT).show();
                    finish();
                }

            }
        });


        btnMedicamento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ClassePrincipal.this, ListaMedicamentos.class);
                startActivity(intent);
            }
        });

        btnLaboratorio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ClassePrincipal.this, ListaLaboratorios.class);
                startActivity(intent);
            }
        });

        btnPrincipio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ClassePrincipal.this, ListaPrincipioAtivo.class);
                startActivity(intent);
            }
        });

        btnClasse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ClassePrincipal.this, ListaClasseTerapeutica.class);
                startActivity(intent);
            }
        });


        btnCodigo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ClassePrincipal.this, ResultadoBarras.class);
                startActivity(intent);
            }
        });



    }

    private void showDialogo(){
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        //define o titulo
        builder.setTitle("Informação de conta");
        //define a mensagem

        builder.setMessage("Não é possível fazer sair no momento pois existe alarmes cadastrados, caso queira realmente sair, apague os alarmes antes.");

        //define um botão como positivo

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {

            }
        });

        //cria o AlertDialog
        builder.create().show();
        //Exibe

    }


}
