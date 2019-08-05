package cn.dark;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * @author dark
 * @date 2019-07-16
 */
public class ClientDemo {

    public static void main(String[] args) throws IOException {
        Socket socke = new Socket("localhost", 8080);
        PrintWriter pw = new PrintWriter(socke.getOutputStream(), true);
        pw.println("hello");
    }

}
