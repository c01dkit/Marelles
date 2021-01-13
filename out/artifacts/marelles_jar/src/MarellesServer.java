import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class MarellesServer {
    private static final Handler[] handlers = new Handler[20];
    private static int handlerNum = 0;
    private static final ExecutorService cachePool = Executors.newCachedThreadPool();
    public static void main(String[] args) throws IOException {
        Socket client;
        ServerSocket server = new ServerSocket(2943);
        while (true){
            client = server.accept();
            handlers[handlerNum] = new Handler(client);
            handlers[handlerNum].start();
            handlerNum =  (handlerNum+1)%20;
        }
    }


}

class Handler extends Thread {
    Socket sock;
    public Handler(Socket sock) {
        this.sock = sock;
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
            System.out.println("client disconnected.");
        }
    }

    private void handle(InputStream input, OutputStream output) throws IOException {
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(output, StandardCharsets.UTF_8));
        BufferedReader reader = new BufferedReader(new InputStreamReader(input, StandardCharsets.UTF_8));
        writer.write("Connected.");
        writer.flush();
        while (true){
            String s = reader.readLine();
            if (s.equals("bye")) {
                writer.write("bye\n");
                writer.flush();
                break;
            }
            writer.write("ok: " + s + "\n");
            writer.flush();
        }
    }

}