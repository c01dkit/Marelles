package model;

import java.sql.*;

public class DatabaseManager {
    private static final String JDBC_PORT = "3306";
    private static final String JDBC_DATABASE = "marelles";
    private static final String JDBC_USER = "root";
    private static final String JDBC_PASSWORD = "";
    private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    private static Connection connection = null;
    Statement statement;
    ResultSet resultSet;
    private final String TAG = "DatabaseManger: ";
    public void init(){
        String JDBC_URL = "jdbc:mysql://localhost:" + JDBC_PORT + "/" + JDBC_DATABASE + "?useSSL=false&characterEncoding=utf8";
        try {
            Class.forName(JDBC_DRIVER);
            connection = DriverManager.getConnection(JDBC_URL,JDBC_USER,JDBC_PASSWORD);
            System.out.println(TAG+"Connected Successfully.");
            statement = connection.createStatement();
            initGameStateTable();
            initStepStateTable();
            initChatTable();
            initPlayerInfoTable();
            statement.close();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() {
        String JDBC_URL = "jdbc:mysql://localhost:" + JDBC_PORT + "/" + JDBC_DATABASE + "?useSSL=false&characterEncoding=utf8";
        if (DatabaseManager.connection == null){
            try {
                Class.forName(JDBC_DRIVER);
                DatabaseManager.connection =
                        DriverManager.getConnection(JDBC_URL,JDBC_USER,JDBC_PASSWORD);
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return DatabaseManager.connection;
    }

    private void initGameStateTable() throws SQLException {
        if (checkTable("game_state")){
            System.out.println(TAG+"game_state table already exists");
        } else {
            String createTableSql = "create table game_state(" +
                    "id int not null," +
                    "bname char(64) not null," +
                    "wname char(64) not null," +
                    "turns int not null," +
                    "state char(16) not null)";
            statement.executeUpdate(createTableSql);
            System.out.println(TAG+"create table : game_state");
        }
    }

    private void initStepStateTable() throws SQLException {
        if (checkTable("step_state")){
            System.out.println(TAG+"step_state table already exists");
        } else {
            String createTableSql = "create table step_state(" +
                    "game_id int not null," +
                    "turn int not null," +
                    "player char(1) not null," +
                    "pos_before int not null," +
                    "pos_after int not null," +
                    "pos_affect int not null)";
            statement.executeUpdate(createTableSql);
            System.out.println(TAG+"create table : step_state");
        }
    }

    private void initChatTable() throws SQLException {
        if (checkTable("chat")){
            System.out.println(TAG+"chat table already exists");
        } else {
            String createTableSql = "create table chat(" +
                    "id int not null," +
                    "game_id int not null," +
                    "turn int not null," +
                    "player char(4) not null," +
                    "content text not null)";
            statement.executeUpdate(createTableSql);
            System.out.println(TAG+"create table : chat");
        }
    }

    private void initPlayerInfoTable() throws SQLException {
        if (checkTable("player_info")){
            System.out.println(TAG+"player_info table already exists");
        } else {
            String createTableSql = "create table player_info(" +
                    "id int not null," +
                    "name char(64) not null," +
                    "ini_win int not null default 0," +
                    "ini_tie int not null default 0," +
                    "ini_lose int not null default 0," +
                    "gote_win int not null default 0," +
                    "gote_tie int not null default 0," +
                    "gote_lose int not null default 0," +
                    "undo_times int not null default 0," +
                    "run_away int not null default 0)";
            statement.executeUpdate(createTableSql);
            System.out.println(TAG+"create table : player_info");
        }
    }

    private boolean checkTable(String tableName) throws SQLException {
        String checkTableExistsSql = "select TABLE_NAME from information_schema.TABLES WHERE TABLE_SCHEMA ="+"'"+JDBC_DATABASE+"'";
        resultSet = statement.executeQuery(checkTableExistsSql);
        while (resultSet.next()){
            String tableFound = resultSet.getString(1);
            if (tableFound.equals(tableName)) return true;
        }
        return false;
    }
}
