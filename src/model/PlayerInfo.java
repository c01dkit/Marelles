package model;

import java.sql.SQLException;
import java.util.ArrayList;

public class PlayerInfo extends TableManager{
    private final String TAG = "PlayerInfo: ";
    private final String TABLE_NAME = "player_info";
    public PlayerInfo(){
        connect();
        if (connection == null) System.out.println(TAG+"connect failed");
    }
    public String addNewPlayer(String name) throws SQLException {
        name = name.trim();
        if (connection == null || name.isEmpty()) return null;
        String search = "select name from "+TABLE_NAME;
        preparedStatement = connection.prepareStatement(search);
        resultSet = preparedStatement.executeQuery();
        int id = 1;
        while (resultSet.next()){
            id++;
            if (resultSet.getString(1).equals(name))
                return name;
        }
        String insert = "insert into "+TABLE_NAME+" (id,name) values (?,?)";
        preparedStatement = connection.prepareStatement(insert);
        preparedStatement.setInt(1,id);
        preparedStatement.setString(2,name);
        preparedStatement.executeUpdate();
        return name;
    }
    public String[] getAllPlayers() throws SQLException {
        if (connection== null) {
            System.out.println(TAG+"getAllPlayers failed: not connected");
            return null;
        }

        statement = connection.createStatement();
        String selectSQL = "select * from "+TABLE_NAME;
        resultSet = statement.executeQuery(selectSQL);
        ArrayList<String> ans = new ArrayList<>();
        String[] res = null;
        ans.add("昵称          先手胜 先手平 先手负 后手胜 后手平 后手负 悔棋 逃跑");
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
                    resultSet.getObject(5)+"      "+resultSet.getObject(6)+"      " +
                    resultSet.getObject(7)+"      "+resultSet.getObject(8)+"     " +
                    resultSet.getObject(9)+"    "+resultSet.getObject(10);
            ans.add(s);
        }
        if (ans.size()>1){
            res = new String[ans.size()];
            for(int i = 0; i < ans.size(); i++) res[i] = ans.get(i);
        }
        return res;
    }
}
