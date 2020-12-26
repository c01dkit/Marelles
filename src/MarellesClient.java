import database.DatabaseManager;
import view.MainWindow;

public class MarellesClient {
    public static void main(String[] args) {
        MainWindow mainWindow = new MainWindow();
        DatabaseManager databaseManager = new DatabaseManager();
        mainWindow.init();
        databaseManager.init();
        mainWindow.bind(databaseManager);
    }
}
