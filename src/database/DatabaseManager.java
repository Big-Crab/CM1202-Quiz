package database;

import common.Quiz;
import common.QuizPane;

public final class DatabaseManager {
    private static DatabaseManager instance;

    private DatabaseManager() {

    }

    public static DatabaseManager getDBM() {
        if(instance == null) {
            DatabaseManager.instance = new DatabaseManager();
        }
        return DatabaseManager.instance;
    }

    public static Quiz getTestData() {
        // Test data
        // 5th option, ID 4 is the correct answer
        return new Quiz(
                new QuizPane("Which of the following language is Czech?", new String[]{"Option A", "Möglichkeit B", "Opción C", "Επιλογή Δ", "Možnost E"}, (byte) 4)
        );
    }
}
