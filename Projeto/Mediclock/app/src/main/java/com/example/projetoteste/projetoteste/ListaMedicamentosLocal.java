package com.example.projetoteste.projetoteste;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.Serializable;
import java.util.ArrayList;

import dao.MedicamentoLocalDAO;
import model.Medicamento;
import model.MedicamentoLocal;

public class ListaMedicamentosLocal extends AppCompatActivity {

    FloatingActionButton btnAddMed;
    ListView listaMed;

    MedicamentoLocalDAO medDao;
    Context context;
    Long retornoDB;

    ArrayList<Medicamento> arrayMed;
    ArrayAdapter<Medicamento> adapterMed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_medicamentos_local);

        this.context = this;

        listaMed = (ListView) findViewById(R.id.lvMedicamentoLocal);

        btnAddMed = (FloatingActionButton) findViewById(R.id.btnAddMedicamentoLocal);

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

    public ArrayList<Medicamento> preencheLista(){

        medDao = new MedicamentoLocalDAO(ListaMedicamentosLocal.this);
        arrayMed = medDao.selectAllMed();
        medDao.close();

        if(listaMed != null){
            adapterMed = new ArrayAdapter<Medicamento>(ListaMedicamentosLocal.this,
                    android.R.layout.simple_list_item_1, arrayMed);
            listaMed.setAdapter(adapterMed);

        }
        return arrayMed;
    }

    @Override
    protected void onResume(){
        super.onResume();
        preencheLista();

    }

}
