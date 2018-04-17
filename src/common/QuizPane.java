package common;

public class QuizPane {
    private String question;
    private String[] answers;
    // Using byte to improve storage size and limit number of answers
    private byte correctID;
    private byte selectedID;
    public enum answerTypes {UNANSWERED, CORRECT, INCORRECT}
    private answerTypes status = answerTypes.UNANSWERED;
    // placeholder
    // private Image image

    public QuizPane(String question, String[] answers, byte correctID) {
        setQuestion(question);
        setAnswers(answers);
        setCorrectID(correctID);
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String[] getAnswers() {
        return answers;
    }

    public void setAnswers(String[] answers) {
        this.answers = answers;
    }

    public byte getCorrectID() {
        return correctID;
    }

    public void setCorrectID(byte correctID) {
        this.correctID = correctID;
    }

    public byte getSelectedID() {
        return selectedID;
    }

    public void setSelectedID(byte selectedID) {
        if(selectedID == correctID) {
            status = answerTypes.CORRECT;
        } else {
            status = answerTypes.INCORRECT;
        }
        this.selectedID = selectedID;
    }

    public boolean isCorrect() {
        return status == answerTypes.CORRECT;
    }

    public boolean isAnswered() {
        return status != answerTypes.UNANSWERED;
    }
}
