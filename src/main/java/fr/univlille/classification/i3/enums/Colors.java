package fr.univlille.classification.i3.enums;

public enum Colors {
    NOIR(0, 0, 0),
    BLEU_MARINE(0, 0, 128),
    ROUGE_FONCE(128, 0, 0),
    VERT_FORET(34, 139, 34),
    ORANGE_BRULE(204, 85, 0),
    GRIS(128, 128, 128),
    VIOLET(204, 51, 255),
    VIOLET_FONCE(153, 102, 255),
    BLEU_FONCE(128, 128, 255),
    FER(102, 153, 153),
    MARRON_CLAIRE(153, 153, 102),
    ROSE(255, 102, 153),
    MARRON(204, 153, 0),
    CORAIL(255, 127, 80),
    VERT_CLAIRE(153, 204, 0),
    BLEU(51, 204, 255),
    ROUGE(255, 173, 51),
    GRIS_PERLE(192, 192, 192),
    TURQUOISE(64, 224, 208),
    JAUNE_MOUTARDE(255, 204, 51),
    VERT(173, 235, 173),
    ROSE_CLAIRE(255, 204, 255),
    CYAN_CLAIR(0, 255, 255),
    LAVANDE(230, 230, 250),
    BLEU_CIEL(214, 245, 245),
    BEIGE(245, 245, 220),
    BLEU_NUAGE(204, 255, 255),
    JAUNE(255, 255, 0);

    public int r;
    public int g;
    public int b;

    Colors(int r, int g, int b) {
        this.r = r;
        this.g = g;
        this.b = b;
    }

    public int getR() {
        return this.r;
    }

    public int getG() {
        return this.g;
    }

    public int getB() {
        return this.b;
    }
}
