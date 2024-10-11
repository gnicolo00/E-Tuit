package model;

import java.sql.*;
import java.util.ArrayList;

public class StudenteDAO {
    /**
     * Il metodo proposto permette di ottenere uno studente data l'email e la password specificate.
     * @param email l'email dello studente.
     * @param passwordhash la password dello studente.
     * @return lo studente corrispondente (null se inesistente).
     */
    public Studente doRetrieveByEmailAndPassword(String email, String passwordhash){
        try(Connection con = ConPool.getConnection()){
            PreparedStatement ps = con.prepareStatement("SELECT email_utente, nome, cognome, passwordhash, ddn, sesso, isAdmin, isBanned " +
                                                            "FROM Studente, Utente " +
                                                            "WHERE Utente.email = Studente.email_utente " +
                                                            "AND Studente.email_utente = ? " +
                                                            "AND passwordhash = SHA1 (?)");
            ps.setString(1,email);
            ps.setString(2,passwordhash);

            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                Studente studente = new Studente();
                studente.setNome(rs.getString(2));
                studente.setCognome(rs.getString(3));
                studente.setEmailUtente(rs.getString(1));
                studente.setPassword(rs.getString(4));
                java.util.Date dataDiNascita = new java.util.Date(rs.getDate(5).getTime());
                studente.setDataDiNascita(dataDiNascita);
                studente.setSesso(rs.getString(6));
                studente.setAdmin(rs.getBoolean(7));
                studente.setBanned(rs.getBoolean(8));
                return studente;
            }
            else return null;
        }
        catch(SQLException e){
            throw new RuntimeException(e);
        }
    }

    /**
     * Il metodo proposto permette di ottenere uno studente data l'email specificata.
     * @param email l'email dello studente che si intende ricercare.
     * @return lo studente corrispondente (null se inesistente).
     */
    public Studente doRetrieveByEmail(String email) {
        try (Connection connection = ConPool.getConnection()) {
            /*
            Query per selezionare i campi di uno studente che ha una certa email. E' necessario
            effettuare una join con la tabella Utente (il campo 'utile' è l'email).
            */
            PreparedStatement ps = connection.prepareStatement("SELECT nome, cognome, email, passwordhash, ddn, sesso, isAdmin, isBanned " +
                                                                    "FROM Studente, Utente " +
                                                                    "WHERE Utente.email = Studente.email_utente " +
                                                                    "AND Studente.email_utente = ?");
            ps.setString(1, email);

            ResultSet rs = ps.executeQuery();
            //Creo un nuovo oggetto studente in cui vengono salvate le informazioni ricavate dalla query.
            if (rs.next()) {
                Studente studente = new Studente();
                studente.setNome(rs.getString(1));
                studente.setCognome(rs.getString(2));
                studente.setEmailUtente(rs.getString(3));
                studente.setPassword(rs.getString(4));
                java.util.Date dataDiNascita = new java.util.Date(rs.getDate(5).getTime());
                studente.setDataDiNascita(dataDiNascita);
                studente.setSesso(rs.getString(6));
                studente.setAdmin(rs.getBoolean(7));
                studente.setBanned(rs.getBoolean(8));
                return studente;
            }
            else return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Il seguente metodo permette di inserire un nuovo studente nel database.
     * @param studente il bean dello studente da salvare nel database.
     */
    public void doSave(Studente studente) {
        try (Connection connection = ConPool.getConnection()) {
            /*
            La procedura di inserimento di una tupla nella tabella Studente è legata all'inserimento nella tabella Utente.
            Per questo motivo, occorre effettuare 2 query distinte per gli inserimenti: le 2 tabelle contengono una porzione
            specifica di informazioni relative allo studente.
            */

            //Inserimento nella tabella Utente.
            PreparedStatement ps1 = connection.prepareStatement("INSERT INTO Utente (nome, cognome, email, passwordhash, ddn, sesso, isAdmin, isBanned) VALUES(?,?,?,?,?,?,?,?)");

            //Settaggio delle proprietà della tupla, prelevandoli dall'oggetto studente.
            ps1.setString(1, studente.getNome());
            ps1.setString(2, studente.getCognome());
            ps1.setString(3, studente.getEmailUtente());
            ps1.setString(4, studente.getPassword());
            java.sql.Date dataDiNascita = new java.sql.Date(studente.getDataDiNascita().getTime());
            ps1.setDate(5, dataDiNascita);
            ps1.setString(6, studente.getSesso());
            ps1.setBoolean(7, studente.isAdmin());
            ps1.setBoolean(8, studente.isBanned());
            ps1.executeUpdate();

            //Inserimento nella tabella Tutor.
            PreparedStatement ps2 = connection.prepareStatement("INSERT INTO Studente (email_utente) VALUES(?)");
            ps2.setString(1, studente.getEmailUtente());
            ps2.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Il seguente metodo permette di aggiornare i campi dello studente in questione,
     * @param studente lo studente che contiene le informazioni, già aggiornate, da modificare nel database.
     */
    public void doUpdateStudente(Studente studente){
        try(Connection connection = ConPool.getConnection()){
            /*
            Aggiornamento nella tabella Utente: in effetti, i dati sono conservati lì.
            Non serve fare un aggiornamento sulla tabella Studente perchè l'email  non varia.
            */
            PreparedStatement ps = connection.prepareStatement("UPDATE Utente " +
                                                                    "SET nome=?, cognome=?, sesso=?, isBanned=?, isAdmin=? " +
                                                                    "WHERE email=?");

            //Settaggio delle proprietà della tupla, prelevandoli dall'oggetto studente.
            ps.setString(1, studente.getNome());
            ps.setString(2, studente.getCognome());
            ps.setString(3, studente.getSesso());
            ps.setBoolean(4, studente.isAdmin());
            ps.setBoolean(5, studente.isBanned());

            ps.executeUpdate();
        } catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    /**
     * Il seguente metodo consente di ottenere tutti gli studenti presenti nel database.
     * @return la lista che contiene tutti gli studenti del database.
     */
    public ArrayList<Studente> doRetrieveAll(){
        try(Connection connection = ConPool.getConnection()){
            ArrayList<Studente> list = new ArrayList<>();
            //Query per selezionare i campi di tutti gli studenti (serve fare una join delle tabelle Utente e Studente).
            PreparedStatement ps = connection.prepareStatement("SELECT nome, cognome, email, passwordhash, ddn, sesso, isAdmin, isBanned " +
                                                                    "FROM Studente, Utente " +
                                                                    "WHERE Utente.email = Studente.email_utente");

            ResultSet rs = ps.executeQuery();
            //Per ogni tupla studente, creo un nuovo oggetto studente,, salvo le informazioni ottenute e lo inserisco nella lista.
            while (rs.next()) {
                Studente studente = new Studente();
                studente.setNome(rs.getString(1));
                studente.setCognome(rs.getString(2));
                studente.setEmailUtente(rs.getString(3));
                studente.setPassword(rs.getString(4));
                java.util.Date dataDiNascita = new java.util.Date(rs.getDate(5).getTime());
                studente.setDataDiNascita(dataDiNascita);
                studente.setSesso(rs.getString(6));
                studente.setAdmin(rs.getBoolean(7));
                studente.setBanned(rs.getBoolean(8));
                list.add(studente);
            }
            return list;
        } catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    /**
     * Il seguente metodo permette di ottenere l'admin che ha quell'email e quella password, serve per il login.
     * @param email l'email inserita dall'utente.
     * @param passwordhash la password inserita dall'utente.
     * @return l'admin corrispondente.
     */
    public Studente doRetrieveAdminByEmailAndPassword(String email, String passwordhash){
        try(Connection con = ConPool.getConnection()){
            PreparedStatement ps = con.prepareStatement("SELECT nome, cognome, passwordhash, ddn, sesso, isBanned " +
                                                            "FROM Utente " +
                                                            "WHERE Utente.email = ? " +
                                                            "AND Utente.isAdmin = 1 " +
                                                            "AND passwordhash = SHA1 (?)");
            ps.setString(1,email);
            ps.setString(2,passwordhash);

            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                Studente admin = new Studente();
                admin.setNome(rs.getString(2));
                admin.setCognome(rs.getString(3));
                admin.setEmailUtente(email);
                admin.setPassword(rs.getString(4));
                java.util.Date dataDiNascita = new java.util.Date(rs.getDate(4).getTime());
                admin.setDataDiNascita(dataDiNascita);
                admin.setSesso(rs.getString(5));
                admin.setAdmin(true);
                admin.setBanned(rs.getBoolean(6));
                return admin;
            }
            else return null;
        }
        catch(SQLException e){
            throw new RuntimeException(e);
        }
    }


    /**
     * Il seguente metodo permette di bannare o sbannare lo studente specificato.
     * @param ban il valore booleano in base a cui si banna o sbanna.
     * @param email l'email dello studente coinvolto.
     */
    public void doUpdateBan(boolean ban, String email){
        //ban è true se l'utente viene bannate, false per rimuovere il ban
        try(Connection connection = ConPool.getConnection()){
            PreparedStatement ps = connection.prepareStatement("UPDATE Utente " +
                                                                   "SET isBanned = ? " +
                                                                   "WHERE Utente.email = ?");

            ps.setBoolean(1, ban);
            ps.setString(2, email);

            ps.executeUpdate();
        }
        catch (SQLException ex){
            throw new RuntimeException(ex);
        }
    }

    /**
     * Il seguente metodo permette di cambiare nominativo allo studente.
     * @param email l'email dello studente.
     * @param newFirstName il nome nuovo da inserire.
     * @param newLastName il cognome nuovo da inserire.
     */
    public void doEditName(String email, String newFirstName, String newLastName){
        try(Connection connection = ConPool.getConnection()){
            PreparedStatement ps = connection.prepareStatement("UPDATE Utente " +
                                                                "SET nome = ?, cognome = ?" +
                                                                "WHERE Utente.email = ?");

            ps.setString(1, newFirstName);
            ps.setString(2, newLastName);
            ps.setString(3, email);

            ps.executeUpdate();
        }
        catch (SQLException ex){
            throw new RuntimeException(ex);
        }
    }
}
