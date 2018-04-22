package UI;

import org.h2.util.StringUtils;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.*;

public class PINCreationDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JFormattedTextField PINEntry;

    public PINCreationDialog() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(e -> onOK());

        PINEntry.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                check();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                check();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                check();
            }

            private void check() {
                if(PINEntry.getText().length() <= 6 && PINEntry.getText().length() >= 1 && StringUtils.isNumber(PINEntry.getText())) {
                    buttonOK.setEnabled(true);
                } else {
                    buttonOK.setEnabled(false);
                }
            }
        }
        );

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    }

    private void onOK() {
        // add your code here
        StartScreen.adminPIN = PINEntry.getText();
        dispose();
    }
}
