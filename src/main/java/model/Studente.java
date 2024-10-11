package model;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

public class Studente {
    private String nome;
    private String cognome;
    private String emailUtente;
    private String passwordhash;
    private Date dataDiNascita;
    private String sesso;
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

    public String getPassword() {
        return passwordhash;
    }

    public void setPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            digest.reset();
            digest.update(password.getBytes(StandardCharsets.UTF_8));
            this.passwordhash = String.format("%040x", new BigInteger(1, digest.digest()));
            System.out.println("password " + this.passwordhash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public Date getDataDiNascita() { return dataDiNascita; }

    public void setDataDiNascita(Date dataDiNascita) { this.dataDiNascita = dataDiNascita; }

    public String getSesso() { return sesso; }

    public void setSesso(String sesso) { this.sesso = sesso; }

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
