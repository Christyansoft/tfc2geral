package com.example.projetoteste.projetoteste;

import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.io.Serializable;
import java.util.ArrayList;

import model.Diagnostico;

public class ListaDiagnosticos extends AppCompatActivity {

    private final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private ListView minhaLista;
    private String usuario;
    private ArrayList<Diagnostico> arrayDiag;
    private ArrayAdapter<Diagnostico> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_diagnosticos);

        minhaLista = findViewById(R.id.lvDiagnosticos);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Diagnostico");
        setSupportActionBar(toolbar);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        usuario = firebaseAuth.getCurrentUser().getEmail();

        FloatingActionButton addDiag = findViewById(R.id.btnAddDiagnostico);

        addDiag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ListaDiagnosticos.this, CadastroDiagnostico.class);
                startActivity(intent);

            }
        });


        minhaLista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent = new Intent (ListaDiagnosticos.this, CadastroDiagnostico.class);
                intent.putExtra("diagnostico-enviado" , (Serializable) adapterView.getItemAtPosition(i));
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);

        MenuItem mSearch = menu.findItem(R.id.action);

        SearchView mSearchView = (SearchView) mSearch.getActionView();
        mSearchView.setQueryHint("Pesquise");
        mSearchView.setBackgroundColor(Color.WHITE);

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mAdapter.getFilter().filter(newText);
                return true;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }


    private void preencheLista(){

        Query pacienteRef = databaseReference.child("diagnostico").orderByChild("usuarioDiagnostico").equalTo(usuario);

        arrayDiag = new ArrayList<>();
        mAdapter = new ArrayAdapter<>(ListaDiagnosticos.this,
                android.R.layout.simple_list_item_1, arrayDiag);
        minhaLista.setAdapter(mAdapter);

        pacienteRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Diagnostico di = dataSnapshot.getValue(Diagnostico.class);
                arrayDiag.add(di);
                mAdapter.notifyDataSetChanged();
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

    @Override
    protected void onResume(){
        super.onResume();
        preencheLista();

    }



}
