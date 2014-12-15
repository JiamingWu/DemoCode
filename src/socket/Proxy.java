package socket;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Proxy {

    public static void main(String[] args) throws Exception {
        ServerSocket ss = new ServerSocket(1433);
        while (true) {
            Socket s = ss.accept();
            InputStream in = s.getInputStream();
            OutputStream out = s.getOutputStream();
            
            Socket server = new Socket("10.60.90.47", 1433);
            OutputStream serverOut = server.getOutputStream();
            InputStream serverIn = server.getInputStream();
            
            new Process(in, serverOut).start();
            new Process(serverIn, out).start();
        }
    }
}

class Process extends Thread {
    private InputStream in;
    private OutputStream out;
    public Process(InputStream in, OutputStream out) {
        this.in = in;
        this.out = out;
    }
    @Override
    public void run() {
        try {
            byte[] buf = new byte[20480];
            int len = 0;
            while ((len = in.read(buf)) != -1) {
                out.write(buf, 0, len);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


