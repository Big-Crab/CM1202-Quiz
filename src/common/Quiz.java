package common;

import java.util.ArrayList;

public class Quiz {

    // TODO: make this an Array if we discover that no future changes to Quizzes are necessary
    public ArrayList<QuizPane> panes = new ArrayList<>();
    private int currentIndex = 0;

    public Quiz(QuizPane pane) {
        //panes.clear();
        panes.add(pane);
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
}
