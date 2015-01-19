package fr.ece.pfe_project.interfaces;

/**
 *
 * @author pierreghazal
 */
public interface ToolbarEntityListener {

    public enum ENTITY {

        NONE, CAMERA, EXCELROW, LISTINGVOLS, CARNETADRESSE
    }

    public void entityHasChange(ENTITY typeEntity);
}
