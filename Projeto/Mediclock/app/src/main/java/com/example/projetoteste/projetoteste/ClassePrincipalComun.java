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

public class ClassePrincipalComun extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    PosologiaDAO posologiaDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classe_principal_comun);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);

        Button sair = findViewById(R.id.logoff);
        Button btnMedico = findViewById(R.id.btnMedico);
        Button btnCodigo = findViewById(R.id.btnCodBarras);
        Button btnTra = findViewById(R.id.btnTrata);
        Button imgPaciente = findViewById(R.id.imageView2);
        Button btnMedLocal = findViewById(R.id.medicamento2);
        Button btnDiag = findViewById(R.id.btnDiagnostico);

        sair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                posologiaDAO = new PosologiaDAO(ClassePrincipalComun.this);
                ArrayList<Alarme> arrayAlarme = new ArrayList<>();
                arrayAlarme = posologiaDAO.selectAllAlarme();
                posologiaDAO.close();

                if(!arrayAlarme.isEmpty()){
                    showDialogo();
                }
                else{
                    firebaseAuth = FirebaseAuth.getInstance();
                    firebaseAuth.signOut();
                    Toast.makeText(ClassePrincipalComun.this, "Logout realizado com sucesso", Toast.LENGTH_SHORT).show();
                    finish();
                }

            }
        });


        btnMedico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ClassePrincipalComun.this, ListaMedicos.class);
                startActivity(intent);
            }
        });

        btnCodigo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ClassePrincipalComun.this, ResultadoBarras.class);
                startActivity(intent);
            }
        });

        btnTra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ClassePrincipalComun.this, ListaTratamento.class);
                startActivity(intent);
            }
        });

        imgPaciente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ClassePrincipalComun.this, ListaPacientes.class);
                startActivity(intent);
            }
        });

        btnDiag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ClassePrincipalComun.this, ListaDiagnosticos.class);
                startActivity(intent);
            }
        });

        btnMedLocal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ClassePrincipalComun.this, ListaMedicamentosLocal.class);
                startActivity(intent);
            }
        });


    }

    public void showDialogo(){
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
