package com.example.projetoteste.projetoteste;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ir.mirrajabi.searchdialog.SimpleSearchDialogCompat;
import ir.mirrajabi.searchdialog.core.BaseSearchDialogCompat;
import ir.mirrajabi.searchdialog.core.SearchResultListener;
import model.ClasseTerapeutica;
import model.CodigoBarras;
import model.Laboratorio;
import model.Medicamento;
import model.PrincipioAtivo;
import model.Tratamento;

public class CadastroMedicamento extends AppCompatActivity{

    private final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private final DatabaseReference medicamentoRef = databaseReference.child("medicamento");
    private final DatabaseReference tratRef = databaseReference.child("tratamento");

    private EditText edtDesc;
    private EditText edtLab;
    private EditText edtPrinc;
    private EditText edtClass;
    private EditText edtCod;
    private Button salvar;

    private ArrayList<Laboratorio> arrayLab;
    private ArrayList<PrincipioAtivo> arrayPrinc;
    private ArrayList<ClasseTerapeutica> arrayClass;
    private ArrayList<Tratamento> arrayTrat;
    private final Medicamento med = new Medicamento();
    private Medicamento med2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_medicamento);

        edtDesc = findViewById(R.id.edtDescricao);
        edtLab = findViewById(R.id.edtLaboratorio);
        edtPrinc = findViewById(R.id.edtPrincipioA);
        edtClass = findViewById(R.id.edtClasseT);
        edtCod =  findViewById(R.id.edtCod);
        salvar = findViewById(R.id.btnSalvar);
        Button apagar = findViewById(R.id.btnApagar);
        ImageButton addLab = findViewById(R.id.addLaboratorio);
        ImageButton addPrinc = findViewById(R.id.addPrincipioA);
        ImageButton addClass = findViewById(R.id.addClass);
        ImageButton addCod = findViewById(R.id.addCod);

        edtLab.setEnabled(false);
        edtLab.setTextColor(Color.BLACK);

        apagar.setVisibility(View.INVISIBLE);

        PrencheArray pr = new PrencheArray();

        arrayTrat = pr.preencheTrat();
        arrayLab = pr.populaLab();
        arrayPrinc = pr.populaPrinc();
        arrayClass = pr.populaClass();

        med2 = (Medicamento) getIntent().getSerializableExtra("medicamento-enviado");

        if(med2!=null){

            apagar.setVisibility(View.VISIBLE);
            salvar.setText("Atualizar");
            salvar.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_refresh_black_24dp, 0, 0, 0);
            edtDesc.setText(med2.getNomeMedicamento());
            edtLab.setText(med2.getNomeLaboratorio());
            edtPrinc.setText(med2.getNomePrincipioAtivo());
            edtClass.setText(med2.getNomeClasseTerapeutica());

            if(med2.getBarras1()!=null){
                edtCod.setText(med2.getBarras1());
            }

        }



        addLab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new SimpleSearchDialogCompat(CadastroMedicamento.this, "Pesquise o laboratorio",
                        "", null, arrayLab, new SearchResultListener() {
                    @Override
                    public void onSelected(BaseSearchDialogCompat baseSearchDialogCompat, Object o, int i) {
                        Laboratorio laboratorio = (Laboratorio) o;
                        edtLab.setText(laboratorio.getNomeLaboratorio());
                        med.setNomeLaboratorio(laboratorio.getNomeLaboratorio());
                        med.setIdLaboratorio(laboratorio.getIdLaboratorio());

                        baseSearchDialogCompat.dismiss();
                    }
                }).show();
            }
        });

        addPrinc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new SimpleSearchDialogCompat(CadastroMedicamento.this, "Pesquise o Principio Ativo",
                        "", null, arrayPrinc, new SearchResultListener() {
                    @Override
                    public void onSelected(BaseSearchDialogCompat baseSearchDialogCompat, Object o, int i) {
                        PrincipioAtivo pr = (PrincipioAtivo) o;
                        edtPrinc.setText(pr.getNomePrincipioAtivo());
                        med.setNomePrincipioAtivo(pr.getNomePrincipioAtivo());
                        med.setIdPrincipioA(pr.getIdPrincipioA());

                        baseSearchDialogCompat.dismiss();
                    }
                }).show();
            }
        });

        addClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new SimpleSearchDialogCompat(CadastroMedicamento.this, "Pesquise a Classe Terapêutica",
                        "", null, arrayClass, new SearchResultListener() {
                    @Override
                    public void onSelected(BaseSearchDialogCompat baseSearchDialogCompat, Object o, int i) {
                        ClasseTerapeutica cl = (ClasseTerapeutica) o;
                        edtClass.setText(cl.getNomeClasseTerapeutica());
                        med.setNomeClasseTerapeutica(cl.getNomeClasseTerapeutica());
                        med.setIdClasseT(cl.getIdClasseT());

                        baseSearchDialogCompat.dismiss();
                    }
                }).show();
            }
        });

        addCod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CadastroMedicamento.this, CodigoBarras.class);
                intent.putExtra("cadastro", "cadastro");
                startActivity(intent);
            }
        });

        salvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                validaCampos();

                if(validaCampos2()) {

                    if (salvar.getText().toString().equals("Salvar")){

                        med.setBarras1(edtCod.getText().toString());
                        med.setIdMedicamento(medicamentoRef.push().getKey());

                        medicamentoRef.child(med.getIdMedicamento()).setValue(med).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if (task.isSuccessful()) {
                                    Toast.makeText(CadastroMedicamento.this, "Medicamento cadastrado", Toast.LENGTH_SHORT).show();
                                    finish();
                                } else {
                                    Toast.makeText(CadastroMedicamento.this, "Erro ao cadastrar", Toast.LENGTH_SHORT).show();
                                }

                            }
                        });

                    }
                    else{

                        final Medicamento med3 = new Medicamento();

                        med3.setNomeMedicamento(med.getNomeMedicamento());

                        //verifica se o usuario modificou o laboratorio
                        if(edtLab.getText().toString().equals(med2.getNomeLaboratorio())){
                            med3.setNomeLaboratorio(med2.getNomeLaboratorio());
                            med3.setIdLaboratorio(med2.getIdLaboratorio());
                        }
                        else{
                            med3.setNomeLaboratorio(med.getNomeLaboratorio());
                            med3.setIdLaboratorio(med.getIdLaboratorio());
                        }

                        //verifica se o usuario modificou o principio ativo
                        if(edtPrinc.getText().toString().equals(med2.getNomePrincipioAtivo())){
                            med3.setNomePrincipioAtivo(med2.getNomePrincipioAtivo());
                            med3.setIdPrincipioA(med2.getIdPrincipioA());
                        }
                        else{
                            med3.setNomePrincipioAtivo(med.getNomePrincipioAtivo());
                            med3.setIdPrincipioA(med.getIdPrincipioA());
                        }

                        //verifica se o usuario modificou a classe terapeutica
                        if(edtClass.getText().toString().equals(med2.getNomeClasseTerapeutica())){
                            med3.setNomeClasseTerapeutica(med2.getNomeClasseTerapeutica());
                            med3.setIdClasseT(med2.getIdClasseT());
                        }
                        else{
                            med3.setNomeClasseTerapeutica(med.getNomeClasseTerapeutica());
                            med3.setIdClasseT(med.getIdClasseT());
                        }

                        med3.setBarras1(edtCod.getText().toString());
                        med3.setIdMedicamento(med2.getIdMedicamento());

                        medicamentoRef.child(med2.getIdMedicamento()).setValue(med3).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if(task.isSuccessful()){
                                    Toast.makeText(CadastroMedicamento.this, "Medicamento atualizado", Toast.LENGTH_SHORT).show();
                                    updateMedicamento(med3);
                                    finish();

                                }
                                else{
                                    Toast.makeText(CadastroMedicamento.this, "Erro ao atualizar", Toast.LENGTH_SHORT).show();
                                }

                            }
                        });

                    }
                }
            }
        });

        apagar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean resultado = false;

                if(!arrayTrat.isEmpty()){

                    for(int j=0; j<arrayTrat.size();j++){
                        for(int k=0; k<arrayTrat.get(j).getArrayMedicamento().size();k++) {
                            if (arrayTrat.get(j).getArrayMedicamento().get(k).getIdMedicamento().equals(med2.getIdMedicamento())) {
                                resultado = true;
                                break;
                            }
                        }
                    }

                }

                if(resultado){
                    showDialogo();
                }
                else{
                    confirmaDelete();
                }

            }
        });


    }


    private void showDialogo(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //define o titulo
        builder.setTitle("Informação de medicamento");
        //define a mensagem

        builder.setMessage("Não é possivel apagar este medicamento, pois o mesmo está vinculado a algum tratamento. Caso deseje" +
                " realmente apagar, delete o tratamento antes.");
        //define um botão como positivo

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {

            }
        });

        //cria o AlertDialog
        builder.create().show();
        //Exibe

    }

    private void confirmaDelete() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //define o titulo
        builder.setTitle("Confirmação");
        //define a mensagem

        builder.setMessage("Confirma a exclusão deste medicamento?");
        //define um botão como positivo

        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {

                medicamentoRef.child(med2.getIdMedicamento()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if(task.isSuccessful()){
                            Toast.makeText(CadastroMedicamento.this, "medicamento apagado", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                        else{
                            Toast.makeText(CadastroMedicamento.this, "Erro ao apagar", Toast.LENGTH_SHORT).show();
                        }

                    }
                });

            }
        });

        builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        //cria o AlertDialog
        builder.create().show();

    }

    private void validaCampos(){
        if(edtDesc.getText().toString().equals("")){
            edtDesc.setError("Informe a descrição");
        }
        else{
            med.setNomeMedicamento(edtDesc.getText().toString());
        }

        if(edtLab.getText().toString().equals("")){
            edtLab.setError("");

        }

        if(edtPrinc.getText().toString().equals("")){
            edtPrinc.setError("");

        }

        if(edtClass.getText().toString().equals("")){
            edtClass.setError("");

        }
    }

    private boolean validaCampos2(){

        boolean retorno = false;

        if(edtDesc.getText().toString().equals("") || edtLab.getText().toString().equals("")
                || edtPrinc.getText().toString().equals("") || edtClass.getText().toString().equals("")){
            retorno = true;
        }
        return !retorno;
    }

    private void updateMedicamento(final Medicamento med){

        tratRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Tratamento t = dataSnapshot.getValue(Tratamento.class);

                for(int j=0; j<t.getArrayMedicamento().size();j++){

                    if(t.getArrayMedicamento().get(j).getIdMedicamento().equals(med.getIdMedicamento())){

                        Map updates = new HashMap<>();

                        updates.put("tratamento/"+t.getIdTratamento()+"/arrayMedicamento/"+j+"/nomeMedicamento", med.getNomeMedicamento());
                        databaseReference.updateChildren(updates);
                    }
                }

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
    protected void onNewIntent(Intent intent){
        edtCod.setText(intent.getStringExtra("resultadoCodigo"));
    }


}
