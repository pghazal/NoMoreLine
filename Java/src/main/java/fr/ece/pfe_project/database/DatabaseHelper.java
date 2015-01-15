package fr.ece.pfe_project.database;

import fr.ece.pfe_project.algo.Algorithm;
import fr.ece.pfe_project.model.FrequentationAnnuelle;
import fr.ece.pfe_project.model.FrequentationJournaliere;
import java.sql.Connection;
import java.sql.DriverManager;
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

    private static Connection connection;

    public DatabaseHelper() {
    }

    public static void initialize() {

        try {
            connection = getConnection();
            System.out.println("Opened database successfully");

            createTableFrequentationJournaliere();
            createTableFrequentationAnnuelle();
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            Connection c = null;
            try {
                Class.forName("org.sqlite.JDBC");
                c = DriverManager.getConnection("jdbc:sqlite:nomoreline.db");
            } catch (Exception e) {
                System.err.println(e.getClass().getName() + ": " + e.getMessage());
                System.exit(0);
            }

            return c;
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
            stmt.executeUpdate(sql);
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
            stmt.executeUpdate(sql);
            stmt.close();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }

        System.out.println("Table " + TABLE_FREQUENTATION_ANNUELLE + " created successfully");
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
        int lastYearComplete = Collections.max(yearsComplete);
        int yearUser = cal.get(Calendar.YEAR);

        return yearUser - lastYearComplete;
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
            ResultSet rs = stmt.executeQuery("SELECT * FROM " + TABLE_FREQUENTATION_JOURNALIERE + ";");

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

    public static void deleteFrequentationJournaliere(Date date) {

        try {
            Connection c = getConnection();
            c.setAutoCommit(false);

            Statement stmt = null;

            int jour = Algorithm.getDayOfMonth(date);
            int mois = Algorithm.getMonth(date);
            int annee = Algorithm.getYear(date);

            stmt = c.createStatement();
            String sql = "DELETE FROM " + TABLE_FREQUENTATION_JOURNALIERE
                    + " WHERE JOUR=" + jour + " AND MOIS=" + mois + " AND ANNEE=" + annee + ";";
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
}
