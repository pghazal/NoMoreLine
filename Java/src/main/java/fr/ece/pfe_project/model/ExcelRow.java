package fr.ece.pfe_project.model;

import java.util.Date;

/**
 *
 * @author pierreghazal
 */
public class ExcelRow implements ModelInterface {
    private Date date;
    private Integer value;
    
    public ExcelRow() {
        this(null, 0);
    }
    
    public ExcelRow(Date date, Integer value) {
        this.date = date;
        this.value = value;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }
}
