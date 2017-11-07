package server;

import game.Game;
import ai.*;

import java.io.*;
import java.net.*;
import java.util.*;
import org.json.simple.*;

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
    public static final int GAME_CAPACITY = 50;
    public static final String root_dir = "web";
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
    final String OK = "HTTP/1.1 200 OK";
    final String BAD_REQUEST = "HTTP/1.1 400 Bad Request";
    final String NOT_FOUND = "HTTP/1.1 404 Not Found";
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
        DataOutputStream out = new DataOutputStream(socket.getOutputStream());

        String request = br.readLine();
        if (request == null) {
            // System.out.println("Null request received.");
            return;
        }
        System.out.format("%n%s%n", request);

        StringTokenizer tokens = new StringTokenizer(request);
        String type = tokens.nextToken();
        String file = tokens.nextToken();

        if (type.equals("GET")) {
            if (file.charAt(file.length() - 1) == '/') file += "index.html";
            file = Server.root_dir + file;
            System.out.println(file);
            getContent(out, file);
        } else if (type.equals("POST")) {
            // Read request body
            int length = 0;
            String content_type = null;
            String line;
            while ((line = br.readLine()) != null && !line.equals("")) {
                // System.out.println(line);
                if (line.toLowerCase().startsWith("content-length: ")) {
                    length = Integer.parseInt(line.substring(line.indexOf(':') + 1).trim());
                }
                if (line.toLowerCase().startsWith("content-type: ")) {
                    content_type = line.substring(line.indexOf(':') + 1).trim();
                }
            }
            StringBuilder body = new StringBuilder();
            if (length > 0) {
                int c;
                while (body.length() < length && (c = br.read()) != -1) {
                    body.append((char)c);
                }
            }
            // System.out.println(body.toString());
            file = file.substring(1);
            // System.out.println(file);
            gameProcess(out, file, content_type, body.toString());
        }
        br.close();
    }

    private void gameProcess(DataOutputStream out, String type, String content_type, String body) throws Exception {
        String statusLine = null;
        String contentTypeLine = null;
        JSONObject response = new JSONObject();
        //fetch body into map
        Map<String, String> param_map = new HashMap<>();
        fetchBody(param_map, content_type, body);
        if (type.equals("start")) {
            if (!param_map.containsKey("first")) {
                // parameter missing
                statusLine = BAD_REQUEST + CRLF;
                contentTypeLine = "Content-type: text/plain" + CRLF;
                response.put("message", "\"first\" parameter is required.");
            }
            else {
                Game g = null;
                Ai ai = null;
                int game_id = 0;
                for (game_id = 0; game_id < Server.GAME_CAPACITY; game_id++) {
                    if (!Server.map.containsKey(game_id)) {
                        synchronized (Server.lock) {
                            g = new Game();
                            ai = new Medium(g);
                            Server.map.put(game_id, new Tuple(g, ai));
                        }
                        break;
                    }
                }
                // Server busy situation should be separated later.
                statusLine = OK + CRLF;
                contentTypeLine = "Content-type: application/json" + CRLF;
                if (g == null) {
                    // Game pool is full.
                    response.put("message", "Server is busy now.");
                }
                else {
                    response.put("game_id", game_id);
                    if (param_map.get("first").equals("ai")) {
                        ai.setKey();
                        int[] last_move = g.getLastMove();
                        response.put("row", last_move[0]);
                        response.put("col", last_move[1]);
                    }
                }
            }
        }
        else if (type.equals("move")) {
            // Parameters check
            StringBuilder missing = new StringBuilder();
            if (!param_map.containsKey("game_id")) {
                missing.append(", game_id");
            }
            if (!param_map.containsKey("row")) {
                missing.append(", row");
            }
            if (!param_map.containsKey("col")) {
                missing.append(", col");
            }
            if (missing.length() > 0) {
                statusLine = BAD_REQUEST + CRLF;
                contentTypeLine = "Content-type: text/plain" + CRLF;
                missing.append(" required.");
                response.put("message", missing.substring(2));
            }
            else {
                statusLine = OK + CRLF;
                contentTypeLine = "Content-type: application/json" + CRLF;
                int game_id = Integer.parseInt(param_map.get("game_id"));
                int row = Integer.parseInt(param_map.get("row"));
                int col = Integer.parseInt(param_map.get("col"));
                Tuple t = Server.map.get(game_id);
                t.g.setKey(row, col);
                response.put("game_id", game_id);
                if (t.g.complete()) {
                    response.put("status", "win");
                    synchronized (Server.lock) {
                        logGame(t);
                        Server.map.remove(game_id);
                    }
                }
                else {
                    t.ai.setKey();
                    if (t.g.complete()) {
                        response.put("status", "lose");
                        synchronized (Server.lock) {
                            logGame(t);
                            Server.map.remove(game_id);
                        }
                    }
                    else {
                        response.put("status", "move");
                    }
                    int[] last_move = t.g.getLastMove();
                    response.put("row", last_move[0]);
                    response.put("col", last_move[1]);
                }
            }
        }

        System.out.println(response.toString());
        out.writeBytes(statusLine);
        out.writeBytes(contentTypeLine);
        out.writeBytes(CRLF);
        out.writeBytes(response.toString());
        out.close();
        socket.close();
    }

    private void fetchBody(Map<String, String> map, String content_type, String body) {
        // System.out.println(content_type);
        System.out.println(body);
        String note = null;
        int index;
        if ((index = content_type.indexOf(';')) != -1) {
            note = content_type.substring(index + 1);
            content_type = content_type.substring(0, index);
        }
        switch (content_type) {
            case "application/x-www-form-urlencoded" :
                String[] pairs = body.split("&");
                for (String pair : pairs) {
                    String[] strs = pair.split("=");
                    map.put(strs[0], strs[1]);
                }
                break;
            case "application/json" :
                break;
            case "multipart/form-data" :
                break;
            default :
                //Unrecognized content type.
        }
    }

    private void getContent(DataOutputStream out, String file) throws Exception {
        FileInputStream fis = null;
        String statusLine;
        String contentTypeLine;

        try {
            fis = new FileInputStream(file);
            statusLine = OK + CRLF;
            // Todo: Add file size. (File.length())
            contentTypeLine = "Content-type: " + contentType(file) + CRLF;
        } catch (FileNotFoundException e) {
            statusLine = NOT_FOUND + CRLF;
            contentTypeLine = "Content-type: text/plain" + CRLF;
        }
        out.writeBytes(statusLine);
        out.writeBytes(contentTypeLine);
        out.writeBytes(CRLF);

        if (fis != null) {
            sendBytes(fis, out);
            fis.close();
        }

        out.close();
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