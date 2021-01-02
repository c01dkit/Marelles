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
}
