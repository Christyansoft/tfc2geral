package com.example.projetoteste.projetoteste;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import model.Laboratorio;
import model.Medicamento;

public class CadastroLaboratorio extends AppCompatActivity {

   private final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
   private final DatabaseReference laboratorioRef = databaseReference.child("laboratorio");
   private final DatabaseReference medicamentoRef = databaseReference.child("medicamento");

   private EditText edtFa;
    private EditText edtCnpj;
   private Button salvar;

    private Laboratorio lab2;
   private ArrayList<Medicamento> arrayMed;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_laboratorio);

        PrencheArray pr = new PrencheArray();

        arrayMed = pr.populaMedicamento();

        edtFa = findViewById(R.id.edtNomeF);
        edtCnpj = findViewById(R.id.edtCnpj);
        salvar = findViewById(R.id.btnSalvar);
        Button apagar = findViewById(R.id.btnApagar);

        apagar.setVisibility(View.INVISIBLE);

        lab2 = (Laboratorio) getIntent().getSerializableExtra("laboratorio-enviado");

        if(lab2!=null){

            salvar.setText("Atualizar");
            salvar.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_refresh_black_24dp, 0, 0, 0);
            apagar.setVisibility(View.VISIBLE);
            edtFa.setText(lab2.getNomeLaboratorio());
            edtCnpj.setText(lab2.getCnpj());
        }



        salvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Laboratorio lab = new Laboratorio();

                if(edtFa.getText().toString().equals("")){
                    edtFa.setError("Informe o nome");
                }
                else{
                    lab.setNomeLaboratorio(edtFa.getText().toString());
                }

                lab.setCnpj(edtCnpj.getText().toString());


                if(validar()){

                    if(salvar.getText().toString().equals("Salvar")) {

                        lab.setIdLaboratorio(laboratorioRef.push().getKey());

                        laboratorioRef.child(lab.getIdLaboratorio()).setValue(lab).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if (task.isSuccessful()) {
                                    Toast.makeText(CadastroLaboratorio.this, "Laboratorio cadastrado", Toast.LENGTH_SHORT).show();
                                    finish();
                                } else {
                                    Toast.makeText(CadastroLaboratorio.this, "Erro ao cadastrar", Toast.LENGTH_SHORT).show();
                                }

                            }
                        });

                    }
                    else{

                        lab.setIdLaboratorio(lab2.getIdLaboratorio());
                        laboratorioRef.child(lab2.getIdLaboratorio()).setValue(lab).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if(task.isSuccessful()){
                                    Toast.makeText(CadastroLaboratorio.this, "Laboratorio atualizado", Toast.LENGTH_SHORT).show();
                                    updateLaboratorio(lab);
                                    finish();
                                }
                                else{
                                    Toast.makeText(CadastroLaboratorio.this, "Erro ao atualizar", Toast.LENGTH_SHORT).show();
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

                boolean result = false;

                if(!arrayMed.isEmpty()){

                    for(Medicamento m: arrayMed){
                        if(m.getIdLaboratorio().equals(lab2.getIdLaboratorio())){
                            result = true;
                            break;
                        }

                    }
                }

                if(result){
                    showDialogo();
                }
                else{
                    confirmaDelete();
                }

            }
        });

    }

    private AlertDialog alerta;

    private void showDialogo() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //define o titulo
        builder.setTitle("Informação de laboratório");
        //define a mensagem

        builder.setMessage("Não é possivel apagar este laboratório, pois o mesmo está vinculado a algum medicamento. Caso deseje" +
                " realmente apagar, delete o medicamento antes");
        //define um botão como positivo

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {

            }
        });

        //cria o AlertDialog
        alerta = builder.create();
        //Exibe
        alerta.show();

    }

    private void confirmaDelete() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //define o titulo
        builder.setTitle("Confirmação");
        //define a mensagem

        builder.setMessage("Confirma a exclusão deste laboratório?");
        //define um botão como positivo

        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {

                laboratorioRef.child(lab2.getIdLaboratorio()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if(task.isSuccessful()){
                                        Toast.makeText(CadastroLaboratorio.this, "Laboratorio apagado", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                    else{
                                        Toast.makeText(CadastroLaboratorio.this, "Erro ao apagar", Toast.LENGTH_SHORT).show();
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
        alerta = builder.create();
        //Exibe
        alerta.show();

    }

    private void updateLaboratorio(final Laboratorio lab2) {

        medicamentoRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                Medicamento med = dataSnapshot.getValue(Medicamento.class);

                if(med.getIdLaboratorio().equals(lab2.getIdLaboratorio())){

                    Map updates = new HashMap<>();

                    updates.put("medicamento/"+med.getIdMedicamento()+"/nomeLaboratorio", lab2.getNomeLaboratorio());
                    databaseReference.updateChildren(updates);

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

    private boolean validar(){
        boolean resultado = false;

        if(edtFa.getText().toString().equals("")){
            resultado = true;
        }

        return !resultado;
    }



}
