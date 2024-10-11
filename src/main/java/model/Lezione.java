package model;

public class Lezione {
    private int ID;
    private String materia;
    private int ore;
    private double prezzo;
    private String emailTutor;
    private String nomeTutor;
    private String emailAcquirente;
    private boolean isPrenotata;
    private int IDOrdine;

    public int getID() { return ID; }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getMateria() { return materia; }

    public void setMateria(String materia) {
        this.materia = materia;
    }

    public int getOre() {
        return ore;
    }

    public void setOre(int ore) {
        this.ore = ore;
    }

    public double getPrezzo() { return prezzo; }

    public void setPrezzo(double prezzo) { this.prezzo = prezzo; }

    public String getEmailTutor() {
        return emailTutor;
    }

    public void setEmailTutor(String emailTutor) {
        this.emailTutor = emailTutor;
    }

    public String getNomeTutor() { return nomeTutor; }

    public void setNomeTutor(String nomeTutor) { this.nomeTutor = nomeTutor; }

    public String getEmailAcquirente() { return emailAcquirente; }

    public void setEmailAcquirente(String emailAcquirente) { this.emailAcquirente = emailAcquirente; }

    public boolean isPrenotata() { return isPrenotata; }

    public void setPrenotata(boolean isPrenotata) { this.isPrenotata = isPrenotata; }

    public int getIDOrdine() { return IDOrdine; }

    public void setIDOrdine(int IDOrdine) { this.IDOrdine = IDOrdine; }
}
