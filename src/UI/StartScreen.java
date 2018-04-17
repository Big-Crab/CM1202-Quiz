package UI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

public class StartScreen {
    private JButton setThemeButton;
    private JComboBox schoolBox;
    private JComboBox yearGroupButton;
    private JButton buttonConfirm;
    private JPanel mainPanel;
    private JPanel panelControls;
    private static JFrame frame;

    public static void main(String[] args) {
        frame = new JFrame("StartScreen");
        frame.setContentPane(new StartScreen().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
    }

    public void goToQuiz() {
        // Create the quiz window then close the current one.
        QuizPage.main();
        //frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
        //frame.setContentPane(new QuizPage().panelMain);
    }
}
