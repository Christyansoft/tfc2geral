package model;

import java.io.Serializable;

import ir.mirrajabi.searchdialog.core.Searchable;

public  class Medico implements Searchable, Serializable {

    private String idMedico;
    private String nomeMedico;
    private String numRegistro;
    private String tipoRegistro;
    private String uf;
    private String title;
    private String usuarioMedico;

    public Medico(){

    }

    public Medico(String nomeMedico, String idMedico){
        this.nomeMedico = nomeMedico;
        this.idMedico = idMedico;
    }

    @Override
    public String toString() {
        return nomeMedico;
    }

    public String getIdMedico() {
        return idMedico;
    }

    public void setIdMedico(String idMedico) {
        this.idMedico = idMedico;
    }

    public String getNomeMedico() {
        return nomeMedico;
    }

    public void setNomeMedico(String nomeMedico) {
        this.nomeMedico = nomeMedico;
    }

    public String getNumRegistro() {
        return numRegistro;
    }

    public void setNumRegistro(String numRegistro) {
        this.numRegistro = numRegistro;
    }

    public String getTipoRegistro() {
        return tipoRegistro;
    }

    public void setTipoRegistro(String tipoRegistro) {
        this.tipoRegistro = tipoRegistro;
    }

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }

    public String getUsuarioMedico() {
        return usuarioMedico;
    }

    public void setUsuarioMedico(String usuarioMedico) {
        this.usuarioMedico = usuarioMedico;
    }

    @Override
    public String getTitle() {
        return nomeMedico;

    }

}
