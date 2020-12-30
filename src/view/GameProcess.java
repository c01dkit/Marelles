package view;

import javax.swing.*;
import java.awt.*;

public class GameProcess extends JPanel {
    public GameProcess(int x, int y, int w, int h){
        this.setLayout(null);
        this.setBounds(x,y,w,h);
    }

    public void init(){
        this.setVisible(true);
    }

//    public static void sendGameInfo(String info){
//        updateGameInfo(info, 1);
//    }
//
//    public static void sendGameInfo(String info, int level){
//        updateGameInfo(info, level);
//    }
//
//    private static void updateGameInfo(String info, int level){
//        gameLabel.setText(info);
//        if (level == 0) gameLabel.setBackground(Color.red);
//        if (level == 1) gameLabel.setBackground(Color.black);
//        if (level == 2) gameLabel.setBackground(Color.lightGray);
//    }
}
