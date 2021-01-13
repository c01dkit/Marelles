package controller;

import view.ConstantDataSet;
import view.StatusPanel;

public class GameTimer extends Thread{
    private int tick;
    private boolean keepOn;
    private final int max;
    public GameTimer(){
        tick = ConstantDataSet.TICKER_MAX;
        max = tick;
        keepOn = false;
    }
    public GameTimer(int start){
        tick = start;
        max = tick;
        keepOn = false;
    }

    public void gameTimerReset(){
        tick = max;
        StatusPanel.updateTime(String.valueOf(tick));
        keepOn = false;
    }

    public void gameTimerRestart(){
        tick = max;
        if (!keepOn) {
            keepOn = true;
            this.start();
        }

        StatusPanel.updateTime(String.valueOf(tick));
    }

    public void gameTimerStart(){
        if (!keepOn){
            keepOn = true;
            this.start();
        }
    }

    public void gameTimerStop(){
        keepOn = false;
    }

    @Override
    public void run() {
        while (keepOn && tick >= 1) {
            tick -= 1;
            StatusPanel.updateTime(String.valueOf(tick));
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
