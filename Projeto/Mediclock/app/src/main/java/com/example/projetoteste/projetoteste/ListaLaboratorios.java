package com.example.projetoteste.projetoteste;

import android.content.Context;
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
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.ui.FirebaseListAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;
import java.util.ArrayList;

import model.Laboratorio;
import model.Medicamento;

public class ListaLaboratorios extends AppCompatActivity {

     DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
     DatabaseReference laboratorioRef = databaseReference.child("laboratorio");

     FloatingActionButton btnAdd;
     Context context;
     ArrayList<Laboratorio> arrayLab;
     ArrayAdapter<Laboratorio> mAdapter;
     Toolbar toolbar;
     ListView minhalista;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_laboratorios);

        this.context = this;

        minhalista = findViewById(R.id.lvLaboratorio);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Laboratorio");
        setSupportActionBar(toolbar);

        btnAdd = findViewById(R.id.btnAddLaboratorio);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ListaLaboratorios.this, CadastroLaboratorio.class);
                startActivity(intent);
            }
        });

        minhalista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(ListaLaboratorios.this, CadastroLaboratorio.class);
                intent.putExtra("laboratorio-enviado", (Serializable) adapterView.getItemAtPosition(i));
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

    public void preencheLista(){
        arrayLab = new ArrayList<Laboratorio>();
        mAdapter = new ArrayAdapter<Laboratorio>(ListaLaboratorios.this,
                android.R.layout.simple_list_item_1, arrayLab);
        minhalista.setAdapter(mAdapter);

        laboratorioRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Laboratorio lab = dataSnapshot.getValue(Laboratorio.class);
                arrayLab.add(lab);
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
