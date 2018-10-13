package com.example.projetoteste.projetoteste;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;

import model.ClasseTerapeutica;
import model.Diagnostico;
import model.Laboratorio;
import model.Medicamento;
import model.Medico;
import model.Paciente;
import model.PrincipioAtivo;
import model.Tratamento;

public class PrencheArray {

    private final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private final String usuario = firebaseAuth.getCurrentUser().getEmail();

    private final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private final Query pacienteRef = databaseReference.child("paciente").orderByChild("usuarioPaciente").equalTo(usuario);
    private final Query medicoRef = databaseReference.child("medico").orderByChild("usuarioMedico").equalTo(usuario);
    private final Query diagnosticoRef = databaseReference.child("diagnostico").orderByChild("usuarioDiagnostico").equalTo(usuario);
    private final DatabaseReference medicamentoRef = databaseReference.child("medicamento");
    private final DatabaseReference laboRef = databaseReference.child("laboratorio");
    private final DatabaseReference princRef = databaseReference.child("principioAtivo");
    private final DatabaseReference classRef = databaseReference.child("classeTerapeutica");
    private final DatabaseReference tratRef = databaseReference.child("tratamento");
    private final Query tratUsu = databaseReference.child("tratamento").orderByChild("usuarioTratamento").equalTo(usuario);

    public ArrayList<Medico> populaMedico() {

        final ArrayList<Medico> arrayMedicos = new ArrayList<>();

        medicoRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Medico medico = dataSnapshot.getValue(Medico.class);
                arrayMedicos.add(medico);

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

        return arrayMedicos;

    }


    public ArrayList<Paciente> populaPaciente() {

        final ArrayList<Paciente> arrayPacientes = new ArrayList<>();

        pacienteRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Paciente paciente = dataSnapshot.getValue(Paciente.class);
                arrayPacientes.add(paciente);

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

        return arrayPacientes;

    }

    public ArrayList<Diagnostico> populaDiag() {

        final ArrayList<Diagnostico> arrayDiagno = new ArrayList<>();

        diagnosticoRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                Diagnostico diagnostico = dataSnapshot.getValue(Diagnostico.class);
                arrayDiagno.add(diagnostico);

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

        return arrayDiagno;
    }

    public ArrayList<Laboratorio> populaLab() {

        final ArrayList<Laboratorio> arrayLaboratorios = new ArrayList<>();

        laboRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Laboratorio laboratorio = dataSnapshot.getValue(Laboratorio.class);
                arrayLaboratorios.add(laboratorio);

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

        return arrayLaboratorios;

    }

    public ArrayList<PrincipioAtivo> populaPrinc() {

        final ArrayList<PrincipioAtivo> arrayPrinc = new ArrayList<>();

        princRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                PrincipioAtivo princ = dataSnapshot.getValue(PrincipioAtivo.class);
                arrayPrinc.add(princ);

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

        return arrayPrinc;

    }

    public ArrayList<ClasseTerapeutica> populaClass() {

        final ArrayList<ClasseTerapeutica> arrayClass = new ArrayList<>();

        classRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                ClasseTerapeutica classt = dataSnapshot.getValue(ClasseTerapeutica.class);
                arrayClass.add(classt);

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

        return arrayClass;

    }

    public ArrayList<Tratamento> preencheTrat() {

        final ArrayList<Tratamento> t2 = new ArrayList<>();

        tratRef.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                Tratamento t = dataSnapshot.getValue(Tratamento.class);
                t2.add(t);
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

        return t2;

    }

    public ArrayList<Tratamento> preencheTratUsu() {

        final ArrayList<Tratamento> t2 = new ArrayList<>();

        tratUsu.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                Tratamento t = dataSnapshot.getValue(Tratamento.class);
                t2.add(t);
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

        return t2;

    }

    public ArrayList<Medicamento> populaMedicamento() {

        final ArrayList<Medicamento> med = new ArrayList<>();

        medicamentoRef.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                Medicamento m = dataSnapshot.getValue(Medicamento.class);
                med.add(m);
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

        return med;

    }



}
