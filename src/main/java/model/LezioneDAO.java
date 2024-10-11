package model;

import java.sql.*;
import java.util.ArrayList;

public class LezioneDAO {
    /**
     * Il seguente metodo permwtte di ottenere una lezione che possiede un certo ID.
     * @param id l'ID della lezione che si sta ricercando.
     * @return la lezione corrispondente (null se inesistente).
     */
    public Lezione doRetrieveByID(int id) {
        try (Connection connection = ConPool.getConnection()) {
            /*
            Query per ottenere i campi di una lezione identificata da un certo ID, precisando che
            è tenuta da un tutor, motivo per cui serve anche effettuare una join tra le due
            tabelle Lezione e Tutor (il campo grazie a cui si effettua la join è l'email del tutor).
            */
            PreparedStatement ps = connection.prepareStatement("SELECT ID, materia, ore, prezzo, email_tutor, nome, cognome, email_acquirente, isPrenotata, ID_ord " +
                                                                   "FROM Lezione, Utente " +
                                                                   "WHERE Lezione.email_tutor = Utente.email " +
                                                                   "AND Lezione.ID=?");

            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Lezione lezione = new Lezione(); //Creo un nuovo oggetto lezione, inserendo le informazioni ricavate dalla query.
                lezione.setID(rs.getInt(1));
                lezione.setMateria(rs.getString(2));
                lezione.setOre(rs.getInt(3));
                lezione.setPrezzo(rs.getDouble(4));
                lezione.setEmailTutor(rs.getString(5));
                lezione.setNomeTutor(rs.getString(6) + " " + rs.getString(7));
                lezione.setEmailAcquirente(rs.getString(8));
                lezione.setPrenotata(rs.getBoolean(9));
                lezione.setIDOrdine(rs.getInt(10));
                return lezione;
            }
            else return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /*
        Non è presente alcun metodo doSave() perché la lezione viene creata e salvata nel momento in cui viene aggiunta al carrello.
        Il metodo analogo, quindi, si trova in CarrelloDAO con il nome doAddLezione().
    */

    /**
     * Il seguente metodo permette di ottenere la lista di tutta le lezioni conservate nel database.
     * @return la lista contenente tutte le lezioni prese dal database.
     */
    public ArrayList<Lezione> doRetrieveAll(){
        try(Connection connection = ConPool.getConnection()){
            ArrayList<Lezione> list = new ArrayList<>();
            /*
            Query per selezionare tutti i campi di tutte le lezioni. Anche in questo caso, servirà effettuare una join
            con la tabella Tutor (per l'email del tutor che tiene la singola lezione).
            */
            PreparedStatement ps = connection.prepareStatement("SELECT ID, materia, ore, prezzo, email_tutor, nome, cognome, email_acquirente, isPrenotata, ID_ord " +
                                                                   "FROM Lezione, Utente " +
                                                                   "WHERE Lezione.email_tutor = Utente.email");

            ResultSet rs = ps.executeQuery();

            //Creo una lista a cui aggiungo gli oggetti lezione creati per le tuple, in cui vengono salvate le informazioni ottenute.
            while (rs.next()) {
                Lezione lezione = new Lezione();
                lezione.setID(rs.getInt(1));
                lezione.setMateria(rs.getString(2));
                lezione.setOre(rs.getInt(3));
                lezione.setPrezzo(rs.getDouble(4));
                lezione.setEmailTutor(rs.getString(5));
                lezione.setNomeTutor(rs.getString(6) + " " + rs.getString(7));
                lezione.setEmailAcquirente(rs.getString(8));
                lezione.setPrenotata(rs.getBoolean(9));
                lezione.setIDOrdine(rs.getInt(10));
                list.add(lezione);
            }
            return list;
        } catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    /**
     * Il seguente metodo permwtte di ottenere tutte le lezioni tenute da un certo tutor (identificato da email).
     * @param emailTutor l'email del tutor di cui si cercano le lezioni.
     * @return la lista che contiene tutte le lezioni di quel tutor.
     */
    public ArrayList<Lezione> doRetrieveByEmailTutor(String emailTutor){
        try(Connection connection = ConPool.getConnection()){
            ArrayList<Lezione> list = new ArrayList<>();
            //Query per selezionare i campi di tutte le lezioni tenute da un tutor preciso (identificato dall'email fornita).
            PreparedStatement ps = connection.prepareStatement("SELECT ID, materia, ore, prezzo, email_tutor, nome, cognome, email_acquirente, isPrenotata, ID_ord " +
                                                                   "FROM Lezione, Utente " +
                                                                   "WHERE Lezione.email_tutor = Utente.email " +
                                                                   "AND Lezione.email_tutor = ?");

            ps.setString(1, emailTutor);

            ResultSet rs = ps.executeQuery();
            //Creo una lista a cui aggiungo gli oggetti lezione creati per le tuple, in cui vengono salvate le informazioni ottenute.
            while (rs.next()) {
                Lezione lezione = new Lezione();
                lezione.setID(rs.getInt(1));
                lezione.setMateria(rs.getString(2));
                lezione.setOre(rs.getInt(3));
                lezione.setPrezzo(rs.getDouble(4));
                lezione.setEmailTutor(rs.getString(5));
                lezione.setNomeTutor(rs.getString(6) + " " + rs.getString(7));
                lezione.setEmailAcquirente(rs.getString(8));
                lezione.setPrenotata(rs.getBoolean(9));
                lezione.setIDOrdine(rs.getInt(10));
                list.add(lezione);
            }
            return list;
        } catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    /**
     * Il metodo proposto permette di cercare tutte le lezioni che trattano una specifica materia.
     * @param materia la materia di cui si vogliono trovare le lezioni.
     * @return la lista di lezioni corrispondenti.
     */
    public ArrayList<Lezione> doRetrieveByMateria(String materia){
        try(Connection connection = ConPool.getConnection()){
            ArrayList<Lezione> list = new ArrayList<>();
            //Query per selezionare i campi di tutte le lezioni che trattano una specifica materia.
            PreparedStatement ps = connection.prepareStatement("SELECT ID, materia, ore, prezzo, email_tutor,  nome, cognome, email_acquirente, isPrenotata, ID_ord " +
                                                                   "FROM Lezione, Utente " +
                                                                   "WHERE Lezione.email_tutor = Utente.email " +
                                                                   "AND Lezione.materia = ? ");
            ps.setString(1, materia);

            ResultSet rs = ps.executeQuery();
            //Creo una lista a cui aggiungo gli oggetti lezione creati per le tuple, in cui vengono salvate le informazioni ottenute.
            while (rs.next()) {
                Lezione lezione = new Lezione();
                lezione.setID(rs.getInt(1));
                lezione.setMateria(rs.getString(2));
                lezione.setOre(rs.getInt(3));
                lezione.setPrezzo(rs.getDouble(4));
                lezione.setEmailTutor(rs.getString(5));
                lezione.setNomeTutor(rs.getString(6) + " " + rs.getString(7));
                lezione.setEmailAcquirente(rs.getString(8));
                lezione.setPrenotata(rs.getBoolean(9));
                lezione.setIDOrdine(rs.getInt(10));
                list.add(lezione);
            }
            return list;
        } catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    /**
     * Il metodo permette di consocere quante lezioni sono state prenotate nel sito.
     * @return il valore in questione.
     */
    public int doRetrieveNumLezioniPrenotate(){
        try (Connection connection = ConPool.getConnection()) {
            int numTotLez = 0;
            PreparedStatement ps = connection.prepareStatement("SELECT COUNT(*) FROM Lezione");

            ResultSet rs = ps.executeQuery();
            if(rs.next())
                numTotLez = rs.getInt(1);

            return numTotLez;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Il seguente metodo permette di sapere quante sono le lezioni prenotate da uno studente.
     * @param email l'email dello studente.
     * @return il valore in questione.
     */
    public int doRetrieveNumLezioniPrenotateByEmail(String email){
        try (Connection connection = ConPool.getConnection()) {
            int numTotLez = 0;
            PreparedStatement ps = connection.prepareStatement("SELECT COUNT(*) FROM Lezione WHERE lezione.email_acquirente = ? AND lezione.isPrenotata = 1");
            ps.setString(1, email);

            ResultSet rs = ps.executeQuery();
            if(rs.next())
                numTotLez = rs.getInt(1);

            return numTotLez;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
