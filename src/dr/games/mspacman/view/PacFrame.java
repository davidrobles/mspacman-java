package dr.games.mspacman.view;

import dr.util.FrameUtils;

import javax.swing.*;

public class PacFrame extends JFrame {

    public PacFrame(JPanel panel) {
        add(panel);
        pack();
        FrameUtils.centerFrame(this);
        setVisible(true);
    }

}
