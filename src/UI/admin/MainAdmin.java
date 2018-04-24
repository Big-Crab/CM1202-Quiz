package UI.admin;

import common.Theme;
import database.DatabaseManager;
import org.h2.engine.Database;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import javax.xml.crypto.Data;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class MainAdmin {
    private JButton buttonThemeAdd;
    private JButton buttonThemeRemove;
    private JButton buttonThemeEdit;
    private JButton buttonSchoolAdd;
    private JButton buttonSchoolRemove;
    private JButton buttonSchoolEdit;
    private JTable tableThemes;
    public JPanel panelMain;
    private JScrollPane scrollThemes;
    private JScrollPane scrollSchools;

    public MainAdmin() {
        createUIComponents();
    }

    private void createUIComponents() {
        DatabaseManager dbm = DatabaseManager.getDBM();
        try {
            Theme[] themes = dbm.getThemes();
            tableThemes = new JTable(new ThemeTableModel(themes));
            tableThemes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            tableThemes.setRowSelectionAllowed(true);
            tableThemes.setColumnSelectionAllowed(false);
            tableThemes.validate();
            tableThemes.repaint();

            scrollThemes.setViewportView(tableThemes);
            panelMain.validate();
            panelMain.repaint();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        buttonThemeRemove.addActionListener(e -> {
            try {
                DatabaseManager.getDBM().executeCommand("DELETE FROM THEMES WHERE ID="+tableThemes.getSelectedRow());
                ((ThemeTableModel) tableThemes.getModel()).removeAt(tableThemes.getSelectedRow());
            } catch (SQLException s) {s.printStackTrace();}
        });

        buttonThemeAdd.addActionListener(e -> {
            ((ThemeTableModel) tableThemes.getModel()).addObject(new Theme(-1, "New Theme"));
            try {
                DatabaseManager.getDBM().executeCommand("INSERT INTO THEMES VALUES (default, 'New Theme')");
            } catch (SQLException s) {s.printStackTrace();}
        });
    }

    public class ThemeTableModel extends AbstractTableModel {
        private List<Theme> objects;

        ThemeTableModel(Theme[] themes) {
            objects = new ArrayList<>(Arrays.asList(themes));
            this.addTableModelListener(new TableModelListener() {
                @Override
                public void tableChanged(TableModelEvent e) {
                    if(e.getColumn()==1) {
                        try {
                            DatabaseManager.getDBM().executeCommand("UPDATE THEMES SET name=" + getValueAt(e.getFirstRow(), 1) + " WHERE ID=" + getValueAt(e.getFirstRow(), 0));
                        } catch (SQLException s) {s.printStackTrace();}
                    }
                }
            });
        }

        public void addObject(Theme obj) {
            // Using .size() here cleverly allows us to automatically get the latest element in the list
            addObject(obj, objects.size());
        }

        public void addObject(Theme obj, int index) {
            obj.ID = objects.size()+1;
            objects.add(index, obj);
            fireTableRowsInserted(index, index);
        }

        public void removeAt(int index) {
            removeObject(getObject(index));
        }

        public void removeObject(Theme obj) {
            int index = objects.indexOf(obj);
            objects.remove(index);
            fireTableRowsDeleted(index, index);
        }

        public Theme getObject(int rowIndex) {
            return objects.get(rowIndex);
        }

        @Override
        public int getRowCount() {
            return objects.size();
        }

        @Override
        public int getColumnCount() {
            return 2;
        }

        @Override
        public String getColumnName(int columnIndex) {
            switch (columnIndex) {
                case 0: return "ID";
                case 1: return "Name";
                default: throw new IndexOutOfBoundsException();
            }
        }

        @Override
        public boolean isCellEditable(int row, int col) {
            //Note that the data/cell address is constant,
            //no matter where the cell appears onscreen.
            if (col > 0) {
                return true;
            }
            return false;
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            return getValueAt(0, columnIndex).getClass();
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            Theme obj = objects.get(rowIndex);
            switch (columnIndex) {
                case 0: return obj.ID;
                case 1: return obj.name;
                default: throw new IndexOutOfBoundsException();
            }
        }
    }
}
