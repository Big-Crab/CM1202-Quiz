package UI;

import common.Quiz;
import common.QuizPane;
import database.DatabaseManager;
import database.QuizStatisticRecorder;

import javax.swing.*;
import javax.xml.crypto.Data;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.sql.SQLException;

public class QuizPage {
    private JTextPane paneQuestionText;

    // Dynamically load quiz progress and score here
    private JPanel paneQuizStatus;
    private JLabel quizProgress;
    private JLabel quizScore;


    // Dynamically generate radio buttons in here
    private JPanel paneAnswers;

    // Load next question, leave current as unanswered
    private JButton buttonPass;

    // Contains stats and admin control buttons
    private JPanel panelControls;

    // Confirms answer, and highlights feedback
    private JButton buttonSelectAnswer;

    // Activated when question is answered
    private JButton buttonNext;
    public JPanel panelMain;
    private JButton buttonStats;
    private JButton buttonAdmin;

    // Load all content from this,
    // and use it to change the statuses of answers etc.
    private Quiz quizContent;

    private boolean answerLocked = false;

    private static JFrame frame;

    private int selectedTheme = -1;

    public static void main(int selectedTheme) {
        QuizPage page = new QuizPage();
        frame = new JFrame("Quiz Page");
        frame.setContentPane(page.panelMain);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        frame.revalidate();
        frame.repaint();

        page.Start(selectedTheme);
    }

    // Not much point in a constructor if we can't edit the components yet.
    /*public QuizPage() {

    }*/

    /**
     * Run when initially loading this UI screen from the start screen.
     * Entry point for this UI.
     */
    public void Start(int selectedTheme) {
        this.selectedTheme = selectedTheme;

        DatabaseManager dbm = DatabaseManager.getDBM();
        try {
            // TODO: Get theme here
            dbm.refreshQuizContent(selectedTheme);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        quizContent = dbm.getQuizContent();

        //load 1st question
        loadContentToUI();

        //assign listener and anonymous class with method to the 'next' button
        buttonNext.addActionListener(e -> nextQuestion());
        buttonSelectAnswer.addActionListener(e -> verifyAnswer());
        buttonPass.addActionListener(e -> passQuestion());
    }

    private void loadContentToUI() {
        // Get answers
        // Get question
        // Activate relevant buttons

        generateAnswerList();
        paneQuestionText.setText(quizContent.getCurrent().getQuestion());
        quizProgress.setText("Total: " + QuizStatisticRecorder.getTotal());
        quizScore.setText("Correct: " + QuizStatisticRecorder.getCorrect());
    }

    /**
     * Slightly different to nextQuestion, in that it
     * forces the status of the current question to Unanswered,
     * to avoid accidentally getting it wrong when you mean to
     * pass it.
     */
    private void passQuestion() {
        QuizStatisticRecorder.addIncorrect();
        quizContent.getCurrent().removeAnswer();
        nextQuestion();
        System.out.println("Added incorrect; now at " + QuizStatisticRecorder.getTotal());
    }

    /**
     * Handle the transition after pressing the 'next' button.
     * Resets the page, except for bottom left control panel.
     * Wipes everything clean and calls loadContentToUI()
     */
    private void nextQuestion() {
        // Select whether to go to the next question or finish entirely
        if(quizContent.getNext() != null) {
            // Reset page and load new data
            answerLocked = false;
            for(Component component : paneAnswers.getComponents()) {
                if(component instanceof JRadioButton) {
                    paneAnswers.remove(component);
                }
            }
            //paneAnswers.validate();
            //paneAnswers.repaint();
            loadContentToUI();
            buttonPass.setEnabled(true);
            buttonNext.setEnabled(false);
        } else {
            FinishScreen.main();
            QuizStatisticRecorder.writeToDB();
            frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
        }
    }

    private void verifyAnswer() {
        if(quizContent.getCurrent().isCorrect()) {
            JOptionPane.showMessageDialog(panelMain, "You chose the correct answer!", "Correct", JOptionPane.PLAIN_MESSAGE);
            QuizStatisticRecorder.addCorrect();
        } else {
            JOptionPane.showMessageDialog(panelMain, "Incorrect answer. The right answer was Option " +
                    (1 + quizContent.getCurrent().getCorrectID()) +
                    ", \"" + quizContent.getCurrent().getAnswers()[quizContent.getCurrent().getCorrectID()] + "\"");
            QuizStatisticRecorder.addIncorrect();
        }
        buttonNext.setEnabled(true);
        buttonSelectAnswer.setEnabled(false);
        buttonPass.setEnabled(false);
        answerLocked = true;
    }

    /**
     * Generates radio buttons for the answers in the designated pane
     */
    private void generateAnswerList() {
        String[] answers = quizContent.getCurrent().getAnswers();
        GridLayout layout  = new GridLayout(answers.length,1 );
        paneAnswers.setLayout(layout);

        ButtonGroup answerGroup = new ButtonGroup();
        int i = 0;
        for (String answer : answers) {
            JRadioButton button = new JRadioButton((i+1) + ". " + answer);
            button.setActionCommand(String.valueOf(i));
            answerGroup.add(button);
            final byte choiceID = (byte) i;
            button.addActionListener(e -> {
                if(!answerLocked) {
                    buttonSelectAnswer.setEnabled(true);
                    quizContent.getCurrent().setSelectedID(choiceID);
                }
            });
            paneAnswers.add(button);
            paneAnswers.validate();
            i++;
        }
    }

    /*private void createUIComponents() {
        // TODO: place custom component creation code here
    }*/

    public void openQuizPage() {
        JFrame frame = new JFrame("Quiz Page");
        frame.setContentPane(new QuizPage().panelMain);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    /*private void createUIComponents() {
        paneAnswers = new JPanel();
        buttonSelectAnswer = new JButton();
        paneQuestionText = new JTextPane();
        buttonNext = new JButton();
        Start();
    }*/
}
