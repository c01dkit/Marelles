package view;

import javax.swing.*;
import java.awt.*;

public class ChatRoom extends JPanel {
    public ChatRoom(int gameWidth, int gameHeight){
        this.setLayout(null);
        int width = Math.abs((gameWidth-gameHeight)*3/4);
        int height = Math.min(gameHeight,gameWidth);
        this.setPreferredSize(new Dimension(width,height));
        this.setBackground(Color.gray);
    }
}
