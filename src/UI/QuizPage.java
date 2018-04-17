package UI;

import common.Quiz;
import common.QuizPane;
import database.DatabaseManager;

import javax.swing.*;
import javax.xml.crypto.Data;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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

    // Load all content from this,
    // and use it to change the statuses of answers etc.
    private Quiz quizContent;

    private static JFrame frame;

    public static void main() {
        QuizPage page = new QuizPage();
        frame = new JFrame("Quiz Page");
        frame.setContentPane(page.panelMain);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        frame.revalidate();
        frame.repaint();

        page.Start();
    }

    public QuizPage() {
        //Start();
    }

    /**
     * Run when initially loading this UI screen from the start screen.
     * Entry point for this UI.
     */
    public void Start() {
        //copy the control buttons


        quizContent = DatabaseManager.getTestData();

        //load 1st question
        loadContentToUI();

        //assign listener and anonymous class with method to the 'next' button
        buttonNext.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                nextQuestion();
            }
        });
    }

    private void loadContentToUI() {
        // Get answers
        // Get question
        // Activate relevant buttons

        generateAnswerList();
        buttonSelectAnswer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                verifyAnswer();
            }
        });

        paneQuestionText.setText(quizContent.getCurrent().getQuestion());
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
        } else {
            // Go to finish page
        }
    }

    private void verifyAnswer() {
        if(quizContent.getCurrent().isCorrect()) {
            JOptionPane.showMessageDialog(panelMain, "You chose the correct answer!", "Correct", JOptionPane.PLAIN_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(panelMain, "Incorrect answer. The right answer was Option " +
                    quizContent.getCurrent().getCorrectID() +
                    ", \"" + quizContent.getCurrent().getAnswers()[quizContent.getCurrent().getCorrectID()] + "\"");
        }
        buttonNext.setEnabled(true);
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
            JRadioButton button = new JRadioButton(i + ". " + answer);
            button.setActionCommand(String.valueOf(i));
            answerGroup.add(button);
            final byte choiceID = (byte) i;
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    buttonSelectAnswer.setEnabled(true);
                    quizContent.getCurrent().setSelectedID(choiceID);
                }
            });
            paneAnswers.getName();
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
