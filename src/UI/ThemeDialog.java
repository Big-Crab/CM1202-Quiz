package UI;

import common.Theme;
import database.DatabaseManager;

import javax.swing.*;
import java.awt.event.*;
import java.sql.SQLException;

public class ThemeDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JComboBox comboBoxTheme;

    private int themeSelected;
    private Theme[] list = null;

    public ThemeDialog() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        DatabaseManager dbm = DatabaseManager.getDBM();
        try {
            list = dbm.getThemes();
            for(Theme theme : list) {
                comboBoxTheme.addItem(theme.name);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            //...
        }
    }

    private void onOK() {
        for(Theme selectedName : list) {
            if(selectedName.name.equals(comboBoxTheme.getSelectedItem())) {
                themeSelected = selectedName.ID;
                break;
            }
        }
        dispose();
    }

    public int showDialog() {
        setVisible(true);
        return themeSelected;
    }
}
