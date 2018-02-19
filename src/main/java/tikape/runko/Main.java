package tikape.runko;

import java.util.HashMap;
import spark.ModelAndView;
import static spark.Spark.*;
import spark.template.thymeleaf.ThymeleafTemplateEngine;
import tikape.runko.database.AinesosaDao;
import tikape.runko.database.Database;
import tikape.runko.database.DrinkkiDao;

public class Main {

    public static void main(String[] args) throws Exception {
        Database database = new Database("jdbc:sqlite:drinkki.db");
//        database.init();

        DrinkkiDao drinkkiDao = new DrinkkiDao(database);
        AinesosaDao ainesosaDao = new AinesosaDao(database);

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

            return new ModelAndView(map, "drinkki");
        }, new ThymeleafTemplateEngine());
    }
}
