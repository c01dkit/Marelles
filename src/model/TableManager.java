package model;

import java.sql.*;

public class TableManager {
    Connection connection;
    Statement statement;
    PreparedStatement preparedStatement;
    ResultSet resultSet;
    boolean connectState = false;
    protected void connect(){
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:marelles.db");
            statement = connection.createStatement();
            connectState = true;
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }
    protected void selecetAll(String tableName) throws SQLException {
        String queryAll = "select * from " + tableName;
        resultSet = statement.executeQuery(queryAll);
    }

}
