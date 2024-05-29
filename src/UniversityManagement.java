import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UniversityManagement {
    private JPanel UniPanel;
    private JTable table1;
    private JButton DELETEButton;
    private JButton INSERTButton;
    private JButton UPDATEButton;
    private JRadioButton studentRadioButton;
    private JRadioButton academicianRadioButton;
    private JTextField nameTextField;
    private JTextField surnameTextField;
    private JTextField courseTextField;
    private DefaultTableModel tableModel;
    private SQLOperations dbOps;

    public UniversityManagement() {
        ButtonGroup group = new ButtonGroup();
        group.add(studentRadioButton);
        group.add(academicianRadioButton);
        dbOps = new SQLOperations();
        tableModel = new DefaultTableModel();
        table1.setModel(tableModel);
        studentRadioButton.addActionListener(e -> showStudents());
        academicianRadioButton.addActionListener(e -> showAcademicians());

        DELETEButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteBtn();
            }
        });
        INSERTButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                insertBtn();
            }
        });
        UPDATEButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateBtn();
            }
        });
    }

    private void showStudents() {
        dbOps.showTableData(tableModel, "student");
    }

    private void showAcademicians() {
        dbOps.showTableData(tableModel, "academician");
    }

    private void deleteBtn(){
        int selected = table1.getSelectedRow();
        if (selected>=0){
            String keyValue = tableModel.getValueAt(selected,0).toString();
            String tableName = studentRadioButton.isSelected() ? "student" : "academician";
            String keyColumn = studentRadioButton.isSelected() ? "StudentID" : "AcademicianID";

            dbOps.deleteSql(tableName,keyColumn,keyValue);
            dbOps.showTableData(tableModel,tableName);
        }else{
            JOptionPane.showMessageDialog(UniPanel,"Please select a row");
        }
    }
    private void insertBtn() {
        String tableName = studentRadioButton.isSelected() ? "student" : "academician";
        String name = nameTextField.getText().trim();
        String surname = surnameTextField.getText().trim();
        String course = courseTextField.getText().trim();
        boolean res = dbOps.controlCourseTable(course);
        if (name.isEmpty() || surname.isEmpty() || course.isEmpty()) {
            JOptionPane.showMessageDialog(UniPanel, "Please fill in all fields");
            return;
        }
        if (res){
            dbOps.insertSql(tableName, name, surname, dbOps.findCourseCode(course));
            dbOps.showTableData(tableModel, tableName);
            JOptionPane.showMessageDialog(UniPanel, "Data inserted successfully");
        }else {
            JOptionPane.showMessageDialog(UniPanel,"Invalid Course Name");
        }

    }
    private void updateBtn(){
        int selectedRow = table1.getSelectedRow();
        if (selectedRow >= 0) {
            String tableName = studentRadioButton.isSelected() ? "student" : "academician";
            String name = nameTextField.getText().trim();
            String surname = surnameTextField.getText().trim();
            String course = courseTextField.getText().trim();
            String id = studentRadioButton.isSelected() ? "StudentID" : "AcademicianID";
            String idValue = tableModel.getValueAt(selectedRow, 0).toString();

            if (name.isEmpty() || surname.isEmpty() || course.isEmpty()) {
                JOptionPane.showMessageDialog(UniPanel, "Please fill in all fields");
                return;
            }

            dbOps.updateSql(tableName, name, surname, dbOps.findCourseCode(course), id, idValue);
            dbOps.showTableData(tableModel, tableName);
            JOptionPane.showMessageDialog(UniPanel, "Data updated successfully");
        } else {
            JOptionPane.showMessageDialog(UniPanel, "Please select a row to update");
        }
    }





    public static void main(String[] args) {
        JFrame frame = new JFrame("University Management System");
        frame.setContentPane(new UniversityManagement().UniPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.setSize(1000, 500);
    }
}
