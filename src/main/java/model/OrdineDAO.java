package model;
//YYYY-MM-DD

import java.sql.*;
import java.util.ArrayList;

public class OrdineDAO {
    /**
     * Il metodo ha lo scopo di ottenere l'ordine che possiede un preciso ID.
     * @param id l'ID dell'ordine che si sta cercando,
     * @return l'ordine corrispondente (null se non esiste alcun ordine con quell'ID).
     */
    public Ordine doRetrieveByID(int id) {
        try (Connection connection = ConPool.getConnection()) {
            //Query per ottenere i campi di un ordine identificato da un ID.
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM Ordine WHERE Ordine.ID = ?");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            //Creo un nuovo oggetto ordine, inserendo le informazioni dedotte dalla query.
            if (rs.next()) {
                Ordine ordine = new Ordine();
                ordine.setID(rs.getInt(1));

                //Conversione della data da oggetto java.sql.Date a oggetto java.util.Date.
                java.util.Date data = new java.util.Date(rs.getDate(2).getTime());
                ordine.setDataOrdine(data);
                ordine.setPrezzoTotale(rs.getDouble(3));
                ordine.setNumLezioni(rs.getInt(4));
                ordine.setEmailAcquirente(rs.getString(5));

                //aggiungiamo la lista... DA FARE
                return ordine;
            }
            else return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Il seguente metodo ha lo scopo di salvare nel database l'ordine.
     * @param ordine il bean dell'ordine da voler salvare nel database.
     */
    public void doSave(Ordine ordine) {
        try (Connection connection = ConPool.getConnection()) {
            //Inserimento nella tabella Ordine.
            PreparedStatement ps = connection.prepareStatement("INSERT INTO Ordine(data_ordine, prezzo_tot, num_lezioni, email_acquirente) VALUES(?,?,?,?)",
                                                                    Statement.RETURN_GENERATED_KEYS);

            //settaggio dei campi da inserire, prelevandoli dall'oggetto ordine.
            java.sql.Date data = new java.sql.Date(ordine.getDataOrdine().getTime()); //Conversione della data da oggetto java.util.Date a oggetto java.sql.Date.
            ps.setDate(1, data);
            ps.setDouble(2, ordine.getPrezzoTotale());
            ps.setInt(3, ordine.getNumLezioni());
            ps.setString(4, ordine.getEmailAcquirente());

            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if(rs.next())
                ordine.setID(rs.getInt(1));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Questo metodo permette di aggiornare i campi di un certo ordine.
     * @param ordine il bean dell'ordine da modificare.
     */
    public void doUpdateOrdine(Ordine ordine){
        try(Connection connection = ConPool.getConnection()){
            //Aggiornamento nella tabella Ordine di tutti i campi, escluso l'ID dell'ordine stesso.
            PreparedStatement ps = connection.prepareStatement("UPDATE Ordine " +
                                                                   "SET prezzo_tot=?, num_lezioni=? " +
                                                                   "WHERE ID=?");

            ps.setDouble(1, ordine.getPrezzoTotale());
            ps.setInt(2, ordine.getNumLezioni());
            ps.setInt(3, ordine.getID());

            ps.executeUpdate();
        } catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    /**
     * Il seguente metodo ricava tutti gli ordini effettuati da un certo studente.
     * @param email l'email dello studente di cui si vogliono ottenere gli ordini effettuati.
     * @return la lista che contiene gli ordini effettuati dallo studente.
     */
    public ArrayList<Ordine> doRetrieveByEmailStudente(String email){
        try(Connection connection = ConPool.getConnection()){
            ArrayList<Ordine> list1 = new ArrayList<>();

            //Query per ottenere tutti i campi di tutti gli ordini effettuati dallo studente che ha l'email precisata come parametro esplicito.
            PreparedStatement ps1 = connection.prepareStatement("SELECT ID, data_ordine, prezzo_tot, num_lezioni, email_acquirente " +
                                                                    "FROM Ordine " +
                                                                    "WHERE email_acquirente = ?");

            /*
            Query per ottenere tutti i campi delle lezioni prenotate (compresi quelli contenuti nelle lezioni,
            perchè il bean li comprende) da un certo studente.
            */
            PreparedStatement ps2 = connection.prepareStatement("SELECT Lezione.ID, Lezione.materia, Lezione.ore, Lezione.prezzo, Lezione.email_tutor, Utente.nome, Utente.cognome " +
                                                                    "FROM Lezione, Ordine, Utente " +
                                                                    "WHERE Lezione.ID_ord = Ordine.ID " +
                                                                    "AND Lezione.email_tutor = Utente.email " +
                                                                    "AND Lezione.isPrenotata = 1 " +
                                                                    "AND Lezione.email_acquirente = ? " +
                                                                    "AND Lezione.ID_ord = ?");

            ps1.setString(1, email);
            ps2.setString(1, email);
            ResultSet rs1 = ps1.executeQuery();

            //Creo una lista di ordini.
            while (rs1.next()) {
                //Ottengo le informazioni per ogni ordine e le salvo nel bean, che alla fine verrà aggiunto alla lista.
                Ordine ordine = new Ordine();
                ordine.setID(rs1.getInt(1));
                java.util.Date data = new java.util.Date(rs1.getDate(2).getTime());
                ordine.setDataOrdine(data);
                ordine.setPrezzoTotale(rs1.getDouble(3));
                ordine.setNumLezioni(rs1.getInt(4));
                ordine.setEmailAcquirente(rs1.getString(5));

                //Setto, per ogni ordine, l'ID nella query per ottenere tutte le lezioni prenotate contenute in uno specifico ordine.
                ps2.setInt(2, ordine.getID());
                ResultSet rs2 = ps2.executeQuery();
                //Creo una lista di lezioni prenotate, ne creo una e ogni lezione prenotata viene salvata in un bean.
                ArrayList<Lezione> list2 = new ArrayList<>();
                while(rs2.next()){
                    Lezione lezione = new Lezione();
                    lezione.setID(rs2.getInt(1));
                    lezione.setMateria(rs2.getString(2));
                    lezione.setOre(rs2.getInt(3));
                    lezione.setPrezzo(rs2.getDouble(4));
                    lezione.setEmailTutor(rs2.getString(5));
                    lezione.setNomeTutor(rs2.getString(6) + " " + rs2.getString(7));
                    lezione.setEmailAcquirente(ordine.getEmailAcquirente());
                    lezione.setPrenotata(true);
                    lezione.setIDOrdine(ordine.getID());
                    list2.add(lezione);
                }
                ordine.setLezioniPrenotate(list2); //Set della lista nel bean dell'ordine.
                list1.add(ordine);
            }
            return list1;
        } catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    /**
     * Il metodo di seguito ha lo scopo di salvare nel database la lezione prenotata specificata.
     * @param lezione il bean della lezione prenotata da salvare.
     */
    public void doSaveLezione(Lezione lezione) {

        /*nel carrello, per ogni lezione, creerò una lezione prenotata. in questo modo avrò tutte le informazioni relative alla lezione,
        incluso l'ID.*/

        try (Connection connection = ConPool.getConnection()) {
            //Inserimento nella tabella Lezione Prenotata.
            PreparedStatement ps = connection.prepareStatement("UPDATE Lezione SET isPrenotata = 1, ID_ord = ? WHERE Lezione.ID = ?");

            //settaggio dei campi da inserire, prelevandoli dall'oggetto lezione prenotata.
            ps.setInt(1, lezione.getIDOrdine());
            ps.setInt(2, lezione.getID());

            ps.executeUpdate();

            //prendo l'ordine per aggiornarlo nel database (prezzo totale e numero di lezioni)
            Ordine ordine = this.doRetrieveByID(lezione.getIDOrdine());
            ordine.setPrezzoTotale(ordine.getPrezzoTotale() + lezione.getPrezzo());
            ordine.setNumLezioni(ordine.getNumLezioni() + 1);
            this.doUpdateOrdine(ordine);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Il seguente metodo permette di conoscere il numero totale di ordini effettuati sul sito.
     * @return il valore in questione.
     */
    public int doRetrieveNumTotOrdini() {
        try (Connection connection = ConPool.getConnection()) {
            int numTotOrdini = 0;
            PreparedStatement ps = connection.prepareStatement("SELECT COUNT(*) FROM Ordine");

            ResultSet rs = ps.executeQuery();
            if(rs.next())
                numTotOrdini = rs.getInt(1);

            return numTotOrdini;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Il seguente metodo permette di conoscere il numero totale di ordini effettuati sul sito.
     * @return il valore in questione.
     */
    public int doRetrieveNumTotOrdiniByEmail(String email) {
        try (Connection connection = ConPool.getConnection()) {
            int numTotOrdini = 0;
            PreparedStatement ps = connection.prepareStatement("SELECT COUNT(*) FROM Ordine WHERE Ordine.email_acquirente = ?");
            ps.setString(1, email);

            ResultSet rs = ps.executeQuery();
            if(rs.next())
                numTotOrdini = rs.getInt(1);

            return numTotOrdini;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
