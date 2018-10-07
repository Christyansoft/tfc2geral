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

import model.Diagnostico;

public class ListaDiagnosticos extends AppCompatActivity {

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    FloatingActionButton addDiag;
    ListView minhaLista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_diagnosticos);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        String usuario = firebaseAuth.getCurrentUser().getEmail();

        Firebase.setAndroidContext(this);
        com.firebase.client.Query firebaseObj = new Firebase("https://projeto-teste-32601.firebaseio.com/diagnostico").orderByChild("usuarioDiagnostico").equalTo(usuario);

        addDiag = findViewById(R.id.btnAddDiagnostico);

        addDiag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ListaDiagnosticos.this, CadastroDiagnostico.class);
                startActivity(intent);

            }
        });

        minhaLista = findViewById(R.id.lvDiagnosticos);

        final FirebaseListAdapter<Diagnostico> adapter = new FirebaseListAdapter<Diagnostico>(this, Diagnostico.class, android.R.layout.simple_list_item_1, firebaseObj) {

            @Override
            protected void populateView(View view, final Diagnostico diagnostico, int i) {

                TextView textView = view.findViewById(android.R.id.text1);
                textView.setText(diagnostico.getNomeDiagnostico());
            }
        };
        minhaLista.setAdapter(adapter);

        minhaLista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent = new Intent (ListaDiagnosticos.this, CadastroDiagnostico.class);
                intent.putExtra("diagnostico-enviado" , (Serializable) adapterView.getItemAtPosition(i));
                startActivity(intent);
            }
        });

    }


}
