package package1;

import java.applet.Applet;
import java.awt.*;

class StringCanvas extends Canvas {
    private Applet parent;
    private String line;
    private Color color;
    public int x, y, xVector, yVector;
    private static final int SIZE = 25,
            COLOR_BASE = 50, COLOR_RANGE = 150,
            VECTOR_BASE = -10, VECTOR_RANGE = 20;

    private int getRandomColorValue() {
        return (int) (COLOR_BASE + Math.random() * COLOR_RANGE);
    }

    private Color getRandomColor() {
        return new Color(getRandomColorValue(), getRandomColorValue(), getRandomColorValue());
    }

    private int getRandomCoordinate() {
        return (int) (VECTOR_BASE + Math.random() * VECTOR_RANGE);
    }

    private void setRandomValues() {
        x = (int) (Math.random() * parent.getWidth());
        y = (int) (Math.random() * parent.getHeight());
        do {
            xVector = getRandomCoordinate();
        } while (xVector == 0);
        do {
            yVector = getRandomCoordinate();
        } while (yVector == 0);

        color = getRandomColor();
    }

    public StringCanvas(String line, Applet parent) {
        this.line = line;
        this.parent = parent;
        setRandomValues();
        setBounds(0, 0, SIZE * line.length(), 2 * SIZE);
    }

    @Override
    public void paint(Graphics g) {
        if (x > parent.getWidth() || y > parent.getHeight() || x < 0 || y < 0) {
            setRandomValues();
        }
        setLocation(x, y);
        g.setColor(this.color);
        g.setFont(new Font("Courier", Font.BOLD, SIZE));
        g.drawString(line, 0, SIZE);
    }

    public void move() {
        this.x += xVector;
        this.y += yVector;
    }
}
