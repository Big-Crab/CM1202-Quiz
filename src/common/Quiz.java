package common;

import java.util.ArrayList;
import java.util.Arrays;

public class Quiz {

    // TODO: make this an Array if we discover that no future changes to Quizzes are necessary
    public ArrayList<QuizPane> panes = new ArrayList<>();
    private int currentIndex = 0;

    public Quiz(QuizPane[] paneArr) {
        //panes.clear();
        panes.addAll(Arrays.asList(paneArr));
    }

    public QuizPane getCurrent() {
        return panes.get(currentIndex);
    }

    public QuizPane getNext() {
        currentIndex++;
        if(currentIndex < panes.size()) {
            return getCurrent();
        } else {
            // Signal to the calling method that we've reached the end of the list, and that the finish screen should be displayed.
            return null;
        }
    }

    public int getNumberOfQuestions() {
        return panes.size();
    }
}
