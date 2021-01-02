package view;

public class ConstantDataSet {
    public static final int ERROR_BEYOND_BOARD = 0;
    public static final int ERROR_OVERLAP = 1;
    public static final int ERROR_TOO_FAR = 2;
    public static final int ERROR_EMPTY_CHESS = 3;
    public static final int ERROR_SELF_CHESS = 4;
    public static final int STATE_MOVE_OK = 200;
    public static final int STATE_JUMP_OK = 201;
    public static final int STATE_REMOVE_OK = 202;
    public static final int STATE_PLACE_OK = 203;
    public static final int STATE_UNKOWN = 404;
    public static final int CRITICAL_PHASE1 = 9; // 九子棋双方各放9子进入第二阶段
    public static final int GAME_PROCESS_NUMBER = 8; // 侧边栏保留8条游戏通知
    public static final String backgourd = "src/res/drawable/background.png";
    public static final String board = "src/res/drawable/board.png";
    public static final String bchess = "src/res/drawable/blackchess.png";
    public static final String bchessSelected = "src/res/drawable/blackchess_selected.png";
    public static final String bchessShadow = "src/res/drawable/blackchess_shadow.png";
    public static final String wchess = "src/res/drawable/whitechess.png";
    public static final String wchessSelected = "src/res/drawable/whitechess_selected.png";
    public static final String wchessShadow = "src/res/drawable/whitechess_shadow.png";
    public static final String undo = "悔 棋";
    public static final String endGame = "结 束 游 戏";
    public static final String defaultOppoName = "Computer";
    public static final String[] gameState = {"unfinished","bwin","wwin","draw"};
    public static final String[] mainButtonHint = {"单 人 游 戏","双 人 对 战","游 戏 帮 助","退 出 游 戏"};
    public static final String[] singleGameButtonHint = {"新 的 游 戏","继 续 游 戏","玩 家 信 息","返 回 主 页"};
    public static final String[] multiGameButtonHint = {"创 建 房 间","加 入 房 间","玩 家 信 息","返 回 主 页"};
    public static final String gameRule = "<html>游戏共分为三个阶段：<br>" +
            "<br>" +
            "1.<b>棋子放置阶段</b>：棋盘起初为空。两名玩家决定谁先开始，轮流在空闲的格点处放置棋子。在放置过程中，<br>" +
            "如果有一方形成了连续的水平或竖直的三个棋子（称为“三连”），则可以从对方已放置的棋子中移除一个<br>" +
            "到游戏外。移除时，必须先移除没有形成一行或一列的棋子。双方轮流放置直到都放置过了九枚棋子。<br><br>" +
            "2.<b>棋子移动阶段</b>：玩家交替进行棋子移动。棋子移动时只能将自己一方的一枚棋子移动到相邻的空闲的格点<br>" +
            "处。如果移动棋子使得自己一方形成了一个“三连”，则该玩家需要立即选择对方玩家任意一枚棋子移出游<br>" +
            "戏。当某方玩家只剩三枚棋子时，进入第三阶段。<br><br>" +
            "3.<b>棋子跳跃阶段</b>：当某方玩家只剩三枚棋子时，他可以不受“每次移动只能选择相邻空闲位置”的限制，而<br>" +
            "可以将自己的任意一枚棋子移动到任意空闲格点。当某方玩家只剩两枚棋子时，该玩家失败。<br>" +
            "<br></html>";
    public static final int [][] chessPostionMap = {{1,1},{4,1},{7,1},{2,2},{4,2},{6,2},{3,3},{4,3},{5,3},{1,4},
            {2,4},{3,4},{5,4},{6,4},{7,4},{3,5},{4,5},{5,5},{2,6},{4,6},{6,6},{1,7},{4,7},{7,7}};
    public static final String[][] chessCoordinate = {{"X","A","B","C","D","E","F","G"},{"X","1","2","3","4","5","6","7"}};
    public static final int [][] chessAdjacentMap = {
        //   0 1 2 3 4 5 6 7 8 9 1 1 1 1 1 1 1 1 1 1 2 2 2 2
        //                       0 1 2 3 4 5 6 7 8 9 0 1 2 3
            {0,1,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0}, //0 -> 1,9
            {1,0,1,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}, //1 -> 0,2,4
            {0,1,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0}, //2 -> 1,14
            {0,0,0,0,1,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0}, //3 -> 4,10
            {0,1,0,1,0,1,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}, //4 -> 1,3,5,7
            {0,0,0,0,1,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0}, //5 -> 4,13

        //   0 1 2 3 4 5 6 7 8 9 1 1 1 1 1 1 1 1 1 1 2 2 2 2
        //                       0 1 2 3 4 5 6 7 8 9 0 1 2 3
            {0,0,0,0,0,0,0,1,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0}, //6 -> 7,11
            {0,0,0,0,1,0,1,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}, //7 -> 4,6,8
            {0,0,0,0,0,0,0,1,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0}, //8 -> 7,12
            {1,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,1,0,0}, //9 -> 0,10,21
            {0,0,0,1,0,0,0,0,0,1,0,1,0,0,0,0,0,0,1,0,0,0,0,0}, //10 -> 3,9,11,18

        //   0 1 2 3 4 5 6 7 8 9 1 1 1 1 1 1 1 1 1 1 2 2 2 2
        //                       0 1 2 3 4 5 6 7 8 9 0 1 2 3
            {0,0,0,0,0,0,1,0,0,0,1,0,0,0,0,1,0,0,0,0,0,0,0,0}, //11 -> 6,10,15
            {0,0,0,0,0,0,0,0,1,0,0,0,0,1,0,0,0,1,0,0,0,0,0,0}, //12 -> 8,13,17
            {0,0,0,0,0,1,0,0,0,0,0,0,1,0,1,0,0,0,0,0,1,0,0,0}, //13 -> 5,12,14,20
            {0,0,1,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,1}, //14 -> 2,13,23
            {0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,1,0,0,0,0,0,0,0}, //15 -> 11,16

        //   0 1 2 3 4 5 6 7 8 9 1 1 1 1 1 1 1 1 1 1 2 2 2 2
        //                       0 1 2 3 4 5 6 7 8 9 0 1 2 3
            {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,1,0,1,0,0,0,0}, //16 -> 15,17,19
            {0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,1,0,0,0,0,0,0,0}, //17 -> 12,16
            {0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,1,0,0,0,0}, //18 -> 10,19
            {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,1,0,1,0,1,0}, //19 -> 16,18,20,22
            {0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,1,0,0,0,0}, //20 -> 13,19

        //   0 1 2 3 4 5 6 7 8 9 1 1 1 1 1 1 1 1 1 1 2 2 2 2
        //                       0 1 2 3 4 5 6 7 8 9 0 1 2 3
            {0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,1,0}, //21 -> 9,22
            {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,1,0,1}, //22 -> 19,21,23
            {0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,1,0}, //23 -> 14,22
    }; //做这个表也太累了
}
