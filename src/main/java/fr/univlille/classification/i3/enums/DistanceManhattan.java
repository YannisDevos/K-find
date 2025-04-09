package fr.univlille.classification.i3.enums;

public class DistanceManhattan implements Distance{
    @Override
    public double distance(Double valX1, Double valX2, Double valY1, Double valY2,double[] minMaxX, double[] minMaxY) {
        return Math.abs((valX2-valX1)) + Math.abs((valY2-valY1));
    }
}
