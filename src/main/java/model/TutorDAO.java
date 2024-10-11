package model;

import java.sql.*;
import java.util.ArrayList;

public class TutorDAO {
    /**
     * Restituisce tutti i tutor conservati nel database.
     * @return la lista che contiene tutti i tutor presenti nel database.
     */
    public ArrayList<Tutor> doRetrieveAll(){
        try(Connection connection = ConPool.getConnection()){
            ArrayList<Tutor> list = new ArrayList<>();
            //Query per selezionare i campi di tutti i tutor (serve fare una join delle tabelle Utente e Tutor).
            PreparedStatement ps = connection.prepareStatement("SELECT nome, cognome, email_utente, passwordhash, ddn, sesso, titoli, settori, guadagno, ore_tutoring, tariffa_oraria, giorni, descrizione_card, descrizione_completa, num_recensioni, recensione_media, isAmbasciatore, isAdmin, isBanned " +
                                                                   "FROM Tutor, Utente " +
                                                                   "WHERE Utente.email = Tutor.email_utente ");

            ResultSet rs = ps.executeQuery();
            //Creo una lista e per ogni tupla creo nuovo oggetto tutor, inserendo tutte le informazioni ottenute dalla query.
            while (rs.next()) {
                Tutor tutor = new Tutor();
                tutor.setNome(rs.getString(1));
                tutor.setCognome(rs.getString(2));
                tutor.setEmailUtente(rs.getString(3));
                tutor.setPassword(rs.getString(4));
                java.util.Date dataDiNascita = new java.util.Date(rs.getDate(5).getTime()); //conversiona della data
                tutor.setDataDiNascita(dataDiNascita);
                tutor.setSesso(rs.getString(6));
                tutor.setTitoli(rs.getString(7));
                tutor.setSettori(rs.getString(8));
                tutor.setGuadagno(rs.getDouble(9));
                tutor.setOreTutoring(rs.getInt(10));
                tutor.setTariffaOraria(rs.getDouble(11));
                tutor.setGiorni(rs.getString(12));
                tutor.setDescrizioneCard(rs.getString(13));
                tutor.setDescrizioneCompleta(rs.getString(14));
                tutor.setNumRecensioni(rs.getInt(15));
                tutor.setRecensioneMedia(rs.getDouble(16));
                tutor.setAmbasciatore(rs.getBoolean(17));
                tutor.setAdmin(rs.getBoolean(18));
                tutor.setBanned(rs.getBoolean(19));
                list.add(tutor);
            }
            return list;
        } catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    /**
     * Restituisce un tutor data l'email e la password specificate.
     * @param email l'email del tutor.
     * @param passwordhash la password del tutor.
     * @return il tutor corrispondente (null se inesistente).
     */
    public Tutor doRetrieveByEmailAndPassword(String email, String passwordhash){
        try(Connection con = ConPool.getConnection()){
            PreparedStatement ps = con.prepareStatement("SELECT nome, cognome, email_utente, passwordhash, ddn, sesso, titoli, settori, " +
                                                            "guadagno, ore_tutoring, tariffa_oraria, giorni, descrizione_card, descrizione_completa, " +
                                                            "num_recensioni, recensione_media, isAmbasciatore, isAdmin, isBanned " +
                                                            "FROM Tutor, Utente " +
                                                            "WHERE Utente.email = Tutor.email_utente " +
                                                            "AND Tutor.email_utente = ?" +
                                                            "AND passwordhash = SHA1 (?)");

            ps.setString(1,email);
            ps.setString(2,passwordhash);

            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                Tutor tutor = new Tutor();
                tutor.setNome(rs.getString(1));
                tutor.setCognome(rs.getString(2));
                tutor.setEmailUtente(rs.getString(3));
                tutor.setPassword(rs.getString(4));
                java.util.Date dataDiNascita = new java.util.Date(rs.getDate(5).getTime()); //conversione della data
                tutor.setDataDiNascita(dataDiNascita);
                tutor.setSesso(rs.getString(6));
                tutor.setTitoli(rs.getString(7));
                tutor.setSettori(rs.getString(8));
                tutor.setGuadagno(rs.getDouble(9));
                tutor.setOreTutoring(rs.getInt(10));
                tutor.setTariffaOraria(rs.getDouble(11));
                tutor.setGiorni(rs.getString(12));
                tutor.setDescrizioneCard(rs.getString(13));
                tutor.setDescrizioneCompleta(rs.getString(14));
                tutor.setNumRecensioni(rs.getInt(15));
                tutor.setRecensioneMedia(rs.getDouble(16));
                tutor.setAmbasciatore(rs.getBoolean(17));
                tutor.setAdmin(rs.getBoolean(18));
                tutor.setBanned(rs.getBoolean(19));
                return tutor;
            }
            else return null;
        }
        catch(SQLException e){
            throw new RuntimeException(e);
        }
    }

