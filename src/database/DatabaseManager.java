package database;

import common.Quiz;
import common.QuizPane;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.h2.command.dml.Delete;
import org.h2.tools.DeleteDbFiles;

import javax.sql.rowset.CachedRowSet;
import javax.swing.plaf.nimbus.State;

public final class DatabaseManager {
    private static DatabaseManager instance;
    private static final String DB_DRIVER = "org.h2.Driver";
    private static final String DB_CONNECTION = "jdbc:h2:./database";
    private static final String DB_USER = "";
    private static final String DB_PASSWORD = "";

    private Quiz quizContent;

    private DatabaseManager() {
        // To reset database to template:
        /*DeleteDbFiles.execute("./","database", true);
        try {
            initialiseDB();
        } catch (SQLException e) {
            e.printStackTrace();
        }*/
        try {
            refreshQuizContent(1);
        } catch (SQLException e){
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
                "CREATE TABLE ANSWERS(ID int auto_increment primary key, content varchar(255), correct bit default 0, QUESTIONID int, FOREIGN KEY (QUESTIONID) REFERENCES QUESTIONS(ID))",
                "CREATE TABLE SCHOOLS(ID int auto_increment primary key, name varchar(255))",
                "CREATE TABLE YEARS(ID int auto_increment primary key, year int, correctlyAnswered int default 0, totalAnswered int default 0, SCHOOLID int, FOREIGN KEY (SCHOOLID) REFERENCES SCHOOLS(ID))",
        };
        executeList(initQueries);

        // TEST DATA ONLY
        String[] insertQueries = { // Use DEFAULT for the autoincrement fields, or simply specify all other fields than the autoincrement ones in the TABLE(value1, value2, ...)
                "INSERT INTO THEMES VALUES (DEFAULT, 'Default Theme')",

                "INSERT INTO QUESTIONS VALUES (DEFAULT, 'Which of the following languages is Czech?', (select ID from THEMES where themeName='Default Theme'))",
                "INSERT INTO ANSWERS VALUES (DEFAULT, 'Option A', 0, (select ID from QUESTIONS where ID=1))",
                "INSERT INTO ANSWERS VALUES (DEFAULT, 'Möglichkeit B', 0, (select ID from QUESTIONS where ID=1))",
                "INSERT INTO ANSWERS VALUES (DEFAULT, 'Opción C', 0, (select ID from QUESTIONS where ID=1))",
                "INSERT INTO ANSWERS VALUES (DEFAULT, 'Επιλογή Δ', 0, (select ID from QUESTIONS where ID=1))",
                "INSERT INTO ANSWERS VALUES (DEFAULT, 'Možnost E', 1, (select ID from QUESTIONS where ID=1))",

                "INSERT INTO QUESTIONS VALUES (DEFAULT, 'Welches ist die Hauptstadt von Österreich?', (select ID from THEMES where themeName='Default Theme'))",
                "INSERT INTO ANSWERS VALUES (DEFAULT, 'Salzburg', 0, (select ID from QUESTIONS where ID=2))",
                "INSERT INTO ANSWERS VALUES (DEFAULT, 'Wien', 1, (select ID from QUESTIONS where ID=2))",
                "INSERT INTO ANSWERS VALUES (DEFAULT, 'München', 0, (select ID from QUESTIONS where ID=2))",
                "INSERT INTO ANSWERS VALUES (DEFAULT, 'Innsbruck', 0, (select ID from QUESTIONS where ID=2))",
                "INSERT INTO ANSWERS VALUES (DEFAULT, 'Jungholz', 0, (select ID from QUESTIONS where ID=2))",

                "INSERT INTO QUESTIONS VALUES (DEFAULT, 'Who is the Prime Minister of Liechtenstein?', (select ID from THEMES where themeName='Default Theme'))",
                "INSERT INTO ANSWERS VALUES (DEFAULT, 'Thomas Zweifelhofer', 0, (select ID from QUESTIONS where ID=3))",
                "INSERT INTO ANSWERS VALUES (DEFAULT, 'Klaus Tschütscher', 0, (select ID from QUESTIONS where ID=3))",
                "INSERT INTO ANSWERS VALUES (DEFAULT, 'Sebastian Kurz', 0, (select ID from QUESTIONS where ID=3))",
                "INSERT INTO ANSWERS VALUES (DEFAULT, 'Adrian Hasler', 1, (select ID from QUESTIONS where ID=3))",
                "INSERT INTO ANSWERS VALUES (DEFAULT, 'Alexander Van der Bellen', 0, (select ID from QUESTIONS where ID=3))",

                "INSERT INTO SCHOOLS VALUES (DEFAULT, 'State School A')",
                "INSERT INTO YEARS VALUES (DEFAULT, 7, 0, 0, (select ID from SCHOOLS where ID=1))",
                "INSERT INTO YEARS VALUES (DEFAULT, 8, 0, 0, (select ID from SCHOOLS where ID=1))",
                "INSERT INTO SCHOOLS VALUES (DEFAULT, 'Private School B')",
                "INSERT INTO YEARS VALUES (DEFAULT, 9, 0, 0, (select ID from SCHOOLS where ID=2))",
                "INSERT INTO YEARS VALUES (DEFAULT, 10, 0, 0, (select ID from SCHOOLS where ID=2))",
                "INSERT INTO SCHOOLS VALUES (DEFAULT, 'Boarding School C')",
                "INSERT INTO YEARS VALUES (DEFAULT, 9, 0, 0, (select ID from SCHOOLS where ID=3))",
                "INSERT INTO YEARS VALUES (DEFAULT, 11, 0, 0, (select ID from SCHOOLS where ID=3))",
        };
        executeList(insertQueries);
    }

