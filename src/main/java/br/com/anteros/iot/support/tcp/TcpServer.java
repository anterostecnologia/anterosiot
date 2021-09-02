package br.com.anteros.iot.support.tcp;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class TcpServer {
    private Socket clientSocket;
    private ServerSocket serverSocket;
    private String host;
    private int port;
    private DataOutputStream os;
    private DataInputStream is;
    private Cache cache;
    private boolean receive;
    private boolean connected;

    public TcpServer() {
    }

    public TcpServer(int port) {
        this.port = port;
    }

    public boolean open() {
        return this.open(this.port);
    }

    public boolean open(int port) {
        try {
            this.serverSocket = new ServerSocket(port);
            this.cache = new Cache();
            this.receive = true;
            this.startRead();
            return true;
        } catch (Exception var3) {
            return false;
        }
    }

    public boolean disconnect() {
        try {
            this.receive = false;
            this.clientSocket.close();
            this.serverSocket.close();
            return true;
        } catch (Exception var2) {
            return false;
        }
    }

    public boolean sendData(char[] data) {
        try {
            if (this.os != null) {
                int len$ = data.length;
                int i$ = 0;
                if (i$ < len$) {
                    char chr = data[i$];
                    byte temp = (byte)(chr & 255);
                    this.os.write(temp);
                    return true;
                }

                this.os.flush();
            }
        } catch (Exception var7) {
        }

        return false;
    }

    private void startRead() {
        (new Thread(new Runnable() {
            public void run() {
                while(true) {
                    try {
                        if (TcpServer.this.receive) {
                            if (TcpServer.this.clientSocket == null) {
                                TcpServer.this.clientSocket = TcpServer.this.serverSocket.accept();
                                TcpServer.this.os = new DataOutputStream(TcpServer.this.clientSocket.getOutputStream());
                                TcpServer.this.is = new DataInputStream(TcpServer.this.clientSocket.getInputStream());
                            } else if (TcpServer.this.is != null) {
                                int i = TcpServer.this.is.available();
                                if (i > 0) {
                                    int j = 0;

                                    char[] temp;
                                    for(temp = new char[i]; j < i; ++j) {
                                        temp[j] = (char)(TcpServer.this.is.readByte() & 255);
                                    }

                                    TcpServer.this.cache.writeCache(temp);
                                }
                            }

                            Thread.sleep(5L);
                            continue;
                        }
                    } catch (Exception var4) {
                    }

                    return;
                }
            }
        }, "startRead")).start();
    }

    public int availableData() {
        try {
            return this.cache.availableData();
        } catch (Exception var2) {
            return 0;
        }
    }

    public char[] receiveData(int size) {
        try {
            return this.cache.readCache(size);
        } catch (Exception var3) {
            return null;
        }
    }

    public boolean isConnected() {
        return this.connected;
    }

    public String getHost() {
        return this.host;
    }

    public int getPort() {
        return this.port;
    }
}
