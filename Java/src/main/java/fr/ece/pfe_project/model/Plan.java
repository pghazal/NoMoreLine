package fr.ece.pfe_project.model;

import java.awt.image.BufferedImage;

/**
 *
 * @author pierreghazal
 */
public class Plan {

    private BufferedImage image;
    private String name;

    public Plan() {
        this(null, null);
    }

    public Plan(BufferedImage image, String name) {
        this.image = image;
        this.name = name;
    }

    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
