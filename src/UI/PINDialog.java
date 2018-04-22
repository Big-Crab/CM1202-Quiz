package UI;

import org.h2.util.StringUtils;

import javax.swing.*;
import javax.swing.text.NumberFormatter;
import java.awt.event.*;
import java.text.NumberFormat;

public class PINDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JFormattedTextField PINEntry;
    private int pinAccepted = -1;

    public PINDialog() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        PINEntry.addActionListener(e -> {
            if(StringUtils.isNumber(PINEntry.getText())) {
                buttonOK.setEnabled(true);
            } else {
                buttonOK.setEnabled(false);
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() {
        // 1 = Accepted, 0 = Not Accepted, 2 = Cancelled
        pinAccepted = PINEntry.getText().equals(StartScreen.adminPIN) ? 1 : 0;
        setVisible(false);
        dispose();
    }

    private void onCancel() {
        pinAccepted = 2;
        dispose();
    }

    public int showDialog() {
        setVisible(true);
        return pinAccepted;
    }
}
