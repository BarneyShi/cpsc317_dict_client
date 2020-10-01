package cs317.a1;

import cs317.a1.Search;
import java.io.*;
import java.net.*;


public class Connection {
    // Define server and port
    private String server;
    private String port;
    public Boolean connected;
    public Socket socket;
    public OutputStream outputStream;
    public PrintWriter printWriter;
    public InputStream inputStream;
    public BufferedReader bufferedReader;

    // Constructor
    public Connection(String server, String port) throws IOException, SocketTimeoutException{
        this.server = server;
        this.port = port;
        this.connected = false;
        this.socket = null;

        // Create socket of "dict.org 2628"
        socket = new Socket();
        socket.setKeepAlive(true);

        //

    }

    // Connect to dict server
    public boolean connect() throws IOException {
        try{
            socket.connect(new InetSocketAddress(server, Integer.parseInt(port)), 5*1000);
        } catch (SocketTimeoutException e) {
            System.out.println("920 Control connection to " + server + " on port " + Integer.parseInt(port) + " failed to open");
            return false;
        }
        outputStream = socket.getOutputStream();
        printWriter = new PrintWriter(outputStream, true);
        //
        inputStream = socket.getInputStream();
        bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        // Get server and port variable from main() then write to dict.org
        printWriter.println("define " + server + " " + port);

        // Check if connected
        if(socket.isConnected()) connected = !connected;

        // Read back from dict.server

        String res;
        while((res = bufferedReader.readLine()) != null){
            if(res.contains("220")){
                res = "<-- " + res;
                break;
            }
        }
        System.out.println(res);
        return true;
    }

    /**
     *
     * @throws IOException
     */
    // Send commands after connection established
    public void showDB() throws IOException {
        // Create outputstream
        printWriter.println("show db");
        String res;
        while( (res = bufferedReader.readLine()) != null){
            if(res.contains("250 ok")) {
                res = "<-- " + res;
                System.out.println(res);
                break;
            }
            System.out.println(res);
        }
    }

    public void close() throws IOException {
        printWriter.println("quit");
        String res;

    }

    public void quit() throws IOException {
        connected = !connected;
        printWriter.println("quit");
        String res;
        while((res = bufferedReader.readLine()) != null){
            if(res.contains("221")) {
                System.out.println("> QUIT");
                res = "<-- " + res;
                System.out.println(res);
            }
        }
    }



}
