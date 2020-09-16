package cs317.a1;

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
    public void connect(){

    }
}
