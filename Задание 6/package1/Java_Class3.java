package package1;

import java.util.Locale;
import java.util.ResourceBundle;

public class Java_Class3 {
    private final static int SIZE = 15;

    private static void printStationList(TelephoneStation station) {
        for(TelephoneStation.Subscriber sub : station.subsList) {
            System.out.println(sub.toString());
        }
    }

    public static void main(String[] args) {

        if(args.length == 2) {
            Locale.setDefault(new Locale(args[0], args[1]));
        }
        else {
            Locale.setDefault(Locale.ENGLISH);
        }

        try {
            //TelephoneStation
            TelephoneStation station1 = new TelephoneStation();

            for (int i = 0; i < SIZE; i++) {
                Thread.sleep(1000);
                station1.addNewSubscriber(1000000 + (int) (9000000 * Math.random()) - 1, (int) (100 * Math.random()));
            }
            station1.subsList.get(station1.subsList.size() - 1).changeNum(1234567);
            printStationList(station1);

            //timer
            station1.timer();
            station1.checkAll();
            printStationList(station1);

            //Connector
            Connector out = new Connector("out.dat");
            out.write(station1.subsList);

            Connector in = new Connector("out.dat");
            TelephoneStation station2 = new TelephoneStation(in.read());
            printStationList(station2);
        }
        catch (Exception e) {
            System.out.print("Exception " + e.getMessage());
        }
    }
}