    /**
     * Restituisce un tutor data l'email specificata.
     * @param email l'email del tutor che si intende ricercare.
     * @return il tutor corrispondente (null se inesistente).
     */
    public Tutor doRetrieveByEmail(String email) {
        try (Connection connection = ConPool.getConnection()) {
            /*
            Query per selezionare i campi di un tutor che ha una certa email, ricordando che serve effettuare
            una join con la tabella Utente (il campo che servirà per effettuarla è l'email).
            */
            PreparedStatement ps = connection.prepareStatement("SELECT nome, cognome, email_utente, passwordhash, ddn, sesso, titoli, settori, guadagno, ore_tutoring, tariffa_oraria, giorni, descrizione_card, descrizione_completa, num_recensioni, recensione_media, isAmbasciatore, isAdmin, isBanned " +
                                                                   "FROM Tutor, Utente " +
                                                                   "WHERE Utente.email = Tutor.email_utente " +
                                                                   "AND Tutor.email_utente=?");
            ps.setString(1, email);

            ResultSet rs = ps.executeQuery();
            //creo un nuovo oggetto tutor, inserendo tutte le informazioni ottenute dalla query
            if (rs.next()) {
                Tutor tutor = new Tutor();
                tutor.setNome(rs.getString(1));
                tutor.setCognome(rs.getString(2));
                tutor.setEmailUtente(rs.getString(3));
                tutor.setPassword(rs.getString(4));
                java.util.Date dataDiNascita = new java.util.Date(rs.getDate(5).getTime());
                tutor.setDataDiNascita(dataDiNascita);
                tutor.setSesso(rs.getString(6));
                tutor.setTitoli(rs.getString(7));
                tutor.setSettori(rs.getString(8));
                tutor.setGuadagno(rs.getDouble(9));
                tutor.setOreTutoring(rs.getInt(10));
                tutor.setTariffaOraria(rs.getDouble(11));
                tutor.setGiorni(rs.getString(12));
                tutor.setDescrizioneCard(rs.getString(13));
                tutor.setDescrizioneCompleta(rs.getString(14));
                tutor.setNumRecensioni(rs.getInt(15));
                tutor.setRecensioneMedia(rs.getDouble(16));
                tutor.setAmbasciatore(rs.getBoolean(17));
                tutor.setAdmin(rs.getBoolean(18));
                tutor.setBanned(rs.getBoolean(19));
                return tutor;
            } else return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Restituisce una lista di tutor che presentano settori contenenti un determinato
     * insieme di caratteri dato come parametro.
     * <br>
     * <br>
     * Per esempio:
     * <br>
     * <br>
     * INPUT: "ic"
     * <br>
     * OUTPUT: Tutti i tutor che trattano settori come "informatica", "fisica", "matematica", "chimica", ecc.
     * <br>
     * <br>
     * Questo output è valido solo se i settori riportati si trovano effettivamente nel database.
     * @param input la stringa utilizzata per la ricerca nel database.
     * @return una lista contenente i tutor che riportano dei settori contenenti la stringa data in input.
     */
    public ArrayList<Tutor> doRetrieveBySearchInput(String input) {
        try(Connection connection = ConPool.getConnection()) {
            ArrayList<Tutor> tutors = new ArrayList<>();
            String query = "SELECT nome, cognome, email_utente, passwordhash, ddn, sesso, titoli, settori, guadagno, ore_tutoring, tariffa_oraria, giorni, descrizione_card, descrizione_completa, num_recensioni, recensione_media, isAmbasciatore, isAdmin, isBanned " +
                           "FROM Tutor, Utente " +
                           "WHERE Tutor.email_utente = Utente.email ";
            if(!input.isBlank())
                query += "AND Tutor.settori LIKE ? ";
            query += "LIMIT 15";

            PreparedStatement ps = connection.prepareStatement(query);

            if(!input.isBlank())
                ps.setString(1,"%" + input + "%");// La clausola LIKE necessita l'input avvolto dai caratteri "% %"

            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                Tutor tutor = new Tutor();
                tutor.setNome(rs.getString(1));
                tutor.setCognome(rs.getString(2));
                tutor.setEmailUtente(rs.getString(3));
                tutor.setPassword(rs.getString(4));
                java.util.Date dataDiNascita = new java.util.Date(rs.getDate(5).getTime());
                tutor.setDataDiNascita(dataDiNascita);
                tutor.setSesso(rs.getString(6));
                tutor.setTitoli(rs.getString(7));
                tutor.setSettori(rs.getString(8));
                tutor.setGuadagno(rs.getDouble(9));
                tutor.setOreTutoring(rs.getInt(10));
                tutor.setTariffaOraria(rs.getDouble(11));
                tutor.setGiorni(rs.getString(12));
                tutor.setDescrizioneCard(rs.getString(13));
                tutor.setDescrizioneCompleta(rs.getString(14));
                tutor.setNumRecensioni(rs.getInt(15));
                tutor.setRecensioneMedia(rs.getDouble(16));
                tutor.setAmbasciatore(rs.getBoolean(17));
                tutor.setAdmin(rs.getBoolean(18));
                tutor.setBanned(rs.getBoolean(19));
                tutors.add(tutor);
            }

            return tutors;
        }
        catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Restituisce tutti i tutor che presentano una corrispondenza con ognuno dei filtri passati come parametri.
     * @param days i giorni della settimana in cui il tutor è disponibile
     * @param maxPrice la tariffa oraria massima ammessa
     * @param sort il tipo di ordinamento sui tutor da visualizzare
     * @param rating la minima recensione media ammessa
     * @return la lista dei tutor che presentano corrispondenze
     */
    public ArrayList<Tutor> doRetrieveByFilters(String input, String days, int maxPrice, String sort, int rating, int offset) {
        try(Connection connection = ConPool.getConnection()) {
            ArrayList<Tutor> tutors = new ArrayList<>();
            String query = "SELECT nome, cognome, email_utente, passwordhash, ddn, sesso, titoli, settori, guadagno, ore_tutoring, tariffa_oraria, giorni, descrizione_card, descrizione_completa, num_recensioni, recensione_media, isAmbasciatore, isAdmin, isBanned " +
                           "FROM Tutor, Utente " +
                           "WHERE Tutor.email_utente = Utente.email " +
                           "AND Tutor.giorni REGEXP ? " +
                           "AND Tutor.tariffa_oraria <= ? " +
                           "AND Tutor.recensione_media >= ? ";

            if(!input.isBlank())
                query += "AND Tutor.settori LIKE ? ";
            query += "ORDER BY " + sort + " LIMIT " + offset + ", 15";

            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1,days);
            ps.setInt(2,maxPrice);
            ps.setInt(3,rating);
            if(!input.isBlank())
                ps.setString(4,"%" + input + "%");

            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                Tutor tutor = new Tutor();
                tutor.setNome(rs.getString(1));
                tutor.setCognome(rs.getString(2));
                tutor.setEmailUtente(rs.getString(3));
                tutor.setPassword(rs.getString(4));
                java.util.Date dataDiNascita = new java.util.Date(rs.getDate(5).getTime());
                tutor.setDataDiNascita(dataDiNascita);
                tutor.setSesso(rs.getString(6));
                tutor.setTitoli(rs.getString(7));
                tutor.setSettori(rs.getString(8));
                tutor.setGuadagno(rs.getDouble(9));
                tutor.setOreTutoring(rs.getInt(10));
                tutor.setTariffaOraria(rs.getDouble(11));
                tutor.setGiorni(rs.getString(12));
                tutor.setDescrizioneCard(rs.getString(13));
                tutor.setDescrizioneCompleta(rs.getString(14));
                tutor.setNumRecensioni(rs.getInt(15));
                tutor.setRecensioneMedia(rs.getDouble(16));
                tutor.setAmbasciatore(rs.getBoolean(17));
                tutor.setAdmin(rs.getBoolean(18));
                tutor.setBanned(rs.getBoolean(19));
                tutors.add(tutor);
            }

            return tutors;
        }
        catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Permette di inserire un nuovo tutor nel database.
     * @param tutor il bean del tutor da salvare nel database.
     */
    public void doSave(Tutor tutor) {
        try (Connection connection = ConPool.getConnection()) {
            /*
            La procedura di inserimento di una tupla nella tabella Tutor è legata all'inserimento nella tabella Utente.
            Per questo motivo, occorre effettuare 2 query distinte per gli inserimenti: le 2 tabelle contengono una porzione
            specifica di informazioni relative allo studente.
            */
            //Inserimento nella tabella di utente.
            PreparedStatement ps1 = connection.prepareStatement("INSERT INTO Utente (email, nome, cognome, passwordhash, ddn, sesso, isAdmin, isBanned) VALUES(?,?,?,?,?,?,?,?)");

            //Settaggio dei campi da inserire, prelevandoli dall'oggetto tutor.
            ps1.setString(1, tutor.getEmailUtente());
            ps1.setString(2, tutor.getNome());
            ps1.setString(3, tutor.getCognome());
            ps1.setString(4, tutor.getPassword());
            java.sql.Date dataDiNascita = new java.sql.Date(tutor.getDataDiNascita().getTime()); //conversione della data
            ps1.setDate(5, dataDiNascita);
            ps1.setString(6, tutor.getSesso());
            ps1.setBoolean(7, tutor.isAdmin());
            ps1.setBoolean(8, tutor.isBanned());

            PreparedStatement ps2 = connection.prepareStatement(
                    "INSERT INTO Tutor (email_utente, titoli, settori, guadagno, ore_tutoring, tariffa_oraria, giorni, descrizione_card, descrizione_completa, num_recensioni, recensione_media, isAmbasciatore) VALUES(?,?,?,?,?,?,?,?,?,?,?,?)");
            ps2.setString(1, tutor.getEmailUtente());
            ps2.setString(2, tutor.getTitoli());
            ps2.setString(3, tutor.getSettori());
            ps2.setDouble(4, tutor.getGuadagno());
            ps2.setInt(5, tutor.getOreTutoring());
            ps2.setDouble(6, tutor.getTariffaOraria());
            ps2.setString(7, tutor.getGiorni());
            ps2.setString(8, tutor.getDescrizioneCard());
            ps2.setString(9, tutor.getDescrizioneCompleta());
            ps2.setInt(10, tutor.getNumRecensioni());
            ps2.setDouble(11, tutor.getRecensioneMedia());
            ps2.setBoolean(12, tutor.isAmbasciatore());

            ps1.executeUpdate();
            ps2.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Restituisce i TOT tutor con il numero di ore di tutoring più alto,
     * visualizzabili nella Home.
     * @return la lista dei tutor corrispondenti.
     */
    public ArrayList<Tutor> doRetrieveByHighestHours(){
        try(Connection connection = ConPool.getConnection()){
            ArrayList<Tutor> list = new ArrayList<>();
            /*
            Query per selezionare i tutor con il campo "ore_tutoring" più alto in ordine crescente. Poiché bisogna
            visualizzare solo i primi quattro tutor, viene posto il limite a 4 (specificato dalla clausola LIMIT 4).
             */
            PreparedStatement ps = connection.prepareStatement("SELECT nome, cognome, email_utente, passwordhash, ddn, sesso, titoli, settori, guadagno, ore_tutoring, tariffa_oraria, giorni, descrizione_card, descrizione_completa, num_recensioni, recensione_media, isAmbasciatore, isAdmin, isBanned " +
                                                                   "FROM Tutor, Utente " +
                                                                   "WHERE Utente.email = Tutor.email_utente " +
                                                                   "ORDER BY ore_tutoring " +
                                                                   "LIMIT 9");// il numero è da decidere E RICORDA L'ORDER BY

            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Tutor tutor = new Tutor();
                tutor.setNome(rs.getString(1));
                tutor.setCognome(rs.getString(2));
                tutor.setEmailUtente(rs.getString(3));
                tutor.setPassword(rs.getString(4));
                java.util.Date dataDiNascita = new java.util.Date(rs.getDate(5).getTime()); //conversione della data
                tutor.setDataDiNascita(dataDiNascita);
                tutor.setSesso(rs.getString(6));
                tutor.setTitoli(rs.getString(7));
                tutor.setSettori(rs.getString(8));
                tutor.setGuadagno(rs.getDouble(9));
                tutor.setOreTutoring(rs.getInt(10));
                tutor.setTariffaOraria(rs.getDouble(11));
                tutor.setGiorni(rs.getString(12));
                tutor.setDescrizioneCard(rs.getString(13));
                tutor.setDescrizioneCompleta(rs.getString(14));
                tutor.setNumRecensioni(rs.getInt(15));
                tutor.setRecensioneMedia(rs.getDouble(16));
                tutor.setAmbasciatore(rs.getBoolean(17));
                tutor.setAdmin(rs.getBoolean(18));
                tutor.setBanned(rs.getBoolean(19));
                list.add(tutor);
            }
            return list;
        }
        catch (SQLException ex){
            throw new RuntimeException(ex);
        }
    }

    /**
     * Permette di aggiornare il guadagno del tutor da cui è stata appena prenotata una lezione.
     * @param email l'email del tutor il cui guadagno è da aggiornare
     * @param guadagno il guadagno da sommare a quello già accumulato
     */
    public void doSumEarnings(String email, double guadagno){
        try(Connection connection = ConPool.getConnection()){//Query per aggiornare il guadagno del tutor dopo un ordine. Viene naturalmente sommato il nuovo guadagno con quello già accumulato.
            PreparedStatement ps = connection.prepareStatement("UPDATE Tutor " +
                                                                   "SET guadagno = guadagno + ? " +
                                                                   "WHERE Tutor.email_utente = ?");

            ps.setDouble(1, guadagno);
            ps.setString(2, email);

            ps.executeUpdate();
        }
        catch (SQLException ex){
            throw new RuntimeException(ex);
        }
    }

    /**
     * Restituisce una lista di stringhe contenente i settori, coperti dai tutor nel database, che contengono un determinato
     * insieme di caratteri dati come parametro.
     * <br>
     * <br>
     * Per esempio:
     * <br>
     * <br>
     * INPUT: "ic"
     * <br>
     * OUTPUT: ["informatica", "fisica", "matematica", "chimica", ecc.]
     * <br>
     * <br>
     * Questo output è ovviamente valido solo se i settori riportati si trovano effettivamente nel database.
     * @param input la stringa utilizzata per la ricerca nel database.
     * @return una lista contenente i settori che contengono la stringa data in input.
     */
    public ArrayList<String> doSuggest(String input) {
        try(Connection connection = ConPool.getConnection()) {
            ArrayList<String> suggestions = new ArrayList<>();

            PreparedStatement ps = connection.prepareStatement("SELECT settori " +
                                                                   "FROM Tutor " +
                                                                   "WHERE settori LIKE ?");

            ps.setString(1, "%" + input + "%");// La clausola LIKE necessita l'input avvolto dai caratteri "% %"

            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                String settoriStr = rs.getString(1);// "informatica-biologia-chimica"
                String[] settoriArr = settoriStr.split("-");// ["informatica", "biologia", "chimica"]

                for (String settore : settoriArr)
                    if (settore.contains(input) && !suggestions.contains(settore))// se la stringa settore contiene l'input E se la lista non contiene già il settore
                        suggestions.add(settore);
                // [..., "informatica", "chimica", ...]
            }

            return suggestions;
        }
        catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Il seguente metodo permette di bannare o sbannare il tutor specificato.
     * @param ban il valore booleano in base a cui si banna o sbanna.
     * @param email l'email del tutor coinvolto.
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
     * Il seguente metodo serve a modificare la descrizione completa di un tutor.
     * @param email del tutor di cui va cambiata la descrizione completa.
     * @param nuovaDescrizione la nuova descrizione da inserire.
     */
    public void doEditCompleteDescription(String email, String nuovaDescrizione){
        //ban è true se l'utente viene bannate, false per rimuovere il ban
        try(Connection connection = ConPool.getConnection()){
            PreparedStatement ps = connection.prepareStatement("UPDATE Tutor " +
                                                                   "SET descrizione_completa = ? " +
                                                                   "WHERE Tutor.email_utente = ?");

            ps.setString(1, nuovaDescrizione);
            ps.setString(2, email);

            ps.executeUpdate();
        }
        catch (SQLException ex){
            throw new RuntimeException(ex);
        }
    }


    /**
     * Il seguente metodo serve a modificare la descrizione della card di un tutor.
     * @param email del tutor di cui va cambiata la descrizione della card.
     * @param nuovaDescrizione la nuova descrizione da inserire.
     */
    public void doEditCardDescription(String email, String nuovaDescrizione){
        //ban è true se l'utente viene bannate, false per rimuovere il ban
        try(Connection connection = ConPool.getConnection()){
            PreparedStatement ps = connection.prepareStatement("UPDATE Tutor " +
                                                                   "SET descrizione_card = ? " +
                                                                   "WHERE Tutor.email_utente = ?");

            ps.setString(1, nuovaDescrizione);
            ps.setString(2, email);

            ps.executeUpdate();
        }
        catch (SQLException ex){
            throw new RuntimeException(ex);
        }
    }

    /**
     * Il seguente metodo permette di cambiare nominativo al tutor.
     * @param email l'email del tutor.
     * @param newFirstName il nome nuovo da inserire
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

    /**
     * Il seguente metodo permette di modificare la tariffa oraria del tutor.
     * @param email l'email del tutor.
     * @param days i nuovi giorni del tutor.
     */
    public void doEditDays(String email, String days){
        try(Connection connection = ConPool.getConnection()){
            PreparedStatement ps = connection.prepareStatement("UPDATE Tutor " +
                                                                    "SET giorni = ?" +
                                                                    "WHERE Tutor.email_utente = ?");
            ps.setString(1, days);
            ps.setString(2, email);

            ps.executeUpdate();
        }
        catch (SQLException ex){
            throw new RuntimeException(ex);
        }
    }

    /**
     * Il seguente metodo permette di modificare la tariffa oraria del tutor.
     * @param email l'email del tutor.
     * @param hoursRate la nuova tariffa oraria.
     */
    public void doEditHoursRate(String email, double hoursRate){
        try(Connection connection = ConPool.getConnection()){
            PreparedStatement ps = connection.prepareStatement("UPDATE Tutor " +
                                                                   "SET tariffa_oraria = ?" +
                                                                   "WHERE Tutor.email_utente = ?");
            ps.setDouble(1, hoursRate);
            ps.setString(2, email);

            ps.executeUpdate();
        }
        catch (SQLException ex){
            throw new RuntimeException(ex);
        }
    }

    /**
     * Il seguente metodo permette di modificare la recensione media di un tutor e il numero di recensioni
     * @param email l'email del tutor di cui vanno modificati i parametri
     * @param recensioneMedia la nuova recensione media da settare
     * @param numRecensioni il nuovo numero di recensioni da impostare
     */
    public void doEditRating(String email, double recensioneMedia, int numRecensioni){
        try(Connection connection = ConPool.getConnection()){
            PreparedStatement ps = connection.prepareStatement("UPDATE Tutor " +
                                                                    "SET recensione_media = ?, num_recensioni = ? " +
                                                                    "WHERE Tutor.email_utente = ?");
            ps.setDouble(1, recensioneMedia);
            ps.setInt(2, numRecensioni);
            ps.setString(3, email);

            ps.executeUpdate();
        }
        catch (SQLException ex){
            throw new RuntimeException(ex);
        }
    }
}
