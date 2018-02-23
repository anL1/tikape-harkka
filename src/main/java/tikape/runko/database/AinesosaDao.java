
package tikape.runko.database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import tikape.runko.domain.*;

public class AinesosaDao implements Dao<Ainesosa, Integer>{
    
    private Database database;

    public AinesosaDao(Database database) {
        this.database = database;
    }
    @Override
    public Ainesosa findOne(Integer key) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Ainesosa WHERE id = ?");
        stmt.setObject(1, key);

        ResultSet rs = stmt.executeQuery();
        boolean hasOne = rs.next();
        if (!hasOne) {
            return null;
        }

        Integer id = rs.getInt("id");
        String nimi = rs.getString("nimi");

        Ainesosa ainesosa = new Ainesosa(id, nimi);

        rs.close();
        stmt.close();
        connection.close();

        return ainesosa;
    }

    @Override
    public List<Ainesosa> findAll() throws SQLException {

        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Ainesosa");

        ResultSet rs = stmt.executeQuery();
        List<Ainesosa> ainesosat = new ArrayList<>();
        while (rs.next()) {
            Integer id = rs.getInt("id");
            String nimi = rs.getString("nimi");

            ainesosat.add(new Ainesosa(id, nimi));
        }

        rs.close();
        stmt.close();
        connection.close();

        return ainesosat;
    }
    public Ainesosa save(Ainesosa ainesosa) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement tarkistaAinesosa = connection.prepareStatement("SELECT * FROM Ainesosa "
                + "WHERE nimi = ?");
        tarkistaAinesosa.setString(1, ainesosa.getNimi());
        ResultSet tarkistus = tarkistaAinesosa.executeQuery();
        boolean loytyyko = tarkistus.next();
        if(loytyyko == false) {
            tarkistaAinesosa.close();
            tarkistus.close();
            
            PreparedStatement stmt = connection.prepareStatement("INSERT INTO Ainesosa (nimi) VALUES (?)");
            stmt.setString(1, ainesosa.getNimi());
            stmt.executeUpdate();
            stmt.close();
            
        }

        connection.close();
        return null;
    }

    @Override
    public void delete(Integer key) throws SQLException {
        Connection conn = database.getConnection();
        PreparedStatement stmt = conn.prepareStatement("DELETE FROM Ainesosa WHERE id = ?");
        
        stmt.setInt(1, key);
        stmt.executeUpdate();

        stmt.close();
        conn.close();
    }
    
}
