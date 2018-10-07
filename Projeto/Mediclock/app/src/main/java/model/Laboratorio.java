package model;

import java.io.Serializable;

import ir.mirrajabi.searchdialog.core.Searchable;

public class Laboratorio implements Serializable, Searchable{

    private String nomeLaboratorio;
    private String idLaboratorio;
    private String cnpj;
    private String title;

    public Laboratorio(){

    }

    @Override
    public String toString(){
        return nomeLaboratorio;
    }

    public String getNomeLaboratorio() {
        return nomeLaboratorio;
    }

    public void setNomeLaboratorio(String nomeLaboratorio) {
        this.nomeLaboratorio = nomeLaboratorio;
    }

    public String getIdLaboratorio() {
        return idLaboratorio;
    }

    public void setIdLaboratorio(String idLaboratorio) {
        this.idLaboratorio = idLaboratorio;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }


    @Override
    public String getTitle() {
        return nomeLaboratorio;
    }
}
