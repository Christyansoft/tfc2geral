package com.example.projetoteste.projetoteste;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import dao.PosologiaDAO;
import model.Alarme;
import model.Diagnostico;
import model.Medicamento;
import model.Medico;
import model.Paciente;
import model.Tratamento;
import ir.mirrajabi.searchdialog.SimpleSearchDialogCompat;
import ir.mirrajabi.searchdialog.core.BaseSearchDialogCompat;
import ir.mirrajabi.searchdialog.core.SearchResultListener;

public class CadastroTratamento extends AppCompatActivity {

    private final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private final DatabaseReference tratamentoRef = databaseReference.child("tratamento");

    private Button btnAddMed;
    private Button salvar;
    private Button apagar;
    private ArrayList<Medico> arrayMedicos;
    private ArrayList<Paciente> arrayPacientes;
    private ArrayList<Diagnostico> arrayDiag;

    private ListView listaMedTrat2;
    private ArrayAdapter adapter;
    private ArrayList<Medicamento> arrayMedicamento = new ArrayList<>();

    private EditText edtdiagnostico;
    private EditText edtpaciente;
    private EditText edtmedico;

    private final PrencheArray prencheArray = new PrencheArray();
    private Tratamento trat2 = null;
    private final Tratamento trat = new Tratamento();
    private PosologiaDAO posologiaDAO;
    private String usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_tratamento);

        edtdiagnostico = findViewById(R.id.edtDiagnostico);
        edtpaciente = findViewById(R.id.edtPaciente);
        edtmedico = findViewById(R.id.edtMedico);
        listaMedTrat2 = findViewById(R.id.listaMedicamentoTra);
        btnAddMed = findViewById(R.id.btnAddMedicamento);
        salvar = findViewById(R.id.salvar);
        apagar = findViewById(R.id.btnApagar);

        apagar.setVisibility(View.INVISIBLE);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        usuario = firebaseAuth.getCurrentUser().getEmail();

        Intent i = getIntent();
        trat2 = (Tratamento) i.getSerializableExtra("tratamento-enviado");

        if (trat2 != null) {

            apagar.setVisibility(View.VISIBLE);
            salvar.setText("Atualizar");
            salvar.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_refresh_black_24dp, 0, 0, 0);

            edtdiagnostico.setText(trat2.getDiagnostico().getNomeDiagnostico());
            if(trat2.getMedico()!=null) {
                edtmedico.setText(trat2.getMedico().getNomeMedico());
            }
            edtpaciente.setText(trat2.getPaciente().getNomePaciente());

            if (trat2.getArrayMedicamento() != null) {
                arrayMedicamento = trat2.getArrayMedicamento();
                adapter = new ArrayAdapter(CadastroTratamento.this, android.R.layout.simple_list_item_1, arrayMedicamento);
                listaMedTrat2.setAdapter(adapter);
            }


            listaMedTrat2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Medicamento med;
                    med = (Medicamento) adapterView.getItemAtPosition(i);

                    Alarme ala;
                    posologiaDAO = new PosologiaDAO(CadastroTratamento.this);
                    ala = posologiaDAO.BuscarAlarme(med.getIdMedicamento(), trat2.getIdTratamento());
                    posologiaDAO.close();

                    if (ala != null) {

                        if (ala.getIdMedicamento() == null) {
                            showDialogo((Medicamento) adapterView.getItemAtPosition(i), trat2.getIdTratamento());
                        } else {
                            Intent intentNova = new Intent(CadastroTratamento.this, AbrirRelogio.class);
                            intentNova.putExtra("alarme-enviado", ala);
                            startActivity(intentNova);

                        }

                    }
                }
            });

            registerForContextMenu(listaMedTrat2);

        }

        arrayMedicos = prencheArray.populaMedico();
        arrayPacientes = prencheArray.populaPaciente();
        arrayDiag = prencheArray.populaDiag();

        ImageButton addMedico = findViewById(R.id.addMedico);
        ImageButton addPaciente = findViewById(R.id.addPaciente);
        ImageButton addDiagno = findViewById(R.id.addDiagnostico);

        edtdiagnostico.setEnabled(false);
        edtpaciente.setEnabled(false);
        edtmedico.setEnabled(false);
        edtmedico.setTextColor(Color.BLACK);
        edtpaciente.setTextColor(Color.BLACK);
        edtdiagnostico.setTextColor(Color.BLACK);

        addMedico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!arrayMedicos.isEmpty()) {

                    new SimpleSearchDialogCompat(CadastroTratamento.this, "Pesquise o médico",
                            "", null, arrayMedicos, new SearchResultListener() {
                        @Override
                        public void onSelected(BaseSearchDialogCompat baseSearchDialogCompat, Object o, int i) {
                            Medico medico = (Medico) o;
                            edtmedico.setText(medico.getNomeMedico());
                            Medico me = new Medico();

                            me.setIdMedico(medico.getIdMedico());
                            me.setNomeMedico(medico.getNomeMedico());
                            trat.setMedico(me);

                            baseSearchDialogCompat.dismiss();
                        }
                    }).show();
                }
                else{
                    showDialogo3("medico");
                }
            }

        });


        addPaciente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!arrayPacientes.isEmpty()) {

                    new SimpleSearchDialogCompat(CadastroTratamento.this, "Pesquise o paciente",
                            "", null, arrayPacientes, new SearchResultListener() {
                        @Override
                        public void onSelected(BaseSearchDialogCompat baseSearchDialogCompat, Object o, int i) {
                            Paciente paciente = (Paciente) o;
                            edtpaciente.setText(paciente.getNomePaciente());
                            Paciente pa = new Paciente();
                            pa.setIdPaciente(paciente.getIdPaciente());
                            pa.setNomePaciente(paciente.getNomePaciente());
                            trat.setPaciente(pa);
                            baseSearchDialogCompat.dismiss();
                        }
                    }).show();
                }
                else{
                    showDialogo3("paciente");
                }
            }
        });

        addDiagno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!arrayDiag.isEmpty()) {

                    new SimpleSearchDialogCompat(CadastroTratamento.this, "Pesquise o diagnostico",
                            "", null, arrayDiag, new SearchResultListener() {
                        @Override
                        public void onSelected(BaseSearchDialogCompat baseSearchDialogCompat, Object o, int i) {
                            Diagnostico diag = (Diagnostico) o;
                            edtdiagnostico.setText(diag.getNomeDiagnostico());
                            Diagnostico d = new Diagnostico();
                            d.setIdDiagnostico(diag.getIdDiagnostico());
                            d.setNomeDiagnostico(diag.getNomeDiagnostico());
                            trat.setDiagnostico(d);
                            baseSearchDialogCompat.dismiss();
                        }
                    }).show();
                }
                else{
                    showDialogo3("diagnostico");
                }
            }
        });


        btnAddMed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CadastroTratamento.this, ListaMedicamentosTrat.class);
                startActivity(intent);

            }
        });

        salvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                trat.setIdTratamento(tratamentoRef.push().getKey());
                trat.setUsuarioTratamento(usuario);

                if(edtdiagnostico.getText().toString().equals("")){
                    edtdiagnostico.setError("");
                }
                if(edtpaciente.getText().toString().equals("")){
                    edtpaciente.setError("");
                }

                if(arrayMedicamento.isEmpty()){
                    Toast.makeText(CadastroTratamento.this, "Adicione algum medicamento antes de salvar o tratamento", Toast.LENGTH_SHORT).show();
                }

                else if(validar()) {
                    trat.setArrayMedicamento(arrayMedicamento);

                    if (salvar.getText().toString().equals("Salvar")){

                        tratamentoRef.child(trat.getIdTratamento()).setValue(trat).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(CadastroTratamento.this, "Tratamento cadastrado", Toast.LENGTH_SHORT).show();
                                    showDialogo("final");
                                } else {
                                    Toast.makeText(CadastroTratamento.this, "Erro ao cadastrar", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                    else{

                        Tratamento trat3 = new Tratamento();

                        //verifica se o usuario modificou o diagnostico
                        if(edtdiagnostico.getText().toString().equals(trat2.getDiagnostico().getNomeDiagnostico())){
                            trat3.setDiagnostico(trat2.getDiagnostico());

                        }
                        else{
                            Diagnostico diag = new Diagnostico(trat.getDiagnostico().getNomeDiagnostico(), trat.getDiagnostico().getIdDiagnostico());
                            trat3.setDiagnostico(diag);
                        }

                        //verifica se o usuario modificou o paciente
                        if(edtpaciente.getText().toString().equals(trat2.getPaciente().getNomePaciente())){
                            trat3.setPaciente(trat2.getPaciente());
                        }
                        else{
                            Paciente pac = new Paciente(trat.getPaciente().getNomePaciente(), trat.getPaciente().getIdPaciente());
                            trat3.setPaciente(pac);

                        }

                        //verificando se o médico é nulo e verifica se o usuario modificou o medico
                        if(trat2.getMedico()!=null) {

                            if (edtmedico.getText().toString().equals(trat2.getMedico().getNomeMedico())) {

                                trat3.setMedico(trat2.getMedico());
                            } else {

                                Medico med = new Medico(trat.getMedico().getNomeMedico(), trat.getMedico().getIdMedico());
                                trat3.setMedico(med);
                            }

                        }else{
                            //medico nao nulo
                            if(!edtmedico.getText().toString().equals("")){
                                Medico med2 = new Medico(trat.getMedico().getNomeMedico(), trat.getMedico().getIdMedico());
                                trat3.setMedico(med2);
                            }
                        }
                        //aqui termina as verificações

                        trat3.setArrayMedicamento(arrayMedicamento);
                        trat3.setIdTratamento(trat2.getIdTratamento());
                        trat3.setUsuarioTratamento(usuario);

                        tratamentoRef.child(trat2.getIdTratamento()).setValue(trat3).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(CadastroTratamento.this, "Tratamento atualizado", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                                else{
                                    Toast.makeText(CadastroTratamento.this, "Erro au atualizar", Toast.LENGTH_SHORT).show();
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


                posologiaDAO = new PosologiaDAO(CadastroTratamento.this);

                boolean existe = false;

                for(Medicamento med: arrayMedicamento){
                    Alarme ala;
                    ala = posologiaDAO.BuscarAlarme(med.getIdMedicamento(), trat2.getIdTratamento());

                    if(ala.getIdMedicamento()!=null){
                        existe = true;
                        break;
                    }

                }
                posologiaDAO.close();

                if(!existe) {

                    tratamentoRef.child(trat2.getIdTratamento()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()) {
                                Toast.makeText(CadastroTratamento.this, "Tratamento apagado", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(CadastroTratamento.this, "erro ao apagar", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                else{
                    showDialogo("Tratamento");
                }
            }
        });


    }

    private void showDialogo(final Medicamento medicamento, final String idTratamento){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //define o titulo
        builder.setTitle("Informação de alarme");
        //define a mensagem
        builder.setMessage("Não existe alarme configurado para esta medicação, deseja criar agora?");
        //define um botão como positivo

        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {

                Intent intent = new Intent(CadastroTratamento.this, AbrirRelogio.class);
                intent.putExtra("med-enviado",  medicamento);
                intent.putExtra("idTratamento", idTratamento);
                startActivity(intent);

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

    private void showDialogo3(String item){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Cadastro");

        switch (item) {
            case "diagnostico":
                builder.setMessage("Você ainda não possui diagnostico cadastrado, deseja criar agora?");

                builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {

                        Intent intent = new Intent(CadastroTratamento.this, CadastroDiagnostico.class);
                        startActivity(intent);

                    }
                });

                builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                break;
            case "paciente":
                builder.setMessage("Você ainda não possui paciente cadastrado, deseja criar agora?");

                builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {

                        Intent intent = new Intent(CadastroTratamento.this, CadastroPaciente.class);
                        startActivity(intent);

                    }
                });

                builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                break;
            case "medico":
                builder.setMessage("Você ainda não possui medico cadastrado, deseja criar agora?");

                builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {

                        Intent intent = new Intent(CadastroTratamento.this, CadastroMedico.class);
                        startActivity(intent);

                    }
                });

                builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                break;
        }

        builder.create().show();


    }

    private void showDialogo(final String item){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //define o titulo

        //define a mensagem
        if(item.equals("final")){
            builder.setTitle("Concluído");
            builder.setMessage("Agora que o seu tratamento está salvo, caso queira você pode entrar nele novmente e selecionar algum" +
                    " medicamento para configurar os alarmes.");
        }
        else if(item.equals("Medicamento")) {
            builder.setTitle("Informação de alarme");

            builder.setMessage("Não é possível excluir este medicamento pois existe alarme vinculado a ele. Caso queira realmente excluir" +
                    " apague o alarme antes.");
        }
        else{

            builder.setTitle("Informação de alarme");
            builder.setMessage("Não é possível excluir este tratamento pois existe alarme vinculado a ele. Caso queira" +
                    " realmente excluir apague o(s) alarme(s) antes.");
        }

        //define um botão como positivo

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {

                if(item.equals("final")){
                    finish();
                }

            }
        });

        builder.create().show();
    }

    private boolean validar(){
        return (!edtdiagnostico.getText().toString().equals("") && (!edtpaciente.getText().toString().equals("")));
    }

    @Override
    protected void onNewIntent(Intent intent) {

        boolean existe = false;
        boolean adicionado = false;

        Medicamento medicamento = (Medicamento) intent.getSerializableExtra("med-enviado");
        Medicamento med2 = new Medicamento();
        med2.setIdMedicamento(medicamento.getIdMedicamento());
        med2.setNomeMedicamento(medicamento.getNomeMedicamento());

        if(!arrayMedicamento.isEmpty()){
            for (int j = 0; j < arrayMedicamento.size(); j++) {
                if (arrayMedicamento.get(j).getIdMedicamento().equals(med2.getIdMedicamento())) {
                    Toast.makeText(this, "A lista já contém o medicamento selecionado", Toast.LENGTH_SHORT).show();
                    existe = true;
                    break;
                }
            }
            if(!existe){
                arrayMedicamento.add(med2);
                adicionado = true;
            }
        }else{
            arrayMedicamento.add(med2);
            adicionado=true;
        }

        adapter = new ArrayAdapter(CadastroTratamento.this, android.R.layout.simple_list_item_1, arrayMedicamento);
        listaMedTrat2.setAdapter(adapter);
        registerForContextMenu(listaMedTrat2);

        if(trat2!=null && adicionado) {
            tratamentoRef.child(trat2.getIdTratamento()).child("arrayMedicamento").setValue(arrayMedicamento).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(CadastroTratamento.this, "medicamento adicionado", Toast.LENGTH_SHORT).show();
                    }
                    else{

                        Toast.makeText(CadastroTratamento.this, "erro ao adicionar", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if (v.getId() == R.id.listaMedicamentoTra) {
            menu.add("Excluir");
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case 0:

                if(trat2==null){
                    arrayMedicamento.remove(info.position);
                }

                if((arrayMedicamento.size()>1) && (trat2!=null)) {

                    Medicamento med;
                    med = arrayMedicamento.get(info.position);

                    Alarme ala;
                    posologiaDAO = new PosologiaDAO(CadastroTratamento.this);
                    ala = posologiaDAO.BuscarAlarme(med.getIdMedicamento(), trat2.getIdTratamento());
                    posologiaDAO.close();

                    if (ala.getIdMedicamento()==null){

                        arrayMedicamento.remove(info.position);
                        tratamentoRef.child(trat2.getIdTratamento()).child("arrayMedicamento").setValue(arrayMedicamento).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(CadastroTratamento.this, "medicamento removido", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                    }
                    else{
                        showDialogo("Medicamento");
                    }

                }

                if((trat2!=null) && (arrayMedicamento.size()==1))

                    Toast.makeText(CadastroTratamento.this, "Não é possível excluir todos medicamentos do tratamento", Toast.LENGTH_SHORT).show();

        }
        adapter.notifyDataSetChanged();
        return true;
    }

}
