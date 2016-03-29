package pl.edu.spring.application;

/**
 * Created by Paweł on 2016-03-20.
 */
public class ConnectionParams {
    String ipAddress;
    String hostPort;
    String clientPort;

    public ConnectionParams() {
    }

    public ConnectionParams(String ipAddress, String hostPort, String clientPort) {
        this.ipAddress = ipAddress;
        this.hostPort = hostPort;
        this.clientPort = clientPort;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getHostPort() {
        return hostPort;
    }

    public void setHostPort(String hostPort) {
        this.hostPort = hostPort;
    }

    public String getClientPort() {
        return clientPort;
    }

    public void setClientPort(String clientPort) {
        this.clientPort = clientPort;
    }
}
