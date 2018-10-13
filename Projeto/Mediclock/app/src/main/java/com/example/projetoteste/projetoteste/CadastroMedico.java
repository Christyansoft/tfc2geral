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

import model.Medico;
import model.Tratamento;

public class CadastroMedico extends AppCompatActivity {

    private final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private final DatabaseReference medicoRef = databaseReference.child("medico");
    private final DatabaseReference tratRef = databaseReference.child("tratamento");

    private Spinner spRegistro;
    private Spinner spUf;
    private List<String> listRegistro;
    private List<String> listUf;
    private EditText edtNome;
    private EditText edtRegistro;
    private String uf;
    private String tipoRegistro;
    private Medico medico2;
    private ArrayList<Tratamento> arrayTrat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_medico);

        PrencheArray pr = new PrencheArray();

        arrayTrat = pr.preencheTrat();
        preencherRegistro();
        preencherUf();

        final Button salvar = findViewById(R.id.btnSalvar);
        Button apagar = findViewById(R.id.btnApagar);

        apagar.setVisibility(View.INVISIBLE);
        edtNome = findViewById(R.id.edtNome);
        edtRegistro = findViewById(R.id.edtRegistro);

        Intent i = getIntent();
        medico2 = (Medico) i.getSerializableExtra("medico-enviado");

        if(medico2!=null){

            int posicao1 = 0;
            int posicao2 = 0;

            uf = medico2.getUf();
            tipoRegistro = medico2.getTipoRegistro();


            for(int j = 0; j<listRegistro.size();j++){
                if(listRegistro.get(j).equals(tipoRegistro)){
                    posicao1 = j;
                }
            }

            for(int k = 0; k<listUf.size();k++){
                if(listUf.get(k).equals(uf)){
                    posicao2 = k;
                }
            }

            salvar.setText("Atualizar");
            apagar.setVisibility(View.VISIBLE);

            salvar.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_refresh_black_24dp,0,0,0);
            edtNome.setText(medico2.getNomeMedico());
            edtRegistro.setText(medico2.getNumRegistro());
            spRegistro.setSelection(posicao1);
            spUf.setSelection(posicao2);

        }

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        final String emailUsuario = firebaseAuth.getCurrentUser().getEmail();

        final Medico medico = new Medico();

        salvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (edtNome.getText().toString().equals("")) {
                    edtNome.setError("Informe o nome");
                } else {
                    medico.setNomeMedico(edtNome.getText().toString());
                }

                if (spUf.getSelectedItem().equals("UF")) {
                    ((TextView) spUf.getSelectedView()).setError("UF obrigatorio");
                } else {
                    medico.setUf(spUf.getSelectedItem().toString());
                }

                medico.setTipoRegistro(spRegistro.getSelectedItem().toString());
                medico.setNumRegistro(edtRegistro.getText().toString());
                medico.setUsuarioMedico(emailUsuario);

                if (validar()) {

                    if (salvar.getText().toString().equals("Salvar")) {

                        medico.setIdMedico(medicoRef.push().getKey());

                        medicoRef.child(medico.getIdMedico()).setValue(medico).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if (task.isSuccessful()) {
                                    Toast.makeText(CadastroMedico.this, "Medico cadastrado", Toast.LENGTH_SHORT).show();
                                    finish();
                                } else {
                                    Toast.makeText(CadastroMedico.this, "Erro ao cadastrar medico", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                    else {

                        medico.setIdMedico(medico2.getIdMedico());

                        medicoRef.child(medico2.getIdMedico()).setValue(medico).addOnCompleteListener(new OnCompleteListener<Void>() {

                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    updateMedico(medico);
                                    Toast.makeText(CadastroMedico.this, "Medico atualizado", Toast.LENGTH_SHORT).show();
                                    finish();
                                } else {
                                    Toast.makeText(CadastroMedico.this, "Erro ao atualizar medico", Toast.LENGTH_SHORT).show();
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
                        if(t.getMedico()!=null){
                            if(t.getMedico().getIdMedico().equals(medico2.getIdMedico())){
                                result = true;
                                break;
                            }
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

    private void showDialogo(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //define o titulo
        builder.setTitle("Informação de médico");
        //define a mensagem

        builder.setMessage("Não é possivel apagar este médico, pois o mesmo está vinculado a algum tratamento. Caso deseje" +
                " realmente apagar, delete o tratamento antes");
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

    private void confirmaDelete(){

    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    //define o titulo
        builder.setTitle("Confirmação");
    //define a mensagem

        builder.setMessage("Confirma a exclusão deste médico?");
    //define um botão como positivo

        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface arg0, int arg1) {

            medicoRef.child(medico2.getIdMedico()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(CadastroMedico.this, "Medico apagado", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(CadastroMedico.this, "Erro ao apagar medico", Toast.LENGTH_SHORT).show();
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



    private void updateMedico(final Medico medico) {

        tratRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                Tratamento t = dataSnapshot.getValue(Tratamento.class);

                if(t.getMedico()!=null) {

                    if (t.getMedico().getIdMedico().equals(medico.getIdMedico())) {

                        Map updates = new HashMap<>();

                        updates.put("tratamento/" + t.getIdTratamento() + "/medico/nomeMedico", medico.getNomeMedico());
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

    private boolean validar(){
        return !edtNome.getText().toString().equals("") && (!spUf.getSelectedItem().equals("UF"));
    }

    private void preencherRegistro(){

        spRegistro = findViewById(R.id.spRegistro);

        listRegistro = new ArrayList<String>();

        listRegistro.add("Tipo de registro");
        listRegistro.add("CRM");
        listRegistro.add("CRO");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, listRegistro);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spRegistro.setAdapter(dataAdapter);
    }

    private void preencherUf(){

        spUf = findViewById(R.id.spUF);
        listUf = new ArrayList<String>();

        listUf.add("UF");
        listUf.add("AC");
        listUf.add("AL");
        listUf.add("AP");
        listUf.add("AM");
        listUf.add("BA");
        listUf.add("CE");
        listUf.add("DF");
        listUf.add("ES");
        listUf.add("GO");
        listUf.add("MA");
        listUf.add("MT");
        listUf.add("MS");
        listUf.add("MG");
        listUf.add("PA");
        listUf.add("PB");
        listUf.add("PR");
        listUf.add("PE");
        listUf.add("PI");
        listUf.add("RJ");
        listUf.add("RN");
        listUf.add("RS");
        listUf.add("RO");
        listUf.add("RR");
        listUf.add("SC");
        listUf.add("SP");
        listUf.add("SE");
        listUf.add("TO");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, listUf);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spUf.setAdapter(dataAdapter);
    }
}
