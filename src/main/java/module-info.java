module K.Find {
    requires javafx.controls;
    requires java.sql;
    requires org.controlsfx.controls;
    opens fr.univlille.classification.i3 to javafx.graphics;
    exports fr.univlille.classification.i3.model;
    exports fr.univlille.classification.i3.enums;
}
