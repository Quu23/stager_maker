import java.awt.Image;
import java.awt.MouseInfo;
import java.util.List;
import java.util.ArrayList;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;
import javax.swing.SwingUtilities;

public class App {

    public static Point mousePoint;
    public static int nowEntity;
    public static Image tempImage;
    protected static List<Entity> entities;

    public static void main(String[] args) {
        Window win = new Window();

        entities = new ArrayList<>();
        nowEntity = EntityKind.NONE;
        mousePoint = new Point(0,0);

        Timer timer = new Timer(10, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                win.drawPanel.repaint();
                mousePoint = MouseInfo.getPointerInfo().getLocation();
                SwingUtilities.convertPointFromScreen(mousePoint, win.drawPanel);
            }
        });

        win.setVisible(true);

        timer.start();
    }

}
