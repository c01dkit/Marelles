package view;

import javax.swing.*;
import java.awt.*;

public class PlayBoard extends JPanel {
    public int block;
    private ImageIcon imageIcon;
    private Image image;
    private int edge;
    private PlayBoardMouseListener playBoardMouseListener;
    public PlayBoard(int x, int y,int edge){
        this.setLayout(null);
        this.setBounds(x,y,edge,edge);
        this.edge = edge;
        this.block = edge/8;
        this.playBoardMouseListener = new PlayBoardMouseListener(this);
        this.addMouseListener(playBoardMouseListener);
        imageIcon = new ImageIcon(MainWindowJFrame.board);
        image = imageIcon.getImage();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image,0,0,edge,edge,this);
        this.repaint();
    }

    public void init(){
        this.setVisible(true);
    }

    public void localClick(int x, int y){
        System.out.println(""+x+" "+y);
    }
}
