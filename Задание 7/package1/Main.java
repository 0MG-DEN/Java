package package1;

import java.io.*;
import java.util.*;

public class Main {
    private final static int SIZE = 5;

    public static void main(String[] args) {
	// write your code here
        List<Employee> eMass = new ArrayList<>();
        try {
            Scanner in = new Scanner(new File("Input.txt")); // fio file
            for(int i = 0; i < SIZE; i++) {
                    eMass.add(new Employee(
                            (i + 1), //tabNum
                            (int)(Math.random() * 7) + 1, //depNum
                            (in.hasNextLine()) ? in.nextLine() : "_", //fio
                            Math.random() * 2000 + 1, //salary
                            Math.random() * 1000 + 1, //surcharge
                            Math.random() //inTax
                            ));
            }
            for(Employee emp : eMass) {
                System.out.println(emp.toString());
            }

            Connector c = new Connector("out.dat");
            List<Long> indexList = c.createIndexList(eMass);
            System.out.println("Backward:");
            for(int i = indexList.size() - 1; i >= 0; i--) {
                Employee tmpEmp = c.readPos(i, indexList);
                System.out.println(tmpEmp.toString());
            }
        }
        catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
