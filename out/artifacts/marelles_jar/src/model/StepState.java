package model;

import view.Chess;
import view.ConstantDataSet;

import java.sql.*;
import java.util.Vector;

public class StepState{
    public static final int PHASE1 = 1; // 双方均可放置棋子
    public static final int PHASE2 = 2; // 双方均只可移动棋子
    public static final int PHASE3 = 3; // 对方可以跳跃而自己不可
    public static final int PHASE4 = 4; // 自己可以跳跃而对方不可
    public static final int PHASE5 = 5; // 双方均可跳跃
    public static final int PHASE6 = 6; // 游戏结束
    private final String TAG = "StepStateTableManager: ";
    private int gameID;
    private int phase;
    private Vector<RecordData> recordDataVector;
    public StepState(){
        phase = PHASE1;
        recordDataVector = getNewRecordDataVector();
    }
    public StepState(int gameID){
        phase = PHASE1;
        this.gameID = gameID;
        recordDataVector = getNewRecordDataVector();
    }

    public int getTurnNum(){
        if (recordDataVector!=null)return recordDataVector.size();
        else return 0;
    }

    public void undo(){
        if (recordDataVector!=null){
            if (recordDataVector.size()>0){
                recordDataVector.remove(recordDataVector.size()-1);
            }
            if (recordDataVector.size()>0 &&
                    recordDataVector.get(recordDataVector.size()-1).player.equals("b"))
                recordDataVector.remove(recordDataVector.size()-1);
        }
    }

    public int getGameID() {
        return gameID;
    }

    public Vector<RecordData> getRecordDataVector() {
        return recordDataVector;
    }

    public void bindGameID(int gameID){
        this.gameID = gameID;
    }

    public int localClick(int x, int y){
        int index = 0;
        for (int[] ints : ConstantDataSet.chessPostionMap) {
            if (ints[0] == x && ints[1] == y) break;
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

    public void addStep(int from, int to, int affect, int color){
        int turn = recordDataVector.size();
        String c = "?";
        if (color == Chess.BLACK) c = "b";
        if (color == Chess.WHITE) c = "w";

        recordDataVector.add(new RecordData(gameID,turn+1,c,from,to,affect));
    }

    public void popStep(){
        int size = recordDataVector.size();
        if (size > 0)
            recordDataVector.remove(size - 1);
    }
    //TODO:TEST
    public void showStep(){
        for (RecordData data : recordDataVector){
            System.out.println(data.toString());
        }
    }
    public void save(){
        Connection connection = DatabaseManager.getConnection();
        try(Statement statement = connection.createStatement()){
            String deleteOld = "delete from step_state where game_id=" + gameID;
            statement.executeUpdate(deleteOld);
        }catch (SQLException e){
            e.printStackTrace();
        }
        try{
            String addNew = "insert into step_state values (?,?,?,?,?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(addNew);
            for (RecordData recordData: recordDataVector){
                preparedStatement.setInt(1,recordData.game_id);
                preparedStatement.setInt(2,recordData.turn);
                preparedStatement.setString(3,recordData.player);
                preparedStatement.setInt(4,recordData.pos_before);
                preparedStatement.setInt(5,recordData.pos_after);
                preparedStatement.setInt(6,recordData.pos_affect);
                preparedStatement.executeUpdate();
            }
            preparedStatement.close();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }
    private Vector<RecordData> getNewRecordDataVector(){
        if (recordDataVector == null)recordDataVector = new Vector<>();
        if (gameID == 0) gameID = GameState.getNewGameID();
        else {
            Connection connection = DatabaseManager.getConnection();
            String sql = "select * from step_state where game_id="+gameID;
            try(Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(sql)){
                while (resultSet.next()){
                    RecordData recordData = new RecordData(
                            resultSet.getInt(1), // game_id
                            resultSet.getInt(2), // turn
                            resultSet.getString(3), // player
                            resultSet.getInt(4), // pos_before
                            resultSet.getInt(5), // pos_after
                            resultSet.getInt(6)); // pos_affect
                    recordDataVector.add(recordData);
                }
            } catch (SQLException e){
                e.printStackTrace();
            }
        }
        return recordDataVector;
    }


}
