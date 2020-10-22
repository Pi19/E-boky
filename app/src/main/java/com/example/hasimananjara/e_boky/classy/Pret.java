package com.example.hasimananjara.e_boky.classy;

/**
 * Created by hasimananjara on 22/06/2017.
 */

public class Pret {
    private String numLivre ;
    private String numLecteur ;
    private String DatePret;

    public Pret(String numLivre, String numLecteur, String datePret) {
        this.numLivre = numLivre;
        this.numLecteur = numLecteur;
        DatePret = datePret;
    }

    public String getNumLivre() {
        return numLivre;
    }

    public void setNumLivre(String numLivre) {
        this.numLivre = numLivre;
    }

    public String getNumLecteur() {
        return numLecteur;
    }

    public void setNumLecteur(String numLecteur) {
        this.numLecteur = numLecteur;
    }

    public String getDatePret() {
        return DatePret;
    }

    public void setDatePret(String datePret) {
        DatePret = datePret;
    }
}
