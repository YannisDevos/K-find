package fr.univlille.classification.i3.view;

import fr.univlille.classification.i3.model.Plateforme;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Accueil extends Stage {
    private Label K_Finds;
    private Button newGraphe;
    private Button quitter;
    private HBox hbox;
    private VBox vbox1;
    private VBox vbox2;
    private Image logo;
    private ImageView imv1;

    public Accueil(Plateforme plt) {
        //Initialisation des nodes
        this.K_Finds = new Label("K_Finds");
        this.newGraphe = new Button("Créer un nouveau graphique");
        this.quitter = new Button("Quitter");
        this.hbox = new HBox();
        this.vbox1 = new VBox();
        this.vbox2 = new VBox();
        try {
            this.logo = new Image(new FileInputStream("res/images/Kfind3.png"));
        } catch (FileNotFoundException e) {
            System.out.println("Image pas trouvé");
        }
        //Ajout des nodes dans les conteneurs
        this.imv1 = new ImageView(this.logo);
        this.vbox2.getChildren().addAll(this.newGraphe, this.quitter);
        this.vbox1.getChildren().addAll(this.K_Finds, this.vbox2);
        this.hbox.getChildren().addAll(this.vbox1, this.imv1);

        //Change de page
        this.newGraphe.setOnAction(event -> {
            this.close();
            new ChoixCategories(plt);
        });
        //Ferme la page
        this.quitter.setOnAction(event -> this.close());
        this.setResizable(false);
        this.initStyleClassAccueil();
        Scene scene = new Scene(this.hbox, 1000, 550);
        scene.getStylesheets().add(getClass().getResource("/CSS/Accueil.css").toExternalForm()); //Chemin pour aller chercher les fichiers CSS
        this.getIcons().add(new Image(new File("res/images/Kfind3.png").toURI().toString()));
        this.setTitle("K_Finds");
        this.setScene(scene);
        this.show();
    }

    private void initStyleClassAccueil(){
        this.K_Finds.getStyleClass().add("K_Finds_Title"); //Nom spécifié pour le CSS
        this.hbox.getStyleClass().add("hbox");
        this.vbox1.getStyleClass().add("vbox1");
        this.vbox2.getStyleClass().add("vbox2");
        this.newGraphe.getStyleClass().add("newGraphe");
        this.quitter.getStyleClass().add("quitter");
    }
}
