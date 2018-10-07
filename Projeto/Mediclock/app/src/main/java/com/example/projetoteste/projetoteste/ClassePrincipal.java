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

    private ArrayList<Medicamento> lista = new ArrayList<Medicamento>();

    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private Query medicamentoRef = databaseReference.child("medicamento").orderByChild("barras1").equalTo("7896472502981");

    private FirebaseAuth firebaseAuth;

    private ArrayList<Medicamento> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classe_principal);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);

        Button sair = findViewById(R.id.logoff);

        sair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth = FirebaseAuth.getInstance();
                firebaseAuth.signOut();
                finish();
            }
        });

        Button btnMedicamento = findViewById(R.id.btnMedicamento);

        btnMedicamento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ClassePrincipal.this, ListaMedicamentos.class);
                startActivity(intent);
            }
        });

        Button btnLaboratorio = findViewById(R.id.btnLaboratorio);

        btnLaboratorio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ClassePrincipal.this, ListaLaboratorios.class);
                startActivity(intent);
            }
        });

        Button btnPrincipio = findViewById(R.id.princiA);

        btnPrincipio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ClassePrincipal.this, ListaPrincipioAtivo.class);
                startActivity(intent);
            }
        });

        Button btnClasse = findViewById(R.id.classeT);

        btnClasse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ClassePrincipal.this, ListaClasseTerapeutica.class);
                startActivity(intent);
            }
        });

        Button btnMedico = findViewById(R.id.btnMedico);

        btnMedico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ClassePrincipal.this, ListaMedicos.class);
                startActivity(intent);
            }
        });

        Button btnCodigo = findViewById(R.id.btnCodBarras);

        btnCodigo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ClassePrincipal.this, ResultadoBarras.class);
                startActivity(intent);
            }
        });

        Button btnTra = findViewById(R.id.btnTrata);

        btnTra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ClassePrincipal.this, ListaTratamento.class);
                startActivity(intent);
            }
        });

        ImageButton imgPaciente = findViewById(R.id.imageView2);

        imgPaciente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ClassePrincipal.this, ListaPacientes.class);
                startActivity(intent);
            }
        });

        Button btnDiag = findViewById(R.id.btnDiagnostico);

        btnDiag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ClassePrincipal.this, ListaDiagnosticos.class);
                startActivity(intent);
            }
        });

        Button btnMedLocal = findViewById(R.id.medicamento2);

        btnMedLocal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ClassePrincipal.this, ListaMedicamentosLocal.class);
                startActivity(intent);
            }
        });
    }

    public void onClick(View view){

        Toast.makeText(this, "clicado", Toast.LENGTH_SHORT).show();

    }


    @Override
    protected void onNewIntent(Intent intent) {

        String codigoBarras = intent.getStringExtra("resultadoCodigo");
        TextView textView = findViewById(R.id.resultado);
        textView.setText(codigoBarras);

    }
}
