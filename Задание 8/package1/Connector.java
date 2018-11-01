package package1;

import java.io.*;
import java.util.*;

public class Connector {
    private String filename;
    private boolean fileIsCreated;

    public List<Long> indexList;
    public Map<Integer, long[]> depNumIndexMap;
    public Map<String, long[]> fioIndexMap;
    public Map<Date, long[]> hireDateIndexMap;

    public Connector(String filename) {
        this.filename = filename;
        this.fileIsCreated = false;

        this.indexList = new ArrayList<>();
        this.depNumIndexMap = new HashMap<>();
        this.fioIndexMap = new HashMap<>();
        this.hireDateIndexMap = new HashMap<>();
    }

    public List<Long> createIndexList(List<Employee> empsList) throws IOException {
        RandomAccessFile outFile = new RandomAccessFile(filename, "rw");
        for(Employee emp : empsList) {
            this.indexList.add(outFile.getFilePointer());

            if(this.depNumIndexMap.containsKey(emp.getDepNum()))
                this.depNumIndexMap.replace(emp.getDepNum(), this.depNumIndexMap.get(emp.getDepNum()),
                        Connector.insertValue(this.depNumIndexMap.get(emp.getDepNum()), outFile.getFilePointer()));
            else
                this.depNumIndexMap.put(emp.getDepNum(), new long[] {outFile.getFilePointer()});

            if(this.fioIndexMap.containsKey(emp.getFio()))
                this.fioIndexMap.replace(emp.getFio(), this.fioIndexMap.get(emp.getFio()),
                        Connector.insertValue(this.fioIndexMap.get(emp.getFio()), outFile.getFilePointer()));
            else
                this.fioIndexMap.put(emp.getFio(), new long[] {outFile.getFilePointer()});

            if(this.hireDateIndexMap.containsKey(emp.getHireDate()))
                this.hireDateIndexMap.replace(emp.getHireDate(), this.hireDateIndexMap.get(emp.getHireDate()),
                        Connector.insertValue(this.hireDateIndexMap.get(emp.getHireDate()), outFile.getFilePointer()));
            else
                this.hireDateIndexMap.put(emp.getHireDate(), new long[] {outFile.getFilePointer()});


            outFile.writeInt(emp.toByteArray().length);
            outFile.write(emp.toByteArray());
        }
        outFile.close();
        this.fileIsCreated = true;
        return this.indexList;
    }

    private Employee readPos(long pos) throws IOException {
        if (this.fileIsCreated) {
            RandomAccessFile inFile = new RandomAccessFile(filename, "r");
            inFile.seek(pos);
            byte[] byteArray = new byte[inFile.readInt()];
            inFile.read(byteArray);
            inFile.close();
            return Employee.fromByteArray(byteArray);
        } else throw new FileNotFoundException();
    }

    public Employee readByIndex(int index) throws IOException {
        if(index < 0 || index >= this.indexList.size())
            throw new IndexOutOfBoundsException();
        return this.readPos(this.indexList.get(index));
    }

    public void printByDepNum(int depNum) throws IllegalArgumentException, IOException {
        if(this.depNumIndexMap.containsKey(depNum)) {
            for (long pos : this.depNumIndexMap.get(depNum)) {
                Employee tmpEmp = readPos(pos);
                if(!tmpEmp.isDeleted())
                    System.out.println(tmpEmp.toString());
            }
        }
        else throw new IllegalArgumentException();
    }

    public void printByFio(String fio) throws IllegalArgumentException, IOException {
        if(this.fioIndexMap.containsKey(fio)) {
            for (long pos : this.fioIndexMap.get(fio)) {
                Employee tmpEmp = readPos(pos);
                if(!tmpEmp.isDeleted())
                    System.out.println(tmpEmp.toString());
            }
        }
        else throw new IllegalArgumentException();
    }

    public void printByHireDate(Date hireDate) throws IllegalArgumentException, IOException {
        if(this.hireDateIndexMap.containsKey(hireDate)) {
            for (long pos : this.hireDateIndexMap.get(hireDate)) {
                Employee tmpEmp = readPos(pos);
                if(!tmpEmp.isDeleted())
                    System.out.println(tmpEmp.toString());
            }
        }
        else throw new IllegalArgumentException();
    }

    private void deletePos(long pos) {
        try {
            if(this.fileIsCreated) {
                RandomAccessFile edFile = new RandomAccessFile(filename, "rw");
                edFile.seek(pos);
                byte[] byteArray = new byte[edFile.readInt()];
                edFile.read(byteArray);
                Employee tmpEmp = Employee.fromByteArray(byteArray);
                if (!tmpEmp.isDeleted()) {
                    tmpEmp.setDeleted(true);
                    edFile.seek(pos);
                    edFile.writeInt(byteArray.length);
                    edFile.write(tmpEmp.toByteArray());
                }
                edFile.close();
            }
            else throw new FileNotFoundException();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteByIndex(int index) {
        if(index < 0 || index >= this.indexList.size())
            throw new IndexOutOfBoundsException();
        deletePos(this.indexList.get(index));
    }

    public void deleteByDepNum(int depNum) {
        if(this.depNumIndexMap.containsKey(depNum))
            for (long pos : this.depNumIndexMap.get(depNum))
                this.deletePos(pos);
    }

    public void deleteByFio(String fio) {
        if(this.fioIndexMap.containsKey(fio))
            for (long pos : this.fioIndexMap.get(fio))
                this.deletePos(pos);
    }

    public void deleteByHireDate(Date hireDate) {
        if(this.hireDateIndexMap.containsKey(hireDate))
            for (long pos : this.hireDateIndexMap.get(hireDate))
                this.deletePos(pos);
    }

    private static long[] insertValue(long[] arr, long value) {
        int length = (arr == null) ? 0 : arr.length;
        long[] result = new long[length + 1];
        for(int i = 0; i < length; i++) {
            result[i] = arr[i];
        }
        result[length] = value;
        return result;
    }

    public Integer[] getSortedDepNumKeys(boolean backward) {
        Set<Integer> resultSet = this.depNumIndexMap.keySet();
        Integer[] result = resultSet.toArray(new Integer[0]);
        Arrays.sort(result, new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return (backward) ? o2.compareTo(o1) : o1.compareTo(o2);
            }
        });
        return result;
    }

    public String[] getSortedFioKeys(boolean backward) {
        Set<String> resultSet = this.fioIndexMap.keySet();
        String[] result = resultSet.toArray(new String[0]);
        Arrays.sort(result, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return (backward) ? o2.compareTo(o1) : o1.compareTo(o2);
            }
        });
        return result;
    }

    public Date[] getSortedHireDateKeys(boolean backward) {
        Set<Date> resultSet = this.hireDateIndexMap.keySet();
        Date[] result = resultSet.toArray(new Date[0]);
        Arrays.sort(result, new Comparator<Date>() {
            @Override
            public int compare(Date o1, Date o2) {
                return (backward) ? o2.compareTo(o1) : o1.compareTo(o2);
            }
        });
        return result;
    }
}
