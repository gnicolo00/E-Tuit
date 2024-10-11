package model;

import java.util.ArrayList;

public class Carrello {
    private String emailProprietario;
    private int carico;
    private ArrayList<Lezione> listaLezioni;

    public String getEmailProprietario() { return emailProprietario; }

    public void setEmailProprietario(String emailProprietario) { this.emailProprietario = emailProprietario; }

    public int getCarico() { return carico; }

    public void setCarico(int carico) { this.carico = carico; }

    public void setListaLezioni(ArrayList<Lezione> listaLezioni) { this.listaLezioni = listaLezioni; }

    public ArrayList<Lezione> getListaLezioni() { return this.listaLezioni; }

    public void aggiungiLezione(Lezione lezione){
        this.listaLezioni.add(lezione);
        this.carico++;
    }

    public void rimuoviLezione(int IDLezione){
        Lezione l = new Lezione();
        for (Lezione lezione : this.listaLezioni)
            if (lezione.getID() == IDLezione)
                l = lezione;
        this.listaLezioni.remove(l);
        this.carico--;
    }

    public void svuota(){
        this.listaLezioni.clear();
        this.carico = 0;
    }

    public double getTotale() {
        double total = 0;
        for(Lezione lezione : listaLezioni)
            total += lezione.getPrezzo();

        return total;
    }
}
