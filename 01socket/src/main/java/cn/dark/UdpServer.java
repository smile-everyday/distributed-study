package cn.dark;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

/**
 * @author dark
 * @date 2019-07-04
 */
public class UdpServer {

    public static void main(String[] args) throws IOException {
        DatagramSocket socket = new DatagramSocket(8080);
        byte[] pack = new byte[1024];
        DatagramPacket packet = new DatagramPacket(pack, pack.length);
        socket.receive(packet);
        System.out.println(new String(pack, 0, packet.getLength()));
    }

}
