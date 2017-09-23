package Server;

import AI.*;
import Game.Game;
import java.io.*;
import java.net.*;
import java.util.*;

class Tuple {
    Game g;
    Ai ai;
    Tuple(Game g, Ai ai) {
        this.g = g;
        this.ai = ai;
    }
}

public class Server {
    public static Object lock = new Object();
    public static Map<Integer, Tuple> map = new HashMap<>();
    private static final int port = 9000;
    public static void main(String[] args) throws Exception {
        ServerSocket serverSocket = new ServerSocket(port);
        while (true) {
            Socket socket = serverSocket.accept();
            Request request = new Request(socket);
            Thread t = new Thread(request);
            t.start();
        }
    }
}

class Request implements Runnable {
    final String CRLF = "\r\n";
    private Socket socket;
    
    public Request(Socket socket) throws Exception {
        this.socket = socket;
    }
    
    public void run() {
        try {
            processRequest();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void processRequest() throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        DataOutputStream outstream = new DataOutputStream(socket.getOutputStream());
        
        String request = br.readLine();
        System.out.format("%n%s%n", request);
        
        StringTokenizer tokens = new StringTokenizer(request);
        String type = tokens.nextToken();
        String file = tokens.nextToken();
        //System.out.println(type);
        //System.out.println(file);
        //Thread.sleep(3000);
        
        if (type.equals("GET")) {
            System.out.println("get content");
            if (file.length() == 1) file += "index.html";
            file = "." + file;
            System.out.println(file);
            getContent(outstream, file);
        }
        else if (type.equals("POST")) {
            System.out.println("game related");
            file = file.substring(1);
            System.out.println(file);
            gameMove(outstream, file);
        }
        br.close();
    }
    
    private void gameMove(DataOutputStream outstream, String file) throws Exception {
        String response = "";
        if (file.startsWith("move")) {
            file = file.substring(4);
            String[] moves = file.split(",");
            int gameId = Integer.parseInt(moves[0]);
            int r = Integer.parseInt(moves[1]);
            int c = Integer.parseInt(moves[2]);
            Tuple t = Server.map.get(gameId);
            t.g.setKey(r, c);
            if (t.g.complete()) {
                response = "win";
                synchronized(Server.lock) {
                    logGame(t);
                    Server.map.remove(gameId);
                }
            }
            else {
                t.ai.setKey();
                if (t.g.complete()) {
                    response = "lose";
                    synchronized(Server.lock) {
                        logGame(t);
                        Server.map.remove(gameId);
                    }
                }
                else {
                    response = "move";
                }
                int[] last = t.g.getLastMove();
                response += gameId + "," + last[0] + "," + last[1];
            }
        }
        else if (file.startsWith("start")) {
            file = file.substring(5);
            int gameId = 0;
            Game g = new Game();
            Ai ai = new Hard(g);
            for (gameId = 0; gameId < 50; gameId++) {
                if (!Server.map.containsKey(gameId)) {
                    synchronized(Server.lock) {
                        Server.map.put(gameId, new Tuple(g, ai));
                    }
                    break;
                }
            }
            if (file.equals("player")) {
                response = "Success!" + gameId;
            }
            else {
                ai.setKey();
                int[] last = g.getLastMove();
                response = "move" + gameId + "," + last[0] + "," + last[1];
            }
        }
        System.out.println(response);
        outstream.writeBytes(response);
        outstream.close();
        socket.close();
    }
    
    private void getContent(DataOutputStream outstream, String file) throws Exception {
        FileInputStream fis = null;
        String statusLine = null;
        String contentTypeLine = null;
        String entityBody = null;
        try {
            fis = new FileInputStream(file);
            statusLine = "HTTP/1.1 200 OK" + CRLF;
            contentTypeLine = "Content-type: " + contentType(file) + CRLF;
        } catch (FileNotFoundException e) {
            statusLine = "HTTP/1.1 404 Not Found" + CRLF;
            contentTypeLine = "Content-type: text/plain" + CRLF;
        }
        outstream.writeBytes(statusLine);
        outstream.writeBytes(contentTypeLine);
        outstream.writeBytes(CRLF);
        
        if (fis != null) {
            sendBytes(fis, outstream);
            fis.close();
        }
        
        outstream.close();
        socket.close();
    }
    
    private String contentType(String fileName) {
        if (fileName.endsWith(".htm") || fileName.endsWith(".html")) return "text/html";
        if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")) return "text/jpeg";
        if (fileName.endsWith(".png")) return "image/png";
        if (fileName.endsWith(".css")) return "text/css";
        if (fileName.endsWith(".js")) return "text/javascript";
        return "application/octet-stream";
    }
    
    private void sendBytes(FileInputStream fis, OutputStream os) throws Exception {
        byte[] buffer = new byte[1024];
        int bytes = 0;
        while ((bytes = fis.read(buffer)) != -1) {
            os.write(buffer, 0, bytes);
        }
    }
    
    private void logGame(Tuple t) {
        
    }
}