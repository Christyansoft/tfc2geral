package com.example.projetoteste.projetoteste;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.io.Serializable;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.ui.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;

import model.Medico;


public class ListaMedicos extends AppCompatActivity {

    FloatingActionButton addMedico;
    ListView minhaLista;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_medicos);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        String usuario = firebaseAuth.getCurrentUser().getEmail();

        Firebase.setAndroidContext(this);
        com.firebase.client.Query firebaseObj = new Firebase("https://projeto-teste-32601.firebaseio.com/medico").orderByChild("usuarioMedico").equalTo(usuario);
        addMedico = findViewById(R.id.btnAddMedico);

        addMedico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ListaMedicos.this, CadastroMedico.class);
                startActivity(intent);
            }
        });


        minhaLista = findViewById(R.id.lvMedicos);

        final FirebaseListAdapter<Medico> adapter = new FirebaseListAdapter<Medico>(this, Medico.class, android.R.layout.simple_list_item_1, firebaseObj) {

            @Override
            protected void populateView(View view, final Medico medico, int i) {

                TextView textView = view.findViewById(android.R.id.text1);
                textView.setText(medico.getNomeMedico());
            }
        };
        minhaLista.setAdapter(adapter);

        minhaLista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent = new Intent (ListaMedicos.this, CadastroMedico.class);
                intent.putExtra("medico-enviado" , (Serializable) adapterView.getItemAtPosition(i));
                startActivity(intent);
            }
        });




    }


}
