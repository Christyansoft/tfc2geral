package com.example.projetoteste.projetoteste;

import android.app.ProgressDialog;
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

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;
import java.util.ArrayList;

import model.Medicamento;

public class ListaMedicamentos extends AppCompatActivity {

    private ProgressDialog progress;

    private ArrayList<Medicamento> arrayMed;
    private ArrayAdapter<Medicamento> mAdapter;

    private final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private final DatabaseReference medicamentoRef = databaseReference.child("medicamento");

    private ListView minhalista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_medicamentos);

        progress = new ProgressDialog(this);
        progress.setMessage("Carregando dados");
        progress.setCanceledOnTouchOutside(false);
        progress.show();

        minhalista = findViewById(R.id.lvMedicamento);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Medicamento");
        setSupportActionBar(toolbar);

        FloatingActionButton btnAddMed = findViewById(R.id.btnAddMedicamento);

        btnAddMed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ListaMedicamentos.this, CadastroMedicamento.class);
                startActivity(intent);
            }
        });


        minhalista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(ListaMedicamentos.this, CadastroMedicamento.class);
                intent.putExtra("medicamento-enviado", (Serializable) adapterView.getItemAtPosition(i));
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

        arrayMed = new ArrayList<>();
        mAdapter = new ArrayAdapter<>(ListaMedicamentos.this,
                android.R.layout.simple_list_item_1, arrayMed);
        minhalista.setAdapter(mAdapter);

                medicamentoRef.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        Medicamento me = dataSnapshot.getValue(Medicamento.class);
                        arrayMed.add(me);
                        mAdapter.notifyDataSetChanged();
                        progress.dismiss();
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
