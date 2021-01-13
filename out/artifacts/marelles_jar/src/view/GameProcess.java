package view;

import javax.swing.*;
import java.awt.*;
import java.util.Vector;

public class GameProcess extends JPanel {
    private static JLabel[] jLabels = null;
    private static Vector<String> gameInfoVector;
    public GameProcess(int x, int y, int w, int h){
        this.setLayout(null);
        this.setBounds(x,y,w,h);
    }

    public void init(){
        this.setVisible(true);
        gameInfoVector = new Vector<>();
        if (jLabels != null){
          for (JLabel jLabel:jLabels){
              jLabel.setText("");
          }
        } else {
            jLabels = new JLabel[ConstantDataSet.GAME_PROCESS_NUMBER];
            Rectangle rectangle = this.getBounds();
            int width = rectangle.width;
            int height = rectangle.height / ConstantDataSet.GAME_PROCESS_NUMBER;
            for (int i = 0; i < ConstantDataSet.GAME_PROCESS_NUMBER; i++) {
                jLabels[i] = new JLabel();
                jLabels[i].setBounds(0, i * height, width, height);
                this.add(jLabels[i]);
            }
        }
    }


    public static void sendGameInfo(int[] index, String info){
        gameInfoTrans(index,info);
    }

    public static void sendGameInfo(String info){
        updateGameInfo(info);
    }

    private static void gameInfoTrans(int[] index, String info){
        String newInfo = info;
        for (int i = 0; i < index.length; i++){
            String positionCoordinate =
                    ConstantDataSet.chessCoordinate[0][ConstantDataSet.chessPostionMap[index[i]][1]] +
                    ConstantDataSet.chessCoordinate[1][ConstantDataSet.chessPostionMap[index[i]][0]];
            newInfo = newInfo.replace("<"+(i+1)+">",positionCoordinate);
        }
        updateGameInfo(newInfo);
    }

    private static void updateGameInfo(String info){
        gameInfoVector.add(info);
        if (gameInfoVector.size()> ConstantDataSet.GAME_PROCESS_NUMBER)
            gameInfoVector.remove(0);
        for (int i = 0; i < Math.min(gameInfoVector.size(),
                ConstantDataSet.GAME_PROCESS_NUMBER); i++){
            String prompt = gameInfoVector.get(i);
            jLabels[i].setText(prompt);
        }
    }
}
