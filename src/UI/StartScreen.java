package UI;

import database.DatabaseManager;
import database.QuizStatisticRecorder;

import javax.swing.*;
import javax.xml.crypto.Data;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
        buttonConfirm.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                goToQuiz();
            }
        });

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
    }

    public void goToQuiz() {
        QuizStatisticRecorder.setSelectedSchoolID(schoolBox.getSelectedIndex());

        // Create the quiz window and then...
        QuizPage.main();
        // -close the current one
        frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
    }
}
