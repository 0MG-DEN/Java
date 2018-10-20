package package1;

import java.io.*;
import java.util.*;

public class Connector {
    private String filename;

    public Connector(String filename) {
        this.filename = filename;
    }

    public List<Long> createIndexList(List<Employee> empsList) throws IOException {
        List<Long> indexList = new ArrayList<>();
        RandomAccessFile outFile = new RandomAccessFile(filename, "rw");
        for(Employee emp : empsList) {
            indexList.add(outFile.getFilePointer());
            outFile.write(emp.toByteArray());
        }
        outFile.close();
        return indexList;
    }

    public Employee readPos(int index, List<Long> indexList) throws IOException {
        RandomAccessFile inFile = new RandomAccessFile(filename, "r");
        inFile.seek(indexList.get(index));
        byte[] byteArray = new byte[1024];
        inFile.read(byteArray);
        inFile.close();
        return Employee.fromByteArray(byteArray);
    }
}
