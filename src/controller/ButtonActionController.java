package controller;

import model.GameState;
import model.PlayerInfo;
import view.MainWindow;
import view.MainWindowJFrame;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class ButtonActionController implements ActionListener {
    private final MainWindow mainWindow;
    private final PlayerInfo playerInfo;
    public ButtonActionController(MainWindow window){
        mainWindow = window;
        playerInfo = new PlayerInfo();
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        String text = e.getActionCommand();
        if (text.equals(MainWindowJFrame.mainButtonHint[0])) {
            mainWindow.setToGameWelcomeView("single");
        } else if (text.equals(MainWindowJFrame.mainButtonHint[1])){
            mainWindow.setToGameWelcomeView("multi");
        } else if (text.equals(MainWindowJFrame.mainButtonHint[2])){
            mainWindow.showRules();
        } else if (text.equals(MainWindowJFrame.mainButtonHint[3])) {
            System.exit(0);
        } else if (text.equals(MainWindowJFrame.singleGameButtonHint[0])){
            String playerName = setUpPlayer();
            if (playerName != null){
                mainWindow.setToSingleGameView(playerName);
            }
        } else if (text.equals(MainWindowJFrame.endGame)){ //从游戏中退出，保存游戏
            mainWindow.saveGame();
            mainWindow.setToGameWelcomeView("single");
        } else if (text.equals(MainWindowJFrame.singleGameButtonHint[1])){ //单人游戏，继续游戏
            mainWindow.loadSingleGameView();
        } else if (text.equals(MainWindowJFrame.singleGameButtonHint[2])){
            dispalyPlayerInfo();
        } else if (text.equals(MainWindowJFrame.singleGameButtonHint[3])){
            mainWindow.setToWelcomeView();
        }

    }
    private String setUpPlayer(){
        String name = JOptionPane.showInputDialog(null,
                "<html>请输入玩家昵称<br>游戏确保玩家名称唯一，不存在则新建</html>",
                "设置昵称",JOptionPane.PLAIN_MESSAGE);
        if (name == null) return null;
        String ans = null;
        try {
            ans = playerInfo.addNewPlayer(name);
            if (ans == null)
                JOptionPane.showMessageDialog(null,"添加玩家失败！");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return ans;
    }

    private void dispalyPlayerInfo(){
        // 玩家信息
        JLabel[] jLabels;
        try {
            String[] info = playerInfo.getAllPlayers();
            if (info!=null){
                int size = info.length;
                jLabels = new JLabel[size];
                for (int i = 0; i < size; i++){
                    jLabels[i] = new JLabel(info[i]);
                }
                JOptionPane.showMessageDialog(null,jLabels,
                        "玩家信息",JOptionPane.PLAIN_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null,"尚未进行游戏",
                        "玩家信息",JOptionPane.PLAIN_MESSAGE);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
