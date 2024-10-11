package model;

import java.util.ArrayList;
import java.util.Date;

public class Ordine {
    private int ID;
    private Date dataOrdine;
    private double prezzoTotale;
    private int numLezioni;
    private String emailAcquirente;
    private ArrayList<Lezione> lezioniPrenotate;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public Date getDataOrdine() {
        return dataOrdine;
    }

    public void setDataOrdine(Date dataOrdine) {
        this.dataOrdine = dataOrdine;
    }

    public double getPrezzoTotale() {
        return prezzoTotale;
    }

    public void setPrezzoTotale(double prezzoTotale) {
        this.prezzoTotale = prezzoTotale;
    }

    public int getNumLezioni() {
        return numLezioni;
    }

    public void setNumLezioni(int numLezioni) { this.numLezioni = numLezioni; }

    public String getEmailAcquirente() { return emailAcquirente; }

    public void setEmailAcquirente(String emailAcquirente) { this.emailAcquirente = emailAcquirente; }

    public ArrayList<Lezione> getLezioniPrenotate() { return lezioniPrenotate; }

    public void setLezioniPrenotate(ArrayList<Lezione> lezioniPrenotate) { this.lezioniPrenotate = lezioniPrenotate; }
}
