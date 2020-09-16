package cs317.a1;

import java.io.*;
import java.net.*;

public class Open {
    //define server and port
    private String server;
    private String port;

    //constructor
    public Open(String server, String port) {
        this.server = server;
        this.port = port;
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

    //connect to dict server
    public void connect() throws IOException {
        //create socket of "dict.org 2628"
        Socket socket = new Socket("dict.org", 2628);
        //create outputstream
        OutputStream outputStream = socket.getOutputStream();
        PrintWriter printWriter = new PrintWriter(outputStream, true);
        //get server and port variable from main() then write to dict.org
        printWriter.println("define " + server + " " + port);

        //read back from dict.server
        InputStream inputStream = socket.getInputStream();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String res = bufferedReader.readLine();
        System.out.println(res);
    }
}
