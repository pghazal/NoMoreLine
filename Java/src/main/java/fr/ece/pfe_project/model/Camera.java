package fr.ece.pfe_project.model;

/**
 *
 * @author Pierre Ghazal
 */
public class Camera implements ModelInterface {

    private long id;
    private int number;

    public Camera(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public int getNumber() {
        return number;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
