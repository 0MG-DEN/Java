package package1;

import java.util.*;

public abstract class Series implements Comparable<Series>, Iterable<Integer>, Iterator<Integer> {
    private Integer areas[] = null;

    public static final String[] names = {"A0", "Difference"};
    public static final String titleString = String.format("%3s", names[0]) + " | " + names[1];

    public static class ArgException extends Exception {
        ArgException(String message) {
            super("Invalid argument: " + message);
        }
    }

    public Series() {
        areas = new Integer[2];
        areas[0] = Integer.MIN_VALUE;
        areas[1] = Integer.MIN_VALUE;
    }
    public Series(Integer[] areas) {
        this.areas = areas;
    }
    public Series(int inA0) {
        areas = new Integer[2];
        areas[0] = inA0;
        areas[1] = Integer.MIN_VALUE;
    }
    public Series(int inA0, int inProgDiff) throws ArgException{
        if(inProgDiff == 0) throw new ArgException("zero progression difference");
        areas = new Integer[2];
        areas[0] = inA0;
        areas[1] = inProgDiff;
    }
    public Series(String str) throws ArgException {
        if (str == null) {
            throw new ArgException( "null pointer input" );
        }
        int num = 1, idx = 0, idxFrom = 0;
        while ((idx = str.indexOf('|', idxFrom)) != -1) {
            idxFrom = idx + 1;
            num++;
        }
        int[] args = new int[num];
        num = 0; idx = 0; idxFrom = 0;
        while ((idx = str.indexOf('|', idxFrom)) != -1) {
            args[num++] = Integer.parseInt(str.substring(idxFrom, idx));
            idxFrom = idx + 1;
        }
        args[num] = Integer.parseInt(str.substring(idxFrom));
        areas = new Integer[names.length];
        int i = 0;
        for (; i < args.length; i++) {
            if(i == 0) {
                setA0(args[0]);
            }
            else if(i == 1) {
                setProgDiff(args[1]);
            }
        }
        while (i < names.length) {
            areas[i++] = Integer.MIN_VALUE;
        }
    }

    private static int sortBy = 0;
    public static int getSortBy() {
        return sortBy;
    }
    public static void setSortBy( int value ) throws IndexOutOfBoundsException {
        if ((value >= names.length) || (value < 0)) {
            throw new IndexOutOfBoundsException();
        }
        sortBy = value;
    }

    public int getA0() {
        return areas[0];
    }
    public int getProgDiff() {
        return areas[1];
    }
    public void setA0(int value) {
        this.areas[0] = value;
    }
    public void setProgDiff(int value) throws ArgException {
        if(value == 0) throw new ArgException("zero progression difference");
        this.areas[1] = value;
    }

    private int iterator_idx = 0;
    public Iterator<Integer> iterator() {
        reset();
        return this;
    }

    public void reset() {
        iterator_idx = 0;
    }
    public boolean hasNext() {
        return (iterator_idx < areas.length);
    }
    public Integer next() {
        if (iterator_idx < areas.length) {
            return areas[iterator_idx++];
        }
        reset();
        return Integer.MIN_VALUE;
    }

    public int compareTo(Series c) {
        return areas[Series.sortBy].compareTo(c.areas[Series.sortBy]);
    }
    public String toString() {
        if (areas == null) {
            return "";
        }
        String res = "";
        for (int i = 0; i < areas.length; i++) {
            res = new StringBuilder().append(res).append(String.format("%3s", this.areas[i])).append(" | ").toString();
        }
        return res;
    }
}