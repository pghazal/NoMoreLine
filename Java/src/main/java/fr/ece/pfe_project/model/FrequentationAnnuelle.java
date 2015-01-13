package fr.ece.pfe_project.model;

/**
 *
 * @author pierreghazal
 */
public class FrequentationAnnuelle {

    private Integer year = null;
    private Integer frequentation = null;

    public FrequentationAnnuelle() {
        year = null;
        frequentation = null;
    }

    public FrequentationAnnuelle(Integer year, Integer frequentation) {
        this.year = year;
        this.frequentation = frequentation;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getFrequentation() {
        return frequentation;
    }

    public void setFrequentation(Integer frequentation) {
        this.frequentation = frequentation;
    }
}
