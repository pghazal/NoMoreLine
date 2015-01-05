package fr.ece.pfe_project.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

/**
 *
 * @author pierreghazal
 */
public class ParametersUtils {

    private final static ParametersUtils INSTANCE = new ParametersUtils();
    private final static String FILENAME_PARAMETERS = "parameters.pfe";

    public final static String PARAM_PATH_EXCEL = "PARAM_PATH_EXCEL";

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
}