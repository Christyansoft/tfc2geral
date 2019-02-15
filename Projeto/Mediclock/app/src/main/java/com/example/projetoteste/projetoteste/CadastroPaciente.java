package com.example.projetoteste.projetoteste;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.Paciente;
import model.Tratamento;

public class CadastroPaciente extends AppCompatActivity {

    private final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    private final DatabaseReference pacienteRef = databaseReference.child("paciente");
    private final DatabaseReference tratRef = databaseReference.child("tratamento");

    private Spinner spSexo;
    private List<String> list;
    private EditText edtNome;
    private EditText edtidade;
    private EditText edtObser;
    private Paciente paciente2;
    private ArrayList<Tratamento> arrayTrat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_paciente);

        preencherS();
        PrencheArray pr = new PrencheArray();
        arrayTrat = pr.preencheTrat();

        final Button salvar = findViewById(R.id.btnSalvar);
        Button apagar = findViewById(R.id.btnApagar);

        apagar.setVisibility(View.INVISIBLE);
        edtNome = findViewById(R.id.edtNome);
        edtidade = findViewById(R.id.edtIdade);
        edtObser = findViewById(R.id.edtObser);

        Intent i = getIntent();
        paciente2 = (Paciente) i.getSerializableExtra("paciente-enviado");

        String sexo;
        if(paciente2!=null){

            if(paciente2.getSexo().equals("M")){
                sexo = "Masculino";
            }
            else{
                sexo = "Feminino";
            }

            int posicao = 0;

            for(int j = 0; j<list.size();j++){
                if(list.get(j).equals(sexo)){
                    posicao = j;
                }
            }

            salvar.setText("Atualizar");
            apagar.setVisibility(View.VISIBLE);

            salvar.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_refresh_black_24dp,0,0,0);
            edtNome.setText(paciente2.getNomePaciente());
            edtidade.setText(paciente2.getIdade());
            spSexo.setSelection(posicao);
            edtObser.setText(paciente2.getObservacao());

        }


        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        final String usuario = firebaseAuth.getCurrentUser().getEmail();



        salvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Paciente pac = new Paciente();
                pac.setUsuarioPaciente(usuario);

                if(edtNome.getText().toString().equals("")){
                    edtNome.setError("Informe o nome");
                }
                else{
                    pac.setNomePaciente(edtNome.getText().toString());

                }

                if(edtidade.getText().toString().equals("")){
                    edtidade.setError("Informe a idade");
                }
                else{
                    pac.setIdade(edtidade.getText().toString());
                }

                if(spSexo.getSelectedItem().equals("Sexo")){
                    ((TextView)spSexo.getSelectedView()).setError("Informe o sexo");
                }
                else{
                    if(spSexo.getSelectedItem().equals("Masculino")){
                        pac.setSexo("M");
                    }else{
                        pac.setSexo("F");
                    }
                }

                pac.setObservacao(edtObser.getText().toString());

                if(validar()) {


                    if(salvar.getText().toString().equals("Salvar")) {

                        pac.setIdPaciente(pacienteRef.push().getKey());

                        pacienteRef.child(pac.getIdPaciente()).setValue(pac);
                        Toast.makeText(CadastroPaciente.this, "cadastrado", Toast.LENGTH_SHORT).show();
                        finish();


//                        pacienteRef.child(pac.getIdPaciente()).setValue(pac).addOnCompleteListener(new OnCompleteListener<Void>() {
//                            @Override
//                            public void onComplete(@NonNull Task<Void> task) {
//                                if (task.isSuccessful()) {
//                                    Toast.makeText(CadastroPaciente.this, "Paciente cadastrado", Toast.LENGTH_SHORT).show();
//                                    finish();
//                                } else {
//                                    Toast.makeText(CadastroPaciente.this, "Erro ao cadastrar paciente", Toast.LENGTH_SHORT).show();
//                                }
//                            }
//                        });
                    }
                    else{
                        pac.setIdPaciente(paciente2.getIdPaciente());

                        pacienteRef.child(paciente2.getIdPaciente()).setValue(pac).addOnCompleteListener(new OnCompleteListener<Void>() {

                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    updatePaciente(pac);
                                    Toast.makeText(CadastroPaciente.this, "Paciente atualizado", Toast.LENGTH_SHORT).show();
                                    finish();
                                } else {
                                    Toast.makeText(CadastroPaciente.this, "Erro ao atualizar paciente", Toast.LENGTH_SHORT).show();
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

                if(!arrayTrat.isEmpty()) {
                    for (Tratamento t : arrayTrat) {

                        if(t.getPaciente().getIdPaciente().equals(paciente2.getIdPaciente())){
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

    private void showDialogo(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //define o titulo
        builder.setTitle("Informação de paciente");
        //define a mensagem

        builder.setMessage("Não é possivel apagar este paciente, pois o mesmo está vinculado a algum tratamento. Caso deseje" +
                " realmente apagar, delete o tratamento antes");
        //define um botão como positivo

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {

            }
        });

        builder.create().show();

    }

    private void confirmaDelete(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //define o titulo
        builder.setTitle("Confirmação");
        //define a mensagem

        builder.setMessage("Confirma a exclusão deste paciente?");
        //define um botão como positivo

        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {

                pacienteRef.child(paciente2.getIdPaciente()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(CadastroPaciente.this, "Paciente apagado", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(CadastroPaciente.this, "Erro ao apagar paciente", Toast.LENGTH_SHORT).show();
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

        builder.create().show();

    }

    private void updatePaciente(final Paciente pac){
        tratRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Tratamento t = dataSnapshot.getValue(Tratamento.class);

                if(t.getPaciente().getIdPaciente().equals(paciente2.getIdPaciente())){

                    Map updates = new HashMap<>();

                    updates.put("tratamento/"+t.getIdTratamento()+"/paciente/nomePaciente", pac.getNomePaciente());
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
        return !edtNome.getText().toString().equals("") && (!edtidade.getText().toString().equals(""))
                && (!spSexo.getSelectedItem().equals("Sexo"));
    }

    private void preencherS(){

        spSexo = findViewById(R.id.spSexo);
        list = new ArrayList<>();
        list.add("Sexo");
        list.add("Masculino");
        list.add("Feminino");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spSexo.setAdapter(dataAdapter);
    }

}
