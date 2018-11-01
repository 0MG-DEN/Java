package package1;

import java.io.*;
import java.util.*;

import static javafx.scene.input.KeyCode.U;

public class Main {
    private final static int SIZE = 7;

    public static void main(String[] args) {
	// write your code here
        List<Employee> empsList = new ArrayList<>();
        try {
            Scanner in = new Scanner(new File("Input.txt")); // fio file
            for(int i = 0; i < SIZE; i++) {
                    empsList.add(new Employee(
                            (int)(Math.random() * 7) + 1, //depNum
                            (in.hasNextLine()) ? in.nextLine() : "_", //fio
                            Math.random() * 2000 + 1, //salary
                            Math.random() * 1000 + 1, //surcharge
                            Math.random() //inTax
                            ));
            }
            for(Employee emp : empsList)
                System.out.println(emp.toString());

            Connector c = new Connector("out.dat");
            List<Long> indexList = c.createIndexList(empsList);

            System.out.println("Backward:");
            for(int i = indexList.size() - 1; i >= 0; i--)
                System.out.println(c.readByIndex(i).toString());

            System.out.println("Sorted by depNum:");
            Integer[] sortedDepNumKeys = c.getSortedDepNumKeys(false);
            for(Integer i : sortedDepNumKeys)
                c.printByDepNum(i);

            System.out.println("Sorted by fio (backward):");
            String[] sortedBackwardFioKeys = c.getSortedFioKeys(true);
            for(String i : sortedBackwardFioKeys)
                c.printByFio(i);

            System.out.println("Sorted by fio (Bob is deleted):");
            sortedBackwardFioKeys = c.getSortedFioKeys(false);
            c.deleteByFio("Bob");
            for(String i : sortedBackwardFioKeys)
                c.printByFio(i);

            System.out.println("Sorted by hireDate:");
            Date[] sortedHireDateKeys = c.getSortedHireDateKeys(false);
            for(Date i : sortedHireDateKeys)
                c.printByHireDate(i);
        }
        catch(Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
}