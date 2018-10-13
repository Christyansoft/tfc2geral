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

import model.ClasseTerapeutica;
import model.Medicamento;

@SuppressWarnings("unchecked")
public class CadastroClasseTerapeutica extends AppCompatActivity {

    private final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private final DatabaseReference classeRef = databaseReference.child("classeTerapeutica");
    private final DatabaseReference medicamentoRef = databaseReference.child("medicamento");

    private EditText edtNome;
    private Button salvar;

    private ClasseTerapeutica cla2;
    private ArrayList<Medicamento> arrayMed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_classe_terapeutica);

        PrencheArray pr = new PrencheArray();

        arrayMed = pr.populaMedicamento();

        edtNome = findViewById(R.id.edtNomeClass);
        salvar = findViewById(R.id.btnSalvar);
        Button apagar = findViewById(R.id.btnApagar);

        apagar.setVisibility(View.INVISIBLE);

        cla2 = (ClasseTerapeutica) getIntent().getSerializableExtra("classe-enviado");

        if(cla2!=null){
            salvar.setText("Atualizar");
            apagar.setVisibility(View.VISIBLE);
            salvar.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_refresh_black_24dp, 0, 0, 0);
            edtNome.setText(cla2.getNomeClasseTerapeutica());
        }


        salvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final ClasseTerapeutica cla = new ClasseTerapeutica();

                if(edtNome.getText().toString().equals("")){
                    edtNome.setError("Informe a descrição");
                }
                else{
                    cla.setNomeClasseTerapeutica(edtNome.getText().toString());

                    if(salvar.getText().toString().equals("Salvar")) {
                        cla.setIdClasseT(classeRef.push().getKey());

                        classeRef.child(cla.getIdClasseT()).setValue(cla).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if (task.isSuccessful()) {
                                    Toast.makeText(CadastroClasseTerapeutica.this, "Classe terapeutica cadastrada", Toast.LENGTH_SHORT).show();
                                    finish();
                                } else {
                                    Toast.makeText(CadastroClasseTerapeutica.this, "Erro ao cadastrar", Toast.LENGTH_SHORT).show();
                                }

                            }
                        });

                    }else{

                        cla.setIdClasseT(cla2.getIdClasseT());

                        classeRef.child(cla2.getIdClasseT()).setValue(cla).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if(task.isSuccessful()){
                                    Toast.makeText(CadastroClasseTerapeutica.this, "Classe Terapeutica atualizada", Toast.LENGTH_SHORT).show();
                                    updateClasse(cla);
                                    finish();
                                }
                                else{
                                    Toast.makeText(CadastroClasseTerapeutica.this, "Erro ao atualizar", Toast.LENGTH_SHORT).show();
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
                        if(m.getIdClasseT().equals(cla2.getIdClasseT())){
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
        builder.setTitle("Informação de Classe terapeutica");
        //define a mensagem

        builder.setMessage("Não é possivel apagar esta Classe terapeutica, pois a mesma está vinculado a algum medicamento. Caso deseje" +
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

        builder.setMessage("Confirma a exclusão desta Classe terapeutica?");
        //define um botão como positivo

        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {

                classeRef.child(cla2.getIdClasseT()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if(task.isSuccessful()){
                            Toast.makeText(CadastroClasseTerapeutica.this, "Classe terapeutica apagada", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                        else{
                            Toast.makeText(CadastroClasseTerapeutica.this, "Erro ao apagar", Toast.LENGTH_SHORT).show();
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

    private void updateClasse(final ClasseTerapeutica cla2) {

        medicamentoRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                Medicamento med = dataSnapshot.getValue(Medicamento.class);

                if(med.getIdClasseT().equals(cla2.getIdClasseT())){

                    Map updates = new HashMap<>();

                    updates.put("medicamento/"+med.getIdMedicamento()+"/nomeClasseTerapeutica", cla2.getNomeClasseTerapeutica());
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
