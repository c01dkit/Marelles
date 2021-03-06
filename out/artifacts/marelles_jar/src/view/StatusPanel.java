package view;

import javax.swing.*;
import java.awt.*;

public class StatusPanel extends JPanel {
    private static JLabel timerLabel = null;
    private static JLabel playerLabel = null;
    private static JLabel oppoLabel = null;
    private final int width;
    private final int height;
    public StatusPanel(int x, int y, int w, int h){
        this.setBounds(x,y,w,h);
        this.setLayout(null);
        this.width = w;
        this.height = h;
    }

    public void init(String playerName, String oppoName){
        this.setVisible(true);
        // 设置倒计时显示
        if (timerLabel == null){
            timerLabel = new JLabel("未开始",JLabel.CENTER);
            timerLabel.setForeground(Color.RED);
            timerLabel.setBounds(0,0,width/4,height);
            this.add(timerLabel);
        }
        // 本地玩家信息展示
        if (playerLabel == null){
            playerLabel = new JLabel(playerName,JLabel.CENTER);
            playerLabel.setBounds(width/4,0,width*3/4,height/2);
            this.add(playerLabel);
        }
        // 对方信息展示
        if (oppoLabel == null){
            oppoLabel = new JLabel(oppoName,JLabel.CENTER);
            oppoLabel.setBounds(width/4,height/2,width*3/4,height/2);
            this.add(oppoLabel);
        }
    }

    public static String getPlayerName(int color){
        if (color == Chess.BLACK){
            return playerLabel.getText();
        }
        if (color == Chess.WHITE){
            return oppoLabel.getText();
        }
        return "Wrong Color!";
    }

    public static void sendGameInfo(String info){
        updateGameInfo(info, 1);
    }

    public static void sendGameInfo(String info, int level){
        updateGameInfo(info, level);
    }

    private static void updateGameInfo(String info, int level){
        oppoLabel.setText(info);
        if (level == 0) oppoLabel.setBackground(Color.red);
        if (level == 1) oppoLabel.setBackground(Color.black);
        if (level == 2) oppoLabel.setBackground(Color.lightGray);
    }
}
