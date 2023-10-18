import javax.swing.*;
import java.awt.*;

public class Frame extends JFrame {
    private RGB rgb;

    public RGB getRgb() {
        return rgb;
    }

    public HSL getHsl() {
        return hsl;
    }

    public CMYK getCmyk() {
        return cmyk;
    }

    private HSL hsl;
    private CMYK cmyk;
    private JPanel window  = new JPanel() {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setColor(rgb.getColor());
            g.fillRect(0, 0, getWidth(), getHeight());
        }
    };

    public Frame() throws HeadlessException {
        setSize(900, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        rgb = new RGB(this);
        hsl = new HSL(this);
        cmyk = new CMYK(this);

        JPanel palette = new JPanel();


        c.fill = GridBagConstraints.BOTH;
        c.insets = new Insets(10, 20, 10 , 20);
        c.weightx = 1;
        c.weighty = 1;
        c.gridy = 0;
        c.gridx = 0;
        add(rgb, c);
        c.gridx = 1;
        add(hsl, c);
        c.gridx = 2;
        add(cmyk, c);

        c.gridx = 0;
        c.gridwidth = 3;
        c.gridy = 1;
        add(window, c);
        c.gridy = 2;
        add(palette, c);
    }

    public JPanel getWindow() {
        return window;
    }
}
