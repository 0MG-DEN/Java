package package1;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.util.ArrayList;

public class MainWindow extends JFrame {
    private final static int constraint = 325;
    private final static String statusHead = " Status: ";

    public MainWindow() throws HeadlessException {
        JFrame self = this;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel status = new JLabel(statusHead + "");
        DefaultListModel<Employee> listModel = new DefaultListModel();
        JList listView = new JList(listModel);
        JScrollPane scrollPane = new JScrollPane(listView);

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));

        JTextField depNumInput = new JTextField();
        JTextField fioInput = new JTextField();
        JTextField salaryInput = new JTextField();
        JButton submit = new JButton("Submit");
        formPanel.add(new JLabel("Department number:"));
        formPanel.add(depNumInput);
        formPanel.add(new JLabel("Full name:"));
        formPanel.add(fioInput);
        formPanel.add(new JLabel("Salary:"));
        formPanel.add(salaryInput);

        submit.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int depNum = Integer.parseInt(depNumInput.getText());
                    double salary = Double.parseDouble(salaryInput.getText());
                    String fio = fioInput.getText();

                    listModel.addElement(new Employee(depNum, fio, salary));
                    status.setText(statusHead + "input succeed");
                } catch (NumberFormatException ignored) {
                    status.setText(statusHead + "input error, check input data");
                }
            }
        });

        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem load = new JMenuItem("Load");
        JMenuItem save = new JMenuItem("Save");

        fileMenu.add(load);
        fileMenu.add(save);
        menuBar.add(fileMenu);
        setJMenuBar(menuBar);

        load.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int result = fileChooser.showOpenDialog(self);
                if (result != JFileChooser.APPROVE_OPTION) {
                    return;
                }
                File file = fileChooser.getSelectedFile();
                try {
                    FileInputStream fis = new FileInputStream(file);
                    ObjectInputStream ois = new ObjectInputStream(fis);
                    ArrayList<Employee> empsList = (ArrayList) ois.readObject();
                    ois.close();
                    fis.close();
                    listModel.clear();
                    for (Employee emp : empsList) {
                        listModel.addElement(emp);
                    }
                    status.setText(statusHead + "loaded file \"" + file.getName() + '\"');
                } catch (IOException | ClassNotFoundException e1) {
                    e1.printStackTrace();
                }
            }
        });

        save.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int result = fileChooser.showSaveDialog(self);
                if (result != JFileChooser.APPROVE_OPTION) {
                    return;
                }
                File file = fileChooser.getSelectedFile();
                try {
                    FileOutputStream fos = new FileOutputStream(file);
                    ObjectOutputStream oos = new ObjectOutputStream(fos);
                    ArrayList<Employee> emps = new ArrayList<>();
                    for (int i = 0; i < listModel.size(); ++i) {
                        emps.add(listModel.get(i));
                    }
                    oos.writeObject(emps);
                    oos.close();
                    fos.close();
                    status.setText(statusHead + "list save as \"" + file.getName() + '\"');
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(scrollPane, BorderLayout.NORTH);

        JPanel southPanel = new JPanel(new BorderLayout());
        JPanel southButtonPanel = new JPanel();
        southButtonPanel.setLayout(new BoxLayout(southButtonPanel, BoxLayout.X_AXIS));
        southButtonPanel.add(submit);
        southButtonPanel.add(status);

        southPanel.add(formPanel, BorderLayout.CENTER);
        southPanel.add(southButtonPanel, BorderLayout.SOUTH);
        panel.add(southPanel, BorderLayout.SOUTH);

        setSize(new Dimension(2 * constraint, constraint));
        setResizable(false);
        setContentPane(panel);
        setVisible(true);

    }
}