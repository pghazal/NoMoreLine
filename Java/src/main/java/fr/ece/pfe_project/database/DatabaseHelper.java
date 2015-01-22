package fr.ece.pfe_project.database;

import fr.ece.pfe_project.algo.Algorithm;
import fr.ece.pfe_project.model.Camera;
import fr.ece.pfe_project.model.CarnetAdresses;
import fr.ece.pfe_project.model.FrequentationAnnuelle;
import fr.ece.pfe_project.model.FrequentationJournaliere;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author pierreghazal
 */
public class DatabaseHelper {

    public final static String TABLE_FREQUENTATION_JOURNALIERE = "FREQUENTATION_JOURNALIERE";
    public final static String TABLE_FREQUENTATION_ANNUELLE = "FREQUENTATION_ANNUELLE";
    public final static String TABLE_CAMERA = "TABLE_CAMERA";
    public final static String TABLE_CARNET_ADRESSES = "TABLE_CARNET_ADRESSES";

    private static Connection connection = null;

    private static ArrayList<String> allPositionsPlan;

    public static ArrayList<String> getAllPositionsPlan() {
        return allPositionsPlan;
    }

    public static void setAllPositionsPlan(ArrayList<String> positionsPlan) {
        DatabaseHelper.allPositionsPlan = positionsPlan;
    }

    public DatabaseHelper() {
    }

