package view;

import javax.swing.*;

public class Chess extends JLabel {
    public static final int NONE = 0;
    public static final int BLACK = 1;
    public static final int WHITE = 2;
    public static final int BLACK_SELECTED = 3;
    public static final int WHITE_SELECTED = 5;
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
        blackChess = new ImageIcon(ConstantDataSet.bchess);
        blackChessSelected = new ImageIcon(ConstantDataSet.bchessSelected);
        blackChessShadow = new ImageIcon(ConstantDataSet.bchessShadow);
        whiteChess = new ImageIcon(ConstantDataSet.wchess);
        whiteChessSelected = new ImageIcon(ConstantDataSet.wchessSelected);
        whiteChessShadow = new ImageIcon(ConstantDataSet.wchessShadow);
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
        if (color == Chess.NONE){
            this.setVisible(false);
        } else if (color == Chess.BLACK){
            this.setVisible(true);
            this.setIcon(blackChess);
        } else if (color == Chess.WHITE){
            this.setVisible(true);
            this.setIcon(whiteChess);
        } else if (color == Chess.BLACK_SELECTED){
            this.setVisible(true);
            this.setIcon(blackChessSelected);
        } else if (color == Chess.WHITE_SELECTED){
            this.setVisible(true);
            this.setIcon(whiteChessSelected);
        }
    }
}
