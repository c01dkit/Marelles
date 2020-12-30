package model;

import view.MainWindowJFrame;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class GameState extends TableManager{
    private final String TABLE_NAME = "game_state";

    public static int getLastUnfinishedGameID() {
        Connection connection= DatabaseManager.getConnection();
        try{
            Statement statement = connection.createStatement();
            String sql = "select id,state from game_state";
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()){
                int id = resultSet.getInt(1);
                String state = resultSet.getString(2);
                if (state.equals(MainWindowJFrame.gameState[3])) return id;
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return 0;
    }

    public static int getNewGameID(){
        Connection connection = DatabaseManager.getConnection();
        int id = 0;
        try{
            Statement statement = connection.createStatement();
            String sql = "select id from game_state";
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) id = resultSet.getInt(1);
        } catch (Exception e){
            e.printStackTrace();
        }
        return id+1;
    }
}
