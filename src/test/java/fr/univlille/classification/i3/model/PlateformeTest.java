/*
package fr.univlille.classification.i3.model;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PlateformeTest {
    Plateforme plt = new Plateforme();

    @BeforeAll
    void init(){
        try{
            plt.charger("data/iris.csv");
        }catch(IOException ioe){
            ioe.getStackTrace();
        }
    }

    @Test
    void minIris() {
        assertEquals(4.3,plt.minIris("sepal_length"));
        assertEquals(2,plt.minIris("sepal_width"));
        assertEquals(1,plt.minIris("petal_length"));
        assertEquals(0.1,plt.minIris("petal_width"));
    }

    @Test
    void maxIris() {
        assertEquals(7.9,plt.maxIris("sepal_length"));
        assertEquals(4.4,plt.maxIris("sepal_width"));
        assertEquals(6.9,plt.maxIris("petal_length"));
        assertEquals(2.5,plt.maxIris("petal_width"));
    }
}*/
