import javax.swing.*;
import java.awt.*;

public class Cpanel extends JPanel {
    boolean updatingFromTextFields = false;
    boolean updatingFromScrollBars = false;
    boolean updatingFromOther = false;

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    void updateParent() {
        parent.getWindow().repaint();
    }
    Color color = new Color(255, 255, 255);
    Frame parent;
}
