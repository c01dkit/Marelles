import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class MarellesServer {
    private static final int MAX_SIZE = 20;
    private static final Handler[] handlers = new Handler[MAX_SIZE];
    private static final String[] clientName = new String[MAX_SIZE];
    private static final boolean[] aliveState = new boolean[MAX_SIZE];
    private static int handlerNum = 0;
    public static void main(String[] args) throws IOException {
        Socket client;
        ServerSocket server = new ServerSocket(2943);
        System.out.println("Server start at 2943");
        while (true){
            client = server.accept();
            handlers[handlerNum] = new Handler(client, handlerNum);
            handlers[handlerNum].start();
            aliveState[handlerNum] = true;
            handlerNum = (handlerNum+1)%MAX_SIZE;
        }
    }

    public static int setUpConnections(String info, int from) throws IOException {
        if (info.startsWith("[host]")) clientName[from] = info.substring(6);
        else if (info.startsWith("[guest]")) clientName[from] = info.substring(7);
        else if (info.startsWith("[find-host]")) {
            String target = info.substring(11);
            for (int i = 0; i < MAX_SIZE; i++){
                if (handlers[i] == null) continue;
                if (clientName[i] == null) continue;
                if (clientName[i].equals(target) && aliveState[i]){
                    System.out.println(clientName[from]+"已与"+clientName[i]+"建立连接");
                    handlers[i].setOppoNumber(from);
                    sendMessageTo(i,"[oppo-name]"+clientName[from]);
                    return i;
                }
            }
        }
        return -1;
    }

    public static void sendMessageTo(int target, String info) throws IOException {
        handlers[target].send(info);
    }

    public static void setAliveState(int num,boolean aliveState) {
        MarellesServer.aliveState[num] = aliveState;
    }
}

class Handler extends Thread {
    Socket sock;
    private final int selfNumber;
    private int oppoNumber;
    private BufferedReader selfReader;
    private BufferedWriter selfWriter;
    public Handler(Socket sock, int selfNumber) {
        this.sock = sock;
        this.selfNumber = selfNumber;
    }

    public void setOppoNumber(int oppoNumber) {
        this.oppoNumber = oppoNumber;
    }

    public void send(String info) throws IOException {
        selfWriter.write(info+"\n");
        selfWriter.flush();
    }

    @Override
    public void run() {
        try (InputStream input = this.sock.getInputStream()) {
            try (OutputStream output = this.sock.getOutputStream()) {
                handle(input, output);
            }
        } catch (Exception e) {
            try {
                this.sock.close();
            } catch (IOException e2) {
                e2.printStackTrace();
            }
//            e.printStackTrace();
            MarellesServer.setAliveState(selfNumber,false);
            System.out.println(selfNumber +" client disconnected.");
        }
    }

    private void handle(InputStream input, OutputStream output) throws IOException {
        selfWriter = new BufferedWriter(new OutputStreamWriter(output, StandardCharsets.UTF_8));
        selfReader = new BufferedReader(new InputStreamReader(input, StandardCharsets.UTF_8));
        selfWriter.write("Successfully connected to server. Current ID is "+ selfNumber +"\n");
        selfWriter.flush();
        while (true){
            String s = selfReader.readLine();
            System.out.println(s);
            if (s.startsWith("[")) {
                oppoNumber = MarellesServer.setUpConnections(s, selfNumber);
                MarellesServer.sendMessageTo(selfNumber,s);
            } else {
                MarellesServer.sendMessageTo(oppoNumber,s);
            }
        }
    }

}