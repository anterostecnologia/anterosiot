package br.com.anteros.iot.support.tcp;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;


public class TcpClient {
    private Socket socket;
    private String host;
    private int port;
    private int timeout;
    private DataOutputStream os;
    private DataInputStream is;
    private Cache cache;
    private boolean receive;
    private boolean connected;


    public TcpClient() {
    }

    public TcpClient(String host, int port) {
        this.host = host;
        this.port = port;
        this.timeout = 3000;
    }

    public TcpClient(String host, int port, int timeout) {
        this.host = host;
        this.port = port;
        this.timeout = timeout;
    }

    public boolean connect() {
        return this.connect(this.host, this.port, this.timeout);
    }

    public boolean connect(String host, int port, int timeout) {
        try {
            this.host = host;
            this.port = port;
            this.socket = new Socket();
            InetSocketAddress insa = new InetSocketAddress(host, port);
            this.socket.connect(insa, timeout);
            this.cache = new Cache();
            this.os = new DataOutputStream(this.socket.getOutputStream());
            this.is = new DataInputStream(this.socket.getInputStream());
            this.receive = true;
            this.startRead();
            this.connected = true;
            return true;
        } catch (SocketTimeoutException var5) {
            this.connected = false;
        } catch (IOException var6) {
            this.connected = false;
        }

        return false;
    }

    public boolean disconnect() {
        try {
            this.receive = false;
            this.socket.close();
            this.connected = false;
            return true;
        } catch (Exception var2) {
            return false;
        }
    }

    public boolean sendData(char[] data) {
        try {
            if (this.os != null) {
                byte[] temp = new byte[data.length];
                int i = 0;
                char[] arr$ = data;
                int len$ = data.length;

                for(int i$ = 0; i$ < len$; ++i$) {
                    char chr = arr$[i$];
                    temp[i] = (byte)(chr & 255);
                    ++i;
                }

                this.os.write(temp);
                this.os.flush();
                return true;
            }
        } catch (Exception var8) {
        }

        return false;
    }

    private void startRead() {
        (new Thread(new Runnable() {
            public void run() {
                while(true) {
                    try {
                        if (TcpClient.this.receive) {
                            if (TcpClient.this.is != null) {
                                int i = TcpClient.this.is.available();
                                if (i > 0) {
                                    int j = 0;

                                    char[] temp;
                                    for(temp = new char[i]; j < i; ++j) {
                                        temp[j] = (char)(TcpClient.this.is.readByte() & 255);
                                    }

                                    TcpClient.this.cache.writeCache(temp);
                                }
                            }

                            Thread.sleep(5L);
                            continue;
                        }
                    } catch (Exception var4) {
                        var4.printStackTrace();
                    }

                    return;
                }
            }
        }, "startRead")).start();
    }

    public int availableData() {
        try {
            return this.cache != null ? this.cache.availableData() : 0;
        } catch (Exception var2) {
            return 0;
        }
    }

    public char[] receiveData(int size) {
        try {
            return this.cache != null ? this.cache.readCache(size) : null;
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
