package view;

import java.awt.event.*;

public class PlayBoardMouseListener implements MouseListener {
    private final PlayBoard playBoard;
    private final int edge;
    public PlayBoardMouseListener(PlayBoard playBoard){
        this.playBoard = playBoard;
        this.edge = playBoard.block;
        System.out.println(edge);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        boolean found = false;
        for (int i = 1 ; i <= 7 && !found; i++){
            if (Math.abs(i*edge-x) > edge) continue;
            for (int j = 1; j <= 7 && !found; j++){
                if (Math.abs(j*edge-y) > edge) continue;
                if (((x-edge*i)*(x-edge*i)+(y-edge*j)*(y-edge*j)) < (edge*edge/4)){
                    found = true;
                    playBoard.localClick(i,j);
                }
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
