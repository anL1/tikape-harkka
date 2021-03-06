package tikape.runko;

import java.sql.*;
import java.util.HashMap;
import spark.ModelAndView;
import static spark.Spark.*;
import spark.template.thymeleaf.ThymeleafTemplateEngine;
import tikape.runko.database.*;
import tikape.runko.domain.*;

public class Main {

    public static void main(String[] args) throws Exception {

        if (System.getenv("PORT") != null) {
            port(Integer.valueOf(System.getenv("PORT")));
        }
        
        
        Database database = new Database("jdbc:sqlite:drinkki.db");
//        database.init();

        DrinkkiDao drinkkiDao = new DrinkkiDao(database);
        AinesosaDao ainesosaDao = new AinesosaDao(database);
        DrinkkiAinesosaDao drinkkiAinesosaDao = new DrinkkiAinesosaDao(database, drinkkiDao, ainesosaDao);

        post("/ainesosat", (req, res) -> {
            ainesosaDao.save(new Ainesosa(null, req.queryParams("name")));
            res.redirect("/ainesosat");
            return "";
        });

        get("/error", (req, res) -> {
            HashMap map = new HashMap();
            map.put("error", null);

            return new ModelAndView(map, "error");
        }, new ThymeleafTemplateEngine());

        post("/ainesosat/poista/:id", (req, res) -> {
            Connection c = database.getConnection();
            PreparedStatement stmt = c.prepareStatement("SELECT * FROM DrinkkiAinesosa "
                    + "WHERE ainesosa_id = ?");
            stmt.setInt(1, Integer.parseInt(req.params("id")));
            ResultSet rs = stmt.executeQuery();
            boolean onko = rs.next();
            if (onko) {
                rs.close();
                stmt.close();
                c.close();
                res.redirect("/error");
                return "";
            }

            ainesosaDao.delete(Integer.parseInt(req.params("id")));
            res.redirect("/ainesosat");
            return "";
        });

        post("/drinkit/poista/:id", (req, res) -> {
            drinkkiAinesosaDao.delete(Integer.parseInt(req.params("id")));
            drinkkiDao.delete(Integer.parseInt(req.params("id")));

            res.redirect("/drinkit");
            return "";
        });

        get("/", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("viesti", "Tervetuloa drinkkisovellukseen!");
            map.put("drinkit", drinkkiDao.findAll());

            return new ModelAndView(map, "index");
        }, new ThymeleafTemplateEngine());

        get("/ainesosat", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("ainesosat", ainesosaDao.findAll());

            return new ModelAndView(map, "ainesosat");
        }, new ThymeleafTemplateEngine());

        get("/drinkit", (req, res) -> {
            HashMap map = new HashMap();
            map.put("drinkit", drinkkiDao.findAll());
            map.put("ainesosat", ainesosaDao.findAll());

            return new ModelAndView(map, "drinkit");
        }, new ThymeleafTemplateEngine());

        post("/drinkit", (req, res) -> {
            drinkkiDao.save(new Drinkki(null, req.queryParams("name")));
            res.redirect("/drinkit");
            return "";
        });
            
        //statistiikka ei toimi herokussa, joten kommentteihin
//        get("/statistiikka", (req, res) -> {
//            HashMap map = new HashMap<>();
//            Connection connection = database.getConnection();
//            PreparedStatement statement = connection.prepareStatement("SELECT COUNT(*) FROM Drinkki");
//            ResultSet rs = statement.executeQuery();
//
//            Integer maara = rs.getInt("count(*)");
//
//            rs.close();
//            statement.close();
//            connection.close();
//
//            map.put("DrinkkienMaara", maara);
//
//            return new ModelAndView(map, "statistiikka");
//        }, new ThymeleafTemplateEngine());

        get("/error2", (req, res) -> {
            HashMap map = new HashMap();
            map.put("error", null);

            return new ModelAndView(map, "error2");
        }, new ThymeleafTemplateEngine());

        post("/lisaadrinkki", (req, res) -> {
            try {
                Drinkki drinkki = drinkkiDao.findOne(Integer.parseInt(req.queryParams("drinkkinimi")));
                Ainesosa ainesosa = ainesosaDao.findOne(Integer.parseInt(req.queryParams("ainesosanimi")));
                Integer jarjestys = Integer.parseInt(req.queryParams("jarjestys"));
                Integer maara = Integer.parseInt((req.queryParams("maara")));
                String ohje = req.queryParams("ohje");

                System.out.println(ohje);

                drinkkiAinesosaDao.save(new DrinkkiAinesosa(drinkki, ainesosa, jarjestys, maara, ohje));

                res.redirect("/drinkit");
                return "";
            } catch (Exception e) {
                res.redirect("/error2");
                return "";
            }
        });

        get("/drinkit/:id", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("drinkki", drinkkiDao.findOne(Integer.parseInt(req.params("id"))));
            map.put("drinkinAinesosat", drinkkiAinesosaDao.findAll(Integer.parseInt(req.params("id"))));

            return new ModelAndView(map, "drinkki");
        }, new ThymeleafTemplateEngine());
    }
}
