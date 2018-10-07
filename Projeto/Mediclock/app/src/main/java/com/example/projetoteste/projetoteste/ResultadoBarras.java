package com.example.projetoteste.projetoteste;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import model.CodigoBarras;
import model.Medicamento;

public class ResultadoBarras extends AppCompatActivity {

    TextView edtLab, edtPrinc, edtClass, edtDesc;
    EditText edtEnt;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    Query medicamentoRef = databaseReference.child("medicamento").orderByChild("barras1").equalTo("7896637023658");
    Button buscar;
    ImageButton scanner;

    private ArrayList<Medicamento> arrayList = new ArrayList<>();
    Medicamento med = new Medicamento();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultado_barras);

     //   med = null;

        edtEnt = findViewById(R.id.edtEntrada);
        edtDesc = findViewById(R.id.edtDescBarras);
        edtLab = findViewById(R.id.edtLabBarras);
        edtPrinc = findViewById(R.id.edtPrincBarras);
        edtClass = findViewById(R.id.edtClassBarras);
        buscar = findViewById(R.id.btnBusca);
        scanner = findViewById(R.id.addCod);

        scanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ResultadoBarras.this, CodigoBarras.class);
                intent.putExtra("leitura", "leitura");
                startActivity(intent);
            }
        });

        final ArrayList<Medicamento> params=new ArrayList();

        buscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(edtEnt.getText().toString().equals("")){
                    edtEnt.setError("Informe um codigo ou digitalize para pesquisar");
                }
                else {

                    medicamentoRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()){

                                medicamentoRef.addChildEventListener(new ChildEventListener() {
                                    @Override
                                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                                        Medicamento med = dataSnapshot.getValue(Medicamento.class);
                                        edtDesc.setText(med.getNomeMedicamento());
                                        edtLab.setText(med.getNomeLaboratorio());
                                        edtPrinc.setText(med.getNomePrincipioAtivo());
                                        edtClass.setText(med.getNomeClasseTerapeutica());

                                    }

                                    @Override
                                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                                    }

                                    @Override
                                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                                    }

                                    @Override
                                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });

                            }
                            else{
                                Toast.makeText(ResultadoBarras.this, "nao existe", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }
        });


    }



 }
