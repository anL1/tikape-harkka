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
        Database database = new Database("jdbc:sqlite:drinkki.db");
//        database.init();

        DrinkkiDao drinkkiDao = new DrinkkiDao(database);
        AinesosaDao ainesosaDao = new AinesosaDao(database);
        DrinkkiAinesosaDao drinkkiAinesosaDao = new DrinkkiAinesosaDao(database, drinkkiDao, ainesosaDao);

        post("/ainesosat", (req, res) -> {
            ainesosaDao.save(new Ainesosa(null,req.queryParams("name")));
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
            if(onko){
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
            drinkkiDao.delete(Integer.parseInt(req.params("id")));
            drinkkiAinesosaDao.delete(Integer.parseInt(req.params("id")));
            
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
            
            return new ModelAndView(map, "drinkit");
        }, new ThymeleafTemplateEngine());
        
        post("/drinkit", (req, res) -> {
            drinkkiDao.save(new Drinkki(null,req.queryParams("name")));
            res.redirect("/drinkit");
            return "";
        });

        get("/drinkit/:id", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("drinkki", drinkkiDao.findOne(Integer.parseInt(req.params("id"))));
            map.put("drinkinAinesosat", drinkkiAinesosaDao.findAll(Integer.parseInt(req.params("id"))));

            return new ModelAndView(map, "drinkki");
        }, new ThymeleafTemplateEngine());
    }
}
