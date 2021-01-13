package view;

import controller.ButtonActionController;
import model.DatabaseManager;
import model.GameState;

import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame {
    private final int gameWidth;
    private final int gameHeight;
    private final int mainButtonWidth;
    private final int mainButtonHeight;
    private final int mainButtonPadding;
    private JButton[] mainButtonSet;
    private PlayBoard playBoard;
    private StatusPanel statusPanel;
    private GameProcess gameProcess;

    public MainWindow(){
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        gameWidth = toolkit.getScreenSize().width*7/10;
        gameHeight = toolkit.getScreenSize().height*4/5;
        mainButtonWidth = gameWidth*5/21;
        mainButtonHeight = gameHeight/15;
        mainButtonPadding = 30;
        Font mainButtonFont = new Font("楷体", Font.PLAIN, 24);
        Font mainMessageFont = new Font("楷体", Font.PLAIN, 24);
        UIManager.put("Button.font", mainButtonFont);
        UIManager.put("Label.font", mainMessageFont);
    }
    public void init(){
        this.setSize(gameWidth, gameHeight); // 设置初始化窗口大小
        this.setResizable(false); // 禁止缩放窗口大小
        this.setLocationRelativeTo(null); // 默认居中显示
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); // 退出窗口则结束进程
        this.setUndecorated(true); // 取消标题栏

        ImageIcon imageIcon = new ImageIcon(ConstantDataSet.backgourd);
        Image image = imageIcon.getImage();
        JPanel jPanel = new JPanel(){
            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(image, 0, 0, gameWidth, gameHeight,this);
                this.repaint();
            }
        };
        this.setIconImage(new ImageIcon(ConstantDataSet.logo).getImage());
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

    public void setToWelcomeView(){
        for (int i = 0; i < mainButtonSet.length; i++){
            mainButtonSet[i].setText(ConstantDataSet.mainButtonHint[i]);
            mainButtonSet[i].setBounds((gameWidth-mainButtonWidth)/2,
                    gameHeight*4/7+i*(mainButtonHeight+mainButtonPadding),
                    mainButtonWidth,mainButtonHeight);
        }
    }

    public void setToSingleGameView(String playerName){
        playBoard.init();
        GameState.startNewGame();
        statusPanel.init(playerName, ConstantDataSet.defaultOppoName);
        setGameInterface();
    }

    public void loadSingleGameView(){
        String[] names = GameState.getLastUnfinishedPlayerIDs();
        if (names == null) {
            JOptionPane.showMessageDialog(null,
                    new JLabel("没有未完成的游戏"),
                    "提示",JOptionPane.PLAIN_MESSAGE);
            return;
        }
        playBoard.load(GameState.getLastUnfinishedGameID());
        statusPanel.init(names[0],names[1]);
        setGameInterface();
    }

    public void setToGameWelcomeView(String type) {
        playBoard.setVisible(false);
        statusPanel.setVisible(false);
        gameProcess.setVisible(false);
        String[] hints = (type.equals("single")) ? ConstantDataSet.singleGameButtonHint :
                ConstantDataSet.multiGameButtonHint;
        for (int i = 0; i < mainButtonSet.length; i++) {
            mainButtonSet[i].setVisible(true);
            mainButtonSet[i].setText(hints[i]);
            mainButtonSet[i].setBounds((gameWidth - mainButtonWidth) / 2,
                    gameHeight * 4 / 7 + i * (mainButtonHeight + mainButtonPadding),
                    mainButtonWidth, mainButtonHeight);
        }
    }

    public void showRules(){
        JLabel jLabel = new JLabel(ConstantDataSet.gameRule);
        JOptionPane.showMessageDialog(null,jLabel,"游戏帮助",JOptionPane.PLAIN_MESSAGE);
    }

    public void saveGame(){
        playBoard.saveGame();
    }

    public void undo(){
        playBoard.undo();
        StatusPanel.sendGameInfo(ConstantDataSet.undoSelf);
    }

    public int getGameResult(){
        return playBoard.getGameResult();
    }

    private void initButtons(){
        mainButtonSet = new JButton[4];
        ButtonActionController buttonActionController = new ButtonActionController(this);
        for (int i = 0; i < mainButtonSet.length; i++){
            mainButtonSet[i] = new JButton();
            mainButtonSet[i].setText(ConstantDataSet.mainButtonHint[i]);
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
        mainButtonSet[2].setText(ConstantDataSet.undo);
        mainButtonSet[2].setBounds((gameWidth-mainButtonWidth)/2-mainButtonWidth-mainButtonPadding,
                gameHeight*4/7+(mainButtonSet.length-1)*(mainButtonHeight+mainButtonPadding),
                mainButtonWidth,mainButtonHeight);
        mainButtonSet[3].setText(ConstantDataSet.endGame);
    }
}
