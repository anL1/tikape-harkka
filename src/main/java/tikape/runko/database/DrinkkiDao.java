/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tikape.runko.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import tikape.runko.domain.Drinkki;

public class DrinkkiDao implements Dao<Drinkki, Integer> {

    private Database database;

    public DrinkkiDao(Database database) {
        this.database = database;
    }

    @Override
    public Drinkki findOne(Integer key) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Drinkki WHERE id = ?");
        stmt.setObject(1, key);

        ResultSet rs = stmt.executeQuery();
        boolean hasOne = rs.next();
        if (!hasOne) {
            return null;
        }

        Integer id = rs.getInt("id");
        String nimi = rs.getString("nimi");

        Drinkki drinkki = new Drinkki(id, nimi);

        rs.close();
        stmt.close();
        connection.close();

        return drinkki;
    }

    @Override
    public List<Drinkki> findAll() throws SQLException {

        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Drinkki");

        ResultSet rs = stmt.executeQuery();
        List<Drinkki> drinkit = new ArrayList<>();
        while (rs.next()) {
            Integer id = rs.getInt("id");
            String nimi = rs.getString("nimi");

            drinkit.add(new Drinkki(id, nimi));
        }

        rs.close();
        stmt.close();
        connection.close();

        return drinkit;
    }

    @Override
    public void delete(Integer key) throws SQLException {
        Connection conn = database.getConnection();
        PreparedStatement stmt = conn.prepareStatement("DELETE FROM Drinkki WHERE id = ?");
        
        stmt.setInt(1, key);
        stmt.executeUpdate();

        stmt.close();
        conn.close();
    }
    public Drinkki save(Drinkki drinkki) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement tarkistaAinesosa = connection.prepareStatement("SELECT * FROM Drinkki "
                + "WHERE nimi = ?");
        tarkistaAinesosa.setString(1, drinkki.getNimi());
        ResultSet tarkistus = tarkistaAinesosa.executeQuery();
        boolean loytyyko = tarkistus.next();
        if(loytyyko == false) {
            tarkistaAinesosa.close();
            tarkistus.close();
            
            PreparedStatement stmt = connection.prepareStatement("INSERT INTO Drinkki (nimi) VALUES (?)");
            stmt.setString(1, drinkki.getNimi());
            stmt.executeUpdate();
            stmt.close();
            
        }

        connection.close();
        return null;
    }

}
