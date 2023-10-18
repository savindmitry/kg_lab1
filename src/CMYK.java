import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;

public class CMYK extends Cpanel{
    private JTextField c = new JTextField("0");
    private JTextField m = new JTextField("0");
    private JTextField y = new JTextField("0");
    private JTextField k = new JTextField("0");
    private JScrollBar cs = new JScrollBar(Adjustable.HORIZONTAL, 0, 1, 0, 101);
    private JScrollBar ms = new JScrollBar(Adjustable.HORIZONTAL, 0, 1, 0, 101);
    private JScrollBar ys = new JScrollBar(Adjustable.HORIZONTAL, 0, 1, 0, 101);
    private JScrollBar ks = new JScrollBar(Adjustable.HORIZONTAL, 0, 1, 0, 101);

    public CMYK(Frame f) {
        parent = f;
        setLayout(new GridLayout(9, 1));

        add(new JLabel("CMYK"));
        add(c);
        add(m);
        add(y);
        add(k);
        add(cs);
        add(ms);
        add(ys);
        add(ks);

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
                    int[] val = cmykToRgb(cs.getValue(), ms.getValue(), ys.getValue(), ks.getValue());
                    color = new Color(val[0], val[1], val[2]);
                    updateText();
                    updateScrolls();
                }
            }
        };

        c.getDocument().addDocumentListener(dl);
        m.getDocument().addDocumentListener(dl);
        y.getDocument().addDocumentListener(dl);
        k.getDocument().addDocumentListener(dl);

        cs.addAdjustmentListener(al);
        ms.addAdjustmentListener(al);
        ys.addAdjustmentListener(al);
        ks.addAdjustmentListener(al);
    }

    private static int[] rgbToCmyk(int red, int green, int blue) {
        double r = red / 255.0;
        double g = green / 255.0;
        double b = blue / 255.0;

        int k = (int) Math.round((1 - Math.max(r, Math.max(g, b))) * 100);
        int c = 0, m = 0, y = 0;
        if (k != 100) {
            c = (int) Math.round(((1 - r - (k / 100.0)) / (1 - (k / 100.0))) * 100);
            m = (int) Math.round(((1 - g - (k / 100.0)) / (1 - (k / 100.0))) * 100);
            y = (int) Math.round(((1 - b - (k / 100.0)) / (1 - (k / 100.0))) * 100);
        }
        if (c < 0) {
            c = 0;
        }
        if (m < 0) {
            m = 0;
        }
        if (y < 0) {
            y = 0;
        }
        return new int[]{c, m, y, k};
    }

    private static int[] cmykToRgb(int c, int m, int y, int k) {
        double r = 255 * (1 - (c / 100.0)) * (1 - (k / 100.0));
        double g = 255 * (1 - (m / 100.0)) * (1 - (k / 100.0));
        double b = 255 * (1 - (y / 100.0)) * (1 - (k / 100.0));

        return new int[]{(int) Math.round(r), (int) Math.round(g), (int) Math.round(b)};
    }

    private void handleTextChange() {
        if (!updatingFromTextFields) {
            Integer cyan;
            Integer magenta;
            Integer yellow;
            Integer black;
            try {
                cyan = Integer.parseInt(c.getText());
            } catch (NumberFormatException e) {
                cyan = 0;
            }
            try {
                magenta = Integer.parseInt(m.getText());
            } catch (NumberFormatException e) {
                magenta = 0;
            }
            try {
                yellow = Integer.parseInt(y.getText());
            } catch (NumberFormatException e) {
                yellow = 0;
            }
            try {
                black = Integer.parseInt(k.getText());
            } catch (NumberFormatException e) {
                black = 0;
            }

            int[] val = cmykToRgb(cyan, magenta, yellow, black);
            setColor(new Color(val[0], val[1], val[2]));
            updateScrolls();
        }
    }

    public void updateText() {
        updatingFromTextFields = true;
        int[] val = rgbToCmyk(color.getRed(), color.getGreen(), color.getBlue());
        c.setText(String.valueOf(val[0]));
        m.setText(String.valueOf(val[1]));
        y.setText(String.valueOf(val[2]));
        k.setText(String.valueOf(val[3]));
        updatingFromTextFields = false;
        updateMainWindow();
    }

    public void updateScrolls() {
        updatingFromScrollBars = true;
        int[] val = rgbToCmyk(color.getRed(), color.getGreen(), color.getBlue());
        cs.setValue(val[0]);
        ms.setValue(val[1]);
        ys.setValue(val[2]);
        ks.setValue(val[3]);
        updatingFromScrollBars = false;
        updateMainWindow();
    }

    private void updateMainWindow() {
        if (!updatingFromOther) {
            updateParent();
            parent.getRgb().updatingFromOther = true;
            parent.getHsl().updatingFromOther = true;
            parent.getRgb().setColor(color);
            parent.getRgb().updateText();
            parent.getRgb().updateScrolls();
            parent.getHsl().setColor(color);
            parent.getHsl().updateText();
            parent.getHsl().updateScrolls();
            parent.getRgb().updatingFromOther = false;
            parent.getHsl().updatingFromOther = false;
        }
    }
}
