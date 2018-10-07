package model;

import java.io.Serializable;

public class MedicamentoLocal implements Serializable{

    private int idMedicamento;
    private String descricao;
    private String laboratorio;
    private String barras;

    public MedicamentoLocal(){

    }

    @Override
    public String toString(){
        return descricao;
    }

    public int getIdMedicamento() {
        return idMedicamento;
    }

    public void setIdMedicamento(int idMedicamento) {
        this.idMedicamento = idMedicamento;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getLaboratorio() {
        return laboratorio;
    }

    public void setLaboratorio(String laboratorio) {
        this.laboratorio = laboratorio;
    }

    public String getBarras() {
        return barras;
    }

    public void setBarras(String barras) {
        this.barras = barras;
    }
}
