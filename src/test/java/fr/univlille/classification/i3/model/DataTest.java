package fr.univlille.classification.i3.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class DataTest {
    Data Data1;
    Data Data2;

    @BeforeAll
    public void setUp(){
        Map<String, String> valsData1 = new HashMap<>();
        valsData1.put("sepal.length","5.1");
        valsData1.put("sepal.width", "3.5");
        valsData1.put("petal.length","1.4");
        valsData1.put("petal.width",".2");
        valsData1.put("variety", "Setosa");
        Map<String, String> valsData2 = new HashMap<>();
        Data1 = new Data("versicolor",valsData1);
        Data2 = new Data("setosa", valsData2);
    }
    @Test
    void getUneCat() {
        Assertions.assertEquals(5.1, Data1.getUneCatToDouble("sepal.length"));
        Assertions.assertEquals(3.5, Data1.getUneCatToDouble("sepal.width"));
        Assertions.assertEquals(1.4, Data1.getUneCatToDouble("petal.length"));
        Assertions.assertEquals(0.2, Data1.getUneCatToDouble("petal.width"));
    }

    @Test
    void testToString() {
        // 1er cas avec l'Data1
        String s = "Data{categories={sepal_length=6.0, sepal_width=5.0, petal_length=0.0, petal_width=6.0}, variety=Versicolor}";
        Assertions.assertEquals(s, Data1.toString());

        // 2ème cas avec l'Data2
        s = "Data{categories={sepal_length=0.0, sepal_width=0.0, petal_length=0.0, petal_width=0.0}, variety=Setosa}";
        Assertions.assertEquals(s, Data2.toString());
    }
}
