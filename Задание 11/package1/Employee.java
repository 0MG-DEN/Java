package package1;

import java.io.*;
import java.util.*;
import java.text.SimpleDateFormat;

public class Employee implements Serializable {
    private static int nextTabNum = 1;

    private final int tabNum;
    private int depNum;
    private String fio;
    private double salary;
    private final Date hireDate;
    private boolean deleted;

    public Employee() {
        this.tabNum = nextTabNum++;
        this.hireDate = new Date();

        this.depNum = -1;
        this.fio = null;
        this.salary = -1;

        this.deleted = false;
    }

    public Employee(int depNum, String fio, double salary) {
        this.tabNum = nextTabNum++;
        this.hireDate = new Date();

        try {
            if (depNum <= 0 || fio == null || salary <= 0) {
                throw new IllegalArgumentException();
            }
            this.depNum = depNum;
            this.fio = fio;
            this.salary = salary;
            this.deleted = false;
        }
        catch (IllegalArgumentException e) {
            e.printStackTrace();
            new Employee();
        }
    }

    public byte[] toByteArray() {
        try {
            ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
            ObjectOutputStream objectStream = new ObjectOutputStream(byteStream);
            objectStream.writeObject(this);
            objectStream.flush();
            objectStream.close();
            byteStream.flush();
            byteStream.close();
            return byteStream.toByteArray();
        }
        catch(IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    static public Employee fromByteArray(byte[] inputByteArray) {
        try {
            if(inputByteArray == null) throw new NullPointerException();
            ByteArrayInputStream byteStream = new ByteArrayInputStream(inputByteArray);
            try(ObjectInputStream objectStream = new ObjectInputStream(byteStream)) {
                return (Employee) objectStream.readObject();
            }
        }
        catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int getDepNum() {
        return depNum;
    }

    public String getFio() {
        return fio;
    }

    public Date getHireDate() {
        return hireDate;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    @Override
    public String toString() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        return "Employee#" + String.format("%07d", tabNum) +
                "{depNum=" + depNum +
                ", fio=" + fio +
                ", salary=" + String.format("%.2f", salary) +
                ", hireDate=" + dateFormat.format(hireDate) +
                '}';
    }
}
