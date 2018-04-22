package database;

import javax.xml.crypto.Data;
import java.sql.ResultSet;
import java.sql.SQLException;

public class QuizStatisticRecorder {
    // If the selected ID for something is 0, DON'T RECORD STATS FOR IT

    static private int selectedTheme,
            selectedSchoolID,
            selectedSchoolYear,
            numCorrect,
            numAnswered;

    public static int getSelectedTheme() {
        return selectedTheme;
    }

    public static void setSelectedTheme(int selectedTheme) {
        QuizStatisticRecorder.selectedTheme = selectedTheme;
    }

    public static int getSelectedSchoolID() {
        return selectedSchoolID;
    }

    public static void setSelectedSchoolID(int selectedSchoolID) {
        QuizStatisticRecorder.selectedSchoolID = selectedSchoolID;
    }

    public static int getSelectedSchoolYear() {
        return selectedSchoolYear;
    }

    public static void setSelectedSchoolYear(int selectedSchoolYear) {
        QuizStatisticRecorder.selectedSchoolYear = selectedSchoolYear;
    }

    public static void addCorrect() {
        numCorrect++;
        addIncorrect();
    }

    // The same as adding a blank value
    public static void addIncorrect() {
        numAnswered++;
    }

    public static void writeToDB() {
        //TODO
        // Should get school ID and year and write the answers/corrects to DB
        if(selectedSchoolYear > 0 && selectedSchoolID > 0) {
            //"INSERT INTO ANSWERS VALUES (DEFAULT, 'Option A', 0, (select ID from QUESTIONS where ID=1))",
            //"CREATE TABLE YEARS(ID int auto_increment primary key, year int, correctlyAnswered int default 0, totalAnswered int default 0, SCHOOLID int, FOREIGN KEY (SCHOOLID) REFERENCES SCHOOLS(ID))",
            try {
                DatabaseManager dbm = DatabaseManager.getDBM();
                dbm.executeCommand("UPDATE YEARS SET correctlyAnswered=correctlyAnswered+"+numCorrect+", totalAnswered=totalAnswered+"+numAnswered+" WHERE year="+selectedSchoolYear+" AND "+"SCHOOLID="+selectedSchoolID);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void clear() {
        numAnswered = 0;
        numCorrect = 0;
        selectedSchoolID = -1;
        selectedSchoolYear = -1;
    }
}
