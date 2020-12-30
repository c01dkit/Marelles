package view;

import javax.swing.*;
import java.awt.*;

public class StatusPanel extends JPanel {
    private static JLabel timerLabel;
    private static JLabel playerLabel;
    private static JLabel gameLabel;
    private final int width;
    private final int height;
    public StatusPanel(int x, int y, int w, int h){
        this.setBounds(x,y,w,h);
        this.setLayout(null);
        this.width = w;
        this.height = h;
    }

    public void init(String playerName){
        this.setVisible(true);
        Font messageFont = new Font("楷体",Font.PLAIN,16);
        // 设置倒计时显示
        timerLabel = new JLabel("--",JLabel.CENTER);
        timerLabel.setForeground(Color.RED);
        timerLabel.setBounds(0,0,width/4,height);
        this.add(timerLabel);
        // 设置玩家信息显示
        playerLabel = new JLabel(playerName,JLabel.CENTER);
        playerLabel.setBounds(width/4,0,width*3/4,height/2);
        this.add(playerLabel);
        // 游戏状态显示
        gameLabel = new JLabel("游戏开始",JLabel.CENTER);
        gameLabel.setFont(messageFont);
        gameLabel.setBounds(width/4,height/2,width*3/4,height/2);
        this.add(gameLabel);
    }

    public static void sendGameInfo(String info){
        updateGameInfo(info, 1);
    }

    public static void sendGameInfo(String info, int level){
        updateGameInfo(info, level);
    }

    private static void updateGameInfo(String info, int level){
        gameLabel.setText(info);
        if (level == 0) gameLabel.setBackground(Color.red);
        if (level == 1) gameLabel.setBackground(Color.black);
        if (level == 2) gameLabel.setBackground(Color.lightGray);
    }
}
