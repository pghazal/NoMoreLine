/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package fr.ece.pfe_project.model;

/**
 *
 * @author Presentation
 */
public class CarnetAdresses implements ModelInterface{
    private String compagnie;
    private int nombreGuichet;
    private String societeAssistance;
    private String telephone;

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public void setSocieteAssistance(String societeAssistance) {
        this.societeAssistance = societeAssistance;
    }

    public void setNombreGuichet(int nombreGuichet) {
        this.nombreGuichet = nombreGuichet;
    }

    public void setCompagnie(String compagnie) {
        this.compagnie = compagnie;
    }

    public String getTelephone() {
        return telephone;
    }

    public String getSocieteAssistance() {
        return societeAssistance;
    }

    public int getNombreGuichet() {
        return nombreGuichet;
    }

    public String getCompagnie() {
        return compagnie;
    }
    
}
