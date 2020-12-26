package view;

import database.PlayerInfoTableManager;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class ButtonActionListener implements ActionListener {
    private final MainWindow mainWindow;
    private PlayerInfoTableManager playerInfoTableManager;
    public ButtonActionListener(MainWindow window){
        mainWindow = window;
        playerInfoTableManager = new PlayerInfoTableManager();
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
            //TODO: 单人游戏
            int playerID = setUpPlayer();
            if (playerID > 0){
                mainWindow.setToSingleGameView();
            }
        } else if (text.equals(MainWindowJFrame.endGame)){
            mainWindow.setToGameWelcomeView("single");
        } else if (text.equals(MainWindowJFrame.singleGameButtonHint[1])){
            //TODO:复盘战局
        } else if (text.equals(MainWindowJFrame.singleGameButtonHint[2])){
            dispalyPlayerInfo();
        } else if (text.equals(MainWindowJFrame.singleGameButtonHint[3])){
            mainWindow.setToWelcomeView();
        }

    }
    private int setUpPlayer(){
        String name = JOptionPane.showInputDialog(null,
                "<html>请输入玩家昵称<br>游戏确保玩家名称唯一，不存在则新建</html>",
                "设置昵称",JOptionPane.PLAIN_MESSAGE);
        if (name == null) return 0;
        int ans = 0;
        try {
            ans = playerInfoTableManager.addNewPlayer(name);
            if (ans == 0)
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
            String[] info = playerInfoTableManager.getAllPlayers();
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
