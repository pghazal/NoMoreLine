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

    private Integer id = null;
    private CAMERA_STATE state;
    private String position = null;

    public Camera() {
    }
    
    public Camera(int id) {
        this.id = id;
        this.state = CAMERA_STATE.NONE;
        this.faceDetectorThread = null;
        this.position = null;
    }

    public FaceDetectorThread getFaceDetectorThread() {
        return faceDetectorThread;
    }

    public void setFaceDetectorThread(FaceDetectorThread faceDetectorThread) {
        this.faceDetectorThread = faceDetectorThread;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public CAMERA_STATE getState() {
        return state;
    }

    public void setState(CAMERA_STATE state) {
        this.state = state;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }
}
