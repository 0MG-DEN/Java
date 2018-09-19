import java.util.Scanner;

public class Java_Matr1 {
    public static final String ANSI_RED = "\u001B[31m";     // <─┬ для удобства
    public static final String ANSI_GREEN = "\u001B[32m";   // <─┤(возможно не сработает)
    public static final String ANSI_RESET = "\u001B[0m";    // <─┘

    public static void main( String[] args ) {
        System.out.print("Введите размерность матрицы: ");
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        if (n < 1) {
            System.err.println(
                    "Invalid input");
            System.exit(1);
        }
        int[][] matr = new int[n][n];

        System.out.print("Исходная матрица:\n");
        for(int i =0; i < n; i++) {
            for(int j =0; j < n; j++) { // инициализация строк
                matr[i][j] = -n + (int)(Math.random() * 2 * n);
                System.out.printf(((matr[i][j]==0) ? ANSI_RED : "") + "%4d" + ANSI_RESET,  matr[i][j]);
            }
            System.out.print("\n");

            int an = n - 1;
            for(int j =0; (j < an) && (an >= n / 2); j++) { // преобразование строк
                if( matr[i][j]==0) {
                    int tmp =  matr[i][j];
                    matr[i][j] =  matr[i][an];
                    matr[i][an--] = tmp;
                }
            }
        }

        System.out.print("Изменённая матрица:\n");
        for(int i =0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                System.out.printf(((matr[i][j]==0) ? ANSI_GREEN : "") + "%4d" + ANSI_RESET, matr[i][j]);
            }
            System.out.print("\n");
        }
        System.exit(0);
    }
}
