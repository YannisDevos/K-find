package fr.univlille.classification.i3.enums;

public class DistanceEuclidienne implements Distance{
    @Override
    public double distance(Double valX1, Double valX2, Double valY1, Double valY2,double[] minMaxX, double[] minMaxY) {
        return Math.sqrt(Math.pow((valX2-valX1),2) +Math.pow((valY2-valY1),2));
    }
}
