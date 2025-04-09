package fr.univlille.classification.i3.enums;

import fr.univlille.classification.i3.model.Plateforme;

public interface Distance {
    double distance(Double valX1, Double valX2, Double valY1, Double valY2, double[] minMaxX, double[] minMaxY );

}
