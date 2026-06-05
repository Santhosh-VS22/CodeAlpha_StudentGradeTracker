import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;

class Student {
    private int id;
    private String name;
    private int mark;
    public Student(int id, String name, int mark) {
        this.id = id;
        this.name = name;
        this.mark = mark;
    }
    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public int getMark() {
        return mark;
    }

    public void setMark(int mark) {
        this.mark = mark;
    }

    public String getGrade() {
        if (mark >= 90)
            return "A";
        else if (mark >= 80)
            return "B";
        else if (mark >= 70)
            return "C";
        else if (mark >= 60)
            return "D";
        else
            return "F";
    }
}

public class StudentGradeTrackerGUI extends JFrame {

    private JTextField txtName;
    private JTextField txtMark;

    private JTable table;
    private DefaultTableModel model;

    private JLabel lblAverage;
    private JLabel lblHighest;
    private JLabel lblLowest;

    private ArrayList<Student> students;
    private int nextId = 1;

    public StudentGradeTrackerGUI() {

        students = new ArrayList<>();

        setTitle("Student Grade Tracker");
        setSize(900, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // TOP PANEL
        JPanel topPanel = new JPanel(new GridLayout(3, 2, 10, 10));

        topPanel.add(new JLabel("Student Name:"));
        txtName = new JTextField();
        topPanel.add(txtName);

        topPanel.add(new JLabel("Student Mark:"));
        txtMark = new JTextField();
        topPanel.add(txtMark);

        add(topPanel, BorderLayout.NORTH);

        // BUTTON PANEL
        JPanel buttonPanel = new JPanel();

        JButton btnAdd = new JButton("Add Student");
        JButton btnSearch = new JButton("Search");
        JButton btnUpdate = new JButton("Update");
        JButton btnDelete = new JButton("Delete");
        JButton btnSave = new JButton("Save File");

        buttonPanel.add(btnAdd);
        buttonPanel.add(btnSearch);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnSave);

        add(buttonPanel, BorderLayout.WEST);

        // TABLE
        model = new DefaultTableModel(
                new String[]{"ID", "Name", "Mark", "Grade"}, 0);

        table = new JTable(model);

        JScrollPane scrollPane = new JScrollPane(table);

        add(scrollPane, BorderLayout.CENTER);

        // STATS PANEL
        JPanel statsPanel = new JPanel(new GridLayout(1, 3));

        lblAverage = new JLabel("Average : 0");
        lblHighest = new JLabel("Highest : 0");
        lblLowest = new JLabel("Lowest : 0");

        statsPanel.add(lblAverage);
        statsPanel.add(lblHighest);
        statsPanel.add(lblLowest);

        add(statsPanel, BorderLayout.SOUTH);

        // EVENTS
        btnAdd.addActionListener(e -> addStudent());
        btnSearch.addActionListener(e -> searchStudent());
        btnUpdate.addActionListener(e -> updateStudent());
        btnDelete.addActionListener(e -> deleteStudent());
        btnSave.addActionListener(e -> saveToFile());

        setVisible(true);
    }

    private void addStudent() {

        String name = txtName.getText().trim();

        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Enter Student Name");
            return;
        }

        int mark;

        try {

            mark = Integer.parseInt(txtMark.getText());

            if (mark < 0 || mark > 100) {
                JOptionPane.showMessageDialog(this,
                        "Mark must be between 0 and 100");
                return;
            }

        } catch (Exception e) {

            JOptionPane.showMessageDialog(this,
                    "Invalid Mark");
            return;
        }

        Student student =
                new Student(nextId++, name, mark);

        students.add(student);

        model.addRow(new Object[]{
                student.getId(),
                student.getName(),
                student.getMark(),
                student.getGrade()
        });

        updateStatistics();

        txtName.setText("");
        txtMark.setText("");

        JOptionPane.showMessageDialog(this,
                "Student Added Successfully");
    }

    private void searchStudent() {

        String name =
                JOptionPane.showInputDialog(
                        this,
                        "Enter Student Name");

        if (name == null)
            return;

        for (Student s : students) {

            if (s.getName()
                    .equalsIgnoreCase(name)) {

                JOptionPane.showMessageDialog(
                        this,
                        "ID : " + s.getId()
                                + "\nName : " + s.getName()
                                + "\nMark : " + s.getMark()
                                + "\nGrade : " + s.getGrade());

                return;
            }
        }

        JOptionPane.showMessageDialog(
                this,
                "Student Not Found");
    }

    private void updateStudent() {

        String name =
                JOptionPane.showInputDialog(
                        this,
                        "Enter Student Name");

        if (name == null)
            return;

        for (Student s : students) {

            if (s.getName()
                    .equalsIgnoreCase(name)) {

                String newMark =
                        JOptionPane.showInputDialog(
                                this,
                                "Enter New Mark");

                try {

                    int mark =
                            Integer.parseInt(newMark);

                    if (mark < 0 || mark > 100) {

                        JOptionPane.showMessageDialog(
                                this,
                                "Invalid Mark");

                        return;
                    }

                    s.setMark(mark);

                    refreshTable();
                    updateStatistics();

                    JOptionPane.showMessageDialog(
                            this,
                            "Updated Successfully");

                } catch (Exception e) {

                    JOptionPane.showMessageDialog(
                            this,
                            "Invalid Input");
                }

                return;
            }
        }

        JOptionPane.showMessageDialog(
                this,
                "Student Not Found");
    }

    private void deleteStudent() {

        String name =
                JOptionPane.showInputDialog(
                        this,
                        "Enter Student Name");

        if (name == null)
            return;

        for (int i = 0; i < students.size(); i++) {

            if (students.get(i)
                    .getName()
                    .equalsIgnoreCase(name)) {

                students.remove(i);

                refreshTable();
                updateStatistics();

                JOptionPane.showMessageDialog(
                        this,
                        "Deleted Successfully");

                return;
            }
        }

        JOptionPane.showMessageDialog(
                this,
                "Student Not Found");
    }

    private void refreshTable() {

        model.setRowCount(0);

        for (Student s : students) {

            model.addRow(new Object[]{
                    s.getId(),
                    s.getName(),
                    s.getMark(),
                    s.getGrade()
            });
        }
    }

    private void saveToFile() {

        try {

            PrintWriter pw =
                    new PrintWriter(
                            new FileWriter(
                                    "students.txt"));

            for (Student s : students) {

                pw.println(
                        s.getId() + ","
                                + s.getName() + ","
                                + s.getMark());
            }

            pw.close();

            JOptionPane.showMessageDialog(
                    this,
                    "Data Saved Successfully");

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Error Saving File");
        }
    }

    private void updateStatistics() {

        if (students.isEmpty()) {

            lblAverage.setText("Average : 0");
            lblHighest.setText("Highest : 0");
            lblLowest.setText("Lowest : 0");
            return;
        }

        int total = 0;

        int highest =
                students.get(0).getMark();

        int lowest =
                students.get(0).getMark();

        for (Student s : students) {

            total += s.getMark();

            if (s.getMark() > highest)
                highest = s.getMark();

            if (s.getMark() < lowest)
                lowest = s.getMark();
        }

        double average =
                (double) total / students.size();
        lblAverage.setText(
                "Average : "
                        + String.format("%.2f",
                        average));
        lblHighest.setText(
                "Highest : " + highest);
        lblLowest.setText(
                "Lowest : " + lowest);
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(
                () -> new StudentGradeTrackerGUI());
    }
}
