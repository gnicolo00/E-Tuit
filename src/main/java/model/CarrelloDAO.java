package model;

import java.sql.*;
import java.util.ArrayList;

public class CarrelloDAO {
    /**
     * Lo scopo del seguente metodo è di ottenere il carrello di un determinato studente.
     * @param ownerEmail l'email del proprietario del carrello.
     * @return il bean del carrello.
     */
    public Carrello doRetrieveByOwner(String ownerEmail) {
        try(Connection con = ConPool.getConnection()) {
            PreparedStatement ps1 = con.prepareStatement("SELECT email_proprietario, carico " +
                                                             "FROM Carrello " +
                                                             "WHERE email_proprietario = ?");// query per ottenere il carrello

            PreparedStatement ps2 = con.prepareStatement("SELECT ID, materia, ore, prezzo, email_tutor, email_acquirente, nome, cognome " +
                                                             "FROM Lezione, Carrello, Tutor, Utente " +
                                                             "WHERE Lezione.email_acquirente = Carrello.email_proprietario " +
                                                             "AND Lezione.email_tutor = Tutor.email_utente " +
                                                             "AND Tutor.email_utente = Utente.email " +
                                                             "AND Lezione.isPrenotata = 0 " +
                                                             "AND Carrello.email_proprietario = ?");// query per ottenere le lezioni contenute nel carrello

            ps1.setString(1,ownerEmail);
            ps2.setString(1,ownerEmail);
            ResultSet rs1 = ps1.executeQuery();
            ResultSet rs2 = ps2.executeQuery();



            if(rs1.next()) {
                Carrello carrello = new Carrello();
                carrello.setEmailProprietario(ownerEmail);
                ArrayList<Lezione> listaLezioni = new ArrayList<>();
                int carico = 0;
                while(rs2.next()) {
                    Lezione lezione = new Lezione();
                    lezione.setID(rs2.getInt(1));
                    lezione.setMateria(rs2.getString(2));
                    lezione.setOre(rs2.getInt(3));
                    lezione.setPrezzo(rs2.getInt(4));
                    lezione.setEmailTutor(rs2.getString(5));
                    lezione.setEmailAcquirente(rs2.getString(6));
                    lezione.setNomeTutor(rs2.getString(7) + " " + rs2.getString(8));
                    lezione.setPrenotata(false);
                    listaLezioni.add(lezione);
                    carico++;
                }
                carrello.setCarico(carico);
                carrello.setListaLezioni(listaLezioni);

                return carrello;
            }
            else
                return null;
        }
        catch(SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Il seguente metodo ha l'obiettivo di creare e salvare nel database il carrello relativo a uno specifico studente.
     * @param carrello il bean del carrello da salvare.
     */
    public void doSave(Carrello carrello) {
        try(Connection con = ConPool.getConnection()) {
            PreparedStatement ps = con.prepareStatement("INSERT INTO Carrello (email_proprietario, carico) VALUES (?, ?)");

            ps.setString(1, carrello.getEmailProprietario());
            ps.setInt(2, carrello.getCarico());
            ps.executeUpdate();
        } catch(SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Il seguente metodo ha lo scopo di aggiornare il carico del carrello.
     * @param carrello Il bean del carrello già aggiornato
     */
    public void doUpdateCarico(Carrello carrello){
        try(Connection con = ConPool.getConnection()) {
            PreparedStatement ps = con.prepareStatement("UPDATE Carrello " +
                                                            "SET carico =? " +
                                                            "WHERE email_proprietario = ?");
            ps.setInt(1, carrello.getCarico());
            ps.setString(2, carrello.getEmailProprietario());
            ps.executeUpdate();
        } catch(SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Lo scopo del seguente metodo è di aggiungere una lezione nel carrello.
     * @param lezione il bean della lezione da aggiungere.
     */
    public void doAddLezione(Lezione lezione) {
        try(Connection connection = ConPool.getConnection()) {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO Lezione (materia, ore, prezzo, email_tutor, email_acquirente, isPrenotata, ID_ord) VALUES(?, ?, ?, ?, ?, 0, null)"
                                                                   ,Statement.RETURN_GENERATED_KEYS);
            ps.setString(1,lezione.getMateria());
            ps.setInt(2,lezione.getOre());
            ps.setDouble(3,lezione.getPrezzo());
            ps.setString(4,lezione.getEmailTutor());
            ps.setString(5,lezione.getEmailAcquirente());

            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if(rs.next())
                lezione.setID(rs.getInt(1));

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Lo scopo del seguente metodo è di rimuovere una lezione dal carrello.
     * @param lezione Il bean della lezione da rimuovere.
     */
    public void doRemoveLezione(Lezione lezione) {
        try(Connection connection = ConPool.getConnection()) {
            PreparedStatement ps = connection.prepareStatement("DELETE FROM Lezione " +
                                                                   "WHERE ID = ?");

            ps.setInt(1,lezione.getID());

            ps.executeUpdate();
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Lo scopo del seguente metodo è di svuotare l'intero carrello.
     * @param ownerEmail l'email del proprietario del carrello.
     */
    public void doClear(String ownerEmail) {
        try(Connection connection = ConPool.getConnection()) {
            PreparedStatement ps = connection.prepareStatement("UPDATE Lezione SET ID_ord=?");

            ps.setString(1,ownerEmail);

            ps.executeUpdate();
            Carrello carrello = this.doRetrieveByOwner(ownerEmail);
            carrello.svuota();
            this.doUpdateCarico(carrello);// azzera il carico del carrello dopo averlo svuotato.
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}