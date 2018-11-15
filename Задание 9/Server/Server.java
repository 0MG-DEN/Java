package Server;

import package1.TicTacToe;

import java.io.*;
import java.net.*;
import java.util.*;

public class Server {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";

    private static TicTacToe game;

    public static final int PORT = 1488;
    public static final String quitMsg = "/quit";
    private static List<ServerClient> clientsList = new ArrayList<>();

    public static void main(String[] args) {
        try {
            ServerSocket server = new ServerSocket(PORT);
            System.out.println("Server started");
            try {
                game = new TicTacToe(Integer.parseInt(args[0]));
            } catch (Exception e) {
                System.out.println("Unable to create a game (invalid parameter)");
                e.printStackTrace();
                server.close();
                System.exit(-1);
            }

            while (Server.clientsList.size() < 2) {
                Socket socket = server.accept();
                System.out.println("Player" + ServerClient.nextID + " connects");
                try {
                    clientsList.add(new ServerClient(socket));
                } catch (IOException e) {
                    e.printStackTrace();
                    socket.close();
                }
            }
            System.out.println("Reached maximum number of clients\nClosing server's socket");
            for (ServerClient sc : Server.clientsList)
                sc.sendToThis(game.currentState());
            server.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class ServerClient extends Thread {
        private static int nextID = 1;

        private final int ID;
        private Socket socket;
        private BufferedReader in;
        private BufferedWriter out;

        private ServerClient(Socket inSocket) throws IOException {
            ID = nextID++;
            socket = inSocket;
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            System.out.println("Player" + ID + "\'s input/output streams initialized.");
            start();
        }

        @Override
        public void run() {
            sendToThis(ANSI_RED + "<Server>: You're Player" + ID + ". To exit this session enter \"" + Server.quitMsg + '\"' + ANSI_RESET);
            try {
                while (true) {
                    String msg = null;
                    try {
                        if (game.getCurrentPlayer() == this.ID && game.isOn())
                            sendToThis("<Server>: Your turn, enter position:");
                        msg = in.readLine();

                        if (msg.equals(Server.quitMsg)) {
                            sendToAll(ANSI_RED + "<Server>: Player" + ID + " quits" + ANSI_RESET);
                            sendToThis(Server.quitMsg);
                            this.quitSession();
                            System.out.println("Player" + ID + " disconnected");
                            break;
                        }

                        sendToAll("<Player" + ID + ">: " + msg);

                        if (game.getCurrentPlayer() == this.ID && game.isOn()) {
                            try {
                                if (game.takePosition(Integer.parseInt(msg))) {
                                    sendToAll(game.currentState());
                                    sendToThis(game.currentState());
                                }
                            } catch (TicTacToe.GameException e) {
                                sendToThis("<Server>: " + e.getMessage());
                            } catch (NumberFormatException ignored) {
                            }

                            try {
                                game.checkGrid();
                            } catch (TicTacToe.GameException e) {
                                sendToAll("<Server>: " + e.getMessage());
                                sendToThis("<Server>: " + e.getMessage());

                                sendToAll("<Server>: Game is over. Enter \"" + Server.quitMsg + "\" to exit");
                                sendToThis("<Server>: Game is over. Enter \"" + Server.quitMsg + "\" to exit");
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } catch (NullPointerException ignored) {

            }
        }

        private void quitSession() {
            try {
                socket.close();
                in.close();
                out.flush();
                out.close();
                Server.clientsList.remove(this);
                this.interrupt();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void sendToAll(String msg) {
            for (ServerClient sc : Server.clientsList) {
                if (sc != this)
                    try {
                        sc.out.write(msg + '\n');
                        sc.out.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
            }
        }

        private void sendToThis(String msg) {
            for (ServerClient sc : Server.clientsList) {
                if (sc == this)
                    try {
                        sc.out.write(msg + '\n');
                        sc.out.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
            }
        }
    }
}
