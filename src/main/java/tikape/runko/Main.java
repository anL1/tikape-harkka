package tikape.runko;

import java.util.HashMap;
import spark.ModelAndView;
import static spark.Spark.*;
import spark.template.thymeleaf.ThymeleafTemplateEngine;
import tikape.runko.database.*;
import tikape.runko.domain.Ainesosa;

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

        get("/drinkit/:id", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("drinkki", drinkkiDao.findOne(Integer.parseInt(req.params("id"))));
            map.put("drinkinAinesosat", drinkkiAinesosaDao.findAll(Integer.parseInt(req.params("id"))));

            return new ModelAndView(map, "drinkki");
        }, new ThymeleafTemplateEngine());
    }
}
