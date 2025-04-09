package fr.univlille.classification.i3;

import fr.univlille.classification.i3.model.CsvReader;
import fr.univlille.classification.i3.model.Plateforme;
import fr.univlille.classification.i3.view.Accueil;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Plateforme plat = new Plateforme();
        try {
            new Accueil(plat);
        } catch (Exception e ){
            System.out.println(e.getMessage());
        }
    }
}