package model;

import java.util.Date;
import java.util.GregorianCalendar;
//YYYY-MM-DD
//NOTA: NEL DAO HO RIPORTATO

public class Ticket {
    private int ID;
    private String tipoRichiesta;
    private String oggetto;
    private String descrizione;
    private Date dataInvio;
    private String emailUtente;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getTipoRichiesta() { return tipoRichiesta; }

    public void setTipoRichiesta(String tipoRichiesta) { this.tipoRichiesta = tipoRichiesta; }

    public String getOggetto() {
        return oggetto;
    }

    public void setOggetto(String oggetto) {
        this.oggetto = oggetto;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public Date getDataInvio() {
        return dataInvio;
    }

    public void setDataInvio(Date dataInvio) {
        this.dataInvio = dataInvio;
    }

    public String getEmailUtente() {
        return emailUtente;
    }

    public void setEmailUtente(String emailUtente) {
        this.emailUtente = emailUtente;
    }
}
