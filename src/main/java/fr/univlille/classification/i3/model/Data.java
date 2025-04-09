package fr.univlille.classification.i3.model;


import java.util.HashMap;
import java.util.Map;

public class Data {
    String nomClass;
    Map<String,String> attributMap;

    public Data(String nomClass, Map<String, String> attributMap) {
        this.nomClass = nomClass;
        this.attributMap = attributMap;
    }

    public Data(String nomClass){
        this(nomClass, new HashMap<>());
    }

    public Data(){
        this("", new HashMap<>());
    }

    /**
     * @param i String permettant de choisir une catégorie
     * @return La valeur associée à une catégorie "i"
     */
    public String getUneCat(String i){
        return this.attributMap.get(i);
    }

    public Double getUneCatToDouble(String i) throws NumberFormatException{
        try{
            return Double.parseDouble(this.attributMap.get(i));
        }catch (NullPointerException | NumberFormatException ne){
            System.out.println(ne.getMessage());
            return 0.0;
        }
    }

    /**
     * Renvoie le nom de la "classe" des données définit à partir du nom du fichier CSV duquel il provient
     *
     * @return le nom sous forme de String
     */
    public String getNomClass() {
        return nomClass;
    }

    /**
     * Permet de changer le nom de "classe"
     *
     * @param nomClass le nom de la "classe"
     */
    public void setNomClass(String nomClass) {
        this.nomClass = nomClass;
    }

    /**
     *
     * @return la Map
     */
    public Map<String, String> getAttributMap() {
        return attributMap;
    }

    /**
     * Permet de changer la Map
     *
     * @param attributMap la nouvelle Map
     */
    public void setAttributMap(Map<String, String> attributMap) {
        this.attributMap = attributMap;
    }

    public void setAttributMap(String key, String val){
        this.attributMap.put(key, val);
    }

    @Override
    public String toString() {
        return "Data{" +
                "nomClass='" + nomClass + '\'' +
                ", attributMap=" + attributMap +
                '}';
    }
}