    public void executeCommand(String cmd) throws SQLException{
        Connection connection = getDBConnection();

        try {
            connection.setAutoCommit(false);
            Statement statement = connection.createStatement();
            statement.execute(cmd);
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

    private void executeList(String[] list) throws SQLException{
        Connection connection = getDBConnection();

        try {
            connection.setAutoCommit(false);
            Statement statement = connection.createStatement();
            for (String query : list) {
                statement.executeUpdate(query);
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
    public Quiz refreshQuizContent(int themeSelected) throws SQLException {
        // Iterate through DB, getting answers and questions together into QuizPanes
        // Get panes and collate into Quiz

        ArrayList<QuizPane> panes = new ArrayList<>();
        String sQuestion = null;
        ArrayList<String> sAnswers = new ArrayList<>();
        byte correctID = -1;

        Connection connection = null;
        try {
            connection = getDBConnection();
            Statement statementQuestions = connection.createStatement();
            ResultSet questions = statementQuestions.executeQuery("SELECT ID, content FROM QUESTIONS WHERE THEMEID='" + themeSelected + "'");
            System.out.println("Questions acquired from DB.");
            while(questions.next()) {
                System.out.println("Question: " + questions.getString("content") + ", Question ID: " + questions.getString("ID"));
                sQuestion = questions.getString("content");

                Statement statementAnswers = connection.createStatement();
                ResultSet answers = statementAnswers.executeQuery("SELECT ID, content, correct, QUESTIONID FROM ANSWERS WHERE QUESTIONID='" + questions.getInt("ID") + "'");

                int i = 0;
                while(answers.next()) {
                    System.out.println("Answer: " + answers.getString("content") + ", Answer ID: " + answers.getString("ID"));
                    sAnswers.add(answers.getString("content"));

                    //Bit gets interpreted as bool here
                    if(answers.getBoolean("correct")) {
                        correctID = (byte) i;
                    }
                    i++;
                }

                //Now that question, list of answers, and correct index have been set, form them into a QuizPane
                panes.add(new QuizPane(sQuestion, sAnswers.toArray(new String[sAnswers.size()]), correctID));
                sAnswers.clear();
            }

            Quiz result = new Quiz(panes.toArray(new QuizPane[panes.size()]));
            statementQuestions.close();
            connection.commit();
            quizContent = result;
            return result;

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

//    public static Quiz getTestData() {
//        // Test data
//        // 5th option, ID 4 is the correct answer
//        return new Quiz(
//                new QuizPane[] {
//                        new QuizPane("Which of the following languages is Czech?", new String[]{"Option A", "Möglichkeit B", "Opción C", "Επιλογή Δ", "Možnost E"}, (byte) 4),
//                        new QuizPane("Welches ist die Hauptstadt von Österreich?", new String[]{"Salzburg", "Wien", "München", "Innsbruck", "Jungholz"}, (byte) 1),
//                        new QuizPane("Who is the Prime Minister of Liechtenstein?", new String[]{"Thomas Zweifelhofer", "Klaus Tschütscher", "Sebastian Kurz", "Adrian Hasler", "Alexander Van der Bellen"}, (byte) 3),
//                }
//        );
//    }

    public String[] getSchools() throws SQLException{
        ArrayList<String> sSchools = new ArrayList<>();
        Connection connection = null;
        try {
            connection = getDBConnection();
            Statement statementSchools = connection.createStatement();
            ResultSet schools = statementSchools.executeQuery("SELECT * FROM SCHOOLS");
            System.out.println("Schools acquired from DB.");
            while(schools.next()) {
                sSchools.add(schools.getString("name"));
            }
            statementSchools.close();
            connection.commit();
            return sSchools.toArray(new String[sSchools.size()]);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            connection.close();
        }
    }

    public Integer[] getYears(int ID) throws SQLException{
        ArrayList<Integer> iYears = new ArrayList<>();
        Connection connection = null;
        try {
            connection = getDBConnection();
            Statement statementYears = connection.createStatement();
            ResultSet schools = statementYears.executeQuery("SELECT * FROM YEARS WHERE SCHOOLID = "+ID);
            System.out.println("Years acquired from DB.");
            while(schools.next()) {
                iYears.add(schools.getInt("year"));
            }
            statementYears.close();
            connection.commit();
            return iYears.toArray(new Integer[iYears.size()]);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            connection.close();
        }
    }
}
