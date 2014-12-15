package multicasting;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;

/**
 *
 * @author lycog
 */
public class MulticastReceiver {
  public static void main(String[] args) {
    MulticastSocket socket = null;
    DatagramPacket inPacket = null;
    byte[] inBuf = new byte[256];
    try {
      //Prepare to join multicast group
      socket = new MulticastSocket(8888);
      
      //
      InetAddress address = InetAddress.getByName("224.2.2.3");
      //socket.joinGroup(address);
      
      //綁定
      //String adapterName = "eth0";
      String adapterName = java.net.InetAddress.getLocalHost().getHostAddress();
      System.out.println(adapterName);
      NetworkInterface nic = NetworkInterface.getByName(adapterName);
      System.out.println(nic);
      socket.joinGroup(new InetSocketAddress("224.2.2.3", 8888), nic);
      
 
      while (true) {
        inPacket = new DatagramPacket(inBuf, inBuf.length);
        socket.receive(inPacket);
        String msg = new String(inBuf, 0, inPacket.getLength());
        System.out.println("From " + inPacket.getAddress() + " Msg : " + msg);
      }
    } catch (IOException ioe) {
      System.out.println(ioe);
    }
  }
}
