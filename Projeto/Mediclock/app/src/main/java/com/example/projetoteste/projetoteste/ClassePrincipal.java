package com.example.projetoteste.projetoteste;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;


import java.util.ArrayList;

import model.CodigoBarras;
import model.Medicamento;

public class ClassePrincipal extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;

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
        Button btnMedico = findViewById(R.id.btnMedico);
        Button btnCodigo = findViewById(R.id.btnCodBarras);
        Button btnTra = findViewById(R.id.btnTrata);
        Button imgPaciente = findViewById(R.id.imageView2);
        Button btnMedLocal = findViewById(R.id.medicamento2);
        Button btnDiag = findViewById(R.id.btnDiagnostico);

        firebaseAuth=FirebaseAuth.getInstance();

        if(!firebaseAuth.getCurrentUser().getEmail().equals("christyansoftperes@gmail.com")){
            btnMedicamento.setVisibility(View.INVISIBLE);
            btnLaboratorio.setVisibility(View.INVISIBLE);
            btnPrincipio.setVisibility(View.INVISIBLE);
            btnClasse.setVisibility(View.INVISIBLE);
        }


        sair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth = FirebaseAuth.getInstance();
                firebaseAuth.signOut();
                finish();
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

        btnMedico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ClassePrincipal.this, ListaMedicos.class);
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

        btnTra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ClassePrincipal.this, ListaTratamento.class);
                startActivity(intent);
            }
        });

        imgPaciente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ClassePrincipal.this, ListaPacientes.class);
                startActivity(intent);
            }
        });

        btnDiag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ClassePrincipal.this, ListaDiagnosticos.class);
                startActivity(intent);
            }
        });

        btnMedLocal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ClassePrincipal.this, ListaMedicamentosLocal.class);
                startActivity(intent);
            }
        });
    }


}
