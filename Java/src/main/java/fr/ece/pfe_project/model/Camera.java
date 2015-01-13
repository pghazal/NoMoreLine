package fr.ece.pfe_project.model;

import real_time_image_processing.FaceDetectorThread;

/**
 *
 * @author Pierre Ghazal
 */
public class Camera implements ModelInterface {

    public enum CAMERA_STATE {

        NONE, NORMAL, ALERT
    }

    private FaceDetectorThread faceDetectorThread;

    private long id;
    private int number;
    private CAMERA_STATE state;

    public Camera(long id) {
        this.id = id;
        this.state = CAMERA_STATE.NONE;
        this.faceDetectorThread = null;
    }

    public FaceDetectorThread getFaceDetectorThread() {
        return faceDetectorThread;
    }

    public void setFaceDetectorThread(FaceDetectorThread faceDetectorThread) {
        this.faceDetectorThread = faceDetectorThread;
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
