package fr.ece.pfe_project.model;

/**
 *
 * @author pierreghazal
 */
public class AlgoResult {

    private Integer previsionPassager;
    private Integer previsionAvecJoursFeries;

    public AlgoResult() {
    }

    public Integer getPrevisionPassager() {
        return previsionPassager;
    }

    public void setPrevisionPassager(Integer previsionPassager) {
        this.previsionPassager = previsionPassager;
    }

    public Integer getPrevisionAvecJoursFeries() {
        return previsionAvecJoursFeries;
    }

    public void setPrevisionAvecJoursFeries(Integer previsionAvecJoursFeries) {
        this.previsionAvecJoursFeries = previsionAvecJoursFeries;
    }

}
