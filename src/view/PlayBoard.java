package view;

import javax.swing.*;
import java.awt.*;

public class PlayBoard extends JPanel {
    public PlayBoard(int gameWidth, int gameHeight){
        this.setLayout(null);
        int edge = Math.min(gameHeight,gameWidth);
        this.setPreferredSize(new Dimension(edge,edge));
        this.setBackground(new Color(255,240,193,255));
    }
}
