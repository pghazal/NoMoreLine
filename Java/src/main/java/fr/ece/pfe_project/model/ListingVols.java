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
public class ListingVols implements ModelInterface {
    
    private int type;
    private String element;

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
