package view;

import controller.PlayBoardController;
import model.RecordData;
import model.StepState;

import javax.swing.*;
import java.awt.*;
import java.util.Vector;

public class PlayBoard extends JPanel {
    public int block;
    private final Image image;
    private final int edge;
    private Chess[] chessSet;
    private final  PlayBoardController playBoardController;
    public PlayBoard(int x, int y,int edge){
        this.setLayout(null);
        this.setBounds(x,y,edge,edge);
        this.edge = edge;
        this.block = edge/8;
        this.playBoardController = new PlayBoardController(this);
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

    public void load(){
        this.setVisible(true);
        Chess.loadChessIcon();
        this.chessSet = new Chess[24];
        for (int i = 0; i < chessSet.length; i++){
            this.chessSet[i] = new Chess(Chess.NONE);
        }
        Vector<RecordData> vector = playBoardController.getRecords();
        if (vector.size() >= 2*MainWindowJFrame.CRITICAL_PHASE1)
            playBoardController.updatePhase(StepState.PHASE2);
        else playBoardController.updatePhase(StepState.PHASE1);
        for (RecordData recordData: vector){
            if (recordData.pos_before == recordData.pos_after
            && recordData.pos_after == recordData.pos_affect){ // phase1
                if (recordData.player.equals("b")) chessSet[recordData.pos_after].setColor(Chess.BLACK);
                else if (recordData.player.equals("w")) chessSet[recordData.pos_after].setColor(Chess.WHITE);
            } else if (recordData.pos_before == recordData.pos_after){
                chessSet[recordData.pos_before].setColor(Chess.NONE);
                chessSet[recordData.pos_affect].setColor(Chess.NONE);
                if (recordData.player.equals("b")) chessSet[recordData.pos_after].setColor(Chess.BLACK);
                else if (recordData.player.equals("w")) chessSet[recordData.pos_after].setColor(Chess.WHITE);
            } else if (recordData.pos_after == recordData.pos_affect) {
                chessSet[recordData.pos_before].setColor(Chess.NONE);
                if (recordData.player.equals("b")) chessSet[recordData.pos_after].setColor(Chess.BLACK);
                else if (recordData.player.equals("w")) chessSet[recordData.pos_after].setColor(Chess.WHITE);
            } else {
                chessSet[recordData.pos_before].setColor(Chess.NONE);
                chessSet[recordData.pos_affect].setColor(Chess.NONE);
                if (recordData.player.equals("b")) chessSet[recordData.pos_after].setColor(Chess.BLACK);
                else if (recordData.player.equals("w")) chessSet[recordData.pos_after].setColor(Chess.WHITE);
            }
        }
    }

    public void undo(){

    }
    public void saveGame(){
        playBoardController.save();
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
        else if (chessSet[from].getColor() == Chess.BLACK_SELECTED){
            placeChess(to,Chess.BLACK);
            chessSet[from].setColor(Chess.NONE);
            return MainWindowJFrame.STATE_OK;
        }
        return MainWindowJFrame.STATE_UNKOWN;
    }

    public void oppoMoveChess(int from, int to){
        chessSet[from].setColor(Chess.NONE);
        chessSet[to].setColor(Chess.WHITE);
        selectChess(to);
    }

    public int placeChess(int to, int color){
        if (to < 0 || to >= chessSet.length) return MainWindowJFrame.ERROR_BEYOND_BOARD;
        if (chessSet[to].getColor() != Chess.NONE) return MainWindowJFrame.ERROR_OVERLAP;
        chessSet[to].setColor(color);
        selectChess(to);
        return MainWindowJFrame.STATE_OK;
    }

    public int removeChess(int index){
        if (index < 0 || index > chessSet.length) return MainWindowJFrame.ERROR_BEYOND_BOARD;
        if (chessSet[index].getColor() == Chess.NONE) return MainWindowJFrame.ERROR_EMPTY_CHESS;
        if (chessSet[index].getColor() == Chess.BLACK) return MainWindowJFrame.ERROR_SELF_CHESS;
        chessSet[index].setColor(Chess.NONE);
        return MainWindowJFrame.STATE_OK;
    }

    public void oppoRemoveChess(int index){
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
        if (color == Chess.BLACK) {
            chessSet[index].setColor(Chess.BLACK_SELECTED);
            StatusPanel.sendGameInfo("你选中了一枚棋子");
        }
        else if (color == Chess.WHITE) chessSet[index].setColor(Chess.WHITE_SELECTED);
    }

    public void updateState(){

    }

    public int checkChessColor(int index){
        if (index < 0 || index > chessSet.length) return MainWindowJFrame.ERROR_BEYOND_BOARD;
        return chessSet[index].getColor();
    }
}
