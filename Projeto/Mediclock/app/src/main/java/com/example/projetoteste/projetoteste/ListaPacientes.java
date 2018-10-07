package com.example.projetoteste.projetoteste;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.firebase.ui.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;

import model.Paciente;


public class ListaPacientes extends AppCompatActivity {

   DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    FloatingActionButton addPaciente;
    ListView minhaLista;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_pacientes);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        String usuario = firebaseAuth.getCurrentUser().getEmail();

        Firebase.setAndroidContext(this);
        com.firebase.client.Query firebaseObj = new Firebase("https://projeto-teste-32601.firebaseio.com/paciente").orderByChild("usuarioPaciente").equalTo(usuario);

        addPaciente = findViewById(R.id.btnAddPaciente);

        addPaciente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ListaPacientes.this, CadastroPaciente.class);
                startActivity(intent);
            }
        });


        minhaLista = findViewById(R.id.lvPacientes);

        final FirebaseListAdapter<Paciente> adapter = new FirebaseListAdapter<Paciente>(this, Paciente.class, android.R.layout.simple_list_item_1, firebaseObj) {

            @Override
            protected void populateView(View view, final Paciente paciente, int i) {

                TextView textView = view.findViewById(android.R.id.text1);
                textView.setText(paciente.getNomePaciente());
            }
        };
        minhaLista.setAdapter(adapter);

        minhaLista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent = new Intent (ListaPacientes.this, CadastroPaciente.class);
                intent.putExtra("paciente-enviado" , (Serializable) adapterView.getItemAtPosition(i));
                startActivity(intent);
            }
        });
    }
}