    public static void initialize() {
        try {
            connection = getConnection();
            System.out.println("Opened database successfully");

            createTableFrequentationJournaliere();
            createTableFrequentationAnnuelle();
            createTableCamera();
            createTableCarnetAdresses();
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                Connection c = null;
                Class.forName("org.sqlite.JDBC");
                c = DriverManager.getConnection("jdbc:sqlite:nomoreline.db");

                return c;
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return connection;
    }

    private static void createTableFrequentationJournaliere() {
        try {
            Connection c = getConnection();

            Statement stmt = c.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_FREQUENTATION_JOURNALIERE
                    + " (JOUR INT NOT NULL,"
                    + " MOIS INT NOT NULL,"
                    + " ANNEE INT NOT NULL,"
                    + " FREQUENTATION INT NOT NULL)";
            stmt.execute(sql);
            stmt.close();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }

        System.out.println("Table " + TABLE_FREQUENTATION_JOURNALIERE + " created successfully");
    }

    private static void createTableFrequentationAnnuelle() {
        try {
            Connection c = getConnection();

            Statement stmt = c.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_FREQUENTATION_ANNUELLE
                    + " (ANNEE INT NOT NULL,"
                    + " FREQUENTATION INT NOT NULL)";
            stmt.execute(sql);
            stmt.close();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }

        System.out.println("Table " + TABLE_FREQUENTATION_ANNUELLE + " created successfully");
    }

    private static void createTableCamera() {
        try {
            Connection c = getConnection();

            Statement stmt = c.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_CAMERA
                    + " (ID INT NOT NULL,"
                    + " POSITION VARCHAR)";
            stmt.execute(sql);
            stmt.close();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }

        System.out.println("Table " + TABLE_CAMERA + " created successfully");
    }

    private static void createTableCarnetAdresses() {
        try {
            Connection c = getConnection();

            Statement stmt = c.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_CARNET_ADRESSES
                    + " (ID INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + " COMPAGNIE VARCHAR NOT NULL,"
                    + " GUICHET INT NOT NULL,"
                    + " ASSISTANCE VARCHAR NOT NULL,"
                    + " TELEPHONE VARCHAR NOT NULL)";
            stmt.execute(sql);
            stmt.close();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }

        System.out.println("Table " + TABLE_CARNET_ADRESSES + " created successfully");
    }

    public static ArrayList<String> getListePositionsCamera() {
        ArrayList<String> camPositions = new ArrayList<String>();

        try {
            Connection c = getConnection();

            Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM " + TABLE_CAMERA);

            //camPositions.add(" - ");
            while (rs.next()) {

                camPositions.add(rs.getString("POSITION"));
            }

            ArrayList<String> result = new ArrayList<String>(allPositionsPlan);

            for (String pos : camPositions) {
                result.remove(pos);
            }

            result.add(0, " - ");

            rs.close();
            stmt.close();
            c.close();

            return result;

        } catch (SQLException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }

        return null;
    }

    public static void addCamera(Camera camera) {
        try {
            Connection c = getConnection();
            c.setAutoCommit(false);

            String sql;
            if (!cameraExists(camera)) {

                sql = "INSERT INTO " + TABLE_CAMERA + "(ID, POSITION)"
                        + " VALUES(?,?)";

                PreparedStatement stmt = c.prepareStatement(sql);
                stmt.setInt(1, camera.getId());
                stmt.setString(2, camera.getPosition());
                stmt.executeUpdate();

                stmt.close();

                System.out.println("Add Camera success");
            }

            c.commit();
            c.setAutoCommit(true);
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }

    public static boolean cameraExists(Camera camera) {
        Camera cam = getCamera(camera);

        if (cam.getId() == null) {
            System.err.println(cam.getId() + " DOES NOT EXIST");

            return false;
        }

        System.err.println(cam.getId() + " DOES EXIST");

        return true;
    }

    public static Camera getCamera(Camera camera) {
        try {
            Connection c = getConnection();

            Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM " + TABLE_CAMERA
                    + " WHERE ID=" + camera.getId() + " LIMIT 1;");

            Camera cam = new Camera();

            while (rs.next()) {

                cam.setId(rs.getInt("ID"));
                cam.setPosition(rs.getString("POSITION"));
            }

            rs.close();
            stmt.close();
            c.close();

            return cam;

        } catch (SQLException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }

        return null;
    }

    public static void addCarnetAdresses(CarnetAdresses carnet) {
        try {
            Connection c = getConnection();
            c.setAutoCommit(false);

            String sql;
            if (!carnetAdresseExists(carnet)) {

                sql = "INSERT INTO " + TABLE_CARNET_ADRESSES + "(COMPAGNIE, GUICHET, ASSISTANCE, TELEPHONE)"
                        + " VALUES(?,?,?,?)";

                PreparedStatement stmt = c.prepareStatement(sql);
                stmt.setString(1, carnet.getCompagnieca().trim().toUpperCase());
                stmt.setInt(2, carnet.getNombreGuichet());
                stmt.setString(3, carnet.getSocieteAssistance().trim().toUpperCase());
                stmt.setString(4, carnet.getTelephone());

                stmt.executeUpdate();

                stmt.close();

                System.out.println("Add Carnet success");
            }

            c.commit();
            c.setAutoCommit(true);
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }

    public static boolean carnetAdresseExists(CarnetAdresses carnet) {
        CarnetAdresses cam = getCarnetAdresses(carnet);

        if (cam.getId() == null) {
            System.err.println(cam.getCompagnieca() + " DOES NOT EXIST");

            return false;
        }

        System.err.println(cam.getCompagnieca() + " DOES EXIST");

        return true;
    }

    public static CarnetAdresses getCarnetAdresses(CarnetAdresses carn) {
        try {
            Connection c = getConnection();

            System.err.println("YOLO");

            String sql = "SELECT * FROM " + TABLE_CARNET_ADRESSES
                    + " WHERE COMPAGNIE=\"" + carn.getCompagnieca().toUpperCase()
                    + "\" AND GUICHET=" + carn.getNombreGuichet()
                    + " AND ASSISTANCE=\"" + carn.getSocieteAssistance().toUpperCase()
                    + "\" AND TELEPHONE=\"" + carn.getTelephone()
                    + "\" LIMIT 1;";

            System.out.println(sql);

            PreparedStatement stmt = c.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            CarnetAdresses carnet = new CarnetAdresses();

            while (rs.next()) {

                carnet.setCompagnieca(rs.getString("COMPAGNIE"));
                carnet.setId(rs.getInt("ID"));
                carnet.setNombreGuichet(rs.getInt("GUICHET"));
                carnet.setSocieteAssistance(rs.getString("ASSISTANCE"));
                carnet.setTelephone(rs.getString("TELEPHONE"));
            }

            rs.close();
            stmt.close();
            c.close();

            return carnet;

        } catch (SQLException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }

        return null;
    }

    public static void addFrequentationJournaliere(Date date, Integer freq) {
        try {
            Connection c = getConnection();
            c.setAutoCommit(false);

            String sql;
            if (!frequentationJournaliereExists(date)) {

                int jour = Algorithm.getDayOfMonth(date);
                int mois = Algorithm.getMonth(date);
                int annee = Algorithm.getYear(date);

                sql = "INSERT INTO " + TABLE_FREQUENTATION_JOURNALIERE + " (JOUR, MOIS, ANNEE, FREQUENTATION)"
                        + " VALUES (" + jour + "," + mois + "," + annee + "," + freq + ")";

                Statement stmt = c.createStatement();
                stmt.executeUpdate(sql);

                stmt.close();

                System.out.println("Add Journalier success");
            }

            c.commit();
            c.setAutoCommit(true);
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }

    public static boolean frequentationJournaliereExists(Date date) {
        FrequentationJournaliere fj = getFrequentationJournaliere(date);

        if (fj.getFrequentation() == null || fj.getDate() == null) {
            System.err.println(date + " DOES NOT EXIST");

            return false;
        }

        System.err.println(date + " DOES EXIST");

        return true;
    }

    /**
     * Permet d'obtenir l'écart entre l'année saisie par l'utilisateur et la
     * dernière année complète en base
     *
     * @param date
     * @return gap
     */
    public static int getGap(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        ArrayList<Integer> yearsComplete = getYearsComplete();

        if (yearsComplete != null && yearsComplete.size() > 0) {
            int lastYearComplete = Collections.max(yearsComplete);
            int yearUser = cal.get(Calendar.YEAR);

            return yearUser - lastYearComplete;
        }

        return 0;
    }

    public static boolean frequentationAnnuelleExists(Integer year) {
        FrequentationAnnuelle fa = getFrequentationAnnuelle(year);

        if (fa.getFrequentation() == null) {
            System.err.println(year + " DOES NOT EXIST");

            return false;
        }

        System.err.println(year + " DOES EXIST");

        return true;
    }

    public static int getLastFrequentationOfCompleteYear(Date date) {

        ArrayList<Integer> yearsComplete = getYearsComplete();
        Integer lastYearComplete = Collections.max(yearsComplete);

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.YEAR, lastYearComplete);

        FrequentationJournaliere freq = getFrequentationJournaliere(cal.getTime());

        System.out.println("Last Freq of Year " + lastYearComplete + " is : " + freq.getFrequentation());

        return freq.getFrequentation();
    }

    public static void addFrequentationAnnuelle(Integer year, Integer freq) {
        try {
            Connection c = getConnection();
            c.setAutoCommit(false);

            String sql;
            if (!frequentationAnnuelleExists(year)) {

                sql = "INSERT INTO FREQUENTATION_ANNUELLE (ANNEE, FREQUENTATION)"
                        + " VALUES (" + year + "," + freq + ")";

                Statement stmt = c.createStatement();
                stmt.executeUpdate(sql);

                stmt.close();
            }

            c.commit();
            c.setAutoCommit(true);
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("Add Annuelle success");
    }

    public static void updateFrequentationAnnuelle(Integer year, Integer freq) {
        try {
            Connection c = getConnection();
            c.setAutoCommit(false);

            String sql;
            if (frequentationAnnuelleExists(year)) {

                sql = "UPDATE FREQUENTATION_ANNUELLE SET FREQUENTATION=" + freq
                        + " WHERE ANNEE=" + year;

                Statement stmt = c.createStatement();
                stmt.executeUpdate(sql);

                stmt.close();
            }

            c.commit();
            c.setAutoCommit(true);
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("Update Annuelle success");
    }

    public static FrequentationJournaliere getFrequentationJournaliere(Date date) {
        try {
            Connection c = getConnection();

            int jour = Algorithm.getDayOfMonth(date);
            int mois = Algorithm.getMonth(date);
            int annee = Algorithm.getYear(date);

            Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM " + TABLE_FREQUENTATION_JOURNALIERE
                    + " WHERE JOUR=" + jour + " AND MOIS=" + mois + " AND ANNEE=" + annee + " LIMIT 1;");
            FrequentationJournaliere fj = new FrequentationJournaliere();

            Calendar cal = Calendar.getInstance();

            while (rs.next()) {

                cal.set(rs.getInt("ANNEE"), rs.getInt("MOIS"), rs.getInt("JOUR"), 0, 0, 0);

                Date dateResult = cal.getTime();
                int freq = rs.getInt("FREQUENTATION");

                System.out.println("DATE = " + dateResult);
                System.out.println("FREQ = " + freq);
                System.out.println();

                fj.setDate(dateResult);
                fj.setFrequentation(freq);
            }

            rs.close();
            stmt.close();
            c.close();

            return fj;

        } catch (SQLException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }

        return null;
    }

    public static boolean isYearComplete(int year) {

        try {
            Connection c = getConnection();

            Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM " + TABLE_FREQUENTATION_JOURNALIERE
                    + " WHERE JOUR=31 AND MOIS=11 AND ANNEE=" + year + ";");

            while (rs.next()) {
                rs.close();
                stmt.close();
                c.close();

                return true;
            }

            rs.close();
            stmt.close();
            c.close();

        } catch (SQLException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }

        return false;
    }

    public static FrequentationAnnuelle getFrequentationAnnuelle(Integer year) {
        try {
            Connection c = getConnection();

            Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM " + TABLE_FREQUENTATION_ANNUELLE + " WHERE ANNEE=" + year + " LIMIT 1;");
            FrequentationAnnuelle fa = new FrequentationAnnuelle();

            while (rs.next()) {
                int yearR = rs.getInt("ANNEE");
                int freq = rs.getInt("FREQUENTATION");

                System.out.println("ANNEE = " + yearR);
                System.out.println("FREQ = " + freq);
                System.out.println();

                fa.setYear(yearR);
                fa.setFrequentation(freq);
            }

            rs.close();
            stmt.close();
            c.close();

            return fa;

        } catch (SQLException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }

        return null;
    }

    public static void updateFrequentationJournaliere(Date date, Integer freq) {
        try {
            Connection c = getConnection();
            c.setAutoCommit(false);

            String sql;
            if (frequentationJournaliereExists(date)) {

                int jour = Algorithm.getDayOfMonth(date);
                int mois = Algorithm.getMonth(date);
                int annee = Algorithm.getYear(date);

                sql = "UPDATE FREQUENTATION_JOURNALIERE SET FREQUENTATION=" + freq
                        + " WHERE JOUR=" + jour + " AND MOIS=" + mois + " AND ANNEE=" + annee;

                Statement stmt = c.createStatement();
                stmt.executeUpdate(sql);

                stmt.close();
            }

            c.commit();
            c.setAutoCommit(true);
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("Update Journalier success");
    }

    public static void updateCamera(Camera camera) {
        try {
            Connection c = getConnection();
            c.setAutoCommit(false);

            String sql;
            if (cameraExists(camera)) {

                sql = "UPDATE " + TABLE_CAMERA + " SET POSITION=" + camera.getPosition()
                        + " WHERE ID=" + camera.getId();

                PreparedStatement stmt = c.prepareStatement(sql);
                stmt.executeUpdate();

                stmt.close();
            }

            c.commit();
            c.setAutoCommit(true);
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("Update Camera success");
    }

    public static void updateCarnetAdresses(CarnetAdresses carnet) {
        try {
            Connection c = getConnection();
            c.setAutoCommit(false);

            String sql;
            if (carnetAdresseExists(carnet)) {

                sql = "UPDATE TABLE_CARNET_ADRESSES SET COMPAGNIE=" + carnet.getCompagnieca() + " AND GUICHET="
                        + carnet.getNombreGuichet() + " AND ASSISTANCE=" + carnet.getSocieteAssistance()
                        + " AND TELEPHONE=" + carnet.getTelephone()
                        + " WHERE ID=" + carnet.getId();

                Statement stmt = c.createStatement();
                stmt.executeUpdate(sql);

                stmt.close();
            }

            c.commit();
            c.setAutoCommit(true);
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("Update Carnet success");
    }

    public static int aggregateFrequentationOfYear(int year) {
        try {
            Connection c = getConnection();

            Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT SUM(FREQUENTATION) AS SOMME FROM " + TABLE_FREQUENTATION_JOURNALIERE
                    + " WHERE ANNEE=" + year + ";");

            int sumFreq = 0;
            while (rs.next()) {
                sumFreq = rs.getInt("SOMME");

                System.out.println("SOMME : " + sumFreq);
            }

            rs.close();
            stmt.close();
            c.close();

            return sumFreq;

        } catch (SQLException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }

        return -1;
    }

    public static HashMap<Integer, Integer> getAllFrequentationAnnuelle() {
        try {
            HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();

            Connection c = getConnection();

            Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM " + TABLE_FREQUENTATION_ANNUELLE + ";");

            while (rs.next()) {

                int yearR = rs.getInt("ANNEE");
                int freq = rs.getInt("FREQUENTATION");

                System.out.println("WE GOT : " + yearR);

                map.put(yearR, freq);
            }

            rs.close();
            stmt.close();
            c.close();

            return map;

        } catch (SQLException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }

        return null;
    }

    public static ArrayList<Integer> getYearsComplete() {
        Calendar cal = Calendar.getInstance();
        int actualYear = cal.get(Calendar.YEAR);

        ArrayList<Integer> list = new ArrayList<Integer>();

        try {
            Connection c = getConnection();

            Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT ANNEE FROM " + TABLE_FREQUENTATION_JOURNALIERE
                    + " WHERE JOUR=31 AND MOIS=11 AND ANNEE<>" + actualYear + ";");

            while (rs.next()) {
                int nbYear = rs.getInt("ANNEE");

                System.out.println("ANNEE : " + nbYear);

                list.add(nbYear);
            }

            rs.close();
            stmt.close();
            c.close();

            return list;

        } catch (SQLException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }

        return null;
    }

    public static ArrayList<Integer> getYearsCompletePlusActual() {
        Calendar cal = Calendar.getInstance();
        int actualYear = cal.get(Calendar.YEAR);

        ArrayList<Integer> list = new ArrayList<Integer>();

        try {
            Connection c = getConnection();

            Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT ANNEE FROM " + TABLE_FREQUENTATION_JOURNALIERE
                    + " WHERE JOUR=31 AND MOIS=11 AND ANNEE<>" + actualYear + ";");

            while (rs.next()) {
                int nbYear = rs.getInt("ANNEE");

                System.out.println("ANNEE : " + nbYear);

                list.add(nbYear);
            }

            rs.close();
            stmt.close();
            c.close();

            list.add(actualYear);

            return list;

        } catch (SQLException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }

        return null;
    }

    public static HashMap<Date, FrequentationJournaliere> getAllFrequentationJournaliere() {
        try {
            HashMap<Date, FrequentationJournaliere> map = new HashMap<Date, FrequentationJournaliere>();

            Connection c = getConnection();

            Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM " + TABLE_FREQUENTATION_JOURNALIERE + " ORDER BY ANNEE, MOIS, JOUR ASC;");

            Calendar cal = Calendar.getInstance();

            while (rs.next()) {

                cal.set(rs.getInt("ANNEE"), rs.getInt("MOIS"), rs.getInt("JOUR"), 0, 0, 0);

                Date date = cal.getTime();
                int freq = rs.getInt("FREQUENTATION");

                map.put(date, new FrequentationJournaliere(date, freq));
            }

            rs.close();
            stmt.close();
            c.close();

            return map;

        } catch (SQLException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }

        return null;
    }

    public static ArrayList<CarnetAdresses> getAllCarnetAdresses() {
        try {
            ArrayList<CarnetAdresses> carnet = new ArrayList<CarnetAdresses>();

            Connection c = getConnection();

            Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM " + TABLE_CARNET_ADRESSES + " ORDER BY ID;");

            while (rs.next()) {
                String compagnie = rs.getString("COMPAGNIE");
                String assistance = rs.getString("ASSISTANCE");
                String telephone = rs.getString("TELEPHONE");
                int guichet = rs.getInt("GUICHET");
                Integer id = rs.getInt("ID");

                carnet.add(new CarnetAdresses(compagnie, guichet, assistance, telephone, id));
            }

            rs.close();
            stmt.close();
            c.close();

            return carnet;

        } catch (SQLException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }

        return null;
    }

    public static void deleteFrequentationJournaliere(Date date) {

        try {
            Connection c = getConnection();
            c.setAutoCommit(false);
            PreparedStatement stmt = null;

            int jour = Algorithm.getDayOfMonth(date);
            int mois = Algorithm.getMonth(date);
            int annee = Algorithm.getYear(date);

            String sql = "DELETE FROM " + TABLE_FREQUENTATION_JOURNALIERE
                    + " WHERE JOUR=" + jour + " AND MOIS=" + mois + " AND ANNEE=" + annee + ";";
            System.out.println(sql);

            stmt = c.prepareStatement(sql);

            stmt.executeUpdate();
            stmt.close();

            c.commit();
            c.setAutoCommit(true);
            c.close();

            System.out.println("Operation done successfully");

        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }

    public static void deleteFrequentationAnnuelle(Integer year) {

        try {
            Connection c = getConnection();
            c.setAutoCommit(false);

            Statement stmt = null;

            stmt = c.createStatement();
            String sql = "DELETE FROM " + TABLE_FREQUENTATION_ANNUELLE + " WHERE ANNEE=" + year + ";";
            stmt.executeUpdate(sql);
            stmt.close();

            c.commit();
            c.setAutoCommit(true);
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }

        System.out.println("Operation done successfully");
    }

    public static void deleteAllFrequentationJournalier() {
        try {
            Connection c = getConnection();
            c.setAutoCommit(false);

            Statement stmt = null;

            stmt = c.createStatement();
            String sql = "DELETE FROM " + TABLE_FREQUENTATION_JOURNALIERE + ";";
            stmt.executeUpdate(sql);
            stmt.close();

            c.commit();
            c.setAutoCommit(true);
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }

        System.out.println("Operation done successfully");
    }

    public static void deleteAllFrequentationAnnuelle() {
        try {
            Connection c = getConnection();
            c.setAutoCommit(false);

            Statement stmt = null;

            stmt = c.createStatement();
            String sql = "DELETE FROM " + TABLE_FREQUENTATION_ANNUELLE + ";";
            stmt.executeUpdate(sql);
            stmt.close();

            c.commit();
            c.setAutoCommit(true);
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }

        System.out.println("Operation done successfully");
    }

    public static void deleteCarnetAdresse(CarnetAdresses carnet) {

        try {
            Connection c = getConnection();
            c.setAutoCommit(false);
            PreparedStatement stmt = null;

            String sql = "DELETE FROM " + TABLE_CARNET_ADRESSES
                    + " WHERE ID=" + carnet.getId();
            System.out.println(sql);

            stmt = c.prepareStatement(sql);

            stmt.executeUpdate();
            stmt.close();

            c.commit();
            c.setAutoCommit(true);
            c.close();

            System.out.println("Operation done successfully");

        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }
}
