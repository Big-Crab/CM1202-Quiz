package UI;

import database.DatabaseManager;
import database.QuizStatisticRecorder;

import javax.swing.*;
import java.awt.event.WindowEvent;
import java.sql.SQLException;

public class StartScreen {
    private JButton setThemeButton;
    private JComboBox schoolBox;
    private JComboBox yearGroupBox;
    private JButton buttonConfirm;
    private JPanel mainPanel;
    private JPanel panelControls;
    private JPanel panelLeft;
    private JButton buttonStats;
    private JButton buttonAdmin;
    private static JFrame frame;

    public static String adminPIN = null;

    private boolean themeValid = false;
    public static int selectedTheme = 1;

    public static void main(String[] args) {
        frame = new JFrame("Start Screen");
        frame.setContentPane(new StartScreen().mainPanel);
        // DISPOSE_ON_CLOSE will delete the window but not stop the application from running.
        // Don't use ExitOnClose otherwise we'll kill the whole thing every time we want to
        // replace a window with a new one
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public StartScreen() {
        buttonConfirm.addActionListener(e -> goToQuiz());
        buttonStats.addActionListener(e -> showStats());
        setThemeButton.addActionListener(e -> displayThemeSelection());

        try {
            schoolBox.addItem("None");
            for (String item : DatabaseManager.getDBM().getSchools()) {
                schoolBox.addItem(item);
            }
            schoolBox.addActionListener(e -> {
                yearGroupBox.setEnabled(schoolBox.getSelectedIndex() > 0);
                yearGroupBox.removeAllItems();
                yearGroupBox.addItem("None");
                try {
                    if(schoolBox.getSelectedIndex() != 0) {
                        for (int i : DatabaseManager.getDBM().getYears(schoolBox.getSelectedIndex())) {
                            yearGroupBox.addItem(i);
                        }
                    }
                } catch (SQLException exc) {
                    exc.printStackTrace();
                }

                validateScreen();
            });

            yearGroupBox.addActionListener(e -> validateScreen());

        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        // enable below if it's necessary to select BOTH a school & a year
        //yearGroupBox.addActionListener(e -> buttonConfirm.setEnabled(schoolBox.getSelectedIndex() > 0 && yearGroupBox.getSelectedIndex() > 0));

        // If the pin is not yet set
        if(adminPIN == null) {
            showPinCreationDialog();
        }
    }

    public void displayThemeSelection() {
        // show PIN
        // then if PIN is accepted, goto theme selection dialogue with dropdown
        int result = -1;
        while(result < 1) {
            result = showPinDialog();
        }
        // If cancel button is hit, the rest is ignored.
        if(result == 1) {
            ThemeDialog dialog = new ThemeDialog();
            dialog.pack();
            selectedTheme = dialog.showDialog();
            System.out.println("Selected Theme: " + selectedTheme);
        }

        //Validate the theme
        validateTheme();
    }

    private void showStats() {
        int result = -1;
        while(result < 1) {
            result = showPinDialog();
        }
        // If cancel button is hit, the rest is ignored.
        if(result == 1) {
            StatsDialog dialog = new StatsDialog();
            dialog.pack();
            dialog.setVisible(true);
        }
    }

    private void showAdmin() {

    }

    public void goToQuiz() {
        // We do not need to add +1 to the index, despite SQL starting at 1, because we have the "None" option, which buffers the others up by 1
        QuizStatisticRecorder.setSelectedSchoolID(schoolBox.getSelectedIndex());
        if(yearGroupBox.getSelectedIndex() > 0) {
            QuizStatisticRecorder.setSelectedSchoolYear((Integer) yearGroupBox.getSelectedItem());
        } else {
            QuizStatisticRecorder.setSelectedSchoolYear(0);
        }
        QuizStatisticRecorder.setSelectedTheme(selectedTheme);

        // Create the quiz window and then...
        QuizPage.main(selectedTheme);
        // -close the current one
        frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
    }

    private void validateScreen() {
        validateTheme();

        buttonConfirm.setEnabled(
                ((schoolBox.getSelectedIndex() > 0 && yearGroupBox.getSelectedIndex() > 0) || (schoolBox.getSelectedIndex() == 0 && yearGroupBox.getSelectedIndex() == 0))
                && themeValid
        );
    }

    private void validateTheme() {
        try {
            DatabaseManager dbm = DatabaseManager.getDBM();
            try {
                dbm.refreshQuizContent(selectedTheme);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            themeValid = dbm.getQuizContent().getNumberOfQuestions() > 0;
            buttonConfirm.setEnabled(themeValid);
        } catch (Exception e) {
            e.printStackTrace();
        }
        /*
         try {
        } catch (NullPointerException e) {
            System.out.println("Theme not valid.");
        }*/
    }

    private void showPinCreationDialog() {
        PINCreationDialog dialog = new PINCreationDialog();
        dialog.pack();
        dialog.setVisible(true);
    }

    private int showPinDialog() {
        PINDialog dialog = new PINDialog();
        dialog.pack();
        return dialog.showDialog();
    }
}
