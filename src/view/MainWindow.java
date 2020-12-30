package view;

import controller.ButtonActionController;
import model.DatabaseManager;

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
        this.setSize(gameWidth, gameHeight); // 设置初始化窗口大小
        this.setResizable(false); // 禁止缩放窗口大小
        this.setLocationRelativeTo(null); // 默认居中显示
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); // 退出窗口则结束进程
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

        // 添加游戏主要的按钮，进行界面切换
        initButtons();

        // 添加游戏所用棋盘
        initPlayBoard();

        // 添加游戏状态面板，提供提示窗口
        initStatusPanel();

        // 添加游戏详情窗口
        initGameProcess();

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

    public void setToSingleGameView(String playerName){
        playBoard.init();
        statusPanel.init(playerName);
        setGameInterface();
    }

    public void loadSingleGameView(){
        playBoard.load();
        statusPanel.init("未完成的游戏");
        setGameInterface();
    }

    public void setToGameWelcomeView(String type) {
        playBoard.setVisible(false);
        statusPanel.setVisible(false);
        gameProcess.setVisible(false);
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

    public void saveGame(){
        playBoard.saveGame();
    }

    public void undo(){
        playBoard.undo();
    }

    private void initButtons(){
        mainButtonSet = new JButton[4];
        ButtonActionController buttonActionController = new ButtonActionController(this);
        for (int i = 0; i < mainButtonSet.length; i++){
            mainButtonSet[i] = new JButton();
            mainButtonSet[i].setText(mainButtonHint[i]);
            mainButtonSet[i].addActionListener(buttonActionController);
            mainButtonSet[i].setBounds((gameWidth-mainButtonWidth)/2,
                    gameHeight*4/7+i*(mainButtonHeight+mainButtonPadding),
                    mainButtonWidth,mainButtonHeight);
            this.add(mainButtonSet[i]);
        }
    }

    private void initPlayBoard(){
        playBoard = new PlayBoard(gameWidth/2-mainButtonWidth*3/2-mainButtonPadding,
                gameHeight/13, 2*mainButtonWidth+mainButtonPadding);
        playBoard.setVisible(false);
        this.add(playBoard);
    }

    private void initStatusPanel(){
        statusPanel = new StatusPanel(gameWidth/2+mainButtonWidth/2+2*mainButtonPadding,
                gameHeight/13,mainButtonPadding+mainButtonWidth,3*mainButtonHeight);
        statusPanel.setVisible(false);
        this.add(statusPanel);
    }

    private void initGameProcess(){
        gameProcess = new GameProcess(gameWidth/2+mainButtonWidth/2+2*mainButtonPadding,
                gameHeight/13+3*mainButtonHeight+2*mainButtonPadding,
                mainButtonWidth+mainButtonPadding,
                gameHeight*45/91+mainButtonPadding+mainButtonHeight);
        gameProcess.setVisible(false);
        this.add(gameProcess);
    }

    private void setGameInterface(){
        gameProcess.init();
        mainButtonSet[0].setVisible(false);
        mainButtonSet[1].setVisible(false);
        mainButtonSet[2].setText(MainWindowJFrame.undo);
        mainButtonSet[2].setBounds((gameWidth-mainButtonWidth)/2-mainButtonWidth-mainButtonPadding,
                gameHeight*4/7+(mainButtonSet.length-1)*(mainButtonHeight+mainButtonPadding),
                mainButtonWidth,mainButtonHeight);
        mainButtonSet[3].setText(MainWindowJFrame.endGame);
    }
}
