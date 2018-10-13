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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import dao.MedicamentoLocalDAO;
import model.Medicamento;
import model.Tratamento;

public class CadastroMedicamentoLocal extends AppCompatActivity {

    private EditText edtDesc, edtLab, edtBarras;
    private Button salvar;
    private long retornoDB;
    private MedicamentoLocalDAO medDao;
    private Medicamento med2;
    private ArrayList<Tratamento> arrayTrat;
    private final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private final DatabaseReference tratRef = databaseReference.child("tratamento");
    private final DatabaseReference medicamentoRef = databaseReference.child("medicamento");
    private String usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_medicamento_local);

        med2 = (Medicamento) getIntent().getSerializableExtra("medicamento-enviado");

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        usuario = firebaseAuth.getCurrentUser().getEmail();

        PrencheArray pr = new PrencheArray();
        arrayTrat = pr.preencheTratUsu();

        edtDesc = findViewById(R.id.edtDescricao);
        edtLab = findViewById(R.id.edtLaboratorio);
        edtBarras = findViewById(R.id.edtBarras);
        salvar = findViewById(R.id.btnSalvar);
        Button apagar = findViewById(R.id.btnApagar);

        apagar.setVisibility(View.INVISIBLE);

        medDao = new MedicamentoLocalDAO(CadastroMedicamentoLocal.this);

        if(med2!=null){
            edtDesc.setText(med2.getNomeMedicamento());
            edtLab.setText(med2.getNomeLaboratorio());
            edtBarras.setText(med2.getBarras1());
            apagar.setVisibility(View.VISIBLE);
            salvar.setText("Atualizar");
        }

        salvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Medicamento med = new Medicamento();

                med.setIdMedicamento(medicamentoRef.push().getKey());
                med.setNomeMedicamento(edtDesc.getText().toString());
                med.setNomeLaboratorio(edtLab.getText().toString());
                med.setBarras1(edtBarras.getText().toString());
                med.setUsuarioMedicamento(usuario);

                if (salvar.getText().toString().equals("Salvar")) {

                    if (edtDesc.getText().toString().equals("")) {
                        edtDesc.setError("Informe a descrição");
                    } else {

                        retornoDB = medDao.salvarMedicamento(med);

                        if (retornoDB == -1) {
                            //deu erro
                            Toast.makeText(CadastroMedicamentoLocal.this, "Erro na gravação", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(CadastroMedicamentoLocal.this, "Medicamento cadastrado", Toast.LENGTH_SHORT).show();

                            finish();
                        }

                    }
                }else{

                    med.setIdMedicamento(String.valueOf(med2.getIdMedicamento()));
                    retornoDB = medDao.atualizarMedicamento(med);

                    if(retornoDB == -1){
                        //deu erro
                        Toast.makeText(CadastroMedicamentoLocal.this, "Erro ao atualizar", Toast.LENGTH_SHORT).show();
                    }else{
                        updateMedicamento(med);
                        Toast.makeText(CadastroMedicamentoLocal.this, "Medicamento atualizado", Toast.LENGTH_SHORT).show();

                        finish();
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

                medDao.deletarMedicamento(med2);

                if(retornoDB == -1){
                    Toast.makeText(CadastroMedicamentoLocal.this, "Erro ao apagar", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(CadastroMedicamentoLocal.this, "Medicamento apagado", Toast.LENGTH_SHORT).show();
                    finish();
                }

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

    private void updateMedicamento(final Medicamento med) {

        tratRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Tratamento t = dataSnapshot.getValue(Tratamento.class);

                for (int j = 0; j < t.getArrayMedicamento().size(); j++) {

                    if (t.getArrayMedicamento().get(j).getIdMedicamento().equals(med.getIdMedicamento())) {

                        Map updates = new HashMap<>();

                        updates.put("tratamento/" + t.getIdTratamento() + "/arrayMedicamento/" + j + "/nomeMedicamento", med.getNomeMedicamento());
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

}
