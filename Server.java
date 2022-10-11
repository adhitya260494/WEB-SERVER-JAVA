
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server implements Runnable {
    final static int PORT = 9090;

    @Override
    public void run() {
        ServerSocket serverSocket = null;
        Socket clientSocket = null;
        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println("server start on port:" + PORT);
            while (true) {
                try {
                    clientSocket = serverSocket.accept();
                    http_protocol handle = new http_protocol(clientSocket);
                    handle.handleRequest();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                clientSocket.close();
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
