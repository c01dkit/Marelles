package model;

import view.Chess;
import view.ConstantDataSet;

import java.sql.SQLException;
import java.util.Vector;

public class StepState extends TableManager{
    public static final int PHASE1 = 1;
    public static final int PHASE2 = 2;
    public static final int PHASE3 = 3;
    public static final int PHASE4 = 4;
    private final String TAG = "StepStateTableManager: ";
    private final String TABLE_NAME = "step_state";
    private int gameID;
    private int phase;
    private Vector<RecordData> recordDataVector;
    public StepState(){
        connect();
        phase = PHASE1;
        if (connection==null) System.out.println(TAG+"connect failed");
        recordDataVector = getNewRecordDataVector();
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
        if (size > 0) recordDataVector.remove(size-1);
    }
    //TODO:TEST
    public void showStep(){
        for (RecordData data : recordDataVector){
            System.out.println(data.toString());
        }
    }
    public void save(){
        try{
            String deleteOld = "delete from " + TABLE_NAME + " where game_id=" + gameID;
            statement = connection.createStatement();
            statement.executeUpdate(deleteOld);
            statement.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
        try{
            String addNew = "insert into "+ TABLE_NAME + " values (?,?,?,?,?,?)";
            preparedStatement = connection.prepareStatement(addNew);
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
        if (gameID == 0) {
            recordDataVector = new Vector<>();
            gameID = GameState.getNewGameID();
        }
        else {
            String sql = "select * from "+TABLE_NAME+" where game_id="+gameID;
            try{
                statement = connection.createStatement();
                resultSet = statement.executeQuery(sql);
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
                resultSet.close();
                statement.close();
            } catch (SQLException e){
                e.printStackTrace();
            }
        }
        return recordDataVector;
    }

    public Vector<RecordData> load(){
        int gameID = GameState.getLastUnfinishedGameID();
        bindGameID(gameID);
        return getNewRecordDataVector();
    }

}
