import java.util.Scanner;

class Maths1 {
    public static void main(String[] args) {
        //переменные
        double x, y, add, accuracy, check;
        int k;

        //ввод, вычисления
        if ( args.length != 2 ) {
            System.err.println("Invalid number of arguments");
            System.exit(1);
        }
        x = Double.parseDouble( args[0] );
        if ( x >= 1 || x < -1 ) {
            System.err.println("Invalid argument: " + x );
            System.exit(1);
        }
        k = Integer.parseInt( args[1] );
        if ( k <= 1 ) {
            System.err.println("Invalid argument: " + k );
            System.exit(1);
        }
        accuracy = Math.pow(10, -k);

        y = add = x;
        int i = 2;
        do {
            add *= x * x / (i * (i + 1));
            y += add;
            i += 2;
        }while(Math.abs(add) > accuracy);
        check = Math.sinh(x);

        //вывод
        System.out.print("X = " + x + "\nAccuracy = " + accuracy + "\n");
        System.out.printf("Recieved value = %." + k + "f\nCheck value = %." + k + "f\n", y, check);
    }
}