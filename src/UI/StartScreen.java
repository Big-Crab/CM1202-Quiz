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
    private static JFrame frame;

    public static int adminPIN;

    public int selectedTheme;

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

                // enable below if it's necessary to select school & year
                //buttonConfirm.setEnabled(schoolBox.getSelectedIndex() > 0 && yearGroupBox.getSelectedIndex() > 0);
            });

        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        // enable below if it's necessary to select BOTH a school & a year
        //yearGroupBox.addActionListener(e -> buttonConfirm.setEnabled(schoolBox.getSelectedIndex() > 0 && yearGroupBox.getSelectedIndex() > 0));

        showPinCreationDialog();
    }

    public void displayThemeSelection() {
        // show PIN
        // then if PIN is accepted, goto theme selection dialogue with dropdown
    }

    public void goToQuiz() {
        // We do not need to add +1 to the index, despite SQL starting at 1, because we have the "None" option, which buffers the others up by 1
        QuizStatisticRecorder.setSelectedSchoolID(schoolBox.getSelectedIndex());
        QuizStatisticRecorder.setSelectedSchoolYear(yearGroupBox.getSelectedIndex());
        QuizStatisticRecorder.setSelectedTheme(selectedTheme);

        // Create the quiz window and then...
        QuizPage.main();
        // -close the current one
        frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
    }

    private void showPinCreationDialog() {
        PINCreationDialog dialog = new PINCreationDialog();
        dialog.pack();
        dialog.setVisible(true);
    }

    private boolean showPinDialog() {
        PINDialog dialog = new PINDialog();
        dialog.pack();
        return dialog.showDialog();
    }
}
