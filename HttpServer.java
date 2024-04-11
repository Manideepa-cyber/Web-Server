import java.io.*;
import java.net.*;

public class HttpServer {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(3974);
        System.out.println("Server started. Listening on port 3974");

        while (true) {
            Socket clientSocket = serverSocket.accept();
            Thread clientThread = new Thread(new ClientHandler(clientSocket));
            clientThread.start();
        }
    }
}

class ClientHandler implements Runnable {
    private final Socket clientSocket;

    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null && !line.isEmpty()) {
                System.out.println(line);
            }

            PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);
            writer.println("HTTP/1.1 200 OK");
            writer.println("Content-Type: text/html; charset=UTF-8");
            writer.println();
            writer.println("<!DOCTYPE html>");
            writer.println("<html lang=\"en\">");
            writer.println("<head><title>Server</title></head>");
            writer.println("<body>");
            writer.println("<h1>Web Page</h1>");
            writer.println("<p>Web page served!</p>");
            writer.println("</body></html>");

            reader.close();
            writer.close();
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
