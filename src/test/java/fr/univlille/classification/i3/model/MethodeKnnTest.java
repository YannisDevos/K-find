package fr.univlille.classification.i3.model;

import fr.univlille.classification.i3.enums.DistanceEuclidienne;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MethodeKnnTest {
    Plateforme plt;
    Data d;
    String attributKNN;
    @BeforeEach
    public void setUp(){
        Plateforme plt = new Plateforme();
        d = new Data("iris");
        Map<String,String> values = new HashMap<>();
        values.put("sepal.length","5.1");
        values.put("sepal.width", "3.5");
        values.put("petal.length","1.4");
        values.put("petal.width",".2");
        d.setAttributMap(values);
        try{
            CsvReader.charger("./data/iris.csv",plt);
            attributKNN = "variety";
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testPlusProchesVoisins(){
        assertThrows(NoSuchElementException.class,()->MethodeKnn.predictionCategory(0,d, new DistanceEuclidienne(),plt.getDatas(),attributKNN,plt));
        assertEquals("setosa",MethodeKnn.predictionCategory(1,d, new DistanceEuclidienne(),plt.getDatas(),attributKNN,plt));
    }

}
