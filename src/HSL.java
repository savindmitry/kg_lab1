import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;

public class HSL extends Cpanel{
    private JTextField h = new JTextField("0");
    private JTextField s = new JTextField("0");
    private JTextField l = new JTextField("100");
    private JScrollBar hs = new JScrollBar(Adjustable.HORIZONTAL, 0, 1, 0, 361);
    private JScrollBar ss = new JScrollBar(Adjustable.HORIZONTAL, 0, 1, 0, 101);
    private JScrollBar ls = new JScrollBar(Adjustable.HORIZONTAL, 100, 1, 0, 101);

    public HSL(Frame f) {
        parent = f;
        setLayout(new GridLayout(7, 1));

        add(new JLabel("HSL"));
        add(h);
        add(s);
        add(l);
        add(hs);
        add(ss);
        add(ls);

        DocumentListener dl = new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                handleTextChange();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                handleTextChange();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                handleTextChange();
            }
        };
        AdjustmentListener al = new AdjustmentListener() {
            @Override
            public void adjustmentValueChanged(AdjustmentEvent e) {
                if (!updatingFromScrollBars) {
                    int[] val = hslToRgb(hs.getValue(), ss.getValue(), ls.getValue());
                    color = new Color(val[0], val[1], val[2]);
                    updateText();
                }
            }
        };

        h.getDocument().addDocumentListener(dl);
        s.getDocument().addDocumentListener(dl);
        l.getDocument().addDocumentListener(dl);

        hs.addAdjustmentListener(al);
        ss.addAdjustmentListener(al);
        ls.addAdjustmentListener(al);
    }
    private static int[] hslToRgb(int hue, int saturation, int lightness) {
        double h = hue / 360.0;
        double s = saturation / 100.0;
        double l = lightness / 100.0;

        double r, g, b;

        if (s == 0) {
            r = g = b = l;
        } else {
            double q = l < 0.5 ? l * (1 + s) : l + s - l * s;
            double p = 2 * l - q;
            r = hueToRgb(p, q, h + 1.0 / 3.0);
            g = hueToRgb(p, q, h);
            b = hueToRgb(p, q, h - 1.0 / 3.0);
        }

        int red = (int) Math.round(r * 255);
        int green = (int) Math.round(g * 255);
        int blue = (int) Math.round(b * 255);

        return new int[] { red, green, blue };
    }

    private static double hueToRgb(double p, double q, double t) {
        if (t < 0)
            t += 1;
        if (t > 1)
            t -= 1;
        if (t < 1.0 / 6.0)
            return p + (q - p) * 6 * t;
        if (t < 1.0 / 2.0)
            return q;
        if (t < 2.0 / 3.0)
            return p + (q - p) * (2.0 / 3.0 - t) * 6;
        return p;
    }
    private static int[] rgbToHsl(int red, int green, int blue) {
        double r = red / 255.0;
        double g = green / 255.0;
        double b = blue / 255.0;

        double max = Math.max(r, Math.max(g, b));
        double min = Math.min(r, Math.min(g, b));
        double hue, saturation, lightness;

        if (max == min) {
            hue = 0;
        } else if (max == r) {
            hue = ((g - b) / (max - min)) % 6.0;
        } else if (max == g) {
            hue = (b - r) / (max - min) + 2.0;
        } else {
            hue = (r - g) / (max - min) + 4.0;
        }
        hue *= 60.0;
        if (hue < 0) {
            hue += 360.0;
        }

        lightness = ((max + min) / 2.0) * 100.0;

        if (max == min) {
            saturation = 0;
        } else if (lightness <= 50.0) {
            saturation = ((max - min) / (max + min)) * 100.0;
        } else {
            saturation = ((max - min) / (2.0 - max - min)) * 100.0;
        }

        return new int[] { (int) Math.round(hue), (int) Math.round(saturation), (int) Math.round(lightness) };
    }

    private void handleTextChange() {
        if (!updatingFromTextFields) {
            Integer hue;
            Integer saturation;
            Integer lightness;
            try {
                hue = Integer.parseInt(h.getText());
            } catch (NumberFormatException e) {
                hue = 0;
            }
            try {
                saturation = Integer.parseInt(s.getText());
            } catch (NumberFormatException e) {
                saturation = 0;
            }
            try {
                lightness = Integer.parseInt(l.getText());
            } catch (NumberFormatException e) {
                lightness = 0;
            }

            int[] val = hslToRgb(hue, saturation, lightness);
            color = new Color(val[0], val[1], val[2]);
            updateScrolls();
        }
    }

    public void updateText() {
        updatingFromTextFields = true;
        int[] val = rgbToHsl(color.getRed(), color.getGreen(), color.getBlue());
        h.setText(String.valueOf(val[0]));
        s.setText(String.valueOf(val[1]));
        l.setText(String.valueOf(val[2]));
        updatingFromTextFields = false;
        updateMainWindow();
    }

    public void updateScrolls() {
        updatingFromScrollBars = true;
        int[] val = rgbToHsl(color.getRed(), color.getGreen(), color.getBlue());
        hs.setValue(val[0]);
        ss.setValue(val[1]);
        ls.setValue(val[2]);
        updatingFromScrollBars = false;
        updateMainWindow();
    }

    private void updateMainWindow() {
        if (!updatingFromOther) {
            updateParent();
            parent.getRgb().updatingFromOther = true;
            parent.getCmyk().updatingFromOther = true;
            parent.getRgb().setColor(color);
            parent.getRgb().updateText();
            parent.getRgb().updateScrolls();
            parent.getCmyk().setColor(color);
            parent.getCmyk().updateText();
            parent.getCmyk().updateScrolls();
            parent.getRgb().updatingFromOther = false;
            parent.getCmyk().updatingFromOther = false;
        }
    }
}
