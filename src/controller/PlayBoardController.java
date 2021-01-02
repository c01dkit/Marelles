package controller;

import model.GameState;
import model.RecordData;
import model.StepState;
import view.Chess;
import view.ConstantDataSet;
import view.PlayBoard;
import view.GameProcess;

import java.awt.event.*;
import java.util.Random;
import java.util.Vector;

public class PlayBoardController implements MouseListener {
    private final int edge;
    private final PlayBoard playBoard;
    private StepState stepState;
    private GameState gameState;
    private final int CLICK = 0;
    private final int ENTER = 1;
    private final int EXIT = 2;
    private int hasPlacedNum;
    private int hasRemoveNum;
    private int from;
    private int to;
    private boolean wait;
    private boolean remove;
    private boolean[][] removeSet;
    private int[] situation;
    private int gameResult;
    public PlayBoardController(PlayBoard playBoard){
        this.playBoard = playBoard;
        this.edge = playBoard.block;
    }

    public void init(){
        stepState = new StepState();
        gameState = new GameState();
        situation = new int[24];
        removeSet = new boolean[2][16];
        hasPlacedNum = 0;
        hasRemoveNum = 0;
        from = -1;
        to = -1;
        wait = false;
        remove = false;
        gameResult = 0;
    }

    public Vector<RecordData> getRecords(){
        return stepState.load();
    }

