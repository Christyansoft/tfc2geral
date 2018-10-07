package model;

import java.io.Serializable;

import ir.mirrajabi.searchdialog.core.Searchable;

public class Diagnostico implements Searchable, Serializable{

    private String idDiagnostico;
    private String nomeDiagnostico;
    private String codigoCid;
    private String title;
    private String usuarioDiagnostico;

    public Diagnostico(){

    }

    public Diagnostico(String nomeDiagnostico, String idDiagnostico){
        this.nomeDiagnostico = nomeDiagnostico;
        this.idDiagnostico = idDiagnostico;
    }

    public String toString(){
        return nomeDiagnostico;
    }

    public String getIdDiagnostico() {
        return idDiagnostico;
    }

    public void setIdDiagnostico(String idDiagnostico) {
        this.idDiagnostico = idDiagnostico;
    }

    public String getNomeDiagnostico() {
        return nomeDiagnostico;
    }

    public void setNomeDiagnostico(String nomeDiagnostico) {
        this.nomeDiagnostico = nomeDiagnostico;
    }

    public String getCodigoCid() {
        return codigoCid;
    }

    public void setCodigoCid(String codigoCid) {
        this.codigoCid = codigoCid;
    }

    public String getUsuarioDiagnostico() {
        return usuarioDiagnostico;
    }

    public void setUsuarioDiagnostico(String usuarioDiagnostico) {
        this.usuarioDiagnostico = usuarioDiagnostico;
    }

    @Override
    public String getTitle() {
        return nomeDiagnostico;
    }
}
