package fr.univlille.classification.i3.model;

import fr.iut.r304.utils.Observable;
import fr.univlille.classification.i3.enums.*;
import fr.univlille.classification.i3.view.ScatterView;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Plateforme extends Observable {
    private String[] currentCatTab;
    private List<Data> datas;
    private List<Data> nouveauPoints;
    private String kNNAble;
    private Distance distance;
    private List<String> cat;
    private File file;
    private Map<String, Set<String>> enums;

    /**
     * Permet de créer une Plateforme
     */
    public Plateforme() {
        this.datas = new ArrayList<>();
        this.nouveauPoints = new ArrayList<>();
        this.currentCatTab = new String[2];
        this.cat = new ArrayList<>();
    }

    /**
     * Permet de charger les données et l' "enum" permettant plus de généricité
     *
     * @param filePath le chemin du fichier
     * @throws IOException peut renvoyer une IOException
     */
    public void charger(String filePath) throws IOException {
        this.file = new File(filePath);
        this.enums = CsvReader.checkCSV(filePath);

        try {
            CsvReader.charger(filePath,this);
        }catch (IOException ioe){
            ioe.getStackTrace();
        }
    }

    /**
     * Prend en paramètre un élément et l'ajoute à une liste modélisant les points de la ScatterChart
     *
     * @param nData la donnée à ajouter
     */
    public void ajouterPoint(Data nData){

        if(nData.getUneCat(kNNAble)==null || nData.getUneCat(kNNAble).isEmpty()){
            nData.setAttributMap(kNNAble, MethodeKnn.predictionCategory(20, nData, distance, datas, kNNAble,this));
        }

        nouveauPoints.add(nData);
        notifyObservers(nData);
    }

    /**
     * @return La liste des Datas
     */
    public List<Data> getDatas() {
        return this.datas;
    }


    /**
     * @return la liste des catégories
     */
    public List<String> getCat(){
        return this.cat;
    }

    /**
     * @return la liste des enums
     */
    public Map<String, Set<String>> getEnums() {
        return enums;
    }

    /**
     * @return La pile de nouvelles données que l'utilisateur ajoute au Scatter
     */
    public List<Data> getNewData(){
        return this.nouveauPoints;
    }

    public void setDatas(List<Data> datas) {
        this.datas = datas;
    }

    public void setCat(List<String> cat) {
        this.cat = cat;
    }

    /**
     * Renvoie la plus petite donnée en fonction de la catégorie
     *
     * @param param Catégorie souhaitée
     * @return Le double le plus petit de la liste de données
     */
    public double minData(String param) {
        double current = Integer.MAX_VALUE;
        List<Data> tmp = new ArrayList<>();
        tmp.addAll(this.datas);
        tmp.addAll(this.nouveauPoints);
        for (Data i :tmp) {
            if (i.getUneCatToDouble(param) < current) {
                current = i.getUneCatToDouble(param);
            }
        }

        return current;
    }

    /**
     * Renvoie la plus grande donnée en fonction de la catégorie
     *
     * @param param Catégorie souhaitée
     * @return Le double le plus grand de la liste de données
     */
    public double maxData(String param) {
        double current = Integer.MIN_VALUE;
        List<Data> tmp = new ArrayList<>();
        tmp.addAll(this.datas);
        tmp.addAll(this.nouveauPoints);
        for (Data i : tmp) {
            if (i.getUneCatToDouble(param) > current) {
                current = i.getUneCatToDouble(param);
            }
        }

        return current;
    }
    public double[] getMinMax(String cat) {
        double[] res = new double[2];
        res[0] = minData(cat);
        res[1] = maxData(cat);
        return res;
    }

    public String getkNNAble() {
        return kNNAble;
    }

    public void setkNNAble(String kNNAble) {
        this.kNNAble = kNNAble;
    }

    public Distance getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        switch (distance) {
            case "Euclidienne" -> this.distance = new DistanceEuclidienne();
            case "Manhattan" -> this.distance = new DistanceManhattan();
            case "Euclidienne Normalisée" -> this.distance = new DistanceEuclidienneNormalisee();
            case "Manhattan Normalisée" -> this.distance = new DistanceManhattanNormalisee();
        }
    }

    /**
     * Créer une nouvelle ScatterView et l'attache
     */
    public void newView(){
        ScatterView sc = new ScatterView(this);
        this.attach(sc);
    }

    /**
     *
     * @return le file de la plateforme
     */
    public File getFile(){
        return this.file;
    }

    /**
     * Permet de remplacer le File
     *
     * @param newFile le nouveau file de type File
     */
    public void setFile(File newFile){
        this.file = newFile;
    }

    /**
     * Modifie le tableau des catégories d'Iris
     * @param catTab Un tableau de catégorie d'Iris
     */
    public void setCurrentCatTab(String[] catTab) {
        this.currentCatTab = catTab;
    }

    /**
     * Modifier la 1ʳᵉ valeur du tableau de catégorie d'Iris
     * @param cat La nouvelle valeur
     */
    public void setCurrentCat1(String cat){
        this.currentCatTab[0] = cat;
    }

    /**
     * Modifier la 2ᵉ valeur du tableau de catégorie d'Iris
     * @param cat La nouvelle valeur
     */
    public void setCurrentCat2(String cat){
        this.currentCatTab[1] = cat;
    }

    /**
     * @return Le tableau des catégories d'Iris
     */
    public String[] getCurrentCat() {
        return currentCatTab;
    }

    /**
     * @return La 1ʳᵉ valeur du tableau des catégories d'Iris
     */
    public String getCurrentCat1(){
        return this.currentCatTab[0];
    }

    /**
     * @return La 2ᵉ valeur du tableau des catégories d'Iris
     */
    public String getCurrentCat2(){
        return this.currentCatTab[1];
    }


}