package cs317.a1;


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
    public Connection(String server, String port) throws IOException {
        this.server = server;
        this.port = port;
        this.connected = false;
        this.socket = null;

        // Create socket of "dict.org 2628"
        socket = new Socket("dict.org", 2628);
        socket.setKeepAlive(true);

        //
        outputStream = socket.getOutputStream();
        printWriter = new PrintWriter(outputStream, true);

        //
        inputStream = socket.getInputStream();
        bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    // Connect to dict server
    public void connect() throws IOException {

        // Create outputstream


        // Get server and port variable from main() then write to dict.org
        printWriter.println("define " + server + " " + port);

        // Check if connected
        if(socket.isConnected()) connected = !connected;

        // Read back from dict.server

        String res = bufferedReader.readLine();
        System.out.println(res);

    }

    //Send commands after connection established
    public void command(String arg1, String arg2) throws IOException {
        // Create outputstream
        printWriter.println(arg1 + " " + arg2);
        String res;
        while( (res = bufferedReader.readLine()) != null){
            System.out.println(res);
            if(res.contains("250 ok")) break;
        }
    }
}
