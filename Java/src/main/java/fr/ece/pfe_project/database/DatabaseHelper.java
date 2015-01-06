package fr.ece.pfe_project.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author pierreghazal
 */
public class DatabaseHelper {

    public final static String TABLE_FREQUENTATION_JOURNALIERE = "FREQUENTATION_JOURNALIERE";
    public final static String TABLE_FREQUENTATION_ANNUELLE = "FREQUENTATION_ANNUELLE";

    private Connection connection;

    public DatabaseHelper() {

        try {
            this.connection = getConnection();
            System.out.println("Opened database successfully");
            
            createTableFrequentationJournaliere();
            createTableFrequentationAnnuelle();
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
    }

    public final Connection getConnection() throws SQLException {
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

    private void createTableFrequentationJournaliere() {
        try {
            Statement stmt = getConnection().createStatement();
            String sql = "CREATE TABLE " + TABLE_FREQUENTATION_JOURNALIERE
                    + " (DATE INT PRIMARY KEY NOT NULL," // Modifier INT par DATE
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

    private void createTableFrequentationAnnuelle() {
        try {
            Statement stmt = getConnection().createStatement();
            String sql = "CREATE TABLE " + TABLE_FREQUENTATION_ANNUELLE
                    + " (ANNEE INT PRIMARY KEY NOT NULL,"
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
}
