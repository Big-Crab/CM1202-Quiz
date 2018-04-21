package database;

public class QuizStatisticRecorder {
    static private int selectedSchoolID,
    selectedSchoolYear,
    numCorrect,
    numAnswered;

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
    }

    public static void clear() {
        numAnswered = 0;
        numCorrect = 0;
        selectedSchoolID = -1;
        selectedSchoolYear = -1;
    }
}
