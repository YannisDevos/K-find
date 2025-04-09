package fr.univlille.classification.i3.view;

import fr.iut.r304.utils.Observable;
import fr.iut.r304.utils.Observer;
import fr.univlille.classification.i3.enums.Colors;
import fr.univlille.classification.i3.model.CsvReader;
import fr.univlille.classification.i3.model.Data;
import fr.univlille.classification.i3.model.Plateforme;
import fr.univlille.classification.i3.model.TextFormater;
import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;

public class ScatterView extends Stage implements Observer {
    private double tickUnit;

    private Plateforme plt;
    private BorderPane bPane;
    private Button boutonAxe;
    private Button bAddPoint;
    private Button buttonNewView;
    private Button retour;
    private Label nom;
    private Label changerAxes;
    private Label axeY;
    private Label axeX;

    private Image imgRetour;
    private ImageView imgVRetour;

    private VBox vbg;
    private VBox vbaxe;
    private HBox hb;
    private HBox hBoxBoutton;
    private ComboBox<String> cBox1;
    private ComboBox<String> cBox2;

    private ScatterChart<Number, Number> sc;

    private Map<String, Map<String, List<Data>>> dataMap;
    private Map<String, Map.Entry<XYChart.Series<Number, Number>, Colors>> seriesMap;

    private String cat1;
    private String cat2;

    private NumberAxis xAxis;
    private NumberAxis yAxis;

    private Map<String, TextField> newData;

    private Label erreurAddPoint;

    public ScatterView(Plateforme plt) {
        this.setResizable(false);
        this.plt = plt;
        this.bPane = new BorderPane();
        this.hb = new HBox();
        this.hBoxBoutton = new HBox();
        this.vbg = new VBox();
        this.vbaxe = new VBox();
        this.boutonAxe = new Button("Changer les axes");
        this.bAddPoint = new Button("Créer un nouveau point");
        this.nom = new Label(TextFormater.format(CsvReader.getFileName(plt.getFile().getName())));
        this.changerAxes = new Label("Changer les axes ");
        this.axeX = new Label("Axe x :");
        this.axeY = new Label("Axe y :");
        this.buttonNewView = new Button("Créer une nouvelle vue");
        this.cBox1 = new ComboBox<>();
        this.cBox2 = new ComboBox<>();

        this.dataMap = new HashMap<>();
        this.newData = new HashMap<>();
        this.cat1 = plt.getCurrentCat1();
        this.cat2 = plt.getCurrentCat2();

        this.xAxis = new NumberAxis(plt.minData(cat1) - 0.3, plt.maxData(cat1) + 0.3, tickUnit);
        this.xAxis.setLabel(cat1);
        this.yAxis = new NumberAxis(plt.minData(cat2) - 0.3, plt.maxData(cat2) + 0.3, tickUnit);
        this.yAxis.setLabel(cat2);

        try {
            this.imgRetour = new Image(new FileInputStream("res/images/retour.png"));
        } catch (FileNotFoundException e) {
            System.out.println("Image pas trouvé");
        }

        this.imgVRetour = new ImageView(imgRetour);
        this.imgVRetour.setFitHeight(10);
        this.imgVRetour.setFitWidth(30);

        this.retour = new Button();
        this.retour.setGraphic(imgVRetour);

        this.setSeriesMap();
        this.initDataSeries();
        this.initSeriesMap();

        cBox1.setItems(FXCollections.observableArrayList(plt.getCat()));
        cBox2.setItems(FXCollections.observableArrayList(plt.getCat()));

        //Choix de l'axes #1
        this.cBox1.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                cBox2.getItems().remove(newValue);
            }
            if (oldValue != null) {
                cBox2.getItems().add(oldValue);
            } //Verifie qu'il n'y a pas deja la valeur dans l'autre ComboBox si oui enleve la valeur, sinon il re-ajoute

