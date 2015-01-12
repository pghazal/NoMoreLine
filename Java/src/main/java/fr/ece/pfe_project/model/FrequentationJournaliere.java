package fr.ece.pfe_project.model;

import java.util.Date;

/**
 *
 * @author pierreghazal
 */
public class FrequentationJournaliere implements ModelInterface {
    private Date date = null;
    private Integer frequentation = null;
    
    public FrequentationJournaliere() {
        this(null, 0);
    }
    
    public FrequentationJournaliere(Date date, Integer value) {
        this.date = date;
        this.frequentation = value;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Integer getFrequentation() {
        return frequentation;
    }

    public void setFrequentation(Integer value) {
        this.frequentation = value;
    }
}
