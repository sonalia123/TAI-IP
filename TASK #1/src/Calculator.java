import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Stack;

public class Calculator extends JFrame implements ActionListener {
    private JTextField inputField;
    private JButton[] buttons;
    private String inputSequence = "";
    private JTextArea historyArea;
    private JPanel calculatorPanel;

    private String currentResult = "";
    private StringBuilder historyBuilder = new StringBuilder();

    public Calculator() {
        setTitle("Calculator");
        setSize(400, 500);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Menu");

        JMenuItem standardMenuItem = new JMenuItem("Standard");
        standardMenuItem.addActionListener(this);
        menu.add(standardMenuItem);

        JMenuItem historyMenuItem = new JMenuItem("History");
        historyMenuItem.addActionListener(this);
        menu.add(historyMenuItem);

        menuBar.add(menu);
        setJMenuBar(menuBar);

        calculatorPanel = new JPanel();
        calculatorPanel.setPreferredSize(new Dimension(400, 500));
        calculatorPanel.setLayout(new BorderLayout());
        createCalculatorPanel();
        add(calculatorPanel, BorderLayout.CENTER);
    }

    private void createCalculatorPanel() {
        calculatorPanel.removeAll();

        JPanel inputPanel = new JPanel();
        inputPanel.setPreferredSize(new Dimension(400, 150));
        inputPanel.setLayout(new BorderLayout());
        inputPanel.setBackground(Color.LIGHT_GRAY);
        inputPanel.setBorder(BorderFactory.createEmptyBorder(15, 10, 20, 10));

        inputField = new JTextField();
        inputField.setFont(new Font("Arial", Font.PLAIN, 20));
        inputField.setHorizontalAlignment(JTextField.RIGHT);
        inputField.setEditable(false);
        inputPanel.add(inputField, BorderLayout.CENTER);
        calculatorPanel.add(inputPanel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setPreferredSize(new Dimension(400, 350));
        buttonPanel.setLayout(new GridLayout(7, 4, 4, 4));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
        buttonPanel.setBackground(Color.LIGHT_GRAY);

        String[] buttonTexts = {
                "n!", "(", ")", "AC",
                "+/-", "^", "%", "←",
                "1/x", "x^2", "√", "/",
                "7", "8", "9", "*",
                "4", "5", "6", "-",
                "1", "2", "3", "+",
                "Exit", "0", ".", "="
        };

        buttons = new JButton[buttonTexts.length];
        for (int i = 0; i < buttonTexts.length; i++) {
            buttons[i] = new JButton(buttonTexts[i]);
            buttons[i].addActionListener(this);

            if (buttonTexts[i].matches("[0-9]")) {
                buttons[i].setPreferredSize(new Dimension(70, 70));
            }
            Font buttonFont = buttons[i].getFont();
            buttons[i].setFont(buttonFont.deriveFont(Font.BOLD, 20));
            buttonPanel.add(buttons[i]);
        }

        calculatorPanel.add(buttonPanel, BorderLayout.CENTER);
        validate();
    }

    private void createHistoryPanel() {
        calculatorPanel.removeAll();

        JPanel historyPanel = new JPanel();
        historyPanel.setLayout(new BorderLayout());

        historyArea = new JTextArea();
        historyArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(historyArea);
        historyPanel.add(scrollPane, BorderLayout.CENTER);

        calculatorPanel.add(historyPanel, BorderLayout.CENTER);
        validate();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

        if (historyArea == null) {
            createHistoryPanel();
        }

        if (command.equals("n!")) {
            if (!inputSequence.isEmpty()) {
                double value = Double.parseDouble(inputSequence);
                value = factorial(value);
                inputField.setText(String.valueOf(value));
                inputSequence = String.valueOf(value);
            }
        } else if (command.equals("1/x")) {
            if (!inputSequence.isEmpty()) {
                double value = Double.parseDouble(inputSequence);
                if (value != 0) {
                    value = 1 / value;
                    inputField.setText(String.valueOf(value));
                    inputSequence = String.valueOf(value);
                } else {
                    inputField.setText("Error");
                    inputSequence = "";
                }
            }
        } else {
            if (command.matches("[0-9]")) {
                inputSequence += command;
                inputField.setText(inputSequence);
            } else if (command.equals(".")) {
                if (!inputSequence.contains(".")) {
                    inputSequence += ".";
                    inputField.setText(inputSequence);
                }
            } else if (command.equals("+/-")) {
                if (!inputSequence.isEmpty()) {
                    double value = Double.parseDouble(inputSequence);
                    value = -value;
                    inputSequence = String.valueOf(value);
                    inputField.setText(inputSequence);
                }
            } else if (command.equals("AC")) {
                inputSequence = "";
                inputField.setText(inputSequence);
            } else if (command.equals("←")) {
                if (!inputSequence.isEmpty()) {
                    inputSequence = inputSequence.substring(0, inputSequence.length() - 1);
                    inputField.setText(inputSequence);
                }
            } else if (command.equals("%")) {
                if (!inputSequence.isEmpty()) {
                    double value = Double.parseDouble(inputSequence);
                    value = value / 100;
                    inputSequence = String.valueOf(value);
                    inputField.setText(inputSequence);
                }
            } else if (command.equals("=")) {
                String expression = inputSequence;
                double result = evaluateExpression(expression);
                inputField.setText(String.valueOf(result));
                inputSequence = String.valueOf(result);
                currentResult = inputSequence;

                historyBuilder.append(expression).append(" = ").append(result).append("\n");
                historyArea.setText(historyBuilder.toString());
            } else if (command.matches("[+\\-*/^]")) {
                if (!inputSequence.isEmpty()) {
                    inputSequence += " " + command + " ";
                    inputField.setText(inputSequence);
                }
            } else if (command.equals("√")) {
                if (!inputSequence.isEmpty()) {
                    double value = Double.parseDouble(inputSequence);
                    value = Math.sqrt(value);
                    inputSequence = String.valueOf(value);
                    inputField.setText(inputSequence);
                }
            } else if (command.equals("x^2")) {
                if (!inputSequence.isEmpty()) {
                    double value = Double.parseDouble(inputSequence);
                    value = value * value;
                    inputSequence = String.valueOf(value);
                    inputField.setText(inputSequence);
                }
            } else if (command.equals("Exit")) {
                System.exit(0);
            } else if (command.equals("History")) {
                createHistoryPanel();
                historyArea.setText(historyBuilder.toString());
            } else if (command.equals("Standard")) {
                setSize(400, 500);
                setLocationRelativeTo(null);

                createCalculatorPanel();
                inputField.setText(currentResult);
                inputSequence = currentResult;
            }
        }
    }

    private boolean isOperator(String token) {
        return token.matches("[+\\-*/^]");
    }

    private double factorial(double n) {
        if (n <= 1) {
            return 1;
        } else {
            return n * factorial(n - 1);
        }
    }

    private double evaluateAdvancedExpression(String operator, double operand) {
        switch (operator) {
            case "n!":
                return factorial(operand);
            case "1/x":
                return 1 / operand;
            case "^":
                return Math.pow(operand, 2);
            default:
                throw new IllegalArgumentException("Invalid operator: " + operator);
        }
    }

    private double evaluateExpression(String expression) {
        String[] parts = expression.split(" ");
        Stack<Double> operands = new Stack<>();
        Stack<String> operators = new Stack<>();

        for (String part : parts) {
            if (part.matches("\\d+(\\.\\d+)?")) {
                operands.push(Double.parseDouble(part));
            } else if (isOperator(part)) {
                while (!operators.isEmpty() && precedence(part.charAt(0)) <= precedence(operators.peek().charAt(0))) {
                    String operator = operators.pop();
                    double operand2 = operands.pop();
                    double operand1 = operands.pop();
                    double result = performOperation(operator.charAt(0), operand1, operand2);
                    operands.push(result);
                }
                operators.push(part);
            } else if (part.equals("(")) {
                operators.push(part);
            } else if (part.equals(")")) {
                while (!operators.isEmpty() && !operators.peek().equals("(")) {
                    String operator = operators.pop();
                    double operand2 = operands.pop();
                    double operand1 = operands.pop();
                    double result = performOperation(operator.charAt(0), operand1, operand2);
                    operands.push(result);
                }
                operators.pop();
            } else if (part.matches("n!|1/x|\\^")) {
                double operand = operands.pop();
                double result = evaluateAdvancedExpression(part, operand);
                operands.push(result);
            }
        }

        while (!operators.isEmpty()) {
            String operator = operators.pop();
            double operand2 = operands.pop();
            double operand1 = operands.pop();
            double result = performOperation(operator.charAt(0), operand1, operand2);
            operands.push(result);
        }

        return operands.pop();
    }

    private int precedence(char operator) {
        switch (operator) {
            case '^':
                return 3;
            case '*':
            case '/':
                return 2;
            case '+':
            case '-':
                return 1;
            default:
                return 0;
        }
    }

    private double performOperation(char operator, double operand1, double operand2) {
        switch (operator) {
            case '^':
                return Math.pow(operand1, operand2);
            case '*':
                return operand1 * operand2;
            case '/':
                if (operand2 != 0) {
                    return operand1 / operand2;
                }
                throw new ArithmeticException("Division by zero");
            case '+':
                return operand1 + operand2;
            case '-':
                return operand1 - operand2;
            default:
                throw new IllegalArgumentException("Invalid operator: " + operator);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Calculator calculator = new Calculator();
            calculator.setVisible(true);
        });
    }
}