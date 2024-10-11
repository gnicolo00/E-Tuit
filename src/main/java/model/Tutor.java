package model;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class Tutor {
    private String nome;
    private String cognome;
    private String emailUtente;
    private String passwordhash;
    private Date dataDiNascita;
    private String sesso;
    private String titoli;
    private String settori;
    private double guadagno;
    private int oreTutoring;
    private double tariffaOraria;
    private String giorni;
    private String descrizioneCard;
    private String descrizioneCompleta;
    private int numRecensioni;
    private double recensioneMedia;
    private boolean isAmbasciatore;
    private boolean isAdmin;
    private boolean isBanned;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public String getEmailUtente() {
        return emailUtente;
    }

    public void setEmailUtente(String emailUtente) {
        this.emailUtente = emailUtente;
    }

    public void setPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            digest.reset();
            digest.update(password.getBytes(StandardCharsets.UTF_8));
            this.passwordhash = String.format("%040x", new BigInteger(1, digest.digest()));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public String getPassword() {
        return this.passwordhash;
    }

    public Date getDataDiNascita() { return dataDiNascita; }

    public void setDataDiNascita(Date dataDiNascita) { this.dataDiNascita = dataDiNascita; }

    public String getSesso() { return sesso; }

    public void setSesso(String sesso) { this.sesso = sesso; }

    public String getTitoli() { return titoli; }

    public String[] getListaTitoli() { return this.titoli.split("-"); }

    public void setTitoli(String titoli) { this.titoli = titoli; }

    public void setTitoliFromArray(String[] titoli) {
        this.titoli = String.join("-",titoli);
    }

    public String getSettori() { return settori; }

    public void setSettori(String settori) { this.settori = settori; }

    public String[] getArraySettori() { return this.settori.split("-"); } //forse serve un metodo per "ricomporre" se devo modificare qualcosa e poi rimetterla dentro

    public void setSettoriFromArray(String[] settori) {
        this.settori = String.join("-",settori);
    }

    public double getGuadagno() {
        return guadagno;
    }

    public void setGuadagno(double guadagno) {
        this.guadagno = guadagno;
    }

    public int getOreTutoring() {
        return oreTutoring;
    }

    public void setOreTutoring(int ore_tutoring) {
        this.oreTutoring = ore_tutoring;
    }

    public double getTariffaOraria() {
        return tariffaOraria;
    }

    public void setTariffaOraria(double tariffaOraria) {
        this.tariffaOraria = tariffaOraria;
    }

    public String getGiorni() { return this.giorni; }

    public ArrayList<String> getListaGiorni() {
        /*
        Nel database, la tabella "Tutor" conserva i giorni sottoforma di stringa binaria (1 per i giorni disponibili, 0 altrimenti).
        Occorre, dunque, rendere più espliciti gli effettivi giorni in cui è disponibile il tutor.
        */
        ArrayList<String> listaGiorni = new ArrayList<>();

        for (int i = 0; i < this.giorni.length(); i++) {
            switch(this.giorni.charAt(i)) {
                case '1': listaGiorni.add("Lunedì"); break;
                case '2': listaGiorni.add("Martedì"); break;
                case '3': listaGiorni.add("Mercoledì"); break;
                case '4': listaGiorni.add("Giovedì"); break;
                case '5': listaGiorni.add("Venerdì"); break;
                case '6': listaGiorni.add("Sabato"); break;
                case '7': listaGiorni.add("Domenica"); break;
            }
        } return listaGiorni;
    }

    public void setGiorni(String giorni) { this.giorni = giorni; }

    public void setGiorniFromArray(String[] giorni) {
        String giorniStringa = "";
        ArrayList<String> listaGiorni = new ArrayList<>(Arrays.asList(giorni));

        if(listaGiorni.contains("lunedì"))
            giorniStringa = giorniStringa.concat("1");
        if(listaGiorni.contains("martedì"))
            giorniStringa = giorniStringa.concat("2");
        if(listaGiorni.contains("mercoledì"))
            giorniStringa = giorniStringa.concat("3");
        if(listaGiorni.contains("giovedì"))
            giorniStringa = giorniStringa.concat("4");
        if(listaGiorni.contains("venerdì"))
            giorniStringa = giorniStringa.concat("5");
        if(listaGiorni.contains("sabato"))
            giorniStringa = giorniStringa.concat("6");
        if(listaGiorni.contains("domenica"))
            giorniStringa = giorniStringa.concat("7");

        this.giorni = giorniStringa;
    }

    public String getDescrizioneCard() { return descrizioneCard; }

    public void setDescrizioneCard(String descrizioneCard) { this.descrizioneCard = descrizioneCard; }

    public String getDescrizioneCompleta() { return descrizioneCompleta; }

    public void setDescrizioneCompleta(String descrizioneCompleta) { this.descrizioneCompleta = descrizioneCompleta; }

    public int getNumRecensioni() { return numRecensioni; }

    public void setNumRecensioni(int numRecensioni) { this.numRecensioni = numRecensioni; }

    public double getRecensioneMedia() { return recensioneMedia; }

    public void setRecensioneMedia(double recensioneMedia) { this.recensioneMedia = recensioneMedia; }

    public boolean isAmbasciatore() { return isAmbasciatore; }

    public void setAmbasciatore(boolean ambasciatore) { isAmbasciatore = ambasciatore; }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public boolean isBanned() {
        return isBanned;
    }

    public void setBanned(boolean banned) {
        isBanned = banned;
    }
}
