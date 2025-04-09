package fr.univlille.classification.i3.model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class CsvReader {
    final static String COMMA_DELIMITER = ",";
    final static String SEPARATOR = "/";

    /**
     * Permet de récupérer les données d'un fichier CSV et de les enregistrer dans une plateforme
     *
     * @param plateforme la plateforme dans laquelle les données seront enregistrer
     * @param filePath chemin sous forme de String du fichier CSV
     * @throws IOException peut renvoyer une IOException
     */
    public static void charger(String filePath, Plateforme plateforme) throws IOException {
        String[] columnNames;
        List<Data> ldatas = new ArrayList<>();

        // Vider les anciennes données et catégories
        plateforme.setDatas(new ArrayList<>());
        plateforme.setCat(new ArrayList<>());

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            columnNames = CsvReader.getColumnName(filePath);
            String line;

            br.readLine();  // Sauter l'en-tête du fichier CSV

            while ((line = br.readLine()) != null) {
                String[] values = line.split(COMMA_DELIMITER);

                Data rdatas = new Data(getFileName(filePath));

                for (int i = 0; i < columnNames.length; i++) {
                    rdatas.getAttributMap().put(columnNames[i], values[i]);
                }

                ldatas.add(rdatas);
            }
        }

        // Ajouter les nouvelles données et catégories
        plateforme.setDatas(ldatas);
        plateforme.setCat(Arrays.asList(columnNames));
    }



    /**
     * Permet de récuperer le nom d'un fichier à partir de son chemin et en focntion du système d'exploitation
     *
     * @param filePath le chemin du fichier
     * @return le nom du fichier sous forme de String
     */
    public static String getFileName(String filePath){
        String[] fileName = filePath.split(SEPARATOR);
        return fileName[fileName.length-1].substring(0,fileName[fileName.length-1].length()-4);
    }

    /**
     *
     * Permet de récuperer le nom des colonnes du CSV
     *
     * @param filePath le chemin du fichier
     * @return un tableau de String contenant les noms des colonnes du CSV
     */
    private static String[] getColumnName(String filePath){
        String[] columnNames = new String[0];

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line = br.readLine();
            columnNames = line.split(COMMA_DELIMITER);

            for (int i = 0; i < columnNames.length; i++) {
                columnNames[i] = TextFormater.cancelQuote(columnNames[i]);
            }
        }catch (IOException ioe){
            System.out.println(ioe.getMessage());
        }

        return columnNames;
    }

    /**
     * Permet de stocker dans une HashMap toutes les données distinct de type String et seulement de type String
     *
     * @param filepath le chemin du fichier
     * @return une HashMap de Set<String> permettant d'avoir les données distinct
     * @throws IOException peut renvoyer une IOException
     */
    public static Map<String, Set<String>> checkCSV(String filepath){
        HashMap<String,Set<String>> res = new HashMap<>();
        String[] colNames = CsvReader.getColumnName(filepath);
        List<Integer> validIndex = new ArrayList<>();

        try{
            validIndex = CsvReader.getValidIndex(filepath);
        }catch (IOException ioe){
            System.out.println(ioe.getMessage());
        }

        for (Integer index : validIndex) {

            try(BufferedReader br = new BufferedReader(new FileReader(filepath))){
                String line;
                br.readLine();
                res.put(colNames[index], new HashSet<String>());

                while ((line = br.readLine()) != null) {
                    res.get(colNames[index]).add(line.split(COMMA_DELIMITER)[index]);
                }

                if (res.get(colNames[index]).size() == CsvReader.countLine(filepath)){
                    res.remove(colNames[index]);
                }

            }catch (IOException ioe){
                System.out.println(ioe.getMessage());
            }
        }
        return res;
    }


    /**
     *
     * Permet de récuperer la liste des indexes valides
     *
     * @param filepath chemin du fichier
     * @return la liste des indexes valides
     * @throws IOException peut renvoyer une IOException
     */
    private static List<Integer> getValidIndex(String filepath) throws IOException {
        List<Integer> index = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filepath))) {
            br.readLine();
            String[] values = br.readLine().split(COMMA_DELIMITER);

            for (int i = 0; i < values.length; i++) {
                try {
                    Double.parseDouble(values[i]);
                } catch (NumberFormatException nfe) {
                    index.add(i);
                }
            }
        }

        return index;
    }


    /**
     *
     * Permet de compter le nombre de ligne du CSV
     *
     * @param filepath chemin du fichier
     * @return un entier correspondant au nombre de ligne du csv
     */
    private static int countLine(String filepath){
        int numberOfLine = -1;

        try (BufferedReader br = new BufferedReader(new FileReader(filepath))) {
            while ((br.readLine()) != null) {
                numberOfLine++;
            }
        }catch(IOException ioe){
            ioe.getMessage();
        }

        return numberOfLine;
    }

}
