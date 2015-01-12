package fr.ece.pfe_project.database;

import fr.ece.pfe_project.model.FrequentationAnnuelle;
import fr.ece.pfe_project.model.FrequentationJournaliere;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.HashMap;

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
            System.err.println(ex.getMessage());
        }
    }

    private final static Connection getConnection() throws SQLException {
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
            Statement stmt = getConnection().createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_FREQUENTATION_JOURNALIERE
                    // + " (ID INT PRIMARY KEY NOT NULL AUTOINCREMENT,"
                    + " (DATE DATE NOT NULL,"
                    + " FREQUENTATION INT NOT NULL)";
            stmt.executeUpdate(sql);
            stmt.close();
            getConnection().close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }

        System.out.println("Table " + TABLE_FREQUENTATION_JOURNALIERE + " created successfully");
    }

    private static void createTableFrequentationAnnuelle() {
        try {
            Statement stmt = getConnection().createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_FREQUENTATION_ANNUELLE
                    //+ " (ID INT PRIMARY KEY NOT NULL AUTOINCREMENT,"
                    + " (ANNEE INT NOT NULL,"
                    + " FREQUENTATION INT NOT NULL)";
            stmt.executeUpdate(sql);
            stmt.close();
            getConnection().close();
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

//            String sql = "INSERT INTO " + TABLE_FREQUENTATION_JOURNALIERE
//                    + " (DATE, FREQUENTATION)"
//                    + " VALUES (" + date + "," + freq + ")"
//                    + " WHERE NOT EXISTS (SELECT *"
//                    + " FROM " + TABLE_FREQUENTATION_JOURNALIERE
//                    + " WHERE DATE =" + date + ");";
            String sql;
            if (!frequentationJournaliereExists(date)) {

                sql = "INSERT INTO " + TABLE_FREQUENTATION_JOURNALIERE + " (DATE, FREQUENTATION)"
                        + " VALUES (" + date + "," + freq + ")";

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
        System.out.println("Add Journalier success");
    }

    public static boolean frequentationJournaliereExists(Date date) {
        FrequentationJournaliere fj = getFrequentationJournaliere(date);

        if (fj.getFrequentation() == null) {
            System.err.println(date + " DOES NOT EXIST");

            return false;
        }

        System.err.println(date + " DOES EXIST");

        return true;
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

    public static void addFrequentationAnnuelle(Integer year, Integer freq) {
        try {
            Connection c = getConnection();
            c.setAutoCommit(false);

//            String sql = "IF NOT EXISTS (SELECT * FROM " + TABLE_FREQUENTATION_ANNUELLE
//                    + " WHERE ANNEE=" + year + ")"
//                    + " Begin INSERT INTO " + TABLE_FREQUENTATION_ANNUELLE
//                    + " (ANNEE, FREQUENTATION) values(" + year + "," + freq + ")"
//                    + " End";
//            String sql = "INSERT INTO " + TABLE_FREQUENTATION_ANNUELLE
//                    + " (ANNEE, FREQUENTATION)"
//                    + " VALUES (" + year + "," + freq + ")"
//                    + " ;";
//            String sql = "IF EXISTS (SELECT * FROM FREQUENTATION_ANNUELLE WHERE ANNEE=" + year + ")"
//                    + " UPDATE FREQUENTATION_ANNUELLE SET FREQUENTATION=" + freq + " WHERE ANNEE=" + year
//                    + " ELSE "
//                    + " INSERT INTO FREQUENTATION_ANNUELLE (ANNEE, FREQUENTATION)"
//                    + " VALUES (" + year + "," + freq + ")";
//            if (frequentationAnnuelleExists(year)) {
//                sql = "UPDATE FREQUENTATION_ANNUELLE SET FREQUENTATION=" + freq + " WHERE ANNEE=" + year;
//            } else {
//                sql = "INSERT INTO FREQUENTATION_ANNUELLE (ANNEE, FREQUENTATION)"
//                        + " VALUES (" + year + "," + freq + ")";
//            }
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

                sql = "UPDATE FREQUENTATION_ANNUELLE SET FREQUENTATION=" + freq + " WHERE ANNEE=" + year;

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

            Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM " + TABLE_FREQUENTATION_JOURNALIERE + " WHERE DATE =" + date + " LIMIT 1;");
            FrequentationJournaliere fj = new FrequentationJournaliere();

            while (rs.next()) {
                Date dateResult = rs.getDate("DATE");
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

                sql = "UPDATE FREQUENTATION_JOURNALIERE SET FREQUENTATION=" + freq + " WHERE DATE=" + date;

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

    public static HashMap<Integer, Integer> getAllFrequentationAnnuelle() {
        try {
            HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();

            Connection c = getConnection();

            Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM " + TABLE_FREQUENTATION_ANNUELLE + ";");

            while (rs.next()) {

                int yearR = rs.getInt("ANNEE");
                int freq = rs.getInt("FREQUENTATION");

//                System.out.println("ANNEE = " + yearR);
//                System.out.println("FREQ = " + freq);
//                System.out.println();
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

    public static HashMap<Date, FrequentationJournaliere> getAllFrequentationJournaliere() {
        try {
            HashMap<Date, FrequentationJournaliere> map = new HashMap<Date, FrequentationJournaliere>();

            Connection c = getConnection();

            Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM " + TABLE_FREQUENTATION_JOURNALIERE + ";");

            while (rs.next()) {

                Date date = rs.getDate("DATE");
                int freq = rs.getInt("FREQUENTATION");

                System.out.println("DATE = " + date);
                System.out.println("FREQ = " + freq);
                System.out.println();

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

            stmt = c.createStatement();
            String sql = "DELETE FROM " + TABLE_FREQUENTATION_JOURNALIERE + " WHERE DATE=" + date + ";";
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
