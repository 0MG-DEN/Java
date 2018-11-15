package Client;

import Server.Server;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;

public class Client {

    private static final int port = Server.PORT;
    private static final String serverAddress = "localhost";

    private static Socket clientSocket;
    private static BufferedReader clientConsole;
    private static BufferedReader in;
    private static BufferedWriter out;


    public static void main(String[] args) {
        new Client();
    }

    public Client() {
        try {
            clientSocket = new Socket(serverAddress, port);
            clientConsole = new BufferedReader(new InputStreamReader(System.in));
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));

            new ReadMsg().start();
            new WriteMsg().start();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    private class ReadMsg extends Thread {

        @Override
        public void run() {
            String msg;
            while (true) {
                try {
                    if (clientSocket.isClosed()) break;
                    msg = in.readLine();

                    if (msg.equals(Server.quitMsg)) {
                        in.close();
                        if (!clientSocket.isClosed())
                            clientSocket.close();
                        System.out.println(Server.ANSI_RED + "You've been disconnected form server" + Server.ANSI_RESET);
                        break;
                    }

                    System.out.println(msg);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("Session is closed");
        }
    }

    private class WriteMsg extends Thread {

        @Override
        public void run() {
            String msg;
            while (true) {
                try {
                    if (clientSocket.isClosed()) break;
                    msg = clientConsole.readLine();

                    if (msg.equals(Server.quitMsg)) {
                        System.out.println("Disconnecting form server");
                        out.write(msg + '\n');
                        out.flush();
                        out.close();
                        clientConsole.close();
                        break;
                    }

                    if (!msg.equals(""))
                        out.write(msg + '\n');
                    out.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}