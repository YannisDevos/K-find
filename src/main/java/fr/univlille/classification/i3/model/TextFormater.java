package fr.univlille.classification.i3.model;

public class TextFormater {

    /**
     *
     * Permet de formater un mot en remplaçant les '_' par des espace et en mettant une majuscule au debut du String
     *
     * @param word le mot a formater
     * @return le String formater
     */
    public static String format(String word) {
        String res = word;
        if (!word.isEmpty()) {
            String firstLetter = word.charAt(0) + "";
            String restOfWord = word.substring(1);

            firstLetter = firstLetter.toUpperCase();
            restOfWord = restOfWord.toLowerCase();

            res = firstLetter + restOfWord;

            res = res.replace('_', ' ');
        }
        return res;
    }

    public static String cancelQuote(String word) {
        StringBuilder text = new StringBuilder();

        if (!word.isEmpty()) {
            for (int i = 0; i < word.length(); i++) {
                char caracter = word.charAt(i);
                if (caracter != '"') {
                    text.append(caracter);
                }
            }
        }
        return text.toString();
    }
}
