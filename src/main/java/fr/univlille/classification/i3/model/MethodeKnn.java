package fr.univlille.classification.i3.model;
import fr.univlille.classification.i3.enums.Distance;
import java.util.*;

public class MethodeKnn {

    /**
     * Sélectionne les k plus proches voisins du point d et leur associe la distance
     * @param k nombre de voisins à comparer avec la Data d
     * @param d Data sur laquelle est appliqué l'algorithme k-NN
     * @param dist Distance avec laquelle comparer les Data
     * @param datas ensemble du catalogue de données
     * @param cat1 première catégorie de tri
     * @param cat2 deuxième catégorie de tri
     * @return la liste des data associée à la distance du point à comparer
     */
    private static List<Map.Entry<Data, Double>> plusProchesVoisins(int k, Data d, Distance dist, List<Data> datas, String cat1, String cat2, Plateforme plt){
        List<Map.Entry<Data, Double>> distEntreDatas = new ArrayList<>();
        for (Data d2 : datas){
            double valCat1D = d.getUneCatToDouble(cat1);
            double valCat1D2 = d2.getUneCatToDouble(cat1);
            double valCat2D = d.getUneCatToDouble(cat2);
            double valCat2D2 = d2.getUneCatToDouble(cat2);
            distEntreDatas.add(new AbstractMap.SimpleEntry<>(d2, dist.distance(valCat1D,valCat1D2,valCat2D,valCat2D2, plt.getMinMax(cat1), plt.getMinMax(cat2))));
        }
        distEntreDatas.sort(Map.Entry.comparingByValue());
        return distEntreDatas.subList(0, k);
    }

    /**
     * Prédit une catégorie pour le point d en fonction de k plus proches voisins
     * @param k nombre de voisins à comparer avec la Data d
     * @param d Data sur laquelle est appliqué l'algorithme k-NN
     * @param dist Distance avec laquelle comparer les Data
     * @param datas ensemble du catalogue de données
     * @param attributChoisiPourKNN nom de l'attribut choisi sur lequel appliquer k-NN ainsi que toutes ses valeurs possibles
     * @param plt plateforme contenant les catégories et le min et max de chaque valeur
     * @return un string représentant la catégorie du point
     */
    public static String predictionCategory(int k, Data d, Distance dist, List<Data> datas, String attributChoisiPourKNN, Plateforme plt)throws NoSuchElementException{
        String cat1 = plt.getCurrentCat1();
        String cat2 = plt.getCurrentCat2();

        Map<String, Double> dataPoints = new HashMap<>();
        //System.out.println(attributChoisiPourKNN);
        for (String category : plt.getEnums().get(attributChoisiPourKNN)){
            dataPoints.put(category , (double) 0);
        }
        List<Map.Entry<Data, Double>> prochesVoisins = plusProchesVoisins(k,d,dist, datas, cat1, cat2, plt);
        for (Map.Entry<Data, Double> distance : prochesVoisins) {
            String category = getCategory(distance, attributChoisiPourKNN);
            //System.out.println(category);
            double distTotalTmp = dataPoints.get(category)+distance.getValue();
            dataPoints.replace(distance.getKey().getAttributMap().get(attributChoisiPourKNN), distTotalTmp/2);
        }
        dataPoints.entrySet().removeIf(entry -> entry.getValue() == 0);
        return Collections.min(dataPoints.entrySet(), Map.Entry.comparingByValue()).getKey();
    }

    /** Récupère la catégorie du point Data
     * @param distance relation entre le point Data et sa distance avec le point sur lequel on utilise k-NN
     * @param attributChoisiPourKNN nom de l'attribut choisi pour mettre le k-NN
     * @return la catégorie du point Data
     */
    private static String getCategory(Map.Entry<Data, Double> distance, String attributChoisiPourKNN){
        //System.out.println(distance.getKey().getAttributMap());
        return distance.getKey().getAttributMap().get(attributChoisiPourKNN);
    }


}
