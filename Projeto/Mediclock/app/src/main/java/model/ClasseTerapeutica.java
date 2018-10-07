package model;

import java.io.Serializable;

import ir.mirrajabi.searchdialog.core.Searchable;

public class ClasseTerapeutica implements Searchable, Serializable{

    private String idClasseT;
    private String nomeClasseTerapeutica;
    private String title;

    public ClasseTerapeutica(){

    }

    @Override
    public String toString(){
        return nomeClasseTerapeutica;
    }

    public String getIdClasseT() {
        return idClasseT;
    }

    public void setIdClasseT(String idClasseT) {
        this.idClasseT = idClasseT;
    }

    public String getNomeClasseTerapeutica() {
        return nomeClasseTerapeutica;
    }

    public void setNomeClasseTerapeutica(String nomeClasseTerapeutica) {
        this.nomeClasseTerapeutica = nomeClasseTerapeutica;
    }

    @Override
    public String getTitle() {
        return nomeClasseTerapeutica;
    }
}
