package UI;

import database.DatabaseManager;
import org.h2.engine.Database;

import javax.swing.*;
import javax.xml.crypto.Data;
import java.awt.event.*;
import java.sql.SQLException;

public class StatsDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JTextArea textArea;

    public StatsDialog() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
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

        textArea.setEditable(false);
        try {
            textArea.setText(DatabaseManager.getDBM().getStatsReadout());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }
}
