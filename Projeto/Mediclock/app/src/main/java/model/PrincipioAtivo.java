package model;

import java.io.Serializable;

import ir.mirrajabi.searchdialog.core.Searchable;

public class PrincipioAtivo implements Serializable, Searchable {

    private String idPrincipioA;
    private String nomePrincipioAtivo;
    private String title;


    public PrincipioAtivo(){

    }

    @Override
    public String toString(){
        return nomePrincipioAtivo;
    }

    public String getIdPrincipioA() {
        return idPrincipioA;
    }

    public void setIdPrincipioA(String idPrincipioA) {
        this.idPrincipioA = idPrincipioA;
    }

    public String getNomePrincipioAtivo() {
        return nomePrincipioAtivo;
    }

    public void setNomePrincipioAtivo(String nomePrincipioAtivo) {
        this.nomePrincipioAtivo = nomePrincipioAtivo;
    }

    @Override
    public String getTitle() {
        return nomePrincipioAtivo;
    }
}
