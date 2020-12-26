package view;

import database.DatabaseManager;

import javax.swing.*;
import java.awt.*;

public class MainWindowJFrame extends JFrame {
    protected Toolkit toolkit;
    protected int gameWidth;
    protected int gameHeight;
    protected int mainButtonWidth;
    protected int mainButtonHeight;
    protected int mainButtonPadding;
    protected  Font mainButtonFont;
    protected  Font mainMessageFont;
    protected JButton[] mainButtonSet;
    protected PlayBoard playBoard;
    protected DatabaseManager databaseManager;
    protected static final String backgourd = "src/res/drawable/background.png";
    protected static final String board = "src/res/drawable/board.png";
    protected static final String bchess = "src/res/drawable/blackchess.png";
    protected static final String wchess = "src/res/drawable/whitechess.png";
    protected static final String undo = "悔 棋";
    protected static final String endGame = "结 束 游 戏";
    protected static final String[] mainButtonHint = {"单 人 游 戏","双 人 对 战","游 戏 帮 助","退 出 游 戏"};
    protected static final String[] singleGameButtonHint = {"新 的 游 戏","复 盘 战 局","玩 家 信 息","返 回 主 页"};
    protected static final String[] multiGameButtonHint = {"创 建 房 间","加 入 房 间","玩 家 信 息","返 回 主 页"};
    protected static final String gameRule = "<html>游戏共分为三个阶段：<br>" +
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
}
