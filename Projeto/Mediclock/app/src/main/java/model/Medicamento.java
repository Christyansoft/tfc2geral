package model;

import java.io.Serializable;

public class Medicamento implements Serializable {

    private String idMedicamento;
    private String nomeMedicamento;
    private String barras1;
    private String barras2;
    private String nomeLaboratorio;
    private String idLaboratorio;
    private String nomePrincipioAtivo;
    private String idPrincipioA;
    private String nomeClasseTerapeutica;
    private String idClasseT;
    private String usuarioMedicamento;

    @Override
    public String toString() {
        return nomeMedicamento;
    }

    public Medicamento(){

    }

    public String getIdMedicamento() {
        return idMedicamento;
    }

    public void setIdMedicamento(String idMedicamento) {
        this.idMedicamento = idMedicamento;
    }

    public String getNomeMedicamento() {
        return nomeMedicamento;
    }

    public void setNomeMedicamento(String nomeMedicamento) {
        this.nomeMedicamento = nomeMedicamento;
    }

    public String getBarras1() {
        return barras1;
    }

    public void setBarras1(String barras1) {
        this.barras1 = barras1;
    }

    public String getBarras2() {
        return barras2;
    }

    public void setBarras2(String barras2) {
        this.barras2 = barras2;
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

    public String getNomePrincipioAtivo() {
        return nomePrincipioAtivo;
    }

    public void setNomePrincipioAtivo(String nomePrincipioAtivo) {
        this.nomePrincipioAtivo = nomePrincipioAtivo;
    }

    public String getIdPrincipioA() {
        return idPrincipioA;
    }

    public void setIdPrincipioA(String idPrincipioA) {
        this.idPrincipioA = idPrincipioA;
    }

    public String getNomeClasseTerapeutica() {
        return nomeClasseTerapeutica;
    }

    public void setNomeClasseTerapeutica(String nomeClasseTerapeutica) {
        this.nomeClasseTerapeutica = nomeClasseTerapeutica;
    }

    public String getIdClasseT() {
        return idClasseT;
    }

    public void setIdClasseT(String idClasseT) {
        this.idClasseT = idClasseT;
    }

    public String getUsuarioMedicamento() {
        return usuarioMedicamento;
    }

    public void setUsuarioMedicamento(String usuarioMedicamento) {
        this.usuarioMedicamento = usuarioMedicamento;
    }
}
