package com.example.hasimananjara.e_boky.classy;

/**
 * Created by hasimananjara on 06/06/2017.
 */

public class Lecteur {

    private String numLecteur;
    private String nom;
    private String prenom;
    private String Adresse;

    public Lecteur(String numLecteur, String nom, String prenom, String adresse) {
        this.numLecteur = numLecteur;
        this.nom = nom;
        this.prenom = prenom;
        Adresse = adresse;
    }

    public Lecteur(String numLecteur, String nom, String prenom) {
        this.numLecteur = numLecteur;
        this.nom = nom;
        this.prenom = prenom;
    }

    public Lecteur(String numLecteur) {
        this.numLecteur = numLecteur;
    }

    public String getNumLecteur() {
        return numLecteur;
    }

    public void setNumLecteur(String numLecteur) {
        this.numLecteur = numLecteur;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getAdresse() {
        return Adresse;
    }

    public void setAdresse(String adresse) {
        Adresse = adresse;
    }

    public Lecteur() {

    }


}
