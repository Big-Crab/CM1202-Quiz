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

    public static int getTotal() {
        return numAnswered;
    }

    public static int getCorrect() {
        return numCorrect;
    }

    public static void writeToDB() {
        if(selectedSchoolYear > 0 && selectedSchoolID > 0) {
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
