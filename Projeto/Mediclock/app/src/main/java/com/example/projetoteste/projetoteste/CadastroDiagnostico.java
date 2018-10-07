package com.example.projetoteste.projetoteste;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import model.Diagnostico;
import model.Tratamento;


public class CadastroDiagnostico extends AppCompatActivity {

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    DatabaseReference diagnosticoRef = databaseReference.child("diagnostico");
    DatabaseReference tratRef = databaseReference.child("tratamento");

    EditText edtDesc, edtCodCid;
    Diagnostico diagnostico2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        final String usuario = firebaseAuth.getCurrentUser().getEmail();

        setContentView(R.layout.activity_cadastro_diagnostico);

        final Button salvar = findViewById(R.id.btnSalvar);
        Button apagar = findViewById(R.id.btnApagar);

        apagar.setVisibility(View.INVISIBLE);
        edtDesc = findViewById(R.id.edtDescricao);
        edtCodCid = findViewById(R.id.edtCodCid);

        Intent i = getIntent();
        diagnostico2 = (Diagnostico) i.getSerializableExtra("diagnostico-enviado");

        if(diagnostico2!=null){

            salvar.setText("Atualizar");
            apagar.setVisibility(View.VISIBLE);
            salvar.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_refresh_black_24dp,0,0,0);
            edtDesc.setText(diagnostico2.getNomeDiagnostico());
            edtCodCid.setText(diagnostico2.getCodigoCid());

        }

        final Diagnostico diag = new Diagnostico();

        salvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(edtDesc.getText().toString().equals("")){
                    edtDesc.setError("Informe a descricao");
                }
                else{
                    diag.setNomeDiagnostico(edtDesc.getText().toString());
                    diag.setCodigoCid(edtCodCid.getText().toString());
                    diag.setUsuarioDiagnostico(usuario);

                    if(salvar.getText().toString().equals("Salvar")) {

                        diag.setIdDiagnostico(diagnosticoRef.push().getKey());

                        diagnosticoRef.child(diag.getIdDiagnostico()).setValue(diag).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(CadastroDiagnostico.this, "Diagnostico cadastrado", Toast.LENGTH_SHORT).show();
                                    limpar();
                                } else {
                                    Toast.makeText(CadastroDiagnostico.this, "Erro ao cadastrar diagnostico", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                    else{
                         diag.setIdDiagnostico(diagnostico2.getIdDiagnostico());

                         diagnosticoRef.child(diagnostico2.getIdDiagnostico()).setValue(diag).addOnCompleteListener(new OnCompleteListener<Void>() {

                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    updateDiagnostico(diag);
                                    Toast.makeText(CadastroDiagnostico.this, "Diagnostico atualizado", Toast.LENGTH_SHORT).show();
                                    limpar();
                                    finish();
                                } else {
                                    Toast.makeText(CadastroDiagnostico.this, "Erro ao atualizar diagnostico", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }

                }


            }
        });

    }

    private void updateDiagnostico(final Diagnostico diag) {

       tratRef.addChildEventListener(new ChildEventListener() {
           @Override
           public void onChildAdded(DataSnapshot dataSnapshot, String s) {

               Tratamento t = dataSnapshot.getValue(Tratamento.class);

               if(t.getDiagnostico()!=null) {

                   if (t.getDiagnostico().getIdDiagnostico().equals(diag.getIdDiagnostico())) {

                       Map updates = new HashMap<>();

                       updates.put("tratamento/" + t.getIdTratamento() + "/diagnostico/nomeDiagnostico", diag.getNomeDiagnostico());
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

    private void limpar() {
        edtDesc.setText("");
        edtCodCid.setText("");
    }


}
