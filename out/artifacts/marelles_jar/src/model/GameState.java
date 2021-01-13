package model;

import view.Chess;
import view.ConstantDataSet;
import view.StatusPanel;

import java.sql.*;
import java.util.Vector;

public class GameState{
    // 返回game_id最小的未完成的游戏
    // 由于只存在一场未完成的游戏 所以返回的是最近一场未完成的游戏
    public static int getLastUnfinishedGameID() {
        try{
            Statement statement = DatabaseManager.getConnection().createStatement();
            String sql = "select id,state from game_state";
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()){
                int id = resultSet.getInt(1);
                String state = resultSet.getString(2);
                if (state.equals(ConstantDataSet.gameState[0])) return id;
            }
            statement.close();
        } catch (Exception e){
            e.printStackTrace();
        }
        return 1;
    }
    public static String[] getLastUnfinishedPlayerIDs(){
        Connection connection = DatabaseManager.getConnection();
        try(Statement statement = connection.createStatement()){
            String sql = "select bname,wname,state from game_state";
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()){
                String bname = resultSet.getString(1);
                String wname = resultSet.getString(2);
                String state = resultSet.getString(3);
                if (state.equals(ConstantDataSet.gameState[0])) return new String[]{bname,wname};
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    public static int getNewGameID(){
        Connection connection = DatabaseManager.getConnection();
        int id = 0;
        try{
            Statement statement = connection.createStatement();
            String sql = "select id from game_state";
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) id = resultSet.getInt(1);
            statement.close();
        } catch (Exception e){
            e.printStackTrace();
        }
        return id+1;
    }

    public static void startNewGame(){
        try {
            Connection connection = DatabaseManager.getConnection();
            Statement statement = connection.createStatement();
            int id = getNewGameID();
            System.out.println("GameState: start new Game, assigned id is "+id);
            String insertNewGame = "insert into game_state values("+id+",'--','--',0,'unfinished')";
            statement.executeUpdate(insertNewGame);
            statement.close();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public static void save(StepState stepState, int resultCode){
        Vector<RecordData> recordDataVector = stepState.getRecordDataVector();
        try{
            Connection connection = DatabaseManager.getConnection();
            PreparedStatement preparedStatement;
            System.out.println("GameState: game state has been refreshed. id is "+stepState.getGameID());
            String updateSQL = "update game_state set bname=?,wname=?,turns=?,state=? where id=?";
            preparedStatement = connection.prepareStatement(updateSQL);
            preparedStatement.setString(1,StatusPanel.getPlayerName(Chess.BLACK));
            preparedStatement.setString(2,StatusPanel.getPlayerName(Chess.WHITE));
            preparedStatement.setInt(3,recordDataVector.size());
            preparedStatement.setString(4, ConstantDataSet.gameState[resultCode]);
            preparedStatement.setInt(5,stepState.getGameID());
            preparedStatement.executeUpdate();
            preparedStatement.close();
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
