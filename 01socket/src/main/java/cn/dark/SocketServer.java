package cn.dark;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author dark
 * @date 2019-07-04
 */
public class SocketServer {

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(8080);
        Socket socket = serverSocket.accept();

        BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter pw = new PrintWriter(socket.getOutputStream());
        BufferedReader sbr = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Client：" + br.readLine());

        String line = sbr.readLine();
        while (!line.equals("bye")) {
            pw.println(line);
            pw.flush();

            System.out.println("Server：" + line);
            System.out.println("Client：" + br.readLine());
            line = sbr.readLine();
        }

        // sbr.close();
        // pw.close();
        // br.close();
        // socket.close();
        // serverSocket.close();
    }

}
