package model;

import java.io.Serializable;
import java.util.ArrayList;

public class Tratamento implements Serializable {

    private String idTratamento;
    private Diagnostico diagnostico;
    private Paciente paciente;
    private Medico medico;
    private ArrayList<Medicamento> arrayMedicamento;
    private String usuarioTratamento;

    public Tratamento(){

    }

    @Override
    public String toString(){
        return diagnostico.getNomeDiagnostico();
    }


    public String getIdTratamento() {
        return idTratamento;
    }

    public void setIdTratamento(String idTratamento) {
        this.idTratamento = idTratamento;
    }

    public Diagnostico getDiagnostico() {
        return diagnostico;
    }

    public void setDiagnostico(Diagnostico diagnostico) {
        this.diagnostico = diagnostico;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    public Medico getMedico() {
        return medico;
    }

    public void setMedico(Medico medico) {
        this.medico = medico;
    }

    public ArrayList<Medicamento> getArrayMedicamento() {
        return arrayMedicamento;
    }

    public void setArrayMedicamento(ArrayList<Medicamento> arrayMedicamento) {
        this.arrayMedicamento = arrayMedicamento;
    }

    public String getUsuarioTratamento() {
        return usuarioTratamento;
    }

    public void setUsuarioTratamento(String usuarioTratamento) {
        this.usuarioTratamento = usuarioTratamento;
    }
}
