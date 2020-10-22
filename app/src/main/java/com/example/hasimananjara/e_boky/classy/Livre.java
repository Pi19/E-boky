package com.example.hasimananjara.e_boky.classy;

/**
 * Created by hasimananjara on 14/06/2017.
 */

public class Livre {
    private String numLivre ;
    private String Designation ;
    private String Auteur;
    private String Date_edition ;
    private String Disponible ;

    public String getNumLivre() {
        return numLivre;
    }

    public void setNumLivre(String numLivre) {
        this.numLivre = numLivre;
    }

    public String getDesignation() {
        return Designation;
    }

    public void setDesignation(String designation) {
        Designation = designation;
    }

    public String getAuteur() {
        return Auteur;
    }

    public void setAuteur(String auteur) {
        Auteur = auteur;
    }

    public String getDate_edition() {
        return Date_edition;
    }

    public void setDate_edition(String date_edition) {
        Date_edition = date_edition;
    }

    public String getDisponible() {
        return Disponible;
    }

    public void setDisponible(String disponible) {
        Disponible = disponible;
    }

    public Livre(String numLivre, String designation, String auteur, String date_edition) {
        this.numLivre = numLivre;
        Designation = designation;
        Auteur = auteur;
        Date_edition = date_edition;
    }

    public Livre(String numLivre, String designation, String disponible) {
        this.numLivre = numLivre;
        Designation = designation;
        Disponible = disponible;
    }

    public Livre(String numLivre, String designation, String auteur, String date_edition, String disponible) {
        this.numLivre = numLivre;
        Designation = designation;
        Auteur = auteur;
        Date_edition = date_edition;
        Disponible = disponible;
    }

    public Livre(String numLivre) {
        this.numLivre = numLivre;
    }
}
