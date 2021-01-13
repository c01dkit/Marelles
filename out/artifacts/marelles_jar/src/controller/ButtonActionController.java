package controller;

import model.PlayerInfo;
import view.Chess;
import view.MainWindow;
import view.ConstantDataSet;
import view.StatusPanel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class ButtonActionController implements ActionListener {
    private final MainWindow mainWindow;
    public ButtonActionController(MainWindow window){
        mainWindow = window;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        String text = e.getActionCommand();
        if (text.equals(ConstantDataSet.mainButtonHint[0])) {
            mainWindow.setToGameWelcomeView("single");
        } else if (text.equals(ConstantDataSet.mainButtonHint[1])){
            mainWindow.setToGameWelcomeView("multi");
        } else if (text.equals(ConstantDataSet.mainButtonHint[2])){
            mainWindow.showRules();
        } else if (text.equals(ConstantDataSet.mainButtonHint[3])) {
            System.exit(0);
        } else if (text.equals(ConstantDataSet.singleGameButtonHint[0])){
            // 设定玩家昵称，新建一个游戏
            String playerName = setUpPlayer();
            if (playerName != null){
                mainWindow.setToSingleGameView(playerName);
            }
        } else if (text.equals(ConstantDataSet.multiGameButtonHint[0])){ // 双人游戏 创建房间
            // 设定玩家昵称，新建一个游戏
            String playerName = setUpPlayer();
            // 设定房间编号，新建一个游戏

        } else if (text.equals(ConstantDataSet.multiGameButtonHint[1])){ // 双人游戏 加入房间
            // 设定玩家昵称
            String playerName = setUpPlayer();
        } else if (text.equals(ConstantDataSet.singleGameButtonHint[1])){ //单人游戏，继续游戏
            mainWindow.loadSingleGameView();
        } else if (text.equals(ConstantDataSet.singleGameButtonHint[2])){ //展示玩家信息
            dispalyPlayerInfo();
        } else if (text.equals(ConstantDataSet.singleGameButtonHint[3])){
            mainWindow.setToWelcomeView();
        } else if (text.equals(ConstantDataSet.undo)){ // 悔棋
            mainWindow.undo();
            String name = StatusPanel.getPlayerName(Chess.BLACK);
            PlayerInfo.updateGameResultInfo(-1,name);
        } else if (text.equals(ConstantDataSet.endGame)){ //从游戏中退出，保存游戏
            mainWindow.saveGame();
            mainWindow.setToGameWelcomeView("single");
            String name = StatusPanel.getPlayerName(Chess.BLACK);
            PlayerInfo.updateGameResultInfo(mainWindow.getGameResult(),name);
        }

    }
    private String setUpPlayer(){
        String name = JOptionPane.showInputDialog(null,
                "<html>请输入玩家昵称<br>游戏确保玩家名称唯一，不存在则新建</html>",
                "设置昵称",JOptionPane.PLAIN_MESSAGE);
        if (name == null) return null;
        String ans = null;
        try {
            ans = PlayerInfo.addNewPlayer(name);
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
            String[] info = PlayerInfo.getAllPlayers();
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
