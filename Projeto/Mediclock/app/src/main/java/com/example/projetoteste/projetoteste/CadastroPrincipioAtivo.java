package com.example.projetoteste.projetoteste;

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

import model.Medicamento;
import model.PrincipioAtivo;

public class CadastroPrincipioAtivo extends AppCompatActivity {

    private final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private final DatabaseReference principioRef = databaseReference.child("principioAtivo");
    private final DatabaseReference medicamentoRef = databaseReference.child("medicamento");

    private EditText edtNome;
    private Button salvar;

    private PrincipioAtivo pri2;
    private ArrayList<Medicamento> arrayMed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_principio_ativo);

        PrencheArray pr = new PrencheArray();

        arrayMed = pr.populaMedicamento();

        edtNome = findViewById(R.id.edtNomePrinc);
        salvar = findViewById(R.id.btnSalvar);
        Button apagar = findViewById(R.id.btnApagar);

        apagar.setVisibility(View.INVISIBLE);

        pri2 = (PrincipioAtivo) getIntent().getSerializableExtra("principio-enviado");

        if(pri2!=null){
            salvar.setText("Atualizar");
            apagar.setVisibility(View.VISIBLE);
            salvar.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_refresh_black_24dp, 0, 0, 0);
            edtNome.setText(pri2.getNomePrincipioAtivo());
        }


        salvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final PrincipioAtivo pri = new PrincipioAtivo();

                if(edtNome.getText().toString().equals("")){
                    edtNome.setError("Informe a descrição");
                }
                else{
                    pri.setNomePrincipioAtivo(edtNome.getText().toString());

                    if(salvar.getText().toString().equals("Salvar")) {
                        pri.setIdPrincipioA(principioRef.push().getKey());

                        principioRef.child(pri.getIdPrincipioA()).setValue(pri).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if (task.isSuccessful()) {
                                    Toast.makeText(CadastroPrincipioAtivo.this, "Principio ativo cadastrado", Toast.LENGTH_SHORT).show();
                                    finish();
                                } else {
                                    Toast.makeText(CadastroPrincipioAtivo.this, "Erro ao cadastrar", Toast.LENGTH_SHORT).show();
                                }

                            }
                        });

                    }else{

                        pri.setIdPrincipioA(pri2.getIdPrincipioA());
                        principioRef.child(pri2.getIdPrincipioA()).setValue(pri).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if(task.isSuccessful()){
                                    Toast.makeText(CadastroPrincipioAtivo.this, "Principio ativo atualizado", Toast.LENGTH_SHORT).show();
                                    updatePrincipio(pri);
                                    finish();
                                }
                                else{
                                    Toast.makeText(CadastroPrincipioAtivo.this, "Erro ao atualizar", Toast.LENGTH_SHORT).show();
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
                        if(m.getIdPrincipioA().equals(pri2.getIdPrincipioA())){
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
        builder.setTitle("Informação de Principio ativo");
        //define a mensagem

        builder.setMessage("Não é possivel apagar este Principio ativo, pois o mesmo está vinculado a algum medicamento. Caso deseje" +
                " realmente apagar, delete o medicamento antes.");
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

        builder.setMessage("Confirma a exclusão deste Principio ativo?");
        //define um botão como positivo

        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {

                principioRef.child(pri2.getIdPrincipioA()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if(task.isSuccessful()){
                            Toast.makeText(CadastroPrincipioAtivo.this, "Principio Ativo apagado", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                        else{
                            Toast.makeText(CadastroPrincipioAtivo.this, "Erro ao apagar", Toast.LENGTH_SHORT).show();
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

    private void updatePrincipio(final PrincipioAtivo pri2) {

        medicamentoRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                Medicamento med = dataSnapshot.getValue(Medicamento.class);

                if(med.getIdPrincipioA().equals(pri2.getIdPrincipioA())){

                    Map updates = new HashMap<>();

                    updates.put("medicamento/"+med.getIdMedicamento()+"/nomePrincipioAtivo", pri2.getNomePrincipioAtivo());
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
}
