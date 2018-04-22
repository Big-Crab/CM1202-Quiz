package UI;

import database.QuizStatisticRecorder;

import javax.swing.*;

public class FinishScreen {
    private static JFrame frame;
    private JPanel panelMain;
    private JTextPane textPaneResults;
    private JButton buttonRestart;
    private JPanel panelControls;
    private JButton buttonStats;
    private JButton buttonAdmin;

    public static void main() {
        FinishScreen screen = new FinishScreen();
        frame = new JFrame("Finish Page");
        frame.setContentPane(screen.panelMain);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        frame.revalidate();
        frame.repaint();

        screen.Start();
    }

    private void Start() {
        int correct = QuizStatisticRecorder.getCorrect();
        int total = QuizStatisticRecorder.getTotal();
        textPaneResults.setText("Quiz complete! You answered " + correct + " correctly, out of " + total + " total questions.");

        buttonRestart.addActionListener(e -> {
            StartScreen.main(null);
            frame.dispose();
        });
    }
}
