package database;

import common.Quiz;
import common.QuizPane;
import common.Theme;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.h2.command.dml.Delete;
import org.h2.tools.DeleteDbFiles;

import javax.swing.plaf.nimbus.State;

public final class DatabaseManager {
    private static DatabaseManager instance;
    private static final String DB_DRIVER = "org.h2.Driver";
    private static final String DB_CONNECTION = "jdbc:h2:./database";
    private static final String DB_USER = "";
    private static final String DB_PASSWORD = "";

    private Quiz quizContent;

    private DatabaseManager() {
        DeleteDbFiles.execute("./","database", true);
        try {
            initialiseDB();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates tables with blank data
     */
    public void initialiseDB() throws SQLException{
        Connection connection = getDBConnection();

        String[] initQueries = {
                "CREATE TABLE THEMES(ID int auto_increment primary key, themeName varchar(255))",
                "CREATE TABLE QUESTIONS(ID int auto_increment primary key, content varchar(255), THEMEID int, foreign key (THEMEID) references THEMES(ID))",
                "CREATE TABLE ANSWERS(ID int auto_increment primary key, content varchar(255), correct bit default 0, QUESTIONID int, foreign key (QUESTIONID) references THEMES(ID))",
                "CREATE TABLE SCHOOLS(ID int auto_increment primary key, name varchar(255))",
                "CREATE TABLE YEARS(ID int auto_increment primary key, year int, correctlyAnswered int default 0, totalAnswered int default 0, SCHOOLID int, foreign key (SCHOOLID) references SCHOOLS(ID))"
        };
        executeList(initQueries);

        // TEST DATA ONLY
        String[] insertQueries = { // Use DEFAULT for the autoincrement fields, or simply specify all other fields than the autoincrement ones in the TABLE(value1, value2, ...)
                "INSERT INTO THEMES VALUES (DEFAULT, 'Default Theme')",
                "INSERT INTO QUESTIONS VALUES (DEFAULT, 'Which of the following languages is Czech?', 0)",
                "INSERT INTO ANSWERS VALUES (DEFAULT, 'Option A', 0, 0)",
                "INSERT INTO ANSWERS VALUES (DEFAULT, 'Möglichkeit B', 0, 0)",
                "INSERT INTO ANSWERS VALUES (DEFAULT, 'Opción C', 0, 0)",
                "INSERT INTO ANSWERS VALUES (DEFAULT, 'Επιλογή Δ', 0, 0)",
                "INSERT INTO ANSWERS VALUES (DEFAULT, 'Možnost E', 1, 0)",

                "INSERT INTO QUESTIONS VALUES (DEFAULT, 'Welches ist die Hauptstadt von Österreich?', 0)",
                "INSERT INTO ANSWERS VALUES (DEFAULT, 'Salzburg', 0, 0)",
                "INSERT INTO ANSWERS VALUES (DEFAULT, 'Wien', 1, 0)",
                "INSERT INTO ANSWERS VALUES (DEFAULT, 'München', 0, 0)",
                "INSERT INTO ANSWERS VALUES (DEFAULT, 'Innsbruck', 0, 0)",
                "INSERT INTO ANSWERS VALUES (DEFAULT, 'Jungholz', 0, 0)",

                "INSERT INTO QUESTIONS VALUES (DEFAULT, 'Who is the Prime Minister of Liechtenstein?', 0)",
                "INSERT INTO ANSWERS VALUES (DEFAULT, 'Thomas Zweifelhofer', 0, 0)",
                "INSERT INTO ANSWERS VALUES (DEFAULT, 'Klaus Tschütscher', 0, 0)",
                "INSERT INTO ANSWERS VALUES (DEFAULT, 'Sebastian Kurz', 0, 0)",
                "INSERT INTO ANSWERS VALUES (DEFAULT, 'Adrian Hasler', 1, 0)",
                "INSERT INTO ANSWERS VALUES (DEFAULT, 'Alexander Van der Bellen', 0, 0)",

                "INSERT INTO SCHOOLS(DEFAULT, 'State School A')",
                "INSERT INTO YEARS(DEFAULT, 7, 0, 0, 0)",
                "INSERT INTO YEARS(DEFAULT, 8, 0, 0, 0)",
                "INSERT INTO SCHOOLS(DEFAULT, 'Private School B')",
                "INSERT INTO YEARS(DEFAULT, 9, 0, 0, 1)",
                "INSERT INTO YEARS(DEFAULT, 10, 0, 0, 1)",
                "INSERT INTO SCHOOLS(DEFAULT, 'Boarding School C')",
                "INSERT INTO YEARS(DEFAULT, 9, 0, 0, 2)",
                "INSERT INTO YEARS(DEFAULT, 11, 0, 0, 2)",
        };
        //
    }

    private void executeList(String[] list) throws SQLException{
        Connection connection = getDBConnection();

        try {
            connection.setAutoCommit(false);
            Statement statement = connection.createStatement();
            for (String query : list) {
                statement.execute(query);
            }
            statement.close();
            connection.commit();
        } catch (SQLException e) {
            System.out.println("Exception Message " + e.getLocalizedMessage());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            connection.close();
        }
    }

    public static DatabaseManager getDBM() {
        if(instance == null) {
            DatabaseManager.instance = new DatabaseManager();
        }
        return DatabaseManager.instance;
    }

    public void insertNewTheme(String title) {
        Connection connection = getDBConnection();
        PreparedStatement createPreparedStatement = null;
        PreparedStatement insertPreparedStatement = null;
        PreparedStatement selectPreparedStatement = null;
    }

    /**
     * Provided a theme ID, will collate all of the Questions and Answers together into Panes, and then into a Quiz and return it.
     * @param themeSelected
     * @throws SQLException
     */
    public Quiz refreshQuizContent(int themeSelected) throws SQLException{
        // Iterate through DB, getting answers and questions together into QuizPanes
        // Get panes and collate into Quiz

        ArrayList<QuizPane> panes = new ArrayList<>();

        Connection connection = null;
        try {
            connection = getDBConnection();
            Statement statement = connection.createStatement();
            ResultSet questions = statement.executeQuery("SELECT content FROM QUESTIONS WHERE THEMEID='" + themeSelected + "'");
            System.out.println("Questions acquired from DB.");
            while(questions.next()) {
                System.out.println("Question: " + questions.getString("content"));
            }
            statement.close();
            connection.commit();

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            connection.close();
        }
    }

    public Quiz getQuizContent() {
        return quizContent;
    }













    private static Connection getDBConnection() {
        Connection dbConnection = null;
        try {
            Class.forName(DB_DRIVER);
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
        try {
            dbConnection = DriverManager.getConnection(DB_CONNECTION, DB_USER,
                    DB_PASSWORD);
            return dbConnection;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return dbConnection;
    }

    public static Quiz getTestData() {
        // Test data
        // 5th option, ID 4 is the correct answer
        return new Quiz(
                new QuizPane[] {
                        new QuizPane("Which of the following languages is Czech?", new String[]{"Option A", "Möglichkeit B", "Opción C", "Επιλογή Δ", "Možnost E"}, (byte) 4),
                        new QuizPane("Welches ist die Hauptstadt von Österreich?", new String[]{"Salzburg", "Wien", "München", "Innsbruck", "Jungholz"}, (byte) 1),
                        new QuizPane("Who is the Prime Minister of Liechtenstein?", new String[]{"Thomas Zweifelhofer", "Klaus Tschütscher", "Sebastian Kurz", "Adrian Hasler", "Alexander Van der Bellen"}, (byte) 3),
                }
        );
    }

    public static String[] getSchools() {
        // Test data
        return new String[] {
                "Default District School",
                "Emmbrook Secondary School",
                "Cardiff School of Schooling",
                "Michael School (New Jersey)"
        };
    }
}
