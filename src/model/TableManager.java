package model;

import java.sql.*;

public class TableManager {
    protected Connection connection = null;
    protected Statement statement = null;
    protected ResultSet resultSet = null;
    protected PreparedStatement preparedStatement = null;
    protected void connect(){
        connection = DatabaseManager.getConnection();
    }
    protected void selectAll(String tableName){
        try {
            statement = connection.createStatement();
            String select = "select * from "+tableName;
            resultSet = statement.executeQuery(select);
            statement.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
