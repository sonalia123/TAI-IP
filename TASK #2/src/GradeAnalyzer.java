import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class GradeAnalyzer {
    private ArrayList<Student> students;
    private JFrame frame;
    private JTextArea outputArea;
    private JPanel studentInputPanel;
    private JButton generateReportButton;
    private JFrame messageFrame;

    public GradeAnalyzer() {
        students = new ArrayList<>();
        frame = new JFrame("Student Grade Analyzer");
        outputArea = new JTextArea(15, 40);

        outputArea.setEditable(false);

        JButton addButton = new JButton("Add Student");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showAddStudentDialog();
            }
        });

        JButton analyzeButton = new JButton("Analyze Grades");
        analyzeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showAnalyzeGradesPanel();

                if (!students.isEmpty()) {
                    Student lastStudent = students.get(students.size() - 1);
                    showMessagePanel(lastStudent.getName(), lastStudent.getSection(), lastStudent.getGrade());
                }
            }
        });

        generateReportButton = new JButton("Generate Report");
        generateReportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generateReport();
            }
        });
        generateReportButton.setEnabled(false);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);
        buttonPanel.add(analyzeButton);
        buttonPanel.add(generateReportButton);

        studentInputPanel = new JPanel();
        studentInputPanel.setLayout(new GridLayout(0, 2, 10, 10));

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(buttonPanel, BorderLayout.NORTH);
        mainPanel.add(new JScrollPane(outputArea), BorderLayout.CENTER);

        frame.add(mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    private void showAddStudentDialog() {
        JTextField nameField = new JTextField();
        JTextField studentIdField = new JTextField();
        JTextField sectionField = new JTextField();
        JTextField physicsField = new JTextField();
        JTextField chemistryField = new JTextField();
        JTextField mathsField = new JTextField();
        JTextField englishField = new JTextField();

        studentInputPanel.removeAll();
        studentInputPanel.add(new JLabel("Name:"));
        studentInputPanel.add(nameField);
        studentInputPanel.add(new JLabel("Student ID:"));
        studentInputPanel.add(studentIdField);
        studentInputPanel.add(new JLabel("Section:"));
        studentInputPanel.add(sectionField);
        studentInputPanel.add(new JLabel("Physics marks:"));
        studentInputPanel.add(physicsField);
        studentInputPanel.add(new JLabel("Chemistry marks:"));
        studentInputPanel.add(chemistryField);
        studentInputPanel.add(new JLabel("Maths marks:"));
        studentInputPanel.add(mathsField);
        studentInputPanel.add(new JLabel("English marks:"));
        studentInputPanel.add(englishField);

        int result = JOptionPane.showConfirmDialog(frame, studentInputPanel, "Add Student",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            String name = nameField.getText();
            String studentId = studentIdField.getText();
            String section = sectionField.getText();
            double physics = Double.parseDouble(physicsField.getText());
            double chemistry = Double.parseDouble(chemistryField.getText());
            double maths = Double.parseDouble(mathsField.getText());
            double english = Double.parseDouble(englishField.getText());

            Student student = new Student(name, studentId, section, physics, chemistry, maths, english);
            students.add(student);
            updateOutput();
            generateReportButton.setEnabled(true); // Enable after adding a student
        }
    }

    private void updateOutput() {
        outputArea.setText("");
        for (Student student : students) {
            JButton gradeButton = new JButton("Grade");
            gradeButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    showMessagePanel(student.getName(), student.getSection(), student.getGrade());
                }
            });

            outputArea.append(student.getName() + " ");
            outputArea.add(gradeButton);
            outputArea.append("\n");
        }
    }

    private void generateReport() {
        DecimalFormat df = new DecimalFormat("#.##");
        outputArea.setText("Student Performance Report\n\n");

        for (Student student : students) {
            outputArea.append("Name: " + student.getName() + "\n");
            outputArea.append("Id: " + student.getStudentId() + "\n");
            outputArea.append("Section: " + student.getSection() + "\n");
            outputArea.append("Average: " + df.format(student.calculateAverage()) + "\n");
            outputArea.append("Grades: " + student.getGrade() + "\n");

            String category = getGradeCategory(student.calculateAverage());
            outputArea.append("Grade Category: " + category + "\n\n");
        }
    }

    private String getGradeCategory(double average) {
        if (average >= 90) {
            return "Excellent";
        } else if (average >= 80) {
            return "Very Good";
        } else if (average >= 70) {
            return "Good";
        } else if (average >= 60) {
            return "Average";
        } else {
            return "Below Average";
        }
    }

    private class Student {
        private String name;
        private String studentId;
        private String section;
        private double physics;
        private double chemistry;
        private double maths;
        private double english;

        public Student(String name, String studentId, String section,
                double physics, double chemistry, double maths, double english) {
            this.name = name;
            this.studentId = studentId;
            this.section = section;
            this.physics = physics;
            this.chemistry = chemistry;
            this.maths = maths;
            this.english = english;
        }

        public double calculateAverage() {
            return (physics + chemistry + maths + english) / 4.0;
        }

        public String getName() {
            return name;
        }

        public String getStudentId() {
            return studentId;
        }

        public String getSection() {
            return section;
        }

        public double getTotalMarks() {
            return physics + chemistry + maths + english;
        }

        public double getPercentage() {
            return (getTotalMarks() / 400) * 100;
        }

        public String getGrade() {
            double percentage = getPercentage();
            if (percentage >= 90) {
                return "A";
            } else if (percentage >= 80) {
                return "B";
            } else if (percentage >= 70) {
                return "C";
            } else if (percentage >= 60) {
                return "D";
            } else {
                return "F";
            }
        }
    }

    private void showAnalyzeGradesPanel() {
        JPanel analyzePanel = new JPanel();
        analyzePanel.setLayout(new BorderLayout());

        JLabel highestLabel = new JLabel();
        JLabel lowestLabel = new JLabel();

        if (students.isEmpty()) {
            highestLabel.setText("No students to analyze.");
            lowestLabel.setText("");
        } else {
            double highestAverage = Double.MIN_VALUE;
            double lowestAverage = Double.MAX_VALUE;
            Student studentWithHighestAverage = null;
            Student studentWithLowestAverage = null;

            for (Student student : students) {
                double average = student.calculateAverage();
                if (average > highestAverage) {
                    highestAverage = average;
                    studentWithHighestAverage = student;
                }
                if (average < lowestAverage) {
                    lowestAverage = average;
                    studentWithLowestAverage = student;
                }
            }

            if (studentWithHighestAverage != null && studentWithLowestAverage != null) {
                highestLabel.setText("Highest Average: " + studentWithHighestAverage.getName() +
                        " (" + highestAverage + ")");
                lowestLabel.setText("Lowest Average: " + studentWithLowestAverage.getName() +
                        " (" + lowestAverage + ")");
            }
        }

        analyzePanel.add(highestLabel, BorderLayout.NORTH);
        analyzePanel.add(lowestLabel, BorderLayout.CENTER);

        JFrame analyzeFrame = new JFrame("Analyze Grades");
        analyzeFrame.setSize(300, 150);
        analyzeFrame.getContentPane().add(analyzePanel);
        analyzeFrame.pack();
        analyzeFrame.setLocationRelativeTo(frame);
        analyzeFrame.setVisible(true);
    }

    private void showMessagePanel(String studentName, String section, String grade) {
        JPanel messagePanel = new JPanel();
        messagePanel.setLayout(new BorderLayout());

        JLabel messageLabel = new JLabel(
                "<html><b>Hello:</b> " + studentName + " of Class " + section + ", Your Grade is: " + grade
                        + "</html>");
        messagePanel.add(messageLabel, BorderLayout.CENTER);

        JButton okButton = new JButton("OK");
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                messageFrame.dispose();
            }
        });
        messagePanel.add(okButton, BorderLayout.SOUTH);

        messageFrame = new JFrame("Grade Result");
        messageFrame.getContentPane().add(messagePanel);
        messageFrame.setSize(280, 150);
        messageFrame.setLocationRelativeTo(frame);
        messageFrame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new GradeAnalyzer();
            }
        });
    }
}