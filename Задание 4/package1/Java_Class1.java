package package1;

public class Java_Class1 {
    public static void main(String[] args)
    {
        int     SIZE = 10, // students
                MARKSC = 5; // marks(subjects) for each abiturient
        String[] names = new String[SIZE];
        for(int i = 0; i < SIZE; i++) {
            names[i] = "Abiturient" + (i + 1) + " name";
            //System.out.println(names[i]);
        }

        int[][] marks = new int[SIZE][MARKSC];
        for(int i = 0; i < SIZE; i++) {
            for (int j = 0; j < MARKSC; j++) {
                marks[i][j] = (int) (Math.random() * 100);
                //System.out.printf("%3d", marks[i][j]);
            }
            //System.out.println();
        }

        Abiturient[] abiturients = new Abiturient[SIZE];
        for(int i = 0; i < SIZE; i++) {
            abiturients[i] = new Abiturient(names[i], marks[i]);
            abiturients[i].out();
            System.out.println();
        }

        Abiturient.passedabiturients(abiturients,250,4);
    }
}