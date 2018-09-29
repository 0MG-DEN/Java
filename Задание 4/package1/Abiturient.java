package package1;

public class Abiturient {
    private String fio = "";
    private int[] marks;

    public Abiturient() {}
    public Abiturient(String inFio) {
        this.fio = inFio;
    }
    public Abiturient(String inFio, int[] inMarks) {
        this.fio = inFio;
        this.marks = inMarks;
    }
    public void out() {
        System.out.println("Abiturient: " + this.fio + "\nMarks:");
        for(int i = 0; i < marks.length; i++) {
            System.out.println("Subject" + (i + 1) + ": " + this.marks[i]);
        }
        System.out.println("Absolute mark: " + this.absmark());
    }
    public int absmark() {
        int absMark = 0;
        for(int i = 0; i < this.marks.length; i++)
            absMark += this.marks[i];
        return absMark;
    }
    public static void passedabiturients(Abiturient[] abiturients, int passAbsMark, int count) {
        assert ((abiturients.length > 0) && (passAbsMark > 0) && (count > 0)) : "Invalid input arguments";
        System.out.println("List of passed abiturient:");
        int p = 0;
        for(int i = 0; (i < abiturients.length) && (p < count); i++) {
            if(abiturients[i].absmark() >= passAbsMark) {
                System.out.println((p++ + 1) + ". " + abiturients[i].fio);
            }
        }
        if(p == 0) {
            System.out.println("None passed");
        }
    }
}