import java.util.Scanner;
import java.io.File;

public class Java_String1 {
    public static void main( String[] args ) {
        try {
            if (args.length != 1) {
                System.err.println(
                        "Invalid number of arguments");
                System.exit(1);
            }
            // параметр - путь к файлу
            Scanner in = new Scanner(new File(args[0]));
            int  idxFrom, idx;
            while (in.hasNextLine()) {
                idxFrom = idx = -1;
                String s = in.nextLine(), temp;
                for(int i = 0; i < s.length(); i++) {
                    if(s.charAt(i)=='(') {
                        idxFrom = i;
                    }
                    else if(s.charAt(i)==')') {
                        idx = i;
                    }
                    if((idx-idxFrom > 0) && (idx > 0) && (idxFrom > 0)) {
                        temp = s.substring(0, idxFrom) + s.substring(idx + 1);
                        s = temp;
                        i -= (idx - idxFrom + 1);
                        idxFrom = idx = -1;
                    }
                }
                System.out.println(s);
            }
            in.close();
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
        System.exit(0);
    }
}



