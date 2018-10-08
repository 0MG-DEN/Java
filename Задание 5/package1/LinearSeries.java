package package1;

public class LinearSeries extends  Series {
    public LinearSeries(int inA0, int inProgDiff) throws Series.ArgException{
        super(inA0, inProgDiff);
    }
    public LinearSeries(String str) throws ArgException {
        super(str);
    }

    int element(int index) throws IndexOutOfBoundsException {
        if(index < 0) {
            throw new IndexOutOfBoundsException();
        }
        if(index == 0) {
            return this.getA0();
        }
        return this.getA0() + (int)(Math.pow(this.getProgDiff(), index));
    }
    int progSum(int index) throws IndexOutOfBoundsException {
        if(index <= 0) {
            throw new IndexOutOfBoundsException();
        }
        if(index == 1) {
            return this.getA0();
        }
        return (int)((2 * this.getA0() + this.getProgDiff() * (index - 1)) * ((double)index / 2));
    }
}
