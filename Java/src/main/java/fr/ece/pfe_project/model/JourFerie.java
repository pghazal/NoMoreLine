package fr.ece.pfe_project.model;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author pierreghazal
 */
public class JourFerie implements Serializable {

    private String libelle;
    private Date date;

    public JourFerie() {
        this.libelle = "label";
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MILLISECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.HOUR, 0);
        this.date = cal.getTime();
    }

    public JourFerie(String libelle, Date date) {
        this.libelle = libelle;
        this.date = date;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
