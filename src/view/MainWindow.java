package view;

import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame {
    private Toolkit toolkit;
    private final int gameWidth;
    private final int gameHeight;
    public MainWindow(){
        toolkit = Toolkit.getDefaultToolkit();
        gameWidth = toolkit.getScreenSize().width*4/5;
        gameHeight = toolkit.getScreenSize().height*4/5;
    }
    public void init(){
        this.setSize(gameWidth, gameHeight); // 设置初始化窗口大小，点击缩小窗口按钮时默认恢复至此大小
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);  // 打开时全屏展示，不覆盖任务栏
        System.out.println(this.getSize().toString());
        this.setLocationRelativeTo(null); // 默认居中显示
        this.setTitle("Marelles");
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.add(new ChatRoom(gameWidth,gameHeight),BorderLayout.EAST);
        this.add(new PlayBoard(gameWidth,gameHeight),BorderLayout.CENTER);
        this.add(new StatusBar(),BorderLayout.SOUTH);
        this.setVisible(true);
    }
}
