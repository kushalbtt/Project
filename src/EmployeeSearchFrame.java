/**
 * Author: Lon Smith, Ph.D.
 * 
 */

import java.awt.EventQueue;
import java.awt.Font;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;

public class EmployeeSearchFrame extends JFrame {

    private JPanel contentPane;
    private JTextField txtDatabase;

    private DefaultListModel<String> department = new DefaultListModel<>();
    private DefaultListModel<String> project = new DefaultListModel<>();

    private JList<String> lstDepartment;
    private JList<String> lstProject;

    private JCheckBox chckbxNotDept;
    private JCheckBox chckbxNotProject;

    private JTextArea textAreaEmployee;

    private Database db = new Database();  // YOUR DB class

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                EmployeeSearchFrame frame = new EmployeeSearchFrame();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public EmployeeSearchFrame() {
        setTitle("Employee Search");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 520, 520);

        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblNewLabel = new JLabel("Database:");
        lblNewLabel.setFont(new Font("Times New Roman", Font.BOLD, 12));
        lblNewLabel.setBounds(21, 20, 80, 20);
        contentPane.add(lblNewLabel);

        txtDatabase = new JTextField();
        txtDatabase.setBounds(100, 20, 200, 20);
        contentPane.add(txtDatabase);

        JButton btnDBFill = new JButton("Fill");
        btnDBFill.setBounds(320, 18, 80, 25);
        btnDBFill.addActionListener(e -> loadLists());
        contentPane.add(btnDBFill);

        JLabel lblDepartment = new JLabel("Department");
        lblDepartment.setFont(new Font("Times New Roman", Font.BOLD, 12));
        lblDepartment.setBounds(40, 60, 100, 20);
        contentPane.add(lblDepartment);

        JLabel lblProject = new JLabel("Project");
        lblProject.setFont(new Font("Times New Roman", Font.BOLD, 12));
        lblProject.setBounds(260, 60, 100, 20);
        contentPane.add(lblProject);

        // Department list
        lstDepartment = new JList<>(department);
        lstDepartment.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        JScrollPane spDept = new JScrollPane(lstDepartment);
        spDept.setBounds(30, 85, 180, 90);
        contentPane.add(spDept);

        // Project list
        lstProject = new JList<>(project);
        lstProject.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        JScrollPane spProj = new JScrollPane(lstProject);
        spProj.setBounds(240, 85, 180, 90);
        contentPane.add(spProj);

        // NOT checkboxes
        chckbxNotDept = new JCheckBox("Not");
        chckbxNotDept.setBounds(80, 180, 70, 20);
        contentPane.add(chckbxNotDept);

        chckbxNotProject = new JCheckBox("Not");
        chckbxNotProject.setBounds(290, 180, 70, 20);
        contentPane.add(chckbxNotProject);

        JLabel lblEmployee = new JLabel("Employees");
        lblEmployee.setFont(new Font("Times New Roman", Font.BOLD, 12));
        lblEmployee.setBounds(30, 215, 100, 20);
        contentPane.add(lblEmployee);

        textAreaEmployee = new JTextArea();
        textAreaEmployee.setEditable(false);
        JScrollPane spEmp = new JScrollPane(textAreaEmployee);
        spEmp.setBounds(30, 240, 420, 200);
        contentPane.add(spEmp);

        JButton btnSearch = new JButton("Search");
        btnSearch.setBounds(90, 450, 100, 25);
        btnSearch.addActionListener(e -> searchEmployees());
        contentPane.add(btnSearch);

        JButton btnClear = new JButton("Clear");
        btnClear.setBounds(270, 450, 100, 25);
        btnClear.addActionListener(e -> clearFields());
        contentPane.add(btnClear);
    }

    /**
     * Load Departments and Projects from the database
     */
    private void loadLists() {

        department.clear();
        project.clear();

        boolean ok = db.connect(txtDatabase.getText().trim());
        if (!ok) {
            JOptionPane.showMessageDialog(null, "Could not open database: " + db.getLastError());
            return;
        }

        try {
            Connection con = db.getConnection();

            PreparedStatement pst1 = con.prepareStatement("SELECT Dname FROM DEPARTMENT ORDER BY Dname;");
            ResultSet rs1 = pst1.executeQuery();
            while (rs1.next()) department.addElement(rs1.getString("Dname"));

            PreparedStatement pst2 = con.prepareStatement("SELECT Pname FROM PROJECT ORDER BY Pname;");
            ResultSet rs2 = pst2.executeQuery();
            while (rs2.next()) project.addElement(rs2.getString("Pname"));

            JOptionPane.showMessageDialog(null, "Lists loaded successfully.");

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error loading data.");
        }
    }

    /**
     * Apply all search logic
     */
    private void searchEmployees() {

        textAreaEmployee.setText("");

        var depts = lstDepartment.getSelectedValuesList();
        var projs = lstProject.getSelectedValuesList();

        boolean deptSelected = !depts.isEmpty();
        boolean projSelected = !projs.isEmpty();

        if (!deptSelected && !projSelected) {
            JOptionPane.showMessageDialog(null, "Select at least one filter.");
            return;
        }

        try {
            Connection con = db.getConnection();

            StringBuilder sql = new StringBuilder();
            sql.append("SELECT DISTINCT E.Fname, E.Lname ");
            sql.append("FROM EMPLOYEE E ");

            if (projSelected) {
                sql.append("JOIN WORKS_ON W ON E.Ssn = W.Essn ");
                sql.append("JOIN PROJECT P ON W.Pno = P.Pnumber ");
            }

            sql.append("WHERE 1=1 ");

            // Department filter
            if (deptSelected) {
                sql.append("AND E.Dno ");
                sql.append(chckbxNotDept.isSelected() ? "NOT IN (" : "IN (");
                sql.append("SELECT Dnumber FROM DEPARTMENT WHERE Dname IN (");

                sql.append("?, ".repeat(depts.size()));
                sql.setLength(sql.length() - 2);

                sql.append(")) ");
            }

            // Project filter
            if (projSelected) {

                if (chckbxNotProject.isSelected()) {
                    sql.append("AND E.Ssn NOT IN (SELECT Essn FROM WORKS_ON W2 ");
                    sql.append("JOIN PROJECT P2 ON W2.Pno = P2.Pnumber ");
                    sql.append("WHERE P2.Pname IN (");
                } else {
                    sql.append("AND P.Pname IN (");
                }

                sql.append("?, ".repeat(projs.size()));
                sql.setLength(sql.length() - 2);
                sql.append(")) ");
            }

            PreparedStatement pst = con.prepareStatement(sql.toString());

            // Set parameters
            int index = 1;
            for (String d : depts) pst.setString(index++, d);
            for (String p : projs) pst.setString(index++, p);

            ResultSet rs = pst.executeQuery();

            if (!rs.isBeforeFirst()) {
                textAreaEmployee.setText("No matching employees found.");
                return;
            }

            while (rs.next()) {
                textAreaEmployee.append(
                    rs.getString("Fname") + " " + rs.getString("Lname") + "\n"
                );
            }

        } catch (Exception ex) {
            textAreaEmployee.setText("Query failed: " + ex.getMessage());
        }
    }

    private void clearFields() {
        lstDepartment.clearSelection();
        lstProject.clearSelection();
        chckbxNotDept.setSelected(false);
        chckbxNotProject.setSelected(false);
        textAreaEmployee.setText("");
    }
}
