package package1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class SearchWindow extends JFrame {
    private final static int constraint = 100;

    public SearchWindow(JList list, DefaultListModel<Employee> listModel) throws HeadlessException {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.add(new JLabel("Full name:"));
        JTextField searchField = new JTextField();
        mainPanel.add(searchField);
        JButton search = new JButton("Search");
        mainPanel.add(search);

        search.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int[] indices = null;
                    java.util.List<Employee> tempList = new ArrayList<>();

                    for (int i = 0; i < listModel.size(); i++)
                        tempList.add(listModel.get(i));

                    for (Employee emp : tempList)
                        if (emp.getFio().equals(searchField.getText()))
                            indices = insertValue(indices, listModel.indexOf(emp));

                    list.setSelectedIndices(indices);
                } catch (NullPointerException ignored) {

                }
            }
        });

        searchField.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_ENTER: {
                        search.doClick();
                    }
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });

        setResizable(false);
        setSize(new Dimension(2 * constraint, constraint));
        setContentPane(mainPanel);
        setVisible(true);
    }

    private static int[] insertValue(int[] arr, int value) {
        int length = (arr == null) ? 0 : arr.length;
        int[] result = new int[length + 1];
        for (int i = 0; i < length; i++) {
            result[i] = arr[i];
        }
        result[length] = value;
        return result;
    }
}
