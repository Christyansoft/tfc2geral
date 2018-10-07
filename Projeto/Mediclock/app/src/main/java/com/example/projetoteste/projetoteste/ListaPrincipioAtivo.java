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

import com.firebase.client.Firebase;
import com.firebase.client.Query;
import com.firebase.ui.FirebaseListAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;
import java.util.ArrayList;

import model.Laboratorio;
import model.PrincipioAtivo;

public class ListaPrincipioAtivo extends AppCompatActivity {

    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference principioRef = databaseReference.child("principioAtivo");

    private ListView minhalista;
    private ArrayList<PrincipioAtivo> arrayPrinc;
    private ArrayAdapter mAdapter;

    FloatingActionButton btnAdd;
    Context context;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_principio_ativo);

        this.context = this;

        minhalista = findViewById(R.id.lvPrincipio);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Principio Ativo");
        setSupportActionBar(toolbar);


        btnAdd = findViewById(R.id.btnAddPrincipio);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ListaPrincipioAtivo.this, CadastroPrincipioAtivo.class);
                startActivity(intent);
            }
        });

        minhalista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(ListaPrincipioAtivo.this, CadastroPrincipioAtivo.class);
                intent.putExtra("principio-enviado", (Serializable) adapterView.getItemAtPosition(i));
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
        arrayPrinc = new ArrayList<PrincipioAtivo>();
        mAdapter = new ArrayAdapter<PrincipioAtivo>(ListaPrincipioAtivo.this,
                android.R.layout.simple_list_item_1, arrayPrinc);
        minhalista.setAdapter(mAdapter);

        principioRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                PrincipioAtivo pa = dataSnapshot.getValue(PrincipioAtivo.class);
                arrayPrinc.add(pa);
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
