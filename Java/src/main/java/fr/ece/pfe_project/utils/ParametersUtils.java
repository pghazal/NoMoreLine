package fr.ece.pfe_project.utils;

import fr.ece.pfe_project.database.DatabaseHelper;
import fr.ece.pfe_project.panel.ParametersDialog;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author pierreghazal
 */
public class ParametersUtils {

    private final static ParametersUtils INSTANCE = new ParametersUtils();
    private final static String FILENAME_PARAMETERS = "parameters.pfe";

    public final static String PARAM_JOURS_FERIES = "PARAM_JOURS_FERIES";
    public final static String PARAM_SUEIL_JOUR = "PARAM_SUEIL_JOUR";
    public final static String PARAM_SUEIL_CAMERA = "PARAM_SUEIL_CAMERA";

    private static HashMap<String, Object> paramsMap;

    private ParametersUtils() {
        loadParameters();
    }

    /**
     * Point d'accès pour l'instance unique du singleton
     *
     * @return INSTANCE
     */
    public static ParametersUtils getInstance() {
        return INSTANCE;
    }

    public static void set(String key, Object value) {
        paramsMap.put(key, value);
    }

    public static Object get(String key) {
        if (paramsMap.containsKey(key)) {
            return paramsMap.get(key);
        }

        return null;
    }

    public static boolean containsKey(String key) {
        return paramsMap.containsKey(key);
    }

    public static void saveParameters() {
        try {
            FileOutputStream fos
                    = new FileOutputStream(FILENAME_PARAMETERS);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(paramsMap);
            oos.close();
            fos.close();
            System.out.printf("Serialized HashMap data is saved in parameters.pfe");
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    private static void loadDefaultParameters() {
        ParametersUtils.set(ParametersUtils.PARAM_SUEIL_JOUR, ParametersDialog.DEFAULT_SEUIL_JOUR);
        ParametersUtils.set(ParametersUtils.PARAM_SUEIL_CAMERA, ParametersDialog.DEFAULT_SEUIL_CAMERA);

        // Saving parameters in file parameter
        ParametersUtils.saveParameters();
    }

    private static void loadParameters() {
        try {
            FileInputStream fis = new FileInputStream(FILENAME_PARAMETERS);
            ObjectInputStream ois = new ObjectInputStream(fis);
            paramsMap = (HashMap) ois.readObject();
            ois.close();
            fis.close();
        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
            paramsMap = new HashMap<String, Object>();
            System.out.println("HashMap does not exist, allocating memory...");
            loadDefaultParameters();
            return;
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return;
        } catch (ClassNotFoundException c) {
            System.out.println("Class not found");
            c.printStackTrace();
            return;
        }

        System.out.println("Deserialized HashMap...");
    }

    public static void loadDatabase() {

        // Si l'année precente est complete, agrege et ajouter à la BDD
        ArrayList<Integer> yearsComplete = DatabaseHelper.getYearsComplete();
        for (Integer year : yearsComplete) {
            DatabaseHelper.aggregateFrequentationOfYear(year);
        }

        GlobalVariableUtils.getFrequentationAnnuelleMap().
                putAll(DatabaseHelper.getAllFrequentationAnnuelle());

        GlobalVariableUtils.getExcelMap().
                putAll(DatabaseHelper.getAllFrequentationJournaliere());

        GlobalVariableUtils.getCarnetA().addAll(DatabaseHelper.getAllCarnetAdresses());
    }
}
