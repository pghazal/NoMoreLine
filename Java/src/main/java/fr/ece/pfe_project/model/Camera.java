package fr.ece.pfe_project.model;

/**
 *
 * @author Pierre Ghazal
 */
public class Camera implements ModelInterface {

    public enum CAMERA_STATE {

        NONE, NORMAL, ALERT
    }

    private long id;
    private int number;
    private CAMERA_STATE state;

    public Camera(long id) {
        this.id = id;
        this.state = CAMERA_STATE.NONE;
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

    public CAMERA_STATE getState() {
        return state;
    }
    
    public void setState(CAMERA_STATE state) {
        this.state = state;
    }
}
