package model;

import java.io.Serializable;

import ir.mirrajabi.searchdialog.core.Searchable;

public class Paciente implements Searchable, Serializable{

    private String idPaciente;
    private String nomePaciente;
    private String idade;
    private String sexo;
    private String observacao;
    private String usuarioPaciente;

    public Paciente(){

    }

    public Paciente(String nomePaciente, String idPaciente) {
        this.nomePaciente = nomePaciente;
        this.idPaciente = idPaciente;
    }

    @Override
    public String toString(){
        return this.getNomePaciente();
    }

    public String getIdPaciente() {
        return idPaciente;
    }

    public void setIdPaciente(String idPaciente) {
        this.idPaciente = idPaciente;
    }

    public String getNomePaciente() {
        return nomePaciente;
    }

    public void setNomePaciente(String nomePaciente) {
        this.nomePaciente = nomePaciente;
    }

    public String getIdade() {
        return idade;
    }

    public void setIdade(String idade) {
        this.idade = idade;
    }



    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public void setTitle(String title) {
        String title1 = title;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public String getUsuarioPaciente() {
        return usuarioPaciente;
    }

    public void setUsuarioPaciente(String usuarioPaciente) {
        this.usuarioPaciente = usuarioPaciente;
    }

    @Override
    public String getTitle() {
        return nomePaciente;
    }
}
