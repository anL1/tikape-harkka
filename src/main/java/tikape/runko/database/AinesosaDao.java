
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
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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

    @Override
    public void delete(Integer key) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
