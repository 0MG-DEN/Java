package package1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

public class MainWindow extends JFrame {
    private final static int constraint = 350;
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

        listView.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                
            }

            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_DELETE: {
                        if (!listView.isSelectionEmpty()) {
                            Employee employee = (Employee) listView.getSelectedValue();
                            if(employee.isDeleted()) {
                                status.setText(statusHead + "object is already deleted");
                                return;
                            }
                            if (JOptionPane.showConfirmDialog(null, "Delete this object?") == 0) {
                                employee.setDeleted(true);
                                status.setText(statusHead + "object " + employee.getTabNumFormat() + " deleted");
                            } else
                                status.setText(statusHead + "deleting refused");
                        }
                    }
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });

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
                    depNumInput.setText("");
                    fioInput.setText("");
                    salaryInput.setText("");
                    status.setText(statusHead + "input succeed");
                } catch (IllegalArgumentException ignored) {
                    status.setText(statusHead + "input error, check input data");
                }
            }
        });

        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem load = new JMenuItem("Load");
        JMenuItem save = new JMenuItem("Save");
        JMenu sortMenu = new JMenu("Sort by...");
        JMenuItem byDepNum = new JMenuItem("dep.num.");
        JMenuItem byFio = new JMenuItem("full name");
        JMenuItem byHireDate = new JMenuItem("hire date");

        fileMenu.add(load);
        fileMenu.add(save);
        sortMenu.add(byDepNum);
        sortMenu.add(byFio);
        sortMenu.add(byHireDate);
        menuBar.add(fileMenu);
        menuBar.add(sortMenu);
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
                        if (!emp.isDeleted())
                            listModel.addElement(emp);
                    }
                    status.setText(statusHead + "loaded file \"" + file.getName() + '\"');
                } catch (IOException | ClassNotFoundException e1) {
                    status.setText(statusHead + e1.getMessage());
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
                    for (int i = 0; i < listModel.size(); i++) {
                        emps.add(listModel.get(i));
                    }
                    oos.writeObject(emps);
                    oos.close();
                    fos.close();
                    status.setText(statusHead + "list saved as \"" + file.getName() + '\"');
                } catch (IOException e1) {
                    status.setText(statusHead + e1.getMessage());
                }
            }
        });

        byDepNum.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                java.util.List<Employee> tempList = new ArrayList<>();
                for (int i = 0; i < listModel.size(); i++)
                    tempList.add(listModel.get(i));
                listModel.clear();
                Collections.sort(tempList, new Comparator<Employee>() {
                    @Override
                    public int compare(Employee o1, Employee o2) {
                        return Integer.compare(o1.getDepNum(), o2.getDepNum());
                    }
                });
                for (Employee emp : tempList)
                    listModel.addElement(emp);
            }
        });

        byFio.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                java.util.List<Employee> tempList = new ArrayList<>();
                for (int i = 0; i < listModel.size(); i++)
                    tempList.add(listModel.get(i));
                listModel.clear();
                Collections.sort(tempList, new Comparator<Employee>() {
                    @Override
                    public int compare(Employee o1, Employee o2) {
                        return o1.getFio().compareTo(o2.getFio());
                    }
                });
                for (Employee emp : tempList)
                    listModel.addElement(emp);
            }
        });

        byHireDate.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                java.util.List<Employee> tempList = new ArrayList<>();
                for (int i = 0; i < listModel.size(); i++)
                    tempList.add(listModel.get(i));
                listModel.clear();
                Collections.sort(tempList, new Comparator<Employee>() {
                    @Override
                    public int compare(Employee o1, Employee o2) {
                        return (o1.getHireDate()).compareTo(o2.getHireDate());
                    }
                });
                for (Employee emp : tempList)
                    listModel.addElement(emp);
            }
        });

        KeyListener enterKL = new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_ENTER: {
                        submit.doClick();
                    }
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        };
        depNumInput.addKeyListener(enterKL);
        fioInput.addKeyListener(enterKL);
        salaryInput.addKeyListener(enterKL);

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