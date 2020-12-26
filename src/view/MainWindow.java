package view;

import database.DatabaseManager;

import javax.swing.*;
import java.awt.*;

public class MainWindow extends MainWindowJFrame {

    public MainWindow(){
        toolkit = Toolkit.getDefaultToolkit();
        gameWidth = toolkit.getScreenSize().width*7/10;
        gameHeight = toolkit.getScreenSize().height*4/5;
        mainButtonWidth = gameWidth*5/21;
        mainButtonHeight = gameHeight/15;
        mainButtonPadding = 30;
        mainButtonFont = new Font("楷体",Font.PLAIN,24);
        mainMessageFont = new Font("楷体",Font.PLAIN,24);
        UIManager.put("Button.font",mainButtonFont);
        UIManager.put("Label.font",mainMessageFont);
    }
    public void init(){
        this.setSize(gameWidth, gameHeight); // 设置初始化窗口大小，点击缩小窗口按钮时默认恢复至此大小
        this.setResizable(false);
        this.setLocationRelativeTo(null); // 默认居中显示
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setUndecorated(true); // 取消标题栏

        ImageIcon imageIcon = new ImageIcon(MainWindowJFrame.backgourd);
        Image image = imageIcon.getImage();
        JPanel jPanel = new JPanel(){
            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(image, 0, 0, gameWidth, gameHeight,this);
                this.repaint();
            }
        };
        this.setContentPane(jPanel);
        this.setLayout(null);

        mainButtonSet = new JButton[4];
        ButtonActionListener buttonActionListener = new ButtonActionListener(this);
        for (int i = 0; i < mainButtonSet.length; i++){
            mainButtonSet[i] = new JButton();
            mainButtonSet[i].setText(mainButtonHint[i]);
            mainButtonSet[i].addActionListener(buttonActionListener);
            mainButtonSet[i].setBounds((gameWidth-mainButtonWidth)/2,
                    gameHeight*4/7+i*(mainButtonHeight+mainButtonPadding),
                    mainButtonWidth,mainButtonHeight);
            this.add(mainButtonSet[i]);
        }

        playBoard = new PlayBoard(gameWidth/2-mainButtonWidth*3/2-mainButtonPadding,
                gameHeight/13, 2*mainButtonWidth+mainButtonPadding);
        playBoard.setVisible(false);
        this.add(playBoard);
        this.setVisible(true);
    }

    public void bind(DatabaseManager databaseManager){
        this.databaseManager = databaseManager;
    }

    public void setToWelcomeView(){
        for (int i = 0; i < mainButtonSet.length; i++){
            mainButtonSet[i].setText(mainButtonHint[i]);
            mainButtonSet[i].setBounds((gameWidth-mainButtonWidth)/2,
                    gameHeight*4/7+i*(mainButtonHeight+mainButtonPadding),
                    mainButtonWidth,mainButtonHeight);
        }
    }
    public void setToSingleGameView(){
        playBoard.init();
        mainButtonSet[0].setVisible(false);
        mainButtonSet[1].setVisible(false);
        mainButtonSet[2].setText(MainWindowJFrame.undo);
        mainButtonSet[2].setBounds((gameWidth-mainButtonWidth)/2-mainButtonWidth-mainButtonPadding,
                gameHeight*4/7+(mainButtonSet.length-1)*(mainButtonHeight+mainButtonPadding),
                mainButtonWidth,mainButtonHeight);
        mainButtonSet[3].setText(MainWindowJFrame.endGame);

    }

    public void setToGameWelcomeView(String type) {
        playBoard.setVisible(false);
        String[] hints = (type.equals("single")) ? singleGameButtonHint : multiGameButtonHint;
        for (int i = 0; i < mainButtonSet.length; i++) {
            mainButtonSet[i].setVisible(true);
            mainButtonSet[i].setText(hints[i]);
            mainButtonSet[i].setBounds((gameWidth - mainButtonWidth) / 2,
                    gameHeight * 4 / 7 + i * (mainButtonHeight + mainButtonPadding),
                    mainButtonWidth, mainButtonHeight);
        }
    }

    public void showRules(){
        JLabel jLabel = new JLabel(gameRule);
        JOptionPane.showMessageDialog(null,jLabel,"游戏帮助",JOptionPane.PLAIN_MESSAGE);
    }
}
