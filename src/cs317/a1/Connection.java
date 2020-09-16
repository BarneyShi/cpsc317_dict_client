package cs317.a1;

import com.sun.org.apache.xpath.internal.operations.Bool;

import java.io.*;
import java.net.*;

public class Connection {
    // Define server and port
    private String server;
    private String port;
    public Boolean connected;

    // Constructor
    public Connection(String server, String port) {
        this.server = server;
        this.port = port;
        this.connected = false;
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
        // Create socket of "dict.org 2628"
        Socket socket = new Socket("dict.org", 2628);
        socket.setKeepAlive(true);
        // Create outputstream
        OutputStream outputStream = socket.getOutputStream();
        PrintWriter printWriter = new PrintWriter(outputStream, true);
        // Get server and port variable from main() then write to dict.org
        printWriter.println("define " + server + " " + port);

        // Check if connected
        if(socket.isConnected()) connected = !connected;

        // Read back from dict.server
        InputStream inputStream = socket.getInputStream();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String res = bufferedReader.readLine();
        System.out.println(res);

    }
}
