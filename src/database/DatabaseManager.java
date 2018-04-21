package database;

import common.Quiz;
import common.QuizPane;
import common.Theme;

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
