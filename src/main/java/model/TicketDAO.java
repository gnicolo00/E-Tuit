package model;
//YYYY-MM-DD
import java.sql.*;
import java.util.ArrayList;

public class TicketDAO {
    /**
     * Restituisce il ticket con l'ID specificato.
     * @param id l'ID del ticket
     * @return il ticket corrispondente (null se inesistente)
     */
    public Ticket doRetrieveByID(int id) {
        try (Connection connection = ConPool.getConnection()) {
            //Query per ottenere le informazioni su un ticket, in base all'id fornito.
            PreparedStatement ps = connection.prepareStatement("SELECT tipo_richiesta, oggetto, descrizione, data_invio, email_utente " +
                                                                    "FROM Ticket, Utente " +
                                                                    "WHERE Ticket.email_utente = Utente.email " +
                                                                    "AND Ticket.ID = ?");
            ps.setInt(1, id);

            /*
            Creo, per la tupla identificata con quell'ID, un nuovo bean Ticket in cui
            inserisco le informazioni ottenute dalla query.
            */
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Ticket ticket = new Ticket();
                ticket.setID(id);
                ticket.setTipoRichiesta(rs.getString(1));
                ticket.setOggetto(rs.getString(2));
                ticket.setDescrizione(rs.getString(3));
                java.util.Date data = new java.util.Date(rs.getDate(4).getTime()); //conversione della data
                ticket.setDataInvio(data);
                ticket.setEmailUtente(rs.getString(5));
                return ticket;
            }
            else return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Salva nel database il ticket specificato.
     * @param ticket il bean del ticket da salvare
     */
    public void doSave(Ticket ticket) {
        try (Connection connection = ConPool.getConnection()) {
            //Inserimento nella tabella Ticket.
            PreparedStatement ps = connection.prepareStatement("INSERT INTO Ticket (tipo_richiesta, oggetto, descrizione, data_invio, email_utente) VALUES(?,?,?,?,?)",
                                                                    Statement.RETURN_GENERATED_KEYS);

            //Set dei campi da inserire, prelevandoli dall'oggetto tutor.
            ps.setString(1, ticket.getTipoRichiesta());
            ps.setString(2, ticket.getOggetto());
            ps.setString(3, ticket.getDescrizione());
            //Conversione della data da oggetto java.util.Date a oggetto java.sql.Date.
            java.sql.Date data = new java.sql.Date(ticket.getDataInvio().getTime());
            ps.setDate(4, data);
            ps.setString(5, ticket.getEmailUtente());

            ps.executeUpdate();

            //Salvo l'ID prodotto dal database nel ticket.
            ResultSet rs = ps.getGeneratedKeys();
            if(rs.next())
                ticket.setID(rs.getInt(1));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Aggiorna l'istanza di un ticket nel database.
     * @param ticket il bean del ticket da modificare
     */
    public void doUpdateTicket(Ticket ticket){
        try(Connection connection = ConPool.getConnection()){
            //Aggiornamento all'interno della tabella Ticket di tutti i campi, escluso ID.
            PreparedStatement ps = connection.prepareStatement("UPDATE Ticket "+
                                                                   "SET oggetto=?, descrizione=?, data_invio=?, email_utente=? " +
                                                                   "WHERE ID=?");

            ps.setString(1, ticket.getOggetto());
            ps.setString(2, ticket.getDescrizione());
            java.sql.Date data = new java.sql.Date(ticket.getDataInvio().getTime());
            ps.setDate(3, data);
            ps.setString(4, ticket.getEmailUtente());
            ps.executeUpdate();
        } catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    /**
     * Il metodo permette di ottenere tutti i tickets conservati nel database.
     * @return la lsita che contiene tutti i tickets.
     */
    public ArrayList<Ticket> doRetrieveAll(){
        try(Connection connection = ConPool.getConnection()){
            ArrayList<Ticket> list = new ArrayList<>();
            //Query per selezionare tutti i campi di tutti i ticket.
            PreparedStatement ps = connection.prepareStatement("SELECT ID, tipo_richiesta, oggetto, descrizione, data_invio, email_utente FROM Ticket");
            ResultSet rs = ps.executeQuery();

            //Creo una lista e per ogni tupla creo nuovo oggetto ticket, salvando le informazioni ottenute dalla query.
            while (rs.next()) {
                Ticket ticket = new Ticket();
                ticket.setID(rs.getInt(1));
                ticket.setTipoRichiesta(rs.getString(2));
                ticket.setOggetto(rs.getString(3));
                ticket.setDescrizione(rs.getString(4));
                //conversione delle date
                java.util.Date data = new java.util.Date(rs.getDate(5).getTime());
                ticket.setDataInvio(data);
                ticket.setEmailUtente(rs.getString(6));
                list.add(ticket);
            }
            return list;
        } catch (SQLException e){
            throw new RuntimeException(e);
        }
    }
}
