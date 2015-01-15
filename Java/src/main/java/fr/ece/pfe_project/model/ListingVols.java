/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package fr.ece.pfe_project.model;

import java.util.Date;

/**
 *
 * @author Presentation
 */
public class ListingVols implements ModelInterface {
    
    private int type;
    private String element;
    private Date date;
    private String destination;
    private String numeroVol;
    private String compagnie;
    private String observation;
    private String date1;
    private String heure;

    public void setHeure(String heure) {
        this.heure = heure;
    }

    public String getHeure() {
        return heure;
    }

    public void setDate1(String date1) {
        this.date1 = date1;
    }

    public String getDate1() {
        return date1;
    }

    public void setCompagnie(String compagnie) {
        this.compagnie = compagnie;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public void setNumeroVol(String numeroVol) {
        this.numeroVol = numeroVol;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    public String getObservation() {
        return observation;
    }

    public String getNumeroVol() {
        return numeroVol;
    }

    public String getDestination() {
        return destination;
    }

    public String getCompagnie() {
        return compagnie;
    }

    public void setDate(Date date) {
        this.date = date;
    }
    

    public Date getDate() {
        return date;
    }
    

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setElement(String element) {
        this.element = element;
    }

    public String getElement() {
        return element;
    }
    
}
