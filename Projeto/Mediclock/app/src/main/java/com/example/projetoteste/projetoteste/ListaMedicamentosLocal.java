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

import java.io.Serializable;
import java.util.ArrayList;

import dao.MedicamentoLocalDAO;
import model.Medicamento;

public class ListaMedicamentosLocal extends AppCompatActivity {

    private ListView listaMed;

    private String usuario;

    private ArrayAdapter<Medicamento> adapterMed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_medicamentos_local);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        usuario = firebaseAuth.getCurrentUser().getEmail();

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Medicamento");
        setSupportActionBar(toolbar);

        listaMed = findViewById(R.id.lvMedicamentoLocal);

        FloatingActionButton btnAddMed = findViewById(R.id.btnAddMedicamentoLocal);

        btnAddMed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(ListaMedicamentosLocal.this, CadastroMedicamentoLocal.class);
                startActivity(intent);

            }
        });

        listaMed.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent = new Intent(ListaMedicamentosLocal.this, CadastroMedicamentoLocal.class);
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
                adapterMed.getFilter().filter(newText);
                return true;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    private void preencheLista(){

        MedicamentoLocalDAO medDao = new MedicamentoLocalDAO(ListaMedicamentosLocal.this);
        ArrayList<Medicamento> arrayMed = medDao.ListaMedicamentoFiltrado(usuario);
        medDao.close();

        if(listaMed != null){
            adapterMed = new ArrayAdapter<>(ListaMedicamentosLocal.this,
                    android.R.layout.simple_list_item_1, arrayMed);
            listaMed.setAdapter(adapterMed);

        }
    }

    @Override
    protected void onResume(){
        super.onResume();
        preencheLista();

    }

}
