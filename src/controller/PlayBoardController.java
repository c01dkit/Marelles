package controller;

import model.RecordData;
import model.StepState;
import view.Chess;
import view.MainWindowJFrame;
import view.PlayBoard;
import view.StatusPanel;

import java.awt.event.*;
import java.util.Random;
import java.util.Vector;

public class PlayBoardController implements MouseListener {
    private final PlayBoard playBoard;
    private final int edge;
    private final StepState stepState;
    private final int CLICK = 0;
    private final int ENTER = 1;
    private final int EXIT = 2;
    private int hasPlacedNum = 0;
    private int hasRemoveNum = 0;
    private int from = -1;
    private int to = -1;
    private boolean wait = false;
    private boolean remove = false;
    private final boolean[][] removeSet;
    private final int[] situation;
    public PlayBoardController(PlayBoard playBoard){
        this.playBoard = playBoard;
        this.edge = playBoard.block;
        stepState = new StepState();
        situation = new int[24];
        removeSet = new boolean[2][16];
    }

    public Vector<RecordData> getRecords(){
        return stepState.load();
    }

    public void save(){
        stepState.save();
    }

    public void updatePhase(int phase){
        stepState.setPhase(phase);
    }

    public void bindGameID(int gameID){
        stepState.bindGameID(gameID);
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


    // 对方移动棋子
    public void opposite(int from, int to, int affect){
        if(!wait) return;
        int randomPlace;
        if (from == -1 && to == -1 && affect == -1){ // 单人模式下模拟系统
            if (hasPlacedNum <= MainWindowJFrame.CRITICAL_PHASE1){ // 随机放置
                randomPlace = getRandomPlace(Chess.NONE);
                playBoard.placeChess(randomPlace,Chess.WHITE);
                situation[randomPlace] = Chess.WHITE;
            } else if (hasRemoveNum < 6) { // 玩家还没有移除6枚棋子，则系统随机移动一枚棋子
                int randomFrom;
                boolean found = false;
                for (int i = 0; i < 50 && !found; i++){
                    randomFrom = getRandomPlace(Chess.WHITE);
                    for (int j = 0; j < 24; j++){
                        if (MainWindowJFrame.chessAdjacentMap[randomFrom][j] == 1
                        && playBoard.checkChessColor(j) <= Chess.NONE ){
                            playBoard.oppoMoveChess(randomFrom,j);
                            situation[randomFrom] = Chess.NONE;
                            situation[j] = Chess.WHITE;
                            found = true;
                            break;
                        }
                    }
                }
                if (!found) {
                    StatusPanel.sendGameInfo("你赢了！", 0);
                    stepState.setPhase(StepState.PHASE4);
                }
            } else if (hasRemoveNum == 6) { // 玩家已经移除了6个白棋，则系统进行跳转

            } else if (hasRemoveNum == 7) { // 玩家获胜
                stepState.setPhase(StepState.PHASE4);
                StatusPanel.sendGameInfo("你赢了！",0);
            }
            if (checkThree(Chess.WHITE)) { //白棋形成三连，随机移除一个黑棋
               randomPlace = getRandomPlace(Chess.BLACK);
                playBoard.oppoRemoveChess(randomPlace);
                situation[randomPlace] = Chess.NONE;
            }
            wait = false;
        }
    }

    private int getRandomPlace(int color){
        Random random = new Random();
        int randomPlace;
        int targetColor;
        do{
            randomPlace = random.nextInt(24);
            targetColor = playBoard.checkChessColor(randomPlace);
        }while (targetColor != color);
        return randomPlace;
    }

    private void trigger(int index, int action){
        if (wait) return;
        int resultCode;
        if (remove){
            resultCode = playBoard.removeChess(index);
            if (resultCode == MainWindowJFrame.ERROR_EMPTY_CHESS){
                StatusPanel.sendGameInfo("该位置没有棋子！");
                return;
            } else if (resultCode == MainWindowJFrame.ERROR_SELF_CHESS){
                StatusPanel.sendGameInfo("不能移除自己的棋子！");
                return;
            } else if (resultCode == MainWindowJFrame.STATE_OK){
                StatusPanel.sendGameInfo("已移除对方一枚棋子");
                situation[index] = Chess.NONE;
                hasRemoveNum++;
                stepState.popStep();
                stepState.addStep(from,to,index,Chess.BLACK);
                updateState();
                return;
            }
        }
        int phase = stepState.getPhase();
        switch (action){
            case CLICK:{
                if (phase == StepState.PHASE1){
                    resultCode = playBoard.placeChess(index, Chess.BLACK);
                    if (resultCode == MainWindowJFrame.ERROR_OVERLAP){
                        playBoard.selectChess(index);
                    } else if (resultCode == MainWindowJFrame.STATE_OK){
                        hasPlacedNum++;
                        situation[index] = Chess.BLACK;
                        from = index;
                        to = index;
                        StatusPanel.sendGameInfo("你放置了一枚棋子");
                        stepState.addStep(index,index,index,Chess.BLACK);
//                        stepState.showStep();
                        updateState();
                    }
                } else if (phase == StepState.PHASE2){
                    resultCode = playBoard.checkChessColor(index);
                    if (resultCode == Chess.BLACK){
                        playBoard.selectChess(index);
                        from = index;
                    } else if (resultCode == Chess.BLACK_SELECTED) {
                        playBoard.selectChess(index);
                        from = -1;
                    } else if (resultCode == Chess.NONE){
                        if (from != -1){
                            int ans = playBoard.moveChess(from,index);
                            switch (ans){
                                case MainWindowJFrame.ERROR_OVERLAP:
                                    StatusPanel.sendGameInfo("该位置已有棋子！",0);
                                    break;
                                case MainWindowJFrame.ERROR_EMPTY_CHESS:
                                    StatusPanel.sendGameInfo("棋子已被移动！",0);
                                    break;
                                case MainWindowJFrame.ERROR_TOO_FAR:
                                    StatusPanel.sendGameInfo("只能相邻移动！",0);
                                    break;
                                case MainWindowJFrame.STATE_UNKOWN:
                                    StatusPanel.sendGameInfo("未知错误",0);
                                    break;
                                case MainWindowJFrame.STATE_OK:
                                    situation[from] = Chess.NONE;
                                    situation[index] = Chess.BLACK;
                                    to = index;
                                    hasPlacedNum++;
                                    stepState.addStep(from,index,index,Chess.BLACK);
                                    StatusPanel.sendGameInfo("你移动了一枚棋子");
                                    updateState();
                            }
                        }
                    }
                } else if (phase == StepState.PHASE3){

                }
                break;
            }
            case ENTER:{
                StatusPanel.sendGameInfo("进入战场");
                break;
            }
            case EXIT:{
                StatusPanel.sendGameInfo("离开战场");
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
    // 扫描全局，观察是否需要更新对决状态
    private void updateState(){
        int currentB = 0;
        int currentW = 0;
        for (int i : situation){
            if (i == Chess.BLACK) currentB++;
            if (i == Chess.WHITE) currentW++;
        }
        if (stepState.getPhase() == StepState.PHASE4){
            StatusPanel.sendGameInfo("你赢了！",0);
            return;
        }
        if (stepState.getPhase() == StepState.PHASE3) {
            if (currentW == 2){
                StatusPanel.sendGameInfo("你赢了！",0);
                stepState.setPhase(StepState.PHASE4);
                return;
            }
            if (currentB == 2){
                StatusPanel.sendGameInfo("你输了。",0);
                stepState.setPhase(StepState.PHASE4);
                return;
            }
        }
        if (stepState.getPhase() == StepState.PHASE2 && (currentW == 3 || currentB == 3)) {
            stepState.setPhase(StepState.PHASE3);
            StatusPanel.sendGameInfo("游戏进入第三阶段",3);
        }
        if (stepState.getPhase() == 1 && hasPlacedNum == MainWindowJFrame.CRITICAL_PHASE1){
            stepState.setPhase(StepState.PHASE2);
            StatusPanel.sendGameInfo("游戏进入第二阶段",2);
        }
        if (checkThree(Chess.BLACK) && !remove){
            StatusPanel.sendGameInfo("你达成了一个三连！可以移除对方一枚棋子");
            remove = true;
            wait = false;
        } else {
            from = -1;
            remove = false;
            wait = true;
        }
        if (wait) opposite(-1,-1,-1);
        playBoard.updateState();
    }



    // 查看是否形成三连
    private boolean checkThree(int color){
        boolean ans = false;
        int rcolor = color - 1;
        if (situation[0] == color && situation[1] == color && situation[2] == color){
            if (!removeSet[rcolor][0]) {
                removeSet[rcolor][0] = true;
                ans = true;
            }
        } else removeSet[rcolor][0] = false;

        if (situation[3] == color && situation[4] == color && situation[5] == color) {
            if (!removeSet[rcolor][1]){
                removeSet[rcolor][1] = true;
                ans = true;
            }
        } else removeSet[rcolor][1] = false;

        if (situation[6] == color && situation[7] == color && situation[8] == color) {
            if (!removeSet[rcolor][2]){
                removeSet[rcolor][2] = true;
                ans = true;
            }
        } else removeSet[rcolor][2] = false;

        if (situation[9] == color && situation[10] == color && situation[11] == color) {
            if (!removeSet[rcolor][3]){
                removeSet[rcolor][3] = true;
                ans = true;
            }
        } else removeSet[rcolor][3] = false;

        if (situation[12] == color && situation[13] == color && situation[14] == color) {
            if (!removeSet[rcolor][4]){
                removeSet[rcolor][4] = true;
                ans = true;
            }
        } else removeSet[rcolor][4] = false;

        if (situation[15] == color && situation[16] == color && situation[17] == color) {
            if (!removeSet[rcolor][5]){
                removeSet[rcolor][5] = true;
                ans = true;
            }
        } else removeSet[rcolor][5] = false;

        if (situation[18] == color && situation[19] == color && situation[20] == color) {
            if (!removeSet[rcolor][6]){
                removeSet[rcolor][6] = true;
                ans = true;
            }
        } else removeSet[rcolor][6] = false;

        if (situation[21] == color && situation[22] == color && situation[23] == color) {
            if (!removeSet[rcolor][7]){
                removeSet[rcolor][7] = true;
                ans = true;
            }
        } else removeSet[rcolor][7] = false;

        if (situation[0] == color && situation[9] == color && situation[21] == color) {
            if (!removeSet[rcolor][8]){
                removeSet[rcolor][8] = true;
                ans = true;
            }
        } else removeSet[rcolor][8] = false;

        if (situation[3] == color && situation[10] == color && situation[18] == color) {
            if (!removeSet[rcolor][9]){
                removeSet[rcolor][9] = true;
                ans = true;
            }
        } else removeSet[rcolor][9] = false;

        if (situation[6] == color && situation[11] == color && situation[15] == color) {
            if (!removeSet[rcolor][10]){
                removeSet[rcolor][10] = true;
                ans = true;
            }
        } else removeSet[rcolor][10] = false;

        if (situation[1] == color && situation[4] == color && situation[7] == color) {
            if (!removeSet[rcolor][11]){
                removeSet[rcolor][11] = true;
                ans = true;
            }
        } else removeSet[rcolor][11] = false;

        if (situation[16] == color && situation[19] == color && situation[22] == color) {
            if (!removeSet[rcolor][12]){
                removeSet[rcolor][12] = true;
                ans = true;
            }
        } else removeSet[rcolor][12] = false;

        if (situation[8] == color && situation[12] == color && situation[17] == color) {
            if (!removeSet[rcolor][13]){
                removeSet[rcolor][13] = true;
                ans = true;
            }
        } else removeSet[rcolor][13] = false;

        if (situation[5] == color && situation[13] == color && situation[20] == color) {
            if (!removeSet[rcolor][14]){
                removeSet[rcolor][14] = true;
                ans = true;
            }
        } else removeSet[rcolor][14] = false;

        if (situation[2] == color && situation[14] == color && situation[23] == color){
            if (!removeSet[rcolor][15]){
                removeSet[rcolor][15] = true;
                ans = true;
            }
        } else removeSet[rcolor][15] = false;
        return ans;
    }

}