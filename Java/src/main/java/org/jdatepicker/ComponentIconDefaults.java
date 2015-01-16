package org.jdatepicker;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;

import org.jdatepicker.graphics.JNextIcon;
import org.jdatepicker.graphics.JPreviousIcon;

/**
 * Created by jheyns on 2014/10/16.
 */
public class ComponentIconDefaults {

    private static final String LOGO = "/org/jdatepicker/icons/logo.png";
    private static final String CAMERAGREEN = "/org/jdatepicker/icons/greencamera.png";
    private static final String CAMERARED = "/org/jdatepicker/icons/redcamera.png";
    private static final String ONLED = "/org/jdatepicker/icons/on_led_icon.png";
    private static final String OFFLED = "/org/jdatepicker/icons/off_led_icon.png";
    private static final String CHECK = "/org/jdatepicker/icons/check.png";
    private static final String CAMERA = "/org/jdatepicker/icons/check.png";
    private static final String LISTINGVOL = "/org/jdatepicker/icons/check.png";
    private static final String CARNETADRESSE = "/org/jdatepicker/icons/check.png";
    private static final String PROJET = "/org/jdatepicker/icons/check.png";
    private static final String EXCEL = "/org/jdatepicker/icons/check.png";

    private Icon logoIcon;
    private Icon nextMonthIcon;
    private Icon nextYearIcon;
    private Icon previousMonthIcon;
    private Icon previousYearIcon;
    private Icon popupButtonIcon;
    private Icon greenCameraIcon;
    private Icon redCameraIcon;
    private Icon onLedIcon;
    private Icon offLedIcon;
    private Icon checkIcon;
    private Icon cameraIcon;
    private Icon listingVolIcon;
    private Icon carnetIcon;
    private Icon projetIcon;
    private Icon excelIcon;

    public ComponentIconDefaults() {
        try {
            //TODO consider making all the icons vector images which will scale
            logoIcon = loadIcon(LOGO);
            greenCameraIcon = loadIcon(CAMERAGREEN);
            redCameraIcon = loadIcon(CAMERARED);
            onLedIcon = loadIcon(ONLED);
            offLedIcon = loadIcon(OFFLED);
            checkIcon = loadIcon(CHECK);
            nextMonthIcon = new JNextIcon(4, 7, false);
            nextYearIcon = new JNextIcon(8, 7, true);
            previousMonthIcon = new JPreviousIcon(4, 7, false);
            previousYearIcon = new JPreviousIcon(8, 7, true);
            popupButtonIcon = null;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Icon loadIcon(String path) throws IOException {
        InputStream stream = ComponentIconDefaults.class.getResourceAsStream(path);
        BufferedImage image = ImageIO.read(stream);
        return new ImageIcon(image);
    }

    public Icon getLogoIcon() {
        return logoIcon;
    }

    public void setLogoIcon(Icon logoIcon) {
        this.logoIcon = logoIcon;
    }

    public Icon getNextMonthIcon() {
        return nextMonthIcon;
    }

    public Icon getCheckIcon() {
        return checkIcon;
    }

    public void setNextMonthIcon(Icon nextMonthIcon) {
        this.nextMonthIcon = nextMonthIcon;
    }

    public Icon getNextYearIcon() {
        return nextYearIcon;
    }

    public Icon getCameraIcon() {
        return cameraIcon;
    }

    public Icon getListingVolIcon() {
        return listingVolIcon;
    }

    public Icon getCarnetIcon() {
        return carnetIcon;
    }

    public Icon getProjetIcon() {
        return projetIcon;
    }

    public Icon getExcelIcon() {
        return excelIcon;
    }

    public void setNextYearIcon(Icon nextYearIcon) {
        this.nextYearIcon = nextYearIcon;
    }

    public Icon getPreviousMonthIcon() {
        return previousMonthIcon;
    }

    public void setPreviousMonthIcon(Icon previousMonthIcon) {
        this.previousMonthIcon = previousMonthIcon;
    }

    public Icon getPreviousYearIcon() {
        return previousYearIcon;
    }

    public void setPreviousYearIcon(Icon previousYearIcon) {
        this.previousYearIcon = previousYearIcon;
    }

    public Icon getPopupButtonIcon() {
        return popupButtonIcon;
    }

    public void setPopupButtonIcon(Icon popupButtonIcon) {
        this.popupButtonIcon = popupButtonIcon;
    }

    public Icon getgreenCameraIcon() {
        return greenCameraIcon;
    }

    public void setgreenCameraIcon(Icon greenCameraIcon) {
        this.greenCameraIcon = greenCameraIcon;
    }

    public Icon getredCameraIcon() {
        return redCameraIcon;
    }

    public void setredCameraIcon(Icon redCameraIcon) {
        this.redCameraIcon = redCameraIcon;
    }

    public Icon getonLedIcon() {
        return onLedIcon;
    }

    public void setonLedIcon(Icon onLedIcon) {
        this.onLedIcon = onLedIcon;
    }

    public Icon getoffLedIcon() {
        return offLedIcon;
    }

    public void setoffLedIcon(Icon offLedIcon) {
        this.offLedIcon = offLedIcon;
    }

    public Icon getIcon() {
        return logoIcon;
    }

}
