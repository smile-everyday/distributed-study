package cn.dark;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * @author dark
 * @date 2019-07-04
 */
public class UdpClient {

    public static void main(String[] args) throws IOException {
        InetAddress address = InetAddress.getByName("localhost");
        byte[] data = "helo!".getBytes();
        DatagramPacket packet = new DatagramPacket(data, data.length, address, 8080);
        DatagramSocket socket = new DatagramSocket();
        socket.send(packet);
    }

}
