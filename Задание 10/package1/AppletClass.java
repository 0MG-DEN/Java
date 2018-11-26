package package1;

import java.applet.*;
import java.util.*;

public class AppletClass extends Applet {
    private static final int W = 300, H = 400;
    private java.util.List<StringCanvas> drawings = new ArrayList<>();

    @Override
    public void init() {
        super.init();
        setSize(W, H);
        setBackground(null);

        int i = 0;
        while (true) {
            String line;
            line = getParameter("param_" + i++);
            if (line == null) break;


            StringCanvas sCanvas = new StringCanvas(line, this);
            add(sCanvas);
            drawings.add(sCanvas);
        }
        new AppletThread(this).start();
    }

    class AppletThread extends Thread {
        AppletClass parent;

        public AppletThread(AppletClass parent) {
            super();
            this.parent = parent;
        }

        public void run() {
            while (true) {
                try {
                    Thread.sleep(50);
                    for (StringCanvas stringCanvas : parent.drawings) {
                        stringCanvas.move();
                        stringCanvas.repaint();
                    }
                } catch (InterruptedException e) {
                    break;
                }
            }
        }
    }
}
