package model;

import view.MainWindowJFrame;

public class StepState extends TableManager{
    private final String TAG = "StepStateTableManager: ";
    private final String TABLE_NAME = "step_state";
    private int gameID;
    private int clickX;
    private int clickY;
    private int color;
    private int phase;
    public final int PHASE1 = 1;
    public final int PHASE2 = 2;
    public final int PHASE3 = 3;
   public StepState(){
        connect();
        phase = PHASE1;
        if (!connectState) System.out.println(TAG+"connect failed");
    }
    public int localClick(int x, int y){
        int index = 0;
        for (int[] ints : MainWindowJFrame.chessPostionMap) {
            if (ints[0] == x && ints[1] == y) {
                clickX = x;
                clickY = y;
                break;
            }
            index++;
        }
        return index;
    }

    public void setPhase(int phase) {
        this.phase = phase;
    }

    public int getPhase() {
        return phase;
    }
}
