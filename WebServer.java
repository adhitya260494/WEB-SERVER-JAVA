
public class WebServer {
    public static void main(String[] args) {
        Server srv = new Server();
        new Thread(srv).start();
    }
}
