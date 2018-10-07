package com.example.projetoteste.projetoteste;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import dao.MedicamentoLocalDAO;
import model.Medicamento;
import model.Tratamento;

public class CadastroMedicamentoLocal extends AppCompatActivity {

    private EditText edtDesc;
    private EditText edtLab;
    private EditText edtBarras;
    private Button salvar;
    private long retornoDB;
    private MedicamentoLocalDAO medDao;
    private Medicamento med2;

    private final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private final DatabaseReference tratRef = databaseReference.child("tratamento");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_medicamento_local);

        med2 = (Medicamento) getIntent().getSerializableExtra("medicamento-enviado");

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

                med.setNomeMedicamento(edtDesc.getText().toString());
                med.setNomeLaboratorio(edtLab.getText().toString());
                med.setBarras1(edtBarras.getText().toString());

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
                            limpar();
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
                        limpar();
                        finish();
                    }

                }

            }
        });

        apagar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                medDao.deletarMedicamento(med2);

                if(retornoDB == -1){
                    Toast.makeText(CadastroMedicamentoLocal.this, "Erro ao apagar", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(CadastroMedicamentoLocal.this, "Medicamento apagado", Toast.LENGTH_SHORT).show();
                }
                finish();
            }
        });

    }

    private void limpar(){
        edtDesc.setText("");
        edtLab.setText("");
        edtBarras.setText("");
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
