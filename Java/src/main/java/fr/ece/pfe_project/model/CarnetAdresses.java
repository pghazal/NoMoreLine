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
    private String compagnieca;
    private int nombreGuichet;
    private String societeAssistance;
    private String telephone;
    private Integer id = null;

    public CarnetAdresses(String test1, Integer test2, String test3, String test4, Integer test5){
        this.compagnieca = test1;
        this.nombreGuichet = test2;
        this.societeAssistance = test3;
        this.telephone = test4;
        this.id = test5;
    }

    public CarnetAdresses() {
        this(null, 0, null, null, null);
    }
    
    public void setId(Integer keyca) {
        this.id = keyca;
    }

    public Integer getId() {
        return id;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public void setSocieteAssistance(String societeAssistance) {
        this.societeAssistance = societeAssistance;
    }

    public void setNombreGuichet(int nombreGuichet) {
        this.nombreGuichet = nombreGuichet;
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

    public void setCompagnieca(String compagnieca) {
        this.compagnieca = compagnieca;
    }

    public String getCompagnieca() {
        return compagnieca;
    }

    
}
