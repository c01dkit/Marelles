package database;

public class StepStateTableManager extends TableManager{
    private final String TAG = "StepStateTableManager: ";
    private final String TABLE_NAME = "step_state";
    public StepStateTableManager(){
        connect();
        if (!connectState) System.out.println(TAG+"connect failed");
    }

}
