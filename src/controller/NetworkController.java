package controller;

import view.GameProcess;
import view.PlayBoard;
import view.StatusPanel;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class NetworkController {
    public static String localName;
    public static String oppoName;
    private static Handler handler;
    private static PlayBoard playBoard;

    public NetworkController(PlayBoard playBoard){
        NetworkController.playBoard = playBoard; // 预先存储，延迟加载
    }
    public static void connectToServer(String ip, int port, String localName){
        NetworkController.localName = localName;
        try{
            Socket sock = new Socket(ip,port);
            NetworkController.handler = new Handler(sock.getInputStream(), sock.getOutputStream(),"[host]");
            NetworkController.handler.start();
            handler.setPlayBoard(playBoard);
        } catch (IOException e){
            e.printStackTrace();
        }
    }
    public static void connectToServer(String ip, int port, String localName, String oppoName){
        NetworkController.localName = localName;
        NetworkController.oppoName = oppoName;
        try{
            Socket sock = new Socket(ip,port);
            NetworkController.handler = new Handler(sock.getInputStream(), sock.getOutputStream(),"[guest]");
            NetworkController.handler.start();
            handler.setPlayBoard(playBoard);
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public static void sendMessage(String info, Boolean localWait){
        if (localWait){
            NetworkController.handler.send(info,"nowait|");
        } else {
            NetworkController.handler.send(info,"wait|");
        }
    }

    public static void sendMessage(String info){
        NetworkController.handler.send(info,"control|");
    }
}

class Handler extends Thread {
    private final BufferedWriter writer;
    private final BufferedReader reader;
    private PlayBoard playBoard;
    public Handler(InputStream inputStream, OutputStream outputStream, String prefix){
        reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        writer = new BufferedWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8));
        if (prefix.equals("[host]")){
            send(NetworkController.localName,prefix);
        } else if (prefix.equals("[guest]")){
            send(NetworkController.localName,prefix);
            send(NetworkController.oppoName,"[find-host]");
        }
    }

    public void setPlayBoard(PlayBoard playBoard) {
        this.playBoard = playBoard;
    }

    public void send(String info, String prefix){
        String temp = prefix+info+"\n";
        try{
            writer.write(temp);
            writer.flush();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while(true){
            try {
                String s = reader.readLine();
                if (s != null) handleString(s);
            } catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    private void handleString(String info){
        if (this.playBoard == null) {
            System.out.println("playBoard is NULL!");
            return;
        }
        if (info.startsWith("[oppo-name]")) {
            NetworkController.oppoName = info.substring(11);
            StatusPanel.updateOppoName(NetworkController.oppoName);
            GameProcess.sendGameInfo(NetworkController.oppoName+"加入了房间。");
            GameProcess.sendGameInfo("请放置棋子以开始游戏。");
        }
        else if (info.startsWith("control|")){
            if (info.substring(8).equals("runAway"))this.playBoard.oppoRunaway();
            if (info.substring(8).equals("undo"))this.playBoard.oppoUndo();
        }
        else if (info.startsWith("nowait")||info.startsWith("wait")){
            this.playBoard.parseOppoStep(info);
        } else System.out.println(info);
    }
}