package model;

import java.io.Serializable;

public class Alarme implements Serializable {

    private int idAlarme;
    private String legenda;
    private int hora;
    private int minuto;
    private int segundo;
    private int idPending;
    private String repeating;
    private String idMedicamento;
    private String idTratameto;


    public Alarme(){

    }

    public String toString(){
        return ""+legenda;
    }

    public int getIdAlarme() {
        return idAlarme;
    }

    public void setIdAlarme(int idAlarme) {
        this.idAlarme = idAlarme;
    }

    public int getHora() {
        return hora;
    }

    public void setHora(int hora) {
        this.hora = hora;
    }

    public int getMinuto() {
        return minuto;
    }

    public void setMinuto(int minuto) {
        this.minuto = minuto;
    }

    public int getSegundo() {
        return segundo;
    }

    public void setSegundo(int segundo) {
        this.segundo = segundo;
    }

    public int getIdPending() {
        return idPending;
    }

    public void setIdPending(int idPending) {
        this.idPending = idPending;
    }

    public String getLegenda() {
        return legenda;
    }

    public void setLegenda(String legenda) {
        this.legenda = legenda;
    }

    public String getRepeating() {
        return repeating;
    }

    public void setRepeating(String repeating) {
        this.repeating = repeating;
    }

    public String getIdMedicamento() {
        return idMedicamento;
    }

    public void setIdMedicamento(String idMedicamento) {
        this.idMedicamento = idMedicamento;
    }

    public String getIdTratameto() {
        return idTratameto;
    }

    public void setIdTratameto(String idTratameto) {
        this.idTratameto = idTratameto;
    }
}