            cat1 = newValue; //Changement de la categorie pour l'axe
            reload(cat1, cat2);
        });

        //Choix de l'axes #2
        this.cBox2.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                cBox1.getItems().remove(newValue);
            }
            if (oldValue != null) {
                cBox1.getItems().add(oldValue);
            }

            cat2 = newValue;
            reload(cat1, cat2);
        }); //même fonctionnement que au dessus

        this.sc = new ScatterChart<>(xAxis, yAxis);
        this.sc.getData().addAll(this.getSeries());
        this.sc.setLegendVisible(false);

        this.boutonAxe.setOnAction(e -> reload(cat1, cat2));
        this.buttonNewView.setOnAction(event -> plt.newView());

        this.bAddPoint.setOnAction(event -> addChampsPoints());
        this.hBoxBoutton.getChildren().addAll(this.bAddPoint, this.buttonNewView);

        this.vbg.getChildren().addAll( this.hBoxBoutton,this.nom, this.sc);
        this.vbaxe.getChildren().addAll(this.changerAxes,this.axeY ,cBox2, this.axeX, cBox1, this.legende());

        this.hb.getChildren().addAll(this.vbg, this.vbaxe);

        this.bPane.setRight(this.vbaxe);
        this.bPane.setCenter(this.vbg);
        this.bPane.setLeft(this.retour);
        this.retour.setOnAction(event -> {
            this.close();
            new ChoixCategories(plt);
        });

        this.initStyleScatter();
        Scene scene = new Scene(this.bPane, 1000, 500);
        scene.getStylesheets().add(getClass().getResource("/CSS/ScatterView.css").toExternalForm()); //Chemin pour aller chercher les fichiers CSS
        this.getIcons().add(new Image(new File("res/images/Kfind3.png").toURI().toString()));
        this.setTitle("K_Finds");
        this.setScene(scene);
        this.show();
    }

    public void setSeriesMap() {
        this.seriesMap = new HashMap<>();
        this.dataMap = new HashMap<>();

        int cpt = 0;
        for (String key : plt.getEnums().keySet()) { // Parcours les enums
            Map<String, List<Data>> typeMap = new HashMap<>();

            for (String val : plt.getEnums().get(key)) { // Parcours les types
                this.seriesMap.put(val, new AbstractMap.SimpleEntry<>(new XYChart.Series<Number, Number>(), Colors.values()[cpt++ % Colors.values().length]));
                typeMap.put(val, new ArrayList<>()); // Initialise une liste pour chaque type
            }

            this.dataMap.put(key, typeMap); // Associe le typeMap à la catégorie
        }
    }


    public void initDataSeries(){
        //Decommenter pour LOGs
        //System.out.println("Enums : " + this.plt.getEnums());
        //System.out.println("Datas : " + this.plt.getDatas());

        for(String s : this.plt.getEnums().keySet()) { //Parcours les enums
            for(String type : this.plt.getEnums().get(s)){ //Recup des elements dans les enums
                for (int i = 0; i < this.plt.getDatas().size(); ++i) { //parcours tout les data
                    if (this.plt.getDatas().get(i).getAttributMap().get(s).equals(type)){ //verif le type de la data
                        if (!this.dataMap.get(s).get(type).contains(this.plt.getDatas().get(i))) {
                            this.dataMap.get(s).get(type).add(this.plt.getDatas().get(i));
                            //System.out.println("Ajout de la donnée : " + this.plt.getDatas().get(i));
                        }
                    }
                }
            }
        }
    }

    public void initSeriesMap(){
        String knn = this.plt.getkNNAble();
        //System.out.println(this.plt.getEnums().get(knn));

        for(String type : this.plt.getEnums().get(knn)) { //Recup des elements dans les enums
            for (Data data : this.dataMap.get(knn).get(type)) { //Parcours les datas
                if (data.getAttributMap().get(knn).equals(type)) { //Verifie le type

                    XYChart.Data<Number, Number> p = new XYChart.Data<>(data.getUneCatToDouble(this.cat1), data.getUneCatToDouble(this.cat2));

                    Circle c = new Circle(data.getUneCatToDouble(this.cat1),data.getUneCatToDouble(this.cat2),5);
                    Colors current_color = this.seriesMap.get(type).getValue();
                    c.setFill(Color.rgb(current_color.r,current_color.g,current_color.b));
                    p.setNode(c);

                    this.seriesMap.get(type).getKey().getData().add(p);
                }
            }
        }
    }

    public void initSeriesNewPoint(){
        String knn = this.plt.getkNNAble();
        //System.out.println(this.plt.getEnums().get(knn));

        for(String type : this.plt.getEnums().get(knn)) { //Recup des elements dans les enums
            for (Data data : this.plt.getNewData()) { //Parcours les datas
                if (data.getAttributMap().get(knn).equals(type)) { //Verifie le type

                    XYChart.Data<Number, Number> p = new XYChart.Data<>(data.getUneCatToDouble(this.cat1), data.getUneCatToDouble(this.cat2));

                    Rectangle r = new Rectangle(data.getUneCatToDouble(this.cat1), data.getUneCatToDouble(this.cat2), 10, 10);
                    Colors current_color = this.seriesMap.get(type).getValue();
                    r.setFill(Color.rgb(current_color.r,current_color.g,current_color.b));
                    p.setNode(r);

                    this.seriesMap.get(type).getKey().getData().add(p);
                }
            }
        }
    }

    //PopUp pour créer point (un bordel :))
    public void addChampsPoints() {
        Dialog<Data> dialog = new Dialog<>();
        dialog.setTitle("Création de Point");
        dialog.setHeaderText("Entrez les coordonnées du point");

        VBox vbDiag = new VBox();
        Map<String, Control> inputFields = new HashMap<>(); // Map des inputs pour TextField ou ComboBox

        // Liste des catégories où aucune validation n'est requise (comme "Nom")
        List<String> freeInputCategories = List.of("Nom", "Name", "nom", "name"); // Ajoutez les catégories libres ici

        // Liste des enums où une valeur vide est autorisée
        List<String> ignorableEnums = List.of(this.plt.getkNNAble()); // Ajoutez les enums ignorables ici

        // Parcours des catégories disponibles
        for (String category : plt.getCat()) {
            Label label = new Label(category);
            if (plt.getEnums().containsKey(category)) { // Si c'est une enum
                ComboBox<String> comboBox = new ComboBox<>(FXCollections.observableArrayList(plt.getEnums().get(category)));
                comboBox.setPromptText("Sélectionnez une valeur");
                vbDiag.getChildren().addAll(label, comboBox);
                inputFields.put(category, comboBox); // On enregistre le ComboBox
            } else { // Sinon, c'est un champ texte classique
                TextField textField = new TextField();
                textField.setPromptText("Entrez une valeur pour " + category);
                vbDiag.getChildren().addAll(label, textField);
                inputFields.put(category, textField); // On enregistre le TextField
            }
        }

        Label label = new Label("Sélectionner rien sur la cible du k-NN pour que le k-NN classifie le point");
        vbDiag.getChildren().addAll(label);
        dialog.getDialogPane().setContent(vbDiag);

        // Boutons OK et Annuler
        ButtonType okButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButtonType = new ButtonType("Annuler", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(okButtonType, cancelButtonType);

        // Conversion du résultat du dialogue en un objet Data
        dialog.setResultConverter(buttonType -> {
            if (buttonType == okButtonType) {
                try {
                    Data newPoint = new Data(CsvReader.getFileName(this.plt.getFile().getName()));
                    for (String cat : plt.getCat()) {
                        Control input = inputFields.get(cat);
                        String value;

                        if (input instanceof TextField) {
                            value = ((TextField) input).getText();

                            if (!freeInputCategories.contains(cat)) {
                                try {
                                    Double.parseDouble(value); // Valide que c'est un double
                                } catch (NumberFormatException e) {
                                    throw new IllegalArgumentException(
                                            "La catégorie '" + cat + "' doit contenir un nombre valide."
                                    );
                                }
                            }

                        } else if (input instanceof ComboBox) {
                            value = (String) ((ComboBox<?>) input).getValue();

                            // Si c'est une enum "ignorable", on autorise les valeurs nulles ou vides
                            if (!ignorableEnums.contains(cat) && (value == null || value.isEmpty())) {
                                throw new IllegalArgumentException("Valeur vide pour la catégorie : " + cat);
                            }
                        } else {
                            throw new IllegalStateException("Type de champ non supporté");
                        }

                        // On ajoute la valeur seulement si elle est non vide
                        if (value != null && !value.isEmpty()) {
                            newPoint.setAttributMap(cat, value);
                        }
                    }
                    return newPoint;
                } catch (IllegalArgumentException e) {
                    // Affiche un message d'erreur en cas de problème
                    new Alert(Alert.AlertType.ERROR, e.getMessage()).showAndWait();
                    return null;
                }
            }
            return null;
        });

        // Affichage de la boîte de dialogue
        dialog.showAndWait().ifPresent(point -> {
            if (point != null) {
                plt.ajouterPoint(point);
                System.out.println("Nouveau Point créé : " + point);
            } else {
                System.out.println("Création de point annulée ou erreur dans la saisie.");
            }
        });
    }

    public void reload(String cat1new, String cat2new) {
        double range = this.plt.maxData(this.cat2) - this.plt.minData(this.cat2);

        int targetTicks = 10;

        this.tickUnit = Math.pow(10, Math.floor(Math.log10(range / targetTicks)));
        if (range / (targetTicks * tickUnit) > 5) {
            tickUnit *= 5;
        } else if (range / (targetTicks * tickUnit) > 2) {
            tickUnit *= 2;
        }

        // Créer de nouveaux axes avec les valeurs appropriées
        xAxis = new NumberAxis(plt.minData(cat1new) - (plt.minData(cat1new)/10), plt.maxData(cat1new) + (plt.maxData(cat1new)/10), tickUnit);
        yAxis = new NumberAxis(plt.minData(cat2new) - (plt.minData(cat2new)/10), plt.maxData(cat2new) + (plt.maxData(cat2new)/10), tickUnit);

        xAxis.setLabel(cat1new);
        yAxis.setLabel(cat2new);

        // Vider les anciennes séries
        clearSeries();

        initDataSeries();
        initSeriesMap();
        initSeriesNewPoint();

        // Créer un nouveau ScatterChart avec les nouveaux axes
        ScatterChart<Number, Number> scNew = new ScatterChart<>(xAxis, yAxis);
        scNew.getData().addAll(this.getSeries()); // Ajouter les nouvelles séries de données
        scNew.setLegendVisible(false);

        // Remplacer le graphique dans la scène
        this.vbg.getChildren().remove(this.sc);
        this.vbg.getChildren().add(scNew);
        this.sc = scNew; // Mettre à jour la référence à sc
    }


    private List<XYChart.Series<Number, Number>> getSeries() {
        ArrayList<XYChart.Series<Number, Number>> res = new ArrayList<>();
        for (String s : seriesMap.keySet()) {
            // Créez une nouvelle instance de XYChart.Series à chaque fois
            XYChart.Series<Number, Number> newSeries = new XYChart.Series<>();
            newSeries.setName(s); // Attribuez le nom de la série

            // Ajoutez les points de données de la série correspondante
            newSeries.getData().addAll(seriesMap.get(s).getKey().getData());

            // Ajoutez cette série à la liste des séries
            res.add(newSeries);
        }
        return res;
    }

    private void clearSeries() {
        // Vider les données de toutes les séries sans les supprimer
        for (XYChart.Series<Number, Number> series : this.sc.getData()) {
            series.getData().clear();  // Effacer les points de chaque série
        }

        for (Map.Entry<XYChart.Series<Number,Number>, Colors> me : this.seriesMap.values()){
            me.getKey().getData().clear();
        }
    }

    private void initStyleScatter(){
        this.bAddPoint.getStyleClass().add("button");
        this.buttonNewView.getStyleClass().add("button");
        this.hb.getStyleClass().add("hbox");
        this.hBoxBoutton.getStyleClass().add("hboxButton");
        this.cBox1.getStyleClass().add("box");
        this.cBox2.getStyleClass().add("box");
        this.retour.getStyleClass().add("retour");
        this.sc.getStyleClass().add("sc");
        this.vbg.getStyleClass().add("vbg");
        this.vbaxe.getStyleClass().add("vbaxe");
        this.bPane.getStyleClass().add("bPane");
        this.axeX.getStyleClass().add("label");
        this.axeY.getStyleClass().add("label");
        this.changerAxes.getStyleClass().add("label");
    }

    private VBox legende(){
        VBox vb = new VBox();

        Set<String> KNNable = this.plt.getEnums().get(this.plt.getkNNAble());

        for (String KNNname : KNNable) {
            HBox hb = new HBox();

            Circle monCercle = new Circle(10,10,5);
            Colors maCouleur = this.seriesMap.get(KNNname).getValue();
            monCercle.setFill(Color.rgb(maCouleur.getR(), maCouleur.getG(), maCouleur.getB()));

            hb.getChildren().add(monCercle);
            hb.getChildren().add(new Label(TextFormater.format(TextFormater.cancelQuote(KNNname))));

            vb.getChildren().add(hb);
        }

        vb.getStyleClass().add("vbox");
        return vb;
    }

    @Override
    public void update(Observable observable) {
        reload(cat1, cat2);
    }

    @Override
    public void update(Observable observable, Object data) {
        reload(cat1, cat2);
    }
}