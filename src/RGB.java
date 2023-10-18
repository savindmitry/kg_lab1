import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;

public class RGB extends Cpanel {
    private JTextField r = new JTextField("255");
    private JTextField g = new JTextField("255");
    private JTextField b = new JTextField("255");
    private JScrollBar rs = new JScrollBar(Adjustable.HORIZONTAL, 255, 1, 0, 256);
    private JScrollBar gs = new JScrollBar(Adjustable.HORIZONTAL, 255, 1, 0, 256);
    private JScrollBar bs = new JScrollBar(Adjustable.HORIZONTAL, 255, 1, 0, 256);

    public RGB(Frame f) {
        parent = f;
        setLayout(new GridLayout(7, 1));

        add(new JLabel("RGB"));
        add(r);
        add(g);
        add(b);
        add(rs);
        add(gs);
        add(bs);

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
                    color = new Color(rs.getValue(), gs.getValue(), bs.getValue());
                    updateText();
                }
            }
        };

        r.getDocument().addDocumentListener(dl);
        g.getDocument().addDocumentListener(dl);
        b.getDocument().addDocumentListener(dl);

        rs.addAdjustmentListener(al);
        gs.addAdjustmentListener(al);
        bs.addAdjustmentListener(al);
    }

    private void handleTextChange() {
        if (!updatingFromTextFields) {
            Integer red;
            Integer green;
            Integer blue;
            try {
                red = Integer.parseInt(r.getText());
            } catch (NumberFormatException e) {
                red = 0;
            }
            try {
                green = Integer.parseInt(g.getText());
            } catch (NumberFormatException e) {
                green = 0;
            }
            try {
                blue = Integer.parseInt(b.getText());
            } catch (NumberFormatException e) {
                blue = 0;
            }

            color = new Color(red, green, blue);
            updateScrolls();
        }
    }

    public void updateText() {
        updatingFromTextFields = true;
        r.setText(String.valueOf(color.getRed()));
        g.setText(String.valueOf(color.getGreen()));
        b.setText(String.valueOf(color.getBlue()));
        updatingFromTextFields = false;
        updateMainWindow();
    }

    private void updateMainWindow() {
        if (!updatingFromOther) {
            updateParent();
            parent.getHsl().updatingFromOther = true;
            parent.getCmyk().updatingFromOther = true;
            parent.getHsl().setColor(color);
            parent.getHsl().updateText();
            parent.getHsl().updateScrolls();
            parent.getCmyk().setColor(color);
            parent.getCmyk().updateText();
            parent.getCmyk().updateScrolls();
            parent.getHsl().updatingFromOther = false;
            parent.getCmyk().updatingFromOther = false;
        }
    }

    public void updateScrolls() {
        updatingFromScrollBars = true;
        rs.setValue(color.getRed());
        gs.setValue(color.getGreen());
        bs.setValue(color.getBlue());
        updatingFromScrollBars = false;
        updateMainWindow();
    }
}
