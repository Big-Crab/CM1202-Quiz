package database;

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

    public static void addIncorrect() {
        numAnswered++;
    }

    public static void writeToDB() {
        //TODO
        // Should get school ID and year and write the answers/corrects to DB
    }

    public static void clear() {
        numAnswered = 0;
        numCorrect = 0;
        selectedSchoolID = -1;
        selectedSchoolYear = -1;
    }
}
