package package1;

import java.io.*;
import java.util.*;

public class Connector {
    private String filename;

    public Connector(String filename) {
        this.filename = filename;
    }

    public void write(List<TelephoneStation.Subscriber> subsList) throws IOException {
        FileOutputStream fileOutStream = new FileOutputStream(filename);
        ObjectOutputStream objOutStream = new ObjectOutputStream(fileOutStream);
        objOutStream.writeInt(subsList.size());
        for(TelephoneStation.Subscriber sub : subsList) {
            objOutStream.writeObject(sub);
        }
        objOutStream.flush();
        objOutStream.close();
    }

    public List<TelephoneStation.Subscriber> read() throws IOException, ClassNotFoundException {
        FileInputStream fileInStream = new FileInputStream(filename);
        ObjectInputStream objInStream = new ObjectInputStream(fileInStream);
        int lenght = objInStream.readInt();
        List<TelephoneStation.Subscriber> list = new ArrayList<>();
        for(int i = 0; i < lenght; i++) {
            list.add((TelephoneStation.Subscriber)objInStream.readObject());
        }
        objInStream.close();
        return list;
    }
}
