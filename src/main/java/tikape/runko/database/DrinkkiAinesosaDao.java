package tikape.runko.database;

import java.sql.SQLException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import tikape.runko.domain.*;

public class DrinkkiAinesosaDao {

    private Database database;
    private Dao<Drinkki, Integer> Ddao;
    private Dao<Ainesosa, Integer> Adao;

    public DrinkkiAinesosaDao(Database database, DrinkkiDao ddao, AinesosaDao adao) {
        this.database = database;
        this.Adao = adao;
        this.Ddao = ddao;
    }

//    @Override
//    public DrinkkiAinesosa findOne(Integer key) throws SQLException {
//        //tee jotain
//        
//        return null;
//    }
    public List<DrinkkiAinesosa> findAll(Integer id) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM DrinkkiAinesosa "
                + "Where drinkki_id = ?");
        stmt.setObject(1, id);

        ResultSet rs = stmt.executeQuery();
        List<DrinkkiAinesosa> drinkinainesosat = new ArrayList<>();

        while (rs.next()) {
            Integer drinkki_id = rs.getInt("drinkki_id");
            Integer ainesosa_id = rs.getInt("ainesosa_id");
            Integer jarjestys = rs.getInt("jarjestys");
            Integer maara = rs.getInt("maara");
            String ohje = rs.getString("ohje");

            Drinkki drinkki = Ddao.findOne(drinkki_id);
            Ainesosa ainesosa = Adao.findOne(ainesosa_id);

            drinkinainesosat.add(new DrinkkiAinesosa(drinkki, ainesosa, jarjestys, maara, ohje));
        }

        rs.close();
        stmt.close();
        connection.close();

        return drinkinainesosat;
    }

    public DrinkkiAinesosa save(DrinkkiAinesosa drinkkiainesosa) throws SQLException {
        Connection connection = database.getConnection();

        PreparedStatement stmt = connection.prepareStatement("INSERT INTO DrinkkiAinesosa "
                + "(drinkki_id, ainesosa_id, jarjestys, maara, ohje)"
                + " VALUES (?, ?, ?, ?, ?)");
        stmt.setInt(1, drinkkiainesosa.getDrinkki().getId());
        stmt.setInt(2, drinkkiainesosa.getAinesosa().getId());
        stmt.setInt(3, drinkkiainesosa.getJarjestys());
        stmt.setInt(4, drinkkiainesosa.getMaara());
        stmt.setString(5, drinkkiainesosa.getOhje());
        stmt.executeUpdate();
        stmt.close();

        connection.close();
        return null;
    }

    public void delete(Integer key) throws SQLException {
        Connection conn = database.getConnection();
        PreparedStatement stmt = conn.prepareStatement("DELETE FROM DrinkkiAinesosa WHERE drinkki_id = ?");

        stmt.setInt(1, key);
        stmt.executeUpdate();

        stmt.close();
        conn.close();
    }

}
