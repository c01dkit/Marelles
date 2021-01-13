package model;

import java.sql.*;
import java.util.ArrayList;

public class PlayerInfo{
    public static final String TAG = "PlayerInfo: ";
    public static String addNewPlayer(String name) throws SQLException {
        name = name.trim();
        Connection connection = DatabaseManager.getConnection();
        PreparedStatement preparedStatement;
        ResultSet resultSet;
        if (connection == null || name.isEmpty()) return null;
        String search = "select name from player_info";
        preparedStatement = connection.prepareStatement(search);
        resultSet = preparedStatement.executeQuery();
        int id = 1;
        while (resultSet.next()){
            id++;
            if (resultSet.getString(1).equals(name))
                return name;
        }
        String insert = "insert into player_info (id,name) values (?,?)";
        preparedStatement = connection.prepareStatement(insert);
        preparedStatement.setInt(1,id);
        preparedStatement.setString(2,name);
        preparedStatement.executeUpdate();
        return name;
    }
    public static void updateGameResultInfo(int gameResult,String name){
        switch (gameResult){
            case 0: runAway(name);break;
            case 1: win(name);break;
            case 2: lose(name);break;
            case 3: draw(name);break;
            default: undo(name);break;
        }
    }
    private static void undo(String name){
        Connection connection = DatabaseManager.getConnection();
        try{
            String updateSQL = "update player_info set undo_times=undo_times+1 where name=?";
            PreparedStatement preparedStatement = connection.prepareStatement(updateSQL);
            preparedStatement.setString(1,name);
            preparedStatement.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
    private static void win(String name){
        Connection connection = DatabaseManager.getConnection();
        try{
            String updateSQL = "update player_info set win_times=win_times+1 where name=?";
            PreparedStatement preparedStatement = connection.prepareStatement(updateSQL);
            preparedStatement.setString(1,name);
            preparedStatement.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
    private static void lose(String name){
        Connection connection = DatabaseManager.getConnection();
        try{
            String updateSQL = "update player_info set lose_times=lose_times+1 where name=?";
            PreparedStatement preparedStatement = connection.prepareStatement(updateSQL);
            preparedStatement.setString(1,name);
            preparedStatement.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    private static void draw(String name){
        Connection connection = DatabaseManager.getConnection();
        try{
            String updateSQL = "update player_info set draw_times=draw_times+1 where name=?";
            PreparedStatement preparedStatement = connection.prepareStatement(updateSQL);
            preparedStatement.setString(1,name);
            preparedStatement.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    private static void runAway(String name){
        Connection connection = DatabaseManager.getConnection();
        try{
            String updateSQL = "update player_info set run_away_times=run_away_times+1 where name=?";
            PreparedStatement preparedStatement = connection.prepareStatement(updateSQL);
            preparedStatement.setString(1,name);
            preparedStatement.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public static String[] getAllPlayers() throws SQLException {
        Connection connection = DatabaseManager.getConnection();
        Statement statement = connection.createStatement();
        String selectSQL = "select * from player_info";
        ResultSet resultSet = statement.executeQuery(selectSQL);
        ArrayList<String> ans = new ArrayList<>();
        String[] res = null;
        ans.add("昵称          赢场数 平场数 输场数 悔棋次数 逃跑次数");
        while(resultSet.next()){
            StringBuilder name = new StringBuilder(resultSet.getString(2));
            int nameWidth = 17;
            if (name.length()>nameWidth) name = new StringBuilder(name.substring(0, 12) + "....");
            else {
                int padding = nameWidth-name.length();
                for(int i = 0; i < padding;i++){
                    name.append(" ");
                }
            }
            String s = ""+name+
                    resultSet.getObject(3)+"      "+resultSet.getObject(4)+"      " +
                    resultSet.getObject(5)+"      "+resultSet.getObject(6)+"        " +
                    resultSet.getObject(7);
            ans.add(s);
        }
        if (ans.size()>1){
            res = new String[ans.size()];
            for(int i = 0; i < ans.size(); i++) res[i] = ans.get(i);
        }
        return res;
    }
}
