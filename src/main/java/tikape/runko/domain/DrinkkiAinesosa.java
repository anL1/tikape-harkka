
package tikape.runko.domain;

public class DrinkkiAinesosa {
    
    private Drinkki drinkki;
    private Ainesosa ainesosa;
    private Integer jarjestys;
    private Integer maara;
    private String ohje;

    public DrinkkiAinesosa(Drinkki drinkki, Ainesosa ainesosa, int jarjestys, int maara, String ohje) {
        this.drinkki = drinkki;
        this.ainesosa = ainesosa;
        this.jarjestys = jarjestys;
        this.maara = maara;
        this.ohje = ohje;
    }
    
    public Integer getJarjestys(){
        return this.jarjestys;
    }
    
    public Integer getMaara(){
        return this.maara;
    }
    
    public String getOhje(){
        return this.ohje;
    }
    
    public Drinkki getDrinkki(){
        return this.drinkki;
    }
    
    public Ainesosa getAinesosa(){
        return this.ainesosa;
    }
    
}
