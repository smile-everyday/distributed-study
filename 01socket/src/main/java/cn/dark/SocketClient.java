package cn.dark;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * @author dark
 * @date 2019-07-04
 */
public class SocketClient {

    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("127.0.0.1", 8080);

        BufferedReader sbr = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter pw = new PrintWriter(socket.getOutputStream());
        BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        String line = sbr.readLine();
        while (!line.equals("bye")) {
            pw.println(line);
            pw.flush();

            System.out.println("Client：" + line);
            System.out.println("Server：" + br.readLine());
            line = sbr.readLine();
        }

        // sbr.close();
        // pw.close();
        // br.close();
        // socket.close();
    }

}
