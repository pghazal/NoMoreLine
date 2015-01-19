package fr.ece.pfe_project.interfaces;

/**
 *
 * @author pierreghazal
 */
public interface ToolbarActionsListener {
    
    public final static int ACTION_ADD = 0;
    public final static int ACTION_DELETE = 1;
    public final static int ACTION_EDIT = 2;

    public void performAction(int action);
}
