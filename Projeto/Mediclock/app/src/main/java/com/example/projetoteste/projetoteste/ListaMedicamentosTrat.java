package com.example.projetoteste.projetoteste;

import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
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
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.ui.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mancj.materialsearchbar.MaterialSearchBar;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import dao.MedicamentoLocalDAO;
import model.Medicamento;
import model.MedicamentoLocal;

public class ListaMedicamentosTrat extends AppCompatActivity {

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    DatabaseReference medicamentoRef = databaseReference.child("medicamento");

    ArrayList<Medicamento> arrayList;
    FloatingActionButton btnAdd;
    ArrayAdapter<Medicamento> mAdapter;
    Toolbar toolbar;
    ListView minhalista;
    String usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_medicamentos_trat);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        usuario = firebaseAuth.getCurrentUser().getEmail();


        minhalista = (ListView) findViewById(R.id.listaMedTrat);

        btnAdd = (FloatingActionButton) findViewById(R.id.btnAddMedicamentoLocal);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ListaMedicamentosTrat.this, CadastroMedicamentoLocal.class);
                startActivity(intent);
            }
        });


        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Selecione o medicamento");
        setSupportActionBar(toolbar);

        minhalista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView adapterView, View view, int i, long l) {

                Intent intent = new Intent(ListaMedicamentosTrat.this, CadastroTratamento.class);
                intent.putExtra("med-enviado" , (Serializable) adapterView.getItemAtPosition(i));

                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);

            }
        });


      //  minhalista.setEmptyView(vazio);

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
        arrayList = new ArrayList<Medicamento>();
        MedicamentoLocalDAO medDao = new MedicamentoLocalDAO(ListaMedicamentosTrat.this);
        arrayList = medDao.ListaMedicamentoFiltrado(usuario);
        medDao.close();

        mAdapter = new ArrayAdapter<Medicamento>(ListaMedicamentosTrat.this,
                android.R.layout.simple_list_item_1, arrayList);

        minhalista.setAdapter(mAdapter);


       medicamentoRef.addChildEventListener(new ChildEventListener() {
           @Override
           public void onChildAdded(DataSnapshot dataSnapshot, String s) {
               Medicamento me = dataSnapshot.getValue(Medicamento.class);
               arrayList.add(me);
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
