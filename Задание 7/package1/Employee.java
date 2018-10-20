package package1;

import java.io.*;
import java.util.*;
import java.text.SimpleDateFormat;

public class Employee implements Serializable {
    private final int tabNum;
    private int depNum;
    private String fio;
    private double salary;
    private final Date hireDate;
    private double surcharge;
    transient private double inTax;

    public Employee(int tabNum, int depNum, String fio, double salary, double surcharge, double inTax) throws IllegalArgumentException {
        if(tabNum <= 0 || depNum <= 0 || fio == null || salary <= 0 || surcharge <= 0 || inTax < 0 || inTax > 1) {
            throw new IllegalArgumentException();
        }
        this.tabNum = tabNum;
        this.depNum = depNum;
        this.fio = fio;
        this.salary = salary;
        this.hireDate = new Date();
        this.surcharge = surcharge;
        this.inTax = inTax;
    }

    public byte[] toByteArray() {
        try {
            ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
            ObjectOutputStream objectStream = new ObjectOutputStream(byteStream);
            objectStream.writeObject(this);
            objectStream.flush();
            objectStream.close();
            return byteStream.toByteArray();
        }
        catch(IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    static public Employee fromByteArray(byte[] inputByteArray) {
        try {
            ObjectInputStream objectStream = new ObjectInputStream(new ByteArrayInputStream(inputByteArray));
            return (Employee) objectStream.readObject();
        }
        catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String toString() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        return "Employee#" + String.format("%07d", tabNum) +
                "{depNum=" + depNum +
                ", fio=" + fio +
                ", salary=" + String.format("%.2f", salary) +
                ", hireDate=" + dateFormat.format(hireDate) +
                ", surcharge=" + String.format("%.2f", surcharge) +
                '}';
    }
}
