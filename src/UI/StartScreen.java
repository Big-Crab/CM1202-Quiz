package UI;

import database.DatabaseManager;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

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

        schoolBox.addItem("None");
        for(String item : DatabaseManager.getSchools()) {
            schoolBox.addItem(item);
        }
        schoolBox.addActionListener(e -> {
            yearGroupBox.setEnabled(schoolBox.getSelectedIndex() > 0);
            // enable below if it's necessary to select school & year
            //buttonConfirm.setEnabled(schoolBox.getSelectedIndex() > 0 && yearGroupBox.getSelectedIndex() > 0);
        });

        yearGroupBox.addItem("None");
        for(int i = 7; i < 13; i++) {
            yearGroupBox.addItem("Year " + i);
        }
        // enable below if it's necessary to select school & year
        //yearGroupBox.addActionListener(e -> buttonConfirm.setEnabled(schoolBox.getSelectedIndex() > 0 && yearGroupBox.getSelectedIndex() > 0));
    }

    public void goToQuiz() {
        // Create the quiz window and then...
        QuizPage.main();
        // -close the current one
        frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
    }
}
