
package fr.univlille.classification.i3.enums;

public class DistanceManhattanNormalisee implements Distance{
    @Override
    public double distance(Double valX1, Double valX2, Double valY1, Double valY2,double[] minMaxX, double[] minMaxY) {
        double minX = minMaxX[0];
        double maxX = minMaxX[1];
        double minY = minMaxY[0];
        double maxY= minMaxY[1];
        return Math.abs(normaliser(valX2,maxX,minX)-normaliser(valX1,maxX,minX))
                + Math.abs(normaliser(valY2,maxY,minY)-normaliser(valY1,maxY,minY));
    }
    private double normaliser(double val1, double max, double min){
        return (val1-min)/(max-min);
    }
}