    public void save(){
        stepState.save();
        gameState.save(stepState, gameResult);
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
        int tempFrom = -1;
        int tempTo = -1;
        if (from == -1 && to == -1 && affect == -1){ // 单人模式下模拟系统
            if (hasPlacedNum <= ConstantDataSet.CRITICAL_PHASE1){ // 第一阶段，系统随机放置
                tempFrom = getRandomPlace(Chess.NONE);
                playBoard.placeChess(tempFrom,Chess.WHITE);
                situation[tempFrom] = Chess.WHITE;
                tempTo = tempFrom;
                stepState.addStep(tempFrom,tempFrom,tempFrom,Chess.WHITE);
                GameProcess.sendGameInfo(new int[]{tempTo},"对方在<1>放置了一枚棋子");
            } else if (hasRemoveNum < 6) { // 玩家还没有移除6枚棋子，则系统随机移动一枚棋子
                boolean found = false;
                for (int i = 0; i < 50 && !found; i++){
                    tempFrom = getRandomPlace(Chess.WHITE);
                    for (tempTo = 0; tempTo < situation.length; tempTo++){
                        if (ConstantDataSet.chessAdjacentMap[tempFrom][tempTo] == 1
                        && playBoard.checkChessColor(tempTo) == Chess.NONE ){
                            playBoard.oppoMoveChess(tempFrom,tempTo);
                            situation[tempFrom] = Chess.NONE;
                            situation[tempTo] = Chess.WHITE;
                            GameProcess.sendGameInfo(new int[]{tempFrom,tempTo},"对方将棋子从<1>移动到<2>");
                            found = true;
                            break;
                        }
                    }
                }
                if (!found) {
                    GameProcess.sendGameInfo("你赢了！");
                    gameResult = 1;
                    stepState.setPhase(StepState.PHASE4);
                    return;
                }
            } else if (hasRemoveNum == 6) { // 玩家已经移除了6个白棋，则系统进行跳转
                tempFrom = getRandomPlace(Chess.WHITE);
                tempTo = getRandomPlace(Chess.NONE);
                situation[tempFrom] = Chess.NONE;
                situation[tempTo] = Chess.WHITE;
                stepState.addStep(tempFrom,tempTo,tempTo,Chess.WHITE);
                playBoard.oppoMoveChess(tempFrom,tempTo);
                GameProcess.sendGameInfo(new int[]{tempFrom,tempTo},"对方将棋子从<1>跳转到<2>");
            } else if (hasRemoveNum == 7) { // 玩家获胜
                stepState.setPhase(StepState.PHASE4);
                GameProcess.sendGameInfo("你赢了！");
                gameResult = 1;
                return;
            }
            if (checkThree(Chess.WHITE)) { //白棋形成三连，随机移除一个黑棋
                int randomPlace;
                randomPlace = getRandomPlace(Chess.BLACK);
                if (tempFrom!=-1){
                    stepState.popStep();
                    stepState.addStep(tempFrom,tempTo,randomPlace,Chess.WHITE);
                }
                playBoard.oppoRemoveChess(randomPlace);
                situation[tempFrom] = Chess.NONE;
                situation[tempTo] = Chess.WHITE;
                situation[randomPlace] = Chess.NONE;
                GameProcess.sendGameInfo(new int[]{randomPlace},"对方移除了你的棋子<1>");
            }
            wait = false;
        }
        if (getPossibleNext(Chess.BLACK) == -1){ // -1表示无路可走
            GameProcess.sendGameInfo("你输了。");
            stepState.setPhase(StepState.PHASE4);
            gameResult = 2;
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
        if (remove && action == CLICK){
            resultCode = playBoard.removeChess(index);
            if (resultCode == ConstantDataSet.ERROR_EMPTY_CHESS){
                GameProcess.sendGameInfo("该位置没有棋子！");
                return;
            } else if (resultCode == ConstantDataSet.ERROR_SELF_CHESS){
                GameProcess.sendGameInfo("不能移除自己的棋子！");
                return;
            } else if (resultCode == ConstantDataSet.STATE_REMOVE_OK){
                GameProcess.sendGameInfo(new int[]{index},"已移除对方一枚棋子<1>");
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
                    handlePhase1(index);
                } else if (phase == StepState.PHASE2){
                    handlePhase2(index);
                } else if (phase == StepState.PHASE3){
                    handlePhase3(index);
                } else if (phase == StepState.PHASE4){
                    handlePhase4(index);
                }
                break;
            }
            case ENTER:{
                GameProcess.sendGameInfo("进入战场");
                break;
            }
            case EXIT:{
                GameProcess.sendGameInfo("离开战场");
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
                    if (validation < ConstantDataSet.chessPostionMap.length)
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
            GameProcess.sendGameInfo("你赢了！");
            gameResult = 1;
            return;
        }
        if (stepState.getPhase() == StepState.PHASE3) {
            if (currentW == 2){
                GameProcess.sendGameInfo("你赢了！");
                stepState.setPhase(StepState.PHASE4);
                gameResult = 1;
                return;
            }
            if (currentB == 2){
                GameProcess.sendGameInfo("你输了。");
                stepState.setPhase(StepState.PHASE4);
                gameResult = 2;
                return;
            }
        }
        if (stepState.getPhase() == StepState.PHASE2 && (currentW == 3 || currentB == 3)) {
            stepState.setPhase(StepState.PHASE3);
            GameProcess.sendGameInfo("游戏进入第三阶段");
        }
        if (stepState.getPhase() == 1 && hasPlacedNum == ConstantDataSet.CRITICAL_PHASE1){
            stepState.setPhase(StepState.PHASE2);
            GameProcess.sendGameInfo("游戏进入第二阶段");
        }
        if (checkThree(Chess.BLACK) && !remove){
            GameProcess.sendGameInfo("三连！请移除对方一枚棋子");
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

    // 获取可能移动到的某个位置
    private int getPossibleNext(int color){
        boolean found = false;
        int possiblePos = -1;
        for (int tempFrom = 0; tempFrom < situation.length && !found; tempFrom++){
            if (color == situation[tempFrom]){
                for (int tempTo = 0; tempTo < situation.length; tempTo++) {
                    if (ConstantDataSet.chessAdjacentMap[tempFrom][tempTo] == 1
                            && playBoard.checkChessColor(tempTo) == Chess.NONE) {
                        found = true;
                        possiblePos = tempTo;
                        break;
                    }
                }
            }
        }
        return possiblePos;
    }

    private void handlePhase1(int index){
        int resultCode = playBoard.placeChess(index, Chess.BLACK);
        if (resultCode == ConstantDataSet.ERROR_OVERLAP){
            playBoard.selectChess(index);
        } else if (resultCode == ConstantDataSet.STATE_PLACE_OK){
            hasPlacedNum++;
            situation[index] = Chess.BLACK;
            from = index;
            to = index;
            GameProcess.sendGameInfo(new int[]{index},"你在<1>放置了一枚棋子");
            stepState.addStep(index,index,index,Chess.BLACK);
            updateState();
        }
    }
    private void handlePhase2(int index){
        int resultCode = playBoard.checkChessColor(index);
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
                    case ConstantDataSet.ERROR_OVERLAP:
                        GameProcess.sendGameInfo("该位置已有棋子！");
                        break;
                    case ConstantDataSet.ERROR_EMPTY_CHESS:
                        GameProcess.sendGameInfo("棋子已被移动！");
                        break;
                    case ConstantDataSet.ERROR_TOO_FAR:
                        GameProcess.sendGameInfo("只能相邻移动！");
                        break;
                    case ConstantDataSet.STATE_UNKOWN:
                        GameProcess.sendGameInfo("未知错误");
                        break;
                    case ConstantDataSet.STATE_MOVE_OK:
                        situation[from] = Chess.NONE;
                        situation[index] = Chess.BLACK;
                        to = index;
                        hasPlacedNum++;
                        stepState.addStep(from,index,index,Chess.BLACK);
                        GameProcess.sendGameInfo(new int[]{from,index},"你将棋子从<1>移动到<2>");
                        updateState();
                }
            }
        }
    }
    private void handlePhase3(int index){
        int resultCode = playBoard.checkChessColor(index);
        if (resultCode == Chess.BLACK){
            playBoard.selectChess(index);
            from = index;
        } else if (resultCode == Chess.BLACK_SELECTED) {
            playBoard.selectChess(index);
            from = -1;
        } else if (resultCode == Chess.NONE){
            if (from != -1){
                int ans = playBoard.jumpChess(from,index);
                switch (ans){
                    case ConstantDataSet.ERROR_OVERLAP:
                        GameProcess.sendGameInfo("该位置已有棋子！");
                        break;
                    case ConstantDataSet.ERROR_EMPTY_CHESS:
                        GameProcess.sendGameInfo("棋子已被移动！");
                        break;
                    case ConstantDataSet.STATE_UNKOWN:
                        GameProcess.sendGameInfo("未知错误");
                        break;
                    case ConstantDataSet.ERROR_TOO_FAR:
                        GameProcess.sendGameInfo("只能相邻移动！");
                        break;
                    case ConstantDataSet.STATE_JUMP_OK:
                        situation[from] = Chess.NONE;
                        situation[index] = Chess.BLACK;
                        to = index;
                        stepState.addStep(from,index,index,Chess.BLACK);
                        GameProcess.sendGameInfo(new int[]{from,index},"你将棋子从<1>跳跃到<2>");
                        updateState();
                }
            }
        }
    }
    private void handlePhase4(int index){
        GameProcess.sendGameInfo("游戏已经结束");
    }
}