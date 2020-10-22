package com.example.hasimananjara.e_boky.classy;

/**
 * Created by hasimananjara on 23/06/2017.
 */

public class PretPk {
    private String datePret;
    private Livre livre ;
    private Lecteur lecteur ;

    public PretPk(String datePret, Livre livre, Lecteur lecteur) {
        this.datePret = datePret;
        this.livre = livre;
        this.lecteur = lecteur;
    }

    public String getDatePret() {
        return datePret;
    }

    public void setDatePret(String datePret) {
        this.datePret = datePret;
    }

    public Livre getLivre() {
        return livre;
    }

    public void setLivre(Livre livre) {
        this.livre = livre;
    }

    public Lecteur getLecteur() {
        return lecteur;
    }

    public void setLecteur(Lecteur lecteur) {
        this.lecteur = lecteur;
    }
}
