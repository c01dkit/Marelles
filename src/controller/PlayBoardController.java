package controller;

import model.StepState;
import view.Chess;
import view.MainWindowJFrame;
import view.PlayBoard;

import java.awt.event.*;

public class PlayBoardController implements MouseListener {
    private final PlayBoard playBoard;
    private final int edge;
    private StepState stepState;
    private final int CLICK = 0;
    private final int ENTER = 1;
    private final int EXIT = 2;

    public PlayBoardController(PlayBoard playBoard){
        this.playBoard = playBoard;
        this.edge = playBoard.block;
        stepState = new StepState();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        register(e,CLICK);
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {
        register(e,ENTER);
    }

    @Override
    public void mouseExited(MouseEvent e) {
        register(e,EXIT);
    }

    private void trigger(int index, int action){
        int resultCode;
        int phase = stepState.getPhase();
        switch (action){
            case CLICK:{
                if (phase == stepState.PHASE1){
                    resultCode = playBoard.placeChess(index, Chess.BLACK);
                    if (resultCode == MainWindowJFrame.ERROR_OVERLAP){
                        playBoard.selectChess(index);
                    }
                }
                break;
            }
            case ENTER:{
                if (phase == stepState.PHASE1){
                    System.out.println(index);
                    playBoard.hoverChess(index);
                }
                break;
            }
            case EXIT:{
                break;
            }
        }
    }
    private void register(MouseEvent e, int eventCode){
        int x = e.getX();
        int y = e.getY();
        boolean found = false;
        for (int i = 1 ; i <= 7 && !found; i++){
            if (Math.abs(i*edge-x) > edge) continue;
            for (int j = 1; j <= 7 && !found; j++){
                if (Math.abs(j*edge-y) > edge) continue;
                if (((x-edge*i)*(x-edge*i)+(y-edge*j)*(y-edge*j)) < (edge*edge/4)){
                    found = true;
                    int validation = stepState.localClick(i,j);
                    if (validation < MainWindowJFrame.chessPostionMap.length)
                        trigger(validation,eventCode);
                }
            }
        }
    }
}
