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
import model.Tratamento;

public class ListaTratamento extends AppCompatActivity {

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    FloatingActionButton addTratamento;
    ListView minhaLista;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_tratamento);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        String usuario = firebaseAuth.getCurrentUser().getEmail();

        Firebase.setAndroidContext(this);
        com.firebase.client.Query firebaseObj = new Firebase("https://projeto-teste-32601.firebaseio.com/tratamento").orderByChild("usuarioTratamento").equalTo(usuario);

        addTratamento = findViewById(R.id.btnAddTratamento);

        addTratamento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(ListaTratamento.this, CadastroTratamento.class);
                startActivity(intent);
            }
        });

        minhaLista = findViewById(R.id.lvTratamento);

        final FirebaseListAdapter<Tratamento> adapter = new FirebaseListAdapter<Tratamento>(this, Tratamento.class, android.R.layout.simple_list_item_1, firebaseObj) {

            @Override
            protected void populateView(View view, final Tratamento tratamento, int i) {

                TextView textView = view.findViewById(android.R.id.text1);
                textView.setText(tratamento.getDiagnostico().getNomeDiagnostico());
            }
        };
        minhaLista.setAdapter(adapter);

        minhaLista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent = new Intent (ListaTratamento.this, CadastroTratamento.class);
                intent.putExtra("tratamento-enviado" , (Serializable) adapterView.getItemAtPosition(i));
                startActivity(intent);
            }
        });


    }
}
