package view;

import javax.swing.*;

public class Chess extends JLabel {
    public static final int NONE = 0;
    public static final int BLACK = 1;
    public static final int BLACK_SHADOW = 2;
    public static final int BLACK_SELECTED = 3;
    public static final int WHITE = 4;
    public static final int WHITE_SHADOW = 5;
    public static final int WHITE_SELECTED = 6;
    private static ImageIcon blackChess;
    private static ImageIcon blackChessShadow;
    private static ImageIcon blackChessSelected;
    private static ImageIcon whiteChess;
    private static ImageIcon whiteChessShadow;
    private static ImageIcon whiteChessSelected;
    private int color;
    public Chess(int color){
        setColor(color);
    }
    public static void loadChessIcon(){
        blackChess = new ImageIcon(MainWindowJFrame.bchess);
        blackChessSelected = new ImageIcon(MainWindowJFrame.bchessSelected);
        blackChessShadow = new ImageIcon(MainWindowJFrame.bchessShadow);
        whiteChess = new ImageIcon(MainWindowJFrame.wchess);
        whiteChessSelected = new ImageIcon(MainWindowJFrame.wchessSelected);
        whiteChessShadow = new ImageIcon(MainWindowJFrame.wchessShadow);
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
        if (color == NONE){
            this.setVisible(false);
        } else if (color == BLACK){
            this.setVisible(true);
            this.setIcon(blackChess);
        } else if (color == WHITE){
            this.setVisible(true);
            this.setIcon(whiteChess);
        } else if (color == BLACK_SELECTED){
            this.setVisible(true);
            this.setIcon(blackChessSelected);
        } else if (color == WHITE_SELECTED){
            this.setVisible(true);
            this.setIcon(whiteChessSelected);
        }
    }
}
