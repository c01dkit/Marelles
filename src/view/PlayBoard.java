package view;

import controller.PlayBoardController;

import javax.swing.*;
import java.awt.*;

public class PlayBoard extends JPanel {
    public int block;
    private final Image image;
    private final int edge;
    private Chess[] chessSet;
    public PlayBoard(int x, int y,int edge){
        this.setLayout(null);
        this.setBounds(x,y,edge,edge);
        this.edge = edge;
        this.block = edge/8;

        PlayBoardController playBoardController = new PlayBoardController(this);
        this.addMouseListener(playBoardController);
        ImageIcon imageIcon = new ImageIcon(MainWindowJFrame.board);
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
        Chess.loadChessIcon();
        this.chessSet = new Chess[24];
        for (int i = 0; i < 24; i++){
            this.chessSet[i] = new Chess(Chess.NONE);
            int x = MainWindowJFrame.chessPostionMap[i][0]*block-block/2;
            int y = MainWindowJFrame.chessPostionMap[i][1]*block-block/2;
            this.chessSet[i].setBounds(x,y,100,100);
            this.add(chessSet[i]);
        }
    }

    public boolean jumpChess(int from, int to){
        if (to < 0 || to >= chessSet.length) return false;
        if (from < 0 || from >= chessSet.length) return false;
        int fromColor = chessSet[from].getColor();
        int toColor = chessSet[to].getColor();
        if (fromColor != Chess.NONE && toColor == Chess.NONE){
            chessSet[to].setColor(fromColor);
            chessSet[from].setColor(Chess.NONE);
            return true;
        }
        return false;
    }

    public int moveChess(int from, int to){
        if (to < 0 || to >= chessSet.length) return MainWindowJFrame.ERROR_BEYOND_BOARD;
        else if (from < 0 || from >= chessSet.length) return MainWindowJFrame.ERROR_BEYOND_BOARD;
        else if (0 == MainWindowJFrame.chessAdjacentMap[from][to]) return MainWindowJFrame.ERROR_TOO_FAR;
        else if (chessSet[to].getColor() != Chess.NONE) return MainWindowJFrame.ERROR_OVERLAP;
        return MainWindowJFrame.STATE_UNKOWN;
    }

    public int placeChess(int to, int color){
        if (to < 0 || to >= chessSet.length) return MainWindowJFrame.ERROR_BEYOND_BOARD;
        if (chessSet[to].getColor() != Chess.NONE) return MainWindowJFrame.ERROR_OVERLAP;
        chessSet[to].setColor(color);
        selectChess(to);
        return MainWindowJFrame.STATE_OK;
    }

    public int removeChess(int index){
        return placeChess(index,Chess.NONE);
    }

    public void hoverChess(int index){
        if (index < 0 || index > chessSet.length) return;
        if (chessSet[index].getColor() == Chess.NONE)
            chessSet[index].setColor(Chess.BLACK_SHADOW);
        else if (chessSet[index].getColor() == Chess.BLACK_SHADOW)
            chessSet[index].setColor(Chess.NONE);
    }

    public void selectChess(int index){
        int color = chessSet[index].getColor();
        if (color == Chess.BLACK_SELECTED) {
            chessSet[index].setColor(Chess.BLACK);
            return;
        } else if (color == Chess.WHITE_SELECTED) {
            chessSet[index].setColor(Chess.WHITE);
            return;
        }
        for (Chess chess : chessSet) {
            int acolor = chess.getColor();
            if (acolor == Chess.BLACK_SELECTED)
                chess.setColor(Chess.BLACK);
            else if (acolor == Chess.WHITE_SELECTED)
                chess.setColor(Chess.WHITE);
        }
        if (color == Chess.BLACK) chessSet[index].setColor(Chess.BLACK_SELECTED);
        else if (color == Chess.WHITE) chessSet[index].setColor(Chess.WHITE_SELECTED);
    }


}
