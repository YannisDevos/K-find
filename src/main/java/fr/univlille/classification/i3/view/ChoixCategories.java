package fr.univlille.classification.i3.view;

import fr.univlille.classification.i3.model.Plateforme;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ChoixCategories extends Stage {
    private Plateforme plt;
    private BorderPane bPane;
    private VBox vBox;
    private HBox hbox;
    private HBox hbox2;
    private Label label1;
    private Label label2;
    private Label label3;
    private Button theme;
    private Button creerGraphe;
    private Button retour;
    private ComboBox<String> knnType1;
    private ComboBox<String> knnType2;
    private ComboBox<String> caracteristique1;
    private ComboBox<String> caracteristique2;
    private Image imgRetour;
    private ImageView imgVRetour;

    public ChoixCategories(Plateforme plt) {
        //Initialisation des nodes
        this.vBox = new VBox();
        this.hbox = new HBox();
        this.hbox2 = new HBox();
        this.bPane = new BorderPane();
        this.plt = plt;
        List<String> listeknnType1 = new ArrayList<>();
        List<String> listeknnType2 = new ArrayList<>();
        this.knnType1 = new ComboBox<>();
        this.knnType2 = new ComboBox<>();

        this.knnType1.resize(275, 275);
        this.knnType2.resize(275, 275);



        try {
            this.imgRetour = new Image(new FileInputStream("res/images/retour.png"));
        } catch (FileNotFoundException e) {
            System.out.println("Image pas trouvé");
        }

        this.imgVRetour = new ImageView(imgRetour);
        this.imgVRetour.setFitHeight(10);
        this.imgVRetour.setFitWidth(30);

        this.label1 = new Label("Choississez un thème");
        this.label2 = new Label("Choississez les KNN");
        this.label3 = new Label("Choississez les caractéristiques");

        this.theme = new Button("Selectionnez un fichier csv");
        this.retour = new Button();
        this.retour.setGraphic(imgVRetour);

        this.caracteristique1 = new ComboBox<>();
        this.caracteristique2 = new ComboBox<>();

        this.caracteristique1.resize(275, 275);
        this.caracteristique2.resize(275, 275);

        final File[] file = new File[1];

        FileChooser.ExtensionFilter exFiltrer = new FileChooser.ExtensionFilter("CSV Files", "*.csv");
        this.theme.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                String userHome = System.getProperty("user.home");
                FileChooser fc = new FileChooser();
                fc.getExtensionFilters().addAll(exFiltrer);
                fc.setInitialDirectory(new File(userHome)); //TODO Windows à faire, trop loin
                File selectedFile = fc.showOpenDialog(ChoixCategories.super.getOwner());
                file[0] = selectedFile;
                System.out.println(selectedFile);

                try {
                    plt.charger(file[0].getAbsolutePath());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                theme.setText(selectedFile.getName());

                caracteristique1.setItems(FXCollections.observableArrayList(plt.getCat()));
                caracteristique2.setItems(FXCollections.observableArrayList(plt.getCat()));

                //Type ajouté pour la méthode k_N
                listeknnType1.clear();
                listeknnType2.clear();
                listeknnType1.addAll(plt.getEnums().keySet());

                if(listeknnType1.contains("name") || listeknnType1.contains("nom")){
                    listeknnType1.remove("name");
                    listeknnType1.remove("nom");
                }

                knnType1.getItems().clear();
                knnType2.getItems().clear();
                knnType1.getItems().addAll(listeknnType1);
                listeknnType2.add("Euclidienne");
                listeknnType2.add("Manhattan");
                listeknnType2.add("Euclidienne Normalisée");
                listeknnType2.add("Manhattan Normalisée");
                knnType2.getItems().addAll(listeknnType2);
            }
        });

        //Ferme la page et retourne à la page d'accueil
        this.retour.setOnAction(event -> {
            this.close();
            new Accueil(plt);
        });

        this.creerGraphe = new Button("Creation du graphe");
        this.hbox.getChildren().addAll(this.caracteristique1, this.caracteristique2);
        this.hbox2.getChildren().addAll(this.knnType1, this.knnType2);
        this.vBox.getChildren().addAll(this.label1, this.theme, this.label2,this.hbox2, this.label3, this.hbox, this.creerGraphe);
        this.bPane.setCenter(this.vBox);
        this.bPane.setTop(this.retour);
        this.creerGraphe.setOnAction(event -> {
            if (this.caracteristique1.getSelectionModel().getSelectedItem() != null || this.caracteristique2.getSelectionModel().getSelectedItem() != null) {
                plt.setCurrentCat1(this.caracteristique1.getSelectionModel().getSelectedItem());
                plt.setCurrentCat2(this.caracteristique2.getSelectionModel().getSelectedItem());
                plt.setkNNAble(knnType1.getSelectionModel().getSelectedItem());
                plt.setDistance(knnType2.getSelectionModel().getSelectedItem());
                System.out.println(knnType1.getSelectionModel().getSelectedItem());

                this.close();
                plt.newView();
            }
        });

        this.setResizable(false);
        initStyleClassChoixCategories();
        Scene scene = new Scene(this.bPane, 1000, 550);
        scene.getStylesheets().add(getClass().getResource("/CSS/ChoixCategories.css").toExternalForm()); //Chemin pour aller chercher les fichiers CSS
        this.getIcons().add(new Image(new File("res/images/Kfind3.png").toURI().toString()));
        this.setTitle("K_Finds");
        this.setScene(scene);
        this.show();
    }

    private void initStyleClassChoixCategories() {
        this.bPane.getStyleClass().add("bPane");
        this.vBox.getStyleClass().add("vbox");
        this.hbox.getStyleClass().add("hbox");
        this.hbox2.getStyleClass().add("hbox");
        this.theme.getStyleClass().add("theme");
        this.label2.getStyleClass().add("label");
        this.label1.getStyleClass().add("label");
        this.label3.getStyleClass().add("label");
        this.creerGraphe.getStyleClass().add("creergraphe");
        this.caracteristique1.getStyleClass().add("caracteristique");
        this.caracteristique2.getStyleClass().add("caracteristique");
        this.retour.getStyleClass().add("retour");
    }
}