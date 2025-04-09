package fr.univlille.classification.i3.enums;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class DistanceTest {
    @Test
    public void testDistanceNonNormalisee(){
        DistanceManhattan dM = new DistanceManhattan();
        DistanceEuclidienne dE = new DistanceEuclidienne();
        assertEquals(2, dM.distance(1.0,2.0,1.0,2.0, new double[]{}, new double[]{}));
        assertEquals(Math.sqrt(2),dE.distance(3.0,4.0,3.0,4.0, new double[]{}, new double[]{}));
    }
    @Test
    public void testDistanceNormalisee(){
        DistanceEuclidienneNormalisee distanceEuclidienneNormalisee = new DistanceEuclidienneNormalisee();
        DistanceManhattanNormalisee distanceManhattanNormalisee = new DistanceManhattanNormalisee();
        double[] minMaxX = new double[]{1,5};
        double[] minMaxY = new double[]{1,5};
        assertEquals((0.75-0.5)+(1-0.5), distanceManhattanNormalisee.distance(3.0,4.0,3.0,5.0,minMaxX,minMaxY));
        assertEquals(Math.sqrt(Math.pow(0.75-0.5,2)+Math.pow(1-0.5,2)),distanceEuclidienneNormalisee.distance(3.0,4.0,3.0,5.0,minMaxX,minMaxY));
    }
}
